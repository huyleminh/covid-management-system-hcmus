package com.utilities;

import java.util.Objects;

public class Pair<L, R> {
	private L leftValue;
	private R rightValue;

	public Pair(L leftValue, R rightValue) {
		this.leftValue = leftValue;
		this.rightValue = rightValue;
	}

	public L getLeftValue() {
		return leftValue;
	}

	public void setLeftValue(L leftValue) {
		this.leftValue = leftValue;
	}

	public R getRightValue() {
		return rightValue;
	}

	public void setRightValue(R rightValue) {
		this.rightValue = rightValue;
	}

	@Override
	public boolean equals(Object another) {
		if (this == another)
			return true;
		if (another == null || getClass() != another.getClass())
			return false;

		final Pair<?, ?> anotherPair = (Pair<?, ?>) another;
		return leftValue.equals(anotherPair.leftValue) && rightValue.equals(anotherPair.rightValue);
	}

	@Override
	public int hashCode() {
		return Objects.hash(leftValue, rightValue);
	}
}
