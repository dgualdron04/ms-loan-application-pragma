package exception;

import utils.ErrorTypes;

public final class NotFoundException extends DomainException {
    public NotFoundException(String message) {
        super(ErrorTypes.NOT_FOUND_EXCEPTION.getErrorCode(),
                ErrorTypes.NOT_FOUND_EXCEPTION.getTitle(),
                message != null ? message : ErrorTypes.NOT_FOUND_EXCEPTION.getMessage(),
                ErrorTypes.NOT_FOUND_EXCEPTION.getStatus(),
                ErrorTypes.NOT_FOUND_EXCEPTION.getErrors());
    }
}
