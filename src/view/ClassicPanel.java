package view;

import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;
import java.util.Observable;
import java.util.Observer;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import model.Card;
import model.Type;
import model.UtilityMethods;
import model.classic.GameClassic;

/**
 * graphical representation of the state of the game in classic mode. 
 * receives game updates via observer pattern.
 * 
 * contains the update method which, depending on the state of the game, manages the graphic components.
 * @author MP
 *
 */
public class ClassicPanel extends Panel {

	private GameClassic gameClassic;

	private BufferedImage backgroundPlay = UtilityMethods.loadImg("res/Uno_tavolo_sfondo.png");

	//pick card from the deck button 
	private JButton pickCardButton;

	//pass turn button
	private JButton passButton;

	//did the opponent play the +4 card correctly?
	private JLabel popUp = new JLabel(new ImageIcon("res/challenging_popup.png"));
	private JButton yesPopUp;
	private JButton noPopUp;

	//uno button
	private JButton unoButton;

	//back to menu endgame button
	private JButton backToMenu;

	private JLabel winnerLab = new JLabel();
	

	



	/**
	 * it creates a panel with the default components
	 * @param gameClassic the gameClassic instance
	 */
	public ClassicPanel(GameClassic gameClassic) {

		super(gameClassic);
		this.gameClassic = gameClassic;
		
		//cards left in the draw pile
		cardsRemained.setText(gameClassic.getDeck().size()+"");
		cardsRemained.setBounds(587, 265, 66, 86);
		cardsRemained.setForeground(new Color(255, 255, 255));
		cardsRemained.setFont(new Font("Tahoma", Font.PLAIN, 15));
		
		add(cardsRemained);
		add(pickupDeck);

		//button to draw a card from the deck
		pickCardButton = new JButton(new ImageIcon("res/Hand.png"));
		pickCardButton.setBounds(580, 375, 64, 84);
		UtilityMethods.backgroundTrasparent(pickCardButton, new ImageIcon("res/Hand_hov.png"));

		//button to pass the turn after drawing an unplayable card
		passButton = new JButton(new ImageIcon("res/pass.png"));
		passButton.setBounds(572, 385, 80, 30);
		UtilityMethods.backgroundTrasparent(passButton, new ImageIcon("res/passHov.png"));

		//back to menu endgame button
		backToMenu = new JButton(new ImageIcon("res/back_to_menu.png"));
		backToMenu.setBounds(580, 400, 150, 50);
		UtilityMethods.backgroundTrasparent(backToMenu, new ImageIcon("res/back_to_menu_hov.png"));
		backToMenu.setBounds(580, 400, 150, 50);

		//uno button
		unoButton = new JButton(new ImageIcon("res/uno_button.png"));
		unoButton.setBounds(660, 382, 90, 70);
		UtilityMethods.backgroundTrasparent(unoButton, new ImageIcon("res/uno_button_hov.png"));
		add(unoButton);

		//popup challenging +4 (did the opponent play the +4 card correctly?)
		yesPopUp = new JButton(new ImageIcon("res/yes_popup.png"));
		yesPopUp.setBounds(530, 440, 90, 30);
		UtilityMethods.backgroundTrasparent(yesPopUp,  new ImageIcon("res/yes_popup_hov.png"));

		noPopUp = new JButton(new ImageIcon("res/no_popup.png"));
		noPopUp.setBounds(647, 440, 90, 30);
		UtilityMethods.backgroundTrasparent(noPopUp,  new ImageIcon("res/no_popup_hov.png"));

		popUp.setBounds(497, 390, 270, 100);

		backHome.setIcon(new ImageIcon("res/backHome.png"));
		UtilityMethods.backgroundTrasparent(backHome,  new ImageIcon("res/backHomeHov.png"));

		initHandsBplayers();

		//the initial discarded card (decided by the game) is a wildcard?
		if(gameClassic.getDiscardedCard().getType() == Type.WILDCARD) {

			firstTimeDiscardedCard = true;
			startWildDiscarded();
		}
	}


	
	/*
	 * it initializes players' hands, AI covered cards (JLabel), Player face up cards (JButton)
	 */
	public void initHandsBplayers () {

		for(int i=0; i<7; i++) {

			//the cards of the artificial players are labels (face down)
			handLabelA1.add(new JLabel(new ImageIcon("Cards/uno_back_60x80_right.png")));
			handLabelA2.add(new JLabel(new ImageIcon("Cards/uno_back_60x80_straight.png")));
			handLabelA3.add(new JLabel(new ImageIcon("Cards/uno_back_60x80_left.png")));	

			//Player cards in hand are buttons
			handCardBPlayer.add(mapCardToCardB.get(gameClassic.getPlayer().getHand().get(i)));
		}
		//adds to panel
		handLabelA1.forEach(lab -> add(lab));
		handLabelA2.forEach(lab -> add(lab));
		handLabelA3.forEach(lab -> add(lab));
		handCardBPlayer.forEach(cardB -> add(cardB));
	}

	

	/**
	 * if the first discarded card is a WildCard the cards are disabled and shows the choosable colors by
	 * the player
	 */
	public void startWildDiscarded() {

		//setEnable(false) all hand cards
		for(int i = 0; i<handCardBPlayer.size();i++) 
			handCardBPlayer.get(i).setEnabled(false);

		//add choose color button for the wildcard to panel
		add(yellowCircle);
		add(blueCircle);
		add(redCircle);
		add(greenCircle);

		remove(unoButton);
		pickCardBoolean = false;
		repaint();
	}

	

	/**
	 * Depending on the state of the game, manages the graphic components.
	 * - shows the end game window when the game is finished
	 * - updates the discardedCard, the drawCards counter.
	 * - displays visually whose turn it is and based on it if is players' turn shows pickCardButton and unobutton.
	 * - updates the cards in the hands of the players  
	 * - when a playerAI has only one card it displays the UNO! (JLabel)
	 */
	@Override
	public void update(Observable o, Object arg) {

		cardsRemained.setText(gameClassic.getDeck().size()+"");

		//the match is over?
		if(gameClassic.getGameStatus().equals("FINISHED")) {

			winnerLab.setText(gameClassic.getWinner() + " WON THE GAME!");
			winnerLab.setBounds(497, 300, 350, 50);
			winnerLab.setFont(new Font("Tahoma", Font.PLAIN, 23));
			winnerLab.setForeground(new Color(255,0,0));

			timer.stop();
			
			//remove all the components from the panel except the popUpEndGame with the name of the winner of the game and the backToMenu button
			for(Component comp : getComponents()) 
				remove(comp);

			add(winnerLab);
			add(backToMenu);
			add(popUpEndGame);

			repaint();
		}

		else {

			discardedCardToDiscardedCardLab();

			if (gameClassic.getCounter() != 0) {

				drawCardsLabel.setText("+"+gameClassic.getCounter());
				add(drawCardsLabel);
			}


			displayCards();

			if (gameClassic.getDrawCardsBoolean() && gameClassic.getCounter()==0) {

				remove(drawCardsLabel);
				repaint();
				gameClassic.setDrawCardsBoolean(false);
			}

			//PLAYER'S TURN
			if (gameClassic.getTurnIndex() == 0) {

				elon.setIcon(new ImageIcon("res/label_player_elonmusk1.png"));
				leclerc.setIcon(new ImageIcon("res/label_player_charlesleclerc1.png")); 
				messi.setIcon(new ImageIcon("res/label_player_lionelmessi1.png")); 


				if(firstTimeAddingRect) {
					add(rect);
					if(gameClassic.getPlayer().getHand().size()>15) {
						add(rect2);
					}
					firstTimeAddingRect = false;
				}

				while(gameClassic.isColorAnsw()) {

					try {
						Thread.sleep(1);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}

				gameClassic.setColorAnsw(false);

				if(pickCardBoolean && gameClassic.getCounter()==0) {

					add(pickCardButton);
					add(unoButton);
					repaint();
					pickCardBoolean = false;
				}

				//if it wasn't the player who threw the +4 card or +2 card, but the AI
				if(!gameClassic.player0ThrewCard() && gameClassic.getDiscardedCard().getType() == Type.WILDCARDPLUS4 && gameClassic.getCounter() == 4) {

					remove(pickCardButton);
					remove(unoButton);

					add(yesPopUp);
					add(noPopUp);
					add(popUp);

					repaint();

					//setEnable(false) all hand cards
					List<Card> hand = gameClassic.getPlayer().getHand();		 
					List<CardB> handB = handToHandB(hand);

					for(int i = 0; i<handB.size();i++) {
						handB.get(i).setEnabled(false);
					}	


					while(gameClassic.getPopUpAnsw().equals("")) {	

						try {
							Thread.sleep(1);
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
					}

					remove(popUp);
					remove(yesPopUp);
					remove(noPopUp);

					setEnabledMethodTrue();

					remove(drawCardsLabel);
					remove(pickCardButton);
					remove(unoButton);

					repaint();
					pickCardBoolean = true;

				}
				
				if (!gameClassic.player0ThrewCard() && (gameClassic.getDiscardedCard().getType() == Type.PLUS2 || gameClassic.getDiscardedCard().getType() == Type.WILDCARDPLUS4 ) 
						&& gameClassic.getCounter() != 0) {

					remove(drawCardsLabel);
					remove(pickCardButton);
					remove(unoButton);

					repaint();
					pickCardBoolean = true;
				}
			}
		}

		switch(gameClassic.getTurnIndex()) {

		//PLAYER_AI1'S TURN
		case 1 -> {
			remove(rect);
			remove(rect2);
			firstTimeAddingRect = true;

			elon.setIcon(new ImageIcon("res/label_player_elonmusk1.png"));
			leclerc.setIcon(new ImageIcon("res/label_player_charlesleclerc1.png")); 

			messi.setIcon(new ImageIcon("res/label_player_lionelmessi1_red.png"));

			if (gameClassic.getPlayerAI_1().isUnoDisplayable()) {

				long start = System.currentTimeMillis();

				sayUno.setBounds(176, 370, 119, 46);
				add(sayUno);
				while(System.currentTimeMillis() < start+1000) {}
				remove(sayUno);
			}
			gameClassic.getPlayerAI_1().setUnoDisplayable(false);
		}

		//PLAYER_AI2'S TURN
		case 2 -> {		
			remove(rect);
			remove(rect2);
			firstTimeAddingRect = true;

			elon.setIcon(new ImageIcon("res/label_player_elonmusk1.png"));
			messi.setIcon(new ImageIcon("res/label_player_lionelmessi1.png")); 

			leclerc.setIcon(new ImageIcon("res/label_player_charlesleclerc1_red.png"));

			if (gameClassic.getPlayerAI_2().isUnoDisplayable()) {

				long start = System.currentTimeMillis();

				sayUno.setBounds(565, 210, 119, 46);
				add(sayUno);
				while(System.currentTimeMillis() < start+1000) {}
				remove(sayUno);
			}
			gameClassic.getPlayerAI_2().setUnoDisplayable(false);
		}

		//PLAYER_AI3'S TURN
		case 3 -> {
			remove(rect);
			remove(rect2);
			firstTimeAddingRect = true;

			leclerc.setIcon(new ImageIcon("res/label_player_charlesleclerc1.png")); 
			messi.setIcon(new ImageIcon("res/label_player_lionelmessi1.png")); 

			elon.setIcon(new ImageIcon("res/label_player_elonmusk1_red.png"));

			if (gameClassic.getPlayerAI_3().isUnoDisplayable()) {

				long start = System.currentTimeMillis();

				sayUno.setBounds(980, 370, 119, 46);
				add(sayUno);
				while(System.currentTimeMillis() < start+1000) {}
				remove(sayUno);
			}
			gameClassic.getPlayerAI_3().setUnoDisplayable(false);
		}
		};
	}



	/**
	 * it displays the cards of the players
	 */
	public void displayCards() {

		displayCardsP1();
		displayCardsAI_1();
		displayCardsAI_2();
		displayCardsAI_3();
	}




	/**
	 * it displays the cards of the player1, if the cards are more than 15 are displayed on two rows.
	 * Here is also set the size of the red rectangle that surrounds the cards in hands
	 */
	//PLAYER
	public void displayCardsP1() {

		List<Card> hand = gameClassic.getPlayer().getHand();

		if(hand.size()!=handCardBPlayer.size())
			handToHandBFixer(new ArrayList<>(hand));

		//two rows are needed
		if(hand.size()>15) {

			double handSize = hand.size();
			List<CardB> handB1, handB2 = null;


			if (handSize % 2 == 0) {
				//even
				int a = (int) (handSize/2);

				handB1 = handCardBPlayer.subList(0, a);
				handB2 = handCardBPlayer.subList(a, (int)handSize);

			}
			else {
				//odd
				int b = (int) ((handSize/2)-0.5);
				int c = (int) ((handSize/2)+0.5);

				handB1 = handCardBPlayer.subList(0, b);  
				handB2 = handCardBPlayer.subList(c-1, (int)handSize); 
			}


			//upper row
			handCardsPrinter(handB1, 490);

			//upper red rectangle bounds
			int width = handB1.size()*62;
			int startingPoint = 640-width/2;
			rect.setBounds(startingPoint-5, 485, width+10, 92);

			//lower row
			handCardsPrinter(handB2, 580);
			
			//lower red rectangle bounds
			int width2 = handB2.size()*62;
			int startingPoint2 = 640-width2/2;
			rect2.setBounds(startingPoint2-5, 575, width2+10, 92);

			add(rect2);
		}
		
		//only one row is needed
		else {

			int width = (gameClassic.getPlayer().getHand().size())*62;
			int startingPoint = 640-width/2;
			rect.setBounds(startingPoint-5, 525, width+10, 92);

			handCardsPrinter(handCardBPlayer, 530);
		}
	}

	

	/**	 
	 *   it displays the cover cards of the playerAI 1, it checks the size of the logic hand and the size of the classicPanel hand 
	 * 	 and based on that, it checks if it needs to remove or add covered cards (JLabel)
	 *   
	 */
	public void displayCardsAI_1() {
		List<Card> hand = gameClassic.getPlayerAI_1().getHand(); 
		int width = hand.size()*21;
		int startingPoint = 320-width/2;

		int difference = handLabelA1.size() - hand.size();

		for(int i=0; i<difference; i++) {

			int x = rand.nextInt(handLabelA1.size());
			remove(handLabelA1.get(x));
			handLabelA1.remove(x);
		}

		if(difference<0) {
			
			for(int i=0; i<Math.abs(difference); i++) {
				JLabel cardLabel = new JLabel(new ImageIcon("Cards/uno_back_60x80_left.png"));
				handLabelA1.add(cardLabel);
				add(cardLabel);
			}
		}
		
		for (JLabel label: handLabelA1) {

			label.setBounds(50, startingPoint, 82, 62);
			startingPoint+=21;
		}
	}

	/**	 
	 *   it displays the cover cards of the playerAI 2, it checks the size of the logic hand and the size of the classicPanel hand 
	 * 	 and based on that, it checks if it needs to remove or add covered cards (JLabel)
	 *   
	 */
	public void displayCardsAI_2() {

		List<Card> hand = gameClassic.getPlayerAI_2().getHand();
		int width = hand.size()*21;
		int startingPoint = 640-width/2;

		int difference = handLabelA2.size() - hand.size();

		for(int i = 0; i<difference; i++) {

			int x = rand.nextInt(handLabelA2.size());
			remove(handLabelA2.get(x));
			handLabelA2.remove(x);
		}

		if(difference<0) {
			
			for(int i=0; i<Math.abs(difference); i++) {
				JLabel cardLabel = new JLabel(new ImageIcon("Cards/uno_back_60x80_straight.png"));
				handLabelA2.add(cardLabel);
				add(cardLabel);
			}
		}

		for (JLabel label: handLabelA2) {

			label.setBounds(startingPoint, 50 , 62, 82);
			startingPoint+=21;
		}
	}

	/**	 
	 *   it displays the cover cards of the playerAI 3, it checks the size of the logic hand and the size of the classicPanel hand 
	 * 	 and based on that, it checks if it needs to remove or add covered cards (JLabel) 
	 */
	public void displayCardsAI_3() {

		List<Card> hand = gameClassic.getPlayerAI_3().getHand();
		int width = hand.size()*21;
		int startingPoint = 320-width/2;

		int difference = handLabelA3.size() - hand.size();

		for(int i=0; i<difference; i++) {

			int x = rand.nextInt(handLabelA3.size());
			remove(handLabelA3.get(x));
			handLabelA3.remove(x);		
		}

		if(difference<0) {
			
			for(int i=0; i<Math.abs(difference); i++) {
				JLabel cardLabel = new JLabel(new ImageIcon("Cards/uno_back_60x80_right.png"));
				handLabelA3.add(cardLabel);
				add(cardLabel);
			}
		}

		for (JLabel label: handLabelA3) {

			label.setBounds(1135, startingPoint, 82, 62);
			startingPoint+=21;
		}
	}


	/** It enables visually all the cards of the hand
	 */
	public void setEnabledMethodTrue() {

		List<Card> hand = gameClassic.getPlayer().getHand();
		List<CardB> handB = handToHandB(hand);

		for(int i=0; i<handB.size(); i++) 
			handB.get(i).setEnabled(true);
	}

	
	/** It disables visually all the cards of the hand except the last one
	 */
	public void setEnabledMethodFalse() {

		List<Card> hand = gameClassic.getPlayer().getHand();		 
		List<CardB> handB = handToHandB(hand);

		for(int i=0; i<handB.size()-1; i++) 
			handB.get(i).setEnabled(false);
	}

	
	
	@Override
	public void paintComponent(Graphics g) {
		super.paintComponent(g);

		Graphics2D g2 = (Graphics2D) g; //casting to a specialized class
		g2.drawImage(backgroundPlay, 0, 0, null);
		g2.drawImage(point, x, y, null);
		g2.drawImage(point2, x2, y2, null);
		g2.drawImage(point3, x3, y3, null);
	}



	 /**
	  * resets the fields of Panel and ClassicPanel to setup a new game
	  */
	public void reset() {

		mapCardToCardB.clear();
		cardToCardB();

		discardedCardToDiscardedCardLab();
		add(discardedCardLabel);
		add(unoButton);
		add(cardsRemained);
		add(backHome);
		add(pickupDeck);

		handCardBPlayer.clear();
		handLabelA1.clear();
		handLabelA2.clear();
		handLabelA3.clear();
		initDrawCardsLabel();
		initHandsBplayers();

		firstTimeAddingRect = true;

		if(gameClassic.getDiscardedCard().getType() == Type.WILDCARD) {

			firstTimeDiscardedCard = true;
			startWildDiscarded();
		}

		add(messi);
		add(elon);
		add(leclerc);
	}

	
	

	//GETTER AND SETTER
	
	public JButton getPickCardButton() {
		return pickCardButton;
	}

	public JButton getPassButton() {
		return passButton;
	}	

	public JButton getYesPopUp() {
		return yesPopUp;
	}

	public JButton getNoPopUp() {
		return noPopUp;
	}

	public JButton getUnoButton() {
		return unoButton;
	}


	public JButton getBackToMenu() {
		return backToMenu;
	}

	public JLabel getWinnerLab() {
		return winnerLab;
	}
}