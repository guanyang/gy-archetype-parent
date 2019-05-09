package ${package}.admin.controller;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import ${package}.util.generator.Pagination;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.servlet.ModelAndView;

public class BaseController {

    protected final Logger logger = LoggerFactory.getLogger(getClass());

    public ModelAndView errorView() {
        return new ModelAndView("redirect:/error.html");
    }

    /**
     * 功能描述: 初始化分页
     * 
     * @param query
     * @param request
     */
    public void initQuery(Pagination query,
                          HttpServletRequest request) {
        String page = request.getParameter("page");
        String rows = request.getParameter("rows");
        int pageNo = 1;
        int pageSize = 10;
        if (StringUtils.isNumeric(page)) {
            pageNo = Integer.parseInt(page);
        }
        if (StringUtils.isNumeric(rows)) {
            pageSize = Integer.parseInt(rows);
        }
        query.setPageNo(pageNo);
        query.setPageSize(pageSize);
    }

}
