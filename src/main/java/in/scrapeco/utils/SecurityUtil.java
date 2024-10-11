package in.scrapeco.utils;

import java.security.SecureRandom;
import java.util.Random;

import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.util.DigestUtils;

public class SecurityUtil {

	public SecurityUtil() {

	}

	public static String generateOtp() {
		Random rand = new Random();
		int n = rand.nextInt(99999) + 100000;
		return n + "";
	}

	public static String hash(String password) {
		return DigestUtils.md5DigestAsHex(salt(password).getBytes());
	}

	public static String hash2(String password) {
		return DigestUtils.md5DigestAsHex(salt2(password).getBytes());
	}

	public static String salt(String password) {
		return "salt_" + password + "_transbnk_hard";
	}

	public static String salt2(String password) {
		return "salt_" + password + "_transbnk_hard_level2";
	}

	public static String generateOpaque(Long userId) {
		SecureRandom random = new SecureRandom();
		long token1 = Math.abs(random.nextLong());
		long token2 = Math.abs(random.nextLong());
		String randomString = Long.toString(token1, 30) + Long.toString(token2, 30) + userId;
		randomString = randomString.replace("/", "").replace("\\", "");
		return randomString;
	}

	public static String generatePassword() {
		String characters = "ABCDEFGHIJKLMNOPQRSTUVWXYZ!@#$%&*abcdefghijklmnopqrstuvwxyz0123456789!@#$%&*";
		String capLetter = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
		Random random = new Random();
		String password = capLetter.charAt(random.nextInt(capLetter.length()))
				+ RandomStringUtils.random(11, characters);
		return password;
	}

}
