package etf.santorini.jl150377d;
import java.awt.*;

import javax.swing.JPanel;

public class Table {
	
	private Field[][] lista=new Field[5][5];
	public Player p1, p2;
	private JPanel panel;
	public Figure origin,current_player=null;
	public boolean start, firstClick,p11,p12,p21,p22;
	public int build,red,player1;
	
	public Table() {
		start=firstClick=false;
		player1=-1;
		build=0;red=99;
		for(int i=0; i<5;i++)
			for(int j=0; j<5; j++) {
				lista[i][j]=new Field(this,i,j);
			}
		p1=new Player(this);
		p2=new Player(this);
		p11=p12=p21=p22=true;
		origin=null;
		
		panel = new JPanel(new GridLayout(5,5));
		fields();
		panel.setBounds(0, 0, 500, 500);
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
}
