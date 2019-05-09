package ${package}.util;

public class ServiceException extends RuntimeException {

    private static final long serialVersionUID = -2916048734404659696L;

    private CodeType          codeType;

    private Object[]          placeholder;
    
    public ServiceException(Throwable e){
        super(e);
    }

    public ServiceException(CodeType codeType, Object... placeholder) {
        super();
        this.codeType = codeType;
        this.placeholder = placeholder;
    }

    public ServiceException(Throwable e, CodeType codeType, Object... placeholder) {
        super(e);
        this.codeType = codeType;
        this.placeholder = placeholder;
    }

    public CodeType getCodeType() {
        return codeType;
    }

    public void setCodeType(CodeType codeType) {
        this.codeType = codeType;
    }

    public Object[] getPlaceholder() {
        return placeholder;
    }

    public void setPlaceholder(Object[] placeholder) {
        this.placeholder = placeholder;
    }

}
