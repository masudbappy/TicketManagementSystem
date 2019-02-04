package com.masudbappy.ticketmanagement.repositories;

import com.masudbappy.ticketmanagement.model.Ticket;
import com.masudbappy.ticketmanagement.model.User;

import java.util.List;

public interface TicketService {
    List<Ticket> getAllTickets();

    List<Ticket> getMyTickets(Integer creatorid);

    void deleteMyTicket(Integer userid, Integer ticketid);

    Ticket getTicket(Integer creatorid, Integer ticketid);

    Ticket getTicket(Integer ticketid);

    void addTicket(Integer creatorid, String content, Integer severity, Integer status);

    void updateTicket(Integer ticketid, String content, Integer severity, Integer status);

    void deleteTickets(User user, String ticketids);

}
