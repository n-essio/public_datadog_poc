package it.ness.poc.service;


import org.eclipse.microprofile.opentracing.Traced;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

@ApplicationScoped
@Traced
public class FirstService {
    Logger logger = LoggerFactory.getLogger(getClass());
    @Inject
    SecondService secondService;

    public String execute() {
        String msg = "first service" + secondService.execute();
        logger.info(msg);
        return msg;
    }
}
