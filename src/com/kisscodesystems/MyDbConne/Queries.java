/*
** This class is the part of the opensource MyDbConne application.
**
** See the header comment lines of Main class.
**
** Queries
** This implements the thread that handles all of the queries,
** in fact, this is the main thread instead of the Main class.
*/
package com . kisscodesystems . MyDbConne ;
import java . io . File ;
import java . io . IOException ;
import java . io . ObjectInputStream ;
import java . io . ObjectOutputStream ;
import java . util . Date ;
public final class Queries extends Exec
implements Runnable
{
/*
** This is the main thread of this application!
*/
  public final void run ( )
  {
// The application can handle the user's commands only if it is ready.
    boolean ready = false ;
// Should we create the connections encrypted file or not.
    boolean connectionsStuffNeeded = true ;
// The elements of the connections folder will be stored here.
    File [ ] connectionsFiles = null ;
// The content of the initially created connections file.
    String connectionsIniContent = null ;
// These objects are for processing the user's input.
    String requestString = null ;
    String [ ] requestParams = null ;
// Getting the active queries into this when exiting from the application.
    String activeQueries = null ;
// Can we break the whole application? (exiting regulary)
    boolean canBreak = false ;
// These are the globally set dbType and dbConn string objects!
// dbType: type of the database
// dbConn: the name of the currently and continuously used database connection.
// Remember: a database connection is identifyable by this two propertyes.
    dbType = "" ;
    dbConn = "" ;
// Changing the application prompt immediately.
// (Selecting and displaying just the application prompt.)
    changePrompt ( promptApp ) ;
// The console object is needed as not-null!
    if ( console != null && utils != null )
    {
// The argument object from the command line (when the application was started) has to be valid.
      if ( isGoodArgsObject ( args ) )
      {
// Now creating the object of the connections folder.
        connectionsDirFolder = new File ( appConnectionsDir ) ;
        if ( connectionsDirFolder != null )
        {
          if ( argHelp != null && argQuestionMark != null && argApplication != null && argDescribe != null && argStory != null && argWelcome != null && argScreen != null )
          {
// Initially, we should create connections content.
            connectionsStuffNeeded = true ;
// We have to create connections things except
// - we have "?" or "help"
// - or we have "application describe" or "application story" or "welcome screen"
// - or we have "connections good password"
            if ( ( ( args . length == 1 ) && ( argQuestionMark . equals ( args [ 0 ] . toLowerCase ( ) ) || argHelp . equals ( args [ 0 ] . toLowerCase ( ) ) ) ) || ( ( args . length == 2 ) && ( ( argApplication . equals ( args [ 0 ] . toLowerCase ( ) ) && argDescribe . equals ( args [ 1 ] . toLowerCase ( ) ) ) || ( argApplication . equals ( args [ 0 ] . toLowerCase ( ) ) && argStory . equals ( args [ 1 ] . toLowerCase ( ) ) ) || ( argWelcome . equals ( args [ 0 ] . toLowerCase ( ) ) && argScreen . equals ( args [ 1 ] . toLowerCase ( ) ) ) ) ) || ( ( args . length == 3 ) && ( argConnections . equals ( args [ 0 ] . toLowerCase ( ) ) && argGood . equals ( args [ 1 ] . toLowerCase ( ) ) && argPassword . equals ( args [ 2 ] . toLowerCase ( ) ) ) ) )
            {
              connectionsStuffNeeded = false ;
            }
// We should create connections content if it should be created and it currently not exisging!
            if ( ! connectionsDirFolder . exists ( ) && connectionsStuffNeeded )
            {
// The welcome screen appears this time.
              outprintln ( messageWelcomeScreen ) ;
              if ( yes != null )
              {
// If the user types that the folder is safe to use this application in.
                if ( yes . equals ( readline ( messageIsFolderSafe , appMaxLengthOfInput ) ) )
                {
// Let's create the folder of the connections files.
                  connectionsDirFolder . mkdirs ( ) ;
// Listing the folder.
                  connectionsFiles = connectionsDirFolder . listFiles ( ) ;
// It is null?
                  if ( connectionsFiles != null )
                  {
// If the folder contains no elements..
                    if ( connectionsFiles . length == 0 )
                    {
// This is it. Let the user be warned to remember its password!
                      outprintln ( messageDoNotForgetYourConnectionsPassword ) ;
// Reading the password.
                      readPassword ( true ) ;
// Now let's create the content of the new connections file.
// At first we have to clear the content by the zeroChar just to be sure..
                      utils . clearCharArray ( fileContentConnectionsOrig , zeroChar ) ;
// Creating the new array of characters.
                      fileContentConnectionsOrig = new char [ appFileContentMaxLength ] ;
// This also should be cleared by zeroChar.
                      utils . clearCharArray ( fileContentConnectionsOrig , zeroChar ) ;
// Creating the content of the connections.
// This will contain the connections header string and a date + message.
                      connectionsIniContent = "" + connectionsHeader + simpleDateFormatForDisplaying . format ( new Date ( ) ) + sep9 + messageLogApplicationInstanceInitialize + newLineChar ;
                      if ( connectionsIniContent != null )
                      {
// Let's add this string content into the character array char-by-char.
                        for ( int i = 0 ; i < Math . min ( connectionsIniContent . length ( ) , appFileContentMaxLength ) ; i ++ )
                        {
                          fileContentConnectionsOrig [ i ] = connectionsIniContent . charAt ( i ) ;
                        }
// Let's save the connections file finally.
                        if ( saveFile ( ) )
                        {
// The saving operation is successfully finished so the user gets a message.
                          outprintln ( messageConnectionsFileHasBeenCreated ) ;
// And we are ready to start of course.
                          ready = true ;
                        }
                      }
                      else
                      {
                        systemexit ( "Error - connectionsIniContent is null, main" ) ;
                      }
                    }
                    else
                    {
                      systemexit ( "Error - Connections folder is not empty, main" ) ;
                    }
                  }
                  else
                  {
                    systemexit ( "Error - connectionsDirFolder is null, main" ) ;
                  }
                }
                else
                {
                  systemexit ( "Error - Folder is not safe by answer, main" ) ;
                }
              }
              else
              {
                systemexit ( "Error - yes is null, main" ) ;
              }
            }
            else
            {
// If the connections folder is existing but the connection stuff is needed..
              if ( connectionsStuffNeeded )
              {
// ..then the password will be questioned to the user.
                readPassword ( false ) ;
// Trying to decrypt the file using the user's password.
                if ( getFileContent ( ) )
                {
// If the decryption is successful then a message goes to the user.
                  outprintln ( messageWelcomeScreen ) ;
                  printIniMessage ( ) ;
// Just for formatting..
                  if ( args . length == 0 )
                  {
                    outprintln ( "" ) ;
                  }
// And we are ready too.
                  ready = true ;
                }
              }
              else
              {
// The connection stuff is not needed so let's give the args to the letsWork method!
// So, we are not ready, the application won't wait continuously for the user's input.
// see: where connectionsStuffNeeded = false ;
                letsWork ( args ) ;
              }
            }
// If we are ready for listen to the user's input
            if ( ready )
            {
// Let these be null.
              requestString = null ;
              requestParams = null ;
// This is the endless while loop for listening to the user continuously.
// Will be broken only if the user types exit.
              while ( true )
              {
// At first we check the original args object: can we do anything with it.
// In case of empty args the application prompt will be shown to the user.
                if ( args . length == 0 )
                {
                  requestString = readiline ( prompt ) . trim ( ) ;
                }
                else
                {
                  requestString = "" ;
                }
                if ( requestString != null )
                {
// Empty args: the requestParams has to be constructed from the user's input.
// else the args object has to be used.
// (If this has been happened, the args has to be replaced by an empty args array!)
                  if ( args . length == 0 )
                  {
                    requestParams = utils . requestStringSplit ( requestString , doubleSpace , doubleQuote , singleSpace , backsla , keyBeginEnd ) ;
                  }
                  else
                  {
                    requestParams = args ;
                    args = new String [ 0 ] ;
                  }
// Some of the commands are considered right here.
                  if ( dbTypeMysql . toLowerCase ( ) . equals ( requestString . toLowerCase ( ) ) )
                  {
                    dbType = dbTypeMysql ;
                    dbConn = "" ;
                    changePrompt ( promptMysql ) ;
                  }
                  else if ( dbTypeOracle . toLowerCase ( ) . equals ( requestString . toLowerCase ( ) ) )
                  {
                    dbType = dbTypeOracle ;
                    dbConn = "" ;
                    changePrompt ( promptOracle ) ;
                  }
                  else if ( dbTypeMssql . toLowerCase ( ) . equals ( requestString . toLowerCase ( ) ) )
                  {
                    dbType = dbTypeMssql ;
                    dbConn = "" ;
                    changePrompt ( promptMssql ) ;
                  }
                  else if ( dbTypeDb2 . toLowerCase ( ) . equals ( requestString . toLowerCase ( ) ) )
                  {
                    dbType = dbTypeDb2 ;
                    dbConn = "" ;
                    changePrompt ( promptDb2 ) ;
                  }
                  else if ( dbTypePostgresql . toLowerCase ( ) . equals ( requestString . toLowerCase ( ) ) )
                  {
                    dbType = dbTypePostgresql ;
                    dbConn = "" ;
                    changePrompt ( promptPostgresql ) ;
                  }
// The user wanted to step upper level.
                  else if ( promptToUpperOrExit . toLowerCase ( ) . equals ( requestString . toLowerCase ( ) ) )
                  {
// If the dbType is empty then leave the application.
                    if ( "" . equals ( dbType ) )
                    {
                      canBreak = false ;
// The active queries are..
                      activeQueries = getActiveQueries ( ) ;
// If this is empty
                      if ( "" . equals ( activeQueries ) )
                      {
// ..then the user can exit immediately.
                        canBreak = true ;
                      }
                      else
                      {
// There are active queries so the user has to confirm the exiting.
                        outprintln ( messageRunningQueries + activeQueries ) ;
                        if ( readYesElseAnything ( messageAreYouSureWantToCancelAllActiveQueriesAndExit , "" ) )
                        {
// Cancelling all of the queries.
                          if ( cancelAllQueries ( false ) )
                          {
// In case of its success, we can break the infinite while loop to exit from this application.
                            canBreak = true ;
                          }
                        }
                      }
// Only if We can break this while loop
                      if ( canBreak )
                      {
// Deleting all of the queries first
                        deleteAllQueries ( false ) ;
// Than break and exit.
                        break ;
                      }
                    }
// The dbConn is empty, then
                    else if ( "" . equals ( dbConn ) )
                    {
// The dbType also will be empty
                      dbType = "" ;
// And the prompt will be the application prompt.
                      changePrompt ( promptApp ) ;
                    }
                    else
                    {
// The dbConn was not empty so let it be empty now.
// (and let the prompt be actualized)
                      setDbConn ( "" ) ;
                    }
                  }
// The else case.
                  else
                  {
// If the argument is valid
                    if ( isGoodArgsObject ( requestParams ) )
                    {
// Then this will be added to the letsWork for further execution.
                      letsWork ( requestParams ) ;
                    }
                  }
                }
                else
                {
// The request string is null so the user has to read a message about it.
                  usageWrongParameters ( ) ;
                }
// This is for formatting only.
                outprintln ( "" ) ;
              }
// A final message is going to the user because the endless while loop has been broken.
              outprintln ( messageBye ) ;
// These are not necessary any more.
              requestString = null ;
              requestParams = null ;
            }
          }
          else
          {
            systemexit ( "Error - One of these is null: argHelp|argQuestionMark|argApplication|argDescribe|argStory|argWelcome|argScreen, main" ) ;
          }
        }
        else
        {
          systemexit ( "Error - connectionsDirFolder is null, main" ) ;
        }
      }
    }
    else
    {
      outprintln ( "Error - one of these is null: utils|console, main." ) ;
    }
// These are not usable any more.
    ready = false ;
    connectionsStuffNeeded = false ;
    connectionsFiles = null ;
    connectionsIniContent = null ;
    requestString = null ;
    requestParams = null ;
    activeQueries = null ;
    canBreak = false ;
  }
/*
** This method is for routing the args into the correct method to be executed.
*/
  private final void letsWork ( String [ ] args )
  {
// If the connections password is not cached then these has to be cleared!
    if ( ! appConnectionPasswordCache )
    {
      clearCharArrays ( ) ;
      clearByteArrays ( ) ;
    }
    if ( args != null )
    {
// Route the args object into the correct method depending on the
// size and the calues of its elements.
// If the content of the args is not the expected then the user
// gets a message about the wrong parameters.
      if ( args . length == 0 ) { }
      else if ( args . length == 1 )
      {
        if ( argQuestionMark . equals ( args [ 0 ] . toLowerCase ( ) ) )
        {
          executeCommandHints ( ) ;
        }
        else if ( argHelp . equals ( args [ 0 ] . toLowerCase ( ) ) )
        {
          executeCommandHelp ( ) ;
        }
        else
        {
          if ( ! "" . equals ( args [ 0 ] ) )
          {
            usageWrongParameters ( ) ;
          }
        }
      }
      else if ( args . length == 2 )
      {
        if ( argApplication . equals ( args [ 0 ] . toLowerCase ( ) ) && argDescribe . equals ( args [ 1 ] . toLowerCase ( ) ) )
        {
          executeCommandApplicationDescribe ( ) ;
        }
        else if ( argApplication . equals ( args [ 0 ] . toLowerCase ( ) ) && argStory . equals ( args [ 1 ] . toLowerCase ( ) ) )
        {
          executeCommandApplicationStory ( ) ;
        }
        else if ( argWelcome . equals ( args [ 0 ] . toLowerCase ( ) ) && argScreen . equals ( args [ 1 ] . toLowerCase ( ) ) )
        {
          executeCommandWelcomeScreen ( ) ;
        }
        else if ( argConnection . equals ( args [ 0 ] . toLowerCase ( ) ) && argListall . equals ( args [ 1 ] . toLowerCase ( ) ) )
        {
          executeCommandConnectionListall ( ) ;
        }
        else if ( argConnection . equals ( args [ 0 ] . toLowerCase ( ) ) && argAdd . equals ( args [ 1 ] . toLowerCase ( ) ) )
        {
          executeCommandConnectionAdd ( ) ;
        }
        else if ( argConnection . equals ( args [ 0 ] . toLowerCase ( ) ) && argDescribe . equals ( args [ 1 ] . toLowerCase ( ) ) )
        {
          executeCommandConnectionDescribe ( ) ;
        }
        else if ( argConnection . equals ( args [ 0 ] . toLowerCase ( ) ) && argChange . equals ( args [ 1 ] . toLowerCase ( ) ) )
        {
          executeCommandConnectionChange ( ) ;
        }
        else if ( argConnection . equals ( args [ 0 ] . toLowerCase ( ) ) && argDelete . equals ( args [ 1 ] . toLowerCase ( ) ) )
        {
          executeCommandConnectionDelete ( ) ;
        }
        else if ( argConnection . equals ( args [ 0 ] . toLowerCase ( ) ) && argDeleteall . equals ( args [ 1 ] . toLowerCase ( ) ) )
        {
          executeCommandConnectionDeleteall ( ) ;
        }
        else if ( argConnection . equals ( args [ 0 ] . toLowerCase ( ) ) && argTest . equals ( args [ 1 ] . toLowerCase ( ) ) )
        {
          executeCommandConnectionTest ( ) ;
        }
        else if ( argQuery . equals ( args [ 0 ] . toLowerCase ( ) ) && argListall . equals ( args [ 1 ] . toLowerCase ( ) ) )
        {
          executeCommandQueryListall ( ) ;
        }
        else if ( argQuery . equals ( args [ 0 ] . toLowerCase ( ) ) && argFactory . equals ( args [ 1 ] . toLowerCase ( ) ) )
        {
          executeCommandQueryFactory ( ) ;
        }
        else if ( argQuery . equals ( args [ 0 ] . toLowerCase ( ) ) && argCancelall . equals ( args [ 1 ] . toLowerCase ( ) ) )
        {
          executeCommandQueryCancelall ( ) ;
        }
        else if ( argQuery . equals ( args [ 0 ] . toLowerCase ( ) ) && argDeleteall . equals ( args [ 1 ] . toLowerCase ( ) ) )
        {
          executeCommandQueryDeleteall ( ) ;
        }
        else if ( argDelimiter . equals ( args [ 0 ] . toLowerCase ( ) ) && argShow . equals ( args [ 1 ] . toLowerCase ( ) ) )
        {
          executeCommandDelimiterShow ( ) ;
        }
        else if ( argDelimiter . equals ( args [ 0 ] . toLowerCase ( ) ) && argChange . equals ( args [ 1 ] . toLowerCase ( ) ) )
        {
          executeCommandDelimiterChange ( "" ) ;
        }
        else
        {
          usageWrongParameters ( ) ;
        }
      }
      else if ( args . length == 3 )
      {
        if ( argConnection . equals ( args [ 0 ] . toLowerCase ( ) ) && argList . equals ( args [ 1 ] . toLowerCase ( ) ) && argActive . equals ( args [ 2 ] . toLowerCase ( ) ) )
        {
          executeCommandConnectionListActive ( ) ;
        }
        else if ( argConnection . equals ( args [ 0 ] . toLowerCase ( ) ) && argList . equals ( args [ 1 ] . toLowerCase ( ) ) && argInactive . equals ( args [ 2 ] . toLowerCase ( ) ) )
        {
          executeCommandConnectionListInactive ( ) ;
        }
        else if ( argConnection . equals ( args [ 0 ] . toLowerCase ( ) ) && argLoad . equals ( args [ 1 ] . toLowerCase ( ) ) )
        {
          executeCommandConnectionLoad ( args [ 2 ] ) ;
        }
        else if ( argConnection . equals ( args [ 0 ] . toLowerCase ( ) ) && argDescribe . equals ( args [ 1 ] . toLowerCase ( ) ) )
        {
          executeCommandConnectionDescribe ( args [ 2 ] ) ;
        }
        else if ( argConnection . equals ( args [ 0 ] . toLowerCase ( ) ) && argChange . equals ( args [ 1 ] . toLowerCase ( ) ) )
        {
          executeCommandConnectionChange ( args [ 2 ] ) ;
        }
        else if ( argConnection . equals ( args [ 0 ] . toLowerCase ( ) ) && argDelete . equals ( args [ 1 ] . toLowerCase ( ) ) )
        {
          executeCommandConnectionDelete ( args [ 2 ] ) ;
        }
        else if ( argConnection . equals ( args [ 0 ] . toLowerCase ( ) ) && argUse . equals ( args [ 1 ] . toLowerCase ( ) ) )
        {
          executeCommandConnectionUse ( args [ 2 ] ) ;
        }
        else if ( argConnection . equals ( args [ 0 ] . toLowerCase ( ) ) && argTest . equals ( args [ 1 ] . toLowerCase ( ) ) )
        {
          executeCommandConnectionTest ( args [ 2 ] ) ;
        }
        else if ( argQuery . equals ( args [ 0 ] . toLowerCase ( ) ) && argList . equals ( args [ 1 ] . toLowerCase ( ) ) && argActive . equals ( args [ 2 ] . toLowerCase ( ) ) )
        {
          executeCommandQueryListActive ( ) ;
        }
        else if ( argQuery . equals ( args [ 0 ] . toLowerCase ( ) ) && argList . equals ( args [ 1 ] . toLowerCase ( ) ) && argInactive . equals ( args [ 2 ] . toLowerCase ( ) ) )
        {
          executeCommandQueryListInactive ( ) ;
        }
        else if ( argQuery . equals ( args [ 0 ] . toLowerCase ( ) ) && argAdd . equals ( args [ 1 ] . toLowerCase ( ) ) && argSingle . equals ( args [ 2 ] . toLowerCase ( ) ) )
        {
          executeCommandQueryAddSingle ( ) ;
        }
        else if ( argQuery . equals ( args [ 0 ] . toLowerCase ( ) ) && argAdd . equals ( args [ 1 ] . toLowerCase ( ) ) && argMultiple . equals ( args [ 2 ] . toLowerCase ( ) ) )
        {
          executeCommandQueryAddMultiple ( ) ;
        }
        else if ( argQuery . equals ( args [ 0 ] . toLowerCase ( ) ) && argAdd . equals ( args [ 1 ] . toLowerCase ( ) ) && argBatch . equals ( args [ 2 ] . toLowerCase ( ) ) )
        {
          executeCommandQueryAddBatch ( ) ;
        }
        else if ( argQuery . equals ( args [ 0 ] . toLowerCase ( ) ) && argFactory . equals ( args [ 1 ] . toLowerCase ( ) ) )
        {
          executeCommandQueryFactory ( args [ 2 ] ) ;
        }
        else if ( argQuery . equals ( args [ 0 ] . toLowerCase ( ) ) && argDescribe . equals ( args [ 1 ] . toLowerCase ( ) ) )
        {
          executeCommandQueryDescribe ( args [ 2 ] ) ;
        }
        else if ( argQuery . equals ( args [ 0 ] . toLowerCase ( ) ) && argRun . equals ( args [ 1 ] . toLowerCase ( ) ) )
        {
          executeCommandQueryRun ( args [ 2 ] ) ;
        }
        else if ( argQuery . equals ( args [ 0 ] . toLowerCase ( ) ) && argCancel . equals ( args [ 1 ] . toLowerCase ( ) ) )
        {
          executeCommandQueryCancel ( args [ 2 ] ) ;
        }
        else if ( argQuery . equals ( args [ 0 ] . toLowerCase ( ) ) && argDelete . equals ( args [ 1 ] . toLowerCase ( ) ) )
        {
          executeCommandQueryDelete ( args [ 2 ] ) ;
        }
        else if ( argResult . equals ( args [ 0 ] . toLowerCase ( ) ) && argEcho . equals ( args [ 1 ] . toLowerCase ( ) ) )
        {
          executeCommandResultEcho ( args [ 2 ] ) ;
        }
        else if ( argDelimiter . equals ( args [ 0 ] . toLowerCase ( ) ) && argChange . equals ( args [ 1 ] . toLowerCase ( ) ) )
        {
          executeCommandDelimiterChange ( args [ 2 ] ) ;
        }
        else if ( argConnections . equals ( args [ 0 ] . toLowerCase ( ) ) && argGood . equals ( args [ 1 ] . toLowerCase ( ) ) && argPassword . equals ( args [ 2 ] . toLowerCase ( ) ) )
        {
          executeCommandConnectionsGoodPassword ( ) ;
        }
        else if ( argConnections . equals ( args [ 0 ] . toLowerCase ( ) ) && argPassword . equals ( args [ 1 ] . toLowerCase ( ) ) && argChange . equals ( args [ 2 ] . toLowerCase ( ) ) )
        {
          executeCommandConnectionsPasswordChange ( ) ;
        }
        else
        {
          usageWrongParameters ( ) ;
        }
      }
      else if ( args . length == 4 )
      {
        if ( argConnection . equals ( args [ 0 ] . toLowerCase ( ) ) && argUse . equals ( args [ 1 ] . toLowerCase ( ) ) )
        {
          executeCommandConnectionUse ( args [ 2 ] , args [ 3 ] ) ;
        }
        else if ( argQuery . equals ( args [ 0 ] . toLowerCase ( ) ) && argFactory . equals ( args [ 1 ] . toLowerCase ( ) ) )
        {
          executeCommandQueryFactory ( args [ 2 ] , args [ 3 ] ) ;
        }
        else
        {
          usageWrongParameters ( ) ;
        }
      }
      else if ( args . length == 5 )
      {
        if ( argQuery . equals ( args [ 0 ] . toLowerCase ( ) ) && argFactory . equals ( args [ 1 ] . toLowerCase ( ) ) )
        {
          executeCommandQueryFactory ( args [ 2 ] , args [ 3 ] , args [ 4 ] ) ;
        }
        else
        {
          usageWrongParameters ( ) ;
        }
      }
      else
      {
        usageWrongParameters ( ) ;
      }
    }
    else
    {
      systemexit ( "Error - args is null, letsWork" ) ;
    }
// If the connections password is not cached then these has to be cleared
// at the end of letsWork too!
    if ( ! appConnectionPasswordCache )
    {
      clearCharArrays ( ) ;
      clearByteArrays ( ) ;
    }
  }
// These are for prevent serialize-deserialize this object.
  private final void readObject ( ObjectInputStream in )
    throws IOException
  {
    throw new IOException ( "" ) ;
  }
  private final void writeObject ( ObjectOutputStream out )
    throws IOException
  {
    throw new IOException ( "" ) ;
  }
}