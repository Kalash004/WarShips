package anton.game.core.event.gameplay;

import anton.game.model.Ship;

public interface IGamePlayObserver {

	void notify(Event e);

	static class HitEvent extends CoordsEvent {

		public HitEvent(int x, int y) {
			super(x, y);
		}

	}

	static class MissEvent extends CoordsEvent {

		public MissEvent(int x, int y) {
			super(x, y);
		}

	}

	static class ShowShipEvent extends CoordsEvent {

		public ShowShipEvent(int x, int y) {
			super(x, y);
		}

	}

	static class PlaceShipEvent extends Event {
		public Ship ship;

		public PlaceShipEvent(Ship ship) {
			this.ship = ship;
		}
	}

	static class ResetEvent extends Event {

	}

	static class CoordsEvent extends Event {
		public int x;
		public int y;

		public CoordsEvent(int x, int y) {
			this.x = x;
			this.y = y;
		}
	}

	static abstract class Event {

	}

}
