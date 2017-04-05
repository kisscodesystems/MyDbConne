/*
** This class is the part of the opensource MyDbConne application.
**
** See the header comment lines of Main class.
**
** Exec
** This is the class of the executor methods.
** These methods will be called from the letsWork router method.
** These are in the same order as the help or ? lists the commands.
*/
package com . kisscodesystems . MyDbConne ;
import java . sql . Connection ;
import java . sql . ResultSet ;
import java . sql . Statement ;
import java . util . ArrayList ;
import java . util . Date ;
import java . util . HashMap ;
public class Exec extends Upper
{
/*
** Describes the application as prints the app... variables.
*/
  protected final void executeCommandApplicationDescribe ( )
  {
    outprintln ( messageApplicationDescribe ) ;
  }
/*
** Prints the story of this application.
*/
  protected final void executeCommandApplicationStory ( )
  {
    outprintln ( messageApplicationStory ) ;
  }
/*
** Prints the welcome screen.
*/
  protected final void executeCommandWelcomeScreen ( )
  {
    outprintln ( messageWelcomeScreen ) ;
  }
/*
** Lists the active connections. (From the content of the connections file.)
** Active connection means a connection which is used at least one
** added query no matter what is the current state of that query.
*/
  protected final void executeCommandConnectionListActive ( )
  {
    listConnections ( typeToListActive ) ;
  }
/*
** Lists the inactive connections. (From the content of the connections file.)
** Inactive connection means that no added query use that query.
*/
  protected final void executeCommandConnectionListInactive ( )
  {
    listConnections ( typeToListInactive ) ;
  }
/*
** Lists all of the connections. (From the content of the connections file.)
** All means active or inactive connections.
*/
  protected final void executeCommandConnectionListall ( )
  {
    listConnections ( typeToListAll ) ;
  }
/*
** Adds a connection into the end of all of the currently existing connections.
** The empty string will go as the parameter of connectionAddOrChange, this
** means adding the new connection.
*/
  protected final void executeCommandConnectionAdd ( )
  {
    connectionAddOrChange ( "" ) ;
  }
/*
** This method reads connections from a given (unencrypted) file and appends
** this content into the end of the current content of the connections encrypted
** file. The validation is the same as manually adding a new connection during
** the processing of the file.
*/
  protected final void executeCommandConnectionLoad ( String fileName )
  {
// A valid and decrypted connections file is needed.
    if ( isFileContentConnectionsOrigReady ( ) )
    {
// The number od connections stored already in the connections file.
      int connectionsInitialCount = getNumOfAllConnections ( ) ;
// This now have to be smaller than the upper bound.
      if ( connectionsInitialCount < appMaxNumOfConnections )
      {
// The file path has to be valid and the file has to be existing.
        if ( isValidFilePath ( fileName , true ) && isExistingFile ( fileName , true ) )
        {
// Asking the user for the formatting of the file.
          if ( readYesElseAnything ( messageIsFileGoodFormatted , messageFileWontBeUsed ) )
          {
// This will be the content of the file.
            String contentString = readFileContent ( fileName , false ) ;
// This will be the array of the above.
            String [ ] contentArray = null ;
// Counters.
            int counter = 0 ;
            int counterLoaded = 0 ;
// These are the actual prooperties of the currently processed connection.
            String dbtype = null ;
            String connna = null ;
            String dbuser = null ;
            String dbpass = null ;
            String driver = null ;
            String connst = null ;
// This would be the position where to insert the data of the new connection.
            int posToInsert = - 1 ;
            if ( contentString != null )
            {
              contentArray = contentString . split ( newLineString ) ;
              if ( contentArray != null )
              {
// The first validation. The connection contains 6 different property
// so the elements of the content array have to have the count of multiple of 6.
                if ( contentArray . length % 6 == 0 )
                {
// A confirmation is required from the user to append.
                  if ( readYesElseAnything ( messageAppendThisFileIntoTheEndOfCurrentConnections , messageConnectionsWillNotBeImported ) )
                  {
// Initialize the counters.
                    counter = 0 ;
                    counterLoaded = 0 ;
// Looping on the content array.
                    for ( int i = 0 ; i < contentArray . length ; i ++ )
                    {
// Processing a connection only if we are on the first proprty of the connection.
// Remember: a connection in the connections file is:
// database_type\n
// database_connection_name\n
// database_user\n
// database_password\n
// database_driver\n
// database_connection_string\n
                      if ( i % 6 == 0 )
                      {
// Just for formatting..
                        if ( counter == 0 )
                        {
                          outprintln ( "" ) ;
                        }
// Plus one processing of connection starts.
                        counter ++ ;
// The properties of the new connection are:
                        dbtype = contentArray [ i ] ;
                        connna = contentArray [ i + 1 ] ;
                        dbuser = contentArray [ i + 2 ] ;
                        dbpass = contentArray [ i + 3 ] ;
                        driver = contentArray [ i + 4 ] ;
                        connst = contentArray [ i + 5 ] ;
// By default, no place to insert into.
                        posToInsert = - 1 ;
// Printing out the base parameters of the current connection.
                        outprint ( messageLoadingConnection + dbtype + spaceChar + "-" + spaceChar + connna + spaceChar + doubleDot + spaceChar ) ;
// Let's validate this connections as it would be added from the connection add command.
                        if ( isValidDbType ( dbtype , false ) )
                        {
                          if ( connna . lastIndexOf ( spaceChar ) == - 1 )
                          {
                            if ( connna . length ( ) > 0 )
                            {
                              if ( getConnnaPos ( dbtype , connna ) == - 1 )
                              {
                                if ( driver . length ( ) > 0 )
                                {
                                  if ( connst . length ( ) > 0 )
                                  {
// OK, the connection seems to be valid, find a position to insert it.
                                    posToInsert = getFirstNewLineAndZeroCharIndex ( ) + 1 ;
// This has to be valid of course.
                                    if ( posToInsert != - 1 )
                                    {
// Inserting the properties of the new connection.
                                      insertAnAttributeIntoFileContentConnectionsOrig ( dbtype , posToInsert ) ;
                                      posToInsert = posToInsert + dbtype . length ( ) + 1 ;
                                      insertAnAttributeIntoFileContentConnectionsOrig ( connna , posToInsert ) ;
                                      posToInsert = posToInsert + connna . length ( ) + 1 ;
                                      insertAnAttributeIntoFileContentConnectionsOrig ( dbuser , posToInsert ) ;
                                      posToInsert = posToInsert + dbuser . length ( ) + 1 ;
                                      insertAnAttributeIntoFileContentConnectionsOrig ( dbpass , posToInsert ) ;
                                      posToInsert = posToInsert + dbpass . length ( ) + 1 ;
                                      insertAnAttributeIntoFileContentConnectionsOrig ( driver , posToInsert ) ;
                                      posToInsert = posToInsert + driver . length ( ) + 1 ;
                                      insertAnAttributeIntoFileContentConnectionsOrig ( connst , posToInsert ) ;
// Finalize the lastly printed message with a done.
                                      outprintln ( messageDone ) ;
// This counter has to be increased because plus one connection has been added.
                                      counterLoaded ++ ;
                                      if ( connectionsInitialCount + counterLoaded >= appMaxNumOfConnections )
                                      {
                                        outprintln ( messageYouHaveReachedTheTopOfTheCountOfStorableConnections ) ;
                                        break ;
                                      }
                                    }
                                    else
                                    {
                                      systemexit ( "Error - posToInsert is -1, executeCommandConnectionLoad" ) ;
                                    }
                                  }
                                  else
                                  {
                                    outprintln ( messageConnectionLoadingFailedConnectionStringCannotBeEmpty ) ;
                                  }
                                }
                                else
                                {
                                  outprintln ( messageConnectionLoadingFailedDriverCannotBeEmpty ) ;
                                }
                              }
                              else
                              {
                                outprintln ( messageConnectionLoadingFailedExistingConnection ) ;
                              }
                            }
                            else
                            {
                              outprintln ( messageConnectionLoadingFailedConnectionNameCannotBeEmpty ) ;
                            }
                          }
                          else
                          {
                            outprintln ( messageConnectionLoadingFailedSpaceInConnectionName ) ;
                          }
                        }
                        else
                        {
                          outprintln ( messageConnectionLoadingFailedWrongDatabaseType ) ;
                        }
                      }
                    }
// If at least one connection has been loaded.
                    if ( counterLoaded > 0 )
                    {
// Saving the file.
                      if ( saveFile ( ) )
                      {
// A final message are going to the user.
                        if ( counter == counterLoaded )
                        {
                          outprintln ( messageAllOfTheConnectionsHaveBeenLoaded + counterLoaded ) ;
                        }
                        else
                        {
                          if ( counterLoaded == 1 )
                          {
                            outprintln ( messageConnectionHaveBeenLoaded ) ;
                          }
                          else
                          {
                            outprintln ( newLineString + fold + counterLoaded + messageConnectionsHaveBeenLoaded ) ;
                          }
                        }
                      }
                    }
                    else
                    {
                      outprintln ( messageNoConnectionsHaveBeenLoaded ) ;
                    }
                  }
                }
                else
                {
                  outprintln ( messageCountOfLinesOfFileIsNotTheExpected ) ;
                }
              }
              else
              {
                systemexit ( "Error - contentArray is null, executeCommandConnectionLoad" ) ;
              }
            }
            else
            {
              systemexit ( "Error - contentString is null, executeCommandConnectionLoad" ) ;
            }
          }
        }
      }
      else
      {
        outprintln ( messageYouHaveReachedTheTopOfTheCountOfStorableConnections ) ;
      }
// Not in use.
      connectionsInitialCount = 0 ;
    }
  }
/*
** Describes a connection given by as the argument.
** Prints out the properties and the usage of the connection (hidden password).
*/
  protected final void executeCommandConnectionDescribe ( String connna )
  {
    describeConnection ( connna ) ;
  }
/*
** Describes a connection we use currently.
** If not, the database type and the name of the connection will be prompted.
*/
  protected final void executeCommandConnectionDescribe ( )
  {
    describeConnection ( "" ) ;
  }
/*
** Changes a connection we added before, specified by the argument.
** The properties of this connection will be read from the file at first
** and the user can modify these or can leave it on the same value.
*/
  protected final void executeCommandConnectionChange ( String connna )
  {
    connectionAddOrChange ( connna ) ;
  }
/*
** As the above but the currently selected global dbConn will be used.
** if it is empty then a message will go tho the user.
*/
  protected final void executeCommandConnectionChange ( )
  {
    if ( ! "" . equals ( dbConn ) )
    {
      connectionAddOrChange ( dbConn ) ;
    }
    else
    {
      outprintln ( messageYouHaveToUseAConnectionOrGiveTheConnectionNameToChange ) ;
    }
  }
/*
** Deletes a connection by the given connna (connection name).
*/
  protected final void executeCommandConnectionDelete ( String connna )
  {
    deleteConnection ( connna ) ;
  }
/*
** Deletes a connection we use currently.
** If not, the database type and the name of the connection will be prompted.
*/
  protected final void executeCommandConnectionDelete ( )
  {
    deleteConnection ( "" ) ;
  }
/*
** Deletes all of the connections.
*/
  protected final void executeCommandConnectionDeleteall ( )
  {
// The file content is needed of course!
    if ( isFileContentConnectionsOrigReady ( ) )
    {
      if ( dbType != null )
      {
// Empty dbType: all of the connections will be lost.
// Not empty dbType: only that type of database connections will be lost.
        if ( "" . equals ( dbType ) )
        {
          deleteConnectionsByDbType ( dbTypeOracle ) ;
          deleteConnectionsByDbType ( dbTypeMssql ) ;
          deleteConnectionsByDbType ( dbTypeDb2 ) ;
          deleteConnectionsByDbType ( dbTypePostgresql ) ;
        }
        else
        {
          deleteConnectionsByDbType ( dbType ) ;
        }
// If we are on the connection that has been deleted
// then the prompt has to be changed.
        if ( ! "" . equals ( dbType ) && ! "" . equals ( dbConn ) )
        {
          if ( getConnnaPos ( dbType , dbConn ) == - 1 )
          {
            setDbConn ( "" ) ;
          }
        }
      }
      else
      {
        systemexit ( "Error . dbType is null, executeCommandConnectionDeleteall" ) ;
      }
    }
  }
/*
** Uses a database connection continuously.
** This is tricky.
** It appears in the front of the user that the connection
** will be used and as we would be logged in, but no.
** Every added query will have their own database connection
** since every query will have their own transaction.
*/
  protected final void executeCommandConnectionUse ( String connna )
  {
    useConnection ( "" , connna ) ;
  }
/*
** As the above but the (may be another) database type have to be specified.
*/
  protected final void executeCommandConnectionUse ( String dbtype , String connna )
  {
    useConnection ( dbtype , connna ) ;
  }
/*
** Tests the database connection given as connna. (The name of the connection.)
*/
  protected final void executeCommandConnectionTest ( String connna )
  {
// We have to know the content of the connections.
    if ( isFileContentConnectionsOrigReady ( ) )
    {
      if ( connna != null )
      {
// These will be the properties of the new connection to be constructed.
        String dbtype = null ;
        String dbuser = null ;
        char [ ] dbpass = null ;
        String driver = null ;
        String connst = null ;
// This will be the position of the connection in the connections file.
// Not found by default.
        int currPos = - 1 ;
// If the dbType is empty then the user will be prompted for it.
        if ( ! "" . equals ( dbType ) )
        {
          dbtype = dbType ;
        }
        else
        {
          dbtype = readline ( newLineString + messageDatabaseType , appMaxLengthOfInput ) . toLowerCase ( ) ;
        }
        if ( dbtype != null )
        {
// And this has to be a valid database type.
          if ( isValidDbType ( dbtype , true ) )
          {
// The position of this connection is..
            currPos = getConnnaPos ( dbtype , connna ) ;
// (has to be a fouond connection, else the user will get the error message.)
            if ( currPos != - 1 )
            {
// These are the current values to use to connect.
              dbuser = getDbuser ( dbtype , connna ) ;
              dbpass = getDbpass ( dbtype , connna ) . toCharArray ( ) ;
              driver = getDriver ( dbtype , connna ) ;
              connst = getConnst ( dbtype , connna ) ;
// Now testing the connection.
              testConnection ( dbuser , new String ( dbpass ) , driver , connst ) ;
            }
            else
            {
              outprintln ( messageYourConnectionDoesNotExist ) ;
            }
          }
        }
        else
        {
          systemexit ( "Error - dbtype is null, executeCommandConnectionTest" ) ;
        }
// Not in use.
        dbtype = null ;
        dbuser = null ;
        dbpass = null ;
        driver = null ;
        connst = null ;
        currPos = 0 ;
      }
      else
      {
        systemexit ( "Error - connna is null, executeCommandConnectionTest" ) ;
      }
    }
  }
/*
** Tests a connection but not a saved connection.
** The user can enter every properties of a brand new connection and this will test it.
*/
  protected final void executeCommandConnectionTest ( )
  {
// The password has to be known and the content of the connections file has to be decrypted.
    if ( isFileContentConnectionsOrigReady ( ) )
    {
// These will be the properties of the connection.
      String dbuser = null ;
      char [ ] dbpass = null ;
      String driver = null ;
      String connst = null ;
// Message to the user to enter the connection properties.
      outprintln ( messageEnterThePropertiesOfTheConnection ) ;
// Reading the properties.
      dbuser = readline ( messageDatabaseUser , appMaxLengthOfInput ) ;
      if ( dbuser != null )
      {
        dbpass = readpassword ( messageDatabasePassword ) ;
        if ( dbpass != null )
        {
          driver = readline ( messageDatabaseDriver , appMaxLengthOfInput ) ;
          if ( driver != null )
          {
            connst = readline ( messageConnectionString , appMaxLengthOfInput ) ;
            if ( connst != null )
            {
// Now testing the connection.
              testConnection ( dbuser , new String ( dbpass ) , driver , connst ) ;
            }
            else
            {
              systemexit ( "Error - connst is null, executeCommandConnectionTest" ) ;
            }
          }
          else
          {
            systemexit ( "Error - driver is null, executeCommandConnectionTest" ) ;
          }
        }
        else
        {
          systemexit ( "Error - dbpass is null, executeCommandConnectionTest" ) ;
        }
      }
      else
      {
        systemexit ( "Error - dbuser is null, executeCommandConnectionTest" ) ;
      }
    }
  }
/*
** Lists the active queries only.
** Active query means query that is currently running.
** (running query: begin timestamp not null, end timestamp null)
*/
  protected final void executeCommandQueryListActive ( )
  {
    listQueries ( typeToListActive ) ;
  }
/*
** Lists the inactive queries.
** Inactive query means query that is currently not running.
** (not started, finished successfully, finished with errors)
*/
  protected final void executeCommandQueryListInactive ( )
  {
    listQueries ( typeToListInactive ) ;
  }
/*
** Lists all of the queries not depend on its type.
** Query states: not started, running, finished successfully, finished with errors.
*/
  protected final void executeCommandQueryListall ( )
  {
    listQueries ( typeToListAll ) ;
  }
/*
** Adds a single query.
** Will have result set.
*/
  protected final void executeCommandQueryAddSingle ( )
  {
    addQuery ( argSingle ) ;
  }
/*
** Adds a muptiple query.
** Will have no result set, separatedly run general sql-s.
*/
  protected final void executeCommandQueryAddMultiple ( )
  {
    addQuery ( argMultiple ) ;
  }
/*
** Adds a query and a datasource for the prepared statement.
** Will have no result set, one query that runs several times.
*/
  protected final void executeCommandQueryAddBatch ( )
  {
    addQuery ( argBatch ) ;
  }
/*
** Query factory: the application enters into the mode to let the user
** write its queries continuously and see the results immediately.
*/
  protected final void executeCommandQueryFactory ( )
  {
    factoryQuery ( "" , "" , "" ) ;
  }
  protected final void executeCommandQueryFactory ( String query )
  {
    factoryQuery ( "" , "" , query ) ;
  }
  protected final void executeCommandQueryFactory ( String dbtype , String connna )
  {
    factoryQuery ( dbtype , connna , "" ) ;
  }
  protected final void executeCommandQueryFactory ( String dbtype , String connna , String query )
  {
    factoryQuery ( dbtype , connna , query ) ;
  }
/*
** Describes the query specified as the qid. (query id)
*/
  protected final void executeCommandQueryDescribe ( String qid )
  {
// The password and the connections have to be known.
    if ( isFileContentConnectionsOrigReady ( ) )
    {
// All of the property of the query.
      int queryId = Integer . parseInt ( qid ) ;
      String queryDbType = null ;
      String queryConnna = null ;
      String queryTitle = null ;
      String queryString = null ;
      String queryFile = null ;
      String queryDelimiter = null ;
      String queryType = null ;
      String queryIsScrollable = null ;
      int queryBatchExecCount = 0 ;
      String queryBatchSourceFile = null ;
      int queryBatchSourceQueryId = - 1 ;
      String queryBatchSourceField = null ;
      Connection queryConnection = null ;
      Statement queryStatement = null ;
      ResultSet queryResultSet = null ;
      Date queryStartDate = null ;
      Date queryEndDate = null ;
      String queryErrorMessage = null ;
      Thread queryThread = null ;
// The columns of the result set if possible.
      String [ ] columns = null ;
// It has to be valid.
      if ( isValidQueryId ( queryId ) )
      {
// The database type of this query is..
        queryDbType = getQueryDbType ( queryId ) ;
        if ( queryDbType != null )
        {
// Let's get all of it properties.
          queryConnna = getQueryConnna ( queryId ) ;
          queryTitle = getQueryTitle ( queryId ) ;
          queryString = getQueryString ( queryId ) ;
          queryFile = getQueryFile ( queryId ) ;
          queryDelimiter = getQueryDelimiter ( queryId ) ;
          queryType = getQueryType ( queryId ) ;
          queryIsScrollable = getQueryIsScrollable ( queryId ) ;
          queryBatchExecCount = getQueryBatchExecCount ( queryId ) ;
          queryBatchSourceFile = getQueryBatchSourceFile ( queryId ) ;
          queryBatchSourceQueryId = getQueryBatchSourceQueryId ( queryId ) ;
          queryBatchSourceField = getQueryBatchSourceField ( queryId ) ;
          queryConnection = getQueryConnection ( queryId ) ;
          queryStatement = getQueryStatement ( queryId ) ;
          queryResultSet = getQueryResultSet ( queryId ) ;
          queryStartDate = getQueryStartDate ( queryId ) ;
          queryEndDate = getQueryEndDate ( queryId ) ;
          queryErrorMessage = getQueryErrorMessage ( queryId ) ;
          queryThread = getQueryThread ( queryId ) ;
// Printing out the properties to informm the user about this query.
          outprintln ( messageQueryDescDbType + queryDbType ) ;
          outprintln ( messageQueryDescConnna + queryConnna ) ;
          outprintln ( messageQueryDescTitle + queryTitle ) ;
          outprintln ( messageQueryDescDelimiter + ( "" . equals ( queryDelimiter ) ? messageEmpty : queryDelimiter ) ) ;
          outprintln ( messageQueryDescType + queryType ) ;
          outprintln ( messageQueryDescIsScrollable + queryIsScrollable ) ;
          if ( queryBatchExecCount > 0 )
          {
            outprintln ( messageQueryDescBatchExecCount + queryBatchExecCount ) ;
            if ( queryBatchSourceFile != null )
            {
              outprintln ( messageQueryDescBatchSourceFile + queryBatchSourceFile ) ;
            }
            else if ( queryBatchSourceQueryId > - 1 )
            {
              outprintln ( messageQueryDescBatchSourceQueryId + queryBatchSourceQueryId ) ;
            }
            outprintln ( messageQueryDescBatchSourceFields + queryBatchSourceField ) ;
          }
          outprintln ( messageQueryDescStartDate + ( queryStartDate == null ? "" : simpleDateFormatForDisplaying . format ( queryStartDate ) ) ) ;
          outprintln ( messageQueryDescEndDate + ( queryEndDate == null ? "" : simpleDateFormatForDisplaying . format ( queryEndDate ) ) ) ;
          outprintln ( messageQueryDescTotalElapsedTime + getQueryElapsedFormatted ( queryId ) ) ;
          printErrorMessage ( queryErrorMessage ) ;
          outprintln ( messageQueryDescState + getQueryState ( queryId ) ) ;
          outprintln ( messageQueryDescThreadState + queryThread . getState ( ) ) ;
          if ( queryResultSet == null )
          {
            outprintln ( messageQueryDescNoResultSet ) ;
          }
          else
          {
            outprintln ( messageQueryDescHasResultSet ) ;
            outprintln ( messageQueryDescColumns ) ;
            columns = getQueryResultSetColumns ( queryId ) ;
            for ( int i = 0 ; i < columns . length ; i ++ )
            {
              outprintln ( fold + fold2 + columns [ i ] ) ;
            }
            if ( yes . equals ( queryIsScrollable ) )
            {
              outprintln ( messageQueryDescRowCount + getQueryResultSetRowCount ( queryId ) ) ;
            }
            else
            {
              outprintln ( messageQueryDescRowCountIsNotDisplayableBecauseOfNotScrollableResultSet ) ;
            }
          }
          outprintln ( messageQueryDescString + newLineChar + ( queryString == null ? messageEmpty : queryString ) ) ;
          if ( queryFile != null )
          {
            outprintln ( messageQueryDescFile + queryFile ) ;
          }
        }
        else
        {
          outprintln ( messageQueryHasNotBeenFound ) ;
        }
      }
// Not in use any more.
      queryId = 0 ;
      queryDbType = null ;
      queryConnna = null ;
      queryTitle = null ;
      queryString = null ;
      queryFile = null ;
      queryDelimiter = null ;
      queryType = null ;
      queryIsScrollable = null ;
      queryBatchExecCount = 0 ;
      queryBatchSourceFile = null ;
      queryBatchSourceQueryId = 0 ;
      queryBatchSourceField = null ;
      queryConnection = null ;
      queryStatement = null ;
      queryResultSet = null ;
      queryStartDate = null ;
      queryEndDate = null ;
      queryErrorMessage = null ;
      queryThread = null ;
      columns = null ;
    }
  }
/*
** Runs a query.
** This can be every type (not started, running, finished successfully, finished with errors.)
*/
  protected final void executeCommandQueryRun ( String qid )
  {
// We have to know the password and the file content of connections.
    if ( isFileContentConnectionsOrigReady ( ) )
    {
// This will be the id of the query.
      int queryId = Integer . parseInt ( qid ) ;
// An it has to be valid.
      if ( isValidQueryId ( queryId ) )
      {
// Starting a valid query.
        startQuery ( queryId ) ;
      }
// Not in use.
      queryId = 0 ;
    }
  }
/*
** Cancels a query. (Has to be in running state.)
** This can be happened in this Queries thread.
** (a separate thread can cancel the execution of an sql statement)
*/
  protected final void executeCommandQueryCancel ( String qid )
  {
// Thi soperation requires the password too.
    if ( isFileContentConnectionsOrigReady ( ) )
    {
// The id of the query is this.
      int queryId = Integer . parseInt ( qid ) ;
// And this has to be valid.
      if ( isValidQueryId ( queryId ) )
      {
// A final confirmation from the user is necessary.
        if ( readYesElseAnything ( messageDoYouWantToCancelThisQuery , messageQueryWontBeCancelled ) )
        {
// Just for formatting.
          outprintln ( "" ) ;
// Let's cancel this query.
          cancelQuery ( queryId ) ;
        }
      }
// Not in use.
      queryId = 0 ;
    }
  }
/*
** Cancels all of the queries ( which can be found in running state)!
*/
  protected final void executeCommandQueryCancelall ( )
  {
    cancelAllQueries ( true ) ;
  }
/*
** Deletes a query specified as the qid (query id).
*/
  protected final void executeCommandQueryDelete ( String qid )
  {
// Password, decrypted connections file content are needed.
    if ( isFileContentConnectionsOrigReady ( ) )
    {
// This will be the id of the query.
      int queryId = Integer . parseInt ( qid ) ;
// The state of the query
      String queryState = null ;
// Can we delete this query?
      boolean go = true ;
// And this has to be checked as a valid query id.
      if ( isValidQueryId ( queryId ) )
      {
// The current state of this query is..
        queryState = getQueryState ( queryId ) ;
        if ( queryState != null )
        {
// This is true by default, we can delete the query.
          go = true ;
// If the query is running, it has to be cancelled first and the user has to say that.
          if ( queryState . equals ( queryStateRunning ) )
          {
            if ( readYesElseAnything ( messageDoYouWantToCancelThisRunningQueryFirst , messageQueryWontBeCancelled ) )
            {
              go = true ;
              cancelQuery ( queryId ) ;
            }
            else
            {
              go = false ;
            }
          }
// We will delete this query if we can do it.
          if ( go )
          {
// Final confirmation from the user..
            if ( readYesElseAnything ( messageDoYouWantToDeleteThisQuery , messageQueryWontBeDeleted ) )
            {
// Just for formatting
              outprintln ( "" ) ;
// We are going to delete the query now.
              deleteQuery ( queryId , true ) ;
            }
          }
        }
        else
        {
          systemexit ( "Error - queryState is null, executeCommandQueryDelete" ) ;
        }
      }
// Not in use.
      queryId = 0 ;
      queryState = null ;
      go = false ;
    }
  }
/*
** Deletes all of the queries and the user has to confirm that.
*/
  protected final void executeCommandQueryDeleteall ( )
  {
    deleteAllQueries ( true ) ;
  }
/*
** Prints the result out onto the console or into file(s).
** All of the validation and data read from console to call the echoResult finally.
*/
  protected final void executeCommandResultEcho ( String qid )
  {
// The connections password is correct?
    if ( isFileContentConnectionsOrigReady ( ) )
    {
// The id of the query will be this.
      int queryId = Integer . parseInt ( qid ) ;
// The state of the query:
      String queryState = null ;
// The variables needed to call the echoResult.
      String resultTargets = "" ;
      String resultFormats = "" ;
      boolean headerToInclude = true ;
      boolean queryToInclude = false ;
// These are just for reading.
      String headerToIncludeString = null ;
      String queryToIncludeString = null ;
// We have to have a valid query id.
      if ( isValidQueryId ( queryId ) )
      {
// This is the current state of the query.
        queryState = getQueryState ( queryId ) ;
        if ( queryState != null )
        {
// Messages if the result is not displayable.
          if ( queryState . equals ( queryStateRunning ) )
          {
            outprintln ( messageYourQueryIsRunning ) ;
          }
          else if ( queryState . equals ( queryStateNotStarted ) )
          {
            outprintln ( messageYourQueryHasToBeRun ) ;
          }
          else if ( queryState . equals ( queryStateFinishedWithErrors ) )
          {
            outprintln ( "" ) ;
            printErrorMessage ( getQueryErrorMessage ( queryId ) ) ;
          }
// Can be displayable..
          else
          {
// The result set is usable?
            if ( isResultSetUsable ( getQueryResultSet ( queryId ) ) )
            {
// Let's initialize them
              resultTargets = "" ;
              resultFormats = "" ;
              headerToInclude = true ;
              queryToInclude = false ;
// Let's read them
              resultTargets = readline ( messageEnterResultTargets , appMaxLengthOfInput ) ;
              resultTargets = resultTargets . replaceAll ( singleSpace , "" ) . trim ( ) ;
// The result targets can be empty, the console will be used in this case.
              if ( "" . equals ( resultTargets ) )
              {
                resultTargets = resultTargetConsValue ;
              }
// The result targets has to be valid. Else message to the user.
              if ( isValidResultTargets ( resultTargets ) )
              {
// If it is not the console then the result formats has to be read.
// (if this is just the console then the txt result format will be used and no need to read it.)
                if ( ! resultTargets . equals ( resultTargetConsValue ) )
                {
                  resultFormats = readline ( messageEnterResultFormats , appMaxLengthOfInput ) ;
                  resultFormats = resultFormats . replaceAll ( singleSpace , "" ) . trim ( ) ;
                }
// In case of empty result formats the txt value will be used. (result into txt table)
                if ( "" . equals ( resultFormats ) || resultTargets . equals ( resultTargetConsValue ) )
                {
                  resultFormats = resultFormatTxtValue ;
                }
// The result formats has to be a valid string.
                if ( isValidResultFormats ( resultFormats ) )
                {
// Initializing these into null.
                  headerToIncludeString = null ;
                  queryToIncludeString = null ;
// Is the result target the console only?
                  if ( ! resultTargets . equals ( resultTargetConsValue ) )
                  {
// If not, the query string and the header to include will be questioned.
                    headerToIncludeString = readline ( messageEnterHeaderToInclude , appMaxLengthOfInput ) . toLowerCase ( ) . trim ( ) ;
                    headerToInclude = "" . equals ( headerToIncludeString ) ;
                    queryToIncludeString = readline ( messageEnterQueryToInclude , appMaxLengthOfInput ) . toLowerCase ( ) . trim ( ) ;
                    queryToInclude = y . equals ( queryToIncludeString ) || yes . equals ( queryToIncludeString ) ;
                  }
                  else
                  {
// Else (print onto the console only) header is needed and the query string is not.
                    headerToIncludeString = null ;
                    headerToInclude = true ;
                    queryToIncludeString = null ;
                    queryToInclude = false ;
                  }
// Inally, lets call the echoResult using its prepared parameters.
                  echoResult ( queryId , resultTargets , resultFormats , headerToInclude , queryToInclude ) ;
                }
                else
                {
                  outprintln ( messageCorrectResultFormatsAre ) ;
                }
              }
              else
              {
                outprintln ( messageCorrectResultTargetsAre ) ;
              }
            }
            else
            {
// Not usable if the resultSet object of the query is null
// or getting the metadata occours sql exception.
              outprintln ( messageSorryButThisResultSetCannotBeDispayed + queryId ) ;
            }
          }
        }
        else
        {
          systemexit ( "Error - queryState is null, executeCommandResultEcho" ) ;
        }
      }
// Not in use.
      queryId = 0 ;
      queryState = null ;
      resultTargets = null ;
      resultFormats = null ;
      headerToInclude = false ;
      queryToInclude = false ;
      headerToIncludeString = null ;
      queryToIncludeString = null ;
    }
  }
/*
** Showing the delimiter of the selected dbType.
** (If it has been not se then it will be prompted.)
*/
  protected final void executeCommandDelimiterShow ( )
  {
// The valid password is needed to show.
    if ( isFileContentConnectionsOrigReady ( ) )
    {
      showDelimiter ( ) ;
    }
  }
/*
** Changes the sql command separator string into the delimiter value.
** If it is empty then the empty delimiter (new line character) will be used.
*/
  protected final void executeCommandDelimiterChange ( String delimiter )
  {
// The valid connections password is required.
    if ( isFileContentConnectionsOrigReady ( ) )
    {
// This will be the dbtype in which we will change the actual value of the delimiter.
      String dbtype = null ;
// The database type has been read from the console or not.
      boolean fromConsole = false ;
// Let's read it if it is empty.
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
// And it also has to be valid.
        if ( isValidDbType ( dbtype , true ) )
        {
// If the type of the database came from the console then the dbType and the prompt string will be changed.
          if ( fromConsole )
          {
            dbType = dbtype ;
            changePromptToTheActual ( ) ;
          }
          if ( dbTypeOracle != null && dbTypeMssql != null && dbTypeDb2 != null && dbTypePostgresql != null )
          {
// Changing the delimiter now.
// The else case is always the postgresql database type.
            if ( dbTypeOracle . equals ( dbType ) )
            {
              delimiterOracle = delimiter ;
            }
            else if ( dbTypeMssql . equals ( dbType ) )
            {
              delimiterMssql = delimiter ;
            }
            else if ( dbTypeDb2 . equals ( dbType ) )
            {
              delimiterDb2 = delimiter ;
            }
            else
            {
              delimiterPostgresql = delimiter ;
            }
// Showing the delimiter now. (Shows the changed value.)
            showDelimiter ( ) ;
          }
          else
          {
            systemexit ( "Error - one of these is null: dbTypeOracle|dbTypeMssql|dbTypeDb2|dbTypePostgresql, executeCommandDelimiterChange" ) ;
          }
        }
      }
      else
      {
        systemexit ( "Error - dbtype is null, executeCommandDelimiterChange" ) ;
      }
    }
  }
/*
** Shows to the user the expectations of the good password
** to use to defend the data of the stored connections.
*/
  protected final void executeCommandConnectionsGoodPassword ( )
  {
    outprintln ( messageConnectionsGoodPassword ) ;
  }
/*
** Changes the password of the connections file.
*/
  protected final void executeCommandConnectionsPasswordChange ( )
  {
// The files has to be ready to do this.
    if ( isExistingConnectionsFiles ( true ) )
    {
// We have to know the original password of corse.
      if ( isFileContentConnectionsOrigReady ( ) )
      {
// A final confirmation is needed from the user.
        if ( readYesElseAnything ( messageSureChangeConnectionsPassword , messageConnectionsPasswordWontBeChanged ) )
        {
// Let the user be warned.
          outprintln ( messageDoNotForgetYourConnectionsPassword ) ;
// Reading the nrew password now.
          readPassword ( true ) ;
// And saving the content (using this new password!)
          if ( saveFile ( ) )
          {
// Message if this is successfully done.
            outprintln ( messageConnectionsPasswordHasBeenChanged ) ;
          }
        }
      }
    }
  }
/*
** Prints the available connands only.
*/
  protected final void executeCommandHints ( )
  {
    outprintln ( messageHints ) ;
  }
/*
** Prints the help of this application.
** (Commands and the describes of these.)
*/
  protected final void executeCommandHelp ( )
  {
    outprintln ( messageHelp ) ;
  }
/*
** This is not a command but belongs here.
** Prints out a message to the user because the parameters haven't been correct.
*/
  protected final void usageWrongParameters ( )
  {
    outprintln ( messageWrongParameters ) ;
  }
}