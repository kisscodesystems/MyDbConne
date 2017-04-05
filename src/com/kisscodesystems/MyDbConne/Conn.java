/*
** This class is the part of the opensource MyDbConne application.
**
** See the header comment lines of Main class.
**
** Conn
** This class handles the connections located in the encrypted connections file.
*/
package com . kisscodesystems . MyDbConne ;
import java . io . File ;
import java . security . AlgorithmParameters ;
import java . security . InvalidAlgorithmParameterException ;
import java . security . InvalidKeyException ;
import java . security . NoSuchAlgorithmException ;
import java . security . SecureRandom ;
import java . security . spec . InvalidKeySpecException ;
import java . security . spec . InvalidParameterSpecException ;
import java . sql . Connection ;
import java . sql . DriverManager ;
import javax . crypto . BadPaddingException ;
import javax . crypto . Cipher ;
import javax . crypto . IllegalBlockSizeException ;
import javax . crypto . NoSuchPaddingException ;
import javax . crypto . SecretKey ;
import javax . crypto . SecretKeyFactory ;
import javax . crypto . spec . IvParameterSpec ;
import javax . crypto . spec . PBEKeySpec ;
import javax . crypto . spec . SecretKeySpec ;
public class Conn extends Base
{
/*
** For password reading from the console.
*/
  protected volatile char [ ] passwordFromInputOriginal = new char [ 0 ] ;
  protected volatile char [ ] passwordFromInputVerified = new char [ 0 ] ;
/*
** This stores the password given by the user
** to encrypt/decrypt the content of the user's connections.
*/
  protected volatile char [ ] passwordForConnections = new char [ 0 ] ;
/*
** These will contain the content of the connections file.
** Orig: all of the connections data padded into the length of
** appFileContentMaxLength
** Trim: the same as above but we would like to save the not zero char part
** so it will be trimmed into this before saving.
*/
  protected volatile char [ ] fileContentConnectionsOrig = new char [ 0 ] ;
  protected volatile char [ ] fileContentConnectionsTrim = new char [ 0 ] ;
/*
** The files of the initialization vector and the salt,
** will be stored next to the connections file.
*/
  protected volatile byte [ ] slConnections = new byte [ 0 ] ;
  protected volatile byte [ ] ivConnections = new byte [ 0 ] ;
/*
** THe folder object of the folder in which the connections file
** and the iv and sl files will be located.
*/
  protected volatile File connectionsDirFolder = null ;
/*
** Clearing all of the character arrays to clear these contents from the memory.
*/
  protected final void clearCharArrays ( )
  {
    if ( utils != null )
    {
      utils . clearCharArray ( passwordFromInputOriginal , zeroChar ) ;
      utils . clearCharArray ( passwordFromInputVerified , zeroChar ) ;
      utils . clearCharArray ( passwordForConnections , zeroChar ) ;
      utils . clearCharArray ( fileContentConnectionsOrig , zeroChar ) ;
      utils . clearCharArray ( fileContentConnectionsTrim , zeroChar ) ;
    }
    else
    {
      systemexit ( "Error - utils is null, clearCharArrays" ) ;
    }
  }
/*
** Clearing all of the byte arrays to clear these contents from the memory.
*/
  protected final void clearByteArrays ( )
  {
    if ( utils != null )
    {
      utils . clearByteArray ( slConnections , nullByte ) ;
      utils . clearByteArray ( ivConnections , nullByte ) ;
    }
    else
    {
      systemexit ( "Error - utils is null, clearByteArrays" ) ;
    }
  }
/*
** Reads the password of the connections file.
** Can be verified.
** Password should be verified if the file of connections will be created.
** Should not be verified if the user starts the application
** at least second time after the connections file initialization.
*/
  protected final void readPassword ( boolean beVerified )
  {
    String fileName = appConnectionsFileName ;
    if ( fileName != null && utils != null )
    {
// Valid and verified password is required
// so these will be the holders of these values.
      boolean isValidPassword = false ;
      boolean isVerifiedPassword = false ;
// Repeat this reading procedure until valid and verified password we have.
      while ( ! isValidPassword || ! isVerifiedPassword )
      {
// Let these values be here.
        isValidPassword = false ;
        isVerifiedPassword = true ;
// Clearing this char array as we don't know how it has been used.
        utils . clearCharArray ( passwordFromInputOriginal , zeroChar ) ;
// Now reading the password into the temporary char array.
        passwordFromInputOriginal = readpassword ( messageEnterPasswordForConnections ) ;
// Clearing the final place of the password from the same reason.
        utils . clearCharArray ( passwordForConnections , zeroChar ) ;
// Let's recreate the final place of password in the known size.
        passwordForConnections = new char [ passwordFromInputOriginal . length ] ;
// Now we are going to validate the password now.
        isValidPassword = isValidGoodPassword ( passwordFromInputOriginal , beVerified ) ;
// If it is a valid password..
        if ( isValidPassword )
        {
// .. then we copy the content of the temp char array into the
// right place of this password.
          for ( int i = 0 ; i < passwordFromInputOriginal . length ; i ++ )
          {
            passwordForConnections [ i ] = passwordFromInputOriginal [ i ] ;
          }
// If we should verify the password then let's do this now.
          if ( beVerified )
          {
// Preparing the secont temporary character array for using.
            utils . clearCharArray ( passwordFromInputVerified , zeroChar ) ;
// Reading the password secont time into this.
            passwordFromInputVerified = readpassword ( messageEnterPasswordVerify ) ;
// Verifying its content: are this and the previous passwords matched?
            if ( passwordFromInputVerified . length != passwordFromInputOriginal . length )
            {
// If the length of the two char array is different, it is not verified immediately.
              isVerifiedPassword = false ;
            }
            else
            {
// The lengths are OK so we have to read all of the characters
// and searching for the first not equal character.
              for ( int i = 0 ; i < passwordFromInputVerified . length ; i ++ )
              {
                if ( passwordFromInputVerified [ i ] != passwordFromInputOriginal [ i ] )
                {
// This is the point where we can decide that the two passwords
// do not match so the whole reading procedure can be started over.
                  isVerifiedPassword = false ;
// It is not needed to searching for another not matched character..
                  break ;
                }
              }
            }
// Clearing the used verifyer character array to be safe.
            utils . clearCharArray ( passwordFromInputVerified , zeroChar ) ;
// Now a message can go tho the user if the verification was not successful.
            if ( ! isVerifiedPassword )
            {
              outprintln ( messagePasswordVerificationError ) ;
            }
          }
        }
// Clearing the first temp char array also to be safe.
        utils . clearCharArray ( passwordFromInputOriginal , zeroChar ) ;
// Now the loop will be started over if the verification error has occurred
// or the password was not valid.
      }
// These are not in use.
      isValidPassword = false ;
      isVerifiedPassword = false ;
    }
    else
    {
      systemexit ( "Error - one of these is null: utils|fileName, readPassword" ) ;
    }
// Not usable.
    fileName = null ;
  }
/*
** Teest database connection according to the given connection data.
*/
  protected final void testConnection ( String dbuser , String dbpass , String driver , String connst )
  {
// This will be the connection object.
    Connection connection = null ;
    try
    {
// Trying to select the driver and build the connection.
      Class . forName ( driver ) ;
      connection = DriverManager . getConnection ( connst , dbuser , new String ( dbpass ) ) ;
// If we are here then the connection has been built so a message can go to the user.
      outprintln ( messageConnectionHasBeenTestedSuccessfully ) ;
    }
    catch ( Exception e )
    {
// Any exception results this message.
      outprintln ( messageUnableToConnect ) ;
      outprintln ( fold + e . toString ( ) . trim ( ) ) ; ;
    }
    finally
    {
      if ( connection != null )
      {
// This should be closed.
        try
        {
          connection . close ( ) ;
        }
        catch ( Exception e )
        {
          outprintln ( messageUnableToCloseTesterConnection ) ;
        }
      }
    }
// Releasing of this.
    connection = null ;
  }
/*
** All the file operations can be rolled back.
** The modifications will be saved into new files at first.
** (-> not in this function! That is in the fileSave function.)
** If this operation can be successfully finished then the old files will be deleted
** (cs and sl and iv) and the new files will be renamed to the old file names.
** (-> this is in this function.)
*/
  protected final boolean removeOldFilesAndRenameNewFiles ( )
  {
// This is the variable which can be returned. False by default.
// It can be true just at the end.
    boolean success = false ;
// This will be the file name.
    String fileName = appConnectionsFileName ;
// These are the file objects.
// We are going to use them to get the correct files from the disk.
    File file = null ;
    File slFile = null ;
    File ivFile = null ;
// The old (the original) files will be read.
    file = new File ( appConnectionsDir + SEP + fileName + appCsPostfix ) ;
    slFile = new File ( appConnectionsDir + SEP + fileName + appSlPostfix ) ;
    ivFile = new File ( appConnectionsDir + SEP + fileName + appIvPostfix ) ;
    if ( file != null && slFile != null && ivFile != null )
    {
// We are trying to delete them.
// If any of this won't be successful then won't continue and the user will get a message
// to do the correction manually. (old files delete, new files rename to old filenames.)
      if ( ( file . exists ( ) && ! file . delete ( ) ) || ( slFile . exists ( ) && ! slFile . delete ( ) ) || ( ivFile . exists ( ) && ! ivFile . delete ( ) ) )
      {
        outprintln ( messageErrorDeletingOldFilesOrRenameNewFiles + fileName ) ;
      }
      else
      {
// Now the old files are deleted.
// We will rename the new files into old filenames by using these objects.
        File fileOld = null ;
        File fileNew = null ;
        File fileOldSl = null ;
        File fileNewSl = null ;
        File fileOldIv = null ;
        File fileNewIv = null ;
// Let's create them.
        fileOld = new File ( appConnectionsDir + SEP + fileName + appCsPostfix + appNwPostfix ) ;
        fileNew = new File ( appConnectionsDir + SEP + fileName + appCsPostfix ) ;
        fileOldSl = new File ( appConnectionsDir + SEP + fileName + appSlPostfix + appNwPostfix ) ;
        fileNewSl = new File ( appConnectionsDir + SEP + fileName + appSlPostfix ) ;
        fileOldIv = new File ( appConnectionsDir + SEP + fileName + appIvPostfix + appNwPostfix ) ;
        fileNewIv = new File ( appConnectionsDir + SEP + fileName + appIvPostfix ) ;
        if ( fileOld != null && fileNew != null && fileOldSl != null && fileNewSl != null && fileOldIv != null && fileNewIv != null )
        {
// Trying to rename the files.
// Error message will go tho the user if it is not successful.
          if ( ! fileOld . renameTo ( fileNew ) || ! fileOldSl . renameTo ( fileNewSl ) || ! fileOldIv . renameTo ( fileNewIv ) )
          {
            outprintln ( messageErrorDeletingOldFilesOrRenameNewFiles + fileName ) ;
          }
          else
          {
// All of the operation has been finished successfully.
            success = true ;
          }
        }
        else
        {
          systemexit ( "Error - One of these is null: fileOld|fileNew|fileOldSl|fileNewSl|fileOldIv|fileNewIv, removeOldFilesAndRenameNewFiles" ) ;
        }
// Not usable references.
        fileOld = null ;
        fileNew = null ;
        fileOldSl = null ;
        fileNewSl = null ;
        fileOldIv = null ;
        fileNewIv = null ;
      }
    }
    else
    {
      systemexit ( "Error - One of these is null: file|slFile|ivFile, removeOldFilesAndRenameNewFiles" ) ;
    }
// Not usable references.
    file = null ;
    slFile = null ;
    ivFile = null ;
// Returning the success.
    return success ;
  }
/*
** This is the file saving function.
** Encrypts the data and saves the data into NEW files!
** Another function (removeOldFilesAndRenameNewFiles) can handle the deleting of old files
** and renaming the new files into the old filenames.
** This is for failsafe considerations. If any of the operation will be failed then the
** old files are still there!
** In every saving operation the content will be written into a NEW file!
** The salt and the initialization vector bytes are also will be generated in every saving operation!
*/
  protected final boolean saveFile ( )
  {
// Successful only at the end!
    boolean success = false ;
// This will be the filename.
    String fileName = appConnectionsFileName ;
    if ( fileName != null && utils != null )
    {
// Our secure random object to generate safe random values.
      SecureRandom secureRandom = new SecureRandom ( ) ;
      if ( secureRandom != null )
      {
// The salt will be generated and written to disk (into new file)
        slConnections = new byte [ appSaltLength ] ;
        secureRandom . nextBytes ( slConnections ) ;
        writeFileBytes ( appConnectionsDir + SEP + fileName + appSlPostfix + appNwPostfix , slConnections ) ;
      }
      else
      {
        systemexit ( "Error - secureRandom is null, saveFile" ) ;
      }
// This is done, we are not going to use this below.
      secureRandom = null ;
// We need a SecretKeyFactory object.
// Exception and exit if we cannot have this.
      SecretKeyFactory skf = null ;
      try
      {
        skf = SecretKeyFactory . getInstance ( appSecretKeyFactoryInstance ) ;
      }
      catch ( NoSuchAlgorithmException e )
      {
        systemexit ( "Exception - NoSuchAlgorithmException0, saveFile" ) ;
      }
      if ( skf == null )
      {
        systemexit ( "Error - skf is null, saveFile" ) ;
      }
// The next is the PBEKeySpec object.
      PBEKeySpec pbeks = new PBEKeySpec ( passwordForConnections , slConnections , appPbeKeySpecIterations , appPbeKeySpecKeyLength ) ;
      if ( pbeks == null )
      {
        systemexit ( "Error - pbeks is null, saveFile" ) ;
      }
// We need a SecretKey too according this pbeks above.
// Exception and exit if it cannot be done.
      SecretKey sk = null ;
      try
      {
        sk = skf . generateSecret ( pbeks ) ;
      }
      catch ( InvalidKeySpecException e )
      {
        systemexit ( "Exception - InvalidKeyException, saveFile" ) ;
      }
      if ( sk == null )
      {
        systemexit ( "Error - sk is null, saveFile" ) ;
      }
// Almost there, the SecretKeySpec object is the next.
      SecretKeySpec sks = new SecretKeySpec ( sk . getEncoded ( ) , appSecretKeySpecAlgorythm ) ;
      if ( sks == null )
      {
        systemexit ( "Error - sks is null, saveFile" ) ;
      }
// Now trying to create the cipher object.
// Exception and exit too when it is not successful.
      Cipher cipher = null ;
      try
      {
        cipher = Cipher . getInstance ( appCipherInstance ) ;
      }
      catch ( NoSuchAlgorithmException e )
      {
        systemexit ( "Exception - NoSuchAlgorithmException1, saveFile" ) ;
      }
      catch ( NoSuchPaddingException e )
      {
        systemexit ( "Exception - NoSuchPaddingException, saveFile" ) ;
      }
      if ( cipher != null )
      {
// Now we are ready to initialize our cipher instance.
// Exception and exit if it is not successful.
        try
        {
          cipher . init ( Cipher . ENCRYPT_MODE , sks ) ;
        }
        catch ( InvalidKeyException e )
        {
          systemexit ( "Exception - InvalidKeyException, saveFile" ) ;
        }
// This object is needed to store the iv.
        AlgorithmParameters ap = cipher . getParameters ( ) ;
        if ( ap != null )
        {
// The iv will be cached into the byte array and will be saved onto the disk.
// The whole process will be broken in case of InvalidParameterSpecException.
          try
          {
            ivConnections = ap . getParameterSpec ( IvParameterSpec . class ) . getIV ( ) ;
          }
          catch ( InvalidParameterSpecException e )
          {
            systemexit ( "Exception - InvalidParameterSpecException2, saveFile" ) ;
          }
          writeFileBytes ( appConnectionsDir + SEP + fileName + appIvPostfix + appNwPostfix , ivConnections ) ;
        }
        else
        {
          systemexit ( "Error - ap is null, saveFile" ) ;
        }
// This object can be null now.
        ap = null ;
// The byte arrays are the following.
// The (original) bytes and the encrypted bytes, nulls at first.
        byte [ ] encryptedBytes = null ;
        byte [ ] bytes = null ;
// We have to know what is the end index of the original character array!
// (the further characters are zeros to the end of the array
// and we do not want to store these zero chars in the encrypted content.)
        int endIndex = getFirstNewLineAndZeroCharIndex ( ) ;
// Clearing, reinitializing and clearing again the correct character array.
        utils . clearCharArray ( fileContentConnectionsTrim , zeroChar ) ;
        fileContentConnectionsTrim = new char [ endIndex + 1 ] ;
        utils . clearCharArray ( fileContentConnectionsTrim , zeroChar ) ;
// The content of the orig char array will be copied into the trim char array,
// but without the last zero chars!!
        for ( int i = 0 ; i < fileContentConnectionsTrim . length ; i ++ )
        {
          fileContentConnectionsTrim [ i ] = fileContentConnectionsOrig [ i ] ;
        }
// Converting to byte array and place this into the bytes array
        bytes = utils . toBytesASCII ( fileContentConnectionsTrim ) ;
// The final step is the following: encrypt the bytes array into the encryptedBytes!
// Exception and exit in case of failure.
        try
        {
          encryptedBytes = cipher . doFinal ( bytes ) ;
        }
        catch ( IllegalBlockSizeException e )
        {
          systemexit ( "Exception - IllegalBlockSizeException, saveFile" ) ;
        }
        catch ( BadPaddingException e )
        {
          systemexit ( "Exception - BadPaddingException, saveFile" ) ;
        }
// The encrypted bytes have to be written to the disk (into new file!)
// next to the earlier written and also new sl and iv files.
        writeFileBytes ( appConnectionsDir + SEP + fileName + appCsPostfix + appNwPostfix , encryptedBytes ) ;
// These byte arrays have to be cleared!
        utils . clearByteArray ( encryptedBytes , nullByte ) ;
        encryptedBytes = null ;
        utils . clearByteArray ( bytes , nullByte ) ;
        bytes = null ;
      }
      else
      {
        systemexit ( "Error - cipher is null, saveFile" ) ;
      }
// These variables have to be set to null now.
      cipher = null ;
      sks = null ;
      sk = null ;
      pbeks = null ;
      skf = null ;
// We will use these objects for the renaming.
      File fileNew = null ;
      File slFileNew = null ;
      File ivFileNew = null ;
// Searching for new files. (appNwPostfix postfix!).
      fileNew = new File ( appConnectionsDir + SEP + fileName + appCsPostfix + appNwPostfix ) ;
      slFileNew = new File ( appConnectionsDir + SEP + fileName + appSlPostfix + appNwPostfix ) ;
      ivFileNew = new File ( appConnectionsDir + SEP + fileName + appIvPostfix + appNwPostfix ) ;
      if ( fileNew != null && slFileNew != null && ivFileNew != null )
      {
// These have to be existing at this point.
        if ( fileNew . exists ( ) && slFileNew . exists ( ) && ivFileNew . exists ( ) )
        {
// Now, the old files have to be removed and the new files have to be renamed back to the original filenames.
          if ( removeOldFilesAndRenameNewFiles ( ) )
          {
// If it is successful then we are done.
            success = true ;
            outprintln ( messageFileHasBeenSaved + fileName ) ;
          }
        }
        else
        {
// Message to the user because this saving operation has been failed.
          outprintln ( messageMissingNewCsOrSlOrIvFile ) ;
// Deleting the existing new files.
// (The old ones are still there.)
          if ( fileNew . exists ( ) )
          {
            if ( ! fileNew . delete ( ) )
            {
              outprintln ( messageErrorDeletingNewAnFile ) ;
            }
          }
          if ( slFileNew . exists ( ) )
          {
            if ( ! slFileNew . delete ( ) )
            {
              outprintln ( messageErrorDeletingNewSlFile ) ;
            }
          }
          if ( ivFileNew . exists ( ) )
          {
            if ( ! ivFileNew . delete ( ) )
            {
              outprintln ( messageErrorDeletingNewIvFile ) ;
            }
          }
        }
      }
      else
      {
        systemexit ( "Error - One of these is null: fileNew|slFileNew|ivFileNew, saveFile" ) ;
      }
// These are releasable references.
      fileNew = null ;
      slFileNew = null ;
      ivFileNew = null ;
    }
    else
    {
      systemexit ( "Error - one of these is null: utils|fileName, saveFile" ) ;
    }
// Returning the success of this operation.
    return success ;
  }
/*
** Getting the content of the connections file.
** The successful operation is set after the header has been successfully found in the
** decrypted content.
*/
  protected final boolean getFileContent ( )
  {
// This is a boolean function so here is the returning value.
// False because we will set it to true only at the end. (header found)
    boolean success = false ;
// The name of the file will be this.
    String fileName = appConnectionsFileName ;
    if ( fileName != null && utils != null )
    {
// THe files have to be ready for decrypting.
      if ( isExistingConnectionsFiles ( true ) )
      {
// We are going to clear the character arrays (just for to be sure),
// reinitialize the array and clear again the character array.
        utils . clearCharArray ( fileContentConnectionsOrig , zeroChar ) ;
        fileContentConnectionsOrig = new char [ appFileContentMaxLength ] ;
        utils . clearCharArray ( fileContentConnectionsOrig , zeroChar ) ;
// Now we have to read the sl and iv bytes from the disk.
        slConnections = readFileBytes ( appConnectionsDir + SEP + fileName + appSlPostfix ) ;
        if ( slConnections == null )
        {
          systemexit ( "Error - slConnections is null, getFileContent" ) ;
        }
        else if ( slConnections . length == 0 )
        {
          systemexit ( "Error - slConnections is empty, getFileContent" ) ;
        }
        ivConnections = readFileBytes ( appConnectionsDir + SEP + fileName + appIvPostfix ) ;
// We are going to initialize the SecretKeyFactory object.
// Will get a nice exception if it is not correct on the computer and exit.
        SecretKeyFactory skf = null ;
        try
        {
          skf = SecretKeyFactory . getInstance ( appSecretKeyFactoryInstance ) ;
        }
        catch ( NoSuchAlgorithmException e )
        {
          systemexit ( "Exception - NoSuchAlgorithmException0, getFileContent" ) ;
        }
        if ( skf == null )
        {
          systemexit ( "Error - skf is null, getFileContent" ) ;
        }
// Now the next is the PBEKeySpec object.
// The salt and the password will be used as you can see.
        PBEKeySpec pbeks = new PBEKeySpec ( passwordForConnections , slConnections , appPbeKeySpecIterations , appPbeKeySpecKeyLength ) ;
        if ( pbeks == null )
        {
          systemexit ( "Error - pbeks is null, getFileContent" ) ;
        }
// The secret key is on the way.
// Exception and exit if it is not created successfully.
        SecretKey sk = null ;
        try
        {
          sk = skf . generateSecret ( pbeks ) ;
        }
        catch ( InvalidKeySpecException e )
        {
          systemexit ( "Exception - InvalidKeySpecException, getFileContent" ) ;
        }
        if ( sk == null )
        {
          systemexit ( "Error - sk is null, getFileContent" ) ;
        }
// The SecretKeySpec is the next after the SecretKey.
        SecretKeySpec sks = new SecretKeySpec ( sk . getEncoded ( ) , appSecretKeySpecAlgorythm ) ;
// We are almost there, the cipher will be initialized soon.
// Exiting if it will be not successful.
        Cipher cipher = null ;
        try
        {
          cipher = Cipher . getInstance ( appCipherInstance ) ;
        }
        catch ( NoSuchAlgorithmException e )
        {
          systemexit ( "Exception - NoSuchAlgorithmException1, getFileContent" ) ;
        }
        catch ( NoSuchPaddingException e )
        {
          systemexit ( "Exception - NoSuchPaddingException, getFileContent" ) ;
        }
        if ( cipher != null )
        {
// The IvParameterSpec is needed to cipher init, let's create it by passwordTypes
          IvParameterSpec ips = new IvParameterSpec ( ivConnections ) ;
          if ( ips == null )
          {
            systemexit ( "Error - ips is null, getFileContent" ) ;
          }
// The cipher object is now ready to initialize for decryption!
// Let's do this!
// Exception and exit in case of InvalidAlgorithmParameter
// Message to the user if the Key is invalid (we will continue at this time.)
          try
          {
            cipher . init ( Cipher . DECRYPT_MODE , sks , ips ) ;
          }
          catch ( InvalidAlgorithmParameterException e )
          {
            systemexit ( "Exception - InvalidAlgorithmParameterException, getFileContent" ) ;
          }
          catch ( InvalidKeyException e )
          {
            systemexit ( "Exception - InvalidKeyException, getFileContent" ) ;
          }
// These are the byte array objects we want to use.
// bytes and decrypted bytes.
          byte [ ] decryptedBytes = null ;
          byte [ ] bytes = null ;
// The (now encrypted) bytes comes from the disk.
          bytes = readFileBytes ( appConnectionsDir + SEP + fileName + appCsPostfix ) ;
// We are trying to decrypt the encrypted byte array.
// Exception and exit when IllegalBlockSize
// Message to the user if BadPaddingException has occurred.
          try
          {
            decryptedBytes = cipher . doFinal ( bytes ) ;
          }
          catch ( IllegalBlockSizeException e )
          {
            systemexit ( "Exception - IllegalBlockSizeException, getFileContent" ) ;
          }
          catch ( BadPaddingException e )
          {
            outprintln ( messageIncorrectFilePassword ) ;
          }
// These objects are not needed any more.
          ips = null ;
          cipher = null ;
          sks = null ;
          sk = null ;
          pbeks = null ;
          skf = null ;
// Searching for header just to be sure about the successful decryption.
// The header is not found by default.
          boolean headerFound = false ;
          if ( decryptedBytes != null )
          {
// We can now read the bytes we have decrypted.
// Do the conversion.
            char [ ] tempChar = utils . toCharsASCII ( decryptedBytes ) ;
// This byte array will be read into the correct type of content character array.
// The appFileContentMaxLength will be used just to be absolutely sure about having the
// correct maximum size of content.
            for ( int i = 0 ; i < Math . min ( tempChar . length , appFileContentMaxLength ) ; i ++ )
            {
              fileContentConnectionsOrig [ i ] = tempChar [ i ] ;
            }
// The header will be searched
            headerFound = isContentDecrypted ( true ) ;
// This temporary character array will be not used any more, can be cleared.
            utils . clearCharArray ( tempChar , zeroChar ) ;
            tempChar = null ;
            utils . clearByteArray ( decryptedBytes , nullByte ) ;
            decryptedBytes = null ;
          }
          else
          {
            headerFound = false ;
          }
// The original byte array can be cleared.
          utils . clearByteArray ( bytes , nullByte ) ;
          bytes = null ;
// If we have the found header then we are good.
// The user gets a message if not.
          if ( headerFound )
          {
            success = true ;
          }
          else
          {
            outprintln ( messageFileContentHasNotBeenFound + fileName ) ;
          }
// This is not necessary but..
          headerFound = false ;
        }
        else
        {
          systemexit ( "Error - cipher is null, getFileContent" ) ;
        }
      }
      else
      {
// We are not started yet because some file is missing.
        outprintln ( messageMissingCsOrSlOrIvFile + fileName ) ;
      }
    }
    else
    {
      systemexit ( "Error - one of these is null: utils|fileName, getFileContent" ) ;
    }
// The return of the success.
    return success ;
  }
/*
** Is the content of the connections being decripted?
*/
  protected final boolean isContentDecrypted ( boolean messageIfNot )
  {
// The header of the content (connectionsHeader) is found.
    boolean headerFound = true ;
    if ( fileContentConnectionsOrig != null && connectionsHeader != null )
    {
// Ha the length at least the length of connectionsHeader?
      if ( fileContentConnectionsOrig . length >= connectionsHeader . length ( ) )
      {
// Looping on the beginning of the content.
        for ( int i = 0 ; i < connectionsHeader . length ( ) ; i ++ )
        {
// In the first not matched character will be unsuuccessful.
          if ( fileContentConnectionsOrig [ i ] != connectionsHeader . charAt ( i ) )
          {
            headerFound = false ;
            break ;
          }
        }
      }
      else
      {
        headerFound = false ;
      }
    }
    else
    {
      headerFound = false ;
    }
// If the header has not been found and a message has been requested then let's print it out!
    if ( ! headerFound && messageIfNot )
    {
      outprintln ( messageContentIsNotDecrypted ) ;
    }
// Returning of the success.
    return headerFound ;
  }
/*
** Searches for the connections files. (connections, salt, iv)
*/
  protected final boolean isExistingConnectionsFiles ( boolean messageIfNot )
  {
    return ( isExistingFile ( appConnectionsDir + SEP + appConnectionsFileName + appCsPostfix , messageIfNot ) && isExistingFile ( appConnectionsDir + SEP + appConnectionsFileName + appIvPostfix , messageIfNot ) && isExistingFile ( appConnectionsDir + SEP + appConnectionsFileName + appSlPostfix , messageIfNot ) ) ;
  }
/*
** Checks that the file content of the connections file is ready or not.
** (Is the content being decrypted.)
** If not, the password will be asked from the user.
** This is necessary while it is possible to not caching this password by
** setting the appConnectionPasswordCache to false. So, if the password is
** needed and it is not typed then it will be asked.
*/
  protected final boolean isFileContentConnectionsOrigReady ( )
  {
// This will be the final success result.
    boolean success = false ;
// If the content is not decrypted
// (the header string of the content (the first line) is not found)
    if ( ! isContentDecrypted ( false ) )
    {
// Then the password will be prompted.
      readPassword ( false ) ;
// Trying to decrypt the content of the connections using this password.
      if ( getFileContent ( ) )
      {
// If it is successful then we are successful.
        success = true ;
      }
    }
    else
    {
// The file content is decrypted, so this is just a making sure validation.
      if ( fileContentConnectionsOrig != null )
      {
// And has to be at the length of appFileContentMaxLength.
        if ( fileContentConnectionsOrig . length == appFileContentMaxLength )
        {
// If this has the correct length then we are successful.
          success = true ;
        }
        else
        {
          systemexit ( "Error - fileContentConnectionsOrig is not at length appFileContentMaxLength, isFileContentConnectionsOrigReady" ) ;
        }
      }
      else
      {
        systemexit ( "Error - fileContentConnectionsOrig is null, isFileContentConnectionsOrigReady" ) ;
      }
    }
// Returning this.
    return success ;
  }
/*
** Gets the end position of the content of the connections file.
*/
  protected final int getFirstNewLineAndZeroCharIndex ( )
  {
// This is the default value (not found)
    int index = - 1 ;
    if ( fileContentConnectionsOrig != null )
    {
// Going on this and find the newLine + zeroChar characters.
      for ( int i = 0 ; i < fileContentConnectionsOrig . length - 1 ; i ++ )
      {
        if ( fileContentConnectionsOrig [ i ] == newLineChar && fileContentConnectionsOrig [ i + 1 ] == zeroChar )
        {
          index = i ;
          break ;
        }
      }
    }
    else
    {
      systemexit ( "Error - fileContentConnectionsOrig is null, getFirstNewLineAndZeroCharIndex" ) ;
    }
// Returning of the content ending.
    return index ;
  }
/*
** These two are for inserting the data and a new line into the correct position of connections file content.
*/
  protected final void insertAnAttributeIntoFileContentConnectionsOrig ( String string , int posToInsert )
  {
    if ( isFileContentConnectionsOrigReady ( ) )
    {
      if ( string != null )
      {
        for ( int i = 0 ; i < string . length ( ) ; i ++ )
        {
          fileContentConnectionsOrig [ posToInsert + i ] = string . charAt ( i ) ;
        }
        fileContentConnectionsOrig [ posToInsert + string . length ( ) ] = newLineChar ;
      }
      else
      {
        systemexit ( "Error - string is null, insertAnAttributeIntoFileContentConnectionsOrig" ) ;
      }
    }
  }
  protected final void insertAnAttributeIntoFileContentConnectionsOrig ( char [ ] chars , int posToInsert )
  {
    if ( isFileContentConnectionsOrigReady ( ) )
    {
      if ( chars != null )
      {
        for ( int i = 0 ; i < chars . length ; i ++ )
        {
          fileContentConnectionsOrig [ posToInsert + i ] = chars [ i ] ;
        }
        fileContentConnectionsOrig [ posToInsert + chars . length ] = newLineChar ;
      }
      else
      {
        systemexit ( "Error - chars is null, insertAnAttributeIntoFileContentConnectionsOrig" ) ;
      }
    }
  }
/*
** Shifts the necessary part of the content when
** - a connection will be deleted
** - a connection will be changed and the characters length of the
**   new connection is defferent from the chars length of old connection.
** This is to be the data of connections continuously in the file.
*/
  protected final void shiftFileContent ( int startPos , int diff )
  {
// Only when it is not 0!
    if ( diff != 0 )
    {
      if ( utils != null )
      {
// This will be the end of the content.
        int firstNewLineAndZeroCharIndexOrig = 0 ;
// This will be the count of the content which are going to be shifted.
        int movedPartCount = 0 ;
// We have to have the content of the connections file.
        if ( isFileContentConnectionsOrigReady ( ) )
        {
// The end of the content is at position..
          firstNewLineAndZeroCharIndexOrig = getFirstNewLineAndZeroCharIndex ( ) ;
// This is the size of the moved part of the content.
          movedPartCount = firstNewLineAndZeroCharIndexOrig - startPos + 1 ;
// Now we can initialize this.
          char [ ] movedPart = new char [ movedPartCount ] ;
// And has to be cleared by the space filling character.
          utils . clearCharArray ( movedPart , zeroChar ) ;
// Let's copy the content of the moved part.
          for ( int i = startPos ; i <= firstNewLineAndZeroCharIndexOrig ; i ++ )
          {
            movedPart [ i - startPos ] = fileContentConnectionsOrig [ i ] ;
          }
// Before writing we would like to ensure that the data will be not lost!
// We will not try to write a part of the moved content before the file content
          if ( startPos + diff >= 0 )
          {
// We will not try to write a part of the moved content after the file content
            if ( startPos + diff + ( movedPart . length - 1 ) < fileContentConnectionsOrig . length )
            {
// And now write it back into the connections char array into the correct new position.
              for ( int i = 0 ; i < movedPart . length ; i ++ )
              {
                fileContentConnectionsOrig [ startPos + diff + i ] = movedPart [ i ] ;
              }
// The final step is clear the correct chars depending on the diff.
// If we took the chars backward then the rest of the char array must be cleared after the moved part.
// Clear: fill with zero characters.
              if ( diff < 0 )
              {
                for ( int i = startPos + diff + movedPartCount ; i <= firstNewLineAndZeroCharIndexOrig ; i ++ )
                {
                  fileContentConnectionsOrig [ i ] = zeroChar ;
                }
              }
// If we took the chars forward then we have to clear the content before the moved part.
// It is taken from the start pos to count moved part count or diff, to which has the smallest count.
              else if ( diff > 0 )
              {
                for ( int i = startPos ; i < startPos + Math . min ( movedPartCount , diff ) ; i ++ )
                {
                  fileContentConnectionsOrig [ i ] = zeroChar ;
                }
              }
            }
            else
            {
              systemexit ( "Error - Writing after content, shiftFileContent" ) ;
            }
          }
          else
          {
            systemexit ( "Error - Write before content, shiftFileContent" ) ;
          }
// The movedPart array is a temp char array, its content must be cleared.
          utils . clearCharArray ( movedPart , zeroChar ) ;
// And the char array must be set to null.
          movedPart = null ;
        }
// Not in use any more.
        firstNewLineAndZeroCharIndexOrig = 0 ;
        movedPartCount = 0 ;
      }
      else
      {
        systemexit ( "Error - utils is null, shiftFileContent" ) ;
      }
    }
  }
/*
** Gets the second line of content from the connections file
** and prints it to the user.
*/
  protected final void printIniMessage ( )
  {
// The password and the connections file content are required.
    if ( isFileContentConnectionsOrigReady ( ) )
    {
// We have to count the new line characters.
      int newLineCounter = 0 ;
// This is the beginning of the printing.
      outprint ( newLineChar + fold ) ;
// Going from the beginning of the connections.
      for ( int i = 0 ; i < fileContentConnectionsOrig . length ; i ++ )
      {
// We are printing if we are currently in the second line.
// That contains the date of the initializatopn of this app instance.
        if ( newLineCounter == 1 )
        {
          outprint ( fileContentConnectionsOrig [ i ] ) ;
        }
// Plus one new lines. If this is 2 (so we are in the third line) we will break.
        if ( fileContentConnectionsOrig [ i ] == newLineChar )
        {
          newLineCounter ++ ;
          if ( newLineCounter == 2 )
          {
            break ;
          }
        }
      }
// Not used.
      newLineCounter = 0 ;
    }
  }
/*
** Getting the data of a connection specified by the database type and the connection name.
** the newLineDelta means what is the difference from the end of the connection name.
** 0 means database user, 1 means database user password, 2 means connection driver, 3 means connection string.
** (4 would be the database type of the next connection!)
*/
  protected final char [ ] getContentData ( String dbtype , String connna , int newlineDelta )
  {
// The content part to be returned.
    char [ ] content = new char [ 0 ] ;
// The newlineDelta sould be between 0 and 3.
    if ( newlineDelta >= 0 && newlineDelta <= 3 )
    {
// The current position of the connection defined by the database type - connection name
// and later the potision of the end of the toSearch
      int pos = - 1 ;
// The position of the content we would like to have.
      int contentPos = - 1 ;
// THe count of the returning array will be this.
      int charArrayCount = 0 ;
// We have to count the new line characters while looping on the content of connections.
      int newLines = 0 ;
// This is a temporary string containing the database type - connection name and the newLines.
      String toSearch = newLineString + dbtype + newLineChar + connna + newLineChar ;
// The valid connections file content is needed for this.
      if ( isFileContentConnectionsOrigReady ( ) )
      {
// Let's determine the position of the connection first.
        pos = getConnnaPos ( dbtype , connna ) ;
// This connection has to be existing.
        if ( pos != - 1 )
        {
// Let's increase this pos into the beginning of the connection data.
          pos = pos + toSearch . length ( ) - 1 ;
// Let's loop on the content..
          for ( int i = pos ; i < fileContentConnectionsOrig . length ; i ++ )
          {
// If the next characer is the zero car that means that we reached the
// end of the whole content so nothing more is to be searched for.
            if ( fileContentConnectionsOrig [ i ] == zeroChar )
            {
              break ;
            }
// The correct count of new lines is missing.
// Marking this potision and exiting from the loop.
            if ( newLines == newlineDelta )
            {
              contentPos = i ;
              break ;
            }
// And here we count the new line characters.
            if ( fileContentConnectionsOrig [ i ] == newLineChar )
            {
              newLines ++ ;
            }
          }
        }
// If this position is not -1 then a valid potision to start has been set.
        if ( contentPos != - 1 )
        {
// Go thru on the content from this found position to the end of the content.
// (We will break as the next newLine character will be reached.)
          for ( int i = contentPos ; i < fileContentConnectionsOrig . length ; i ++ )
          {
// Determining the charArrayCount and breaking the loop.
            if ( fileContentConnectionsOrig [ i ] == newLineChar )
            {
              charArrayCount = i - contentPos ;
              break ;
            }
            if ( fileContentConnectionsOrig [ i ] == zeroChar )
            {
              charArrayCount = 0 ;
              break ;
            }
          }
// Now we can initialize the character array in the correct size.
          content = new char [ charArrayCount ] ;
// Finally, we can copy the right part of the content into the returning char array.
          for ( int i = contentPos ; i < contentPos + charArrayCount ; i ++ )
          {
            content [ i - contentPos ] = fileContentConnectionsOrig [ i ] ;
          }
        }
      }
// Not in use any more.
      pos = 0 ;
      contentPos = 0 ;
      charArrayCount = 0 ;
      newLines = 0 ;
      toSearch = null ;
    }
// Returning of the requested content part.
    return content ;
  }
/*
** The following 4 gets the data of the database connection
** as described in the header content of getContentData.
*/
  protected final String getDbuser ( String dbtype , String connna )
  {
    return new String ( getContentData ( dbtype , connna , 0 ) ) ;
  }
  protected final String getDbpass ( String dbtype , String connna )
  {
// Yes, sadly it is a string..
    return new String ( getContentData ( dbtype , connna , 1 ) ) ;
  }
  protected final String getDriver ( String dbtype , String connna )
  {
    return new String ( getContentData ( dbtype , connna , 2 ) ) ;
  }
  protected final String getConnst ( String dbtype , String connna )
  {
    return new String ( getContentData ( dbtype , connna , 3 ) ) ;
  }
/*
** Gets the position of a given connection specified by the database type and the connection name.
** the potision of the right database type will be returned.
** In case of not found connection, the -1 will be returned.
*/
  protected final int getConnnaPos ( String dbtype , String connna )
  {
// This will be the position of the connection.
    int pos = - 1 ;
// We have to count the new line characters.
    int newLines = 0 ;
// To validate the current position.
    boolean innerBreak = false ;
// This is a temporary string only.
    String toSearch = newLineString + dbtype + newLineChar + connna + newLineChar ;
// The password and decrypted connections file content are missing.
    if ( isFileContentConnectionsOrigReady ( ) )
    {
      if ( connectionsHeader != null && appDateFormatForDisplaying != null && sep9 != null && messageLogApplicationInstanceInitialize != null )
      {
// Let's loop on the content of the connections data. (Not from the beginning since there are header informations.)
        for ( int i = connectionsHeader . length ( ) + appDateFormatForDisplaying . length ( ) + sep9 . length ( ) + messageLogApplicationInstanceInitialize . length ( ) + 1 - 1 ; i < fileContentConnectionsOrig . length - toSearch . length ( ) ; i ++ )
        {
// Counting the new lines, this is important.
          if ( fileContentConnectionsOrig [ i ] == newLineChar )
          {
            newLines ++ ;
          }
// Let it be false, this is the default value of this.
          innerBreak = false ;
// Now, char-by-char we will see that from this current position the temp string can be found or not..
          for ( int j = 0 ; j < toSearch . length ( ) ; j ++ )
          {
            if ( fileContentConnectionsOrig [ i + j ] != toSearch . charAt ( j ) )
            {
// In the first not mathed character the toSearch string is not found.
              innerBreak = true ;
              break ;
            }
          }
// If the connection is found that means immerBreak remains false and the newLineCharacters is the multipy of 6 plus 1!
          if ( ! innerBreak && newLines % 6 == 1 )
          {
// The position is the next character of i (toSearch starts with the newLine, we dont need this position, the next position is good for us.)
            pos = i + 1 ;
            break ;
          }
        }
      }
      else
      {
        systemexit ( "Error - one of these is null: connectionsHeader|appDateFormatForDisplaying|sep9|messageLogApplicationInstanceInitialize, getConnnaPos" ) ;
      }
    }
// Now returning of the pos.
// (Remains -1 if the connection has not been found.)
    return pos ;
  }
}