package cn.Jims.bookstore.user.web.servlet;

import cn.Jims.bookstore.user.UserException.UserException;
import cn.Jims.bookstore.user.entity.User;
import cn.Jims.bookstore.user.service.UserService;
import cn.itcast.commons.CommonUtils;
import cn.itcast.mail.Mail;
import cn.itcast.mail.MailUtils;
import cn.itcast.servlet.BaseServlet;

import javax.mail.Session;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.text.MessageFormat;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

/**
 * user表述层
 * Created by Jims on 2017/1/26.
 * BaseServlet继承了HttpServlet，并重写了service方法
 */
public class UserServlet extends BaseServlet {
    private UserService userService = new UserService();

    /*
    注册功能
     */
    public String regist(HttpServletRequest request, HttpServletResponse response)
            throws Exception {
        /*
         * 1. 封装表单数据到form对象中
		 * 2. 补全表单数据：uid、code
		 * 3. 输入校验
		 *   > 保存错误信息、form到request域，转发到regist.jsp
		 * 4. 调用service方法完成注册
		 *   > 保存错误信息、form到request域，转发到regist.jsp
		 * 5. 发邮件
		 * 6. 保存成功信息转发到msg.jsp
		 */
        //封装表单数据
        User form = CommonUtils.toBean(request.getParameterMap(), User.class);
        //补全表单数据
        form.setUid(CommonUtils.uuid());
        form.setCode(CommonUtils.uuid() + CommonUtils.uuid());//在表单里添加了code
        //输入校验
        //1. 创建一个Map，用来封装错误信息，其中key为表单字段名称，值为错误信息
        Map<String, String> errors = new HashMap<String, String>();
        //2. 获取form中的username、password、email进行校验
        String username = form.getUsername();
        if (username == null || username.trim().isEmpty()) {
            errors.put("username", "用户名不能为空！");
        } else if (username.length() < 3 || username.length() > 10) {
            errors.put("username", "用户名长度在3-10之间！");
        }

        String password = form.getPassword();
        if (password == null || password.trim().isEmpty()) {
            errors.put("password", "密码不能为空！");
        } else if (password.length() < 3 || password.length() > 10) {
            errors.put("password", "密码长度必须在3~10之间！");
        }

        String email = form.getEmail();
        if (email == null || email.trim().isEmpty()) {
            errors.put("email", "Email不能为空！");
        } else if (!email.matches("\\w+@\\w+\\.\\w+")) {//email的正则表达式的邮件格式
            errors.put("email", "Email格式错误！");
        }

        //3. 判断是否存在错误信息
        if (errors.size() > 0) {
            // 1. 保存错误信息
            request.setAttribute("errors", errors);
            // 2. 保存表单数据(回显)
            request.setAttribute("form", form);
            // 3. 转发到regist.jsp
            return "f:/jsps/user/regist.jsp";
        }
        //调用service的regist()方法
        try {
            userService.regist(form);
        } catch (UserException e) {
            //1. 保存异常信息
            request.setAttribute("msg", e.getMessage());
            //2. 保存form
            request.setAttribute("form", form);
            //3. 转发到regist.jsp
            return "f:/jsps/user/regist.jsp";
        }
        /*
        发邮件
        准备配置文件
         */
        //获取配置文件的内容
        Properties props = new Properties();
        props.load(this.getClass().getClassLoader().getResourceAsStream("email.properties"));
        String host = props.getProperty("host");//获取服务器主机
        String uname = props.getProperty("uname");//获取用户名
        String pwd = props.getProperty("pwd");//获取密码
        String from = props.getProperty("from");//获取发件人
        String to = form.getEmail();//获取收件人
        String subject = props.getProperty("subject");//获取主题
        String content = props.getProperty("content");//获取邮件内容
        content = MessageFormat.format(content, form.getCode());//替换{0}

        Session session = MailUtils.createSession(host, uname, pwd);//得到session
        Mail mail = new Mail(from, to, subject, content);//创建邮件对象
        MailUtils.send(session, mail);//发邮件

        request.setAttribute("msg", "恭喜，注册成功,请马上到邮箱激活！");
        return "f:/jsps/msg.jsp";
    }

    /*
    激活功能
     */
    public String active(HttpServletRequest request, HttpServletResponse response) throws Exception {
        /*
		 * 1. 获取参数激活码
		 * 2. 调用service方法完成激活
		 *   > 保存异常信息到request域，转发到msg.jsp
		 * 3. 保存成功信息到request域，转发到msg.jsp
		 */
        String code = request.getParameter("code");
        try {
            userService.active(code);
            request.setAttribute("msg", "恭喜你，激活成功，请马上登录！");
        } catch (UserException e) {
            request.setAttribute("msg", e.getMessage());
        }
        return "f:/jsps/msg.jsp";
    }

    /*
    登录功能
     */
    public String login(HttpServletRequest request, HttpServletResponse response) {
        /*
		 * 1. 封装表单数据到form中
		 * 2. 输入校验
		 * 3. 调用service完成激活
		 *   > 保存错误信息、form到request，转发到login.jsp
		 * 4. 保存用户信息到session中，然后重定向到index.jsp
		 */
        User form2 = CommonUtils.toBean(request.getParameterMap(), User.class);
        //输入校验
        //1. 创建一个Map，用来封装错误信息，其中key为表单字段名称，值为错误信息
        Map<String, String> errors = new HashMap<String, String>();
        //2. 获取form中的username、password、email进行校验
        String username = form2.getUsername();
        if (username == null || username.trim().isEmpty()) {
            errors.put("username", "用户名不能为空！");
        } else if (username.length() < 3 || username.length() > 10) {
            errors.put("username", "用户名长度在3-10之间！");
        }

        String password = form2.getPassword();
        if (password == null || password.trim().isEmpty()) {
            errors.put("password", "密码不能为空！");
        } else if (password.length() < 3 || password.length() > 10) {
            errors.put("password", "密码长度必须在3~10之间！");
        }


        //3. 判断是否存在错误信息
        if (errors.size() > 0) {
            // 1. 保存错误信息
            request.setAttribute("errors", errors);
            // 2. 保存表单数据(回显)
            request.setAttribute("form", form2);
            // 3. 转发到regist.jsp
            return "f:/jsps/user/login.jsp";
        }

        try {
            User user = userService.login(form2);
            //为了确保上次访问的安全，先销毁之前的session
            request.getSession().invalidate();
            request.getSession().setAttribute("session_user", user);
            return "r:/index.jsp";//重定向
        } catch (UserException e) {
            request.setAttribute("msg", e.getMessage());
            request.setAttribute("form2", form2);//回显
            return "f:/jsps/user/login.jsp";
        }
    }

    /*
    退出功能
     */
    public String quit(HttpServletRequest request, HttpServletResponse response)
            throws Exception {
        //销毁session
        request.getSession().invalidate();
        return "r:/index.jsp";
    }
}
