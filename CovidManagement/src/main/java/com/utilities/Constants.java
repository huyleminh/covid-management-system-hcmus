package com.utilities;

import javax.swing.*;
import java.awt.*;

public class Constants {
	private Constants() {}

	// Label and TextField
	public static final int TEXT_HEIGHT = 30;
	public static final int TEXT_LARGE_HEIGHT = 40;

	// Button
	public static final int BUTTON_SMALL_WIDTH = 80;
	public static final int BUTTON_WIDTH = 110;
	public static final int BUTTON_LARGE_WIDTH = 140;
	//	public static final int BUTTON_HEIGHT_INSIDE_TABLE = 25;
	public static final int BUTTON_HEIGHT = 30;
	public static final int BUTTON_LARGE_HEIGHT = 40;

	// App
	public static final int APP_WIDTH = 1000;
	public static final int APP_HEIGHT = 600;
	public static final int LEFT_PANEL_WIDTH = 200;
	public static final int RIGHT_PANEL_WIDTH = 800;
	public static final int TOP_PADDING = 20;
	//	public static final int BOTTOM_PADDING = 10;
	public static final int HORIZONTAL_PADDING = 10;

	//	public static final int COMPONENT_VERTICAL_PADDING = 10;

	// Login view
	public static final int LOGIN_VIEW_WIDTH = 350;
	public static final int LOGIN_VIEW_HEIGHT = 250;

	// Table
	public static final int TABLE_CELL_HEIGHT = 35;
	public static final int TABLE_CELL_HORIZONTAL_PADDING = 5;
	public static final int TABLE_CELL_VERTICAL_PADDING = 1;


	// Color
	public static final Color RED = new Color(237, 36, 36);
	public static final Color LIGHT_BLUE = new Color(36, 160, 237);
	public static final Color DARK_BLUE = new Color(0, 104, 169);
	public static final Color DARK_YELLOW = new Color(255, 192, 9);
	public static final Color GREEN = new Color(8, 199, 8);

	// File path
	public static final String PROFILE_ICON_FILE_PATH = "src/main/resources/images/profile-128x128.png";
	public static final String RIGHT_CHEVRON_ICON_FILE_PATH = "src/main/resources/images/right-chevron-24x24.png";
	public static final String CALENDAR_ICON_FILE_PATH = "src/main/resources/images/calendar-24x24.png";

	// Icon
	public static final Icon RIGHT_CHEVRON_ICON = new ImageIcon(RIGHT_CHEVRON_ICON_FILE_PATH);
}
