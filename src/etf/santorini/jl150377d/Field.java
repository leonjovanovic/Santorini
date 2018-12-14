package etf.santorini.jl150377d;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JButton;

public class Field {
	private Color color;
	private Table table;
	private Figure figure;
	public int x,y;
	private int cur_height;
	private int id,id2;
	private JButton button;
	private FieldAction listener = new FieldAction();
	
	public Field(Table table,int x, int y) {
		this.table=table;
		this.x=x;
		this.y=y;
		cur_height=0;
		figure=null;
		id=id2=0;
		color=Color.GREEN;
		button=new JButton(cur_height +" / "+ id+""+id2);
		button.setBackground(Color.GREEN);
		button.setForeground(Color.BLACK);
		button.setMaximumSize(new Dimension(100,100)); //PROVERITI
		button.addActionListener(listener);
	}
	
	public int id() {
		return id;
	}
	
	public Table get_table() {
		return table;
	}
	
	public int get_height() {
		return cur_height;
	}
	
	public void remove_figure() {
		figure=null;
		id=0;
		id2=0;
	}
	
	public void add_figure(Figure f) {
		figure=f;
		id=figure.id();
		id2=figure.id2;
	}
	
	public JButton get_button() {
		return button;
	}
	
	public boolean is_taken() {
		if(figure!=null) return true;
		else return false;
	}
	
	public void build() {
		cur_height++;
	}
	
	public Figure get_figure() {
		return figure;
	}
	
	private class FieldAction implements ActionListener{

		@Override
		public void actionPerformed(ActionEvent arg) {
			Object source=arg.getSource();
			JButton f=(JButton)source;
			
						
			if(table.start) {
				//*****SELECT*******
				if(table.get_firstClick()&& figure!=null && table.player1==id) { //FIGURA KOJU KORISTIMO
					table.origin=figure;
					System.out.println(figure.f.x+" - "+figure.f.y);
					table.origin.f.button.setBackground(Color.RED); //Selektujemo figuru koja gradi
					table.set_first_click(false);
				}
				//POLJE KOJE GRADIMO/NA KOJE SE PREMESTANO
				else {
					if(table.origin==null)return;//Moramo prvo da selektujemo neko polje sa figurom
					table.origin.f.button.setBackground(table.origin.f.color); //Vracamo staru boju figuri koja gradi
					switch(table.build) {
						//*******MOVE********
						case 0:{ 
							if(table.origin.id!=table.player1) {table.set_first_click(true);return;}//Da ne bi smo smeli da pomeramo figuricu igraca koji nije na redu
							Field origin_f=table.origin.f;
							boolean flag=table.origin.move(x, y);//pomeri figuricu na x,y
							if(flag) {
								button.setText(cur_height +" / "+ id+""+id2);//promeni naziv polja **NA** koje smo pomerili
								origin_f.button.setText(origin_f.cur_height +" / "+ origin_f.id+""+origin_f.id2);//promeni naziv polja **SA** kojeg smo pomerili
								table.current_player=figure;//Ona figura koju smo pomerili da bi kasnije samo ona mogla da gradi, da bi izbegli da jedna pomera a druga gradi
								table.build=(table.build+1)%2;//Ovim vrtimo da MOVE i BUILD idu naizmenicno
								
							}
							table.set_first_click(true);
							break;
						}
						//*********BUILD*********
						case 1:{ 
							if(table.origin!=table.current_player) {table.set_first_click(true);return;}//da ne bi dozvolili da neka druga figurica gradi osim one koje se pomerila
							if(table.origin.id!=table.player1) {table.set_first_click(true);return;}//da figurica igraca koji nije na redu ne bi mogla da gradi
							boolean flag=table.origin.build(x, y);//gradi
							if(flag) {//ukoliko uspesno
								button.setText(cur_height +" / "+ id+""+id2);//promeni naziv polja koji smo gradili i
								switch(cur_height) {//zavisnosti od nove visine polja promeni mu boju
									case 1:
									case 2:
									case 3:{
										change_color(Color.WHITE);
										button.setBackground(color);
										break;
									}
									case 4:{
										change_color(Color.CYAN);
										button.setBackground(color);
										break;
									}
								}
								table.build=(table.build+1)%2;//Ovim vrtimo da MOVE i BUILD idu naizmenicno
								if(table.player1==1)table.player1=2;
								else if(table.player1==2) table.player1=1;//ovim vrtimo igrace sa id=1 i 2 koje gore ispitujemo
							}
							table.set_first_click(true);//Mozemo opet da selektujemo
							break;
						}
					}
				}
			}
			//*******Postavljanje figurica************
			if(table.p11) {
				table.p1.create_figures1(x, y);
				add_figure(table.p1.f1);
				table.p11=false;
				f.setText(cur_height +" / "+ id+""+id2);
				System.out.println(x+" - "+y+" - "+id);
			}
			else if(table.p12) {
					table.p1.create_figures2(x, y);
					add_figure(table.p1.f2);
					table.p12=false;
					f.setText(cur_height +" / "+ id+""+id2);
					System.out.println(x+" - "+y);
					table.text_file.println(table.convert(table.p1.f1.f.y, table.p1.f1.f.x)+" "+table.convert(table.p1.f2.f.y, table.p1.f2.f.x));
				}
				else if(table.p21) {
						table.p2.create_figures1(x, y);
						add_figure(table.p2.f1);
						table.p21=false;
						f.setText(cur_height +" / "+ id+""+id2);
						System.out.println(x+" - "+y);
					}
					else if(table.p22){
						table.p2.create_figures2(x, y);
						add_figure(table.p2.f2);
						table.p22=false;
						f.setText(cur_height +" / "+ id+""+id2);
						System.out.println(x+" - "+y);
						table.firstClick=table.start=true;
						table.player1=1;
						table.text_file.println(table.convert(table.p2.f1.f.y, table.p2.f1.f.x)+" "+table.convert(table.p2.f2.f.y, table.p2.f2.f.x));
					}
			

		}
		
	}

	public void change_color(Color clr) {
		color=clr;		
	}
	
}
