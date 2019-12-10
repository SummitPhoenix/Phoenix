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
	private static String s = "CREATE TABLE `ent_jiangsushenghuagong1_datapool_anquanpingjiajigouxi` (\n" +
			"  `id` int(11) NOT NULL AUTO_INCREMENT,\n" +
			"  `owner` varchar(40) NOT NULL,\n" +
			"  `modifier` varchar(40) NOT NULL,\n" +
			"  `last_modified` datetime NOT NULL,\n" +
			"  `created_at` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,\n" +
			"  `modified_method` varchar(255) NOT NULL,\n" +
			"  `app_key` varchar(20) NOT NULL,\n" +
			"  `app_extend_key` varchar(20) NOT NULL,\n" +
			"  `disable` tinyint(1) NOT NULL DEFAULT '0',\n" +
			"  `jigoumingcheng` text,\n" +
			"  `fadingdaibiaoren` text,\n" +
			"  `tongyishehuixinyongd` text,\n" +
			"  `tongyishehuixinyong1` varchar(255) DEFAULT '',\n" +
			"  `sheng` text,\n" +
			"  `shi37` text,\n" +
			"  `quxian` text,\n" +
			"  `jiedao` text,\n" +
			"  `shengbangong` text,\n" +
			"  `shibangong` text,\n" +
			"  `quxianbangong` text,\n" +
			"  `jiedaobangong` text,\n" +
			"  `yewufanwei` text,\n" +
			"  `lishuguanxi` text,\n" +
			"  `jigouguanwang` varchar(255) DEFAULT '',\n" +
			"  `farenqianming` text,\n" +
			"  `farenpeixunzhengshub` varchar(255) DEFAULT '',\n" +
			"  `farenpeixunzhengshu` text,\n" +
			"  `lianxiren` varchar(255) DEFAULT '',\n" +
			"  `lianxirenshouji` varchar(255) DEFAULT '',\n" +
			"  `dianziyoujian` varchar(255) DEFAULT '',\n" +
			"  `jigoujibie` text,\n" +
			"  `yingyezhizhaobianhao` varchar(255) DEFAULT '',\n" +
			"  `yingyezhizhao` text,\n" +
			"  `jigouzizhizhengshubi` varchar(255) DEFAULT '',\n" +
			"  `jigouzizhizhengshu` text,\n" +
			"  `zhengshuyouxiaoqi` varchar(255) DEFAULT '',\n" +
			"  `gudingzichanshenjiba` text,\n" +
			"  `gongzuochangsuochanq` text,\n" +
			"  `anquanpingjiaguochen` text,\n" +
			"  `neibuguanlizhidu` text,\n" +
			"  `zhucezijin` varchar(20) DEFAULT '',\n" +
			"  `gudingzichan` varchar(20) DEFAULT '',\n" +
			"  `gongzuochangsuomianj` varchar(20) DEFAULT '',\n" +
			"  `zhuanzhianquanpingji` varchar(20) DEFAULT '',\n" +
			"  `yijipingjiashirenshu` varchar(20) DEFAULT '',\n" +
			"  `erjipingjiashirenshu` varchar(20) DEFAULT '',\n" +
			"  `sanjipingjiashirensh` varchar(20) DEFAULT '',\n" +
			"  `zhuanshirenshu` varchar(20) DEFAULT '',\n" +
			"  `zhigongsixianyijinzh` text,\n" +
			"  `danweijianjie` text,\n" +
			"  `farenlianxifangshi` varchar(255) DEFAULT '',\n" +
			"  `sheng1` text,\n" +
			"  `shi12` text,\n" +
			"  `quxian1` text,\n" +
			"  `jiedao1` text,\n" +
			"  PRIMARY KEY (`id`),\n" +
			"  KEY `owner` (`owner`),\n" +
			"  KEY `last_modified` (`last_modified`),\n" +
			"  KEY `modified_method` (`modified_method`),\n" +
			"  KEY `app_key` (`app_key`)\n" +
			") ENGINE=InnoDB AUTO_INCREMENT=9 DEFAULT CHARSET=utf8;";
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
