package etf.santorini.jl150377d;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JOptionPane;

public class SimpleAI extends Player implements Cloneable{
	
	public JButton nextStep;
	public FieldAction listener = new FieldAction();
	public int x_func,y_func;
	private Tree tree;

	public SimpleAI(Table table) {
		super(table);
		System.out.println(id);
		x_func=1;
		y_func=1;
		nextStep=new JButton("Next Step");
		nextStep.addActionListener(listener);
	}

	public void function() {//SREDITI DA START BUDE RANDOM
		x_func=(int) Math.round(Math.random()*4);
		y_func=(int) Math.round(Math.random()*4);	//Koja nece dozvoliti da odrediste je zauzeto ili u bilo kom slucaju nedostupno
		if(table.get_field(x_func, y_func).is_taken())function();
	}



	private class FieldAction implements ActionListener{

		@Override
		public void actionPerformed(ActionEvent e) {
			System.out.println(id);
			if(table.p11&&table.sant.mode==2&&id==2) {
				table.p11=false;
				table.ai1.function();
				table.ai1.create_figures1(table.ai1.x_func, table.ai1.y_func);
				Field temp=table.get_field(table.ai1.x_func, table.ai1.y_func);
				temp.add_figure(table.ai1.f1);
				temp.button.setText(temp.cur_height +" / "+ temp.id+""+temp.id2);
				
				table.ai1.function();
				table.ai1.create_figures2(x_func, y_func);
				temp=table.get_field(table.ai1.x_func, table.ai1.y_func);
				temp.add_figure(table.ai1.f2);
				temp.button.setText(temp.cur_height +" / "+ temp.id+""+temp.id2);
				table.p12=false;
				table.text_file.println(table.encrypt(table.ai1.f1.f.y, table.ai1.f1.f.x)+" "+table.encrypt(table.ai1.f2.f.y, table.ai1.f2.f.x));
				return;
			}
			if(table.p21&&!table.p12) {
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
				table.text_file.println(table.encrypt(table.ai2.f1.f.y, table.ai2.f1.f.x)+" "+table.encrypt(table.ai2.f2.f.y, table.ai2.f2.f.x));
				table.firstClick=table.start=true;//Zavrseno postavljanje, figurice mogu da se krecu
				table.player1=1;
				return;
			}
			if(table.sant.mode==1 && table.player1==2&&id==2) {//Za PvE
				table.building_tree=true;
				tree=new Tree(table);//Pravimo stablo dubine 4 sa svim mogucim potezima od trenutnog stanja table
				table.building_tree=false;
				minimax(tree.root,0,true);
				System.out.println("Najbolji score: "+tree.root.f_score);
				Node next=tree.next_root();//Tabla sa sledecim odigranim potezom;
				tree=null;
				//System.out.println(next.f_score);
				table.repaint(next.table);
				//*****Uslovi pobede i poraza******
				if(table.ai2.f1.isWinner()||table.ai2.f2.isWinner()) {JOptionPane.showMessageDialog(table.sant, "Player 2 is winner!");table.sant.dispose();table.sant.reset();table.p1.reset();table.ai2.reset();}
				if(table.get_player(2).isLoser()){JOptionPane.showMessageDialog(table.sant, "Player 2 is loser!");table.sant.dispose();table.sant.dispose();table.sant.reset();table.p1.reset();table.ai2.reset();}
				table.player1=1;
				table.start=true;
			}
			if(table.sant.mode==2) {//Za EvE
				tree=new Tree(table);//Pravimo stablo dubine 4 sa svim mogucim potezima od trenutnog stanja table
				minimax(tree.root,0,true);
				System.out.println("Najbolji score: "+tree.root.f_score);
				Node next=tree.next_root();//Tabla sa sledecim odigranim potezom;
				tree=null;
				System.gc();//Hintujemo Garbage collectoru da imamo visece reference
				table.repaint(next.table);
				table.sant.validate();
				//*****Uslovi pobede i poraza******
				if(table.ai1.f1.isWinner()||table.ai1.f2.isWinner()) {table.sant.end_game=true;JOptionPane.showMessageDialog(table.sant, "Player 1 is winner!");table.sant.dispose();table.sant.reset();table.ai1.reset();table.ai2.reset();}//
				if(table.get_player(1).isLoser()){table.sant.end_game=true;JOptionPane.showMessageDialog(table.sant, "Player 2 is loser!");table.sant.dispose();table.sant.reset();table.ai1.reset();table.ai2.reset();}//
				//*****Uslovi pobede i poraza******
				if(table.ai2.f1.isWinner()||table.ai2.f2.isWinner()) {table.sant.end_game=true;JOptionPane.showMessageDialog(table.sant, "Player 2 is winner!");table.sant.dispose();table.sant.reset();table.ai1.reset();table.ai2.reset();}//
				if(table.get_player(2).isLoser()){table.sant.end_game=true;JOptionPane.showMessageDialog(table.sant, "Player 2 is loser!");table.sant.dispose();table.sant.reset();table.ai1.reset();table.ai2.reset();}//
				
				if(table.player1==1)table.player1=2;
				else if(table.player1==2) table.player1=1;//ovim vrtimo igrace sa id=1 i 2 koje gore ispitujemo
				
				/*
				 Dopuniti uslove za racunar vs racunar
				 */			
			}
		
		}		
	}
	
	public double minimax(Node node,int depth,boolean isMaximizingPlayer) {

	    if(node.children==null || depth==2) {//children.isEmpty()
	        return node.f_score;}
	    
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
