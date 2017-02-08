package com.burr.benjamin.controllers;

import com.burr.benjamin.Services.UserRepository;
import com.burr.benjamin.entities.User;
import com.burr.benjamin.utilities.PasswordStorage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpSession;

/**
 * Created by Ben on 2/7/17.
 */

@Controller
public class DrumCircleController {
    @Autowired
    UserRepository users;

    @RequestMapping(path = "/", method = RequestMethod.GET)
    public String home(HttpSession session, Model model) {
        String username = (String) session.getAttribute("username");
        User user = users.findByUsername(username);
        if (user != null) {
            System.out.println("welcome");
        }
        return "home";
    }


    @RequestMapping(path = "/login", method = RequestMethod.POST)
    public String login(HttpSession session, String username, String password) throws Exception {
        User user = users.findByUsername(username);
        if (user == null) {
            throw new Exception("User Name does not exist. Please create an account.");
        } else if (!PasswordStorage.verifyPassword(password, user.getPassword())) {
            throw new Exception("Incorrect password.");
        }
        session.setAttribute("username", username);
        return "redirect:/";
    }

    @RequestMapping(path = "/sign-up", method = RequestMethod.POST)
    public String signUp(HttpSession session, String username, String password) {
        User user = new User(username, password);
        users.save(user);
        return "redirect:/";
    }

    @PostConstruct
    public void init() {
        if (users.count() == 0) {
            User user = new User();
            user.setUsername("bburrva");
            user.setPassword("1234");
            users.save(user);
        }
    }
}