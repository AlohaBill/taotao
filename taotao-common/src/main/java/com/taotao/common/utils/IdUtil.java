package com.taotao.common.utils;

import java.util.Random;
import java.util.UUID;

public class IdUtil {
	public static String getUUID() {
		String uuid = UUID.randomUUID().toString().replaceAll("-", "");
		return uuid;
	}
	public static Long getItemId() {
		//生成当前时间的毫秒数(自1970年以来)
		long millis = System.currentTimeMillis();
		Random random = new Random();
		//为了保险起见后面添加两个随机数
		int end2 = random.nextInt(98)+1;
		//最终得出一个不重复的数字
		String str=millis+end2+"";
		Long id=new Long(str);
		return id;
	}
}
