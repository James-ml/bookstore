package cn.Jims.bookstore.book.service;

import cn.Jims.bookstore.book.dao.BookDao;
import cn.Jims.bookstore.book.entity.Book;

import java.util.List;

/**
 * Created by Jims on 2017/1/27.
 */
public class BookService {
    private BookDao bookDao = new BookDao();

    //查询所有图书
    public List<Book> findAll() {
        return bookDao.findAll();
    }

    //按分类查询图书
    public List<Book> findByCategory(String cid) {
        return bookDao.findByCategory(cid);
    }

    //加载图书
    public Book load(String bid) {
        return bookDao.findById(bid);
    }
}
