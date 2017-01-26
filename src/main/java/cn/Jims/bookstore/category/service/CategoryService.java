package cn.Jims.bookstore.category.service;

import cn.Jims.bookstore.category.dao.CategoryDao;
import cn.Jims.bookstore.category.entity.Category;

import java.util.List;

/**
 * Created by Jims on 2017/1/27.
 */
public class CategoryService {
    private CategoryDao categoryDao = new CategoryDao();
    //查询所有分类
    public List<Category> findAll(){
        return categoryDao.findAll();
    }
}
