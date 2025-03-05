package model;

import java.util.ArrayList;
import java.util.List;
import java.util.Observable;
import java.util.Random;

import model.sound.Sound;

public abstract class Game extends Observable implements Runnable {

	protected Card discardedCard;
	
	protected Stats stats;
	protected Sound sound;
	protected Thread gameThread;
	
	protected int turnIndex;

	protected List<Card> deck = new ArrayList<>();
	
	/**
	 * a copy of the deck gets created in order to add the listeners to all the cards, 
	 * because some cards wont be anymore in the deck since they will have been distributed to the players
	 */
	protected List<Card> untouchedDeck;
	
	protected int drawCardsCounter;
	
	protected boolean clockwise = true;

	/**
	 * if the drawCardsCounter label is to be removed, the boolean must be set to true
	 */
	protected boolean drawCardsBoolean;
	
	protected boolean specialDiscarded;

	/**
	 * true if it is the player who throws the card otherwise if it is an artificial player it is set to false
	 */
	protected boolean player0ThrewCard;

	/**
	 * indicating the dynamics of choosing the color after a wildcard and wildcard+4.
	 * 
	 * True if we still have to choose the color.
	 */
	protected boolean colorAnsw; 
	
	protected String winner;

	protected String gameStatus = "";

	protected Random rand = new Random();



	/**
	 * Constructs a Game object
	 * @param stats the stats of the game 
	 * @param sound the sound of the game 
	 */
	public Game(Stats stats, Sound sound) {

		this.stats = stats;
		this.sound = sound;
	}



	/**
	 * creates the cards and adds them to the deck
	 */
	public void initDeck() {

		
		char[] list_colors = {'B','R','G','Y'};
		
		//default cards
		int c=0;
		for(int i=1; i<=9; i++) { //for the nine numbers

			for(int j=1; j<=8; j++) { //each number eight times (4colors*2)

				deck.add(new Card(i, list_colors[c], Type.DEFAULT));
				if(c==3)
					c=0;
				else
					c++;
			}
		}
		
		//add 0s cards to the deck
		c=0;
		for(int j=1; j<=4; j++) { 
			
			deck.add( new Card(0, list_colors[c], Type.DEFAULT));
			c++;
		}
		
		//adds only Cards TYPE.WILDCARD and TYPE.WILDCARDPLUS4
		for(int j=1; j<=4; j++) { 

			deck.add( new Card(-1, 'N', Type.WILDCARD));
			deck.add( new Card(-1, 'N', Type.WILDCARDPLUS4));
		}
		
		//adds all the special cards except the wildcard and wildcard+4 
		Type[] list_types = {Type.PLUS2, Type.REVERSE, Type.SKIP};
		c=0;
		for(int i=0; i<3 ;i++) { //for the 3 types

			for(int j=1; j<=8; j++) { //each type eight times (4colors*2)

				deck.add(new Card(-1, list_colors[c], list_types[i]));
				if(c==3)
					c=0;
				else
					c++;
			}
		}
	}

	

	/**
	 * it makes play the next player based on the direction(clockwise, anticlockwise) of the turn
	 */
	public void passTurn() {

		if (clockwise)
			turnIndex++;
		else
			turnIndex--;

		if(turnIndex==4)
			turnIndex=0;
		if(turnIndex==-1)
			turnIndex=3;	
	}

	
	
	/**
	 * it makes play the next-next player based on the direction(clockwise, anticlockwise) of the turn 
	 */
	public void passTurnSkip() {

		if (clockwise)
			turnIndex+=2;
		else
			turnIndex-=2;

		switch(turnIndex) {

		case -2 -> turnIndex=2;		
		case -1 -> turnIndex=3;		
		case 4 -> turnIndex=0;
		case 5 -> turnIndex=1;
		}
	}

	
	
	/**
	 * it initializes randomly the first discarded card and its effects on the game
	 */
	public void initDiscardedCard() {

		int size = deck.size();
		discardedCard = deck.get((rand.nextInt(size)));

		while(discardedCard.getType() == Type.WILDCARDPLUS4) //the first discarded card cant be a wildcard+4
			discardedCard = deck.get((rand.nextInt(size)));
		
		if(discardedCard.getType() == Type.PLUS2) {
			
			drawCardsCounter+=2;	
			specialDiscarded=true;
		}

		else if(discardedCard.getType() == Type.REVERSE) {
			
			clockwise = !clockwise;
			specialDiscarded=true;
			passTurn();
		}

		else if(discardedCard.getType() == Type.SKIP) {
			
			specialDiscarded=true;
			passTurnSkip();
		}
		deck.remove(discardedCard);
	}


	
	public abstract void gameLoop();
	
	
	
	/**
	 * it updates the stats of the game based on the outcome of the game and it calculates the level of the player
	 */
	public void updateStats() {

		stats.setMatchesPlayed(stats.getMatchesPlayed()+1);
		
		if(winner.equals(stats.getUsername()))
			stats.setMatchesWon(stats.getMatchesWon()+1);
		else
			stats.setMatchesLost(stats.getMatchesLost()+1);

		int p = 100 - stats.getMatchesWon() + stats.getMatchesLost();
		if(p<=0)
			p=1;
		
		double a1 = (double)stats.getMatchesWon()/p;
		double a2 = (Math.sin(Math.atan(a1)))*100;
		
		stats.setLevel((int)a2); 
		stats.save();
	}

	
	
	/**
	 * Restrictions on the frequency (60 FPS) in which the program calls gameLoop()
	 * Delta/Accumulator method.
	 */
	public void run() {
		
		double Interval = 1000000000/60; //1 second (in nanoseconds)/60. 
		double delta = 0;
		long lastTime = System.nanoTime(); //current value of the running JVM time source in nanoseconds
		long currentTime;
		long timer = 0;
		int drawCount = 0;
		
		while (gameThread != null) {

			currentTime = System.nanoTime(); //current value of the running JVM time source in nanoseconds
			delta += (currentTime - lastTime) / Interval;
			lastTime = currentTime;

			if(delta >= 1) {
				gameLoop();
				delta--;
			}
			
			if (timer >= 1000000000) {
				System.out.println("FPS:" + drawCount);
				drawCount = 0;
				timer = 0;
			}
		}
	}
	

	/**
	 * It notifies the view the changes made to the game
	 */
	public void notifyView() {
		setChanged();
		notifyObservers();	
	}


	
	/**
	 * it creates a new Thread and starts it
	 */
	public void startGameThread() {

		gameThread = new Thread(this); 
		gameThread.start(); //automatically call run() method 
	}
	
	
	
	
	
	
	
	
	
	
	
	//GETTER AND SETTER
	public void setDeck(List<Card> deck) {
		this.deck = deck;
	}

	public List<Card> getUntouchedDeck() {
		return untouchedDeck;
	}

	public Card getDiscardedCard() {
		return discardedCard;
	}

	public void setDiscardedCard(Card discardedCard) {
		this.discardedCard = discardedCard;
	}

	public int getCounter() {
		return drawCardsCounter;
	}

	public void setCounter(int i) {
		drawCardsCounter = i;
	}

	public boolean player0ThrewCard() {
		return player0ThrewCard;
	}

	public void setPlayer0ThrewCard(boolean thrownCard) {
		this.player0ThrewCard = thrownCard;
	}

	public int getTurnIndex() {
		return turnIndex;
	}

	public void setTurnIndex(int i) {
		turnIndex = i;
	}

	public boolean isClockwise() {
		return clockwise;
	}

	public void setClockwise(boolean clockwise) {
		this.clockwise = clockwise;
	}

	public boolean getDrawCardsBoolean() {
		return drawCardsBoolean;
	}

	public void setDrawCardsBoolean(boolean drawCardsBoolean) {
		this.drawCardsBoolean = drawCardsBoolean;
	}

	public String getWinner() {
		return winner;
	}
	
	public Sound getSound() {
		return sound;
	}

	public void setGameStatus(String gameStatus) {
		this.gameStatus = gameStatus;
	}

	public String getGameStatus() {
		return gameStatus;
	}

	public Thread getGameThread() {
		return gameThread;
	}
}