package com.utilities;

import javax.swing.*;
import java.awt.*;
import java.util.Locale;

public class Constants {
	private Constants() {
	}

	// Label and TextField
	public static final int TEXT_HEIGHT = 30;

	// Button
	public static final int BUTTON_SMALL_WIDTH = 80;
	public static final int BUTTON_WIDTH = 110;
	public static final int BUTTON_LARGE_WIDTH = 140;
	public static final int BUTTON_HEIGHT = 30;
	public static final int BUTTON_LARGE_HEIGHT = 40;

	// App
	public static final int APP_WIDTH = 1000;
	public static final int APP_HEIGHT = 600;
	public static final int LEFT_PANEL_WIDTH = 200;
	public static final int TOP_PADDING = 20;

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
	public static final String HIDE_PASSWORD_ICON_FILE_PATH = "src/main/resources/images/hidden-24x24.png";
	public static final String SHOW_PASSWORD_ICON_FILE_PATH = "src/main/resources/images/view-24x24.png";
	public static final String ERROR_MESSAGE_ICON_FILE_PATH = "src/main/resources/images/error-message-icon-64x64.png";
	public static final String PROCESSING_ICON_FILE_PATH = "src/main/resources/images/sand-clock-64x64.png";

	// Icon
	public static final Icon RIGHT_CHEVRON_ICON = new ImageIcon(RIGHT_CHEVRON_ICON_FILE_PATH);

	// Format
	public static final String TIMESTAMP_WITHOUT_NANOSECOND = "yyyy-MM-dd HH:mm:ss";
	public static final Locale VN_LOCALE = new Locale("vi", "VN");

	public static final int MAX_DEBT = 2000000;

	public static final int CREATE_NEW_PAYMENT_ACCOUNT = 0;
	public static final int PAY_DEBT = 1;

	// Responses json
	public static final String SUCCESS_RESPONSE = "{ status: 201 }";
	public static final String FORBIDDEN_RESPONSE = "{ status: 403 }";
	public static final String DB_CONNECTION_ERROR_RESPONSE = "{ status: 500 }";
	public static final String SERVER_CLOSING_RESPONSE = "{ status: 500, message: \"Server is closing\" }";
	public static final String NOT_ENOUGH_BALANCE_RESPONSE = "{ status: 400, message: \"Not enough balance\" }";
}
