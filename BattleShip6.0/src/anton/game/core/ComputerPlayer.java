package anton.game.core;

import anton.game.core.shooting.IShootingStrategy;
import anton.game.model.Field;
import anton.game.model.Field.ShootStatus;

public class ComputerPlayer extends AbstractPlayer {
	private IShootingStrategy shootingStrategy;

	public ComputerPlayer(Field opponentField, IShootingStrategy shootingStrategy) {
		super(opponentField);
		this.shootingStrategy = shootingStrategy;

	}

	public ShootStatus play() {
		return shootingStrategy.shoot();
	}

	@Override
	public ShootStatus play(int x, int y) {
		throw new UnsupportedOperationException("Computer Player does not support external play");
	}
	
	@Override
	public void reset() {
		super.reset();
		shootingStrategy.reset();
	}
}
