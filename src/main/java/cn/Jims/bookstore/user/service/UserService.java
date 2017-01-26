package cn.Jims.bookstore.user.service;

import cn.Jims.bookstore.user.UserException.UserException;
import cn.Jims.bookstore.user.dao.UserDao;
import cn.Jims.bookstore.user.entity.User;

/**
 * user业务层
 * Created by Jims on 2017/1/26.
 */
public class UserService {
    UserDao userDao = new UserDao();

    /*
    注册功能
     */
    public void regist(User form) throws UserException {
        //校验用户名
        User user = userDao.findByUsername(form.getUsername());
        if (user != null) {
            throw new UserException("用户名已被注册！");
        }
        // 校验email
        user = userDao.findByEmail(form.getEmail());
        if (user != null) {
            throw new UserException("Email已被注册！");
        }
        //把用户插入到数据库
        userDao.add(form);
    }

    /*
    激活功能
     */
    public void active(String code) throws UserException {
        //1. 使用code查询数据库，得到user
        User user = userDao.findByCode(code);
        //2. 如果user不存在，说明激活码错误
        if (user == null) {
            throw new UserException("激活码无效！");
        }
        //3. 校验用户的状态是否为未激活状态，如果已激活，说明是二次激活，抛出异常
        if (user.isState()) {
            throw new UserException("你已经激活过了，请不要在激活！");
        }
        //4. 修改用户的状态
        userDao.updateState(user.getUid(), true);
    }

    /*
    登录功能
    dao有findByUsername(String username)方法，不用再写dao
     */
    public User login(User form2) throws UserException {
        /*
         * 1. 使用username查询，得到User
		 * 2. 如果user为null，抛出异常（用户名不存在）
		 * 3. 比较form和user的密码，若不同，抛出异常（密码错误）
		 * 4. 查看用户的状态，若为false，抛出异常（尚未激活）
		 * 5. 返回user
		 */
        User user = userDao.findByUsername(form2.getUsername());
        if (user == null) {
            throw new UserException("用户名不存在！");
        }
        if (!user.getPassword().equals(form2.getPassword())) {
            throw new UserException("密码错误！");
        }
        if (!user.isState()) {
            throw new UserException("你还未激活邮箱！");
        }
        return user;
    }
}
