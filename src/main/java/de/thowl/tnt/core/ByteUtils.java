package de.thowl.tnt.core;

import java.nio.charset.StandardCharsets;

public class ByteUtils {
	public static String byteArrayToString(byte[] byteArray) {
		return new String(byteArray, StandardCharsets.UTF_8);
	}
}
