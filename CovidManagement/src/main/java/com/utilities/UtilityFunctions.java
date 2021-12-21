package com.utilities;

import org.apache.commons.codec.digest.DigestUtils;

import javax.swing.*;
import java.awt.event.WindowEvent;
import java.sql.Timestamp;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Locale;

public class UtilityFunctions {
	private UtilityFunctions() {
	}

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
		mainFrame.dispatchEvent(new WindowEvent(mainFrame, WindowEvent.WINDOW_CLOSING));
	}

	public static String formatTimestamp(String pattern, Timestamp timestamp) {
		SimpleDateFormat formatter = new SimpleDateFormat(pattern);
		return formatter.format(timestamp);
	}

	public static String formatMoneyVND(int money) {
		Locale vnLocale = new Locale("vi", "VN");
		NumberFormat currencyInstance = NumberFormat.getCurrencyInstance(vnLocale);
		currencyInstance.setGroupingUsed(true);

		String moneyFormatted = currencyInstance.format(money);
		return moneyFormatted.substring(0, moneyFormatted.length() - 2);
	}
}
