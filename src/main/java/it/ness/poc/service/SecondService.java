package it.ness.poc.service;


import org.eclipse.microprofile.opentracing.Traced;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.enterprise.context.ApplicationScoped;

@ApplicationScoped
@Traced
public class SecondService {

    Logger logger = LoggerFactory.getLogger(getClass());

    public String execute() {
        String msg = "second service";
        logger.info(msg);
        return msg;
    }
}
