package com.yong9ai.ssologin;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.UserDetailsServiceAutoConfiguration;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.annotation.ComponentScan;


@SpringBootApplication(exclude = {UserDetailsServiceAutoConfiguration.class})
@ComponentScan(basePackages = {"com.yong9ai.ssologin"})
public class CasWebApplication {

  /**
   * Main entry point of the CAS web application.
   *
   * @param args the args
   */
  public static void main(final String[] args) {

    new SpringApplicationBuilder(CasWebApplication.class)
        .logStartupInfo(true)
        .run(args);


  }

}
