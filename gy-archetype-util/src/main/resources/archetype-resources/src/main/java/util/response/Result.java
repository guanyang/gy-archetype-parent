package ${package}.util.response;

import java.io.Serializable;

/**
 * 用于返回业务处理结果<br>
 * 
 * @author 管阳
 */
public class Result implements Serializable {

    private static final long  serialVersionUID = 5635102161617106832L;

    public static final String SUCCESS_CODE     = ReturnCode.SUCCESS_CODE.getCode();

    /**
     * 返回码，0表示成功，其他表示失败
     */
    private String             code             = SUCCESS_CODE;

    /**
     * 返回消息
     */
    private String             message;
    /**
     * 友好返回消息，可作为页面提示
     */
    private String             friendlyMessage;

    /**
     * 返回数据
     */
    private Object             data;

    /**
     * 是否成功判断
     */
    public boolean isSuccess() {
        return SUCCESS_CODE.equals(this.code);
    }

    public void wrap(ReturnCode returnCode) {
        this.setCode(returnCode.getCode());
        this.setMessage(returnCode.getMessage());
    }

    public void wrapFriendly(ReturnCode returnCode) {
        this.setCode(returnCode.getCode());
        this.setFriendlyMessage(returnCode.getMessage());
    }

    /**
     * 获取返回码，0表示成功，其他表示失败
     * 
     * @return code 返回码，0表示成功，其他表示失败
     */
    public String getCode() {
        return code;
    }

    /**
     * 设置返回码，0表示成功，其他表示失败
     * 
     * @param code 返回码，0表示成功，其他表示失败
     */
    public void setCode(String code) {
        this.code = code;
    }

    /**
     * 获取返回消息
     * 
     * @return message 返回消息
     */
    public String getMessage() {
        return message;
    }

    /**
     * 设置返回消息
     * 
     * @param message 返回消息
     */
    public void setMessage(String message) {
        this.message = message;
    }

    /**
     * 获取友好返回消息，可作为页面提示
     * 
     * @return friendlyMessage 友好返回消息，可作为页面提示
     */
    public String getFriendlyMessage() {
        return friendlyMessage;
    }

    /**
     * 设置友好返回消息，可作为页面提示
     * 
     * @param friendlyMessage 友好返回消息，可作为页面提示
     */
    public void setFriendlyMessage(String friendlyMessage) {
        this.friendlyMessage = friendlyMessage;
    }

    /**
     * 获取返回数据
     * 
     * @return data 返回数据
     */
    public Object getData() {
        return data;
    }

    /**
     * 设置返回数据
     * 
     * @param data 返回数据
     */
    public void setData(Object data) {
        this.data = data;
    }

}
