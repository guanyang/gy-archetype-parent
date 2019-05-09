package ${package}.util.qrcode.model;

/**
 * 功能描述：二维码输出类型枚举
 * 
 * @Author gy
 * @Date 2016年9月16日下午11:38:26
 */
public enum QRCodeFormatEnum {

    JPG, PNG, GIF;

    public static final String CONTENT_TYPE_GIF = "image/gif";
    public static final String CONTENT_TYPE_JPG = "image/jpeg";
    public static final String CONTENT_TYPE_PNG = "image/png";

    public static QRCodeFormatEnum checkFormat(String format) {
        for (QRCodeFormatEnum entity : QRCodeFormatEnum.values()) {
            if (entity.toString().equalsIgnoreCase(format)) {
                return entity;
            }
        }
        return null;
    }

    public static String getContentType(QRCodeFormatEnum entity) {
        if (JPG == entity) {
            return CONTENT_TYPE_JPG;
        } else if (GIF == entity) {
            return CONTENT_TYPE_GIF;
        } else if (PNG == entity) {
            return CONTENT_TYPE_PNG;
        }
        return null;
    }

}
