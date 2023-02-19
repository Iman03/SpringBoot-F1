package com.iman.bootthymeleaff1.controller;

import com.iman.bootthymeleaff1.entity.Order;
import com.iman.bootthymeleaff1.entity.Tickets;
import com.iman.bootthymeleaff1.entity.User;
import com.iman.bootthymeleaff1.repository.OrderRepository;
import com.iman.bootthymeleaff1.repository.TicketsRepository;
import com.iman.bootthymeleaff1.repository.UserRepository;
import com.iman.bootthymeleaff1.token.JwtToken;
import jakarta.security.auth.message.AuthException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.servlet.ModelAndView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

@Controller
public class TicketsController {
    @Autowired
    private TicketsRepository ticketsRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private OrderRepository orderRepository;

    @GetMapping({"/tickets"})
    public ModelAndView showTicketPage(){
        ModelAndView mav = new ModelAndView("ticket-seats");

        User user = new User();
        mav.addObject("user", user);
        return mav;
    }

    // show seats
    @GetMapping({"/tickets/{date}","/tickets/"})
    public ModelAndView showSeats(@PathVariable (required = false) String date) throws ParseException {
        ModelAndView mav = new ModelAndView("ticket-seats");
        // when not select a date
        if (date == null){
            Tickets tickets = new Tickets();
            tickets.setMemo("Please select a date!");
            mav.addObject("selectDate", tickets);
        } else {
            List<Tickets> ticketsList = ticketsRepository.findTicketsByDate(new SimpleDateFormat("yyyy-MM-dd").parse(date));
            // show date ticket info
            if (ticketsList.size() > 0){
                mav.addObject("tickets", ticketsList);
            } else {
                // show no race that date
                Tickets tickets = new Tickets();
                tickets.setDate(new SimpleDateFormat("yyyy-MM-dd").parse(date));
                mav.addObject("noRace", tickets);
            }
        }
        User user = new User();
        mav.addObject("user", user);
        return mav;
    }

    // when press confirm button
    @GetMapping("/tickets/reserve/{info}")
    public ModelAndView loginForTicket(@PathVariable String info){
        ModelAndView mav = new ModelAndView("loginForTickets");
        User user = new User();
        mav.addObject("user", user);
        Integer total = Integer.parseInt(info.substring(info.indexOf("total=")+6));
        String seat = info.subSequence((info.indexOf("seat=")+5), info.indexOf("&")).toString();

        mav.addObject("seat", seat);
        mav.addObject("total", total);

        return mav;
    }

    // when press Reserve button, finish ordering
    @GetMapping("/ticketsReserve/{info}")
    public ModelAndView ticketsConfirm(@PathVariable String info){
        ModelAndView mav = new ModelAndView("reserving");
        // get user and send user info into member page
        User user = userRepository.findById(Integer.parseInt(info.substring(info.indexOf("&")+8))).get();
        mav.addObject("userInfo", user);

        // get seats
        String seatString = info.substring(5, info.indexOf("&"));
        String[] seats = seatString.split(",");

        // finish ticket ordering with seats
        Integer total = 0;
        for (String s : seats) {
            Tickets tickets = ticketsRepository.findTicketsBySeat(s);
            total += tickets.getPrice();
        }

        // insert order and get orderId back
        Order newOrder = new Order();
        newOrder.setUserId(user.getId());
        newOrder.setTotal(total);
        newOrder.setOrderDate(new Date());
        orderRepository.save(newOrder);

        // update tickets with orderId
        for (String s : seats) {
            Tickets updateTickets = ticketsRepository.findTicketsBySeat(s);
            updateTickets.setOrderId(newOrder.getId());
            ticketsRepository.save(updateTickets);
        }

        return mav;
    }
}
