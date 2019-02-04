package com.masudbappy.ticketmanagement.controller;

import com.masudbappy.ticketmanagement.aop.TokenRequired;
import com.masudbappy.ticketmanagement.model.User;
import com.masudbappy.ticketmanagement.repositories.SecurityService;
import com.masudbappy.ticketmanagement.repositories.UserService;
import com.masudbappy.ticketmanagement.util.Util;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    UserService userSevice;

    @Autowired
    SecurityService securityService;

    @ResponseBody
    @RequestMapping("")
    public List<User> getAllUsers() {
        return userSevice.getAllUsers();
    }

    @ResponseBody
    @RequestMapping("/{id}")
    public User getUser(@PathVariable("id") Integer id) {

        return Util.getSuccessResult(userSevice.getUser(id));
    }

    @ResponseBody
    @RequestMapping(value = "/register/admin", method = RequestMethod.POST)
    public Map<String, Object> registerAdmin(
            @RequestParam(value = "username") String username,
            @RequestParam(value = "password") String password
    ) {

        userSevice.createUser(username, password, 3);

        return Util.getSuccessResult();
    }

    @ResponseBody
    @RequestMapping(value = "/login/admin", method = RequestMethod.POST)
    public Map<String, Object> loginAdmin(
            @RequestParam(value = "username") String username,
            @RequestParam(value = "password") String password
    ) {

        User user = userSevice.getUser(username, password, 3);

        if (user == null) {
            return Util.getUserNotAvailableError();
        }

        String subject = user.getUserid() + "=" + user.getUsertype();
        String token = securityService.createToken(subject, (15 * 1000 * 60)); // 15 mins expiry time

        return Util.getSuccessResult(token);
    }

    @ResponseBody
    @RequestMapping(value = "/login/customer", method = RequestMethod.POST)
    public Map<String, Object> loginCustomer(
            @RequestParam(value = "username") String username,
            @RequestParam(value = "password") String password
    ) {

        User user = userSevice.getUser(username, password, 1);

        if (user == null) {
            return Util.getUserNotAvailableError();
        }

        String subject = user.getUserid() + "=" + user.getUsertype();
        String token = securityService.createToken(subject, (15 * 1000 * 60)); // 15 mins expiry time

        return Util.getSuccessResult(token);
    }

    @ResponseBody
    @RequestMapping(value = "/login/csr", method = RequestMethod.POST)
    public Map<String, Object> loginCSR(
            @RequestParam(value = "username") String username,
            @RequestParam(value = "password") String password
    ) {

        User user = userSevice.getUser(username, password, 2);

        if (user == null) {
            return Util.getUserNotAvailableError();
        }

        String subject = user.getUserid() + "=" + user.getUsertype();
        String token = securityService.createToken(subject, (15 * 1000 * 60)); // 15 mins expiry time

        return Util.getSuccessResult(token);
    }

    @ResponseBody
    @RequestMapping(value = "/register/customer", method = RequestMethod.POST)
    public Map<String, Object> registerCustomer(
            @RequestParam(value = "username") String username,
            @RequestParam(value = "password") String password
    ) {

        userSevice.createUser(username, password, 1);
        return Util.getSuccessResult();
    }

    @ResponseBody
    @RequestMapping(value = "/register/csr", method = RequestMethod.POST)
    public Map<String, Object> registerCSR(
            @RequestParam(value = "username") String username,
            @RequestParam(value = "password") String password
    ) {

        userSevice.createUser(username, password, 2);
        return Util.getSuccessResult();
    }

    @ResponseBody
    @RequestMapping(value = "", method = RequestMethod.PUT)
    public Map<String, Object> updateUser(@RequestParam(value = "userid") Integer userid,
                                          @RequestParam(value = "username") String username) {

        userSevice.updateUser(userid, username);
        return Util.getSuccessResult();
    }

    @ResponseBody
    @TokenRequired
    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    public Map<String, Object> deleteUser(
            @PathVariable("id") Integer userid) {

        userSevice.deleteUser(userid);
        return Util.getSuccessResult();
    }
}
