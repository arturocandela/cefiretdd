package es.arturocandela.rentalcarapp.usecase;

import es.arturocandela.rentalcarapp.model.User;
import es.arturocandela.rentalcarapp.service.DBConnection;
import es.arturocandela.rentalcarapp.service.InsertException;

import javax.inject.Inject;

public class RegisterUser {

    private DBConnection conn;

    @Inject
    public RegisterUser(DBConnection conn)
    {
        this.conn = conn;
    }

    public User execute(User user) throws InsertException
    {
        String insertSQL =
                String.format("INSERT INTO users(firstname,age,mail,password) " +
                        "values (\"%s\",%d,\"%s\",\"%s\")",user.getName(),user.getAge(),user.getMail(),user.getPassword());

            user.setId(conn.insert(insertSQL));
            return user;

    }
}
