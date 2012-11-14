// vim: set ts=2 sw=2 expandtab:

/*
 * Credits: Rewokring of TwitBase code from HBase In Action
 *
 * https://github.com/hbaseinaction/twitbase
 *
 */

import java.io.IOException;
import java.util.UUID;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.HBaseConfiguration;
import org.apache.hadoop.hbase.HColumnDescriptor;
import org.apache.hadoop.hbase.HTableDescriptor;
import org.apache.hadoop.hbase.client.HTableInterface;
import org.apache.hadoop.hbase.client.HTablePool;
import org.apache.hadoop.hbase.client.Get;
import org.apache.hadoop.hbase.client.HBaseAdmin;
import org.apache.hadoop.hbase.client.HTable;
import org.apache.hadoop.hbase.client.Put;
import org.apache.hadoop.hbase.client.Result;
import org.apache.hadoop.hbase.client.ResultScanner;
import org.apache.hadoop.hbase.client.Scan;
import org.apache.hadoop.hbase.util.Bytes;
import org.apache.hadoop.hbase.TableExistsException;
import org.apache.log4j.Logger;

public class HBaseExample {
  private static final Logger log = Logger.getLogger(UsersHBase.class);
  /*
   * Change the following, as appropriate, to the hostname or
   * IP address of your server. If using hostname, make sure
   * that name resolves to an IP address, e.g. has an entry in
   * your /etc/hosts file
   */
  private static final String serverName = "kingfisher";

  public static void main(String args []) throws Exception {

    Configuration config = HBaseConfiguration.create();
    config.set("hbase.zookeeper.quorum", serverName);
    HBaseAdmin admin = new HBaseAdmin(config);

    HTablePool pool = new HTablePool();
    HTableInterface usersTable;
    if (admin.tableExists(UsersHBase.TABLE_NAME)) {
      log.debug(UsersHBase.TABLE_NAME + " table already exists.");
      usersTable = pool.getTable(UsersHBase.TABLE_NAME);
    } else {
      log.debug("Creating " + UsersHBase.TABLE_NAME + "table ...");
      HTableDescriptor desc = new HTableDescriptor(UsersHBase.TABLE_NAME);
      HColumnDescriptor c = new HColumnDescriptor(UsersHBase.INFO_FAM);
      desc.addFamily(c);
      admin.createTable(desc);
      log.debug(UsersHBase.TABLE_NAME + " table created.");
    }
  }
}
