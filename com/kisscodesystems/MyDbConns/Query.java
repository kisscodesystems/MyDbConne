/*
** This class is the part of the opensource MyDbConns application.
**
** See the header comment lines of Main class.
**
** Query
** This will implement the threads used for the separately added queries.
*/
package com . kisscodesystems . MyDbConns ;
import java . io . IOException ;
import java . io . ObjectInputStream ;
import java . io . ObjectOutputStream ;
import java . sql . Connection ;
import java . sql . DriverManager ;
import java . sql . PreparedStatement ;
import java . sql . ResultSet ;
import java . sql . ResultSetMetaData ;
import java . sql . Statement ;
import java . util . Date ;
public final class Query
implements Runnable
{
// A reference to the Queries object!
  Queries parentRef = null ;
// This is the string we collect the error imformations into.
  private String errorString = "" ;
/*
** This is the method which is used by the Queries object
** to set this parentRef to Queries itself.
*/
  protected final void setParentRef ( Queries ref )
  {
// Setting the reference (called from Queries: ..setParentRef(this);)
    parentRef = ref ;
  }
/*
** The thread of the executing of sql queries added by
** "query add single", "query add multiple" and "query add batch" commands.
** single: single sql (select), will have a result set object.
** multiple: multiple sql queries separated by the given delimiter,
**           has no result sets, just executes the given sql queries.
** batch: an sql executed several times using a dataset file or
**        a resultSet object of another single sql query.
** Reading all of the data belonging to this query from the hashmaps
** from the Qdata and working with them. The id of the query is known:
** the name of this thread is the queryId.
*/
  public final void run ( )
  {
    if ( parentRef != null )
    {
// These are also necessary to process the query execution.
      if ( parentRef . argSingle != null && parentRef . argMultiple != null && parentRef . argBatch != null && parentRef . yes != null && parentRef . newLineString != null )
      {
// This will be the id of this query to be executed.
        int queryId = Integer . parseInt ( Thread . currentThread ( ) . getName ( ) ) ;
// This has to be a valid query id!
        if ( parentRef . isValidQueryId ( queryId ) )
        {
// Getting the data of the query.
// The data for the connection.
          String dbtype = parentRef . getQueryDbType ( queryId ) ;
          String connna = parentRef . getQueryConnna ( queryId ) ;
          String dbuser = parentRef . getDbuser ( dbtype , connna ) ;
          String dbpass = parentRef . getDbpass ( dbtype , connna ) ;
          String driver = parentRef . getDriver ( dbtype , connna ) ;
          String connst = parentRef . getConnst ( dbtype , connna ) ;
// The data for the query executing.
          String queryString = parentRef . getQueryString ( queryId ) ;
          String queryFile = parentRef . getQueryFile ( queryId ) ;
          String queryDelimiter = parentRef . getQueryDelimiter ( queryId ) ;
          String queryType = parentRef . getQueryType ( queryId ) ;
          String queryIsScrollable = parentRef . getQueryIsScrollable ( queryId ) ;
          int queryBatchExecCount = parentRef . getQueryBatchExecCount ( queryId ) ;
          String queryBatchSourceFile = parentRef . getQueryBatchSourceFile ( queryId ) ;
          int queryBatchSourceQueryId = parentRef . getQueryBatchSourceQueryId ( queryId ) ;
          String queryBatchSourceField = parentRef . getQueryBatchSourceField ( queryId ) ;
// These will be the object of the connection and the query.
          Connection connection = null ;
          Statement statement = null ;
          PreparedStatement preparedStatement = null ;
          ResultSet resultSet = null ;
// Sql queries in case of multiple query execution.
          String [ ] queries = null ;
// Tricky solution for cancelled batch query.
          boolean queryIsCancelled = false ;
// The queryFile is null or not.
// Null if we haven't specified the added query as to read sql query from a file dynamically.
// Not null if we would like to read the query string from the given file.
          if ( queryFile != null )
          {
// Valid filepath and existing file is needed!
            if ( parentRef . isValidFilePath ( queryFile , true ) && parentRef . isExistingFile ( queryFile , true ) )
            {
// Let the queryString be the trimmed content of the given file!
              queryString = parentRef . readFileContent ( queryFile , false ) . trim ( ) ;
// Trim this query string. (Delimiter removing from the end of the string and trimming.)
              queryString = parentRef . trimQueryString ( queryString , queryDelimiter ) ;
// Let's give back this query string!
// The query string is null here if the file is read in the first time.
// And anyway, the given not null queryFile means that the user wants to
// execute query dynamically from the file so we have to update the query
// string now!
              parentRef . setQueryString ( queryId , queryString ) ;
            }
            else
            {
              systemexit ( "Error - invalid queryFile, run" ) ;
            }
          }
// This validation is necessary.
// It is unable to execute the query if the query delimiter is empty (newLine)
// and the type of this query is batch.
          if ( parentRef . isQueryTypeAndDelimiterOk ( queryType , queryDelimiter ) )
          {
            if ( queryString != null )
            {
// And a not empty query string.
              if ( queryString . length ( ) > 0 )
              {
// And a not too long query string.
                if ( queryString . length ( ) <= parentRef . appMaxLengthOfSql )
                {
// This is now, but it will be re-added if possible at the very beginning of the query execution start.
                  parentRef . setQueryStartDate ( queryId , new Date ( ) ) ;
                  try
                  {
// Trying to select the driver.
                    Class . forName ( driver ) ;
// And create the connection.
                    connection = DriverManager . getConnection ( connst , dbuser , dbpass ) ;
// If it is successful then it will be added into the hashmap.
                    parentRef . setQueryConnection ( queryId , connection ) ;
// The type of the queries are single, multiple or batch!
// Else the application exists.
                    if ( parentRef . argSingle . equals ( queryType ) )
                    {
// Single query.
// Let's create the statement depending on the is scrollable property.
                      if ( parentRef . yes . equals ( queryIsScrollable ) )
                      {
                        statement = connection . createStatement ( ResultSet . TYPE_SCROLL_INSENSITIVE , ResultSet . CONCUR_READ_ONLY ) ;
                      }
                      else
                      {
                        statement = connection . createStatement ( ) ;
                      }
// If we are here then the statement can be updated.
                      parentRef . setQueryStatement ( queryId , statement ) ;
// And the timestamp of query start.
                      parentRef . setQueryStartDate ( queryId , new Date ( ) ) ;
// Here is the query executing and its resultSet.
                      resultSet = statement . executeQuery ( queryString ) ;
// End of execution of single query.
// The resultSet object and the ending date will be updated into the hashmap in the finally block.
                    }
                    else if ( parentRef . argMultiple . equals ( queryType ) )
                    {
// Multiple query.
// Creating the statement object, it will be configured as not scrollable.
                      statement = connection . createStatement ( ) ;
// The statement is updatable now.
                      parentRef . setQueryStatement ( queryId , statement ) ;
// Remember: there are no resultSets in case of multiple sql execution.
                      resultSet = null ;
// Now, we have to know the sql queries each. Delimiter or new line character is the separator of sql queries.
                      queries = queryString . split ( ( "" . equals ( queryDelimiter ) ? parentRef . newLineString : queryDelimiter ) ) ;
// Starting the executions, updating the start timestamp.
                      parentRef . setQueryStartDate ( queryId , new Date ( ) ) ;
// Executing all of the sql queries now.
                      for ( int i = 0 ; i < queries . length ; i ++ )
                      {
// Each sql query will be executed in separate try-catch block
// since we would like to memorize all of the error strings one-by-one.
                        try
                        {
// Not empty queries are to be executed!
// Else the fact of empty sql wil be appended to the end of the error string.
                          if ( ! queries [ i ] . trim ( ) . equals ( "" ) )
                          {
                            statement . execute ( queries [ i ] . trim ( ) ) ;
                          }
                          else
                          {
                            appendErrorString ( new String ( "(" + i + ") " + " query is empty." ) ) ;
                          }
                        }
                        catch ( Exception e )
                        {
                          appendErrorString ( new String ( "(" + i + ") " + e . toString ( ) ) ) ;
                        }
                      }
// End of execution of multiple query.
// The (null) resultSet object and the ending date will be updated into the hashmap in the finally block.
                    }
                    else if ( parentRef . argBatch . equals ( queryType ) )
                    {
// Batch query.
// This uses not a Statement but a Prepared statement object. We can do this of course.
                      preparedStatement = connection . prepareStatement ( queryString ) ;
// The PreparedStatement is also a Statement so we can update onto this.
                      parentRef . setQueryStatement ( queryId , preparedStatement ) ;
// Remember: the batch execution of sqls will have no result set objects!
                      resultSet = null ;
// Update the starting timestamp now!
                      parentRef . setQueryStartDate ( queryId , new Date ( ) ) ;
// All of the given fields will be found in the source header
// (coming from result set of another query or from header of a csv file)
// Will be false in case of first not found field.
                      boolean fieldsOk = true ;
// For result set and also for csv file as source.
// This will contain all of the fields (columns) located in the source.
                      String [ ] sourceAllHeaderFieldsArr = null ;
// This is the array containing the fields we want to use in our executable query.
                      String [ ] sourceHeaderFieldsArr = queryBatchSourceField . trim ( ) . split ( queryDelimiter ) ;
// This next one will contain the positions in the sourceAllHeaderFieldsArr of the elements of the sourceHeaderFieldsArr
                      int [ ] sourceHeaderPositionsArr = new int [ sourceHeaderFieldsArr . length ] ;
// This is the content of the source file line-by-line.
                      String [ ] data = null ;
// The first line of the above is the source header of the source file.
                      String sourceAllHeaderFields = null ;
// This is the reference of the result set using as the source of the batch.
                      ResultSet sourceResultSet = null ;
// The source result set is scrollable or not.
                      String sourceResultSetIsScrollable = null ;
// This is the result set metadata of the source result set.
                      ResultSetMetaData resultSetMetaData = null ;
// This is the count of the columns in the source result set.
                      int colscount = 0 ;
// This will contain the data coming from the current line of the source file.
                      String [ ] currentData = null ;
// because of result set . next ( ) we have to count the executed sqls in case of resultSet source.
// (File source the variable of the loop is usable to count the executed sqls. -> batch execution!)
                      int queryCounter = 0 ;
// And this will be the index of the setObject in case of source result set.
                      int fieldIndex = 0 ;
// We can step further if the source file or source result set is OK.
                      if ( queryBatchSourceFile != null || queryBatchSourceQueryId > - 1 )
                      {
// In case of source file.
                        if ( queryBatchSourceFile != null )
                        {
// Valid and existing file is required! Else exiting!
                          if ( parentRef . isValidFilePath ( queryBatchSourceFile , true ) && parentRef . isExistingFile ( queryBatchSourceFile , true ) )
                          {
// The data is needed which will be used during the execution.
                            data = parentRef . readFileContent ( queryBatchSourceFile , false ) . split ( parentRef . newLineString ) ;
// The header of this is the first line.
                            sourceAllHeaderFields = data [ 0 ] . trim ( ) ;
// The array of the above is splitting it up by query delimiter.
                            sourceAllHeaderFieldsArr = sourceAllHeaderFields . split ( queryDelimiter ) ;
                          }
                          else
                          {
                            systemexit ( "Error - invalid queryBatchSourceFile, run" ) ;
                          }
                        }
// In case of source result set.
                        else
                        {
// This will be the source result set object.
                          sourceResultSet = parentRef . getQueryResultSet ( queryBatchSourceQueryId ) ;
// And its scrollable property.
                          sourceResultSetIsScrollable = parentRef . getQueryIsScrollable ( queryBatchSourceQueryId ) ;
                          if ( sourceResultSet != null & sourceResultSetIsScrollable != null )
                          {
// This is the metadata object which will give us the columns of source.
                            resultSetMetaData = sourceResultSet . getMetaData ( ) ;
                            if ( resultSetMetaData != null )
                            {
// Let's create the array of the source headers containing all of the fields.
                              colscount = resultSetMetaData . getColumnCount ( ) ;
                              sourceAllHeaderFieldsArr = new String [ colscount ] ;
// Let's fill it from metadata.
                              for ( int i = 1 ; i <= colscount ; i ++ )
                              {
                                sourceAllHeaderFieldsArr [ i - 1 ] = resultSetMetaData . getColumnName ( i ) ;
                              }
                            }
                            else
                            {
                              systemexit ( "Error - resultSetMetaData is null, run" ) ;
                            }
                          }
                          else
                          {
                            systemexit ( "Error - one of these is null: sourceResultSet|sourceResultSetIsScrollable, run" ) ;
                          }
                        }
// File and resultSet source: we are going to check the fields!
// Every columns contained by the sourceHeaderFieldsArr
// (given by the user during adding its query)
// have to be contained by the sourceAllHeaderFieldsArr
// (contained by the first line of source file or columns of source result set.)
// It is fine by default: true.
                        fieldsOk = true ;
// Looping thru the user given fields. Is its all items in the sourceAllHeaderFieldsArr?
                        for ( int i = 0 ; i < sourceHeaderFieldsArr . length ; i ++ )
                        {
// This is the current user added column and its position in the source header.
                          sourceHeaderPositionsArr [ i ] = getFieldPosition ( sourceAllHeaderFieldsArr , sourceHeaderFieldsArr [ i ] ) ;
// If this is -1 then it is not found and this field verification is not successful.
                          if ( sourceHeaderPositionsArr [ i ] == - 1 )
                          {
                            fieldsOk = false ;
                            appendErrorString ( "Field not found in source: " + sourceHeaderFieldsArr [ i ] ) ;
                          }
                        }
// Initially we don't want to break the batch executing.
                        queryIsCancelled = false ;
// Only if the fields are OK!
// If not, we won't do anything, the user already has got the message about it.
                        if ( fieldsOk )
                        {
// The magic will happen here.
// Processing the batch from file and from source is very similar
// but it is different in some details.
// In case of source file.
                          if ( queryBatchSourceFile != null )
                          {
// Looping thru the data.
// Remember: the first line (indexed by 0) is the header line so we are not gonna use it.
// Starting from 1.
                            for ( int i = 1 ; i < data . length ; i ++ )
                            {
// This has to be checked!
                              if ( queryIsCancelled )
                              {
                                break ;
                              }
// This is the current data line!
                              currentData = data [ i ] . split ( queryDelimiter ) ;
// Now looping thru the columns given by the user to prepare the statement.
                              for ( int j = 0 ; j < sourceHeaderFieldsArr . length ; j ++ )
                              {
// Adding the correct column by setString since this data coming from the file as a string.
                                try
                                {
                                  preparedStatement . setString ( j + 1 , currentData [ sourceHeaderPositionsArr [ j ] ] ) ;
                                }
                                catch ( Exception e )
                                {
                                  appendErrorString ( e . toString ( ) ) ;
                                }
                              }
// Now adding this into the batch.
                              try
                              {
                                preparedStatement . addBatch ( ) ;
                              }
                              catch ( Exception e )
                              {
                                appendErrorString ( e . toString ( ) ) ;
                              }
// If the number of executable batch count is reached..
                              if ( i % queryBatchExecCount == 0 )
                              {
// ..then execute it!
                                try
                                {
// Correction: if this query is cancelled then the start date is set to null.
                                  queryIsCancelled = null == parentRef . getQueryStartDate ( queryId ) ;
// So execute the next batch only if it is not cancelled.
                                  if ( ! queryIsCancelled )
                                  {
                                    preparedStatement . executeBatch ( ) ;
                                  }
                                }
                                catch ( Exception e )
                                {
                                  appendErrorString ( e . toString ( ) ) ;
                                }
                              }
                            }
// Executing the remaining prepared statements if there are any.
                            try
                            {
// Correction: if this query is cancelled then the start date is set to null.
                              queryIsCancelled = null == parentRef . getQueryStartDate ( queryId ) ;
// So execute the next batch only if it is not cancelled.
                              if ( ! queryIsCancelled )
                              {
                                preparedStatement . executeBatch ( ) ;
                              }
                            }
                            catch ( Exception e )
                            {
                              appendErrorString ( e . toString ( ) ) ;
                            }
                          }
// In case of source result set.
                          else
                          {
// Let's initialize these.
                            queryCounter = 1 ;
                            fieldIndex = 1 ;
// If it is a scrollable result set we are using it as the source,
                            if ( parentRef . yes . equals ( sourceResultSetIsScrollable ) )
                            {
// then let's step before the first line.
                              sourceResultSet . beforeFirst ( ) ;
                            }
// Looping thru the result set object now!
                            while ( sourceResultSet . next ( ) )
                            {
// This has to be checked!
                              if ( queryIsCancelled )
                              {
                                break ;
                              }
// This is now the first element to be added!
                              fieldIndex = 1 ;
// Looping thru the columns given by the user.
                              for ( String colname : sourceHeaderFieldsArr )
                              {
// Adding the correct column by setObject since this data coming from the result set in any format.
                                try
                                {
                                  preparedStatement . setObject ( fieldIndex , sourceResultSet . getObject ( colname ) ) ;
                                }
                                catch ( Exception e )
                                {
                                  appendErrorString ( e . toString ( ) ) ;
                                }
// The next index of the next field is now increased.
                                fieldIndex ++ ;
                              }
// Now adding this into the batch.
                              try
                              {
                                preparedStatement . addBatch ( ) ;
                              }
                              catch ( Exception e )
                              {
                                appendErrorString ( e . toString ( ) ) ;
                              }
// If the number of executable batch count is reached..
                              if ( queryCounter % queryBatchExecCount == 0 )
                              {
// ..then execute it!
                                try
                                {
// Correction: if this query is cancelled then the start date is set to null.
                                  queryIsCancelled = null == parentRef . getQueryStartDate ( queryId ) ;
// So execute the next batch only if it is not cancelled.
                                  if ( ! queryIsCancelled )
                                  {
                                    preparedStatement . executeBatch ( ) ;
                                  }
                                }
                                catch ( Exception e )
                                {
                                  appendErrorString ( e . toString ( ) ) ;
                                }
                              }
                              queryCounter ++ ;
                            }
// Executing the remaining prepared statements if there are any.
                            try
                            {
// Correction: if this query is cancelled then the start date is set to null.
                              queryIsCancelled = null == parentRef . getQueryStartDate ( queryId ) ;
// So execute the next batch only if it is not cancelled.
                              if ( ! queryIsCancelled )
                              {
                                preparedStatement . executeBatch ( ) ;
                              }
                            }
                            catch ( Exception e )
                            {
                              appendErrorString ( e . toString ( ) ) ;
                            }
                          }
                        }
                      }
                      else
                      {
                        systemexit ( "Error - queryBatchSourceFile is null and queryBatchSourceQueryId is negative, run" ) ;
                      }
// Releasable variables, references.
                      fieldsOk = false ;
                      sourceAllHeaderFieldsArr = null ;
                      sourceHeaderFieldsArr = null ;
                      sourceHeaderPositionsArr = null ;
                      data = null ;
                      sourceAllHeaderFields = null ;
                      sourceResultSet = null ;
                      sourceResultSetIsScrollable = null ;
                      resultSetMetaData = null ;
                      colscount = 0 ;
                      currentData = null ;
                      queryCounter = 0 ;
                      fieldIndex = 0 ;
// End of execution of batch query.
// The (null) resultSet object and the ending date will be updated into the hashmap in the finally block.
                    }
                    else
                    {
                      systemexit ( "Error - wrong queryType, run" ) ;
                    }
                  }
                  catch ( Exception e )
                  {
// If there are any Exception, the result set will be null and appending the current error.
                    resultSet = null ;
                    appendErrorString ( e . toString ( ) ) ;
                  }
                  finally
                  {
// Correction: if this query is cancelled then the start date is set to null.
                    queryIsCancelled = null == parentRef . getQueryStartDate ( queryId ) ;
// So execute the next batch only if it is not cancelled.
                    if ( ! queryIsCancelled )
                    {
// The query end date and the result set have to be updated on this query.
                      parentRef . setQueryEndDate ( queryId , new Date ( ) ) ;
                      parentRef . setQueryResultSet ( queryId , resultSet ) ;
                    }
                  }
                }
                else
                {
                  appendErrorString ( "Too long sql statement has been found!" ) ;
                }
              }
              else
              {
                appendErrorString ( "Empty sql statement has been found!" ) ;
              }
// Correction: if this query is cancelled then the start date is set to null.
              queryIsCancelled = null == parentRef . getQueryStartDate ( queryId ) ;
// So execute the next batch only if it is not cancelled.
              if ( ! queryIsCancelled )
              {
// Finally, the error string will be added into the correct hashmap in case of
// not empty error string, else the error object will be the null.
                if ( ! "" . equals ( errorString ) )
                {
                  parentRef . setQueryErrorMessage ( queryId , errorString . trim ( ) ) ;
                }
                else
                {
                  parentRef . setQueryErrorMessage ( queryId , null ) ;
                }
              }
            }
            else
            {
              systemexit ( "Error - queryString is null, run" ) ;
            }
          }
          else
          {
            systemexit ( "Error - batch sql and empty delimiter have been given, run" ) ;
          }
// Not used.
          dbtype = null ;
          connna = null ;
          dbuser = null ;
          dbpass = null ;
          driver = null ;
          connst = null ;
          queryString = null ;
          queryFile = null ;
          queryDelimiter = null ;
          queryType = null ;
          queryIsScrollable = null ;
          queryBatchExecCount = 0 ;
          queryBatchSourceFile = null ;
          queryBatchSourceQueryId = 0 ;
          queryBatchSourceField = null ;
          connection = null ;
          statement = null ;
          preparedStatement = null ;
          resultSet = null ;
          queries = null ;
          queryIsCancelled = false ;
        }
// Not used too.
        queryId = - 1 ;
      }
      else
      {
        systemexit ( "Error - one of these is null: parentRef.argSingle|parentRef.argMultiple|parentRef.argBatch|parentRef.yes|parentRef.newLineString, run" ) ;
      }
    }
    else
    {
      systemexit ( "Error - parentRef is null, run" ) ;
    }
  }
/*
** Trims and translates the string into lower case letters.
*/
  private final String stringTrimAndToLowerCase ( String string )
  {
// Return the trimmed and lowercased value or empty if the string is null.
    if ( string != null )
    {
      return string . trim ( ) . toLowerCase ( ) ;
    }
    else
    {
      return "" ;
    }
  }
/*
** Getting the position of the field. (Index in the array.)
*/
  private final int getFieldPosition ( String [ ] fields , String field )
  {
// This will be the position, the field is not found by default.
    int position = - 1 ;
    if ( fields != null && field != null )
    {
// This is the modified field.
      String trimmedLowerCasedField = stringTrimAndToLowerCase ( field ) ;
// Let's search for the position!
      for ( int i = 0 ; i < fields . length ; i ++ )
      {
        if ( trimmedLowerCasedField . equals ( stringTrimAndToLowerCase ( fields [ i ] ) ) )
        {
// This is the position and leave the for loop immediately.
          position = i ;
          break ;
        }
      }
// This is releasable.
      trimmedLowerCasedField = null ;
    }
// Returning with the position. (Will remain -1 if the field is not found in field [ ].)
    return position ;
  }
/*
** Appends the actual error message into the ending of error string.
*/
  private final void appendErrorString ( String error )
  {
    if ( parentRef != null )
    {
// Appending the error.
      errorString = errorString . trim ( ) + parentRef . newLineString + error . trim ( ) ;
      errorString = errorString . trim ( ) ;
    }
    else
    {
      systemexit ( "Error - parentRef is null, appendErrorString" ) ;
    }
// Trimming again because of the newly appended part.
  }
/*
** Exiting from the whole application in case of fatal error.
** Uses the parentRef similar named method if parentRef is not null.
*/
  private final void systemexit ( String s )
  {
    if ( parentRef != null )
    {
      parentRef . systemexit ( s ) ;
    }
    else
    {
      System . out . println ( s ) ;
      System . exit ( 1 ) ;
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