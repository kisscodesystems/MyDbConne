# MyDbConns
The lightweight java sql client application.

The most important features of MyDbConns.

  - connection management, in encrypted files
    ; adds, edits and deletes connections
    ; loads connections from file
    ; the possibility to cache the connections password
    (compile time property, this app has to be recompiled to change this)
  - Mysql, Oracle, Mssql, Db2 and Postgresql databases are supported
  - multiple queries are available to run
    ; in multiple database types at the same time
  - sql-s can come from the console or from file
    ; supporting multi line sql commands, sql command delimiter by database types
  - added sql queries will be run in separate threads
    ; these can be watched and the results can be seen
    ; user can work while several sql-s are running on the separate threads
  - sql queries can be cancelled if your driver also supports this
  - query factory mode: the user can type its sql queries or commands
    ; continuously and see the results of these
    (user has to wait these queries to be finished, not in separate threads)
  - sql results can go onto the console or into txt, csv and/or htm files
  - single, multiple or batch type of queries can be added
    ; single: single sql query, selects mostly. it has a result set object
    ; multiple: executes multiple sql commands and it has no result set
    ; batch: one single sql query using a file or result set as datasource
    
Read more: http://kcsopensource.com
