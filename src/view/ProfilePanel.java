package view;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.LineBorder;
import model.Stats;
import model.UtilityMethods;



/**
 * This class represents visually the Stats class, it includes the nickname, matches won, matches lost and matches played by the player
 * 
 * @author MP
 *
 */
public class ProfilePanel extends JPanel {

	private Stats stats;
	
	private BufferedImage backgroundProfile = null; 

	private JButton resetButton = new JButton(new ImageIcon("res/resetButton.png"));
	
	private JTextField nicknameField;

	private JLabel propic;
	private JButton backArrowButton = new JButton(new ImageIcon("res/Backward_Arrow_1280x720.png"));
	private JButton changePropic = new JButton(new ImageIcon("res/changePropic.png"));

	private JLabel counterLevelsLabel;
	private JLabel counterPlayLabel;
	private JLabel counterWinLabel;
	private JLabel counterLostLabel;
	
	
	
	/**
	 * Constructs a ProfilePanel object and creates and adds all the components to represents the Stats class
	 * @param stats the stats of the game
	 */
	public ProfilePanel(Stats stats) {
		
		setLayout(null);
		this.stats = stats;
		
		//BACKGROUND PROFILE IMAGE
		backgroundProfile = UtilityMethods.loadImg("res/Uno-Menu_0_Stretched.png");
		
		ImageIcon resetButtonHov = new ImageIcon("res/resetButtonHov.png");
		UtilityMethods.backgroundTrasparent(resetButton, resetButtonHov);
		resetButton.setBounds(425, 440, 87, 39);
		add(resetButton);
		 
		//PROFILE PANEL BACKARROW BUTTON
		backArrowButton.setBounds(22, 16, 150, 111);
		ImageIcon backwardsArrowHovImg = new ImageIcon("res/Backward_Arrow_Hov_1280x720.png");
		UtilityMethods.backgroundTrasparent(backArrowButton,backwardsArrowHovImg);

		
		//PROFILE_IMG LABEL
		JLabel profileLabel = new JLabel(new ImageIcon("res/Profile_profilePanel.png"));
		profileLabel.setBounds(549, 58, 187, 58);

		//NICKNAME_IMG LABEL
		JLabel nicknameLabel = new JLabel(new ImageIcon("res/nickname.png"));
		nicknameLabel.setBounds(423, 182, 152, 31);
		
		//LEVELS_IMG LABEL
		JLabel levelsLabel = new JLabel(new ImageIcon("res/level.png"));
		levelsLabel.setBounds(418, 282, 93, 31);
		
		//LEVELS_COUNTER LABEL
		counterLevelsLabel = new JLabel(""+stats.getLevel());
		counterLevelsLabel.setBounds(525, 284, 93, 31);
		counterLevelsLabel.setFont(new Font("Bebas Neue", Font.BOLD, 35));
		counterLevelsLabel.setForeground(new Color(255,140,0));

				
		//MATCHESPLAYED_IMG LABEL
		JLabel matchesPlayedLabel = new JLabel(new ImageIcon("res/matches_played.png"));
		matchesPlayedLabel.setBounds(415, 323, 250, 31); //+38
		
		//MATCHESPLAYED_COUNTER LABEL
		counterPlayLabel = new JLabel();
		counterPlayLabel.setBounds(675, 320, 250, 35);
		counterPlayLabel.setText(stats.getMatchesPlayed()+"");
		counterPlayLabel.setFont(new Font("Bebas Neue", Font.BOLD, 35));
		counterPlayLabel.setForeground(new Color(255,140,0));
		

		
		//MATCHESWON_IMG LABEL
		JLabel matchesWonLabel = new JLabel(new ImageIcon("res/wins.png"));
		matchesWonLabel.setBounds(423, 358, 80, 31);
		
		//MATCHESWON_COUNTER LABEL
		counterWinLabel = new JLabel();
		counterWinLabel.setBounds(530, 361, 80, 31);
		counterWinLabel.setText(stats.getMatchesWon()+"");
		counterWinLabel.setFont(new Font("Bebas Neue", Font.BOLD, 35));
		counterWinLabel.setForeground(new Color(255,140,0));

		//MATCHESLOST_IMG LABEL
		JLabel matchesLostLabel = new JLabel(new ImageIcon("res/losses.png"));
		matchesLostLabel.setBounds(423, 396, 100, 31);
	
		//MATCHESLOST_COUNTER LABEL
		counterLostLabel = new JLabel();
		counterLostLabel.setBounds(550, 399, 100, 31);
		counterLostLabel.setText(stats.getMatchesLost()+"");
		counterLostLabel.setFont(new Font("Bebas Neue", Font.BOLD, 35));
		counterLostLabel.setForeground(new Color(255,140,0));

		//JTEXTFIELD NICKNAME	
		nicknameField = new JTextField();
		initTextfield(stats.getUsername());


		//add to profilePanel the following buttons and labels
		add(backArrowButton);
		add(profileLabel);
	    add(nicknameLabel);
	    
		add(nicknameField);
	    
	    add(levelsLabel);
	    add(counterLevelsLabel);
	    
	    add(matchesPlayedLabel);
	    add(counterPlayLabel);
	    
	    add(matchesWonLabel);
	    add(counterWinLabel);
	    
	    add(matchesLostLabel);
	    add(counterLostLabel);
	    
	    
	    
	    
		ImageIcon changePropicHov = new ImageIcon("res/changePropicHov.png");
		UtilityMethods.backgroundTrasparent(changePropic, changePropicHov);
	    changePropic.setBounds(490, 510, 141, 40);
	    add(changePropic);
	    
	    
	    propic = new JLabel(new ImageIcon(stats.getPropicFilePath()));
	    propic.setBounds(650,470, 120, 120);
		propic.setBorder(new LineBorder(Color.black,3));
	    add(propic);
	    
	    
	    JLabel pngText = new JLabel("(png, jpg)");
	    pngText.setBounds(535, 540, 150, 30);
	    add(pngText);
	}



	/**
	 * it takes the fields from the stats class and it sets them to the JLabels
	 */
	public void load() {
		
		nicknameField.setText(stats.getUsername());
		counterPlayLabel.setText(""+stats.getMatchesPlayed());
	    counterWinLabel.setText(""+stats.getMatchesWon());
	    counterLostLabel.setText(""+stats.getMatchesLost());
		counterLevelsLabel.setText(""+stats.getLevel());
		propic.setIcon(new ImageIcon(stats.getPropicFilePath()));

	}

	

	public void initTextfield(String testo) {
		
		nicknameField.setText(testo);
		nicknameField.setFont(new Font("Tahoma", Font.PLAIN, 21));
		nicknameField.setBounds(580, 188, 276, 23);
		nicknameField.setBorder(new LineBorder(Color.black,2));
		nicknameField.setColumns(10);
		
	}
	
	
	
	/**
	 * it resets the JLabels with the default stats value
	 */
	public void reset() {
		
		nicknameField.setText("username");
		counterPlayLabel.setText("0");
	    counterWinLabel.setText("0");
	    counterLostLabel.setText("0");
		counterLevelsLabel.setText("0");
	}
	

	
	@Override
	public void paintComponent(Graphics g) {
		super.paintComponent(g);

		Graphics2D g2 = (Graphics2D) g; //casting to a specialized class
		g2.drawImage(backgroundProfile, 0, 0, 1280, 720, null);
	}
	
	
	
	public JLabel getPropic() {
		return propic;
	}

	public JButton getChangePropic() {
		return changePropic;
	}

	public JButton getResetButton() {
		return resetButton;
	}

	public JTextField getNicknameField() {
		return nicknameField;
	}

	public JButton getBackArrowButton() {
		return backArrowButton;
	}
}