package cn.com.koriesh.memo.service.impl;

import cn.com.koriesh.memo.entity.NsmmUserBizLog;
import cn.com.koriesh.memo.entity.PageData;
import cn.com.koriesh.memo.mapper.NsmmUserBizLogMapper;
import cn.com.koriesh.memo.service.UserBizLogService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;

@Slf4j
@Service("userBizLogService")
public class UserBizLogBusinessImpl implements UserBizLogService {

    @Resource
    private NsmmUserBizLogMapper nsmmUserBizLogMapper;

    @Override
    @Transactional
    public void insertNsmmUserBizLog(NsmmUserBizLog nsmmUserBizLog) {
        this.nsmmUserBizLogMapper.insert(nsmmUserBizLog);
    }

    @Override
    @Transactional
    public void deleteNsmmUserBizLog(PageData pageData) {
        // do something code here
    }

    @Override
    @Transactional
    public NsmmUserBizLog updateNsmmUserBizLog(PageData pageData) {
        return null;
    }

    @Override
    public PageInfo<NsmmUserBizLog> listNsmmUserBizLogPage(PageData pageData) {
        return null;
    }

    @Override
    public List<NsmmUserBizLog> listNsmmUserBizLogAll(PageData pageData) {
        return null;
    }

    @Override
    public NsmmUserBizLog getNsmmUserBizLog(PageData pageData) {
        return null;
    }

    /**
     * @Author: wangh
     * @Description: 初始化分页数据参数
     * @Date: 2020/11/16
     * @Param: [pageData]
     * @Return: void
     */
    private void initPageHelper(PageData pageData) {
        Object currentObj = pageData.get("current");
        Object pageSizeObj = pageData.get("pageSize");
        int current = 0;
        int pageSize = 10;
        if (currentObj == null || "".equals(currentObj.toString())) {
            current = 0;
        } else {
            current = pageData.getInt("current", 0);
        }
        if (pageSizeObj == null || "".equals(pageSizeObj.toString())) {
            pageSize = 10;
        } else {
            pageSize = pageData.getInt("pageSize", 0);
        }
        PageHelper.startPage(current, pageSize);
    }

}
