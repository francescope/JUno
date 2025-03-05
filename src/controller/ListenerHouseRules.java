package controller;

import java.awt.CardLayout;
import java.awt.Component;
import java.util.List;
import java.util.Random;
import javax.swing.JPanel;
import model.Card;
import model.Type;
import model.house_rules.GameHouseRules;
import model.sound.Sound;
import view.CardB;
import view.HouseRulesPanel;


public class ListenerHouseRules {

	private HouseRulesPanel houseRulesPanel;
	private GameHouseRules gameHouseRules;
	private CardLayout cl;
	private JPanel gamePanel;
	private Sound sound;
	private int wildcardORplus4;
	
	
	public ListenerHouseRules(HouseRulesPanel houseRulesPanel, GameHouseRules gameHouseRules, CardLayout cl, JPanel gamePanel, Sound sound) {

		this.houseRulesPanel = houseRulesPanel;
		this.gameHouseRules = gameHouseRules;
		this.cl = cl;
		this.gamePanel = gamePanel;
		this.sound = sound;

		cardListener();
		pickCardListener();
		passButton();
		changeColorsListener();
		unoButton();
		backToMenuListener();
		backHomeListener();
	}



	public void backHomeListener() {
		houseRulesPanel.getBackHome().addActionListener(e -> {
			backToMenu();
		});
	}

	
	
	//pickCardButton listener
	public void pickCardListener() {
		
		houseRulesPanel.getPickCardButton().addActionListener(e -> {

			if(gameHouseRules.getTurnIndex()==0) {

				sound.playSE(12);

				int size = gameHouseRules.getDeck().size();
				Random rand = new Random();
				Card card = gameHouseRules.getDeck().get(rand.nextInt(size));
				gameHouseRules.getPlayer().getHand().add(card);
				gameHouseRules.getDeck().remove(card); 

				//passButton
				houseRulesPanel.remove(houseRulesPanel.getPickCardButton());
				houseRulesPanel.remove(houseRulesPanel.getUnoButton());
				houseRulesPanel.repaint();

				houseRulesPanel.add(houseRulesPanel.getPassButton());
				houseRulesPanel.repaint();

				houseRulesPanel.setEnabledMethodFalse();
			}
		});
	}



	public void passButton() {		

		houseRulesPanel.getPassButton().addActionListener(e -> {

			houseRulesPanel.remove(houseRulesPanel.getPassButton());

			houseRulesPanel.repaint();
			houseRulesPanel.setEnabledMethodTrue();
			houseRulesPanel.repaint();
			gameHouseRules.passTurn();
			houseRulesPanel.setPickCardBoolean(true);
		});
	}

	

	public void unoButton() {

		houseRulesPanel.getUnoButton().addActionListener(e -> {

			if(gameHouseRules.getPlayer().getHand().size()==2 && gameHouseRules.getTurnIndex()==0) {

				sound.playSE(11);

				gameHouseRules.setUnoButtonClicked(true);
				houseRulesPanel.remove(houseRulesPanel.getUnoButton());
			}
		});
	}


	public void changeColorsListener() {

		houseRulesPanel.getYellowCircle().addActionListener(e -> {

			if(gameHouseRules.getTurnIndex()==0) {

				changeColorActions('Y');	
				sound.playSE(6);
			}}
				);

		houseRulesPanel.getRedCircle().addActionListener(e -> {

			if(gameHouseRules.getTurnIndex()==0) {

				changeColorActions('R');	
				sound.playSE(5);

			}}
				);

		houseRulesPanel.getGreenCircle().addActionListener(e -> {

			if(gameHouseRules.getTurnIndex()==0) {

				changeColorActions('G');
				sound.playSE(4);

			}}
				);

		houseRulesPanel.getBlueCircle().addActionListener(e -> {

			if(gameHouseRules.getTurnIndex()==0) {

				changeColorActions('B');	
				sound.playSE(3);

			}}
				);		
	}



	public void changeColorActions(char c) {

		gameHouseRules.setColorAnsw(false);
		houseRulesPanel.remove(houseRulesPanel.getYellowCircle());
		houseRulesPanel.remove(houseRulesPanel.getBlueCircle());
		houseRulesPanel.remove(houseRulesPanel.getRedCircle());
		houseRulesPanel.remove(houseRulesPanel.getGreenCircle());

		houseRulesPanel.repaint();

		if (wildcardORplus4==0)
			gameHouseRules.setDiscardedCard(new Card(-1, c, Type.WILDCARD));
		else
			gameHouseRules.setDiscardedCard(new Card(-1, c, Type.WILDCARDPLUS4));

		houseRulesPanel.setEnabledMethodTrue();
		houseRulesPanel.displayCards();

		/*if it's the first cycle of the gameloop and the discarded card is of the wildcard type 
		  then it's up to us to play after choosing the color*/
		if (houseRulesPanel.isFirstTimeDiscardedCard()) {

			houseRulesPanel.setPickCardBoolean(true);
			houseRulesPanel.setFirstTimeDiscardedCard(false); //it will never be the first cycle
		}
		
		else {

			gameHouseRules.passTurn();
			houseRulesPanel.setPickCardBoolean(true); //next our turn adds the hand again
		}
	}



	//Card listener
	public void cardListener() {

		for(CardB cardB: houseRulesPanel.getMapCardToCardB().values()) {
			
			//each type of card has a different behavior

			if (cardB.getCard().getType().equals(Type.DEFAULT)) {

				cardB.addActionListener(e -> {

					//the player can throw the cards only when it is his turn
					if(gameHouseRules.getTurnIndex()==0) {

						removeButtonDefault(cardB); //can the player throw the default type card?
						houseRulesPanel.displayCards();
					}
				}
						);
			}

			else if (cardB.getCard().getType().equals(Type.PLUS2)) {

				cardB.addActionListener(e -> {

					//the player can throw the cards only when it is his turn
					if (gameHouseRules.getTurnIndex()==0) {

						removeButtonPlus2(cardB); //can the player throw the plus2 type card?
						houseRulesPanel.displayCards();	
					}
				});
			}

			else if (cardB.getCard().getType().equals(Type.REVERSE)) {

				cardB.addActionListener(e -> {

					//the player can throw the cards only when it is his turn
					if (gameHouseRules.getTurnIndex()==0) {

						removeButtonReverse(cardB); //can the player throw the reverse type card?
						houseRulesPanel.displayCards();	
					}
				});
			}

			else if (cardB.getCard().getType().equals(Type.SKIP)) {

				cardB.addActionListener(e -> {

					//the player can throw the cards only when it is his turn
					if (gameHouseRules.getTurnIndex()==0) {

						removeButtonSkip(cardB); //can the player throw the skip type card?
						houseRulesPanel.displayCards();	
					}
				});			
			}

			else if (cardB.getCard().getType().equals(Type.WILDCARD)) {

				cardB.addActionListener(e -> {

					//the player can throw the cards only when it is his turn
					if (gameHouseRules.getTurnIndex()==0) {

						removeButtonWildcard(cardB); //can the player throw the wildcard type card?
						houseRulesPanel.displayCards();	
					}
				});			
			}



			else if (cardB.getCard().getType().equals(Type.WILDCARDPLUS4)) {

				cardB.addActionListener(e -> {

					//the player can throw the cards only when it is his turn
					if (gameHouseRules.getTurnIndex()==0) {

						removeButtonWildcardPlus4(cardB); //can the player throw the wildcard+4 type card?
						houseRulesPanel.displayCards();	
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

		houseRulesPanel.remove(houseRulesPanel.getPickCardButton());
		houseRulesPanel.remove(houseRulesPanel.getUnoButton());

		gameHouseRules.getPlayer().getHand().remove(cardB.getCard());
		houseRulesPanel.remove(cardB);

		houseRulesPanel.displayCards();

		//setenable(false) all hand card
		for(int i = 0; i<houseRulesPanel.getHandCardBPlayer().size();i++) 
			houseRulesPanel.getHandCardBPlayer().get(i).setEnabled(false);			
	
		houseRulesPanel.remove(houseRulesPanel.getPassButton());

		//add choose color button for the wildcard to houseRulesPanel			
		houseRulesPanel.add(houseRulesPanel.getYellowCircle());
		houseRulesPanel.add(houseRulesPanel.getBlueCircle());
		houseRulesPanel.add(houseRulesPanel.getRedCircle());
		houseRulesPanel.add(houseRulesPanel.getGreenCircle());

		houseRulesPanel.repaint();

		gameHouseRules.setCounter(gameHouseRules.getCounter()+4);
		gameHouseRules.setPlayer0ThrewCard(true);	

		gameHouseRules.setColorAnsw(true);
	}

	

	//can the player throw the wildcard type card?
	public void removeButtonWildcard(CardB cardB) {

		//if the player should draw cards he cannot throw the following card
		if (gameHouseRules.getCounter() == 0) {

			sound.playSE(12);
			
			//change turn actions wildcard
			wildcardORplus4 = 0;

			houseRulesPanel.remove(houseRulesPanel.getPickCardButton());
			houseRulesPanel.remove(houseRulesPanel.getUnoButton());

			gameHouseRules.getPlayer().getHand().remove(cardB.getCard());
			houseRulesPanel.remove(cardB);

			houseRulesPanel.displayCards();

			//setenable(false) all hand  cards
			List<Card> hand = gameHouseRules.getPlayer().getHand();		 
			List<CardB> handB = houseRulesPanel.handToHandB(hand);

			for(int i = 0; i<handB.size();i++) 
				handB.get(i).setEnabled(false);
			
			houseRulesPanel.remove(houseRulesPanel.getPassButton());

			//add choose color button for the wildcard to classicPanel				
			houseRulesPanel.add(houseRulesPanel.getYellowCircle());
			houseRulesPanel.add(houseRulesPanel.getBlueCircle());
			houseRulesPanel.add(houseRulesPanel.getRedCircle());
			houseRulesPanel.add(houseRulesPanel.getGreenCircle());

			houseRulesPanel.repaint();
		}
	}



	//can the player throw the default type card?
	public void removeButtonDefault(CardB cardB) {

		boolean playable = false;

		if(gameHouseRules.getDiscardedCard().getType() == Type.DEFAULT && 
				(gameHouseRules.getDiscardedCard().getN()==cardB.getCard().getN() || gameHouseRules.getDiscardedCard().getC()==cardB.getCard().getC())) {
			playable = true;
		}

		//card +2 vented on the previous player, so you can throw any card as long as it's the same color
		else if(gameHouseRules.getCounter() == 0 && gameHouseRules.getDiscardedCard().getC()==cardB.getCard().getC())
			playable = true;

		if (playable) {

			sound.playSE(12);
			changeTurnActions(cardB);
			gameHouseRules.passTurn();	
		}
	}



	//can the player throw the plus2 type card?
	public void removeButtonPlus2(CardB cardB) {

		boolean playable = false;

		//discarded card of type default
		if(gameHouseRules.getDiscardedCard().getC()==cardB.getCard().getC() && gameHouseRules.getCounter() == 0)
			playable = true;

		//discardedCard of special type, so the player can discard a special card of the same type (doesn't count color and number)
		else if(gameHouseRules.getDiscardedCard().getType() == Type.PLUS2)
			playable = true;

		if (playable) {

			sound.playSE(12);

			gameHouseRules.setCounter(gameHouseRules.getCounter()+2);
			gameHouseRules.setPlayer0ThrewCard(true);

			changeTurnActions(cardB);
			gameHouseRules.passTurn();	
		}
	}




	//can the player throw the reverse type card?
	public void removeButtonReverse(CardB cardB) {

		boolean playable = false;

		//card in the middle vented so the player can throw reverse as long as it has the same color as the one in the middle
		if(gameHouseRules.getCounter() == 0 && gameHouseRules.getDiscardedCard().getC()==cardB.getCard().getC())
			playable = true;

		//discardedCard of special type, so the player can discard a special card of the same type (doesn't count color and number)
		else if(gameHouseRules.getDiscardedCard().getType() == Type.REVERSE)
			playable = true;

		if (playable) {

			sound.playSE(7);
			gameHouseRules.setClockwise(!gameHouseRules.isClockwise());
			changeTurnActions(cardB);
			gameHouseRules.passTurn();	
		}
	}


	
	//can the player throw the skip type card?
	public void removeButtonSkip(CardB cardB) {

		boolean playable = false;

		//card in the middle vented so the player can throw reverse as long as it has the same color as the one in the middle
		if(gameHouseRules.getCounter() == 0 && gameHouseRules.getDiscardedCard().getC()==cardB.getCard().getC())
			playable = true;

		//discardedCard of special type, so the player can discard a special card of the same type (doesn't count color and number)
		else if(gameHouseRules.getDiscardedCard().getType() == Type.SKIP)
			playable = true;

		if(playable) {

			sound.playSE(8);
			changeTurnActions(cardB);
			gameHouseRules.passTurnSkip();	
		}
	}


	
	public void backToMenuListener() {

		houseRulesPanel.getBackToMenu().addActionListener(e -> {

			backToMenu();
		});
	}

	
	
	public void backToMenu() {
		
		gameHouseRules.getGameThread().stop();
		
		//remove all the components from the panel except the popUpEndGame with the name of the winner of the game and the backToMenu button
		for(Component comp : houseRulesPanel.getComponents()) 
			houseRulesPanel.remove(comp);
		
		cl.show(gamePanel, "1");		

		houseRulesPanel.remove(houseRulesPanel.getWinnerLab());
		houseRulesPanel.remove(houseRulesPanel.getPopUpEndGame());
		houseRulesPanel.remove(houseRulesPanel.getBackToMenu());

		gameHouseRules.initializeVariables();
		
		houseRulesPanel.getTimer().stop();
		
		
		houseRulesPanel.reset();
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
		
		gameHouseRules.setDiscardedCard(cardB.getCard());
		gameHouseRules.getPlayer().getHand().remove(cardB.getCard());


		houseRulesPanel.remove(houseRulesPanel.getPickCardButton());
		houseRulesPanel.remove(houseRulesPanel.getUnoButton());
		houseRulesPanel.remove(houseRulesPanel.getPassButton());
		houseRulesPanel.repaint();

		houseRulesPanel.setEnabledMethodTrue();
		houseRulesPanel.setPickCardBoolean(true);
	}
}