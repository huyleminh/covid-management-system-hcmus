package com.views.shared.dialogs;

import com.utilities.Constants;
import com.views.shared.panels.ImagePanel;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;

public class ProcessingDialog extends JDialog {
	private JFrame mainFrame;
	private JLabel message;
	private boolean exitOnCloseButton = true;

	public ProcessingDialog(JFrame frame) {
		super(frame, "Processing", true);
		this.mainFrame = frame;

		JPanel panel = new JPanel();
		panel.setLayout(null);
		panel.setPreferredSize(new Dimension(435, 90));

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
		ImagePanel processMessageIcon = new ImagePanel(Constants.PROCESSING_ICON_FILE_PATH, 64, 64);
		processMessageIcon.setBounds(25, 18, 64, 64);
		panel.add(processMessageIcon);

		message = new JLabel("The system is processing, please wait a moment.");
		message.setBounds(100, 24, 305, 20);
		panel.add(message);
	}

	public void setExitOnCloseButton(boolean exitOnCloseButton) {
		this.exitOnCloseButton = exitOnCloseButton;
	}
}
