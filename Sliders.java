package net.codejava;

import java.awt.GridLayout;

import javax.swing.*;

public class Sliders extends JPanel {  
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public JSlider slider;
	static JLabel label;
	public Sliders(String text, int minValue, int maxValue, int startValue) {
		slider = new JSlider(JSlider.HORIZONTAL, minValue, maxValue, startValue);  
		slider.setMinorTickSpacing(2);  
		slider.setMajorTickSpacing(10);  
		slider.setPaintTicks(true);  
		slider.setPaintLabels(true);
        label = new JLabel();
		label.setText(text);
		add(label);
		add(slider);
		setLayout(new GridLayout(2, 1));
	}
	
	public int getValue() {
		return slider.getValue();
	}
}