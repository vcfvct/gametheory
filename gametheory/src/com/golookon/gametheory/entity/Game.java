package com.golookon.gametheory.entity;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;
import java.util.ResourceBundle;

import javax.faces.context.FacesContext;

public class Game {
	private Long firstPlayerId;
	private Long secondPlayerId;
	private String firstPlayerName;
	private String secondPlayerName;
	private int firstPlayerScore;
	private int secondPlayerScore;
	private int firstPlayerChoice;
	private int secondPlayerChoice;
	private Date createdDate;
	private int round;
	private List<PlayerChoice> playerChoices;

	private ResourceBundle bundle;

	// get resource bundle.
	public ResourceBundle getBundle() {
		if (bundle == null) {
			FacesContext context = FacesContext.getCurrentInstance();
			bundle = context.getApplication()
					.getResourceBundle(context, "msgs");
		}
		return bundle;
	}

	public Game() {
		playerChoices = new ArrayList<PlayerChoice>();
		round = Integer.parseInt(getBundle().getString("round"))
				- new Random().nextInt(Integer.parseInt(getBundle().getString("roundVariance")));
	}

	public Long getFirstPlayerId() {
		return firstPlayerId;
	}

	public void setFirstPlayerId(Long long1) {
		this.firstPlayerId = long1;
	}

	public Long getSecondPlayerId() {
		return secondPlayerId;
	}

	public void setSecondPlayerId(Long secondPlayerId) {
		this.secondPlayerId = secondPlayerId;
	}

	public String getFirstPlayerName() {
		return firstPlayerName;
	}

	public void setFirstPlayerName(String firstPlayerName) {
		this.firstPlayerName = firstPlayerName;
	}

	public String getSecondPlayerName() {
		return secondPlayerName;
	}

	public void setSecondPlayerName(String secondPlayerName) {
		this.secondPlayerName = secondPlayerName;
	}

	public int getFirstPlayerScore() {
		return firstPlayerScore;
	}

	public void setFirstPlayerScore(int firstPlayerScore) {
		this.firstPlayerScore = firstPlayerScore;
	}

	public int getSecondPlayerScore() {
		return secondPlayerScore;
	}

	public void setSecondPlayerScore(int secondPlayerScore) {
		this.secondPlayerScore = secondPlayerScore;
	}

	public int getFirstPlayerChoice() {
		return firstPlayerChoice;
	}

	public void setFirstPlayerChoice(int fisrtPlayerChoice) {
		this.firstPlayerChoice = fisrtPlayerChoice;
	}

	public int getSecondPlayerChoice() {
		return secondPlayerChoice;
	}

	public void setSecondPlayerChoice(int secondlayerChoice) {
		this.secondPlayerChoice = secondlayerChoice;
	}

	public List<PlayerChoice> getPlayerChoices() {
		return playerChoices;
	}

	public void setPlayerChoices(List<PlayerChoice> playerChoices) {
		this.playerChoices = playerChoices;
	}

	public Date getCreatedDate() {
		return createdDate;
	}

	public void setCreatedDate(Date createdDate) {
		this.createdDate = createdDate;
	}

	public int getRound() {
		return round;
	}

	public void setRound(int round) {
		this.round = round;
	}

}
