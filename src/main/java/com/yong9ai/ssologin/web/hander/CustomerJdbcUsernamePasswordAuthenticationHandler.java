package com.yong9ai.ssologin.web.hander;

import com.yong9ai.ssologin.web.entity.CustomerCredential;
import com.yong9ai.ssologin.web.exception.AccountFailLockedException;
import com.yong9ai.ssologin.web.exception.AccountFailOverException;
import java.security.GeneralSecurityException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import javax.security.auth.login.AccountNotFoundException;
import javax.security.auth.login.FailedLoginException;
import javax.sql.DataSource;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.BooleanUtils;
import org.apache.commons.lang3.StringUtils;
import org.apereo.cas.adaptors.jdbc.AbstractJdbcUsernamePasswordAuthenticationHandler;
import org.apereo.cas.authentication.AuthenticationHandlerExecutionResult;
import org.apereo.cas.authentication.Credential;
import org.apereo.cas.authentication.PreventedException;
import org.apereo.cas.authentication.credential.UsernamePasswordCredential;
import org.apereo.cas.authentication.principal.Principal;
import org.apereo.cas.authentication.principal.PrincipalFactory;
import org.apereo.cas.authentication.principal.Service;
import org.apereo.cas.services.ServicesManager;
import org.apereo.inspektr.common.web.ClientInfoHolder;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.IncorrectResultSizeDataAccessException;



@Slf4j
public class CustomerJdbcUsernamePasswordAuthenticationHandler extends
    AbstractJdbcUsernamePasswordAuthenticationHandler {


  public CustomerJdbcUsernamePasswordAuthenticationHandler(String name,
      ServicesManager servicesManager,
      PrincipalFactory principalFactory,
      Integer order, DataSource dataSource) {

    super(name, servicesManager, principalFactory, order, dataSource);
  }


  private String sql;
  private String fieldPassword;
  private String fieldDisabled;



  private int AFS_CODE_SUCCESS = 100;
  private int AFS_CODE_FAIL = 900;
  private String STATUS_DISABLED = "1";


  @SneakyThrows
  public AuthenticationHandlerExecutionResult doAuthentication(final Credential credential)
      throws PreventedException {
    CustomerCredential customerCredential = null;
    if (credential instanceof CustomerCredential) {
      customerCredential = (CustomerCredential) credential;
    } else if (credential instanceof UsernamePasswordCredential) {
      //来自restful 接口的请求
      customerCredential = new CustomerCredential();
      UsernamePasswordCredential usernamePasswordCredential = (UsernamePasswordCredential) credential;
      customerCredential.setUsername(usernamePasswordCredential.getUsername());
      customerCredential.setPassword(usernamePasswordCredential.getPassword());
    } else {
      throw new FailedLoginException("login method error");
    }


    AuthenticationHandlerExecutionResult authenticationHandlerExecutionResult = authenticateUsernamePasswordInternal(
        (UsernamePasswordCredential) credential, String.valueOf(customerCredential.getPassword()));


    return authenticationHandlerExecutionResult;
  }

  /**
   * Authenticates a username/password credential by an arbitrary strategy with extra parameter
   * original credential password before encoding password. Override it if implementation need to
   * use original password for authentication.
   *
   * @param credential       the credential object bearing the transformed username and password.
   * @param originalPassword original password from credential before password encoding
   * @return AuthenticationHandlerExecutionResult resolved from credential on authentication success
   * or null if no principal could be resolved from the credential.
   * @throws GeneralSecurityException On authentication failure.
   * @throws PreventedException       On the indeterminate case when authentication is prevented.
   */
  @Override
  protected AuthenticationHandlerExecutionResult authenticateUsernamePasswordInternal(
      UsernamePasswordCredential credential, String originalPassword)
      throws GeneralSecurityException, PreventedException {
    if (StringUtils.isBlank(this.sql) || getJdbcTemplate() == null) {
      throw new GeneralSecurityException("Authentication handler is not configured correctly. "
          + "No SQL statement or JDBC template is found.");
    }

    final String username = credential.getUsername();
    final String password = String.valueOf(credential.getPassword());

    try {
      final Map<String, Object> dbFields = query(credential);
      checkPassword(originalPassword, username, password, dbFields);
    } catch (final IncorrectResultSizeDataAccessException e) {
      if (e.getActualSize() == 0) {
        log.error(username + " not found with SQL query");
        throw new AccountNotFoundException(username + " not found with SQL query");
      }
      log.error("Multiple records found for " + username);
      throw new FailedLoginException("Multiple records found for " + username);
    } catch (final DataAccessException e) {
      log.error("SQL exception while executing");
      throw new PreventedException("SQL exception while executing query for " + username);
    }

    final Map<String, List<Object>> attributes = new LinkedHashMap<>();
    final Principal principal = this.principalFactory.createPrincipal(username, attributes);
    return createHandlerResult(credential, principal, new ArrayList<>(0));
  }

  private void checkPassword(String originalPassword, String username, String password,
      Map<String, Object> dbFields)
      throws AccountFailOverException, FailedLoginException, AccountFailLockedException {
    if (dbFields.containsKey(this.fieldPassword)) {
      String dbPassword = (String) dbFields.get(this.fieldPassword);
      //解密处理
      if (StringUtils.isEmpty(originalPassword) && !StringUtils.equals(originalPassword, dbPassword)
          || StringUtils.isBlank(originalPassword) && !StringUtils.equals(password,
          dbPassword)) {

        log.error(ClientInfoHolder.getClientInfo().getClientIpAddress()
            + "Password does not match value on record");
        throw new FailedLoginException(ClientInfoHolder.getClientInfo().getClientIpAddress()
            + "Password does not match value on record.");
      }
    }

    if (StringUtils.isNotBlank(this.fieldDisabled) && dbFields.containsKey(this.fieldDisabled)) {
      final String dbDisabled = dbFields.get(this.fieldDisabled).toString();
      if (BooleanUtils.toBoolean(dbDisabled) || STATUS_DISABLED.equals(dbDisabled)) {
        log.error("Account has been disabled");
        throw new AccountFailLockedException("Account has been disabled");
      }
    }
  }


  private Map<String, Object> query(final UsernamePasswordCredential credential) {
    if (this.sql.contains("?")) {
      return getJdbcTemplate().queryForMap(this.sql, credential.getUsername());
    }
    final Map<String, Object> parameters = new LinkedHashMap();
    parameters.put("username", credential.getUsername());
    parameters.put("password", credential.getPassword());
    return getNamedParameterJdbcTemplate().queryForMap(this.sql, parameters);
  }

  @Override
  public AuthenticationHandlerExecutionResult authenticate(Credential credential, Service service)
      throws GeneralSecurityException, PreventedException {
    if (!this.preAuthenticate(credential)) {
      throw new FailedLoginException();
    } else {
      return this.postAuthenticate(credential, this.doAuthentication(credential));
    }
  }

  @Override
  public boolean supports(Credential credential) {
    return credential instanceof UsernamePasswordCredential
        || credential instanceof CustomerCredential;
  }

  public String getSql() {
    return sql;
  }

  public void setSql(String sql) {
    this.sql = sql;
  }

  public String getFieldPassword() {
    return fieldPassword;
  }

  public void setFieldPassword(String fieldPassword) {
    this.fieldPassword = fieldPassword;
  }

  public String getFieldDisabled() {
    return fieldDisabled;
  }

  public void setFieldDisabled(String fieldDisabled) {
    this.fieldDisabled = fieldDisabled;
  }


}
