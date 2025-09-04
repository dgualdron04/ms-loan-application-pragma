package exception;

import java.time.LocalDateTime;
import java.util.Map;

public interface BaseException {
    String getErrorCode();
    String getTitle();
    String getMessage();
    int getStatus();
    LocalDateTime getTimestamp();
    Map<String, String> getErrors();
}
