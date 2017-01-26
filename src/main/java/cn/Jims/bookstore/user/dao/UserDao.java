package cn.Jims.bookstore.user.dao;

import cn.Jims.bookstore.user.entity.User;
import cn.itcast.jdbc.TxQueryRunner;
import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.BeanHandler;

import java.sql.SQLException;

/**
 * user持久层
 * Created by Jims on 2017/1/26.
 */
public class UserDao {
    //DBUtils工具类，简化jdbc的操作
    private QueryRunner qr = new TxQueryRunner();

    /*
    根据用户名查询
     */
    public User findByUsername(String username) {
        try {
            String sql = "select * from tb_user where username=?";
            return qr.query(sql,new BeanHandler<User>(User.class),username);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    /*
    根据email查询
     */
    public User findByEmail(String email) {
        try {
            String sql = "select * from tb_user where email=?";
            return qr.query(sql,new BeanHandler<User>(User.class),email);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    /*
    向数据库添加user对象
     */
    public void add(User user) {
        try {
            String sql = "insert into tb_user values(?,?,?,?,?,?)";
            Object[] params = {user.getUid(),user.getUsername(),user.getPassword(),
            user.getEmail(),user.getCode(),user.isState()};
            qr.update(sql,params);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    /*
    根据激活码查询
     */
    public User findByCode(String code) {
        try {
            String sql = "select * from tb_user where code=?";
            return qr.query(sql,new BeanHandler<User>(User.class),code);
        } catch(SQLException e) {
            throw new RuntimeException(e);
        }
    }

    /*
    修改指定用户的指定状态
     */
    public void updateState(String uid, boolean state) {
        try {
            String sql = "update tb_user set state=? where uid=?";
            qr.update(sql,state,uid);
        } catch(SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
