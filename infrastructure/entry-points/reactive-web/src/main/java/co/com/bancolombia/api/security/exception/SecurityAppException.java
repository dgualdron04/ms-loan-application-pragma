package co.com.bancolombia.api.security.exception;

import lombok.Getter;
import org.springframework.security.core.AuthenticationException;
import utils.ErrorTypes;

import java.util.Map;

@Getter
public class SecurityAppException extends AuthenticationException {

    private final ErrorTypes errorType;
    private final Map<String, String> detailsOverride;

    public SecurityAppException(ErrorTypes type) {
        super(type.getMessage());
        this.errorType = type;
        this.detailsOverride = null;
    }

    public SecurityAppException(ErrorTypes type, Throwable cause) {
        super(type.getMessage(), cause);
        this.errorType = type;
        this.detailsOverride = null;
    }

    public SecurityAppException(ErrorTypes type, Map<String, String> detailsOverride) {
        super(type.getMessage());
        this.errorType = type;
        this.detailsOverride = detailsOverride;
    }
}
