package com.sparkle.util;


import org.apache.cxf.endpoint.Client;
import org.apache.cxf.jaxws.endpoint.dynamic.JaxWsDynamicClientFactory;

import javax.xml.namespace.QName;

/**
 * @Description
 * @Author: XuanXiangHui
 * @Date: 2019/12/27 下午2:24
 */
public class MyWebService {

    public static Object[] getService(String url,Object[] parameters, String namespace,String methodName) {
        try {
            JaxWsDynamicClientFactory dcf = JaxWsDynamicClientFactory.newInstance();
            Client client = dcf.createClient(url);
            QName name=new QName(namespace, methodName);
            Object[] params = new Object[] {parameters};
            return client.invoke(name,params);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static void main(String[] args) {
        String url = "http://10.4.111.49/ZRSMSoapServer/services/SMSoapService?wsdl";
        LogonInfo logonInfo = new LogonInfo();
        logonInfo.setUserName("");
        logonInfo.setPassWord("");
        Object[] parameters = new Object[]{logonInfo};
        String namespace = "http://10.4.111.49/ZRSMSoapServer/services/SMSoapService";
        String methodName = "logon";
        System.out.println(getService(url,parameters,namespace,methodName));
    }
}
