package ${package}.service.impl;

import ${package}.dto.BaseDTO;
import ${package}.dto.test.TestRequestDTO;
import ${package}.dto.test.TestResponseDTO;
import ${package}.service.TestService;
import ${package}.util.ResponseDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service("testService")
public class TestServiceImpl implements TestService {

    protected final Logger logger = LoggerFactory.getLogger(getClass());

    @Override
    public ResponseDTO<BaseDTO> test() {
        ResponseDTO<BaseDTO> res = new ResponseDTO<BaseDTO>();
        BaseDTO dto = new BaseDTO();
        dto.setSuccess(true);
        dto.setMessage("测试");
        logger.debug("测试service日志");
        res.setResult(dto);
        return res;
    }

    @Override
    public ResponseDTO<TestResponseDTO> test(TestRequestDTO dto) {
        ResponseDTO<TestResponseDTO> res = new ResponseDTO<TestResponseDTO>();
        TestResponseDTO responseDTO = new TestResponseDTO();
        responseDTO.setMessage(dto.getName());
        res.setResult(responseDTO);
        return res;
    }

}
