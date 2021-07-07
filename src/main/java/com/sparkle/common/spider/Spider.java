package com.sparkle.common.spider;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * @Description
 * @Date: 2020/1/8 下午5:07
 */
@Controller
public class Spider {
    @RequestMapping("")
    public String spider() {
        return "";
    }
}
