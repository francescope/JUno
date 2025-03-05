package model.deadly;

import java.util.ArrayList;
import java.util.List;
import model.Card;
import model.Game;
import model.Stats;
import model.Type;
import model.sound.Sound;

/**
 * This class logically represents the deadly game mode.
 * The latter follows custom rules of the game.
 * 
 * - if a player cannot play any cards he is eliminated from the game.
 * - it's not possible to draw a single card, doubt the +4s and say UNO!
 * - it's possible to win both in the classic way (finish the cards in the hand) and if you are the only player left.
 * 
 * Contains the game loop that manages all the possible game dynamics (e.g. game state, turns, etc.) and communicates them to the view with the observer pattern.
 * @author MP
 *
 */
public class GameDeadly extends Game {

	/**
	 * it is our representation in the game
	 */
	private PlayerDeadly player;
	
	/**
	 * it is the representation of the artificial players in the game
	 */
	private PlayerAIDeadly playerAI_1;
	private PlayerAIDeadly playerAI_2;
	private PlayerAIDeadly playerAI_3;
		
	/**
	 * true if this is the first time we check if we have playable cards 
	 */
	private boolean firstTimeCheck = true;
	

	
	/**
	 * Constructs a gameDeadly object
	 * @param stats the stats of the game 
	 * @param sound the sound of the game 
	 */
	public GameDeadly(Stats stats, Sound sound) {
		
		super(stats, sound);
		
		initializeVariables();
	}



	/**
	 * manages all the possible game dynamics
	 * - check if and who won the game
	 * - manages player turns and eliminations
	 * - manages the drawing (not single) of cards 
	 * - plays sound effects in the appropriate case
	 *    
	 * It communicates them to the view with the observer pattern.
	 */
	@Override
	public void gameLoop() {

		if(player.getHand().size() == 0 
				|| (playerAI_1.getHand().size() == 0 && playerAI_1.isInGame()) 
				|| (playerAI_2.getHand().size() == 0 && playerAI_2.isInGame() ) 
				|| (playerAI_3.getHand().size() == 0 && playerAI_3.isInGame())) {

			//who won?
			if(player.getHand().size() == 0)
				winner = stats.getUsername();
			else if(playerAI_1.getHand().size() == 0 && playerAI_1.isInGame())
				winner = "Lionel Messi";
			else if(playerAI_2.getHand().size() == 0 && playerAI_2.isInGame())
				winner = "Charles Leclerc";
			else if(playerAI_3.getHand().size() == 0 && playerAI_3.isInGame())
				winner = "Elon Musk";

			gameStatus = "FINISHED";
			updateStats();
			
			notifyView();
			gameThread.stop();
		}

		//Check if am the only one left in the game
		if(!playerAI_1.isInGame() && !playerAI_2.isInGame() && !playerAI_3.isInGame() ) {
			
			gameStatus = "FINISHED";
			winner = stats.getUsername();

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
		
		
		// AI's turns
		switch(turnIndex) {
		
		case 0 -> player.play();

		case 1 -> {
			firstTimeCheck = true;
			
			if(playerAI_1.isInGame()) {
				notifyView();
				playerAI_1.AI();
			}
			
			else
				passTurn();
		}

		case 2 -> {
			firstTimeCheck = true;

			if(playerAI_2.isInGame()) {
				notifyView();
				playerAI_2.AI();
			}
			
			else
				passTurn();
		}

		case 3 -> {
			firstTimeCheck = true;

			if(playerAI_3.isInGame()) {
				notifyView();
				playerAI_3.AI();
			}
			
			else
				passTurn();
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
	 *  resets the fields of Game and GameDeadly to setup a new game
	 */
	public void initializeVariables() {

		discardedCard=null;

		turnIndex=0;
		
		clockwise = true;

		drawCardsBoolean = false;

		drawCardsCounter=0;

		player0ThrewCard=false;

		winner="";
		gameStatus = "";

		deck.clear();
		initDeck();
		
		untouchedDeck = new ArrayList<>(deck);

		initDiscardedCard();
		
		if(discardedCard.getType()==Type.WILDCARD || discardedCard.getType()==Type.WILDCARDPLUS4 ) {
						
			deck.add(discardedCard);
			
			while(discardedCard.getType()==Type.WILDCARD || discardedCard.getType()==Type.WILDCARDPLUS4) {
				
				int size = deck.size();
				discardedCard = deck.get((rand.nextInt(size)));
			}
			deck.remove(discardedCard);
		}
		
		player = new PlayerDeadly(this);
		playerAI_1 = new PlayerAIDeadly(this);
		playerAI_2 = new PlayerAIDeadly(this);
		playerAI_3 = new PlayerAIDeadly(this);
	}
	
	
	
	
	
	
	
	

	//GETTER AND SETTER

	public boolean isFirstTimeCheck() {
		return firstTimeCheck;
	}

	public void setFirstTimeCheck(boolean firstTimeCheck) {
		this.firstTimeCheck = firstTimeCheck;
	}

	public PlayerDeadly getPlayer() {
		return player;
	}

	public PlayerAIDeadly getPlayerAI_1() {
		return playerAI_1;
	}

	public PlayerAIDeadly getPlayerAI_2() {
		return playerAI_2;
	}

	public PlayerAIDeadly getPlayerAI_3() {
		return playerAI_3;
	}

	public boolean isColorAnsw() {
		return colorAnsw;
	}
	
	public void setColorAnsw(boolean colorAnsw) {
		this.colorAnsw = colorAnsw;
	}
}