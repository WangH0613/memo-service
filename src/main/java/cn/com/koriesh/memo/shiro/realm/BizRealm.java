package cn.com.koriesh.memo.shiro.realm;

import cn.com.koriesh.memo.jwt.JwtTokenUtil;
import cn.com.koriesh.memo.shiro.po.BizUser;
import cn.com.koriesh.memo.shiro.token.BizToken;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.springframework.beans.factory.annotation.Value;

import java.util.HashSet;
import java.util.Set;

@Slf4j
public class BizRealm extends AuthorizingRealm {

    @Value("${spring.profiles.active}")
    private String porfileActive;

    public static final String AUTH_ROLE_ADMIN = "admin";

    public static final String AUTH_ROLE_USER = "user";

    /**
     * @Description: 在当前域中 使用定制的AuthenticationToken
     * @CreateDate: 2020/11/2 2020/11/2
     * @UpdateDate: 2020/11/2 2020/11/2
     */
    @Override
    public boolean supports(AuthenticationToken authenticationToken) {
        return authenticationToken instanceof BizToken;
    }

    /**
     * @Description: 身份验证控制
     * @CreateDate: 2020/11/2 2020/11/2
     * @UpdateDate: 2020/11/2 2020/11/2
     */
    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken authenticationToken) throws AuthenticationException {
        BizToken bizToken = (BizToken) authenticationToken;
        String token = (String) authenticationToken.getCredentials();
        if (token == null) {
            throw new AuthenticationException("token invalid");
        }
        if (token.startsWith(JwtTokenUtil.BIZ_SUBJECT)) {
            return new SimpleAuthenticationInfo(bizToken.getPrincipal(), bizToken.getCredentials(), this.getName());
        }
        return null;
    }

    /**
     * @Description: 授权管理控制
     * @CreateDate: 2020/11/2 2020/11/2
     * @UpdateDate: 2020/11/2 2020/11/2
     */
    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principalCollection) {
        String token = principalCollection.getPrimaryPrincipal().toString();
        if (token == null) {
            throw new AuthenticationException("token invalid");
        }
        SimpleAuthorizationInfo simpleAuthorizationInfo = new SimpleAuthorizationInfo();
        Set<String> roles = new HashSet<>();
        // 能进方案发请求默认起码有普通用户权限
        roles.add(AUTH_ROLE_USER);

        BizUser user = (BizUser) SecurityUtils.getSubject().getPrincipal();
        roles.add(AUTH_ROLE_ADMIN);
        log.info("用户权限包括{}", roles);
        simpleAuthorizationInfo.setRoles(roles);
        return simpleAuthorizationInfo;
    }
}



