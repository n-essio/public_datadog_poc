package it.ness.poc;

import it.ness.poc.service.FirstService;
import org.eclipse.microprofile.opentracing.Traced;
import org.jboss.logging.Logger;

import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

@Path("/public_datadog_poc")
@Traced
public class GreetingResource {

    Logger logger = Logger.getLogger(getClass());

    @Inject
    FirstService firstService;

    @GET
    @Produces(MediaType.TEXT_PLAIN)
    public String hello() {
        String msg = firstService.execute();
        logger.info(msg);
        return "Hello RESTEasy: " + msg;
    }
}
