package com.mycompany.utils;

import java.util.UUID;

public class UUIDUtils {

	public static String uuidString(){
		return UUID.randomUUID().toString().replaceAll("-", "");
	}
}
