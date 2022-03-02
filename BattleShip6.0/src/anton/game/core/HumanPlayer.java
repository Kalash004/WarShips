package anton.game.core;

import anton.game.model.Field;
import anton.game.model.Field.ShootStatus;

public class HumanPlayer extends AbstractPlayer {

	public HumanPlayer(Field opponentField) {
		super(opponentField);
	}

	public ShootStatus play(int x, int y) {
		return opponentField.shoot(x, y);
	}

	@Override
	public ShootStatus play() {
		throw new UnsupportedOperationException("Human Player does not supported autonomous play");
	}

}
