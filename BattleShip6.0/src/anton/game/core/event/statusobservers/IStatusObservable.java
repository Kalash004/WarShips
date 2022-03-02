package anton.game.core.event.statusobservers;

public interface IStatusObservable {

	void trigger(Event e);

//	void update(String message);
//
//	void finish();

	static class MyEvent extends Event {
		
	}
	
	static class UpdateEvent extends Event {
		public final String message;

		public UpdateEvent(String message) {
			this.message = message;
		}
	}

	static class FinishEvent extends Event {
	}

	static class Event {
	}
}
