package sein.nutritionist.error;


import lombok.Getter;

import java.util.Map;

@Getter
public class BusinessException extends RuntimeException{
    
    private final ErrorCode errorCode;
    private final Map<String,Object> details;

    public BusinessException(ErrorCode errorCode) {
        super(errorCode.getDefaultMessage());
        this.errorCode = errorCode;
        this.details = null;
    }

    public BusinessException(ErrorCode errorCode, String overrideMessage) {
        super(overrideMessage);
        this.errorCode = errorCode;
        this.details = null;
    }

    public BusinessException(ErrorCode errorCode, String overrideMessage, Map<String, Object> details) {
        super(overrideMessage);
        this.errorCode = errorCode;
        this.details = details;
    }
}
