package cn.com.koriesh.memo.shiro.po;

import lombok.Data;

import java.util.HashMap;
import java.util.List;

/**
 * @Description: 业务 - 用户的身份信息对象实体类
 * @CreateDate: 2020/11/19 2020/11/19
 * @UpdateDate: 2020/11/19 2020/11/19
 */
@Data
public class BizUser {

    private String userName;
    private String name;
    private String avatar;
    private String email;
    private String phone;
    private String dept;
    private String userJob;
    private String isEnabled;
    private String createTime;
    private List<HashMap<String,Object>> roles;
    private List<HashMap<String,Object>> resGroups;
    private List<HashMap<String,Object>> dataGroups;

    public HashMap<String, Object> covert2Map() {
        HashMap<String, Object> bizUserMap = new HashMap<>();
        bizUserMap.put("userName", this.getUserName());
        bizUserMap.put("name", this.getName());
        bizUserMap.put("avatar", this.getAvatar());
        bizUserMap.put("email", this.getEmail());
        bizUserMap.put("phone", this.getPhone());
        bizUserMap.put("dept", this.getDept());
        bizUserMap.put("userJob", this.getUserJob());
        bizUserMap.put("isEnabled", this.getIsEnabled());
        bizUserMap.put("createTime", this.getCreateTime());
        bizUserMap.put("roles",this.getRoles());
        bizUserMap.put("resGroups",this.getResGroups());
        return bizUserMap;
    }

}
