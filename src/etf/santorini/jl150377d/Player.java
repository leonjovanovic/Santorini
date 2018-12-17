package etf.santorini.jl150377d;

public class Player {
	
	protected Table table;
	public Figure f1,f2;
	private static int pos_id=0;
	protected int id=++pos_id,id2;
	
	public Player(Table table) {
		this.table=table;
		id2=0;
	}
	
	public void create_figures1(int x1, int y1) {
		id2++;
		f1=new Figure(x1,y1,table,id,id2);
	}
	public void create_figures2(int x2, int y2) {
		id2++;
		f2=new Figure(x2,y2,table,id,id2);
	}
	

	public boolean isLoser() {
		if(!this.f1.is_movable()&&!this.f2.is_movable())return true;
		return false;
	}
	
}
