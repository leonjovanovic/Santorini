package etf.santorini.jl150377d;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;

public class SimpleAI extends Player {
	
	public JButton nextStep;
	private FieldAction listener = new FieldAction();
	public int x_func,y_func;

	public SimpleAI(Table table) {
		super(table);
		x_func=1;
		y_func=1;
		nextStep=new JButton("Next Step");
		nextStep.addActionListener(listener);
		//DOPUNITI KONSTRUKTOR		
	}

	public void function() {
		x_func++;y_func++;//Dodacemo pravu funkciju
		//Koja nece dozvoliti da odrediste je zauzeto ili u bilo kom slucaju nedostupno
	}



	private class FieldAction implements ActionListener{

		@Override
		public void actionPerformed(ActionEvent e) {
			
			Object source=e.getSource();
			JButton f=(JButton)source;

			if(table.p11&&table.sant.mode==2&&id==1) {
				table.p11=false;
				function();
				create_figures1(x_func, y_func);
				Field temp=table.get_field(x_func, y_func);
				temp.add_figure(f1);
				temp.button.setText(temp.cur_height +" / "+ temp.id+""+temp.id2);
				
				function();
				create_figures2(x_func, y_func);
				temp=table.get_field(x_func, y_func);
				temp.add_figure(f2);
				temp.button.setText(temp.cur_height +" / "+ temp.id+""+temp.id2);
				table.p12=false;
				return;
			}
			if(table.p21&&id==2&&!table.p12) {
				table.p21=false;
				function();
				create_figures1(x_func, y_func);
				Field temp=table.get_field(x_func, y_func);
				temp.add_figure(f1);
				temp.button.setText(temp.cur_height +" / "+ temp.id+""+temp.id2);
				
				function();
				create_figures2(x_func, y_func);
				temp=table.get_field(x_func, y_func);
				temp.add_figure(f2);
				temp.button.setText(temp.cur_height +" / "+ temp.id+""+temp.id2);
				table.p22=false;
				table.firstClick=table.start=true;//Zavrseno postavljanje, figurice mogu da se krecu
				table.player1=1;
				return;
			}
			if(table.sant.mode==1 && table.player1==2) {
				move();
				build();//Dodati uslove pobede i poraza
				table.player1=1;
				table.start=true;
			}
		
		}		
	}
	
	public void move() {
		
	}
	
	public void build() {
		
	}
}
