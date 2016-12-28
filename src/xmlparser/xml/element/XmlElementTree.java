package xmlparser.xml.element;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/**
 * Tree-like structure to store the layers of XML nodes
 * @author Man Chon Kuok
 */
public class XmlElementTree {

	private final ElementNode rootNode;
	
	/**
	 * Creates a new instance of XmlElementTree.
	 * @param rootNode root of this xml tree
	 */
	public XmlElementTree(ElementNode rootNode) {
	  if (rootNode == null) {
	    throw new IllegalArgumentException("Root cannot be null");
	  }
		this.rootNode = rootNode;
	}
	
	/**
	 * Returns the root of the tree
	 * @return the root of this tree
	 */
	public ElementNode getRoot() {
		return this.rootNode;
	}
	
	/**
	 * BFS to get the list of top-most layer of nodes that matches with the provided tag name
	 * @param tag the name of the tag to be searched
	 * @return a list of ElementNode that matches with the given tag
	 */
	public List<ElementNode> getNodes(String tag) {
	   if (tag == null) {
	      throw new IllegalArgumentException("Tag cannot be null");
	    }
	   
		List<ElementNode> matchingNodes = new ArrayList<>();
		LinkedList<ElementNode> nextChildQueue = new LinkedList<>();
		
		if (this.rootNode.getTag().equals(tag)) {
			matchingNodes.add(this.rootNode);
			return matchingNodes;
			
		} else {
			ElementNode currentNode = this.rootNode;
			
			// BFS-like search
			while (currentNode != null) {
				List<ElementNode> childNodes = currentNode.getChildNodes(); 
				for (ElementNode child : childNodes) {
					if (child.getTag().equals(tag)) {
						matchingNodes.add(child);
					} else {
						nextChildQueue.offer(child);
					}
				}
				
				currentNode = nextChildQueue.poll();
			}
			
			return matchingNodes;
		}
	}
	
	/**
	 * Append the child to the parent
	 * @param parent the node parent
	 * @param child the child to be added to the parent
	 * @return the added child
	 */
	public ElementNode appendChild(ElementNode parent, ElementNode child) {
	  if (parent == null) {
	    throw new IllegalArgumentException("Parent cannot be null");
	  }
	  
	  if (child == null) { 
	    throw new IllegalArgumentException("Child cannot be null");
	  }
	  
	  return parent.addChildNode(child);
	}
	
	/**
   * {@inheritDoc} 
   */
  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = prime * result + ((rootNode == null) ? 0 : rootNode.hashCode());
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
    XmlElementTree other = (XmlElementTree) obj;
    if (rootNode == null) {
      if (other.rootNode != null)
        return false;
    }
    else if (!rootNode.equals(other.rootNode))
      return false;
    return true;
  }

  /**
	 * {@inheritDoc}
	 */
	public String toString() {
		return this.rootNode.toString();
	}
	
	
}
