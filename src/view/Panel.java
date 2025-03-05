package view;

import java.awt.Color;
import java.awt.Font;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Observer;
import java.util.Random;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.Timer;
import javax.swing.border.LineBorder;

import model.Card;
import model.Game;
import model.Type;
import model.UtilityMethods;


/**
 * This class contains the default components shared by all game panels (classicPanel, houseRulesPanel, deadlyPanel) and
 * implementing Observer receives all updates from the model
 * @author MP
 *
 */
public abstract class Panel extends JPanel implements Observer {

	protected Game game;
	
	protected Random rand;

	//clockwise/anticlockwise points
	protected Timer timer;

	protected BufferedImage point = UtilityMethods.loadImg("res/point_orbit.png");
	protected BufferedImage point2 = UtilityMethods.loadImg("res/point_orbit.png");
	protected BufferedImage point3 = UtilityMethods.loadImg("res/point_orbit.png");

	protected int x,y;
	protected int x2,y2;
	protected int x3,y3;

	protected int cx = 648, cy = 324;
	protected double a;
	protected double a2=180;
	protected double a3=90;

	protected int radius = 140;

	//select color button 
	protected JButton redCircle;
	protected JButton blueCircle;
	protected JButton greenCircle;
	protected JButton yellowCircle;


	//other players info
	protected JLabel messi = new JLabel(new ImageIcon("res/label_player_lionelmessi1.png"));
	protected JLabel leclerc = new JLabel(new ImageIcon("res/label_player_charlesleclerc1.png"));
	protected JLabel elon = new JLabel(new ImageIcon("res/label_player_elonmusk1.png"));


	//counter of the cards you might draw
	protected JLabel drawCardsLabel;


	protected JLabel pickupDeck = new JLabel(new ImageIcon("Cards/uno_back_4.png"));

	protected JLabel sayUno = new JLabel(new ImageIcon("res/uno!.png"));

	//cards left in the draw pile
	protected JLabel cardsRemained = new JLabel();

	//endgame popup
	protected JLabel popUpEndGame = new JLabel(new ImageIcon("res/endGame.png"));

	protected boolean pickCardBoolean = true;

	protected JLabel discardedCardLabel = new JLabel();

	//rectangle around your cards that appears on your turn
	protected JLabel rect = new JLabel();
	protected JLabel rect2 = new JLabel();
	//booleans to manage the objects in the panel that appear and disappear
	protected boolean firstTimeAddingRect = true;

	protected Map<Card,CardB>  mapCardToCardB = new HashMap<>();


	protected boolean firstTimeDiscardedCard;


	protected List<JLabel> handLabelA1 = new ArrayList<>();
	protected List<JLabel> handLabelA2 = new ArrayList<>();
	protected List<JLabel> handLabelA3 = new ArrayList<>();

	protected List<CardB> handCardBPlayer = new ArrayList<>();

	
	protected JButton backHome = new JButton();


	
	
	/**
	 * Constructs a Panel object and it instantiates all the standard components
	 * @param game game object in order to take some model informations that it needs to display
	 */
	public Panel(Game game) {

		this.game = game;

		setLayout(null);

		rand = new Random();

		//CONVERSION FROM CARD TO CARD BUTTON
		cardToCardB();

		//converts the discarded card (card) into a label to be displayed on the discard pile
		discardedCardToDiscardedCardLab();
		add(discardedCardLabel);

		//timer object that synchronizes the three spheres that rotate according to the direction of the game
		timer = new Timer(10, e -> {

			//	x = cx + r * cos(a)
			//	y = cy + r * sin(a)
			x = (int) (cx + radius *  Math.cos(a));
			y = (int) (cy + radius *  Math.sin(a));

			x2 = (int) (cx + radius *  Math.cos(a2));
			y2 = (int) (cy + radius *  Math.sin(a2));

			x3 = (int) (cx + radius *  Math.cos(a3));
			y3 = (int) (cy + radius *  Math.sin(a3));

			if(game.isClockwise()) {
				a+=0.01;
				a2+=0.01;
				a3+=0.01;
			}

			else {
				a-=0.01;
				a2-=0.01;
				a3-=0.01;
			}
			repaint();
		});

		popUpEndGame.setBounds(350,70,600,600);

		//deck to draw cards from
		pickupDeck.setBounds(580, 295, 66, 86);
		
		backHome.setBounds(1200, 20, 60, 60);
		add(backHome);
		
		//choose color buttons for the wildcard and wildcard+4
		yellowCircle = new JButton(new ImageIcon("res/yellow_circle.png"));
		yellowCircle.setBounds(460, 410, 70, 70);
		UtilityMethods.backgroundTrasparent(yellowCircle, new ImageIcon("res/yellow_circle_hov.png"));

		greenCircle = new JButton(new ImageIcon("res/green_circle.png"));
		greenCircle.setBounds(539, 410, 70, 70);
		UtilityMethods.backgroundTrasparent(greenCircle, new ImageIcon("res/green_circle_hov.png"));

		redCircle = new JButton(new ImageIcon("res/red_circle.png"));
		redCircle.setBounds(618, 410, 70, 70);
		UtilityMethods.backgroundTrasparent(redCircle, new ImageIcon("res/red_circle_hov.png"));

		blueCircle = new JButton(new ImageIcon("res/blue_circle.png"));
		blueCircle.setBounds(697, 410, 70, 70);
		UtilityMethods.backgroundTrasparent(blueCircle, new ImageIcon("res/blue_circle_hov.png"));

		//red rectangles surrounding the hand when it is the Player's turn
		rect.setBorder(new LineBorder(Color.red,5));
		rect2.setBorder(new LineBorder(Color.red,5));

		//other players info (name and propic)
		messi.setBounds(130, 290, 200, 107);
		add(messi);
		leclerc.setBounds(560, 120, 200, 107);
		add(leclerc);
		elon.setBounds(930, 290, 200, 107);
		add(elon);

		initDrawCardsLabel(); //counter of the cards you might draw
	}



	/**
	 * this method converts all the model Card into CardB (JButton) and it adds them to a Map (Card:CardB)
	 */
	public void cardToCardB () {

		for (Card card : game.getUntouchedDeck()) {

			if (card.getType().equals(Type.DEFAULT)) {

				String filePath = generateFilePath(card.getN(), card.getC());
				mapCardToCardB.put(card, new CardB(filePath, card));
			}

			else {
				String filePath = generateFilePathSpecial(card.getType(), card.getC());
				mapCardToCardB.put(card, new CardB(filePath, card));
			}
		}
	}



	/**
	 * based on the Type and color of the Special Card (+2, reverse, skip, wildCard, wildCard+4) it generates a filepath for it, 
	 * @param t Type of the card
	 * @param c color of the card
	 * @return the filepath generated
	 */
	public String generateFilePathSpecial(Type t, char c) {

		if(t==Type.PLUS2) {
			String s = switch(c) {

			case 'R' -> "Cards/+2_RED.png";
			case 'B' -> "Cards/+2_BLU.png";
			case 'G' -> "Cards/+2_GREEN.png";
			case 'Y' -> "Cards/+2_YELL.png";
			default -> "";	
			};
			return s;
		}

		if(t==Type.REVERSE) {
			String s = switch(c) {

			case 'R' -> "Cards/reverse_RED.png";
			case 'B' -> "Cards/reverse_BLU.png";
			case 'G' -> "Cards/reverse_GREEN.png";
			case 'Y' -> "Cards/reverse_YELL.png";
			default -> "";	
			};
			return s;
		}

		if(t==Type.SKIP) {
			String s = switch(c) {

			case 'R' -> "Cards/skip_RED.png";
			case 'B' -> "Cards/skip_BLU.png";
			case 'G' -> "Cards/skip_GREEN.png";
			case 'Y' -> "Cards/skip_YELL.png";
			default -> "";	
			};
			return s;
		}

		if(t==Type.WILDCARD) 
			return "Cards/wildCard.png";


		if(t==Type.WILDCARDPLUS4) 
			return "Cards/wildCard+4.png";

		return null;
	}


	/**
	 * based on the number and color of the default Card it generates a filepath for it, 
	 * @param n number of the card
	 * @param c color of the card
	 * @return the filepath generated
	 */
	public String generateFilePath(int n, char c) {

		String s = switch(c) {

		case 'R' -> "Cards/"+n+"_RED.png";
		case 'B' -> "Cards/"+n+"_BLU.png";
		case 'G' -> "Cards/"+n+"_GREEN.png";
		case 'Y' -> "Cards/"+n+"_YELL.png";
		default -> "";	
		};
		return s;
	}


	/**
	 * This card converts the model discardedCard to a view DiscardedCard (JLabel)
	 */
	public void discardedCardToDiscardedCardLab () {

		Card discardedCardModel = game.getDiscardedCard(); //discarded card already logically decided in game

		if(discardedCardModel.getType()==Type.WILDCARD) {

			String s = switch(discardedCardModel.getC()) {

			case 'Y' -> "Cards/wildcard_yell.png";
			case 'R' -> "Cards/wildcard_red.png";
			case 'G' -> "Cards/wildcard_green.png";
			case 'B' -> "Cards/wildcard_blue.png";
			default -> "Cards/wildCard.png";

			};
			discardedCardLabel.setIcon(new ImageIcon(s));
		}

		else if(discardedCardModel.getType()==Type.WILDCARDPLUS4) {
			String s = switch(discardedCardModel.getC()) {

			case 'Y' -> "Cards/wildcardplus4_yell.png";
			case 'R' -> "Cards/wildcardplus4_red.png";
			case 'G' -> "Cards/wildcardplus4_green.png";
			case 'B' -> "Cards/wildcardplus4_blue.png";
			default -> "Cards/wildCard+4.png";

			};
			discardedCardLabel.setIcon(new ImageIcon(s));
		}
		
		else 
			discardedCardLabel.setIcon(new ImageIcon(mapCardToCardB.get(discardedCardModel).getFilePath()));

		discardedCardLabel.setBounds(675, 297, 62, 82);
		
		/*It actually checks that it is in the dictionary and therefore I can remove it, because the wildcards are not in the dictionary,
		   those as soon as they are clicked are removed, both from the listener and from the playerAI*/
		if(mapCardToCardB.containsKey(discardedCardModel)) 
			remove(mapCardToCardB.get(discardedCardModel)); 
	}	


	/**
	 * it converts the model hand into a view Hand by getting its equivalent cardB from the dictionary
	 * @param hand the model hand
	 * @return a list of CardB (JButton)
	 */
	public List<CardB> handToHandB(List<Card> hand) {

		List<CardB> handB = new ArrayList<>();

		for (Card card : hand) 
			handB.add(mapCardToCardB.get(card));

		return handB;
	}


	/**
	 * it checks the size of the model hand and the view hand (of the Player) and it adds/removes the missing/unnecessary cards
	 * @param hand the model hand
	 */
	public void handToHandBFixer(List<Card> hand) {

		List<Card> handCardPlayer = new ArrayList<>(handCardBPlayer.stream().map(cardB -> cardB.getCard()).toList());

		int difference = handCardBPlayer.size() - hand.size();

		if(difference>0) {
			handCardPlayer.removeAll(hand);

			handCardPlayer.forEach(card -> {

				handCardBPlayer.remove(mapCardToCardB.get(card));
				remove(mapCardToCardB.get(card));
			});
		}

		if(difference<0) {

			hand.removeAll(handCardPlayer);

			hand.forEach(card -> {

				handCardBPlayer.add(mapCardToCardB.get(card));
				add(mapCardToCardB.get(card));
			});
		}
		repaint();
	}


	/**
	 *  it sets the bounds of the player's hand card
	 * @param handB the view hand
	 * @param y the y coordinate
	 */
	public void handCardsPrinter(List<CardB> handB, int y) {

		int width = handB.size()*62;
		int startingPoint = 640-width/2;

		for (CardB cardB: handB) {

			cardB.setBounds(startingPoint, y, 62, 82);
			startingPoint+=62;
		}
	}

	
	
	/**
	 * 
	 * it initializes the drawCardsLabel which is a visual representation of the drawCardsCounter in the Game
	 */
	public void initDrawCardsLabel() {

		drawCardsLabel = new JLabel("+"+game.getCounter());
		drawCardsLabel.setFont(new Font("Tahoma", Font.PLAIN, 25));
		drawCardsLabel.setForeground(new Color(255, 255, 255));
		drawCardsLabel.setBounds(595, 225, 66, 86);

		if (game.getCounter() != 0) {

			drawCardsLabel.setText("+"+game.getCounter()); 
			add(drawCardsLabel);
		}
	}

	

	//GETTER AND SETTER
	public JButton getBackHome() {
		return backHome;
	}
	
	//CHOOSE COLOR BUTTON FOR THE WILDCARD
	public JButton getRedCircle() {
		return redCircle;
	}

	public JButton getBlueCircle() {
		return blueCircle;
	}

	public JButton getGreenCircle() {
		return greenCircle;
	}

	public JButton getYellowCircle() {
		return yellowCircle;
	}

	public boolean isFirstTimeDiscardedCard() {
		return firstTimeDiscardedCard;
	}

	public void setFirstTimeDiscardedCard(boolean firstTimeDiscardedCard) {
		this.firstTimeDiscardedCard = firstTimeDiscardedCard;
	}

	public List<CardB> getHandCardBPlayer() {
		return handCardBPlayer;
	}

	public JLabel getPopUpEndGame() {
		return popUpEndGame;
	}

	public Timer getTimer() {
		return timer;
	}

	public boolean isPickCardBoolean() {
		return pickCardBoolean;
	}

	public void setPickCardBoolean(boolean pickCardBoolean) {
		this.pickCardBoolean = pickCardBoolean;
	}

	public Map<Card, CardB> getMapCardToCardB() {
		return mapCardToCardB;
	}
}
