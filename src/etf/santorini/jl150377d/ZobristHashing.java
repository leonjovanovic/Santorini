package etf.santorini.jl150377d;

import java.util.HashMap;

public class ZobristHashing {

	private int hash;
	private final int[][][] bitStrings;
	
	public ZobristHashing() {
		bitStrings=new int [5][5][4];
		 for (int i = 0; i<8; i++) 
		      for (int j = 0; j<8; j++) 
		        for (int k = 0; k<12; k++) 
		        	bitStrings[i][j][k] = randomInt(); 
	}
	
	public int randomInt(){ 
		return (int) (((long) (Math.random() * Long.MAX_VALUE)) & 0xFFFFFFFF);
	} 
	
	int indexOf(Figure piece) 
	{ 
	    if (piece.id==1&&piece.id2==1) 
	        return 0; 
	    if (piece.id==1&&piece.id2==2) 
	        return 1; 
	    if (piece.id==2&&piece.id2==1) 
	        return 2; 
	    if (piece.id==2&&piece.id2==2) 
	        return 3; 
	    else
	        return -1; 
	} 
	
	public int computeHash(Table table) 
	{ 
	    int h = 0; 
	    for (int i = 0; i<5; i++) 
	    { 
	        for (int j = 0; j<5; j++) 
	        { 
	            if (table.lista[i][j].is_taken()) 
	            { 
	                int piece = indexOf(table.lista[i][j].figure); 
	                h ^= bitStrings[i][j][piece]; 
	            } 
	        } 
	    } 
	    return h; 
	} 
}
