package anton.game;

public class Util {

	private static int[][] m;
	private static int[] off_x = new int[] { -1, 0, 1, 0 };
	private static int[] off_y = new int[] { 0, 1, 0, -1 };

	public static void printMatrix() {
		for (int i = 0; i < m.length; i++) {
			for (int j = 0; j < m[i].length; j++) {
				System.out.print(m[i][j]);
				if (j < m[i].length - 1) {
					System.out.print(", ");
				} else {
					System.out.println();
				}
			}
		}
	}

	public static void findingNeighbors(int[][] matr, int x, int y) {

		for (int i = Math.max(0, x - 1); i <= Math.min(x + 1, matr.length - 1); i++) {
			for (int j = Math.max(0, y - 1); j <= Math.min(y + 1, matr.length - 1); j++) {
				int cursorSum = i + j;
				int coordSum = x + y;
				if ((i != x || j != y) && !(coordSum == cursorSum - 2 || coordSum == cursorSum + 2 || coordSum == cursorSum)) {
					System.out.print(matr[i][j] + " (" + i + ":" + j + ") ");
				}
			}
		}
	}

	public static void main(String[] args) {
		m = new int[5][5];
		int xx = 10;
		for (int i = 0; i < m.length; i++) {
			for (int j = 0; j < m[i].length; j++) {
				m[i][j] = xx++;
			}
		}

		printMatrix();
		System.out.println();
		int x = 3;
		int y = 3;

		int boundXMin = 0;
		int boundYMin = 0;
		int boundXMax = m.length;
		int boundYMax = m.length;
		System.out.println(">>> " + m[x][y]);
		System.out.println("------");
		System.out.print("LT");
		if (x - 1 >= boundXMin) {
			System.out.print(m[x - 1][y]);
		} else {
			System.out.print("**");
		}
		System.out.print("RT");
		System.out.println();
		if (y - 1 <= boundYMin) {
			System.out.print(m[x][y - 1]);
		} else {
			System.out.print("XX");
		}
		System.out.print(m[x][y]);
		if (y + 1 < boundYMax) {
			System.out.print(m[x][y + 1]);
		} else {
			System.out.print("**");
		}
		System.out.print("  ");
		System.out.println();
		System.out.print("LB");
		if (x + 1 < boundXMax) {
			System.out.print(m[x + 1][y]);
		}
		System.out.print("RB");
		System.out.println();
		System.out.println("------");

		for (int i = 0; i < 4; i++) {
			int oX = off_x[i] + x;
			int oY = off_y[i] + y;
			if (oX >= boundXMin && oX < boundXMax && oY >= boundYMin && oY < boundYMax) {
				System.out.print(m[oX][oY]);
				System.out.print(" ");
			}
		}

		System.out.println("------");
		findingNeighbors(m, x, y);
	}
}
