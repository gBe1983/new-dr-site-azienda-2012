/**
 * 
 */
package it.util.password;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * @author Dr
 *
 */
public class MD5 {
	public static String encript(String input) {
		try {
			MessageDigest md = MessageDigest.getInstance("MD5");
			byte[] array = md.digest(input.getBytes());
			StringBuilder sb = new StringBuilder();
			for (int i = 0; i < array.length; ++i) {
				sb.append(Integer.toHexString((array[i] & 0xFF) | 0x100).substring(1, 3));
			}
			return sb.toString();
		} catch (NoSuchAlgorithmException e) {}
		return null;
	}
}