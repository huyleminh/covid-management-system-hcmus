package com.utilities;

public class UtilityFunctions {
	private UtilityFunctions() {}
	static public int sum(int[] a) {
		int result = 0;
		for (int item : a)
			result += item;

		return result;
	}
}
