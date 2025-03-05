package model;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.stream.Collectors;

/**
 * This class represents the generic artificial player (PlayerAI).
 * - handles the situation where the artificial player throws a card
 * - manages the choice of color after an artificial player has thrown a wildcard or wildcard+4 card
 * @author MP
 *
 */
public abstract class PlayerAI {

	protected Game game;
	
	/**
	 * boolean set to true when the artificial player must say UNO!
	 */
	protected boolean isUnoDisplayable;
	
	/**
	 * playerAI's hand
	 */
	protected List<Card> hand = new ArrayList<>();
	
	protected Random rand = new Random();
	
	
	
	/**
	 * constructs a PlayerAI object 
	 * @param game
	 */
	public PlayerAI(Game game) {
		
		this.game = game;
	}
	
	
	
	/**
	 * it removes the card from the hand and updates the discarded card as well as notifing the view
	 * @param card the card that has to be thrown 
	 */
	public void throwCard(Card card) {

		hand.remove(card);
		game.setDiscardedCard(card); 
		game.notifyView();
	}
	
	
	
	/**
	 * chooses the most recurring color among the cards in the player's hand
	 * @return the char of the chosen color
	 */
	public char chooseColorSmart() {

		Map<Character, Long> counters = hand.stream()
				.filter(card -> card.getC() != 'N')
				.collect(Collectors.groupingBy(card -> card.getC(), 
						Collectors.counting()));

		//when playerAI has only black cards (wildcard or wildcard4) he chooses a random color
		if (counters.isEmpty()) {

			int x = rand.nextInt(4);

			char c = switch(x) {

			case 0 -> 'Y';
			case 1 -> 'R';
			case 2 -> 'B';
			case 3 -> 'G';
			default -> throw new IllegalArgumentException("Unexpected value: " + x);
			};

			//sound
			switch (c) {

			case 'Y' -> game.getSound().playSE(6);
			case 'R' -> game.getSound().playSE(5);
			case 'B' -> game.getSound().playSE(3);
			case 'G' -> game.getSound().playSE(4);
			};

			return c;
		}

		//it chooses the most recurring color (char) in the map (char : int)
		char color = counters.entrySet().stream().max((entry1, entry2) -> entry1.getValue() > entry2.getValue() ? 1 : -1).get().getKey();

		switch (color) {

		case 'Y' -> game.getSound().playSE(6);
		case 'R' -> game.getSound().playSE(5);
		case 'B' -> game.getSound().playSE(3);
		case 'G' -> game.getSound().playSE(4);
		};

		return color;
	}
}