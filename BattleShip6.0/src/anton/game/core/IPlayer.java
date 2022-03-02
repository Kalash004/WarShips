package anton.game.core;

import anton.game.model.Field.ShootStatus;

public interface IPlayer {
	ShootStatus play();

	ShootStatus play(int x, int y);

	void reset();
}
