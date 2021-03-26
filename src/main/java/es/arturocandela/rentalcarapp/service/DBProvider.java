package es.arturocandela.rentalcarapp.service;

import dagger.Module;
import dagger.Provides;
import es.arturocandela.rentalcarapp.service.implementation.MySQLDBConnection;

import java.util.logging.Logger;

@Module
public class DBProvider {

    @Provides
    public DBConnection MySQLDatabaseConnetion()
    {
        try  {
            return new MySQLDBConnection("localhost","root","root","booking");
        } catch (NonValidConnectionException e){
            Logger.getLogger(MySQLDBConnection.class.getName(),e.getLocalizedMessage());
            return null;
        }

    }

}
