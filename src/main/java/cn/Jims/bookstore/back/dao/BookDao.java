package cn.Jims.bookstore.back.dao;

import cn.Jims.bookstore.book.entity.Book;
import cn.Jims.bookstore.category.entity.Category;
import cn.itcast.commons.CommonUtils;
import cn.itcast.jdbc.TxQueryRunner;
import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.BeanListHandler;
import org.apache.commons.dbutils.handlers.MapHandler;
import org.apache.commons.dbutils.handlers.ScalarHandler;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

/**
 * dao层
 * Created by Jims on 2017/1/27.
 */
public class BookDao {
    private QueryRunner qr = new TxQueryRunner();

    //查询指定分类下图书的数量
    public int getCountByCid(String cid) {
        try {
            String sql = "select count(*) from book where cid=? and del=false";
            Number cnt = (Number)qr.query(sql, new ScalarHandler(), cid);
            return cnt.intValue();
        } catch(SQLException e) {
            throw new RuntimeException(e);
        }
    }

    //查询所有图书
    public List<Book> findAll() {
        try {
            String sql = "select * from book where del=false";
            return qr.query(sql, new BeanListHandler<Book>(Book.class));
        } catch(SQLException e) {
            throw new RuntimeException(e);
        }
    }

    //加载图书
    public Book findByBid(String bid) {
        try {
			/*
			 * 我们需要在Book对象中保存Category的信息
			 */
            String sql = "select * from book where bid=? and del=false";
            Map<String,Object> map = qr.query(sql, new MapHandler(), bid);
			/*
			 * 使用一个Map，映射出两个对象（一个图书详情和一个分类），再给这两个对象建立关系！
			 */
            Category category = CommonUtils.toBean(map, Category.class);
            Book book = CommonUtils.toBean(map, Book.class);
            book.setCategory(category);
            return book;
        } catch(SQLException e) {
            throw new RuntimeException(e);
        }
    }

    //添加图书
    public void add(Book book) {
        try {
            String sql = "insert into book values(?,?,?,?,?,?,?)";
            Object[] params = {book.getBid(), book.getBname(), book.getPrice(),
                    book.getAuthor(), book.getImage(), book.getCategory().getCid(),book.isDel()};
            qr.update(sql, params);
        } catch(SQLException e) {
            throw new RuntimeException(e);
        }
    }

    //修改图书
    public void edit(Book book) {
        try {
            //修改五个字段（cid是分类）
            String sql = "update book set bname=?, price=?,author=?, image=?, cid=? where bid=?";
            Object[] params = {book.getBname(), book.getPrice(),
                    book.getAuthor(), book.getImage(),
                    book.getCategory().getCid(), book.getBid()};
            qr.update(sql, params);
        } catch(SQLException e) {
            throw new RuntimeException(e);
        }
    }

    //删除图书
    public void delete(String bid) {
        try {
            String sql = "update book set del=true where bid=?";
            qr.update(sql, bid);
        } catch(SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
