package bean;

import java.util.Date;

public class CartItem {
    private int cart_item_id;
    private int userId;
    private int goodsId;
    private int quantity;
    private String selected_date;
    private int travelers_count;
    private Date add_time;
    private Date update_time;
    
    // 关联商品信息
    private Goods goods;
    
    // 计算小计金额
    private Double subtotal;
    
    // 构造函数
    public CartItem() {
    }
    
    public CartItem(int userId, int goodsId, int quantity, String selected_date, int travelers_count) {
        this.userId = userId;
        this.goodsId = goodsId;
        this.quantity = quantity;
        this.selected_date = selected_date;
        this.travelers_count = travelers_count;
        this.add_time = new Date();
        this.update_time = new Date();
    }

    public int getCart_item_id() {
        return cart_item_id;
    }
    public void setCart_item_id(int cart_item_id) {
        this.cart_item_id = cart_item_id;
    }
    
    public int getUserId() {
        return userId;
    }
    public void setUserId(int userId) {
        this.userId = userId;
    }
    
    public int getGoodsId() {
        return goodsId;
    }
    public void setGoodsId(int goodsId) {
        this.goodsId = goodsId;
    }
    
    public int getQuantity() {
        return quantity;
    }
    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }
    
    public String getSelected_date() {
        return selected_date;
    }
    public void setSelected_date(String selected_date) {
        this.selected_date = selected_date;
    }
    
    public int getTravelers_count() {
        return travelers_count;
    }
    public void setTravelers_count(int travelers_count) {
        this.travelers_count = travelers_count;
    }
    
    public Date getAdd_time() {
        return add_time;
    }
    public void setAdd_time(Date add_time) {
        this.add_time = add_time;
    }
    
    public Date getUpdate_time() {
        return update_time;
    }
    public void setUpdate_time(Date update_time) {
        this.update_time = update_time;
    }
    
    public Goods getGoods() {
        return goods;
    }
    public void setGoods(Goods goods) {
        this.goods = goods;
        // 当设置商品信息时，重新计算小计
        this.subtotal = calculateSubtotal();
    }
    
    // 计算小计金额
    public double getSubtotal() {
        if (subtotal != null) {
            return subtotal;
        }
        return calculateSubtotal();
    }
    
    private double calculateSubtotal() {
        if (goods != null && goods.getPrice() != null && !goods.getPrice().trim().isEmpty()) {
            try {
                double price = Double.parseDouble(goods.getPrice().trim());
                return price * quantity * travelers_count;
            } catch (NumberFormatException e) {
                return 0;
            }
        }
        return 0;
    }
    
    // 获取商品名称
    public String getGoodsName() {
        return goods != null ? goods.getGoodsName() : "";
    }
    
    // 获取商品价格
    public double getGoodsPrice() {
        if (goods != null && goods.getPrice() != null && !goods.getPrice().trim().isEmpty()) {
            try {
                return Double.parseDouble(goods.getPrice().trim());
            } catch (NumberFormatException e) {
                return 0;
            }
        }
        return 0;
    }
    
    // 获取商品图片
    public String getGoodsImage() {
        return goods != null ? goods.getImage() : "";
    }
    
    // 获取商品地点
    public String getGoodsLocation() {
        return goods != null ? goods.getLocation() : "";
    }
    
    // 检查是否有出行日期
    public boolean hasSelectedDate() {
        return selected_date != null && !selected_date.trim().isEmpty();
    }
    
    @Override
    public String toString() {
        return "CartItem{" +
                "cart_item_id=" + cart_item_id +
                ", userId=" + userId +
                ", goodsId=" + goodsId +
                ", quantity=" + quantity +
                ", selected_date='" + selected_date + '\'' +
                ", travelers_count=" + travelers_count +
                ", subtotal=" + getSubtotal() +
                ", goods=" + (goods != null ? goods.getGoodsName() : "null") +
                '}';
    }
}