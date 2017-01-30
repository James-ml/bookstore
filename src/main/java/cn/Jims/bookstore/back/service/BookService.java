package cn.Jims.bookstore.back.service;

import cn.Jims.bookstore.back.dao.BookDao;
import cn.Jims.bookstore.book.entity.Book;

import java.util.List;

/**
 * Created by Jims on 2017/1/30.
 */
public class BookService {
    private BookDao bookDao = new BookDao();

    //查询所有图书
    public List<Book> findAll() {
        return bookDao.findAll();
    }

    //查询图书详细信息
    public Book load(String bid) {
        return bookDao.findByBid(bid);
    }

    //添加图书
    public void add(Book book) {
        bookDao.add(book);
    }

    //修改图书
    public void edit(Book book) {
        bookDao.edit(book);
    }

    //删除图书
    public void delete(String bid) {
        bookDao.delete(bid);
    }
}
