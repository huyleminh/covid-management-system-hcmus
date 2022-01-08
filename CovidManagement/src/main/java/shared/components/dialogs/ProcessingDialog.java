package shared.components.dialogs;

import covid_management.views.shared.panels.ImagePanel;
import shared.utilities.Constants;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;

public class ProcessingDialog extends JDialog {
	// Components
	private JFrame mainFrame;
	private JLabel message;
	private boolean exitOnCloseButton = true;

	public ProcessingDialog(JFrame frame) {
		super(frame, "Processing", true);
		this.mainFrame = frame;

		JPanel panel = new JPanel();
		panel.setLayout(null);
		panel.setPreferredSize(new Dimension(430, 70));

		initComponents(panel);

		addComponentListener(new ComponentAdapter() {
			@Override
			public void componentHidden(ComponentEvent e) {
				if (!exitOnCloseButton)
					setVisible(true);
			}
		});

		setAlwaysOnTop(true);
		setResizable(false);
		setContentPane(panel);
		pack();
		setLocationRelativeTo(null);
	}

	private void initComponents(JPanel panel) {
		ImagePanel processMessageIcon = new ImagePanel(Constants.PROCESSING_ICON_FILE_PATH, 32, 32);
		processMessageIcon.setBounds(25, 19, 32, 32);
		panel.add(processMessageIcon);

		message = new JLabel("The system is processing, please wait some seconds.");
		message.setBounds(70, 20, 335, 30);
		panel.add(message);
	}

	public void setExitOnCloseButton(boolean exitOnCloseButton) {
		this.exitOnCloseButton = exitOnCloseButton;
	}
}
