package edu.ustb.controller;

import edu.ustb.entity.Novels;
import edu.ustb.service.NovelsService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.HashMap;
import java.util.Map;

@Controller
@RequestMapping("novel")
public class NovelController
{
    //    @PostMapping("login")
//    @ResponseBody
//    public Map<String, Object> login(String username, String password)
//    {
//        Map<String, Object> result = new HashMap<String, Object>();
//        String userToken = UsersService.login(username, password);
//        if (userToken != null)
//        {
//            result.put("code", 200);
//            result.put("token", userToken);
//            result.put("msg", "loginSuccess");
//        } else
//        {
//            result.put("code", 401);
//            result.put("token", null);
//            result.put("msg", "loginFailure");
//        }
//        System.out.println(result);
//        return result;
//    }
    @PostMapping("create")
    @ResponseBody
    public Map<String, Object> create(String novelName, String writer, String summary)
    {
        Map<String, Object> result = new HashMap<String, Object>();

        Novels novels = NovelsService.create(novelName, writer, summary);

        if (novels != null)
        {
            result.put("code", 200);
            result.put("novels", novels);
            result.put("msg", "createSuccess");
        } else
        {
            result.put("code", 400);
            result.put("novels", null);
            result.put("msg", "createFailure");
        }
        System.out.println(result);
        return result;
    }
}
