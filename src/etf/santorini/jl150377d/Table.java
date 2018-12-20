package etf.santorini.jl150377d;
import java.awt.*;
import java.io.FileNotFoundException;
import java.io.PrintWriter;

import javax.swing.JPanel;

public class Table implements Cloneable{
	
	public Field[][] lista=new Field[5][5];
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
		if(sant.mode==0) {
			if(p1.id==id)return p1;
			if(p2.id==id)return p2;
		}
		if(sant.mode==1) {
			if(p1.id==id)return p1;
			if(ai2.id==id)return ai2;
		}
		if(sant.mode==2) {
			if(ai1.id==id)return ai1;
			if(ai2.id==id)return ai2;
		}
		return null;
	}

	public Figure find_figure(int id, int id2) {
		if(id==1) {
			if(sant.mode==0||sant.mode==1) {//Igrac
				if(id2==1) return this.p1.f1;
				if(id2==2) return this.p1.f2;
			}
			if(sant.mode==2) {
				if(id2==1) return this.ai1.f1;
				if(id2==2) return this.ai1.f2;
			}
		}
		if(id==2) {
			if(sant.mode==0) {//Igrac
				if(id2==1) return this.p2.f1;
				if(id2==2) return this.p2.f2;
			}
			if(sant.mode==1||sant.mode==2) {
				if(id2==1) return this.ai2.f1;
				if(id2==2) return this.ai2.f2;
			}
		}
		return null;
	}
	
	public int switch_player(int id) {
		if(id==1)return 2;
		if(id==2)return 1;
		return 0;
	}

	public Field find_field(Field field) {
		for(int i=0;i<5;i++)
			for(int j=0;j<5;j++) {
				if(lista[i][j].x==field.x&&lista[i][j].y==field.y)return lista[i][j];
			}
		return null;
	}
	
	public Table clone() throws CloneNotSupportedException {
		Table table=(Table)super.clone();
		table.lista=new Field[5][5];
		for(int i=0;i<5;i++)
			for(int j=0;j<5;j++)
				table.lista[i][j]=this.lista[i][j].clone();
		if(this.p1!=null) {
			table.p1=this.p1;
			table.p1=table.p1.clone();
			p1.f1.table=table;
			p1.f2.table=table;
		}
		if(this.p2!=null) {
			table.p2=this.p2;
			table.p2=table.p2.clone();
			p2.f1.table=table;
			p2.f2.table=table;
		}
		if(this.ai1!=null) {
			table.ai1=this.ai1;
			table.ai1=table.ai1.clone();
			ai1.f1.table=table;
			ai1.f2.table=table;
		}
		if(this.ai2!=null) {
			table.ai2=this.ai2;
			table.ai2=table.ai2.clone();
			ai2.f1.table=table;
			ai2.f2.table=table;
		}
		//table.panel=this.panel;
		table.current_player=this.current_player;
		table.current_player=table.current_player.clone();
		table.origin=this.origin;
		table.origin=table.origin.clone();
		table.start=this.start;
		table.firstClick=this.firstClick;
		table.p11=this.p11;
		table.p12=this.p12;
		table.p21=this.p21;
		table.p22=this.p22;
		table.build=this.build;
		table.red=this.red;
		table.player1=this.player1;
		table.x_decrypt=this.x_decrypt;
		table.y_decrypt=this.y_decrypt;
		table.sant=this.sant;
		
		return table;
	}

	public void repaint(Table table) {
		for(int i=0;i<5;i++)
			for(int j=0;j<5;j++) {
				if(this.lista[i][j].cur_height!=table.lista[i][j].cur_height) {
					this.lista[i][j].cur_height=table.lista[i][j].cur_height;
					this.lista[i][j].button.setText(this.lista[i][j].cur_height +" / "+ this.lista[i][j].id+""+this.lista[i][j].id2);
					this.lista[i][j].change_color_on_height(this.lista[i][j].cur_height);
				}
			}
		
		if(ai1!=null && ((ai1.f1.x!=table.ai1.f1.x || ai1.f1.y!=table.ai1.f1.y) || (ai1.f2.x!=table.ai1.f2.x || ai1.f2.y!=table.ai1.f2.y))) {
			Field f1=get_field(ai1.f1.x, ai1.f1.y);
			f1.remove_figure();
			f1.button.setText(f1.cur_height +" / "+ f1.id+""+f1.id2);
			ai1.f1.x=table.ai1.f1.x;
			ai1.f1.y=table.ai1.f1.y;
			f1=get_field(ai1.f1.x, ai1.f1.y);
			ai1.f1.f=f1;
			f1.add_figure(ai1.f1);		
			f1.button.setText(f1.cur_height +" / "+ f1.id+""+f1.id2);
			
			Field f2=get_field(ai1.f2.x, ai1.f2.y);
			f2.remove_figure();
			f2.button.setText(f2.cur_height +" / "+ f2.id+""+f2.id2);
			ai1.f2.x=table.ai1.f2.x;
			ai1.f2.y=table.ai1.f2.y;
			f2=get_field(ai1.f2.x, ai1.f2.y);
			ai1.f2.f=f2;
			f2.add_figure(ai1.f2);
			f2.button.setText(f2.cur_height +" / "+ f2.id+""+f2.id2);
		}
		if(ai2!=null && ((ai2.f1.x!=table.ai2.f1.x || ai2.f1.y!=table.ai2.f1.y) || (ai2.f2.x!=table.ai2.f2.x || ai2.f2.y!=table.ai2.f2.y))) {
			Field f1=get_field(ai2.f1.x, ai2.f1.y);
			f1.remove_figure();
			f1.button.setText(f1.cur_height +" / "+ f1.id+""+f1.id2);
			ai2.f1.x=table.ai2.f1.x;
			ai2.f1.y=table.ai2.f1.y;
			f1=get_field(ai2.f1.x, ai2.f1.y);
			ai2.f1.f=f1;
			f1.add_figure(ai2.f1);		
			f1.button.setText(f1.cur_height +" / "+ f1.id+""+f1.id2);
			
			Field f2=get_field(ai2.f2.x, ai2.f2.y);
			f2.remove_figure();
			f2.button.setText(f2.cur_height +" / "+ f2.id+""+f2.id2);
			ai2.f2.x=table.ai2.f2.x;
			ai2.f2.y=table.ai2.f2.y;
			f2=get_field(ai2.f2.x, ai2.f2.y);
			ai2.f2.f=f2;
			f2.add_figure(ai2.f2);
			f2.button.setText(f2.cur_height +" / "+ f2.id+""+f2.id2);
		}
	}
}
