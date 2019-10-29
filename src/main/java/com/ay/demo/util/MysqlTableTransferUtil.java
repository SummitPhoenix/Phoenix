package com.ay.demo.util;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 
 * @author sparkle
 *
 */
public class MysqlTableTransferUtil {
	private static String s = "CREATE TABLE `t_user` (\n" + 
			"  `id` varchar(32) NOT NULL,\n" + 
			"  `username` varchar(20) DEFAULT NULL,\n" + 
			"  `phone` varchar(11) DEFAULT NULL,\n" + 
			"  `address` varchar(20) DEFAULT NULL,\n" + 
			"  `password` varchar(32) DEFAULT NULL,\n" + 
			"  `accountId` varchar(45) DEFAULT NULL,\n" + 
			"  PRIMARY KEY (`id`),\n" + 
			"  UNIQUE KEY `phone_UNIQUE` (`phone`)\n" + 
			") ENGINE=InnoDB DEFAULT CHARSET=utf8mb4";
	public static void main(String[] args) {
		System.out.println(transfer(s));
	}
	public static String transfer(String data) {
		String[] columns = data.split("`");
		List<String> columnList = new ArrayList<>();
		for(int i=3;i<columns.length;i++) {
			if(i%2==0) {
				continue;
			}
			columnList.add(columns[i]);
		}
		columnList.removeAll(columnList.stream().filter(s->s.contains("_")).collect(Collectors.toList()));
		String str = columnList.stream().distinct().collect(Collectors.toList()).toString();
		str = str.substring(1,str.length()-1);
		str = str.replace(" ", "");
		str = str.replace(",", ",\n");
		return str;
	}
}
