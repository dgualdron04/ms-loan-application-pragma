package co.com.bancolombia.api.exception.model;

import exception.DomainException;
import utils.ErrorTypes;

public final class InternalException extends DomainException {
    public InternalException(Throwable cause, String message) {
        super(ErrorTypes.INTERNAL_SERVER_ERROR.getErrorCode(),
                ErrorTypes.INTERNAL_SERVER_ERROR.getTitle(),
                message != null ? message : ErrorTypes.INTERNAL_SERVER_ERROR.getMessage(),
                ErrorTypes.INTERNAL_SERVER_ERROR.getStatus(),
                ErrorTypes.INTERNAL_SERVER_ERROR.getErrors());
        initCause(cause);
    }
}
