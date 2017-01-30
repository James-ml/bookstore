package cn.Jims.bookstore.back.web.servlet;

import cn.Jims.bookstore.back.CategoryException.CategoryException;
import cn.Jims.bookstore.back.service.CategoryService;
import cn.Jims.bookstore.category.entity.Category;
import cn.itcast.commons.CommonUtils;
import cn.itcast.servlet.BaseServlet;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Created by Jims on 2017/1/30.
 */
public class AdminCategoryServlet extends BaseServlet {
    private CategoryService categoryService = new CategoryService();

    //查看所有分类
    public String findAll(HttpServletRequest request, HttpServletResponse response)
            throws Exception {
		/*
		 * 1. 调用service方法，得到所有分类
		 * 2. 保存到request域，转发到/adminjsps/admin/category/list.jsp
		 */
        request.setAttribute("categoryList", categoryService.findAll());
        return "f:/adminjsps/admin/category/list.jsp";
    }

    //添加分类
    public String add(HttpServletRequest request, HttpServletResponse response)
            throws Exception {
		/*
		 * 1. 封装表单数据
		 * 2. 补全：cid
		 * 3. 调用service方法完成添加工作
		 * 4. 调用findAll()
		 */
        Category category = CommonUtils.toBean(request.getParameterMap(), Category.class);
        category.setCid(CommonUtils.uuid());

        categoryService.add(category);

        return findAll(request, response);
    }

    //修改分类之前的加载工作
    public String editPre(HttpServletRequest request, HttpServletResponse response)
            throws Exception {
        String cid = request.getParameter("cid");
        request.setAttribute("category", categoryService.load(cid));
        return "f:/adminjsps/admin/category/mod.jsp";
    }

    //修改分类
    public String edit(HttpServletRequest request, HttpServletResponse response)
            throws Exception {
		/*
		 * 1. 封装表单数据
		 * 2. 调用service方法完成修改工作
		 * 3. 调用findAll
		 */
		//把map封装成Category，要传cid
        Category category = CommonUtils.toBean(request.getParameterMap(), Category.class);
        categoryService.edit(category);
        return findAll(request, response);
    }

    //删除分类
    public String delete(HttpServletRequest request, HttpServletResponse response)
            throws Exception {
		/*
		 * 1. 获取参数:cid
		 * 2. 调用service方法，传递cid参数
		 *   > 如果抛出异常，保存异常信息，转发到msg.jsp显示
		 * 3. 调用findAll()
		 */
        String cid = request.getParameter("cid");
        try {
            categoryService.delete(cid);
            //成功就返回到图书的分类
            return findAll(request, response);
        } catch(CategoryException e) {
            request.setAttribute("msg", e.getMessage());
            return "f:/adminjsps/msg.jsp";
        }
    }
}
