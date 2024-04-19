package com.yong9ai.ssologin.web.config;

import com.yong9ai.ssologin.web.hander.CustomerJdbcUsernamePasswordAuthenticationHandler;
import java.sql.Connection;
import java.sql.SQLException;
import javax.sql.DataSource;
import lombok.SneakyThrows;
import org.apereo.cas.authentication.AuthenticationEventExecutionPlan;
import org.apereo.cas.authentication.AuthenticationEventExecutionPlanConfigurer;
import org.apereo.cas.authentication.AuthenticationHandler;
import org.apereo.cas.authentication.principal.DefaultPrincipalFactory;
import org.apereo.cas.authentication.support.password.PasswordEncoderUtils;
import org.apereo.cas.configuration.CasConfigurationProperties;
import org.apereo.cas.configuration.model.support.jdbc.authn.QueryJdbcAuthenticationProperties;
import org.apereo.cas.configuration.support.JpaBeans;
import org.apereo.cas.services.ServicesManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;




@Configuration("customerAuthenticationConfiguration")
@EnableConfigurationProperties(CasConfigurationProperties.class)
public class CustomerAuthenticationConfiguration implements
    AuthenticationEventExecutionPlanConfigurer {

  @Autowired
  @Qualifier("servicesManager")
  private ServicesManager servicesManager;


  @Autowired
  private ApplicationContext applicationContext;


  @Value("${login.authn.jdbc.query[0].url}")
  private String url;

  @Value("${login.authn.jdbc.query[0].user}")
  private String user;

  @Value("${login.authn.jdbc.query[0].password}")
  private String password;

  @Value("${login.authn.jdbc.query[0].sql}")
  private String sql;

  @Value("${login.authn.jdbc.query[0].fieldPassword}")
  private String fieldPassword;

  @Value("${login.authn.jdbc.query[0].fieldDisabled}")
  private String fieldDisabled;

  @Value("${login.authn.jdbc.query[0].driverClass}")
  private String driverClass;



  public AuthenticationHandler jdbcAuthenticationHandler() throws SQLException {

    QueryJdbcAuthenticationProperties queryJdbcAuthenticationProperties = new QueryJdbcAuthenticationProperties();
    queryJdbcAuthenticationProperties.setFieldDisabled(fieldDisabled);
    queryJdbcAuthenticationProperties.setFieldPassword(fieldPassword);
    queryJdbcAuthenticationProperties.setSql(sql);
    queryJdbcAuthenticationProperties.setPassword(password);
    queryJdbcAuthenticationProperties.setUrl(url);
    queryJdbcAuthenticationProperties.setDriverClass(driverClass);
    queryJdbcAuthenticationProperties.setUser(user);

    DataSource dataSource = JpaBeans.newDataSource(queryJdbcAuthenticationProperties);

    //校验数据库连接是否可用
    Connection connection = dataSource.getConnection();
    connection.close();

    CustomerJdbcUsernamePasswordAuthenticationHandler customerhandler = new CustomerJdbcUsernamePasswordAuthenticationHandler(
        CustomerJdbcUsernamePasswordAuthenticationHandler.class.getName(),
        servicesManager, new DefaultPrincipalFactory(), 1,
        dataSource);
    customerhandler.setSql(queryJdbcAuthenticationProperties.getSql());
    customerhandler.setFieldPassword(queryJdbcAuthenticationProperties.getFieldPassword());
    customerhandler.setFieldDisabled(queryJdbcAuthenticationProperties.getFieldDisabled());

    PasswordEncoder newPasswordEncoder = PasswordEncoderUtils.newPasswordEncoder(
        queryJdbcAuthenticationProperties.getPasswordEncoder(), applicationContext);
    customerhandler.setPasswordEncoder(newPasswordEncoder);

    return customerhandler;
  }


  @SneakyThrows
  @Override
  public void configureAuthenticationExecutionPlan(final AuthenticationEventExecutionPlan plan) {
    plan.registerAuthenticationHandler(jdbcAuthenticationHandler());
  }
}
