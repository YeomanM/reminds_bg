package com.mjj.wxdemoreminds.util;

import lombok.Data;
import org.apache.commons.lang.StringUtils;

/**
 * @author 李学照
 * @version 1.0
 * @date 2019/12/4
 * @desc
 */
@Data
public class HttpProxy {
    private String proxyIp;
    private int proxyPort;
    private String userName;
    private String passWord;

    public boolean isAuthenticate() {
        return StringUtils.isNotBlank(userName) && StringUtils.isNotBlank(passWord);
    }
}
