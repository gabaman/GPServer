package com.stan.service;

import com.github.abel533.entity.Example;
import com.stan.mapper.GPItemMapper;
import com.stan.mapper.GPWalkthroughMapper;
import com.stan.model.pojo.GPItem;
import com.stan.model.pojo.GPWalkthrough;
import com.stan.utils.GPUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ScriptService {

    @Autowired
    private GPItemMapper itemMapper;

    @Autowired
    private GPWalkthroughMapper walkthroughMapper;

    public List<GPItem> getAllItem(){
        Example example = new Example(GPItem.class);
        example.createCriteria().andIsNotNull("id");
        return itemMapper.selectByExample(example);
    }

    public List<GPWalkthrough> getAllWalkthrough(){
        Example example = new Example(GPWalkthrough.class);
        example.createCriteria().andIsNotNull("id").andEqualTo("istext",1);
        return walkthroughMapper.selectByExample(example);
    }

    public void downloadWalkthroughPic(){
        Integer tag = 1;
        int lastLocId = 0;
        for (GPWalkthrough wt:this.getAllWalkthrough()){

//
            if (lastLocId == wt.getLocid().intValue()){
                tag ++;
            }else {
                lastLocId = wt.getLocid().intValue();
                tag = 1;
            }
            String img = wt.getLocid().toString()+"_"+tag.toString();

            String url = "https://gamepedia-1257100500.cos.ap-shanghai.myqcloud.com/zelda/" + img +".jpg";
            wt.setContent(url);
            walkthroughMapper.updateByPrimaryKeySelective(wt);
//            GPUtil.downloadPicture(wt.getContent(),img,"zelda");
            System.out.println(url);
//
        }
    }
}
