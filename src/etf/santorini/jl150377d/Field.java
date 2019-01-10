package etf.santorini.jl150377d;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JButton;
import javax.swing.JOptionPane;

public class Field implements Cloneable {
	public Color color;
	public Table table, test = null;
	public Figure figure;
	public int x, y;
	public int cur_height;
	public int id, id2;
	public JButton button;
	private FieldAction listener = new FieldAction();

	public Field(Table table, int x, int y) {
		this.table = table;
		this.x = x;
		this.y = y;
		cur_height = 0;
		figure = null;
		id = id2 = 0;
		color = Color.GREEN;
		button = new JButton(cur_height + " / " + id + "" + id2);
		button.setBackground(Color.GREEN);
		button.setForeground(Color.BLACK);
		button.setMaximumSize(new Dimension(100, 100));
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
		figure = null;
		id = 0;
		id2 = 0;
	}

	public void add_figure(Figure f) {
		figure = f;
		id = f.id;
		id2 = f.id2;
	}

	public JButton get_button() {
		return button;
	}

	public boolean is_taken() {
		if (figure != null)
			return true;
		else
			return false;
	}

	public void build() {
		cur_height++;
	}

	public Figure get_figure() {
		return figure;
	}

	private class FieldAction implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent arg) {
			Object source = arg.getSource();
			JButton f = (JButton) source;

			if (table.start) {
				// *****SELECT*******
				if (table.get_firstClick() && figure != null && table.player1 == id) { // FIGURA KOJU KORISTIMO
					table.origin = figure;
					table.origin.f.button.setBackground(Color.RED); // Selektujemo figuru koja gradi
					table.set_first_click(false);
				}
				// POLJE KOJE GRADIMO/NA KOJE SE PREMESTANO
				else {
					if (table.origin == null)
						return;// Moramo prvo da selektujemo neko polje sa figurom
					table.origin.f.button.setBackground(table.origin.f.color); // Vracamo staru boju figuri koja gradi
					switch (table.build) {
					// *******MOVE********
					case 0: {
						if (table.origin.id != table.player1) {
							table.set_first_click(true);
							return;
						} // Da ne bi smo smeli da pomeramo figuricu igraca koji nije na redu
						Field origin_f = table.origin.f;
						boolean flag = table.origin.move(x, y);// pomeri figuricu na x,y
						if (flag) {
							button.setText(cur_height + " / " + id + "" + id2);// promeni naziv polja **NA** koje smo
																				// pomerili
							origin_f.button.setText(origin_f.cur_height + " / " + origin_f.id + "" + origin_f.id2);// promeni naziv polja **SA** kojeg smo pomerili
							table.current_player = figure;// Ona figura koju smo pomerili da bi kasnije samo ona mogla da gradi, da bi izbegli da jedna pomera a druga gradi
							table.build = (table.build + 1) % 2;// Ovim vrtimo da MOVE i BUILD idu naizmenicno
							if (figure.isWinner()) {
								if (table != null)
									table.text_file.close();
								table.sant.end_game = true;
								JOptionPane.showMessageDialog(table.sant, "Player " + figure.id + " is winner!");
								table.sant.dispose();
								table.sant.reset();
								table.p1.reset();
								table.p2.reset();
							}
						}
						table.set_first_click(true);
						break;
					}
					// *********BUILD*********
					case 1: {
						if (table.origin != table.current_player) {
							table.set_first_click(true);
							return;
						} // da ne bi dozvolili da neka druga figurica gradi osim one koje se pomerila
						if (table.origin.id != table.player1) {
							table.set_first_click(true);
							return;
						} // da figurica igraca koji nije na redu ne bi mogla da gradi
						boolean flag = table.origin.build(x, y);// gradi
						if (flag) {// ukoliko uspesno
							button.setText(cur_height + " / " + id + "" + id2);// promeni naziv polja koji smo gradili i
							switch (cur_height) {// zavisnosti od nove visine polja promeni mu boju
							case 1:
							case 2:
							case 3: {
								change_color(Color.WHITE);
								button.setBackground(color);
								break;
							}
							case 4: {
								change_color(Color.CYAN);
								button.setBackground(color);
								break;
							}
							}
							table.build = (table.build + 1) % 2;// Ovim vrtimo da MOVE i BUILD idu naizmenicno
							if (table.player1 == 1)
								table.player1 = 2;
							else if (table.player1 == 2)
								table.player1 = 1;// ovim vrtimo igrace sa id=1 i 2 koje gore ispitujemo
							table.sant.stanje.setText(
									" State: Player " + table.player1 + " is on move. Previous player moved figure "
											+ table.sant.coordM + " and built on " + table.sant.coordB);
							if (table.get_player(table.player1).isLoser()) {
								if (table != null)
									table.text_file.close();
								table.sant.end_game = true;
								JOptionPane.showMessageDialog(table.sant, "Player " + table.player1 + " is loser!");
								table.sant.dispose();
								table.sant.reset();
								table.p1.reset();
								table.p2.reset();
							}
							if (table.sant.mode != 0)
								table.start = false;
						}
						table.set_first_click(true);// Mozemo opet da selektujemo

						break;
					}
					}
				}
				return;
			}
			// *******Postavljanje figurica************
			if (table.p11) {
				table.p1.create_figures1(x, y);
				add_figure(table.p1.f1);
				table.p11 = false;
				f.setText(cur_height + " / " + id + "" + id2);
				table.sant.stanje.setText(" State: Player 1, pick starting position for your second figurine.");
				return;
			}
			if (table.p12 && table.p1.f1.f != get_field()) {
				table.p1.create_figures2(x, y);
				add_figure(table.p1.f2);
				table.p12 = false;
				f.setText(cur_height + " / " + id + "" + id2);
				table.sant.stanje.setText(" State: Player 2, pick starting position for your first figurine.");
				table.text_file.println(table.encrypt(table.p1.f1.f.y, table.p1.f1.f.x) + " "
						+ table.encrypt(table.p1.f2.f.y, table.p1.f2.f.x));
				return;
			}
			if (table.p21 && table.p1.f1.f != get_field() && table.p1.f2.f != get_field() && table.sant.mode == 0) {
				table.p2.create_figures1(x, y);
				add_figure(table.p2.f1);
				table.p21 = false;
				f.setText(cur_height + " / " + id + "" + id2);
				table.sant.stanje.setText(" State: Player 2, pick starting position for your second figurine.");
				return;
			}
			if (table.p22 && table.p1.f1.f != get_field() && table.p1.f2.f != get_field() && table.sant.mode == 0
					&& table.p2.f1.f != get_field()) {
				table.p2.create_figures2(x, y);
				add_figure(table.p2.f2);
				table.p22 = false;
				f.setText(cur_height + " / " + id + "" + id2);
				table.firstClick = table.start = true;// Zavrseno postavljanje, figurice mogu da se krecu
				table.player1 = 1;
				table.text_file.println(table.encrypt(table.p2.f1.f.y, table.p2.f1.f.x) + " "
						+ table.encrypt(table.p2.f2.f.y, table.p2.f2.f.x));
				table.sant.stanje.setText(" State: Player " + table.player1 + " is on the move.");
				return;
			}
		}
	}

	public void change_color(Color clr) {
		color = clr;
	}

	public Field get_field() {
		return this;
	}

	public void change_color_on_height(int i) {
		switch (i) {// zavisnosti od nove visine polja promeni mu boju
		case 1:
		case 2:
		case 3: {
			change_color(Color.WHITE);
			button.setBackground(color);
			break;
		}
		case 4: {
			change_color(Color.CYAN);
			button.setBackground(color);
			break;
		}
		}
	}

	public String toString() {
		String s = "x: " + x + " y: " + y;
		return s;
	}

	public Field clone() throws CloneNotSupportedException {
		Field field = (Field) super.clone();
		field.x = this.x;
		field.y = this.y;
		field.cur_height = this.cur_height;
		field.id = this.id;
		field.id2 = this.id2;
		field.color = this.color;
		return field;
	}
}
