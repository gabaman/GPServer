package com.stan.service;

import com.github.abel533.entity.Example;
import com.stan.dao.ElasticsearchDao;
import com.stan.mapper.*;
import com.stan.model.ESResult;
import com.stan.model.GPResult;
import com.stan.model.pojo.GPGame;
import com.stan.model.pojo.GPItem;
import com.stan.model.pojo.GPType;
import com.stan.model.pojo.GPWalkthrough;
import com.stan.model.vo.ContentResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class GameService {

    @Autowired
    private GPGameMapper gameMapper;

    @Autowired
    private GPTypeMapper typeMapper;

    @Autowired
    private GPWalkthroughMapper walkthroughMapper;

    @Autowired
    private GPItemMapper itemMapper;

    @Autowired
    private GPItemAttributeMapper itemAttributeMapper;

    @Autowired
    private ElasticsearchDao esDao;

    public ESResult findAll(String query) {
        return esDao.searchAll(query,0,10);
    }

    /**
     * 获取游戏列表
     * @return
     */
    public List<GPGame> getGameList() {

        Example example = new Example(GPGame.class);
        example.createCriteria().andIsNull("typeid");
        List<GPGame> list = gameMapper.selectByExample(example);
        return list;
    }

    /**
     * 获取type列表
     * @param gameid
     * @return
     */
    public List<GPType> getGameCategory(Long gameid) {

        Example example = new Example(GPType.class);
        example.createCriteria().andEqualTo("gameid",gameid);
        List<GPType> list = typeMapper.selectByExample(example);
        return list;
    }

    /**
     * 获取攻略
     * @param locId
     * @return
     */
    public List<GPWalkthrough> getWalkthroughByLocId(String locId) {

        Example example = new Example(GPWalkthrough.class);
        example.createCriteria().andEqualTo("locid",locId);
        List<GPWalkthrough> list = walkthroughMapper.selectByExample(example);

        return list;
    }

    /**
     * 获取item
     * @param typeid
     * @return
     */
    public GPItem getItemByTypeId(String typeid){
        Example example = new Example(GPItem.class);
        example.createCriteria().andEqualTo("typeid",typeid);
        List<GPItem> list = itemMapper.selectByExample(example);
        return list.get(0);

    }

    public GPResult getContentList(String typeId) {

        Example typeExample = new Example(GPType.class);
        typeExample.createCriteria().andEqualTo("id",typeId);
        List<GPType> typeList = typeMapper.selectByExample(typeExample);
        if (typeList.size() == 0 || typeList == null) {
            return GPResult.build(400,"typeId不正确");
        }
        GPType type = typeList.get(0);

        List<ContentResult> temp = new ArrayList<>();

        if (type.getIsItem() == 1){
            Example itemExample = new Example(GPItem.class);
            itemExample.createCriteria().andEqualTo("typeid",type.getId());
            List<GPItem> itemRes = itemMapper.selectByExample(itemExample);
            return GPResult.ok(temp);

        }else{
            List<GPWalkthrough> wtRes = this.getPicOneWalkthrough(type);
            for (GPWalkthrough wt:wtRes){
                ContentResult content = new ContentResult();
                content.setId(wt.getId());
                content.setImage(wt.getContent());
                content.setDescription("");
                content.setName(wt.getTitle());
                content.setLocId(wt.getLocid());
                temp.add(content);
            }
            return GPResult.ok(temp);
        }

    }

    public List<GPWalkthrough> getPicOneWalkthrough(GPType type) {

        if (type.getIsItem() == 0){
            return null;
        }


        Example example = new Example(GPWalkthrough.class);
        example.createCriteria().andEqualTo("istext",1).andEqualTo("typeid",type.getId());
        List<GPWalkthrough> list = walkthroughMapper.selectByExample(example);
        List<GPWalkthrough> temp = new ArrayList<>();
        int tag = 1;
        for (int i = 0; i < list.size(); i++) {
            GPWalkthrough wt = list.get(i);
            String prefix = type.getTypeprefix().toString();
            String tagStr = prefix + String.valueOf(tag);

            if (wt.getTypeid().equals(tagStr)) {
                tag++;
                System.out.println("tag="+tag);
                temp.add(wt);
            }
        }
        return temp;
    }

}
