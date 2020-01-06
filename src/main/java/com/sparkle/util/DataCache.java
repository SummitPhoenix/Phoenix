package com.sparkle.util;

import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * @Description
 * @Author: XuanXiangHui
 * @Date: 2019/12/30 下午3:42
 */
@Component
public class DataCache {
    /**
     * key spaceId
     */
    public Map<String,ArrayList<String>> photoList = new HashMap<>();

}
