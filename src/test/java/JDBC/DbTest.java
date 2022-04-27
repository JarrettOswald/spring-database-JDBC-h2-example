package JDBC;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.support.GenericApplicationContext;
import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class DbTest {
    private static final Logger logger = LoggerFactory.getLogger(DbTest.class);
    GenericApplicationContext context = new AnnotationConfigApplicationContext(Config.class);
    DataSource dataSource = context.getBean("dataSource", DataSource.class);
    Connection connection;

    @BeforeClass
    public void before() {
        try {
            connection = dataSource.getConnection();
        } catch (SQLException sqlException) {
            throw new RuntimeException(sqlException);
        }
    }

    @Test
    public void dataSourceIsNotNull() {
        Assert.assertNotNull(dataSource);
    }

    @Test
    public void dataSourceSelect() {
        try {
            PreparedStatement statement = connection.prepareStatement("SELECT * FROM singers");
            ResultSet resultSet = statement.executeQuery();
            int resultCount = 0;
            while (resultSet.next()) {
                resultCount++;
                String columnValue = resultSet.getString("singer_name");
                logger.info(columnValue);
            }
            Assert.assertEquals(resultCount, 2);
        } catch (SQLException ex) {
            Assert.fail(ex.toString());
        }
    }


    @Test
    public void dataSourceSelectById() {
        try {
            PreparedStatement statement = connection.prepareStatement("SELECT * FROM singers WHERE singer_id = '1'");
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                String actualValue = resultSet.getString("singer_name");
                Assert.assertEquals(actualValue, "Ivan Ivanov");
                logger.info(actualValue);
            }
        } catch (SQLException ex) {
            Assert.fail(ex.toString());
        }
    }


    @AfterClass
    public void after() {
        try {
            connection.close();
        } catch (SQLException sqlException) {
            throw new RuntimeException(sqlException);
        }
    }

}
