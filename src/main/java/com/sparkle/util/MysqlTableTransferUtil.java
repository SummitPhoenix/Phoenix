package com.sparkle.util;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 
 * @author sparkle
 *
 */
public class MysqlTableTransferUtil {
	private static String s = "CREATE TABLE `ent_zhangjiagangdianziko_datapool_bumenyingjifangan` (\n" + 
			"  `id` int(11) NOT NULL AUTO_INCREMENT,\n" + 
			"  `owner` varchar(40) NOT NULL,\n" + 
			"  `modifier` varchar(40) NOT NULL,\n" + 
			"  `last_modified` datetime NOT NULL,\n" + 
			"  `created_at` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,\n" + 
			"  `modified_method` varchar(255) NOT NULL,\n" + 
			"  `app_key` varchar(20) NOT NULL,\n" + 
			"  `app_extend_key` varchar(20) NOT NULL,\n" + 
			"  `disable` tinyint(1) NOT NULL DEFAULT '0',\n" + 
			"  `xianchangchuzhifanga` varchar(255) DEFAULT '',\n" + 
			"  `chuzhifanganleixing` text,\n" + 
			"  `shiguleixing` text,\n" + 
			"  `xianchangchuzhifang1` text,\n" + 
			"  `tianxieshijian` datetime DEFAULT '0000-00-00 00:00:00',\n" + 
			"  `fujian` text,\n" + 
			"  `a1shigufengxianfenxi` text,\n" + 
			"  `a2yingjigongzuozhize` text,\n" + 
			"  `a4zhuyishixiang` text,\n" + 
			"  `a3yingjichuzhi` text,\n" + 
			"  `tianxiebumen` varchar(255) DEFAULT '',\n" + 
			"  `yuanleibie` text,\n" + 
			"  `shigubianhao` varchar(255) DEFAULT '',\n" + 
			"  PRIMARY KEY (`id`),\n" + 
			"  KEY `owner` (`owner`),\n" + 
			"  KEY `last_modified` (`last_modified`),\n" + 
			"  KEY `modified_method` (`modified_method`),\n" + 
			"  KEY `app_key` (`app_key`)\n" + 
			") ENGINE=InnoDB DEFAULT CHARSET=utf8;";
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
