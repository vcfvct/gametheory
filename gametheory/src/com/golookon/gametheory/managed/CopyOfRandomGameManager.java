package com.golookon.gametheory.managed;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.ResourceBundle;

import javax.faces.context.FacesContext;

import com.golookon.gametheory.entity.PlayerChoice;

public class CopyOfRandomGameManager {
	private int ss;
	private int sn_s;
	private int sn_n;
	private int nn;
	private int firstPlayerChoice;
	private int secondPlayerChoice;
	private int firstPlayerScore;
	private int secondPlayerScore;
	private int round;
	private boolean finished;
	private List<PlayerChoice> playerChoices;
	private ResourceBundle bundle;

	public CopyOfRandomGameManager() {
		playerChoices = new ArrayList<PlayerChoice>();
		generateScoreCriterion();
		round = 6;
	}

	// initialize the scoring criterion from the configuration file.
	private void generateScoreCriterion() {
		int earning = Integer.parseInt(getBundle().getString("earning"));
		int schooling = Integer.parseInt(getBundle().getString("schooling"));
		int newFine = Integer.parseInt(getBundle().getString("newFine"));

		ss = earning - schooling;
		sn_s = earning - schooling;
		sn_n = earning ;
		nn = earning - newFine;
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

	public void next() {
		PlayerChoice pc = new PlayerChoice();
		firstPlayerChoice = new Random().nextInt(2);
		secondPlayerChoice = new Random().nextInt(2);
		pc.setFirstPlayerChoice(convertChoice(firstPlayerChoice));
		pc.setSecondPlayerChoice(convertChoice(secondPlayerChoice));
		pc.setRound(playerChoices.size() + 1);
		playerChoices.add(pc);
		if (firstPlayerChoice == 1 && secondPlayerChoice == 1) {
			firstPlayerScore += ss;
			secondPlayerScore += ss;
		} else if (firstPlayerChoice == 1 && secondPlayerChoice == 0) {
			firstPlayerScore += sn_s;
			secondPlayerScore += sn_n;
		} else if (firstPlayerChoice == 0 && secondPlayerChoice == 1) {
			firstPlayerScore += sn_n;
			secondPlayerScore += sn_s;
		} else if (firstPlayerChoice == 0 && secondPlayerChoice == 0) {
			firstPlayerScore += nn;
			secondPlayerScore += nn;
		}

		if (playerChoices.size() == round)
			finished = true;
	}

	public void reset() {
		playerChoices.clear();
		firstPlayerScore = 0;
		secondPlayerScore = 0;
		finished = false;
		round = Integer.parseInt(getBundle().getString("round"))
				- new Random().nextInt(Integer.parseInt(getBundle().getString(
						"roundVariance"))) + 3;
	}

	public boolean isFinished() {
		return finished;
	}

	public void setFinished(boolean finisted) {
		this.finished = finisted;
	}

	public int getRound() {
		return round;
	}

	public void setRound(int round) {
		this.round = round;
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

	public int getFirstPlayerChoice() {
		return firstPlayerChoice;
	}

	public void setFirstPlayerChoice(int firstPlayerChoice) {
		this.firstPlayerChoice = firstPlayerChoice;
	}

	public int getSecondPlayerChoice() {
		return secondPlayerChoice;
	}

	public void setSecondPlayerChoice(int secondPlayerChoice) {
		this.secondPlayerChoice = secondPlayerChoice;
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

	public List<PlayerChoice> getPlayerChoices() {
		return playerChoices;
	}

	public void setPlayerChoices(List<PlayerChoice> playerChoices) {
		this.playerChoices = playerChoices;
	}

	// convert value
	private String convertChoice(int i) {
		if (i == 0)
			return "No Schooling";
		return "Schooling";
	}
}
