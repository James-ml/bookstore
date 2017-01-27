package cn.Jims.bookstore.cart.entity;

import cn.Jims.bookstore.book.entity.Book;

import java.math.BigDecimal;

/**
 * 购物车条目类
 * Created by Jims on 2017/1/27.
 */
public class CartItem {
    private Book book;//商品
    private int count;//数量

    /*
    小计方法
     */
    public double getSubtotal(){
        //处理了二进制运算误差问题(大浮点型)
        BigDecimal d1 = new BigDecimal(book.getPrice()+"");//转字符串
        BigDecimal d2 = new BigDecimal(count+"");
        return d1.multiply(d2).doubleValue();
    }

    public Book getBook() {
        return book;
    }

    public void setBook(Book book) {
        this.book = book;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }
}
