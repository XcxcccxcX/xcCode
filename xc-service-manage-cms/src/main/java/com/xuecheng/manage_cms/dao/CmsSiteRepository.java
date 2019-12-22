package com.xuecheng.manage_cms.dao;

import com.xuecheng.framework.domain.cms.CmsPage;
import com.xuecheng.framework.domain.cms.CmsSite;
import org.springframework.data.mongodb.repository.MongoRepository;

/**
 * Created by Enzo Cotter on 2019/12/12.
 * @author xcx
 */
public interface CmsSiteRepository extends MongoRepository<CmsSite,String> {
}
