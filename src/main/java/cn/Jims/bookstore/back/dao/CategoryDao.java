package cn.Jims.bookstore.back.dao;

import cn.Jims.bookstore.category.entity.Category;
import cn.itcast.jdbc.TxQueryRunner;
import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.BeanHandler;
import org.apache.commons.dbutils.handlers.BeanListHandler;

import java.sql.SQLException;
import java.util.List;

/**
 * Created by Jims on 2017/1/30.
 */
public class CategoryDao {
    private QueryRunner qr = new TxQueryRunner();


    //查询所有分类
    public List<Category> findAll() {
        try {
            String sql = "select * from category";
            return qr.query(sql, new BeanListHandler<Category>(Category.class));
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    //添加分类
    public void add(Category category) {
        try {
            String sql = "insert into category values(?,?)";
            qr.update(sql, category.getCid(), category.getCname());
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    //删除图书
    public void delete(String cid){
        try {
            String sql = "delete from category where cid=?";
            qr.update(sql, cid);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public Category load(String cid) {
        try {
            String sql = "select * from category where cid=?";
            return qr.query(sql, new BeanHandler<Category>(Category.class), cid);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    //修改分类
    public void edit(Category category) {
        try {
            String sql = "update category set cname=? where cid=?";
            qr.update(sql, category.getCname(), category.getCid());
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
