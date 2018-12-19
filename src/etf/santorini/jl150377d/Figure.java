package etf.santorini.jl150377d;

import java.util.ArrayList;
import java.util.List;

public class Figure {
	public int id,id2;
	public int x,y;
	private int cur_height;
	public Field f;
	private Table table;
	
	public Figure(int x, int y, Table table,int id,int id2) {
		this.x=x;
		this.y=y;	
		this.table=table;
		f=table.get_field(x,y);
		cur_height=0;
		this.id=id;
		this.id2=id2;
	}
	
	public int id() {
		return id;
	}
	
	public boolean isWinner() {
		if(this.f.cur_height==3)return true;
		return false;
	}
	
	public boolean is_movable() {
		Field temp;
		for(int i=this.x-1; i<=this.x+1; i++)
			for(int j=this.y-1; j<=this.y+1; j++) {
				if(!table.field_exist(i,j)) continue;
				if(x==i && y==j) continue;
				temp=table.get_field(i,j);
				if(possible_to_move(i,j))return true;
			}
		return false;
	}
	
	public boolean possible_to_move(int x, int y) {
		Field dest=table.get_field(x,y);
		if(!table.field_exist(x,y)) return false;
		if(dest.get_height()-cur_height>1 || dest.is_taken() || Math.abs(x-this.x)>1 || Math.abs(y-this.y)>1 || (x==this.x && y==this.y) || dest.cur_height==4) return false;
		return true;
	}
	
	public List<Field> possible_moves() {
		Field temp;
		List<Field> ret=new ArrayList<Field>();  
		
		for(int i=this.x-1; i<=this.x+1; i++)
			for(int j=this.y-1; j<=this.y+1; j++) {
				if(!table.field_exist(i,j)) continue;
				if(x==i && y==j) continue;
				temp=table.get_field(i,j);
				if(possible_to_move(i,j))ret.add(temp);
			}
		if(ret.isEmpty())return null;
		return ret;
	}
	
	public boolean move(int x, int y) {
		if(!this.possible_to_move(x, y)) return false;
		Field old=table.get_field(this.x, this.y);//Uzima staro polje
		old.remove_figure();//izbacuje figuru sa polja
		remove_field();//izbacuje polje sa figure
		this.x=x;
		this.y=y;
		Field cur=table.get_field(x,y);//novo polje
		cur.add_figure(this);//dodaj figuru na to polje
		add_field(cur);//polje na figuru
		cur_height=cur.get_height();//updatuj visinu
		if(table.sant.load==0)table.text_file.print(table.encrypt(old.y, old.x)+" "+table.encrypt(cur.y, cur.x)+" ");//zapisi pomeraj
		return true;
	}
	

	public boolean possible_to_build(int x, int y) {
		Field temp=table.get_field(x,y);
		if(!table.field_exist(x,y)) return false;
		if(temp.get_height()==4 || temp.is_taken() || Math.abs(x-this.x)>1 || Math.abs(y-this.y)>1 || (x==this.x && y==this.y)) {
			return false;}
		return true;
	}
	
	public List<Field> possible_builds() {
		Field temp;
		List<Field> ret=new ArrayList<Field>();  
		
		for(int i=this.x-1; i<=this.x+1; i++)
			for(int j=this.y-1; j<=this.y+1; j++) {
				if(!table.field_exist(i,j)) continue;
				if(x==i && y==j) continue;
				temp=table.get_field(i,j);
				if(possible_to_build(i,j))ret.add(temp);
			}
		if(ret.isEmpty())return null;
		return ret;
	}
	
	public boolean build(int x, int y) {
		if(!this.possible_to_build(x, y)) return false;
		Field temp=table.get_field(x,y);
		temp.build();
		table.text_file.println(table.encrypt(temp.y, temp.x));
		return true;
	}	

	private void add_field(Field cur) {
		this.f=cur;
		
	}

	private void remove_field() {
		this.f=null;
		
	}
}
