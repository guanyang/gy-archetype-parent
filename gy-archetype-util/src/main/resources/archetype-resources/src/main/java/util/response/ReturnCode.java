package ${package}.util.response;

/**
 * 功能描述：返回码声明
 * <p>
 * <strong>说明：</strong>
 * </p>
 * <ul>
 * <li>如果需要明确错误类型，需要定义具体状态码</li>
 * <li>如果不需要明确错误类型，使用全局状态码即可，可补充</li>
 * <li>状态码枚举格式：ERROR_{状态码}</li>
 * </ul>
 * 
 */
public enum ReturnCode {

    SUCCESS_CODE("0", "操作成功"),

    /********************** 全局状态码 start **********************/

    ERROR_9997("9997", "业务数据错误"),

    ERROR_9998("9998", "参数错误"),

    ERROR_9999("9999", "系统异常");

    /********************** 全局状态码 end **********************/

    /**
     * 返回码
     */
    private String code;
    /**
     * 返回消息
     */
    private String message;

    private ReturnCode(String code, String message) {
        this.code = code;
        this.message = message;
    }

    /**
     * 获取返回码
     * 
     * @return code 返回码
     */
    public String getCode() {
        return code;
    }

    /**
     * 设置返回码
     * 
     * @param code 返回码
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

}
