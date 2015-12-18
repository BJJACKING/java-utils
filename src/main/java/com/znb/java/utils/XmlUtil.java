package com.znb.java.utils;

import net.sf.json.JSON;
import net.sf.json.JSONObject;
import net.sf.json.xml.XMLSerializer;
import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;

/**
 * @author zhangnaibin
 * @Date 2015-12-18 上午11:02
 */
public class XmlUtil {
    protected static final Logger LOG = LoggerFactory.getLogger(XmlUtil.class);

    /**
     * 将xml格式的InputStream转换成json格式的返回
     * @param in
     * @return
     */
    public static JSONObject convertXMLtoJSON(InputStream in) {
        JSONObject jsonObject = new JSONObject();
        try {
            String xml;
            xml = IOUtils.toString(in);
            XMLSerializer xmlSerializer = new XMLSerializer();
            JSON json = xmlSerializer.read(xml);
            jsonObject = JSONObject.fromObject(json);
            in.close();
        } catch (IOException e) {
            LOG.error("xml parase exception!", e);
        }
        return jsonObject;
    }

    /**
     * 将xml转换为json
     * @param xml
     * @return
     */
    public static JSONObject convertXMLtoJSON(String xml) {
        JSONObject jsonObject = new JSONObject(true);
        try {
            XMLSerializer xmlSerializer = new XMLSerializer();
            JSON json = xmlSerializer.read(xml);
            jsonObject = JSONObject.fromObject(json);
        } catch (Exception e) {
            LOG.error("xml parase exception!", e);
        }
        return jsonObject;
    }

    /**
     * 将json转为xml
     * @param rootName xml的父元素
     * @param object
     * @return
     */
    public static String toXMLString(String rootName, JSONObject object){
        XMLSerializer xmlSerializer = new XMLSerializer();
        xmlSerializer.clearNamespaces();
        xmlSerializer.setForceTopLevelObject(false);
        xmlSerializer.setTypeHintsEnabled(false);
        xmlSerializer.setObjectName(rootName);
        return xmlSerializer.write(object, "UTF-8");
    }
}
