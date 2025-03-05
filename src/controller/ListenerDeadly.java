package controller;

import java.awt.CardLayout;
import java.awt.Component;
import java.util.List;
import javax.swing.JPanel;
import model.Card;
import model.Type;
import model.deadly.GameDeadly;
import model.sound.Sound;
import view.CardB;
import view.DeadlyPanel;


public class ListenerDeadly {

	private DeadlyPanel deadlyPanel;
	private GameDeadly gameDeadly;
	private CardLayout cl;
	private JPanel gamePanel;
	private Sound sound;
	private int wildcardORplus4;

	
	
	public ListenerDeadly(DeadlyPanel deadlyPanel, GameDeadly gameDeadly, CardLayout cl, JPanel gamePanel, Sound sound) {

		this.deadlyPanel = deadlyPanel;
		this.gameDeadly = gameDeadly;
		this.cl = cl;
		this.gamePanel = gamePanel;
		this.sound = sound;

		cardListener();
		changeColorsListener();
		backToMenuListener();
		backHomeListener();
	}



	public void backHomeListener() {
		deadlyPanel.getBackHome().addActionListener(e -> {
			backToMenu();
		});
	}



	public void changeColorsListener() {

		deadlyPanel.getYellowCircle().addActionListener(e -> {

			if(gameDeadly.getTurnIndex()==0) {

				changeColorActions('Y');	
				sound.playSE(6);
			}}
				);

		deadlyPanel.getRedCircle().addActionListener(e -> {

			if(gameDeadly.getTurnIndex()==0) {

				changeColorActions('R');	
				sound.playSE(5);

			}}		
				);

		deadlyPanel.getGreenCircle().addActionListener(e -> {

			if(gameDeadly.getTurnIndex()==0) {

				changeColorActions('G');
				sound.playSE(4);

			}}
				);

		deadlyPanel.getBlueCircle().addActionListener(e -> {

			if(gameDeadly.getTurnIndex()==0) {

				changeColorActions('B');	
				sound.playSE(3);

			}}
				);		
	}



	public void changeColorActions(char c) {

		gameDeadly.setColorAnsw(false);
		deadlyPanel.remove(deadlyPanel.getYellowCircle());
		deadlyPanel.remove(deadlyPanel.getBlueCircle());
		deadlyPanel.remove(deadlyPanel.getRedCircle());
		deadlyPanel.remove(deadlyPanel.getGreenCircle());

		deadlyPanel.repaint();

		if (wildcardORplus4==0)
			gameDeadly.setDiscardedCard(new Card(-1, c, Type.WILDCARD));
		else
			gameDeadly.setDiscardedCard(new Card(-1, c, Type.WILDCARDPLUS4));

		deadlyPanel.setEnabledMethodTrue();
		deadlyPanel.displayCards();

		/*if it's the first cycle of the gameloop and the discarded card is of the wildcard type 
		  then it's up to us to play after choosing the color*/
		if (deadlyPanel.isFirstTimeDiscardedCard()) {

			deadlyPanel.setPickCardBoolean(true);
			deadlyPanel.setFirstTimeDiscardedCard(false); //it will never be the first cycle 
		}
		
		else {

			gameDeadly.passTurn();
			deadlyPanel.setPickCardBoolean(true); //next our turn adds the hand again
		}
	}



	//Card listener
	public void cardListener() {

		for(CardB cardB: deadlyPanel.getMapCardToCardB().values()) {

			//each type of card has a different behavior
			
			if (cardB.getCard().getType().equals(Type.DEFAULT)) {

				cardB.addActionListener(e -> {

					//the player can throw the cards only when it is his turn
					if(gameDeadly.getTurnIndex()==0) {

						removeButtonDefault(cardB); //can the player throw the default type card?
						deadlyPanel.displayCards();
					}
				}
						);
			}

			else if (cardB.getCard().getType().equals(Type.PLUS2)) {

				cardB.addActionListener(e -> {

					//the player can throw the cards only when it is his turn
					if (gameDeadly.getTurnIndex()==0) {

						removeButtonPlus2(cardB); //can the player throw the plus2 type card?
						deadlyPanel.displayCards();	
					}
				});
			}

			else if (cardB.getCard().getType().equals(Type.REVERSE)) {

				cardB.addActionListener(e -> {

					//the player can throw the cards only when it is his turn
					if (gameDeadly.getTurnIndex()==0) {

						removeButtonReverse(cardB); //can the player throw the reverse type card?
						deadlyPanel.displayCards();	
					}
				});
			}

			else if (cardB.getCard().getType().equals(Type.SKIP)) {

				cardB.addActionListener(e -> {

					//the player can throw the cards only when it is his turn
					if (gameDeadly.getTurnIndex()==0) {

						removeButtonSkip(cardB); //can the player throw the skip type card?
						deadlyPanel.displayCards();	
					}
				});			
			}

			else if (cardB.getCard().getType().equals(Type.WILDCARD)) {

				cardB.addActionListener(e -> {

					//the player can throw the cards only when it is his turn
					if (gameDeadly.getTurnIndex()==0) {

						removeButtonWildcard(cardB); //can the player throw the wildcard type card?
						deadlyPanel.displayCards();	
					}
				});			
			}

			else if (cardB.getCard().getType().equals(Type.WILDCARDPLUS4)) {

				cardB.addActionListener(e -> {

					//the player can throw the cards only when it is his turn
					if (gameDeadly.getTurnIndex()==0) {

						removeButtonWildcardPlus4(cardB); //can the player throw the wildcard+4 type card?
						deadlyPanel.displayCards();	
					}
				});		
			}
		}
	}

	
	
	//can the player throw the wildcard+4 type card?
	public void removeButtonWildcardPlus4(CardB cardB) {

		sound.playSE(12);

		//change turn actions wildcard+4
		wildcardORplus4 = 1;

		gameDeadly.getPlayer().getHand().remove(cardB.getCard());
		deadlyPanel.remove(cardB);

		deadlyPanel.displayCards();

		//setenable(false) all hand cards
		List<Card> hand = gameDeadly.getPlayer().getHand();		 

		for(int i = 0; i<deadlyPanel.getHandCardBPlayer().size();i++) 
			deadlyPanel.getHandCardBPlayer().get(i).setEnabled(false);
		
		//add choose color button for the wildcard to deadlyPanel			
		deadlyPanel.add(deadlyPanel.getYellowCircle());
		deadlyPanel.add(deadlyPanel.getBlueCircle());
		deadlyPanel.add(deadlyPanel.getRedCircle());
		deadlyPanel.add(deadlyPanel.getGreenCircle());

		deadlyPanel.repaint();

		gameDeadly.setCounter(gameDeadly.getCounter()+4);
		gameDeadly.setPlayer0ThrewCard(true);	
		gameDeadly.setColorAnsw(true);
	}



	//can the player throw the wildcard type card?
	public void removeButtonWildcard(CardB cardB) {

		//if the player should draw cards he cannot throw the following card
		if (gameDeadly.getCounter() == 0) {

			sound.playSE(12);

			//change turn actions wildcard
			wildcardORplus4 = 0;

			gameDeadly.getPlayer().getHand().remove(cardB.getCard());
			deadlyPanel.remove(cardB);

			deadlyPanel.displayCards();

			//setenable(false) all hand  cards
			List<Card> hand = gameDeadly.getPlayer().getHand();		 
			List<CardB> handB = deadlyPanel.handToHandB(hand);

			for(int i = 0; i<handB.size(); i++) 
				handB.get(i).setEnabled(false);
			

			//add choose color button for the wildcard to deadlyPanel	
			deadlyPanel.add(deadlyPanel.getYellowCircle());
			deadlyPanel.add(deadlyPanel.getBlueCircle());
			deadlyPanel.add(deadlyPanel.getRedCircle());
			deadlyPanel.add(deadlyPanel.getGreenCircle());

			deadlyPanel.repaint();
		}
	}



	//can the player throw the default type card?
	public void removeButtonDefault(CardB cardB) {

		boolean playable = false;

		if(gameDeadly.getDiscardedCard().getType() == Type.DEFAULT && 
				(gameDeadly.getDiscardedCard().getN()==cardB.getCard().getN() || gameDeadly.getDiscardedCard().getC()==cardB.getCard().getC())) {
			playable = true;
		}

		//card +2 vented on the previous player, so you can throw any card as long as it's the same color 
		else if(gameDeadly.getCounter() == 0 && gameDeadly.getDiscardedCard().getC()==cardB.getCard().getC())
			playable = true;

		
		if (playable) {
			
			sound.playSE(12);
			changeTurnActions(cardB);
			gameDeadly.passTurn();	
		}
	}



	//can the player throw the plus2 type card?
	public void removeButtonPlus2(CardB cardB) {

		boolean playable = false;

		//discarded card of type default
		if(gameDeadly.getDiscardedCard().getC()==cardB.getCard().getC() && gameDeadly.getCounter() == 0)
			playable = true;

		//discardedCard of special type, so the player can discard a special card of the same type (doesn't count color and number)
		else if(gameDeadly.getDiscardedCard().getType() == Type.PLUS2 && gameDeadly.getCounter() == 0)
			playable = true;

		if (playable) {

			gameDeadly.setCounter(gameDeadly.getCounter()+2);
			gameDeadly.setPlayer0ThrewCard(true);

			sound.playSE(12);
			changeTurnActions(cardB);
			gameDeadly.passTurn();	
		}
	}



	//can the player throw the reverse type card?
	public void removeButtonReverse(CardB cardB) {

		boolean playable = false;

		//card in the middle vented so the player can throw reverse as long as it has the same color as the one in the middle
		if(gameDeadly.getCounter() == 0 && gameDeadly.getDiscardedCard().getC()==cardB.getCard().getC())
			playable = true;

		//discardedCard of special type, so the player can discard a special card of the same type (doesn't count color and number)
		else if(gameDeadly.getDiscardedCard().getType() == Type.REVERSE)
			playable = true;

		if (playable) {

			gameDeadly.setClockwise(!gameDeadly.isClockwise());

			sound.playSE(7);
			changeTurnActions(cardB);
			gameDeadly.passTurn();	
		}
	}



	//can the player throw the skip type card?
	public void removeButtonSkip(CardB cardB) {

		boolean playable = false;

		//card in the middle vented so the player can throw reverse as long as it has the same color as the one in the middle
		if(gameDeadly.getCounter() == 0 && gameDeadly.getDiscardedCard().getC()==cardB.getCard().getC())
			playable = true;

		//discardedCard of special type, so the player can discard a special card of the same type (doesn't count color and number)
		else if(gameDeadly.getDiscardedCard().getType() == Type.SKIP)
			playable = true;

		if(playable) {
			
			sound.playSE(8);
			changeTurnActions(cardB);
			gameDeadly.passTurnSkip();	
		}
	}


	
	public void backToMenuListener() {

		deadlyPanel.getBackToMenu().addActionListener(e -> {

			backToMenu();
		});
	}

	
	
	public void backToMenu() {

		gameDeadly.getGameThread().stop();

		//remove all the components from the panel except the popUpEndGame with the name of the winner of the game and the backToMenu button
		for(Component comp : deadlyPanel.getComponents()) 
			deadlyPanel.remove(comp);

		cl.show(gamePanel, "1");		

		deadlyPanel.remove(deadlyPanel.getWinnerLab());
		deadlyPanel.remove(deadlyPanel.getPopUpEndGame());
		deadlyPanel.remove(deadlyPanel.getBackToMenu());

		gameDeadly.initializeVariables();

		deadlyPanel.getTimer().stop();

		deadlyPanel.reset();
		reset();

		sound.stop();

		if(!sound.getStatus().equals("PAUSE"))
			sound.playMusic(14);
	}



	public void reset() {

		wildcardORplus4 = 0;
		cardListener();
	}



	public void changeTurnActions(CardB cardB) {
		gameDeadly.setDiscardedCard(cardB.getCard());
		gameDeadly.getPlayer().getHand().remove(cardB.getCard());

		deadlyPanel.repaint();

		deadlyPanel.setEnabledMethodTrue();
		deadlyPanel.setPickCardBoolean(true);
	}
}