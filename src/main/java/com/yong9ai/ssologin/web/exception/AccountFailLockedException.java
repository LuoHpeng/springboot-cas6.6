package com.yong9ai.ssologin.web.exception;

import javax.security.auth.login.AccountException;


public class AccountFailLockedException extends AccountException {

  private static final long serialVersionUID = 802556922354616288L;

  /**
   * Constructs a FailedLoginException with no detail message. A detail message is a String that
   * describes this particular exception.
   */

  public AccountFailLockedException() {
    super();
  }

  /**
   * Constructs a FailedLoginException with the specified detail message.  A detail message is a
   * String that describes this particular exception.
   *
   * @param msg the detail message.
   */

  public AccountFailLockedException(String msg) {
    super(msg);
  }
}
