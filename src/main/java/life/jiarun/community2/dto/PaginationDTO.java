package life.jiarun.community2.dto;

import lombok.Data;
import java.util.ArrayList;
import java.util.List;

@Data
public class PaginationDTO {
    //存储问题数据
    private List<QuestionDTO> questions;
    //显示页码按钮
    private boolean showPrevious;
    private boolean showFirstPage;
    private boolean showNext;
    private boolean showEndPage;
    private Integer page;
    //按钮显示页码的数字集合
    private List<Integer> pages = new ArrayList<>();
    private Integer totalPage;

    //输入页数总数，当前页数，目标分页数据显示数量进行设置分页相关的数据，使用List存储页面下标
    public void setPagination(Integer totalPage, Integer page) {
        //得到页面总数
        this.totalPage = totalPage;
        this.page = page;
        pages.add(page);

        //起始页和最终页显示四页，最多显示7页
        for(int i = 1; i <= 3; i++){
            if(page - i > 0){
                pages.add(0,page - i);
            }
            if(page + i <= totalPage){
                pages.add(page+i);
            }
        }

        //是否展示上一页
        if(page == 1){
            showPrevious = false;
        }else{
            showPrevious = true;
        }

        //是否展示下一页
        if(page == totalPage){
            showNext = false;
        }else{
            showNext = true;
        }

        //是否展示第一页
        if(pages.contains(1)){
            showFirstPage = false;
        }else {
            showFirstPage = true;
        }

        //是否展示最后一页
        if(pages.contains(totalPage)){
            showEndPage = false;
        }else{
            showEndPage = true;
        }
    }
}
