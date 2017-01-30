package cn.Jims.bookstore.back.service;

import cn.Jims.bookstore.back.CategoryException.CategoryException;
import cn.Jims.bookstore.back.dao.CategoryDao;
import cn.Jims.bookstore.back.dao.BookDao;
import cn.Jims.bookstore.category.entity.Category;

import java.util.List;

/**
 * Created by Jims on 2017/1/30.
 */
public class CategoryService {
    private CategoryDao categoryDao = new CategoryDao();
    private BookDao bookDao = new BookDao();

    //查询所有分类
    public List<Category> findAll(){
        return  categoryDao.findAll();
    }

    //添加分类
    public void add(Category category){
        categoryDao.add(category);
    }

    //删除分类
    public void delete(String cid) throws CategoryException {
        // 获取该分类下图书的本数
        int count = bookDao.getCountByCid(cid);
        // 如果该分类下存在图书，不让删除，我们抛出异常
        if(count > 0) {
            throw new CategoryException("该分类下还有图书，不能删除！");
        }
        // 删除该分类
        categoryDao.delete(cid);
    }

    //加载分类
    public Category load(String cid) {
        return categoryDao.load(cid);
    }

    //修改分类
    public void edit(Category category) {
        categoryDao.edit(category);
    }
}
