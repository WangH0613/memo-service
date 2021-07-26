package cn.com.koriesh.memo.mapper;

import cn.com.koriesh.memo.entity.NsmmUserBizLog;
import cn.com.koriesh.memo.entity.PageData;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;

@Mapper
@Repository
public interface NsmmUserBizLogMapper {

    int insert(NsmmUserBizLog nsmmUserBizLog);

    int delete(PageData pageData);

    NsmmUserBizLog get(PageData pageData);

    List<NsmmUserBizLog> listAll(PageData pageData);

    List<NsmmUserBizLog> listPage(PageData pageData);

    int update(NsmmUserBizLog nsmmUserBizLog);

}
