package anton.game.model;

public class Ship {
	public int size;
	public int hits;
	public Coords coords;

	public Ship(int size, Coords coords) {
		this.size = size;
		this.coords = coords;
		this.hits = 0;
	}

	public static class Coords {
		public int x;
		public int y;
		public Position position;

		public Coords(int x, int y, Position position) {
			this.x = x;
			this.y = y;
			this.position = position;
		}

		public static enum Position {
			HORIZONTAL, VERTICAL;
		}
	}
}
