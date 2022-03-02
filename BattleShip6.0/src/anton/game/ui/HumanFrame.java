package anton.game.ui;

import java.awt.HeadlessException;

import anton.game.model.Ship;
import anton.game.model.Ship.Coords.Position;

public class HumanFrame extends AbstractFrame {
	private static final long serialVersionUID = 1L;

	public HumanFrame(int size) throws HeadlessException {
		super(size);
	}

	@Override
	public void placeShip(Ship ship) {
		Position position = ship.coords.position;
		int x = ship.coords.x;
		int y = ship.coords.y;
		for (int j = 0; j < ship.size; j++) {
			showShip(x, y);
			if (position == Position.VERTICAL) {
				x++;
			} else {
				y++;
			}
		}
	}
}
