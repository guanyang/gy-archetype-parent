package ${package}.admin.controller;

import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.json.MappingJacksonJsonView;

@Controller
@RequestMapping("/test")
public class TestController {

    protected final Logger logger = LoggerFactory.getLogger(getClass());

    @RequestMapping(value = "/index", method = RequestMethod.GET)
    public ModelAndView test() {
        ModelAndView mav = new ModelAndView();
        mav.setViewName("admin/test.ftl");
        mav.addObject("time", new Date());
        return mav;
    }

    @RequestMapping(value = "/json", method = RequestMethod.GET)
    public ModelAndView json() {
        ModelAndView mav = new ModelAndView(new MappingJacksonJsonView());
        mav.addObject("time", new Date());
        logger.debug("测试日志");
        return mav;
    }

}
