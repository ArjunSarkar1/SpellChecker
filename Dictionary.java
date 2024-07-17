/**
 * --------------------------------------------------------------
 * NAME: Arjun Sarkar 
 * STUDENT NUMBER: 007935095
 * COURSE: COMP 2140, SECTION: A02
 * INSTRUCTOR: Michael Zapp
 * ASSIGNMENT: Assignment #5, QUESTION: Question #1
 * REMARKS: What is the purpose of this code?
 * The main purpose of this program is to implement a 2-3 Tree
 * with the main functionalities of insert() and lookup(). Each 
 * Node in the tree holds at most two words(small word and big word)
 * --------------------------------------------------------------
 */
import java.io.*;
import java.util.*;

class Dictionary {

  /******************************************************************************
  Inner class
  *******************************************************************************/

  /**
   * A private Node class to hold essential parts such as 
   * references to its parent, children(right, left, mid) and
   * containing the at most two words. These words are categorized 
   * in lexical order (smallWord and bigWord).
   */
  private class Node {    

    // NODE CLASS INSTANCES
    Node parent;
    Node left;
    Node mid;
    Node right;
    String smallWord;
    String bigWord;

    public Node(String newWord, Node parent) {
      this.parent = parent;
      smallWord = newWord;
      bigWord = "";
    } // Node Constructor

  }
  /******************************************************************************
  Attribute
  *******************************************************************************/
  private static Node treeRoot; // A node to hold the root of the Tree

  /******************************************************************************
  Public Methods
  *******************************************************************************/

  Dictionary() {
    treeRoot = null;

  } // Dictionary Constructor

  /** 
   * Our Dictionary constructor will read in the file and insert each word 
   * into the 2-3 Tree in lower case.
  */
  Dictionary(String fileName ) {
   
    String read_word;

    // read the words into the table
    try {
      Scanner theFile = new Scanner(new File(fileName));

      System.out.print( "Loading dictionary" );
      
      Dictionary twoThreeTree = new Dictionary();

      // each line contains 1 word...
      while ( theFile.hasNextLine() ) {
        // get the next word to process
        read_word = theFile.nextLine();

        System.out.print( "." );
        
        // always work in lower case
        read_word = read_word.toLowerCase();

        // INSERTING WORDS INTO OUR 2-3 Tree !!!
        treeRoot = twoThreeTree.insert(read_word, treeRoot);
        
      }

      System.out.println();

      theFile.close();
    }

    catch( FileNotFoundException ex ) {
      System.out.println( "Could not find " + fileName + ". Check the file name and try again." );
    }

  } // Dictionary Constructor

  /**
   * Essentially, this small function returns a boolean to verify if a 
   * Node in the Tree is a Leaf or not.
   * 
   * INPUT PARAMETER:
   * @param node - The given Node to check
   * OUTPUT PARAMETER:
   * @return - a boolean value 
   */
  private boolean isALeafNode(Node node) {

    return (node.left == null) && (node.right == null) && (node.mid == null);

  } // isALeafNode

  /**
   * Attempts to insert the new word into the Tree by traversing to 
   * a leaf node first then placing in its proper place within the Node; 
   * either in the "smallWord" or "bigWord". If the Node isn't 
   * a Leaf (contains two words already), it calls splitLeafNode() to 
   * split apart the current Node and re-balance the tree. It will call
   * pushUpParent if the current Node has a Parent that's not null.
   * 
   * INPUT PARAMETERS:
   * @param newWord - The new word given to insert.
   * @param node - The current Node always starting from the root of the Tree.
   * OUTPUT PARAMETER:
   * @return - current Node at which we are at the end.
   */
  private Node insert(String newWord, Node node) {

    // checks if the root is null
    if ( node == null ) {
      node = new Node(newWord, null); // creating root Node

    } else {

      // checks if it's a leaf node!
      if ( isALeafNode(node) ) {

        // Leaf Node has one data
        if ( node.bigWord.length() == 0 ) {
          
          // if the new word is bigger than the word stored in "smallWord"
          if ( (node.smallWord).compareTo(newWord) < 0 ) {
            node.bigWord = newWord;
            
          } else { // otherwise switch the words into their proper places within the Node
            node.bigWord = node.smallWord;  
            node.smallWord = newWord;
          
          }

        } else { // Leaf Node has two items!

          /**
           * We are calling the function below to create 
           * a balanced tree after comparison calculations.
           * We are getting back the node we started with 
           * but with an updated word within our node and proper
           * tree structure.
           */
          node = splitLeafNode(newWord, node); 

          /**
           * The latter will execute only if the current node's
           * parent isn't null (That means the current node is the root!)
           * We need to call it to restructure our tree with proper
           * places of the words.
          */
          if ( node.parent != null ) {
            pushUpParent(node, node.parent);
          }

        }

      } else {

        // TRAVERSING THE TWO-THREE TREE
        if ( (node.smallWord).compareTo(newWord) > 0 ) {
          node.left = insert(newWord, node.left); 
        
        } else if ( node.bigWord.length() == 0 ) {
          node.right = insert(newWord, node.right); 
        
        } else {
          if ( (node.bigWord).compareTo(newWord) > 0) {
            node.mid = insert(newWord, node.mid); 
  
          } else {
            node.right = insert(newWord, node.right); 
          }
    
        }

      }

    }

    return node;
  } // insert

  
  /**
   * In the splitLeafNode function, we are creating another mini subtree
   * which will store the updated word in the "theNode" Node after comparison
   * calculations with the new word. In other words, creating a successful 2-Node!
   * 
   * INPUT PARAMETERS:
   * @param newWord - The word we want to insert into our Tree and for comparison purposes
   * @param theNode - The current Node in which the newWord is stored
   * OUTPUT PARAMETER:
   * @return - The Node we initially passed but with a different structure.
   */
  private Node splitLeafNode(String newWord, Node theNode) {

    // small word BIGGER than new word
    if ( (theNode.smallWord).compareTo(newWord) > 0 ) {
      theNode.left = new Node(newWord, theNode);
      theNode.right = new Node(theNode.bigWord, theNode);
      theNode.bigWord = "";

    } else {

      // big word BIGGER than new word
      if ( (theNode.bigWord).compareTo(newWord) > 0 ) {
        theNode.left = new Node(theNode.smallWord, theNode);
        theNode.right = new Node(theNode.bigWord, theNode);
        theNode.smallWord = newWord;
        theNode.bigWord = "";

      } else { // new word is BIGGER than both the words in the Node
        theNode.left = new Node(theNode.smallWord, theNode);
        theNode.right = new Node(newWord, theNode);
        theNode.smallWord = theNode.bigWord;
        theNode.bigWord = "";

      }

    }

    return theNode;
  } // splitLeafNode

  /**
   * The main purpose of this function is to push up the middle word 
   * of a Node which could could be either 2-Node or 3-Node. If it's a 2-Node
   * theNode is inserted/structured properly into the tree. If it's a 3-Node,
   * it must be split and again be pushed up recursively with this function.
   * 
   * INPUT PARAMETERS:
   * @param theNode - The current Node that has the updated word in it
   * @param parentNode - Possibly a 2-NODE or 3-NODE and parent of "theNode"
   * OUTPUT PARAMETERS:
   * NONE
   */
  private void pushUpParent(Node theNode, Node parentNode) {

    // checking if the current Node's Parent has only one word
    if ( parentNode.bigWord.length() == 0 ) {
      
      // checks if the Parent Node's small word is BIGGER than the current Node's small word
      if ( (theNode.smallWord).compareTo(parentNode.smallWord) < 0) {
        parentNode.bigWord = parentNode.smallWord;
        parentNode.smallWord = theNode.smallWord;
        parentNode.left = theNode.left;
        parentNode.left.parent = parentNode;
        parentNode.mid = theNode.right;
        parentNode.mid.parent = parentNode;   

      } else { // otherwise, the current Node's small word is BIGGER than its Parent's small word
        parentNode.bigWord = theNode.smallWord;
        parentNode.mid = theNode.left;
        parentNode.mid.parent = parentNode;
        parentNode.right = theNode.right;
        parentNode.right.parent = parentNode;
      }

    } else { // We are here because the Parent Node is a 3-Node

      /**
       * We are calling the latter because the current Node's Parent
       * already has at most two words in it!
       */
      parentNode = nodeThreeSplit(theNode, parentNode);

      // Recursively calling this function to restructure the tree
      // after comparison calculations.
      if ( parentNode != null ) {
        pushUpParent(theNode.parent, parentNode);
      } 

    }

  } // pushUpParent


  /**
   * In this method, we are splitting a 3-NODE (parentNode) 
   * into a 2-NODE which will be restructured using the pushUpParent 
   * method afterwards. Parent Node of theNode is parentNode.
   * 
   * INPUT PARAMETERS:
   * @param theNode - The current Node that has the updated word in it
   * @param parentNode - This Node is a 3-Node (three children) and it's the parent of "theNode"
   * OUTPUT PARAMETER:
   * @return - The parent Node of our current Node.
   */
  private Node nodeThreeSplit(Node theNode, Node parentNode) {

    // the new word to add is SMALLER than the small word our Node
    if ( (theNode.smallWord).compareTo(parentNode.smallWord) < 0 ) {
      parentNode.left = theNode;
      parentNode.left.parent = parentNode;
      Node newNode = new Node(parentNode.bigWord, parentNode);
      parentNode.bigWord = "";
      newNode.left = parentNode.mid;
      newNode.left.parent = newNode;
      newNode.right = parentNode.right;
      newNode.right.parent = newNode;
      parentNode.right = newNode;
      parentNode.mid = null; // parentNode is no longer connected to theNode
    
      // if the new word to be added is between the two words in our Node
    } else if ( (theNode.smallWord).compareTo(parentNode.bigWord) < 0 ) {
      
      // creating a Node with Parent Node's small word in it along with the correct Parent reference
      Node newNodeOne = new Node(parentNode.smallWord, parentNode);
      newNodeOne.left = parentNode.left;
      newNodeOne.left.parent = newNodeOne;
      newNodeOne.right = theNode.left;
      newNodeOne.right.parent = newNodeOne;
      parentNode.left = newNodeOne;

      // creating another Node with Parent Node's big word in it along with the correct Parent reference
      Node newNodeTwo = new Node(parentNode.bigWord, parentNode);
      newNodeTwo.left = theNode.right;
      newNodeTwo.left.parent = newNodeTwo;
      newNodeTwo.right = parentNode.right;
      newNodeTwo.right.parent = newNodeTwo;
      parentNode.right = newNodeTwo;
      parentNode.smallWord = theNode.smallWord;
      parentNode.bigWord = "";
      parentNode.mid = null; 

      // if the new word is BIGGER than both of the words in our Node
    } else {
      parentNode.right = theNode;
      parentNode.right.parent = parentNode;
      Node newNodeThree = new Node(parentNode.smallWord, parentNode);
      newNodeThree.left = parentNode.left;
      newNodeThree.left.parent = newNodeThree;
      newNodeThree.right = parentNode.mid;
      newNodeThree.right.parent = newNodeThree;
      parentNode.left = newNodeThree;
      parentNode.smallWord = parentNode.bigWord;
      parentNode.bigWord = "";
      parentNode.mid = null; 

    }

    return parentNode; // returning the Parent Node

  } // nodeThreeSplit
  
  /**
   * Traversing the 2-3 Tree to look for a certain word. If 
   * it's found return true otherwise, false.
   * 
   * INPUT PARAMETER:
   * @param the_word - the word to be searched for
   * OUTPUT PARAMETER:
   * @return - A boolean value either true or false
   */
  public boolean lookup( String the_word )
  {
    String findWord = the_word.toLowerCase(); // converting the given word to lower case
    boolean found = false; // a boolean flag set to false by default
    Node currNode = treeRoot;

    // A while loop to traverse through the 2-3 Tree 
    while ( (currNode != null) && (!found) )  {

      if ( (findWord).equals(currNode.smallWord) || (findWord).equals(currNode.bigWord) ) {
        found = true; // word is found!
      
      } else {

        if ( (findWord).compareTo(currNode.smallWord) < 0 ) {
          currNode = currNode.left;

        } else if ( (findWord).compareTo(currNode.bigWord) > 0 ) {
          currNode = currNode.right;

        } else {
          currNode = currNode.mid;
        }
        
      }
      
    }

    return found; // returns the appropriate boolean value!

  } // lookup

} // Dictionary Class