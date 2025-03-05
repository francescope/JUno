package view;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JPanel;

import model.UtilityMethods;

/**
 * this panel contains the Menu's JButton (Play, Profile, Exit) and the Mute sound JButton
 * 
 * @author MP
 *
 */
public class MenuPanel extends JPanel {

	private BufferedImage background = null; 
	
	private JButton profileButton = new JButton(new ImageIcon("res/Profile_Button.png"));
	private JButton playButton = new JButton(new ImageIcon("res/Play_Button.png"));
	private JButton exitButton = new JButton(new ImageIcon("res/Exit_Button.png"));
	private JButton muteButton = new JButton(new ImageIcon("res/mute.png"));

	
	/**
	 * Constructs a MenuPanel object
	 */
	public MenuPanel() {
		
		setLayout(null);

		//BACKGROUND MENU IMAGE
		background = UtilityMethods.loadImg("res/Uno_Menu_1.png");
		
		//MENU PANEL PLAY BUTTON
		playButton.setBounds(579, 144, 123, 80);
		ImageIcon playHovImg = new ImageIcon("res/Play_Button_Hover.png");
		UtilityMethods.backgroundTrasparent(playButton,playHovImg);
		
		//MENU PANEL PROFILE BUTTON
		profileButton.setBounds(548, 312, 182, 80);
		ImageIcon profileHovImg = new ImageIcon("res/Profile_Button_Hover.png");
		UtilityMethods.backgroundTrasparent(profileButton, profileHovImg);

		//MENU PANEL EXIT BUTTON
		exitButton.setBounds(586, 470, 109, 80);
		ImageIcon exitHovImg = new ImageIcon("res/Exit_Button_Hover.png");
		UtilityMethods.backgroundTrasparent(exitButton,exitHovImg);

		muteButton.setBounds(500,100,50,50);
		mute();

		//add to menuPanel the following buttons
		add(playButton);
		add(profileButton);
		add(exitButton);
		add(muteButton);
	}

	
	
	/**
	 * it sets the Mute JButton to the default one (Black) (Sound on)
	 */
	public void mute() {
		
		muteButton.setIcon(new ImageIcon("res/mute.png"));
		ImageIcon muteHovImg = new ImageIcon("res/muteHov.png");
		UtilityMethods.backgroundTrasparent(muteButton,muteHovImg);
	}
	
	
	
	/**
	 * it sets the Mute JButton to the red one (Muted sound)
	 */
	public void muteRed() {
		
		muteButton.setIcon(new ImageIcon("res/muteRed.png"));
		ImageIcon muteHovImg = new ImageIcon("res/muteRedHov.png");
		UtilityMethods.backgroundTrasparent(muteButton,muteHovImg);
		
	}
	
	
	
	@Override
	public void paintComponent(Graphics g) {
		super.paintComponent(g);

		Graphics2D g2 = (Graphics2D) g; //casting to a specialized class
		g2.drawImage(background, 0, 0, 1280, 720, null);
	}	
	
	
	
	public JButton getMuteButton() {
		
		return muteButton;
	}

	public JButton getExitButton() {
		return exitButton;
	}

	public JButton getProfileButton() {
		return profileButton;
	}

	public JButton getPlayButton() {
		return playButton;
	}
}