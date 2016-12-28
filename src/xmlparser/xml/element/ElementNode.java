package xmlparser.xml.element;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

/**
 * ElementNode is the storage unit for each XML node. When using ElementNode, 
 * one should be aware of the mutability of the child nodes to avoid unintended deletion / mutation.
 * 
 * @author Man Chon Kuok
 */
public class ElementNode {
	
	private final List<ElementNode> childNodes;
	private final String data;
	private final String tag;
	
	/**
	 * Creates a new instance of ElementNode with no child nodes attached
	 * @param tag the tag for this node
	 * @param data the data for this node
	 */
	public ElementNode(String tag, String data) {
		this(tag, data, new LinkedList<ElementNode>());
	}
	
	/**
	 * Creates a new instance of ElementNode.
   * @param tag the tag for this node
   * @param data the data for this node
	 * @param childNodes the list of child nodes for this node
	 */
	public ElementNode(String tag, String data, List<ElementNode> childNodes) {
		this.tag = tag;
		this.data = data.trim();
		this.childNodes = childNodes;
	}

	/**
	 * Returns a mutable ordered list of child nodes
	 * @return a mutable ordered list of child nodes
	 */
	public List<ElementNode> getChildNodes() {
		return childNodes;
	}
	
	/**
	 * Add a child node to this node to the end of the list
	 * @param childNode a child node to be added to this node
	 * @return the added child node
	 */
	public ElementNode addChildNode(ElementNode childNode) {
		return this.addChildNodes(Arrays.asList(childNode)).get(0);
	}
	
	/**
	 * Add a list of child nodes to this node
	 * @param childNodes a list of child nodes to be added
	 * @return the added list of child nodes
	 */
	public List<ElementNode> addChildNodes(List<ElementNode> childNodes) {
		this.childNodes.addAll(childNodes);
		return childNodes;
	}
	
	/**
	 * Get the tag for this node 
	 * @return the tag
	 */
	public String getTag() {
		return tag;
	}
	
	/**
	 * Get the data for this node 
	 * @return the data
	 */
	public String getData() {
		return data;
	}

	/**
   * {@inheritDoc} 
   */
  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = prime * result + ((childNodes == null) ? 0 : childNodes.hashCode());
    result = prime * result + ((data == null) ? 0 : data.hashCode());
    result = prime * result + ((tag == null) ? 0 : tag.hashCode());
    return result;
  }

  /**
   * {@inheritDoc} 
   */
  @Override
  public boolean equals(Object obj) {
    if (this == obj)
      return true;
    if (obj == null)
      return false;
    if (getClass() != obj.getClass())
      return false;
    ElementNode other = (ElementNode) obj;
    if (childNodes == null) {
      if (other.childNodes != null)
        return false;
    }
    else if (!childNodes.equals(other.childNodes))
      return false;
    if (data == null) {
      if (other.data != null)
        return false;
    }
    else if (!data.equals(other.data))
      return false;
    if (tag == null) {
      if (other.tag != null)
        return false;
    }
    else if (!tag.equals(other.tag))
      return false;
    return true;
  }

  /**
	 * {@inheritDoc}
	 */
	public String toString() {
		StringBuilder childNodesContent = new StringBuilder();
		this.childNodes.stream().forEach(node -> childNodesContent.append(node.toString()));
		return String.format("<%s>%s%s</%s>", this.tag, this.data, childNodesContent, this.tag);
	}
	
}
