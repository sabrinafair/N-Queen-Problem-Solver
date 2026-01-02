package nQueens;

public class puzzleState implements Comparable<puzzleState>{
	public final Integer cost;
	public final Integer row;
	public final Integer col;
	
	public puzzleState(Integer cost, Integer row, Integer col) {
		this.cost = cost;
		this.row = row;
		this.col = col;
	}
	
	@Override
	public String toString() {
		return "(" + row + ", " + col + "): " + cost;
	}
	
	@Override
	public int compareTo(puzzleState o) {
		// TODO Auto-generated method stub
		return Integer.compare(this.cost, o.cost);
	}	
}
