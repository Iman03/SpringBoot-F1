package com.iman.bootthymeleaff1.controller;

import com.iman.bootthymeleaff1.entity.Schedule;
import com.iman.bootthymeleaff1.entity.User;
import com.iman.bootthymeleaff1.repository.ScheduleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.ModelAndView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

@Controller
public class ScheduleController {

    @Autowired
    private ScheduleRepository scheduleRepository;

    // get 2022 schedule result
    @GetMapping("/schedule")
    public ModelAndView getResult() throws ParseException {
        ModelAndView mav = new ModelAndView("schedule-result");
        List<Schedule> scheduleList = scheduleRepository.findAllByDateBefore(new SimpleDateFormat("yyyy-MM-dd").parse("2023-01-01"));
        mav.addObject("results", scheduleList);
        User user = new User();
        mav.addObject("user", user);
        return mav;
    }

    // get 2023 schedule
    @GetMapping("/schedule2023")
    public ModelAndView getSchedule() throws ParseException {
        ModelAndView mav = new ModelAndView("schedule-2023");
        List<Schedule> scheduleList = scheduleRepository.findAllByDateAfter(new SimpleDateFormat("yyyy-MM-dd").parse("2023-01-01"));
        mav.addObject("results", scheduleList);
        User user = new User();
        mav.addObject("user", user);
        return mav;
    }
}
