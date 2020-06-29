package com.ssm.core;
import com.mysql.jdbc.AbandonedConnectionCleanupThread;
import org.apache.commons.dbcp.BasicDataSource;
import java.sql.DriverManager;
import java.sql.SQLException;

public class XBasicDataSource extends BasicDataSource {
    @Override
    public synchronized void close() throws SQLException {
        //以下两句代码分别对应两个资源的关闭
        DriverManager.deregisterDriver(DriverManager.getDriver(getUrl()));
        AbandonedConnectionCleanupThread.checkedShutdown();
        super.close();
    }
}