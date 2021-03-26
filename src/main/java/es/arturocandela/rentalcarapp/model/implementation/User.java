package es.arturocandela.rentalcarapp.model.implementation;

import es.arturocandela.rentalcarapp.model.AUser;

public class User extends AUser {

    private static final int LEGAL_AGE_FOR_DRIVING=18;
    private static final int MINIMUM_USER_AGE=6;

    private int id;
    private String name;
    private int age;
    private String mail;
    private String password;

    public User(String name, int age, String mail, String password) throws  IllegalArgumentException{
        super(name,age,mail,password);

        if (age < MINIMUM_USER_AGE){
            throw new IllegalArgumentException("The age for using this platform should be over "+MINIMUM_USER_AGE);


        }

        this.name = name;
        this.age = age;
        this.mail = mail;
        this.password = password;
    }

    public boolean isAnAdult()
    {
        return age >= LEGAL_AGE_FOR_DRIVING;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public int getAge() {
        return age;
    }

    public String getMail() {
        return mail;
    }

    public String getPassword() {
        return password;
    }

}
