// Databricks notebook source
dbutils.widgets.text("azuresql-servername", "servername")
dbutils.widgets.text("azuresql-finaltable", "[dbo].[rawdata]")
dbutils.widgets.text("assert-events-per-second", "900", "Assert min events per second (computed over 1 min windows)")
dbutils.widgets.text("assert-latency-milliseconds", "15000", "Assert max latency in milliseconds (averaged over 1 min windows)")

// COMMAND ----------
import com.microsoft.azure.sqldb.spark.bulkcopy.BulkCopyMetadata
import com.microsoft.azure.sqldb.spark.config.Config
import com.microsoft.azure.sqldb.spark.connect._

val dbConfig = Config(Map(
  "url"               -> (dbutils.widgets.get("azuresql-servername") + ".database.windows.net"),
  "user"              -> "serveradmin",
  "password"          -> dbutils.secrets.get(scope = "MAIN", key = "azuresql-pass"),
  "databaseName"      -> "streaming",
  "dbTable"           -> dbutils.widgets.get("azuresql-finaltable")
))

val data = spark
  .read
  .sqlDB(dbConfig)

// COMMAND ----------
import java.util.UUID.randomUUID

val tempTable = "tempresult_" + randomUUID().toString.replace("-","_")

data
  .createOrReplaceGlobalTempView(tempTable)

// COMMAND ----------

dbutils.notebook.run("verify-common", 0, Map(
    "input-table" -> (spark.conf.get("spark.sql.globalTempDatabase") + "." + tempTable),
    "assert-events-per-second" -> dbutils.widgets.get("assert-events-per-second"),
    "assert-latency-milliseconds" -> dbutils.widgets.get("assert-latency-milliseconds")
))
