// This program provides a spell checker using hashing. It reads in the dictionary file
// and then loads another file (as a command line parameter...) and performs a spell
// check on all the words in the document.

import java.io.*;
import java.util.*;

public class a3q2
{
  public static void main( String args[] )
  {
    // create (and read in) our dictionary
    Dictionary dictionary = new Dictionary( args[0] );
    // hold the invalid words and their line numbers
    Words bad_words = new Words();
    int line = 0;
    int invalid_words = 0;
    String read_line;
    // used to extract the words in a line of text
    String[] tokens;

    // read the document 1 line at a time
    try
    {
      Scanner theFile = new Scanner(new File(args[1]));

      System.out.println( "Checking " + args[1] );
      
      // process each line in turn
      while ( theFile.hasNextLine() )
      {
        // get the next word to process
        read_line = theFile.nextLine();
        line++;

        tokens = read_line.split( "\\W+" );

        // blank lines show up as a single empty token
        if ( tokens.length > 0 && tokens[0].length() > 0 )
        {
          for ( int i=0 ; i<tokens.length ; i++ )
          {
            // see if the word is in the dictionary and issue an error if not
            if ( !dictionary.lookup( tokens[i] ) )
            {
              bad_words.addWord( tokens[i], line );
              invalid_words++;
            }
          }
        }
      }

      theFile.close();
    }

    catch( FileNotFoundException ex )
    {
      System.out.println( "Could not find " + args[1] + ". Check the file name and try again." );
    }

    System.out.println( "There are a total of " + invalid_words + " invalid words:" );

    bad_words.print();
    
    System.out.println( "\nEnd of processing" );
  }
}