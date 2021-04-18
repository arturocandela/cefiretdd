package es.arturocandela.rentalcarapp.service;

import es.arturocandela.rentalcarapp.model.implementation.Booking;

public interface IConfirmationNotifier {

    void send(Booking booking);

}
