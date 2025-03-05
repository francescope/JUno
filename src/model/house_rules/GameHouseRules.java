package model.house_rules;

import java.util.ArrayList;
import java.util.List;
import model.Card;
import model.Game;
import model.Stats;
import model.sound.Sound;

/**
 * This class logically represents the house rules game mode.
 * The latter follows the house rules of the game 
 * - wildcards+4 and +2 are cumulative
 * - wildcards+4 are not questionable
 * 
 * Contains the game loop that manages all the possible game dynamics (e.g. game state, turns, etc.) and communicates them to the view with the observer pattern.
 * @author MP
 *
 */
public class GameHouseRules extends Game {

	/**
	 * it is our representation in the game
	 */
	private PlayerHouseRules player;
	
	/**
	 * it is the representation of the artificial players in the game
	 */
	private PlayerAIHouseRules playerAI_1;
	private PlayerAIHouseRules playerAI_2;
	private PlayerAIHouseRules playerAI_3;


	/**
	 * Was the button to say UNO! clicked?
	 */
	private boolean unoButtonClicked;
	
	

	/**
	 * Constructs a gameHouseRules object
	 * @param stats the stats of the game 
	 * @param sound the sound of the game 
	 */
	public GameHouseRules(Stats stats, Sound sound) {

		super(stats, sound);
		initializeVariables();
	}

	
	
	/**
	 * manages all the possible game dynamics
	 * - check if and who won the game
	 * - manages player turns
	 * - manages the drawing of cards (check if the +4 and +2 cards are combined)
	 * - plays sound effects in the appropriate case
	 * - check if player said UNO correctly
	 *    
	 * It communicates them to the view with the observer pattern.
	 */
	@Override
	public void gameLoop() {

		if(player.getHand().size() == 0 || playerAI_1.getHand().size() == 0 ||playerAI_2.getHand().size() == 0 ||playerAI_3.getHand().size() == 0) {

			//who won
			if(player.getHand().size() == 0)
				winner = stats.getUsername();
			if(playerAI_1.getHand().size() == 0)
				winner = "Lionel Messi";
			if(playerAI_2.getHand().size() == 0)
				winner = "Charles Leclerc";
			if(playerAI_3.getHand().size() == 0)
				winner = "Elon Musk";

			gameStatus = "FINISHED";
			updateStats();

			notifyView();
			gameThread.stop(); 
		}
		
		notifyView();

		//slows down the game to make you understand whats going on as soon as the game starts
		if(specialDiscarded) {

			try {
				Thread.sleep(2000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			specialDiscarded=false;
		}

		//Player's turn
		if(turnIndex==0) {

			notifyView();
			player.play();
		}

		// check if player said UNO correctly

		//the player forgot to say UNO so he draw 4 cards
		if (!unoButtonClicked && player.getHand().size()==1) {			

			for(int i=0; i<4; i++) 
				player.pickCard();

			sound.playSE(10); //draw4 
			setUnoButtonClicked(false);
		}

		
		// AI's turns
		switch(turnIndex) {

		case 1 -> {
			notifyView();
			playerAI_1.AI();
		}

		case 2 -> {
			notifyView();
			playerAI_2.AI();
		}

		case 3 -> {
			notifyView();
			playerAI_3.AI();
		}
		};
	}



	/**
	 * if the deck's size is zero it refills the deck with the played cards else it returns the deck
	 * @return the deck object which is an array of cards
	 */
	public List<Card> getDeck() {

		if (deck.size()==0) {

			//we get the cards to be reused from those that have been thrown away
			List<Card> deckCopy = new ArrayList<>(untouchedDeck);

			deckCopy.removeAll(player.getHand());
			deckCopy.removeAll(playerAI_1.getHand());
			deckCopy.removeAll(playerAI_2.getHand());
			deckCopy.removeAll(playerAI_3.getHand());

			deck=deckCopy;
		}
		return deck;
	}
	
	

	/**
	 *  resets the fields of Game and GameHouseRules to setup a new game
	 */
	public void initializeVariables() {

		discardedCard=null;

		turnIndex=0;
		
		clockwise = true;

		drawCardsBoolean = false;

		drawCardsCounter=0;

		player0ThrewCard=false;

		unoButtonClicked=false;

		winner="";
		gameStatus = "";

		deck.clear();
		initDeck();

		untouchedDeck = new ArrayList<>(deck);

		initDiscardedCard();

		player = new PlayerHouseRules(this);
		playerAI_1 = new PlayerAIHouseRules(this);
		playerAI_2 = new PlayerAIHouseRules(this);
		playerAI_3 = new PlayerAIHouseRules(this);	
	}


	
	
	
	
	
	
	
	
	
	
	//GETTER AND SETTER

	public PlayerHouseRules getPlayer() {
		return player;
	}

	public PlayerAIHouseRules getPlayerAI_1() {
		return playerAI_1;
	}

	public PlayerAIHouseRules getPlayerAI_2() {
		return playerAI_2;
	}

	public PlayerAIHouseRules getPlayerAI_3() {
		return playerAI_3;
	}

	public void setUnoButtonClicked(boolean b) {
		unoButtonClicked = b;
	}

	public boolean isColorAnsw() {
		return colorAnsw;
	}

	public void setColorAnsw(boolean colorAnsw) {
		this.colorAnsw = colorAnsw;
	}
}