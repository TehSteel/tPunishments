package com.github.tehsteel.tpunishments.core.util;

public final class TimeUtil {

	private TimeUtil() {
	}

	public static long toMillisFromString(final String duration) {
		final String[] stringArray = duration.toLowerCase().split("(?<=\\D)(?=\\d)|(?<=\\d)(?=\\D)");


		try {
			final long i = Long.parseLong(stringArray[0]);

			return switch (stringArray[1]) {
				case "s" -> i * 1000;
				case "m" -> i * 1000 * 60;
				case "h" -> i * 1000 * 60 * 60;
				case "d" -> i * 1000 * 60 * 60 * 24;
				case "w" -> i * 1000 * 60 * 60 * 24 * 7;
				case "mo" -> i * 1000 * 60 * 60 * 24 * 30;
				case "y" -> i * 1000 * 60 * 60 * 24 * 365;
				default -> -1;
			};
		} catch (final NumberFormatException e) {
			return -1;
		}
	}

	public static String fromMillisToString(final long millis) {
		if (millis < 0) {
			return "Never";
		}

		long remainingMillis = millis;
		final long years = remainingMillis / (1000L * 60 * 60 * 24 * 365);
		remainingMillis %= (1000L * 60 * 60 * 24 * 365);
		final long months = remainingMillis / (1000L * 60 * 60 * 24 * 30);
		remainingMillis %= (1000L * 60 * 60 * 24 * 30);
		final long weeks = remainingMillis / (1000L * 60 * 60 * 24 * 7);
		remainingMillis %= (1000L * 60 * 60 * 24 * 7);
		final long days = remainingMillis / (1000L * 60 * 60 * 24);
		remainingMillis %= (1000L * 60 * 60 * 24);
		final long hours = remainingMillis / (1000L * 60 * 60);
		remainingMillis %= (1000L * 60 * 60);
		final long minutes = remainingMillis / (1000L * 60);
		remainingMillis %= (1000L * 60);
		final long seconds = remainingMillis / 1000L;

		final StringBuilder result = new StringBuilder();

		if (years > 0) {
			result.append(years).append(" Years");
		}
		if (months > 0) {
			result.append(months).append(" Months");
		}
		if (weeks > 0) {
			result.append(weeks).append(" Weeks");
		}
		if (days > 0) {
			result.append(days).append(" Days");
		}
		if (hours > 0) {
			result.append(hours).append(" Hours");
		}
		if (minutes > 0) {
			result.append(minutes).append(" Minutes");
		}
		if (seconds > 0) {
			result.append(seconds).append(" Seconds");
		}

		return result.toString();
	}


}
