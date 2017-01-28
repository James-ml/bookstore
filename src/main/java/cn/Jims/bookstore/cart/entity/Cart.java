package cn.Jims.bookstore.cart.entity;

import java.math.BigDecimal;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * 购物车类
 * Created by Jims on 2017/1/27.
 */
public class Cart {
    private Map<String, CartItem> map = new LinkedHashMap<String, CartItem>();

    //计算合计
    public double getTotal() {
        //合计等于所有条目的小计之和
        BigDecimal total = new BigDecimal("0");
        for (CartItem cartItem : map.values()) {
            BigDecimal subtotal = new BigDecimal(""+cartItem.getSubtotal());
            total = total.add(subtotal);
        }
        return total.doubleValue();
    }

    //添加条目到购物车中
    public void add(CartItem cartItem) {
        //判断原来购物车中是否存在该条目
        if (map.containsKey(cartItem.getBook().getBid())) {
            //返回原条目,通过key获取值
            CartItem _cartItem = map.get(cartItem.getBook().getBid());
            //设置老条目的数量为:自己条目数量+新条目的数量
            _cartItem.setCount(_cartItem.getCount() + cartItem.getCount());
            map.put(cartItem.getBook().getBid(), _cartItem);
        } else {
            map.put(cartItem.getBook().getBid(), cartItem);
        }
    }

    //清空所有条目
    public void clear() {
        map.clear();
    }

    //删除指定条目
    public void delete(String bid) {
        map.remove(bid);
    }

    //获取所有条目
    public Collection<CartItem> getCartItems() {
        return map.values();
    }
}
