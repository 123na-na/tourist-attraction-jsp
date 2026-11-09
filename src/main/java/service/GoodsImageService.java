package service;

import java.io.File;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;

import bean.Goods;
import dao.GoodsDao;

public class GoodsImageService {
	
	String uploadFolder = "fileUpload\\goodsImage\\";								//上传商品图片的目录
	
	
	/**------------------------------- 生成商品图片的图片标签和图片链接 */
	public String getImageTag (HttpServletRequest request, String image) {
		
		String msg = "（无图片）";
			
		try {
			if (image == null || image.trim().equals("")) {
				return msg;
			}
	
			String path = request.getServletContext().getRealPath("\\") + "\\"; 	//当前项目运行时所在的物理目录
			path += uploadFolder; 
			
			File file = new File(path, image); 										//创建File对象
			
			if (file.exists() == false || file.isFile() == false) {					//如果图片文件不存在，或者不是文件而是文件夹
				return msg;
			}
			
			uploadFolder = uploadFolder.replace("\\", "/");							// 将“\”替换为“/”
			String urlImage = uploadFolder + image + "?t=" + (new Date()).getTime();//加上时间参数以显示最新图片
			
			msg = "<a href='" + urlImage + "' target='_blank'><img src='" + urlImage + "' height='100'></a>";
			return msg;																//图片高度为100px，点击图片链接将在新打开页面中显示图片
			
		} catch (Exception e) {
			msg = "系统发生错误。";	
			e.printStackTrace();
		}
		
		return msg;
	}	
		
	
	/**------------------------------- 生成商品图片的图片标签、图片链接和管理链接 */
	public String getImageTagAndLink (HttpServletRequest request, String image) {
		
		String msg = "";		
		
		try {	
			String goodsId = request.getParameter("goodsId");						//从URL中获取值
			
			if (goodsId == null || goodsId.trim().equals("")) {
				goodsId = "";
			}
		
			String action  = "GoodsImageUploadDo?goodsId=" + goodsId + "&image=" + image;
			request.setAttribute("action", action);									//将给页面表单的action赋值
	
			String imageTag = getImageTag(request, image);							//****获取商品图片的图片标签和图片链接。调用本类中的方法
			
			msg = imageTag;
			msg += "<div style='margin-top:60px;'>&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;";
			
			if (image != null && image.trim().equals("") == false) {
				String urlDelete = "GoodsImageDeleteDo?goodsId=" + goodsId + "&image=" + image;
				msg += "<a href='" + urlDelete + "' onclick='return confirm(\"确认删除吗？\")'>删除此图片</a>&emsp;&emsp;&emsp;";	
			}
			
			if (goodsId.length() < 30) {														//如果是修改商品，则goodsId为一个整数
				msg += "<a href='GoodsShow?goodsId=" + goodsId + "' target='_top'>取消</a>";		//将在页面中而不是在框架内打开链接
				msg += "&ensp;<span style='font-size:small; color:gray;'>（隐藏上传控件）</span>";	
			}
				
			msg += "</div>";
			return msg;
			
		} catch (Exception e) {
			msg = "系统发生错误。";	
			e.printStackTrace();
		}
		
		return msg;
	}		

	
	/**------------------------------- 生成商品图片的图片标签、图片链接和管理链接。图片文件名来自于URL中的参数值 */
	public String getImageTagAndLink (HttpServletRequest request) {	
		
		String msg = "";		
		
		try {	
			String image = request.getParameter("image");							//值来自于地址栏中的参数
			
			msg = getImageTagAndLink(request, image);								//****获取商品图片的图片标签、图片链接和管理链接。调用本类中的方法			
			return msg;
			
		} catch (Exception e) {
			msg = "系统发生错误。";	
			e.printStackTrace();
		}
		
		return msg;
	}
	
	
	/**------------------------------- 上传图片，并更新数据表中的image字段，或在新添商品页中更新session中的image的值 */	
	public boolean imageUploadDo(HttpServletRequest request) {
	    
	    String msg = "";	
	    String goodsId = "";
	    String currentImage = "";			
	    String newImage = "";
	    
	    try {	
	        // 从request属性中获取参数
	        goodsId = (String) request.getAttribute("goodsId");
	        currentImage = (String) request.getAttribute("image");
	        FileItem fileItem = (FileItem) request.getAttribute("fileItem");
	        
	        System.out.println("Service收到参数 - goodsId: " + goodsId + 
	                ", currentImage: " + currentImage);
	        
	        if (goodsId == null || goodsId.trim().isEmpty()) {
	            msg = "商品ID不能为空";
	            request.setAttribute("msg", msg);
	            return false;
	        }
	        
	        if (fileItem == null || fileItem.getSize() == 0) {
	            msg = "没有上传文件";
	            request.setAttribute("msg", msg);
	            return false;
	        }
	        
	        HttpSession session = request.getSession();
	        
	        // 如果是新添商品（goodsId长度大于30的情况）
	        boolean isNewGoods = goodsId.length() > 30;
	        
	        if (isNewGoods) {
	            String sessionKey = "image" + goodsId;
	            if (session.getAttribute(sessionKey) == null) {
	                session.setAttribute(sessionKey, currentImage);
	            }
	            currentImage = (String) session.getAttribute(sessionKey);
	        }
	        
	        // 1. 文件大小检查
	        long size = fileItem.getSize();
	        if (size > 2 * 1024 * 1024) { // 2MB
	            msg = "图片大小不能超过2MB";
	            request.setAttribute("msg", msg);
	            return false;
	        }
	        
	        // 2. 获取原始文件名
	        String fileName = fileItem.getName();
	        System.out.println("原始文件名: " + fileName);
	        
	        if (fileName == null || fileName.trim().isEmpty()) {
	            msg = "文件名不能为空";
	            request.setAttribute("msg", msg);
	            return false;
	        }
	        
	        // 处理Windows路径
	        int index = fileName.lastIndexOf("\\");
	        if (index >= 0) {
	            fileName = fileName.substring(index + 1);
	        }
	        
	        // 3. 检查文件扩展名
	        index = fileName.lastIndexOf(".");
	        if (index == -1) {
	            msg = "请上传有效的图片文件（需要文件扩展名）";
	            request.setAttribute("msg", msg);
	            return false;
	        }
	        
	        String nameExt = fileName.substring(index + 1).toLowerCase();
	        String allowedExtensions = ",jpg,jpeg,png,gif,bmp,";
	        if (!allowedExtensions.contains("," + nameExt + ",")) {
	            msg = "只支持JPG、JPEG、PNG、GIF、BMP格式的图片";
	            request.setAttribute("msg", msg);
	            return false;
	        }
	        
	        // 4. 生成新的文件名
	        // 如果是新商品，使用session生成的goodsId
	        // 如果是现有商品，使用数据库中的goodsId
	        String uploadId = isNewGoods ? goodsId : goodsId;
	        newImage = uploadId + "." + nameExt;
	        System.out.println("新文件名: " + newImage);
	        
	        // 5. 保存文件的目录
	        String uploadFolder = this.uploadFolder;
	        String path = request.getServletContext().getRealPath("\\") + "\\" + uploadFolder;
	        System.out.println("保存路径: " + path);
	        
	        // 确保目录存在
	        File folder = new File(path);
	        if (!folder.exists()) {
	            if (!folder.mkdirs()) {
	                msg = "创建上传目录失败";
	                request.setAttribute("msg", msg);
	                return false;
	            }
	        }
	        
	        // 6. 删除同名的旧文件
	        File newFile = new File(path, newImage);
	        if (newFile.exists()) {
	            if (!newFile.delete()) {
	                msg = "删除旧文件失败";
	                request.setAttribute("msg", msg);
	                return false;
	            }
	        }
	        
	        // 7. 保存新文件
	        try {
	            fileItem.write(newFile);
	            System.out.println("文件保存成功: " + newFile.getAbsolutePath());
	        } catch (Exception e) {
	            msg = "保存文件失败: " + e.getMessage();
	            request.setAttribute("msg", msg);
	            return false;
	        }
	        
	        // 8. 验证文件是否保存成功
	        if (!newFile.exists() || newFile.length() == 0) {
	            msg = "文件保存失败";
	            request.setAttribute("msg", msg);
	            return false;
	        }
	        
	        // 9. 更新数据库或session
	        if (isNewGoods) {
	            // 更新session
	            session.setAttribute("image" + goodsId, newImage);
	            System.out.println("Session更新: image" + goodsId + " = " + newImage);
	        } else {
	            // 更新数据库
	            GoodsDao goodsDao = new GoodsDao();
	            int count = goodsDao.editImage(request, newImage, goodsId);
	            
	            if (count == 0) {
	                msg = "更新数据库失败";
	                request.setAttribute("msg", msg);
	                // 删除刚上传的文件
	                newFile.delete();
	                return false;
	            }
	            System.out.println("数据库更新成功，影响行数: " + count);
	        }
	        
	        // 10. 删除旧图片文件（如果有）
	        if (currentImage != null && !currentImage.trim().isEmpty() && 
	            !currentImage.equals(newImage)) {
	            File oldFile = new File(path, currentImage);
	            if (oldFile.exists()) {
	                oldFile.delete();
	                System.out.println("删除旧文件: " + currentImage);
	            }
	        }
	        
	        // 11. 设置成功信息
	        msg = "图片上传成功！";
	        request.setAttribute("msg", msg);
	        request.setAttribute("newImage", newImage);
	        
	        return true;
	        
	    } catch (Exception e) {
	        msg = "系统发生错误：" + e.getMessage();
	        System.out.println("Service错误: " + msg);
	        e.printStackTrace();
	        request.setAttribute("msg", msg);
	        return false;
	    }
	}
	
	
	/**------------------------------- 删除图片 */
	public Goods imageDeleteDo (HttpServletRequest request) {
		
		String msg = "";
		Goods goods = null;
		
		try {	
			String goodsId = request.getParameter("goodsId");					//从URL中获取值			
			String image   = request.getParameter("image");	

			if (goodsId == null || goodsId.trim().equals("")) {
				msg = "参数goodsId有误。";
				return goods;
			}
			
			if (image == null || image.trim().equals("")) {
				msg = "参数image有误。";
				return goods;
			}
			
			goods = new Goods();
			goods.setGoodsId(goodsId);
			goods.setImage(image);
			
			String imageTag = getImageTag(request, image);						//****获取商品图片的图片标签和图片链接
			
			HttpSession session = request.getSession();							//获取session对象		
			
			if (goodsId.length() > 30) {										//如果是新添商品
				if (session.getAttribute("image" + goodsId) == null) {			//如果此session还没创建
					session.setAttribute("image" + goodsId, image);				//创建session并保存
				}
				
				image = (String) session.getAttribute("image" + goodsId);		//从session中获取值
				goods.setImage(image);
			}
			
			if (image.equals("")) {
				return goods;
			}
	
			String path = request.getServletContext().getRealPath("\\") + "\\"; //当前项目运行时所在的物理目录
			path += uploadFolder; 
						
			File file = new File(path, image);
			
			if (file.exists()) {
				if (file.delete() == false) {
					msg = imageTag + "&emsp;&emsp;";
					msg += "删除图片失败！";
					return goods;
				}
			}
			
			image = "";															//重置image的值
			goods.setImage(image);
			
			if (goodsId.length() > 30) {										//如果是新添商品
				session.setAttribute("image" + goodsId, image);					//设置值为空字符串
			} else {															//如果是修改商品
				GoodsDao goodsDao = new GoodsDao();
				
				int count = goodsDao.editImage(request, image, goodsId);		//****更新数据表中的image字段
				
				if (count == 0) {
					msg = imageTag + "&emsp;&emsp;";
					msg += "数据表中的商品图片值清空失败！请重试。";
				}
			}
			
			return goods;
			
		} catch (Exception e) {
			msg = "系统发生错误。";	
			e.printStackTrace();
		} finally {
			if (request.getAttribute("msg") != null) {
				msg += request.getAttribute("msg").toString();
			}
			request.setAttribute("msg", msg);
		}	
		
		return goods;
	}
}
