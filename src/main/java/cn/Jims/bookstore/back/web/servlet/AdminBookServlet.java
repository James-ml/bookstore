package cn.Jims.bookstore.back.web.servlet;

import cn.Jims.bookstore.back.service.BookService;
import cn.Jims.bookstore.back.service.CategoryService;
import cn.Jims.bookstore.book.entity.Book;
import cn.Jims.bookstore.category.entity.Category;
import cn.itcast.commons.CommonUtils;
import cn.itcast.servlet.BaseServlet;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Created by Jims on 2017/1/30.
 */
public class AdminBookServlet extends BaseServlet {
    private BookService bookService = new BookService();
    private CategoryService categoryService = new CategoryService();

    //查看所有图书
    public String findAll(HttpServletRequest request, HttpServletResponse response)
            throws Exception {
        request.setAttribute("bookList", bookService.findAll());
        return "f:/adminjsps/admin/book/list.jsp";
    }

    //加载图书
    public String load(HttpServletRequest request, HttpServletResponse response)
            throws Exception {
		/*
		 * 1. 获取参数bid，通过bid调用service方法得到Book对象
		 * 2. 获取所有分类，保存到request域中
		 * 3. 保存book到request域中，转发到desc.jsp
		 */
        request.setAttribute("book", bookService.load(request.getParameter("bid")));
        request.setAttribute("categoryList", categoryService.findAll());
        return "f:/adminjsps/admin/book/desc.jsp";
    }

    //添加图书
    public String addPre(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
		/*
		 * 查询所有分类，保存到request，转发到add.jsp
		 * add.jsp中把所有的分类使用下拉列表显示在表单中
		 */
        request.setAttribute("categoryList", categoryService.findAll());
        return "f:/adminjsps/admin/book/add.jsp";
    }

    //修改图书
    public String edit(HttpServletRequest request, HttpServletResponse response)
            throws Exception {
        //request.getParameterMap()得到的表单数据没有image属性，向数据添加image值为null
        //所有请求该servlet时要添加一个image属性
        Book book = CommonUtils.toBean(request.getParameterMap(), Book.class);
        Category category = CommonUtils.toBean(request.getParameterMap(), Category.class);
        book.setCategory(category);
        bookService.edit(book);
        return findAll(request, response);
    }

    //删除图书
    public String delete(HttpServletRequest request, HttpServletResponse response)
            throws Exception {
        String bid = request.getParameter("bid");
        bookService.delete(bid);
        return findAll(request, response);
    }
}
