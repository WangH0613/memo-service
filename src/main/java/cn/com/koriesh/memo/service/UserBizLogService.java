package cn.com.koriesh.memo.service;

import cn.com.koriesh.memo.entity.NsmmUserBizLog;
import cn.com.koriesh.memo.entity.PageData;
import com.github.pagehelper.PageInfo;

import java.util.List;

public interface UserBizLogService {

    // 新增 - 用户日志 信息
    public void insertNsmmUserBizLog(NsmmUserBizLog nsmmUserBizLog);

    //**********************************

    // 删除 - 用户日志 信息
    public void deleteNsmmUserBizLog(PageData pageData);

    //**********************************

    // 修改 - 用户日志 信息
    public NsmmUserBizLog updateNsmmUserBizLog(PageData pageData);

    //**********************************

    // 分页 - 获取用户日志数据
    PageInfo<NsmmUserBizLog> listNsmmUserBizLogPage(PageData pageData);

    // 全部 - 获取用户日志数据
    List<NsmmUserBizLog> listNsmmUserBizLogAll(PageData pageData);

    // 获取 单个用户日志数据
    NsmmUserBizLog getNsmmUserBizLog(PageData pageData);

}
