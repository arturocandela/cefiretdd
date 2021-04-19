package es.arturocandela.rentalcarapp.service.implementation;

import es.arturocandela.rentalcarapp.service.DBConnection;
import es.arturocandela.rentalcarapp.service.InsertException;
import es.arturocandela.rentalcarapp.service.NonValidConnectionException;

import java.sql.*;
import java.util.Map;

/**
 * La clase gestiona la conexión con la base de datos
 *
 * @TODO Se debe revisar las conexiones, la obertura y el cierre. Esta hecho de pruebas, pero no para ejecucion.
 */
public class MySQLDBConnection implements DBConnection {

    private Connection conn = null;

    /**
     * Tries to create a connection to MySQL Database
     *
     * @param host Host where is the database
     * @param dbUser User of the Database
     * @param dbPass Password
     * @param dbName Database Name
     * @param dbPort Database Port
     * @throws NonValidConnectionException
     */
    public MySQLDBConnection(String host, String dbUser, String dbPass, String dbName, int dbPort) throws NonValidConnectionException
    {

        String mysqlConnUrl = String.format("jdbc:mysql://%s:%d/%s",host,dbPort,dbName);

        try
        {
            conn = DriverManager.getConnection(mysqlConnUrl,dbUser,dbPass);

        } catch (SQLTimeoutException e){
            throw new NonValidConnectionException(e);
        } catch (SQLException e)
        {
            throw new NonValidConnectionException(e);
        }
        catch (Exception e)
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
     * @throws NonValidConnectionException
     */
    public MySQLDBConnection(String host, String dbUser, String dbPass, String dbName) throws NonValidConnectionException
    {
        this(host,dbUser,dbPass,dbName,3306);
    }

    /// <summary>
    /// Runs and insert sql statment in the database
    /// </summary>
    /// <param name="sql">SQL to insert</param>
    /// <returns>first ID of the inserted registry</returns>
    /// <exception cref="InsertException">If there is a problem with the insert</exception>
    public int insert(String sql) throws InsertException
    {
        Statement statement = null;
        try
        {
            statement = conn.createStatement();
            statement.execute(sql,Statement.RETURN_GENERATED_KEYS);

            int autoIncKeyFromApi = -1;

            ResultSet rs = statement.getGeneratedKeys();

            if (rs.next()) {
                return rs.getInt(1);
            } else {

                throw new InsertException("I was unable to get the inserted id");
            }

        } catch(Exception e)
        {
            if (statement != null){
                try {
                    statement.close();
                } catch (SQLException throwables) {
                    throw  new InsertException(throwables.getLocalizedMessage());
                }
            }

            throw new InsertException(e);
        }

    }

    public Map<String,Object> query(String sql)
    {
        return null;
    }

    public void close() throws SQLException
    {
        //TODO: Si conn es null fallará ()
        conn.close();
    }
}
