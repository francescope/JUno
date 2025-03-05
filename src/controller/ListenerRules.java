package controller;

import java.awt.CardLayout;

import javax.swing.JPanel;

import view.RulesPanel;

public class ListenerRules {

	public ListenerRules(CardLayout cl, JPanel gamePanel, RulesPanel rulesPanel) {
		
		rulesPanel.getBackArrowButton().addActionListener(e -> {
			
			cl.show(gamePanel, "3");
		});
	}

	
}
