/*
** This class is the part of the opensource MyDbConne application.
**
** See the header comment lines of Main class.
**
** Utils
** This class contains independent utilities to help the MyDbConne application.
*/
package com . kisscodesystems . MyDbConne ;
import java . io . File ;
import java . io . FileOutputStream ;
import java . io . InputStream ;
import java . sql . Blob ;
import java . sql . Clob ;
import java . sql . ResultSet ;
import java . sql . SQLXML ;
import java . util . ArrayList ;
import java . util . HashMap ;
import oracle . jdbc . OracleBfile ;
public final class Utils
{
/*
** Gets the value of a column from a result set.
** In case of any exception or error, the message
** will be returned as the value to show it to the user.
*/
  protected final Object getVal ( ResultSet rs , String colname )
  {
// This will be the value to be returned.
    Object val = null ;
    if ( rs != null )
    {
// Getting the value.
// If any error or exception has ccurred
// then the error message will be the value of the object.
      try
      {
        val = rs . getObject ( colname ) ;
      }
      catch ( Error e )
      {
        val = e . toString ( ) . trim ( ) ;
      }
      catch ( Exception e )
      {
        val = e . toString ( ) . trim ( ) ;
      }
    }
    else
    {
// Let the val be null.
      val = null ;
    }
// Returning the val.
    return val ;
  }
/*
** Gets the string representation of the object.
*/
  protected final String getValStr ( Object val , String nullStr )
  {
    if ( val == null )
    {
      return nullStr ;
    }
    else
    {
      return val . toString ( ) ;
    }
  }
/*
** Getting xmls in Db2 and Postgresql databases.
** The Mssql database handles well the xml data.
** The Oracle can handle the whole xml selecting
** without the XMLType oracle datatype (and jar)
** in the way:convert it to clob in your select!
** select t.xmlcolumn.getclobval() from table t;
*/
  protected final boolean getXml ( ResultSet rs , String destfile , String colname , String utf8 )
  {
    boolean separately = false ;
    SQLXML sxml = null ;
    FileOutputStream fout = null ;
    String buff = null ;
    try
    {
      sxml = rs . getSQLXML ( colname ) ;
      buff = sxml . getString ( ) ;
      fout = new FileOutputStream ( destfile ) ;
      fout . write ( buff . getBytes ( utf8 ) ) ;
      fout . flush ( ) ;
      separately = true ;
    }
    catch ( Exception e )
    {
      separately = false ;
    }
    finally
    {
      try
      {
        fout . close ( ) ;
      }
      catch ( Exception e )
      {
        separately = false ;
      }
    }
    sxml = null ;
    fout = null ;
    buff = null ;
    return separately ;
  }
/*
** Getting blobs from a column of a result set and trying to write it to a file.
*/
  protected final boolean getBlob ( ResultSet rs , String destfile , String colname )
  {
    boolean separately = false ;
    Blob blob = null ;
    File file = null ;
    FileOutputStream fout = null ;
    byte [ ] buff = null ;
    try
    {
      blob = rs . getBlob ( colname ) ;
      file = new File ( destfile ) ;
      if ( ! file . exists ( ) )
      {
        fout = new FileOutputStream ( destfile ) ;
        buff = blob . getBytes ( 1 , ( int ) blob . length ( ) ) ;
        fout . write ( buff ) ;
        fout . flush ( ) ;
        separately = true ;
      }
    }
    catch ( Exception e )
    {
      separately = false ;
    }
    finally
    {
      try
      {
        fout . close ( ) ;
      }
      catch ( Exception e )
      {
        separately = false ;
      }
    }
    blob = null ;
    file = null ;
    fout = null ;
    buff = null ;
    return separately ;
  }
/*
** Getting clobs from a column of a result set and trying to write it to a file.
*/
  protected final boolean getClob ( ResultSet rs , String destfile , String colname , String utf8 )
  {
    boolean separately = false ;
    Clob clob = null ;
    File file = null ;
    FileOutputStream fout = null ;
    String buff = null ;
    try
    {
      clob = rs . getClob ( colname ) ;
      file = new File ( destfile ) ;
      if ( ! file . exists ( ) )
      {
        fout = new FileOutputStream ( destfile ) ;
        buff = clob . getSubString ( 1 , ( int ) clob . length ( ) ) ;
        fout . write ( buff . getBytes ( utf8 ) ) ;
        fout . flush ( ) ;
        separately = true ;
      }
    }
    catch ( Exception e )
    {
      separately = false ;
    }
    finally
    {
      try
      {
        fout . close ( ) ;
      }
      catch ( Exception e )
      {
        separately = false ;
      }
    }
    clob = null ;
    file = null ;
    fout = null ;
    buff = null ;
    return separately ;
  }
/*
** Getting raws from a column of a result set and trying to write it to a file.
*/
  protected final boolean getRaw ( ResultSet rs , String destfile , String colname , int bufflength )
  {
    boolean separately = false ;
    InputStream inst = null ;
    File file = null ;
    FileOutputStream fout = null ;
    byte [ ] buff = null ;
    int bytes = 0 ;
    try
    {
      inst = rs . getBinaryStream ( colname ) ;
      file = new File ( destfile ) ;
      if ( ! file . exists ( ) )
      {
        fout = new FileOutputStream ( destfile ) ;
        buff = new byte [ bufflength ] ;
        while ( ( bytes = inst . read ( buff ) ) != - 1 )
        {
          fout . write ( buff ) ;
        }
        fout . flush ( ) ;
        separately = true ;
      }
    }
    catch ( Exception e )
    {
      separately = false ;
    }
    finally
    {
      try
      {
        fout . close ( ) ;
      }
      catch ( Exception e )
      {
        separately = false ;
      }
      try
      {
        inst . close ( ) ;
      }
      catch ( Exception e )
      {
        separately = false ;
      }
    }
    inst = null ;
    file = null ;
    fout = null ;
    buff = null ;
    bytes = 0 ;
    return separately ;
  }
/*
** Getting bfiles from a column of a result set and trying to write it to a file.
*/
  protected final boolean getBfile ( ResultSet rs , String destfile , String colname , int bufflength )
  {
    boolean separately = false ;
    OracleBfile bfile = null ;
    InputStream inst = null ;
    File file = null ;
    FileOutputStream fout = null ;
    byte [ ] buff = null ;
    int bytes = 0 ;
    try
    {
      bfile = ( OracleBfile ) rs . getObject ( colname ) ;
      bfile . openFile ( ) ;
      inst = bfile . getBinaryStream ( ) ;
      file = new File ( destfile ) ;
      if ( ! file . exists ( ) )
      {
        fout = new FileOutputStream ( destfile ) ;
        buff = new byte [ bufflength ] ;
        while ( ( bytes = inst . read ( buff ) ) != - 1 )
        {
          fout . write ( buff ) ;
        }
        fout . flush ( ) ;
        separately = true ;
      }
    }
    catch ( Exception e )
    {
      separately = false ;
    }
    finally
    {
      try
      {
        fout . close ( ) ;
      }
      catch ( Exception e )
      {
        separately = false ;
      }
      try
      {
        inst . close ( ) ;
      }
      catch ( Exception e )
      {
        separately = false ;
      }
      try
      {
        bfile . closeFile ( ) ;
      }
      catch ( Exception e )
      {
        separately = false ;
      }
    }
    bfile = null ;
    inst = null ;
    file = null ;
    fout = null ;
    buff = null ;
    bytes = 0 ;
    return separately ;
  }
/*
** This function will calculate the elapsed time according the elapsedMs
** milliseconds of query duration.
*/
  protected final String calculateElapsed ( long elapsedMs )
  {
// This will be the final result.
    String totalElapsedTime = "" ;
// To help the calculations.
    long milliseconds = 0 ;
    long seconds = 0 ;
    long minutes = 0 ;
    long hours = 0 ;
// If for example 1h 0m 12s is the final result, we want to display the 0m.
    boolean zeroIsNeeded = false ;
// Calculating hours, minutes, seconds and milliseconds.
    if ( elapsedMs >= 3600 * 1000 )
    {
      hours = ( long ) Math . floor ( elapsedMs / ( 3600 * 1000 ) ) ;
      elapsedMs -= hours * ( 3600 * 1000 ) ;
    }
    if ( elapsedMs >= 60 * 1000 )
    {
      minutes = ( long ) Math . floor ( elapsedMs / ( 60 * 1000 ) ) ;
      elapsedMs -= minutes * ( 60 * 1000 ) ;
    }
    if ( elapsedMs >= 1000 )
    {
      seconds = ( long ) Math . floor ( elapsedMs / 1000 ) ;
      elapsedMs -= seconds * ( 1000 ) ;
    }
    milliseconds = elapsedMs ;
// And then place these into the returning string.
    if ( hours > 0 )
    {
      zeroIsNeeded = true ;
      totalElapsedTime += hours + "h " ;
    }
    if ( minutes > 0 || zeroIsNeeded )
    {
      zeroIsNeeded = true ;
      totalElapsedTime += minutes + "m " ;
    }
    if ( seconds > 0 || zeroIsNeeded )
    {
      zeroIsNeeded = true ;
      totalElapsedTime += seconds + "s " ;
    }
    if ( milliseconds > 0 || zeroIsNeeded )
    {
      totalElapsedTime += milliseconds + "ms" ;
    }
// This is for ensure that we display something if nothing has happened.
    if ( hours == 0 && minutes == 0 && seconds == 0 && milliseconds == 0 )
    {
      totalElapsedTime = "0ms" ;
    }
// No need them.
    milliseconds = 0 ;
    seconds = 0 ;
    minutes = 0 ;
    hours = 0 ;
    zeroIsNeeded = false ;
// Return this.
    return totalElapsedTime ;
  }
/*
** This is for joining an integer array list with a separator.
*/
  protected final String joinArrayListInteger ( ArrayList < Integer > elements , String sep1 )
  {
// This will be the result.
    String s = "" ;
    if ( elements != null && sep1 != null && s != null )
    {
// Looping thru the elements
      for ( int element : elements )
      {
// The actual element and the separator will be appended to the end of the result.
        s += String . valueOf ( element ) + sep1 ;
      }
// It is necessary to remove the separator if the result ends with it.
      if ( s . endsWith ( sep1 ) )
      {
        s = s . substring ( 0 , s . length ( ) - sep1 . length ( ) ) ;
      }
    }
// We are done.
    return s ;
  }
/*
** Pads a string with space char or any given char into the length of i.
** If the length of the string initially was longer than i then the string
** will be truncated!
*/
  protected final String pad ( String s , int i , char spaceChar )
  {
// This is the object we are return with.
    String b = new String ( s ) ;
    if ( b != null && i > - 1 )
    {
// The given char will be appended to the end of the string.
      while ( b . length ( ) < i )
      {
        b = b + spaceChar ;
      }
// And finally it will be truncated if it is longer than the length of i.
      b = b . substring ( 0 , i ) ;
    }
// Let's return this.
    return b ;
  }
/*
** This is a tricky function.
** To handle the parameters surrounded by double quotes.
** The logic of this is that we will search for contents between double quotes
** and we "cut" these values and replacing unique and generated strings.
** Then we will split the string into smaller pieces by space and then
** we can paste back into place the strings we cut before.
*/
  protected final String [ ] requestStringSplit ( String args , String doubleSpace , String doubleQuote , String singleSpace , String backsla , String keyBeginEnd )
  {
// This will contain the final result.
    String [ ] requestParams = null ;
// This is to create a new string.
    String requestString = null ;
// This hashmap will contain the string split by space.
    HashMap < String , String > strings = new HashMap < String , String > ( ) ;
// This will contain the arguments but without the quoted contents.
    String tempArgs = "" ;
// Simple counter we use in the loop of the request string.
    int charPos = 0 ;
// While looping on the original string, we are in a double quoted content or not.
    boolean inString = false ;
// Temporary string to store the actual double quoted content.
// Its final value will be replaced by the next variable.
    String tempString = "" ;
// This will be the key to replace the previous double quoted stemp tring value.
    String tempStringKey = "" ;
// The count of the double quotes contained by the request string.
    int count = 0 ;
// Not-null objects are needed first.
    if ( args != null && doubleSpace != null && doubleQuote != null && singleSpace != null && backsla != null && keyBeginEnd != null )
    {
// Creating a new one.
      requestString = new String ( args ) ;
// We don't need double spaces so let's remove these.
      while ( requestString . contains ( doubleSpace ) )
      {
        requestString = requestString . replace ( doubleSpace , singleSpace ) ;
      }
// If this reques string contains double quotes then we are going to startsWith
// to fint them.
// If not, it will be very simple: this string will be split up and we will be done.
      if ( requestString . contains ( doubleQuote ) )
      {
// Let's count the double quotes.
        count = 0 ;
        for ( int i = 0 ; i < requestString . length ( ) ; i ++ )
        {
          if ( requestString . charAt ( i ) == ( char ) 34 )
          {
            count ++ ;
          }
        }
// If this contains 2, 4, 6 and so on double quotes then we can continue.
        if ( count % 2 == 0 )
        {
// Let's pad this string if it is necessary.
          if ( requestString . startsWith ( doubleQuote ) || requestString . startsWith ( singleSpace + doubleQuote ) )
          {
            requestString = doubleSpace + requestString ;
          }
// We don't work with empty strings.
          requestString = requestString . replace ( doubleQuote + doubleQuote , doubleQuote + singleSpace + doubleQuote ) ;
// So we are going thru in the characters of the string.
          charPos = 0 ;
          inString = false ;
          while ( charPos < requestString . length ( ) )
          {
// If the next character is a double quoted char
            if ( requestString . substring ( charPos , charPos + 1 ) . equals ( doubleQuote ) )
            {
// If the previous is not a backslash
              if ( ! requestString . substring ( charPos - 1 , charPos ) . equals ( backsla ) )
              {
// Then inString change!
                inString = ! inString ;
              }
              else
              {
// If the previous previous character is also a backslash
                if ( requestString . substring ( charPos - 2 , charPos - 1 ) . equals ( backsla ) )
                {
// Then also inString change!
                  inString = ! inString ;
                }
              }
            }
// We are in string content or not?
            if ( inString )
            {
// If we are in a string now then we should copy this character.
              tempString += requestString . substring ( charPos , charPos + 1 ) ;
            }
            else
            {
// Else.
// Not in string content.
              if ( ! "" . equals ( tempString ) )
              {
// If the temp string is not empty then we have to finalize the
// found string content.
// Let's copy this last character.
                tempString += requestString . substring ( charPos , charPos + 1 ) ;
// Create the key of the new string content. This will contain the
// size of the hashmap so unique identifiers will be coustructed.
                tempStringKey = keyBeginEnd + strings . size ( ) + keyBeginEnd ;
// Let's copy these values: key and copyed string.
                strings . put ( tempStringKey , tempString ) ;
// The key of the found double quoted content has to be appended
// to the end of the tempArgs
                tempArgs += tempStringKey ;
// Will be started over, the tempString should now empty again.
                tempString = "" ;
              }
              else
              {
// The string content is empty now so we are not in a string content
// and we are copy this current not-doublequote character into the
// tempArgs.
                tempArgs += requestString . substring ( charPos , charPos + 1 ) ;
              }
            }
// Going to the next position.
            charPos ++ ;
          }
// If we are here then we have the hashmap filled by the double quoted string contents
// and the tempArgs which contains the original request string content having the keys
// instead of the double quoted strings.
// Now we have to split this new string by single space.
          requestParams = tempArgs . trim ( ) . split ( singleSpace ) ;
// This is done, so now it is time to paste back the string contents into the place of their keys.
          for ( int i = 0 ; i < requestParams . length ; i ++ )
          {
// If this item is a key...
            if ( requestParams [ i ] . startsWith ( keyBeginEnd ) && requestParams [ i ] . endsWith ( keyBeginEnd ) )
            {
// Pasting back.
              requestParams [ i ] = strings . get ( requestParams [ i ] ) ;
// Removing the double quotes from the beginning and from the ending of this argument item.
              requestParams [ i ] = requestParams [ i ] . substring ( 1 , requestParams [ i ] . length ( ) - 1 ) ;
            }
          }
        }
        else
        {
// The count of double quotes is 1, 3, 5 ... so we cannot interpret this,
// We will return with an empty array. (This will be failed in any validation.)
          requestParams = new String [ 0 ] ;
        }
      }
      else
      {
// This is the simplest case: without double quotes, the request array is
// the result of a single split operation.
        requestParams = requestString . split ( singleSpace ) ;
      }
// This elements will be trimmed!
      for ( int i = 0 ; i < requestParams . length ; i ++ )
      {
        requestParams [ i ] = requestParams [ i ] . trim ( ) ;
      }
    }
    else
    {
// With null initialized value, an empty array will be returned.
// (Will be failed in any validation.)
      requestParams = new String [ 0 ] ;
    }
// They are not needed.
    requestString = null ;
    strings = null ;
    tempArgs = null ;
    charPos = 0 ;
    inString = false ;
    tempString = null ;
    tempStringKey = null ;
    count = 0 ;
// Return this params object.
    return requestParams ;
  }
/*
** Clears the given char array as fills that by the given chars.
*/
  protected final void clearCharArray ( char [ ] charArray , char zeroChar )
  {
    if ( charArray != null )
    {
// Filling by chars.
      for ( int i = 0 ; i < charArray . length ; i ++ )
      {
        charArray [ i ] = zeroChar ;
      }
    }
  }
/*
** Clears the given byte array as fills that by the given bytes.
*/
  protected final void clearByteArray ( byte [ ] byteArray , byte nullByte )
  {
    if ( byteArray != null )
    {
// Filling by bytes.
      for ( int i = 0 ; i < byteArray . length ; i ++ )
      {
        byteArray [ i ] = nullByte ;
      }
    }
  }
/*
** ASCII validator functions.
** isASCIIorNEWLINE means 32-126 chars or char 10 (newLine)
** isASCIIandNONSPACE means 33-126 chars.
** We know that the set of ASCII characters is a wider set but nobody will type
** for example CR character into its connection data.
*/
  protected final boolean isASCIIorNEWLINE ( char c )
  {
    return ( ( c >= 32 && c <= 126 ) || c == 10 ) ;
  }
  protected final boolean isASCIIorNEWLINE ( char [ ] cs )
  {
    boolean success = true ;
    if ( cs != null )
    {
      for ( int i = 0 ; i < cs . length ; i ++ )
      {
        if ( ! ( ( cs [ i ] >= 32 && cs [ i ] <= 126 ) || cs [ i ] == 10 ) )
        {
          success = false ;
          break ;
        }
      }
    }
    else
    {
      success = false ;
    }
    return success ;
  }
  protected final boolean isASCIIandNONSPACE ( char [ ] cs )
  {
    boolean success = true ;
    if ( cs != null )
    {
      for ( int i = 0 ; i < cs . length ; i ++ )
      {
        if ( ! ( cs [ i ] >= 33 && cs [ i ] <= 126 ) )
        {
          success = false ;
          break ;
        }
      }
    }
    else
    {
      success = false ;
    }
    return success ;
  }
/*
** ASCII bytes to chars and chars to bytes functions.
*/
  protected final byte [ ] toBytesASCII ( char [ ] chars )
  {
    byte [ ] bytes = new byte [ 0 ] ;
    if ( chars != null )
    {
      bytes = new byte [ chars . length ] ;
      for ( int i = 0 ; i < chars . length ; i ++ )
      {
        bytes [ i ] = ( byte ) chars [ i ] ;
      }
    }
    return bytes ;
  }
  protected final char [ ] toCharsASCII ( byte [ ] bytes )
  {
    char [ ] chars = new char [ 0 ] ;
    if ( bytes != null )
    {
      chars = new char [ bytes . length ] ;
      for ( int i = 0 ; i < bytes . length ; i ++ )
      {
        chars [ i ] = ( char ) bytes [ i ] ;
      }
    }
    return chars ;
  }
}