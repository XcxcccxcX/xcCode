package com.xuecheng.manage_cms_client.dao;

import com.xuecheng.framework.domain.cms.CmsPage;
import org.springframework.data.mongodb.repository.MongoRepository;

/**
 * Created by Enzo Cotter on 2019/12/12.
 * @author xcx
 */
public interface CmsPageRepository extends MongoRepository<CmsPage,String> {
}
