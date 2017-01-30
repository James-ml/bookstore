package cn.Jims.bookstore.back.web.servlet;

import cn.Jims.bookstore.back.service.BookService;
import cn.Jims.bookstore.back.service.CategoryService;
import cn.Jims.bookstore.book.entity.Book;
import cn.Jims.bookstore.category.entity.Category;
import cn.itcast.commons.CommonUtils;
import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileUploadBase;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 上传的文件不能直接通过request.getParameter();获取表的数据
 * Created by Jims on 2017/1/30.
 */
public class AdminAddBookServlet extends HttpServlet {
    private BookService bookService = new BookService();
    private CategoryService categoryService = new CategoryService();

    public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        request.setCharacterEncoding("utf-8");
        response.setContentType("text/html;charset=utf-8");

		/*
         * 1. 把表单数据封装到Book对象中
		 *   * 上传三步
		 */
        // 创建工厂（磁盘的文件路径）
        //15 * 1024KB上传文件指定大小（默认值为10KB）F:/temp：临时目录随便给
        DiskFileItemFactory factory = new DiskFileItemFactory(20 * 1024, new File("e:/temp"));
        // 得到解析器
        ServletFileUpload sfu = new ServletFileUpload(factory);
        // 设置单个文件大小为20KB（最大值）
        //sfu.setFileSizeMax(20 * 1024);
        // 使用解析器去解析request对象，得到List<FileItem>
        try {
            List<FileItem> fileItemList = sfu.parseRequest(request);
            /*
			 * * 把fileItemList中的数据封装到Book对象中
			 *   > 把所有的普通表单字段数据先封装到Map中
			 *   > 再把map中的数据封装到Book对象中
			 */
            Map<String, String> map = new HashMap<String, String>();
            for (FileItem fileItem : fileItemList) {
                if (fileItem.isFormField()) {
                    map.put(fileItem.getFieldName(), fileItem.getString("UTF-8"));
                }
            }

            Book book = CommonUtils.toBean(map, Book.class);
            // 为book指定bid
            book.setBid(CommonUtils.uuid());
			/*
			 * 需要把Map中的cid封装到Category对象中，再把Category赋给Book
			 */
            Category category = CommonUtils.toBean(map, Category.class);
            book.setCategory(category);

            /*
			 * 2. 保存上传的文件
			 *   * 保存的目录
			 *   * 保存的文件名称
			 */
            // 得到保存的目录
            //String savepath = this.getServletContext().getRealPath("/book_img");
            String savepath = request.getSession().getServletContext().getRealPath("/book_img");
            // 得到文件名称：给原来文件名称添加uuid前缀！避免文件名冲突
            String filename = CommonUtils.uuid() + "_" + fileItemList.get(1).getName();

            /*
			 * 校验文件的扩展名
			 */
            if (!filename.toLowerCase().endsWith("jpg")) {
                request.setAttribute("msg", "您上传的图片不是JPG扩展名！");
                request.setAttribute("categoryList", categoryService.findAll());
                request.getRequestDispatcher("/adminjsps/admin/book/add.jsp")
                        .forward(request, response);
                return;
            }

            // 使用目录和文件名称创建目标文件
            File destFile = new File(savepath, filename);
            System.out.println(destFile);

            // 保存上传文件到目标文件位置（项目上的目标位置）
            fileItemList.get(1).write(destFile);

			/*
			 * 3. 设置Book对象的image，即把图片的路径设置给Book的image
			 */
            book.setImage("book_img/" + filename);

            //注意
            book.isDel();

            /*
			 * 4. 使用BookService完成保存
			 */
            bookService.add(book);

            /* 先保存才能校验图片的尺寸
			 * 校验图片的尺寸
			 */
            Image image = new ImageIcon(destFile.getAbsolutePath()).getImage();
            System.out.println(destFile.getAbsolutePath());
            if (image.getWidth(null) > 200 || image.getHeight(null) > 200) {
                destFile.delete();//删除这个文件！
                request.setAttribute("msg", "您上传的图片尺寸超出了200 * 200！");
                request.setAttribute("categoryList", categoryService.findAll());
                request.getRequestDispatcher("/adminjsps/admin/book/add.jsp")
                        .forward(request, response);
                return;
            }

            /*
			 * 5. 返回到图书列表
			 */
            request.getRequestDispatcher("/AdminBookServlet?method=findAll").forward(request, response);
        } catch (Exception e) {
            //判断e是什么类型
            if (e instanceof FileUploadBase.FileSizeLimitExceededException) {
                request.setAttribute("msg", "您上传的文件超出了15KB");
                //校验异常保存异常信息，防止重新修改表单数据之后丢失categoryList
                request.setAttribute("categoryList", categoryService.findAll());
                request.getRequestDispatcher("/adminjsps/admin/book/add.jsp")
                        .forward(request, response);
            }
        }
    }
}
