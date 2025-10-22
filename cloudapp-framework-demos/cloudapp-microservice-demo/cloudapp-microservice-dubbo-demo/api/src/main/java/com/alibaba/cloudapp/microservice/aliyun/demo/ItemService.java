package com.alibaba.cloudapp.microservice.aliyun.demo;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

/**
 * Item service interface
 */
public interface ItemService {
    
    /**
     * Get item by id
     */
    Map<String, Object> getItem(Long itemId);
    
    /**
     * Get items by category
     */
    List<Map<String, Object>> getItemsByCategory(String category);
    
    /**
     * Update item price
     */
    boolean updateItemPrice(Long itemId, BigDecimal price);
    
    /**
     * Search items by keyword
     */
    List<Map<String, Object>> searchItems(String keyword, int page, int size);
    
    /**
     * Check item stock
     */
    int checkStock(Long itemId);
}
