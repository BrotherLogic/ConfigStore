package com.brotherlogic.configstore.unit.mongo;

import java.io.IOException;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.brotherlogic.configstore.ConfigStore;
import com.brotherlogic.configstore.mongo.MongoConfig;
import com.mongodb.Mongo;

/**
 * Tests the mongo storage thingy
 * 
 * @author simon
 * 
 */
public class MongoStoreTest
{
   /** The store to use for tests */
   private ConfigStore conf;

   /**
    * Prepares for testing
    * 
    * @throws IOException
    *            if something goes wrong
    */
   @Before
   public void deleteDB() throws IOException
   {
      Mongo m = new Mongo();
      m.dropDatabase(MongoConfig.TEST_DB_NAME);

      MongoConfig mc = new MongoConfig();
      mc.setForTest();
      conf = mc;
   }

   /**
    * Tests that we can store and retrieve from the mongo
    * 
    * @throws IOException
    *            if something goes wrong
    */
   @Test
   public void testStoreAndRetreive() throws IOException
   {
      String key = "test.key";
      String data = "This is some test data";

      conf.store(key, data.getBytes());

      byte[] dateRet = conf.get(key);
      String strRet = new String(dateRet);

      Assert.assertEquals("Mismatch in retrieved data: " + strRet + " vs " + data, strRet, data);
   }

   @Test
   public void testOverwrite() throws IOException
   {
      String key = "test.key";
      String data = "Test data";

      conf.store(key, data.getBytes());

      String data2 = "Test data 2";
      conf.store(key, data2.getBytes());

      byte[] dateRet = conf.get(key);
      String strRet = new String(dateRet);

      Assert.assertEquals("Data not overwritten", data2, strRet);
   }

   @Test
   public void testEmpty() throws IOException
   {
      byte[] dataRet = conf.get("blah");
      Assert.assertTrue("Data Ret has data? " + dataRet.length, dataRet.length == 0);
   }
}
