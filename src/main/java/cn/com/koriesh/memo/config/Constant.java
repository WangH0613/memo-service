package cn.com.koriesh.memo.config;

public class Constant {
    public static final String BIZ_ACCESS_TOKEN = "memo-access-token";
    // 以下开始用户注解
    // userBizLog 缩写 UBL
    //   操作类型相关
    public static final String UBL_TYPE_INSERT = "新增";
    public static final String UBL_TYPE_UPDATE = "修改";
    public static final String UBL_TYPE_SEARCH = "查询";
    public static final String UBL_TYPE_DELETE = "删除";
    public static final String UBL_TYPE_LOGIN = "登入";
    public static final String UBL_TYPE_LOGOUT = "登出";
    public static final String UBL_TYPE_UPLOAD = "上传";
    public static final String UBL_TYPE_DOWNLOAD = "下载";
    // 配置文件相关
    public static final String PROFILES_DEV = "dev";
    public static final String PROFILES_PROD = "prod";
    public static final String PROFILES_PTEST_CCB = "ptest-ccb";
    public static final String PROFILES_PTEST_VT = "ptest-vt";
    public static final String PROFILES_PTEST_HX = "ptest-hx";

    // ************** openFeign 相关
    //定义token 前缀
    public static final String TOKEN_PREFIX = "Bearer ";
}
