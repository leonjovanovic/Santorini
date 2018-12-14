package etf.santorini.jl150377d;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;


public class Santorini extends Frame{
	
	private static final long serialVersionUID = 1L;
	
	Table table;
	JPanel panel,panel2;
	public int load,mode;
	
	public Santorini() {
		super("Santorini");
		mode=0;load=0;
		this.setSize(750, 650);
		this.setResizable(false);
		panel=fill_panel();		
		add(panel);
		
		this.addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent dog) {
				table.text_file.close();
				dispose();
			}
		});
	}
	
	public JPanel fill_panel() {
		JPanel panel_temp=new JPanel(new GridBagLayout());
		//******First group - choose opponent**********
		JRadioButton PvP = new JRadioButton("Player vs Player");
		PvP.setActionCommand("PvP");
		PvP.setSelected(true);

        JRadioButton PvE = new JRadioButton("Player vs Computer");
        PvE.setActionCommand("PvE");

        JRadioButton EvE = new JRadioButton("Computer vs Computer");
        EvE.setActionCommand("EvE");
        
        ButtonGroup group1 = new ButtonGroup();
        group1.add(PvP);
        group1.add(PvE);
        group1.add(EvE);
        
        GameMode player=new GameMode();
        
        //******Second group - save game**********  
        JRadioButton not_load_save = new JRadioButton("Start new game");
        not_load_save.setActionCommand("new");      
        not_load_save.setSelected(true);
        
        JRadioButton load_save = new JRadioButton("Load last saved game");
        load_save.setActionCommand("load");
        load_save.setSelected(true);
        
        ButtonGroup group2 = new ButtonGroup();
        group2.add(not_load_save);
        group2.add(load_save);
        
        SaveGame save_game=new SaveGame();
        
        //Listeners
        PvP.addItemListener(player);
        PvE.addItemListener(player);
        EvE.addItemListener(player);
        not_load_save.addItemListener(save_game);
        load_save.addItemListener(save_game);
        //Adding to panel
        panel_temp.add(PvP);
        panel_temp.add(PvE);
        panel_temp.add(EvE);
        panel_temp.add(not_load_save);
        panel_temp.add(load_save);
        
        //Button that will start our game
        JButton button1=new JButton("Kreni");
        StartGame start=new StartGame();
        button1.addActionListener(start);
        panel_temp.add(button1);
        
		return panel_temp;
	}
	
	private class GameMode implements ItemListener{
		@Override
		public void itemStateChanged(ItemEvent arg) {
			String sign=((JRadioButton)arg.getSource()).getActionCommand();
			switch(sign) {
			case "PvP":{mode=0;break;}
			case "PvE":{mode=1;break;}	//CHOOSE OPPONENT
			case "EvE":{mode=2;break;}
			default:{mode=3;break;}
			}			
		}		
	}
	
	private class SaveGame implements ItemListener{
		@Override
		public void itemStateChanged(ItemEvent e) {
			String sign=((JRadioButton)e.getSource()).getActionCommand();
			switch(sign) {
			case "new":{load=0;break;}	//CHOOSE NEW GAME OR LOAD EXISTING GAME
			case "load":{load=1;break;}
			default:{load=2;break;}
			}			
		}		
	}
	
	private class StartGame implements ActionListener{
		@Override
		public void actionPerformed(ActionEvent arg0) {
			System.out.println(mode+" - "+load);
			remove(panel);
			panel=fill_panel2();		//START GAME			
			add(panel);			
			validate();
		}		
	}

	public JPanel fill_panel2() {
		table=new Table();
		JPanel panel_temp = new JPanel(new GridBagLayout());
		GridBagConstraints c = new GridBagConstraints();
		
		
		JLabel stanje=new JLabel("Stanje");
		stanje.setBackground(Color.CYAN);
		stanje.setOpaque(true);
		c.fill = GridBagConstraints.HORIZONTAL;
		c.ipady = 40;
		c.gridx = 0;
		c.gridy = 0;
		c.gridwidth=7;
		c.anchor=GridBagConstraints.PAGE_START;
		panel_temp.add(stanje,c);
		
		//panel2 = new JPanel();
		JLabel l1=new JLabel("Player 1");
		l1.setBackground(Color.ORANGE);
		l1.setOpaque(true);
		c.fill = GridBagConstraints.HORIZONTAL;
		c.ipady = 0;
		c.weightx=0;
		c.gridx = 0;
		c.gridwidth=1;
		c.gridy = 1;
		c.gridheight=5;
		c.ipadx = 50;
		panel_temp.add(l1,c);
		
		JPanel board=table.panel();
		c.fill = GridBagConstraints.HORIZONTAL;
		c.weightx=1;
		c.gridx = 1;
		c.gridwidth=5;
		c.ipady = 425;
		c.ipadx =200;
		c.gridheight=5;
		c.gridy = 1;
		panel_temp.add(board,c);
		
		JLabel l2=new JLabel("Player 2");
		c.fill = GridBagConstraints.HORIZONTAL;
		c.weightx=1;
		c.gridx = 6;
		c.gridwidth=1;
		c.ipady = 0;
		c.ipadx =50;
		c.gridy = 2;
		c.gridheight=5;
		l2.setBackground(Color.ORANGE);
		l2.setOpaque(true);
		panel_temp.add(l2,c);
		return panel_temp;
	}
	
	public static void main(String [] varg) {
		Santorini s=new Santorini();
		s.setVisible(true);
	}
}
