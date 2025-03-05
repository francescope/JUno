package controller;

import java.awt.CardLayout;
import java.awt.Component;
import java.util.List;
import java.util.Random;
import javax.swing.JPanel;
import model.Card;
import model.Type;
import model.classic.GameClassic;
import model.sound.Sound;
import view.CardB;
import view.ClassicPanel;

public class ListenerClassic {

	private ClassicPanel classicPanel;
	private GameClassic gameClassic;
	private CardLayout cl;
	private JPanel gamePanel;
	private Sound sound;
	private int wildcardORplus4;
	
	
	
	public ListenerClassic(ClassicPanel classicPanel, GameClassic gameClassic, CardLayout cl, JPanel gamePanel, Sound sound) {

		this.classicPanel = classicPanel;
		this.gameClassic = gameClassic;
		this.cl = cl;
		this.gamePanel = gamePanel;
		this.sound = sound;

		cardListener();
		pickCardListener();
		passButton();
		changeColorsListener();
		popUpListener();
		unoButton();
		backToMenuListener();
		backHomeListener();
	}



	public void backHomeListener() {
		classicPanel.getBackHome().addActionListener(e -> {
			backToMenu();
		});

	}



	public void popUpListener() {

		classicPanel.getYesPopUp().addActionListener(e -> {

			gameClassic.setPopUpAnsw("yes");
		});

		classicPanel.getNoPopUp().addActionListener(e -> {

			gameClassic.setPopUpAnsw("no");
		});
	}

	

	//pickCardButton listener
	public void pickCardListener() {
		
		classicPanel.getPickCardButton().addActionListener(e -> {

			if(gameClassic.getTurnIndex()==0) {

				sound.playSE(12);

				Random rand = new Random();
				int size = gameClassic.getDeck().size();
				Card card = gameClassic.getDeck().get(rand.nextInt(size));
				gameClassic.getPlayer().getHand().add(card);
				gameClassic.getDeck().remove(card); 

				//passButton
				classicPanel.remove(classicPanel.getPickCardButton());
				classicPanel.remove(classicPanel.getUnoButton());
				classicPanel.repaint();

				classicPanel.add(classicPanel.getPassButton());
				classicPanel.repaint();

				classicPanel.setEnabledMethodFalse();
			}
		});
	}



	public void passButton() {		

		classicPanel.getPassButton().addActionListener(e -> {

			classicPanel.remove(classicPanel.getPassButton());

			classicPanel.repaint();
			classicPanel.setEnabledMethodTrue();
			classicPanel.repaint();
			gameClassic.passTurn();
			classicPanel.setPickCardBoolean(true);
		});
	}

	

	public void unoButton() {

		classicPanel.getUnoButton().addActionListener(e -> {

			if(gameClassic.getPlayer().getHand().size()==2 && gameClassic.getTurnIndex()==0) {

				sound.playSE(11);
				gameClassic.setUnoButtonClicked(true);
				classicPanel.remove(classicPanel.getUnoButton());
			}
		});
	}


	
	public void changeColorsListener() {

		classicPanel.getYellowCircle().addActionListener(e -> {

			if(gameClassic.getTurnIndex()==0) {

				changeColorActions('Y');		
				sound.playSE(6);

			}}
				);

		classicPanel.getRedCircle().addActionListener(e -> {

			if(gameClassic.getTurnIndex()==0) {

				changeColorActions('R');
				sound.playSE(5);

			}}
				);

		classicPanel.getGreenCircle().addActionListener(e -> {

			if(gameClassic.getTurnIndex()==0) {

				changeColorActions('G');	
				sound.playSE(4);

			}}
				);

		classicPanel.getBlueCircle().addActionListener(e -> {

			if(gameClassic.getTurnIndex()==0) {

				changeColorActions('B');	
				sound.playSE(3);

			}}
				);		
	}



	public void changeColorActions(char c) {

		gameClassic.setColorAnsw(false);
		classicPanel.remove(classicPanel.getYellowCircle());
		classicPanel.remove(classicPanel.getBlueCircle());
		classicPanel.remove(classicPanel.getRedCircle());
		classicPanel.remove(classicPanel.getGreenCircle());

		classicPanel.repaint();

		if (wildcardORplus4==0)
			gameClassic.setDiscardedCard(new Card(-1, c, Type.WILDCARD));
		else
			gameClassic.setDiscardedCard(new Card(-1, c, Type.WILDCARDPLUS4));

		classicPanel.setEnabledMethodTrue();
		classicPanel.displayCards();

		/*if it's the first cycle of the gameloop and the discarded card is of the wildcard type 
		  then it's up to us to play after choosing the color*/
		if (classicPanel.isFirstTimeDiscardedCard()) {

			classicPanel.setPickCardBoolean(true);
			classicPanel.setFirstTimeDiscardedCard(false); //it will never be the first cycle
		}
		
		else {

			gameClassic.passTurn();
			classicPanel.setPickCardBoolean(true); //next our turn adds the hand again
		}
	}



	//Card listener
	public void cardListener() {

		for(CardB cardB: classicPanel.getMapCardToCardB().values()) {

			//each type of card has a different behavior
			
			if (cardB.getCard().getType().equals(Type.DEFAULT)) {

				cardB.addActionListener(e -> {

					//the player can throw the cards only when it is his turn
					if(gameClassic.getTurnIndex()==0) { 

						removeButtonDefault(cardB); //can the player throw the default type card?
						classicPanel.displayCards();
					}
				}
						);
			}

			else if (cardB.getCard().getType().equals(Type.PLUS2)) {

				cardB.addActionListener(e -> {

					//the player can throw the cards only when it is his turn
					if (gameClassic.getTurnIndex()==0) {

						removeButtonPlus2(cardB); //can the player throw the plus2 type card?
						classicPanel.displayCards();	
					}
				});
			}

			else if (cardB.getCard().getType().equals(Type.REVERSE)) {

				cardB.addActionListener(e -> {

					//the player can throw the cards only when it is his turn
					if (gameClassic.getTurnIndex()==0) {

						removeButtonReverse(cardB); //can the player throw the reverse type card?
						classicPanel.displayCards();	
					}
				});
			}

			else if (cardB.getCard().getType().equals(Type.SKIP)) {

				cardB.addActionListener(e -> {

					//the player can throw the cards only when it is his turn
					if (gameClassic.getTurnIndex()==0) {

						removeButtonSkip(cardB); //can the player throw the skip type card?
						classicPanel.displayCards();	
					}
				});			
			}

			else if (cardB.getCard().getType().equals(Type.WILDCARD)) {

				cardB.addActionListener(e -> {

					//the player can throw the cards only when it is his turn
					if (gameClassic.getTurnIndex()==0) {

						removeButtonWildcard(cardB); //can the player throw the wildcard type card?
						classicPanel.displayCards();	
					}
				});			
			}

			else if (cardB.getCard().getType().equals(Type.WILDCARDPLUS4)) {

				cardB.addActionListener(e -> {
					
					//the player can throw the cards only when it is his turn
					if (gameClassic.getTurnIndex()==0) {

						removeButtonWildcardPlus4(cardB); //can the player throw the wildcard+4 type card?
						classicPanel.displayCards();	
					}
				});		
			}
		}
	}

	
	
	//can the player throw the wildcard+4 type card?
	public void removeButtonWildcardPlus4(CardB cardB) {

		//if the player should draw cards he cannot throw the following card
		if (gameClassic.getCounter() == 0) {

			sound.playSE(12);

			//change turn actions wildcard+4
			wildcardORplus4 = 1;

			classicPanel.remove(classicPanel.getPickCardButton());
			classicPanel.remove(classicPanel.getUnoButton());

			gameClassic.getPlayer().getHand().remove(cardB.getCard());
			classicPanel.remove(cardB);

			classicPanel.displayCards();

			//setenable(false) all hand cards
			List<Card> hand = gameClassic.getPlayer().getHand();		 

			for(int i=0; i<classicPanel.getHandCardBPlayer().size(); i++) 
				classicPanel.getHandCardBPlayer().get(i).setEnabled(false);
			
			classicPanel.remove(classicPanel.getPassButton());

			//add choose color button for the wildcard to classicPanel			
			classicPanel.add(classicPanel.getYellowCircle());
			classicPanel.add(classicPanel.getBlueCircle());
			classicPanel.add(classicPanel.getRedCircle());
			classicPanel.add(classicPanel.getGreenCircle());

			classicPanel.repaint();

			gameClassic.setCounter(4);
			gameClassic.setPlayer0ThrewCard(true);

			gameClassic.setColorAnsw(true);

			//could the player play the +4 card?
			gameClassic.setPlayableWildCardPlus4(true);
			hand.forEach(card -> {
				if(gameClassic.getDiscardedCard().getC() == card.getC()) 
					gameClassic.setPlayableWildCardPlus4(false);
			});		
		}
	}



	//can the player throw the wildcard type card?
	public void removeButtonWildcard(CardB cardB) {

		//if the player should draw cards he cannot throw the following card
		if (gameClassic.getCounter() == 0) {

			sound.playSE(12);

			//change turn actions wildcard
			wildcardORplus4 = 0;

			classicPanel.remove(classicPanel.getPickCardButton());
			classicPanel.remove(classicPanel.getUnoButton());

			gameClassic.getPlayer().getHand().remove(cardB.getCard());
			classicPanel.remove(cardB);

			classicPanel.displayCards();

			//setenable(false) all hand  cards
			List<Card> hand = gameClassic.getPlayer().getHand();		 
			List<CardB> handB = classicPanel.handToHandB(hand);

			for(int i = 0; i<handB.size();i++) 
				handB.get(i).setEnabled(false);

			classicPanel.remove(classicPanel.getPassButton());

			//add choose color button for the wildcard to classicPanel				
			classicPanel.add(classicPanel.getYellowCircle());
			classicPanel.add(classicPanel.getBlueCircle());
			classicPanel.add(classicPanel.getRedCircle());
			classicPanel.add(classicPanel.getGreenCircle());

			classicPanel.repaint();
		}
	}



	//can the player throw the default type card?
	public void removeButtonDefault(CardB cardB) {

		boolean playable = false;

		if(gameClassic.getDiscardedCard().getType() == Type.DEFAULT && 
				(gameClassic.getDiscardedCard().getN()==cardB.getCard().getN() || gameClassic.getDiscardedCard().getC()==cardB.getCard().getC())) {
			playable = true;
		}

		//card +2 vented on the previous player, so you can throw any card as long as it's the same color
		else if(gameClassic.getCounter() == 0 && gameClassic.getDiscardedCard().getC()==cardB.getCard().getC())
			playable = true;

		if (playable) {

			sound.playSE(12);
			changeTurnActions(cardB);
			gameClassic.passTurn();	
		}
	}



	//can the player throw the plus2 type card?
	public void removeButtonPlus2(CardB cardB) {

		boolean playable = false;

		//discarded card of type default
		if(gameClassic.getDiscardedCard().getC()==cardB.getCard().getC() && gameClassic.getCounter() == 0)
			playable = true;

		//discardedCard of special type, so the player can discard a special card of the same type (doesn't count color and number)
		else if(gameClassic.getDiscardedCard().getType() == Type.PLUS2 && gameClassic.getCounter() == 0)
			playable = true;

		if (playable) {

			gameClassic.setCounter(2);
			gameClassic.setPlayer0ThrewCard(true);

			changeTurnActions(cardB);
			gameClassic.passTurn();	

			sound.playSE(12);
		}
	}



	//can the player throw the reverse type card?
	public void removeButtonReverse(CardB cardB) {

		boolean playable = false;

		//card in the middle vented so the player can throw reverse as long as it has the same color as the one in the middle
		if(gameClassic.getCounter() == 0 && gameClassic.getDiscardedCard().getC()==cardB.getCard().getC())
			playable = true;

		//discardedCard of special type, so the player can discard a special card of the same type (doesn't count color and number)
		else if(gameClassic.getDiscardedCard().getType() == Type.REVERSE)
			playable = true;

		if (playable) {

			gameClassic.setClockwise(!gameClassic.isClockwise());

			changeTurnActions(cardB);
			gameClassic.passTurn();	
			sound.playSE(7);
		}
	}


	
	//can the player throw the skip type card?
	public void removeButtonSkip(CardB cardB) {

		boolean playable = false;

		//card in the middle vented so the player can throw reverse as long as it has the same color as the one in the middle
		if(gameClassic.getCounter() == 0 && gameClassic.getDiscardedCard().getC()==cardB.getCard().getC())
			playable = true;

		//discardedCard of special type, so the player can discard a special card of the same type (doesn't count color and number)
		else if(gameClassic.getDiscardedCard().getType() == Type.SKIP)
			playable = true;

		if(playable) {
			changeTurnActions(cardB);
			gameClassic.passTurnSkip();	
			sound.playSE(8);
		}
	}


	
	public void backToMenuListener() {

		classicPanel.getBackToMenu().addActionListener(e -> {

			backToMenu();
		});
	}



	public void backToMenu() {

		gameClassic.getGameThread().stop();

		//remove all the components from the panel except the popUpEndGame with the name of the winner of the game and the backToMenu button
		for(Component comp : classicPanel.getComponents()) 
			classicPanel.remove(comp);
		
		cl.show(gamePanel, "1");		

		classicPanel.remove(classicPanel.getWinnerLab());
		classicPanel.remove(classicPanel.getPopUpEndGame());
		classicPanel.remove(classicPanel.getBackToMenu());

		gameClassic.initializeVariables();

		classicPanel.getTimer().stop();


		classicPanel.reset();
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
		
		gameClassic.setDiscardedCard(cardB.getCard());
		gameClassic.getPlayer().getHand().remove(cardB.getCard());

		classicPanel.remove(classicPanel.getPickCardButton());
		classicPanel.remove(classicPanel.getUnoButton());
		classicPanel.remove(classicPanel.getPassButton());
		classicPanel.repaint();

		classicPanel.setEnabledMethodTrue();
		classicPanel.setPickCardBoolean(true);
	}
}