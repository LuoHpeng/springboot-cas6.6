package com.yong9ai.ssologin.web;//package com.yong9ai.ssologin.web;

import java.util.Collection;
import org.apereo.cas.services.CasRegisteredService;
import org.apereo.cas.services.RegisteredService;
import org.apereo.cas.services.ServicesManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/cas1/services1")
public class ServiceController{


  @Autowired
  @Qualifier("servicesManager")
  private ServicesManager servicesManager;

  /**
   * 注册service
   *
   * @return
   */
  @RequestMapping(value = "/create.json", method = RequestMethod.GET)
  public void addClient() {

    CasRegisteredService casRegisteredService = new CasRegisteredService();
    casRegisteredService.setId(2);
    casRegisteredService.setServiceId("serviceId2");
    casRegisteredService.setName("name2");
    casRegisteredService.setTheme("them2");
    casRegisteredService.setEvaluationOrder(2);
//    casRegisteredService.setCreator("123");
//    casRegisteredService.setModifier("123");
//    casRegisteredService.setGmtCreate(LocalDateTime.now());
//    casRegisteredService.setGmtModified(LocalDateTime.now());

    try {
      servicesManager.save(casRegisteredService);
    }catch (Throwable throwable){
      System.out.println(throwable);
    }


    Collection<RegisteredService> allServices = servicesManager.getAllServices();

    RegisteredService serviceBy = servicesManager.findServiceBy(1);
    System.out.println("123");
  }


}
