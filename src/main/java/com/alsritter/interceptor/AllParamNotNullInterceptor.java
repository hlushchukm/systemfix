package com.alsritter.interceptor;

import com.alsritter.annotation.AllParamNotNull;
import lombok.extern.slf4j.Slf4j;
import net.minidev.json.JSONObject;
import org.springframework.http.MediaType;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.PrintWriter;
import java.lang.reflect.Method;
import java.util.Map;

/**
 * 检查全部参数非空拦截器
 *
 * @author alsritter
 * @version 1.0
 **/
@Slf4j
public class AllParamNotNullInterceptor implements HandlerInterceptor {
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        // 这个 HandlerMethod 可以用来匹配 Controller，如果不是 Controller 则跳过
        if (!(handler instanceof HandlerMethod)) {
            return true;
        }

        HandlerMethod handlerMethod = (HandlerMethod) handler;
        Method method = handlerMethod.getMethod();

        // 如果打上了 AllParamNotNull
        if (method.getAnnotation(AllParamNotNull.class) != null ||
                handlerMethod.getBeanType().getAnnotation(AllParamNotNull.class) != null) {
            //从httpServletRequest获取注解上指定的参数
            Map<String, String[]> parameterMap = request.getParameterMap();

            for (String[] value : parameterMap.values()) {
                if (value == null || value[0].equals("")) {
                    JSONObject jsonObject = new JSONObject();
                    PrintWriter out = null;
                    try {
                        // 参数有空的 返回 400 错误
                        response.setStatus(400);
                        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
                        jsonObject.put("code", response.getStatus());
                        jsonObject.put("message", "The parameter cannot be null");
                        out = response.getWriter();
                        out.println(jsonObject);
                        return false;
                    } catch (Exception e) {
                        e.printStackTrace();
                    } finally {
                        if (null != out) {
                            out.flush();
                            out.close();
                        }
                    }
                }
            }
        }
        return true;
    }
}