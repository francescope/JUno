package view;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import model.UtilityMethods;

/** This class contains the rules of the modes described inside a JScrollPane
 * 
 * @author MP
 *
 */
public class RulesPanel extends JPanel{
	
	private BufferedImage backgroundProfile = null; 

	private JButton backArrowButton = new JButton(new ImageIcon("res/Backward_Arrow_1280x720.png"));
	
	private JLabel classicModeLabel = new JLabel(new ImageIcon("res/classicMode.png"));
	private JLabel houseRulesLabel = new JLabel(new ImageIcon("res/houseRules.png"));
	private JLabel deadlylabel = new JLabel(new ImageIcon("res/deadlyMode.png"));
	
	/**
	 * Constructs a RulesPanel object and adds and sets the 3 JLabel representing the 3 modes and the JScrollPane with the JTextArea within them
	 */
	public RulesPanel() {
		
		setLayout(null);
		backgroundProfile = UtilityMethods.loadImg("res/Uno-Menu_0_Stretched.png");
		
		//BACK ARROW
		backArrowButton.setBounds(22, 16, 150, 111);
		ImageIcon backwardsArrowHovImg = new ImageIcon("res/Backward_Arrow_Hov_1280x720.png");
		UtilityMethods.backgroundTrasparent(backArrowButton,backwardsArrowHovImg);
		
		add(backArrowButton);
		
		classicModeLabel.setBounds(570, 65,144 , 46);
		add(classicModeLabel);
		
		houseRulesLabel.setBounds(520, 243, 258 , 46);
		add(houseRulesLabel);
		
		deadlylabel.setBounds(565, 428, 152 , 57);
		add(deadlylabel);
		
		
		//CLASSIC MODE RULES
		JTextArea area = new JTextArea(10,10);
		JScrollPane scrollPane = new JScrollPane(area);
		scrollPane.setBounds(470,133, 350,100);
		
		area.setText(
				 "Each player draws a card. Player with the highest point value\r\n"
				+ "is the dealer. Shuffle the deck. Each player is dealt 7 cards.\r\n"
				+ "Place the remaining cards facedown to form a DRAW pile. \r\n"
				+ "Turn over the top card of the DRAW pile to begin a \r\n"
				+ "DISCARD pile. If the top card is a Wild or Wild Draw 4, \r\n"
				+ "return it to the deck and pick another card. For all other \r\n"
				+ "cards, see directions that follow.\r\n"
				+ "\n"+
				"Player to the left of the dealer plays first. Play passes to the \r\n"
				+ "left to start. Match the top card on the DISCARD pile either \r\n"
				+ "by number, color or word. For example, if the card is a \r\n"
				+ "Green 7, you must play a Green card or any color 7. Or, \r\n"
				+ "you may play any Wild card or a Wild Draw 4 card. If you \r\n"
				+ "don't have anything that matches, you must pick a card \r\n"
				+ "from the DRAW pile. If you draw a card you can play,\r\n"
				+ "play it. Otherwise, play moves to the next person. Before \r\n"
				+ "playing your next to last card, you must say \"UNO.\" If you \r\n"
				+ "don't say UNO and another player catches you with just \r\n"
				+ "one card before the next player begins their turn you must \r\n"
				+ "pick FOUR more cards from the DRAW pile. If you are not \r\n"
				+ "caught before the next player either draws a card from the \r\n"
				+ "DRAW pile or draws a card from their hand to play, you do \r\n"
				+ "not have to draw the extra cards. Once a player plays their \r\n"
				+ "last card, the hand is over. Points are tallied (see Scoring \r\n"
				+ "section) and you start over again."
				+"\n"+
				"Draw 2 Card - When you play this card, the next person to \r\n"
				+ "play must draw 2 cards and forfeit his/her turn. If this card \r\n"
				+ "is turned up at the beginning of play, the first player must \r\n"
				+ "draw two cards. This card may only be played on a \r\n"
				+ "matching color or on another Draw 2 card.\r\n"
				+ "\r\n"
				+ "Reverse Card - This card reverses direction of play. Play to \r\n"
				+ "the left now passes to the right, and vice versa. If this card \r\n"
				+ "is turned up at the beginning of play, the player to the right \r\n"
				+ "now plays first, and play then goes to the right instead of \r\n"
				+ "left. This card may only be played on a matching color or \r\n"
				+ "on another Reverse card.\r\n"
				+ "\r\n"
				+ "Skip Card - The next person in line to play after this card is \r\n"
				+ "played loses his/her turn and is \"skipped.\" If this card is \r\n"
				+ "turned up at the beginning of play, the first player (the \r\n"
				+ "player to the left of the dealer) is skipped. This card may \r\n"
				+ "only be played on a matching color or on another Skip \r\n"
				+ "card.\r\n"
				+ "\r\n"
				+ "Wild Card - When you play this card, you may change the \r\n"
				+ "color being played to any color (including the current color) \r\n"
				+ "to continue play. You may play a Wild card even if you have \r\n"
				+ "another playable card in hand. If this card is turned up at \r\n"
				+ "the beginning of play, the person to the left of the dealer \r\n"
				+ "chooses the color to begin play and plays the first card.\r\n"
				+ "\r\n"
				+ "Wild Draw 4 Card - This card allows you to call the next \r\n"
				+ "color played and requires the next player to pick 4 cards \r\n"
				+ "from the DRAW pile and forfeit his/her turn. However, there \r\n"
				+ "is a hitch! You can only play this card when you don't have \r\n"
				+ "a card in your hand that matches the color of the card \r\n"
				+ "previously played. If turned up at the beginning of play, \r\n"
				+ "return this card to the deck and pick another card. Note: A \r\n"
				+ "Player may play a \"Wild Draw Four\" card even if they have \r\n"
				+ "a matching number or word card of a different color in their \r\n"
				+ "hand. If you suspect that a player has played a Wild Draw 4 \r\n"
				+ "card illegally, you may challenge them. A challenged \r\n"
				+ "player must show his/her hand to the player who \r\n"
				+ "challenged. If the challenged player is guilty, he/she must \r\n"
				+ "draw the 4 cards. If the challenged player is not guilty, the \r\n"
				+ "challenger must draw the 4 cards, plus 2 additional cards. \r\n"
				+ "Only the person required to draw the 4 cards can make the \r\n"
				+ "challenge.\r\n"
				+ "");
		
		
		area.setCaretPosition(0);
		area.setEditable(false);

		add(scrollPane);
		
		
		//HOUSE MODE RULES
		JTextArea area2 = new JTextArea(10,10);
		JScrollPane scrollPane2 = new JScrollPane(area2);
		scrollPane2.setBounds(470,313, 350,100);
		
		area2.setText("This mode follows the rules of the classic mode, \r\n"
				+ "but its also possible to stack the Wild Card 4 card and \r\n"
				+ "the Draw 2 Card, so\r\n"
				+ "\r\n"
				+ "Stacking Draw 2s:\r\n"
				+ "When a Draw 2 card is played, the next person has the \r\n"
				+ "opportunity to play a Draw 2 card immediately to avoid \r\n"
				+ "drawing cards. The next person then has to either draw\r\n"
				+ "4 cards or add her own Draw 2 card. Play continues until\r\n"
				+ "a player cannot add a Draw 2 to the pile. \r\n"
				+ "That person then draws the number of cards equal to all \r\n"
				+ "of the Draw 2s that were stacked and play continues with\r\n"
				+ "the next person. \r\n"
				+ "The same thing is also applied to the Wild Card 4s.\r\n"
				+ "\r\n"
				+ "No-limits Draw 4s:\r\n"
				+ "Draw 4 cards can be played at any time\r\n"
				+ "even if a player is able to match the color in play.");
		
		
		area2.setCaretPosition(0);
		area2.setEditable(false);

		add(scrollPane2);
		
		
		//DEADLY MODE RULES
		JTextArea area3 = new JTextArea(10,10);
		JScrollPane scrollPane3 = new JScrollPane(area3);
		scrollPane3.setBounds(470,498, 350,100);
		
		area3.setText("It's basically just Uno played with normal cards, but with\r\n"
				+ "one major difference. If you can't play a card, you LOSE and\r\n"
				+ "immediately drop out of play (rather than drawing a card).\r\n"
				+ "The only ways to draw cards are if someone plays \r\n"
				+ "a \"Draw 2\" or \"Draw 4 Wild\" on you.\r\n"
				+ "\r\n"
				+ "This makes the individual games very quick and exciting,\r\n"
				+ "because there are now 2 ways to win. Either get rid of all \r\n"
				+ "your cards or be the last person who hasn't lost.");
		
		
		area3.setCaretPosition(0);
		area3.setEditable(false);

		add(scrollPane3);
	}
	
	@Override
	public void paintComponent(Graphics g) {
		super.paintComponent(g);

		Graphics2D g2 = (Graphics2D) g; //casting to a specialized class
		g2.drawImage(backgroundProfile, 0, 0, 1280, 720, null);
	}
	
	
	public JButton getBackArrowButton() {
		return backArrowButton;
	}
}
