package com.utilities;

import org.apache.commons.codec.digest.DigestUtils;

import javax.swing.*;
import java.awt.event.WindowEvent;
import java.sql.SQLException;

public class UtilityFunctions {
	private UtilityFunctions() {}

	public static int sum(int[] a) {
		int result = 0;
		for (int item : a)
			result += item;

		return result;
	}

	public static boolean isLeapYear(short year) {
		return (year % 4 == 0 && year % 100 != 0) || (year % 400 == 0);
	}

	public static String hashPassword(String password) {
		return DigestUtils.sha256Hex(password);
	}

	public static void quitApp(JFrame mainFrame) {
		try {
			SingletonDBConnection.getInstance().getConnection().close();
		} catch (SQLException e) {
			System.out.println(">>> UtilityFunctions.java - line 32");
			e.printStackTrace();
		} finally {
			mainFrame.dispatchEvent(new WindowEvent(mainFrame, WindowEvent.WINDOW_CLOSING));
		}
	}
}
