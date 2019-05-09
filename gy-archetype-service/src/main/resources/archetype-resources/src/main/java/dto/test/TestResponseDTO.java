package ${package}.dto.test;

import java.io.Serializable;

public class TestResponseDTO implements Serializable {

    private static final long serialVersionUID = -5364350977216004383L;

    private String            message;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

}
