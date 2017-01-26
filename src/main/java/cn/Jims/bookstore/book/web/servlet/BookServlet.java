package cn.Jims.bookstore.book.web.servlet;

import cn.Jims.bookstore.book.service.BookService;
import cn.itcast.servlet.BaseServlet;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Created by Jims on 2017/1/27.
 */
public class BookServlet extends BaseServlet {
    private BookService bookService = new BookService();

    //查询所有图书
    public String findAll(HttpServletRequest request, HttpServletResponse response) throws Exception{
        request.setAttribute("bookList",bookService.findAll());
        return "f:/jsps/book/list.jsp";
    }

    //按分类查询图书
    public String findByCategory(HttpServletRequest request, HttpServletResponse response)
            throws Exception {
        String cid = request.getParameter("cid");
        request.setAttribute("bookList",bookService.findByCategory(cid));
        return "f:/jsps/book/list.jsp";
    }

    //加载图书
    public String load(HttpServletRequest request, HttpServletResponse response)
            throws Exception {
        /*
		 * 1. 得到参数bid
		 * 2. 查询得到Book
		 * 3. 保存，转发到desc.jsp
		 */
        String bid = request.getParameter("bid");
        request.setAttribute("book",bookService.load(bid));
        return "f:/jsps/book/desc.jsp";
    }
}
