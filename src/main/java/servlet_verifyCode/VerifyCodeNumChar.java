package servlet_verifyCode;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Random;

import javax.imageio.ImageIO;
import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@WebServlet("/VerifyCodeNumChar")
public class VerifyCodeNumChar extends HttpServlet {
	private static final long serialVersionUID = 1L;

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		response.setContentType("image/gif");					//返回图片文件
		response.setHeader("Pragma", "No-cache");
		response.setHeader("Cache-Control", "no-cache");
		response.setDateHeader("Expires", 0);
		
		ServletOutputStream out = response.getOutputStream();	//输出流
		
		Font mFont = new Font("Times New Roman", Font.PLAIN, 18); 	//设置字体
		int width = 60, height = 20;							//设置图片大小
		
		BufferedImage image = new BufferedImage(width, height,
				BufferedImage.TYPE_INT_RGB); 					//图像缓冲对象
		Graphics gra = image.getGraphics();
		
		Random random = new Random(); 							//设置随机对象

		gra.setColor(getRandColor(200, 250)); 					//设置背景色
		gra.fillRect(0, 0, width, height); 						//填充长方形
		gra.setColor(Color.black); 								//设置字体色
		gra.setFont(mFont); 		
		gra.setColor(getRandColor(160, 200)); 					//设置线条颜色
		
		for (int i = 0; i < 100; i++) {							//随机产生100条干扰线，使图象中的验证码不易被其它程序探测到
			int x = random.nextInt(width);
			int y = random.nextInt(height);
			int xl = random.nextInt(12);
			int yl = random.nextInt(12);
			gra.drawLine(x, y, x + xl, y + yl); 				//画线
		}
		
		String code = "";										//验证码内容
		
		for (int i = 0; i < 4; i++) {							//随机产生4位的验证码
			//String rand = String.valueOf(random.nextInt(10)); //0~9的随机数字
			String[] str = {"2", "3", "4", "5", "6", "7", "8", "9", "A", "B", "C", "D", "E", "F", "G", "H", "J", "K", "L", "M", "N", "P", "Q", "R", "S", "T", "U", "V", "W", "X", "Y", "Z"}; 
			String rand = str[random.nextInt(str.length)]; 		//随机字符。不含易混淆的字符0、O、1、I
			code += rand;
			
			gra.setColor(new Color(20 + random.nextInt(110), 
					20 + random.nextInt(110), 20 + random.nextInt(110))); 	//设置颜色
			gra.drawString(rand, 13 * i + 6, 16);				//将验证码合成到图像中
		}
		
		gra.setColor(getRandColor(160, 200)); 					//设置线条颜色
		
		for (int i = 0; i < 20; i++) {							//再随机产生20条干扰线，使图象中的验证码不易被其它程序探测到
			int x = random.nextInt(width);
			int y = random.nextInt(height);
			int xl = random.nextInt(12);
			int yl = random.nextInt(12);
			gra.drawLine(x, y, x + xl, y + yl); 				//画线
		}
		
		HttpSession session = request.getSession(true);	 		//等同于request.getSession();将创建一个新的session。若为false则是尝试读取session
		session.setAttribute("code", code); 					//验证码内容保存到 session
		session.setMaxInactiveInterval(30); 					//多少秒后该session过期
		
		ImageIO.write(image, "gif", out);						//将图像缓冲对象写入到图像实体
		out.close();
	}

	static Color getRandColor(int fc, int bc) { 				//在给定范围内获得随机颜色
		Random random = new Random();
		if (fc > 255)
			fc = 255;
		if (bc > 255)
			bc = 255;
		int r = fc + random.nextInt(bc - fc);
		int g = fc + random.nextInt(bc - fc);
		int b = fc + random.nextInt(bc - fc);
		return new Color(r, g, b);
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doGet(request, response);
	}
}
