package etf.santorini.jl150377d;

public class Figure {
	public int id,id2;
	private int x,y;
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
	
	public boolean is_movable() {
		boolean flag=false;
		Field temp;
		for(int i=this.x-1; i<=this.x+1; i++)
			for(int j=this.y-1; j<=this.y+1; j++) {
				if(!table.field_exist(i,j)) continue;
				if(x==i && y==j) continue;
				temp=table.get_field(i,j);
				if(temp.get_height()-cur_height<2)flag=true;
			}
		if(flag==true)return false;
		else return true;
	}
	
	public boolean possible_to_move(int x, int y) {
		Field dest=table.get_field(x,y);
		if(!table.field_exist(x,y)) return false;
		if(dest.get_height()-cur_height>1 || dest.is_taken() || Math.abs(x-this.x)>1 || Math.abs(y-this.y)>1 || (x==this.x && y==this.y)) return false;
		return true;
	}
	
	public boolean move(int x, int y) {
		if(!this.possible_to_move(x, y)) return false;
		Field old=table.get_field(this.x, this.y);
		old.remove_figure();
		remove_field();
		this.x=x;
		this.y=y;
		Field cur=table.get_field(x,y);
		cur.add_figure(this);
		add_field(cur);
		cur_height=cur.get_height();
		table.text_file.print(table.convert(old.y, old.x)+" "+table.convert(cur.y, cur.x)+" ");
		return true;
	}
	
	private void add_field(Field cur) {
		this.f=cur;
		
	}

	private void remove_field() {
		this.f=null;
		
	}

	public boolean possible_to_build(int x, int y) {
		Field temp=table.get_field(x,y);
		if(!table.field_exist(x,y)) return false;
		if(temp.get_height()==4 || temp.is_taken() || Math.abs(x-this.x)>1 || Math.abs(y-this.y)>1 || (x==this.x && y==this.y)) {
			return false;}
		return true;
	}
	
	public boolean build(int x, int y) {
		if(!this.possible_to_build(x, y)) return false;
		Field temp=table.get_field(x,y);
		temp.build();
		table.text_file.println(table.convert(temp.y, temp.x));
		return true;
	}
}
