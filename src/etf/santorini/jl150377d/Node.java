package etf.santorini.jl150377d;

import java.util.ArrayList;
import java.util.List;

public class Node {
	public Table table;
	public double f_score;
	boolean isMax;
	public List<Node> children;
	public int depth;
	boolean won = false, lost = false, deny = false;

	public Node(Table table, int d, boolean max, Field temp_field_move, Field temp_field_build, int delta) {
		this.table = table;
		isMax = max;
		depth = d;
		children = null;
		f_score = this.calc_score(temp_field_move, temp_field_build, delta);
		if (f_score > table.sant.max) { // pomeranje maxova
			table.sant.max2 = table.sant.max1;
			table.sant.max1 = table.sant.max;
			table.sant.max = f_score;
		}
		else if (f_score > table.sant.max1) {
			table.sant.max2 = table.sant.max1;
			table.sant.max1 = f_score;
		} else if (f_score > table.sant.max2) {
			table.sant.max2 = f_score;
		}
		if (depth < table.sant.diff && !won && !lost && !deny)
			try {
				children = new ArrayList<Node>();
				possibleChildren();
			} catch (CloneNotSupportedException e) {
				e.printStackTrace();
			} // pravimo decu samo za 4 nivoa (root je 0)
		else
			children = null;
		table.sant.cnt++;
	}

	public double calc_score(Field f, Field ff, int delta) {
		Player p1, p2;
		p1 = table.get_player(table.player1);// Uvek ce p1 biti table.player1 jer uvek gledamo u odnosu na onog koji poziva ovo
		p2 = table.get_player(table.switch_player(table.player1));

		if (f == null && table.sant.root == true) {
			table.sant.root = false;
			return -1;
		}
		double diff11, diff12, diff21, diff22;
		diff11 = Math.sqrt(Math.pow(2, p1.f1.x - ff.x) + Math.pow(2, p1.f1.y - ff.y));
		diff12 = Math.sqrt(Math.pow(2, p1.f2.x - ff.x) + Math.pow(2, p1.f2.y - ff.y));
		diff21 = Math.sqrt(Math.pow(2, p2.f1.x - ff.x) + Math.pow(2, p2.f1.y - ff.y));
		diff22 = Math.sqrt(Math.pow(2, p2.f2.x - ff.x) + Math.pow(2, p2.f2.y - ff.y));
		double m = f.cur_height;
		double b = ff.cur_height;
		double step = b - m;
		if (m == 3 && depth == 1) {
			this.won = true;
			return 9999;
		}
		if (b == 3 && depth == 1) {
			if (diff21 <= 1.5 && p2.f1.cur_height >= 2) {
				lost = true;
				return -9999;
			}
			if (diff22 <= 1.5 && p2.f2.cur_height >= 2) {
				lost = true;
				return -9999;
			}
		}
		if (step > 1) {
			if (b == 4) {
				if (diff21 <= 1.5 && p2.f1.cur_height >= 2 && depth == 1) {
					deny = true;
					return 10000;
				}
				if (diff22 <= 1.5 && p2.f2.cur_height >= 2 && depth == 1) {
					deny = true;
					return 10000;
				}
				b = -4;
			}
			if (b == 3)
				b = 8;
			if (step == 2)
				step = -10;
			if (step == 3)
				step = -20;
			if (step == 4)
				step = -30;
		}

		if (delta > 0)
			delta = delta * (-100);
		if (delta == 0)
			delta = 2;
		if (delta == 1)
			delta = 5;

		// Treba build da bude +1 od m ali da to ne bude 4 ali b mora da bude 4 ukoliko je protivnicka figura pred njega
		// Odmah do X polja je ako je diffXY<=1.5
		double diff = Math.abs((diff11 + diff12) - (diff21 + diff22));
		double func = (m + step / 2 + delta * 2 + b) * (1 + diff);
		f_score = func;
		return f_score;
	}

	public void possibleChildren() throws CloneNotSupportedException {
		Table temp_table = table, temp_table_move, temp_table_build; // Kopiramo table da bi kasnije menjali
		List<Field> temp_fields_move = new ArrayList<Field>();
		List<Field> temp_fields_build = new ArrayList<Field>();
		Player player = null;
		Figure figure = null;

		if (isMax)
			player = temp_table.get_player(table.player1);// Max player=root player tj igrac koji pravi celo stablo
		else
			player = temp_table.get_player(temp_table.switch_player(table.player1));// Min igrac mu je protivnik

		if (player != null) {
			for (int p = 0; p < 2; p++) {// Dve figure

				temp_table = table;// Uzimamo tabelu pocetnog stanja da kada se vratimo na figuru 2 se vratimo na isto stanje
				if (p == 0)
					figure = player.f1;
				else
					figure = player.f2;
				temp_fields_move = figure.possible_moves();
				if (temp_fields_move == null)
					continue;

				for (int i = 0; i < temp_fields_move.size(); i++) {// Za svaki moguci slobodan field gde mozemo move
					temp_table_move = temp_table.clone();// Uzimamo tabelu da zapamtimo pomeranje
					figure = temp_table_move.find_figure(figure.id, figure.id2);// nalazimo istu figuru na novoj tabli
																				// koju cemo koristiti
					Field temp_field_move = temp_fields_move.get(i);// uzimamo taj field

					// Menjamo tekst i boju polja sa kojeg i na koje pomeramo figuru
					temp_field_move = temp_table_move.find_field(temp_field_move);// Prebacujemo referencu sa regularnog
																					// table na table_move
					int delta = figure.cur_height;
					figure.move(temp_field_move.x, temp_field_move.y);// Pomeramo
					delta -= figure.cur_height;
					temp_fields_build = figure.possible_builds();
					if (temp_fields_build == null)
						continue;

					for (int j = 0; j < temp_fields_build.size(); j++) {// Za sve moguce gradnje
						temp_table_build = temp_table_move.clone();// Uzimamo tabelu da zapamtimo gradnju
						figure = temp_table_build.find_figure(figure.id, figure.id2);// nalazimo istu figuru na novoj tabli koju cemo koristiti
						Field temp_field_build = temp_fields_build.get(j);
						figure.build(temp_field_build.x, temp_field_build.y);
						// Menjamo tekst i boju polja koje gradimo
						temp_field_build = temp_table_build.find_field(temp_field_build);// Prebacujemo referencu sa table_move na table_build
						Node temp = null;
						if (temp_field_move != null && temp_field_build != null)
							temp = new Node(temp_table_build, depth + 1, !isMax, temp_field_move, temp_field_build,
									delta);

						if (depth == table.sant.diff-1 && temp.f_score == table.sant.max) children.add(0, temp);
						if (depth == table.sant.diff-1 && temp.f_score == table.sant.max1 && children.size() > 1) children.add(1, temp);
						if (depth == table.sant.diff-1 && temp.f_score == table.sant.max2 && children.size() > 2) children.add(2, temp);
						else children.add(temp);

						// prvo napravi new Node a tek onda kad se rekurzija vrati posle pravljenja sve dece
						// sortiraj najefikasnije po f_score tako da su najbolji pri pocetku da bi njih alfa beta odsecanje
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
