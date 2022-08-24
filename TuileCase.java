package net.codejava;

import java.awt.Color;

import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JLayeredPane;
import javax.swing.JOptionPane;

public class TuileCase {
	String terrain = "Brume";
    JButton caseBackground;
    Tuile parentTuile;
    String casePosition;
    
    // Preview
    JFrame infoFrame;
    JDialog infoDialog;    
   
    // Position
    Integer xPos;
    Integer yPos;
    
    //Productions
    Integer prodFibre = 0;
    Integer prodSpore = 0;
    Integer prodSuc = 0;
    Integer prodPhospho = 0;
    Integer prodCoins = 0;
    // For iteration
    Integer remainingFibre = 0;
    Integer remainingSpore = 0;
    Integer remainingSuc = 0;
    Integer remainingPhospho = 0;
    
    //Buildings
    String building = "";
    String [] floorBuildingParts = Main.settings.emptyStringArray;
    String [] wallBuildingParts = Main.settings.emptyStringArray;
    String [] roofBuildingParts = Main.settings.emptyStringArray;
    Integer roadLevel = 0;
    Integer sucLevel = 0;
    Boolean isSucFed = false;
    Boolean isActive = false;
    Boolean isRuins = false;
    // For construction
    String inConstruction = "";
    Integer buildFibre = 0;
    Integer buildSpore = 0;
    Integer buildSuc = 0;
    Integer buildPhospho = 0;
    // For generators
    public Object[][] buildingsPowered = {};
    
    
    //Constructeur
    public TuileCase(Tuile tuileFrom, JLayeredPane panel, String position, int x, int y, boolean isViewer) {
    	parentTuile = tuileFrom;
    	//TODO: position still necessary?
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
    	
    	infoFrame = new JFrame();
        infoDialog = new JDialog(infoFrame);
        
    	caseBackground.addMouseListener(new java.awt.event.MouseAdapter() {
    	    public void mouseEntered(java.awt.event.MouseEvent evt) {
    	    	switch (Main.gameController.overviewSelector.getItemAt(Main.gameController.overviewSelector.getSelectedIndex())) {
		    		case "Cases" :
		    			showPanelCaseInfo();
		    			break;
		    		case "Tuiles":
		    			parentTuile.showPanelTuileInfo();
		    			break;
		    	}
    	    }

			public void mouseExited(java.awt.event.MouseEvent evt) {
    	    	switch (Main.gameController.overviewSelector.getItemAt(Main.gameController.overviewSelector.getSelectedIndex())) {
		    		case "Cases" :
		    			hidePanelCaseInfo();
		    			break;
		    		case "Tuiles":
		    			parentTuile.hidePanelTuileInfo();
		    			break;
		    	}
    	    }
    	});
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
	
	public void startConstruction(String buildName) {
		if (buildName.equals("Route")) {
			// Construction de route
			if (roadLevel == 0) {
				setConstructionStart(buildName);
			} else {
	    		JOptionPane.showMessageDialog(Main.frame,
	    			    "Road already existing here",
	    			    "Inane warning",
	    			    JOptionPane.WARNING_MESSAGE);
			}
		} else if (building.equals("")) {
			// Construction sur terrain vide
			setConstructionStart(buildName);
		} else {
			// Terrain déjà occupé
			if (Main.settings.buildingConditions.get(buildName)[0].split(",")[0].equals(building)) {
				// Construction de module
				setConstructionStart(buildName);
			} else {
				// Replace existing building ?
				if (setConstructionStart(buildName)) {
					floorBuildingParts = Main.settings.emptyStringArray;
					wallBuildingParts = Main.settings.emptyStringArray;
					roofBuildingParts = Main.settings.emptyStringArray;
				}
			}
		}
	}
	
	private boolean setConstructionStart(String buildName) {
		if(!(parentTuile.getFreeInhabitants() > 0)) {
    		JOptionPane.showMessageDialog(Main.frame,
    			    "no Inhabitant free for construction",
    			    "Inane warning",
    			    JOptionPane.WARNING_MESSAGE);
    		return false;
		}
		inConstruction = buildName;
		Object[] inHabitantWorkPlace = {xPos, yPos, "builder"};
		parentTuile.inhabitantWork.add(inHabitantWorkPlace);
		return true;
	}
	
	public void setFinishedBuilding(String buildName) {
		if (buildName.equals("Route")) {
			roadLevel++;
		} else if (building.equals("")) {
			building = buildName;
//			Main.settings.AddDebugLog("no building");
		} else {
			String[] buildingReq = Main.settings.buildingConditions.get(buildName)[0].split(",");
			if (buildingReq[0].equals(building)) {
				addBuildingPart(buildName, buildingReq[1]);
			} else {
				// Replace existing building ?
				building = buildName;
				floorBuildingParts = Main.settings.emptyStringArray;
				wallBuildingParts = Main.settings.emptyStringArray;
				roofBuildingParts = Main.settings.emptyStringArray;
				sucLevel = 0;
			}
		}
		inConstruction = "";
		isSucFed = true;
		isActive = false;
		sucLevel = Main.settings.maxSucLevel;
		buildFibre = 0;
		buildSpore = 0;
		buildSuc = 0;
		buildPhospho = 0;
		parentTuile.getAndUpdatePopulation();
		Object[] inHabitantWorkPlace = {xPos, yPos, "builder"};
		if(!parentTuile.removeInhabitant(inHabitantWorkPlace)) {
			Main.settings.AddDebugLog("ERROR: do not suceed to free the inhabitant");
		}
	}
	
	public void addBuildingPart(String buildingPart, String localisation) {
		String[] oldBuildingParts = getBuildingPartsList(localisation);
		
		Integer buildingPartLength = oldBuildingParts != null ? oldBuildingParts.length : 0;
		String[] newBuildingParts = new String[buildingPartLength + 1];
		for (int i = 0; i < buildingPartLength; i++) {
			newBuildingParts[i] = oldBuildingParts[i];
		}
		newBuildingParts[buildingPartLength] = buildingPart;
		switch (localisation) {
			case "sol":
				floorBuildingParts = newBuildingParts;
				break;
			case "mur":
				wallBuildingParts = newBuildingParts;
				break;
			case "toit":
				roofBuildingParts = newBuildingParts;
				break;
			default:
				Main.settings.AddDebugLog("Part localisation is unknown: " + localisation);
		}
	}
	
	public String[] getBuildingPartsList(String partsLocation) {
		String[] buildingPartsList = {};
		switch (partsLocation) {
			case "sol":
				buildingPartsList = floorBuildingParts;
				break;
			case "mur":
				buildingPartsList = wallBuildingParts;
				break;
			case "toit":
				buildingPartsList = roofBuildingParts;
				break;
			default:
				Main.settings.AddDebugLog("Part localisation is unknown: " + partsLocation);
		}
		return buildingPartsList;
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
	
	public void setRemainingProd() {
		updateCaseProduction();
	    remainingFibre = prodFibre;
	    remainingSpore = prodSpore;
	    remainingSuc = prodSuc;
	    remainingPhospho = prodPhospho;
	}
	
	public Integer getProdFromHashMap(HashMap<String, String[]> baseProductionRules) {
		Integer prod = 0;
		String[] possibleBiomes = {"Brume", "Plaine", "Hauteurs", "Foret", "Désert", "Marais"};
        // Convert String Array to List
        List<String> possibleBiomesList = Arrays.asList(possibleBiomes);
        // ruleValuesExample = ["biome/building", "distance", "valeur de production", "terrains,oula,regle,nesappliquepas"];
		for (String ruleName : baseProductionRules.keySet()) {
			String[] ruleValues = baseProductionRules.get(ruleName);
			if (ruleValues.length > 4 && !ruleValues[4].equals("")) {
				String[] allowedSeasons = ruleValues[4].split(",");
	    		if (!Arrays.stream(allowedSeasons).anyMatch(Main.settings.currentSeason::equals)) {
	    			continue;
	    		}
			}
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
//			Boolean isCaseTypePartOfBuildings = (!isBiome && buildingParts!= null && buildingParts.length > 0 && Arrays.stream(buildingParts).anyMatch(caseType :: equals));
			if (distanceValue == 0 && (caseType.equals(typeToCompare) || (!isBiome && isBuildTypePartOfBuildings(this, caseType)))) {
				
				if (!isEnergyDependant || getBuildingPowerSourcePosition(caseType) != null) {
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
						if ((caseType.equals(typeToCompare) || (!isBiome && isBuildTypePartOfBuildings(relativeCase, caseType))) && (applyOnAllBiome || !biomesToNotApply.contains(terrain))) {
							if (!isEnergyDependant || relativeCase.getBuildingPowerSourcePosition(caseType) != null) {
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
	
	Boolean isBuildTypePartOfBuildings(TuileCase tuile, String buildType) {
		Boolean isPart = false;
		String[][] buildingPartLists = {tuile.floorBuildingParts, tuile.wallBuildingParts, tuile.roofBuildingParts};
		for (String[] buildingPartList : buildingPartLists) {
			if (buildingPartList!= null && buildingPartList.length > 0 && Arrays.stream(buildingPartList).anyMatch(buildType :: equals)) {
				isPart = true;
				break;
			}
		}
		return isPart;
	}	

	
	public Integer[] getBuildingPowerSourcePosition(String buildingName) {
		Integer[] powerPosition = null;
		if (building.equals("")) {
			Main.settings.AddDebugLog("[getBuildingPowerSourcePosition] Unexpected: no building to check if powered");
		} else if (!building.equals(buildingName) && !isBuildTypePartOfBuildings(this, buildingName)) {
				//buildingParts.length > 0 && !Arrays.stream(buildingParts).anyMatch(buildingName :: equals)) {
			Main.settings.AddDebugLog("[getBuildingPowerSourcePosition] Unexpected: no building or building part with this name: " + buildingName + " on this case");
		}
		Integer buildX = getCaseXPos();
		Integer buildY = getCaseYPos();
		for (TuileCase inRangeCase : getCasesFromDistance(Main.settings.maxGeneratorDistance)) {
//			Main.settings.AddDebugLog("[getBuildingPowerSourcePosition] building here is :" + inRangeCase.building);
			for (Object[] buildingPowered : inRangeCase.buildingsPowered) {
//				Main.settings.AddDebugLog("[getBuildingPowerSourcePosition] building powered here is :" + (String)buildingPowered[0]);
				if (buildingName.equals((String)buildingPowered[0]) && (Integer)buildingPowered[1] == buildX && (Integer)buildingPowered[2] == buildY) {
//					Main.settings.AddDebugLog("[getBuildingPowerSourcePosition] building powered!");
					powerPosition = new Integer[]{inRangeCase.getCaseXPos(), inRangeCase.getCaseYPos()};
					return powerPosition;
				}
			}
		}
		return powerPosition;
	}
	
	void removeBuildingPowered(String buildingName, int xPos, int yPos) {
		Main.settings.AddDebugLog("removeBuildingPowered");
		List<Object[]> buildingsPoweredList = new ArrayList<Object[]>();
		for (Object [] buildingPowered : buildingsPowered) {
			if ((String)buildingPowered[0] == buildingName && (Integer)buildingPowered[1] == xPos && (Integer)buildingPowered[2] == yPos) {
			} else {
				buildingsPoweredList.add(buildingPowered);				
			}
		}
		Object [][] buildingsPoweredArray =  new Object[buildingsPoweredList.size()][4];
		buildingsPoweredList.toArray(buildingsPoweredArray);
		buildingsPowered = buildingsPoweredArray;
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
		return getCasesFromDistance(caseDistance, 0);
	}
		
	// Original case is excluded if minDistance >= 0
	public TuileCase[] getCasesFromDistance(int caseDistance, int minDistance) {
		TuileCase[] casesArray = {};
		if (minDistance >= caseDistance) {
			return casesArray;
		}
		
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
				int distance = Math.abs(x - xCase) + Math.abs(y - yCase);
				if (distance < (caseDistance + 1) && distance > minDistance) {
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
	
	public void getRessourcesForConstruction(TuileCase exploitedCase, Integer[] priceInRessource) {
		feedWithRessource("Fibre", exploitedCase.remainingFibre, priceInRessource[0]);
		feedWithRessource("Spore", exploitedCase.remainingSpore, priceInRessource[1]);
		feedWithRessource("Suc", exploitedCase.remainingSuc, priceInRessource[2]);
		feedWithRessource("Phospho", exploitedCase.remainingPhospho, priceInRessource[3]);
	}

	public void iterateConstruction() {
		if (!inConstruction.equals("") ) {
			// Get construction price
			String[] priceInRessourceString = Main.settings.getRessourcePrice(inConstruction).split(",");
			Integer[] priceInRessource = new Integer[priceInRessourceString.length];
			for (int i = 0; i < priceInRessourceString.length; i++) {
				priceInRessource[i] = Integer.parseInt(priceInRessourceString[i]);
			}
			// Getting ressource on present case
			getRessourcesForConstruction(this, priceInRessource);
			Integer rangeIndex = 1;
			// Getting ressources line by line
			while (rangeIndex <= Main.settings.constructRange && !isBuildingFinished(priceInRessource)) {
				TuileCase[] inRangeCases = getCasesFromDistance(rangeIndex, rangeIndex - 1);
				Integer casesIndex = 0;
				while (casesIndex < inRangeCases.length && !isBuildingFinished(priceInRessource)) {
					TuileCase inRangeCase = inRangeCases[casesIndex];
					if (inRangeCase.parentTuile.owner.equals(parentTuile.owner)) {
						getRessourcesForConstruction(inRangeCase, priceInRessource);					
					}
					casesIndex++;
				}
				rangeIndex++;
			}
			// Set building once building is finished
			if (isBuildingFinished(priceInRessource)) {
				setFinishedBuilding(inConstruction);
			}
		}
	}
	
	Boolean isBuildingFinished(Integer[] priceInRessource) {
		return buildFibre == priceInRessource[0] && buildSpore == priceInRessource[1] && buildSuc == priceInRessource[2] && buildPhospho == priceInRessource[3];
	}

	private Integer feedWithRessource(String ressource, int prod, Integer requiredRessource) {
		Integer remainingRessource = 0;		
		switch(ressource) {
			case "Fibre":
				if (buildFibre + prod > requiredRessource) {
					remainingRessource = buildFibre + prod - requiredRessource;
					buildFibre = requiredRessource;
					remainingFibre = remainingRessource;
					return remainingRessource;
				} else {
					buildFibre += prod;
					return 0;
				}
			case "Spore":
				if (buildSpore + prod > requiredRessource) {
					remainingRessource = buildSpore + prod - requiredRessource;
					buildSpore = requiredRessource;
					remainingSpore  = remainingRessource;
					return remainingRessource;
				} else {
					buildSpore += prod;
					return 0;
				}
			case "Suc":
				if (buildSuc + prod > requiredRessource) {
					remainingRessource = buildSuc + prod - requiredRessource;
					buildSuc = requiredRessource;
					remainingSuc  = remainingRessource;
					return remainingRessource;
				} else {
					buildSuc += prod;
					return 0;
				}
			case "Phospho":
				if (buildPhospho + prod > requiredRessource) {
					remainingRessource = buildPhospho + prod - requiredRessource;
					buildPhospho = requiredRessource;
					remainingPhospho = remainingRessource;
					return remainingRessource;
				} else {
					buildPhospho += prod;
					return 0;
				}
			case "SucCons":
				if (building.equals("")) {
					return prod;
				}
				if (sucLevel + prod > requiredRessource) {
					remainingRessource = sucLevel + prod - requiredRessource;
					sucLevel = requiredRessource;
					remainingSuc  = remainingRessource;
					return remainingRessource;
				} else {
					sucLevel += prod;
					return 0;
				}
		}
		Main.settings.AddDebugLog("[feedWithRessource] unexpected ressource exploited");
		return remainingRessource;
	}

	// Suc Cons.
	public void consumeSucAndSetActivity() {
		if (building.equals("")) {
			return;
		}
		int consumption = getSucTotalCons();
		if (!isSucFed || consumption > sucLevel) {
			// Building does not have enough suc anymore
			isActive = false;
			Main.settings.AddDebugLog("[consumeSucAndSetActivity] " + getCaseXPos() +":"+ getCaseYPos() + " , isSucFed: " + isSucFed + " ,consumption: " + consumption);
			if (Main.settings.isGeneratingPower(building)) {
				// Desactivate all porwered buildings from this TuileCase
				buildingsPowered = new Object[0][0];
				if (isSucFed) {Main.settings.AddDebugLog("[consumeSucAndSetActivity] Generator is out of suc! Desactivating powered buildings");}
			} else if (!building.equals("")) {
				if (Main.settings.getPowerCons(building) > 0) {
					// Desactivate the building from this TuileCase if it is powered
					Integer[] sourcePositionMain = getBuildingPowerSourcePosition(building);
					if (sourcePositionMain != null) {
						TuileCase generatorSourceMain = Main.cartePanel.getTuileCase(sourcePositionMain[0], sourcePositionMain[1]);
						generatorSourceMain.removeBuildingPowered(building, getCaseXPos(), getCaseYPos());
						Main.settings.AddDebugLog("[consumeSucAndSetActivity] Building is out of suc! Desactivating power connection of " + getCaseXPos() + ";" + getCaseYPos() + " from " + sourcePositionMain[0] + ";" +sourcePositionMain[1]);
					}
				}
				// Desactivate the building parts from this TuileCase they are powered
        		String[][] buildingPartLists = {floorBuildingParts, wallBuildingParts, roofBuildingParts};
        		for (String[] buildingPartList : buildingPartLists) {
        			for (String buildingPart : buildingPartList) {
        				if (Main.settings.getPowerCons(buildingPart) > 0) {
        					Integer[] sourcePositionPart = getBuildingPowerSourcePosition(buildingPart);
        					if (sourcePositionPart != null) {
        						TuileCase generatorSourcePart = Main.cartePanel.getTuileCase(sourcePositionPart[0], sourcePositionPart[1]);
        						generatorSourcePart.removeBuildingPowered(buildingPart, getCaseXPos(), getCaseYPos());
        						Main.settings.AddDebugLog("[consumeSucAndSetActivity] Building part is out of suc! Desactivating power connection of " + getCaseXPos() + ";" + getCaseYPos() + " from " + sourcePositionPart[0] + ";" +sourcePositionPart[1]);
        					}
        				}
        			}
        		}
			}
			// Desactivate building
			isSucFed = false;
			isRuins = true;
		} else {
			sucLevel -= consumption;
			isActive = true;
			isRuins = false;
		}
		parentTuile.getAndUpdatePopulation();
	}
	
	public int getSucTotalCons() {
		int consumption = Main.settings.getSucCons(building);
		String[][] buildingPartLists = {floorBuildingParts, wallBuildingParts, roofBuildingParts};
		for (String[] buildingPartList : buildingPartLists) {
			for (int i = 0; i < (buildingPartList != null ? buildingPartList.length : 0); i++) {
				consumption += Main.settings.getSucCons(buildingPartList[i]);
			}
		}
		return consumption;
	}

	public void feedSucStock() {
		int maxSucLevel = Main.settings.maxSucLevel;
		feedWithRessource("SucCons", remainingSuc, maxSucLevel);
		Integer rangeIndex = 1;
		while (rangeIndex <= Main.settings.constructRange && !isBuildingSucFilled(maxSucLevel)) {
			TuileCase[] inRangeCases = getCasesFromDistance(rangeIndex, rangeIndex - 1);
			Integer casesIndex = 0;
			while (casesIndex < inRangeCases.length && !isBuildingSucFilled(maxSucLevel)) {
				TuileCase inRangeCase = inRangeCases[casesIndex];
				if (inRangeCase.parentTuile.owner.equals(parentTuile.owner)) {
					feedWithRessource("SucCons", inRangeCase.remainingSuc, maxSucLevel);
				}
				casesIndex++;
			}
			rangeIndex++;
		}		
	}
	
	Boolean isBuildingSucFilled(Integer maxSucLevel) {
		return sucLevel >= maxSucLevel;
	}
	
	// PANEL PREVIEW
	private void showPanelCaseInfo() {
    	infoFrame = new JFrame();
        infoDialog = new JDialog(infoFrame);
        
        updateCaseProduction();
        
        JLabel playerLabel = new JLabel(parentTuile.owner.playerName + "");
        playerLabel.setForeground(parentTuile.owner.playerColor);
        infoDialog.add(playerLabel);
        infoDialog.add(new JLabel(""));
        infoDialog.add(new JLabel(terrain + " (" + getCaseXPos() + ":" + getCaseYPos() + ")"));
        infoDialog.add(new JLabel(""));
        infoDialog.add(new JLabel("Fibre: +" + prodFibre));
        infoDialog.add(new JLabel("Spore: +" + prodSpore));
        infoDialog.add(new JLabel("Suc: +" + prodSuc));
        infoDialog.add(new JLabel("Phosphorite: +" + prodPhospho));
        infoDialog.add(new JLabel("MushCoins: +" + prodCoins));
        int numberOfLines = 9;

        if (!building.equals("")) {
        	infoDialog.add(new JLabel(""));
            infoDialog.add(new JLabel(building));
            numberOfLines += 2;
    		String[][] buildingPartLists = {floorBuildingParts, wallBuildingParts, roofBuildingParts};
    		for (String[] buildingPartList : buildingPartLists) {
	            for (var i = 0; i < (buildingPartList != null ? buildingPartList.length : 0) ; i++) {
	                infoDialog.add(new JLabel(" - " + buildingPartList[i]));
	                numberOfLines++;
	            }
    		}
    		infoDialog.add(new JLabel("Suc Level: " + sucLevel + "/" + Main.settings.maxSucLevel));
    		numberOfLines++;
        }
        if (roadLevel > 0) {
            infoDialog.add(new JLabel(""));
        	infoDialog.add(new JLabel("Route de niveau " + roadLevel));
            numberOfLines += 2;
        }
        if (!inConstruction.equals("")) {
            infoDialog.add(new JLabel(""));
        	infoDialog.add(new JLabel(inConstruction + " en constrcution"));
            numberOfLines += 2;
        }
		
		infoDialog.setLayout(new GridLayout(numberOfLines, 1));
		
        infoDialog.setBounds(350, 50, 200, numberOfLines * 30);
        infoDialog.setVisible(true);
	}
	
	
	private void hidePanelCaseInfo() {
		infoDialog.setVisible(false);
		infoDialog.dispose();
		infoDialog.removeAll();
	}
}


	