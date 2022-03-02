package anton.game.core;

import java.util.Random;

import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.SwingConstants;

import anton.game.core.event.shootobservers.IShootObservable;
import anton.game.core.event.statusobservers.IStatusObserver;
import anton.game.core.event.statusobservers.IStatusObservable.FinishEvent;
import anton.game.core.event.statusobservers.IStatusObservable.UpdateEvent;
import anton.game.model.Field.ShootStatus;

public class Core implements IShootObservable {

	public IPlayer computer;
	public IPlayer human;
	private PlayerType currentPlayer;
	public ShootStatus shootStatus;
	private IStatusObserver observer;

	public Core(IPlayer computer, IPlayer human, PlayerType player, IStatusObserver observer) {
		this.computer = computer;
		this.human = human;
		this.currentPlayer = player;
		this.observer = observer;
	}

	/**
	 * Returns true if next player can play
	 * 
	 * @return
	 */
	public boolean play() {
		if (currentPlayer != PlayerType.COMPUTER) {
			throw new IllegalStateException("It is not your turn!");
		}
//		refresh();
		try {
			Thread.sleep(500l);
		} catch (InterruptedException e) {
		}
		if (shootStatus == ShootStatus.WON) {
			return false;
		}

		shootStatus = computer.play();
		boolean nextTurn = shootStatus != ShootStatus.WON;
		if (nextTurn) {
			currentPlayer = PlayerType.HUMAN;
		}
		refresh();
//		if (!nextTurn) {
//			showWonDialog();
//		}
		return nextTurn;
	}

	@Override
	public boolean shoot(int x, int y) {
		if (currentPlayer != PlayerType.HUMAN) {
			throw new IllegalStateException("It is not your turn!");
		}
		boolean status = true;
		shootStatus = human.play(x, y);
		if (shootStatus == ShootStatus.MISS) {
			currentPlayer = PlayerType.COMPUTER;
			status = play();
		}
		if (shootStatus == ShootStatus.WON) {
			status = false;
		}
		if (!status) {
			refresh();
			showWonDialog();
		}
		return status;
	}

	public void refresh() {
		String message;
		if (shootStatus == ShootStatus.WON && currentPlayer == PlayerType.HUMAN) {
			message = "You Won!";
		} else if (shootStatus == ShootStatus.WON && currentPlayer == PlayerType.COMPUTER) {
			message = "Computer Won :(";
		} else {
			message = String.format("Current player: %s", currentPlayer);
		}
		observer.notify(new UpdateEvent(message));
	}

	public void reset() {
		shootStatus = null;
		currentPlayer = PlayerType.getRandom();
		human.reset();
		computer.reset();
	}

	public void showWonDialog() {
		String message = currentPlayer == PlayerType.HUMAN ? "You Won!" : "Computer Won :(";

		JLabel label = new JLabel();
		label.setHorizontalAlignment(SwingConstants.CENTER);
		label.setText(String.format("<html><center>%s<br/>Do you want to play one more game?</center></html>", message));
		int buttonCode = JOptionPane.showConfirmDialog(null, //
				label, //
				"Game Finished", //
				JOptionPane.YES_NO_OPTION, //
				JOptionPane.INFORMATION_MESSAGE);
		if (buttonCode == JOptionPane.YES_OPTION) {
			reset();
			if (currentPlayer == PlayerType.COMPUTER) {
				play();
			}
			refresh();
		} else {
			observer.notify(new FinishEvent());
		}
	}

	public static enum PlayerType {
		HUMAN, COMPUTER;

		private static final Random RANDOM = new Random();

		public static PlayerType getRandom() {
			return RANDOM.nextInt(2) > 0 ? PlayerType.COMPUTER : PlayerType.HUMAN;
		}
	}

	public PlayerType getCurrentPlayer() {
		return currentPlayer;
	}
}
