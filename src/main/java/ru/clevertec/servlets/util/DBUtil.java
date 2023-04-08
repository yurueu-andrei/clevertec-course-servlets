package ru.clevertec.servlets.util;

import liquibase.Contexts;
import liquibase.LabelExpression;
import liquibase.Liquibase;
import liquibase.database.Database;
import liquibase.database.DatabaseFactory;
import liquibase.database.jvm.JdbcConnection;
import liquibase.resource.ClassLoaderResourceAccessor;
import lombok.SneakyThrows;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBUtil {
    private final String url;
    private final String user;
    private final String password;
    private final boolean ddlAutoGeneration;

    private static DBUtil INSTANCE;

    @SneakyThrows
    private DBUtil() {
        YamlParser yamlParser = new YamlParser();
        String driver = yamlParser.getYamlValue("postgres.driver");
        Class.forName(driver);
        this.url = yamlParser.getYamlValue("postgres.url");
        this.user = yamlParser.getYamlValue("postgres.user");
        this.password = yamlParser.getYamlValue("postgres.password");
        this.ddlAutoGeneration = Boolean.parseBoolean(yamlParser.getYamlValue("database.initialize"));
    }

    public static DBUtil getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new DBUtil();
        }
        return INSTANCE;
    }

    public Connection getConnection() throws SQLException {
        return DriverManager.getConnection(url, user, password);
    }

    @SneakyThrows
    public void migration() {
        if (ddlAutoGeneration) {
            Connection connection = getConnection();
            Database database = DatabaseFactory.getInstance()
                    .findCorrectDatabaseImplementation(new JdbcConnection(connection));
            Liquibase liquibase = new Liquibase
                    ("databases\\cheque\\changelog.xml",
                            new ClassLoaderResourceAccessor(),
                            database
                    );
            liquibase.update(new Contexts(), new LabelExpression());
        }
    }
}
