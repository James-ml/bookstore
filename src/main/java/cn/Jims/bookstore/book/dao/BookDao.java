package cn.Jims.bookstore.book.dao;

import cn.Jims.bookstore.book.entity.Book;
import cn.itcast.jdbc.TxQueryRunner;
import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.BeanHandler;
import org.apache.commons.dbutils.handlers.BeanListHandler;

import java.sql.SQLException;
import java.util.List;

/**
 * dao层
 * Created by Jims on 2017/1/27.
 */
public class BookDao {
    private QueryRunner qr = new TxQueryRunner();

    //查询所有图书
    public List<Book> findAll() {
        try {
            String sql = "select * from book";
            return qr.query(sql, new BeanListHandler<Book>(Book.class));
        } catch (SQLException e) {
            throw new RuntimeException();
        }
    }

    //按分类查询图书(由于Book表关联了Category表，所以可以根据cid查询)
    public List<Book> findByCategory(String cid) {
        try {
            String sql = "select * from book where cid=?";
            return qr.query(sql, new BeanListHandler<Book>(Book.class), cid);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    //加载图书的方法
    public Book findById(String bid) {
        try {
            String sql = "select * from book where bid=?";
            return qr.query(sql, new BeanHandler<Book>(Book.class), bid);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
