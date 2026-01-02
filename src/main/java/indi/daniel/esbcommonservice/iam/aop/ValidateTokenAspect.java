package indi.daniel.esbcommonservice.iam.aop;

import java.util.Map;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import indi.daniel.esbcommonservice.common.exception.InvalidJwtTokenException;
import indi.daniel.esbcommonservice.common.utils.JwtUtils;
import indi.daniel.esbcommonservice.iam.exception.ScopeWithNoApiException;
import indi.daniel.esbcommonservice.iam.repository.model.AccessTokenDetail;
import indi.daniel.esbcommonservice.iam.service.IamService;

@Aspect
@Component
public class ValidateTokenAspect {

    private String AUTHORIZATIN_KEY = "Authorization";

    private String AUTHORIZATION_PREFIX = "Bearer ";

    @Autowired
    private JwtUtils jwtUtils;

    @Autowired
    private IamService iamService;

    /*
     * 定義一個切入點，匹配所有帶有 @ValidateToken 註解的方法
     * @within -> class
     * @annotation -> method
     * 要指定到一個 @interface
     */
    @Pointcut("@within(indi.daniel.esbcommonservice.iam.constraint.ValidateJwtToken) || @annotation(indi.daniel.esbcommonservice.iam.constraint.ValidateJwtToken)")
    public void validateTokenPointcut() {}

    @Before("validateTokenPointcut()")
    public void beforeValidateToken(JoinPoint joinPoint) {
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = attributes.getRequest();
        String authorizationHeader = request.getHeader(AUTHORIZATIN_KEY);

        String token;
        Map<String, Object> payloadMap;
        if (authorizationHeader != null && authorizationHeader.startsWith(AUTHORIZATION_PREFIX)) {
            token = authorizationHeader.substring(7);
            payloadMap = jwtUtils.validateToken(token);
        } else {
            throw new InvalidJwtTokenException("未找到authorization bearer token");
        }

        AccessTokenDetail accessTokenDetail = iamService.getAccessTokenDetail(token);
        if (accessTokenDetail == null) {
            throw new InvalidJwtTokenException("非正常核發Jwt Token");
        }

        Integer useCount = accessTokenDetail.getUseCount();
        if (useCount != null) {
            if (useCount == 0) {
                throw new InvalidJwtTokenException("Jwt Token達到使用次數上限");
            } else {
                iamService.updateAccessTokenUseCount(token);
            }
        }

        String requestUri = request.getRequestURI();
        // String requestMethod = request.getMethod();
        if (payloadMap.get(requestUri) == null) {
            throw new ScopeWithNoApiException();
        }
    }
}
