package etf.santorini.jl150377d;

import java.awt.*;
import java.awt.event.*;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

import javax.swing.*;


public class Santorini extends JFrame{
	
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
				if(table!=null)table.text_file.close();
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
			panel.setVisible(false);
			panel=fill_panel2();		//START GAME			
			resize_panel();
			add(panel);
			validate();
		}		
	}

	public JPanel fill_panel2() {
		table=new Table(this);
		if(load==1)load_game();
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
		c.fill = GridBagConstraints.BOTH;
		c.ipady = 0;
		c.weightx=0;
		c.gridx = 0;
		c.gridwidth=1;
		c.gridheight=5;
		if(mode==2){c.gridy = 2;c.gridheight=4;}
		else c.gridy=1;
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
		c.fill = GridBagConstraints.BOTH;
		c.weightx=0;
		c.gridx = 6;
		c.gridwidth=1;
		c.ipady =0;
		c.ipadx =50;
		if(mode==2||mode==1) {c.gridy = 2;c.gridheight=4;}
		//c.gridheight=5;
		l2.setBackground(Color.ORANGE);
		l2.setOpaque(true);
		panel_temp.add(l2,c);		
		
		return panel_temp;
	}
	
	public void load_game() {
		try (BufferedReader br = new BufferedReader(new FileReader("save.txt"))) {
		    String line;
		    while ((line = br.readLine()) != null) {
		    	if(table.p11&&table.p12) {
		    		table.p11=table.p12=false;
		    		String[] splited = line.split("\\s+");
		    		System.out.println(splited[0]);
		    		table.decrypt(splited[0]);
		    		table.p1.create_figures1(table.x_decrypt, table.y_decrypt);
		    		Field temp=table.get_field(table.x_decrypt, table.y_decrypt);
					temp.add_figure(table.p1.f1);
					temp.button.setText(temp.cur_height +" / "+ temp.id+""+temp.id2);
					
		    		table.decrypt(splited[1]);
		    		table.p1.create_figures2(table.x_decrypt, table.y_decrypt);
		    		temp=table.get_field(table.x_decrypt, table.y_decrypt);
					temp.add_figure(table.p1.f2);
					temp.button.setText(temp.cur_height +" / "+ temp.id+""+temp.id2);
		    	}
		    	else if(table.p21&&table.p22) {
		    		table.p21=table.p22=false;
		    		String[] splited = line.split("\\s+");
		    		System.out.println(splited[0]);
		    		table.decrypt(splited[0]);
		    		table.p2.create_figures1(table.x_decrypt, table.y_decrypt);
		    		Field temp=table.get_field(table.x_decrypt, table.y_decrypt);
					temp.add_figure(table.p2.f1);
					temp.button.setText(temp.cur_height +" / "+ temp.id+""+temp.id2);
					
		    		table.decrypt(splited[1]);
		    		table.p2.create_figures2(table.x_decrypt, table.y_decrypt);
		    		temp=table.get_field(table.x_decrypt, table.y_decrypt);
					temp.add_figure(table.p2.f2);
					temp.button.setText(temp.cur_height +" / "+ temp.id+""+temp.id2);

					table.firstClick=table.start=true;
					table.player1=1;
		    	}
		    	
		    	else {
		    		String[] splited = line.split("\\s+");
		    		if(splited.length!=3){ //Ukoliko nakon postavljanja figura red u txt nema 3 polja reci da je save file korumpiran
		    			JOptionPane.showMessageDialog(this, "Save file is corrupt.");this.dispose();Santorini s=new Santorini();s.setVisible(true);return;}
		    		System.out.println(splited[0]);
		    		table.decrypt(splited[0]);//Odakle pomeramo
		    		Field temp=table.get_field(table.x_decrypt, table.y_decrypt);
		    		table.decrypt(splited[1]);//Nalazimo destinaciju
		    		boolean flag=temp.get_figure().move(table.x_decrypt, table.y_decrypt);//pomeri figuricu na x,y
					if(flag) {
						temp.button.setText(temp.cur_height +" / "+ temp.id+""+temp.id2);//promeni naziv polja **NA** koje smo pomerili
						temp=table.get_field(table.x_decrypt, table.y_decrypt);
						temp.button.setText(temp.cur_height +" / "+ temp.id+""+temp.id2);//promeni naziv polja **SA** kojeg smo pomerili
						table.current_player=temp.get_figure();//Ona figura koju smo pomerili da bi kasnije samo ona mogla da gradi, da bi izbegli da jedna pomera a druga gradi
					}
					table.decrypt(splited[2]);
					temp=table.get_field(table.x_decrypt, table.y_decrypt);
					temp.build();
					temp.button.setText(temp.cur_height +" / "+ temp.id+""+temp.id2);//promeni naziv polja koji smo gradili i
					switch(temp.cur_height) {//zavisnosti od nove visine polja promeni mu boju
						case 1:
						case 2:
						case 3:{
							temp.change_color(Color.WHITE);
							temp.button.setBackground(temp.color);
							break;
						}
						case 4:{
							temp.change_color(Color.CYAN);
							temp.button.setBackground(temp.color);
							break;
						}
					}
					if(table.player1==1)table.player1=2;
					else if(table.player1==2) table.player1=1;
		    	}
		    	
		    	
		    }
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		try {
			load=0;
			table.text_file = new PrintWriter(new BufferedWriter(new FileWriter("save.txt", true)));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void add_left_button() {
		GridBagConstraints c = new GridBagConstraints();		
		c.fill = GridBagConstraints.VERTICAL;
		c.ipady = 0;
		c.weightx=0;
		c.gridx = 0;
		c.gridwidth=1;
		c.gridy = 1;
		c.gridheight=1;
		c.ipadx = 50;
		panel.add(table.ai1.nextStep,c);
	}
	
	public void add_right_button() {
		GridBagConstraints c = new GridBagConstraints();
		c.fill = GridBagConstraints.VERTICAL;
		c.weightx=0;
		c.gridx = 6;
		c.gridwidth=1;
		c.ipady = 0;
		c.ipadx =50;
		c.gridy = 1;
		c.gridheight=1;
		panel.add(table.ai2.nextStep,c);
		
	}

	public void resize_panel() {
		switch(mode) {
		case 0:{
			this.setSize(750, 650);
			break;
		}
		case 1:{
			this.setSize(790, 650);
			add_right_button();
			break;
		}
		case 2:{
			this.setSize(830, 650);
			add_right_button();
			add_left_button();
			break;
		}
		}
	}
	
	public JFrame get_frame() {
		return this;
	}
	
	public static void main(String [] varg) {
		Santorini s=new Santorini();
		s.setVisible(true);
	}
}
