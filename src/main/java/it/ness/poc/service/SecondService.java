package it.ness.poc.service;

import org.eclipse.microprofile.opentracing.Traced;
import org.jboss.logging.Logger;

import javax.enterprise.context.ApplicationScoped;

@ApplicationScoped
@Traced
public class SecondService {

    Logger logger = Logger.getLogger(getClass());

    public String execute() {
        String msg = "second service";
        logger.info(msg);
        return msg;
    }
}
