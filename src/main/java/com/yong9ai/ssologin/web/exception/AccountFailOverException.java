package com.yong9ai.ssologin.web.exception;

import javax.security.auth.login.AccountException;


public class AccountFailOverException extends AccountException {

  private static final long serialVersionUID = 802556962454616288L;

  /**
   * Constructs a FailedLoginException with no detail message. A detail message is a String that
   * describes this particular exception.
   */

  public AccountFailOverException() {
    super();
  }

  /**
   * Constructs a FailedLoginException with the specified detail message.  A detail message is a
   * String that describes this particular exception.
   *
   * @param msg the detail message.
   */

  public AccountFailOverException(String msg) {
    super(msg);
  }
}