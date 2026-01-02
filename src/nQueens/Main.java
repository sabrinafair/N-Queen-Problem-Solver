package nQueens;

import java.util.PriorityQueue;
import java.util.Random;
import java.util.Scanner;

import nQueens.HelperFunctions;
//import nQueens.HelperFunctions.IntPair;

public class Main {
	
	public static int displayMenu(Scanner scan) {
		System.out.println("-------------------");
		System.out.println("N-Queen(N=8) Problem");
		System.out.println("-------------------");
		System.out.println("Select Algorithm for Local Search");
		System.out.println("[1] -------- Steepest-Ascent Hill Climbing");
		System.out.println("[2] -------- CSP MIN Conflict");
//		System.out.println("[3] -------- Genetic");
		
		int algOption = scan.nextInt(); 
		scan.nextLine();
		
		return algOption;
	}
	
	public static void displayResults(int countSolves, double percCorrect, long totalTimeNano, int countTotalSteps) {
		System.out.println("-----------------------------------");
		System.out.println("-----------------------------------");
		System.out.println("-----------------------------------");
		System.out.println("Total N-Queens Solved: " + countSolves);
		System.out.println("Percent correct: " + percCorrect + "%");
		System.out.println("Average Total Time ns: " + (totalTimeNano / countSolves));
		System.out.println("Average Steps Taken: " + (countTotalSteps / countSolves));
	}
	
	public static String initPuzzle(int n) {
		String puzzle = "";
		Random rand = new Random();
		for(int i = 0; i < n; i++) {
			int randNum = rand.nextInt(n) + 1;
			puzzle += randNum;
		}
		
		return puzzle;
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
	
	public static String replaceChar(String str, int i, char c) {
		String newStr = str.substring(0, i) + c + str.substring(i + 1);
		return newStr;
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
	
	public static String[] useClimbHill(String puzzle, int n) {
		boolean endSearch = false;
		String newPuzzle = puzzle;
		Integer steps = 1;
		
		while(!endSearch) {
			PriorityQueue<puzzleState> currOptions = getAllAttacksOptions(newPuzzle, n);
			int currTotal = getCurrTotalAttackers(newPuzzle);
			puzzleState selectedOption = currOptions.poll();
			if(selectedOption.cost >= currTotal) {
				endSearch = true;
				break;
			}else {
				int selectedVal = selectedOption.row;
				int selectedIndex = selectedOption.col;
				newPuzzle = replaceChar(newPuzzle, selectedIndex - 1, (char) (selectedVal + '0'));
				currOptions.clear();
			}
			steps++;
		}
		
		String[] options = {newPuzzle, steps.toString()}; 
		return options;
	}

	
	public static String[] useCSP(String puzzle, int n, int maxSteps) {
		Integer steps = 1;
		String newPuzzle = puzzle; //initial complete assignment
		boolean solved = false;
		Random rand = new Random();
		
		while(steps < maxSteps && !solved) {
			int currTotal = getCurrTotalAttackers(newPuzzle);
			if(currTotal == 0) { //if current is a solution to csp
				solved = true;
				break;
			}
			
			int randCol = rand.nextInt(n) + 1;
			PriorityQueue<puzzleState> currOptions = getAttacksOptionsByColumn(newPuzzle, randCol, n);
			puzzleState selectedOption = currOptions.poll(); //get the lowest option
			if(selectedOption.cost < currTotal) { //if total cost has improved take option
				int selectedVal = selectedOption.row;
				int selectedIndex = selectedOption.col;
				newPuzzle = replaceChar(newPuzzle, selectedIndex - 1, (char) (selectedVal + '0'));
				currOptions.clear();
			}
			
			steps++;
		}
		String[] options = {newPuzzle, steps.toString()}; 
		return options;
	}
	
	public static String useGeneric(String puzzle) {
		//crossover point randomly taken
		return puzzle;
	}

	public static void main(String[] args) {
		System.out.println("CS 4200 - Project 1");
		Scanner scanner = new Scanner(System.in);
		
		int algChosen = displayMenu(scanner);
		int N = 8;

		String puzzle = "";
		String finalState = "";
		
		System.out.println("Enter number of times to run solver: ");
		int runSolver = scanner.nextInt(); 
		scanner.nextLine();
	

		if(algChosen == 1) {
			int countSolves = 0;
			int countTotalSteps = 0;
			int totalAttackers = 0;
			long totalTimeNano = 0;
			
			for(int i = 0; i < runSolver; i++) {
				long startTime = System.nanoTime();
				long endTime = System.nanoTime();
				long durationTimeNano = 0;
				long durationTimeMili = 0;
				
				puzzle = initPuzzle(N);

				System.out.println();
				System.out.println("-----------------------------------");
				System.out.println("Initial Puzzle: " + puzzle);
				
				String[] result = useClimbHill(puzzle, N);
				finalState = result[0];
				totalAttackers = getCurrTotalAttackers(finalState);

				System.out.println("Final State: " + finalState);
				System.out.println("Final Total Attackers: " + totalAttackers);	
				
				if(totalAttackers == 0) {
					countSolves++;
					durationTimeNano = endTime - startTime;
					System.out.println("Time ns: " + durationTimeNano);
					System.out.println("Steps Taken: " + result[1]);
					totalTimeNano = totalTimeNano +  durationTimeNano;
					countTotalSteps += Integer.parseInt(result[1]);
				}
			}
			
			double percCorrect = (((double)countSolves / (double)runSolver)) * 100;
			displayResults(countSolves, percCorrect, totalTimeNano, countTotalSteps);
			
		} else if (algChosen == 2) {
			int countSolves = 0;
			int countTotalSteps = 0;
			int totalAttackers = 0;
			long totalTimeNano = 0;
			long totalTimeMili = 0;
			
			for(int i = 0; i < runSolver; i++) {
				long startTime = System.nanoTime();
				long endTime = System.nanoTime();
				long durationTimeNano = 0;
				
				puzzle = initPuzzle(N);

				System.out.println();
				System.out.println("-----------------------------------");
				System.out.println("Initial Puzzle");
				System.out.println(puzzle);
				String[] result = useCSP(puzzle, N, 100);
				finalState = result[0];
				totalAttackers = getCurrTotalAttackers(finalState);

				System.out.println("Final State" + finalState);				
				System.out.println("Final Total Attackers: " + totalAttackers);	
				
				if(totalAttackers == 0) {
					countSolves++;
					durationTimeNano = endTime - startTime;
					System.out.println("Time ns: " + durationTimeNano);

					System.out.println("Steps Taken: " + result[1]);
					totalTimeNano = totalTimeNano +  durationTimeNano;
					countTotalSteps += Integer.parseInt(result[1]);
				}
			}
			
			double percCorrect = (((double)countSolves / (double)runSolver)) * 100;
			displayResults(countSolves, percCorrect, totalTimeNano, countTotalSteps);
			
		}else if (algChosen == 3) finalState = useGeneric(puzzle);
		else {
			System.out.println("Incorrect input");	
		}
		
	}

}
