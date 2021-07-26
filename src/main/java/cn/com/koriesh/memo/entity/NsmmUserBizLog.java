package cn.com.koriesh.memo.entity;

import lombok.Data;

import java.util.Date;

@Data
public class NsmmUserBizLog {

    private Long userBizLogId;

    private String userId;

    private String userBh;

    private String userRealName;

    private String userRoleId;

    private String userRoleName;

    private String userDeptId;

    private String userDeptName;

    private String requestFromIp;

    private String requestToUrl;

    private String className;

    private String methodName;

    private String operationType;

    private String operationName;

    private String responseStatus;

    private String responseMessage;

    private String responseData;

    private String responsePage;

    private Date generateDate;

}
