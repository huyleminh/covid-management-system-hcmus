package com.utilities;

public class UtilityFunctions {
	private UtilityFunctions() {}
	static public int sum(int[] a) {
		int result = 0;
		for (int item : a)
			result += item;

		return result;
	}

	static public boolean isLeapYear(short year) {
		return (year % 4 == 0 && year % 100 != 0) || (year % 400 == 0);
	}
}
