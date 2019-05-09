package ${package}.util.serialization;

import java.io.InputStream;
import java.io.OutputStream;

import ${package}.util.serialization.hessian.HessianSerializer;
import ${package}.util.serialization.jdk.JavaSerializer;
import ${package}.util.serialization.json.JsonSerializer;
import ${package}.util.serialization.kryo.KryoSerializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SerializerFactory {

    private static final Logger logger = LoggerFactory.getLogger(SerializerFactory.class);

    private SerializerFactory() {
    }

    public static Serializer getSerializer(Serialization serialization) {
        if (serialization.equals(Serialization.HESSIAN)) {
            return HessianSerializer.INSTANCE;
        } else if (serialization.equals(Serialization.JSON)) {
            return JsonSerializer.INSTANCE;
        } else if (serialization.equals(Serialization.KRYO)) {
            return KryoSerializer.INSTANCE;
        } else if (serialization.equals(Serialization.OBJ_STREAM)) {
            return JavaSerializer.INSTANCE;
        }
        throw new IllegalArgumentException("Unsupported serialization.");
    }

    public static void closeInputStream(InputStream stream) {
        if (stream != null) {
            try {
                stream.close();
            } catch (Exception e) {
                logger.error("Close InputStream error:" + e.getMessage(), e);
            }
        }
    }

    public static void closeOutputStream(OutputStream stream) {
        if (stream != null) {
            try {
                stream.close();
            } catch (Exception e) {
                logger.error("Close OutputStream error:" + e.getMessage(), e);
            }
        }
    }

}
