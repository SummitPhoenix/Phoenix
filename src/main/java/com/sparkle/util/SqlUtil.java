package com.sparkle.util;

import com.alibaba.fastjson.JSONObject;
import org.apache.commons.io.FileUtils;
import org.springframework.util.StringUtils;

import java.io.File;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.Map.Entry;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SqlUtil {
    public static void main(String[] args) throws Exception {
//        String s = "AlterInfo,EntInfo,Branch,GSInfo,Shareholder,KeyPerson,BaikeInfo,Investment,B2B,EnterpriseProductInfo,OriginateTeam,Financing,AnnualReport,Tender,RegisterPerson,SimilarCom,RecruitmentOverview,TradeShow,LawOfficeInfo,RecruitmentDetail,PromotionOverview,AdministrativeLicense,ImportAndExportCredit,TaxCredit,PromotionDetail,TaxQualify,ShopGoodsInfo,BrandInfoOverView,ShopInfo,EcomOverview,BrandInfo,Wechat,APPViewAndDetail,Microblog,Patents,PatentsOverview,softWareCopyright,opusCopyright,WebsiteInformation,TradeMark,Certificate,TaxArrearsNoticeDetail,SpotCheckInfo,EquityPledged,TaxIllegal,IllegalInfoCB,HeightLimitInfoCB,ChattelMortgageInfoCB,Abnormality,CourtAnnouncement,Dishonest,EndBookInfo,AnnTrialInfo,Executor,AdministrativePenaltyTax,AdministrativePenalty,TaxArrearsNoticeOverview,JudicialAssist,TaxAbnormal,JudgementsInfo,IntellectualPropertyPledge,LinkedinUserInfo,PersonalMicroblog,Maimai";
//        s = s.toLowerCase();
//        String[] labels = s.split(",");
//        String tables = "abnormality,administrativepenalty,administrativepenaltytax,alterinfo,annualreport,baikeinfo,branch,brandinfo,brandinfooverview,courtannouncement,dishonest,ecomoverview,endbookinfo,enterpriseproductinfo,equitypledged,executor,financing,gsinfo,heightlimitinfocb,importandexportcredit,investment,judgementsinfo,judicialassist,keyperson,microblog,opuscopyright,originateteam,patents,patentsoverview,promotiondetail,promotionoverview,recruitmentdetail,shareholder,shopinfo,similarcom,softwarecopyright,taxcredit,taxqualify,tender,trademark,tradeshow,websiteinformation,wechat";
//        String[] tableNames = tables.split(",");
//        Set<String> a = new LinkedHashSet<>(Arrays.asList(labels));
//        Set<String> b = new LinkedHashSet<>(Arrays.asList(tableNames));
//        Set<String> set = new LinkedHashSet<>(a);
//        set.removeAll(b);
//        System.out.println(set);
//        set.clear();
//        set.addAll(b);
//        set.removeAll(a);
//        System.out.println(set);

        test();
    }

    private static void generateBatchInsertSql() {
        String s = "CREATE TABLE `taxinfo_qybgdjxx_list` (\n" +
                "  `nsrsbh` varchar(50) NOT NULL COMMENT '纳税人识别号',\n" +
                "  `bgxmmc` varchar(200) DEFAULT NULL COMMENT '变更项目名称',\n" +
                "  `bgxmdm` varchar(50) DEFAULT NULL COMMENT '变更项目代码',\n" +
                "  `bgqnr` varchar(3000) DEFAULT NULL COMMENT '变更前内容',\n" +
                "  `bghnr` varchar(3000) DEFAULT NULL COMMENT '变更后内容',\n" +
                "  `bgrq` varchar(20) DEFAULT NULL COMMENT '变更日期',\n" +
                "  `update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP\n" +
                ") ENGINE=InnoDB DEFAULT CHARSET=utf8mb3 COMMENT='企业变更表';";
        String tableName = s.substring(s.indexOf("`") + 1, s.indexOf("` ("));
        tableName = tableName.toLowerCase();
        if (s.contains("PRIMARY")) {
            s = s.substring(s.indexOf("(") + 1, s.indexOf("  PRIMARY"));
        } else {
            s = s.substring(s.indexOf("(") + 1, s.indexOf(") ENGINE"));
        }
        String[] rows = s.split("\n");
        List<String> values = new ArrayList<>();
        List<String> lowerValues = new ArrayList<>();
        for (String row : rows) {
            if ("".equals(row) || row.contains("update_time")) {
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

    private static void generateTaxCreateTableSql() throws Exception {
        String table = "1 纳税人识别号 NSRSBH VARCHAR2(50)\n" +
                "2 变更项目名称 BGXMMC VARCHAR2(200)\n" +
                "3 变更项目代码 BGXMDM VARCHAR2(50)\n" +
                "4 变更前内容 BGQNR VARCHAR2(3000)\n" +
                "5 变更后内容 BGHNR VARCHAR2(3000)\n" +
                "6 变更日期 BGRQ VARCHAR2(20)";
        String tableName = "QYBGDJXX_LIST";
        tableName = "taxinfo_" + tableName.toLowerCase();
        String tableNameComment = "企业变更表";

        String sql = "CREATE TABLE `report_business`.`" + tableName + "`  (\n";
        String[] columns = table.split("\n");
        String primaryKey = "nsrsbh";
        for (int i = 0; i < columns.length; i++) {
            String[] values = columns[i].split(" ");
            values[2] = values[2].toLowerCase();
            //字段类型
            String dataType = "";
            if (columns[i].contains("VARCHAR")) {
                String varcharLength = values[3].substring(values[3].indexOf("("), values[3].indexOf(")") + 1);
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
                nullControl = "NOT " + nullControl;
            }
            sql += "`" + values[2] + "` " + dataType + " " + nullControl + " COMMENT '" + values[1] + "',\n";
        }
        if (StringUtils.hasText(primaryKey)) {
            sql += "PRIMARY KEY (`" + primaryKey + "`)\n" +
                    ") COMMENT = '" + tableNameComment + "';";
        }
        System.out.println(sql);
    }

    private static void generateMotivationalSalesCloudCreateTableSql(String tableName, String tableNameComment, String primaryKey, String table) throws Exception {
        tableName = "lxy_" + tableName;
        tableName = tableName.toLowerCase();
        String sql = "CREATE TABLE `report_business`.`" + tableName + "`  (\n";
        String[] columns = table.split("\n");

        /**
         * key type default description
         */
        for (int i = 0; i < columns.length; i++) {
            String[] values = columns[i].split("\t");
            //字段类型
            String dataType;
            switch (values[1]) {
                case "string":
                    dataType = "varchar(255)";
                    break;
                case "timestamp":
                    dataType = "datetime";
                    break;
                case "int":
                    dataType = "int";
                    break;
                default:
                    throw new Exception("字段类型错误");
            }
            //是否需要非空
            String nullControl = "NULL";
            if ("pid".equals(values[0]) || "PID".equals(values[0])) {
                nullControl = "NOT " + nullControl;
            }
            String defaultStr = "";
            if (!" ".equals(values[2])) {
                defaultStr = " DEFAULT " + values[2];
            }
            String comment = "";
            if (!" ".equals(values[3])) {
                comment = " COMMENT '" + values[3] + "'";
            }
            sql += "`" + values[0] + "` " + dataType + " " + nullControl + defaultStr + comment + ",\n";
        }
        if (StringUtils.hasText(primaryKey)) {
            sql += "PRIMARY KEY (`" + primaryKey + "`)\n";
        }
        sql += ") COMMENT = '" + tableNameComment + "';";
        System.out.println(sql);
        FileUtils.writeStringToFile(new File("C:\\Users\\0028\\Desktop\\SQL\\" + tableNameComment + ".txt"), sql, StandardCharsets.UTF_8.toString());
    }

    private static void databaseTransfer() throws Exception {
        String str = FileUtils.readFileToString(new File("C:\\Users\\0028\\Desktop\\MongoDB.json"), StandardCharsets.UTF_8.toString());
        String[] jsonArr = str.split("\n");
        for (String json : jsonArr) {
            JSONObject jsonObject = JSONObject.parseObject(json);
            String tableName = (String) jsonObject.get("name");
            String tableNameComment = (String) jsonObject.get("title");
            String primaryKey = null;
            String schemaStr = (String) jsonObject.get("schema_strings");

            JSONObject schema = JSONObject.parseObject(schemaStr);
            Map<String, Object> schemaMap = schema.getJSONObject("properties").toJavaObject(Map.class);

            StringBuilder sb = new StringBuilder();
            //第一行添加税号
            sb.append("socialCreditCode" + "\t" + "string" + "\t" + " " + "\t" + "统一社会信用代码" + "\n");
            Iterator<Entry<String, Object>> entries = schemaMap.entrySet().iterator();
            while (entries.hasNext()) {
                Entry<String, Object> entry = entries.next();
                String key = entry.getKey();
                if (!"_id".equals(key) && !key.startsWith("text_")) {
                    Map<String, Object> values = (Map<String, Object>) entry.getValue();
                    String bsonType = (String) values.get("bsonType");
                    if (!"string".equals(bsonType) && !"timestamp".equals(bsonType) && !"int".equals(bsonType)) {
                        throw new Exception("字段类型错误:" + bsonType);
                    }
                    String defaultValue = StringUtils.hasText(String.valueOf(values.get("defaultValue"))) ? String.valueOf(values.get("defaultValue")) : " ";
                    String description = StringUtils.hasText((String) values.get("description")) ? (String) values.get("description") : " ";
                    sb.append(key + "\t" + bsonType + "\t" + defaultValue + "\t" + description + "\n");
                }
            }
            //最后一行添加创建时间
            sb.append("createTime" + "\t" + "timestamp" + "\t" + "CURRENT_TIMESTAMP" + "\t" + "创建时间" + "\n");
            if (schemaMap.containsKey("pid")) {
                primaryKey = "pid";
            }
            if (schemaMap.containsKey("PID")) {
                primaryKey = "PID";
            }
            generateMotivationalSalesCloudCreateTableSql(tableName, tableNameComment, primaryKey, sb.toString());
            System.out.println();
        }
//        FileUtils.writeStringToFile(new File("C:\\Users\\0028\\Desktop\\MongoDB1.json"), str, StandardCharsets.UTF_8.toString());
    }

    /**
     * 驼峰转下划线
     *
     * @param str 驼峰命名字符串
     * @return 下划线命名字符串
     */
    private static String camelToUnderline(String str) {
        if (str == null || str.isEmpty()) {
            return "";
        }
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < str.length(); i++) {
            char c = str.charAt(i);
            if (Character.isUpperCase(c)) {
                sb.append("_").append(Character.toLowerCase(c));
            } else {
                sb.append(c);
            }
        }
        return sb.toString();
    }

    private static void test() throws Exception {
        String str = FileUtils.readFileToString(new File("C:\\Users\\0028\\Desktop\\lxy_all.sql"), StandardCharsets.UTF_8.toString());
        while (str.contains("CREATE TABLE")) {
            String createTableSql = str.substring(str.indexOf("CREATE TABLE"), str.indexOf("ROW_FORMAT = Dynamic;"));
            str = str.substring(str.indexOf("DROP TABLE") + 10);
            generateMotivationalSalesCloudInsertSql(createTableSql);
            System.out.println("</insert>");
        }
    }

    private static void generateMotivationalSalesCloudInsertSql(String table) {
        String tableName = table.substring(table.indexOf("`") + 1, table.indexOf("`  ("));
//        tableName = tableName.toLowerCase();
        if (table.contains("PRIMARY")) {
            table = table.substring(table.indexOf("(") + 3, table.indexOf("  PRIMARY"));
        } else {
            table = table.substring(table.indexOf("(") + 3, table.indexOf(") ENGINE"));
        }
        String[] rows = table.split("\n");
        List<String> values = new ArrayList<>();
//        List<String> lowerValues = new ArrayList<>();
        for (String row : rows) {
            if ("".equals(row) || row.contains("createTime")) {
                continue;
            }
            row = row.replace("default 0", "");
            int start = row.indexOf("`") + 1;
            int end = row.indexOf("`", 4);
            row = row.substring(start, end);
            values.add(row);
//            lowerValues.add(row.toLowerCase());
        }
        StringBuilder fieldNamesBuilder = new StringBuilder();
        for (String field : values) {
            fieldNamesBuilder.append("`");
            fieldNamesBuilder.append(field);
            fieldNamesBuilder.append("`,\n");
        }
        String fieldNames = fieldNamesBuilder.toString();
        fieldNames = fieldNames.substring(0, fieldNames.length() - 2);

        StringBuilder fieldValuesBuilder = new StringBuilder();
        for (String field : values) {
            fieldValuesBuilder.append("#{item.");
            fieldValuesBuilder.append(field);
            fieldValuesBuilder.append("},\n");
        }
        String fieldValues = fieldValuesBuilder.toString();
        fieldValues = fieldValues.substring(0, fieldValues.length() - 2);

//        StringBuilder updateFieldsBuilder = new StringBuilder();
//        for (int i = 1; i < values.size(); i++) {
//            updateFieldsBuilder.append("`");
//            updateFieldsBuilder.append(values.get(i));
//            updateFieldsBuilder.append("`=VALUES(");
//            updateFieldsBuilder.append(values.get(i));
//            updateFieldsBuilder.append("),\n");
//        }
//        String updateFields = updateFieldsBuilder.toString();
//        updateFields = updateFields.substring(0, updateFields.length() - 2);
        String updateFields = "";

        String sql = "INSERT INTO report_business." + tableName + "(\n" +
                fieldNames + ")\n" +
                "VALUES\n" +
                "<foreach collection=\"list\" item=\"item\" separator=\",\"> \n(" +
                fieldValues + ")\n </foreach> \n" +
                "ON DUPLICATE KEY UPDATE\n" + updateFields;
        System.out.println("<insert id=\"insert" + tableName + "\" parameterType=\"java.util.List\">");
        System.out.println(sql);
    }
}