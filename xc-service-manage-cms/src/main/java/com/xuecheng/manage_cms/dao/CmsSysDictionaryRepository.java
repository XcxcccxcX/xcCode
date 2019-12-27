package com.xuecheng.manage_cms.dao;

import com.xuecheng.framework.domain.cms.CmsSite;
import com.xuecheng.framework.domain.system.SysDictionary;
import org.springframework.data.mongodb.repository.MongoRepository;

/**
 * Created by Enzo Cotter on 2019/12/12.
 * @author xcx
 */
public interface CmsSysDictionaryRepository extends MongoRepository<SysDictionary,String> {
    SysDictionary findByDType(String dType);
}
