package cn.Jims.bookstore.order.entity;

import cn.Jims.bookstore.book.entity.Book;

/**
 * Created by Jims on 2017/1/27.
 */
public class OrderItem {
    private String iid;
    private int count;// 数量
    private double subtotal;// 小计
    private Order order;// 所属订单
    private Book book;// 所要购买的图书

    public String getIid() {
        return iid;
    }

    public void setIid(String iid) {
        this.iid = iid;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public double getSubtotal() {
        return subtotal;
    }

    public void setSubtotal(double subtotal) {
        this.subtotal = subtotal;
    }

    public Order getOrder() {
        return order;
    }

    public void setOrder(Order order) {
        this.order = order;
    }

    public Book getBook() {
        return book;
    }

    public void setBook(Book book) {
        this.book = book;
    }
}
