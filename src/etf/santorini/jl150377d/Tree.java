package etf.santorini.jl150377d;

import java.util.ArrayList;
import java.util.List;

public class Tree {
	public Node root;
	Table table;
	
	public Tree(Table table) {
		this.table=table;
		root=new Node(table,0,true);
	}
	
	public Node next_root() {
		Double score=root.f_score;
		//if(score==0)return null;
		List<Node> children=new ArrayList<Node>();
		children=root.children;
		for(int i=0;i<children.size();i++) {
			if(children.get(i).f_score==score)return children.get(i);
		}
		return null;
	}

}
