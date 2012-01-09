package com.tfc.evolve.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.tfc.evolve.entity.AlternateResult;

@Service
@Transactional(readOnly = true)
public class EvolveService {
    /**
     * 这是交互入口处。所有的与“进化者”聊天的入口。
     * 
     * @param content
     * @return
     */
    public AlternateResult alternate(String content) {
        AlternateResult result = new AlternateResult();
        // 获取对应的历史信息
        result.addHistory(welcome());
        return result;
    }

    /**
     * 欢迎语
     * 
     * @return
     */
    public String welcome() {
        // TODO 需要根据不同的角色和用户来给出相应的提示语
        return "evolution：你好啊，陌生人。我有什么可以帮助你的吗？";
    }
}
