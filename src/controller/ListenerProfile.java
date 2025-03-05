package controller;

import java.awt.CardLayout;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JFileChooser;
import javax.swing.JPanel;
import model.Stats;
import model.UtilityMethods;
import view.ProfilePanel;

public class ListenerProfile {

	private Stats stats;
	private ProfilePanel profilePanel;
	private CardLayout cl;
	private JPanel gamePanel;
	
	
	
	public ListenerProfile(CardLayout cl, JPanel gamePanel, Stats stats, ProfilePanel profilePanel) {

		this.stats = stats;
		this.profilePanel = profilePanel;
		this.cl = cl;
		this.gamePanel = gamePanel;

		backArrowListener();
		resetListener();
		changePropicListener();
	}

	

	public void changePropicListener() {

		profilePanel.getChangePropic().addActionListener(e -> {

			JFileChooser fileChooser = new JFileChooser();

			int returnValue = fileChooser.showOpenDialog(null);
			
			if (returnValue == JFileChooser.APPROVE_OPTION) {

				File selectedFile = fileChooser.getSelectedFile();
				String path = selectedFile.getAbsolutePath();
				
				//extensions accepted
				if(path.endsWith("png") || path.endsWith("jpg")) {

					BufferedImage resizeImage = null;
					
					try {
						resizeImage = UtilityMethods.resizeImage(UtilityMethods.loadImg(path), 120, 120);
					} catch (IOException e1) {
						e1.printStackTrace();
					}

					profilePanel.getPropic().setIcon(new ImageIcon(resizeImage));

					try {
						ImageIO.write(resizeImage, "png", new File("propic/"+selectedFile.getName()));
					} catch (IOException e1) {
						e1.printStackTrace();
					}

					stats.setPropicFilePath("propic/"+selectedFile.getName());
				}
			}
		});
	}

	

	public void resetListener() {
		profilePanel.getResetButton().addActionListener(e -> {

			stats.reset();
			profilePanel.reset();
		});
	}


	
	public void backArrowListener() {

		profilePanel.getBackArrowButton().addActionListener(e -> {

			stats.setUsername(profilePanel.getNicknameField().getText());
			stats.save();
			cl.show(gamePanel, "1");
		});
	}
}