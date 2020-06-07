/*
** This class is the part of the opensource MyDbConns application.
**
** See the header comment lines of Main class.
**
** Qdata
** This is one of the most important parts of this application.
** The hashmaps are for store the data of the executed queries
** running in separate threads.
*/
package com . kisscodesystems . MyDbConns ;
import java . sql . Connection ;
import java . sql . ResultSet ;
import java . sql . ResultSetMetaData ;
import java . sql . SQLException ;
import java . sql . Statement ;
import java . util . ArrayList ;
import java . util . Date ;
import java . util . HashMap ;
public class Qdata extends Conn
{
// All of the added queries ("query add single", "query add multiple", "query add batch")
// must have a unique queryId to identify it. This would be stored in this nextQueryId.
  protected volatile int nextQueryId = - 1 ;
// Storing the database types of the queries.
  protected volatile HashMap < Integer , String > queryDbTypes = new HashMap < Integer , String > ( ) ;
// Storing the connection names of the queries.
// The queryDbTypes and the queryConnnas together can identify the data of the database connection to use!
  protected volatile HashMap < Integer , String > queryConnnas = new HashMap < Integer , String > ( ) ;
// Storing the titles of the queries. This is just a short name to remember easily.
  protected volatile HashMap < Integer , String > queryTitles = new HashMap < Integer , String > ( ) ;
// These are the query strings to execute. Can be null if the queryFiles entry is not null!
// If we specify a query string this is nnot modifyable later.
// The user can cancel, delete or re-run this type of query but the query string itself will remain the same.
  protected volatile HashMap < Integer , String > queryStrings = new HashMap < Integer , String > ( ) ;
// These are the files containing the actual value of the query string.
// The queryStrings entry can be null when the queryFiles entry is not null.
// If this queryFiles entry is not null then the query string will be read dynamically
// at run time, every time when run or re-run the query. The above queryStrings entry will
// be updated every time when run query with not null queryFiles entry.
// (so it is possible to change the query string in this way.)
  protected volatile HashMap < Integer , String > queryFiles = new HashMap < Integer , String > ( ) ;
// This is the delimiter to separate the queries if there are two or more.
// Multiple queries can use this property.
// This delimiter is the value which has been set by the user from its "delimiter change " command.
  protected volatile HashMap < Integer , String > queryDelimiters = new HashMap < Integer , String > ( ) ;
// Storing the type of the query.
// argSingle, argMultiple or argBatch are the possible values.
  protected volatile HashMap < Integer , String > queryTypes = new HashMap < Integer , String > ( ) ;
// In single query mode, the user can set it to yes or no.
// If the statement and the result set are not scrollable, then there may be problems some datatype
// to handle while selecting these.
// The user gets the opportunity to create not scrollable result sets.
// The default is scrollable.
  protected volatile HashMap < Integer , String > queryIsScrollables = new HashMap < Integer , String > ( ) ;
// In batch queries: how many queries have to be executed at the same time in statement . executeBatch ( ).
  protected volatile HashMap < Integer , Integer > queryBatchExecCounts = new HashMap < Integer , Integer > ( ) ;
// In batch queries: user can specify a file that contains the data used by the batch executed query.
//                   This or the source queryId has to be specified. (the other will be null)
  protected volatile HashMap < Integer , String > queryBatchSourceFiles = new HashMap < Integer , String > ( ) ;
// In batch queries: user can specify a result set of a single query that contains the data used by the batch executed query.
//                   This or the source file has to be specified. (the other will be null)
//                   The file has to contain the columns in the first line!
//                   (Otherwise we cannot know which field is stored in that data column.)
  protected volatile HashMap < Integer , Integer > queryBatchSourceQueryIds = new HashMap < Integer , Integer > ( ) ;
// In batch queries: The user can enter the fields (columns) which have to be used to execute its batch query.
//                   This set has to be the subset of the set of the source columns (coming from source file or source queryId).
  protected volatile HashMap < Integer , String > queryBatchSourceFields = new HashMap < Integer , String > ( ) ;
// Storing the connection object of the query.
// The separate query thread will create this and will store into this hashmap for further use.
  protected volatile HashMap < Integer , Connection > queryConnections = new HashMap < Integer , Connection > ( ) ;
// Storing the Statement object of the queries.
// This is also made by the executor thread of the query and stores here.
// This can be Statement (single or multiple queries) or PreparedStatement (batch queries).
// This object can be used for example to cancel that query in the Queries thread.
  protected volatile HashMap < Integer , Statement > queryStatements = new HashMap < Integer , Statement > ( ) ;
// Storing the result sets of the queries.
// Only the single query will have result set, multiple and batch queries will have null here.
  protected volatile HashMap < Integer , ResultSet > queryResultSets = new HashMap < Integer , ResultSet > ( ) ;
// Storing the starting timestamps of the queries.
  protected volatile HashMap < Integer , Date > queryStartDates = new HashMap < Integer , Date > ( ) ;
// Storing the ending timestamps of the queries.
  protected volatile HashMap < Integer , Date > queryEndDates = new HashMap < Integer , Date > ( ) ;
// Storing error messages here.
// These error messages can come from the executing of the sql query
// or from any part of the preparing operation in the executor thread.
  protected volatile HashMap < Integer , String > queryErrorMessages = new HashMap < Integer , String > ( ) ;
// Storing the references of the query executor threads!
  protected volatile HashMap < Integer , Thread > queryThreads = new HashMap < Integer , Thread > ( ) ;
/*
** Getter functions of the properties of queries above.
*/
  protected final String getQueryDbType ( int queryId )
  {
    if ( queryDbTypes != null )
    {
      return queryDbTypes . get ( queryId ) ;
    }
    else
    {
      systemexit ( "Error - queryDbTypes is null, getQueryDbType" ) ;
      return null ;
    }
  }
  protected final String getQueryConnna ( int queryId )
  {
    if ( queryConnnas != null )
    {
      return queryConnnas . get ( queryId ) ;
    }
    else
    {
      systemexit ( "Error - queryConnnas is null, getQueryConnna" ) ;
      return null ;
    }
  }
  protected final String getQueryTitle ( int queryId )
  {
    if ( queryTitles != null )
    {
      return queryTitles . get ( queryId ) ;
    }
    else
    {
      systemexit ( "Error - queryTitles is null, getQueryTitle" ) ;
      return null ;
    }
  }
  protected final String getQueryString ( int queryId )
  {
    if ( queryStrings != null )
    {
      return queryStrings . get ( queryId ) ;
    }
    else
    {
      systemexit ( "Error - queryStrings is null, getQueryString" ) ;
      return null ;
    }
  }
  protected final String getQueryFile ( int queryId )
  {
    if ( queryFiles != null )
    {
      return queryFiles . get ( queryId ) ;
    }
    else
    {
      systemexit ( "Error - queryFiles is null, getQueryFile" ) ;
      return null ;
    }
  }
  protected final String getQueryDelimiter ( int queryId )
  {
    if ( queryDelimiters != null )
    {
      return queryDelimiters . get ( queryId ) ;
    }
    else
    {
      systemexit ( "Error - queryDelimiters is null, getQueryDelimiter" ) ;
      return null ;
    }
  }
  protected final String getQueryType ( int queryId )
  {
    if ( queryTypes != null )
    {
      return queryTypes . get ( queryId ) ;
    }
    else
    {
      systemexit ( "Error - queryTypes is null, getQueryType" ) ;
      return null ;
    }
  }
  protected final String getQueryIsScrollable ( int queryId )
  {
    if ( queryIsScrollables != null )
    {
      return queryIsScrollables . get ( queryId ) ;
    }
    else
    {
      systemexit ( "Error - queryIsScrollables is null, getQueryIsScrollable" ) ;
      return null ;
    }
  }
  protected final int getQueryBatchExecCount ( int queryId )
  {
    if ( queryBatchExecCounts != null )
    {
      return queryBatchExecCounts . get ( queryId ) ;
    }
    else
    {
      systemexit ( "Error - queryBatchExecCounts is null, getQueryBatchExecCount" ) ;
      return 0 ;
    }
  }
  protected final String getQueryBatchSourceFile ( int queryId )
  {
    if ( queryBatchSourceFiles != null )
    {
      return queryBatchSourceFiles . get ( queryId ) ;
    }
    else
    {
      systemexit ( "Error - queryBatchSourceFiles is null, getQueryBatchSourceFile" ) ;
      return null ;
    }
  }
  protected final int getQueryBatchSourceQueryId ( int queryId )
  {
    if ( queryBatchSourceQueryIds != null )
    {
      return queryBatchSourceQueryIds . get ( queryId ) ;
    }
    else
    {
      systemexit ( "Error - queryBatchSourceQueryIds is null, getQueryBatchSourceQueryId" ) ;
      return 0 ;
    }
  }
  protected final String getQueryBatchSourceField ( int queryId )
  {
    if ( queryBatchSourceFields != null )
    {
      return queryBatchSourceFields . get ( queryId ) ;
    }
    else
    {
      systemexit ( "Error - queryBatchSourceFields is null, getQueryBatchSourceField" ) ;
      return null ;
    }
  }
  protected final Connection getQueryConnection ( int queryId )
  {
    if ( queryConnections != null )
    {
      return queryConnections . get ( queryId ) ;
    }
    else
    {
      systemexit ( "Error - queryConnections is null, getQueryConnection" ) ;
      return null ;
    }
  }
  protected final Statement getQueryStatement ( int queryId )
  {
    if ( queryStatements != null )
    {
      return queryStatements . get ( queryId ) ;
    }
    else
    {
      systemexit ( "Error - queryStatements is null, getQueryStatement" ) ;
      return null ;
    }
  }
  protected final ResultSet getQueryResultSet ( int queryId )
  {
    if ( queryResultSets != null )
    {
      return queryResultSets . get ( queryId ) ;
    }
    else
    {
      systemexit ( "Error - queryResultSets is null, getQueryResultSet" ) ;
      return null ;
    }
  }
  protected final Date getQueryStartDate ( int queryId )
  {
    if ( queryStartDates != null )
    {
      return queryStartDates . get ( queryId ) ;
    }
    else
    {
      systemexit ( "Error - queryStartDates is null, getQueryStartDate" ) ;
      return null ;
    }
  }
  protected final Date getQueryEndDate ( int queryId )
  {
    if ( queryEndDates != null )
    {
      return queryEndDates . get ( queryId ) ;
    }
    else
    {
      systemexit ( "Error - queryEndDates is null, getQueryEndDate" ) ;
      return null ;
    }
  }
  protected final String getQueryErrorMessage ( int queryId )
  {
    if ( queryErrorMessages != null )
    {
      return queryErrorMessages . get ( queryId ) ;
    }
    else
    {
      systemexit ( "Error - queryErrorMessages is null, getQueryErrorMessage" ) ;
      return null ;
    }
  }
  protected final Thread getQueryThread ( int queryId )
  {
    if ( queryThreads != null )
    {
      return queryThreads . get ( queryId ) ;
    }
    else
    {
      systemexit ( "Error - queryThreads is null, getQueryThread" ) ;
      return null ;
    }
  }
/*
** Setter methods of the properties of queries above.
*/
  protected final synchronized void setQueryDbType ( int queryId , String queryDbType )
  {
    if ( queryDbTypes != null )
    {
      queryDbTypes . put ( queryId , queryDbType ) ;
    }
    else
    {
      systemexit ( "Error - queryDbTypes is null, setQueryDbType" ) ;
    }
  }
  protected final synchronized void setQueryConnna ( int queryId , String queryConnna )
  {
    if ( queryConnnas != null )
    {
      queryConnnas . put ( queryId , queryConnna ) ;
    }
    else
    {
      systemexit ( "Error - queryConnnas is null, setQueryConnna" ) ;
    }
  }
  protected final synchronized void setQueryTitle ( int queryId , String queryTitle )
  {
    if ( queryTitles != null )
    {
      queryTitles . put ( queryId , queryTitle ) ;
    }
    else
    {
      systemexit ( "Error - queryTitles is null, setQueryTitle" ) ;
    }
  }
  protected final synchronized void setQueryString ( int queryId , String queryString )
  {
    if ( queryStrings != null )
    {
      queryStrings . put ( queryId , queryString ) ;
    }
    else
    {
      systemexit ( "Error - queryStrings is null, setQueryString" ) ;
    }
  }
  protected final synchronized void setQueryFile ( int queryId , String queryFile )
  {
    if ( queryFiles != null )
    {
      queryFiles . put ( queryId , queryFile ) ;
    }
    else
    {
      systemexit ( "Error - queryFiles is null, setQueryFile" ) ;
    }
  }
  protected final synchronized void setQueryDelimiter ( int queryId , String queryDelimiter )
  {
    if ( queryDelimiters != null )
    {
      queryDelimiters . put ( queryId , queryDelimiter ) ;
    }
    else
    {
      systemexit ( "Error - queryDelimiters is null, setQueryDelimiter" ) ;
    }
  }
  protected final synchronized void setQueryType ( int queryId , String queryType )
  {
    if ( queryTypes != null )
    {
      queryTypes . put ( queryId , queryType ) ;
    }
    else
    {
      systemexit ( "Error - queryTypes is null, setQueryType" ) ;
    }
  }
  protected final synchronized void setQueryIsScrollable ( int queryId , String queryIsScrollable )
  {
    if ( queryIsScrollables != null )
    {
      queryIsScrollables . put ( queryId , queryIsScrollable ) ;
    }
    else
    {
      systemexit ( "Error - queryIsScrollables is null, setQueryIsScrollable" ) ;
    }
  }
  protected final synchronized void setQueryBatchExecCount ( int queryId , int queryBatchExecCount )
  {
    if ( queryBatchExecCounts != null )
    {
      queryBatchExecCounts . put ( queryId , queryBatchExecCount ) ;
    }
    else
    {
      systemexit ( "Error - queryBatchExecCounts is null, setQueryBatchExecCount" ) ;
    }
  }
  protected final synchronized void setQueryBatchSourceFile ( int queryId , String queryBatchSourceFile )
  {
    if ( queryBatchSourceFiles != null )
    {
      queryBatchSourceFiles . put ( queryId , queryBatchSourceFile ) ;
    }
    else
    {
      systemexit ( "Error - queryBatchSourceFiles is null, setQueryBatchSourceFile" ) ;
    }
  }
  protected final synchronized void setQueryBatchSourceQueryId ( int queryId , int queryBatchSourceQueryId )
  {
    if ( queryBatchSourceQueryIds != null )
    {
      queryBatchSourceQueryIds . put ( queryId , queryBatchSourceQueryId ) ;
    }
    else
    {
      systemexit ( "Error - queryBatchSourceQueryIds is null, setQueryBatchSourceQueryId" ) ;
    }
  }
  protected final synchronized void setQueryBatchSourceField ( int queryId , String queryBatchSourceField )
  {
    if ( queryBatchSourceFields != null )
    {
      queryBatchSourceFields . put ( queryId , queryBatchSourceField ) ;
    }
    else
    {
      systemexit ( "Error - queryBatchSourceFields is null, setQueryBatchSourceField" ) ;
    }
  }
  protected final synchronized void setQueryConnection ( int queryId , Connection queryConnection )
  {
    if ( queryConnections != null )
    {
      queryConnections . put ( queryId , queryConnection ) ;
    }
    else
    {
      systemexit ( "Error - queryConnections is null, setQueryConnection" ) ;
    }
  }
  protected final synchronized void setQueryStatement ( int queryId , Statement queryStatement )
  {
    if ( queryStatements != null )
    {
      queryStatements . put ( queryId , queryStatement ) ;
    }
    else
    {
      systemexit ( "Error - queryStatements is null, setQueryStatement" ) ;
    }
  }
  protected final synchronized void setQueryResultSet ( int queryId , ResultSet queryResultSet )
  {
    if ( queryResultSets != null )
    {
      queryResultSets . put ( queryId , queryResultSet ) ;
    }
    else
    {
      systemexit ( "Error - queryResultSets is null, setQueryResultSet" ) ;
    }
  }
  protected final synchronized void setQueryStartDate ( int queryId , Date queryStartDate )
  {
    if ( queryStartDates != null )
    {
      queryStartDates . put ( queryId , queryStartDate ) ;
    }
    else
    {
      systemexit ( "Error - queryStartDates is null, setQueryStartDate" ) ;
    }
  }
  protected final synchronized void setQueryEndDate ( int queryId , Date queryEndDate )
  {
    if ( queryEndDates != null )
    {
      queryEndDates . put ( queryId , queryEndDate ) ;
    }
    else
    {
      systemexit ( "Error - queryEndDates is null, setQueryEndDate" ) ;
    }
  }
  protected final synchronized void setQueryErrorMessage ( int queryId , String queryErrorMessage )
  {
    if ( queryErrorMessages != null )
    {
      queryErrorMessages . put ( queryId , queryErrorMessage ) ;
    }
    else
    {
      systemexit ( "Error - queryErrorMessages is null, setQueryErrorMessage" ) ;
    }
  }
  protected final synchronized void setQueryThread ( int queryId , Thread queryThread )
  {
    if ( queryThreads != null )
    {
      queryThreads . put ( queryId , queryThread ) ;
    }
    else
    {
      systemexit ( "Error - queryThreads is null, setQueryThread" ) ;
    }
  }
/*
** Getting the next ID for the next query.
*/
  protected final synchronized int getNextQueryId ( )
  {
    return ++ nextQueryId ;
  }
/*
** Freeing the resources used by a query.
*/
  protected final void freeQueryResources ( int queryId )
  {
// Closing result set.
    ResultSet resultSet = getQueryResultSet ( queryId ) ;
    if ( resultSet != null )
    {
      try
      {
        resultSet . close ( ) ;
      }
      catch ( Exception e )
      {
        outprintln ( messageFailedToCloseResultSet + queryId + spaceChar + e . toString ( ) ) ;
      }
    }
// Closing statement.
    Statement statement = getQueryStatement ( queryId ) ;
    if ( statement != null )
    {
      try
      {
        statement . close ( ) ;
      }
      catch ( Exception e )
      {
        outprintln ( messageFailedToCloseStatement + queryId + spaceChar + e . toString ( ) ) ;
      }
    }
// Closing connection.
    Connection connection = getQueryConnection ( queryId ) ;
    if ( connection != null )
    {
      try
      {
        connection . close ( ) ;
      }
      catch ( Exception e )
      {
        outprintln ( messageFailedToCloseConnection + queryId + spaceChar + e . toString ( ) ) ;
      }
    }
  }
/*
** Deletes a query.
** It is possible only when the state of the query is not running!
*/
  protected final void deleteQuery ( int queryId , boolean messageIfSuccess )
  {
// A valid query id is required to complete this operation.
    if ( isValidQueryId ( queryId ) )
    {
// The state of this query is..
      String queryState = getQueryState ( queryId ) ;
// If it is not started or finished successfully or finished with errors then it can be deleted.
      if ( ! queryState . equals ( queryStateRunning ) )
      {
// Freeing the resources at first.
        freeQueryResources ( queryId ) ;
// Waiting for the thread to be joined
        joinQueryThread ( queryId ) ;
// Then remove the entries from the hashmaps.
        queryDbTypes . remove ( queryId ) ;
        queryConnnas . remove ( queryId ) ;
        queryTitles . remove ( queryId ) ;
        queryStrings . remove ( queryId ) ;
        queryFiles . remove ( queryId ) ;
        queryDelimiters . remove ( queryId ) ;
        queryTypes . remove ( queryId ) ;
        queryIsScrollables . remove ( queryId ) ;
        queryBatchExecCounts . remove ( queryId ) ;
        queryBatchSourceFiles . remove ( queryId ) ;
        queryBatchSourceQueryIds . remove ( queryId ) ;
        queryBatchSourceFields . remove ( queryId ) ;
        queryConnections . remove ( queryId ) ;
        queryStatements . remove ( queryId ) ;
        queryResultSets . remove ( queryId ) ;
        queryStartDates . remove ( queryId ) ;
        queryEndDates . remove ( queryId ) ;
        queryErrorMessages . remove ( queryId ) ;
        queryThreads . remove ( queryId ) ;
// A message can go to the user if requested.
        if ( messageIfSuccess )
        {
          outprintln ( messageQueryIsDeleted + " (" + queryId + ")" ) ;
        }
      }
      else
      {
// If it is running then cannot be deleted.
        outprintln ( messageQueryCannotBeDeleted + " (" + queryId + ") " + queryState ) ;
      }
// Not needed any more.
      queryState = null ;
    }
  }
/*
** Joins the thread of the query into the Queries.
*/
  protected final void joinQueryThread ( int queryId )
  {
// Validquery id is required.
    if ( isValidQueryId ( queryId ) )
    {
// The thread of the query executor.
      Thread thread = getQueryThread ( queryId ) ;
      if ( thread != null )
      {
// A message informs the user about the starting of thread joining.
        outprint ( messageJoiningQueryThread + thread . getName ( ) + singleSpace + "(" + getQueryTitle ( queryId ) + ")" + doubleDot + spaceChar ) ;
        try
        {
// The thread is joining.
          thread . join ( ) ;
// It is done!
          outprintln ( singleSpace + messageDone ) ;
        }
        catch ( InterruptedException e )
        {
          System . out . println ( fold2 + "Exception while joining single query thread (id: " + queryId + "): " + e . toString ( ) ) ;
        }
      }
// Not usable.
      thread = null ;
    }
  }
/*
** Cancels all of the active (running) queries.
*/
  protected final boolean cancelAllQueries ( boolean toConfirm )
  {
// Success only at the end of all of the operations.
    boolean success = false ;
// We would like to count the cancelled queries.
    int counter = 0 ;
// This is for determining whether to continue or not.
    boolean toContinue = false ;
// The valid connections password is required to be typed.
    if ( isFileContentConnectionsOrigReady ( ) )
    {
      if ( queryStateRunning != null )
      {
// If the dbType has been set then the user can read this to see
// what kind of queries will be cancelled.
        if ( ! "" . equals ( dbType ) )
        {
          outprint ( newLineString + fold + dbType + ":" ) ;
        }
// If it has to be confirmed..
        if ( toConfirm )
        {
// ..then asking the user.
          if ( readYesElseAnything ( messageDoYouWantToCancelAllQueries , messageQueriesWontBeCancelled ) )
          {
// If the answer is yes, we can continue.
            toContinue = true ;
          }
        }
        else
        {
// If it is not has to be confirmed then we can go.
          toContinue = true ;
        }
// If we can do this operation.
        if ( toContinue )
        {
// Looping thru the hashmap of dbtypes.
          for ( HashMap . Entry < Integer , String > queryDbType : queryDbTypes . entrySet ( ) )
          {
            if ( queryDbType != null )
            {
              if ( queryDbType . getKey ( ) != null )
              {
                if ( queryDbType . getValue ( ) != null )
                {
// The queries have the type running which can be cancelled.
                  if ( queryStateRunning . equals ( getQueryState ( queryDbType . getKey ( ) ) ) )
                  {
// Cancelling only if the current database type has the same value as dbType
// or the dbType has the empty string value.
                    if ( "" . equals ( dbType ) || queryDbType . getValue ( ) . equals ( dbType ) )
                    {
// Just for formatting..
                      if ( counter == 0 )
                      {
                        outprintln ( "" ) ;
                      }
// Cancelling this query now.
                      cancelQuery ( queryDbType . getKey ( ) ) ;
// Plus one query.
                      counter ++ ;
                    }
                  }
                }
                else
                {
                  systemexit ( "Error - queryDbTypeValue is null, cancelAllQueries" ) ;
                }
              }
              else
              {
                systemexit ( "Error - queryDbTypeKey is null, cancelAllQueries" ) ;
              }
            }
            else
            {
              systemexit ( "Error - queryDbType is null, cancelAllQueries" ) ;
            }
          }
// Messages to the user depending on how mutch queries have been cancelled.
          if ( counter == 0 )
          {
            outprintln ( messageNoRunningQueriesHaveBeenFoundToCancel ) ;
          }
          else if ( counter == 1 )
          {
            outprintln ( messageOneRunningQueryHasBeenCancelled ) ;
          }
          else
          {
            outprintln ( messageRunningQueriesHaveBeenCancelled + counter ) ;
          }
// Success only here.
          success = true ;
        }
      }
      else
      {
        systemexit ( "Error - queryStateRunning is null, cancelAllQueries" ) ;
      }
    }
// Returning of the success.
    return success ;
  }
/*
** Cancels a query.
*/
  protected final void cancelQuery ( int queryId )
  {
// A valid query id is required of course.
    if ( isValidQueryId ( queryId ) )
    {
// The state of the query is..
      String queryState = getQueryState ( queryId ) ;
// Running query state is required.
      if ( queryState . equals ( queryStateRunning ) )
      {
// This is the statement object to be used for cancelling.
        Statement statement = getQueryStatement ( queryId ) ;
        if ( statement != null )
        {
// Trying to cancel this quuery.
          try
          {
// The start date has to be cleared at first! (batch queries)
            setQueryStartDate ( queryId , null ) ;
// Cancel the query thru its statement object.
            statement . cancel ( ) ;
// The end date and the error message has to be cleared too.
            setQueryEndDate ( queryId , null ) ;
            setQueryErrorMessage ( queryId , null ) ;
// The resources are not needed any more.
// (if the user want to re-run a query,
// new connection, statement and result sets will be created.)
            freeQueryResources ( queryId ) ;
// Message to the user of successful cancelling.
            outprintln ( messageQueryIsCancelled + " (" + queryId + ")" ) ;
          }
          catch ( Exception e )
          {
// The user gets message if it is not successful.
            outprintln ( messageQueryCannotBeCancelled + " (" + queryId + ") " + e . getMessage ( ) ) ;
          }
        }
        else
        {
          systemexit ( "Error - statement is null, cancelQuery" ) ;
        }
// Not usable.
        statement = null ;
      }
      else
      {
        outprintln ( messageQueryCannotBeCancelled + " (" + queryId + ") " + queryState ) ;
      }
// Not usable.
      queryState = null ;
    }
  }
/*
** Starts a query.
** - start date is null: can be started
** - start date is not null.
**   - end date is null: it is running and user has to cancel first.
**   - end date is not null: finished (successfully or with errors)
**                           user has to confirm to drop existing results.
*/
  protected final void startQuery ( int queryId )
  {
// A valid query id is required!
    if ( isValidQueryId ( queryId ) )
    {
// If the start date is null then it can be started.
      if ( getQueryStartDate ( queryId ) == null )
      {
// The thread object is needed, because this will be started!
        Thread queryThread = getQueryThread ( queryId ) ;
        if ( queryThread != null )
        {
// Starting this thread.
          queryThread . start ( ) ;
// Displaying the result if it will have.
          displayResultImmediately ( queryId ) ;
        }
        else
        {
          systemexit ( "Error - queryThread is null, startQuery" ) ;
        }
// Not needed.
        queryThread = null ;
      }
      else
      {
// The start date is not null.
// In this situation, we can consider the belows.
        boolean startOver = false ;
// The end timestamp can be null.
        if ( getQueryEndDate ( queryId ) == null )
        {
// So, the user will be warned with the fact: the query is still running and has to be
// cancelled if it is really needed to start over this query.
          outprintln ( messageQueryHasNotBeenFinished ) ;
          if ( readYesElseAnything ( messageDoYouWantToCancelThisQuery , messageQueryWontBeCancelled ) )
          {
// Cancelling if the user wants that.
            cancelQuery ( queryId ) ;
// We can start over now.
            startOver = true ;
          }
        }
        else
        {
// If we are here, then the query has been finished and a confirmation is needed from the
// user to drop the existing results before starting over.
          if ( readYesElseAnything ( messageSureReRunQueryAndDropExistingResult , messageQueryWontBeReRun ) )
          {
// The query will be started over if the user confirmes this.
            startOver = true ;
          }
        }
// If we can start over..
        if ( startOver )
        {
// Drop existing objects
          setQueryConnection ( queryId , null ) ;
          setQueryStatement ( queryId , null ) ;
          setQueryResultSet ( queryId , null ) ;
          setQueryStartDate ( queryId , null ) ;
          setQueryEndDate ( queryId , null ) ;
          setQueryErrorMessage ( queryId , null ) ;
// And drop the thread of this query.
          setQueryThread ( queryId , null ) ;
// Creating a new Query object to execute this query.
          Query query = new Query ( ) ;
// The reference of this current Queries object
// has to be given to the new Query object.
          query . setParentRef ( ( Queries ) this ) ;
// Creating the new Thread to execute this query.
          Thread queryThreadNew = new Thread ( query ) ;
// Setting the name of it: will be named as the id of the query.
          queryThreadNew . setName ( String . valueOf ( queryId ) ) ;
// Adding the reference of this thread object into the hashmap.
          setQueryThread ( queryId , queryThreadNew ) ;
// Starting the query now.
          queryThreadNew . start ( ) ;
// Displaying the result if there will be any.
          displayResultImmediately ( queryId ) ;
// These are not needed.
          queryThreadNew = null ;
          query = null ;
        }
// Not necessary.
        startOver = false ;
      }
    }
  }
/*
** Saving a brand new query to be executed.
** Called from the save query method.
** Every properti of the new query are required here.
*/
  protected final int saveQuery ( String dbtype , String connna , String qtitle , String qstrin , String qfile , String qdelim , String qtype , String qscroll , int qbatchcount , String qbatchsfile , int qbatchsqueryid , String qbatchsfields )
  {
// This will be the
    int queryId = getNextQueryId ( ) ;
// Setting all of the data into the correct hashmap.
    setQueryDbType ( queryId , dbtype ) ;
    setQueryConnna ( queryId , connna ) ;
    setQueryTitle ( queryId , qtitle ) ;
    setQueryString ( queryId , qstrin ) ;
    setQueryFile ( queryId , qfile ) ;
    setQueryDelimiter ( queryId , qdelim ) ;
    setQueryType ( queryId , qtype ) ;
    setQueryIsScrollable ( queryId , qscroll ) ;
    setQueryBatchExecCount ( queryId , qbatchcount ) ;
    setQueryBatchSourceFile ( queryId , qbatchsfile ) ;
    setQueryBatchSourceQueryId ( queryId , qbatchsqueryid ) ;
    setQueryBatchSourceField ( queryId , qbatchsfields ) ;
// Creating and saving the thread of this new query.
    Query query = new Query ( ) ;
    query . setParentRef ( ( Queries ) this ) ;
    Thread queryThread = new Thread ( query ) ;
    queryThread . setName ( String . valueOf ( queryId ) ) ;
    setQueryThread ( queryId , queryThread ) ;
// Message to the user of the successful query saving.
    outprintln ( messageSavedQueryId + queryId ) ;
// Not needed any more.
    query = null ;
    queryThread = null ;
// Returning the id of the newly created query.
    return queryId ;
  }
/*
** Adding a query.
** Constructing and asking from the user all of the data necessary to save a new query.
*/
  protected final void addQuery ( String queryType )
  {
// The valid password of the connections is necessary.
    if ( isFileContentConnectionsOrigReady ( ) )
    {
// Database type and database connection name.
// These two can determine the data of the connection to execute this query.
      String dbtype = null ;
      String connna = null ;
// This will be the delimiter of the query. (multiple query will use this)
      String qdelim = null ;
// A title of the query.
      String qtitle = null ;
// Query from file? (if blank -> from console, if not empty -> the content of the given file will be used as the query)
      String qfromf = null ;
// This will be the query string. Null if it should come from file. (qfile is null at this time.)
      String qstrin = null ;
// This will be the name of the file from the query string will come. (qstrin is null at this time.)
      String qfile = null ;
// The position of the used connection will be this. (-1: not found by default)
      int connPos = - 1 ;
// What is the delimiter to show to the user?
      String delimToShow = null ;
// Needed for the query reading from console.
      boolean aReq = false ;
// The count of sqls to execute at the same batch.
      int qbatchc = 0 ;
// The file to use as the source of batch
      String batchSourceFromFile = null ;
// The query id (its result set object) to use as the source of batch
      int batchSourceFromQueryId = - 1 ;
// The fields to use while executing the user's batch query.
      String sourceFields = null ;
// Reading the batch source from the user.
      String batchSourceFrom = null ;
// Reading the scrollable property of this single query
      String queryIsScrollable = null ;
// The id of the newly saved query.
      int queryId = - 1 ;
// The confirmation message about can we run the query immediately.
      String runnow = null ;
// The value of dbType will be used or the user will be asked if it is empty.
      if ( ! "" . equals ( dbType ) )
      {
        dbtype = dbType ;
      }
      else
      {
        dbtype = readline ( newLineString + messageDatabaseType , appMaxLengthOfInput ) . toLowerCase ( ) ;
      }
// This shouuuld be a valid database type.
      if ( isValidDbType ( dbtype , true ) )
      {
// The delimiter is known at this point, get this.
        qdelim = getDelimiter ( dbtype ) ;
// Batch query type and empty delimiter are not good, the user gets a message if this is the situation.
        if ( isQueryTypeAndDelimiterOk ( queryType , qdelim ) )
        {
// Getting the database connection name in the same logic as the database type.
          if ( ! "" . equals ( dbConn ) )
          {
            connna = dbConn ;
          }
          else
          {
            connna = readline ( newLineString + messageConnectionName , appMaxLengthOfInput ) ;
          }
// The position of the connection is..
          connPos = getConnnaPos ( dbtype , connna ) ;
// This has to be a valid and found connection, else this procedure is broken.
          if ( connPos != - 1 )
          {
// Reading the title of this query.
            qtitle = readline ( newLineString + messageQueryTitle , appMaxLengthOfInput ) ;
// The user can enter a filename now to read the query from.
            qfromf = readline ( messageQueryFromConsoleOrFile , appMaxLengthOfInput ) . trim ( ) . toLowerCase ( ) ;
            if ( qfromf != null )
            {
// If it is empty then the user is foing to type its query onto the console.
              if ( qfromf . equals ( "" ) )
              {
// The displayable delimiter is being calculated.
                if ( "" . equals ( qdelim ) )
                {
                  delimToShow = messageEnter ;
                }
                else
                {
                  delimToShow = qdelim ;
                }
                if ( argMultiple . equals ( queryType ) )
                {
                  delimToShow = delimToShow + delimToShow ;
                }
// The one or multiline sql will be read
// in almost the similar logic as described and implemented
// in the factoryQuery method of Upper class.
                aReq = false ;
                qstrin = "" ;
                while ( true )
                {
                  if ( ! aReq )
                  {
                    aReq = true ;
                    if ( ! "" . equals ( qdelim ) )
                    {
                      qstrin += readline ( messageEnterQueryString + delimToShow + messageQueryStringEndNotEmpty , appMaxLengthOfSql ) ;
                    }
                    else
                    {
                      qstrin += readline ( messageEnterQueryString + delimToShow + messageQueryStringEndEmpty , appMaxLengthOfSql ) ;
                    }
                  }
                  else
                  {
                    qstrin += newLineString + readline ( "" , appMaxLengthOfSql ) ;
                  }
                  if ( ( ! "" . equals ( qdelim ) && ( ( ( argSingle . equals ( queryType ) || argBatch . equals ( queryType ) ) && qstrin . trim ( ) . endsWith ( qdelim ) ) || ( argMultiple . equals ( queryType ) && qstrin . trim ( ) . endsWith ( qdelim + newLineString + qdelim ) ) || ( argMultiple . equals ( queryType ) && qstrin . trim ( ) . endsWith ( qdelim + qdelim ) ) ) ) || ( "" . equals ( qdelim ) && ( argSingle . equals ( queryType ) || argBatch . equals ( queryType ) || ( argMultiple . equals ( queryType ) && qstrin . endsWith ( newLineString ) ) ) ) )
                  {
                    break ;
                  }
                }
// In this situation the string of the query to be executed
// is the trimmed string and the file to read the query from is null.
                qstrin = trimQueryString ( qstrin , qdelim ) ;
                qfile = null ;
              }
              else
              {
// If the qfromf is not empty then the file attribute
// will be set to this value and the query string will be null.
                qstrin = null ;
                qfile = qfromf . trim ( ) ;
              }
// The file has to be null or not null but valid.
              if ( qfile == null || ( qfile != null && isExistingFile ( qfile , true ) ) && isValidFilePath ( qfile , true ) )
              {
// Initialize these!
                qbatchc = 0 ;
                batchSourceFromFile = null ;
                batchSourceFromQueryId = - 1 ;
                sourceFields = null ;
// The batch queries requires additional informations to be created.
                if ( argBatch . equals ( queryType ) )
                {
// Reading the count of batch executed sqls. This will be minimum 1.
                  qbatchc = Integer . parseInt ( readline ( messageQueryBatchExecCount , appMaxLengthOfInput ) ) ;
                  if ( qbatchc < 1 )
                  {
                    qbatchc = 1 ;
                  }
// Reading the source type. (from file or from query id.)
                  batchSourceFrom = readline ( messageQueryBatchSourceFrom , appMaxLengthOfInput ) . trim ( ) . toLowerCase ( ) ;
// The user wanted to read the data from file.
                  if ( batchSourceFrom . equals ( batchSourceFromFileValue ) )
                  {
// Reading the filename of the source file.
                    batchSourceFromFile = readline ( messageQueryBatchSourceFromFile , appMaxLengthOfInput ) . trim ( ) ;
// This has to be existing and valid! (else the query won't be saved.)
                    if ( isExistingFile ( batchSourceFromFile , true ) && isValidFilePath ( batchSourceFromFile , true ) )
                    {
// Reading the source fields to use while executing the batch query.
// It will be validated only the query run time.
                      sourceFields = readline ( messageQueryBatchSourceFieldsFields . replace ( fieldsReplace , getSourceHeaderFromFile ( batchSourceFromFile ) ) , appMaxLengthOfInput ) . trim ( ) ;
// Let's save the new query!
                      queryId = saveQuery ( dbtype , connna , qtitle , qstrin , qfile , qdelim , queryType , no , qbatchc , batchSourceFromFile , batchSourceFromQueryId , sourceFields ) ;
                    }
                  }
// The user wanted to read the data from an other query (from its resultSet object).
                  else if ( batchSourceFrom . equals ( batchSourceFromResultSetValue ) )
                  {
// Reading the id of the source query (result set)
                    batchSourceFromQueryId = Integer . parseInt ( readline ( messageQueryBatchSourceFromQureryId , appMaxLengthOfInput ) . trim ( ) ) ;
// The query id has to be valid!
                    if ( isValidQueryId ( batchSourceFromQueryId ) )
                    {
// The result set of this source query has to be usable!
// ( scrollable or not scrollable but not used )
                      if ( isResultSetUsable ( getQueryResultSet ( batchSourceFromQueryId ) ) )
                      {
// Reading the source fields to use while executing the batch query.
// It will be validated only the query run time.
                        sourceFields = readline ( messageQueryBatchSourceFieldsFields . replace ( fieldsReplace , getSourceHeaderFromResultSet ( batchSourceFromQueryId , qdelim ) ) , appMaxLengthOfInput ) . trim ( ) ;
// Let's save the new query!
                        queryId = saveQuery ( dbtype , connna , qtitle , qstrin , qfile , qdelim , queryType , no , qbatchc , batchSourceFromFile , batchSourceFromQueryId , sourceFields ) ;
                      }
                      else
                      {
// Message to the user as it is not usable any more.
                        outprintln ( messageThisResultSetCannotBeUsedAsTheSource ) ;
                      }
                    }
                  }
                  else
                  {
// Exists since it is not valid.
                    outprintln ( messageWrongQueryBatchSourceFrom ) ;
                  }
                }
                else
                {
// Single query is required one more information:
                  if ( argSingle . equals ( queryType ) )
                  {
// the result set of this should be scrollable or not.
                    queryIsScrollable = readline ( messageQueryIsScrollable , appMaxLengthOfInput ) ;
// yes or no is the correct answer.
                    if ( y . equals ( queryIsScrollable ) || yes . equals ( queryIsScrollable ) || "" . equals ( queryIsScrollable ) )
                    {
                      if ( y . equals ( queryIsScrollable ) || "" . equals ( queryIsScrollable ) )
                      {
                        queryIsScrollable = yes ;
                      }
                    }
                    else
                    {
                      queryIsScrollable = no ;
                    }
// Save this query knowing the scrollable information.
                    queryId = saveQuery ( dbtype , connna , qtitle , qstrin , qfile , qdelim , queryType , queryIsScrollable , qbatchc , batchSourceFromFile , batchSourceFromQueryId , sourceFields ) ;
                  }
                  else
                  {
// This is a multiple sql, we can save it now.
                    queryId = saveQuery ( dbtype , connna , qtitle , qstrin , qfile , qdelim , queryType , no , qbatchc , batchSourceFromFile , batchSourceFromQueryId , sourceFields ) ;
                  }
                }
// If a query saving has happened..
                if ( queryId > - 1 )
                {
// The user can start this query immediately.
                  runnow = readline ( messageQueryRunNow , appMaxLengthOfInput ) ;
                  if ( runnow != null )
                  {
                    if ( runnow . toLowerCase ( ) . equals ( yes ) || runnow . toLowerCase ( ) . equals ( y ) || runnow . equals ( "" ) )
                    {
// Starting the query now if the user has wanted this.
                      startQuery ( queryId ) ;
                    }
                    else
                    {
// Otherwise, the message will go to the user: how to run this new query.
                      outprintln ( messageYouCanRunThisQueryByTyping + queryId ) ;
                    }
                  }
                  else
                  {
                    systemexit ( "Error - runnow is null, addQuery" ) ;
                  }
                }
              }
            }
            else
            {
              systemexit ( "Error - qfromf is null, addQuery" ) ;
            }
          }
          else
          {
            outprintln ( messageYourConnectionDoesNotExist ) ;
          }
        }
        else
        {
          outprintln ( messageDelimiterIsEmptyWhileAddingBatchSql ) ;
        }
      }
// These are not usable any more.
      dbtype = null ;
      connna = null ;
      qdelim = null ;
      qtitle = null ;
      qfromf = null ;
      qstrin = null ;
      qfile = null ;
      connPos = 0 ;
      delimToShow = null ;
      aReq = false ;
      qbatchc = 0 ;
      batchSourceFromFile = null ;
      batchSourceFromQueryId = 0 ;
      sourceFields = null ;
      batchSourceFrom = null ;
      queryIsScrollable = null ;
      queryId = 0 ;
      runnow = null ;
    }
  }
/*
** Has to be here but later will be overrided.
*/
  protected void displayResultImmediately ( int queryId ) { }
/*
** Getting the formatted elapsed time of a query.
*/
  protected final String getQueryElapsedFormatted ( int queryId )
  {
// Getting this using the us object.
    if ( us != null )
    {
      return us . calculateElapsed ( getQueryElapsedMs ( queryId ) ) ;
    }
    else
    {
      systemexit ( "Error - us is null, getQueryElapsedFormatted" ) ;
      return "" ;
    }
  }
/*
** Getting the elapsed time of a query in milliseconds.
*/
  protected final long getQueryElapsedMs ( int queryId )
  {
// This will be the final answer.
    long elapsedMs = 0 ;
// A valid query id is required.
    if ( isValidQueryId ( queryId ) )
    {
      String queryState = getQueryState ( queryId ) ;
      if ( queryStateNotStarted . equals ( queryState ) )
      {
// The elapsed time of a not started query is 0.
        elapsedMs = 0 ;
      }
      else if ( queryStateRunning . equals ( queryState ) )
      {
// The elapsed time of a running query is the time between now and the start timestamp.
        elapsedMs = new Date ( ) . getTime ( ) - getQueryStartDate ( queryId ) . getTime ( ) ;
      }
      else if ( queryStateFinishedSuccessfully . equals ( queryState ) || queryStateFinishedWithErrors . equals ( queryState ) )
      {
// The elapsed time of a finished query is the time between the start and the end date.
        elapsedMs = getQueryEndDate ( queryId ) . getTime ( ) - getQueryStartDate ( queryId ) . getTime ( ) ;
      }
// Not needed.
      queryState = null ;
    }
// Returning of this elapsed time.
    return elapsedMs ;
  }
/*
** Getting the state of the query.
** It it can be one of these four type:
** - not started           (start date is null)
** - running               (start date is not null but the end date is null)
** - finished successfully (start and end dates are not null but the error message is null)
** - finished with errors  (start date, end date and error message are not null)
** We don't care about the situation: the end date is not null but the start date is null.
** This is not gonna happen during the regular operation of this application.
*/
  protected final String getQueryState ( int queryId )
  {
// This will be the state.
    String queryState = "" ;
// Only valid query has a state.
    if ( isValidQueryId ( queryId ) )
    {
// Let's determine the state as mentioned in the comment of this function.
      if ( getQueryStartDate ( queryId ) == null )
      {
        queryState = queryStateNotStarted ;
      }
      else
      {
        if ( getQueryEndDate ( queryId ) == null )
        {
          queryState = queryStateRunning ;
        }
        else
        {
          if ( getQueryErrorMessage ( queryId ) == null )
          {
            queryState = queryStateFinishedSuccessfully ;
          }
          else
          {
            queryState = queryStateFinishedWithErrors ;
          }
        }
      }
    }
// Let's return this state.
    return queryState ;
  }
/*
** Getting the columns and its datatypes of the result set of a query.
*/
  protected final String [ ] getQueryResultSetColumns ( int queryId )
  {
// This will be the set of columns.
    String [ ] columns = new String [ 0 ] ;
// Getting the result set at first.
    ResultSet rs = getQueryResultSet ( queryId ) ;
    if ( rs != null )
    {
      if ( us != null )
      {
// Trying to create the set.
        try
        {
// We need a result set metadata.
          ResultSetMetaData rsmd = rs . getMetaData ( ) ;
// This is the count of the columns of the result set.
          int colscount = rsmd . getColumnCount ( ) ;
// Now we can create the array in a known size.
          columns = new String [ colscount ] ;
// This will be the maximum width of the columns,
// since these will be listed vertically.
          int maxWidthColname = 0 ;
// Let's create the list with the column names
// and determine the maximum column width.
          for ( int i = 1 ; i <= colscount ; i ++ )
          {
            columns [ i - 1 ] = rsmd . getColumnName ( i ) ;
            if ( maxWidthColname < rsmd . getColumnName ( i ) . length ( ) )
            {
              maxWidthColname = rsmd . getColumnName ( i ) . length ( ) ;
            }
          }
// This should be increased because at least a space char is needed to
// separate the column of the column names and the column of the datatypes.
          maxWidthColname += 1 ;
// Finally, let's pad the column names and append the datatypes.
          for ( int i = 1 ; i <= colscount ; i ++ )
          {
            columns [ i - 1 ] = us . pad ( columns [ i - 1 ] , maxWidthColname , spaceChar ) + rsmd . getColumnTypeName ( i ) ;
          }
// Won't use any more.
          rsmd = null ;
          colscount = 0 ;
          maxWidthColname = 0 ;
        }
        catch ( Exception e )
        {
// In case of any mistake, an empty set will be returned.
          columns = new String [ 0 ] ;
        }
      }
      else
      {
        systemexit ( "Error - us is null, getQueryResultSetColumns" ) ;
      }
    }
// Not usable.
    rs = null ;
// Let's return with the set of the columns.
    return columns ;
  }
/*
** Getting the count of rows of a result set.
*/
  protected final int getQueryResultSetRowCount ( int queryId )
  {
// This will be the coount.
    int rowCount = 0 ;
// This is necessary.
    ResultSet rs = getQueryResultSet ( queryId ) ;
    if ( rs != null && yes != null )
    {
      try
      {
// This is important, the scrollable property of a query.
        String scrollable = getQueryIsScrollable ( queryId ) ;
// We can do it if it is a scrollable statement and result set.
        if ( yes . equals ( scrollable ) )
        {
// Go onto the last element, get the row and go back before the first element.
          rs . last ( ) ;
          rowCount = rs . getRow ( ) ;
          rs . beforeFirst ( ) ;
        }
        else
        {
// Not scrollable, a zero is given.
          rowCount = 0 ;
        }
// Not needed any more.
        scrollable = null ;
      }
      catch ( Exception e )
      {
        rowCount = 0 ;
      }
    }
    else
    {
      rowCount = 0 ;
    }
// Not needed any more.
    rs = null ;
    return rowCount ;
  }
/*
** This is a valid query or not?
*/
  protected final boolean isValidQueryId ( int queryId )
  {
// This will be the final result.
    boolean success = false ;
// Success (existing) only if the queryDbTypes contains this key.
    if ( queryDbTypes != null )
    {
      if ( queryDbTypes . containsKey ( queryId ) )
      {
        success = true ;
      }
    }
// Message will go to the user if it is not a valid query.
    if ( ! success )
    {
      outprintln ( messageQueryIdDoesNotExist + queryId ) ;
    }
// Returning of the success.
    return success ;
  }
/*
  ** Deletes all of the queries.
  ** In execute command delete all and when the applicaiton exists.
  ** A final confirmation from the user is necessary or not.
*/
  protected final void deleteAllQueries ( boolean confirmNeeded )
  {
// The count of the deleted queries.
    int counter = 0 ;
// Can we continue?
    boolean toContinue = true ;
// Valid fileContent is needed.
    if ( isFileContentConnectionsOrigReady ( ) )
    {
// Let's print the dbType if it is not empty for the user to see what type of queries will be deleted.
      if ( ! "" . equals ( dbType ) && confirmNeeded )
      {
        outprint ( newLineString + fold + dbType + ":" ) ;
      }
// If the confirmation is required from the user then let's ask for it.
// Cannot continue if the user's answer is not yes.
      if ( confirmNeeded )
      {
        if ( ! readYesElseAnything ( messageDoYouWantToDeleteAllQueries , messageQueriesWontBeDeleted ) )
        {
          toContinue = false ;
        }
      }
// If we can contnue.
// (if the confirmation is not needed from the user tneh this toContinue remains on the same true value.)
      if ( toContinue )
      {
// This is the list of the queries to be deleted!
        ArrayList < Integer > queryIds = new ArrayList < Integer > ( ) ;
        if ( queryDbTypes != null )
        {
// Looping on the queries now.
          for ( HashMap . Entry < Integer , String > queryDbType : queryDbTypes . entrySet ( ) )
          {
            if ( queryDbType != null )
            {
              if ( queryDbType . getKey ( ) != null )
              {
                if ( queryDbType . getValue ( ) != null )
                {
// The running state has to be not running of the query to be deleted!
                  if ( ! queryStateRunning . equals ( getQueryState ( queryDbType . getKey ( ) ) ) )
                  {
// Empty dbType or matched database type of the query is required to add this query to the list.
                    if ( "" . equals ( dbType ) || queryDbType . getValue ( ) . equals ( dbType ) )
                    {
                      queryIds . add ( queryDbType . getKey ( ) ) ;
                    }
                  }
                }
                else
                {
                  systemexit ( "Error - queryDbTypeValue is null, deleteAllQueries" ) ;
                }
              }
              else
              {
                systemexit ( "Error - queryDbTypeKey is null, deleteAllQueries" ) ;
              }
            }
            else
            {
              systemexit ( "Error - queryDbType is null, deleteAllQueries" ) ;
            }
          }
// Now the list is ready for use, let's use it.
          for ( int queryId : queryIds )
          {
// Just for romatting.
            if ( counter == 0 )
            {
              outprintln ( "" ) ;
            }
// Plus one query will be deleted.
            counter ++ ;
// Now deleting the query.
            deleteQuery ( queryId , confirmNeeded ) ;
          }
// Final message will go to the user if confirmNeeded
          if ( confirmNeeded )
          {
            if ( counter == 0 )
            {
              outprintln ( messageNoNotRunningQueriesToDelete ) ;
            }
            else if ( counter == 1 )
            {
              outprintln ( messageOneNotRunningQueryHasBeenDeleted ) ;
            }
            else
            {
              outprintln ( messageNotRunningQueriesHaveBeenDeleted + counter ) ;
            }
          }
        }
        else
        {
          systemexit ( "Error - queryDbTypes is null, deleteAllQueries" ) ;
        }
// Not in use.
        queryIds = null ;
      }
    }
// Not in use.
    counter = 0 ;
    toContinue = true ;
  }
}
