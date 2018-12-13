package etf.santorini.jl150377d;

import java.awt.*;
import java.awt.event.*;
import javax.swing.JPanel;


public class Santorini extends Frame{
	Table table;
	JPanel panel;
	
	public Santorini() {
		super("Santorini");
		table=new Table();
		panel = new JPanel(new BorderLayout());
		panel.add(table.panel(),BorderLayout.CENTER);
		this.setBounds(0, 0, 700, 600);
		this.setResizable(false);
		panel.add(new Label("Player 1"), BorderLayout.WEST);
		panel.add(new Label("Player 2"), BorderLayout.EAST);
		panel.add(new Label("Stanje"), BorderLayout.NORTH);
		add(panel);
		
		this.addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent dog) {
				dispose();
			}
		});
	}
	
	public static void main(String [] varg) {
		Santorini s=new Santorini();
		s.setVisible(true);
	}
}
