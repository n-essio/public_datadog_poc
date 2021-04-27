# public_datadog_poc project

Poc using quarkiverse-datadog-tracing extensions.

## preparatory steps

- add in pom.xml some dependencies

```
        <dependency>
            <groupId>org.kij.quarkiverse.datadog</groupId>
            <artifactId>quarkus-datadog-tracer</artifactId>
            <version>0.1.0-SNAPSHOT</version>
        </dependency>
        <dependency>
            <groupId>io.quarkus</groupId>
            <artifactId>quarkus-smallrye-opentracing</artifactId>
        </dependency>

        <dependency>
            <groupId>io.opentracing</groupId>
            <artifactId>opentracing-api</artifactId>
            <version>0.32.0</version>
        </dependency>

        <dependency>
            <groupId>io.opentracing</groupId>
            <artifactId>opentracing-util</artifactId>
            <version>0.32.0</version>
        </dependency>

        <dependency>
            <groupId>io.quarkus</groupId>
            <artifactId>quarkus-logging-json</artifactId>
        </dependency>
```

- add to application.properties, some parameters:

```
quarkus.application.name=public_datadog_poc

quarkus.datadog.enabled=true
quarkus.datadog.logs.injection=true

dd.service=public_datadog_poc
dd.agent.host=localhost
dd.trace.enabled=true
dd.trace.agent.port=8126
dd.env=dev
dd.trace.header.tags=tag1:test,tag2:poc

```

- add some annotations in the services

```
@ApplicationScoped
@Traced
public class FirstService {}

```

- run datadog-agent with some options:

```
docker run -d --name dd-agent \
  --network web \
  -v /var/run/docker.sock:/var/run/docker.sock:ro \
  -v /proc/:/host/proc/:ro \
  -v /sys/fs/cgroup/:/host/sys/fs/cgroup:ro \
  -p 8126:8126/tcp \
  -p 8125:8125/udp \
  -e DD_API_KEY=xxxxxxxxx \
  -e DD_SITE="datadoghq.eu"  \
  -e DD_LOGS_ENABLED=true  \
  -e DD_LOGS_CONFIG_CONTAINER_COLLECT_ALL=true  \
  -e DD_DOGSTATSD_NON_LOCAL_TRAFFIC=true \
  -e DD_APM_ENABLED=true \
  -e DD_HOSTNAME=MacBook-Pro-di-fiorenzo.local \
  -e DD_ENV=MacBook-Pro-di-fiorenzo.local \
  -e NON_LOCAL_TRAFFIC=false \
  gcr.io/datadoghq/agent:latest
```

- mvn build package, docker build, docker run:

```
 mvn clean package -DskipTests=true
 docker build -f src/main/docker/Dockerfile.jvm -t quarkus/public_datadog_poc-jvm .  
 docker run -i --name /public_datadog_poc-jvm  --rm --network web -p 8080:8080 -e DD_AGENT_HOST=dd-agent quarkus/public_datadog_poc-jvm
```

- quarkus logs at startup:

```
exec java -Dquarkus.http.host=0.0.0.0 -Djava.util.logging.manager=org.jboss.logmanager.LogManager -XX:+ExitOnOutOfMemoryError -cp . -jar /deployments/quarkus-run.jar
{"timestamp":"2021-04-27T23:27:54.007Z","sequence":149,"loggerClassName":"org.jboss.logging.Logger","loggerName":"io.quarkus","level":"INFO","message":"public_datadog_poc 1.0.0-SNAPSHOT on JVM (powered by Quarkus 1.13.2.Final) started in 1.063s. Listening on: http://0.0.0.0:8080","threadName":"main","threadId":1,"mdc":{},"ndc":"","hostName":"8ed3642b77a5","processName":"quarkus-run.jar","processId":1}
{"timestamp":"2021-04-27T23:27:54.013Z","sequence":150,"loggerClassName":"org.jboss.logging.Logger","loggerName":"io.quarkus","level":"INFO","message":"Profile prod activated. ","threadName":"main","threadId":1,"mdc":{},"ndc":"","hostName":"8ed3642b77a5","processName":"quarkus-run.jar","processId":1}
{"timestamp":"2021-04-27T23:27:54.013Z","sequence":151,"loggerClassName":"org.jboss.logging.Logger","loggerName":"io.quarkus","level":"INFO","message":"Installed features: [cdi, datadog-tracer, jaeger, resteasy, smallrye-opentracing]","threadName":"main","threadId":1,"mdc":{},"ndc":"","hostName":"8ed3642b77a5","processName":"quarkus-run.jar","processId":1}
{"timestamp":"2021-04-27T23:27:54.158Z","sequence":152,"loggerClassName":"org.slf4j.impl.Slf4jLogger","loggerName":"datadog.trace.core.StatusLogger","level":"INFO","message":"DATADOG TRACER CONFIGURATION {\"version\":\"0.78.3~c3312399e\",\"os_name\":\"Linux\",\"os_version\":\"5.10.25-linuxkit\",\"architecture\":\"amd64\",\"lang\":\"jvm\",\"lang_version\":\"11.0.10\",\"jvm_vendor\":\"Red Hat, Inc.\",\"jvm_version\":\"11.0.10+9-LTS\",\"java_class_version\":\"55.0\",\"http_nonProxyHosts\":\"null\",\"http_proxyHost\":\"null\",\"enabled\":true,\"service\":\"public_datadog_poc\",\"agent_url\":\"http://dd-agent:8126\",\"agent_error\":false,\"debug\":false,\"analytics_enabled\":false,\"sampling_rules\":[{},{}],\"priority_sampling_enabled\":true,\"logs_correlation_enabled\":true,\"profiling_enabled\":false,\"dd_version\":\"0.78.3~c3312399e\",\"health_checks_enabled\":true,\"configuration_file\":\"no config file present\",\"runtime_id\":\"8f33205b-a457-4db3-b536-e1a9363a221d\",\"logging_settings\":{}}","threadName":"dd-task-scheduler","threadId":17,"mdc":{},"ndc":"","hostName":"8ed3642b77a5","processName":"quarkus-run.jar","processId":1}
```

- open the browser on http://localhost:8080/public_datadog_poc and watch the logs:

```
{"timestamp":"2021-04-27T23:28:21.16Z","sequence":153,"loggerClassName":"org.jboss.logging.Logger","loggerName":"it.ness.poc.service.SecondService_Subclass","level":"INFO","message":"second service","threadName":"executor-thread-1","threadId":21,"mdc":{"dd.span_id":"959552198482537278","dd.trace_id":"5180959077599665371"},"ndc":"","hostName":"8ed3642b77a5","processName":"quarkus-run.jar","processId":1}

{"timestamp":"2021-04-27T23:28:21.171Z","sequence":154,"loggerClassName":"org.jboss.logging.Logger","loggerName":"it.ness.poc.service.FirstService_Subclass","level":"INFO","message":"first servicesecond service","threadName":"executor-thread-1","threadId":21,"mdc":{"dd.span_id":"956468608708527309","dd.trace_id":"5180959077599665371"},"ndc":"","hostName":"8ed3642b77a5","processName":"quarkus-run.jar","processId":1}

{"timestamp":"2021-04-27T23:28:21.171Z","sequence":155,"loggerClassName":"org.jboss.logging.Logger","loggerName":"it.ness.poc.GreetingResource_Subclass","level":"INFO","message":"first servicesecond service","threadName":"executor-thread-1","threadId":21,"mdc":{"dd.span_id":"2382824871336687330","dd.trace_id":"5180959077599665371"},"ndc":"","hostName":"8ed3642b77a5","processName":"quarkus-run.jar","processId":1}

```

- now enjoy!

