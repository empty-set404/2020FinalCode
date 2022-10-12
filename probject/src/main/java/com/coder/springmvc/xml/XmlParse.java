package com.coder.springmvc.xml;

import org.dom4j.Attribute;
import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import java.io.InputStream;

public class XmlParse {

    public static String getBasePack(String xml) {
        try {
            SAXReader reader = new SAXReader();
            InputStream inputStream = XmlParse.class.getClassLoader().getResourceAsStream(xml);

            // xml 文档对象
            Document document = reader.read(inputStream);
            Element rootElement = document.getRootElement();
            Element componentScan = rootElement.element("component-scan");
            Attribute attribute = componentScan.attribute("base-package");
            String basePackage = attribute.getText();
            return basePackage;
        }catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }

}
