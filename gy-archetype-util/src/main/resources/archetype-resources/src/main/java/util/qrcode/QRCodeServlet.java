package ${package}.util.qrcode;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;
import ${package}.util.qrcode.model.QRCodeFormatEnum;
import ${package}.util.qrcode.model.QRCodeRequest;

/**
 * 功能描述：二维码生成servlet
 * 
 */
public class QRCodeServlet extends HttpServlet {

    private static final long   serialVersionUID   = 419865071466496571L;

    private static final Logger logger             = LoggerFactory.getLogger(QRCodeServlet.class);

    private static final String DEFAULT_TARGET_URL = "http://www.guanyang.org";                     // 默认url

    @Override
    protected void doGet(HttpServletRequest req,
                         HttpServletResponse resp) throws ServletException, IOException {
        QRCodeRequest param = buildParam(req);
        try {
            // 设置content-type
            resp.setContentType(QRCodeFormatEnum.getContentType(param.getFormat()));
            QRCodeUtil.writeToStream(param, resp.getOutputStream());
        } catch (Exception e) {
            logger.error("[QRCodeServlet]生成二维码异常：" + e.getMessage(), e);
        }
    }

    private QRCodeRequest buildParam(HttpServletRequest req) {
        String content = req.getParameter("content");
        String width = req.getParameter("width");
        String height = req.getParameter("height");
        String format = req.getParameter("format");
        String ecLevel = req.getParameter("ecLevel");
        QRCodeRequest param = new QRCodeRequest();
        if (StringUtils.isBlank(content)) {
            param.setContent(DEFAULT_TARGET_URL);
        } else {
            param.setContent(content);
        }
        if (StringUtils.isNumeric(width)) {
            param.setWidth(Integer.parseInt(width));
        }
        if (StringUtils.isNumeric(height)) {
            param.setHeight(Integer.parseInt(height));
        }
        QRCodeFormatEnum tmp = QRCodeFormatEnum.checkFormat(format);
        if (tmp != null) {
            param.setFormat(tmp);
        } else {
            param.setFormat(QRCodeRequest.DEFAULT_FORMAT);
        }
        ErrorCorrectionLevel level = checkCorrectionLevel(ecLevel);
        if (level != null) {
            param.setEcLevel(level);
        }
        return param;
    }

    private ErrorCorrectionLevel checkCorrectionLevel(String level) {
        for (ErrorCorrectionLevel entity : ErrorCorrectionLevel.values()) {
            if (entity.toString().equalsIgnoreCase(level)) {
                return entity;
            }
        }
        return null;
    }

    @Override
    protected void doPost(HttpServletRequest req,
                          HttpServletResponse resp) throws ServletException, IOException {
        try {
            doGet(req, resp);
        } catch (Exception e) {
            logger.error("[QRCodeServlet]生成二维码异常：" + e.getMessage(), e);
        }
    }

}
