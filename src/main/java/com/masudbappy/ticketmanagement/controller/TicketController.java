package com.masudbappy.ticketmanagement.controller;

import com.masudbappy.ticketmanagement.aop.AdminTokenRequired;
import com.masudbappy.ticketmanagement.aop.CSRTokenRequired;
import com.masudbappy.ticketmanagement.aop.UserTokenRequired;
import com.masudbappy.ticketmanagement.model.User;
import com.masudbappy.ticketmanagement.repositories.TicketService;
import com.masudbappy.ticketmanagement.repositories.UserService;
import com.masudbappy.ticketmanagement.util.Util;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

@RestController
@RequestMapping("/ticket")
public class TicketController {

    @Autowired
    TicketService ticketSevice;

    @Autowired
    UserService userSevice;

    @ResponseBody
    @AdminTokenRequired
    @RequestMapping("/by/admin")
    public <T> T getAllTickets(HttpServletRequest request) {

        return (T) Util.getSuccessResult(ticketSevice.getAllTickets());
    }

    @ResponseBody
    @CSRTokenRequired
    @RequestMapping("/by/csr")
    public <T> T getAllTicketsByCSR(HttpServletRequest request) {

        return (T) Util.getSuccessResult(ticketSevice.getAllTickets());
    }

    @ResponseBody
    @UserTokenRequired
    @RequestMapping("/{ticketid}")
    public <T> T getTicket(
            @PathVariable("ticketid") final Integer ticketid,
            HttpServletRequest request
    ) {

        return (T) Util.getSuccessResult(ticketSevice.getTicket(ticketid));
    }


    /*
     * Rule:
     * 		Only user can create a ticket
     *
     */
    @SuppressWarnings("unchecked")
    @ResponseBody
    @UserTokenRequired
    @RequestMapping(value = "", method = RequestMethod.POST)
    public <T> T addTicket(
            @RequestParam(value="content") String content,
            HttpServletRequest request
    ) {

        User user = userSevice.getUserByToken(request.getHeader("token"));
        ticketSevice.addTicket(user.getUserid(), content, 2, 1);

        return Util.getSuccessResult();
    }

    @ResponseBody
    @RequestMapping("/my/tickets")
    public Map<String, Object> getMyTickets(
            HttpServletRequest request
    ) {

        User user = userSevice.getUserByToken(request.getHeader("token"));

        if(user == null){
            return Util.getUserNotAvailableError();
        }

        return Util.getSuccessResult(ticketSevice.getMyTickets(user.getUserid()));
    }

    @ResponseBody
    @UserTokenRequired
    @RequestMapping(value = "/{ticketid}", method =  RequestMethod.DELETE)
    public <T> T deleteTicketByUser (
            @PathVariable("ticketid") final Integer ticketid,

            HttpServletRequest request
    ) {

        User user = userSevice.getUserByToken(request.getHeader("token"));

        ticketSevice.deleteMyTicket(user.getUserid(), ticketid);

        return Util.getSuccessResult();
    }


    @ResponseBody
    @RequestMapping(value = "/{ticketid}", method =  RequestMethod.PUT)
    public <T> T updateTicketByCustomer (
            @PathVariable("ticketid") final Integer ticketid,

            @RequestParam(value="content") String content,

            HttpServletRequest request
    ) {

        User user = userSevice.getUserByToken(request.getHeader("token"));

        if(user == null){
            return Util.getUserNotAvailableError();
        }

        ticketSevice.updateTicket(ticketid, content, 2, 1);

        return Util.getSuccessResult();
    }

    @ResponseBody
    @CSRTokenRequired
    @RequestMapping(value = "/by/csr", method =  RequestMethod.PUT)
    public <T> T updateTicketByCSR (
            @RequestParam("ticketid") final Integer ticketid,

            @RequestParam(value="content") String content,
            @RequestParam(value="severity") Integer severity,
            @RequestParam(value="status") Integer status,

            HttpServletRequest request
    ) {

        ticketSevice.updateTicket(ticketid, content, severity, status);

        return Util.getSuccessResult();
    }

    @ResponseBody
    @AdminTokenRequired
    @RequestMapping(value = "/by/admin", method =  RequestMethod.PUT)
    public <T> T updateTicketByAdmin (
            @RequestParam("ticketid") final Integer ticketid,

            @RequestParam(value="content") String content,
            @RequestParam(value="severity") Integer severity,
            @RequestParam(value="status") Integer status,

            HttpServletRequest request
    ) {

        ticketSevice.updateTicket(ticketid, content, severity, status);

        return Util.getSuccessResult();
    }

    @ResponseBody
    @CSRTokenRequired
    @RequestMapping(value = "/by/csr", method =  RequestMethod.DELETE)
    public <T> T deleteTicketsByCSR (
            @RequestParam("ticketids") final String ticketids,

            HttpServletRequest request
    ) {

        User user = userSevice.getUserByToken(request.getHeader("token"));
        ticketSevice.deleteTickets(user, ticketids);

        return Util.getSuccessResult();
    }

    @ResponseBody
    @AdminTokenRequired
    @RequestMapping(value = "/by/admin", method =  RequestMethod.DELETE)
    public <T> T deleteTicketsByAdmin (
            @RequestParam("ticketids") final String ticketids,
            HttpServletRequest request
    ) {

        User user = userSevice.getUserByToken(request.getHeader("token"));

        ticketSevice.deleteTickets(user, ticketids);

        return Util.getSuccessResult();
    }
}
