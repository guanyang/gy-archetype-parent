package ${package}.service;

import ${package}.dto.BaseDTO;
import ${package}.dto.test.TestRequestDTO;
import ${package}.dto.test.TestResponseDTO;
import ${package}.util.ResponseDTO;

public interface TestService {

    ResponseDTO<BaseDTO> test();

    ResponseDTO<TestResponseDTO> test(TestRequestDTO dto);

}
