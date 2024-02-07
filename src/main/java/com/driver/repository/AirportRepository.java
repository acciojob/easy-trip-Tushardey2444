package com.driver.repository;

import com.driver.model.Airport;
import com.driver.model.Flight;
import com.driver.model.Passenger;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Repository
public class AirportRepository {
    private final HashMap<String, Airport> AirportDb=new HashMap<>();
    private final HashMap<Integer, Flight> FlightDb=new HashMap<>();
    private final HashMap<Integer, Passenger> PassengerDb=new HashMap<>();
    private final HashMap<Flight,List<Passenger>> flightPassengerDb=new HashMap<>();
    private final HashMap<Passenger,Integer> passengerBookingDb=new HashMap<>();

    public void addAirport(Airport airport){
        String name=airport.getAirportName();
        AirportDb.put(name,airport);
    }
    public Airport getAirport(String airportName){
        return AirportDb.getOrDefault(airportName,null);
    }
    public Flight getFlight(Integer flightId){
        return FlightDb.getOrDefault(flightId,null);
    }
    public Passenger getPassenger(Integer passengerId){
        return PassengerDb.getOrDefault(passengerId,null);
    }
    public List<Airport> getAllAirports(){
        return new ArrayList<>(AirportDb.values());
    }
    public List<Flight> getAllFlights(){
        return new ArrayList<>(FlightDb.values());
    }
    public List<Passenger> getAllPassengers(Flight flight){
        return flightPassengerDb.getOrDefault(flight,null);
    }
    public String bookAFlight(Flight flight,Passenger passenger){
        if(!flightPassengerDb.containsKey(flight)){
            flightPassengerDb.put(flight,new ArrayList<>());
        }
        passengerBookingDb.put(passenger,passengerBookingDb.getOrDefault(passenger,0)+1);
        flightPassengerDb.get(flight).add(passenger);
        return "SUCCESS";
    }
    public String CancelATicket(Flight flight,List<Passenger> passengers){
        flightPassengerDb.put(flight,passengers);
        return "SUCCESS";
    }
    public int getPassengerBookingCount(Passenger passenger){
        return passengerBookingDb.getOrDefault(passenger,0);
    }

    public String addFlight(Flight flight) {
        FlightDb.put(flight.getFlightId(),flight);
        flightPassengerDb.put(flight,new ArrayList<>());
        return "SUCCESS";
    }
    public String addPassenger(Passenger passenger) {
        passengerBookingDb.put(passenger,0);
        PassengerDb.put(passenger.getPassengerId(),passenger);
        return "SUCCESS";
    }

}
