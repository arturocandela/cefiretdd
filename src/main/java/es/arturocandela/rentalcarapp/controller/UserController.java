package es.arturocandela.rentalcarapp.controller;

import com.google.gson.Gson;
import com.mysql.cj.MysqlConnection;
import es.arturocandela.rentalcarapp.model.AUser;
import es.arturocandela.rentalcarapp.service.DBConnection;
import es.arturocandela.rentalcarapp.service.NonValidConnectionException;
import es.arturocandela.rentalcarapp.usecase.RegisterUser;
import es.arturocandela.rentalcarapp.usecase.UserPersistanceException;

import javax.inject.Inject;
import java.sql.SQLException;
import java.util.HashMap;

public class UserController {

    @Inject
    DBConnection connection;

    @Inject
    RegisterUser registerUserCase;


    public String register(AUser user){

        try {
            user = registerUserCase.execute(user);
            connection.close();
        } catch (UserPersistanceException|SQLException $e) {
            try {
                connection.close();
            } catch (SQLException e){

            }

            return handleFailOnInsert();
        }

        return userResponse(user);
    }

    private String userResponse(AUser user){

        Gson gson = new Gson();
        HashMap<String,AUser> mapRes = new HashMap<>();
        mapRes.put("user",user);
        return gson.toJson(mapRes);

    }

    private String handleFailOnInsert() {

        Gson gson = new Gson();
        HashMap<String,String> mapRes = new HashMap<>();
        mapRes.put("Error","Algo ha fallado al guardar los datos");
        return gson.toJson(mapRes);

    }


    /*protected void createConnection(String host, String user, String password, String db) throws NonValidConnectionException
    {
        return new MysqlConnection();
    }*/
}
