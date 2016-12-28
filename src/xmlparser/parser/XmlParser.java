package xmlparser.parser;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;
import xmlparser.xml.element.ElementNode;
import xmlparser.xml.element.XmlElementTree;

/**
 * A light weight XML File parser that parses XML content into a tree-like
 * structure. This parser is optimized to handle large file, with the maximum
 * size of 2GB. Attributes are accepted in the XML file, but are ignored for
 * simplicity. XML File should follow the standard set by w3 and can be found
 * here "https://www.w3.org/TR/xml/". Any invalid XML file might lead to
 * ParseException. This parser runs with O(n) for space and time, where n is the
 * number of characters in the xml file / string.
 * 
 * Known Problem: Possible StackOverFlowError caused by recursion when parsing
 * layers deep XML element node
 * @author Man Chon Kuok
 */
public class XmlParser {

  private static final int EOF_INDEX = -1;
  private final char[] xmlCharArray;

  /**
   * Creates a new instance of XmlParser.
   * @param xmlFile xmlFile to be parsed
   * @throws IOException thrown if file cannot be found
   */
  public XmlParser(File xmlFile) throws IOException {
    this(readFile(xmlFile));
  }

  /**
   * Creates a new instance of XmlParser.
   * @param xmlContent a XML formatted string to be parsed
   */
  public XmlParser(String xmlContent) {
    this.xmlCharArray = xmlContent.trim().toCharArray();
  }

  /**
   * Parses the XML content defined in the constructor, and return
   * a searchable XmlElementTree object
   * @return an XmlElementTree object that contains all the node from the XML Content
   * @throws ParseException thrown if the xml content is formatted incorrectly
   */
  public XmlElementTree parseXml() throws ParseException {
    ElementNodeParsedResult result = parseXml(0, "");
    if (result.elementNode == null) {
      throw new ParseException("Possible XML formatting error");
    }
    XmlElementTree tree = new XmlElementTree(result.elementNode);
    return tree;
  }

  /**
   * <p>Recursive function helper. Each recursive call is in the unit of an  node.
   * 
   * <p>Since this is a recursive call, for performance purpose it is meant to reduce the number of outside
   * method calls.
   *  
   * @param startIndex the start index of the char array, indicating the start iteration position of the array
   * @param openTag the open tag for the current node. 
   * @return a ElementNodeParsedResult object, containing the new start index and the created node
   * @throws ParseException thrown if the xml content is formatted incorrectly
   */
  private ElementNodeParsedResult parseXml(int startIndex, String openTag)
    throws ParseException {
    StringBuilder data = new StringBuilder();
    ElementNode topMostNode = null;
    List<ElementNode> childNodes = new LinkedList<>();
    int xmlLength = this.xmlCharArray.length;

    char c;
    for (int i = startIndex; i < xmlLength; i++) {
      c = this.xmlCharArray[i];

      // if it is a tag
      if (c == '<') {

        // get the tag
        StringBuilder tag = new StringBuilder();
        while ((c = this.xmlCharArray[++i]) != '>') {
          tag.append(c);
        }

        // ignore the tag if it is a property tag
        if (tag.charAt(0) != '?') {
          String tagString = tag.toString().trim();

          // remove any attributes
          int spaceIndex = tagString.indexOf(' ');
          if (!tagString.endsWith("/") && spaceIndex > -1) {
            tagString = tagString.substring(0, spaceIndex);
          }

          // if it is a matching closing tag
          if (tagString.equals("/" + openTag)) {
            ElementNode currentNode = new ElementNode(openTag, data.toString(), childNodes);
            return new ElementNodeParsedResult(currentNode, i);
          }

          // else if it is a self-closing tag, treat it as a tag
          // without data
          else if (tagString.endsWith("/")) {
            
            // removes the last '/', and ignore any attributes by removing anything after the first space 
            String strippedTag = tagString.substring(0, tagString.length() - 1);
            if (spaceIndex > -1) {
              strippedTag = strippedTag.substring(0, spaceIndex);
            }

            ElementNode currentNode = new ElementNode(strippedTag, "");
            childNodes.add(currentNode);

          }
          
          // then it must be another element node
          else {
            ElementNodeParsedResult result = parseXml(++i, tagString);
            i = result.udpatedIndex;

            //
            if (i == EOF_INDEX) {
              throw new ParseException("Possible XML formatting error, unmatched tag: "
                + openTag);
            }

            topMostNode = result.elementNode;
            childNodes.add(topMostNode);
          }
        }

      }
      // then it must be part of the data segment
      else {
        data.append(c);
      }

    }
    return new ElementNodeParsedResult(topMostNode, EOF_INDEX);
  }

  /**
   * Read the file into a String
   * @param file file to be read
   * @return String containing file's content, with new line characters removed
   * @throws IOException thrown if file does not exist
   */
  private static String readFile(File file) throws IOException {
    StringBuilder fileContent = new StringBuilder();
    BufferedReader reader = new BufferedReader(new FileReader(file));
    String line;
    while ((line = reader.readLine()) != null) {
      fileContent.append(line);
    }
    reader.close();
    return fileContent.toString();
  }

  /**
   * Wrapper to stores the new index and the created node.
   * @author Man Chon Kuok
   */
  private class ElementNodeParsedResult {

    private final ElementNode elementNode;
    private final int udpatedIndex;

    public ElementNodeParsedResult(ElementNode elementNode, int updatedIndex) {
      this.elementNode = elementNode;
      this.udpatedIndex = updatedIndex;
    }

    public String toString() {
      return this.elementNode.toString();
    }

  }

  /**
   * Custom Exception for the indication of parsing error
   * @author Man Chon Kuok
   */
  public static class ParseException extends Exception {

    private static final long serialVersionUID = 1426378157605142539L;

    public ParseException(String reason) {
      super(reason);
    }
  }

}
