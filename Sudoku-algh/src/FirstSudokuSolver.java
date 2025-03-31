import java.util.Arrays;

public class FirstSudokuSolver {

    public static void main(String[] args) {
    	int[][] sudokuBoard = {
        		{6, 7, 3, 8, 9, 4, 5, 1, 2},
                {9, 1, 2, 7, 3, 5, 0, 0, 0},
                {0, 0, 0, 6, 1, 2, 0, 7, 0},
                {7, 0, 0, 0, 6, 1, 3, 0, 0},
                {0, 0, 0, 4, 0, 0, 8, 0, 0},
                {1, 0, 0, 0, 0, 0, 0, 0, 0},
                {0, 0, 0, 1, 2, 0, 0, 0, 0},
                {0, 8, 0, 0, 0, 0, 0, 4, 0},
                {0, 5, 0, 0, 0, 0, 6, 0, 0}
        };
    	//27
    	
//    	int[][] sudokuBoard = {
//    			{13, 16,  5,  9, 10,  0,  0, 12, 15,  0, 14,  0,  0,  0,  0, 11},
//                { 0, 11,  0,  0,  0, 13, 14, 15, 10, 12,  0,  0,  0, 16,  0,  0},
//                { 1, 15, 10,  3,  6,  7,  8, 11,  5, 16,  2, 13, 12, 14,  0,  9},
//                { 0,  0, 14,  0, 16,  4,  0,  1,  9,  0, 11,  6,  0,  3,  0,  0},
//                { 0,  2,  0,  8,  0,  0,  0,  6, 16,  0,  0,  0, 13,  0,  1,  0},
//                { 0,  1, 16, 12,  0,  0,  7,  0,  0, 15,  0,  0, 14,  4,  9,  0},
//                { 0,  0,  0,  0,  0, 16,  4,  9, 12,  6,  1,  0,  0,  0,  0,  0},
//                { 0,  6,  0,  4,  1,  0, 13,  0,  0,  5,  0,  2, 16,  0,  3,  0},
//                { 0,  9,  0, 13,  5,  0, 11,  0,  0, 10,  0, 15,  4,  0,  2,  0},
//                { 0,  0,  0,  0,  0,  6, 16, 13,  3, 11,  5,  0,  0,  0,  0,  0},
//                { 0,  5,  6, 15,  0,  0,  9,  0,  0,  4,  0,  0, 11,  1, 14,  0},
//                {12,  3, 11, 16,  4,  1, 10,  2,  8,  0,  0,  7,  9, 13,  5, 15},
//                { 0,  0,  1,  0,  7, 11,  0,  5,  4,  0,  8, 14,  0,  9,  0,  0},
//                { 6,  0, 12, 11,  0, 15,  0,  0,  0,  0,  9,  0,  3,  2,  0,  4},
//                { 3,  0,  0,  0, 13,  8,  0, 10, 11,  0, 16, 12,  0,  0,  0,  1},
//                { 16, 0, 13,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0}
//        };
    	//-128 129 -> 0.037 - 0.023 - 0.019
    	System.out.println("Original Sudoku:");
        printBoard(sudokuBoard);
    	double start = System.nanoTime();
        if (improvedSolveSudoku(sudokuBoard)) {
            printSudoku(sudokuBoard);
        } else {
            System.out.println("No solution exists.");
        }
        double end = System.nanoTime();
        System.out.println((end - start)/1_000_000_000);
    }

    private static boolean improvedSolveSudoku(int[][] board) {
        int[][][] candidates = new int[9][9][];

        // Initialize candidates for each empty cell.
        //9
        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 9; j++) {
                if (board[i][j] == 0) {
                    candidates[i][j] = getCandidates(board, i, j);
                }
            }
        }

        return improvedSolver(board, candidates);
    }

    private static boolean improvedSolver(int[][] board, int[][][] candidates) {
        int[] nextEmptyCell = findNextEmptyCell(board, candidates);

        if (nextEmptyCell == null) {
            // No empty cell found, the board is solved.
            return true;
        }

        int row = nextEmptyCell[0];
        int col = nextEmptyCell[1];

        int[] cellCandidates = candidates[row][col];

        for (int candidate : cellCandidates) {
            // Try each candidate for the current empty cell.
            board[row][col] = candidate;
            candidates[row][col] = null;

            // Update candidates for other cells affected by the current placement.
            updateCandidates(board, candidates, row, col, candidate);

            // Recursive call to solve the updated board.
            if (improvedSolver(board, candidates)) {
                return true;
            }

            // If placing the current number doesn't lead to a solution, backtrack.
            board[row][col] = 0;
            candidates[row][col] = cellCandidates.clone();
        }

        // No valid number found for this cell, trigger backtracking.
        return false;
    }

    private static void updateCandidates(int[][] board, int[][][] candidates, int row, int col, int num) {
        // Update candidates in the same row and column.
        for (int i = 0; i < 9; i++) {
            if (candidates[row][i] != null) {
                candidates[row][i] = removeCandidate(candidates[row][i], num);
            }
            if (candidates[i][col] != null) {
                candidates[i][col] = removeCandidate(candidates[i][col], num);
            }
        }

        // Update candidates in the same 3x3 subgrid.
        int subgridStartRow = row - row % 3;
        int subgridStartCol = col - col % 3;

        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                int r = subgridStartRow + i;
                int c = subgridStartCol + j;

                if (candidates[r][c] != null) {
                    candidates[r][c] = removeCandidate(candidates[r][c], num);
                }
            }
        }
    }

    private static int[] removeCandidate(int[] candidates, int num) {
        return Arrays.stream(candidates)
            .filter(candidate -> candidate != num)
            .toArray();
    }

    private static int[] findNextEmptyCell(int[][] board, int[][][] candidates) {
        int minCandidates = 9; // Start with a number larger than the maximum possible candidates.

        int[] nextEmptyCell = null;

        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 9; j++) {
                if (board[i][j] == 0 && candidates[i][j].length < minCandidates) {
                    minCandidates = candidates[i][j].length;
                    nextEmptyCell = new int[]{i, j};
                }
            }
        }

        return nextEmptyCell;
    }

    private static int[] getCandidates(int[][] board, int row, int col) {
        boolean[] usedNumbers = new boolean[10];

        // Check used numbers in the row and column.
        for (int i = 0; i < 9; i++) {
            usedNumbers[board[row][i]] = true;
            usedNumbers[board[i][col]] = true;
        }

        // Check used numbers in the 3x3 subgrid.
        int subgridStartRow = row - row % 3;
        int subgridStartCol = col - col % 3;

        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                usedNumbers[board[subgridStartRow + i][subgridStartCol + j]] = true;
            }
        }

        // Count the number of remaining candidates.
        int count = countFalse(usedNumbers);
        int[] candidates = new int[count];

        // Fill in the remaining candidates.
        for (int i = 1, j = 0; i <= 9; i++) {
            if (!usedNumbers[i]) {
                candidates[j++] = i;
            }
        }

        return candidates;
    }

    private static void printSudoku(int[][] board) {
    	
        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 9; j++) {
                System.out.print(board[i][j] + " ");
                
            }
            System.out.println();
        }
    }
    
    private static int countFalse(boolean[] array) {
        int count = 0;
        for (boolean value : array) {
            if (!value) {
                count++;
            }
        }
        return count;
    }
    
    public static void printBoard(int[][] board) {
    	int n = 0;
        for (int i = 0; i < board.length; i++) {
            for (int j = 0; j < board[0].length; j++) {
                System.out.print(board[i][j] + " ");
                if(board[i][j] != 0) {
                	n++;
                }
            }
            System.out.println();
        }
       System.out.println(n);
    }
}
