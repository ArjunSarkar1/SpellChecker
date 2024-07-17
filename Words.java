// This class is used to make a list of words and the lines they appear on, in the order the words appear
class Words
{
  /******************************************************************************
  Inner class
  *******************************************************************************/

  // The objects stored in this queue consist of the word and a pointer to a queue
  private class Node
  {
    public String  word;
    public Lines   lines;
    public Node    link;

    // the constructor takes a line number since we have one if we're adding the word
    Node( String new_word, int line, Node new_link )
    {
      word = new_word;

      // create the queue and insert the first line number
      lines = new Lines();
      lines.addLine( line );

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

  // standard constructor
  Words()
  {
    rear = null;
  }

  // We have an insert routine which will look for the word and add it if not found.
  // We queue the words so that they will come out in the order they appeared. 
  // If the word is found it will simply enqueue the line number.
  public void addWord( String word, int line )
  {
    Node currWord = null;
    boolean found = false;
    Node next = null;

    // make sure the queue isn't empty
    if ( !isEmpty() )
    {

      currWord = rear;
      next = rear.link;
      
      // we have a special case with rear so check it first
      if ( currWord.word.equals( word ) )
        found = true;

      // first, search the queue for the word
      while ( next != rear && !found )
      {
        currWord = next;

        if ( currWord.word.equals( word ) )
          found = true;
        else
          next = next.link;
      }
    }

    // if it's not there, add it to the queue
    if ( !found )
    {
      enter( word, line );
    }

    // otherwise, add the line to the word's queue
    else
    {
      currWord.lines.addLine( line );
    }
  }

  // we can print all of their words and the lines they appear on
  public void print()
  {
    Node nextWord;

    // iterate through the queue and print the word and all its line numbers
    while ( !isEmpty() )
    {
      nextWord = leave();

      System.out.print( "Invalid word \"" + nextWord.word + "\" found on lines " );

      // get all the line numbers for the word and print them
      while ( !nextWord.lines.isEmpty() )
        System.out.print( nextWord.lines.removeLine() + " " );

      System.out.println();
    }
  }
  
  /******************************************************************************
  Private Methods
  *******************************************************************************/

  // the outside world doesn't need to know this is a queue!
  private void enter( String word, int line )
  {
    Node new_node = new Node( word, line, null );
    
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

  private Node leave()
  {
    Node front = null;
    
    // only process if there's something in the queue
    if ( rear != null )
    {
      front = rear.link;

      // if there's only 1 element then we have to set rear to null
      if ( rear.link == rear )
        rear = null;
      else
        rear.link = rear.link.link;
    }

    return front;
  }

  private boolean isEmpty()
  {
    return rear==null;
  }
  
}
