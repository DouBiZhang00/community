package life.jiarun.community2.dto;

import lombok.Data;


@Data
public class HotTagDTO implements Comparable {
    private String name;
    private Integer priority;
//重写compareTo，定义priorityqueue逻辑为值越小优先poll
    @Override
    public int compareTo(Object o) {
        return this.getPriority() - ((HotTagDTO) o).getPriority();
    }
}
