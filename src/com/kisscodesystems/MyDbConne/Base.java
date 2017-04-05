/*
** This class is the part of the opensource MyDbConne application.
**
** See the header comment lines of Main class.
**
** Base
** This is the base class of the MyDbConne application
** containing the base variables and functions.
*/
package com . kisscodesystems . MyDbConne ;
import java . io . BufferedReader ;
import java . io . BufferedWriter ;
import java . io . File ;
import java . io . FileInputStream ;
import java . io . FileNotFoundException ;
import java . io . FileOutputStream ;
import java . io . FileReader ;
import java . io . FileWriter ;
import java . io . IOException ;
import java . sql . ResultSet ;
import java . sql . ResultSetMetaData ;
import java . sql . SQLException ;
import java . text . SimpleDateFormat ;
import java . util . Date ;
public class Base extends Const
{
// This will be the args object from the Main object.
  protected volatile String [ ] args = null ;
// Date formats.
  protected final SimpleDateFormat simpleDateFormatForDisplaying = new SimpleDateFormat ( appDateFormatForDisplaying ) ;
  protected final SimpleDateFormat simpleDateFormatForFilenames = new SimpleDateFormat ( appDateFormatForFilenames ) ;
  protected final SimpleDateFormat simpleDateFormatForTimestamps = new SimpleDateFormat ( appDateFormatForTimestamps ) ;
// The globally selectable database type and connection name.
// These two property can identify an exact datanase connection
// from the connections file.
  protected volatile String dbType = null ;
  protected volatile String dbConn = null ;
// The prompt that will wait for the user's interaction.
// appName>
// appName dbType>
// appName dbType dbConn>
// These above are the possible strings to be displayed as the prompt,
// Replaced the variable names intp the actual appName, dbType, dbConn values of course.
  protected volatile String prompt = null ;
// The delimiters of the database types.
// By default, these will be empty strings, so the sql statement delimiters are new line characters.
  protected volatile String delimiterOracle = "" ;
  protected volatile String delimiterMssql = "" ;
  protected volatile String delimiterDb2 = "" ;
  protected volatile String delimiterPostgresql = "" ;
/*
** Getting the arguments from the Main object.
*/
  protected final void setArgs ( String args [ ] )
  {
    this . args = args ;
  }
/*
** Printing methods.
*/
  protected final void outprintln ( String s )
  {
    System . out . println ( s ) ;
  }
  protected final void outprint ( char c )
  {
    System . out . print ( c ) ;
  }
  protected final void outprint ( String s )
  {
    System . out . print ( s ) ;
  }
/*
** Debugging methods.
*/
  protected final void debugln ( String s )
  {
    outprintln ( "# " + s ) ;
  }
  protected final void debugln ( char c )
  {
    outprintln ( "# " + c ) ;
  }
  protected final void debug ( String s )
  {
    outprint ( s ) ;
  }
  protected final void debug ( char c )
  {
    outprint ( c ) ;
  }
/*
** Exist the whole application after the final message to the console.
*/
  protected final void systemexit ( String s )
  {
    outprintln ( messageExiting + s ) ;
    System . exit ( 1 ) ;
  }
/*
** Getting the delimiter according to the parameter.
*/
  protected final String getDelimiter ( String dbtype )
  {
    if ( dbtype . equals ( dbTypeOracle ) )
    {
      return delimiterOracle ;
    }
    else if ( dbtype . equals ( dbTypeMssql ) )
    {
      return delimiterMssql ;
    }
    else if ( dbtype . equals ( dbTypeDb2 ) )
    {
      return delimiterDb2 ;
    }
    else if ( dbtype . equals ( dbTypePostgresql ) )
    {
      return delimiterPostgresql ;
    }
    else
    {
      return "" ;
    }
  }
/*
** Getting the delimiter according to the globally selected dbtType database type.
*/
  protected final String getDelimiter ( )
  {
    if ( dbType != null )
    {
      if ( dbType . equals ( dbTypeOracle ) )
      {
        return delimiterOracle ;
      }
      else if ( dbType . equals ( dbTypeMssql ) )
      {
        return delimiterMssql ;
      }
      else if ( dbType . equals ( dbTypeDb2 ) )
      {
        return delimiterDb2 ;
      }
      else if ( dbType . equals ( dbTypePostgresql ) )
      {
        return delimiterPostgresql ;
      }
      else
      {
        return "" ;
      }
    }
    else
    {
      systemexit ( "Error - dbType is null, getDelimiter" ) ;
      return null ;
    }
  }
/*
** These are necessary to be here, later will be overwritten.
*/
  protected ResultSet getQueryResultSet ( int queryId )
  {
    return null ;
  }
  protected void changePromptToTheActual ( ) { }
/*
** Getting the column names from a result set constructed as a string.
*/
  protected final String getSourceHeaderFromResultSet ( int queryId , String delimiter )
  {
// This will be the header string.
    String sourceHeader = "" ;
// The result set is on the way.
    ResultSet sourceResultSet = getQueryResultSet ( queryId ) ;
    if ( sourceResultSet != null )
    {
// Trying to create the header string.
// Any exception results empty header string.
      try
      {
// Creating the metadata of the result set.
        ResultSetMetaData resultSetMetaData = sourceResultSet . getMetaData ( ) ;
// This number of columns are in the result set.
        int colscount = resultSetMetaData . getColumnCount ( ) ;
// Now we can loop and append the elements into the end of the header.
        for ( int i = 1 ; i <= colscount ; i ++ )
        {
// Appending the name of the column.
          sourceHeader = sourceHeader + resultSetMetaData . getColumnName ( i ) ;
// appending the separator if it is not the last column.
          if ( i < colscount )
          {
            sourceHeader = sourceHeader + delimiter ;
          }
        }
      }
      catch ( SQLException e )
      {
        sourceHeader = "" ;
      }
    }
// Returning of the header string.
    return sourceHeader ;
  }
/*
** Getting the first line of the file.
*/
  protected final String getSourceHeaderFromFile ( String fileName )
  {
// This is the header, empty by default.
    String sourceHeader = "" ;
// In case of existing file and valid file path..
    if ( isExistingFile ( fileName , false ) && isValidFilePath ( fileName , false ) )
    {
// The header content is the first line of the file, so let's read it.
      sourceHeader = readFileContent ( fileName , true ) ;
    }
// Returning of the header.
    return sourceHeader ;
  }
/*
** Prints the error message of a query.
*/
  protected final void printErrorMessage ( String queryErrorMessage )
  {
    outprintln ( messageQueryDescErrorMessage + ( queryErrorMessage == null ? "" : ( queryErrorMessage . contains ( newLineString ) ? newLineString : "" ) + queryErrorMessage ) ) ;
  }
/*
** Trimming the query string typed by the user or comes from file.
*/
  protected final String trimQueryString ( String q , String d )
  {
// The string objects to work.
    String queryString = q ;
    String queryDelimiter = d ;
// At first, the query string has to be trimmed.
    queryString = queryString . trim ( ) ;
// In while loop: doing that until it has the delimiter as the string ending.
    while ( queryString . endsWith ( queryDelimiter ) && ! "" . equals ( queryDelimiter ) )
    {
// Removing the delimiter
      queryString = queryString . substring ( 0 , queryString . length ( ) - queryDelimiter . length ( ) ) ;
// Trimming the query string again.
      queryString = queryString . trim ( ) ;
    }
// And now we are done, the sql string can go back.
    return queryString ;
  }
/*
** Is this result set usable?
** This is a good question if this is a not scrollable result set.
*/
  protected final boolean isResultSetUsable ( ResultSet rs )
  {
// Not usable by default.
    boolean usable = false ;
    if ( rs != null )
    {
// This will be the metadata object.
      ResultSetMetaData rsmd = null ;
      try
      {
// Trying to have this.
        rsmd = rs . getMetaData ( ) ;
// Usable since we have no exception at this point.
        usable = true ;
      }
      catch ( SQLException e )
      {
        usable = false ;
      }
    }
    else
    {
      usable = false ;
    }
// Returning this result.
    return usable ;
  }
/*
** Impossible to do batch sql execution with empty delimiter.
*/
  protected final boolean isQueryTypeAndDelimiterOk ( String queryType , String queryDelimiter )
  {
    return ( ! ( argBatch . equals ( queryType ) && "" . equals ( queryDelimiter ) ) ) ;
  }
/*
** This is good args object if it is not null.
*/
  protected final boolean isGoodArgsObject ( String [ ] args )
  {
    return args != null ;
  }
/*
** Returns true if the connections file or its salt or iv file or the new file has been given.
*/
  protected final boolean isValidConnectionsFilePath ( String filePath )
  {
    boolean valid = false ;
    if ( filePath != null )
    {
      if ( filePath . startsWith ( appConnectionsDir + SEP ) && ( filePath . endsWith ( appCsPostfix ) || filePath . endsWith ( appSlPostfix ) || filePath . endsWith ( appIvPostfix ) || filePath . endsWith ( appNwPostfix ) ) )
      {
        valid = true ;
      }
    }
    return valid ;
  }
/*
** Validating the given database type.
** Only the supported database types are acceptable.
*/
  protected final boolean isValidDbType ( String dbtype , boolean messageIfNot )
  {
// Valid by default.
    boolean valid = true ;
    if ( dbtype . equals ( dbTypeOracle ) || dbtype . equals ( dbTypeMssql ) || dbtype . equals ( dbTypeDb2 ) || dbtype . equals ( dbTypePostgresql ) )
    {
// Valid now.
      valid = true ;
    }
    else
    {
// Not valid singe this value is not one of the expecteds.
      valid = false ;
// A message can go to the output.
      if ( messageIfNot )
      {
        outprintln ( messageDatabaseTypeHasNotBeenCorrect ) ;
      }
    }
// And returning this.
    return valid ;
  }
/*
** Searching for a single file on the disk in the specified file path.
*/
  protected final boolean isExistingFile ( String filePath , boolean messageIfNot )
  {
// Success only at the end.
    boolean success = false ;
    File file = new File ( filePath ) ;
    if ( file != null )
    {
// Success if this file is existing and if it is really a file.
// Else user gets error messages.
      if ( file . exists ( ) )
      {
        if ( file . isFile ( ) )
        {
          success = true ;
        }
        else
        {
          if ( messageIfNot )
          {
            outprintln ( messageFileIsNotFile + filePath ) ;
          }
        }
      }
      else
      {
        if ( messageIfNot )
        {
          outprintln ( messageFileDoesNotExist + filePath ) ;
        }
      }
    }
    else
    {
      systemexit ( "Error - file is null, isExistingFile" ) ;
    }
// This can be released.
    file = null ;
// Returns the result.
    return success ;
  }
/*
** Check for a valid password.
** The function writes a message out if the input is not valid and it is requested to print out the message.
** Password char array reference and the boolean values are expected.
** This char array will be validated by the standards of good password.
*/
  protected final boolean isValidGoodPassword ( char [ ] password , boolean messageIfNot )
  {
// The password is not valid by default.
    boolean valid = false ;
// At first this would be ASCII and nonspace.
    if ( utils != null )
    {
      if ( utils . isASCIIandNONSPACE ( password ) )
      {
// The length must be between the correct upper and lower bounds.
        if ( password . length <= appMaxLengthOfInput )
        {
          if ( password . length >= appGoodPasswordMinLengthOfGoodPasswords )
          {
// The counts of letters will be collected by letter types.
            int countUCLetters = 0 ;
            int countLCLetters = 0 ;
            int countDigits = 0 ;
            int countSpecChars = 0 ;
// Let's count the letters by types.
            for ( int i = 0 ; i < password . length ; i ++ )
            {
              if ( password [ i ] >= 33 && password [ i ] <= 47 )
              {
                countSpecChars ++ ;
              }
              else if ( password [ i ] >= 48 && password [ i ] <= 57 )
              {
                countDigits ++ ;
              }
              else if ( password [ i ] >= 58 && password [ i ] <= 64 )
              {
                countSpecChars ++ ;
              }
              else if ( password [ i ] >= 65 && password [ i ] <= 90 )
              {
                countUCLetters ++ ;
              }
              else if ( password [ i ] >= 91 && password [ i ] <= 96 )
              {
                countSpecChars ++ ;
              }
              else if ( password [ i ] >= 97 && password [ i ] <= 122 )
              {
                countLCLetters ++ ;
              }
              else if ( password [ i ] >= 123 && password [ i ] <= 126 )
              {
                countSpecChars ++ ;
              }
            }
// These counts have to be above the correct bounds
            if ( countUCLetters >= appGoodPasswordMinCountOfUCLetters && countLCLetters >= appGoodPasswordMinCountOfLCLetters && countDigits >= appGoodPasswordMinCountOfDigits && countSpecChars >= appGoodPasswordMinCountOfSpecChars )
            {
// This is the case of valid name. Every other cases are invalid cases.
              valid = true ;
            }
// These should be out of using.
            countUCLetters = 0 ;
            countLCLetters = 0 ;
            countDigits = 0 ;
            countSpecChars = 0 ;
          }
        }
      }
    }
    else
    {
      systemexit ( "Error - utils is null, isValidGoodPassword" ) ;
    }
// The message will be written out if it is requested.
    if ( ! valid && messageIfNot )
    {
      outprintln ( messageConnectionsGoodPasswordIsNotValid ) ;
    }
// Give the validity back.
    return valid ;
  }
/*
** This is for reading a file from a specified file path into a byte array and return with it.
*/
  protected final byte [ ] readFileBytes ( String filePath )
  {
// This will be the byte array, not null and empty by default.
    byte [ ] bytes = new byte [ 0 ] ;
// Let's check the file path, it has to be passed by the following validation.
// Else don't do anything.
    if ( isValidConnectionsFilePath ( filePath ) )
    {
// At first we are creating a new File object.
      File file = new File ( filePath ) ;
      if ( file != null )
      {
// We can continue if this file is existing and really is a file.
// Else we have to break the whole program.
        if ( file . exists ( ) && file . isFile ( ) )
        {
// Now we can specify the size of the byte array, let's recreate it.
          bytes = new byte [ ( int ) file . length ( ) ] ;
// We can create now the FileInputStream object as null by default.
          FileInputStream fis = null ;
          try
          {
            fis = new FileInputStream ( filePath ) ;
          }
          catch ( FileNotFoundException e )
          {
            systemexit ( "Exception - FileNotFoundException, readFileBytes" ) ;
          }
          if ( fis != null )
          {
// Trying to read and close the fis object.
// If it is not successful, we are going to exit.
            try
            {
              fis . read ( bytes ) ;
            }
            catch ( IOException e )
            {
              systemexit ( "Exception - IOException, readFileBytes" ) ;
            }
            finally
            {
              try
              {
                fis . close ( ) ;
              }
              catch ( Exception e )
              {
                systemexit ( "Exception - Exception, readFileBytes" ) ;
              }
            }
          }
          else
          {
            systemexit ( "Error - fis is null, readFileBytes" ) ;
          }
// This object is not needed any more.
          fis = null ;
        }
        else
        {
          systemexit ( "Error - File does not exist or it is not a file, readFileBytes" ) ;
        }
      }
      else
      {
        systemexit ( "Error - file is null, readFileBytes" ) ;
      }
    }
// Give this.
    return bytes ;
  }
/*
** This method is for writing a file with the given byte array to the given file path.
*/
  protected final void writeFileBytes ( String filePath , byte [ ] bytes )
  {
// Let's check the file path, it has to be passed by the following validation.
// Else don't do anything.
    if ( isValidConnectionsFilePath ( filePath ) )
    {
// We will continue if the bytes object is not null
// Else exiting.
      if ( bytes != null )
      {
// This is null by default, has to be not null after the initialization.
        FileOutputStream fos = null ;
// Trying to create a new FileOutputStream object.
// If it is not possible (exception), exit.
        try
        {
          fos = new FileOutputStream ( filePath ) ;
        }
        catch ( FileNotFoundException e )
        {
          systemexit ( "Exception - FileNotFoundException, writeFileBytes" ) ;
        }
        if ( fos != null )
        {
// Now we are trying to write the bytes and close the fos object.
// If it is not successful, exit.
          try
          {
            fos . write ( bytes ) ;
          }
          catch ( IOException e )
          {
            systemexit ( "Exception - Exception, writeFileBytes" ) ;
          }
          finally
          {
            try
            {
              fos . close ( ) ;
            }
            catch ( Exception e )
            {
              systemexit ( "Exception - Exception, writeFileBytes" ) ;
            }
          }
        }
        else
        {
          systemexit ( "Error - fos is null, writeFileBytes" ) ;
        }
// We don't need this object any more.
        fos = null ;
      }
      else
      {
        systemexit ( "Error - bytes is null, writeFileBytes" ) ;
      }
    }
  }
/*
** Reads a single input line from console and returns with it as a String.
*/
  protected final String readline ( String s , int maxLength )
  {
// This is am empty string at the beginning, this will be the reading.
    String read = "" ;
    if ( s != null )
    {
      if ( console != null )
      {
// Waiting the user's interaction.
        read = console . readLine ( s ) ;
        if ( read != null )
        {
// Exiting when it is too long.
          if ( read . length ( ) > maxLength )
          {
            systemexit ( "Error - Too long input has been read, readline" ) ;
          }
        }
        else
        {
          systemexit ( "Error - read is null, readline" ) ;
        }
      }
      else
      {
        systemexit ( "Error - console is null, readline" ) ;
      }
    }
    else
    {
      systemexit ( "Error - s is null, readline" ) ;
    }
// Giving this back.
    return read ;
  }
/*
** Reads a single input line from console and returns with it as a String.
*/
  protected final String readiline ( String s )
  {
// This is am empty string at the beginning, this will be the reading.
    String read = "" ;
    if ( s != null )
    {
      if ( console != null )
      {
// Waiting for the user's input.
        read = console . readLine ( s ) ;
        if ( read != null )
        {
// Trimming first!
          read = read . trim ( ) ;
// And now checking the correct length of this input. Exiting if it is too long.
          if ( read . length ( ) > appMaxLengthOfSql )
          {
            systemexit ( "Error - Too long input has been read, readiline" ) ;
          }
        }
        else
        {
          systemexit ( "Error - read is null, readiline" ) ;
        }
      }
      else
      {
        systemexit ( "Error - console is null, readiline" ) ;
      }
    }
    else
    {
      systemexit ( "Error - s is null, readiline" ) ;
    }
// Give this.
    return read ;
  }
/*
** Reads a password from the console into char array and returns with it.
*/
  protected final char [ ] readpassword ( String s )
  {
// This will be the char array containing the password.
    char [ ] read = new char [ 0 ] ;
    if ( s != null )
    {
      if ( console != null )
      {
// The time to type a password is limited!
// Let's create a timestamp.
        Date wait = new Date ( ) ;
        if ( wait != null )
        {
// Waiting for the user's input.
          read = console . readPassword ( s ) ;
          if ( read != null )
          {
// Check the waiting time. If it took too long then exit.
            if ( ( int ) ( ( new Date ( ) . getTime ( ) - wait . getTime ( ) ) / 1000 ) > appMaxNotReadInputsSeconds )
            {
              systemexit ( "Error - Waited too long, readpassword" ) ;
            }
// Also exiting when it is too long.
            if ( read . length > appMaxLengthOfInput )
            {
              systemexit ( "Error - Too long password has been read, readpassword" ) ;
            }
            else
            {
// Checking the format of the read input.
// We will return an empty password instead of current value of read if it is not correct.
// (Empty password will fail in any validation.)
              if ( utils != null )
              {
                if ( ! utils . isASCIIandNONSPACE ( read ) )
                {
                  read = new char [ 0 ] ;
                }
              }
              else
              {
                systemexit ( "Error - utils is null, readpassword" ) ;
              }
            }
// We don't need this object.
            wait = null ;
          }
          else
          {
            systemexit ( "Error - read is null, readpassword" ) ;
          }
        }
        else
        {
          systemexit ( "Error - wait is null, readpassword" ) ;
        }
      }
      else
      {
        systemexit ( "Error - console is null, readpassword" ) ;
      }
    }
    else
    {
      systemexit ( "Error - s is null, readpassword" ) ;
    }
// Returning the password contained character array.
    return read ;
  }
/*
** Reading the answers to the questions should be answered by yes.
** Not all of this questions are questioned by this function!
** The reason of that is the different logic.
*/
  protected final boolean readYesElseAnything ( String questionMessage , String notYesMessage )
  {
// Is the answer "yes"? No by default.
    boolean success = false ;
    if ( yes != null )
    {
// Is this the same as typed by the user?
      if ( yes . equals ( readline ( questionMessage , appMaxLengthOfInput ) ) )
      {
// This is the successful case.
        success = true ;
      }
      else
      {
// Let's print the message given to the case if the user's answer is not yes.
        outprintln ( notYesMessage ) ;
      }
    }
    else
    {
      systemexit ( "Error - yes is null, readYesElseAnything" ) ;
    }
// Now returning of the success.
    return success ;
  }
/*
** Validates the filePath.
** Acceptable file pathes are filenames, so the file path cannot conatin the file separator.
** A message will go tho the user if it is not valid.
*/
  protected final boolean isValidFilePath ( String filePath , boolean messageIfNot )
  {
// By default it is valid.
    boolean success = false ;
// This is a null value or not.
    if ( filePath != null )
    {
// This contains the file separator or not OR the appNams is in it.
      if ( ! filePath . contains ( SEP ) || filePath . contains ( appName ) )
      {
// If both are fine then it is a valid file path.
        success = true ;
      }
    }
// Message if it is not valid file path.
    if ( ! success && messageIfNot )
    {
      outprintln ( messageFilesAreAllowedFromNextToTheApplication ) ;
    }
// Returning of the result of this validation.
    return success ;
  }
/*
** Reads the file content.
** Only the first line or the whole file.
*/
  protected final String readFileContent ( String fileName , boolean firstLineHeaderOnly )
  {
// This will be the content of the file or the first line of the file.
    String contentString = "" ;
    if ( fileName != null )
    {
// A valid filepath is needed. (A file next to the application)
      if ( isValidFilePath ( fileName , true ) )
      {
// This will be the file object we are going to use.
        File file = new File ( fileName ) ;
        if ( file != null )
        {
// This file has to be existing and has to be a file.
          if ( file . exists ( ) && file . isFile ( ) )
          {
// This buffered reader will be used to get the file content.
            BufferedReader br = null ;
            try
            {
              br = new BufferedReader ( new FileReader ( fileName ) ) ;
            }
            catch ( FileNotFoundException e )
            {
              systemexit ( "Exception - FileNotFoundException, readFileContent" ) ;
            }
            if ( br != null )
            {
// The StringBuilder and the current line.
              StringBuilder sb = new StringBuilder ( ) ;
              String line = null ;
              try
              {
// Reading the first line.
                line = br . readLine ( ) ;
// And now the loop.
// Going inside if the line is not null now and the whole file content is expected.
                while ( line != null && ! firstLineHeaderOnly )
                {
                  sb . append ( line ) ;
                  sb . append ( newLineChar ) ;
                  line = br . readLine ( ) ;
                }
// One more to append the lastly read line.
                if ( line != null )
                {
                  sb . append ( line ) ;
                }
// This will be the content.
                contentString = sb . toString ( ) ;
// Closing this buffered reader.
                br . close ( ) ;
              }
              catch ( IOException e )
              {
                systemexit ( "Exception - IOException, readFileContent" ) ;
              }
// Releasable.
              sb = null ;
              line = null ;
            }
            else
            {
              systemexit ( "Error - br is null, readFileContent" ) ;
            }
// Not in use.
            br = null ;
          }
          else
          {
            outprintln ( messageYourFileHasNotBeenFoundOrNotBeenFile ) ;
          }
        }
        else
        {
          systemexit ( "Error - file is null, readFileContent" ) ;
        }
// Not in use.
        file = null ;
      }
      else
      {
        outprintln ( messageFilesAllowedBeingNextToTheApplication ) ;
      }
    }
    else
    {
      systemexit ( "Error - filePath is null, readFileContent" ) ;
    }
// At least the empty value vill go back to the caller.
    if ( contentString == null )
    {
      contentString = "" ;
    }
// Let's return this.
    return contentString ;
  }
/*
** Writing the file content into the file specified by the fileName.
*/
  protected final void writeFileContent ( String result , String fileName )
  {
// We can write into a valid filepath
    if ( isValidFilePath ( fileName , true ) )
    {
// The file object used for writing into.
      File file = new File ( fileName ) ;
      if ( file != null )
      {
// This file has to be not existing on the disk.
        if ( ! file . exists ( ) )
        {
// Creating a new empty file.
          try
          {
            file . createNewFile ( ) ;
          }
          catch ( IOException e )
          {
            systemexit ( "Exception - IOException (1), writeFileContent" ) ;
          }
// The writer objects.
          FileWriter fileWriter = null ;
          BufferedWriter bufferedWriter = null ;
          try
          {
// Trying to create a fileWriter
            fileWriter = new FileWriter ( file . getAbsoluteFile ( ) ) ;
            if ( fileWriter != null )
            {
// Trying to create a bufferedWriter
              bufferedWriter = new BufferedWriter ( fileWriter ) ;
              if ( bufferedWriter != null )
              {
// Let's write the content into the file.
                try
                {
                  bufferedWriter . write ( result ) ;
                }
                catch ( IOException e )
                {
                  systemexit ( "Exception - IOException (2), writeFileContent" ) ;
                }
              }
              else
              {
                systemexit ( "Error - bufferedWriter is null, writeFileContent" ) ;
              }
            }
            else
            {
              systemexit ( "Error - fileWriter is null, writeFileContent" ) ;
            }
          }
          catch ( IOException e )
          {
            systemexit ( "Exception - IOException (3), writeFileContent" ) ;
          }
          finally
          {
// These sould be closed if not nulls.
            if ( bufferedWriter != null )
            {
              try
              {
                bufferedWriter . close ( ) ;
              }
              catch ( IOException e )
              {
                systemexit ( "Exception - IOException (4), writeFileContent" ) ;
              }
              bufferedWriter = null ;
            }
            if ( fileWriter != null )
            {
              try
              {
                fileWriter . close ( ) ;
              }
              catch ( IOException e )
              {
                systemexit ( "Exception - IOException (5), writeFileContent" ) ;
              }
              fileWriter = null ;
            }
          }
        }
        else
        {
          systemexit ( messageResultFileAlreadyExists + fileName ) ;
        }
      }
      else
      {
        systemexit ( "Error - file is null, writeFileContent" ) ;
      }
    }
  }
}