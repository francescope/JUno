package controller;

import java.awt.CardLayout;
import javax.swing.JPanel;
import model.classic.GameClassic;
import model.deadly.GameDeadly;
import model.house_rules.GameHouseRules;
import model.sound.Sound;
import view.ClassicPanel;
import view.DeadlyPanel;
import view.HouseRulesPanel;
import view.ModesPanel;

public class ListenerModes {

	private ModesPanel modesPanel;
	private JPanel gamePanel;
	private CardLayout cl;
	private GameClassic gameClassic;
	private GameHouseRules gameHouseRules;
	private ClassicPanel classicPanel;
	private HouseRulesPanel houseRulesPanel;
	private GameDeadly gameDeadly;
	private DeadlyPanel deadlyPanel;
	private Sound sound;


	public ListenerModes(ModesPanel modesPanel, JPanel gamePanel, CardLayout cl, GameClassic gameClassic, GameHouseRules gameHouseRules,
			GameDeadly gameDeadly, ClassicPanel classicPanel, HouseRulesPanel houseRulesPanel, DeadlyPanel deadlyPanel, Sound sound) {

		this.modesPanel = modesPanel;
		this.gamePanel = gamePanel;
		this.cl = cl;
		this.gameClassic = gameClassic;
		this.gameHouseRules = gameHouseRules;

		this.houseRulesPanel = houseRulesPanel;
		this.classicPanel = classicPanel;
		this.deadlyPanel = deadlyPanel;
		this.gameDeadly = gameDeadly;

		this.sound = sound;

		classicButtonListener();
		houseRulesButtonListener();
		deadlyButtonListener();

		backArrowListener();
		rulesButtonListener();
	}

	

	



	public void backArrowListener() {

		modesPanel.getBackArrowButton().addActionListener(e -> {

			cl.show(gamePanel, "1");
		});
	}


	
	public void classicButtonListener() {

		modesPanel.getClassicModeButton().addActionListener(e -> {

			cl.show(gamePanel, "4");

			sound.stop();
			sound.playMusic(1);
			gameClassic.startGameThread();
			classicPanel.getTimer().start();
		});
	}

	
	
	public void houseRulesButtonListener() {

		modesPanel.getHouseRulesButton().addActionListener( e-> {

			cl.show(gamePanel, "5");

			sound.stop();
			sound.playMusic(0);
			gameHouseRules.startGameThread();
			houseRulesPanel.getTimer().start();
		});
	}



	public void deadlyButtonListener() {

		modesPanel.getDeadlyButton().addActionListener( e-> {

			cl.show(gamePanel, "6");

			sound.stop();
			sound.playMusic(2);
			gameDeadly.startGameThread();
			deadlyPanel.getTimer().start();
		});
	}
	
	public void rulesButtonListener() {

		modesPanel.getRulesButton().addActionListener(e -> {
			
			cl.show(gamePanel, "7");
		});
		
	}
	
}