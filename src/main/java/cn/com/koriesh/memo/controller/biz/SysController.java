package cn.com.koriesh.memo.controller.biz;

import cn.com.koriesh.memo.aop.annotation.BizLog;
import cn.com.koriesh.memo.common.ErrorCodeEnum;
import cn.com.koriesh.memo.config.BaseController;
import cn.com.koriesh.memo.config.Constant;
import cn.com.koriesh.memo.entity.PageData;
import cn.com.koriesh.memo.jwt.JwtTokenUtil;
import cn.com.koriesh.memo.shiro.po.BizUser;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.SecurityUtils;
import org.springframework.context.annotation.Scope;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.*;

@Slf4j
@RequestMapping(value = "/sys", produces = MediaType.APPLICATION_JSON_VALUE)
@Scope("prototype")
@RestController("sysController")
public class SysController extends BaseController {


    @BizLog(operationType = Constant.UBL_TYPE_LOGIN, operationName = "用户登入")
    @GetMapping(value = "/login")
    public HashMap<String, Object> login() {
        PageData pageData = this.getPageData();
        BizUser bizUser = new BizUser();
        // 在这里执行用户身份校验 调用open-feign客户端
        String userName = pageData.getString("username", "");
        if ("".equals(userName)) {
            return this.getResMap(
                    ErrorCodeEnum.USER_ERROR_A0150.getCode(),
                    ErrorCodeEnum.USER_ERROR_A0150.getDescription());
        }
        // 2、生成本系统内的token
        String tokenString = JwtTokenUtil.getAccessToken(JwtTokenUtil.BIZ_SUBJECT + bizUser.getUserName(), bizUser.covert2Map());
        // 3、返回自定义的结果
        HashMap<String, Object> userTokenMap = new HashMap<>();
        // 将token返回
        userTokenMap.put("token", tokenString);
        // 将用户信息返回
        userTokenMap.put("info", pageData);
        // 返回token
        return this.getResMap(
                ErrorCodeEnum.SUCCESS.getCode(),
                ErrorCodeEnum.SUCCESS.getDescription(),
                userTokenMap);
    }


    @BizLog(operationType = Constant.UBL_TYPE_LOGOUT, operationName = "用户登出")
    @GetMapping(value = "/logout")
    public HashMap<String, Object> logout() {
        SecurityUtils.getSubject().logout();
        // 返回token
        return this.getResMap(
                ErrorCodeEnum.SUCCESS.getCode(),
                ErrorCodeEnum.SUCCESS.getDescription());
    }
}
