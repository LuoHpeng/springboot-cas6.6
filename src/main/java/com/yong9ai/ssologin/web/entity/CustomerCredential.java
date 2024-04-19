package com.yong9ai.ssologin.web.entity;

import javax.validation.constraints.Size;
import lombok.Data;
import org.apereo.cas.authentication.credential.UsernamePasswordCredential;


@Data
public class CustomerCredential extends UsernamePasswordCredential {

  private static final long serialVersionUID = 4939585213410853452L;

  @Size(min = 1, message = "required.sessionId")
  private String sessionId;
  @Size(min = 1, message = "required.sig")
  private String sig;
  @Size(min = 1, message = "required.token")
  private String token;

}
