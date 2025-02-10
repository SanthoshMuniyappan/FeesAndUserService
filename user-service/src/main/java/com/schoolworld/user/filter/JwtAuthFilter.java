package com.schoolworld.user.filter;

import com.schoolworld.user.service.JwtService;
import com.schoolworld.user.service.UserDetailService;
import com.schoolworld.user.service.UsersDetailsService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class JwtAuthFilter extends OncePerRequestFilter {

    private final JwtService jwtService;
    private final UsersDetailsService usersDetailsService;

    public JwtAuthFilter(JwtService jwtService, UsersDetailsService usersDetailsService){
        this.jwtService=jwtService;
        this.usersDetailsService=usersDetailsService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException{
        String authHeader=request.getHeader("Authorization");
        String token=null;
        String userName=null;

        if (authHeader!=null && authHeader.startsWith("Bearer ")){
            token=authHeader.substring(7);
            userName= jwtService.extractUsername(token);
        }
        if (userName!=null && SecurityContextHolder.getContext().getAuthentication() == null){
            UserDetails userDetails= usersDetailsService.loadUserByUsername(userName);
            if(jwtService.validateToken(token,userDetails)){
                UsernamePasswordAuthenticationToken authToken=new UsernamePasswordAuthenticationToken(userDetails,null,userDetails.getAuthorities());
                authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                SecurityContextHolder.getContext().setAuthentication(authToken);
            }
        }
        filterChain.doFilter(request,response);
    }
}
