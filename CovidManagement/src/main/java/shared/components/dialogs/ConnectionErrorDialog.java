package shared.components.dialogs;

import covid_management.views.shared.panels.ImagePanel;
import shared.utilities.Constants;
import shared.utilities.UtilityFunctions;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;

public class ConnectionErrorDialog extends JDialog {
	private JFrame mainFrame;
	private JLabel message;
	private JLabel reconnectMessageLabel;
	private JButton reconnectButton;
	private JButton quitButton;
	private boolean exitOnCloseButton = true;

	public ConnectionErrorDialog(JFrame frame) {
		super(frame, "Connection Failed", true);
		this.mainFrame = frame;

		JPanel panel = new JPanel();
		panel.setLayout(null);
		panel.setPreferredSize(new Dimension(420, 135));

		initComponents(panel);

		quitButton.addActionListener((event) -> {
			exitOnCloseButton = false;
			UtilityFunctions.quitApp(mainFrame);
		});

		addComponentListener(new ComponentAdapter() {
			@Override
			public void componentHidden(ComponentEvent e) {
				if (exitOnCloseButton)
					UtilityFunctions.quitApp(mainFrame);
				else
					exitOnCloseButton = true;
			}
		});

		setAlwaysOnTop(true);
		setResizable(false);
		setContentPane(panel);
		pack();
		setLocationRelativeTo(null);
	}

	private void initComponents(JPanel panel) {
		ImagePanel errorMessageIcon = new ImagePanel(Constants.ERROR_MESSAGE_ICON_FILE_PATH, 64, 64);
		errorMessageIcon.setBounds(25, 18, 64, 64);
		panel.add(errorMessageIcon);

		message = new JLabel("Can not connect to the application.");
		message.setBounds(100, 24, 295, 20);
		panel.add(message);

		reconnectMessageLabel = new JLabel("You can click Reconnect button to reconnect.");
		reconnectMessageLabel.setBounds(100, 46, 295, 20);
		panel.add(reconnectMessageLabel);

		reconnectButton = new JButton("Reconnect");
		reconnectButton.setBounds(95, 95, Constants.BUTTON_WIDTH, Constants.BUTTON_HEIGHT);
		reconnectButton.setHorizontalTextPosition(SwingConstants.CENTER);
		reconnectButton.setBackground(Constants.LIGHT_BLUE);
		reconnectButton.setForeground(Color.WHITE);
		panel.add(reconnectButton);

		quitButton = new JButton("Quit App");
		quitButton.setBounds(215, 95, Constants.BUTTON_WIDTH, Constants.BUTTON_HEIGHT);
		quitButton.setHorizontalTextPosition(SwingConstants.CENTER);
		quitButton.setBackground(Constants.RED);
		quitButton.setForeground(Color.WHITE);
		panel.add(quitButton);
	}

	public JButton getReconnectButton() {
		return reconnectButton;
	}

	public void setExitOnCloseButton(boolean exitOnCloseButton) {
		this.exitOnCloseButton = exitOnCloseButton;
	}
}
