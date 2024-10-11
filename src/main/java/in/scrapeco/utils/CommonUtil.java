package in.scrapeco.utils;

import java.util.Base64;

import jakarta.servlet.http.HttpServletRequest;

public class CommonUtil {

	public static String getRemoteAddress(HttpServletRequest request) {
		String ip = "";
		try {
			ip = request.getHeader("X-Forwarded-For");
			if (ip == null) {
				ip = "";
			}
		} catch (Exception ignored) {
		}

		if (ip.equalsIgnoreCase("")) {
			ip = request.getRemoteAddr();
		}

		return ip;
	}

	public static String toBase64(String input) {
		return Base64.getEncoder().encodeToString(input.getBytes());
	}

	public static String toBase64(byte[] input) {
		return Base64.getEncoder().encodeToString(input);
	}

	public static String convertToTitleCaseIteratingChars(String text) {
		if (text == null || text.isEmpty()) {
			return text;
		}

		StringBuilder converted = new StringBuilder();

		boolean convertNext = true;
		for (char ch : text.toCharArray()) {
			if (Character.isSpaceChar(ch)) {
				convertNext = true;
			} else if (convertNext) {
				ch = Character.toTitleCase(ch);
				convertNext = false;
			} else {
				ch = Character.toLowerCase(ch);
			}
			converted.append(ch);
		}

		return converted.toString();
	}

}
