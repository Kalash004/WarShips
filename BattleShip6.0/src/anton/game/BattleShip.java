package anton.game;

import java.awt.AWTException;
import java.awt.Image;
import java.awt.SystemTray;
import java.awt.Taskbar;
import java.awt.Toolkit;
import java.awt.TrayIcon;
import java.util.Arrays;
import java.util.List;

import javax.swing.JFrame;
import javax.swing.UnsupportedLookAndFeelException;

import anton.game.config.ShipConfig;
import anton.game.core.ComputerPlayer;
import anton.game.core.Core;
import anton.game.core.Core.PlayerType;
import anton.game.core.HumanPlayer;
import anton.game.core.event.gameplay.GamePlayObserverImpl;
import anton.game.core.event.shootobservers.ShootObserverImpl;
import anton.game.core.event.statusobservers.StatusObserverImpl;
import anton.game.core.shooting.CustomShootingStrategy;
import anton.game.core.shooting.IShootingStrategy;
import anton.game.core.shooting.SimpleRandomShootingStrategy;
import anton.game.model.Field;
import anton.game.ui.AbstractFrame;
import anton.game.ui.Board;
import anton.game.ui.ComputerFrame;
import anton.game.ui.HumanFrame;

public class BattleShip {
	private static int SIZE = 10;
	private static List<ShipConfig> CONFIG = Arrays.asList(//
			new ShipConfig(4, 1), //
			new ShipConfig(3, 2), //
			new ShipConfig(2, 3), //
			new ShipConfig(1, 4)//
	);

	public static void main(String[] args)
			throws ClassNotFoundException, InstantiationException, IllegalAccessException, UnsupportedLookAndFeelException, AWTException {
		Image icon = Toolkit.getDefaultToolkit().createImage(BattleShip.class.getResource("/icon6.png"));
		Taskbar taskbar = Taskbar.getTaskbar();
		/** 
		 * doesnt work in this version of java sad ;-;
		 */
//		taskbar.setIconImage(icon);
//		taskbar.setIconBadge("BATTLE");
//		JFrame.setDefaultLookAndFeelDecorated(true);
//		SwingUtilities.invokeLater(() -> {
		SystemTray.getSystemTray().add(new TrayIcon(icon));

		ShootObserverImpl shootObserver = new ShootObserverImpl();
		StatusObserverImpl statusObserver = new StatusObserverImpl();

		AbstractFrame humanFrame = new HumanFrame(SIZE);
		AbstractFrame computerFrame = new ComputerFrame(SIZE, shootObserver);

		Board board = new Board(computerFrame, humanFrame);
		statusObserver.addListener(board);

		GamePlayObserverImpl humanObserver = new GamePlayObserverImpl();
		humanObserver.addListener(humanFrame);
		GamePlayObserverImpl computerObserver = new GamePlayObserverImpl();
		computerObserver.addListener(computerFrame);

		Field humanField = new Field(SIZE, humanObserver, CONFIG);
		IShootingStrategy shootingStrategy;// = new SimpleRandomShootingStrategy(humanField);
		shootingStrategy = new CustomShootingStrategy(humanField);
		Field computerField = new Field(SIZE, computerObserver, CONFIG);
		Core core = new Core(new ComputerPlayer(humanField, shootingStrategy), //
				new HumanPlayer(computerField), //
				PlayerType.getRandom(), //
				statusObserver);
		shootObserver.addListener(core);

		if (core.getCurrentPlayer() == Core.PlayerType.COMPUTER) {
			core.play();
		}
//		});
	}
}
