/*
** This class is the part of the opensource MyDbConne application.
**
** See the header comment lines of Main class.
**
** Upper
** This class contains the upper level functions and methods of MyDbConne.
*/
package com . kisscodesystems . MyDbConne ;
import java . sql . Connection ;
import java . sql . DriverManager ;
import java . sql . ResultSet ;
import java . sql . Statement ;
import java . util . ArrayList ;
import java . util . Date ;
import java . util . HashMap ;
public class Upper extends Print
{
/*
** Changes the prompt depending on the actual dbType value.
*/
  protected final void changePromptToTheActual ( )
  {
    if ( dbType != null )
    {
// Changing the prompt.
// The else case is always the Postgresql.
      if ( dbType . equals ( dbTypeOracle ) )
      {
        changePrompt ( promptOracle ) ;
      }
      else if ( dbType . equals ( dbTypeMssql ) )
      {
        changePrompt ( promptMssql ) ;
      }
      else if ( dbType . equals ( dbTypeDb2 ) )
      {
        changePrompt ( promptDb2 ) ;
      }
      else
      {
        changePrompt ( promptPostgresql ) ;
      }
    }
    else
    {
      systemexit ( "Error - dbType is null, changePromptToTheActual" ) ;
    }
  }
/*
** Changes the prompt accordint to the given parameter.
** The prompt of the application can be the following:
** [appName]>
**    this is the default
** [appName] [dbType]>
**    this is when a dbType has been specified
** [appName] [dbType] [dbConn]>
**    if we have specified a database connection to use continuously
*/
  protected final void changePrompt ( String s )
  {
    if ( s != null )
    {
// This will be the string of the prompt.
      String tempPrompt = s . replace ( dbConnToChangeInPrompt , "" . equals ( dbConn ) ? "" : "" + spaceChar + dbConn ) ;
      if ( tempPrompt != null )
      {
// If the value of it is not the actual prompt
        if ( ! tempPrompt . equals ( prompt ) )
        {
// Then let the prompt be the new one.
          prompt = tempPrompt ;
        }
      }
      else
      {
        systemexit ( "Error - thePrompt is null, changePrompt" ) ;
      }
// This is no usable any more.
      tempPrompt = null ;
    }
    else
    {
      systemexit ( "Error - s is null, changePrompt" ) ;
    }
  }
/*
** Sets the value of the dbConn variable and changes the prompt depending on it.
*/
  protected final void setDbConn ( String dbconn )
  {
// Not-null object references are needed!
    if ( dbConn != null && dbconn != null )
    {
// Let the value of dbConn change if this is not set already
// and also the prompt be changed!
      if ( ! dbConn . equals ( dbconn ) )
      {
        dbConn = dbconn ;
        changePromptToTheActual ( ) ;
      }
    }
    else
    {
      systemexit ( "Error - one of these is null: dbConn|dbconn, setDbConn" ) ;
    }
  }
/*
** Shows the delimiter of the actual dbType.
** If this is not set then it will be prompted first.
*/
  protected final void showDelimiter ( )
  {
// The actual dbType value.
    String dbtype = null ;
// The dbtype value will be given from console or it is already set.
    boolean fromConsole = false ;
// Let the dbtype be changed to the actual value from dbType variable or from console.
    if ( ! "" . equals ( dbType ) )
    {
      fromConsole = false ;
      dbtype = dbType ;
    }
    else
    {
      fromConsole = true ;
      dbtype = readline ( newLineString + messageDatabaseType , appMaxLengthOfInput ) . toLowerCase ( ) ;
    }
    if ( dbtype != null )
    {
// And also has to be valid.
      if ( isValidDbType ( dbtype , true ) )
      {
// If it is from console then we change it and the prompt.
        if ( fromConsole )
        {
          dbType = dbtype ;
          changePromptToTheActual ( ) ;
        }
        if ( dbTypeOracle != null && dbTypeMssql != null && dbTypeDb2 != null && dbTypePostgresql != null )
        {
// Print the delimiter out.
// The else case is always the Postgresql.
          if ( dbTypeOracle . equals ( dbType ) )
          {
            outprintln ( newLineString + fold + dbTypeOracle + messageDelimiterHasBeenChangedTo + ( "" . equals ( delimiterOracle ) ? messageEmpty : delimiterOracle ) ) ;
          }
          else if ( dbTypeMssql . equals ( dbType ) )
          {
            outprintln ( newLineString + fold + dbTypeMssql + messageDelimiterHasBeenChangedTo + ( "" . equals ( delimiterMssql ) ? messageEmpty : delimiterMssql ) ) ;
          }
          else if ( dbTypeDb2 . equals ( dbType ) )
          {
            outprintln ( newLineString + fold + dbTypeDb2 + messageDelimiterHasBeenChangedTo + ( "" . equals ( delimiterDb2 ) ? messageEmpty : delimiterDb2 ) ) ;
          }
          else
          {
            outprintln ( newLineString + fold + dbTypePostgresql + messageDelimiterHasBeenChangedTo + ( "" . equals ( delimiterPostgresql ) ? messageEmpty : delimiterPostgresql ) ) ;
          }
        }
        else
        {
          systemexit ( "Error - one of these is null: dbTypeOracle|dbTypeMssql|dbTypeDb2|dbTypePostgresql, showDelimiter" ) ;
        }
      }
    }
    else
    {
      systemexit ( "Error - dbtype is null, showDelimiter" ) ;
    }
// Not used.
    dbtype = null ;
    fromConsole = false ;
  }
/*
** This is the method which will be executed in case of query factory commands.
** There is an infinite loop inside it that waits for the user interaction.
** These queries will be executed immediately and won't be added as a single
** or multiple queries. It uses a single database connection and this will be
** constructed in this Queries thread, not in a separate Query thread.
** This method can be called on several ways.
** d: means database type. If it is not specified and nor dbType then it will be prompted.
** c: means connection name. If it is not specified and nor dbConn then it will be prompted too.
** q: means an initial query to execute.
** The initial query is optional but we have to know
** the type of the database and the name of the database connection.
*/
  protected final void factoryQuery ( String d , String c , String q )
  {
// It requires the connections password and its decrypted content.
    if ( isFileContentConnectionsOrigReady ( ) )
    {
      if ( d != null && c != null && q != null && utils != null )
      {
// These are the dbtype and dbconn attributes.
// Please note that these are usable here!
// These are different from dbType and dbConn
// which are global properties!
        String dbtype = null ;
        String dbconn = null ;
// Let's determine the value of dbtype.
// The logic:
// - if not empty d has been specified then it will be used.
// - if empty d has been specified and the dbType is not empty then
//   this dbType value will be used.
// - if both the d and the dbType is empty then the user will be asked.
        if ( "" . equals ( d ) )
        {
          if ( ! "" . equals ( dbType ) )
          {
            dbtype = dbType ;
          }
          else
          {
            dbtype = readline ( newLineString + messageDatabaseType , appMaxLengthOfInput ) . toLowerCase ( ) ;
          }
        }
        else
        {
          dbtype = d ;
        }
// This dbtype has to be valid!
        if ( isValidDbType ( dbtype , true ) )
        {
// Determine the the value of dbconn using the same logic as the dbtype.
          if ( "" . equals ( c ) )
          {
            if ( ! "" . equals ( dbConn ) )
            {
              dbconn = dbConn ;
            }
            else
            {
              dbconn = readline ( newLineString + messageConnectionName , appMaxLengthOfInput ) ;
            }
          }
          else
          {
            dbconn = c ;
          }
// This dbtype - dbconn have to have a valid database connection.
          if ( getConnnaPos ( dbtype , dbconn ) != - 1 )
          {
// If we are here then the values and the actual prompt can be set.
            dbType = dbtype ;
            dbConn = dbconn ;
            changePromptToTheActual ( ) ;
// This is a local prompt! This will be at use only in query factory mode.
            String prompt = promptEnding ;
// This is the query string which will be executed.
            String query = null ;
// The first query execution has been done or not.
// This is important because if it is false then the q query will be executed.
// empty equals q is the value. (Empty q query string won't be executed.)
            boolean firstQueryExecuted = "" . equals ( q ) ;
// These are the part of this connection and query execution.
            Connection connection = null ;
            Statement statement = null ;
            ResultSet resultSet = null ;
// If this query has result set then this variable will store the displayable result.
            String resultTxt = null ;
// This string will contain the total elapsed time of the query.
            String elapsedFormatted = null ;
// This will be set to the actual timestamp before the query starts.
            Date startDate = null ;
// In case of non empty delimiter, the application can handle multi line sql
// queries. In this situation, only the first line will be ended with the prompt,
// and the other sql lines will be started as empty.
// This boolean variable handles the cases of we are the first sql line typed or not.
            boolean aReq = false ;
// If the request string starts with appQueryFilePrefix and the other part of the
// query is a valid named and existing file then the content of the file will be
// read and executed.
            String fileName = null ;
// This is for easier handling of the query
            String queryWithoutSpaces = null ;
// These are the properties of the database connection.
            String dbuser = getDbuser ( dbtype , dbconn ) ; ;
            char [ ] dbpass = getDbpass ( dbtype , dbconn ) . toCharArray ( ) ;
            String driver = getDriver ( dbtype , dbconn ) ;
            String connst = getConnst ( dbtype , dbconn ) ;
            try
            {
// Selecting the database driver..
              Class . forName ( driver ) ;
// ..and building the connection based on the dbuser dbpass driver and connst.
              connection = DriverManager . getConnection ( connst , dbuser , new String ( dbpass ) ) ;
// Creating the statement.
// Warning!
// This is a scrollable statement. This means that there may be datatypes that does not support this.
// (This is the reason in case of added queries of asking the user for that query is scrollable or not..)
// (The application does not ask this in query factory mode.)
              statement = connection . createStatement ( ResultSet . TYPE_SCROLL_INSENSITIVE , ResultSet . CONCUR_READ_ONLY ) ;
// If we are here then the connection and the statement object are ready for the action.
// Messages go to the user.
              outprintln ( messageYouAreNowConnected + dbType + sep9 + dbConn ) ;
              outprintln ( messageWelcomeToQueryFactory ) ;
// This is the endless loop in which the sql queries will be read from the user.
              while ( true )
              {
// This is empty at first.
                query = "" ;
                if ( ! firstQueryExecuted )
                {
// If the first query is not executed so we will do it.
                  firstQueryExecuted = true ;
                  query = q ;
                }
                else
                {
// The first query is executed or the q was empty..
// The first line should be the prompt, this var is false.
                  aReq = false ;
// In a second endless loop we read the sql.
// This is necessary while it is possible to have the
// sql delimiter different from the empty string so it
// is possible to type multi line sql.
                  while ( true )
                  {
// Prompt or empty the next read line?
                    if ( ! aReq )
                    {
// This is the first line, so this has to be set to true now.
// (The next line will be without the prompt.)
                      aReq = true ;
// Reading the next line from a line with the prompt.
                      query += console . readLine ( newLineString + prompt ) ;
                    }
                    else
                    {
// Reading the next line from a line without the prompt.
                      query += newLineString + console . readLine ( "" ) ;
                    }
// Exit from this inner endless loop if the delimiter is empty
// or when the trimmed query ends with the delimiter.
                    if ( ( ! "" . equals ( getDelimiter ( dbtype ) ) && query . trim ( ) . endsWith ( getDelimiter ( dbtype ) ) ) || "" . equals ( getDelimiter ( dbtype ) ) )
                    {
                      break ;
                    }
                  }
                }
// The query is now read.
// Trim this.
                query = trimQueryString ( query , getDelimiter ( dbtype ) ) ;
// Checking for the file prefix.
                if ( query . startsWith ( appQueryFilePrefix ) )
                {
// The filename is the content without the prefix.
                  fileName = query . substring ( appQueryFilePrefix . length ( ) , query . length ( ) ) . trim ( ) ;
// If it is a valid file then reading its content else let the query be the empty string.
                  if ( isValidFilePath ( fileName , false ) && isExistingFile ( fileName , false ) )
                  {
                    query = readFileContent ( fileName , false ) . trim ( ) ;
                  }
                  else
                  {
                    query = "" ;
                  }
                }
// Trim again.
                query = trimQueryString ( query , getDelimiter ( dbtype ) ) ;
// Let it be created.
                queryWithoutSpaces = query . replaceAll ( singleSpace , "" ) ;
// And now let's consider the possibilities.
                if ( promptToUpperOrExit . toLowerCase ( ) . equals ( query . toLowerCase ( ) ) )
                {
// Exiting from the main endless loop when the argExit has been typed.
// We jump to the message of successfully exiting.
                  break ;
                }
                else if ( query . equals ( argQuestionMark ) || query . equals ( argHelp ) )
                {
// Printing information about this query factory mode.
                  outprintln ( messageWelcomeToQueryFactory ) ;
                  outprintln ( messageYouAreNowConnected + dbType + sep9 + dbConn ) ;
                }
                else if ( queryWithoutSpaces . equals ( argDelimiter + argShow ) )
                {
// Showing the delimiter if this has been requested.
                  executeCommandDelimiterShow ( ) ;
                }
                else if ( queryWithoutSpaces . startsWith ( argDelimiter + argChange ) )
                {
// Or changing the delimiter if this has been requested.
                  executeCommandDelimiterChange ( queryWithoutSpaces . substring ( ( argDelimiter + argChange ) . length ( ) , queryWithoutSpaces . length ( ) ) ) ;
                }
                else if ( queryWithoutSpaces . startsWith ( argConnection + argDescribe ) )
                {
// Describing the currently used connection.
                  executeCommandConnectionDescribe ( ) ;
                }
                else
                {
// The else case and a not empty query string is expected.
// (Otherwise nothing will happen.)
                  if ( ! "" . equals ( query ) )
                  {
// This query string can be a select or an other type of query.
// The method to determine that this query is a select or not.
                    if ( query . toLowerCase ( ) . startsWith ( select + singleSpace ) || query . toLowerCase ( ) . contains ( singleSpace + select + singleSpace ) || query . toLowerCase ( ) . contains ( newLineString + select + newLineString ) || query . toLowerCase ( ) . contains ( newLineString + select + singleSpace ) || query . toLowerCase ( ) . contains ( singleSpace + select + newLineString ) )
                    {
// This is a select, so we will use a resultSet object to store the result of this query.
// At first we have to close this if it has been used.
                      if ( resultSet != null )
                      {
                        try
                        {
                          resultSet . close ( ) ;
                          resultSet = null ;
                        }
                        catch ( Exception e )
                        {
                          resultSet = null ;
                        }
                      }
// Starting the query running.
                      try
                      {
// This is the timestamp when the execution starts.
                        startDate = new Date ( ) ;
// Run this immediately.
                        resultSet = statement . executeQuery ( query ) ;
// Calculate the elapsed time when it is finished.
                        elapsedFormatted = utils . calculateElapsed ( new Date ( ) . getTime ( ) - startDate . getTime ( ) ) ;
// Construct the result using this elapsed time.
                        resultTxt = constructResult ( yes , resultSet , true , "" , resultFormatTxtValue , elapsedFormatted , null , dbtype , getDelimiter ( dbtype ) ) ;
// Just print it out.
                        outprint ( resultTxt ) ;
                      }
                      catch ( Exception e )
                      {
// Every type of Exception will be catched and printed out to the user.
                        outprintln ( newLineString + e . toString ( ) . trim ( ) ) ;
                      }
// This is the end of a while cycle (of a query execution).
// The user can start over now, it has the prompt soon again.
                    }
                    else
                    {
// Starting the general query running without a result set.
                      try
                      {
// This is the timestamp when the execution starts.
                        startDate = new Date ( ) ;
// Run this immediately.
                        statement . execute ( query ) ;
// Calculate the elapsed time when it is finished.
                        elapsedFormatted = utils . calculateElapsed ( new Date ( ) . getTime ( ) - startDate . getTime ( ) ) ;
// In this case, we have no result set so only the elapsed time will be printed out.
                        outprintln ( fold + elapsedFormatted ) ;
                      }
                      catch ( Exception e )
                      {
// Every type of Exception will be catched and printed out to the user.
                        outprintln ( newLineString + e . toString ( ) . trim ( ) ) ;
                      }
                    }
                  }
                }
              }
// argExis has been typed so this is the end of query factory mode.
// (The user will have the application prompt again.)
              outprintln ( messageSuccessfullyExitedFromQueryFactory ) ;
            }
            catch ( Exception e )
            {
// This exception should come from a connection problem.
              outprintln ( messageUnableToConnect ) ;
              outprintln ( fold + e . toString ( ) . trim ( ) ) ;
            }
            finally
            {
// Let's clean it up.
              if ( resultSet != null )
              {
                try
                {
                  resultSet . close ( ) ;
                }
                catch ( Exception e )
                {
                  outprintln ( messageUnableToFreeQueryFactory + e . toString ( ) . trim ( ) ) ;
                }
                resultSet = null ;
              }
              if ( statement != null )
              {
                try
                {
                  statement . close ( ) ;
                }
                catch ( Exception e )
                {
                  outprintln ( messageUnableToFreeQueryFactory + e . toString ( ) . trim ( ) ) ;
                }
                statement = null ;
              }
              if ( connection != null )
              {
                try
                {
                  connection . close ( ) ;
                }
                catch ( Exception e )
                {
                  outprintln ( messageUnableToFreeQueryFactory + e . toString ( ) . trim ( ) ) ;
                }
                connection = null ;
              }
            }
// We should release these.
            prompt = null ;
            query = null ;
            firstQueryExecuted = false ;
            connection = null ;
            statement = null ;
            resultSet = null ;
            resultTxt = null ;
            elapsedFormatted = null ;
            startDate = null ;
            aReq = false ;
            fileName = null ;
            queryWithoutSpaces = null ;
            dbuser = null ;
            dbpass = null ;
            driver = null ;
            connst = null ;
          }
          else
          {
// A valid dbType - dbConn pair is missing.
            outprintln ( messageYourConnectionDoesNotExist ) ;
          }
        }
// We should release these too.
        dbtype = null ;
        dbconn = null ;
      }
      else
      {
        systemexit ( "Error - one of these is null: d|c|q|utils, factoryQuery" ) ;
      }
    }
  }
/*
** These have to be here, later will be overrided.
*/
  protected void executeCommandDelimiterShow ( ) { }
  protected void executeCommandDelimiterChange ( String delimiter ) { }
  protected void executeCommandConnectionDescribe ( ) { }
/*
** This method is for adding a new or changing an existing connection.
** The validation of adding and changing has to be exactly the same,
** so these two is here because of this.
** To add or change we have to have a connection name: connnaIn
** This is empty or not. (Null is not allowed.)
** The complex logic of the determining of what should we do can be read below.
*/
  protected final void connectionAddOrChange ( String connnaIn )
  {
// It requires the connections password and its decrypted content.
    if ( isFileContentConnectionsOrigReady ( ) )
    {
      if ( connnaIn != null )
      {
// The default values go into these. If a connection changing
// is in progress then these will contain the original values.
        String connnaDefault = null ;
        String dbuserDefault = null ;
        char [ ] dbpassDefault = null ;
        String driverDefault = null ;
        String connstDefault = null ;
// The actual connection data will be stored in these given by the user.
        String dbtype = null ;
        String connna = null ;
        String dbuser = null ;
        char [ ] dbpass = null ;
        String driver = null ;
        String connst = null ;
// The <empty> value is one of the expecteds so this boolean is required.
        boolean innerBreak = false ;
// The application has to determine to continue or not according to the not empty connnaIn.
        boolean toContinue = true ;
// The current position of the connection.
        int currPos = - 1 ;
// At first, the dbtype is missing.
        if ( ! "" . equals ( dbType ) )
        {
// This will be the actual dbType..
          dbtype = dbType ;
        }
        else
        {
// ..or typed by the user.
          dbtype = readline ( newLineString + messageDatabaseType , appMaxLengthOfInput ) . toLowerCase ( ) ;
        }
        if ( dbtype != null )
        {
// And also has to be a valid value!
          if ( isValidDbType ( dbtype , true ) )
          {
// We think that we can continue later.
            toContinue = true ;
// This can be set now. (Yes, it can be -1.)
            currPos = getConnnaPos ( dbtype , connnaIn ) ;
// So, the connnaIn is empty or not. If the connnaIn is empty,
// the true value of toContinue will remain true.
            if ( ! "" . equals ( connnaIn ) )
            {
// If not empty then we can check now for queries using this connection.
              if ( isConnectionInUse ( dbtype , connnaIn , false ) )
              {
// We cannot continue (change this connection) if this connection is in use.
                toContinue = false ;
              }
              else
              {
// If this connection is existing..
                if ( currPos != - 1 )
                {
// ..then getting the current properties of this connection now!
                  connnaDefault = connnaIn ;
                  dbuserDefault = getDbuser ( dbtype , connnaIn ) ;
                  dbpassDefault = getDbpass ( dbtype , connnaIn ) . toCharArray ( ) ;
                  driverDefault = getDriver ( dbtype , connnaIn ) ;
                  connstDefault = getConnst ( dbtype , connnaIn ) ;
// And a message is going to the user about changing this connection.
                  outprintln ( messageChangeConnPropertyBehaviour ) ;
                }
                else
                {
// If this connnaIn is not empty and the connection doesn't exist
// Then we also cannot continue and a message will be shown to the user.
                  toContinue = false ;
                  outprintln ( messageYourConnectionDoesNotExist ) ;
                }
              }
            }
            else
            {
              if ( getNumOfAllConnections ( ) >= appMaxNumOfConnections )
              {
                toContinue = false ;
                outprintln ( messageYouHaveReachedTheTopOfTheCountOfStorableConnections ) ;
              }
            }
// If we can continue..
            if ( toContinue )
            {
// The properties of the connection have to by typed by the user.
              outprintln ( messageEnterThePropertiesOfTheConnection ) ;
// Let's start. The first is the connection name to be specified.
// If the default value is not null (changing the connection)
// then it will be displayed to the user. If null (adding a new connection)
// then empty string will displayed in the place of the default value.
// This is the logic of typing all of the connection properties.
              connna = readline ( messageConnectionName + ( connnaDefault != null ? ( "[" + connnaDefault + "]" + spaceChar ) : "" ) , appMaxLengthOfInput ) ;
              if ( connna != null )
              {
// This connna value shouldn't contain space char!
                if ( connna . lastIndexOf ( spaceChar ) == - 1 )
                {
// If the empty value has been set and a default value exists
// then the connna will be set as the default connna.
                  if ( connnaDefault != null && connna . equals ( "" ) )
                  {
                    connna = connnaDefault ;
                  }
// At the end, the connection name has to be at least one character.
                  if ( connna . length ( ) > 0 )
                  {
// This connection name has to be not existing or the value of connnaIn!
// (This last situation means that the name
// of the connection wont'be changed, the user can do that of course.)
                    if ( getConnnaPos ( dbtype , connna ) == - 1 || connnaIn . equals ( connna ) )
                    {
// The connection name is fine. The next one is the user of the database connection.
                      dbuser = readline ( messageDatabaseUser + ( dbuserDefault != null ? ( "[now " + dbuserDefault + sep9 + messageChangeConnUserOrPasswordBehaviour + "]" + spaceChar ) : "" ) , appMaxLengthOfInput ) ;
                      if ( dbuser != null )
                      {
// If an empty value typed and the default is not null
// then that default value will be used.
                        if ( dbuser . length ( ) == 0 && dbpassDefault != null )
                        {
                          dbuser = dbuserDefault ;
                        }
                        else
                        {
// If the value is not empty or the dbuserDefault is null,
// we are going to search for the <empty> string.
                          innerBreak = false ;
                          for ( int i = 0 ; i < dbuser . length ( ) ; i ++ )
                          {
                            if ( dbuser . charAt ( i ) != messageEmpty . charAt ( i ) )
                            {
                              innerBreak = true ;
                              break ;
                            }
                          }
// If this remains false that means that this value is exactly
// the <empty> string, so the user wanted to change this dbuser
// to empty, let's do that.
                          if ( ! innerBreak )
                          {
                            dbuser = "" ;
                          }
                        }
// The dbuser is fine too. The password of this database user is missing now.
                        dbpass = readpassword ( messageDatabasePassword + ( dbpassDefault != null ? ( "[" + messageChangeConnUserOrPasswordBehaviour + "]" + spaceChar ) : "" ) ) ;
                        if ( dbpass != null )
                        {
// If this is an empty dbpass and the default value is not null then
// that default value will be used.
                          if ( dbpass . length == 0 && dbpassDefault != null )
                          {
                            dbpass = dbpassDefault ;
                          }
                          else
                          {
// If the value is not empty or the dbpassDefault is null,
// we are going to search for the char array containing <empty>.
                            innerBreak = false ;
                            for ( int i = 0 ; i < dbpass . length ; i ++ )
                            {
                              if ( dbpass [ i ] != messageEmpty . charAt ( i ) )
                              {
                                innerBreak = true ;
                                break ;
                              }
                            }
// If this remains false that means that this value is exactly
// the <empty> char array, so the user wanted to change this dbpass
// to empty, let's do that.
                            if ( ! innerBreak )
                            {
                              dbpass = new char [ 0 ] ;
                            }
                          }
// The dbpass is fine, 2 more to go. The driver is the next one.
                          driver = readline ( messageDatabaseDriver + ( driverDefault != null ? ( "[" + driverDefault + "]" + spaceChar ) : "" ) , appMaxLengthOfInput ) ;
                          if ( driver != null )
                          {
// If the driver is empty and the default is not,
// we will use that.
                            if ( driverDefault != null && driver . equals ( "" ) )
                            {
                              driver = driverDefault ;
                            }
// A not empty driver is needed..
                            if ( driver . length ( ) > 0 )
                            {
// And it also has to be valid. (according the database type)
                              if ( isValidDriver ( driver , dbtype ) )
                              {
// The connection string is the last one.
                                connst = readline ( messageConnectionString + ( connstDefault != null ? ( "[" + connstDefault + "]" + spaceChar ) : "" ) , appMaxLengthOfInput ) ;
                                if ( connst != null )
                                {
// In case of empty connst and not null default connst the default value will be used.
                                  if ( connstDefault != null && connst . equals ( "" ) )
                                  {
                                    connst = connstDefault ;
                                  }
// The connst has to be at least one character.
                                  if ( connst . length ( ) > 0 )
                                  {
// This is the final point. If we are here then we are ready for test this connection.
// (It will print out the success of this testing.)
                                    testConnection ( dbuser , new String ( dbpass ) , driver , connst ) ;
// A final confirmation fom the user to save this connection.
                                    if ( readYesElseAnything ( messageSaveConnection , messageConnectionHasNotBeenSaved ) )
                                    {
// Where to insert this connection.
                                      int posToInsert = - 1 ;
// If the current position of the connection is a valid position..
                                      if ( currPos != - 1 )
                                      {
// then this position will be used.
                                        posToInsert = currPos ;
// These are to calculate the length of the old and new data of connection.
                                        int currentContentLength = dbtype . length ( ) + 1 + connnaDefault . length ( ) + 1 + dbuserDefault . length ( ) + 1 + dbpassDefault . length + 1 + driverDefault . length ( ) + 1 + connstDefault . length ( ) + 1 ;
                                        int newContentLength = dbtype . length ( ) + 1 + connna . length ( ) + 1 + dbuser . length ( ) + 1 + dbpass . length + 1 + driver . length ( ) + 1 + connst . length ( ) + 1 ;
                                        int toMoveFromPos = posToInsert + currentContentLength ;
                                        int toMoveDiff = newContentLength - currentContentLength ;
// If it is necessary, the rest of the connections cile content has to be shifted.
                                        if ( toMoveDiff != 0 )
                                        {
                                          shiftFileContent ( toMoveFromPos , toMoveDiff ) ;
                                        }
// These are not in used any more.
                                        currentContentLength = 0 ;
                                        newContentLength = 0 ;
                                        toMoveFromPos = 0 ;
                                        toMoveDiff = 0 ;
                                      }
                                      else
                                      {
// This is a not existing connection so it will be appended to the end of the existing connections.
                                        posToInsert = getFirstNewLineAndZeroCharIndex ( ) + 1 ;
                                      }
// This has to be a valid position at this point!
// Else exiting from the application!
                                      if ( posToInsert > 0 )
                                      {
// Inserting the data into the correct positions.
                                        insertAnAttributeIntoFileContentConnectionsOrig ( dbtype , posToInsert ) ;
                                        posToInsert = posToInsert + dbtype . length ( ) + 1 ;
                                        insertAnAttributeIntoFileContentConnectionsOrig ( connna , posToInsert ) ;
                                        posToInsert = posToInsert + connna . length ( ) + 1 ;
                                        insertAnAttributeIntoFileContentConnectionsOrig ( dbuser , posToInsert ) ;
                                        posToInsert = posToInsert + dbuser . length ( ) + 1 ;
                                        insertAnAttributeIntoFileContentConnectionsOrig ( dbpass , posToInsert ) ;
                                        posToInsert = posToInsert + dbpass . length + 1 ;
                                        insertAnAttributeIntoFileContentConnectionsOrig ( driver , posToInsert ) ;
                                        posToInsert = posToInsert + driver . length ( ) + 1 ;
                                        insertAnAttributeIntoFileContentConnectionsOrig ( connst , posToInsert ) ;
// Saving the file.
                                        if ( saveFile ( ) )
                                        {
// This is done, the user gets a message.
                                          outprintln ( messageConnectionHasBeenSavedSuccessfully ) ;
// Changing the prompt if necessary.
                                          if ( dbtype . equals ( dbType ) && connnaIn . equals ( dbConn ) )
                                          {
                                            setDbConn ( connna ) ;
                                          }
                                        }
                                      }
                                      else
                                      {
                                        systemexit ( "Error - posToInsert is not greater than 0, connectionAddOrChange" ) ;
                                      }
// This will be not used later.
                                      posToInsert = 0 ;
                                    }
                                  }
                                  else
                                  {
                                    outprintln ( messageTheConnectionStringCannotBeEmpty ) ;
                                  }
                                }
                                else
                                {
                                  systemexit ( "Error - connst is null, connectionAddOrChange" ) ;
                                }
                              }
                            }
                            else
                            {
                              outprintln ( messageTheDriverCannotBeEmpty ) ;
                            }
                          }
                          else
                          {
                            systemexit ( "Error - driver is null, connectionAddOrChange" ) ;
                          }
                        }
                        else
                        {
                          systemexit ( "Error - dbpass is null, connectionAddOrChange" ) ;
                        }
                      }
                      else
                      {
                        systemexit ( "Error - dbuser is null, connectionAddOrChange" ) ;
                      }
                    }
                    else
                    {
                      outprintln ( messageThisConnectionNameInDatabaseTypeAlreadyExists ) ;
                    }
                  }
                  else
                  {
                    outprintln ( messageTheNameOfTheConnectionHasToBeAtLeastOneChar ) ;
                  }
                }
                else
                {
                  outprintln ( messageTheNameOfTheConnectionCannotContainSpaceChar ) ;
                }
              }
              else
              {
                systemexit ( "Error - connna is null, connectionAddOrChange" ) ;
              }
            }
          }
        }
        else
        {
          systemexit ( "Error - dbtype is null, connectionAddOrChange" ) ;
        }
// These are releasable now.
        connnaDefault = null ;
        dbuserDefault = null ;
        dbpassDefault = null ;
        driverDefault = null ;
        connstDefault = null ;
        dbtype = null ;
        connna = null ;
        dbuser = null ;
        dbpass = null ;
        driver = null ;
        connst = null ;
        innerBreak = false ;
        toContinue = true ;
        currPos = 0 ;
      }
      else
      {
        systemexit ( "Error - connnaIn is null, connectionAddOrChange" ) ;
      }
    }
  }
/*
** The driver has to be correct according to the given database type.
*/
  protected final boolean isValidDriver ( String driver , String dbtype )
  {
// This is not succesws by default.
    boolean success = false ;
    if ( dbtype != null )
    {
// This is too.
      if ( driver != null )
      {
// By dbTypes.
// The driver has to contain the string we searching for.
// If that string is contained by the driver then the validation is successful.
// Else not and a message goes to the user.
        if ( dbtype . equals ( dbTypeOracle ) )
        {
          if ( driver . contains ( dbTypeDriverSearchOracle ) )
          {
            success = true ;
          }
          else
          {
            outprintln ( messageDriverForOracleHasToContain + dbTypeDriverSearchOracle ) ;
          }
        }
        else if ( dbtype . equals ( dbTypeMssql ) )
        {
          if ( driver . contains ( dbTypeDriverSearchMssql ) )
          {
            success = true ;
          }
          else
          {
            outprintln ( messageDriverForMssqlHasToContain + dbTypeDriverSearchMssql ) ;
          }
        }
        else if ( dbtype . equals ( dbTypeDb2 ) )
        {
          if ( driver . contains ( dbTypeDriverSearchDb2 ) )
          {
            success = true ;
          }
          else
          {
            outprintln ( messageDriverForDb2HasToContain + dbTypeDriverSearchDb2 ) ;
          }
        }
        else
        {
          if ( driver . contains ( dbTypeDriverSearchPostgresql ) )
          {
            success = true ;
          }
          else
          {
            outprintln ( messageDriverForPostgresqlHasToContain + dbTypeDriverSearchPostgresql ) ;
          }
        }
      }
      else
      {
        systemexit ( "Error - driver is null, isValidDriver" ) ;
      }
    }
    else
    {
      systemexit ( "Error - dbtype is null, isValidDriver" ) ;
    }
    return success ;
  }
/*
** Deletes a connection specified by the dbtype and connna.
** The connPos is not necessary at all, but this has been calculated by the
** caller every time, so we don't want to do this again.
** Delete a connection: shifting the connections content back if not the last
** connection is to be deleted.
*/
  protected final boolean deleteConnection ( String dbtype , String connna , int connPos )
  {
// Boolean and it will be true only if everything will be passed.
    boolean success = false ;
// The valid password and connections content.
    if ( isFileContentConnectionsOrigReady ( ) )
    {
// This has to be a valid position.
      if ( connPos > - 1 )
      {
// These are necessary to determine the index: where starts the next connection.
        int newLineCounter = 0 ;
        int nextConnPos = - 1 ;
// The connection has to be not in use to be able to delete it.
        if ( ! isConnectionInUse ( dbtype , connna , false ) )
        {
// Let's search for the next connection. Looping
          for ( int i = connPos ; i < fileContentConnectionsOrig . length ; i ++ )
          {
// Counting the new lines.
            if ( fileContentConnectionsOrig [ i ] == newLineChar )
            {
              newLineCounter ++ ;
            }
// If this is 6 then we have reached the end of the current connectioin.
            if ( newLineCounter == 6 )
            {
// The next connection begins in the next character.
// (Or, the next character can be a zero character too
// if this is the last connection. Doesn't matter.)
              nextConnPos = i + 1 ;
              break ;
            }
          }
// Has this connection a valid length?
          if ( nextConnPos - connPos > - 1 )
          {
// These are required to shift the content of the connections file.
            int toMoveFromPos = nextConnPos ;
            int toMoveDiff = connPos - nextConnPos ;
// Shifting the file content if necessary.
            if ( toMoveDiff != 0 )
            {
              shiftFileContent ( toMoveFromPos , toMoveDiff ) ;
            }
// These are not in use.
            toMoveFromPos = 0 ;
            toMoveDiff = 0 ;
// This operation is successful only at this point.
            success = true ;
          }
          else
          {
            systemexit ( "Error - nextConnPos - connPos is negative, deleteConnection" ) ;
          }
        }
// Not usable too.
        newLineCounter = 0 ;
        nextConnPos = 0 ;
      }
      else
      {
        systemexit ( "Error - connPos is negative, deleteConnection" ) ;
      }
    }
// Returning the result.
    return success ;
  }
/*
** Deletes a connection when only a connection name has been specified.
** While the dbtype is also required for deleting a connection, it will be
** questioned. The dbconn too if the connna is empty.
*/
  protected final void deleteConnection ( String connna )
  {
// It is necessary to know the password of the connections.
    if ( isFileContentConnectionsOrigReady ( ) )
    {
      if ( connna != null )
      {
// The type of database and the name of the connection to be deleted.
        String dbtype = null ;
        String dbconn = null ;
// This is the position of the connection. It is not found by default.
        int connPos = - 1 ;
// If the dbType global database type has not been specified (empty) then
// It will be questioned, otherwise that value will be used.
        if ( ! "" . equals ( dbType ) )
        {
          dbtype = dbType ;
        }
        else
        {
          dbtype = readline ( newLineString + messageDatabaseType , appMaxLengthOfInput ) . toLowerCase ( ) ;
        }
// This dbtype has to be a valid database type.
        if ( isValidDbType ( dbtype , true ) )
        {
// It the connna is empty too.. else that value will be used as the name of connection.
          if ( "" . equals ( connna ) )
          {
// As the dbtype, the dbConn global database connection will be used if it is not empty.
            if ( ! "" . equals ( dbConn ) )
            {
              dbconn = dbConn ;
            }
            else
            {
              dbconn = readline ( messageConnectionName , appMaxLengthOfInput ) ;
            }
          }
          else
          {
            dbconn = connna ;
          }
// Now the position of the connection can be searched for.
          connPos = getConnnaPos ( dbtype , dbconn ) ;
// If it has been found..
          if ( connPos != - 1 )
          {
// A final confirmation from the user is required.
            if ( readYesElseAnything ( messageAreYouSureDeleteConnection , messageYourConnectionWontBeDeleted ) )
            {
// Deleting the connection now.
              if ( deleteConnection ( dbtype , dbconn , connPos ) )
              {
// And saving immediately the changes to the disk.
                if ( saveFile ( ) )
                {
// If we are here then both the delete and the save operation has been finished, a message is going to the user.
                  outprintln ( messageYourConnectionHasBeenDeletedSuccessfully ) ;
// Changing the prompt if necessary.
// (If we are in the deleted connection now, the application will step back into the database type only.)
                  if ( dbconn . equals ( dbConn ) && dbtype . equals ( dbType ) )
                  {
                    setDbConn ( "" ) ;
                  }
                }
              }
            }
          }
          else
          {
            outprintln ( messageYourConnectionDoesNotExist ) ;
          }
        }
// These variables are not in use.
        dbtype = null ;
        dbconn = null ;
        connPos = 0 ;
      }
      else
      {
        systemexit ( "Error - connna is null, deleteConnection" ) ;
      }
    }
  }
/*
** Deletes all of the connections belonging to the specified database type.
*/
  protected final void deleteConnectionsByDbType ( String dbtype )
  {
// Just to be sure.
    if ( isValidDbType ( dbtype , true ) )
    {
      if ( utils != null )
      {
// Just for the formatting.
        int counter = 0 ;
// Counts the deleted connections.
        int counterDeleted = 0 ;
// The position of a current connection.
        int connPos = - 1 ;
// The database type is printed out to the user.
        outprintln ( newLineString + fold + dbtype ) ;
// A final confirmation is necessary from the user.
        if ( readYesElseAnything ( messageAreYouSureDeleteConnectionsByDbType , messageYourConnectionsWontBeDeleted ) )
        {
// These are the connections in the specified dbtype.
// The key is the name of the connection and the value is the ArrayList that contains the active connections.
          HashMap < String , ArrayList < Integer > > connections = getConnectionsByDbtype ( dbtype , true ) ;
          if ( connections != null )
          {
// Looping on this.
            for ( HashMap . Entry < String , ArrayList < Integer > > connection : connections . entrySet ( ) )
            {
              if ( connection != null )
              {
                if ( connection . getKey ( ) != null )
                {
                  if ( connection . getValue ( ) != null )
                  {
// If this is the first hit then an empty line is printed out.
                    if ( counter == 0 )
                    {
                      outprintln ( "" ) ;
                    }
                    counter ++ ;
// If the current connection is in use then the value arraylist contains at least one item.
                    if ( connection . getValue ( ) . size ( ) > 0 )
                    {
// If a connection is currently in use then it is unable to delete it, message to the user.
                      outprintln ( fold + connection . getKey ( ) + spaceChar + messageThisConnectionIsInUse + utils . joinArrayListInteger ( connection . getValue ( ) , sep1 ) ) ;
                    }
                    else
                    {
// The position of the current connection is..
                      connPos = getConnnaPos ( dbtype , connection . getKey ( ) ) ;
// If it is found..
                      if ( connPos != - 1 )
                      {
// ..we can delete the connection. If not then the application exits.
                        if ( deleteConnection ( dbtype , connection . getKey ( ) , connPos ) )
                        {
// Increase the counter of deleted connections and message of the successfully deleting.
                          counterDeleted ++ ;
                          outprintln ( fold + connection . getKey ( ) + messageConnectionHasBeenDeleted ) ;
                        }
                        else
                        {
                          systemexit ( "Error - deleteConnection is not successful, deleteConnectionsByDbType" ) ;
                        }
                      }
                      else
                      {
                        systemexit ( "Error - connPos is -1, deleteConnectionsByDbType" ) ;
                      }
                    }
                  }
                  else
                  {
                    systemexit ( "Error - connectionValue is null, deleteConnectionsByDbType" ) ;
                  }
                }
                else
                {
                  systemexit ( "Error - connectionKey is null, deleteConnectionsByDbType" ) ;
                }
              }
              else
              {
                systemexit ( "Error - connection is null, deleteConnectionsByDbType" ) ;
              }
            }
// A final message goes to the user.
// And saving the connections file if at least one connection is deleted.
            if ( counterDeleted > 0 )
            {
              if ( saveFile ( ) )
              {
                outprintln ( messageAllConnectionsHaveBeenHandeled ) ;
              }
            }
            else
            {
              outprintln ( messageNoConnectionsHaveBeenHandeled ) ;
            }
          }
          else
          {
            systemexit ( "Error - connections is null, deleteConnectionsByDbType" ) ;
          }
// This has to be null.
          connections = null ;
        }
// These are releasable too.
        counter = 0 ;
        counterDeleted = 0 ;
        connPos = 0 ;
      }
      else
      {
        systemexit ( "Error - utils is null, deleteConnectionsByDbType" ) ;
      }
    }
  }
/*
** We will use a specific database connection.
** The d may be empty but the not empty c is required.
** d: database type, c: connection name.
*/
  protected final void useConnection ( String d , String c )
  {
// The valid password of connections has to be known and typed.
    if ( isFileContentConnectionsOrigReady ( ) )
    {
      if ( d != null && c != null )
      {
// These will be the identifiers of the connection.
        String dbtype = null ;
        String connna = null ;
// The position of the connection, not found by default.
        int connPos = - 1 ;
// The dbtype will be determined.
// The d or the dbType global database type will be used if any of them is not empty.
// If both d and dbType is empty, the user will be questined about it.
        if ( "" . equals ( d ) )
        {
          if ( ! "" . equals ( dbType ) )
          {
            dbtype = dbType ;
          }
          else
          {
            dbtype = readline ( newLineString + messageDatabaseType , appMaxLengthOfInput ) . toLowerCase ( ) ;
          }
        }
        else
        {
          dbtype = d ;
        }
// A valid dbtype is required.
        if ( isValidDbType ( dbtype , true ) )
        {
// The name of the connection is the value of c.
          connna = c ;
// Searching for the connection.
          connPos = getConnnaPos ( dbtype , connna ) ;
// If it is found.
          if ( connPos != - 1 )
          {
// Setting the dbtype and dbconn values and changing the prompt.
            dbType = dbtype ;
            dbConn = connna ;
            changePromptToTheActual ( ) ;
          }
          else
          {
            outprintln ( messageYourConnectionDoesNotExist ) ;
          }
        }
      }
      else
      {
        systemexit ( "Error - one of these is null: d|c, useConnection" ) ;
      }
    }
  }
/*
** Describes a connection.
** Prints out the properties of a connection (except the password of database user.)
** Prints out the fact that this connection is in use or not.
*/
  protected final void describeConnection ( String connna )
  {
// Is the connections file decrypted?
    if ( isFileContentConnectionsOrigReady ( ) )
    {
      if ( connna != null )
      {
// The database type and the connection name to be described.
        String dbtype = null ;
        String dbconn = null ;
// dbType globally selected database type or given by the user.
        if ( ! "" . equals ( dbType ) )
        {
          dbtype = dbType ;
        }
        else
        {
          dbtype = readline ( newLineString + messageDatabaseType , appMaxLengthOfInput ) . toLowerCase ( ) ;
        }
// A valid dbtype required.
        if ( isValidDbType ( dbtype , true ) )
        {
// The connna or the dbConn globally selected connection name will be used if
// any of them is not empty. Else the value is used given by the user.
          if ( "" . equals ( connna ) )
          {
            if ( ! "" . equals ( dbConn ) )
            {
              dbconn = dbConn ;
            }
            else
            {
              dbconn = readline ( newLineString + messageConnectionName , appMaxLengthOfInput ) ;
            }
          }
          else
          {
            dbconn = connna ;
          }
// Is the connection existing?
          if ( getConnnaPos ( dbtype , dbconn ) != - 1 )
          {
// Let's print out the properties of the connection.
            outprintln ( messageThePropertiesOfYourConnection ) ;
            outprintln ( messageDatabaseType + dbtype ) ;
            outprintln ( messageConnectionName + dbconn ) ;
            outprintln ( messageDatabaseUser + getDbuser ( dbtype , dbconn ) ) ;
            outprintln ( messageDatabasePassword + messageHidden ) ;
            outprintln ( messageDatabaseDriver + getDriver ( dbtype , dbconn ) ) ;
            outprintln ( messageConnectionString + getConnst ( dbtype , dbconn ) ) ;
// Let's print out the usage of this connection.
            isConnectionInUse ( dbtype , dbconn , true ) ;
          }
          else
          {
            outprintln ( messageYourConnectionDoesNotExist ) ;
          }
        }
// Releasable references.
        dbtype = null ;
        dbconn = null ;
      }
      else
      {
        systemexit ( "Error - connna is null, describeConnection" ) ;
      }
    }
  }
}