import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.nio.Buffer;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.PriorityQueue;
import java.util.Stack;

public class Solver {
	// find a solution to the initial
	// board (using the A* algorithm)

	ArrayList<Board> solution;

	Comparator<SearchNode> evaluationFunctionHamming = new Comparator<SearchNode>() {

		@Override
		public int compare(SearchNode sn1, SearchNode sn2) {
			int fn1 = sn1.moves + sn1.currentBoard.hamming();
			int fn2 = sn2.moves + sn2.currentBoard.hamming();
			return fn1 - fn2;
		}

	};
	
	Comparator<SearchNode> evaluationFunctionManhattan = new Comparator<SearchNode>() {

		@Override
		public int compare(SearchNode sn1, SearchNode sn2) {
			int fn1 = sn1.moves + sn1.currentBoard.manhattan();
			int fn2 = sn2.moves + sn2.currentBoard.manhattan();
			return fn1 - fn2;
		}

	};

	Comparator<SearchNode> evaluationFunctionLinearConflict = new Comparator<SearchNode>() {

		@Override
		public int compare(SearchNode sn1, SearchNode sn2) {
			int fn1 = sn1.moves + sn1.currentBoard.linearConflict();
			int fn2 = sn2.moves + sn2.currentBoard.linearConflict();
			return fn1 - fn2;
		}

	};

	public Solver(Board initial) {

		solution = (ArrayList<Board>) aStarSeacrch(initial);

	}

	private Iterable<Board> aStarSeacrch(Board initial) {
		SearchNode initialSearchNode = new SearchNode(initial, null, 0);

		// If using manhattan heuristic change the second parameter of following
		// statement to evaluationFucntionManhattan else for linear conflict
		// evaluationFucntionLinearConflict

		PriorityQueue<SearchNode> pq = new PriorityQueue<>(10,
				evaluationFunctionManhattan);
		pq.add(initialSearchNode);
		SearchNode cur, temp;

		while ((cur = pq.poll()) != null) {
			if (cur.currentBoard.isGoal())
				break;

			ArrayList<Board> neighbourList = (ArrayList<Board>) cur.currentBoard
					.neighbors();
			for (Board board : neighbourList) {
				if (cur.parent != null && board.equals(cur.parent.currentBoard))
					continue;
				temp = new SearchNode(board, cur, cur.moves + 1);
				pq.add(temp);
			}
		}

		Stack<Board> solution = new Stack<>();

		while (cur != null) {
			solution.push(cur.currentBoard);
			cur = cur.parent;
		}

		ArrayList<Board> solList = new ArrayList<>();

		while (!solution.isEmpty()) {

			solList.add(solution.pop());
		}

		return solList;
	}

	// min number of moves to solve
	// initial board
	public int moves() {
		return solution.size() - 1;
	}

	// sequence of boards in a
	// shortest solution
	public Iterable<Board> solution() {
		return solution;
	}

	// solve a slider puzzle
	// (code given below)}

	public static void main(String[] args) throws Exception {
		// create initial board from file

		In in = new In(args[0]);
		int N = in.readInt();
		int[][] blocks = new int[N][N];
		for (int i = 0; i < N; i++)
			for (int j = 0; j < N; j++)
				blocks[i][j] = in.readInt();
		Board initial = new Board(blocks);
		// check if puzzle is solvable; if so, solve it and output solution
		if (initial.isSolvable()) {
			Solver solver = new Solver(initial);
			System.out.println("Minimum number of moves = " + solver.moves());
			for (Board board : solver.solution())
				System.out.println(board);
		}
		// if not, report unsolvable
		else {
			System.out.println("Unsolvable puzzle");

		}
	}
}

class SearchNode {
	public SearchNode(Board currentBoard, SearchNode previousBoard, int moves) {
		super();
		this.currentBoard = currentBoard;
		this.parent = previousBoard;
		this.moves = moves;
	}

	Board currentBoard;
	SearchNode parent;
	// g(n)
	int moves;
}
