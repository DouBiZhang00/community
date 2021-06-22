package life.jiarun.community2;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan("life.jiarun.community2.mapper")
public class Community2Application {

    public static void main(String[] args) {
        SpringApplication.run(Community2Application.class, args);
    }

}
