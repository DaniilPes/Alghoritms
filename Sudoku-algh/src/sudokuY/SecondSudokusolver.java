package sudokuY;
public class SecondSudokusolver {

    public static void main(String[] args) {
    	int[][] input = {
        		{13, 16,  5,  0,  0,  0,  0,  0,  0,  0,  0,  4,  0,  8,  6, 11},
                { 8,  0,  0,  0,  9, 13,  0, 15, 10,  0,  3,  1,  0,  0,  0,  2},
                { 1,  0, 10,  3,  6,  7,  8, 11,  5, 16,  2, 13, 12, 14,  0,  9},
                { 0,  0, 14,  0, 16,  4,  0,  1,  9,  0, 11,  6,  0,  3,  0,  0},
                { 0,  2,  0,  8,  0,  0,  0,  6, 16,  0,  0,  0, 13,  0,  1,  7},
                { 0,  1, 16, 12,  0,  0,  7,  0,  0, 15,  0,  0,  0,  4,  9,  0},
                { 0,  0,  0,  0,  0, 16,  0,  9, 12,  6,  1,  0,  0,  0,  0,  0},
                { 0,  6,  0,  4,  1,  0, 13,  0,  0,  5,  0,  2, 16, 11,  3,  0},
                { 0,  9,  0,  0,  5,  0, 11,  0,  0, 10,  0,  0,  4,  0,  2,  0},
                { 0,  0,  0,  0,  0,  6, 16, 13,  3, 11,  5,  0,  0,  0,  0,  0},
                { 0,  5,  6, 15,  0,  0,  9,  0,  0,  4,  0,  0, 11,  1, 14,  0},
                {12,  3, 11, 16,  4,  1, 10,  2,  8,  0,  0,  7,  9, 13,  5, 15},
                { 0,  0,  1,  0,  7, 11,  0,  5,  4,  0,  0, 14,  0,  9,  0,  0},
                { 6,  0, 12, 11,  0, 15,  0,  0,  0,  0,  9,  0,  3,  2,  0,  4},
                { 3,  0,  0,  0, 13,  0,  0, 10, 11,  0, 16, 12,  0,  0,  0,  1},
                { 16, 0, 13,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  11, 14}
        };
    	
    	//129 -> 13.06 - 15.2 - 15.12
    	//128 -> 19.13 - 15.48 - 20.72
    	//127 -> 13.48 - 18.55 - 19.73
    	//126 -> 72.69 - 149.01 - 119.27
    	//125 -> 257.4 - 226.32 - 62.32
    	
    	printBoard(input);
        double start = System.nanoTime();
        solveSudoku(input);
        printBoard(input);
        double end = System.nanoTime();
        System.out.println((end - start)/1_000_000_000);
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

    private static void solveSudoku(int[][] board) {
//        int size = 9;
//        int boxSize = 3;
        int size = 16;
        int boxSize = 4;

        solve(board, size, boxSize);
    }

    private static boolean solve(int[][] board, int size, int boxSize) {
        int[] currPos = findEmpty(board, size);

        if (currPos == null) {
            return true;
        }

        for (int i = 1; i <= size; i++) {
            char currNum = (char) (i + 0);
//            '0'
            if (validate(currNum, currPos, board, size, boxSize)) {
                int x = currPos[0];
                int y = currPos[1];
                board[x][y] = currNum;

                if (solve(board, size, boxSize)) {
                    return true;
                }

                board[x][y] = 0;
            }
        }

        return false;
    }

    private static int[] findEmpty(int[][] board, int size) {
        for (int r = 0; r < size; r++) {
            for (int c = 0; c < size; c++) {
                if (board[r][c] == 0) {
                    return new int[]{r, c};
                }
            }
        }
        return null;
    }

    private static boolean validate(char num, int[] pos, int[][] board, int size, int boxSize) {
        int r = pos[0];
        int c = pos[1];

        // Check rows
        for (int i = 0; i < size; i++) {
            if (board[i][c] == num && i != r) {
                return false;
            }
        }

        // Check columns
        for (int i = 0; i < size; i++) {
            if (board[r][i] == num && i != c) {
                return false;
            }
        }

        // Check box
        int boxRow = (r / boxSize) * boxSize;
        int boxCol = (c / boxSize) * boxSize;

        for (int i = boxRow; i < boxRow + boxSize; i++) {
            for (int j = boxCol; j < boxCol + boxSize; j++) {
                if (board[i][j] == num && (i != r || j != c)) {
                    return false;
                }
            }
        }

        return true;
    }
}
