package cn.com.koriesh.memo.shiro.token;

import cn.com.koriesh.memo.jwt.JwtTokenUtil;
import cn.com.koriesh.memo.shiro.po.BizUser;
import lombok.Data;
import org.apache.shiro.authc.AuthenticationToken;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Data
public class BizToken implements AuthenticationToken {

    private static final long serialVersionUID = 3499350627606042345L;

    private transient BizUser bizUser = new BizUser();

    public BizToken(Map jwtBizToken) {
        bizUser.setUserName((String) jwtBizToken.get("userName"));
        bizUser.setName((String) jwtBizToken.get("name"));
        bizUser.setUserJob((String) jwtBizToken.get("userJob"));
        bizUser.setDept((String) jwtBizToken.get("dept"));
        bizUser.setAvatar((String) jwtBizToken.get("avatar"));
        bizUser.setEmail((String) jwtBizToken.get("email"));
        bizUser.setIsEnabled((String) jwtBizToken.get("isEnabled"));
        bizUser.setCreateTime((String) jwtBizToken.get("createTime"));
        bizUser.setPhone((String) jwtBizToken.get("phone"));
        bizUser.setRoles((List<HashMap<String, Object>>) jwtBizToken.get("roles"));
        bizUser.setDataGroups((List<HashMap<String, Object>>) jwtBizToken.get("dataGroups"));
        bizUser.setResGroups((List<HashMap<String, Object>>) jwtBizToken.get("resGroups"));
    }

    /**
     * @Description: 详细信息
     * 这里封装了 biz 前缀的 user对象来标识
     * @CreateDate: 2020/11/2 2020/11/2
     * @UpdateDate: 2020/11/2 2020/11/2
     */
    @Override
    public Object getPrincipal() {
        return bizUser;
    }

    /**
     * @Description: token的凭证信息
     * 这里使用 biz 开头前缀来标识
     * @CreateDate: 2020/11/2 2020/11/2
     * @UpdateDate: 2020/11/2 2020/11/2
     */
    @Override
    public Object getCredentials() {
        return JwtTokenUtil.BIZ_SUBJECT + bizUser.getUserName();
    }

}
