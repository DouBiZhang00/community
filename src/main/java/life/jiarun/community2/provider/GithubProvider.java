package life.jiarun.community2.provider;

import com.alibaba.fastjson.JSON;
import life.jiarun.community2.dto.AccessTokenDTO;
import life.jiarun.community2.dto.GithubUser;
import okhttp3.*;
import org.springframework.stereotype.Component;
import java.io.IOException;
import java.util.concurrent.TimeUnit;

//OAuth2授权服务提供类
@Component
public class GithubProvider {
    //传入授权码code等得到token所需要的数据包装的对象，返回token
    public String getAccessToken(AccessTokenDTO accessTokenDTO){
        //okhttp快速简单发起http请求，处理请求
        //1.指定交互格式为json，字符集为utf-8
        MediaType mediaType = MediaType.get("application/json; charset=utf-8");
        //2.生成客户端对象
        OkHttpClient client = new OkHttpClient
                .Builder()
                .readTimeout(30, TimeUnit.SECONDS)
                .connectTimeout(30, TimeUnit.SECONDS)
                .writeTimeout(30, TimeUnit.SECONDS).build();
        //3.构建request的body对象，使用FastJson将对象转为JSON字符串
        RequestBody body = RequestBody.create(mediaType, JSON.toJSONString(accessTokenDTO));
        //4.生成request，并发出请求
        Request request = new Request.Builder()
                .url("https://github.com/login/oauth/access_token")
                .post(body)
                .build();
        //5.使用try-with-recource包裹，根据request承接response，处理Response的body部分并使用字符串划分，得到token
        try (Response response = client.newCall(request).execute()) {
            String string = response.body().string();
            String token = string.split("&")[0].split("=")[1];
            return token;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;

    }
    //根据传入的token值得到第三方服务器的user数据
    public GithubUser getUser(String accessToken){
        OkHttpClient client = new OkHttpClient.Builder()
                .readTimeout(30, TimeUnit.SECONDS)
                .connectTimeout(30, TimeUnit.SECONDS)
                .writeTimeout(30, TimeUnit.SECONDS).build();;
        Request request = new Request.Builder()
                .url("https://api.github.com/user")
                .header("Authorization", "token " + accessToken)
                .build();
        try {
            Response response = client.newCall(request).execute();
            String string = response.body().string();
            //根据传回的response的JSON字符串，使用FastJson构建user对象
            GithubUser githubUser = JSON.parseObject(string,GithubUser.class);
            return githubUser;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
