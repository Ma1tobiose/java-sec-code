package org.joychou.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.RequestDispatcher;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import org.joychou.security.SecurityUtil;

/**
 * @author  JoyChou (joychou@joychou.org)
 * @date    2017.12.28
 * @desc    Java url redirect.
 * @fix     Check redirect url whitelist.
 */


@Controller
@RequestMapping("/urlRedirect")
public class URLRedirect {

    /**
     * usage: http://localhost:8080/urlRedirect/redirect?url=http://www.baidu.com
     *
     */
    @GetMapping("/redirect")
    public String redirect(@RequestParam("url") String url) {
        return "redirect:" + url;
    }

    /**
     * usage: http://localhost:8080/urlRedirect/setHeader?url=http://www.baidu.com
     *
     */
    @RequestMapping("/setHeader")
    @ResponseBody
    public static void setHeader(HttpServletRequest request, HttpServletResponse response){
        String url = request.getParameter("url");
        response.setStatus(HttpServletResponse.SC_MOVED_PERMANENTLY); // 301 redirect
        response.setHeader("Location", url);
    }

    /**
     * usage: http://localhost:8080/urlRedirect/sendRedirect?url=http://www.baidu.com
     *
     */
    @RequestMapping("/sendRedirect")
    @ResponseBody
    public static void sendRedirect(HttpServletRequest request, HttpServletResponse response) throws IOException{
        String url = request.getParameter("url");
        response.sendRedirect(url); // 302 redirect
    }


    /**
     * desc: security code.Because it can only jump according to the path, it cannot jump according to other urls.
     * usage: http://localhost:8080/urlRedirect/forward?url=/urlRedirect/test
     *
     */
    @RequestMapping("/forward")
    @ResponseBody
    public static void forward(HttpServletRequest request, HttpServletResponse response) throws IOException{
        String url = request.getParameter("url");
        RequestDispatcher rd =request.getRequestDispatcher(url);
        try{
            rd.forward(request, response);
        }catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * desc: sendRedirect security code
     * usage: http://localhost:8080/urlRedirect/sendRedirect_seccode?url=http://www.baidu.com
     *
     */
    @RequestMapping("/sendRedirect_seccode")
    @ResponseBody
    public static void sendRedirect_seccode(HttpServletRequest request, HttpServletResponse response) throws IOException{
        String url = request.getParameter("url");
        String urlwhitelist[] = {"joychou.org", "joychou.com"};
        if (!SecurityUtil.checkURLbyEndsWith(url, urlwhitelist)) {
            // Redirect to error page.
            response.sendRedirect("https://test.joychou.org/error3.html");
            return;
        }
        response.sendRedirect(url);
    }
}
