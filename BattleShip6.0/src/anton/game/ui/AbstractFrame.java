package anton.game.ui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.HeadlessException;

import javax.swing.BorderFactory;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;

import anton.game.core.event.gameplay.IGamePlayObservable;
import anton.game.core.event.gameplay.IGamePlayObserver.Event;
import anton.game.core.event.gameplay.IGamePlayObserver.HitEvent;
import anton.game.core.event.gameplay.IGamePlayObserver.MissEvent;
import anton.game.core.event.gameplay.IGamePlayObserver.PlaceShipEvent;
import anton.game.core.event.gameplay.IGamePlayObserver.ResetEvent;
import anton.game.core.event.gameplay.IGamePlayObserver.ShowShipEvent;
import anton.game.model.Ship;

public abstract class AbstractFrame extends JPanel implements IGamePlayObservable {

	private static final long serialVersionUID = 1L;

	private static final String[] LETTERS = new String[] { " ", "A", "B", "C", "D", "E", "F", "G", "H", "I", "J", "K", "L", "M", "N" };

	protected static final Color COLOR_SHIP = Color.BLACK;
	protected static final Color COLOR_HIT = Color.RED;
	protected static final Color COLOR_MISS = Color.LIGHT_GRAY;

	protected static final Color COLOR_SEA = new Color(77, 205, 247);
	protected static final Color COLOR_LAND = Color.GRAY;

	static final Color COLOR_BORDER = Color.DARK_GRAY.brighter();

	protected final JComponent[][] matrix;

	public final int size;

	public AbstractFrame(int size) throws HeadlessException {
		this.size = size;
		matrix = new JComponent[size][size];
		setLayout(new GridLayout(size + 1, size + 1, 2, 2));
		drawField();
	}

	protected void drawField() {
		for (int i = 0; i <= size; i++) {
			for (int j = 0; j <= size; j++) {
				String text;
				if (i == 0 && i + j > 0) {
					text = String.valueOf(j);
				} else if (j == 0 && i + j > 0) {
					text = LETTERS[i];
				} else {
					text = "";
				}

				JLabel element = new JLabel();
				Color bg = i == 0 || j == 0 ? COLOR_LAND : COLOR_SEA;
				element.setBackground(bg);
				element.setPreferredSize(new Dimension(60, 60));
				element.setOpaque(true);
				element.setVerticalAlignment(JLabel.CENTER);
				element.setHorizontalAlignment(JLabel.CENTER);
				element.setBorder(BorderFactory.createLineBorder(COLOR_BORDER));
				element.setText(text);
				add(element);

				if (i > 0 && j > 0) {
					matrix[i - 1][j - 1] = element;
				}
			}
		}
	}

	public abstract void placeShip(Ship ship);

	public void reset() {
		removeAll();
		drawField();
		for (int i = 0; i < matrix.length; i++) {
			for (int j = 0; j < matrix[i].length; j++) {
				paintSquare(i, j, COLOR_SEA, false);
			}
		}
	}

	public void hitShip(int x, int y) {
		paintSquare(x, y, COLOR_HIT, true);
	}

	public void missShip(int x, int y) {
		paintSquare(x, y, COLOR_MISS, true);
	}

	// public abstract void showShip(int x, int y);
	public void showShip(int x, int y) {
		paintSquare(x, y, COLOR_SHIP, false);
	}

	protected void paintSquare(int x, int y, Color color, boolean animated) {
		JComponent element = matrix[x][y];
		element.setBackground(color);
		element.paintImmediately(element.getVisibleRect());
		if (animated) {
			try {
				Thread.sleep(50l);
			} catch (InterruptedException e) {
			}
			element.setBackground(color.darker());
			element.paintImmediately(element.getVisibleRect());
			try {
				Thread.sleep(50l);
			} catch (InterruptedException e) {
			}
			element.setBackground(color);
			element.paintImmediately(element.getVisibleRect());
		}
	}

	@Override
	public void trigger(Event e) {
		if (e instanceof ResetEvent) {
			reset();
		} else if (e instanceof MissEvent) {
			MissEvent ev = (MissEvent) e;
			missShip(ev.x, ev.y);
		} else if (e instanceof HitEvent) {
			HitEvent ev = (HitEvent) e;
			hitShip(ev.x, ev.y);
		} else if (e instanceof ShowShipEvent) {
			ShowShipEvent ev = (ShowShipEvent) e;
			showShip(ev.x, ev.y);
		} else if (e instanceof PlaceShipEvent) {
			PlaceShipEvent ev = (PlaceShipEvent) e;
			placeShip(ev.ship);
		}
	}

}
