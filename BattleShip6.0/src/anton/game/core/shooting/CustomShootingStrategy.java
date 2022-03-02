package anton.game.core.shooting;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import anton.game.model.Field;
import anton.game.model.Field.ShootStatus;

public class CustomShootingStrategy extends AbstractShootingStrategy {

	private ShootStatus[][] shootMatrix;
	private Point lastHitPoint;
	private Point latestHitPoint;

	private int maxShipLength = 4;

	public CustomShootingStrategy(Field field) {
		super(field);
		shootMatrix = new ShootStatus[field.fieldSize][field.fieldSize];
		for (ShootStatus[] item : shootMatrix) {
			Arrays.fill(item, null);
		}
	}

	@Override
	public ShootStatus shoot() {
		ShootStatus lastStatus = null;
		Point hitPoint = lastHitPoint;

		do {
			if (hitPoint != null) { // lastStatus == ShootStatus.HIT &&
				Point tempPoint = null;
				for (Point item : findNeighbors(shootMatrix.length, hitPoint, NeighborType.SIDE)) {
					if (shootMatrix[item.x][item.y] == null) {
						tempPoint = item;
						break;
					}
				}
				if (tempPoint == null && latestHitPoint != null) {
					if (latestHitPoint.y == hitPoint.y) { // horizontal
						for (int i = 1; i <= maxShipLength && hitPoint.x + i < shootMatrix.length; i++) { // go right
							ShootStatus status = shootMatrix[hitPoint.x + i][hitPoint.y];
							if (status == null) {
								tempPoint = new Point(hitPoint.x + i, hitPoint.y);
								break;
							} else if (status == ShootStatus.MISS) {
								break;
							}
						}
						if (tempPoint == null) { // if there is no ship right
							for (int i = 1; i <= maxShipLength && hitPoint.x - i >= 0; i++) { // go right
								ShootStatus status = shootMatrix[hitPoint.x - i][hitPoint.y];
								if (status == null) {
									tempPoint = new Point(hitPoint.x - i, hitPoint.y);
									break;
								} else if (status == ShootStatus.MISS) {
									break;
								}
							}
						}
					} else if (latestHitPoint.x == hitPoint.x) { // vertical
						for (int i = 1; i <= maxShipLength && hitPoint.y + i < shootMatrix.length; i++) { // go up
							ShootStatus status = shootMatrix[hitPoint.x][hitPoint.y + i];
							if (status == null) {
								tempPoint = new Point(hitPoint.x, hitPoint.y + i);
								break;
							} else if (status == ShootStatus.MISS) {
								break;
							}
						}
						if (tempPoint == null) { // if there is no ship right
							for (int i = 1; i <= maxShipLength && hitPoint.y - i >= 0; i++) { // go down
								ShootStatus status = shootMatrix[hitPoint.x][hitPoint.y - i];
								if (status == null) {
									tempPoint = new Point(hitPoint.x, hitPoint.y - i);
									break;
								} else if (status == ShootStatus.MISS) {
									break;
								}
							}
						}
					}
				}
				hitPoint = tempPoint;
			}
			if (hitPoint == null) {
				do {
					hitPoint = new Point();
					hitPoint.x = random.nextInt(field.fieldSize);
					hitPoint.y = random.nextInt(field.fieldSize);
				} while (shootMatrix[hitPoint.x][hitPoint.y] != null);
			}
			lastStatus = field.shoot(hitPoint.x, hitPoint.y);
			shootMatrix[hitPoint.x][hitPoint.y] = lastStatus;
			if (lastStatus == ShootStatus.HIT) {
				latestHitPoint = lastHitPoint;
				lastHitPoint = hitPoint;

				for (Point item : findNeighbors(shootMatrix.length, hitPoint, NeighborType.CORNER)) {
					if (shootMatrix[item.x][item.y] == null) {
						shootMatrix[item.x][item.y] = ShootStatus.MISS;
					}
				}
			} else if (lastStatus == ShootStatus.SINK) {
				for (Point item : findNeighbors(shootMatrix.length, hitPoint, NeighborType.ALL)) {
					if (shootMatrix[item.x][item.y] == null) {
						shootMatrix[item.x][item.y] = ShootStatus.MISS;
					}
				}
				hitPoint = null;
				lastHitPoint = null;
			}
		} while (lastStatus == ShootStatus.HIT || lastStatus == ShootStatus.SINK);
		return lastStatus;
	}

	private List<Point> findNeighbors(int size, Point p, NeighborType type) {
		List<Point> result = new ArrayList<Point>();
		for (int i = Math.max(0, p.x - 1); i <= Math.min(p.x + 1, size - 1); i++) {
			for (int j = Math.max(0, p.y - 1); j <= Math.min(p.y + 1, size - 1); j++) {
				int cursorSum = i + j;
				int coordSum = p.x + p.y;
				boolean neighborTypeCondition;
				switch (type) {
				case CORNER:
					neighborTypeCondition = (coordSum == cursorSum - 2 || coordSum == cursorSum + 2 || coordSum == cursorSum);
					break;
				case SIDE:
					neighborTypeCondition = !(coordSum == cursorSum - 2 || coordSum == cursorSum + 2 || coordSum == cursorSum);
					break;
				default:
					neighborTypeCondition = true;
					break;
				}
				if ((i != p.x || j != p.y) && neighborTypeCondition) {
					result.add(new Point(i, j));
				}
			}
		}
		return result;
	}

	enum NeighborType {
		SIDE, CORNER, ALL;
	}

	class Point {
		public int x;
		public int y;

		public Point() {
		}

		public Point(int x, int y) {
			this.x = x;
			this.y = y;
		}
	}

	@Override
	public void reset() {
		lastHitPoint = null;
		latestHitPoint = null;
		for (ShootStatus[] item : shootMatrix) {
			Arrays.fill(item, null);
		}
	}
}
