package com.example.hrinterface.security.filter;

import com.example.hrinterface.constant.JwtConstant;
import com.example.hrinterface.security.util.CookieUtil;
import com.example.hrinterface.security.util.LoginJwtUtil;
import io.jsonwebtoken.Claims;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class LoginFilter extends OncePerRequestFilter {
    @Override
    protected void doFilterInternal(HttpServletRequest req, HttpServletResponse res, FilterChain filterChain) throws ServletException, IOException {
        String token = CookieUtil.getValue(req, JwtConstant.JWT_COOKIE_NAME);
        System.out.println("+++++++++JwtFilter++++++++");
        System.out.println(token);
        if (token!=null) {
            String userName = LoginJwtUtil.getSubjectFromJwt(token);
            System.out.println(userName);
            String role = null;
            Claims claims = LoginJwtUtil.getClaimsFromJwt(token);
            if(claims!=null){
                role = claims.get("role").toString();
            }
            if (role!=null && role.equals("HR")) {
                req.setAttribute("role",role);
                filterChain.doFilter(req, res);
            } else {
                String authLoginUrl = getFilterConfig().getInitParameter("authLoginUrl");
                res.sendRedirect(authLoginUrl + "?redirect=" + req.getRequestURL());
            }
        } else {
            String authLoginUrl = getFilterConfig().getInitParameter("authLoginUrl");
            res.sendRedirect(authLoginUrl + "?redirect=" + req.getRequestURL());
        }
    }
}
