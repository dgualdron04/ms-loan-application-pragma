package exception;

import utils.ErrorTypes;

public final class BusinessRuleViolatedException extends DomainException {
    public BusinessRuleViolatedException(String message) {
        super(ErrorTypes.VALIDATION_EXCEPTION.getErrorCode(),
            ErrorTypes.VALIDATION_EXCEPTION.getTitle(),
            message != null ? message : ErrorTypes.VALIDATION_EXCEPTION.getMessage(),
            ErrorTypes.VALIDATION_EXCEPTION.getStatus(),
            ErrorTypes.VALIDATION_EXCEPTION.getErrors());
    }
}
