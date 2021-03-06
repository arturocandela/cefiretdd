package es.arturocandela.rentalcarapp.service.implementation;

import es.arturocandela.rentalcarapp.service.DBConnection;
import es.arturocandela.rentalcarapp.service.InsertException;
import es.arturocandela.rentalcarapp.service.NonValidConnectionException;

import java.sql.*;
import java.util.Map;
import java.util.logging.Logger;

/**
 * This class manages a connection to MySQL Server Storage Provider
 */
public class MySQLDBConnection implements DBConnection {

    private final Connection conn;
    Logger logger = Logger.getLogger(this.getClass().getName());

    /**
     * Tries to create a connection to MySQL Database
     *
     * @param host Host where is the database
     * @param dbUser User of the Database
     * @param dbPass Password
     * @param dbName Database Name
     * @param dbPort Database Port
     * @throws NonValidConnectionException If there is an error trying to connect
     */
    public MySQLDBConnection(String host, String dbUser, String dbPass, String dbName, int dbPort) throws NonValidConnectionException
    {

        String mysqlConnUrl = String.format("jdbc:mysql://%s:%d/%s",host,dbPort,dbName);

        try
        {
            conn = DriverManager.getConnection(mysqlConnUrl,dbUser,dbPass);

        }  catch (SQLException e)
        {
            throw new NonValidConnectionException(e);
        }

    }

    /**
     * Tries to create a MySQL Database connection to standard port 3306
     *
     * @param host Host where is the database
     * @param dbUser User of the Database
     * @param dbPass Password
     * @param dbName Database Name
     * @throws NonValidConnectionException If there is an error trying to connect
     */
    public MySQLDBConnection(String host, String dbUser, String dbPass, String dbName) throws NonValidConnectionException
    {
        this(host,dbUser,dbPass,dbName,3306);
    }

    /// <summary>
    /// Runs and insert sql statement in the database
    /// </summary>
    /// <param name="sql">SQL to insert</param>
    /// <returns>first ID of the inserted registry</returns>
    /// <exception cref="InsertException">If there is a problem with the insert</exception>
    public int insert(String sql) throws InsertException
    {
        try {

            try(Statement statement = conn.createStatement()){

                statement.execute(sql,Statement.RETURN_GENERATED_KEYS);

                ResultSet rs = statement.getGeneratedKeys();

                if (rs.next()) {
                    int id = rs.getInt(1);
                    rs.close();
                    return id;
                } else {
                    throw new InsertException("I was unable to get the inserted id");
                }

            }

        } catch (SQLException e){
            throw new InsertException(e);
        }

    }

    public Map<String,Object> query(String sql)
    {
        return null;
    }

    public void close() throws SQLException
    {
        if (conn != null){
            conn.close();
        }

    }
}
