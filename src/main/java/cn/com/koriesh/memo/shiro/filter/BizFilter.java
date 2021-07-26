package cn.com.koriesh.memo.shiro.filter;

import cn.com.koriesh.memo.config.Constant;
import cn.com.koriesh.memo.jwt.JwtTokenUtil;
import cn.com.koriesh.memo.shiro.token.BizToken;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.web.filter.authc.BasicHttpAuthenticationFilter;
import org.apache.shiro.web.util.WebUtils;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

@Slf4j
public class BizFilter extends BasicHttpAuthenticationFilter {

    /**
     * @Description: 如果返回true  则执行 isAccessAllowed
     * 如果返回false 则执行 onAccessDenied
     * @CreateDate: 2020/11/2 2020/11/2
     * @UpdateDate: 2020/11/2 2020/11/2
     */
    @Override
    protected boolean preHandle(ServletRequest request, ServletResponse response) throws Exception {
        HttpServletRequest httpServletRequest = WebUtils.toHttp(request);
        HttpServletResponse httpServletResponse = WebUtils.toHttp(response);
        if (httpServletRequest.getMethod().equals(RequestMethod.OPTIONS.name())) {
            httpServletResponse.setStatus(HttpStatus.OK.value());
            return false;
        }
        return super.preHandle(request, response);
    }

    @Override
    public boolean onPreHandle(ServletRequest request, ServletResponse response, Object mappedValue) throws Exception {
        return isAccessAllowed(request, response, mappedValue) || onAccessDenied(request, response, mappedValue);
    }

    @Override
    protected boolean isAccessAllowed(ServletRequest request, ServletResponse response, Object mappedValue) {
        HttpServletRequest httpServletRequest = (HttpServletRequest) request;
        if (httpServletRequest.getMethod().equals(RequestMethod.OPTIONS.name())) {
            return true;
        }
        if (!isLoginAttempt(request, response)) {
            responseError(request, response);
            return false;
        }
        try {
            executeLogin(request, response);
        } catch (Exception e) {
            log.error(e.getMessage());
            responseError(request, response);
            return false;
        }
        return true;
    }

    /**
     * @Description: 执行登录前的检查
     * 判断header中是否有biz的token标识
     * @CreateDate: 2020/11/2 2020/11/2
     * @UpdateDate: 2020/11/2 2020/11/2
     */
    @Override
    protected boolean isLoginAttempt(ServletRequest request, ServletResponse response) {
        HttpServletRequest req = (HttpServletRequest) request;
        String authorization = req.getHeader(Constant.BIZ_ACCESS_TOKEN);
        return !StringUtils.isEmpty(authorization);
    }

    /**
     * @Description: 对所有的请求进行身份验证
     * @CreateDate: 2020/11/2 2020/11/2
     * @UpdateDate: 2020/11/2 2020/11/2
     */
    @Override
    protected boolean executeLogin(ServletRequest request, ServletResponse response) throws UnsupportedEncodingException {
        HttpServletRequest httpServletRequest = (HttpServletRequest) request;
        String authorization = httpServletRequest.getHeader(Constant.BIZ_ACCESS_TOKEN);
        // 当token不存在时 则直接返回false
        if (StringUtils.isEmpty(authorization)) {
            return false;
        }
        // 当token存在时 则进行分类处理
        else {
            // 当token存在 可以解析 但已超期时
//            System.out.println(JwtTokenUtil.getClaimsFromToken(authorization));
//            System.out.println(JwtTokenUtil.isTokenExpired(authorization));
            if (JwtTokenUtil.getClaimsFromToken(authorization) != null && JwtTokenUtil.isTokenExpired(authorization)) {
                authorization = JwtTokenUtil.refreshToken(authorization, null);
            }
        }
        BizToken token = new BizToken(JwtTokenUtil.getClaimsFromToken(authorization));
        // 提交给realm进行登入，如果错误他会抛出异常并被捕获
        getSubject(request, response).login(token);
        // 如果没有抛出异常则代表登入成功，返回true
        HttpServletResponse httpServletResponse = (HttpServletResponse) response;
        httpServletResponse.addHeader(Constant.BIZ_ACCESS_TOKEN, URLEncoder.encode(authorization, StandardCharsets.UTF_8.displayName()));
        return true;
    }

    /**
     * @Description: 对非法请求进行重定向
     * @CreateDate: 2020/11/2 2020/11/2
     * @UpdateDate: 2020/11/2 2020/11/2
     */
    private void responseError(ServletRequest req, ServletResponse resp) {
        HttpServletResponse httpServletResponse = (HttpServletResponse) resp;
        HttpServletRequest httpServletResquest = (HttpServletRequest) req;
        try {
            httpServletResquest.getRequestDispatcher("/unAuthorized").forward(req, resp);
        } catch (ServletException e) {
            log.error(e.getMessage());
        } catch (IOException e) {
            log.error(e.getMessage());
        }
    }

    @Override
    protected boolean onAccessDenied(ServletRequest request, ServletResponse response) throws Exception {
        return false;
    }
}
