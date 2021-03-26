package es.arturocandela.rentalcarapp.model;

public abstract class AUser {

    public AUser(String name, int age, String mail, String password) throws  IllegalArgumentException{

    }

    public abstract boolean isAnAdult();

    public abstract int getId();

    public abstract void setId (int id);

    public abstract String getName();

    public abstract int getAge();

    public abstract String getMail() ;

    public abstract String getPassword();
}
