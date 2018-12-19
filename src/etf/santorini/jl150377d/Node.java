
						//!!!!!!!!!MORA DA SE NAPRAVI NOVA TABELA SA new I DA SE KOPIRAJU STANJA

package etf.santorini.jl150377d;

import java.util.ArrayList;
import java.util.List;

public class Node {
	public Table table;
	public double f_score;
	boolean isMax;
	public List<Node> children;
	public int depth;

	public Node(Table table, int d, boolean max) {
		this.table = table;
		f_score = 0;
		isMax = max;
		depth=d;
		children = new ArrayList<Node>();
		if(depth<3)possibleChildren();//pravimo decu samo za 4 nivoa (root je 0)
	}

	public double calc_score(Field f) {
		//f = m + l
		//m = temp_field_move.cur_height;
		//l = m*((Igrac1_figura1_razlika+ igrac1_figura2_razlika)-(Igrac2_figura1_razlika+ igrac2_figura2_razlika))
		//Igrac1_figura1_razlika=sqrt(pow(2,igrac1_figura1.x-temp_field_move.x)+pow(2,igrac1_figura1.y-temp_field_move.y));
		Player p1,p2;
		p1=table.get_player(table.player1);//Uvek ce p1 biti table.player1 jer uvek gledamo u odnosu na onog koji poziva ovo
		p2=table.get_player(table.switch_player(table.player1));
		Field field_move=f;
		double diff11,diff12,diff21,diff22;
		diff11=Math.sqrt(Math.pow(2, p1.f1.x-field_move.x)+Math.pow(2, p1.f1.y-field_move.y));
		diff12=Math.sqrt(Math.pow(2, p1.f2.x-field_move.x)+Math.pow(2, p1.f2.y-field_move.y));
		diff21=Math.sqrt(Math.pow(2, p2.f1.x-field_move.x)+Math.pow(2, p2.f1.y-field_move.y));
		diff22=Math.sqrt(Math.pow(2, p2.f2.x-field_move.x)+Math.pow(2, p2.f2.y-field_move.y));
		double m=field_move.cur_height;
		double diff = (diff11+diff12)-(diff21+diff22);
		double func=m+m*diff;
		f_score=func;
		System.out.println(f_score);
		return f_score;
	}

	public void possibleChildren() {
		Table temp_table= table,temp_table_move,temp_table_build; // Kopiramo table da bi kasnije menjali
		List<Field> temp_fields_move = new ArrayList<Field>();
		List<Field> temp_fields_build = new ArrayList<Field>();
		Player player = null;
		Figure figure = null;
		
		if(isMax)player=temp_table.get_player(table.player1);//Max player=root player tj igrac koji pravi celo stablo
		else player=temp_table.get_player(temp_table.switch_player(table.player1));//Min igrac mu je protivnik

		if (player != null) {
			for (int p = 0; p < 2; p++) {//Dve figure
				
				temp_table = table;//Uzimamo tabelu pocetnog stanja da kada se vratimo na figuru 2 se vratimo na isto stanje
				if (p == 0)figure = player.f1;
				else figure = player.f2;				
				temp_fields_move = figure.possible_moves();
				if(temp_fields_move==null)return;
				
				for (int i = 0; i < temp_fields_move.size(); i++) {// Za svaki moguci slobodan field gde mozemo move
					//!!!!!!!!!MORA DA SE NAPRAVI NOVA TABELA SA new I DA SE KOPIRAJU STANJA
					temp_table_move = temp_table;//Uzimamo tabelu da zapamtimo pomeranje
					figure=temp_table_move.find_figure(figure.id,figure.id2);//nalazimo istu figuru na novoj tabli koju cemo koristiti
					Field temp_field_move = temp_fields_move.get(i);// uzimamo taj field
					//Menjamo tekst i boju polja sa kojeg i na koje pomeramo figuru
					temp_field_move=temp_table_move.find_field(temp_field_move);//Prebacujemo referencu sa regularnog table na table_move
					temp_field_move.button.setText(temp_field_move.cur_height +" / "+ temp_field_move.id+""+temp_field_move.id2);//promeni naziv polja **NA** koje smo pomerili
					figure.f.button.setText(figure.f.cur_height +" / "+ figure.f.id+""+figure.f.id2);//promeni naziv polja **SA** kojeg smo pomerili
					
					figure.move(temp_field_move.x, temp_field_move.y);// Pomeramo
					temp_fields_build = figure.possible_builds();
					if(depth==3)f_score=this.calc_score(temp_field_move);
					if(temp_fields_build==null)return;
					
					for (int j = 0; j < temp_fields_build.size(); j++) {//Za sve moguce gradnje
						//!!!!!!!!!MORA DA SE NAPRAVI NOVA TABELA SA new I DA SE KOPIRAJU STANJA
						temp_table_build=temp_table_move;//Uzimamo tabelu da zapamtimo gradnju
						figure=temp_table_build.find_figure(figure.id,figure.id2);//nalazimo istu figuru na novoj tabli koju cemo koristiti
						Field temp_field_build = temp_fields_build.get(j);
						figure.build(temp_field_build.x, temp_field_build.y);
						//Menjamo tekst i boju polja koje gradimo
						temp_field_build=temp_table_build.find_field(temp_field_build);//Prebacujemo referencu sa table_move na table_build
						temp_field_build.button.setText(temp_field_build.cur_height +" / "+ temp_field_build.id+""+temp_field_build.id2);//promeni naziv polja koji smo gradili i
						temp_field_build.change_color_on_height(temp_field_build.cur_height);//change color based on height
						//Ovde pitamo da li je dubine 3 i odredjujemo vrednost funkcije jer ako bismo to radili	
						//u konstruktoru morali bi da prosledjujemo razne potrebne podatke sto bi zakomplikovalo	
						children.add(new Node(temp_table_build,depth+1, !isMax));
					
					}
				}
			}

		}
		if (children.isEmpty())
			children = null;
	}

	public void addChild(Node n) {
		children.add(n);
	}

	public int children_lenght() {
		return children.size();
	}
}
