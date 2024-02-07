package com.driver.service;

import com.driver.model.Airport;
import com.driver.model.City;
import com.driver.model.Flight;
import com.driver.model.Passenger;
import com.driver.repository.AirportRepository;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class AirportService {
    AirportRepository airportRepository=new AirportRepository();

    public void addAirport(Airport airport){
        airportRepository.addAirport(airport);
    }
    public String getLargestAirportName(){
        List<Airport> Airports=airportRepository.getAllAirports();
        String airportName="";
        int max=0;
        for(Airport airport:Airports){
            if(airport.getNoOfTerminals()>max){
                max=airport.getNoOfTerminals();
                airportName=airport.getAirportName();
            }else if(airport.getNoOfTerminals()==max){
                if(airportName.compareTo(airport.getAirportName())>0){
                    airportName=airport.getAirportName();
                }
            }
        }
        return airportName;
    }
    public double getShortestDurationOfPossibleBetweenTwoCities(City fromCity, City toCity){
        List<Flight> flights=airportRepository.getAllFlights();
        double duration=Double.MAX_VALUE;
        for (Flight flight:flights){
            if(fromCity.equals(flight.getFromCity()) && toCity.equals(flight.getToCity()) && duration>flight.getDuration()){
                duration=flight.getDuration();
            }
        }
        return duration==Double.MAX_VALUE?-1:duration;
    }
    public int getNumberOfPeopleOn(Date date, String airportName){
        int peoples=0;
        List<Flight> flights=airportRepository.getAllFlights();
        Airport airport=airportRepository.getAirport(airportName);
        if(airport==null){
            return 0;
        }
        for(Flight flight:flights){
            if(flight.getFlightDate().equals(date) && (flight.getFromCity().equals(airport.getCity()) || flight.getToCity().equals(airport.getCity()))){
                peoples+=airportRepository.getAllPassengers(flight).size();
            }
        }
        return peoples;
    }

    public int calculateFlightFare(Integer flightId) {
        Flight flight=airportRepository.getFlight(flightId);
        int passengerListSize=airportRepository.getAllPassengers(flight).size();
        return 3000+passengerListSize*50;
    }

    public String bookATicket(Integer flightId, Integer passengerId) {
        Flight flight=airportRepository.getFlight(flightId);
        List<Passenger> passengerListSize=airportRepository.getAllPassengers(flight);
        if(passengerListSize.size()>=flight.getMaxCapacity()){
            return "FAILURE";
        }
        for(Passenger passenger:passengerListSize){
            if(passenger.getPassengerId()==passengerId){
                return "FAILURE";
            }
        }
        Passenger passenger=airportRepository.getPassenger(passengerId);
        return airportRepository.bookAFlight(flight,passenger);
    }

    public String cancelATicket(Integer flightId, Integer passengerId) {
        Flight flight=airportRepository.getFlight(flightId);
        if(flight==null){
            return "FAILURE";
        }
        Passenger passengerBooked=null;

        List<Passenger> passengers=airportRepository.getAllPassengers(flight);
        for(Passenger passenger:passengers){
            if(passenger.getPassengerId()==passengerId){
                passengerBooked=passenger;
                passengers.remove(passenger);
                break;
            }
        }
        if(passengerBooked==null){
            return "FAILURE";
        }
        return airportRepository.CancelATicket(flight,passengers);
    }

    public int countOfBookingsDoneByPassengerAllCombined(Integer passengerId) {
        Passenger passenger=airportRepository.getPassenger(passengerId);
        return airportRepository.getPassengerBookingCount(passenger);
    }

    public String addFlight(Flight flight) {
        return airportRepository.addFlight(flight);
    }

    public String getAirportNameFromFlightId(Integer flightId) {
        Flight flight=airportRepository.getFlight(flightId);
        if(flight==null){
            return null;
        }
        Airport airport=null;
        List<Airport> airports=airportRepository.getAllAirports();

        for(Airport airport1:airports){
            if(airport1.getCity().equals(flight.getFromCity())){
                airport=airport1;
                break;
            }
        }
        return airport==null?null:airport.getAirportName();
    }

    public int calculateRevenueOfAFlight(Integer flightId) {
        Flight flight=airportRepository.getFlight(flightId);
        if(flight==null){
            return 0;
        }
        int totalRevenue=0;
        int passengersListSize=airportRepository.getAllPassengers(flight).size();

        for(int i=0;i<passengersListSize;i++){
            totalRevenue+=(3000+i*50);
        }
        return totalRevenue;
    }

    public String addPassenger(Passenger passenger) {
        return airportRepository.addPassenger(passenger);
    }

}
