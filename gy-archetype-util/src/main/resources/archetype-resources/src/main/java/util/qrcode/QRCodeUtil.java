package ${package}.util.qrcode;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URI;
import java.util.EnumMap;

import javax.imageio.ImageIO;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.BinaryBitmap;
import com.google.zxing.ChecksumException;
import com.google.zxing.DecodeHintType;
import com.google.zxing.EncodeHintType;
import com.google.zxing.FormatException;
import com.google.zxing.LuminanceSource;
import com.google.zxing.NotFoundException;
import com.google.zxing.ReaderException;
import com.google.zxing.Result;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.BufferedImageLuminanceSource;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.common.HybridBinarizer;
import com.google.zxing.qrcode.QRCodeReader;
import com.google.zxing.qrcode.QRCodeWriter;
import ${package}.util.qrcode.model.QRCodeRequest;

/**
 * 功能描述：二维码生成工具类
 * 
 * @Author gy
 * @Date 2016年9月17日上午12:43:02
 */
public class QRCodeUtil {

    public static final String        DEFAULT_CHARSET = "utf-8";

    private static final QRCodeWriter qrCodeWriter    = new QRCodeWriter();

    private static final QRCodeReader qrCodeReader    = new QRCodeReader();

    private QRCodeUtil() {
    }

    /**
     * 功能描述: 生成二维码到流
     * 
     * @param param
     * @param stream
     * @throws IOException
     * @throws WriterException
     * @Author gy
     * @Date 2016年9月17日上午1:31:59
     */
    public static void writeToStream(QRCodeRequest param,
                                     OutputStream stream) throws IOException, WriterException {
        BitMatrix matrix = buildBitMatrix(param);
        String format = param.getFormat().toString();
        MatrixToImageWriter.writeToStream(matrix, format, stream);
    }

    /**
     * 功能描述: 生成二维码到文件
     * 
     * @param param
     * @param file
     * @throws IOException
     * @throws WriterException
     * @Author gy
     * @Date 2016年9月17日上午1:32:34
     */
    public static void writeToFile(QRCodeRequest param,
                                   File file) throws IOException, WriterException {
        BitMatrix matrix = buildBitMatrix(param);
        String format = param.getFormat().toString();
        MatrixToImageWriter.writeToPath(matrix, format, file.toPath());
    }

    private static BitMatrix buildBitMatrix(QRCodeRequest param) throws WriterException {
        EnumMap<EncodeHintType, Object> hints = new EnumMap(EncodeHintType.class);
        hints.put(EncodeHintType.ERROR_CORRECTION, param.getEcLevel());
        hints.put(EncodeHintType.CHARACTER_SET, DEFAULT_CHARSET);
        hints.put(EncodeHintType.MARGIN, param.getMargin());
        return qrCodeWriter.encode(param.getContent(), BarcodeFormat.QR_CODE, param.getWidth(), param.getHeight(), hints);

    }

    /**
     * 功能描述: 从流解析二维码
     * 
     * @param stream
     * @return
     * @throws IOException
     * @throws NotFoundException
     * @throws ChecksumException
     * @throws FormatException
     * @Author gy
     * @Date 2016年9月17日上午2:17:39
     */
    public static Result readFromStream(InputStream stream) throws IOException, ReaderException {
        BinaryBitmap image = buildBinaryBitmap(stream);
        return decode(image);
    }

    /**
     * 功能描述: 从文件解析二维码
     * 
     * @param file
     * @return
     * @throws IOException
     * @throws NotFoundException
     * @throws ChecksumException
     * @throws FormatException
     * @Author gy
     * @Date 2016年9月17日上午2:22:24
     */
    public static Result readFromFile(File file) throws IOException, ReaderException {
        BinaryBitmap image = buildBinaryBitmap(file);
        return decode(image);
    }

    /**
     * 功能描述: 从uri解析二维码
     * 
     * @param uri
     * @return
     * @throws IOException
     * @throws NotFoundException
     * @throws ChecksumException
     * @throws FormatException
     * @Author gy
     * @Date 2016年9月17日上午2:23:41
     */
    public static Result readFromUri(URI uri) throws IOException, ReaderException {
        BinaryBitmap image = buildBinaryBitmap(uri);
        return decode(image);
    }

    private static Result decode(BinaryBitmap image) throws ReaderException {
        EnumMap<DecodeHintType, Object> hints = new EnumMap(DecodeHintType.class);
        hints.put(DecodeHintType.CHARACTER_SET, DEFAULT_CHARSET);
        return qrCodeReader.decode(image, hints);
    }

    private static BinaryBitmap buildBinaryBitmap(BufferedImage image) {
        LuminanceSource source = new BufferedImageLuminanceSource(image);
        return new BinaryBitmap(new HybridBinarizer(source));
    }

    private static BinaryBitmap buildBinaryBitmap(InputStream stream) throws IOException {
        BufferedImage image = ImageIO.read(stream);
        return buildBinaryBitmap(image);
    }

    private static BinaryBitmap buildBinaryBitmap(URI uri) throws IOException {
        BufferedImage image = ImageIO.read(uri.toURL());
        return buildBinaryBitmap(image);
    }

    private static BinaryBitmap buildBinaryBitmap(File file) throws IOException {
        BufferedImage image = ImageIO.read(file);
        return buildBinaryBitmap(image);
    }

}
