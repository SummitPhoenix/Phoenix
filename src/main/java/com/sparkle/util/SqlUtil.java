package com.sparkle.util;

import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SqlUtil {

    public static void main(String[] args) throws Exception {
        generateBatchInsertSql();
    }

    private static void generateBatchInsertSql() {
        String s = "CREATE TABLE `invoice_temporary_indicator` (\n" +
                "  `social_credit_code` varchar(18) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '统一社会信用代码',\n" +
                "  `invoice_suspend_12` int DEFAULT NULL COMMENT '近12个月断票次数',\n" +
                "  `invoice_stop_12` int DEFAULT NULL COMMENT '近12个月最大连续未开票天数',\n" +
                "  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建日期',\n" +
                "  `update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新日期',\n" +
                "  PRIMARY KEY (`social_credit_code`)\n" +
                ") ENGINE=InnoDB DEFAULT CHARSET=utf8mb3 COMMENT='发票数据中间表';";
        String tableName = s.substring(s.indexOf("`") + 1, s.indexOf("` ("));
        tableName = tableName.toLowerCase();
        s = s.substring(s.indexOf("(") + 1, s.indexOf("  PRIMARY"));
        String[] rows = s.split("\n");
        List<String> values = new ArrayList<>();
        List<String> lowerValues = new ArrayList<>();
        for (String row : rows) {
            if ("".equals(row)) {
                continue;
            }
            int start = row.indexOf("`") + 1;
            int end = row.indexOf("`", 3);
            row = row.substring(start, end);
            values.add(row);
            lowerValues.add(row.toLowerCase());
        }
        StringBuilder fieldNamesBuilder = new StringBuilder();
        for (String field : lowerValues) {
            fieldNamesBuilder.append("`");
            fieldNamesBuilder.append(field);
            fieldNamesBuilder.append("`,\n");
        }
        String fieldNames = fieldNamesBuilder.toString();
        fieldNames = fieldNames.substring(0, fieldNames.length() - 2);

        StringBuilder fieldValuesBuilder = new StringBuilder();
        for (String field : values) {
            fieldValuesBuilder.append("#{item.");
            fieldValuesBuilder.append(field.toUpperCase());
            fieldValuesBuilder.append("},\n");
        }
        String fieldValues = fieldValuesBuilder.toString();
        fieldValues = fieldValues.substring(0, fieldValues.length() - 2);

        StringBuilder updateFieldsBuilder = new StringBuilder();
        for (int i = 1; i < values.size(); i++) {
            updateFieldsBuilder.append("`");
            updateFieldsBuilder.append(lowerValues.get(i));
            updateFieldsBuilder.append("`=VALUES(");
            updateFieldsBuilder.append(values.get(i));
            updateFieldsBuilder.append("),\n");
        }
        String updateFields = updateFieldsBuilder.toString();
        updateFields = updateFields.substring(0, updateFields.length() - 2);

        String sql = "INSERT INTO report_business." + tableName + "(\n" +
                fieldNames + ")\n" +
                "VALUES\n" +
                "<foreach collection=\"list\" item=\"item\" separator=\",\"> \n(" +
                fieldValues + ")\n </foreach> \n" +
                "ON DUPLICATE KEY UPDATE\n" + updateFields;
        System.out.println(sql);
    }

    private static void generateCreateTableSql() throws Exception {
        String table = "SERIAL_ID\t主键\tVARCHAR2(128)\tFALSE\tFALSE\t主键\n" +
                "TRADEMARK_INFO_ID\t商标信息id\tVARCHAR2(128)\tFALSE\tFALSE\t商标信息id\n" +
                "EID\teid\tVARCHAR2(64)\tFALSE\tFALSE\teid\n" +
                "REGISTRANT_NAME_ZH\t注册人中文名称\tCLOB\tFALSE\tFALSE\t注册人中文名称\n" +
                "REGISTRANT_NAME_EN\t注册人外文名称\tCLOB\tFALSE\tFALSE\t注册人外文名称\n" +
                "REGISTRANT_ADDRESS_ZH\t注册人中文地址\tCLOB\tFALSE\tFALSE\t注册人中文地址\n" +
                "REGISTRANT_ADDRESS_EN\t注册人英文地址\tCLOB\tFALSE\tFALSE\t注册人英文地址\n" +
                "ENT_INFO\t企业名称/注册号/统一社会信用代码/有数企业 ID\tVARCHAR2(128)\tFALSE\tFALSE\t企业名称/注册号/统一社会信用代码/有数企业 ID\n" +
                "ENT_NAME\t企业名称\tVARCHAR2(128)\tFALSE\tFALSE\t企业名称\n" +
                "ENT_NO\t纳税人识别号\tVARCHAR2(128)\tFALSE\tFALSE\t纳税人识别号\n" +
                "BATCH_ID\t批次号\tVARCHAR2(128)\tFALSE\tFALSE\t批次号\n" +
                "LRSJ\t录入时间\tTIMESTAMP\tFALSE\tFALSE\t录入时间";
        String tableName = "hsj_trademark_registrantinfo";
        String tableNameComment = "商标注册人信息列表";

        String sql = "CREATE TABLE `report_business`.`" + tableName + "`  (\n";
        String[] columns = table.split("\n");
        String primaryKey = "serial_id";
        for (int i = 0; i < columns.length; i++) {
            String[] values = columns[i].split("\t");
            values[0] = values[0].toLowerCase();
            //主键
            if ("TRUE".equals(values[3])) {
                primaryKey = values[0];
            }
            //字段类型
            String dataType = "";
            if (columns[i].contains("VARCHAR")) {
                String varcharLength = values[2].substring(values[2].indexOf("("), values[2].indexOf(")") + 1);
                dataType = "varchar" + varcharLength;
            } else if (columns[i].contains("TIMESTAMP")) {
                dataType = "timestamp";
            } else if (columns[i].contains("DATE")) {
                dataType = "datetime";
            } else if (columns[i].contains("CLOB")) {
                dataType = "text";
            } else {
                throw new Exception();
            }
            //是否需要非空
            String nullControl = "NULL";
            if ("TRUE".equals(values[4])) {
                nullControl = "NOT " + nullControl;
            }
            sql += "`" + values[0] + "` " + dataType + " " + nullControl + " COMMENT '" + values[1] + "',\n";
        }
        if (StringUtils.hasText(primaryKey)) {
            sql += "PRIMARY KEY (`" + primaryKey + "`)\n" +
                    ") COMMENT = '" + tableNameComment + "';";
        }
        System.out.println(sql);
    }

    private static void generateCreateTableSqlJudicial() throws Exception {
        String table = "ZXGG_ID\tZXGG_ID\tVARCHAR2(500)\tFALSE\tFALSE\t执行公告 ID 执行公告唯一标识\n" +
                "CASE_STATE_T\tCASE_STATE_T\tVARCHAR2(500)\tFALSE\tFALSE\t案件状态,立案受理、归档、 执行中、结案等\n" +
                "EXEC_MONEY\tEXEC_MONEY\tCLOB\tFALSE\tFALSE\t执行金额 执行标的\n" +
                "IDCARD_NO\tIDCARD_NO\tCLOB\tFALSE\tFALSE\t身份证号码\n" +
                "PARTY_TYPE\tPARTY_TYPE\tVARCHAR2(500)\tFALSE\tFALSE\t主体类型 公司/自然人\n" +
                "PNAME\tPNAME\tVARCHAR2(500)\tFALSE\tFALSE\t当事人名称 企业名称/姓名\n" +
                "CREATE_TIME\tCREATE_TIME\tDATE\tFALSE\tFALSE\t\n" +
                "SIGNAL_DESC\tSIGNAL_DESC\tVARCHAR2(512)\tFALSE\tFALSE\t信号描述（高精字段）\n" +
                "SIGNAL_RATING\tSIGNAL_RATING\tVARCHAR2(512)\tFALSE\tFALSE\t信号等级（高精字段）\n" +
                "SIGNAL_RULE_NO\tSIGNAL_RULE_NO\tVARCHAR2(512)\tFALSE\tFALSE\t规则编码（高精字段）\n" +
                "SIGNAL_RULE_VERSION\tSIGNAL_RULE_VERSION\tVARCHAR2(512)\tFALSE\tFALSE\t规则版本（高精字段）\n" +
                "RULE_MAIN_TYPE\tRULE_MAIN_TYPE\tVARCHAR2(512)\tFALSE\tFALSE\t大类（高精字段）\n" +
                "RULE_SUB_TYPE\tRULE_SUB_TYPE\tVARCHAR2(512)\tFALSE\tFALSE\t小类（高精字段）\n" +
                "CASE_NO_KWORD_T\tCASE_NO_KWORD_T\tVARCHAR2(512)\tFALSE\tFALSE\t代字，执、执恢、执保等（高精字段）\n" +
                "QUERY_VERSION\tQUERY_VERSION\tVARCHAR2(32)\tFALSE\tFALSE\t接口版本，“v2”标准版，“vip”高精版\n" +
                "REQ_MC\tREQ_MC\tVARCHAR2(512)\tFALSE\tFALSE\t名称\n" +
                "REQ_HM\tREQ_HM\tVARCHAR2(128)\tFALSE\tFALSE\t号码\n" +
                "QUERY_TYPE\tQUERY_TYPE\tVARCHAR2(128)\tFALSE\tFALSE\t查询类型，“company”企业，“person”个人";
        table = table.replace("\n", "\t \n");
        String tableName = "fahai_sifa_zxgg_party";
        String tableNameComment = "涉诉开庭公告当事人数据";

        String sql = "DROP TABLE IF EXISTS report_business." + tableName + ";\n" +
                "CREATE TABLE `report_business`.`" + tableName + "`  (\n";
        String[] columns = table.split("\n");
        String primaryKey = columns[0].split("\t")[0];
        primaryKey = primaryKey.toLowerCase();
        for (int i = 0; i < columns.length; i++) {
            String[] values = columns[i].split("\t");
            values[0] = values[0].toLowerCase();
            //主键
            if ("TRUE".equals(values[3])) {
                primaryKey = values[0];
            }
            //字段类型
            String dataType = "";
            if (columns[i].contains("VARCHAR")) {
                String varcharLength = values[2].substring(values[2].indexOf("("), values[2].indexOf(")") + 1);
                dataType = "varchar" + varcharLength;
            } else if (columns[i].contains("TIMESTAMP")) {
                dataType = "timestamp";
            } else if (columns[i].contains("DATE")) {
                dataType = "datetime";
            } else if (columns[i].contains("CLOB")) {
                dataType = "text";
            } else {
                throw new Exception();
            }
            //是否需要非空
            String nullControl = "NULL";
            if (i == 0) {
                values[4] = "TRUE";
            }
            if ("TRUE".equals(values[4])) {
                nullControl = "NOT " + nullControl;
            }
            sql += "`" + values[0] + "` " + dataType + " " + nullControl + " COMMENT '" + values[5] + "',\n";
        }
        if (StringUtils.hasText(primaryKey)) {
            sql += "PRIMARY KEY (`" + primaryKey + "`)\n" +
                    ") COMMENT = '" + tableNameComment + "';";
        }
        System.out.println(sql);
    }

    static Pattern linePattern = Pattern.compile("_(\\w)");

    /**
     * 下划线转驼峰
     */
    public static String lineToHump(String str) {
        str = str.toLowerCase();
        Matcher matcher = linePattern.matcher(str);
        StringBuffer sb = new StringBuffer();
        while (matcher.find()) {
            matcher.appendReplacement(sb, matcher.group(1).toUpperCase());
        }
        matcher.appendTail(sb);
        return sb.toString();
    }
}