package exception;

import utils.ErrorTypes;

public final class AlreadyExistsException extends DomainException {
    public AlreadyExistsException(String message) {
        super(ErrorTypes.ALREADY_EXISTS_EXCEPTION.getErrorCode(),
                ErrorTypes.ALREADY_EXISTS_EXCEPTION.getTitle(),
                message != null ? message : ErrorTypes.ALREADY_EXISTS_EXCEPTION.getMessage(),
                ErrorTypes.ALREADY_EXISTS_EXCEPTION.getStatus(),
                ErrorTypes.ALREADY_EXISTS_EXCEPTION.getErrors());
    }
}
