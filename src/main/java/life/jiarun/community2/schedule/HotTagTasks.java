package life.jiarun.community2.schedule;


import life.jiarun.community2.cache.HotTagCache;
import life.jiarun.community2.mapper.QuestionMapper;
import life.jiarun.community2.model.Question;
import life.jiarun.community2.model.QuestionExample;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.session.RowBounds;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.*;

@Component
@Slf4j
public class HotTagTasks {

    @Autowired
    private QuestionMapper questionMapper;

    @Autowired
    private HotTagCache hotTagCache;
//定时：两分钟刷新一次
    @Scheduled(fixedDelay = 120000)
    public void hotTagSchedule() {
        int offset = 0;
        int limit = 5;
        log.info("hotTagSchedule start {}", new Date());
        List<Question> list = new ArrayList<>();
//        利用map自动去重，根据评论数量增加热度标签数值
        Map<String, Integer> priorities = new HashMap<>();

        //首次运行以及数据包含有下一页的数据的情况下都会进去，确保计算了所有题目的标签数据
        while (offset == 0 || list.size() == limit) {
            list = questionMapper.selectByExampleWithRowbounds(new QuestionExample(), new RowBounds(offset, limit));
            for (Question question : list) {
                //得到每个问题的全部标签，并根据标签进行累加热值，热值根据为评论数量
                String[] tags = StringUtils.split(question.getTag(), ",");
                for (String tag : tags) {
                    Integer priority = priorities.get(tag);
                    if (priority != null) {
                        priorities.put(tag, priority + 5 + question.getCommentCount());
                    } else {
                        priorities.put(tag, 5 + question.getCommentCount());
                    }
                }
            }
            //增加偏移量
            offset += limit;
        }
        //根据定时器更新的标签-热值map，更新热值标签
        hotTagCache.updateTags(priorities);
        log.info("hotTagSchedule stop {}", new Date());
    }
}
