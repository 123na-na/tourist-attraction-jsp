package dao;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import bean.Goods;

public class GoodsDao {
    
    Db db = new Db();
    String sql = "";
    ResultSet rs = null;
    
    /**------------------------------ 获取记录总数（支持按分类筛选） */
    public int queryCount (HttpServletRequest request, String search, String category_id) {

        int count = 0;
        String msg = "";
        
        try {
            String sqlWhere = "";
            
            if (search != null && !search.equals("") && !search.equals("null")) {
                sqlWhere = " where concat_ws(',', goodsName, location) like ?";
            }
            
            if (category_id != null && !category_id.equals("") && !category_id.equals("0") && !category_id.equals("null")) {
                if (sqlWhere.equals("")) {
                    sqlWhere = " where category_id = ?";
                } else {
                    sqlWhere += " and category_id = ?";
                }
            }
            
            if (search != null && !search.equals("") && !search.equals("null") 
                && category_id != null && !category_id.equals("") && !category_id.equals("0") && !category_id.equals("null")) {
                sql = "select count(*) from tb_goods " + sqlWhere;
                rs = db.select(sql, "%" + search + "%", category_id);
            } else if (search != null && !search.equals("") && !search.equals("null")) {
                sql = "select count(*) from tb_goods " + sqlWhere;
                rs = db.select(sql, "%" + search + "%");
            } else if (category_id != null && !category_id.equals("") && !category_id.equals("0") && !category_id.equals("null")) {
                sql = "select count(*) from tb_goods " + sqlWhere;
                rs = db.select(sql, category_id);
            } else {
                sql = "select count(*) from tb_goods";
                rs = db.select(sql);
            }
            
            if (rs == null) {            
                msg += "数据库操作发生错误！";
                request.setAttribute("msg", msg);
                return count;
            }
            
            if (rs.next() == false) {
                db.close();
                return count;
            }            
            count = rs.getInt(1);
            db.close();
            
        } catch (Exception e) {
            msg += "系统发生错误。";    
            e.printStackTrace();
        } finally {
            if (request.getAttribute("msg") != null) {
                msg += request.getAttribute("msg").toString();
            }
            request.setAttribute("msg", msg);
        }

        return count;
    }    

    /**------------------------------ 获取满足查询条件的要显示的页中的记录列表（支持按分类筛选） */
    public List<Goods> queryAll (HttpServletRequest request, String search, String category_id, int indexStart, int pageSize) {
        
        List<Goods> goodsList = new ArrayList<Goods>();
        String msg = "";
        
        try {
            String goodsId, start_date, goodsName, end_date, guide, price, status, cat_id, location, image;
            
            String sqlWhere = "";
            
            if (search != null && !search.equals("") && !search.equals("null")) {
                sqlWhere = " where concat_ws(',', goodsName, location) like ?";
            }
            
            if (category_id != null && !category_id.equals("") && !category_id.equals("0") && !category_id.equals("null")) {
                if (sqlWhere.equals("")) {
                    sqlWhere = " where category_id = ?";
                } else {
                    sqlWhere += " and category_id = ?";
                }
            }
            
            // 按状态和商品名称排序
            String orderBy = " order by status asc, goodsName asc";
            
            if (search != null && !search.equals("") && !search.equals("null") 
                && category_id != null && !category_id.equals("") && !category_id.equals("0") && !category_id.equals("null")) {
                sql = "select * from tb_goods " + sqlWhere + orderBy 
                        + " limit " + indexStart + "," + pageSize;
                rs = db.select(sql, "%" + search + "%", category_id);
            } else if (search != null && !search.equals("") && !search.equals("null")) {
                sql = "select * from tb_goods " + sqlWhere + orderBy 
                        + " limit " + indexStart + "," + pageSize;
                rs = db.select(sql, "%" + search + "%");
            } else if (category_id != null && !category_id.equals("") && !category_id.equals("0") && !category_id.equals("null")) {
                sql = "select * from tb_goods " + sqlWhere + orderBy 
                        + " limit " + indexStart + "," + pageSize;
                rs = db.select(sql, category_id);
            } else {
                sql = "select * from tb_goods " + orderBy 
                        + " limit " + indexStart + "," + pageSize;
                rs = db.select(sql);
            }
            
            if (rs == null) {            
                msg += "数据库操作发生错误！";
                request.setAttribute("msg", msg);
                return goodsList;
            }
            
            Goods goods = null;
            long time = (new Date()).getTime();
            
            while (rs.next()) {
                goodsId     = rs.getString("goodsId");        
                goodsName     = rs.getString("goodsName");        
                start_date = rs.getString("start_date");
                price = rs.getString("price");
                end_date = rs.getString("end_date");
                guide = rs.getString("guide");
                status = rs.getString("status");
                cat_id = rs.getString("category_id");
                image         = rs.getString("image");    
                if (image != null && !image.equals("")) {
                    image = image + "?t=" + time;
                }
                location = rs.getString("location");
                
                goods = new Goods();

                goods.setGoodsId(goodsId);
                goods.setGoodsName(goodsName);
                goods.setStart_date(start_date);
                goods.setPrice(price);            
                goods.setEnd_date(end_date);
                goods.setGuide(guide);
                goods.setStatus(status);
                goods.setCategory_id(cat_id);
                goods.setImage(image);    
                goods.setLocation(location);

                goodsList.add(goods);
            }
            db.close();
            
        } catch (Exception e) {
            msg += "系统发生错误。";    
            e.printStackTrace();
        } finally {
            if (request.getAttribute("msg") != null) {
                msg += request.getAttribute("msg").toString();
            }
            request.setAttribute("msg", msg);
        }        

        return goodsList;
    }    

    /**------------------------------ 根据ID查询记录 */
    public Goods queryByGoodsId (HttpServletRequest request, String goodsId) {
        
        Goods goods = null;
        String msg = "";
        
        try {
            String start_date, goodsName, end_date, guide, price, status, cat_id, location, image;
            
            sql = "select * from tb_goods where goodsId = ?";
            rs = db.select(sql, goodsId);
            
            if (rs == null) {            
                msg += "数据库操作发生错误！";
                request.setAttribute("msg", msg);
                return goods;
            }
            
            if (rs.next() == false) {
                db.close();
                return goods;
            }
                
            goodsId     = rs.getString("goodsId");        
            goodsName     = rs.getString("goodsName");        
            start_date = rs.getString("start_date");
            price = rs.getString("price");
            end_date = rs.getString("end_date");
            guide = rs.getString("guide");
            status = rs.getString("status");
            cat_id = rs.getString("category_id");
            image         = rs.getString("image");    
            if (image != null && !image.equals("")) {
                long time = (new Date()).getTime();
                image = image + "?t=" + time;
            }
            location = rs.getString("location");
            db.close();
    
            goods = new Goods();
            
            goods.setGoodsId(goodsId);
            goods.setGoodsName(goodsName);
            goods.setStart_date(start_date);
            goods.setPrice(price);            
            goods.setEnd_date(end_date);
            goods.setGuide(guide);
            goods.setStatus(status);
            goods.setCategory_id(cat_id);
            goods.setImage(image);    
            goods.setLocation(location);
            
        } catch (Exception e) {
            msg += "系统发生错误。";    
            e.printStackTrace();
        } finally {
            if (request.getAttribute("msg") != null) {
                msg += request.getAttribute("msg").toString();
            }
            request.setAttribute("msg", msg);
        }        

        return goods;
    }

    /**------------------------------ 是否存在商品名称相同的记录 */
    public boolean existByGoodsName (HttpServletRequest request, String goodsName) {

        String msg = "";
        
        try {            
            sql = "select * from tb_goods where goodsName = ?";
            rs = db.select(sql, goodsName);
            
            if (rs == null) {            
                msg += "数据库操作发生错误！";
                request.setAttribute("msg", msg);
                return false;
            }
            
            if (rs.next() == false) {
                db.close();
                return false;
            }
            db.close();
            
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
    
    /**------------------------------ 是否存在商品名称相同而与ID不同的记录 */
    public boolean existByGoodsNameExptId (HttpServletRequest request, String goodsName, String goodsId) {

        String msg = "";
        
        try {            
            sql = "select * from tb_goods where goodsName = ? and goodsId != ?";
            rs = db.select(sql, goodsName, goodsId);
            
            if (rs == null) {            
                msg += "数据库操作发生错误！";
                request.setAttribute("msg", msg);
                return false;
            }
            
            if (rs.next() == false) {
                db.close();
                return false;
            }
            db.close();            
            
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
    
    /**------------------------------ 新添记录 */
    public String addGoods(HttpServletRequest request, Goods goods) {        
        
        String msg = "";
        
        try {
            String goodsName = goods.getGoodsName();
            String start_date = goods.getStart_date();
            String end_date = goods.getEnd_date();
            String price = goods.getPrice();
            String guide = goods.getGuide();
            String status = goods.getStatus();  // 这里已经是字符串值
            String category_id = goods.getCategory_id();
            String location = goods.getLocation();
            String image = goods.getImage();
            
            // 调试输出状态值
            System.out.println("插入商品状态值: " + status);
            
            sql = "INSERT INTO tb_goods (goodsName, start_date, end_date, price, guide, status, category_id, location, image) "
                    + "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";                
            String newGoodsId = db.insert(sql, goodsName, start_date, end_date, price, guide, 
                                          status, category_id, location, image);
            
            if (newGoodsId.equals("0")) {
                msg += "商品添加失败！";
                return "0";
            }
        
            msg = "商品添加成功！";        
            return newGoodsId;
        
        } catch (Exception e) {
            msg += "系统发生错误。";    
            e.printStackTrace();
            request.setAttribute("msg", msg);
        }        
            
        return "0";
    }
    
    /**------------------------------ 更新记录 */
    public int editGoods(HttpServletRequest request, Goods goods) {		
        
        int count = 0;
        String msg = "";
        
        try {
            String goodsId, goodsName, start_date, end_date, price, guide, status, category_id, location, image;
            
            goodsId = goods.getGoodsId();
            goodsName = goods.getGoodsName();
            start_date = goods.getStart_date();
            end_date = goods.getEnd_date();
            price = goods.getPrice();
            guide = goods.getGuide();
            status = goods.getStatus();
            category_id = goods.getCategory_id();
            location = goods.getLocation();
            image = goods.getImage();
            
            // 修改SQL语句，更新所有字段
            sql = "UPDATE tb_goods SET goodsName=?, start_date=?, end_date=?, price=?, "
                + "guide=?, status=?, category_id=?, location=?, image=? WHERE goodsId=?";
            count = db.update(sql, goodsName, start_date, end_date, price, 
                             guide, status, category_id, location, image, goodsId);
            
            if (count == 0) {
                msg = "商品信息修改失败！";
                return count;
            }	
        
            msg = "商品信息修改成功！";		
            return count;
        
        } catch (Exception e) {
            msg += "系统发生错误。";	
            e.printStackTrace();
        } finally {
            if (request.getAttribute("msg") != null) {
                msg += request.getAttribute("msg").toString();
            }
            request.setAttribute("msg", msg);
        }		
        
        return count;
    }
    
    /**------------------------------ 更新记录：更新商品图片image */
    public int editImage (HttpServletRequest request, String image, String goodsId) {        
        
        int count = 0;
        String msg = "";
        
        try {
            sql = "update tb_goods set image = ? where goodsId = ?";
            count = db.update(sql, image, goodsId);
            
            if (count == 0) {
                msg = "商品图片更新失败！";
                request.setAttribute("msg", msg);
                return count;
            }    
        
            return count;
        
        } catch (Exception e) {
            msg += "系统发生错误。";    
            e.printStackTrace();
        } finally {
            if (request.getAttribute("msg") != null) {
                msg += request.getAttribute("msg").toString();
            }
            request.setAttribute("msg", msg);
        }        

        return count;
    }        
    
    
    /**------------------------------ 删除ID对应的记录 */
    public boolean deleteByGoodsId (HttpServletRequest request, String goodsId) {

        String msg = "";
        
        try {    
            sql = "delete from tb_goods where goodsId = ?";
            int count = db.delete(sql, goodsId);
            
            if (count == 0) {
                msg += "商品删除失败！";
                request.setAttribute("msg", msg);
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
        }        

        return false;
    }
    
    /**------------------------------ 删除ID列表中的记录 */
    public boolean deleteByGoodsIdLot (HttpServletRequest request, String goodsIdLot) {

        String msg = "";
        
        try {            
            sql = "delete from tb_goods where goodsId in ("+ goodsIdLot +")";
            int count = db.delete(sql);
            
            if (count == 0) {
                msg += "商品批量删除失败！";
                request.setAttribute("msg", msg);
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
        }        

        return false;
    }    

    /**------------------------------ 是否存在拥有该category_id的商品记录 */
    public boolean existByCategoryId (HttpServletRequest request, String category_id) {

        String msg = "";
        
        try {            
            sql = "select * from tb_goods where category_id = ?";
            rs = db.select(sql, category_id);
            
            if (rs == null) {            
                msg += "数据库操作发生错误！";
                request.setAttribute("msg", msg);
                return false;
            }
            
            if (rs.next() == false) {
                db.close();
                return false;
            }
            db.close();            
            
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
    
    /**------------------------------ 根据状态筛选商品 */
    public List<Goods> queryByStatus(HttpServletRequest request, String status, int indexStart, int pageSize) {
        
        List<Goods> goodsList = new ArrayList<Goods>();
        String msg = "";
        
        try {
            sql = "select * from tb_goods where status = ? order by goodsName asc limit " + indexStart + "," + pageSize;
            rs = db.select(sql, status);
            
            if (rs == null) {            
                msg += "数据库操作发生错误！";
                request.setAttribute("msg", msg);
                return goodsList;
            }
            
            Goods goods = null;
            long time = (new Date()).getTime();
            
            while (rs.next()) {
                String goodsId = rs.getString("goodsId");        
                String goodsName = rs.getString("goodsName");        
                String start_date = rs.getString("start_date");
                String price = rs.getString("price");
                String end_date = rs.getString("end_date");
                String guide = rs.getString("guide");
                String cat_id = rs.getString("category_id");
                String image = rs.getString("image");    
                if (image != null && !image.equals("")) {
                    image = image + "?t=" + time;
                }
                String location = rs.getString("location");
                
                goods = new Goods();

                goods.setGoodsId(goodsId);
                goods.setGoodsName(goodsName);
                goods.setStart_date(start_date);
                goods.setPrice(price);            
                goods.setEnd_date(end_date);
                goods.setGuide(guide);
                goods.setStatus(status);
                goods.setCategory_id(cat_id);
                goods.setImage(image);    
                goods.setLocation(location);

                goodsList.add(goods);
            }
            db.close();
            
        } catch (Exception e) {
            msg += "系统发生错误。";    
            e.printStackTrace();
        } finally {
            if (request.getAttribute("msg") != null) {
                msg += request.getAttribute("msg").toString();
            }
            request.setAttribute("msg", msg);
        }        

        return goodsList;
    }
    
    /**------------------------------ 根据状态获取商品数量 */
    public int queryCountByStatus(HttpServletRequest request, String status) {

        int count = 0;
        String msg = "";
        
        try {            
            sql = "select count(*) from tb_goods where status = ?";
            rs = db.select(sql, status);
            
            if (rs == null) {            
                msg += "数据库操作发生错误！";
                request.setAttribute("msg", msg);
                return count;
            }
            
            if (rs.next() == false) {
                db.close();
                return count;
            }            
            count = rs.getInt(1);
            db.close();
            
        } catch (Exception e) {
            msg += "系统发生错误。";    
            e.printStackTrace();
        } finally {
            if (request.getAttribute("msg") != null) {
                msg += request.getAttribute("msg").toString();
            }
            request.setAttribute("msg", msg);
        }

        return count;
    }
    
    
    
 // 添加到GoodsDao.java中（如果还没有的话）

    /**------------------------------ 根据分类ID查询商品 */
    public List<Goods> queryByCategoryId(HttpServletRequest request, int categoryId) {
        List<Goods> goodsList = new ArrayList<Goods>();
        String msg = "";
        
        try {
            sql = "select * from tb_goods where category_id = ? order by start_date";
            rs = db.select(sql, String.valueOf(categoryId));
            
            if (rs == null) {           
                msg += "数据库操作发生错误！";
                return goodsList;
            }
            
            while (rs.next()) {
                Goods goods = new Goods();
                
                goods.setGoodsId(rs.getString("goodsId"));
                goods.setGoodsName(rs.getString("goodsName"));
                goods.setStart_date(rs.getString("start_date"));
                goods.setEnd_date(rs.getString("end_date"));
                goods.setPrice(rs.getString("price"));
                goods.setGuide(rs.getString("guide"));
                goods.setStatus(rs.getString("status"));
                goods.setCategory_id(rs.getString("category_id"));
                goods.setLocation(rs.getString("location"));
                goods.setImage(rs.getString("image"));
                
                goodsList.add(goods);
            }
            db.close();
            
        } catch (Exception e) {
            msg += "系统发生错误。";  
            e.printStackTrace();
        } finally {
            if (request.getAttribute("msg") != null) {
                msg += request.getAttribute("msg").toString();
            }
            request.setAttribute("msg", msg);
        }       

        return goodsList;
    }

    /**------------------------------ 查询未分类商品 */
    public List<Goods> queryUncategorizedGoods(HttpServletRequest request, int indexStart, int pageSize) {
        
        List<Goods> goodsList = new ArrayList<Goods>();
        String msg = "";
        
        try {
            // 查询category_id为NULL或'0'的商品
            sql = "select * from tb_goods where category_id is null or category_id = '0' or category_id = '' "
                    + "order by goodsName asc limit " + indexStart + "," + pageSize;
            rs = db.select(sql);
            
            if (rs == null) {            
                msg += "数据库操作发生错误！";
                request.setAttribute("msg", msg);
                return goodsList;
            }
            
            while (rs.next()) {
                Goods goods = new Goods();
                
                goods.setGoodsId(rs.getString("goodsId"));
                goods.setGoodsName(rs.getString("goodsName"));
                goods.setStart_date(rs.getString("start_date"));
                goods.setEnd_date(rs.getString("end_date"));
                goods.setPrice(rs.getString("price"));
                goods.setGuide(rs.getString("guide"));
                goods.setStatus(rs.getString("status"));
                goods.setCategory_id(rs.getString("category_id"));
                goods.setLocation(rs.getString("location"));
                goods.setImage(rs.getString("image"));
                
                goodsList.add(goods);
            }
            db.close();
            
        } catch (Exception e) {
            msg += "系统发生错误。";    
            e.printStackTrace();
        } finally {
            if (request.getAttribute("msg") != null) {
                msg += request.getAttribute("msg").toString();
            }
            request.setAttribute("msg", msg);
        }        

        return goodsList;
    }

    /**------------------------------ 获取未分类商品数量 */
    public int getUncategorizedGoodsCount(HttpServletRequest request) {

        int count = 0;
        String msg = "";
        
        try {            
            sql = "select count(*) from tb_goods where category_id is null or category_id = '0' or category_id = ''";
            rs = db.select(sql);
            
            if (rs == null) {            
                msg += "数据库操作发生错误！";
                request.setAttribute("msg", msg);
                return count;
            }
            
            if (rs.next() == false) {
                db.close();
                return count;
            }            
            count = rs.getInt(1);
            db.close();
            
        } catch (Exception e) {
            msg += "系统发生错误。";    
            e.printStackTrace();
        } finally {
            if (request.getAttribute("msg") != null) {
                msg += request.getAttribute("msg").toString();
            }
            request.setAttribute("msg", msg);
        }

        return count;
    }
  }