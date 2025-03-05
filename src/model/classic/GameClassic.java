package model.classic;

import java.util.ArrayList;
import java.util.List;
import model.Card;
import model.Game;
import model.Stats;
import model.sound.Sound;



/**
 * This class logically represents the classic game mode.
 * The latter follows the official rules of the game.
 * 
 * Contains the game loop that manages all the possible game dynamics (e.g. game state, turns, etc.) and communicates them to the view with the observer pattern.
 * @author MP
 *
 */
public class GameClassic extends Game {

	/**
	 * it is our representation in the game
	 */
	private PlayerClassic player;
	
	/**
	 * it is the representation of the artificial players in the game
	 */
	private PlayerAIClassic playerAI_1;
	private PlayerAIClassic playerAI_2;
	private PlayerAIClassic playerAI_3;

	/**
	 * 0 if i still have to decide whether to doubt or not
	 * 1 YES +4 (i chose not to doubt the previous player's +4)
	 * 2 NO +6/0 (i chose to doubt the previous player's +4)
	 */
	private String popUpAnsw="";

	/**
	 * If the wildcard+4 is playable correctly is set to true
	 */
	private boolean wasPlayableWildCardPlus4;
	
	/**
	 * Was the button to say UNO! clicked?
	 */
	private boolean unoButtonClicked;


	




	/**
	 * Constructs a gameClassic object
	 * @param stats the stats of the game 
	 * @param sound the sound of the game 
	 */
	public GameClassic(Stats stats, Sound sound) {
		
		super(stats, sound);
		initializeVariables();
	}



	/**
	 * manages all the possible game dynamics
	 * - check if and who won the game
	 * - manages player turns
	 * - manages the drawing of cards 
	 * - plays sound effects in the appropriate case
	 * - check if player said UNO correctly
	 * - handles the dynamic of doubt with wildcards +4
	 *    
	 * It communicates them to the view with the observer pattern.
	 */
	@Override
	public void gameLoop() {

		if(player.getHand().size() == 0 || playerAI_1.getHand().size() == 0 || playerAI_2.getHand().size() == 0 || playerAI_3.getHand().size() == 0 ) {

			//who won?
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
	 *  resets the fields of Game and GameClassic to setup a new game
	 */
	public void initializeVariables() {

		discardedCard=null;

		turnIndex=0;
		
		clockwise = true;

		popUpAnsw="";

		drawCardsBoolean = false;

		wasPlayableWildCardPlus4 = false;
		
		drawCardsCounter=0;

		player0ThrewCard=false;
		
		unoButtonClicked=false;

		winner="";
		gameStatus = "";

		deck.clear();
		initDeck();

		untouchedDeck = new ArrayList<>(deck);

		initDiscardedCard();

		player = new PlayerClassic(this);
		playerAI_1 = new PlayerAIClassic(this);
		playerAI_2 = new PlayerAIClassic(this);
		playerAI_3 = new PlayerAIClassic(this);
	}


	
	
	
	
	
	
	//GETTER AND SETTER 

	public PlayerClassic getPlayer() {
		return player;
	}

	public PlayerAIClassic getPlayerAI_1() {
		return playerAI_1;
	}

	public PlayerAIClassic getPlayerAI_2() {
		return playerAI_2;
	}

	public PlayerAIClassic getPlayerAI_3() {
		return playerAI_3;
	}

	public boolean wasPlayableWildCardPlus4() {
		return wasPlayableWildCardPlus4;
	}

	public void setPlayableWildCardPlus4(boolean playableWildCardPlus4) {
		this.wasPlayableWildCardPlus4 = playableWildCardPlus4;
	}

	public String getPopUpAnsw() {
		return popUpAnsw;
	}

	public void setPopUpAnsw(String popUpAnsw) {
		this.popUpAnsw = popUpAnsw;
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