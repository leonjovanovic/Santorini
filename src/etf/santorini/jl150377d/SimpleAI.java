package etf.santorini.jl150377d;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JOptionPane;

public class SimpleAI extends Player implements Cloneable{
	
	public JButton nextStep;
	private FieldAction listener = new FieldAction();
	public int x_func,y_func;
	private Tree tree;

	public SimpleAI(Table table) {
		super(table);
		x_func=1;
		y_func=1;
		nextStep=new JButton("Next Step");
		nextStep.addActionListener(listener);
		//DOPUNITI KONSTRUKTOR		
	}

	public void function() {//SREDITI DA START BUDE RANDOM
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
			if(table.sant.mode==1 && table.player1==2) {//Za PvE
				tree=new Tree(table);//Pravimo stablo dubine 4 sa svim mogucim potezima od trenutnog stanja table
				minimax(tree.root,0,true);
				System.out.println("Najbolji score: "+tree.root.f_score);
				Node next=tree.next_root();//Tabla sa sledecim odigranim potezom;
				//System.out.println(next.f_score);
				table.repaint(next.table);
				//*****Uslovi pobede i poraza******
				if(table.ai2.f1.isWinner()||table.ai2.f2.isWinner()) {JOptionPane.showMessageDialog(table.sant, "Player 2 is winner!");table.sant.dispose();table.sant.main(null);}
				if(table.get_player(2).isLoser()){JOptionPane.showMessageDialog(table.sant, "Player 2 is loser!");table.sant.dispose();table.sant.main(null);}
				System.out.println(next.table.ai2.f1+" "+next.table.ai2.f2);
				System.out.println(next.table.lista[2][2].figure);
				System.out.println(next.table.lista[1][2].figure);
				table.player1=1;
				table.start=true;
			}
			if(table.sant.mode==2) {//Za EvE
				/*
				 Dopuniti uslove za racunar vs racunar
				 */			
			}
		
		}		
	}
	
	public double minimax(Node node,int depth,boolean isMaximizingPlayer) {

	    if(node.children==null || depth==1)//children.isEmpty()
	        return node.f_score;//PROVERITI!!!!!!!!
	    
	    if(isMaximizingPlayer) {
	    	node.f_score = Double.NEGATIVE_INFINITY;
	        for(int i=0;i<node.children_lenght();i++) {
	        	Node n=node.children.get(i);
	            double value = minimax(n, depth+1, false);
	            node.f_score = Math.max( node.f_score, value); 
	        }
	        return node.f_score;
	    }
	    else {
	    	node.f_score = Double.POSITIVE_INFINITY;
	        for(int i=0;i<node.children_lenght();i++) {
	        	Node n=node.children.get(i);
	            double value = minimax(n, depth+1, true);
	            node.f_score = Math.min( node.f_score, value); 
	        }
	        return node.f_score;
	    }
	}
	
	public SimpleAI clone() throws CloneNotSupportedException {
		SimpleAI ai=(SimpleAI)super.clone();
		return ai;
	}
}
