package es.arturocandela.rentalcarapp.service;

import es.arturocandela.rentalcarapp.model.implementation.Car;

public abstract class CarFinder {

    public abstract Car find(int id);
}
