package com.golookon.gametheory.managed;

import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.servlet.http.HttpSession;

import org.primefaces.event.FlowEvent;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.golookon.gametheory.dao.UserDao;
import com.golookon.gametheory.entity.User;

@Component("userManager")
@Scope("session")
public class UserManager {
	/**
	 * <p>
	 * The key for the session scoped attribute holding the appropriate
	 * <code>User</code> instance.
	 * </p>
	 */
	public static final String USER_SESSION_KEY = "user";

	@Inject
	UserDao userDao;
	private boolean userLoggedIn = false;
	private User potentialUser;
	private User currentUser;
	private boolean skip;
	private List<User> users;
	
	

	public UserManager() {
	}

	@PostConstruct
	public void init() {
		loadUsers();
		potentialUser = new User();
	}

	public void loadUsers() {
		users = userDao.getAllUsers();
	}

	public UserDao getUserDao() {
		return userDao;
	}

	public void setUserDao(UserDao userDao) {
		this.userDao = userDao;
	}

	public boolean isUserLoggedIn() {
		return userLoggedIn;
	}

	public void setUserLoggedIn(boolean userLoggedIn) {
		this.userLoggedIn = userLoggedIn;
	}

	public List<User> getUsers() {
		return users;
	}

	public void setUsers(List<User> users) {
		this.users = users;
	}

	public User getPotentialUser() {
		return potentialUser;
	}

	public void setPotentialUser(User potentialUser) {
		this.potentialUser = potentialUser;
	}
	

	public User getCurrentUser() {
		return currentUser;
	}

	public void setCurrentUser(User currentUser) {
		this.currentUser = currentUser;
	}
	

	public boolean isSkip() {
		return skip;
	}

	public void setSkip(boolean skip) {
		this.skip = skip;
	}

	public String validateUser() {
		FacesContext context = FacesContext.getCurrentInstance();
		User user = getUser();
		if (user != null) {
			if (!user.getPassword().equals(potentialUser.getPassword())) {
				FacesMessage message = new FacesMessage(
						FacesMessage.SEVERITY_ERROR, "Login Failed!",
						"The password specified is not correct.");
				context.addMessage(null, message);
				return null;
			}

			context.getExternalContext().getSessionMap()
					.put(USER_SESSION_KEY, user);
			userLoggedIn = true;
			currentUser = user;
			return "gameList?faces-redirect=true";
		} else {
			FacesMessage message = new FacesMessage(
					FacesMessage.SEVERITY_ERROR, "Login Failed!", "Username '"
							+ potentialUser.getUsername() + "' does not exist.");
			context.addMessage(null, message);
			return null;
		}
	}

	@Transactional(propagation = Propagation.REQUIRED)
	public String createUser() {
		FacesContext context = FacesContext.getCurrentInstance();
		User User = getUser();
		if (User == null) {
			/*
			 * if (!potentialUser.getPassword().equals(passwordv)) {
			 * FacesMessage message = new FacesMessage(
			 * "The specified passwords do not match.  Please try again");
			 * context.addMessage(null, message); return null; }
			 */

			try {
				userDao.persist(deepCopyPotential());
				loadUsers();
				FacesMessage message = new FacesMessage(
						FacesMessage.SEVERITY_INFO, "Username '"
								+ potentialUser.getUsername()
								+ "'created!  Please use it to Login",
						"user created.");
				context.addMessage(null, message);

				return "menus?faces-redirect=true";
			} catch (Exception e) {
				FacesMessage message = new FacesMessage(
						FacesMessage.SEVERITY_ERROR,
						"Error creating user!",
						"Unexpected error when creating your account.  Please contact the system Administrator");
				context.addMessage(null, message);
				Logger.getAnonymousLogger().log(Level.SEVERE,
						"Unable to create new user", e);
				return null;
			}
		} else {
			FacesMessage message = new FacesMessage(
					FacesMessage.SEVERITY_ERROR, "Username '"
							+ potentialUser.getUsername()
							+ "' already exists!  ",
					"Please choose a different username.");
			context.addMessage(null, message);
			return null;
		}

	}

	/**
	 * <p>
	 * When invoked, it will invalidate the user's session and move them to the
	 * login view.
	 * </p>
	 * 
	 * @return <code>login</code>
	 */
	public String logout() {
		HttpSession session = (HttpSession) FacesContext.getCurrentInstance()
				.getExternalContext().getSession(false);
		if (session != null) {
			FacesContext.getCurrentInstance().getExternalContext()
					.getSessionMap().remove(USER_SESSION_KEY);
			session.invalidate();
		}
		userLoggedIn = false;
		return "login?faces-redirect=true";

	}
	
    public String onFlowProcess(FlowEvent event) {  
          
        if(skip) {  
            skip = false;   //reset in case user goes back  
            return "confirm";  
        }  
        else {  
            return event.getNewStep();  
        }  
    }  
    
    public void updateScore(long id, int score){
    	User u = userDao.load(User.class, id);
    	u.setScore(u.getScore()+score);
    	userDao.merge(u);
    }

	// --------------------------------------------------------- Private Methods

	/**
	 * <p>
	 * This will attempt to lookup a <code>User</code> object based on the
	 * provided user name.
	 * </p>
	 * 
	 * @return a <code>User</code> object associated with the current username,
	 *         otherwise, if no <code>User</code> can be found, returns
	 *         <code>null</code>
	 */
	private User getUser() {
		return userDao.findUserByName(potentialUser.getUsername());
	}
	

	private User deepCopyPotential() {
		User user = new User();
		user.setUsername(potentialUser.getUsername());
		user.setFirstname(potentialUser.getFirstname());
		user.setPassword(potentialUser.getPassword());
		user.setLastname(potentialUser.getLastname());
		user.setPhone(potentialUser.getPhone());
		user.setEmail(potentialUser.getEmail());
		user.setCreatedDate(new Date());
		return user;
	}

}
