package it.ness.poc.service;


import org.eclipse.microprofile.opentracing.Traced;
import org.jboss.logging.Logger;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

@ApplicationScoped
@Traced
public class FirstService {
    Logger logger = Logger.getLogger(getClass());
    @Inject
    SecondService secondService;

    public String execute() {
        String msg = "first service" + secondService.execute();
        logger.info(msg);
        return msg;
    }
}
