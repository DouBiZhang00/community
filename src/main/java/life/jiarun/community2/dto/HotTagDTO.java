package life.jiarun.community2.dto;

import lombok.Data;
import org.springframework.stereotype.Service;


@Data
public class HotTagDTO implements Comparable {
    private String name;
    private Integer priority;

    @Override
    public int compareTo(Object o) {
        return this.getPriority() - ((HotTagDTO) o).getPriority();
    }
}
