package edu.ustb.controller;

import edu.ustb.service.UsersService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.HashMap;
import java.util.Map;

@Controller
@RequestMapping("user")
public class UserController
{
    @PostMapping("login")
    @ResponseBody
    public Map<String, Object> login(String username, String password)
    {
        Map<String, Object> result = new HashMap<String, Object>();
        String userToken = UsersService.login(username, password);
        if (userToken != null)
        {
            result.put("code", 200);
            result.put("token", userToken);
            result.put("msg", "loginSuccess");
        } else
        {
            result.put("code", 401);
            result.put("token", null);
            result.put("msg", "loginFailure");
        }
        System.out.println(result);
        return result;
    }
}
