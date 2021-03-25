package es.arturocandela.rentalcarapp.usecase;

import es.arturocandela.rentalcarapp.model.implementation.User;
import es.arturocandela.rentalcarapp.service.DBConnection;
import es.arturocandela.rentalcarapp.service.InsertException;

public class RegisterUser {

    private DBConnection conn;

    public RegisterUser(DBConnection conn)
    {
        this.conn = conn;
    }

    public User execute(User user) throws UserPersistanceException
    {
        String insertSQL =
                String.format("INSERT INTO users(firstname,age,mail,password) " +
                        "values (\"%s\",%d,\"%s\",\"%s\")",user.getName(),user.getAge(),user.getMail(),user.getPassword());
        try {
            user.setId(conn.insert(insertSQL));
            return user;
        } catch (InsertException e)
        {
            throw new UserPersistanceException(e);
        }

    }
}
