package anton.game.core.event.shootobservers;

import java.util.ArrayList;
import java.util.List;

public class ShootObserverImpl implements IShootObserver {

	private List<IShootObservable> observables;

	public ShootObserverImpl() {
		observables = new ArrayList<IShootObservable>();
	}

	public void addListener(IShootObservable observable) {
		observables.add(observable);
	}

	@Override
	public void notify(int x, int y) {
		for (IShootObservable observable : observables) {
			try {
				observable.shoot(x, y);
			} catch (Exception e) {
			}
		}
	}
}
