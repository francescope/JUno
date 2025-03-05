package model;

import java.io.FileWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

/** This class contains all of the player's stats
 * 
 * @author MP
 *
 */
public class Stats {

	private String propicFilePath;
	
	private String username;
	private int matchesPlayed;
	private int matchesLost;
	private int matchesWon;
	private int level;
	
	
	
	/**
	 * Constructs a Stats object and loads all the parameters from a txt file
	 */
	public Stats() {
		
		load();
	}

	
	
	/**
	 * it loads all the stats contained in the txt file into the local fields of the class
	 */
	public void load() {
		
		List<String> allLines = null;
		
		String fileName = "stats.txt";
		Path path = Paths.get(fileName);
		try {
			byte[] bytes = Files.readAllBytes(path);
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		try {
			allLines = Files.readAllLines(path, StandardCharsets.UTF_8);
			
			
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		username = allLines.get(0);
		matchesPlayed = Integer.parseInt(allLines.get(1));
		matchesLost = Integer.parseInt(allLines.get(2));
		matchesWon = Integer.parseInt(allLines.get(3));
		level = Integer.parseInt(allLines.get(4));
		propicFilePath = allLines.get(5);
	}
	
	
	
	/**
	 * it saves all the local fields of the class into the txt file
	 */
	public void save() {
		
		 try {
		      FileWriter myWriter = new FileWriter("stats.txt");
		      myWriter.write(username+"\n");
		      myWriter.write(""+matchesPlayed+"\n");
		      myWriter.write(""+matchesLost+"\n");
		      myWriter.write(""+matchesWon+"\n");
		      myWriter.write(""+level+"\n");
		      
		      myWriter.write(propicFilePath);
		      
		      myWriter.close();
		      
		    } catch (IOException e) {
		      e.printStackTrace();
		    }
	}
	
	
	
	/**
	 * it resets the local fields and the txt values with the default parameter
	 */
	public void reset() {
				
		username = "username";
		matchesPlayed = 0;
		matchesLost = 0;
		matchesWon = 0;
		level = 0;
		
		 try {
		      FileWriter myWriter = new FileWriter("stats.txt");
		      myWriter.write("username"+"\n");
		      myWriter.write("0"+"\n");
		      myWriter.write("0"+"\n");
		      myWriter.write("0"+"\n");
		      myWriter.write("0"+"\n");
		      
		      myWriter.write(propicFilePath);
		      
		      myWriter.close();
		      
		    } catch (IOException e) {
		      e.printStackTrace();
		    }
	}
	
	
	
	//SETTER AND SETTER
	public String getPropicFilePath() {
		return propicFilePath;
	}

	public void setPropicFilePath(String propicFilePath) {
		this.propicFilePath = propicFilePath;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public int getMatchesPlayed() {
		return matchesPlayed;
	}

	public void setMatchesPlayed(int matchesPlayed) {
		this.matchesPlayed = matchesPlayed;
	}

	public int getMatchesLost() {
		return matchesLost;
	}

	public void setMatchesLost(int matchesLost) {
		this.matchesLost = matchesLost;
	}

	public int getMatchesWon() {
		return matchesWon;
	}

	public void setMatchesWon(int matchesWon) {
		this.matchesWon = matchesWon;
	}

	public int getLevel() {
		return level;
	}

	public void setLevel(int level) {
		this.level = level;
	}
}