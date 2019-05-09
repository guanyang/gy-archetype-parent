package ${package}.service.util;

import java.util.Set;
import java.util.UUID;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;

import org.aspectj.lang.ProceedingJoinPoint;
import ${package}.util.CodeType;
import ${package}.util.MessageDTO;
import ${package}.util.ResponseDTO;
import ${package}.util.ServiceException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ServiceInterceptor {

    private static Logger    logger    = LoggerFactory.getLogger(ServiceInterceptor.class);
    private static Validator validator = Validation.buildDefaultValidatorFactory().getValidator();

    public Object around(ProceedingJoinPoint proceedingJoinPoint) throws Throwable {

        MessageDTO message = new MessageDTO();
        message.setCallId(UUID.randomUUID().toString());
        Object result = null;
        try {
            long start = System.currentTimeMillis();
            message.setTimestamp(start);
            // 参数校验
            Object[] args = proceedingJoinPoint.getArgs();
            if (args != null && args.length > 0) {
                for (Object obj : args) {
                    Set<ConstraintViolation<Object>> constraintViolations = validator.validate(obj);
                    if (constraintViolations != null && constraintViolations.size() > 0) {
                        // 返回任意一个错误信息
                        ConstraintViolation<Object> violation = constraintViolations.iterator().next();
                        MessageUtil.buildMessage(message, CodeType.PARAM_ERROR, violation.getMessage());
                        ResponseDTO tmp = new ResponseDTO();
                        tmp.setMessage(message);
                        return tmp;
                    }
                }
            }
            // 具体业务逻辑处理
            result = proceedingJoinPoint.proceed();
            long end = System.currentTimeMillis();
            message.setCost(end - start);
            ResponseDTO base = (ResponseDTO) result;
            base.setMessage(message);
            MessageUtil.wrapSuccess(base);
        } catch (Exception e) {
            // 异常处理
            ResponseDTO tmp = new ResponseDTO();
            tmp.setMessage(message);
            result = tmp;
            if (e instanceof ServiceException) {
                MessageUtil.wrapFailure((ResponseDTO) result, (ServiceException) e);
            } else {
                MessageUtil.wrapFailure((ResponseDTO) result, e, CodeType.SERVICE_EXCEPTION, new Object[] {
                    e.getMessage()
                });
            }
        }
        return result;
    }

}
