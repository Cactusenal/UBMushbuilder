package net.codejava;

import java.awt.Color;

import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JLayeredPane;

public class TuileCase {
	String terrain = "Brume";
    JButton caseBackground;
    Tuile parentTuile;
    String casePosition;
    
    Integer xPos;
    Integer yPos;
    
    String building = "";
    String [] buildingParts = {};
    
    public Object[][] buildingsPowered = {};
    
    Integer prodFibre = 0;
    Integer prodSpore = 0;
    Integer prodSuc = 0;
    Integer prodPhospho = 0;
    Integer prodCoins = 0;
    
    //Constructeur
    public TuileCase(Tuile tuileFrom, JLayeredPane panel, String position, int x, int y, boolean isViewer) {
    	parentTuile = tuileFrom;
    	casePosition = position;
    	xPos = x;
    	yPos = y;
    	caseBackground = new JButton();
    	caseBackground.setBackground(Color.blue);
    	caseBackground.setFont(new Font("Monospace", Font.PLAIN, 12));
    	TuileCase thisCase = this;
    	// bouton listener
    	caseBackground.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				Main.gameController.clickOnCarteCase(thisCase, isViewer);
			}
		});
    	if (xPos == 1 && yPos == 1) {
    		caseBackground.setFont(new Font("Monospace", Font.BOLD, 14));
    	}
    }

	void changeTerrain() {
    	setTerrain(Main.settings.getRandomTerrainValue());
	}
	
	public String getTerrain() {
		return terrain;
	}
	
	public void setTerrain(String newTerrain) {
		terrain = newTerrain;
	}
	
	public void updateCaseProduction() {
    	setProdFibre();
    	setProdSpore();
    	setProdSuc();
    	setProdPhospho();
    	setProdCoins();
	}
	
	public void setBuilding(String buildName) {
		if (building.equals("")) {
			building = buildName;
			Main.settings.AddDebugLog("no building");
		} else {
			Main.settings.AddDebugLog("build to set: " + buildName);
			Main.settings.AddDebugLog("present building: " + building);
			Main.settings.AddDebugLog("buildingConditions: " + Main.settings.buildingConditions.get(buildName)[0]);

			if (Main.settings.buildingConditions.get(buildName)[0].equals(building)) {
				addBuildingPart(buildName);
			} else {
				building = buildName;
				buildingParts = null;
			}
		}
	}
	
	public void addBuildingPart(String buildingPart) {
		Integer buildingPartLength = buildingParts != null ? buildingParts.length : 0;
		String[] newBuildingParts = new String[buildingPartLength + 1];
		for (int i = 0; i < buildingPartLength; i++) {
			newBuildingParts[i] = buildingParts[i];
		}
		newBuildingParts[buildingPartLength] = buildingPart;
		buildingParts = newBuildingParts;		
	}
	
	public int getRessource(String ressourceToGet) {
		switch(ressourceToGet) {
			case "prodFibre":
				return prodFibre;
			case "prodSpore":
				return prodSpore;
			case "prodSuc":
				return prodSuc;
			case "prodPhospho":
				return prodPhospho;
			case "prodCoins":
				return prodCoins;
			default:
				Main.settings.AddDebugLog("This ressource is unknown: " + ressourceToGet);
				return 0;
		}
	}
	
	public void setProdFibre() {
    	prodFibre = getProdFromHashMap(Main.settings.fibreProdRules);
	}
	
	public void setProdSpore() {
		prodSpore = getProdFromHashMap(Main.settings.sporeProdRules);
	}
	
	public void setProdSuc() {
    	prodSuc = getProdFromHashMap(Main.settings.sucProdRules);
	}
	
	public void setProdPhospho() {
    	prodPhospho = getProdFromHashMap(Main.settings.phosphoProdRules);
	}
	
	public void setProdCoins() {
		prodCoins = getProdFromHashMap(Main.settings.coinProdRules);
	}
	
	public Integer getProdFromHashMap(HashMap<String, String[]> baseProductionRules) {
		Integer prod = 0;
		String[] possibleBiomes = {"Brume", "Plaine", "Hauteurs", "Foret", "Désert", "Marais"};
        // Convert String Array to List
        List<String> possibleBiomesList = Arrays.asList(possibleBiomes);
        // ruleValuesExample = ["biome/building", "distance", "valeur de production", "terrains,oula,regle,nesappliquepas"];
		for (String ruleName : baseProductionRules.keySet()) {
			String[] ruleValues = baseProductionRules.get(ruleName);
			// Extract rules infos
			String caseType = ruleValues[0];
			Integer distanceValue = Integer.parseInt(ruleValues[1]);
			Integer prodValue = Integer.parseInt(ruleValues[2]);
//			Main.settings.AddDebugLog("[getProdFromHashMap]: prodValue: " + prodValue);
			String[] biomeToNotApplyString = ruleValues.length > 3 ? ruleValues[3].split(",") : null;
			// Determine if it is biome or building rule
			Boolean isBiome = possibleBiomesList.contains(caseType);
			String typeToCompare = isBiome ? terrain : building;
			// If it is a building, check if energy is necessary
			Boolean isEnergyDependant = false;
			if (!isBiome && caseType.contains("+")) {
				caseType = caseType.replace("+", "");
				isEnergyDependant = true;
			}
			// Determine on wich biomes to apply
			List<String> biomesToNotApply = null;
			Boolean applyOnAllBiome = false;				
			if (biomeToNotApplyString != null) {
				biomesToNotApply = Arrays.asList(biomeToNotApplyString);
			}
			else {
				applyOnAllBiome = true;
			}
			// Calculate productions
			if (distanceValue == 0 && (caseType.equals(typeToCompare) || (!isBiome && buildingParts!= null && buildingParts.length > 0 && Arrays.stream(buildingParts).anyMatch(caseType :: equals)))) {
				if (!isEnergyDependant || checkIfBuildingPowered(caseType)) {
					prod += prodValue;
//					Main.settings.AddDebugLog("[getProdFromHashMap]: adding prod: " + prodValue);
				}
			}
			else if (distanceValue > 0) {
				TuileCase[] tuilesWithinDistance = getCasesFromDistance(distanceValue);
				for (TuileCase relativeCase : tuilesWithinDistance) {
					if (relativeCase != null) {
						typeToCompare = isBiome ? relativeCase.terrain : relativeCase.building;
						// TODO: authorize multiple identical building parts
						if ((caseType.equals(typeToCompare) || (!isBiome && relativeCase.buildingParts!= null && relativeCase.buildingParts.length > 0 && Arrays.stream(relativeCase.buildingParts).anyMatch(caseType :: equals))) && (applyOnAllBiome || !biomesToNotApply.contains(terrain))) {
							if (!isEnergyDependant || relativeCase.checkIfBuildingPowered(caseType)) {
								prod += prodValue;
//								Main.settings.AddDebugLog("[getProdFromHashMap]: adding prod: " + prodValue);								
							}
						}
					}
				}
			}
		}
//		Main.settings.AddDebugLog("[getProdFromHashMap]: prod: " + prod);
		return prod;
	}
	
	public boolean checkIfBuildingPowered(String buildingName) {
		if (building.equals("")) {
			Main.settings.AddDebugLog("[checkIfBuildingPowered] Unexpected: no building to check if powered");
			return false;
		} else if (!buildingName.equals(building) && buildingParts.length > 0 && !Arrays.stream(buildingParts).anyMatch(buildingName :: equals)) {
			Main.settings.AddDebugLog("[checkIfBuildingPowered] Unexpected: no building or building part with this name: " + buildingName + " on this case");
			Main.settings.AddDebugLog("[checkIfBuildingPowered] " + buildingParts.length + ", " + buildingParts);
			return false;
		}
		Integer buildX = getCaseXPos();
		Integer buildY = getCaseYPos();
		for (TuileCase inRangeCase : getCasesFromDistance(Main.settings.maxGeneratorDistance)) {
//			Main.settings.AddDebugLog("[checkIfBuildingPowered] building here is :" + inRangeCase.building);
			for (Object[] buildingPowered : inRangeCase.buildingsPowered) {
//				Main.settings.AddDebugLog("[checkIfBuildingPowered] building powered here is :" + (String)buildingPowered[0]);
				if (buildingName.equals((String)buildingPowered[0]) && (Integer)buildingPowered[1] == buildX && (Integer)buildingPowered[2] == buildY) {
//					Main.settings.AddDebugLog("[checkIfBuildingPowered] building powered!");
					return true;
				}
			}
		}
		return false;
	}
		
	Integer getCaseXPos() {
		return parentTuile.xPos * 3 + xPos;
	}
	
	Integer getCaseYPos() {
		return parentTuile.yPos * 3 + yPos;
	}
	
	TuileCase getRelativeCase(String orientation) {
		TuileCase relativeTuileCase = null;
		
		int relativeTuileCasex = xPos;
		int relativeTuileCasey = yPos;
		Tuile relativeTuileCaseParent = parentTuile;
		
		switch (orientation) {
			case "Y-":
				relativeTuileCasey -= 1;
				break;
			case "X+":
				relativeTuileCasex += 1;
				break;
			case "Y+":
				relativeTuileCasey += 1;
				break;
			case "X-":
				relativeTuileCasex -= 1;
				break;
			default:
				return null;
		}
		if(relativeTuileCasex < 0) {
			relativeTuileCasex = 2;
			relativeTuileCaseParent = parentTuile.getRelativeTuile("X-");
		} else if(relativeTuileCasex > 2) {
			relativeTuileCasex = 0;
			relativeTuileCaseParent = parentTuile.getRelativeTuile("X+");
		}
		if(relativeTuileCasey < 0) {
			relativeTuileCasey = 2;
			relativeTuileCaseParent = parentTuile.getRelativeTuile("Y-");
		} else if(relativeTuileCasey > 2) {
			relativeTuileCasey = 0;
			relativeTuileCaseParent = parentTuile.getRelativeTuile("Y+");
		}
		if (relativeTuileCaseParent == null) {
			return null;
		}
		relativeTuileCase = relativeTuileCaseParent.cases[relativeTuileCasex][relativeTuileCasey];
		return relativeTuileCase;
	}
	
	public TuileCase[] getCasesFromDistance(int caseDistance) {
		TuileCase[] casesArray = {};
		List<TuileCase> casesList = new ArrayList<TuileCase>(Arrays.asList(casesArray));
		int maxColumn = parentTuile.isViewTuile ? 3 : Main.cartePanel.column * 3;
		int maxLign = parentTuile.isViewTuile ? 3 : Main.cartePanel.lign * 3;
		int xCase = parentTuile.xPos * 3 + xPos;
		int yCase = parentTuile.yPos * 3 + yPos;
		int minX = Math.max(0, xCase - caseDistance);
		int minY = Math.max(0, yCase - caseDistance);
		int maxX = Math.min(maxColumn -1, xCase + caseDistance);
		int maxY = Math.min(maxLign - 1, yCase + caseDistance);

		for (int x = minX; x < maxX + 1; x++) {
			for (int y = minY; y < maxY + 1; y++) {
				// Main.settings.AddDebugLog("[" + x / 3 + ", " + y / 3 + "], [" + x % 3 + ", " + y % 3 + "]");
				if ((Math.abs(x - xCase) + Math.abs(y - yCase)) < (caseDistance + 1) && !(x == xCase && y == yCase)) {
//					 && (!this.parentTuile.isViewTuile || (x / 3 == parentTuile.xPos && y / 3 == parentTuile.yPos))
					if (this.parentTuile.isViewTuile) {
						casesList.add(parentTuile.cases[x][y]);
					}
					else {
						casesList.add(Main.cartePanel.getTuile(x / 3, y / 3).cases[x % 3][y % 3]);
					}
				}
			}
		}
		casesArray = casesList.toArray(casesArray);
//		Main.settings.AddDebugLog("Nombre de cases retournées: " + casesArray.length);
		return casesArray;
	}
	
	public void updateFilterView() {
		Main.filterViews.updateCaseView(this);
	}
}

