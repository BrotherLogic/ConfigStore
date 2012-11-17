package com.brotherlogic.configstore;

import java.io.IOException;
import java.io.InputStream;
import java.io.PrintStream;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.brotherlogic.configstore.mongo.MongoConfig;

public class Front extends HttpServlet
{
   Logger logger = Logger.getLogger(this.getClass().getName());

   ConfigStore store = null;

   private ConfigStore getConfigStore()
   {
      if (store == null)
         store = new MongoConfig();
      return store;
   }

   @Override
   protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException,
         IOException
   {
      resp.setContentType("application/octet-stream");
      String key = req.getParameter("key");
      byte[] content = getConfigStore().get(key);

      logger.log(Level.INFO, "Getting " + key);

      PrintStream bos = new PrintStream(resp.getOutputStream());
      bos.write(content);
      bos.close();
   }

   @Override
   protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws ServletException,
         IOException
   {
      // Get the key
      String key = req.getParameter("key");
      logger.log(Level.INFO, "Putting " + key);
      InputStream is = req.getInputStream();
      byte[] inputData = new byte[is.available()];
      is.read(inputData);
      is.close();

      getConfigStore().store(key, inputData);
   }

}
