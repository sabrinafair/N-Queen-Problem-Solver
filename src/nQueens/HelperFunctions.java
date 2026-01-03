package nQueens;

import java.util.PriorityQueue;

public class HelperFunctions {
	
	public static String replaceChar(String str, int i, char c) {
		String newStr = str.substring(0, i) + c + str.substring(i + 1);
		return newStr;
	}
	
	public static int countUnderAttack(String puzzle, int location) {
		//under attack if there are queens in: it row or diagonal
		int count = 0;
		int onRow = puzzle.charAt(location - 1) - '0';
		
		for(int i = 1; i <= puzzle.length(); i++) {
			if(location == i) continue;
			int currQueen = puzzle.charAt(i - 1) - '0';
			int additive = Math.abs(location - i);
			int diagonalCheck1 = onRow + additive;
			int diagonalCheck2 = onRow - additive;
			if(currQueen == onRow) count++; //checks if on same row
			//checks if on diagonal
			if(currQueen == diagonalCheck1) count++;
			if(currQueen == diagonalCheck2) count++;	
		}
		
		return count;
	}
	
	public static int getCurrTotalAttackers(String puzzle) {
		int total = 0;
		
		for(int i = 1; i <= puzzle.length(); i++) {
			total += countUnderAttack(puzzle, i);
		}
		return total / 2;
	}
	
	public static PriorityQueue<puzzleState> getAttacksOptionsByColumn(String puzzle, int col, int n) {
		PriorityQueue<puzzleState> currOptions = new PriorityQueue<>();
		
			String currPuzzle = puzzle;
			for(int j = 1; j <= n; j++) { //loop through testing queen at all places in col for 1 to N
				currPuzzle = replaceChar(currPuzzle, col - 1, (char) (j + '0'));
				int currTotal = countUnderAttack(currPuzzle, col); //get # for current queen under attack
				currOptions.add(new puzzleState(currTotal, j, col)); //add to priority queue
			}
		return currOptions;
	}

	public static PriorityQueue<puzzleState> getAllAttacksOptions(String puzzle, int n) {
		PriorityQueue<puzzleState> currOptions = new PriorityQueue<>();
		
		for(int i = 1; i <= n; i++) {
			String currPuzzle = puzzle;
			for(int j = 1; j <= n; j++) {
				currPuzzle = replaceChar(currPuzzle, i - 1, (char) (j + '0'));
				int currTotal = getCurrTotalAttackers(currPuzzle);
				currOptions.add(new puzzleState(currTotal, j, i));
			}
		}
		return currOptions;
	}
	
}
