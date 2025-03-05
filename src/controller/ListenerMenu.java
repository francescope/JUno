package controller;

import java.awt.CardLayout;
import javax.swing.JFrame;
import javax.swing.JPanel;
import model.Stats;
import model.sound.Sound;
import view.ClassicPanel;
import view.DeadlyPanel;
import view.HouseRulesPanel;
import view.MenuPanel;
import view.ProfilePanel;

public class ListenerMenu {

	private MenuPanel menuPanel;
	private JPanel gamePanel;
	private CardLayout cl;
	private JFrame jframe;
	private ClassicPanel classicPanel;
	private HouseRulesPanel houseRulesPanel;
	private DeadlyPanel deadlyPanel;
	private ProfilePanel profilePanel;
	private Stats stats;
	private Sound sound;


	
	public ListenerMenu(MenuPanel menuPanel, JPanel gamePanel, CardLayout cl, JFrame jframe, ClassicPanel classicPanel,
			HouseRulesPanel houseRulesPanel, DeadlyPanel deadlyPanel, ProfilePanel profilePanel, Stats stats, Sound sound) {

		this.menuPanel = menuPanel;
		this.gamePanel = gamePanel;
		this.cl = cl;
		this.jframe = jframe;
		this.classicPanel = classicPanel;
		this.houseRulesPanel = houseRulesPanel;
		this.deadlyPanel = deadlyPanel;
		this.profilePanel = profilePanel;		
		this.stats = stats;
		this.sound = sound;

		profileButtonListener();
		playButtonListener();
		exitButtonListener();
		muteButtonListener();
	}



	public void profileButtonListener() {

		menuPanel.getProfileButton().addActionListener(e-> {
			
			stats.load();
			profilePanel.load();
			cl.show(gamePanel, "2");
		});
	}



	public void muteButtonListener() {
		
		menuPanel.getMuteButton().addActionListener(e-> {

			if(sound.getStatus().equals("PAUSE")) {

				sound.resume();
				menuPanel.mute();
			}

			else {
				sound.pause();
				menuPanel.muteRed();
			}
		});
	}

	

	public void playButtonListener() {

		menuPanel.getPlayButton().addActionListener(e -> {
			cl.show(gamePanel, "3");
		});
	}


	
	public void exitButtonListener() {

		menuPanel.getExitButton().addActionListener(e -> { 
			jframe.setVisible(false); //you can't see me!
			jframe.dispose(); //destroy the JFrame object
			classicPanel.getTimer().stop();
			houseRulesPanel.getTimer().stop();
			deadlyPanel.getTimer().stop();
		});
	}
}