package anton.game.core.shooting;

import java.util.HashSet;
import java.util.Set;

import anton.game.model.Field;
import anton.game.model.Field.ShootStatus;

public class SimpleRandomShootingStrategy extends AbstractShootingStrategy {
	private Set<String> heatMap;

	public SimpleRandomShootingStrategy(Field field) {
		super(field);
		this.heatMap = new HashSet<>();
	}

	@Override
	public ShootStatus shoot() {
		ShootStatus shootStatus;
		do {
			int x;
			int y;
			String key;
			do {
				x = random.nextInt(field.fieldSize);
				y = random.nextInt(field.fieldSize);
				key = String.format("%s:%s", x, y);
			} while (heatMap.contains(key));
			heatMap.add(key);

			shootStatus = field.shoot(x, y);
		} while (shootStatus == ShootStatus.HIT || shootStatus == ShootStatus.SINK);
		return shootStatus;
	}

	@Override
	public void reset() {
		heatMap.clear();
	}
}
