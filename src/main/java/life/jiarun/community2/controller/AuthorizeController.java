package life.jiarun.community2.controller;

import life.jiarun.community2.dto.AccessTokenDTO;
import life.jiarun.community2.dto.GithubUser;
import life.jiarun.community2.mapper.UserMapper;
import life.jiarun.community2.model.User;
import life.jiarun.community2.provider.GithubProvider;
import life.jiarun.community2.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.UUID;

@Controller
public class AuthorizeController {
    //OAuth2服务提供类对象，包含方法getToken,getUser
    @Autowired
    private GithubProvider githubProvider;

    //OAuth2第三方授权传参要求
    @Value("${github.client.id}")
    private String clientId;
    @Value("${github.client.secret}")
    private String clientSecret;
    @Value("${github.redirect.uri}")
    private String redirectUri;

    //持久层对象user
//    @Autowired
//    private UserMapper userMapper;

    @Autowired
    private UserService userService;


    @GetMapping("/callback")
    public String callback(@RequestParam(name = "code") String code,
                           @RequestParam(name = "state") String state,
                           HttpServletResponse response
    ) {
        /*
        OAuth2授权流程简述
        1.第三方验证
        2.得到授权码
        3.根据授权码请求Token
        4.拿到Token
        5.根据Token发起信息请求
        6.得到信息
         */

        AccessTokenDTO accessTokenDTO = new AccessTokenDTO();
        accessTokenDTO.setClient_id(clientId);
        accessTokenDTO.setClient_secret(clientSecret);
        accessTokenDTO.setCode(code);
        accessTokenDTO.setState(state);
        accessTokenDTO.setRedirect_uri(redirectUri);
        String accessToken = githubProvider.getAccessToken(accessTokenDTO);
        GithubUser githubUser = githubProvider.getUser(accessToken);

        //得到第三方user对象，创建本地user对象，通过mybatis持久层写入数据库并将本地生成的cookie发回给客户端
        if (githubUser != null && accessToken != null) {
            User user = new User();
            //本地生成token
            String token = UUID.randomUUID().toString();
            user.setToken(token);
            user.setName(githubUser.getName());
            user.setAccountId(String.valueOf(githubUser.getId()));
            user.setAvatarUrl(githubUser.getAvatarUrl());
            userService.createOrUpdate(user);
            //持久层对象插入数据库
//            userMapper.insert(user);
            response.addCookie(new Cookie("token", token));
            return "redirect:/";
            //登录成功，写入cookie
        } else {
            return "redirect:/";
            //登录失败，重新登录
        }

    }

    //退出登录处理
    @GetMapping("/logout")
    public String logout(HttpServletRequest request,
                         HttpServletResponse response) {
        //移除session中的数据
        request.getSession().removeAttribute("user");
        //清楚cookie，生成新的token，设置过期时间为马上，并将cookie发回给客户端替换实现清除
        Cookie cookie = new Cookie("token", null);
        cookie.setMaxAge(0);
        response.addCookie(cookie);
        return "redirect:/";
    }


}





































