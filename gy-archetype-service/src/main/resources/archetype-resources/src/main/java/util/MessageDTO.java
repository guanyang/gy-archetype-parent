package ${package}.util;

import java.io.Serializable;

public final class MessageDTO implements Serializable {

    private static final long serialVersionUID = 8068706366313933859L;

    /**
     * 返回码
     */
    private String            code;
    /**
     * 返回信息
     */
    private String            message;

    /**
     * 调用事务ID
     */
    private String            callId;

    /**
     * 调用时间戳
     */
    private long              timestamp;

    /**
     * 方法执行耗时
     */
    private long              cost;

    /**
     * 中台服务器的ip
     */
    private String            ip;

    /**
     * 中台服务器的hostName
     */
    private String            hostName;

    @Override
    public String toString() {
        return "MessageDTO [code=" + code + ", message=" + message + ", callId=" + callId + ", timestamp=" + timestamp + ", cost=" + cost + ", ip=" + ip + ", hostName=" + hostName + "]";
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getCallId() {
        return callId;
    }

    public void setCallId(String callId) {
        this.callId = callId;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    public long getCost() {
        return cost;
    }

    public void setCost(long cost) {
        this.cost = cost;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public String getHostName() {
        return hostName;
    }

    public void setHostName(String hostName) {
        this.hostName = hostName;
    }

}
