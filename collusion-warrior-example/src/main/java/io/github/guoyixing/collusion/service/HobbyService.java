package io.github.guoyixing.collusion.service;

import io.github.guoyixing.collusion.pojo.po.Hobby;

/**
 * @author 敲代码的旺财
 * @date 7/7/2022 下午10:07
 */
public interface HobbyService {
    /**
     * 添加一个爱好
     *
     * @param hobby 爱好信息
     * @return 爱好
     */
    Hobby save(Hobby hobby);

    /**
     * 删除一个爱好
     *
     * @param id 爱好id
     */
    void del(Long id);
}
