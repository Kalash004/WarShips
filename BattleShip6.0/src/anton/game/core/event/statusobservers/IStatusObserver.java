package anton.game.core.event.statusobservers;

import anton.game.core.event.statusobservers.IStatusObservable.Event;

public interface IStatusObserver {

	void notify(Event e);
}
