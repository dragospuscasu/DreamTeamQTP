package core.TestVee2.RequestTesting;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Properties;

public class RequestUtils extends XmlUtils
{
    static String resourcePath  = core.TestVee2.RequestTesting.RequestUtils.class.getClassLoader().getResource(".").getPath();
//    static String pathToRefData = resourcePath + "/resources/testdata/requestTests/RefData.xml";
	static String pathToRefData = resourcePath + "/resources/testdata/requestTests/NEW_RefData.xml";
    static String refMapPath    = resourcePath + "resources/testdata/requestTests/refDataMap.properties";

    /**
     * Main method for request testing. Based on provided xsd file, input Data
     * file, request/input Map and a location for generated request, method
     * generates an expected request then compares it with the actual one.
     * @param actRequest
     * @param xsdLoc
     * @param inputDataLoc
     * @param requestMapLoc
     * @param generatedRequestLoc
     */
    public static void generateAndCompareRequest(String actRequest, String xsdLoc, String inputDataLoc, String requestMapLoc, String generatedRequestLoc)
    {
        logger.info("Request testing - Starting Request verification.");

        // generate expected request content
        RequestUtils.generateExpRequestXml(xsdLoc, inputDataLoc, requestMapLoc, generatedRequestLoc);

        // append NameSpace to actual request
        RequestUtils.addNamespace(xsdLoc, actRequest, generatedRequestLoc);

        // compare expected vs actual request
        RequestUtils.compareExpActRequest(generatedRequestLoc, actRequest);
    }

    /**
     * Adds namespace to xml nodes for the compliance with request node names
     * @param xsdPath
     * @param actReq
     * @param genReqPath
     */
    public static void addNamespace(String xsdPath, String actReq, String genReqPath)
    {
        logger.info("Request testing - Adding namespace to request nodes.");

        String nameSpace = getReqNameSpace(xsdPath, actReq);
        String[] sepNameSpace = nameSpace.split("[:]");
        nameSpace = sepNameSpace[1];
        nameSpace = nameSpace + ":";

        addNamespaceToAllXmlNodes(genReqPath, nameSpace);

    }

    /**
     * Extracts namespace from actual request based on xml schema
     * @param xsdPath
     * @param actReq
     * @return
     */
    private static String getReqNameSpace(String xsdPath, String actReq)
    {
        String nodeName = "xs:schema";
        String attrName = "targetNamespace";
        String attrValue = null;

        try
        {
            attrValue = getAttributeValueByName(xsdPath, nodeName, attrName);
            return getAttributeNameByValue(actReq, "#first", attrValue);
        }
        catch (Exception e)
        {
            e.printStackTrace();
            return null;
        }
    }

	/**
     * Case for multiple values when same node appears multiple times
	 * Adds for each node (key) the new value in its corresponding list
     * @param comValueList - the list that will hold the nodes and their list of values
     * @param comValues - the values that need to be added in the list of comNode
     * @param comNode - the node to which the values should be added
	 * @return
     */
	private static void createComValuesForNode(HashMap<String, ArrayList<String>> comValueList, String[] comValues, String comNode)
    {
        ArrayList<String> comValuesForNode = new ArrayList<>();
        for (String comValue : comValues) {
            comValuesForNode.add(comValue);
        }
        
        if(comValueList.containsKey(comNode))
        {
			comValueList.remove(comNode);
			comValueList.put(comNode, comValuesForNode);
		}
        else
        {
            comValueList.put(comNode, comValuesForNode);
        }
    }
    /**
     * Generates expected request, based on xml schema, input data and
     * request/input map
     * @param xsdPath
     * @param inPropsPath
     * @param mapPath
     * @param outputPath
     */
    public static void generateExpRequestXml(String xsdPath, String inPropsPath, String mapPath, String outputPath)
    {

        logger.info("Request testing - Generating expected request.");

        ArrayList<String> reqNodes;
        ArrayList<String> childNodes;

        Properties mapB = PropertiesUtils.loadProperties(mapPath);
        Properties inputB = PropertiesUtils.loadProperties(inPropsPath);

        Document xsdDoc = loadXml(xsdPath);

        String xsdKey;
        String xsdKeyV;
        String requestValue = "";

        Element nodeToFile = null;

        Element comXsdParent = null;
        Element comXsdPSibling = null;
        String comNode = null;
        String[] comValues = null;
		HashMap<String, ArrayList<String>> comValueList = new HashMap<>();

        try
        {
            // creating doc to be writen on disk
            DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
            Document doc = docBuilder.newDocument();

            // get all attribute "name" values
            reqNodes = getAllValuesWithParentForAttribute(xsdDoc, "name");

            // root node will be request name
            Element root = doc.createElement(getRNodeNameFromKey(reqNodes, 0));
            doc.appendChild(root);

            // construct node
            // first value was written as parent node
            for (int i = 1; i < reqNodes.size(); i++)
            {
                // full map key
                xsdKey = reqNodes.get(i);

                // special verification for multiple values
                if ((comXsdPSibling != null) && (comXsdPSibling.getAttribute("name").equals(getRNodeNameFromKey(reqNodes, i))))
                {
                    // prepare data for multiple values
                    childNodes = getFilteredValuesWithParentForAttribute(comXsdParent, "name");

                    // write the iteration of constructed structure first
                    // first value was already written
                    for (int n = 1; n < comValues.length; n++)
                    {

                        nodeToFile = doc.createElement(comXsdParent.getAttribute("name"));
                        requestValue = mapB.getProperty(getXsdParentsAtrValue(comXsdParent, "name") + comXsdParent.getAttribute("name"));
                        nodeToFile.appendChild(doc.createTextNode(requestValue));

                        root.appendChild(nodeToFile);

                        for (int p = 0; p < childNodes.size(); p++)
                        {
                            xsdKeyV = mapB.getProperty(childNodes.get(p));
                            if (xsdKeyV.length() > 0)
                            {
                                // get value for node
                             if(comValueList.containsKey(childNodes.get(p)))
                                {
                                    requestValue = getRequestValue(inputB, comValueList.get(childNodes.get(p)).get(n));

                                }
                             else
                                {
                                    requestValue = getRequestValue(inputB, xsdKeyV);
                                }      

                                // write node
                                nodeToFile = doc.createElement(getRNodeNameFromKey(childNodes, p));
                                nodeToFile.appendChild(doc.createTextNode(requestValue));
                            }
                            else
                            {
                                nodeToFile = doc.createElement(getRNodeNameFromKey(childNodes, p));
                            }
                            root.appendChild(nodeToFile);
                        }

                    }
                    comXsdPSibling = null;
					comValueList = new HashMap<>();
                }

                // regular case, for non-multiple values
                xsdKeyV = mapB.getProperty(xsdKey);
                nodeToFile = doc.createElement(getArrayElement(xsdKey, 2));
                if (xsdKeyV.length() > 0)
                {
                    requestValue = getRequestValue(inputB, xsdKeyV);

                    // special verification for multiple values (structure
                    // construction) NOTE: imbricated multiple values not
                    // supported
                    if (requestValue.contains("!!!"))
                    {
                        comValues = requestValue.split("!!!");
                        comNode = xsdKey;
						createComValuesForNode(comValueList, comValues, comNode);
                        comXsdParent = getXsdParentComplexKey(xsdDoc, xsdKey);
                        comXsdPSibling = getXsdParentSibling(comXsdParent);

                        nodeToFile.appendChild(doc.createTextNode(getRequestValue(inputB, comValues[0])));
                    }
                    else
                    {
                        nodeToFile.appendChild(doc.createTextNode(requestValue));
                    }
                }

                root.appendChild(nodeToFile);
            }

            // write the content into xml file
            Transformer transformer = TransformerFactory.newInstance().newTransformer();
            transformer.setOutputProperty(OutputKeys.INDENT, "yes");
            DOMSource source = new DOMSource(doc);
            StreamResult result = new StreamResult(new File(outputPath));

            transformer.transform(source, result);

        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    /**
     * Utility to extract a part of a string from a structure like
     * "parent.child.child"
     * @param complexString
     * @param i
     * @return
     */
    private static String getArrayElement(String complexString, int i)
    {
        String[] splitStr = complexString.split("[.]");
        return splitStr[i];

    }

    /**
     * Utility to extract a request node name from a list of elements like
     * "parent.child.child"
     * @param listOfElements
     * @param idx
     * @return
     */
    private static String getRNodeNameFromKey(ArrayList<String> listOfElements, int idx)
    {
        String tmpValue;
        String[] tmpRes;

        try
        {
            tmpValue = listOfElements.get(idx);
            tmpRes = tmpValue.split("[.]");

            return tmpRes[2];
        }
        catch (Exception e)
        {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Returns node value for request node, based on values in request/input map
     * @param properties
     * @param xsdKeyV
     * @return
     * @throws Exception
     */
    private static String getRequestValue(Properties properties, String xsdKeyV) throws Exception
    {
        String xsdKeyVT;
        String requestValue;
        xsdKeyVT = xsdKeyV.substring(1);

        // flags used in map
        String decodedFlag = "&";
        String codedFlag = "$";
        String calculatedFlag = "%";
        String multipleInputFlag = "~";

        // get 1st char from xsdKeyV and search in the correct file for value
        if (xsdKeyV.charAt(0) == decodedFlag.charAt(0))
        {
            // decoded values - get from input.properties
            requestValue = properties.getProperty(xsdKeyVT);
        }
        else if (xsdKeyV.charAt(0) == codedFlag.charAt(0))
        {
            // coded values - get from input.properties encoding using refData
            requestValue = getEncodedValue(pathToRefData, properties.getProperty(xsdKeyVT), getRefCategory(xsdKeyVT));
        }
        else if (xsdKeyV.charAt(0) == calculatedFlag.charAt(0))
        {
            // calculated value, based on pattern
            requestValue = calculateValue(properties, xsdKeyVT);
        }
        else if (xsdKeyV.charAt(0) == multipleInputFlag.charAt(0))
        {
            // multiple values - passing the whole string back
            requestValue = xsdKeyVT;
        }
        else
        {
            requestValue = xsdKeyV;
        }
        return requestValue;
    }

    /**
     * Calculates request value based on a pattern extracted from map value
     * @param properties
     * @param xsdKeyVT
     * @return
     * @throws Exception
     */
    private static String calculateValue(Properties properties, String xsdKeyVT) throws Exception
    {
        String requestValue;
        requestValue = "";
        int counter = 0;

        String[] tmpKeys = xsdKeyVT.split("!!");
        String[] keys = tmpKeys[0].split("[|]");
        String[] template = tmpKeys[1].split("[|]");

        for (String template1 : template) {
            if (template1.equals("xxx")) {
                counter++;
            }
        }
        if (keys.length != counter)
        {
            throw new Exception("Provided values do notmatch with the pattern: " + xsdKeyVT);
        }

        for (String key : keys) {
            String tmpValue = properties.getProperty(key);
            for (int l = 0; l < template.length; l++)
            {
                if (template[l].equals("xxx"))
                {
                    template[l] = tmpValue;
                    break;
                }
            }
        }

        for (String template1 : template) {
            requestValue = requestValue + template1;
        }
        return requestValue;
    }

    /**
     * Compares expected vs actual requests
     * @param genReqPath
     * @param actReq
     */
    public static void compareExpActRequest(String genReqPath, String actReq)
    {

        logger.info("Request testing - Comparing expected vs actual");

        NodeList actFNodes = null;
        ArrayList<Integer> exclusionIdxList = new ArrayList<>();
        int foundEIdx = -1;
        String expValue = null;
        ArrayList<String> errorList = new ArrayList<>();
        String errorMessage = null;

        try
        {
            // loads generated expected request
            Document gendoc = loadXml(genReqPath);
            NodeList genAllNodes = gendoc.getElementsByTagName("*");

            // loads actual request
            Document actdoc = loadString(actReq);
            NodeList actAllNodes = actdoc.getElementsByTagName("*");

            for (int i = 0; i < actAllNodes.getLength(); i++)
            {
                if (actAllNodes.item(i).getNodeName().equals(gendoc.getDocumentElement().getNodeName()))
                {
                    Element element = (Element) actAllNodes.item(i);
                    actFNodes = element.getElementsByTagName("*");
                    break;
                }
            }

            errorMessage = "-----------------  Differences found expected vs actual   -----------------";
            errorList.add(errorMessage);

            // verify expected -> actual
            for (int i = 1; i < genAllNodes.getLength(); i++)
            {
                Element expElement = (Element) genAllNodes.item(i);

                expValue = getDataFromElement(expElement, "");

                foundEIdx = isNodePresent(actFNodes, expElement, exclusionIdxList);

                if (foundEIdx != -1)
                {
                    Element actElement = (Element) actFNodes.item(foundEIdx);

                    if (expValue.equals("/"))
                    {
                        // in case expValue == "/" -> actual should have
                        // children
                        if (!hasChildElementNodes(actElement))
                        {
                            errorMessage = "Node: [" + expElement.getNodeName() + "] has no children, xsd structure is not valid.";
                            errorList.add(errorMessage);
                        }
                    }
                    else if (expValue.equals("^"))
                    {
                        // in case expValue == "^" -> actual should have empty
                        // value
                        String actValue = getDataFromElement(actElement, "");
                        if (!"".equals(actValue.trim()))
                        {
                            errorMessage = "Node: [" + expElement.getNodeName() + "] has incorrect value, expected: empty,  actual: [" + actValue + "].";
                            errorList.add(errorMessage);
                        }
                    }
                    else
                    {
                        String actValue = getDataFromElement(actElement, "");
                        if (!expValue.trim().equals(actValue.trim()))
                        {
                            errorMessage = "Node: [" + expElement.getNodeName() + "] has incorrect value, expected: [" + expValue + "], actual: [" + actValue + "].";
                            errorList.add(errorMessage);
                        }
                    }
                    // Add node idx to future exclusion list
                    exclusionIdxList.add(foundEIdx);
                }
                else
                {
                    if (!expValue.equals(""))
                    {
                        errorMessage = "Node: [" + expElement.getNodeName() + "] is missing from actual request, expected to be present and to have value: [" + expValue + "].";
                        errorList.add(errorMessage);
                    }
                }

            }
            // verify remaining actual
            errorList.addAll(getRemainingList(actFNodes, exclusionIdxList));

            // print result
            if (errorList.size() > 2)
            {
                logger.error("Request testing - FAILED - Differences found:");
                for (String errorList1 : errorList) {
                    logger.error(errorList1);
                }
            }
            else
            {
                logger.info("Request testing - Request verification successful, no differences found.");
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    /**
     * Returns a list of elements that are not in the exclusion list
     * @param actFNodes
     * @param exIdxList
     * @return
     */
    private static ArrayList<String> getRemainingList(NodeList actFNodes, ArrayList<Integer> exIdxList)
    {
        ArrayList<String> difList = new ArrayList<>();
        String difMessage = "-----------------  Unexpected elements found in actual request   -----------------";

        try
        {
            for (int i = 0; i < actFNodes.getLength(); i++)
            {
                if (exIdxList.indexOf(i) == -1)
                {
                    Element element = (Element) actFNodes.item(i);
                    if (difMessage != null)
                    {
                        difList.add(difMessage);
                    }
                    difMessage = "Node: [" + element.getNodeName() + "] with value [" + getDataFromElement(element).trim() + "] was not expected in actual request";
                    difList.add(difMessage);
                    difMessage = null;
                }
            }
            return difList;
        }
        catch (Exception e)
        {
            e.printStackTrace();
            difList.add("Unexpected errors, see log for more info");
            return difList;
        }
    }

    /**
     * Returns refData category for a specific element from a previously created
     * map, based on input keys
     * @param xsdKeyVT
     * @return
     */
    private static String getRefCategory(String xsdKeyVT)
    {
        try
        {
            Properties refMapB = PropertiesUtils.loadProperties(refMapPath);

            return refMapB.getProperty(xsdKeyVT);
        }
        catch (Exception e)
        {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Returns encoded value from refData for a provided decoded value and
     * category
     * @param pathToRefData
     * @param decodedValue
     * @param category
     * @return
     */
    public static String getEncodedValue(String pathToRefData, String decodedValue, String category)
    {
        try
        {
            String tmpRes = "";
            Document doc = loadXml(pathToRefData);

            if (category == null)
            {

                int counter = 0;
                // get all decoded nodes
                NodeList allNodes = doc.getElementsByTagName("Decode");
                for (int i = 1; i < allNodes.getLength(); i++)
                {
                    Element decodedElement = (Element) allNodes.item(i);
                    if (getDataFromElement(decodedElement).equals(decodedValue))
                    {
                        counter++;

                        // get <coded> value
                        Element codedElement = getPreviousSiblingElement(decodedElement);

                        if (codedElement.getNodeName().equals("Code"))
                        {
                            tmpRes = getDataFromElement(codedElement);
                        }
                    }
                }
                if (counter > 1)
                {
                    throw new Exception(Integer.toString(counter) + " nodes with the same decoded value were found and no Category was provided");
                }
                else
                {
                    return tmpRes;
                }
            }
            else
            {
                // get all category nodes
                NodeList allNodes = doc.getElementsByTagName("Category");
                for (int i = 1; i < allNodes.getLength(); i++)
                {
                    Element element = (Element) allNodes.item(i);
                    if (getDataFromElement(element).equals(category))
                    {
                        // get <DataItemList> node
                        element = getNextSiblingElement(element);

                        NodeList inCategoryNodes = element.getElementsByTagName("*");

                        for (int j = 0; j < inCategoryNodes.getLength(); j++)
                        {
                            Element inCatElement = (Element) inCategoryNodes.item(j);
                            if (getDataFromElement(inCatElement).equals(decodedValue))
                            {
                                // get <coded> value
                                Element codedElement = getPreviousSiblingElement(inCatElement);
                                if (codedElement.getNodeName().equals("Code"))
                                {
                                    tmpRes = getDataFromElement(codedElement);
                                }
                                break;
                            }
                        }
                        break;
                    }
                }
            }
            return tmpRes;
        }
        catch (Exception e)
        {
            e.printStackTrace();
            return "";
        }
    }
}
