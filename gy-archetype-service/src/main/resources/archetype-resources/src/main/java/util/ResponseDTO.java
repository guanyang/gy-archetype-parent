package ${package}.util;

import java.io.Serializable;

public final class ResponseDTO<T> implements Serializable {

    private static final long serialVersionUID = -3343183091924503527L;

    /**
     * 统一信息DTO
     */
    private MessageDTO        message;

    /**
     * 结果集
     */
    private T                 result;

    public MessageDTO getMessage() {
        return message;
    }

    public void setMessage(MessageDTO message) {
        this.message = message;
    }

    public T getResult() {
        return result;
    }

    public void setResult(T result) {
        this.result = result;
    }

}
