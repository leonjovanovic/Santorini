package etf.santorini.jl150377d;
import java.awt.*;
import java.io.FileNotFoundException;
import java.io.PrintWriter;

import javax.swing.JPanel;

public class Table {
	
	private Field[][] lista=new Field[5][5];
	public Player p1, p2;
	public SimpleAI ai1, ai2;
	private JPanel panel;
	public Figure origin,current_player=null;
	public boolean start, firstClick,p11,p12,p21,p22;
	public int build,red,player1;
	PrintWriter text_file;
	public int x_decrypt, y_decrypt;
	public Santorini sant;
	
	
	public Table(Santorini s) {
		start=firstClick=false;
		player1=-1;
		sant=s;
		try {
			if(s.load==0) {text_file = new PrintWriter("save.txt");System.out.println("novi");}
		} catch (FileNotFoundException e) {	e.printStackTrace();}
		build=0;red=99;
		for(int i=0; i<5;i++)
			for(int j=0; j<5; j++) {
				lista[i][j]=new Field(this,i,j);
			}
		create_players();
		
		p11=p12=p21=p22=true;
		origin=null;
		
		panel = new JPanel(new GridLayout(5,5));
		fields();
		panel.setMaximumSize(new Dimension(500,500));
		panel.setVisible(true);
		
	}
	
	public boolean get_move() {
		return start;
	}
	public boolean get_firstClick() {
		return firstClick;
	}
	
	public void set_first_click(boolean b) {
		firstClick=b;
	}
	
	public Field get_field(int x, int y) {
		return lista[x][y];
	}
	
	public boolean field_exist(int x, int y) {
		if(x>4 || x<0 || y>4 || y<0) return false;
		return true;
	}
	
	public void fields() {
		for(int i=0; i<5;i++)
			for(int j=0; j<5; j++) {
				panel.add(lista[i][j].get_button());
			}
		
	}
	
	public JPanel panel() {
		return panel;
	}
	
	public String encrypt(int y,int x) {
		switch(y) {
		case 0: return "A"+(x+1)+"";
		case 1: return "B"+(x+1)+"";
		case 2: return "C"+(x+1)+"";
		case 3: return "D"+(x+1)+"";
		case 4: return "E"+(x+1)+"";
		default: return "X"+(x+1)+"";
		}
	}
	
	public void decrypt(String s) {
		this.x_decrypt=Integer.parseInt(Character.toString(s.charAt(1)))-1;
		switch(s.charAt(0)) {
		case 'A': {			
			this.y_decrypt=0;
			return;
		}
		case 'B': {
			this.y_decrypt=1;
			return;
		}
		case 'C': {
			this.y_decrypt=2;
			return;
		}
		case 'D': {
			this.y_decrypt=3;
			return;
		}
		case 'E': {
			this.y_decrypt=4;
			return;
		}
		}
	}
	
	public void create_players() {
		switch(sant.mode) {
		case 0:{
			p1=new Player(this);
			p2=new Player(this);
			break;
		}
		case 1:{
			p1=new Player(this);
			ai2=new SimpleAI(this);
			break;
		}
		case 2:{
			ai1=new SimpleAI(this);
			ai2=new SimpleAI(this);
			break;
		}
		}
	}
	
	public Player get_player(int id) {
		if(p1.id==id)return p1;
		if(p2.id==id)return p2;
		if(ai1.id==id)return ai1;
		if(ai2.id==id)return ai2;
		return null;
	}
}
