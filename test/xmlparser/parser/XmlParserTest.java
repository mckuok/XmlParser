package xmlparser.parser;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;
import java.util.Arrays;
import org.junit.Test;
import xmlparser.parser.XmlParser.ParseException;
import xmlparser.xml.element.ElementNode;
import xmlparser.xml.element.XmlElementTree;

/**
 * Unit tests for XML Parser
 * @author Man Chon Kuok
 */
public class XmlParserTest {

  
  @Test
  public void xmlParsingTest() throws ParseException {
    String xml = "<a><b><c>letters</c></b></a>";
    parseAndAssert(xml, xml);
    
    xml = "<a><b><c>letters</c><c>numbers</c></b></a>";
    parseAndAssert(xml, xml);
    
    xml = "<a  ><b  ><c >letters</c ><c     >numbers</c ></b></a        >";
    ElementNode aNode = new ElementNode("a", "");
    ElementNode bNode = new ElementNode("b", "");
    ElementNode cNode1 = new ElementNode("c", "letters");
    ElementNode cNode2 = new ElementNode("c", "numbers");
    
    aNode.addChildNode(bNode);
    bNode.addChildNodes(Arrays.asList(cNode1, cNode2));
    XmlElementTree tree = new XmlElementTree(aNode);
    
    parseAndAssert(xml, tree);
    
    xml = "<a><b><c>letters</c><c>numbers</c></b> data for a </a>";
    aNode = new ElementNode("a", "data for a");
    aNode.addChildNode(bNode);
    tree = new XmlElementTree(aNode);    
    parseAndAssert(xml, tree);
    
    xml = "<a><b><c>letters</c><c>numbers</c><c/></b> data for a </a>";
    aNode = new ElementNode("a", "data for a");
    ElementNode cNode3 = new ElementNode("c", "");
    bNode.addChildNode(cNode3);
    aNode.addChildNode(bNode);
    tree = new XmlElementTree(aNode);    
    parseAndAssert(xml, tree);
    
  }
  
  @Test
  public void invalidXmlParsingTest() {
    String xml = "";
    parseAndExpectToFail(xml);
    
    xml = "<a></b>";
    parseAndExpectToFail(xml);
    
    xml = "<a><b></b>";
    parseAndExpectToFail(xml);
  }
  
  
  @Test
  public void xmlWithIdAttributesTest() throws ParseException {
    String xml = "<a id=\"1\"><b> content </b></a>";
    
    ElementNode aNode = new ElementNode("a", "");
    ElementNode bNode = new ElementNode("b", "content");    
    aNode.addChildNode(bNode);
    
    XmlElementTree tree = new XmlElementTree(aNode);
    parseAndAssert(xml, tree);
    
    xml = "<a id=\"1\" ref=\"2\"><b id=\"3\"> content </b></a>";
    parseAndAssert(xml, tree);
  }
  
  /**
   * Parse the input xml and assert if the output is the same as expected 
   * @param inputXml xml to be parsed
   * @param expectedXml xml result expected 
   * @throws ParseException thrown if parse runs into problems
   */
  private void parseAndAssert(String inputXml, String expectedXml) throws ParseException {
    XmlParser parser = new XmlParser(inputXml);
    XmlElementTree tree = parser.parseXml();
    assertEquals(expectedXml, tree.toString());
  }
  
  /**
   * Parse the input xml and assert if the output is the same as expected 
   * @param xml xml to be parsed
   * @param expectedTree the tree expected
   * @throws ParseException thrown if parse runs into problems
   */
  private void parseAndAssert(String xml, XmlElementTree expectedTree) throws ParseException {
    XmlParser parser = new XmlParser(xml);
    XmlElementTree tree = parser.parseXml();
    assertEquals(expectedTree, tree);
  }
  
  /**
   * Parse the input xml, expect the xml to be invalid
   * @param xml xml to be parsed
   */
  private void parseAndExpectToFail(String xml) {
    XmlParser parser = new XmlParser(xml);
    try {
      parser.parseXml();
      fail();
    }
    catch (ParseException e) {}
    
  }
  
}
