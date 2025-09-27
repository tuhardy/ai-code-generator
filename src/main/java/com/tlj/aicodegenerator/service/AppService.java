package com.tlj.aicodegenerator.service;

import com.mybatisflex.core.query.QueryWrapper;
import com.mybatisflex.core.service.IService;
import com.tlj.aicodegenerator.model.dto.app.AppQueryRequest;
import com.tlj.aicodegenerator.model.entity.App;
import com.tlj.aicodegenerator.model.vo.AppVO;

import java.util.List;

/**
 * 应用 服务层。
 *
 * @author <a href='https://github.com/tlj-x'>tlj</a>
 */
public interface AppService extends IService<App> {
    /**
     * 根据App实体获取AppVO对象
     * @param app
     * @return
     */
    AppVO getAppVO(App app);

    /**
     * 根据AppQueryRequest获取QueryWrapper对象
     * @param appQueryRequest
     * @return
     */
    QueryWrapper getQueryWrapper(AppQueryRequest appQueryRequest);

    /**
     * 根据App实体列表获取AppVO列表
     * @param appList
     * @return
     */
    List<AppVO> getAppVOList(List<App> appList);
}
