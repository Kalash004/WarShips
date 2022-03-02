package anton.game.core;

import anton.game.model.Field;

public abstract class AbstractPlayer implements IPlayer {
	public Field opponentField;

	public AbstractPlayer(Field opponentField) {
		this.opponentField = opponentField;
	}

	public void reset() {
		opponentField.reset();
	}
}
