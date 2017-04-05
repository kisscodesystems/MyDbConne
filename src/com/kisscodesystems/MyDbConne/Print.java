/*
** This class is the part of the opensource MyDbConne application.
**
** See the header comment lines of Main class.
**
** Print
** Containing most of the informational functions
** of connections queries and result sets.
*/
package com . kisscodesystems . MyDbConne ;
import java . io . File ;
import java . io . FileOutputStream ;
import java . sql . ResultSet ;
import java . sql . ResultSetMetaData ;
import java . sql . SQLException ;
import java . sql . SQLXML ;
import java . util . ArrayList ;
import java . util . Collections ;
import java . util . Date ;
import java . util . HashMap ;
public class Print extends Qdata
{
/*
** Lists the connections by db types.
** Active, inactive or all connections will be listed.
*/
  protected final void listConnectionsByDbtype ( String dbtype , String typeToList )
  {
    if ( typeToList != null && dbtype != null && utils != null )
    {
// The valid password of the connections are necessary.
      if ( isFileContentConnectionsOrigReady ( ) )
      {
// The count of the items to be listed.
        int countOfListableConnections = 0 ;
// What kind of connections have to be listed?
        String stringToDisplay = null ;
// The connections in this application.
        HashMap < String , ArrayList < Integer > > connections = new HashMap < String , ArrayList < Integer > > ( ) ;
// This is the list to be sorted and then printed out.
        ArrayList < String > sortedList = new ArrayList < String > ( ) ;
// This will be the actual element.
        String listElementToSortedList = null ;
// What is the type of the connections to be listed?
        if ( typeToList . equals ( typeToListAll ) )
        {
          stringToDisplay = messageAllConnections ;
        }
        else if ( typeToList . equals ( typeToListActive ) )
        {
          stringToDisplay = messageActiveConnections ;
        }
        else if ( typeToList . equals ( typeToListInactive ) )
        {
          stringToDisplay = messageInactiveConnections ;
        }
// If it has been successfully determined..
        if ( stringToDisplay != null )
        {
// Getting the connections of this application. (all.)
          connections = getConnectionsByDbtype ( dbtype , true ) ;
          if ( connections != null )
          {
// These are necessary to display this result!
            if ( fold != null && fold2 != null )
            {
// Printing out the header. (dbtype + type of connections being listed)
              outprint ( newLineString + fold + utils . pad ( dbtype + stringToDisplay , appMaxQueryIdTitleConnnaWidth + ( fold2 . length ( ) - fold . length ( ) ) , spaceChar ) ) ;
              if ( ! typeToList . equals ( typeToListInactive ) )
              {
// Queries header element if the active or all connections are needed to be displayed.
                outprint ( messageQueries ) ;
              }
// Looping thru on the connections now!
              for ( HashMap . Entry < String , ArrayList < Integer > > connection : connections . entrySet ( ) )
              {
                if ( connection != null )
                {
                  if ( connection . getKey ( ) != null )
                  {
                    if ( connection . getValue ( ) != null )
                    {
// This item counts if its type is the expected or we would like to list all of the connections here.
// Active connections: the size of the getValue ( ) is greather than 0 since it contains the arraylist of the queries using this connection.
// Inactive connection: the size of the getValue ( ) is  exactly 0.
                      if ( ( typeToList . equals ( typeToListAll ) ) || ( typeToList . equals ( typeToListActive ) && connection . getValue ( ) . size ( ) > 0 ) || ( typeToList . equals ( typeToListInactive ) && connection . getValue ( ) . size ( ) == 0 ) )
                      {
// Let's create the element to be listed later.
                        listElementToSortedList = newLineString + fold2 + utils . pad ( connection . getKey ( ) , appMaxQueryIdTitleConnnaWidth , spaceChar ) ;
                        if ( ! typeToList . equals ( typeToListInactive ) )
                        {
// Let's append the queries belongs to this connection if the active or all connections are expected.
                          listElementToSortedList = listElementToSortedList + sep9 + utils . joinArrayListInteger ( connection . getValue ( ) , sep1 ) ;
                        }
// Now adding the final element.
                        sortedList . add ( listElementToSortedList ) ;
// Increase this, plus one item has been added.
                        countOfListableConnections ++ ;
                      }
                    }
                    else
                    {
                      systemexit ( "Error - connectionValue is null ( 2 ), listConnectionsByDbtype" ) ;
                    }
                  }
                  else
                  {
                    systemexit ( "Error - connectionKey is null ( 2 ), listConnectionsByDbtype" ) ;
                  }
                }
                else
                {
                  systemexit ( "Error - connection is null ( 2 ), listConnectionsByDbtype" ) ;
                }
              }
// Let the list be shorted.
              Collections . sort ( sortedList ) ;
// Now printing out the elements.
              for ( String listElement : sortedList )
              {
                outprint ( listElement ) ;
              }
// And a final message.
              outprintln ( newLineString + fold + countOfListableConnections + messageHits ) ;
            }
            else
            {
              systemexit ( "Error - one of these is null: fold|fold2, listConnectionsByDbtype" ) ;
            }
          }
          else
          {
            systemexit ( "Error - connections is null, listConnectionsByDbtype" ) ;
          }
        }
        else
        {
          systemexit ( "Error - stringToDisplay is null, listConnectionsByDbtype" ) ;
        }
// These are releasable now.
        countOfListableConnections = 0 ;
        stringToDisplay = null ;
        connections = null ;
        sortedList = null ;
        listElementToSortedList = null ;
      }
    }
    else
    {
      systemexit ( "Error - one of these is null: dbtype|utils|typeToList, listConnectionsByDbtype" ) ;
    }
  }
/*
** Gets the count of all connections stored already.
*/
  protected final int getNumOfAllConnections ( )
  {
    return getConnectionsByDbtype ( dbTypeOracle , false ) . size ( ) + getConnectionsByDbtype ( dbTypeMssql , false ) . size ( ) + getConnectionsByDbtype ( dbTypeDb2 , false ) . size ( ) + getConnectionsByDbtype ( dbTypePostgresql , false ) . size ( ) ;
  }
/*
** Getting the connections by dbtypes.
** Will scan the content of the connections and also will get the queries belonging to that connection if there are any.
// The content of the connections is based on characters, keep it in mind.
*/
  protected final HashMap < String , ArrayList < Integer > > getConnectionsByDbtype ( String dbtype , boolean queryIdsNeeded )
  {
// This will be the final result to be returned.
    HashMap < String , ArrayList < Integer > > connections = new HashMap < String , ArrayList < Integer > > ( ) ;
// It is necessary to count the newLine characters!
    int newLines = 0 ;
// For checking for the correct database type.
    boolean innerBreak = false ;
// This will be the name of the connection!
    String connna = null ;
// And this is the added queries belonging to the current connection.
    ArrayList < Integer > queryIds = null ;
// The string to be searched in the content of the connections.
    String toSearch = newLineString + dbtype + newLineChar ;
// The valid password is needed in the beginning.
    if ( isFileContentConnectionsOrigReady ( ) )
    {
      if ( connectionsHeader != null && appDateFormatForDisplaying != null && sep9 != null && messageLogApplicationInstanceInitialize != null )
      {
// Let's loop on the relevant part of the content.
        for ( int i = connectionsHeader . length ( ) + appDateFormatForDisplaying . length ( ) + sep9 . length ( ) + messageLogApplicationInstanceInitialize . length ( ) + 1 - 1 ; i < fileContentConnectionsOrig . length - toSearch . length ( ) ; i ++ )
        {
// Counting the newLine characters.
          if ( fileContentConnectionsOrig [ i ] == newLineChar )
          {
            newLines ++ ;
          }
// In case of zero character, we will break this loop.
// Zero char mans we have reached the end of the content so nothing further to be searched for.
          else if ( fileContentConnectionsOrig [ i ] == zeroChar )
          {
            break ;
          }
// This is false now by default. Will be true if the validation will be failed.
          innerBreak = false ;
// From the current position.
// We are checking that the characters (content and the searched string) are the same or not.
// If the characters will be the same till the end of the searched string that means that
// we just have found the string, so this is a connection being in the database type we are
// searching for.
          for ( int j = 0 ; j < toSearch . length ( ) ; j ++ )
          {
            if ( fileContentConnectionsOrig [ i + j ] != toSearch . charAt ( j ) )
            {
              innerBreak = true ;
              break ;
            }
          }
// If the inner break remains false..
// But this is not enough.
// The newLine characters has to be the multiple of 6 + 1!
// If this is true then we have really found the searched database types.
          if ( ! innerBreak && newLines % 6 == 1 )
          {
// Now we are getting the name of the connection.
// The database type and a newLine character is followed by this name until the next new line.
// The current connection name is empty by default.
            connna = "" ;
// Let's append the characters until the next newLine character.
            for ( int j = i + toSearch . length ( ) ; j < appMaxLengthOfInput ; j ++ )
            {
              if ( fileContentConnectionsOrig [ j ] == newLineChar )
              {
                break ;
              }
              connna += fileContentConnectionsOrig [ j ] ;
            }
// The name of the connection is also known, so we can get the queries of this connection if requested
            if ( queryIdsNeeded )
            {
              queryIds = getQueriesByConnection ( dbtype , connna ) ;
            }
            else
            {
              queryIds = null ;
            }
// Add these two: the connection name is the key and the list of the queries is the value of this hashmap.
            connections . put ( connna , queryIds ) ;
          }
        }
      }
      else
      {
        systemexit ( "Error - one of these is null: connectionsHeader|appDateFormatForDisplaying|sep9|messageLogApplicationInstanceInitialize, getConnectionsByDbtype" ) ;
      }
    }
// These are releasable now.
    newLines = 0 ;
    innerBreak = false ;
    connna = null ;
    queryIds = null ;
    toSearch = null ;
// Let's return with the connections!
    return connections ;
  }
/*
** Gets the query list of the specified connection.
** A connection can be identifyed by the database type - connection name values, so these two are required.
*/
  protected final ArrayList < Integer > getQueriesByConnection ( String dbtype , String connna )
  {
// This will be the final result containing the queries.
    ArrayList < Integer > queryIds = new ArrayList < Integer > ( ) ;
// The name of the current connection.
    String cn = null ;
    if ( queryDbTypes != null && queryConnnas != null )
    {
// Looping on the dbtypes hashmap.
      for ( HashMap . Entry < Integer , String > queryDbType : queryDbTypes . entrySet ( ) )
      {
        if ( queryDbType != null )
        {
          if ( queryDbType . getKey ( ) != null )
          {
            if ( queryDbType . getValue ( ) != null )
            {
// The type of database is correct?
              if ( queryDbType . getValue ( ) . equals ( dbtype ) )
              {
// Getting the name of the connection.
                cn = queryConnnas . get ( queryDbType . getKey ( ) ) ;
// This would be fatal..
                if ( cn != null )
                {
// The connection name is correct?
                  if ( cn . equals ( connna ) )
                  {
// If we are here then the database type and the connection name
// both equal the arguments so we can add this query id.
                    queryIds . add ( queryDbType . getKey ( ) ) ;
                  }
                }
                else
                {
                  systemexit ( "Error - cn is null, getQueriesByConnection" ) ;
                }
              }
            }
            else
            {
              systemexit ( "Error - queryDbTypeValue is null, getQueriesByConnection" ) ;
            }
          }
          else
          {
            systemexit ( "Error - queryDbTypeKey is null, getQueriesByConnection" ) ;
          }
        }
        else
        {
          systemexit ( "Error - queryDbType is null, getQueriesByConnection" ) ;
        }
      }
    }
    else
    {
      systemexit ( "Error - one of these is null: queryDbTypes|queryConnnas, getQueriesByConnection" ) ;
    }
// Not necessary reference.
    cn = null ;
// Shorting this.
    Collections . sort ( queryIds ) ;
// Return with the list of the queries.
    return queryIds ;
  }
/*
** Lists the connections according to the dbType.
** The type of the connection to be listed is expected.
*/
  protected final void listConnections ( String typeToList )
  {
// Valid password and decrypted content of the connections are needed.
    if ( isFileContentConnectionsOrigReady ( ) )
    {
      if ( dbType != null )
      {
// Empty database type: listing all of the database types.
        if ( "" . equals ( dbType ) )
        {
          listConnectionsByDbtype ( dbTypeOracle , typeToList ) ;
          listConnectionsByDbtype ( dbTypeMssql , typeToList ) ;
          listConnectionsByDbtype ( dbTypeDb2 , typeToList ) ;
          listConnectionsByDbtype ( dbTypePostgresql , typeToList ) ;
        }
// Not empty database type: listing that type of database.
        else
        {
          listConnectionsByDbtype ( dbType , typeToList ) ;
        }
      }
      else
      {
        systemexit ( "Error . dbType is null, listConnections" ) ;
      }
    }
  }
/*
** Gets the queries that are active at this moment.
** Active query means query that is now running.
*/
  protected final String getActiveQueries ( )
  {
// This string will contain the query ids.
    String activeQueries = "" ;
// Valid password and decrypted content of the connections are needed.
    if ( isFileContentConnectionsOrigReady ( ) )
    {
      if ( queryDbTypes != null )
      {
// Looping on the database types hashmap.
        for ( HashMap . Entry < Integer , String > queryDbType : queryDbTypes . entrySet ( ) )
        {
          if ( queryDbType != null )
          {
            if ( queryDbType . getKey ( ) != null )
            {
              if ( queryDbType . getValue ( ) != null )
              {
// The running queries are interesting for us.
                if ( queryStateRunning . equals ( getQueryState ( queryDbType . getKey ( ) ) ) )
                {
// Appending a space and this id.
                  activeQueries = activeQueries + spaceChar + String . valueOf ( queryDbType . getKey ( ) ) ;
                }
              }
              else
              {
                systemexit ( "Error - queryDbTypeValue is null, getActiveQueries" ) ;
              }
            }
            else
            {
              systemexit ( "Error - queryDbTypeKey is null, getActiveQueries" ) ;
            }
          }
          else
          {
            systemexit ( "Error - queryDbType is null, getActiveQueries" ) ;
          }
        }
      }
      else
      {
        systemexit ( "Error - queryDbTypes is null, getActiveQueries" ) ;
      }
    }
// Let's give back the string containing the active queries.
// (Empty means no active query.)
    return activeQueries ;
  }
/*
** Gets the queries by dbtypes.
** The query id and its state will be returned.
*/
  protected final HashMap < String , String > getQueriesByDbtype ( String dbtype )
  {
// Will be the final list of the queries.
    HashMap < String , String > queries = new HashMap < String , String > ( ) ;
// The actual state of the current query.
    String queryState = null ;
// The valid password and the decrypted content of the connections are needed.
    if ( isFileContentConnectionsOrigReady ( ) )
    {
      if ( queryDbTypes != null )
      {
// The database type hashmap will be looped.
        for ( HashMap . Entry < Integer , String > queryDbType : queryDbTypes . entrySet ( ) )
        {
          if ( queryDbType != null )
          {
            if ( queryDbType . getKey ( ) != null )
            {
              if ( queryDbType . getValue ( ) != null )
              {
// The type of the database has to meet the expected.
                if ( queryDbType . getValue ( ) . equals ( dbtype ) )
                {
// This is the current state of the query.
                  queryState = getQueryState ( queryDbType . getKey ( ) ) ;
// What is the state?
                  if ( queryStateRunning . equals ( queryState ) )
                  {
// In case of running query, the actual elapsed time will be displayed.
                    queries . put ( String . valueOf ( queryDbType . getKey ( ) ) , messageRunningElapsed + getQueryElapsedFormatted ( queryDbType . getKey ( ) ) ) ;
                  }
                  else
                  {
// The actual state of the query will be displayed otherwise.
                    queries . put ( String . valueOf ( queryDbType . getKey ( ) ) , queryState ) ;
                  }
                }
              }
              else
              {
                systemexit ( "Error - queryDbTypeValue is null, getQueriesByDbtype" ) ;
              }
            }
            else
            {
              systemexit ( "Error - queryDbTypeKey is null, getQueriesByDbtype" ) ;
            }
          }
          else
          {
            systemexit ( "Error - queryDbType is null, getQueriesByDbtype" ) ;
          }
        }
      }
      else
      {
        systemexit ( "Error - queryDbTypes is null, getQueriesByDbtype" ) ;
      }
    }
// Not used reference.
    queryState = null ;
// We are done, return this!
    return queries ;
  }
/*
** Lists the queries. Database type can be active, inactive or all.
** If the dbType globally set database type has been set then only that
** type of database else every database type will be listed.
*/
  protected final void listQueries ( String typeToList )
  {
// The password is needed for this.
    if ( isFileContentConnectionsOrigReady ( ) )
    {
      if ( dbType != null )
      {
// The empty dbType means that we will list the queries of all of the database types.
// If it is not empty then that type of the database will be listed.
        if ( "" . equals ( dbType ) )
        {
          listQueriesByDbtype ( dbTypeOracle , typeToList ) ;
          listQueriesByDbtype ( dbTypeMssql , typeToList ) ;
          listQueriesByDbtype ( dbTypeDb2 , typeToList ) ;
          listQueriesByDbtype ( dbTypePostgresql , typeToList ) ;
        }
        else
        {
          listQueriesByDbtype ( dbType , typeToList ) ;
        }
      }
      else
      {
        systemexit ( "Error . dbType is null, listQueries" ) ;
      }
    }
  }
/*
** Lists the queries of the given database type.
** The type of the listing also can be specified. (active, inactive, all)
*/
  protected final void listQueriesByDbtype ( String dbtype , String typeToList )
  {
    if ( typeToList != null && dbtype != null && utils != null )
    {
// Password, decrypted file content are needed.
      if ( isFileContentConnectionsOrigReady ( ) )
      {
// The count of listable queries for the final message.
        int countOfListableQueries = 0 ;
// To the header.
        String stringToDisplay = null ;
// The queries by dbtype.
        HashMap < String , String > queries = new HashMap < String , String > ( ) ;
// This list will be sorted and printed.
        ArrayList < String > sortedList = new ArrayList < String > ( ) ;
// What is the string to be displayed in the header?
        if ( typeToList . equals ( typeToListAll ) )
        {
          stringToDisplay = messageAllQueries ;
        }
        else if ( typeToList . equals ( typeToListActive ) )
        {
          stringToDisplay = messageActiveQueries ;
        }
        else if ( typeToList . equals ( typeToListInactive ) )
        {
          stringToDisplay = messageInactiveQueries ;
        }
        if ( stringToDisplay != null )
        {
// These are also necessary.
          if ( dbTypePostgresql != null && fold != null && fold2 != null )
          {
// Printing the header.
            outprint ( newLineString + fold + utils . pad ( dbtype + stringToDisplay , appMaxQueryIdTitleConnnaWidth + ( fold2 . length ( ) - fold . length ( ) ) , spaceChar ) ) ;
// Appending to the header the state or the elapsed time.
            if ( typeToList . equals ( typeToListActive ) )
            {
              outprint ( sep9 + messageQueryElapseds ) ;
            }
            else
            {
              outprint ( sep9 + queryStates ) ;
            }
// This is the list of the queries belongs to the dbtype.
            queries = getQueriesByDbtype ( dbtype ) ;
// This has to be ot null!
            if ( queries != null )
            {
// Looping on this hashmap now.
              for ( HashMap . Entry < String , String > query : queries . entrySet ( ) )
              {
                if ( query != null )
                {
                  if ( query . getKey ( ) != null )
                  {
                    if ( query . getValue ( ) != null )
                    {
// Counts if
// - all queries are needed
// active and the state ( query . getValue ( ) ) starts with running, elapsed..
// inactive and the state is not started or finished successfully or finished with errors.
                      if ( ( typeToList . equals ( typeToListAll ) ) || ( typeToList . equals ( typeToListActive ) && query . getValue ( ) . startsWith ( messageRunningElapsed ) ) || ( typeToList . equals ( typeToListInactive ) && ( query . getValue ( ) . equals ( queryStateNotStarted ) || query . getValue ( ) . equals ( queryStateFinishedSuccessfully ) || query . getValue ( ) . equals ( queryStateFinishedWithErrors ) ) ) )
                      {
// Increasing the count.
                        countOfListableQueries ++ ;
// Adding the current to the list.
                        sortedList . add ( newLineString + fold2 + utils . pad ( query . getKey ( ) + " (" + getQueryTitle ( Integer . parseInt ( query . getKey ( ) ) ) + ")" , appMaxQueryIdTitleConnnaWidth , spaceChar ) + sep9 + query . getValue ( ) ) ;
                      }
                    }
                    else
                    {
                      systemexit ( "Error - queryValue is null, listQueriesByDbtype" ) ;
                    }
                  }
                  else
                  {
                    systemexit ( "Error - queryKey is null, listQueriesByDbtype" ) ;
                  }
                }
              }
// Sort first to be nice!
              Collections . sort ( sortedList ) ;
// Now printing out the elements.
              for ( String listElement : sortedList )
              {
                outprint ( listElement ) ;
              }
// A final message to the user.
              outprintln ( newLineString + fold + countOfListableQueries + messageHits ) ;
            }
            else
            {
              systemexit ( "Error - queries is null, listQueriesByDbtype" ) ;
            }
          }
          else
          {
            systemexit ( "Error - one of these is null: fold|fold2|dbTypePostgresql, listQueriesByDbtype" ) ;
          }
        }
        else
        {
          systemexit ( "Error - stringToDisplay is null, listQueriesByDbtype" ) ;
        }
// Not used references.
        countOfListableQueries = 0 ;
        stringToDisplay = null ;
        queries = null ;
        sortedList = null ;
      }
    }
    else
    {
      systemexit ( "Error - one of these is null: dbtype|utils|typeToList, listQueriesByDbtype" ) ;
    }
  }
/*
** Displaying the result immediateli after the start of a query.
** We will wait a little bit and then check for the end date of that query.
** If it is not null then the query is finished, we will display the
** result if it is not a not scrollable statement and result set.
*/
  protected final void displayResultImmediately ( int queryId )
  {
    if ( argSingle != null && yes != null )
    {
// A valid query id is needed of course.
      if ( isValidQueryId ( queryId ) )
      {
// This Queries thread will wait a little bit.
        try
        {
          Thread . sleep ( appMaxNumOfMillisecondsToWaitForTheResult ) ;
        }
        catch ( InterruptedException e )
        {
          systemexit ( "Exception - InterruptedException, displayResultImmediately" ) ;
        }
// Is the end date null?
        if ( getQueryEndDate ( queryId ) != null )
        {
// Not null, the query is finished.
// Is this a single query or not..
          if ( argSingle . equals ( getQueryType ( queryId ) ) )
          {
// Displaying the result if it has got a scrollable result set.
            if ( yes . equals ( getQueryIsScrollable ( queryId ) ) )
            {
              echoResult ( queryId , resultTargetConsValue , resultFormatTxtValue , true , false ) ;
            }
            else
            {
              outprintln ( messageYourQueryResultSetIsNotScrollable + queryId ) ;
            }
          }
          else
          {
            echoResult ( queryId , resultTargetConsValue , resultFormatTxtValue , true , false ) ;
          }
        }
        else
        {
// The query is running now so this is the information for the user.
          outprintln ( messageYourQueryIsRunning ) ;
        }
      }
    }
    else
    {
      systemexit ( "Error - one of these is null: argSingle|yes, displayResultImmediately" ) ;
    }
  }
/*
** From result echo command and in display result immediately methods.
** Echos the result of a specified query into the specified targets and formats.
** Also echos the header and the query if requested.
** The result can go to the console and/or to a file.
** The result can be txt (onto console or into file) and/or csv (into file) and/or htm (into file).
** Multiple choice is available, these aboves are independent from each other.
*/
  protected final void echoResult ( int queryId , String resultTargets , String resultFormats , boolean headerToInclude , boolean queryToInclude )
  {
// A valid query id is needed here.
    if ( isValidQueryId ( queryId ) )
    {
// This will be the state and the scrollable property of this valid query.
      String queryState = getQueryState ( queryId ) ;
      String queryIsScrollable = getQueryIsScrollable ( queryId ) ;
// The type of the database is also needed for constructing the result.
      String queryDbType = getQueryDbType ( queryId ) ;
      if ( queryState != null && queryIsScrollable != null && queryDbType != null )
      {
        if ( queryState . equals ( queryStateRunning ) )
        {
// Message: the query is running, so nothing is to display.
          outprintln ( messageYourQueryIsRunning ) ;
        }
        else if ( queryState . equals ( queryStateNotStarted ) )
        {
// Message: a not started query has to be run to have a result set.
          outprintln ( messageYourQueryHasToBeRun ) ;
        }
        else if ( queryState . equals ( queryStateFinishedWithErrors ) )
        {
// Message: error has occurred, printing the error message.
          outprint ( newLineString + fold2 ) ;
          printErrorMessage ( getQueryErrorMessage ( queryId ) ) ;
        }
        else
        {
// This is the case we should display something interesting.
// The result set and the elapsed time first.
// Also getting here the delimiter of the query (for delimiter of csv result).
          ResultSet resultSet = getQueryResultSet ( queryId ) ;
          String elapsedFormatted = getQueryElapsedFormatted ( queryId ) ;
          String queryDelimiter = getQueryDelimiter ( queryId ) ;
// The result set can be null or not null, let's check this.
          if ( resultSet != null )
          {
// The result set is not null so we can dig into it.
// The results wil be stored in this strings.
            String resultTxt = null ;
            String resultCsv = null ;
            String resultHtm = null ;
// The query string has to be empty by default.
            String queryString = "" ;
// If the result is empty then we don't want to display all of the results!
            boolean emptyResult = false ;
// This is the date object to use for constructing the filename of result files.
            Date dateForFilenames = new Date ( ) ;
// Folder for the htm result binary content.
            File htmFolder = null ;
// The name of the folder above.
            String htmFolderName = null ;
// If we need the query string then get it.
            if ( queryToInclude )
            {
              queryString = getQueryString ( queryId ) ;
            }
// If the result is wanted to be on the console..
            if ( resultTargets . contains ( resultTargetConsValue ) )
            {
// Getting the txt result.
              resultTxt = constructResult ( queryIsScrollable , resultSet , headerToInclude , queryString , resultFormatTxtValue , elapsedFormatted , null , queryDbType , queryDelimiter ) ;
              if ( ! "" . equals ( resultTxt ) )
              {
// Print this if this is not empty.
                outprint ( resultTxt ) ;
              }
              else
              {
// The txt result is empty so this var should be set to true.
                emptyResult = true ;
// Message that this result set cannot be displayed now.
                outprintln ( messageSorryButThisResultSetCannotBeDispayed + queryId ) ;
              }
            }
// If the result set wanted to be in file(s)..
            if ( resultTargets . contains ( resultTargetFileValue ) )
            {
// Just for formatting.
              if ( ! emptyResult )
              {
                outprintln ( "" ) ;
              }
// Writing the specified content into file in the same logic.
// (txt: if it has been get then it won't be constructed again.)
// Do something if the result format contains that result format value and the result is not empty.
// Printint to the user: producing the result.
// Constructing the specified result.
// If it is not empty then writing it to file and printing a done message to the user.
// If it is empty then emptyResult is set to true and printing the not displayable message to the user.
              if ( resultFormats . contains ( resultFormatTxtValue ) && ! emptyResult )
              {
                outprint ( messageProducingTxtResult ) ;
                if ( resultTxt == null )
                {
                  resultTxt = constructResult ( queryIsScrollable , resultSet , headerToInclude , queryString , resultFormatTxtValue , elapsedFormatted , null , queryDbType , queryDelimiter ) ;
                }
                if ( ! "" . equals ( resultTxt ) )
                {
                  writeFileContent ( resultTxt , fileNameResult + simpleDateFormatForFilenames . format ( dateForFilenames ) + filePostfixTxt ) ;
                  outprintln ( messageDone ) ;
                }
                else
                {
                  emptyResult = true ;
                  outprintln ( messageSorryButThisResultSetCannotBeDispayed + queryId ) ;
                }
              }
              if ( resultFormats . contains ( resultFormatCsvValue ) && ! emptyResult )
              {
                outprint ( messageProducingCsvResult ) ;
                if ( ! "" . equals ( queryDelimiter ) )
                {
                  resultCsv = constructResult ( queryIsScrollable , resultSet , headerToInclude , queryString , resultFormatCsvValue , elapsedFormatted , null , queryDbType , queryDelimiter ) ;
                  if ( ! "" . equals ( resultCsv ) )
                  {
                    writeFileContent ( resultCsv , fileNameResult + simpleDateFormatForFilenames . format ( dateForFilenames ) + filePostfixCsv ) ;
                    outprintln ( messageDone ) ;
                  }
                  else
                  {
                    emptyResult = true ;
                    outprintln ( messageSorryButThisResultSetCannotBeDispayed + queryId ) ;
                  }
                }
                else
                {
                  outprintln ( messageDelimiterIsEmpty ) ;
                }
              }
              if ( resultFormats . contains ( resultFormatHtmValue ) && ! emptyResult )
              {
                outprint ( messageProducingHtmResult ) ;
                resultHtm = constructResult ( queryIsScrollable , resultSet , headerToInclude , queryString , resultFormatHtmValue , elapsedFormatted , fileNameResult + simpleDateFormatForFilenames . format ( dateForFilenames ) + SEP , queryDbType , queryDelimiter ) ;
                if ( ! "" . equals ( resultHtm ) )
                {
                  writeFileContent ( resultHtm , fileNameResult + simpleDateFormatForFilenames . format ( dateForFilenames ) + filePostfixHtm ) ;
                  outprintln ( messageDone ) ;
                }
                else
                {
                  emptyResult = true ;
                  outprintln ( messageSorryButThisResultSetCannotBeDispayed + queryId ) ;
                }
              }
            }
// Not used.
            resultTxt = null ;
            resultCsv = null ;
            resultHtm = null ;
            queryString = "" ;
            emptyResult = false ;
            dateForFilenames = null ;
          }
          else
          {
// The result set is null, so print the successfully execution and the elapsed time.
            outprintln ( messageYourQueryHasBeenExecutedSuccessfully ) ;
            outprintln ( fold + elapsedFormatted ) ;
          }
// Not used.
          resultSet = null ;
          elapsedFormatted = null ;
          queryDelimiter = null ;
        }
      }
      else
      {
        systemexit ( "Error - One of these is null: queryState|queryIsScrollable|queryDbType, echoResult" ) ;
      }
// Not used.
      queryState = null ;
      queryIsScrollable = null ;
    }
  }
/*
** Constructing the result into a single string.
** This result string can be printed onto the console or into files.
** txt (table), csv or htm (html table), one of them will be constructed.
*/
  protected final String constructResult ( String scr , ResultSet rs , boolean headerToInclude , String queryString , String resultFormatValue , String elapsedFormatted , String htmFolderName , String dbtype , String queryDelimiter )
  {
// This will be the string containing the finaly constructed result.
    String resultString = "" ;
// The field separator used during the result constructing.
    String fieldsep = null ;
// This result set is scrollable? (Getting from parameter.)
    boolean scrollable = yes . equals ( scr ) ;
// In case of htm result, this folder will contain the elements not displayable in the htm table.
    File htmFolder = null ;
// This has to be a usable result set!
// If not, an empty string will be returned to the caller.
// (Not usable if the result set is null or the result set metadata is not available.)
    if ( isResultSetUsable ( rs ) )
    {
      if ( utils != null )
      {
// Determining the field separator.
// The separator will come from the parameter of this functions
// because the csv field and data delimiter will be the delimiter
// of that query.
        if ( resultFormatValue . equals ( resultFormatTxtValue ) )
        {
          fieldsep = fieldsepTxt ;
        }
        else if ( resultFormatValue . equals ( resultFormatCsvValue ) )
        {
          fieldsep = queryDelimiter ;
        }
        else if ( resultFormatValue . equals ( resultFormatHtmValue ) )
        {
          fieldsep = fieldsepHtm ;
// Creating the folder of the other content
          if ( htmFolderName != null )
          {
            htmFolder = new File ( htmFolderName ) ;
            if ( htmFolder != null )
            {
              htmFolder . mkdirs ( ) ;
              if ( ! ( htmFolder . exists ( ) && htmFolder . isDirectory ( ) ) )
              {
                systemexit ( "Error - htmFolder is not existing or not a folder, constructResult" ) ;
              }
            }
            else
            {
              systemexit ( "Error - htmFolder is null, constructResult" ) ;
            }
          }
          else
          {
            systemexit ( "Error - htmFolderName is null (htm result), constructResult" ) ;
          }
        }
        if ( fieldsep != null )
        {
// This metadata of the result set will be important.
          ResultSetMetaData rsmd = null ;
// The count of the columns being in the result set.
          int colscount = 0 ;
// The list of the columns. (Just the names.)
          ArrayList < String > cols = new ArrayList < String > ( ) ;
// The widths of the columns. This is necessary to print out the txt result.
// (csv and htm won't use these column width values.)
// And the types of columns.
          int [ ] colw = new int [ 0 ] ;
          String [ ] colt = new String [ 0 ] ;
// The current value of the rgesult column has been written onto the disk separately or not.
          boolean onTheDiskSeparately = false ;
          try
          {
// Getting the metadata.
            rsmd = rs . getMetaData ( ) ;
            if ( rsmd != null )
            {
// SQL exception is not expected, exiting if we catch one.
              try
              {
// The count of the columns of the result set.
                colscount = rsmd . getColumnCount ( ) ;
// Depending this, we can construct the new array for the widths and types of columns.
                colw = new int [ colscount ] ;
                colt = new String [ colscount ] ;
// A newLineChar is added because of formatting.
                resultString += newLineChar ;
// The following loop collects the names of the columns
// And initialize the width of the current column to the length of the column name.
                for ( int i = 1 ; i <= colscount ; i ++ )
                {
                  cols . add ( rsmd . getColumnName ( i ) ) ;
                  colw [ i - 1 ] = rsmd . getColumnName ( i ) . length ( ) ;
                  colt [ i - 1 ] = rsmd . getColumnTypeName ( i ) . toLowerCase ( ) . trim ( ) ;
                }
              }
              catch ( SQLException e )
              {
                systemexit ( "Exception - SQLException (2), constructResult" ) ;
              }
// Empty query string will not be added into the result string.
              if ( ! "" . equals ( queryString ) )
              {
// Adding the not empty result string.
                if ( resultFormatValue . equals ( resultFormatHtmValue ) )
                {
                  resultString += "<p><b><i>" + queryString . replaceAll ( newLineString , "<br/>\n" ) + "</i></b></p>" + newLineChar + newLineChar ;
                }
                else
                {
                  resultString += queryString + newLineChar + newLineChar ;
                }
              }
// Cycle variable.
              int j = 0 ;
// How many lines we have.
              int lines = 0 ;
// A current value, this is a general object.
              Object val = null ;
// This is the string representation of the above.
              String valStr = null ;
// The result set string will be constructed line-by-line, this will be the actual line.
              String line = null ;
// This is the line printed out 3 times in case of txt format:
// first line, header separator line and last line.
              String plusMinusLine = null ;
              try
              {
// Only if this is a scrollable result set.. we can determine the maximum length of a column.
// Depending on the data, the maximum length truncated into an upper limit will be used.
                if ( scrollable )
                {
// .. because result set . beforeFirst can be called on this.
                  rs . beforeFirst ( ) ;
// Now we can loop on this result set from the beginning to finalize the widths of the columns.
// (So, not to display any data from the result set.)
                  while ( rs . next ( ) )
                  {
// The count of lines can be increased.
                    lines ++ ;
// We are in the first column.
                    j = 1 ;
// Go thru on the cols array: finalizing the maximum length of the columns.
                    for ( String colname : cols )
                    {
                      val = utils . getVal ( rs , colname ) ;
                      valStr = utils . getValStr ( val , nullStr ) ;
                      if ( colw [ j - 1 ] < valStr . length ( ) && ( valStr . length ( ) <= appMaxColLengthTxt || ! resultFormatValue . equals ( resultFormatTxtValue ) ) )
                      {
                        colw [ j - 1 ] = valStr . length ( ) ;
                      }
                      j ++ ;
                    }
                  }
                }
// We can start now to construct the result.
// The beginning of the first line is..
                if ( resultFormatValue . equals ( resultFormatTxtValue ) )
                {
                  line = fieldsep ;
                }
                else if ( resultFormatValue . equals ( resultFormatHtmValue ) )
                {
                  line = "<tr><th>" ;
                }
                else
                {
                  line = "" ;
                }
// Now we can construct the header. (No this is not early.)
// Looping to get the columns.
                for ( int i = 1 ; i <= colscount ; i ++ )
                {
// The next lower case colname will be appended into the end of the current line.
                  line = line + ( ( resultFormatValue . equals ( resultFormatTxtValue ) && scrollable ) ? utils . pad ( rsmd . getColumnName ( i ) . toLowerCase ( ) , colw [ i - 1 ] , spaceChar ) : rsmd . getColumnName ( i ) . toLowerCase ( ) ) ;
// After the column, a field separator is needed.
                  if ( resultFormatValue . equals ( resultFormatHtmValue ) )
                  {
// In case of htm result, the first columns are not terminated by "</td><td>" but "<th></th>".
                    line += fieldsep . replaceAll ( "td" , "th" ) ;
                  }
                  else
                  {
// Else we have to append the single actual fieldsep.
                    line += fieldsep ;
                  }
                }
// csv or htm: the final separator has to be removed from the end of the line.
                if ( resultFormatValue . equals ( resultFormatCsvValue ) || resultFormatValue . equals ( resultFormatHtmValue ) )
                {
                  line = line . substring ( 0 , line . length ( ) - fieldsep . length ( ) ) ;
                }
// htm case: the ending of the header has to be appended now.
                if ( resultFormatValue . equals ( resultFormatHtmValue ) )
                {
                  line += "</th></tr>" ;
                }
// Constructing the plusminus line in case of txt and leave it empty while others.
                if ( resultFormatValue . equals ( resultFormatTxtValue ) && scrollable )
                {
                  plusMinusLine = getPlusMinusLine ( colw , fieldsep ) ;
                }
                else
                {
                  plusMinusLine = "" ;
                }
              }
              catch ( SQLException e )
              {
// The exceptions of sql are not expected.
                systemexit ( "Exception - SQLException (3), constructResult" ) ;
              }
// The txt and the htm results has to be prepared.
              if ( resultFormatValue . equals ( resultFormatTxtValue ) )
              {
                resultString += plusMinusLine + newLineChar ;
              }
              else if ( resultFormatValue . equals ( resultFormatHtmValue ) )
              {
                resultString += "<table width=\"100%\" cellpadding=\"1\" cellspacing=\"1\" border=\"1\">" + newLineChar ;
              }
// If the header has to be included..
              if ( headerToInclude )
              {
// Then let's append this header line constructed before.
                resultString += line + newLineChar ;
// In case of txt result, the header separator plusminus line has to be appended.
                if ( resultFormatValue . equals ( resultFormatTxtValue ) )
                {
                  resultString += plusMinusLine + newLineChar ;
                }
              }
// We can step forward, if
// - we have lines in the result set. (we have counted it during the colw construction in case of scrollable result set)
// - or we have a not scollable result set. ( the lines remains the 0 at this time.)
// We would like to appending the actual data into the result string!
// So, we are looping on the result set (scrollable or not!) instead of the first case,
// where we have looped on the result set only if the result set is scrollable.
              if ( lines > 0 || ! scrollable )
              {
                try
                {
// beforeFirst can be called only on scrollable result set.
// (if it is not scrollable, its pointer has to be already at the beginning.)
                  if ( scrollable )
                  {
                    rs . beforeFirst ( ) ;
                  }
// Counting it again or first time.
                  lines = 0 ;
// Looping on the result set to display the data from it.
                  while ( rs . next ( ) )
                  {
// Plus one line..
                    lines ++ ;
// The line has to be started with fieldsep in case of txt result and the others can start with empty.
                    if ( resultFormatValue . equals ( resultFormatTxtValue ) )
                    {
                      line = fieldsep ;
                    }
                    else
                    {
                      line = "" ;
                    }
// We are in the first column.
                    j = 1 ;
// Looping on the columns, this time for the actual value and appending it into the end of the current line.
                    for ( String colname : cols )
                    {
// Special formattings first and then the else (others) case.
                      if ( dbType . equals ( dbTypeOracle ) && ( colt [ j - 1 ] . equals ( timestampltz ) || colt [ j - 1 ] . equals ( timestamptz ) || colt [ j - 1 ] . equals ( timestamp ) ) )
                      {
                        val = null ;
                        valStr = simpleDateFormatForTimestamps . format ( rs . getTimestamp ( colname ) ) ;
                      }
                      else
                      {
                        val = utils . getVal ( rs , colname ) ;
                        valStr = utils . getValStr ( val , nullStr ) ;
                      }
// If it is a htm result then we have further actions to do.
                      if ( resultFormatValue . equals ( resultFormatHtmValue ) )
                      {
// This object has to be written to the disk if necessary.
                        onTheDiskSeparately = valToTheDiskSeparately ( rs , htmFolderName + valStr , colname , colt [ j - 1 ] , dbtype ) ;
// The htm link will be placed instead of the single name of the object.
                        if ( onTheDiskSeparately )
                        {
                          valStr = "<a href=\"" + htmFolderName + valStr + "\" target=\"_blank\">" + valStr + "</a>" ;
                        }
// If the data (as string) is too long then it also wil be written into separate file
// and it will be replaced by a htm link in the htm table.
                        else if ( valStr . length ( ) > appMaxColLengthTxt )
                        {
                          writeFileContent ( valStr , htmFolderName + colname + lines ) ;
                          valStr = "<a href=\"" + htmFolderName + colname + lines + "\" target=\"_blank\">" + colname + lines + "</a>" ;
                        }
                      }
// Processing the line.
                      line = line + ( ( resultFormatValue . equals ( resultFormatTxtValue ) && scrollable ) ? utils . pad ( valStr , colw [ j - 1 ] , spaceChar ) : valStr ) + fieldsep ;
                      j ++ ;
                    }
// csv or htm: the final separator has to be removed from the end of the line.
                    if ( resultFormatValue . equals ( resultFormatCsvValue ) || resultFormatValue . equals ( resultFormatHtmValue ) )
                    {
                      line = line . substring ( 0 , line . length ( ) - fieldsep . length ( ) ) ;
                    }
// htm: the result has to be continued with the beginning of a htm line.
                    if ( resultFormatValue . equals ( resultFormatHtmValue ) )
                    {
                      resultString += "<tr><td>" ;
                    }
// Now adding the line.
                    resultString += line ;
// htm: the result has to be continued with the line ending string.
                    if ( resultFormatValue . equals ( resultFormatHtmValue ) )
                    {
                      resultString += "</td></tr>" ;
                    }
// And a newLineChar will be added.
                    resultString += newLineChar ;
                  }
// This is the end of the data displaying of the result set.
// In case of txt result, the last (third) plusminus line has to be printed out into the result String.
                  if ( resultFormatValue . equals ( resultFormatTxtValue ) )
                  {
                    resultString += plusMinusLine + newLineChar ;
                  }
                }
                catch ( SQLException e )
                {
// Exiting too if sqlexception has occurred.
                  systemexit ( "Exception - SQLException (4), constructResult" ) ;
                }
              }
// And finally the elapsed time will be appended in the way depending on the requested result format.
              if ( resultFormatValue . equals ( resultFormatTxtValue ) )
              {
                resultString += "" + spaceChar + lines + messageRowsSelected + newLineChar ;
                resultString += "" + spaceChar + elapsedFormatted + newLineChar ;
              }
              else if ( resultFormatValue . equals ( resultFormatCsvValue ) )
              {
                resultString += lines + messageRowsSelected + newLineChar ;
                resultString += elapsedFormatted + newLineChar ;
              }
              else if ( resultFormatValue . equals ( resultFormatHtmValue ) )
              {
                resultString += "<tr><td colspan=\"" + colw . length + "\">" + lines + messageRowsSelected + "</td></tr>" + newLineChar ;
                resultString += "<tr><td colspan=\"" + colw . length + "\">" + elapsedFormatted + "</td></tr>" + newLineChar ;
                resultString += "</table>" + newLineChar ;
              }
// Not used.
              j = 0 ;
              lines = 0 ;
              val = null ;
              valStr = null ;
              line = null ;
              plusMinusLine = null ;
            }
            else
            {
              systemexit ( "Error - rsmd is null, constructResult" ) ;
            }
          }
          catch ( Exception e )
          {
// Every exception results the empty result.
            resultString = "" ;
          }
// Not in use.
          rsmd = null ;
          colscount = 0 ;
          cols = null ;
          colw = null ;
          colt = null ;
          onTheDiskSeparately = false ;
        }
        else
        {
          systemexit ( "Error - fieldsep is null, constructResult" ) ;
        }
      }
      else
      {
        systemexit ( "Error - utils is null, constructResult" ) ;
      }
    }
    else
    {
// The result set is not usable so empty string should go back to the caller.
      resultString = "" ;
    }
// Not in use.
    fieldsep = null ;
    scrollable = false ;
    htmFolder = null ;
// Now we can return the result string.
    return resultString ;
  }
/*
** Writes the content of the current column in result set if necessary.
** By dbtype-by-dbtype, the columns will be decided to be in place or
** in separate files in the directory named the same as the result set file.
*/
  protected final boolean valToTheDiskSeparately ( ResultSet rs , String destfile , String colname , String coltype , String dbtype )
  {
    boolean separately = false ;
    if ( coltype != null && dbtype != null )
    {
      if ( dbtype . equals ( dbTypeOracle ) )
      {
        if ( coltype . equals ( blob ) )
        {
          separately = utils . getBlob ( rs , destfile , colname ) ;
        }
        else if ( coltype . equals ( clob ) || coltype . equals ( nclob ) )
        {
          separately = utils . getClob ( rs , destfile , colname , utf8 ) ;
        }
        else if ( coltype . equals ( raw ) || coltype . equals ( longraw ) )
        {
          separately = utils . getRaw ( rs , destfile , colname , bufflength ) ;
        }
        else if ( coltype . equals ( bfile ) )
        {
          separately = utils . getBfile ( rs , destfile , colname , bufflength ) ;
        }
      }
      else if ( dbtype . equals ( dbTypeMssql ) )
      {
        if ( coltype . equals ( image ) )
        {
          separately = utils . getBlob ( rs , destfile , colname ) ;
        }
        else if ( coltype . equals ( binary ) || coltype . equals ( varbinary ) || coltype . equals ( longvarbinary ) )
        {
          separately = utils . getRaw ( rs , destfile , colname , bufflength ) ;
        }
      }
      else if ( dbtype . equals ( dbTypeDb2 ) )
      {
        if ( coltype . equals ( blob ) )
        {
          separately = utils . getBlob ( rs , destfile , colname ) ;
        }
        else if ( coltype . equals ( clob ) || coltype . equals ( dbclob ) )
        {
          separately = utils . getClob ( rs , destfile , colname , utf8 ) ;
        }
        else if ( coltype . equals ( xml ) )
        {
          separately = utils . getXml ( rs , destfile , colname , utf8 ) ;
        }
      }
      else if ( dbtype . equals ( dbTypePostgresql ) )
      {
        if ( coltype . equals ( bytea ) )
        {
          separately = utils . getRaw ( rs , destfile , colname , bufflength ) ;
        }
        else if ( coltype . equals ( xml ) )
        {
          separately = utils . getXml ( rs , destfile , colname , utf8 ) ;
        }
      }
      else
      {
        systemexit ( "Error - dbtype has unexpected value, valToTheDiskSeparately" ) ;
      }
    }
    return separately ;
  }
/*
** Gets the separator line during constructing result in txt format.
** The colw array (containing the width values of each columns),
** the line length and the field separator are needed to do this.
*/
  protected final String getPlusMinusLine ( int [ ] colw , String fieldsep )
  {
// The length of the line.
    int len = fieldsep . length ( ) ;
    if ( colw . length > 0 )
    {
      for ( int i = 0 ; i < colw . length ; i ++ )
      {
        len += colw [ i ] + fieldsep . length ( ) ;
      }
    }
    else
    {
      len += fieldsep . length ( ) ;
    }
// The final line will be this.
    char [ ] plusMinusLine = new char [ len ] ;
// This A and B is the key. Dependin on the length of the field separator.
    int B = fieldsep . length ( ) ;
    int A = ( int ) Math . floor ( B / 2 ) ;
// The positions in which a plus character has to be written.
    int index = 0 ;
// Let it be a line with 1 space - minuses - 1 space.
    for ( int i = 0 ; i < len ; i ++ )
    {
      if ( i == 0 || i == len - 1 )
      {
        plusMinusLine [ i ] = spaceChar ;
      }
      else
      {
        plusMinusLine [ i ] = minusChar ;
      }
    }
// Let's change the characters into pluses when needed.
    for ( int j = 0 ; j <= colw . length ; j ++ )
    {
// The index of the plus line will be calculated
      if ( j == 0 )
      {
        index = A ;
      }
      else
      {
        index = index + B + colw [ j - 1 ] ;
      }
      plusMinusLine [ index ] = plusChar ;
    }
// Not used.
    len = 0 ;
    A = 0 ;
    B = 0 ;
    index = 0 ;
// Return this line.
    return String . valueOf ( plusMinusLine ) ;
  }
/*
** The result target has to be valid.
** Every combination of resultTargetConsValue and/or resultTargetFileValue are acceptable.
*/
  protected final boolean isValidResultTargets ( String resultTargets )
  {
    return resultTargetConsValue . equals ( resultTargets ) || resultTargetFileValue . equals ( resultTargets ) || ( resultTargetConsValue + resultTargetFileValue ) . equals ( resultTargets ) || ( resultTargetFileValue + resultTargetConsValue ) . equals ( resultTargets ) ;
  }
/*
** The result format has to be valid.
** Every combination of the resultFormatTxtValue and/or resultFormatCsvValue and/or resultFormatHtmValue are acceptable.
*/
  protected final boolean isValidResultFormats ( String resultTargets )
  {
    return resultFormatTxtValue . equals ( resultTargets ) || resultFormatCsvValue . equals ( resultTargets ) || resultFormatHtmValue . equals ( resultTargets ) || ( resultFormatTxtValue + resultFormatCsvValue ) . equals ( resultTargets ) || ( resultFormatCsvValue + resultFormatTxtValue ) . equals ( resultTargets ) || ( resultFormatTxtValue + resultFormatHtmValue ) . equals ( resultTargets ) || ( resultFormatHtmValue + resultFormatTxtValue ) . equals ( resultTargets ) || ( resultFormatHtmValue + resultFormatCsvValue ) . equals ( resultTargets ) || ( resultFormatCsvValue + resultFormatHtmValue ) . equals ( resultTargets ) || ( resultFormatTxtValue + resultFormatCsvValue + resultFormatHtmValue ) . equals ( resultTargets ) || ( resultFormatTxtValue + resultFormatHtmValue + resultFormatCsvValue ) . equals ( resultTargets ) || ( resultFormatCsvValue + resultFormatTxtValue + resultFormatHtmValue ) . equals ( resultTargets ) || ( resultFormatCsvValue + resultFormatHtmValue + resultFormatTxtValue ) . equals ( resultTargets ) || ( resultFormatHtmValue + resultFormatTxtValue + resultFormatCsvValue ) . equals ( resultTargets ) || ( resultFormatHtmValue + resultFormatCsvValue + resultFormatTxtValue ) . equals ( resultTargets ) ;
  }
/*
** Is this connection in use?
** If there are at least 1 query (whatever state) then this connection is in use.
*/
  protected final boolean isConnectionInUse ( String dbtype , String connna , boolean messageIfNot )
  {
// In use by default.
    boolean inUse = true ;
    if ( dbtype != null && connna != null && utils != null )
    {
      ArrayList < Integer > queryIds = getQueriesByConnection ( dbtype , connna ) ;
      if ( queryIds != null )
      {
// Not in use only if the size of this list is 0.
// Messages will go to the user if it has been requested.
        if ( queryIds . size ( ) == 0 )
        {
          inUse = false ;
          if ( messageIfNot )
          {
            outprintln ( fold + messageThisConnectionIsNotInUseByAddedQuery ) ;
          }
        }
        else
        {
          inUse = true ;
          outprintln ( "" + ( messageIfNot ? "" : newLineChar ) + fold + messageThisConnectionIsInUse + utils . joinArrayListInteger ( queryIds , sep1 ) ) ;
        }
      }
      else
      {
        systemexit ( "Error - queryIds is null, isConnectionInUse" ) ;
      }
// Not in use any more.
      queryIds = null ;
    }
    else
    {
      systemexit ( "Error - one of these is null: dbtype|connna|utils, isConnectionInUse" ) ;
    }
// Returning the usage.
    return inUse ;
  }
}