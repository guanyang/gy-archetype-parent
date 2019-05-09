package ${package}.dto.test;

import java.io.Serializable;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.hibernate.validator.constraints.Range;

public class TestRequestDTO implements Serializable {

    private static final long serialVersionUID = -4994558447214124694L;

    /**
     * 名称
     */
    @NotNull(message = "名称不能为空")
    @Size(min = 2, max = 12, message = "名称长度必须是2至12个字符")
    private String            name;

    /**
     * 年龄
     */
    @NotNull(message = "年龄不能为空")
    @Range(min = 20, max = 40, message = "年龄必须介于20至40岁之间")
    private Integer           age;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

}
