package es.arturocandela.rentalcarapp.service;

import java.sql.*;
import java.util.Map;

/**
 * La clase gestiona la conexión con la base de datos
 *
 * @TODO Se debe revisar las conexiones, la obertura y el cierre. Esta hecho de pruebas, pero no para ejecucion.
 */
public interface DBConnection {

    public int insert(String sql) throws InsertException;

    public Map<String,Object> query(String sql);

    public void close() throws SQLException;

}
