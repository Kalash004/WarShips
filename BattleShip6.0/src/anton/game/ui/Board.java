package anton.game.ui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.HeadlessException;
import java.awt.Image;
import java.awt.Toolkit;

import javax.swing.BorderFactory;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSeparator;
import javax.swing.SwingConstants;

import anton.game.core.event.statusobservers.IStatusObservable;

public class Board extends JFrame implements IStatusObservable {

	private static final long serialVersionUID = 1L;
	public final JLabel status;
	private JPanel computerFrame;

	public Board(JPanel computerFrame, JPanel humanFrame) throws HeadlessException {
		setTitle("Battle Ship");
		Image icon = Toolkit.getDefaultToolkit().createImage(getClass().getResource("/icon.png"));
		setIconImage(icon);

		setLocationRelativeTo(null);
		setResizable(true);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		Container board = getContentPane();

		JPanel fieldsPanel = new JPanel();
		fieldsPanel.setLayout(new FlowLayout(FlowLayout.TRAILING, 5, 5));
		board.add(fieldsPanel, BorderLayout.NORTH);
		fieldsPanel.add(computerFrame);
		fieldsPanel.add(humanFrame);
//
//		JLabel scoreLabel = new JLabel();
//		scoreLabel.setText("Here is my score");
//		scoreLabel.setBorder(BorderFactory.createLineBorder(AbstractFrame.COLOR_BORDER));
//		scoreLabel.setSize(fieldsPanel.getSize());
//		fieldsPanel.add(scoreLabel);

		this.computerFrame = computerFrame;
		board.add(new JSeparator(), BorderLayout.CENTER);

		JLabel status = new JLabel();
		status.setForeground(Color.RED);
		status.setHorizontalAlignment(SwingConstants.CENTER);
		status.setText("Battle Ship");

		board.add(status, BorderLayout.SOUTH);
		this.status = status;

		pack();

		Dimension dimension = Toolkit.getDefaultToolkit().getScreenSize();
		int x = (int) ((dimension.getWidth() - getWidth()) / 2);
		int y = (int) ((dimension.getHeight() - getHeight()) / 2);
		setLocation(x, y);
		setVisible(true);
	}

	@Override
	public void trigger(Event e) {
		if (e instanceof FinishEvent && computerFrame instanceof ComputerFrame) {
			((ComputerFrame) computerFrame).removeActions();
		} else if (e instanceof UpdateEvent) {
			status.setText(((UpdateEvent) e).message);
			status.paintImmediately(status.getVisibleRect());
		}
	}

}
