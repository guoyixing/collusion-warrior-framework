package io.github.guoyixing.collusion.service.impl;

import io.github.guoyixing.collusion.pojo.po.Hobby;
import io.github.guoyixing.collusion.repository.db.HobbyRepository;
import io.github.guoyixing.collusion.service.HobbyService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author 敲代码的旺财
 * @date 7/7/2022 下午10:23
 */
@Slf4j
@Service
@Transactional(rollbackFor = Exception.class)
public class HobbyServiceImpl implements HobbyService {

    @Autowired
    private HobbyRepository hobbyRepository;

    /**
     * 添加一个爱好
     *
     * @param hobby 爱好信息
     * @return 爱好
     */
    @Override
    public Hobby save(Hobby hobby) {
        return hobbyRepository.save(hobby);
    }

    /**
     * 删除一个爱好
     *
     * @param id 爱好id
     */
    @Override
    public void del(Long id) {
        Hobby hobby = hobbyRepository.findById(id).get();
        hobbyRepository.delete(hobby);
    }
}
