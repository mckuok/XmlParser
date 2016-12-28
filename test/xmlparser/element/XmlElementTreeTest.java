package xmlparser.element;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import java.util.Arrays;
import java.util.List;
import org.junit.Test;
import xmlparser.xml.element.ElementNode;
import xmlparser.xml.element.XmlElementTree;

/**
 * Unit tests for searching and build the XmlElementTree
 * @author Man Chon Kuok
 */
public class XmlElementTreeTest {

  private static final String TAG1 = "tag1";
  private static final String TAG2 = "tag2";

  @Test
  public void baseSearchTest() {
    ElementNode root = new ElementNode("root", "root data");
    ElementNode child1 = new ElementNode(TAG1, "child1 data");
    ElementNode child2 = new ElementNode(TAG1, "child2 data");
    ElementNode child3 = new ElementNode(TAG1, "child3 data");
    List<ElementNode> children = Arrays.asList(child1, child2, child3);
    root.addChildNodes(children);

    XmlElementTree tree = new XmlElementTree(root);
    List<ElementNode> searchResult = tree.getNodes(TAG1);
    assertEquals(children, searchResult);

    searchResult = tree.getNodes("inexistent");
    assertTrue(searchResult.isEmpty());

    ElementNode name1 = new ElementNode(TAG2, "name1");
    ElementNode name2 = new ElementNode(TAG2, "name2");

    name1.addChildNode(name2);

    child1.addChildNode(name1);
    child1.addChildNode(name2);
    child2.addChildNode(name2);
    searchResult = tree.getNodes(TAG2);
    assertEquals(Arrays.asList(name1, name2, name2), searchResult);

  }

  @Test
  public void toStringTest() {
    ElementNode root = new ElementNode("root", "root data");
    ElementNode child1 = new ElementNode(TAG1, "child1 data");
    ElementNode child2 = new ElementNode(TAG1, "child2 data");
    ElementNode child3 = new ElementNode(TAG1, "child3 data");
    List<ElementNode> children = Arrays.asList(child1, child2, child3);
    root.addChildNodes(children);

    XmlElementTree tree = new XmlElementTree(root);
    String expectedXml = String.format(
      "<root>root data<%s>child1 data</%s><%s>child2 data</%s><%s>child3 data</%s></root>",
      TAG1, TAG1, TAG1, TAG1, TAG1, TAG1);
    assertEquals(expectedXml, tree.toString());

    tree.appendChild(child2, new ElementNode(TAG2, "grandchild data"));
    expectedXml = String
      .format(
        "<root>root data<%s>child1 data</%s><%s>child2 data<%s>grandchild data</%s></%s><%s>child3 data</%s></root>",
        TAG1, TAG1, TAG1, TAG2, TAG2, TAG1, TAG1, TAG1);
    assertEquals(expectedXml, tree.toString());
  }

}
