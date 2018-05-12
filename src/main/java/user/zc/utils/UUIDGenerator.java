package user.zc.utils;

import java.util.UUID;

public class UUIDGenerator {

	public static String randomUUID() {
		String uuid = UUID.randomUUID().toString().replaceAll("\\-", "");
		return uuid;
	}
	public static String randomUUID(int len) {
		String uuid = UUID.randomUUID().toString().replaceAll("\\-", "");
		return uuid.substring(0, len);
	}
}
