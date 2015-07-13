package core.TestVee2.RequestTesting;

import com.google.common.base.Charsets;
import com.google.common.io.Files;
import core.TestVee2.Page;
import org.w3c.dom.CharacterData;
import org.w3c.dom.*;
import org.xml.sax.InputSource;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.File;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.Collections;

public class XmlUtils extends Page
{
    
    /**
     * Loads provided XML content into Document
     * @param xmlPath
     * @return null in case of exception
     */
    public static Document loadXml(String xmlPath)
    {
        try
        {
            String fileContent = Files.toString(new File(xmlPath), Charsets.UTF_8);
            DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
            DocumentBuilder db = dbf.newDocumentBuilder();
            InputSource is = new InputSource();
            is.setEncoding("UTF-8");
            is.setCharacterStream(new StringReader(fileContent));
            Document doc = db.parse(is);
            return doc;
        }
        catch (Exception e)
        {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Loads provided String content into Document
     * @param aString
     * @return null in case of exception
     */
    public static Document loadString(String aString)
    {
        try
        {
            DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
            DocumentBuilder db = dbf.newDocumentBuilder();
            InputSource is = new InputSource();
            is.setEncoding("UTF-8");
            is.setCharacterStream(new StringReader(aString));
            Document doc = db.parse(is);
            return doc;
        }
        catch (Exception e)
        {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Gets data from an xml element node
     * @param e
     * @param returnOnNull
     * @return
     */
    public static String getDataFromElement(Element e, String... returnOnNull)
    {
        assert returnOnNull.length <= 1;
        String nullValue = returnOnNull.length > 0 ? returnOnNull[0] : "?";

        Node child = e.getFirstChild();
        if (child instanceof CharacterData)
        {
            CharacterData cd = (CharacterData) child;
            return cd.getData();
        }
        return nullValue;
    }

    /**
     * Returns all duplicated node values for a specific node tag
     * @param pathToXml
     * @param nodeTag
     * @return
     */
    public static ArrayList<Node> getDuplicatedValuesForNodeType(String pathToXml, String nodeTag)
    {
        ArrayList<String> tmpList = new ArrayList<String>();
        ArrayList<Node> resList = new ArrayList<Node>();
        String tmpvalue = null;
        int occurrences = 0;
        try
        {
            Document doc = loadXml(pathToXml);

            NodeList allFNodes = doc.getElementsByTagName(nodeTag);
            // get all values into a list to be compared
            for (int i = 0; i < allFNodes.getLength(); i++)
            {
                tmpvalue = getDataFromElement((Element) allFNodes.item(i));
                if (!tmpvalue.equals("?"))
                {
                    tmpList.add(tmpvalue);
                }
            }
            for (int i = 0; i < allFNodes.getLength(); i++)
            {
                tmpvalue = getDataFromElement((Element) allFNodes.item(i));
                occurrences = Collections.frequency(tmpList, tmpvalue);
                if (occurrences > 1)
                {
                    resList.add(allFNodes.item(i));
                }
            }
            return resList;
        }
        catch (Exception e)
        {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Returns an array of strings representing values for provided attribute,
     * all nodes
     * @param pathToXml
     * @param atrName
     * @return
     */
    public static ArrayList<String> getAllValuesForAttribute(String pathToXml, String atrName)
    {
        ArrayList<String> filteredNodesV = new ArrayList<String>();
        try
        {
            Document doc = loadXml(pathToXml);
            NodeList allNodes = doc.getElementsByTagName("*");
            for (int i = 0; i < allNodes.getLength(); i++)
            {
                Element element = (Element) allNodes.item(i);
                if (element.hasAttribute(atrName))
                {
                    filteredNodesV.add(element.getAttribute(atrName));
                }
            }
            return filteredNodesV;
        }
        catch (Exception e)
        {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Returns an array of strings representing all values for provided
     * attribute prefixed by 2 levels of parents, all nodes (e.g.:
     * parenetNodeAtrValue.childNodeAtrValue.childNodeAtrValue)
     * @param xsdDoc
     * @param atrName
     * @return
     */
    public static ArrayList<String> getAllValuesWithParentForAttribute(Document xsdDoc, String atrName)
    {
        try
        {
            NodeList allNodes = xsdDoc.getElementsByTagName("*");
            return getValuesWithParentForAttribute(atrName, allNodes);
        }
        catch (Exception e)
        {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Returns an array of strings representing filtered values for provided
     * attribute prefixed by 2 levels of parents, based on a parent node (e.g.:
     * parenetNodeAtrValue.childNodeAtrValue.childNodeAtrValue)
     * @param parent
     * @param atrName
     * @return
     */
    protected static ArrayList<String> getFilteredValuesWithParentForAttribute(Element parent, String atrName)
    {
        try
        {
            NodeList childNodes = parent.getElementsByTagName("*");
            return getValuesWithParentForAttribute(atrName, childNodes);
        }
        catch (Exception e)
        {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Returns an array of strings representing values for provided attribute
     * prefixed by 2 levels of parents, extracted from a provided list of nodes
     * (e.g.: parenetNodeAtrValue.childNodeAtrValue.childNodeAtrValue)
     * @param atrName
     * @param nodes
     * @return
     */
    private static ArrayList<String> getValuesWithParentForAttribute(String atrName, NodeList nodes)
    {
        ArrayList<String> filteredNodesV = new ArrayList<String>();
        for (int j = 0; j < nodes.getLength(); j++)
        {
            Element childE = (Element) nodes.item(j);
            if (childE.hasAttribute(atrName))
            {
                filteredNodesV.add(getXsdParentsAtrValue(childE, atrName) + childE.getAttribute(atrName));
            }
        }
        return filteredNodesV;
    }

    /**
     * Returns parent attribute value for a specific element (2 levels of
     * parents)
     * @param element
     * @param attr
     * @return
     */
    protected static String getXsdParentsAtrValue(Element element, String attr)
    {
        Node tmpElement = null;
        String tmpRes = "";
        try
        {
            for (int i = 1; i < 3; i++)
            {
                if (i == 1)
                {
                    tmpElement = element.getParentNode();
                }
                else
                {
                    tmpElement = tmpElement.getParentNode();
                }
                if (tmpElement.getNodeName().equals("xs:schema"))
                {
                    if (i == 1)
                    {
                        tmpRes = "..";
                    }
                    else
                    {
                        tmpRes = "." + tmpRes;
                    }
                    break;
                }
                while ((tmpElement.getNodeType() != Node.ELEMENT_NODE) || ((tmpElement.getNodeType() != Node.DOCUMENT_NODE) && !((Element) tmpElement).hasAttribute(attr)))
                {
                    if (tmpElement.getNodeName().equals("xs:schema"))
                    {
                        break;
                    }
                    tmpElement = tmpElement.getParentNode();
                }
                tmpRes = ((Element) tmpElement).getAttribute(attr) + "." + tmpRes;
            }
            return tmpRes;
        }
        catch (Exception e)
        {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Returns an element parent node with "name" attribute based on a complex
     * key filter (parent.child.child)
     * @param xsdDoc
     * @param xsdKey
     * @return
     */
    protected static Element getXsdParentComplexKey(Document xsdDoc, String xsdKey)
    {
        Element tmpRes = null;
        Element element = null;

        String[] splitStr = xsdKey.split("[.]");

        String topParent = splitStr[0];
        String Parent = splitStr[1];
        String Node = splitStr[2];

        try
        {
            NodeList allNodes = xsdDoc.getElementsByTagName("*");
            for (int i = 0; i < allNodes.getLength(); i++)
            {
                element = (Element) allNodes.item(i);
                if (element.getAttribute("name").equals(Node))
                {
                    element = (Element) element.getParentNode();
                    while (!((Element) element).hasAttribute("name"))
                    {
                        element = (Element) element.getParentNode();
                    }
                    if (element.getAttribute("name").equals(Parent))
                    {
                        Element pElement = (Element) element.getParentNode();
                        while (!((Element) pElement).hasAttribute("name"))
                        {
                            pElement = (Element) pElement.getParentNode();
                        }
                        if (pElement.getAttribute("name").equals(topParent))
                        {
                            tmpRes = element;
                        }
                    }
                }
            }
            return tmpRes;
        }
        catch (Exception e)
        {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Returns an element parent node with "name" attribute based on a key
     * representing current node's "name" attribute value
     * @param xsdPath
     * @param xsdKey
     * @return
     */
    protected static Element getXsdParent(String xsdPath, String xsdKey)
    {
        Element tmpRes = null;
        try
        {
            Document doc = loadXml(xsdPath);
            NodeList allNodes = doc.getElementsByTagName("*");
            for (int i = 0; i < allNodes.getLength(); i++)
            {

                Element element = (Element) allNodes.item(i);
                if (element.hasAttribute("name"))
                {
                    if (element.getAttribute("name").equals(xsdKey))
                    {
                        element = (Element) element.getParentNode();
                        while (!((Element) element).hasAttribute("name"))
                        {
                            element = (Element) element.getParentNode();
                        }
                        tmpRes = element;
                    }
                }
            }
            return tmpRes;
        }
        catch (Exception e)
        {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Returns an element's sibling node with "name" attribute
     * @param parent
     * @return
     */
    protected static Element getXsdParentSibling(Element parent)
    {
        Element tmpRes = null;
        try
        {
            Element element = parent;
            element = getNextSiblingElement(element);
            while (!((Element) element).hasAttribute("name"))
            {
                element = getNextSiblingElement(element);
            }
            tmpRes = element;
            return tmpRes;
        }
        catch (Exception e)
        {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Returns next sibling element node
     * @param element
     * @return
     */
    protected static Element getNextSiblingElement(Element element)
    {
        try
        {
            Node tmpNode = null;
            tmpNode = element.getNextSibling();

            while (tmpNode.getNodeType() != Node.ELEMENT_NODE)
            {
				//fix for structure that has more than two parents null 
				if(tmpNode.getNextSibling()==null)
                    tmpNode = tmpNode.getParentNode();
                tmpNode = tmpNode.getNextSibling();
            }
            return (Element) tmpNode;
        }
        catch (Exception e)
        {
            e.printStackTrace();
            return null;
        }

    }

    /**
     * Returns previous sibling element node
     * @param element
     * @return
     */
    protected static Element getPreviousSiblingElement(Element element)
    {
        try
        {
            Node tmpNode = null;
            tmpNode = element.getPreviousSibling();

            while (tmpNode.getNodeType() != Node.ELEMENT_NODE)
            {
                tmpNode = tmpNode.getPreviousSibling();
            }

            return (Element) tmpNode;
        }
        catch (Exception e)
        {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Returns name for an attribute based on it's value
     * @param request
     * @param nodeName
     * @param attrValue
     * @return
     */
    protected static String getAttributeNameByValue(String request, String nodeName, String attrValue)
    {
        Element element = null;
        try
        {
            Document doc = loadString(request);

            if (nodeName.equals("#first"))
            {
                element = doc.getDocumentElement();

                NamedNodeMap attrList = element.getAttributes();
                for (int i = 0; i < attrList.getLength(); i++)
                {
                    if (element.getAttribute(attrList.item(i).getNodeName()).equals(attrValue))
                    {
                        return attrList.item(i).getNodeName();
                    }
                }
            }
            else
            {
                NodeList allNodes = doc.getElementsByTagName(nodeName);

                for (int i = 0; i < allNodes.getLength(); i++)
                {
                    element = (Element) allNodes.item(i);
                    NamedNodeMap attrList = element.getAttributes();
                    for (int j = 0; j < attrList.getLength(); j++)
                    {
                        if (element.getAttribute(attrList.item(j).getNodeName()).equals(attrValue))
                        {
                            return attrList.item(j).getNodeName();
                        }
                    }
                }
            }
            throw new Exception("The requested NameSpace not found");
        }
        catch (Exception e)
        {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Returns value for a specific attribute from a provided node
     * @param xmlPath
     * @param nodeName
     * @param attrName
     * @return
     */
    protected static String getAttributeValueByName(String xmlPath, String nodeName, String attrName)
    {
        Element element = null;
        try
        {
            Document doc = loadXml(xmlPath);
            NodeList allNodes = doc.getElementsByTagName(nodeName);

            if (allNodes.getLength() > 1)
            {
                throw new Exception("More then one node found that have the provided name");
            }
            else
            {
                element = (Element) allNodes.item(0);
                NamedNodeMap attrList = element.getAttributes();
                for (int i = 0; i < attrList.getLength(); i++)
                {
                    if (attrList.item(i).getNodeName().equals(attrName))
                    {
                        return element.getAttribute(attrList.item(i).getNodeName());
                    }
                }
            }
            return null;
        }
        catch (Exception e)
        {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Returns true if element node has children elements
     * @param actElement
     * @return
     */
    protected static boolean hasChildElementNodes(Element actElement)
    {
        try
        {
            if (actElement.hasChildNodes())
            {
                NodeList childNodes = actElement.getChildNodes();

                for (int i = 0; i < childNodes.getLength(); i++)
                {
                    Node element = childNodes.item(i);
                    if (element.getNodeType() == Node.ELEMENT_NODE)
                    {
                        return true;
                    }
                }
            }
            return false;
        }
        catch (Exception e)
        {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Returns the index of an element node from a provided list
     * @param actAllNodes
     * @param element
     * @param exceptionList - a list of indexes that will not be taken in count
     * @return -1 if node not found in the list
     */
    protected static int isNodePresent(NodeList actAllNodes, Element element, ArrayList<Integer> exceptionList)
    {
        int idx = -1;
        int tmpIdx = -1;
        try
        {
            String tmpElementName = element.getNodeName();

            for (int i = 0; i < actAllNodes.getLength(); i++)
            {
                if (actAllNodes.item(i).getNodeName().equals(tmpElementName))
                {
                    tmpIdx = i;
                    if (exceptionList.indexOf(tmpIdx) == -1)
                    {
                        idx = tmpIdx;
                        break;
                    }
                }
            }
            return idx;
        }
        catch (Exception e)
        {
            e.printStackTrace();
            return -1;
        }
    }

    /**
     * Adds namespace to all nodes in a Document
     * @param xmlPath
     * @param nameSpace
     */
    protected static void addNamespaceToAllXmlNodes(String xmlPath, String nameSpace)

    {
        Node element = null;
        try
        {
            Document doc = loadXml(xmlPath);
            NodeList allNodes = doc.getElementsByTagName("*");

            for (int i = 0; i < allNodes.getLength(); i++)
            {
                element = allNodes.item(i);
                doc.renameNode(element, "", nameSpace + element.getNodeName());
            }

            Transformer transformer = TransformerFactory.newInstance().newTransformer();
            transformer.setOutputProperty(OutputKeys.INDENT, "yes");
            DOMSource source = new DOMSource(doc);
            StreamResult result = new StreamResult(new File(xmlPath));

            transformer.transform(source, result);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }

    }
}
