package net.codejava;

import javax.swing.*;

import java.awt.Dimension;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;

public class Main 
{
	public static Settings settings = null;
	public static GameController gameController = null;
	public static Carte cartePanel;
	public static FilterViews filterViews;
    	
	public static void main(String[] args) 
    {
        // Définissez le frame
        JFrame frame = new JFrame("Univ. Builder proto");
        
        settings = new Settings();
        settings.createPlayersMenus();
        
        filterViews = new FilterViews(settings);

        gameController = new GameController();

        // Définition de la carte
        cartePanel = new Carte(settings);
        cartePanel.randomize();
        cartePanel.updateWorldView();
        
        // PANNEAU DROIT
        JPanel viewPanel = new JPanel(); 
        JScrollPane scrPanel = new JScrollPane(cartePanel, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        scrPanel.setPreferredSize(new Dimension(1300, 800));
        viewPanel.add(scrPanel);

        // créer un séparateur de panneau
        JSplitPane sep = new JSplitPane(SwingConstants.VERTICAL, gameController.controllerPanel, viewPanel); 
        // définir l'orientation du séparateur
        sep.setOrientation(SwingConstants.VERTICAL);
        // Ajouter le séparateur
        frame.add(sep);

        frame.pack();
		frame.setLocationRelativeTo(null);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
        
        GraphicsEnvironment graphics = GraphicsEnvironment.getLocalGraphicsEnvironment();
        GraphicsDevice device = graphics.getDefaultScreenDevice();
        device.setFullScreenWindow(frame);
    }
}