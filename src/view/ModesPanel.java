package view;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JPanel;
import model.UtilityMethods;

/**
 * this class contains all the JButtons of the modes available to play (Classic, House Rules, Deadly)
 * @author MP
 */
public class ModesPanel extends JPanel {

	private BufferedImage background = null; 
	
	private JButton classicModeButton = new JButton(new ImageIcon("res/classicMode.png"));
	private JButton houseRulesButton = new JButton(new ImageIcon("res/houseRules.png"));
	private JButton deadlyButton = new JButton(new ImageIcon("res/deadlyMode.png"));
	private JButton backArrowButton = new JButton(new ImageIcon("res/Backward_Arrow_1280x720.png"));

	private JButton rulesButton = new JButton(new ImageIcon("res/rules.png"));
	
	/**
	 * constructs a ModesPanel object and sets the bounds of the JButtons
	 */
	public ModesPanel() {
		
		setLayout(null);
		

		//BACKGROUND MENU IMAGE
		background = UtilityMethods.loadImg("res/Uno_Menu_1.png");
		
		//MODES PANEL BACKARROW BUTTON
		backArrowButton.setBounds(22, 16, 150, 111);
		ImageIcon backwardsArrowHovImg = new ImageIcon("res/Backward_Arrow_Hov_1280x720.png");
		UtilityMethods.backgroundTrasparent(backArrowButton,backwardsArrowHovImg);

		//classicModeButton BUTTON
		classicModeButton.setBounds(565, 144, 150, 55);
		ImageIcon classicModeButtonHov = new ImageIcon("res/classicModeHov.png");
		UtilityMethods.backgroundTrasparent(classicModeButton, classicModeButtonHov);

		//houseRulesButton BUTTON
		houseRulesButton.setBounds(505, 312, 270, 55);
		ImageIcon houseRulesButtonHov = new ImageIcon("res/houseRulesHov.png");
		UtilityMethods.backgroundTrasparent(houseRulesButton, houseRulesButtonHov);
		
		//deadlyButton BUTTON
		deadlyButton.setBounds(565, 470, 157, 60);
		ImageIcon deadlyButtonHov = new ImageIcon("res/deadlyModeHov.png");
		UtilityMethods.backgroundTrasparent(deadlyButton, deadlyButtonHov);

		//Rules BUTTON
		rulesButton.setBounds(565, 560, 157, 60);
		ImageIcon rulesButtonHov = new ImageIcon("res/rulesHov.png");
		UtilityMethods.backgroundTrasparent(rulesButton, rulesButtonHov);
		
		
		add(classicModeButton);
		add(houseRulesButton);
		add(deadlyButton);
		add(backArrowButton);
		add(rulesButton);
	}

	
	@Override
	public void paintComponent(Graphics g) {
		super.paintComponent(g);

		Graphics2D g2 = (Graphics2D) g; //casting to a specialized class
		g2.drawImage(background, 0, 0, 1280, 720, null);
	}	


	public JButton getDeadlyButton() {
		return deadlyButton;
	}

	public JButton getBackArrowButton() {
		return backArrowButton;
	}

	public JButton getClassicModeButton() {
		return classicModeButton;
	}

	public JButton getHouseRulesButton() {
		return houseRulesButton;
	}


	public JButton getRulesButton() {
		return rulesButton;
	}

	
}