/*
** MyDbConne application.
**
** Description:    : This tiny application works for you as a command line
**                   sql client.
**                   Supports Mysql, Oracle, Mssql, Db2 and Postgresql.
**                   Handles 42 commands and this is not an accident.
**
** Published       : 05.03.2017
**
** Current version : 1.4
**
** Developed by    : Jozsef Kiss
**                   KissCode Systems Kft
**                   <http://www.prdare.com>
**
** Changelog       : 1.0 - 04.01.2017
**                   Initial release.
**                   1.1 - 04.05.2017
**                   Htm results contain the binary and long length contents
**                     behind a htm link.
**                   Data visualization has been improved.
**                   Smaller improvements.
**                   The oracle jdbc is needed to the recompiling because of
**                     BFILE datatype!
**                     (The jdbc driver is needed usually for building
**                      the connections into the database.)
**                   1.2 - 05.03.2017
**                   Oracle BFILE is now deprecated, using OracleBfile instead.
**                   1.3 - 08.19.2017
**                   1.4 - 03.19.2018
**                   Now supports Mysql.
**
** Example command to start this application:
**   "C:\Program Files\Java\jdk1.8.0_121\bin\java.exe" /
**   -cp C:\drivers\OracleJdbc.jar;C:\opensourcejava\MyDbConne\MyDbConne.jar /
**   com.kisscodesystems.MyDbConne.Main
**
** Class hierarchy:
**   - Utils
**     Independent utilities.
**   - Const -> Base -> Conn -> Qdata -> Print -> Upper -> Exec -> Queries
**     The Queries class is the thread to manage this application.
**   - Query
**     This will be the thread of each separately started queries.
**   - Main
**     This will start the application.
**
** Queries and Query implement runnable (these are threads!)
** and the others don't.
**
** MyDbConne is free software: you can redistribute it and/or modify
** it under the terms of the GNU General Public License as published by
** Free Software Foundation, version 3.
**
** MyDbConne is distributed in the hope that it will be useful,
** but WITHOUT ANY WARRANTY; without even the implied warranty of
** MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
** GNU General Public License for more details.
**
** You should have received a copy of the GNU General Public License
** along with MyDbConne. If not, see <http://www.gnu.org/licenses/>.
*/
package com . kisscodesystems . MyDbConne ;
import java . io . IOException ;
import java . io . ObjectInputStream ;
import java . io . ObjectOutputStream ;
public final class Main
{
// This is to be called when the user starts the application.
  public static void main ( String [ ] args )
  {
// Let's create the object that will be the main thread instead of this.
    Queries queries = new Queries ( ) ;
// The arguments will be its parameter.
    queries . setArgs ( args ) ;
// Let's create the thread of this object and start it!
    Thread queriesThread = new Thread ( queries ) ;
    if ( queriesThread != null )
    {
      queriesThread . start ( ) ;
// Waiting for this thread to be joined.
      try
      {
        queriesThread . join ( ) ;
      }
      catch ( InterruptedException e )
      {
        System . out . println ( "Exception while joining queriesThread: " + e . toString ( ) ) ;
      }
      finally
      {
// This can be null now.
        queriesThread = null ;
      }
    }
    else
    {
      System . out . println ( "The queriesThread of Queries is found as null, main" ) ;
    }
// Just for fun.
    System . exit ( 0 ) ;
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