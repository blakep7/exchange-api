package com.horizon.exchangeapi

import com.horizon.exchangeapi.ExchangeApiApp.logger
import com.mchange.v2.c3p0.ComboPooledDataSource
import slick.util.AsyncExecutor
import slick.jdbc.PostgresProfile.api.Database

class TestDBConnection {
  ExchConfig.load() // get config file, normally in /etc/horizon/exchange/config.json
  ExchConfig.getHostAndPort match {
    case (h, p) => ExchangeApi.serviceHost = h;
      ExchangeApi.servicePort = p
  }
  
  // Load the db backend. The db access info must be in config.json
  var cpds: ComboPooledDataSource = _
  cpds = new ComboPooledDataSource
  cpds.setAcquireIncrement(ExchConfig.getInt("api.db.acquireIncrement"))
  cpds.setDriverClass(ExchConfig.getString("api.db.driverClass")) //loads the jdbc driver
  cpds.setIdleConnectionTestPeriod(ExchConfig.getInt("api.db.idleConnectionTestPeriod"))
  cpds.setInitialPoolSize(ExchConfig.getInt("api.db.initialPoolSize"))
  cpds.setJdbcUrl(ExchConfig.getString("api.db.jdbcUrl"))
  cpds.setMaxConnectionAge(ExchConfig.getInt("api.db.maxConnectionAge"))
  cpds.setMaxIdleTimeExcessConnections(ExchConfig.getInt("api.db.maxIdleTimeExcessConnections"))
  cpds.setMaxPoolSize(ExchConfig.getInt("api.db.maxPoolSize"))
  cpds.setMaxStatementsPerConnection(ExchConfig.getInt("api.db.maxStatementsPerConnection"))
  cpds.setMinPoolSize(ExchConfig.getInt("api.db.minPoolSize"))
  cpds.setNumHelperThreads(ExchConfig.getInt("api.db.numHelperThreads"))
  cpds.setPassword(ExchConfig.getString("api.db.password"))
  cpds.setTestConnectionOnCheckin(ExchConfig.getBoolean("api.db.testConnectionOnCheckin"))
  cpds.setUser(ExchConfig.getString("api.db.user"))
  
  // maxConnections, maxThreads, and minThreads should all be the same size.
  val db: Database =
    if (cpds != null) {
      Database.forDataSource(ds = cpds,
                             executor = AsyncExecutor(name = "ExchangeExecutor",
                                                      numThreads = ExchConfig.getInt("api.db.maxPoolSize"),
                                                      queueSize = ExchConfig.getInt("api.db.queueSize")),
                             maxConnections = Option(ExchConfig.getInt("api.db.maxPoolSize")))
      
    }
    else
      null
  
  def getDb: Database = db
}
