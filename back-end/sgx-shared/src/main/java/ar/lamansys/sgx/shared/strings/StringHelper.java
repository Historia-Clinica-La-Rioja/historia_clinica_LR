package ar.lamansys.sgx.shared.strings;

import java.util.Collections;

public class StringHelper {

	private static final int MAX_LENGTH = 4;

	private StringHelper() {
		super();
	}

	public static String soundex(String data) {
		StringBuilder result = new StringBuilder();
		if (data != null && !data.isEmpty()) {
			String previousCode;
			String currentCode;

			result.append(Character.toUpperCase(data.charAt(0)));
			previousCode = "";
			for (int i = 1; i < data.length(); i++) {
				currentCode = encodeChar(data.charAt(i));
				if (!currentCode.equals(previousCode))
					result.append(currentCode);
				if (result.length() == MAX_LENGTH)
					break;
				if (!currentCode.isEmpty())
					previousCode = currentCode;
			}
		}
		if (result.length() < 4)
			result.append(String.join("", Collections.nCopies(4 - result.length(), "0")));

		return result.toString();
	}

	private static String encodeChar(char c) {
		switch (Character.toLowerCase(c)) {
		case 'b':
		case 'f':
		case 'p':
		case 'v':
			return "1";
		case 'c':
		case 'g':
		case 'j':
		case 'k':
		case 'q':
		case 's':
		case 'x':
		case 'z':
			return "2";
		case 'd':
		case 't':
			return "3";
		case 'l':
			return "4";
		case 'm':
		case 'n':
			return "5";
		case 'r':
			return "6";
		default:
			return "";
		}
	}

	public static boolean isNullOrWhiteSpace(String str) {
		return (str == null || str.length() == 0 || str.equals(" "));
	}

	public static String toString(Object value) {
		return value == null ? null : value.toString();
	}
}
