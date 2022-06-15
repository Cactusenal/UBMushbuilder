package net.codejava;

import javax.swing.*;

import java.awt.Frame;
import java.awt.Point;


public class Main 
{
	public static Settings settings = null;
	public static GameController gameController = null;
	public static Carte cartePanel;
	public static FilterViews filterViews;
	public static JFrame frame = new JFrame("Univ. Builder proto");
    	
	public static void main(String[] args) 
    {
        // Définissez le frame
//        JFrame frame = new JFrame("Univ. Builder proto");
        
        settings = new Settings();
        settings.createPlayersMenus();
        
        filterViews = new FilterViews(settings);

        gameController = new GameController();

        // Définition de la carte
        cartePanel = new Carte(settings);
        cartePanel.randomize();
        cartePanel.updateWorldView();
        
        // PANNEAU DROIT
        JScrollPane scrPanel = new JScrollPane(cartePanel, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
//        scrPanel.setPreferredSize(new Dimension(1300, 790));
        
        // Center the scroll pane
        JViewport jvp = new JViewport();
        jvp = scrPanel.getViewport();
        jvp.setViewPosition(new Point(800,0));

        // créer un séparateur de panneau
        JSplitPane sep = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, gameController.controllerPanel, scrPanel); 
        // définir l'orientation du séparateur
        sep.setOrientation(SwingConstants.VERTICAL);
        // Ajouter le séparateur
        frame.add(sep);

        frame.pack();
		frame.setLocationRelativeTo(null);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
        
        // Plein ecran
        int state = frame.getExtendedState();
        frame.setExtendedState(state | Frame.MAXIMIZED_BOTH);
    }
}