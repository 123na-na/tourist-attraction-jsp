package service;

import java.io.File;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import bean.Goods;
import bean.Category;
//import bean.CartItem;
import dao.GoodsDao;
import dao.CategoryDao;
//import dao.CartItemDao;


public class GoodsService {                                    //业务层、业务逻辑层，service或biz
    
    GoodsDao goodsDao = new GoodsDao();                        //创建对象

    public boolean queryAll (HttpServletRequest request) {
        
        List<Goods> goodsList = null;                         //记录列表
        List<Category> categoryList = null;                    //分类列表
        String search = "";                                    //搜索的内容
        String category_id = "";                               //分类ID筛选
        int indexStart = 0;                                     //查询的起始位置
        int pageShow = 1;                                     //当前页码                    
        String page = "";                                     //页码链接组    
        String msg = "";
        
        try {
            if (request.getParameter("buttonDelete") != null) {                 //如果单击了删除按钮
                String[] goodsIdArray = request.getParameterValues("goodsId");     //获取ID列表            
                
                if (goodsIdArray != null) {
                    deleteByGoodsIdLot(request, goodsIdArray);                    //****删除所选。在本类中定义的方法
                }
            }

            String buttonSearch    = request.getParameter("buttonSearch");    //搜索按钮
            String buttonPage      = request.getParameter("buttonPage");     //页码提交按钮
            String buttonFilter    = request.getParameter("buttonFilter");   //分类筛选按钮
            String pageInput       = "1";                                    //输入的页码
            
            if (buttonSearch != null && request.getParameter("search") != null) {         //如果按下了搜索按钮
                search = request.getParameter("search").trim();                         //搜索内容
            } else if (buttonPage != null && request.getParameter("search") != null) {     //如果按下了页码提交按钮
                search      = request.getParameter("search").trim();                         //搜索内容
                pageInput = request.getParameter("pageShow");                             //页码输入框中的值
            } else {                                                     //点击了页码链接，或者刚打开此页
                if (request.getParameter("searchUrl") != null) { 
                    search = request.getParameter("searchUrl");         //不需要进行解码操作，系统会自动解码
                }
                
                if (request.getParameter("pageUrl") != null) {            //地址栏中的页码
                    pageInput = request.getParameter("pageUrl");
                }
            }    
            
            // 处理分类筛选
            if (buttonFilter != null) {                                 //如果单击了分类筛选按钮
                String filterCategory = request.getParameter("category_id");     //获取分类下拉列表的值        
                
                if (filterCategory != null && filterCategory.equals("") == false) {
                    category_id = filterCategory;                                 //设为分类ID
                }
            } else if (request.getParameter("category_id") != null) {
                category_id = request.getParameter("category_id");                //从地址栏获取分类ID
            }
            
            // 获取分类列表
            CategoryDao categoryDao = new CategoryDao();
            categoryList = categoryDao.queryAll(request);
            
            int countRow = goodsDao.queryCount(request, search, category_id);        //****获得记录总数
                    
            int pageSize  = 6;                                            //每页6条记录
            int pageCount = 0;                                             //预设总页数为0
        
            if (countRow % pageSize == 0) {                                //如果余数为0，即能整除
                pageCount = countRow / pageSize;                         //总页数
            } else {
                pageCount = countRow / pageSize + 1;                    //不能整除则加1页。如果除数为小数，将自动去除小数部分得到整数            
            }
            
            try {                                      
                pageShow = Integer.parseInt(pageInput);                    //如果是数字，返回字符串对应的整数
            } catch (Exception e) {
                //showPage = 1;                                         //如果抛出异常，则取预设值
            }                
        
            if (pageShow < 1) {                                            //如果当前页码小于1
                pageShow = 1;
            } else if (pageShow > pageCount && pageCount >= 1) {         //如果当前页码大于总页数，且总页数>=1
                pageShow = pageCount;
            }
            
            String searchUrl = "";
            
            if (search.equals("") == false) {
                searchUrl = URLEncoder.encode(search, "UTF-8");            //进行URL编码，以便在地址栏传递        
            }
            
            // 将分类ID也编码到URL中
            String categoryUrl = "";
            if (category_id != null && !category_id.equals("")) {
                categoryUrl = URLEncoder.encode(category_id, "UTF-8");
            }
            
            String mapName = request.getServletPath();                    //当前的请求名称   /GoodsListLayout
            mapName = mapName.substring(1);                                //删除最前面的斜杠  GoodsListLayout
            
            // 构建分页链接
            if (pageShow <= 1) {
                page += "<span style='color:gray;'>首页&ensp;";
                page += "上一页&ensp;</span>";
            } else { 
                page += "<a href='" + mapName + "?pageUrl=1"
                        + "&searchUrl=" + searchUrl + "&category_id=" + categoryUrl + "'>首页</a>&ensp;";
                page += "<a href='" + mapName + "?pageUrl=" + (pageShow - 1) 
                        + "&searchUrl=" + searchUrl + "&category_id=" + categoryUrl + "'>上一页</a>&ensp;";
            }
    
            if (pageShow >= pageCount) {
                page += "<span style='color:gray;'>下一页&ensp;";
                page += "尾页</span>";
            } else { 
                page += "<a href='" + mapName + "?pageUrl=" + (pageShow + 1) 
                        + "&searchUrl=" + searchUrl + "&category_id=" + categoryUrl + "'>下一页</a>&ensp;";
                page += "<a href='" + mapName + "?pageUrl=" + pageCount 
                        + "&searchUrl=" + searchUrl + "&category_id=" + categoryUrl + "'>尾页</a>";
            }
            
            page += "&emsp;&emsp;";
            page += "页码：" + pageShow + "/" + pageCount + "&emsp;";
            page += "记录数：" + countRow + "&emsp;&emsp;";    
            
            page += "输入页码:";
            page += "    <input type='text' name='pageShow' value='" + pageShow 
                    + "' style='width:40px; text-align:center;'>";
            page += "    <input type='submit' name='buttonPage' value='提交'>&emsp;";

            if (pageShow > 0) {
                indexStart = (pageShow - 1) * pageSize;                              //查询的起始位置
            }
            
            goodsList = goodsDao.queryAll(request, search, category_id, indexStart, pageSize);    //****获取当前页的记录列表
            
            if (goodsList == null || goodsList.size() == 0) {
                msg += "（查无记录）";
            }

            request.setAttribute("categoryList", categoryList);                     //传递分类列表
            request.setAttribute("search", search);                    //搜索内容
            request.setAttribute("category_id", category_id);          //分类ID
            request.setAttribute("indexStart", indexStart);            //查询的起始位置
            request.setAttribute("page", page);                        //页码相关的链接
            request.setAttribute("goodsList", goodsList);            //商品对象列表
            
            return true;
        
        } catch (Exception e) {
            msg += "系统发生错误。";    
            e.printStackTrace();
        } finally {
            if (request.getAttribute("msg") != null) {
                msg += request.getAttribute("msg").toString();
            }
            request.setAttribute("msg", msg);
            
            if (request.getSession().getAttribute("msg") != null) {                //如果session中有消息。在详情页删除记录后保存的消息
                msg += request.getSession().getAttribute("msg").toString();        //读取session中的消息
                request.getSession().removeAttribute("msg");                    //从session中移除此键值
                request.setAttribute("msg", msg);                                //将消息赋值给request
            }
        }
        
        return false;            
    }

    public boolean queryByGoodsId (HttpServletRequest request) {        
        
        Goods goods = null;
        String msg = "";
        
        try {
            String goodsId = request.getParameter("goodsId");         //获取地址栏参数goodsId的值 
            
            try {                                      
                Integer.parseInt(goodsId);                            //尝试转换为整数，即判断其是否为整数
            } catch (Exception e) {
                msg += "参数goodsId错误！";
                return false;
            }    
            
            goods = goodsDao.queryByGoodsId(request, goodsId);        //****根据ID获取记录
            
            if (goods == null) {
                msg += "（查无此商品记录）";
                return false;
            }
            
            return true;
        
        } catch (Exception e) {
            msg += "系统发生错误。";    
            e.printStackTrace();
        } finally {
            if (request.getAttribute("msg") != null) {
                msg += request.getAttribute("msg").toString();
            }
            request.setAttribute("msg", msg);
            
            if (request.getSession().getAttribute("msg") != null) {                //如果session中有消息。新添或修改记录后保存的消息
                msg += request.getSession().getAttribute("msg").toString();        //读取session中的消息
                request.getSession().removeAttribute("msg");                    //从session中移除此键值
                request.setAttribute("msg", msg);                                //将消息赋值给request
            }
            
            request.setAttribute("goods", goods);                                //通过request对象传递goods对象
        }
        
        return false;
    }    
    
 // 在GoodsService.java中，只修改addGoods方法：

    public String addGoods(HttpServletRequest request) {
        
        String msg = "";
        String goodsId = "0";
        
        try {
            String goodsName = (String) request.getAttribute("goodsName");
            String category_id = (String) request.getAttribute("category_id");
            String price = (String) request.getAttribute("price");
            String start_date = (String) request.getAttribute("start_date");
            String end_date = (String) request.getAttribute("end_date");
            String location = (String) request.getAttribute("location");
            String guide = (String) request.getAttribute("guide");
            String status = (String) request.getAttribute("status");
            String image = (String) request.getAttribute("image");
            
            // 验证状态值 - 必须是指定的字符串值
            if (status == null || status.trim().isEmpty()) {
                msg += "状态不能为空！";
                request.setAttribute("msg", msg);
                return "0";
            }
            
            // 确保状态值是有效的字符串值
            if (!"计划中".equals(status) && !"进行中".equals(status) && !"已结束".equals(status)) {
                msg += "状态值不正确，必须是'计划中'、'进行中'或'已结束'！";
                request.setAttribute("msg", msg);
                return "0";
            }
            
            // 验证其他字段...
            if (goodsName == null || goodsName.trim().isEmpty()) {
                msg += "商品名称不能为空！";
                request.setAttribute("msg", msg);
                return "0";
            }
            
            // 创建Goods对象
            Goods goods = new Goods();
            goods.setGoodsName(goodsName);
            goods.setCategory_id(category_id);
            goods.setPrice(price);
            goods.setStart_date(start_date);
            goods.setEnd_date(end_date);
            goods.setLocation(location);
            goods.setGuide(guide);
            goods.setStatus(status);  // 这里使用的是字符串值
            goods.setImage(image);
            
            // 调用DAO方法
            goodsId = goodsDao.addGoods(request, goods);
            
        } catch (Exception e) {
            msg += "系统发生错误。";
            e.printStackTrace();
            request.setAttribute("msg", msg);
        }
        
        return goodsId;
    }
    
    public boolean editGoods(HttpServletRequest request) {
        
        String msg = "";
        boolean result = false;
        
        try {
            String goodsId = request.getParameter("goodsId");
            String goodsName = request.getParameter("goodsName");
            String start_date = request.getParameter("start_date");
            String end_date = request.getParameter("end_date");
            String price = request.getParameter("price");
            String guide = request.getParameter("guide");
            String status = request.getParameter("status");
            String category_id = request.getParameter("category_id");
            String location = request.getParameter("location");
            String image = request.getParameter("image");
            
            // 验证必要字段
            if (goodsName == null || goodsName.trim().equals("")) {
                msg += "商品名称不能为空！";
                request.setAttribute("msg", msg);
                return false;
            }
            
            if (price == null || price.trim().equals("")) {
                msg += "价格不能为空！";
                request.setAttribute("msg", msg);
                return false;
            }
            
            // 创建Goods对象
            Goods goods = new Goods();
            goods.setGoodsId(goodsId);
            goods.setGoodsName(goodsName);
            goods.setStart_date(start_date);
            goods.setEnd_date(end_date);
            goods.setPrice(price);
            goods.setGuide(guide);
            goods.setStatus(status);
            goods.setCategory_id(category_id);
            goods.setLocation(location);
            goods.setImage(image);
            
            // 调用DAO层方法
            int count = goodsDao.editGoods(request, goods);
            if (count > 0) {
                result = true;
            }
            
        } catch (Exception e) {
            msg += "系统发生错误。";
            e.printStackTrace();
            request.setAttribute("msg", msg);
        }
        
        return result;
    }
    
    public boolean deleteByGoodsId (HttpServletRequest request) {
        
        String msg = "";
        
        try {    
            String goodsId = request.getParameter("goodsId");             //取得地址栏参数id
                        
            try {                                      
                Integer.parseInt(goodsId);                
            } catch (Exception e) {
                msg += "参数goodsId错误！";
                return false;
            }

            Goods goods = goodsDao.queryByGoodsId(request, goodsId);    //****    
            
            if (goods == null) {
                msg += "goodsId号对应的记录已不存在，请刷新页面后重试！";
                return false;
            }
            
            boolean result = goodsDao.deleteByGoodsId(request, goodsId);        //****删除这条记录
            
            if (result == false) {
                msg += "删除记录失败！请重试。";
                return false;
            }

            msg += "删除记录成功。";            

            // 这里可以添加删除图片文件的逻辑
            
            return true;
        
        } catch (Exception e) {
            msg += "系统发生错误。";    
            e.printStackTrace();
        } finally {
            if (request.getAttribute("msg") != null) {
                msg += request.getAttribute("msg").toString();
            }
            request.setAttribute("msg", msg);
            
            request.getSession().setAttribute("msg", msg);                        //通过session传递消息。消息将显示在列表页中
        }
        
        return false;
    }
    
    public boolean deleteByGoodsIdLot (HttpServletRequest request, String[] goodsIdArray) {        //被本类中的queryAll()方法调用
        
        String msg = "";
        
        try {
            String goodsId = "";
            
            String goodsIdLot = "";
            
            for (int i = 0; i < goodsIdArray.length; i++) {
                try {                                      
                    goodsId = String.valueOf(Integer.parseInt(goodsIdArray[i]));           
                } catch (Exception e) {
                    continue;                                                    //如果转换失败，即不是整数，则略过此项
                }
                
                goodsIdLot += "," + goodsId;    
            }
            
            if (goodsIdLot.equals("")) {
                return false;
            }
            goodsIdLot = goodsIdLot.substring(1);                                //去除最前面的逗号
            
            boolean result = goodsDao.deleteByGoodsIdLot(request, goodsIdLot);    //****删除这些记录

            if (result == false) {
                msg += "删除记录失败！请重试。";
                return false;
            }
            
            msg += "删除记录成功。";
            return true;
        
        } catch (Exception e) {
            msg += "系统发生错误。";
            e.printStackTrace();
        } finally {
            if (request.getAttribute("msg") != null) {
                msg += request.getAttribute("msg").toString();
            }
            request.setAttribute("msg", msg);
        }
        
        return false;    
    }    
    
    public boolean updateGoods(HttpServletRequest request) {        
        
        String msg = "";
        
        try {            
            String goodsId = request.getParameter("goodsId"); // 获取地址栏参数id的值
            
            if (goodsId == null || goodsId.trim().isEmpty()) {
                msg += "参数goodsId错误！";
                request.setAttribute("msg", msg);
                return false;
            }
            
            String goodsName = request.getParameter("goodsName");
            String start_date = request.getParameter("start_date");
            String end_date = request.getParameter("end_date");
            String guide = request.getParameter("guide");
            String price = request.getParameter("price");
            String status = request.getParameter("status");
            String category_id = request.getParameter("category_id");
            String location = request.getParameter("location");
            String image = request.getParameter("image");

            // 基本验证
            if (goodsName == null || goodsName.trim().isEmpty()) {
                msg = "商品名称不能为空！";
                request.setAttribute("msg", msg);
                return false;
            }
            
            if (price == null || price.trim().isEmpty()) {
                msg = "商品价格不能为空！";
                request.setAttribute("msg", msg);
                return false;
            }
            
            if (category_id == null || category_id.trim().isEmpty()) {
                msg = "请选择商品分类！";
                request.setAttribute("msg", msg);
                return false;
            }

            // 验证商品是否存在
            if (!checkGoodsExists(request, goodsId)) {
                msg += "商品记录已不存在，请刷新页面后重试！";
                request.setAttribute("msg", msg);
                return false;
            }

            Goods goods = new Goods();
            
            goods.setGoodsId(goodsId);
            goods.setGoodsName(goodsName);
            goods.setStart_date(start_date != null ? start_date : "");
            goods.setEnd_date(end_date != null ? end_date : "");
            goods.setGuide(guide != null ? guide : "");
            goods.setPrice(price);
            goods.setStatus(status != null ? status : "1");
            goods.setCategory_id(category_id);
            goods.setLocation(location != null ? location : "");
            goods.setImage(image != null ? image : "");

            int count = goodsDao.editGoods(request, goods); // 更新记录
            
            if (count == 0) {
                msg += "商品信息修改失败！请重试。";
                request.setAttribute("msg", msg);
                return false;
            }

            msg += "商品信息修改成功！";
            request.getSession().setAttribute("msg", msg); // 通过session传递消息
            return true;    
        
        } catch (Exception e) {
            msg += "系统发生错误。";    
            e.printStackTrace();
        } finally {
            if (request.getAttribute("msg") != null) {
                msg += request.getAttribute("msg").toString();
            }
            request.setAttribute("msg", msg);
        }    
        
        return false;
    }
    public boolean checkGoodsExists(HttpServletRequest request, String goodsId) {
        Goods goods = goodsDao.queryByGoodsId(request, goodsId);
        return goods != null;
    }
    
 // 添加到GoodsService.java中

    /**------------------------------ 根据分类ID查询商品 */
    public boolean queryByCategoryId(HttpServletRequest request, int categoryId) {
        
        String msg = "";
        List<Goods> goodsList = null;
        
        try {
            // 调用GoodsDao的queryByCategoryId方法
            goodsList = goodsDao.queryByCategoryId(request, categoryId);
            
            if (goodsList != null && !goodsList.isEmpty()) {
                request.setAttribute("goodsList", goodsList);
                return true;
            } else {
                msg = "该分类下暂无商品";
                request.setAttribute("msg", msg);
                return false;
            }
            
        } catch (Exception e) {
            msg = "系统发生错误：" + e.getMessage();
            e.printStackTrace();
        } finally {
            if (request.getAttribute("msg") != null) {
                msg += request.getAttribute("msg").toString();
            }
            request.setAttribute("msg", msg);
        }
        
        return false;
    }

    /**------------------------------ 查询未分类的商品 */
    public boolean queryUncategorizedGoods(HttpServletRequest request, int indexStart, int pageSize) {
        
        String msg = "";
        List<Goods> goodsList = null;
        
        try {
            // 调用GoodsDao的queryUncategorizedGoods方法
            goodsList = goodsDao.queryUncategorizedGoods(request, indexStart, pageSize);
            
            if (goodsList != null && !goodsList.isEmpty()) {
                request.setAttribute("goodsList", goodsList);
                return true;
            } else {
                msg = "暂无未分类商品";
                request.setAttribute("msg", msg);
                return false;
            }
            
        } catch (Exception e) {
            msg = "系统发生错误：" + e.getMessage();
            e.printStackTrace();
        } finally {
            if (request.getAttribute("msg") != null) {
                msg += request.getAttribute("msg").toString();
            }
            request.setAttribute("msg", msg);
        }
        
        return false;
    }

    /**------------------------------ 获取未分类商品数量 */
    public int getUncategorizedGoodsCount(HttpServletRequest request) {
        
        try {
            return goodsDao.getUncategorizedGoodsCount(request);
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }
    
}