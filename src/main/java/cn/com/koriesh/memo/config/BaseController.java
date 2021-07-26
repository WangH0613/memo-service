package cn.com.koriesh.memo.config;

import cn.com.koriesh.memo.common.ErrorCodeEnum;
import cn.com.koriesh.memo.entity.PageData;
import com.github.pagehelper.PageInfo;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.HashMap;
import java.util.UUID;

@Slf4j
public class BaseController {

    public static final String RES_STATUS = "status";
    public static final String RES_MESSAGE = "message";
    public static final String RES_DATA = "data";
    public static final String RES_PAGE = "page";

    public static final String RES_PAGE_CURRENT = "current";
    public static final String RES_PAGE_PAGESIZE = "pageSize";
    public static final String RES_PAGE_TOTAL = "total";

    public static final String CREATE_USER_ID = "createUserId";
    public static final String CREATE_USER_NAME = "createUserName";
    public static final String MODIFY_USER_ID = "modifyUserId";
    public static final String MODIFY_USER_NAME = "modifyUserName";
    public static final String MODEL_BASE_ID = "modelBaseId";
    public static final String COMBO_BASE_ID = "comboBaseId";
    public static final String SCHEME_BASE_ID = "schemeBaseId";
    public static final String MODEL_ROLE_BASE_ID = "modelRoleBaseId";
    public static final String COMBO_NODE_BASE_ID = "comboNodeBaseId";
    public static final String COMBO_VERSION = "comboVersion";
    public static final String DEVICE_GROUP_ID = "deviceGroupId";
    public static final String TECHNICAL_ATTRIBUTE_BASE_ID = "technicalAttributeBaseId";
    public static final String TECHNICAL_ATTRIBUTE_NAME = "technicalAttributeName";

    public static final String MODEL_VERSION = "modelVersion";


    public static final String CHECK_STATUS = "checkStatus";
    public static final String COMBO_STATUS = "comboStatus";

    public static final String DELETED = "deleted";

    public static final String TEST_DEPT_NAME = "网络管理处";

    public static final String USER_MENUS = "userMenus";


    Date getCurDate() {
        Date date = new Date();
        return date;
    }

    protected String getUuid() {
        String uuid = UUID.randomUUID().toString().toUpperCase().replaceAll("-", "");
        return uuid;
    }

    protected PageData getPageData() {
        PageData pageData = new PageData(this.getRequest());
        return pageData;
    }

    protected HttpServletRequest getRequest() {
        HttpServletRequest request = null;
        ServletRequestAttributes servletRequestAttributes =
                (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        if (servletRequestAttributes != null) {
            request = servletRequestAttributes.getRequest();
        }
        return request;
    }

    protected HashMap<String, Object> getResMap(String result, String msg) {
        HashMap<String, Object> returnMap = new HashMap<String, Object>();
        returnMap.put(RES_STATUS, result);
        returnMap.put(RES_MESSAGE, (msg == null || "".equals(msg)) ? "" : msg);
        returnMap.put(RES_DATA, null);
        returnMap.put(RES_PAGE, null);
        return returnMap;
    }

    protected HashMap<String, Object> getResMap(String result, String msg, Object data) {
        HashMap<String, Object> returnMap = new HashMap<String, Object>();
        returnMap.put(RES_STATUS, result);
        returnMap.put(RES_MESSAGE, (msg == null || "".equals(msg)) ? "" : msg);
        returnMap.put(RES_DATA, data == null ? "" : data);
        returnMap.put(RES_PAGE, null);
        return returnMap;
    }

    protected HashMap<String, Object> getResMap(String result, String msg, Object data, PageInfo page) {
        HashMap<String, Object> returnMap = new HashMap<String, Object>();
        returnMap.put(RES_STATUS, result);
        returnMap.put(RES_MESSAGE, (msg == null || "".equals(msg)) ? "" : msg);
        returnMap.put(RES_DATA, data == null ? "" : data);
        returnMap.put(RES_PAGE, page == null ? "" : this.initRetuenPageInfo(page));
        return returnMap;
    }

    /**
     * @Description: 获取biz过程中的用户信息
     * @CreateDate: 2020/10/27 2020/10/27
     * @UpdateDate: 2020/10/27 2020/10/27
     */
    protected Object getShiroPrincipal() {
        Subject sb = SecurityUtils.getSubject();
        return sb.getPrincipal();
    }

    protected HashMap<String, Object> checkBizUserNull() {
        return this.getResMap(ErrorCodeEnum.USER_ERROR_A0311.getCode(),
                ErrorCodeEnum.USER_ERROR_A0311.getDescription());
    }

    /**
     * @Description: 处理返回的分页 兼容接口规范要求
     * @CreateDate: 2020/10/26 2020/10/26
     * @UpdateDate: 2020/10/26 2020/10/26
     */
    private HashMap<String, Object> initRetuenPageInfo(PageInfo page) {
        if (page == null) {
            return null;
        }
        HashMap<String, Object> returnMap = new HashMap();
        returnMap.put(RES_PAGE_CURRENT, page.getPageNum());
        returnMap.put(RES_PAGE_PAGESIZE, page.getPageSize());
        returnMap.put(RES_PAGE_TOTAL, Integer.parseInt(String.valueOf(page.getTotal())));
        return returnMap;
    }

}
