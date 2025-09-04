package co.com.bancolombia.logger;

import gateways.CustomLogger;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class Slf4jLogger implements CustomLogger {
    private static final Logger log = LoggerFactory.getLogger(Slf4jLogger.class);

    @Override
    public void trace(String message, Object... args) {
        log.trace(message, args);
    }

    @Override
    public void debug(String message, Object... args) {
        log.debug(message, args);
    }

    @Override
    public void info(String message, Object... args)  {
        log.info(message, args);
    }

    @Override
    public void warn(String message, Object... args)  {
        log.warn(message, args);
    }

    @Override
    public void error(String message, Object... args) {
        log.error(message, args);
    }
}
