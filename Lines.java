// This class is used to track line numbers, in order they are received.
public class Lines
{
  /******************************************************************************
  Inner class
  *******************************************************************************/

  private class Node
  {
    public int  line;
    public Node link;

    Node( int new_line, Node new_link )
    {
      line = new_line;
      link = new_link;
    }
  }

  /******************************************************************************
  Attributes
  *******************************************************************************/

  // we'll be using a circular linked list with a single pointer to the last element in the queue
  private Node rear;   // pointer to the tail of the queue
  
  /******************************************************************************
  Public Methods
  *******************************************************************************/

  // our constructor just uses the superclass
  Lines()
  {
    rear = null;
  }

  // we don't need to say that we're a queue, nor should we
  public void addLine( int line )
  {
    Node new_node = new Node( line, null );
    
    // we have to have special processing if the queue is empty
    if ( rear != null )
    {
      // our new node points to the front of the queue
      new_node.link = rear.link;
      
      // the current rear node has to point to our new node
      rear.link = new_node;
    }

    else
    {
      // since it's circular the element should point to itself
      // this makes getting the front node easier...
      new_node.link = new_node;
    }

    // our rear pointer always points to the new node
    rear = new_node;
  }

  // we don't need to say that we're a queue, nor should we
  public int removeLine()
  {
    int value = -1;

    // only process if there's something in the queue
    if ( rear != null )
    {
      value = rear.link.line;

      // if there's only 1 element then we have to set rear to null
      if ( rear.link == rear )
        rear = null;
      else
        rear.link = rear.link.link;
    }

    return value;
  }

  public boolean isEmpty()
  {
    return rear==null;
  }
}
