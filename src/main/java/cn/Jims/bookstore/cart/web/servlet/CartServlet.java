package cn.Jims.bookstore.cart.web.servlet;

import cn.Jims.bookstore.book.entity.Book;
import cn.Jims.bookstore.book.service.BookService;
import cn.Jims.bookstore.cart.entity.Cart;
import cn.Jims.bookstore.cart.entity.CartItem;
import cn.itcast.servlet.BaseServlet;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Created by Jims on 2017/1/27.
 */
public class CartServlet extends BaseServlet {
    //添加购物条目
    public String add(HttpServletRequest request, HttpServletResponse response)
            throws Exception {
        /*
		 * 1. 得到车
		 * 2. 得到条目（得到图书和数量）
		 * 3. 把条目添加到车中
		 */
        //1. 得到车
        Cart cart = (Cart) request.getSession().getAttribute("cart");

        if (cart==null){
            //回显未解决？
            request.setAttribute("msg","你还未登录，请先登录！");
            return "f:/jsps/book/desc.jsp";
        }
        /*
         * 表单传递的只有bid和数量
         * 2. 得到条目
         *   > 得到图书和数量
         *   > 先得到图书的bid，然后我们需要通过bid查询数据库得到Book
         *   > 数量表单中有
         */
        String bid = request.getParameter("bid");
        Book book = new BookService().load(bid);
        int count = Integer.parseInt(request.getParameter("count"));
        CartItem cartItem = new CartItem();
        cartItem.setBook(book);
        cartItem.setCount(count);

        //3.把条目添加到购物车
        cart.add(cartItem);
        return "f:/jsps/cart/list.jsp";
    }

    //清空购物条目
    public String clear(HttpServletRequest request, HttpServletResponse response)
            throws Exception {
        /*
         * 1. 得到车
         * 2. 设置车的clear
         */
        Cart cart = (Cart)request.getSession().getAttribute("cart");
        cart.clear();
        return "f:/jsps/cart/list.jsp";
    }

    //删除购物条目
    public String delete(HttpServletRequest request, HttpServletResponse response)
            throws Exception {
		/*
		 * 1. 得到车
		 * 2. 得到要删除的bid
		 */
        Cart cart = (Cart)request.getSession().getAttribute("cart");
        String bid = request.getParameter("bid");
        cart.delete(bid);
        return "f:/jsps/cart/list.jsp";
    }
}
