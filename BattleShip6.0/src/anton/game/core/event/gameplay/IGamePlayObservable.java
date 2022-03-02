package anton.game.core.event.gameplay;

import anton.game.core.event.gameplay.IGamePlayObserver.Event;

public interface IGamePlayObservable {

	void trigger(Event e);
}
