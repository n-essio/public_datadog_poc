# public_datadog_poc project

Poc using quarkiverse-datadog-tracing extensions.

## Problems

- datadog agent is locally started with this command:

```
docker run -d --name dd-agent  -p 8126:8126   -p 8125:8125 -v /var/run/docker.sock:/var/run/docker.sock:ro -v /proc/:/host/proc/:ro -v /sys/fs/cgroup/:/host/sys/fs/cgroup:ro -e DD_API_KEY=xxxxxxxx -e DD_SITE="datadoghq.eu" -e DD_APM_ENABLED=true -e DD_LOGS_ENABLED=true -e DD_LOGS_ENABLED=true  -e DD_LOGS_CONFIG_CONTAINER_COLLECT_ALL=true  -e DD_LOGS_CONFIG_DOCKER_CONTAINER_USE_FILE=true  gcr.io/datadoghq/agent:7
```

- startup logs (seems like the quarkus.datadog is reserved?:

```
2021-04-25 11:35:26,791 WARN  [io.qua.config] (Quarkus Main Thread) Unrecognized configuration key "quarkus.datadog.trace.header.tags" was provided; it will be ignored; verify that the dependency extension for this configuration is set or that you did not make a typo
2021-04-25 11:35:26,791 WARN  [io.qua.config] (Quarkus Main Thread) Unrecognized configuration key "quarkus.datadog.trace.agent.port" was provided; it will be ignored; verify that the dependency extension for this configuration is set or that you did not make a typo
2021-04-25 11:35:26,791 WARN  [io.qua.config] (Quarkus Main Thread) Unrecognized configuration key "quarkus.datadog.agent.host" was provided; it will be ignored; verify that the dependency extension for this configuration is set or that you did not make a typo
2021-04-25 11:35:26,791 WARN  [io.qua.config] (Quarkus Main Thread) Unrecognized configuration key "quarkus.datadog.trace.enabled" was provided; it will be ignored; verify that the dependency extension for this configuration is set or that you did not make a typo
2021-04-25 11:35:26,906 WARN  [io.qua.config] (Quarkus Main Thread) Unrecognized configuration key "quarkus.datadog.trace.header.tags" was provided; it will be ignored; verify that the dependency extension for this configuration is set or that you did not make a typo
2021-04-25 11:35:26,906 WARN  [io.qua.config] (Quarkus Main Thread) Unrecognized configuration key "quarkus.datadog.trace.agent.port" was provided; it will be ignored; verify that the dependency extension for this configuration is set or that you did not make a typo
2021-04-25 11:35:26,906 WARN  [io.qua.config] (Quarkus Main Thread) Unrecognized configuration key "quarkus.datadog.agent.host" was provided; it will be ignored; verify that the dependency extension for this configuration is set or that you did not make a typo
2021-04-25 11:35:26,906 WARN  [io.qua.config] (Quarkus Main Thread) Unrecognized configuration key "quarkus.datadog.trace.enabled" was provided; it will be ignored; verify that the dependency extension for this configuration is set or that you did not make a typo
```

- the Tracedr is started correctly, but on datadog the service dont exist (no logs, no traces)

```
2021-04-25 11:35:27,021 INFO  [dat.tra.cor.StatusLogger] (Quarkus Main Thread) DATADOG TRACER CONFIGURATION {"version":"0.74.0~9fcc12d26","os_name":"Mac OS X","os_version":"10.16","architecture":"x86_64","lang":"jvm","lang_version":"13.0.2","jvm_vendor":"N/A","jvm_version":"13.0.2+8","java_class_version":"57.0","http_nonProxyHosts":"local|*.local|169.254/16|*.169.254/16","http_proxyHost":"null","enabled":true,"service":"public_datadog_poc-dev","agent_url":"http://localhost:8126","agent_error":false,"debug":false,"analytics_enabled":false,"sampling_rules":[{},{}],"priority_sampling_enabled":true,"logs_correlation_enabled":true,"profiling_enabled":false,"dd_version":"0.74.0~9fcc12d26","health_checks_enabled":true,"configuration_file":"no config file present","runtime_id":"6dd3804c-3923-4df4-8aec-f706cf04b01f","logging_settings":{}}
2021-04-25 11:35:27,225 INFO  [io.quarkus] (Quarkus Main Thread) public_datadog_poc 1.0.0-SNAPSHOT on JVM (powered by Quarkus 1.13.2.Final) started in 1.465s. Listening on: http://localhost:8080
2021-04-25 11:35:27,226 INFO  [io.quarkus] (Quarkus Main Thread) Profile dev activated. Live Coding activated.
2021-04-25 11:35:27,226 INFO  [io.quarkus] (Quarkus Main Thread) Installed features: [cdi, datadog-tracer, jaeger, resteasy, smallrye-opentracing]

```

- no traceId and span on the logs (also using org.slf4j.Logger)

```
    Logger logger = LoggerFactory.getLogger(getClass());
```

