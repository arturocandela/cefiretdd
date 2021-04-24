package es.arturocandela.rentalcarapp.unit.common.user;

import es.arturocandela.rentalcarapp.model.User;

public class UserMother {

    static User generateAdult()
    {
        return new User("Pepe",18,"pepe@correo.com","Pepito");
    }

}
