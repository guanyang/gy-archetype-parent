package ${package}.util;

import java.text.MessageFormat;

/**
 * 统一消息枚举
 */
public enum CodeType {

    SUCCESS("0", "操作成功"),

    PARAM_NULL("10001", "参数异常：{0}不能为空"),

    PARAM_ERROR("10002", "参数异常：{0}"),

    DATA_NULL("20001", "数据异常：{0}不能为空"),

    DATA_ERROR("20002", "数据异常：{0}"),

    SERVICE_EXCEPTION("90001", "服务异常：{0}"),

    CUSTOM_ERROR("90002", "{0}"),

    UNKNOW_ERROR("99999", "系统繁忙");

    public static void main(String[] args) {
        System.out.println(getFormatMessage(CUSTOM_ERROR, "测试消息"));
        System.out.println(PARAM_NULL.getFormatMessage("商品编码"));
    }

    public String getFormatMessage(Object... placeholder) {
        return getFormatMessage(this, placeholder);
    }

    public String getFormatMessageWithoutCode(Object... placeholder) {
        return getFormatMessageWithoutCode(this, placeholder);
    }

    public boolean isSuccess() {
        return SUCCESS.getCode().equals(this.getCode());
    }

    public static boolean isSuccess(String code) {
        return SUCCESS.getCode().equals(code);
    }

    /**
     * 获取格式化消息，带错误码
     * 
     * @param type
     * @param placeholder
     * @return
     */
    public static String getFormatMessage(CodeType type,
                                          Object... placeholder) {
        return "[" + type.getCode() + "]" + getFormatMessageWithoutCode(type, placeholder);
    }

    /**
     * 获取格式化消息，不带错误码
     * 
     * @param type
     * @param placeholder
     * @return
     */
    public static String getFormatMessageWithoutCode(CodeType type,
                                                     Object... placeholder) {
        return MessageFormat.format(type.getPattern(), placeholder);
    }

    /**
     * 标识码
     */
    private String code;
    /**
     * 消息格式
     */
    private String pattern;

    CodeType(String code, String pattern) {
        this.code = code;
        this.pattern = pattern;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getPattern() {
        return pattern;
    }

    public void setPattern(String pattern) {
        this.pattern = pattern;
    }

}
