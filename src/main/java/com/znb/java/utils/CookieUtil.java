package com.znb.java.utils;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author zhangnaibin
 * @Date 2015-12-18 上午11:02
 */
public class CookieUtil {
    
    private static Cookie createCookie(String name, String value, int maxAge, String path, String domain, boolean secure) {
        Cookie cookie = new Cookie(name, value);
        cookie.setPath(path);
        cookie.setMaxAge(maxAge);
        cookie.setDomain(domain);
        // according to rfc2109, this attribute is optional
        if (secure) {
            cookie.setSecure(true);
        }
        return cookie;
    }

    public static String getCookie(HttpServletRequest request, String key) {
        Cookie[] cookies = request.getCookies();
        if (cookies == null || cookies.length == 0) {
            return null;
        }
        for (int i = 0; i < cookies.length; i++) {
            if (cookies[i].getName().equals(key)) {
                return cookies[i].getValue();
            }
        }
        return null;
    }

    public static Cookie getCookieByName(HttpServletRequest request, String name) {
        Cookie[] cookies = request.getCookies();
        if (null != cookies) {
            for (javax.servlet.http.Cookie cookie : cookies) {
                if (cookie.getName().equals(name)) {
                    return cookie;
                }
            }
        }
        return null;
    }

    public static void addCookie(HttpServletResponse response, String name, String value, int maxAge, String path, String domain, boolean secure) {
        response.addCookie(createCookie(name, value, maxAge, path, domain, secure));
    }

    public static void addCookie(HttpServletResponse response, String name, String value, int maxAge, String domain) {
        response.addCookie(createCookie(name, value, maxAge, "/", domain, false));
    }

    public static void clearCookie(HttpServletResponse response, String name, String path, String domain) {
        response.addCookie(createCookie(name, null, 0, path, domain, false));
    }

    public static void clearCookie(HttpServletResponse response, String name, String domain) {
        response.addCookie(createCookie(name, null, 0, "/", domain, false));
    }

    public static void main(String[] args) {
        System.out.println("12345".substring("12345".indexOf("2")));
    }
}
