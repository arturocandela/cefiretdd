package es.arturocandela.rentalcarapp.service;

import es.arturocandela.rentalcarapp.model.Booking;

public interface IConfirmationNotifier {

    void send(Booking booking) throws NotificationFailedException;

}
