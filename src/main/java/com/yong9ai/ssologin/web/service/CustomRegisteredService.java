package com.yong9ai.ssologin.web.service;

import java.time.LocalDateTime;
import lombok.Data;
import org.apereo.cas.services.BaseRegisteredService;


@Data
public class CustomRegisteredService extends BaseRegisteredService {

  private static final long serialVersionUID = 4262361479352461845L;
  /**
   * 创建时间
   */
  private LocalDateTime gmtCreate;
  /**
   * 修改时间
   */
  private LocalDateTime gmtModified;
  /**
   * 创建人
   */
  private String creator;
  /**
   * 修改人
   */
  private String modifier;
}
