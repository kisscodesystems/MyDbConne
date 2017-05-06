/*
** This class is the part of the opensource MyDbConne application.
**
** See the header comment lines of Main class.
**
** Const
** This is the initial class containing every constant beeded by the app.
*/
package com . kisscodesystems . MyDbConne ;
import java . io . Console ;
import java . io . File ;
public class Const
{
/*
** Some misc byte or character based constants.
*/
  protected final byte nullByte = '\0' ;
  protected final char newLineChar = '\n' ;
  protected final char spaceChar = ' ' ;
  protected final char minusChar = "-" . charAt ( 0 ) ;
  protected final char plusChar = "+" . charAt ( 0 ) ;
  protected final char zeroChar = '\0' ;
  protected final String newLineString = "\n" ;
  protected final String backsla = String . valueOf ( Character . toChars ( 92 ) ) ;
  protected final String doubleQuote = "\"" ;
  protected final String doubleDot = ".." ;
  protected final String doubleSpace = "" + spaceChar + spaceChar ;
  protected final String singleSpace = "" + spaceChar ;
/*
** The header string of the connections file.
** The content of the connections file is the following:
** characters_of_connectionsHeader\n
** date_and_initialization_entry\n
** database_type0\n
** connection_name0\n
** database_username0\n
** user_password0\n
** database_driver0\n
** connection_string0\n
** database_type1\n
** connection_name1\n
** ..
*/
  protected final String connectionsHeader = "-c-o-n-n-e-c-t-i-o-n-s-" + newLineChar ;
/*
** The app.. constants are here which are the most importants.
** The application describe prints the informations being in these constants.
*/
  protected final String appName = "MyDbConne" ;
  protected final String appVersion = "1.2" ;
  protected final int appMaxLengthOfInput = 999 ;
  protected final int appMaxLengthOfSql = 1000000 ;
  protected final String appConnectionsDir = "cs" ;
  protected final String appConnectionsFileName = "cs" ;
  protected final String appCsPostfix = ".cs" ;
  protected final String appSlPostfix = ".sl" ;
  protected final String appIvPostfix = ".iv" ;
  protected final String appNwPostfix = ".nw" ;
  protected final boolean appConnectionPasswordCache = true ;
  protected final int appMaxNumOfConnections = 1000 ;
  protected final int appFileContentMaxLength = appMaxNumOfConnections * 6 * appMaxLengthOfInput + connectionsHeader . length ( ) ;
  protected final int appGoodPasswordMinCountOfUCLetters = 2 ;
  protected final int appGoodPasswordMinCountOfLCLetters = 2 ;
  protected final int appGoodPasswordMinCountOfDigits = 2 ;
  protected final int appGoodPasswordMinCountOfSpecChars = 1 ;
  protected final int appGoodPasswordMinLengthOfGoodPasswords = 7 ;
  protected final int appSaltLength = 64 ;
  protected final int appPbeKeySpecIterations = 65536 ;
  protected final int appPbeKeySpecKeyLength = 128 ;
  protected final String appSecretKeyFactoryInstance = "PBKDF2WithHmacSHA512" ;
  protected final String appSecretKeySpecAlgorythm = "AES" ;
  protected final String appCipherInstance = "AES/CBC/PKCS5Padding" ;
  protected final String appQueryFilePrefix = "file://" ;
  protected final String appDateFormatForDisplaying = "MM/dd/yyyy HH:mm:ss" ;
  protected final String appDateFormatForFilenames = "yyyy-MM-dd-HHmmss" ;
  protected final String appDateFormatForTimestamps = "MM/dd/yyyy HH:mm:ss.SSSSSS" ;
  protected final int appMaxNotReadInputsSeconds = 60 ;
  protected final int appMaxNumOfMillisecondsToWaitForTheResult = 1234 ;
  protected final int appMaxColLengthTxt = 150 ;
  protected final int appMaxQueryIdTitleConnnaWidth = 42 ;
/*
** The console and the utils objects!
** The console is used for data reading.
** The utils is used by its misc functions.
*/
  protected final Console console = System . console ( ) ;
  protected final Utils utils = new Utils ( ) ;
/*
** Foldings and separators.
*/
  protected final String fold = "" + spaceChar + spaceChar ;
  protected final String fold2 = "" + spaceChar + spaceChar + spaceChar + spaceChar + spaceChar ;
  protected final String sep1 = "," + spaceChar ;
  protected final String sep9 = "" + spaceChar + "|" + spaceChar ;
/*
** Yes and no.
*/
  protected final String yes = "yes" ;
  protected final String no = "no" ;
  protected final String y = "y" ;
  protected final String n = "n" ;
/*
** Other constants.
*/
  protected final String nullStr = "null" ;
  protected final String keyBeginEnd = "$$$$$" ;
  protected final String select = "select" ;
  protected final String SEP = File . separator ;
  protected final String fieldsReplace = "$$_fields_$$" ;
  protected final String dbConnToChangeInPrompt = "$dbConn$" ;
  protected final String utf8 = "UTF-8" ;
/*
** Datatypes.
*/
  protected final String xml = "xml" ;
  protected final String bytea = "bytea" ;
  protected final String clob = "clob" ;
  protected final String nclob = "nclob" ;
  protected final String dbclob = "dbclob" ;
  protected final String blob = "blob" ;
  protected final String binary = "binary" ;
  protected final String varbinary = "varbinary" ;
  protected final String longvarbinary = "longvarbinary" ;
  protected final String image = "image" ;
  protected final String bfile = "bfile" ;
  protected final String raw = "raw" ;
  protected final String longraw = "long raw" ;
  protected final String timestamp = "timestamp" ;
  protected final String timestamptz = "timestamp with time zone" ;
  protected final String timestampltz = "timestamp with local time zone" ;
// Length of the buffer while copying raw and bfile data
  protected final int bufflength = 4096 ;
/*
** Constants needed for the result echoing.
*/
  protected final String fieldsepTxt = " | " ;
  protected final String fieldsepHtm = "</td><td>" ;
  protected final String fileNameResult = appName + "-result-" ;
  protected final String filePostfixTxt = ".txt" ;
  protected final String filePostfixCsv = ".csv" ;
  protected final String filePostfixHtm = ".htm" ;
  protected final String resultTargetConsString = "console" ;
  protected final String resultTargetFileString = "file" ;
  protected final String resultTargetConsValue = "1" ;
  protected final String resultTargetFileValue = "2" ;
  protected final String resultFormatTxtString = "txt" ;
  protected final String resultFormatCsvString = "csv" ;
  protected final String resultFormatHtmString = "htm" ;
  protected final String resultFormatTxtValue = "1" ;
  protected final String resultFormatCsvValue = "2" ;
  protected final String resultFormatHtmValue = "3" ;
/*
** Constants used by the reading of properties of a batch sql.
*/
  protected final String batchSourceFromFileValue = "1" ;
  protected final String batchSourceFromResultSetValue = "2" ;
  protected final String batchSourceFromFileString = "from file" ;
  protected final String batchSourceFromResultSetString = "from query" ;
/*
** Onstants used by displaying the the good password.
*/
  protected final String lettersUCAZ = "[A-Z]" ;
  protected final String lettersLCAZ = "[a-z]" ;
  protected final String letters09 = "[0-9]" ;
  protected final String lettersSpecChars = "[.?!,;:-+_*@=<>]" ;
/*
** Types. For queries and connections.
*/
  protected final String typeToListAll = "all" ;
  protected final String typeToListActive = "active" ;
  protected final String typeToListInactive = "inactive" ;
/*
** These are the arguments usable in this application.
*/
  protected final String argApplication = "application" ;
  protected final String argDescribe = "describe" ;
  protected final String argStory = "story" ;
  protected final String argQuestionMark = "?" ;
  protected final String argHelp = "help" ;
  protected final String argWelcome = "welcome" ;
  protected final String argScreen = "screen" ;
  protected final String argGood = "good" ;
  protected final String argPassword = "password" ;
  protected final String argExit = "exit" ;
  protected final String argChange = "change" ;
  protected final String argDelimiter = "delimiter" ;
  protected final String argShow = "show" ;
  protected final String argConnection = "connection" ;
  protected final String argConnections = "connections" ;
  protected final String argAdd = "add" ;
  protected final String argList = "list" ;
  protected final String argActive = "active" ;
  protected final String argInactive = "inactive" ;
  protected final String argListall = "listall" ;
  protected final String argDelete = "delete" ;
  protected final String argDeleteall = "deleteall" ;
  protected final String argLoad = "load" ;
  protected final String argQuery = "query" ;
  protected final String argSingle = "single" ;
  protected final String argMultiple = "multiple" ;
  protected final String argBatch = "batch" ;
  protected final String argRun = "run" ;
  protected final String argCancel = "cancel" ;
  protected final String argCancelall = "cancelall" ;
  protected final String argResult = "result" ;
  protected final String argEcho = "echo" ;
  protected final String argUse = "use" ;
  protected final String argTest = "test" ;
  protected final String argFactory = "factory" ;
/*
** Types of the databases usable in thes app and the driver validators of these.
*/
  protected final String dbTypeOracle = "oracle" ;
  protected final String dbTypeMssql = "mssql" ;
  protected final String dbTypeDb2 = "db2" ;
  protected final String dbTypePostgresql = "postgresql" ;
  protected final String dbTypeDriverSearchOracle = "oracle" ;
  protected final String dbTypeDriverSearchMssql = "sqlserver" ;
  protected final String dbTypeDriverSearchDb2 = "db2" ;
  protected final String dbTypeDriverSearchPostgresql = "postgresql" ;
/*
** The available states of the added queries.
*/
  protected final String queryStates = "query states: " ;
  protected final String queryStateNotStarted = "not started" ;
  protected final String queryStateRunning = "running" ;
  protected final String queryStateFinishedSuccessfully = "finished successfully" ;
  protected final String queryStateFinishedWithErrors = "finished with errors" ;
/*
** Constants needed for the proper displaying of the application prompt.
*/
  protected final String promptEnding = ">" + spaceChar ;
  protected final String promptApp = appName + promptEnding ;
  protected final String promptOracle = appName + spaceChar + dbTypeOracle + dbConnToChangeInPrompt + promptEnding ;
  protected final String promptMssql = appName + spaceChar + dbTypeMssql + dbConnToChangeInPrompt + promptEnding ;
  protected final String promptDb2 = appName + spaceChar + dbTypeDb2 + dbConnToChangeInPrompt + promptEnding ;
  protected final String promptPostgresql = appName + spaceChar + dbTypePostgresql + dbConnToChangeInPrompt + promptEnding ;
/*
** The upper stepper and exiter constant.
*/
  protected final String promptToUpperOrExit = "<" ;
/*
** Messages used in the help and hints content.
*/
  protected final String messageYourConnectionName = "<your_connection_name>" ;
  protected final String messageYourFileName = "<your_file_name>" ;
  protected final String messageYourQueryId = "<your_query_id>" ;
  protected final String messageYourDatabaseType = "<your_database_type>" ;
  protected final String messageYourNewDelimiter = "<your_new_delimiter>" ;
  protected final String messageYourQuery = "\"<your_query>\"" ;
/*
** Constants replace values.
*/
  protected final String messageEmpty = "<empty>" ;
  protected final String messageHidden = "<hidden>" ;
  protected final String messageEnter = "<enter>" ;
/*
** Constants support the lists.
*/
  protected final String messageActiveConnections = " active connections: " ;
  protected final String messageInactiveConnections = " inactive connections: " ;
  protected final String messageAllConnections = " all connections: " ;
  protected final String messageActiveQueries = " active queries: " ;
  protected final String messageInactiveQueries = " inactive queries: " ;
  protected final String messageAllQueries = " all queries: " ;
  protected final String messageQueryElapseds = "elapsed times: " ;
  protected final String messageRowsSelected = " rows selected." ;
  protected final String messageRunningElapsed = "running, elapsed: " ;
  protected final String messageQueries = sep9 + "queries: " ;
  protected final String messageHits = " hit(s)." ;
/*
** General messages to the user.
*/
  protected final String messageDelimiterIsEmpty = "delimiter is empty!" ;
  protected final String messageConnectionHasBeenDeleted = " connection has been deleted." ;
  protected final String messageYouAreNowConnected = newLineString + fold + "You are now connected: " ;
  protected final String messageWelcomeToQueryFactory = newLineString + fold + "Welcome to query factory." + newLineChar + fold + "You can now type your sql queries one-by-one." + newLineChar + fold + "Type " + promptToUpperOrExit + " to have the application prompt!" + newLineChar + fold + "Other commands work in query factory:" + newLineChar + fold + fold2 + argDelimiter + spaceChar + argShow + newLineChar + fold + fold2 + argDelimiter + spaceChar + argChange + spaceChar + messageYourNewDelimiter + newLineChar + fold + fold2 + argConnection + spaceChar + argDescribe + newLineChar + fold + fold2 + appQueryFilePrefix + messageYourFileName + newLineChar + fold + fold2 + argQuestionMark + newLineChar + fold + fold2 + argHelp ;
  protected final String messageUnableToFreeQueryFactory = newLineString + fold + "Unable to free query factory: " ;
  protected final String messageSuccessfullyExitedFromQueryFactory = newLineString + fold + "Successfully exited from query factory." ;
  protected final String messageThePropertiesOfYourConnection = newLineString + fold + "The properties of your connection." ;
  protected final String messageThisConnectionIsNotInUseByAddedQuery = "This connection is not in use by added query." ;
  protected final String messageThisConnectionIsInUse = "This connection is in use: " ;
  protected final String messageDelimiterIsEmptyWhileAddingBatchSql = newLineString + fold + "Delimiter is empty while adding a batch sql!" ;
  protected final String messageYourConnectionDoesNotExist = newLineString + fold + "Your connection does not exist!" ;
  protected final String messageEnterThePropertiesOfTheConnection = newLineString + fold + "Please enter the properties of the connection!" + newLineChar ;
  protected final String messageTheDriverCannotBeEmpty = newLineString + fold + "The driver cannot be empty!" + newLineChar + fold + "Please type a non-empty driver (for example: 'oracle.jdbc.driver.OracleDriver')" ;
  protected final String messageTheConnectionStringCannotBeEmpty = newLineString + fold + "The connection string cannot be empty!" + newLineChar + fold + "Please type a non-empty connection string (for example: 'jdbc:oracle:thin:@localhost:1521:orcl')" ;
  protected final String messageSorryButThisResultSetCannotBeDispayed = newLineString + fold + "Sorry, but this resultSet cannot be displayed." + newLineChar + fold + "Re-run this query by typing: query run " ;
  protected final String messageDatabaseType = fold + fold2 + "Database type     : " ;
  protected final String messageConnectionName = fold + fold2 + "Connection name   : " ;
  protected final String messageDatabaseUser = fold + fold2 + "Database user     : " ;
  protected final String messageDatabasePassword = fold + fold2 + "Database password : " ;
  protected final String messageDatabaseDriver = fold + fold2 + "Connection driver : " ;
  protected final String messageConnectionString = fold + fold2 + "Connection string : " ;
  protected final String messageQueryTitle = fold + fold2 + "Query title       : " ;
  protected final String messageEnterQueryString = newLineString + "Enter your query:" + newLineChar + "(type \"" ;
  protected final String messageEnterResultTargets = newLineString + fold + "Enter the result targets (can be multiple)." + newLineChar + fold + fold2 + resultTargetConsValue + " (" + resultTargetConsString + ")" + newLineChar + fold + fold2 + resultTargetFileValue + " (" + resultTargetFileString + ")" + newLineChar + fold + fold2 + messageEmpty + " (" + resultTargetConsString + ")" + newLineChar + fold + ": " ;
  protected final String messageEnterResultFormats = fold + "Enter the result formats (can be multiple)." + newLineChar + fold + fold2 + resultFormatTxtValue + " (" + resultFormatTxtString + ")" + newLineChar + fold + fold2 + resultFormatCsvValue + " (" + resultFormatCsvString + ")" + newLineChar + fold + fold2 + resultFormatHtmValue + " (" + resultFormatHtmString + ")" + newLineChar + fold + fold2 + messageEmpty + " (" + resultFormatTxtString + ")" + newLineChar + fold + ": " ;
  protected final String messageCorrectResultTargetsAre = newLineString + fold + "Correct result targets are " + resultTargetConsValue + " and/or " + resultTargetFileValue + "!" ;
  protected final String messageCorrectResultFormatsAre = newLineString + fold + "Correct result formats are " + resultFormatTxtValue + " and/or " + resultFormatCsvValue + " and/or " + resultFormatHtmValue + "!" ;
  protected final String messageEnterHeaderToInclude = fold + "Header included? [yes]: " ;
  protected final String messageEnterQueryToInclude = fold + "Query included? [no]: " ;
  protected final String messageProducingTxtResult = fold + "Producing txt result" + doubleDot + spaceChar ;
  protected final String messageProducingCsvResult = fold + "Producing csv result" + doubleDot + spaceChar ;
  protected final String messageProducingHtmResult = fold + "Producing htm result" + doubleDot + spaceChar ;
  protected final String messageQueryStringEndNotEmpty = "\" and press " + messageEnter + " to save)" + newLineChar + promptEnding ;
  protected final String messageQueryStringEndEmpty = "\" to save)" + newLineChar + promptEnding ;
  protected final String messageQueryIsScrollable = newLineString + fold + fold2 + "Scrollable resultSet? [yes]: " ;
  protected final String messageQueryRunNow = newLineString + fold + fold2 + "Run it now? [" + yes + "]: " ;
  protected final String messageConnectionsGoodPassword = newLineString + fold + "THE GOOD CONNECTIONS PASSWORD IS:" + newLineChar + fold + "+------------------------------------------------------+" + newLineChar + fold + "| - ASCII 33-126 characters are acceptable, no spaces! |" + newLineChar + fold + "| - min. " + appGoodPasswordMinCountOfUCLetters + " uppercase letters                     " + lettersUCAZ + " |" + newLineChar + fold + "| - min. " + appGoodPasswordMinCountOfLCLetters + " lowercase letters                     " + lettersLCAZ + " |" + newLineChar + fold + "| - min. " + appGoodPasswordMinCountOfDigits + " digits                                " + letters09 + " |" + newLineChar + fold + "| - min. " + appGoodPasswordMinCountOfSpecChars + " special chars, for example " + lettersSpecChars + " |" + newLineChar + fold + "| - min. _" + appGoodPasswordMinLengthOfGoodPasswords + "_ and max. _" + appMaxLengthOfInput + "_ characters length password |" + newLineChar + fold + "+------------------------------------------------------+" ;
  protected final String messageLogApplicationInstanceInitialize = "Application instance was initialized." ;
  protected final String messageYouHaveReachedTheTopOfTheCountOfStorableConnections = newLineString + fold + "You have reached the top of the count of storable connections." ;
  protected final String messageConnectionsFileHasBeenCreated = newLineString + fold + "Your connections file has been created successfully." + newLineChar + fold ;
  protected final String messageSavedQueryId = newLineString + fold + "Your query has been added successfully with ID: " ;
  protected final String messageThisConnectionNameInDatabaseTypeAlreadyExists = newLineString + fold + "This connection name in database type already exists!" ;
  protected final String messageConnectionHasBeenTestedSuccessfully = newLineString + fold + "Connection has been tested successfully." ;
  protected final String messageUnableToConnect = newLineString + fold + "Unable to connect:" ;
  protected final String messageFailedToCloseResultSet = fold + "Unable to close result set " ;
  protected final String messageFailedToCloseConnection = fold + "Unable to close connection " ;
  protected final String messageFailedToCloseStatement = fold + "Unable to close statement " ;
  protected final String messageUnableToCloseTesterConnection = fold + "Unable to close tester connection!" ;
  protected final String messageDatabaseTypeHasNotBeenCorrect = newLineString + fold + "Database type has not been correct!" ;
  protected final String messageFilesAreAllowedFromNextToTheApplication = newLineString + fold + "Files are allowed from next to the application." ;
  protected final String messageFileDoesNotExist = newLineString + fold + "The file does not exist: " ;
  protected final String messageFileIsNotFile = newLineString + fold + "The file is not a file: " ;
  protected final String messageDoNotForgetYourConnectionsPassword = newLineString + fold + "The password of the application instance will be questioned." + newLineChar + fold + newLineChar + fold + "Please do not forget your connections password" + newLineChar + fold + "otherwise you won't be able to use your " + appName + " instance! " ;
  protected final String messageQueryIdDoesNotExist = newLineString + fold + "Query ID does not exist: " ;
  protected final String messageTypeYesElseAnything = "[type " + yes + " else anything]: " ;
  protected final String messageExiting = newLineString + fold + "The " + appName + " is exiting with error:" + newLineChar + fold ;
  protected final String messageErrorDeletingOldFilesOrRenameNewFiles = newLineString + fold + "Error has occurred while deleting old files or rename back to new files! " + newLineChar + fold + "Please fix it manually:" + newLineChar + fold + "The files has to be deleted (" + appCsPostfix + sep1 + appSlPostfix + " and " + appIvPostfix + ")" + newLineChar + fold + "and the " + appNwPostfix + " files have to be renamed back without " + appNwPostfix + " extension! " + newLineChar + fold + "The filename is: " ;
  protected final String messagePasswordVerificationError = newLineString + fold + "Sorry but the password and its verification are not the same." ;
  protected final String messageWelcomeScreen = newLineString + fold + "Welcome to " + appName + "!" + newLineChar + fold + newLineChar + fold + "Type " + dbTypeOracle + " or " + dbTypeMssql + " or " + dbTypeDb2 + " or " + dbTypePostgresql + " to use the specific database!" + newLineChar + fold + "Type " + promptToUpperOrExit + " to return to upper levels or exit!" + newLineChar + fold + "Type " + argQuestionMark + " or " + argHelp + " for more information about using this application!" ;
  protected final String messageErrorDeletingNewAnFile = fold + "Error while deleting newly created " + appCsPostfix + " file! " ;
  protected final String messageSureChangeConnectionsPassword = newLineString + fold + "Are you sure change the connections password?" + newLineChar + fold + messageTypeYesElseAnything ;
  protected final String messageDriverForOracleHasToContain = newLineString + fold + "The driver for Oracle has to contain: " ;
  protected final String messageDriverForMssqlHasToContain = newLineString + fold + "The driver for Mssql has to contain: " ;
  protected final String messageDriverForDb2HasToContain = newLineString + fold + "The driver for Db2 has to contain: " ;
  protected final String messageDriverForPostgresqlHasToContain = newLineString + fold + "The driver for Postgresql has to contain: " ;
  protected final String messageSaveConnection = newLineString + fold + "Save connection?" + newLineChar + fold + messageTypeYesElseAnything ;
  protected final String messageConnectionsPasswordHasBeenChanged = newLineString + fold + "The connections password has been changed successfully." ;
  protected final String messageConnectionsPasswordWontBeChanged = newLineString + fold + "The connections password is the same." ;
  protected final String messageRunningQueries = newLineString + fold + "The ID-s of your currently running queries are: " ;
  protected final String messageFilesAllowedBeingNextToTheApplication = newLineString + fold + "Files allowed being next to the application." ;
  protected final String messageNoRunningQueriesHaveBeenFoundToCancel = newLineString + fold + "No running queries have been found to cancel." ;
  protected final String messageOneRunningQueryHasBeenCancelled = newLineString + fold + "1 running query has been cancelled." ;
  protected final String messageRunningQueriesHaveBeenCancelled = newLineString + fold + "Running queries have been cancelled: " ;
  protected final String messageNoNotRunningQueriesToDelete = newLineString + fold + "No not running queries to delete." ;
  protected final String messageOneNotRunningQueryHasBeenDeleted = newLineString + fold + "1 not running query has been deleted." ;
  protected final String messageNotRunningQueriesHaveBeenDeleted = newLineString + fold + "Not running queries have been deleted: " ;
  protected final String messageAreYouSureWantToCancelAllActiveQueriesAndExit = newLineString + fold + "Are you sure want to cancel all active queries and exit?" + newLineChar + fold + messageTypeYesElseAnything ;
  protected final String messageConnectionHasNotBeenSaved = newLineString + fold + "Connection has not been saved." ;
  protected final String messageQueryIsCancelled = fold + "Query is cancelled." ;
  protected final String messageQueryIsDeleted = fold + "Query is deleted." ;
  protected final String messageQueryHasNotBeenFound = newLineString + fold + "Query has not been found." ;
  protected final String messageAllConnectionsHaveBeenHandeled = newLineString + fold + "All connections have been handeled." ;
  protected final String messageNoConnectionsHaveBeenHandeled = newLineString + fold + "No connections have been handeled." ;
  protected final String messageFileContentHasNotBeenFound = newLineString + fold + "The content of the file has not been found: " ;
  protected final String messageErrorDeletingNewSlFile = fold + "Error while deleting newly created " + appSlPostfix + " file! " ;
  protected final String messageContentIsNotDecrypted = newLineString + fold + "The content is not decrypted." ;
  protected final String messageYourQueryIsRunning = newLineString + fold + "Your query is running." ;
  protected final String messageYourQueryResultSetIsNotScrollable = newLineString + fold + "Your query resultSet is not scrollable." + newLineChar + fold + "Type this to echo the result: " + argResult + spaceChar + argEcho + spaceChar ;
  protected final String messageYourQueryHasToBeRun = newLineString + fold + "Your query has to be run." ;
  protected final String messageQueryHasNotBeenFinished = newLineString + fold + "Query has not been finished!" ;
  protected final String messageQueryWontBeCancelled = newLineString + fold + "Query won't be cancelled." ;
  protected final String messageQueriesWontBeCancelled = newLineString + fold + "Queries won't be cancelled." ;
  protected final String messageQueryWontBeDeleted = newLineString + fold + "Query won't be deleted." ;
  protected final String messageQueriesWontBeDeleted = newLineString + fold + "Queries won't be deleted." ;
  protected final String messageResultFileAlreadyExists = newLineString + fold + "Result file already exists: " ;
  protected final String messageQueryWontBeReRun = newLineString + fold + "Query won't be re-run." ;
  protected final String messageDoYouWantToCancelThisQuery = newLineString + fold + "Do you want to cancel this query?" + newLineChar + fold + messageTypeYesElseAnything ;
  protected final String messageDoYouWantToDeleteThisQuery = newLineString + fold + "Do you want to delete this query?" + newLineChar + fold + messageTypeYesElseAnything ;
  protected final String messageDoYouWantToCancelAllQueries = newLineString + fold + "Do you want to cancel all queries?" + newLineChar + fold + messageTypeYesElseAnything ;
  protected final String messageDoYouWantToDeleteAllQueries = newLineString + fold + "Do you want to delete all queries?" + newLineChar + fold + messageTypeYesElseAnything ;
  protected final String messageDoYouWantToCancelThisRunningQueryFirst = newLineString + fold + "Do you want to cancel this running query first?" + newLineChar + fold + messageTypeYesElseAnything ;
  protected final String messageSureReRunQueryAndDropExistingResult = newLineString + fold + "Sure re-run query and drop existing result?" + newLineChar + fold + messageTypeYesElseAnything ;
  protected final String messageYouCanRunThisQueryByTyping = newLineString + fold + "You can run this query by typing: " + argQuery + spaceChar + argRun + spaceChar ;
  protected final String messageConnectionHasBeenSavedSuccessfully = newLineString + fold + "The connection has been saved successfully." ;
  protected final String messageErrorDeletingNewIvFile = fold + "Error while deleting newly created " + appIvPostfix + " file! " ;
  protected final String messageMissingCsOrSlOrIvFile = newLineString + fold + "Sorry but you have no original " + appCsPostfix + " file or " + appSlPostfix + " file or " + appIvPostfix + " file for " ;
  protected final String messageEnterPasswordVerify = fold + "Please verify it: " ;
  protected final String messageWrongParameters = newLineString + fold + "You have used wrong parameters!" ;
  protected final String messageThePasswordIsNotValid = "The password is not valid." ;
  protected final String messageConnectionsGoodPasswordIsNotValid = newLineString + fold + messageThePasswordIsNotValid + newLineChar + messageConnectionsGoodPassword ;
  protected final String messageIncorrectFilePassword = newLineString + fold + "The password you have entered is incorrect." ;
  protected final String messageEnterPasswordForConnections = newLineString + fold + "Enter your connections password: " ;
  protected final String messageYouHaveToUseAConnectionOrGiveTheConnectionNameToChange = newLineString + fold + "You have to use a connection or give the connection name to change!" ;
  protected final String messageMissingNewCsOrSlOrIvFile = newLineString + fold + "Sorry but one or more new file is missing after the saving operation" + newLineChar + fold + "( " + appCsPostfix + " file or " + appSlPostfix + " file or " + appIvPostfix + " ), your changes will be rolled back! " ;
  protected final String messageFileHasBeenSaved = newLineString + fold + "File has been saved: " ;
  protected final String messageTheNameOfTheConnectionHasToBeAtLeastOneChar = newLineString + fold + "The name of the connection has to be at least one char!" ;
  protected final String messageTheNameOfTheConnectionCannotContainSpaceChar = newLineString + fold + "The name of the connection cannot contain space char!" ;
  protected final String messageIsFolderSafe = newLineString + fold + "As the first step, please move the classes of this " + appName + newLineChar + fold + "into a safe (local personal) folder or into a removable device!" + newLineChar + fold + newLineChar + fold + "Is the folder of this " + appName + ".class safe enough?" + newLineChar + fold + messageTypeYesElseAnything ;
  protected final String messageAreYouSureDeleteConnection = newLineString + fold + "Are you sure delete connection?" + newLineChar + fold + messageTypeYesElseAnything ;
  protected final String messageAreYouSureDeleteConnectionsByDbType = newLineString + fold + "Are you sure delete connections by the database type?" + newLineChar + fold + messageTypeYesElseAnything ;
  protected final String messageYourConnectionWontBeDeleted = newLineString + fold + "OK, your connection won't be deleted." ;
  protected final String messageYourConnectionsWontBeDeleted = newLineString + fold + "OK, your connections won't be deleted." ;
  protected final String messageYourConnectionHasBeenDeletedSuccessfully = newLineString + fold + "Your connection has been deleted successfully." ;
  protected final String messageYourConnectionsHaveBeenDeletedSuccessfully = newLineString + fold + "Your connections have been deleted successfully." ;
  protected final String messageQueryCannotBeCancelled = newLineString + fold + "Query cannot be cancelled : " ;
  protected final String messageQueryCannotBeDeleted = newLineString + fold + "Query cannot be deleted : " ;
  protected final String messageJoiningQueryThread = fold + "Joining query thread: " ;
  protected final String messageDelimiterHasBeenChangedTo = " delimiter has been changed to " ;
  protected final String messageChangeConnUserOrPasswordBehaviour = "empty, " + messageEmpty + ", a new value" ;
  protected final String messageChangeConnPropertyBehaviour = newLineString + fold + "For changing the username or password of this connection" + newLineChar + fold + "- leave empty    (just hit the enter) : remains the same" + newLineChar + fold + "- type '" + messageEmpty + "'     (without quotes) : will be an empty string: ''" + newLineChar + fold + "- type 'a new value' (without quotes) : your new non-empty value will be used" + newLineChar + fold + "For changing any other property of this connection" + newLineChar + fold + "leave empty the property for further use of value between [] or type a new value" + newLineChar ;
  protected final String messageYourFileHasNotBeenFoundOrNotBeenFile = newLineString + fold + "Your file has not been found or not been file!" ;
  protected final String messageIsFileGoodFormatted = newLineString + fold + "The file you would like to use has to be at this format!" + newLineChar + fold + "Line by line contains the attributes of the connections." + newLineChar + newLineChar + fold + "line 1: database_type     [oracle, mssql, db2, postgresql]" + newLineChar + fold + "line 2: connection_name   (a name without space, at least 1 char)" + newLineChar + fold + "line 3: database_user     (username, space is allowed ('sys as sysdba'), can be empty)" + newLineChar + fold + "line 4: database_password (password, empty line means empty password)" + newLineChar + fold + "line 5: database_driver   (a driver, for example org.postgresql.Driver, at least 1 char)" + newLineChar + fold + "line 6: connection_string (for example jdbc:postgresql://localhost:5432/postgres, at least 1 char)" + newLineChar + fold + "line 7: database_type2" + newLineChar + fold + "line 8: connection_name2, etc." + newLineChar + newLineChar + fold + "Whitespace chars or empty lines should not be placed at the end or at the begin of the file!" + newLineChar + newLineChar + fold + "Is your file formatted like that?" + newLineChar + fold + messageTypeYesElseAnything ;
  protected final String messageFileWontBeUsed = newLineString + fold + "File won't be used." ;
  protected final String messageQueryDescDbType = newLineString + fold + "Database type       : " ;
  protected final String messageQueryDescConnna = fold + "Connection name     : " ;
  protected final String messageQueryDescTitle = fold + "Title of query      : " ;
  protected final String messageQueryDescString = newLineString + "--Query string: " ;
  protected final String messageQueryDescDelimiter = newLineString + fold + "Query delimiter     : " ;
  protected final String messageQueryDescType = fold + "Type of query       : " ;
  protected final String messageQueryDescIsScrollable = fold + "Query is scrollable : " ;
  protected final String messageQueryDescBatchExecCount = newLineString + fold + "Batch exec count    : " ;
  protected final String messageQueryDescBatchSourceFile = fold + "Batch surce file    : " ;
  protected final String messageQueryDescBatchSourceQueryId = fold + "Batch surce queryId : " ;
  protected final String messageQueryDescBatchSourceFields = fold + "Batch surce fields  : " ;
  protected final String messageQueryDescStartDate = newLineString + fold + "Start date          : " ;
  protected final String messageQueryDescEndDate = fold + "End date            : " ;
  protected final String messageQueryDescErrorMessage = fold + "Error message(s)    : " ;
  protected final String messageQueryDescState = newLineString + fold + "Query state         : " ;
  protected final String messageQueryDescHasResultSet = newLineString + fold + "This query has a result set." ;
  protected final String messageQueryDescNoResultSet = newLineString + fold + "This query has no result set." ;
  protected final String messageQueryDescColumns = fold + "Columns: " ;
  protected final String messageQueryDescRowCount = fold + "Row count: " ;
  protected final String messageQueryDescRowCountIsNotDisplayableBecauseOfNotScrollableResultSet = fold + "Row count is not displayable because of not scrollable resultSet." ;
  protected final String messageQueryDescTotalElapsedTime = fold + "Total elapsed time  : " ;
  protected final String messageQueryDescThreadState = newLineString + fold + "Exec thread state   : " ;
  protected final String messageQueryDescFile = newLineString + fold + "Query from file     : " ;
  protected final String messageQueryFromConsoleOrFile = newLineString + fold + "Leave blank for typing sql into the console" + newLineChar + fold + "or type your filename containing your sql statement(s)" + newLineChar + fold + ": " ;
  protected final String messageQueryBatchExecCount = newLineString + fold + "The count of the executed sqls at a time is" + newLineChar + fold + ": " ;
  protected final String messageQueryBatchSourceFrom = newLineString + fold + "The source of your sql batch containing the data separated by your query delimiter is" + newLineChar + fold + fold2 + batchSourceFromFileValue + ": " + batchSourceFromFileString + newLineChar + fold + fold2 + batchSourceFromResultSetValue + ": " + batchSourceFromResultSetString + newLineChar + fold + ": " ;
  protected final String messageQueryBatchSourceFromFile = newLineString + fold + "Enter the filename" + newLineChar + fold + ": " ;
  protected final String messageQueryBatchSourceFromQureryId = newLineString + fold + "Enter the query ID" + newLineChar + fold + ": " ;
  protected final String messageQueryBatchSourceFieldsFields = newLineString + fold + "Fields you are going to use (separated by the delimiter) are" + newLineChar + fold + "[available: " + fieldsReplace + "]" + newLineChar + fold + ": " ;
  protected final String messageWrongQueryBatchSourceFrom = newLineString + fold + "The query batch source can be " + batchSourceFromFileString + " or " + batchSourceFromResultSetString + "!" ;
  protected final String messageCountOfLinesOfFileIsNotTheExpected = newLineString + fold + "Count of lines of file is not the expected." ;
  protected final String messageYourQueryHasBeenExecutedSuccessfully = newLineString + fold + "Your query has been executed successfully." ;
  protected final String messageThisResultSetCannotBeUsedAsTheSource = newLineString + fold + "This result set cannot be used as the source." ;
  protected final String messageAppendThisFileIntoTheEndOfCurrentConnections = newLineString + fold + "Append this file into the end of current connections?" + newLineChar + fold + messageTypeYesElseAnything ;
  protected final String messageConnectionsWillNotBeImported = newLineString + fold + "Connections will not be imported." ;
  protected final String messageLoadingConnection = fold + "Loading connection " ;
  protected final String messageConnectionLoadingFailedWrongDatabaseType = "wrong database type!" ;
  protected final String messageConnectionLoadingFailedSpaceInConnectionName = "space in connection name!" ;
  protected final String messageConnectionLoadingFailedConnectionNameCannotBeEmpty = "connection name cannot be empty!" ;
  protected final String messageConnectionLoadingFailedExistingConnection = "this connection has already been set!" ;
  protected final String messageConnectionLoadingFailedDriverCannotBeEmpty = "driver cannot be empty!" ;
  protected final String messageConnectionLoadingFailedConnectionStringCannotBeEmpty = "connection string cannot be empty!" ;
  protected final String messageDone = "done." ;
  protected final String messageConnectionsHaveBeenLoaded = " connections have been loaded." ;
  protected final String messageConnectionHaveBeenLoaded = newLineString + fold + "One connection has been loaded." ;
  protected final String messageAllOfTheConnectionsHaveBeenLoaded = newLineString + fold + "All of the connections have been loaded: " ;
  protected final String messageNoConnectionsHaveBeenLoaded = newLineString + fold + "No connections have been loaded." ;
/*
** Application messages. (Describe and story.)
*/
  protected final String messageApplicationDescribe = newLineString + fold + appName + " information." + newLineChar + fold + newLineChar + fold + "Current version: " + appVersion + newLineChar + fold + newLineChar + fold + "Input information." + newLineChar + fold + fold2 + "Maximum length of any input                     : " + appMaxLengthOfInput + newLineChar + fold + fold2 + "Maximum length of an sql to be executed         : " + appMaxLengthOfSql + newLineChar + fold + newLineChar + fold + "Connections file content specific information." + newLineChar + fold + fold2 + "Directory name of the connections file          : " + appConnectionsDir + newLineChar + fold + fold2 + "File name of the connections file               : " + appConnectionsFileName + newLineChar + fold + fold2 + "Postfix of connection file                      : " + appCsPostfix + newLineChar + fold + fold2 + "Postfix of salt file                            : " + appSlPostfix + newLineChar + fold + fold2 + "Postfix of initialization vector file           : " + appIvPostfix + newLineChar + fold + fold2 + "Postfix of newly created files of above         : " + appNwPostfix + newLineChar + fold + fold2 + "The caching of the connections password         : " + appConnectionPasswordCache + newLineChar + fold + fold2 + "Maximum storable connections                    : " + appMaxNumOfConnections + newLineChar + fold + fold2 + "Max length of the content of cs file (bytes)    : " + appFileContentMaxLength + newLineChar + fold + newLineChar + fold + "Connections file password specific information." + newLineChar + fold + fold2 + "Minimum count of uppercase letters              : " + appGoodPasswordMinCountOfUCLetters + newLineChar + fold + fold2 + "Minimum count of lowercase letters              : " + appGoodPasswordMinCountOfLCLetters + newLineChar + fold + fold2 + "Minimum count of digits                         : " + appGoodPasswordMinCountOfDigits + newLineChar + fold + fold2 + "Minimum count of special chars                  : " + appGoodPasswordMinCountOfSpecChars + newLineChar + fold + fold2 + "Minimum length of password                      : " + appGoodPasswordMinLengthOfGoodPasswords + newLineChar + fold + newLineChar + fold + "Connections encrypt/decrypt information." + newLineChar + fold + fold2 + "Salt length                                     : " + appSaltLength + newLineChar + fold + fold2 + "Pbe key spec iterations                         : " + appPbeKeySpecIterations + newLineChar + fold + fold2 + "Pbe key spec key length                         : " + appPbeKeySpecKeyLength + newLineChar + fold + fold2 + "Secret key factory instance                     : " + appSecretKeyFactoryInstance + newLineChar + fold + fold2 + "Secret key spec algorithm                       : " + appSecretKeySpecAlgorythm + newLineChar + fold + fold2 + "Cipher instance                                 : " + appCipherInstance + newLineChar + fold + newLineChar + fold + "Other information." + newLineChar + fold + fold2 + "Query file prefix                               : " + appQueryFilePrefix + newLineChar + fold + fold2 + "Date format for displaying                      : " + appDateFormatForDisplaying + newLineChar + fold + fold2 + "Date format for filenames                       : " + appDateFormatForFilenames + newLineChar + fold + fold2 + "Date format for timestamps                      : " + appDateFormatForTimestamps + newLineChar + fold + fold2 + "Max seconds to enter any input                  : " + appMaxNotReadInputsSeconds + newLineChar + fold + fold2 + "Max time (ms) to wait for the immediate result  : " + appMaxNumOfMillisecondsToWaitForTheResult + newLineChar + fold + fold2 + "Max length of the column in txt results         : " + appMaxColLengthTxt + newLineChar + fold + fold2 + "Max length of the id + title                    : " + appMaxQueryIdTitleConnnaWidth ;
  protected final String messageApplicationStory = newLineString + fold + "The application story." + newLineChar + fold + newLineChar + "The situation." + newLineChar + fold + newLineChar + fold + "The most important goal was to construct a lightweight sql client that" + newLineChar + fold + "supports our daily developing of prdare. We thought that it would be nice to" + newLineChar + fold + "connect all of the databases supported by the prdare in the same application." + newLineChar + fold + "This was the reason of the birth of this " + appName + " command line application." + newLineChar + fold + "Its modified version is published." + newLineChar + fold + newLineChar + "The most important features of " + appName + "." + newLineChar + fold + newLineChar + fold + "- connection management, in encrypted files" + newLineChar + fold + "  adds, edits and deletes connections" + newLineChar + fold + "  loads connections from file" + newLineChar + fold + "  the possibility to cache the connections password" + newLineChar + fold + "  (compile time property, this app has to be recompiled to change this)" + newLineChar + fold + "- Oracle, Mssql, Db2 and Postgresql databases are supported" + newLineChar + fold + "- multiple queries are available to run" + newLineChar + fold + "  in multiple database types at the same time" + newLineChar + fold + "- sql-s can come from the console or from file" + newLineChar + fold + "  supporting multi line sql commands, sql command delimiter by database types" + newLineChar + fold + "- added sql queries will be run in separate threads" + newLineChar + fold + "  these can be watched and the results can be seen" + newLineChar + fold + "  user can work while several sql-s are running on the separate threads" + newLineChar + fold + "- sql queries can be cancelled if your driver also supports this" + newLineChar + fold + "- query factory mode: the user can type its sql queries or commands" + newLineChar + fold + "  continuously and see the results of these" + newLineChar + fold + "  (user has to wait these queries to be finished, not in separate threads)" + newLineChar + fold + "- sql results can go onto the console or into txt, csv and/or htm files" + newLineChar + fold + "- single, multiple or batch type of queries can be added" + newLineChar + fold + "  single: single sql query, selects mostly. it has a result set object" + newLineChar + fold + "  multiple: executes multiple sql commands and it has no result set" + newLineChar + fold + "  batch: one single sql query using a file or result set as datasource" + newLineChar + fold + newLineChar + "When to use this application?" + newLineChar + fold + newLineChar + fold + "- if the user likes the command line apps" + newLineChar + fold + "- or the user can work in the command line environment like a unix server." + newLineChar + fold + newLineChar + "Warning!" + newLineChar + fold + newLineChar + fold + "- The passwords of the connections are stored as strings in many points" + newLineChar + fold + "  in the memory area of jvm as it uses" + newLineChar + fold + "  DriverManager.getConnection(connection_string,database_user,password)." + newLineChar + fold + "  This getConnection method allows only String typed password." + newLineChar + fold + "- Use ssl connection when possible to avoid password leak on the network!" + newLineChar + fold + "  (Your connection passwords are safe in your encrypted connections file.)" ;
/*
** Help messages. (Hints and help.)
*/
  protected final String messageHints = newLineString + fold + argApplication + spaceChar + argDescribe + newLineChar + fold + argApplication + spaceChar + argStory + newLineChar + fold + argWelcome + spaceChar + argScreen + newLineChar + fold + messageYourDatabaseType + newLineChar + fold + promptToUpperOrExit + newLineChar + fold + argConnection + spaceChar + argList + spaceChar + argActive + newLineChar + fold + argConnection + spaceChar + argList + spaceChar + argInactive + newLineChar + fold + argConnection + spaceChar + argListall + newLineChar + fold + argConnection + spaceChar + argAdd + newLineChar + fold + argConnection + spaceChar + argLoad + spaceChar + messageYourFileName + newLineChar + fold + argConnection + spaceChar + argDescribe + spaceChar + messageYourConnectionName + newLineChar + fold + argConnection + spaceChar + argDescribe + newLineChar + fold + argConnection + spaceChar + argChange + spaceChar + messageYourConnectionName + newLineChar + fold + argConnection + spaceChar + argChange + newLineChar + fold + argConnection + spaceChar + argDelete + spaceChar + messageYourConnectionName + newLineChar + fold + argConnection + spaceChar + argDelete + newLineChar + fold + argConnection + spaceChar + argDeleteall + newLineChar + fold + argConnection + spaceChar + argUse + spaceChar + messageYourConnectionName + newLineChar + fold + argConnection + spaceChar + argUse + spaceChar + messageYourDatabaseType + spaceChar + messageYourConnectionName + newLineChar + fold + argConnection + spaceChar + argTest + spaceChar + messageYourConnectionName + newLineChar + fold + argConnection + spaceChar + argTest + newLineChar + fold + argQuery + spaceChar + argList + spaceChar + argActive + newLineChar + fold + argQuery + spaceChar + argList + spaceChar + argInactive + newLineChar + fold + argQuery + spaceChar + argListall + newLineChar + fold + argQuery + spaceChar + argAdd + spaceChar + argSingle + newLineChar + fold + argQuery + spaceChar + argAdd + spaceChar + argMultiple + newLineChar + fold + argQuery + spaceChar + argAdd + spaceChar + argBatch + newLineChar + fold + argQuery + spaceChar + argFactory + spaceChar + messageYourQuery + newLineChar + fold + argQuery + spaceChar + argFactory + spaceChar + messageYourDatabaseType + spaceChar + messageYourConnectionName + spaceChar + messageYourQuery + newLineChar + fold + argQuery + spaceChar + argDescribe + spaceChar + messageYourQueryId + newLineChar + fold + argQuery + spaceChar + argRun + spaceChar + messageYourQueryId + newLineChar + fold + argQuery + spaceChar + argCancel + spaceChar + messageYourQueryId + newLineChar + fold + argQuery + spaceChar + argCancelall + newLineChar + fold + argQuery + spaceChar + argDelete + spaceChar + messageYourQueryId + newLineChar + fold + argQuery + spaceChar + argDeleteall + newLineChar + fold + argResult + spaceChar + argEcho + spaceChar + messageYourQueryId + newLineChar + fold + argDelimiter + spaceChar + argShow + newLineChar + fold + argDelimiter + spaceChar + argChange + spaceChar + messageYourNewDelimiter + newLineChar + fold + argConnections + spaceChar + argGood + spaceChar + argPassword + newLineChar + fold + argConnections + spaceChar + argPassword + spaceChar + argChange + newLineChar + fold + argQuestionMark + newLineChar + fold + argHelp ;
  protected final String messageHelp = newLineString + fold + "Please type these arguments to use " + appName + " correctly:" + newLineChar + fold + newLineChar + fold + argApplication + spaceChar + argDescribe + newLineChar + fold + fold2 + "Prints the information of this application for you." + newLineChar + fold + newLineChar + fold + argApplication + spaceChar + argStory + newLineChar + fold + fold2 + "Prints the basic concept and the basic usage, please read it carefully." + newLineChar + fold + newLineChar + fold + argWelcome + spaceChar + argScreen + newLineChar + fold + fold2 + "Prints the first run screen." + newLineChar + fold + newLineChar + fold + messageYourDatabaseType + newLineChar + fold + fold2 + dbTypeOracle + " or " + dbTypeMssql + " or " + dbTypeDb2 + " or " + dbTypePostgresql + newLineChar + fold + fold2 + "These databases can be used. Changes the application prompt." + newLineChar + fold + newLineChar + fold + promptToUpperOrExit + newLineChar + fold + fold2 + "Steps upper levels." + newLineChar + fold + fold2 + "If the application level is set then you can exit." + newLineChar + fold + fold2 + "If a database level is set then steps to application." + newLineChar + fold + fold2 + "If a connection level is set then steps to database." + newLineChar + fold + newLineChar + fold + argConnection + spaceChar + argList + spaceChar + argActive + newLineChar + fold + fold2 + "Lists the connections used by at least one query." + newLineChar + fold + newLineChar + fold + argConnection + spaceChar + argList + spaceChar + argInactive + newLineChar + fold + fold2 + "Lists the connections not used by queries." + newLineChar + fold + newLineChar + fold + argConnection + spaceChar + argListall + newLineChar + fold + fold2 + "Lists all of your defined connections." + newLineChar + fold + newLineChar + fold + argConnection + spaceChar + argAdd + newLineChar + fold + fold2 + "Adds a new connection into your set of database connections." + newLineChar + fold + newLineChar + fold + argConnection + spaceChar + argLoad + spaceChar + messageYourFileName + newLineChar + fold + fold2 + "Loads and appends database conections from a file." + newLineChar + fold + fold2 + "Files are allowed being right next to the application." + newLineChar + fold + newLineChar + fold + argConnection + spaceChar + argDescribe + spaceChar + messageYourConnectionName + newLineChar + fold + fold2 + "Describes a connection you specified before." + newLineChar + fold + newLineChar + fold + argConnection + spaceChar + argDescribe + newLineChar + fold + fold2 + "Describes a connection you are using." + newLineChar + fold + newLineChar + fold + argConnection + spaceChar + argChange + spaceChar + messageYourConnectionName + newLineChar + fold + fold2 + "Changes a not in used connection you specified before." + newLineChar + fold + newLineChar + fold + argConnection + spaceChar + argChange + newLineChar + fold + fold2 + "Changes a not in used connection you are using." + newLineChar + fold + newLineChar + fold + argConnection + spaceChar + argDelete + spaceChar + messageYourConnectionName + newLineChar + fold + fold2 + "Deletes a not in used connection you specified before." + newLineChar + fold + newLineChar + fold + argConnection + spaceChar + argDelete + newLineChar + fold + fold2 + "Deletes the connection you are using." + newLineChar + fold + newLineChar + fold + argConnection + spaceChar + argDeleteall + newLineChar + fold + fold2 + "Deletes all of not in used connections you specified before." + newLineChar + fold + newLineChar + fold + argConnection + spaceChar + argUse + spaceChar + messageYourConnectionName + newLineChar + fold + fold2 + "Use this to use a specific database connection continuously." + newLineChar + fold + newLineChar + fold + argConnection + spaceChar + argUse + spaceChar + messageYourDatabaseType + spaceChar + messageYourConnectionName + newLineChar + fold + fold2 + "As the above but you can use a different connection in a different database type." + newLineChar + fold + newLineChar + fold + argConnection + spaceChar + argTest + spaceChar + messageYourConnectionName + newLineChar + fold + fold2 + "Tests the database connection." + newLineChar + fold + newLineChar + fold + argConnection + spaceChar + argTest + newLineChar + fold + fold2 + "Tests a new and unsaved database connection." + newLineChar + fold + newLineChar + fold + argQuery + spaceChar + argList + spaceChar + argActive + newLineChar + fold + fold2 + "Lists the active (running) queries." + newLineChar + fold + newLineChar + fold + argQuery + spaceChar + argList + spaceChar + argInactive + newLineChar + fold + fold2 + "Lists the inactive (not started, finished) queries." + newLineChar + fold + newLineChar + fold + argQuery + spaceChar + argListall + newLineChar + fold + fold2 + "Lists all of your queries." + newLineChar + fold + newLineChar + fold + argQuery + spaceChar + argAdd + spaceChar + argSingle + newLineChar + fold + fold2 + "Adds a single query to run." + newLineChar + fold + fold2 + "Will have a result set in case of running successfully." + newLineChar + fold + newLineChar + fold + argQuery + spaceChar + argAdd + spaceChar + argMultiple + newLineChar + fold + fold2 + "Adds multiple and separated queries to run." + newLineChar + fold + fold2 + "Won't have any result set." + newLineChar + fold + newLineChar + fold + argQuery + spaceChar + argAdd + spaceChar + argBatch + newLineChar + fold + fold2 + "Adds an sql and repeat it as will be specified." + newLineChar + fold + newLineChar + fold + argQuery + spaceChar + argFactory + spaceChar + messageYourQuery + newLineChar + fold + fold2 + "Writes sql commands continuously and displays the results." + newLineChar + fold + fold2 + "The queries will be executed on the thread of Queries," + newLineChar + fold + fold2 + "so the prompt will return only if the currently running query will be finished." + newLineChar + fold + fold2 + "The " + messageYourQuery + " is optional and has to doublequoted if it contains space." + newLineChar + fold + fold2 + "It can start with \"" + appQueryFilePrefix + "\" to read the content dynamically of the file next to" + newLineChar + fold + fold2 + "this application to use its content as the initial sql statement to execute." + newLineChar + fold + fold2 + "If you specify this query then this application will execute it immediately." + newLineChar + fold + newLineChar + fold + argQuery + spaceChar + argFactory + spaceChar + messageYourDatabaseType + spaceChar + messageYourConnectionName + spaceChar + messageYourQuery + newLineChar + fold + fold2 + "As the above but you can use a connection immediately." + newLineChar + fold + newLineChar + fold + argQuery + spaceChar + argDescribe + spaceChar + messageYourQueryId + newLineChar + fold + fold2 + "Describes the current state of a query." + newLineChar + fold + newLineChar + fold + argQuery + spaceChar + argRun + spaceChar + messageYourQueryId + newLineChar + fold + fold2 + "Runs your query if it is possible (not started or finished query)." + newLineChar + fold + newLineChar + fold + argQuery + spaceChar + argCancel + spaceChar + messageYourQueryId + newLineChar + fold + fold2 + "Cancels your query if it is possible (running query)." + newLineChar + fold + newLineChar + fold + argQuery + spaceChar + argCancelall + newLineChar + fold + fold2 + "Cancels all of your queries if it is possible (running queries)." + newLineChar + fold + newLineChar + fold + argQuery + spaceChar + argDelete + spaceChar + messageYourQueryId + newLineChar + fold + fold2 + "Deletes your query if it is possible (not started or finished query)." + newLineChar + fold + newLineChar + fold + argQuery + spaceChar + argDeleteall + newLineChar + fold + fold2 + "Deletes all of your queries if it is possible (not started or finished queries)." + newLineChar + fold + newLineChar + fold + argResult + spaceChar + argEcho + spaceChar + messageYourQueryId + newLineChar + fold + fold2 + "Echos the last result of that query onto console or into files." + newLineChar + fold + newLineChar + fold + argDelimiter + spaceChar + argShow + newLineChar + fold + fold2 + "Shows your delimiter in a specific database type." + newLineChar + fold + newLineChar + fold + argDelimiter + spaceChar + argChange + spaceChar + messageYourNewDelimiter + newLineChar + fold + fold2 + "Changes your delimiter in a specific database type." + newLineChar + fold + fold2 + "If the " + messageYourNewDelimiter + " is empty then the empty character" + newLineChar + fold + fold2 + "(new line char) will be used as the delimiter." + newLineChar + fold + newLineChar + fold + argConnections + spaceChar + argGood + spaceChar + argPassword + newLineChar + fold + fold2 + "Prints the expectations of the encrypted file containing the connections data." + newLineChar + fold + newLineChar + fold + argConnections + spaceChar + argPassword + spaceChar + argChange + newLineChar + fold + fold2 + "Changes your connections password." + newLineChar + fold + newLineChar + fold + argQuestionMark + newLineChar + fold + fold2 + "Prints the available commands only." + newLineChar + fold + newLineChar + fold + argHelp + newLineChar + fold + fold2 + "Prints this page." ;
/*
** Bye!
*/
  protected final String messageBye = newLineString + fold + "Bye!" ;
}