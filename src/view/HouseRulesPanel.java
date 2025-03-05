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
import model.house_rules.GameHouseRules;

/**
 * graphical representation of the state of the game in houseRules mode. 
 * receives game updates via observer pattern.
 * 
 * contains the update method which, depending on the state of the game, manages the graphic components.
 * @author MP
 *
 */
public class HouseRulesPanel extends Panel {

	private GameHouseRules gameHouseRules;

	private BufferedImage backgroundPlay = UtilityMethods.loadImg("res/upscalata.png");

	//pick card from the deck button 
	private JButton pickCardButton;
	
	//pass turn button
	private JButton passButton;

	//uno button
	private JButton unoButton;

	//back to menu endgame button
	private JButton backToMenu;

	private JLabel winnerLab = new JLabel();


	/**
	 * it creates a panel with the default components
	 * @param gameHouseRules the gameHouseRules instance
	 */
	public HouseRulesPanel(GameHouseRules gameHouseRules) {

		super(gameHouseRules);
		this.gameHouseRules = gameHouseRules;
		drawCardsLabel.setForeground(new Color(0, 0, 0));

		//cards left in the draw pile
		cardsRemained.setText(gameHouseRules.getDeck().size()+"");
		cardsRemained.setBounds(587, 265, 66, 86);
		cardsRemained.setForeground(new Color(255, 255, 255));
		cardsRemained.setFont(new Font("Tahoma", Font.PLAIN, 15));
		add(cardsRemained);
		add(pickupDeck);

		//PLAYER BUTTONS
		pickCardButton = new JButton(new ImageIcon("res/Hand.png"));
		pickCardButton.setBounds(580, 375, 64, 84);
		UtilityMethods.backgroundTrasparent(pickCardButton, new ImageIcon("res/Hand_hov.png"));

		//PASS BUTTON
		passButton = new JButton(new ImageIcon("res/pass.png"));
		passButton.setBounds(572, 385, 80, 30);
		UtilityMethods.backgroundTrasparent(passButton, new ImageIcon("res/passHov.png"));

		//BACK TO MENU
		backToMenu = new JButton(new ImageIcon("res/back_to_menu.png"));
		backToMenu.setBounds(580, 400, 150, 50);
		UtilityMethods.backgroundTrasparent(backToMenu, new ImageIcon("res/back_to_menu_hov.png"));

		//UNO BUTTON
		unoButton = new JButton(new ImageIcon("res/uno_button.png"));
		unoButton.setBounds(660, 382, 90, 70);
		UtilityMethods.backgroundTrasparent(unoButton, new ImageIcon("res/uno_button_hov.png"));
		add(unoButton);

		backHome.setIcon(new ImageIcon("res/backHome.png"));
		UtilityMethods.backgroundTrasparent(backHome,  new ImageIcon("res/backHomeHov.png"));
		
		initHandsBplayers();

		//the initial discarded card (decided by the game) is a wildcard?
		if(gameHouseRules.getDiscardedCard().getType() == Type.WILDCARD) {

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
			handCardBPlayer.add(mapCardToCardB.get(gameHouseRules.getPlayer().getHand().get(i)));
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
		repaint();
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

		cardsRemained.setText(gameHouseRules.getDeck().size()+"");

		if(gameHouseRules.getGameStatus().equals("FINISHED")) {

			winnerLab.setText(gameHouseRules.getWinner()+" WON THE GAME!");
			winnerLab.setBounds(497, 300, 350, 50);
			winnerLab.setFont(new Font("Tahoma", Font.PLAIN, 23));
			winnerLab.setForeground(new Color(255,0,0));

			timer.stop();
			
			for(Component comp : getComponents()) 
				remove(comp);
			
			add(winnerLab);
			add(backToMenu);
			add(popUpEndGame);

			repaint();
		}

		else {

			discardedCardToDiscardedCardLab();

			if (gameHouseRules.getCounter() != 0) {

				drawCardsLabel.setText("+"+gameHouseRules.getCounter());
				add(drawCardsLabel);
			}

			displayCards();

			if (gameHouseRules.getDrawCardsBoolean() && gameHouseRules.getCounter()==0)	{

				remove(drawCardsLabel);
				repaint();
				gameHouseRules.setDrawCardsBoolean(false);
			}
			
			//PLAYER'S TURN
			if (gameHouseRules.getTurnIndex() == 0) {

				elon.setIcon(new ImageIcon("res/label_player_elonmusk1.png"));
				leclerc.setIcon(new ImageIcon("res/label_player_charlesleclerc1.png")); 
				messi.setIcon(new ImageIcon("res/label_player_lionelmessi1.png")); 


				if(firstTimeAddingRect) {
					add(rect);
					if(gameHouseRules.getPlayer().getHand().size()>15) {
						add(rect2);
					}
					firstTimeAddingRect = false;
				}

				if(pickCardBoolean && gameHouseRules.getCounter()==0) {

					add(pickCardButton);
					add(unoButton);
					repaint();
					pickCardBoolean = false;
				}

				while(gameHouseRules.isColorAnsw()) {

					try {
						Thread.sleep(1);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}

				gameHouseRules.setColorAnsw(false);
			}
		}
		
		
		switch(gameHouseRules.getTurnIndex()) {

		//PLAYERAI1'S TURN
		case 1 -> {
			remove(rect);
			remove(rect2);
			firstTimeAddingRect = true;

			elon.setIcon(new ImageIcon("res/label_player_elonmusk1.png"));
			leclerc.setIcon(new ImageIcon("res/label_player_charlesleclerc1.png")); 

			messi.setIcon(new ImageIcon("res/label_player_lionelmessi1_red.png"));

			if (gameHouseRules.getPlayerAI_1().isUnoDisplayable()) {

				long start = System.currentTimeMillis();

				sayUno.setBounds(176, 370, 119, 46);
				add(sayUno);
				while(System.currentTimeMillis() < start+1000) {}
				remove(sayUno);
			}
			gameHouseRules.getPlayerAI_1().setUnoDisplayable(false);
		}

		//PLAYERAI2'S TURN
		case 2 -> {		
			remove(rect);
			remove(rect2);
			firstTimeAddingRect = true;

			elon.setIcon(new ImageIcon("res/label_player_elonmusk1.png"));
			messi.setIcon(new ImageIcon("res/label_player_lionelmessi1.png")); 

			leclerc.setIcon(new ImageIcon("res/label_player_charlesleclerc1_red.png"));

			if (gameHouseRules.getPlayerAI_2().isUnoDisplayable()) {

				long start = System.currentTimeMillis();

				sayUno.setBounds(565, 210, 119, 46);
				add(sayUno);
				while(System.currentTimeMillis() < start+1000) {}
				remove(sayUno);
			}
			gameHouseRules.getPlayerAI_2().setUnoDisplayable(false);
		}

		//PLAYERAI3'S TURN
		case 3 -> {
			remove(rect);
			remove(rect2);
			firstTimeAddingRect = true;

			leclerc.setIcon(new ImageIcon("res/label_player_charlesleclerc1.png")); 
			messi.setIcon(new ImageIcon("res/label_player_lionelmessi1.png")); 

			elon.setIcon(new ImageIcon("res/label_player_elonmusk1_red.png"));

			if (gameHouseRules.getPlayerAI_3().isUnoDisplayable()) {

				long start = System.currentTimeMillis();

				sayUno.setBounds(980, 370, 119, 46);
				add(sayUno);
				while(System.currentTimeMillis() < start+1000) {}
				remove(sayUno);
			}
			gameHouseRules.getPlayerAI_3().setUnoDisplayable(false);
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
	 * it displays the cards of the player, if the cards are more than 15 are displayed on two rows.
	 * Here is also set the size of the red rectangle that surrounds the cards in hands
	 */
	public void displayCardsP1() {

		List<Card> hand = gameHouseRules.getPlayer().getHand();

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

		else {
			//only one row is needed
			int width = (gameHouseRules.getPlayer().getHand().size())*62;
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
		
		List<Card> hand = gameHouseRules.getPlayerAI_1().getHand(); 
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

		List<Card> hand = gameHouseRules.getPlayerAI_2().getHand();
		int width = hand.size()*21;
		int startingPoint = 640-width/2;

		int difference = handLabelA2.size() - hand.size();

		for(int i=0; i<difference; i++) {

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

		List<Card> hand = gameHouseRules.getPlayerAI_3().getHand();
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

		List<Card> hand = gameHouseRules.getPlayer().getHand();
		List<CardB> handB = handToHandB(hand);

		for(int i = 0; i<handB.size();i++) 
			handB.get(i).setEnabled(true);
	}

	

	/** It disables visually all the cards of the hand except the last one
	 */
	public void setEnabledMethodFalse() {

		List<Card> hand = gameHouseRules.getPlayer().getHand();		 
		List<CardB> handB = handToHandB(hand);

		for(int i = 0; i<handB.size()-1;i++) 
			handB.get(i).setEnabled(false);
	}
	


	/**
	  * resets the fields of Panel and HouseRulesPanel to setup a new game
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

		if(gameHouseRules.getDiscardedCard().getType() == Type.WILDCARD) {

			firstTimeDiscardedCard = true;
			startWildDiscarded();
		}

		add(messi);
		add(elon);
		add(leclerc);
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

	

	//GETTER AND SETTER

	public JButton getPickCardButton() {
		return pickCardButton;
	}

	public JButton getPassButton() {
		return passButton;
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