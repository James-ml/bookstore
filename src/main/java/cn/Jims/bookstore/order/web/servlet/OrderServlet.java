package cn.Jims.bookstore.order.web.servlet;

import cn.Jims.bookstore.cart.entity.Cart;
import cn.Jims.bookstore.cart.entity.CartItem;
import cn.Jims.bookstore.order.OrderException.OrderException;
import cn.Jims.bookstore.order.entity.Order;
import cn.Jims.bookstore.order.entity.OrderItem;
import cn.Jims.bookstore.order.service.OrderService;
import cn.Jims.bookstore.user.entity.User;
import cn.itcast.commons.CommonUtils;
import cn.itcast.servlet.BaseServlet;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by Jims on 2017/1/27.
 */
public class OrderServlet extends BaseServlet {
    private OrderService orderService = new OrderService();

    //添加订单
    //把session中的购物车用来生成Order对象
    public String add(HttpServletRequest request, HttpServletResponse response)
            throws Exception {
        /*
         * 1. 从session中得到cart
		 * 2. 使用cart生成Order对象
		 * 3. 调用service方法完成添加订单
		 * 4. 保存order到request域中，转发到/jsps/order/desc.jsp
		 */
        // 从session中获取cart
        Cart cart = (Cart) request.getSession().getAttribute("cart");
        // 把cart转换成Order对象
        //创建Order对象，并设置当前属性
        Order order = new Order();
        order.setOid(CommonUtils.uuid());//设置编号
        order.setOrdertime(new Date());//设置下单时间
        order.setState(1);//设置订单状态为1，表示未付款
        User user = (User) request.getSession().getAttribute("session_user");
        order.setOwner(user);//设置订单所属者
        order.setTotal(cart.getTotal());//设置订单的合计，从cart中获取合计

        //创建订单条目集合
        List<OrderItem> orderItemList = new ArrayList<OrderItem>();
        // 循环遍历Cart中的所有CartItem，
        // 使用每一个CartItem的属性设置到OrderItem对象，并添加到集合中
        for (CartItem cartItem : cart.getCartItems()) {
            OrderItem oi = new OrderItem();//创建订单条目

            oi.setIid(CommonUtils.uuid());//设置条目的id
            oi.setCount(cartItem.getCount());//设置条目的数量
            oi.setBook(cartItem.getBook());//设置条目的图书
            oi.setSubtotal(cartItem.getSubtotal());//设置条目的小计
            oi.setOrder(order);//设置所属订单

            orderItemList.add(oi);//把订单条目添加到集合中
        }
        // 把所有的订单条目添加到订单中
        order.setOrderItemList(orderItemList);

        // 清空购物车
        cart.clear();

        //调用orderService添加订单
        orderService.add(order);

        //保存order到request域，转发到/jsps/order/desc.jsp
        request.setAttribute("order", order);
        return "f:/jsps/order/desc.jsp";
    }

    //我的订单
    public String myOrders(HttpServletRequest request, HttpServletResponse response)
            throws Exception {
		/*
		 * 1. 从session得到当前用户，再获取其uid
		 * 2. 使用uid调用orderService的myOrders(uid)方法得到该用户的所有订单List<Order>
		 * 3. 把订单列表保存到request域中，转发到/jsps/order/list.jsp
		 */
        User user = (User)request.getSession().getAttribute("session_user");
        List<Order> orderList = orderService.myOrders(user.getUid());
        request.setAttribute("orderList", orderList);
        return "f:/jsps/order/list.jsp";
    }

    //加载订单
    public String load(HttpServletRequest request, HttpServletResponse response)
            throws Exception {
        /*
		 * 1. 得到oid参数
		 * 2. 使用oid调用service方法得到Order
		 * 3. 保存到request域，转发到/jsps/order/desc.jsp
		 */
        request.setAttribute("order", orderService.load(request.getParameter("oid")));
        return "f:/jsps/order/desc.jsp";
    }
    //确认收货
    public String confirm(HttpServletRequest request, HttpServletResponse response)
            throws Exception {
        /*
		 * 1. 获取oid参数
		 * 2. 调用service方法
		 *   > 如果有异常，保存异常信息，转发到msg.jsp
		 * 3. 保存成功信息，转发到msg.jsp
		 */
        String oid = request.getParameter("oid");
        try {
            orderService.confirm(oid);
            request.setAttribute("msg", "恭喜，交易成功！");
        } catch (OrderException e) {
            request.setAttribute("msg", e.getMessage());
        }
        return "f:/jsps/msg.jsp";
    }
}
