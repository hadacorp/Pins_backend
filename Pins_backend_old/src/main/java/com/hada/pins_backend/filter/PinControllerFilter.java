package com.hada.pins_backend.filter;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.util.ContentCachingRequestWrapper;
import org.springframework.web.util.ContentCachingResponseWrapper;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.IOException;
/**
 * Created by bangjinhyuk on 2021/11/10.
 */
@Slf4j
@WebFilter(urlPatterns= "/pin/*")
public class PinControllerFilter implements Filter {

    //들어오는 요청에 대해 전처리 및 후처리 기능
    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        ContentCachingRequestWrapper httpServletRequest = new ContentCachingRequestWrapper((HttpServletRequest)request);
        ContentCachingResponseWrapper httpServletResponse = new ContentCachingResponseWrapper((HttpServletResponse)response);
        //전처리
        chain.doFilter(httpServletRequest,httpServletResponse); //dofilter 이후에 모든것을 실핼


        String url = httpServletRequest.getRequestURI();
        //후처리
        String reqContent = new String(httpServletRequest.getContentAsByteArray());
        log.info("url status : {}, requestBody : {}",url, reqContent);

        String resContent = new String(httpServletResponse.getContentAsByteArray());

        int httpStatus = httpServletResponse.getStatus();

        httpServletResponse.copyBodyToResponse(); //다 읽었지만 복붙하여 response 결과를 다시 줄수 있음

        log.info("response status : {}, responseBody : {}",httpStatus, resContent);


    }
}
