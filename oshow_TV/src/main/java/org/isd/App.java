package org.isd;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * springboot 启动类
 *
 */
@SpringBootApplication
@MapperScan(basePackages = {"org.isd.dao"})
public class App {
    public static void main( String[] args ) {
        SpringApplication.run(App.class, args);
    }
}
