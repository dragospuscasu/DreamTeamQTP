package core.TestVee2.RequestTesting;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

import java.io.*;
import java.util.ArrayList;

public class TestGenerationUtils
{
    static String resourcePath = core.TestVee2.RequestTesting.TestGenerationUtils.class.getClassLoader().getResource(".").getPath();
    //static String xsdPath      = resourcePath + "/resources/testdata/requestTests/xsdFile.xsd";
    static String xsdPath      = resourcePath + "/resources/testdata/requestTests/xsdFile2.xsd";
    /**
     * utility to generate templates for map.roperties and refDataMap.txt
     */
    public static void main()
    {
		String inputRefData = resourcePath+"//resources//testdata//requestTests//" + "NEW_RefData.xml";
        String outputRefData = resourcePath+"//resources//testdata//requestTests//"+ "refDataMap3.txt";
        String outputMap = resourcePath+"//resources//testdata//requestTests//"+"map2.properties";
        
		TestGenerationUtils.generateRefDataMap(inputRefData, "Decode", outputRefData);
        TestGenerationUtils.generateMapPropFile(xsdPath, outputMap);
    }

    /**
     * Utility for refDataMap.txt generation. Extracts all duplicate values for
     * nodeTag nodes and places them in outputPath as keys having RefData
     * Category as value
     * @param pathToRefData
     * @param nodeTag
     * @param outputPath
     */
    public static void generateRefDataMap(String pathToRefData, String nodeTag, String outputPath)
    {
        Node catNode = null;
        BufferedWriter writer = null;
        try
        {
            writer = new BufferedWriter(new FileWriter(outputPath));
            writer.write("# <" + nodeTag + "> node = refData Category value ");
            writer.newLine();
            // get duplicated values
            ArrayList<Node> dupNodes = XmlUtils.getDuplicatedValuesForNodeType(pathToRefData, nodeTag);

            for (Node dupNode : dupNodes) {
                catNode = dupNode;
                while (!catNode.getNodeName().equals("DataItemList"))
                {
                    catNode = catNode.getParentNode();
                }
                catNode = catNode.getPreviousSibling();
                while (catNode != null && !(catNode instanceof Element))
                {
                    catNode = catNode.getPreviousSibling();
                }
                writer.write(XmlUtils.getDataFromElement((Element) dupNode) + "=" + XmlUtils.getDataFromElement((Element) catNode));
                writer.newLine();
            }

        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
        finally
        {
            try
            {
                writer.close();
            }
            catch (IOException e)
            {
                e.printStackTrace();
            }
        }

    }

    /**
     * Utility for map.roperties template generation. Gets all values for
     * attribute "name" from xsd, adds 2 level of parents and writes them as
     * keys in outputPath
     * @param xsdPath
     * @param outputPath
     */
    public static void generateMapPropFile(String xsdPath, String outputPath)
    {
        OutputStream out = null;
        try
        {

            Document xsdDoc = XmlUtils.loadXml(xsdPath);
            ArrayList<String> reqKeys;

            LinkedProperties props = new LinkedProperties();
            out = new FileOutputStream(new File(outputPath));

            reqKeys = XmlUtils.getAllValuesWithParentForAttribute(xsdDoc, "name");

            for (int i = 0; i < reqKeys.size(); i++)
            {
                props.setProperty(reqKeys.get(i), "");
            }

            props.store(out, null);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        finally
        {
            try
            {
                out.close();
            }
            catch (IOException e)
            {
                e.printStackTrace();
            }
        }

    }
}
