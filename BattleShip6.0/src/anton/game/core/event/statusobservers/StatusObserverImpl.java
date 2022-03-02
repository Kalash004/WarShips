package anton.game.core.event.statusobservers;

import java.util.ArrayList;
import java.util.List;

import anton.game.core.event.statusobservers.IStatusObservable.Event;

public class StatusObserverImpl implements IStatusObserver {
	private List<IStatusObservable> observables;

	public StatusObserverImpl() {
		observables = new ArrayList<IStatusObservable>();
	}

	public void addListener(IStatusObservable observable) {
		observables.add(observable);
	}

	@Override
	public void notify(Event e) {
		for (IStatusObservable observable : observables) {
			try {
				observable.trigger(e);
			} catch (Exception ex) {
			}
		}
	}
}
