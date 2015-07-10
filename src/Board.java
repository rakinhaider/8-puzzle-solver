import java.awt.List;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;

import javax.swing.text.StyledEditorKit.BoldAction;

public class Board {
	// construct a board from an N-by-N
	// array of blocks
	// (where blocks[i][j] = block in
	// row i, column j)
	int[][] blocks = null;
	int N;

	public Board(int[][] blocks) {
		// this.blocks = blocks;
		N = blocks.length;
		this.blocks = new int[N][];
		for (int i = 0; i < blocks.length; i++) {
			this.blocks[i] = new int[N];
			for (int j = 0; j < blocks.length; j++) {
				this.blocks[i][j] = blocks[i][j];
			}
		}

	}

	// board size N
	public int size() {
		if (blocks == null)
			return 0;
		return blocks[0].length;
	}

	// number of blocks out of place
	public int hamming() {
		int i, j, k, ham;
		ham = 0;
		k = 1;
		for (i = 0; i < N; i++) {
			for (j = 0; j < N; j++) {
				if (blocks[i][j] != k && blocks[i][j] != 0)
					ham++;
				k++;
			}
		}
		return ham;
	}

	// sum of Manhattan distances between blocks and goal
	public int manhattan() {
		int i, j, k, man = 0;
		int expectedI, expectedJ;
		for (i = 0; i < N; i++) {
			for (j = 0; j < N; j++) {
				if (blocks[i][j] == 0)
					continue;
				expectedJ = (blocks[i][j] - 1) % N;
				expectedI = (blocks[i][j] - 1) / N;
				man += (expectedI > i) ? (expectedI - i) : (i - expectedI);
				man += (expectedJ > j) ? (expectedJ - j) : (j - expectedJ);
			}
		}
		return man;
	}

	public int linearConflict() {

		int man = manhattan();
		int rowConflict=getRowConflict();
		int colConflict=getColConflict();
		return man+rowConflict*2+colConflict*2;
	}

	private int getColConflict() {
		// TODO Auto-generated method stub
		int expectedI, expectedJ,conflict=0;
		int nextExpectedI, nextExpectedJ;
		for (int k = 0; k < N; k++) {
			for (int i = 0; i < N; i++) {
			
				expectedJ = (blocks[i][k] - 1) % N;
				expectedI = (blocks[i][k] - 1) / N;
				
				if(expectedJ==k)
				{
					for (int j = i + 1; j < N; j++) {
						nextExpectedJ = (blocks[j][k] - 1) % N;
						nextExpectedI = (blocks[j][k] - 1) / N;
						
						if(expectedJ==nextExpectedJ && nextExpectedI<expectedI)
						{
							conflict+=1;
						}
					}
				}
			}
		}
		return conflict;
	}

	private int getRowConflict() {
		// TODO Auto-generated method stub
		int expectedI, expectedJ,conflict=0;
		int nextExpectedI, nextExpectedJ;
		for (int i = 0; i < N; i++) {
			for (int k = 0; k < N; k++) {
				expectedJ = (blocks[i][k] - 1) % N;
				expectedI = (blocks[i][k] - 1) / N;
				
				if(expectedI==i)
				{
					for (int j = k + 1; j < N; j++) {
						nextExpectedJ = (blocks[i][j] - 1) % N;
						nextExpectedI = (blocks[i][j] - 1) / N;
						
						if(expectedI==nextExpectedI && nextExpectedJ<expectedJ)
						{
							conflict+=1;
						}
					}
				}
			}
		}
		return conflict;
	}

	// is this board the goal board?
	public boolean isGoal() {
		if (hamming() == 0)
			return true;
		else
			return false;
	}

	// is the board solvable?
	public boolean isSolvable() {
		if (N % 2 == 1)
			return checkOddSizeBoard();
		else
			return checkEvenSizedBoard();
	}

	private boolean checkEvenSizedBoard() {
		int i, j, k;
		int inversion = 0;
		int blankRow = 0;
		int[] blockArray = generateBlockArray();

		for (i = 0; i < N * N; i++) {
			for (j = i + 1; j < N * N; j++) {
				if (blockArray[j] == 0) {
					blankRow = i / N;
					break;
				} else if (blockArray[i] > blockArray[j] && blockArray[j] != 0)
					inversion++;

			}
		}
		if ((inversion + blankRow) % 2 == 1)
			return true;
		return false;
	}

	private boolean checkOddSizeBoard() {
		// TODO Auto-generated method stub
		int i, j, k;
		int inversion = 0;

		int[] blockArray = generateBlockArray();

		for (i = 0; i < N * N; i++) {
			for (j = i + 1; j < N * N; j++) {
				if (blockArray[i] > blockArray[j] && blockArray[j] != 0)
					inversion++;
			}
		}

		if (inversion % 2 == 0)
			return true;
		return false;
	}

	private int[] generateBlockArray() {
		int i, j, k;
		int[] blockArray = new int[N * N];
		k = 0;
		for (i = 0; i < N; i++) {
			for (j = 0; j < N; j++) {
				blockArray[k] = blocks[i][j];
				k++;
			}
		}
		return blockArray;
	}

	// does this board equal y?
	public boolean equals(Object y) {
		int i, j, k;
		Board brd = (Board) y;

		for (i = 0; i < N; i++) {
			for (j = 0; j < N; j++) {
				if (blocks[i][j] != brd.blocks[i][j])
					return false;
			}
		}
		return true;
	}

	// all neighboring boards
	public Iterable<Board> neighbors() {
		int i, j = 0, k, temp;
		boolean found = false;
		ArrayList<Board> neighbourList = new ArrayList<Board>();
		Board neighbour;
		int[][] neighBourBlocks;
		for (i = 0; i < blocks.length; i++) {
			for (j = 0; j < blocks[i].length; j++) {
				if (blocks[i][j] == 0) {
					found = true;
					break;
				}
			}
			if (found)
				break;
		}

		if (i > 0) {
			neighbour = new Board(blocks);
			neighbour.swapPieces(i, j, i - 1, j);
			neighbourList.add(neighbour);
		}
		if (i < N - 1) {
			neighbour = new Board(blocks);
			neighbour.swapPieces(i, j, i + 1, j);
			neighbourList.add(neighbour);
		}

		if (j > 0) {
			neighbour = new Board(blocks);
			neighbour.swapPieces(i, j, i, j - 1);
			neighbourList.add(neighbour);
		}

		if (j < N - 1) {
			neighbour = new Board(blocks);
			neighbour.swapPieces(i, j, i, j + 1);
			neighbourList.add(neighbour);
		}

		return neighbourList;
	}

	// swap two pieces to get the neighbour
	public void swapPieces(int i, int j, int l, int m) {
		int temp = this.blocks[l][m];
		this.blocks[l][m] = 0;
		this.blocks[i][j] = temp;
	}

	// string representation of the
	// board (in the output format specified below)
	public String toString() {
		int i, j, k;
		String s = "";

		for (i = 0; i < N; i++) {
			for (j = 0; j < N; j++) {
				s += blocks[i][j] + " ";
			}
			s += "\n";
		}
		s += "\n";
		return s;
	}

	// unit test
	public static void main(String[] args) throws Exception {

		// DataInputStream in= new DataInputStream(new
		// FileInputStream(args[0]));

		In in = new In(args[0]);
		int N = in.readInt();
		int[][] blocks = new int[N][N];
		for (int i = 0; i < N; i++)
			for (int j = 0; j < N; j++)
				blocks[i][j] = in.readInt();
		Board initial = new Board(blocks);
		// check if puzzle is solvable; if so, solve it and output solution
		System.out.println(initial.hamming());
		System.out.println(initial.manhattan());
		System.out.println(initial.linearConflict());
		if (initial.isSolvable()) {
			// Solver solver = new Solver(initial);
			// System.out.println("Minimum number of moves = " +
			// solver.moves());
			// for (Board board : solver.solution())
			System.out.println(initial);

			System.out.println(initial.hamming());
			System.out.println(initial.manhattan());
			System.out.println(initial.linearConflict());
			ArrayList<Board> neiArrayList = (ArrayList<Board>) initial
					.neighbors();
			for (Board b : neiArrayList) {
				System.out.println(b);
			}
		}
		// if not, report unsolvable
		else {
			System.out.println("Unsolvable puzzle");
			System.out.println(initial.hamming());
			System.out.println(initial.manhattan());
			System.out.println(initial.linearConflict());

		}
	}

}
