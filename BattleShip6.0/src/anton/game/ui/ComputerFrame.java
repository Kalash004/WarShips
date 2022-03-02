package anton.game.ui;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.HeadlessException;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.JComponent;

import anton.game.core.event.shootobservers.IShootObserver;
import anton.game.model.Ship;

public class ComputerFrame extends AbstractFrame {
	private static final long serialVersionUID = 1L;
	private IShootObserver observer;

	public ComputerFrame(int size, IShootObserver observer) throws HeadlessException {
		super(size);
		this.observer = observer;
		addActions(observer);
	}

	public void removeActions() {
		for (int i = 0; i < matrix.length; i++) {
			for (int j = 0; j < matrix[i].length; j++) {
				JComponent element = matrix[i][j];
				element.setCursor(Cursor.getDefaultCursor());
				MouseListener[] listeners = element.getMouseListeners();
				if (listeners != null) {
					for (MouseListener listener : listeners) {
						element.removeMouseListener(listener);
					}
				}
			}
		}
	}

	private void addActions(IShootObserver observer) {
		for (int i = 0; i < matrix.length; i++) {
			for (int j = 0; j < matrix[i].length; j++) {
				int x = i;
				int y = j;
				JComponent element = matrix[i][j];
				element.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

				element.addMouseListener(new MouseListener() {
					Color labelColor;

					@Override
					public void mouseReleased(MouseEvent e) {
						synchronized (this) {
							observer.notify(x, y);
							labelColor = element.getBackground();
							if (labelColor.getRGB() != COLOR_SEA.getRGB()) {
								element.removeMouseListener(this);
								element.setCursor(Cursor.getDefaultCursor());
							}
						}
					}

					@Override
					public void mouseExited(MouseEvent e) {
						synchronized (this) {
							element.setBackground(labelColor);
							element.paintImmediately(element.getVisibleRect());
						}
					}

					@Override
					public void mouseEntered(MouseEvent e) {
						synchronized (this) {
							labelColor = element.getBackground();
							element.setBackground(labelColor.brighter());
							element.paintImmediately(element.getVisibleRect());
						}
					}

					@Override
					public void mousePressed(MouseEvent e) {
					}

					@Override
					public void mouseClicked(MouseEvent e) {
					}
				});
			}
		}
	}

	public void placeShip(Ship ship) {
		// do nothing
	}

	@Override
	public void reset() {
		super.reset();
		addActions(observer);
	}
}
