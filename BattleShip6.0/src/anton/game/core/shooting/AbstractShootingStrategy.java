package anton.game.core.shooting;

import java.util.Random;

import anton.game.model.Field;

public abstract class AbstractShootingStrategy implements IShootingStrategy {
	protected Random random;
	protected Field field;

	public AbstractShootingStrategy(Field field) {
		this.random = new Random();
		this.field = field;
	}

}
