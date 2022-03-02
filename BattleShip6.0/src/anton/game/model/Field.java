package anton.game.model;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Random;

import anton.game.config.ShipConfig;
import anton.game.core.event.gameplay.IGamePlayObserver;
import anton.game.core.event.gameplay.IGamePlayObserver.HitEvent;
import anton.game.core.event.gameplay.IGamePlayObserver.MissEvent;
import anton.game.core.event.gameplay.IGamePlayObserver.PlaceShipEvent;
import anton.game.core.event.gameplay.IGamePlayObserver.ResetEvent;
import anton.game.core.event.gameplay.IGamePlayObserver.ShowShipEvent;
import anton.game.model.Ship.Coords;
import anton.game.model.Ship.Coords.Position;

public class Field {

	private List<Ship> ships;
	public final int fieldSize;
	private SquareStatus[][] matrix; // 0 empty, 1 ship
	private Random random;
	private List<ShipConfig> shipConfigs;
	private IGamePlayObserver observer;

	public Field(int fieldSize, IGamePlayObserver observer, List<ShipConfig> shipConfigs) {// AbstractFrame frame,
		this.observer = observer;
		this.fieldSize = fieldSize;
		this.matrix = new SquareStatus[fieldSize][fieldSize];
		this.random = new Random();
		this.shipConfigs = shipConfigs;
		this.ships = placeShips(shipConfigs);
	}
	
	public ShootStatus shoot(int x, int y) {
		if (x < 0 && x > matrix.length) {
			throw new IllegalArgumentException(String.format("Value for coordinate X (%s) is invalid!", x));
		}
		if (y < 0 && y > matrix[x].length) {
			throw new IllegalArgumentException(String.format("Value for coordinate Y (%s) is invalid!", y));
		}
		ShootStatus status;
		SquareStatus value = matrix[x][y];
		if (value == SquareStatus.EMPTY) {
			matrix[x][y] = SquareStatus.MISS;
			status = ShootStatus.MISS;
			observer.notify(new MissEvent(x, y));
		} else if (value == SquareStatus.SHIP) {
			status = ShootStatus.HIT;
			// hit
			for (Iterator<Ship> it = ships.iterator(); it.hasNext();) {
				Ship ship = it.next();
				Coords coords = ship.coords;
				boolean hit = false;
				if (coords.position == Position.HORIZONTAL // matches
						&& coords.x == x // matches
						&& coords.y <= y && (coords.y + ship.size - 1) >= y) { // in range
					hit = true;
				} else if (coords.position == Position.VERTICAL // matches
						&& coords.y == y // matches
						&& coords.x <= x && (coords.x + ship.size - 1) >= x) { // in range
					hit = true;
				}
				if (hit) {
					ship.hits++;
					if (ship.hits >= ship.size) {
						it.remove();
						status = ShootStatus.SINK;
					}
					matrix[x][y] = SquareStatus.HIT;
					observer.notify(new HitEvent(x, y));
				}
			}
			if (ships.isEmpty()) {
				status = ShootStatus.WON;
			}
		} else {
			// already shot
			status = ShootStatus.MISS;
		}
		if (ships.isEmpty()) {
			status = ShootStatus.WON;
		}
		return status;
	}

	private void resetMatrix() {
		for (SquareStatus[] item : matrix) {
			Arrays.fill(item, SquareStatus.EMPTY);
		}
	}

	private List<Ship> placeShips(List<ShipConfig> shipConfigs) {
		resetMatrix();

		List<Ship> ships = new ArrayList<>();
		for (ShipConfig shipConfig : shipConfigs) {
			int count = shipConfig.count;
			int shipSize = shipConfig.shipSize;
			for (int i = 0; i < count; i++) {
				Coords coords = getShipPosition(shipSize, matrix);
				Ship ship = new Ship(shipSize, coords);
				ships.add(ship);
				int sX = coords.x;
				int sY = coords.y;
				for (int j = 0; j < shipSize; j++) {
					matrix[sX][sY] = SquareStatus.SHIP;
					if (coords.position == Position.VERTICAL) {
						sX++;
					} else {
						sY++;
					}
				}
				observer.notify(new PlaceShipEvent(ship));
			}
		}
		return ships;
	}

	private Coords getShipPosition(int shipSize, SquareStatus[][] matrix) {
		Position position;
		int x;
		int y;
		do {
			position = random.nextInt(2) > 0 ? Position.VERTICAL : Position.HORIZONTAL;
			if (position == Position.VERTICAL) {
				x = random.nextInt(fieldSize - shipSize); // 0 .. 5
				y = random.nextInt(fieldSize); // 0 .. 9
			} else {
				x = random.nextInt(fieldSize);
				y = random.nextInt(fieldSize - shipSize);
			}
		} while (!checkShipPosition(x, y, shipSize, position, matrix));

		return new Coords(x, y, position);
	}

	private boolean checkShipPosition(int x, int y, int shipSize, Position position, SquareStatus[][] matrix) {
		int fromX = x == 0 ? 0 : x - 1;
		int toX = -1;
		if (position == Position.VERTICAL && x + shipSize - 1 == fieldSize - 1) {
			toX = x + shipSize;
		} else if (position == Position.VERTICAL && x + shipSize - 1 < fieldSize - 1) {
			toX = x + shipSize + 1;
		} else if (position == Position.HORIZONTAL && x == fieldSize - 1) {
			toX = x;
		} else if (position == Position.HORIZONTAL && x < fieldSize - 1) {
			toX = x + 1;
		}
		int fromY = y == 0 ? 0 : y - 1;
		int toY = -1;
		if (position == Position.HORIZONTAL && y + shipSize - 1 == fieldSize - 1) {
			toY = y + shipSize;
		} else if (position == Position.HORIZONTAL && y + shipSize - 1 < fieldSize - 1) {
			toY = y + shipSize + 1;
		} else if (position == Position.VERTICAL && y == fieldSize - 1) {
			toY = y;
		} else if (position == Position.VERTICAL && y < fieldSize - 1) {
			toY = y + 1;
		}

		if (toX < 0 || toY < 0) {
			return false;
		}
		for (int i = 0; i < matrix.length; i++) {
			for (int j = 0; j < matrix[i].length; j++) {
				if (i >= fromX // current position is at the beginning of the ship block X
						&& i <= toX // current position is at the end of she ship block X
						&& j >= fromY // current position is at the beginning of the ship block Y
						&& j <= toY // current position is at the end of she ship block Y
						&& matrix[i][j] == SquareStatus.SHIP // and there is ship already placed
				) {
					return false;
				}
			}
		}
		return true;
	}

	public void printMap() {
		for (int i = 0; i < matrix.length; i++) {
			for (int j = 0; j < matrix.length; j++) {
				if (matrix[i][j] == SquareStatus.SHIP) {
					observer.notify(new ShowShipEvent(i, j));
				}
			}
		}
	}

	public void reset() {
		observer.notify(new ResetEvent());
		ships = placeShips(shipConfigs);
	}

	public static enum ShootStatus {
		MISS, HIT, SINK, WON;
	}

	private static enum SquareStatus {
		EMPTY, SHIP, MISS, HIT;
	}
}
