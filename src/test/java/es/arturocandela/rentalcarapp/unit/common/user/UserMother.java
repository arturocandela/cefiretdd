package es.arturocandela.rentalcarapp.unit.common.user;

import es.arturocandela.rentalcarapp.model.User;

public class UserMother {

    public static User WithId()
    {
        User user = new User("Pepe",18,"pepe@correo.com","Pepito");
        user.setId(1);
        return user;
    }

    public static User generateAdult()
    {
        return new User("Pepe",18,"pepe@correo.com","Pepito");
    }

}
