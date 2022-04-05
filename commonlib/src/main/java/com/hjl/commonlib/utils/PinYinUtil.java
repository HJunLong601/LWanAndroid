package com.hjl.commonlib.utils;

import java.util.ArrayList;
import java.util.Locale;

public class PinYinUtil {
	// 汉字返回拼音，字母原样返回，都转换为小写
	public static String getPinYin(String input) {
		ArrayList<HanziToPinyin3.Token> tokens = HanziToPinyin3.getInstance().get(input);
		StringBuilder sb = new StringBuilder();
		if (tokens != null && tokens.size() > 0) {
			for (HanziToPinyin3.Token token : tokens) {
				if (HanziToPinyin3.Token.PINYIN == token.type) {
					sb.append(token.target);
				} else {
					sb.append(token.source);
				}
			}
		}
		return sb.toString().toLowerCase(Locale.US);
	}

	public static String getPinYin(Character character) {
		return getPinYin(String.valueOf(character));
	}

	/**
	 * 获取一串字符串的拼音首字母
	 * @param name
	 * @return
	 */
	public static String getFirstLetter(String name) {
		StringBuffer sb = new StringBuffer();
		for (int i = 0;i <name.length();i++){
			sb.append(getPinYin(name.charAt(i)).charAt(0));
		}

		return sb.toString();
	}



}
