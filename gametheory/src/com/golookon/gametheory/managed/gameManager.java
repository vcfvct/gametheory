package com.golookon.gametheory.managed;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.ResourceBundle;

import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.golookon.gametheory.entity.Game;
import com.golookon.gametheory.entity.PlayerChoice;
import com.golookon.gametheory.entity.User;

@Component("gameManager")
@Scope("session")
public class gameManager {
	public static List<Game> waitingGameList = new ArrayList<Game>();
	public static List<Game> activeGameList = new ArrayList<Game>();
	public static List<Game> finishedGameList = new ArrayList<Game>();
	private Game currentGame;
	private Game selectedGame;
	private ResourceBundle bundle;
	private int ss;
	private int sn_s;
	private int sn_n;
	private int nn;
	
	private UserManager userManager;	
	public UserManager getUserManager() {
		return userManager;
	}
	@Autowired
	public void setUserManager(UserManager userManager) {
		this.userManager = userManager;
	}
	
	public gameManager() {
		currentGame = new Game();
		generateScoreCriterion();
	}

	// initialize the scoring criterion from the configuration file.
	private void generateScoreCriterion() {
		int earning = Integer.parseInt(getBundle().getString("earning"));
		int schooling = Integer.parseInt(getBundle().getString("schooling"));
		int smallFine = Integer.parseInt(getBundle().getString("smallFine"));
		int compensation = Integer.parseInt(getBundle().getString(
				"compensation"));
		int bigFine = Integer.parseInt(getBundle().getString("bigFine"));

		ss = earning - schooling;
		sn_s = earning - schooling + compensation;
		sn_n = earning - bigFine;
		nn = earning - smallFine;
		System.out.println(ss + " " + sn_s + " " + sn_n + " " + nn);
	}

	// create a new game in the waiting table
	public void createGame() {
		FacesContext context = FacesContext.getCurrentInstance();
		User user = (User) context.getExternalContext().getSessionMap()
				.get(UserManager.USER_SESSION_KEY);
		Game game = new Game();
		game.setFirstPlayerId(user.getId());
		game.setFirstPlayerName(user.getUsername());
		game.setFirstPlayerScore(0);
		game.setFirstPlayerChoice(-1);
		game.setPlayerChoices(new ArrayList<PlayerChoice>());
		game.setCreatedDate(new Date());
		waitingGameList.add(game);
		context.addMessage(null, new FacesMessage("Attention",
		"A New Game Session Created"));
		// return "gameList";
	}

	// event handler in waiting table.
	public String joinWaitingGame() {

		FacesContext context = FacesContext.getCurrentInstance();
		User user = (User) context.getExternalContext().getSessionMap()
				.get("user");
		if (user.getUsername().equals(currentGame.getFirstPlayerName())) {
			if (currentGame.getSecondPlayerName() != null) {
				return "game?faces-redirect=true";
			} else {
				context.addMessage(null, new FacesMessage("Attention",
						"You can not join your own game."));
				return null;
			}
		}
		currentGame.setSecondPlayerId(user.getId());
		currentGame.setSecondPlayerName(user.getUsername());
		currentGame.setSecondPlayerScore(0);
		currentGame.setSecondPlayerChoice(-1);
		activeGameList.add(currentGame);
		waitingGameList.remove(currentGame);
		return "game?faces-redirect=true";
	}

	// remove game in waiting table
	public void removeGame() {
		FacesContext context = FacesContext.getCurrentInstance();
		User user = (User) context.getExternalContext().getSessionMap()
				.get("user");
		if (user.getUsername().equals(currentGame.getFirstPlayerName())) {
			waitingGameList.remove(currentGame);
			context.addMessage(null, new FacesMessage("Attention",
					"Game removed"));
		} else {
			context.addMessage(null, new FacesMessage("Access Denied",
					"You are not the owner of the Game"));
		}

	}

	// refresh waiting and active table
	public void refreshList() {

	}

	// event handler for active table
	public String joinActiveGame() {

		FacesContext context = FacesContext.getCurrentInstance();
		User user = (User) context.getExternalContext().getSessionMap()
				.get("user");
		if (user.getUsername().equals(currentGame.getFirstPlayerName())) {
			if (currentGame.getFirstPlayerChoice() == -1)
				return "game?faces-redirect=true";
			else {
				context.addMessage(null, new FacesMessage("Attention",
						"You Need to wait the other player's move "));
				return null;
			}
		}
		if (user.getUsername().equals(currentGame.getSecondPlayerName())) {
			if (currentGame.getSecondPlayerChoice() == -1)
				return "game?faces-redirect=true";
			else {
				context.addMessage(null, new FacesMessage("Attention",
						"You Need to wait the other player's move "));
				return null;
			}
		}
		context.addMessage(null, new FacesMessage("Attention",
				"You are not the paticipant of this Game "));

		return null;
	}

	// action when schooling seleced
	public String school() {
		return schoolHandler(true);
	}

	// action when no schooling seleced
	public String noSchool() {
		return schoolHandler(false);
	}

	private String schoolHandler(boolean isSchool) {
		FacesContext context = FacesContext.getCurrentInstance();
		User user = (User) context.getExternalContext().getSessionMap()
				.get("user");
		if (user.getUsername().equals(currentGame.getFirstPlayerName())) {
			if (isSchool)
				currentGame.setFirstPlayerChoice(1);
			else
				currentGame.setFirstPlayerChoice(0);
			if (currentGame.getSecondPlayerChoice() == -1) {
				context.addMessage(
						null,
						new FacesMessage("Attention",
								"You Choice has been recorded. Wait the other player's move "));
				return "gameList?faces-redirect=true";
			} else {
				recordAndCleanup();
				context.addMessage(null, new FacesMessage("Finished a round",
						"You can select to begin a new round. "));
				return "gameList?faces-redirect=true";
			}
		} else {
			if (isSchool)
				currentGame.setSecondPlayerChoice(1);
			else
				currentGame.setSecondPlayerChoice(0);
			if (currentGame.getFirstPlayerChoice() == -1) {
				context.addMessage(
						null,
						new FacesMessage("Attention",
								"You Choice has been recorded. Wait the other player's move "));
				return "gameList?faces-redirect=true";
			} else {
				recordAndCleanup();
				context.addMessage(null, new FacesMessage("Finished a round",
						"You can select to begin a new round if the Game is not finished. "));
				return "gameList?faces-redirect=true";
			}
		}
	}

	// persist when a round finishes
	private void recordAndCleanup() {
		PlayerChoice pc = new PlayerChoice();
		pc.setFirstPlayerChoice(convertChoice(currentGame
				.getFirstPlayerChoice()));
		pc.setSecondPlayerChoice(convertChoice(currentGame
				.getSecondPlayerChoice()));
		pc.setRound(currentGame.getPlayerChoices().size() + 1);
		currentGame.getPlayerChoices().add(pc);
		if (currentGame.getFirstPlayerChoice() == 1
				&& currentGame.getSecondPlayerChoice() == 1) {
			currentGame.setFirstPlayerScore(currentGame.getFirstPlayerScore()
					+ ss);
			currentGame.setSecondPlayerScore(currentGame.getSecondPlayerScore()
					+ ss);
		} else if (currentGame.getFirstPlayerChoice() == 1
				&& currentGame.getSecondPlayerChoice() == 0) {
			currentGame.setFirstPlayerScore(currentGame.getFirstPlayerScore()
					+ sn_s);
			currentGame.setSecondPlayerScore(currentGame.getSecondPlayerScore()
					+ sn_n);
		} else if (currentGame.getFirstPlayerChoice() == 0
				&& currentGame.getSecondPlayerChoice() == 1) {
			currentGame.setFirstPlayerScore(currentGame.getFirstPlayerScore()
					+ sn_n);
			currentGame.setSecondPlayerScore(currentGame.getSecondPlayerScore()
					+ sn_s);
		} else if (currentGame.getFirstPlayerChoice() == 0
				&& currentGame.getSecondPlayerChoice() == 0) {
			currentGame.setFirstPlayerScore(currentGame.getFirstPlayerScore()
					+ nn);
			currentGame.setSecondPlayerScore(currentGame.getSecondPlayerScore()
					+ nn);
		}
		currentGame.setFirstPlayerChoice(-1);
		currentGame.setSecondPlayerChoice(-1);

		if (currentGame.getRound() == currentGame.getPlayerChoices().size()) {
			finishedGameList.add(currentGame);
			activeGameList.remove(currentGame);
			//TODO update score in Modal
			userManager.updateScore(currentGame.getFirstPlayerId(), currentGame.getFirstPlayerScore());
			userManager.updateScore(currentGame.getSecondPlayerId(), currentGame.getSecondPlayerScore());
			userManager.loadUsers();
		}
	}

	// get resource bundle.
	public ResourceBundle getBundle() {
		if (bundle == null) {
			FacesContext context = FacesContext.getCurrentInstance();
			bundle = context.getApplication()
					.getResourceBundle(context, "msgs");
		}
		return bundle;
	}

	// convert value
	private String convertChoice(int i) {
		if (i == 0)
			return "No Schooling";
		return "Schooling";
	}
	
	public String seeGameReport(){
		return "gameReport?faces-redirect=true";
	}

	public List<Game> getWaitingGameList() {
		return waitingGameList;
	}

	public List<Game> getFinishedGameList() {
		return finishedGameList;
	}

	public List<Game> getActiveGameList() {
		return activeGameList;
	}

	public Game getCurrentGame() {
		return currentGame;
	}

	public void setCurrentGame(Game currentGame) {
		this.currentGame = currentGame;
	}

	public Game getSelectedGame() {
		return selectedGame;
	}

	public void setSelectedGame(Game selectedGame) {
		this.selectedGame = selectedGame;
	}

	public int getSs() {
		return ss;
	}

	public void setSs(int ss) {
		this.ss = ss;
	}

	public int getSn_s() {
		return sn_s;
	}

	public void setSn_s(int sn_s) {
		this.sn_s = sn_s;
	}

	public int getSn_n() {
		return sn_n;
	}

	public void setSn_n(int sn_n) {
		this.sn_n = sn_n;
	}

	public int getNn() {
		return nn;
	}

	public void setNn(int nn) {
		this.nn = nn;
	}

}
