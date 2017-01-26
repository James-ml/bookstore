package cn.Jims.bookstore.category.web.servlet;

import cn.Jims.bookstore.category.service.CategoryService;
import cn.itcast.servlet.BaseServlet;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Created by Jims on 2017/1/27.
 */
public class CategoryServlet extends BaseServlet {
    private CategoryService categoryService = new CategoryService();
    //查询所有分类
    public String findAll(HttpServletRequest request, HttpServletResponse response) throws Exception{
        request.setAttribute("categoryList",categoryService.findAll());
        return "f:/jsps/left.jsp";
    }
}
