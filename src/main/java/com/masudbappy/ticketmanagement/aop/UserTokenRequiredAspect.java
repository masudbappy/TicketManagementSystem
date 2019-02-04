package com.masudbappy.ticketmanagement.aop;

import javax.servlet.http.HttpServletRequest;
import javax.xml.bind.DatatypeConverter;

import com.masudbappy.ticketmanagement.services.SecurityServiceImpl;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;


import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;

@Aspect
@Component
public class UserTokenRequiredAspect {

    @Before("@annotation(userTokenRequired)")
    public void tokenRequiredWithAnnotation(UserTokenRequired userTokenRequired) throws Throwable{

        ServletRequestAttributes reqAttributes = (ServletRequestAttributes)RequestContextHolder.currentRequestAttributes();
        HttpServletRequest request = reqAttributes.getRequest();

        // checks for token in request header
        String tokenInHeader = request.getHeader("token");

        if(StringUtils.isEmpty(tokenInHeader)){
            throw new IllegalArgumentException("Empty token");
        }

        Claims claims = Jwts.parser()
                .setSigningKey(DatatypeConverter.parseBase64Binary(SecurityServiceImpl.secretKey))
                .parseClaimsJws(tokenInHeader).getBody();

        if(claims == null || claims.getSubject() == null){
            throw new IllegalArgumentException("Token Error : Claim is null");
        }

        String subject = claims.getSubject();

        if(subject.split("=").length != 2){
            throw new IllegalArgumentException("User token is not authorized");
        }
    }
}
