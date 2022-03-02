package anton.game.core.shooting;

import anton.game.model.Field.ShootStatus;

public interface IShootingStrategy {
	ShootStatus shoot();

	void reset();
}
