package nQueens;

import java.util.PriorityQueue;
import java.util.Random;
import java.util.Scanner;

import nQueens.HelperFunctions;

public class Main {
	
	public static int displayMenu(Scanner scan) {

		System.out.println("CS 4200 - Project 1: N-Queen(N=8) Problem");
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
	
	public static String[] useClimbHill(String puzzle, int n) {
		boolean endSearch = false;
		String newPuzzle = puzzle;
		Integer steps = 1;
		
		while(!endSearch) { //algorithm loop
			PriorityQueue<puzzleState> currOptions = HelperFunctions.getAllAttacksOptions(newPuzzle, n);
			int currTotal = HelperFunctions.getCurrTotalAttackers(newPuzzle); //set current
			puzzleState selectedOption = currOptions.poll();
			if(selectedOption.cost >= currTotal) { //check for if Value[neighbor] <= Value[current]
				endSearch = true;
				break;
			}else {
				int selectedVal = selectedOption.row;
				int selectedIndex = selectedOption.col;
				newPuzzle = HelperFunctions.replaceChar(newPuzzle, selectedIndex - 1, (char) (selectedVal + '0'));
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
			int currTotal = HelperFunctions.getCurrTotalAttackers(newPuzzle);
			if(currTotal == 0) { //if current is a solution to csp
				solved = true;
				break;
			}
			
			int randCol = rand.nextInt(n) + 1;
			PriorityQueue<puzzleState> currOptions = HelperFunctions.getAttacksOptionsByColumn(newPuzzle, randCol, n);
			puzzleState selectedOption = currOptions.poll(); //get the lowest option
			if(selectedOption.cost < currTotal) { //if total cost has improved take option
				int selectedVal = selectedOption.row;
				int selectedIndex = selectedOption.col;
				newPuzzle = HelperFunctions.replaceChar(newPuzzle, selectedIndex - 1, (char) (selectedVal + '0'));
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
		Scanner scanner = new Scanner(System.in);
		
		int algChosen = displayMenu(scanner); //get algorithm to use
		int N = 8; //can set N here if want to test different N values
		
		//variable to use
		String puzzle = "";
		String finalState = "";
		
		System.out.println("Enter number of times to run solver: ");
		int runSolver = scanner.nextInt(); 
		scanner.nextLine();
	

		if(algChosen == 1) { 
			//HILL CLIMBING ALGORITHM
			
			//variables for averages and timing
			int countSolves = 0;
			int countTotalSteps = 0;
			int totalAttackers = 0;
			long totalTimeNano = 0;
			
			for(int i = 0; i < runSolver; i++) {
				long startTime = System.nanoTime();
				long endTime = System.nanoTime();
				long durationTimeNano = 0;
				
				puzzle = initPuzzle(N);

				System.out.println();
				System.out.println("-----------------------------------");
				System.out.println("Initial Puzzle: " + puzzle);
				
				String[] result = useClimbHill(puzzle, N);
				finalState = result[0]; //result returns array with final state of queens problem result[0] and steps taken result[1]
				
				totalAttackers = HelperFunctions.getCurrTotalAttackers(finalState);

				System.out.println("Final State: " + finalState);
				System.out.println("Final Total Attackers: " + totalAttackers);	
				
				if(totalAttackers == 0) { //check of solution is completely solved
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
			//CSP MIN CONFLICT ALGORITHM
			
			//variables for averages and timing
			int countSolves = 0;
			int countTotalSteps = 0;
			int totalAttackers = 0;
			long totalTimeNano = 0;
			
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
				finalState = result[0]; //result returns array with final state of queens problem result[0] and steps taken result[1]
				totalAttackers = HelperFunctions.getCurrTotalAttackers(finalState);

				System.out.println("Final State" + finalState);				
				System.out.println("Final Total Attackers: " + totalAttackers);	
				
				if(totalAttackers == 0) { //check of solution is completely solved
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
			
		}else {
			System.out.println("Incorrect input");	
		}
		
	}

}
