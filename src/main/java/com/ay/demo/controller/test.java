package com.ay.demo.controller;

import java.util.Arrays;
import java.util.List;
import java.util.StringJoiner;

public class test {
	public static void main(String[] args) {
		StringJoiner result = new StringJoiner(", ","{","}");
		List<String> list = Arrays.asList("a", "b", "c", "d", "e", "f", "g");
		for (String str : list) {
			result.add(str);
		}
		// 删除末尾多余的 delimiter
		System.out.println(result.toString());
	}
}
