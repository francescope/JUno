package controller;

import java.awt.CardLayout;
import javax.swing.JFrame;
import javax.swing.JPanel;
import model.Stats;
import model.classic.GameClassic;
import model.deadly.GameDeadly;
import model.house_rules.GameHouseRules;
import model.sound.Sound;
import view.ClassicPanel;
import view.DeadlyPanel;
import view.HouseRulesPanel;
import view.MenuPanel;
import view.ModesPanel;
import view.ProfilePanel;
import view.RulesPanel;

public class JUno {
	
	public static void main(String[] args) 	{

		JFrame jframe = new JFrame(); 
		JPanel gamePanel = new JPanel();
		CardLayout cl = new CardLayout();

		jframe.setTitle("JUno");
		jframe.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		jframe.setResizable(false);
		jframe.setLocationRelativeTo(null);
		jframe.setBounds(100, 100, 1280, 720);

		Stats stats = new Stats();
		Sound sound = new Sound();
		
		sound.playMusic(14);
		
		//MENU PANEL
		MenuPanel menuPanel = new MenuPanel();

		//MODES PANEL
		ModesPanel modesPanel = new ModesPanel();

		//RULES PANEL
		RulesPanel rulesPanel = new RulesPanel();
	
		GameClassic gameClassic = new GameClassic(stats, sound);
		GameHouseRules gameHouseRules = new GameHouseRules(stats, sound);
		GameDeadly gameDeadly = new GameDeadly(stats, sound);
		
		
		//PROFILE PANEL
		ProfilePanel profilePanel = new ProfilePanel(stats);

		ClassicPanel classicPanel = new ClassicPanel(gameClassic);
		HouseRulesPanel houseRulesPanel = new HouseRulesPanel(gameHouseRules);
		DeadlyPanel deadlyPanel = new DeadlyPanel(gameDeadly);

		gameClassic.addObserver(classicPanel);
		gameHouseRules.addObserver(houseRulesPanel);
		gameDeadly.addObserver(deadlyPanel);

		ListenerClassic listenerClassic = new ListenerClassic(classicPanel, gameClassic, cl, gamePanel, sound);
		ListenerHouseRules listenerHouseRules = new ListenerHouseRules(houseRulesPanel, gameHouseRules, cl, gamePanel, sound);
		ListenerDeadly listenerDeadly = new ListenerDeadly(deadlyPanel, gameDeadly, cl, gamePanel, sound);
		
		ListenerProfile listenerProfile = new ListenerProfile(cl, gamePanel, stats, profilePanel);	
		ListenerMenu listenerMenu = new ListenerMenu(menuPanel, gamePanel, cl, jframe, classicPanel, houseRulesPanel, deadlyPanel, profilePanel, stats, sound );
		ListenerModes listenerModes = new ListenerModes(modesPanel, gamePanel, cl, gameClassic,gameHouseRules, gameDeadly, classicPanel, houseRulesPanel, deadlyPanel, sound);
		ListenerRules listenerRules = new ListenerRules(cl, gamePanel, rulesPanel);

		gamePanel.setLayout(cl);		
		gamePanel.add(menuPanel, "1");
		gamePanel.add(profilePanel, "2");
		gamePanel.add(modesPanel, "3");
		
		gamePanel.add(classicPanel,"4");
		gamePanel.add(houseRulesPanel, "5");
		gamePanel.add(deadlyPanel, "6");
		
		gamePanel.add(rulesPanel,"7");

		cl.show(gamePanel, "1");		

		jframe.add(gamePanel);
		jframe.setVisible(true);
	}
}