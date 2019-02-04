package com.masudbappy.ticketmanagement.repositories;

public interface SecurityService {
    String createToken(String subject, long ttlMillis);

    String getSubject(String token);
}
