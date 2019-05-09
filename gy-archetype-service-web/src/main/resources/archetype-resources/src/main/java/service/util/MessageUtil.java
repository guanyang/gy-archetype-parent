package ${package}.service.util;

import java.util.UUID;

import ${package}.util.CodeType;
import ${package}.util.MessageDTO;
import ${package}.util.ResponseDTO;
import ${package}.util.ServiceException;
import ${package}.util.ip.IPUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MessageUtil {

    private static final Logger logger = LoggerFactory.getLogger(MessageUtil.class);

    public static void wrapSuccess(ResponseDTO result) {
        MessageDTO message = result.getMessage();
        buildMessage(message, CodeType.SUCCESS);
        result.setMessage(message);
    }

    public static void wrapFailure(ResponseDTO result,
                                   ServiceException e) {
        CodeType type = e.getCodeType();
        Object[] placeholder = e.getPlaceholder();
        wrapFailure(result, e, type, placeholder);
    }

    public static void wrapFailure(ResponseDTO result,
                                   Exception e,
                                   CodeType type,
                                   Object... placeholder) {
        MessageDTO message = result.getMessage();
        buildMessage(message, type, placeholder);
        result.setMessage(message);
        // 输出错误日志
        logger.error("[" + message.getCallId() + "]" + message.getMessage(), e);
    }

    public static void buildMessage(MessageDTO message,
                                    CodeType type,
                                    Object... placeholder) {
        if (null == type) {
            type = CodeType.UNKNOW_ERROR;
        }
        message.setCode(type.getCode());
        String formatMessage = null;
        if (placeholder == null) {
            formatMessage = type.getFormatMessageWithoutCode("");
        } else {
            formatMessage = type.getFormatMessageWithoutCode(placeholder);
        }
        message.setMessage(formatMessage);
        if (message.getCallId() == null) {
            message.setCallId(UUID.randomUUID().toString());
        }
        if (message.getTimestamp() == 0) {
            message.setTimestamp(System.currentTimeMillis());
        }
        message.setIp(IPUtil.getIp());
        message.setHostName(IPUtil.getHostName());
    }

}
