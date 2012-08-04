package com.golookon.gametheory.entity;

public class PlayerChoice {
	private String firstPlayerChoice;
	private String secondPlayerChoice;
	private int round;

	public PlayerChoice() {
		super();
	}

	public String getFirstPlayerChoice() {
		return firstPlayerChoice;
	}

	public void setFirstPlayerChoice(String firstPlayerChoice) {
		this.firstPlayerChoice = firstPlayerChoice;
	}

	public String getSecondPlayerChoice() {
		return secondPlayerChoice;
	}

	public void setSecondPlayerChoice(String secondPlayerChoice) {
		this.secondPlayerChoice = secondPlayerChoice;
	}

	public int getRound() {
		return round;
	}

	public void setRound(int round) {
		this.round = round;
	}

	public int getFirstPlayerScore() {
		//to be implemented
		return 0;
	}

	public int getSecondPlayerScore() {
		//to be implemented
		return 0;
	}

}
