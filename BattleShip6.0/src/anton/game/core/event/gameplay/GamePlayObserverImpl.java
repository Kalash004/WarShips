package anton.game.core.event.gameplay;

import java.util.ArrayList;
import java.util.List;

public class GamePlayObserverImpl implements IGamePlayObserver {

	private final List<IGamePlayObservable> observables;

	public GamePlayObserverImpl() {
		observables = new ArrayList<IGamePlayObservable>();
	}

	public void addListener(IGamePlayObservable observable) {
		observables.add(observable);
	}

	@Override
	public void notify(Event e) {
		for (IGamePlayObservable observable : observables) {
			try {
				observable.trigger(e);
			} catch (Exception ex) {
			}
		}
	}

}
