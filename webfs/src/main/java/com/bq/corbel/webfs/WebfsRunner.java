package com.bq.corbel.webfs;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;

import com.bq.corbel.webfs.api.WebResource;
import com.bq.corbel.webfs.ioc.WebfsIoc;
import com.bq.corbel.lib.ws.cli.ServiceRunnerWithVersionResource;
import com.bq.corbel.lib.ws.health.BasicHealthCheck;
import io.dropwizard.setup.Environment;

/**
 * @author Rubén Carrasco
 *
 */
public class WebfsRunner extends ServiceRunnerWithVersionResource<WebfsIoc> {

    private static final Logger LOG = LoggerFactory.getLogger(WebfsRunner.class);

    public static void main(String[] args) {
        try {
            new WebfsRunner().run(args);
        } catch (Exception e) {
            LOG.error("Unable to start webfs", e);
        }
    }

    @Override
    protected String getArtifactId() {
        // This has to be the same as in pom.xml
        return "webfs";
    }

    @Override
    protected void configureService(Environment environment, ApplicationContext context) {
        super.configureService(environment, context);
        environment.jersey().register(context.getBean(WebResource.class));
        environment.healthChecks().register("basic", new BasicHealthCheck());
    }

}
