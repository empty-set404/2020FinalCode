package com.coder.mybatis.parsing;

import com.coder.mybatis.mapping.Environment;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathFactory;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class XMLConfigBuilder {

    private DocumentBuilderFactory documentBuilderFactory;
    private DocumentBuilder documentBuilder;
    private Document document;
    private Map<String, NodeList> nodeLists = new ConcurrentHashMap<String, NodeList>();

    public XMLConfigBuilder() {
        try {
            this.documentBuilderFactory = DocumentBuilderFactory.newInstance();
            this.documentBuilder = this.documentBuilderFactory.newDocumentBuilder();
        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        }
    }

    public XMLConfigBuilder load(InputStream inputStream) throws Exception {
        this.document = this.documentBuilder.parse(inputStream);
        document.getDocumentElement().normalize();
        return this;
    }

    // 解析xml
    public Environment parse() {
        Environment environment = new Environment();

        try {
            List<String> nameList = getList("//configuration//environment//dataSource//property", "name");
            List<String> valueList = getList("//configuration//environment//dataSource//property", "value");
            for (int i = 0; i < nameList.size(); i++) {
                if ("driver".equals(nameList.get(i))) {
                    environment.setDriver(valueList.get(i));
                }
                if ("url".equals(nameList.get(i))) {
                    environment.setUrl(valueList.get(i));
                }
                if ("username".equals(nameList.get(i))) {
                    environment.setUsername(valueList.get(i));
                }
                if ("password".equals(nameList.get(i))) {
                    environment.setPassword(valueList.get(i));
                }
            }
        }catch (Exception e) {
            e.printStackTrace();
        }
        return environment;
    }

    public static List<String> get(NodeList nodeList, String attributeName) {
        List<String> list = new ArrayList<String>();
        for (int i = 0; i < nodeList.getLength(); i++) {
            Node item = nodeList.item(i);
            Node namedItem = item.getAttributes().getNamedItem(attributeName);
            if (namedItem != null) {
                String nodeValue = namedItem.getNodeValue();
                list.add(nodeValue);
            }
        }
        return list;
    }

    public List<String> getList(String expression, String attributeName) throws Exception {
        XPath xPath = XPathFactory.newInstance().newXPath();
        NodeList nodeList = (NodeList) xPath.compile(expression).evaluate(document, XPathConstants.NODESET);
        return get(nodeList, attributeName);
    }

    public String getOne(String expression, String attributeName) throws Exception {
        List<String> result = getList(expression, attributeName);
        if (result == null || result.size() == 0) {
            return null;
        }
        return String.valueOf(result.get(0));
    }

}
