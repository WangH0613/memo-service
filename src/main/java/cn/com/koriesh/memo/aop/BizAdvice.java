package cn.com.koriesh.memo.aop;

import cn.com.koriesh.memo.aop.annotation.BizLog;
import cn.com.koriesh.memo.common.DateUtil;
import cn.com.koriesh.memo.entity.NsmmUserBizLog;
import cn.com.koriesh.memo.service.UserBizLogService;
import cn.com.koriesh.memo.shiro.po.BizUser;
import com.github.pagehelper.util.StringUtil;
import com.google.gson.Gson;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;
import java.util.Date;
import java.util.HashMap;

@Slf4j
@Component
@Aspect
public class BizAdvice {

    Logger userBizLog = LoggerFactory.getLogger("userBizLog");

    private static String unknown = "unknown";

    @Resource
    @Qualifier("userBizLogService")
    private UserBizLogService userBizLogService;

    // 定义切点
    @Pointcut("execution(* cn.com.koriesh.memo.controller.biz..*.*(..))")
    public void pointCut() {
        // Do nothing because of X and Y.
    }

    /**
     * @Description: 前置增强
     * 在进入目标方法之前触发
     * @CreateDate: 2020/11/26 2020/11/26
     * @UpdateDate: 2020/11/26 2020/11/26
     */
    @Before("pointCut()")
    public void beforeMethod() {
        // Do nothing because of X and Y.
    }

    /**
     * @Description: 后置增强
     * @CreateDate: 2020/11/26 2020/11/26
     * @UpdateDate: 2020/11/26 2020/11/26
     */
    @After("pointCut()")
    public void afterMethod() {
        // Do nothing because of X and Y.
    }

    /**
     * @Description: 环绕通知
     * @CreateDate: 2020/11/26 2020/11/26
     * @UpdateDate: 2020/11/26 2020/11/26
     */
    @Around("pointCut()")
    public Object aroundMethod(ProceedingJoinPoint proceedingJoinPoint) throws Throwable {
        Object result = proceedingJoinPoint.proceed();
        Subject sb = SecurityUtils.getSubject();
        HttpServletRequest request = null;
        ServletRequestAttributes servletRequestAttributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        if (servletRequestAttributes != null) {
            request = servletRequestAttributes.getRequest();
        }

        BizUser bizUser = (BizUser) sb.getPrincipal();
        NsmmUserBizLog nsmmUserBizLog = new NsmmUserBizLog();
        if (this.checkFirst(result, bizUser) && request != null) {
            Date startDate = new Date();
            // 获取用户
            nsmmUserBizLog.setUserId(bizUser.getUserName());
            nsmmUserBizLog.setUserBh(bizUser.getUserName());
            nsmmUserBizLog.setUserRealName(bizUser.getUserName());
            nsmmUserBizLog.setUserDeptName(bizUser.getDept());
            nsmmUserBizLog.setUserRoleName(bizUser.getUserJob());
            // 获取请求参数
            nsmmUserBizLog.setRequestFromIp(this.getIpAddr(request));
            nsmmUserBizLog.setRequestToUrl(request.getRequestURL().toString());
            // 获取方法信息
            nsmmUserBizLog.setClassName(proceedingJoinPoint.getTarget().getClass().getName());
            nsmmUserBizLog.setMethodName(proceedingJoinPoint.getSignature().getName());
            // 获取结果信息
            HashMap resMap = (HashMap) result;
            Gson gson = new Gson();
            nsmmUserBizLog.setResponseStatus(gson.toJson(resMap.get("status")));
            nsmmUserBizLog.setResponseMessage(gson.toJson(resMap.get("message")));
            nsmmUserBizLog.setResponseData(this.getResData(gson.toJson(resMap.get("data"))));
            nsmmUserBizLog.setResponsePage(gson.toJson(resMap.get("page")));
            // 获取注解信息
            BizLog bizLog = this.getClassInfo(nsmmUserBizLog.getClassName(), nsmmUserBizLog.getMethodName(), proceedingJoinPoint);
            String operationType = "";
            String operationName = "";
            if (bizLog != null) {
                operationType = bizLog.operationType();
                operationName = bizLog.operationName();
            }
            nsmmUserBizLog.setOperationType(operationType);
            nsmmUserBizLog.setOperationName(operationName);
            // 存储至文件log
            this.userBizLog.info(DateUtil.getDate(startDate, DateUtil.DATE_TIME_FORMAT) + " **** 用户ID 【{}】  用户编号 【{}】  真实姓名 【{}】  所属部门 【{}】  所属角色 【{}】  " +
                            "请求路径 【{}】  来源IP 【{}】  控制器 【{}】  方法 【{}】  " +
                            "操作类型 【{}】  操作名称 【{}】  " +
                            "返回状态 【{}】  返回消息 【{}】  返回分页 【{}】 返回数据 【{}】",
                    nsmmUserBizLog.getUserId(), nsmmUserBizLog.getUserBh(), nsmmUserBizLog.getUserRealName(), nsmmUserBizLog.getUserDeptName(), nsmmUserBizLog.getUserRoleName(),
                    nsmmUserBizLog.getRequestToUrl(), nsmmUserBizLog.getRequestFromIp(), nsmmUserBizLog.getClassName(), nsmmUserBizLog.getMethodName(),
                    nsmmUserBizLog.getOperationType(), nsmmUserBizLog.getOperationName(),
                    nsmmUserBizLog.getResponseStatus(), nsmmUserBizLog.getResponseMessage(), nsmmUserBizLog.getResponsePage(), nsmmUserBizLog.getResponseData());
            // 存储至数据库日志
            nsmmUserBizLog.setGenerateDate(startDate);
            this.userBizLogService.insertNsmmUserBizLog(nsmmUserBizLog);
        }
        return result;
    }

    private boolean checkFirst(Object object, BizUser bizUser) {
        return object != null && bizUser != null;
    }

    private String getResData(String resData) {
        if (StringUtils.isNotEmpty(resData) && resData.length() > 200) {
            return resData.substring(0, 199) + "...";
        }
        return resData;
    }

    private BizLog getClassInfo(String className, String methodName, ProceedingJoinPoint proceedingJoinPoint) throws ClassNotFoundException {
        if (StringUtil.isNotEmpty(className) && StringUtil.isNotEmpty(methodName)
                && Class.forName(className) != null && proceedingJoinPoint.getArgs() != null) {
            Object[] arguments = proceedingJoinPoint.getArgs();
            Class targetClass = Class.forName(className);
            Method[] methods = targetClass.getMethods();
            for (Method method : methods) {
                if (method.getName().equals(methodName)) {
                    Class[] clazzs = method.getParameterTypes();
                    if (clazzs.length == arguments.length && method.getAnnotation(BizLog.class) != null) {
                        return method.getAnnotation(BizLog.class);
                    }
                }
            }
        }
        return null;
    }

    private String getIpAddr(HttpServletRequest request) {
        String ipAddress = null;
        ipAddress = request.getHeader("x-forwarded-for");
        if (ipAddress == null || ipAddress.length() == 0
                || unknown.equalsIgnoreCase(ipAddress)) {
            ipAddress = request.getHeader("Proxy-Client-IP");
        }
        if (ipAddress == null || ipAddress.length() == 0
                || unknown.equalsIgnoreCase(ipAddress)) {
            ipAddress = request.getHeader("WL-Proxy-Client-IP");
        }
        if (ipAddress == null || ipAddress.length() == 0
                || unknown.equalsIgnoreCase(ipAddress)) {
            ipAddress = request.getRemoteAddr();
        }
        if (ipAddress != null && ipAddress.length() > 15 && ipAddress.contains(",")) {
            ipAddress = ipAddress.substring(0, ipAddress.indexOf(','));
        }
        return ipAddress;
    }

}
