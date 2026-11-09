package servlet_image;

import java.io.IOException;
import java.io.File;
import java.util.List;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;

import service.GoodsImageService;

@WebServlet("/GoodsImageUploadDo")
public class GoodsImageUploadDo extends HttpServlet {
    private static final long serialVersionUID = 1L;

    protected void doGet(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        doPost(request, response);
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        request.setCharacterEncoding("UTF-8");
        response.setContentType("text/html;charset=UTF-8");
        
        String goodsId = "";
        String currentImage = "";
        String msg = "";
        
        try {
            // 1. 检查登录
            if (util.LoginCheck.verify(request) == false) {
                response.sendRedirect("login.jsp");
                return;
            }
            
            System.out.println("=== GoodsImageUploadDo 开始处理 ===");
            
            // 2. 检查是否为multipart表单
            boolean isMultipart = ServletFileUpload.isMultipartContent(request);
            System.out.println("是否为multipart表单: " + isMultipart);
            
            if (!isMultipart) {
                msg = "错误：表单必须为multipart格式";
                response.sendRedirect("GoodsImageUpload?msg=" + 
                        java.net.URLEncoder.encode(msg, "UTF-8"));
                return;
            }
            
            // 3. 创建文件上传处理器
            DiskFileItemFactory factory = new DiskFileItemFactory();
            ServletFileUpload upload = new ServletFileUpload(factory);
            
            // 4. 解析请求
            List<FileItem> items = upload.parseRequest(request);
            System.out.println("解析到表单项数量: " + items.size());
            
            FileItem fileItem = null;
            
            // 5. 处理每个表单项
            for (FileItem item : items) {
                if (item.isFormField()) {
                    // 普通表单字段
                    String fieldName = item.getFieldName();
                    String fieldValue = item.getString("UTF-8");
                    
                    System.out.println("表单字段: " + fieldName + " = " + fieldValue);
                    
                    if ("goodsId".equals(fieldName)) {
                        goodsId = fieldValue;
                    } else if ("image".equals(fieldName)) {
                        currentImage = fieldValue;
                    }
                } else {
                    // 文件字段
                    fileItem = item;
                    System.out.println("文件字段: " + item.getFieldName() + 
                            ", 文件名: " + item.getName() + 
                            ", 文件大小: " + item.getSize() + " bytes");
                }
            }
            
            System.out.println("解析结果 - goodsId: '" + goodsId + 
                    "', currentImage: '" + currentImage + "'");
            
            // 6. 验证参数
            if (goodsId == null || goodsId.trim().isEmpty()) {
                msg = "错误：商品ID不能为空";
                response.sendRedirect("GoodsImageUpload?msg=" + 
                        java.net.URLEncoder.encode(msg, "UTF-8"));
                return;
            }
            
            // 7. 验证是否有文件上传
            if (fileItem == null || fileItem.getSize() == 0) {
                msg = "错误：请选择要上传的图片文件";
                response.sendRedirect("GoodsImageUpload?goodsId=" + goodsId + 
                        "&msg=" + java.net.URLEncoder.encode(msg, "UTF-8"));
                return;
            }
            
            // 8. 设置参数到request属性中
            request.setAttribute("goodsId", goodsId);
            request.setAttribute("image", currentImage);
            request.setAttribute("fileItem", fileItem);
            
            // 9. 调用Service处理上传
            GoodsImageService goodsImageService = new GoodsImageService();
            boolean success = goodsImageService.imageUploadDo(request);
            
            if (success) {
                msg = "图片上传成功！";
                // 获取上传后的图片名
                String newImage = (String) request.getAttribute("newImage");
                if (newImage != null) {
                    currentImage = newImage;
                }
            } else {
                Object errorMsg = request.getAttribute("msg");
                if (errorMsg != null) {
                    msg = errorMsg.toString();
                } else {
                    msg = "图片上传失败！";
                }
            }
            
            System.out.println("上传结果: " + msg + ", 新图片: " + currentImage);
            
            // 10. 重定向回上传页面
            String redirectUrl = "GoodsImageUpload?goodsId=" + goodsId + 
                    "&image=" + java.net.URLEncoder.encode(currentImage, "UTF-8");
            if (!msg.isEmpty()) {
                redirectUrl += "&msg=" + java.net.URLEncoder.encode(msg, "UTF-8");
            }
            response.sendRedirect(redirectUrl);
            
        } catch (Exception e) {
            msg = "系统发生错误：" + e.getMessage();
            System.out.println("ERROR: " + msg);
            e.printStackTrace();
            
            String redirectUrl = "GoodsImageUpload";
            if (goodsId != null && !goodsId.isEmpty()) {
                redirectUrl += "?goodsId=" + goodsId;
            }
            if (!msg.isEmpty()) {
                redirectUrl += (redirectUrl.contains("?") ? "&" : "?") + "msg=" + 
                        java.net.URLEncoder.encode(msg, "UTF-8");
            }
            response.sendRedirect(redirectUrl);
        }
    }
}