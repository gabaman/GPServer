package com.stan.service;

import com.github.pagehelper.PageHelper;
import com.stan.dao.ElasticsearchDao;
import com.stan.mapper.*;
import com.stan.model.ESResult;
import com.stan.model.GPResult;
import com.stan.model.pojo.*;
import com.stan.model.vo.ContentResult;
import com.stan.utils.GPUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.additional.aggregation.AggregateCondition;
import tk.mybatis.mapper.additional.aggregation.AggregateType;
import tk.mybatis.mapper.additional.aggregation.AggregationProvider;
import tk.mybatis.mapper.entity.Example;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
        example.createCriteria().andIsNotNull("id");
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
    public List<GPWalkthrough> getWalkthroughByLocId(Long locId) {

        Example example = new Example(GPWalkthrough.class);
        example.createCriteria().andEqualTo("locid",locId);
        List<GPWalkthrough> list = walkthroughMapper.selectByExample(example);

        return list;
    }

    /**
     * 获取item
     * @param locId
     * @return
     */
    public GPResult getItemByTypeId(Long locId){
        Example example = new Example(GPItem.class);
        example.createCriteria().andEqualTo("locId",locId);
        List<GPItem> list = itemMapper.selectByExample(example);

        if (list.size() < 1 || list == null){
            return GPResult.build(400,"typeId不正确");
        }

        Example attributeExample = new Example(GPItemAttribute.class);
        attributeExample.createCriteria().andEqualTo("typeid",list.get(0).getTypeid());
        List<GPItemAttribute> attributeList = itemAttributeMapper.selectByExample(example);


        GPItem resItem = list.get(0);

        return GPResult.ok(GPUtil.convertItem(resItem,attributeList));

    }



    public GPResult getContentList(Long typeId,int pageNum,int pageSize) {

        Example typeExample = new Example(GPType.class);
        typeExample.createCriteria().andEqualTo("id",typeId);
        List<GPType> typeList = typeMapper.selectByExample(typeExample);
        if (typeList.size() == 0 || typeList == null) {
            return GPResult.build(400,"typeId不正确");
        }
        GPType type = typeList.get(0);

        List<ContentResult> temp = new ArrayList<>();

        if (type.getIsitem() == 0){
            Example itemExample = new Example(GPItem.class);
            itemExample.createCriteria().andEqualTo("typeid",type.getId());
            PageHelper.startPage(pageNum, pageSize);
            List<GPItem> itemRes = itemMapper.selectByExample(itemExample);
            for (GPItem wt:itemRes){
                ContentResult content = new ContentResult();
                content.setId(wt.getId());
                content.setImage(wt.getImage());
                content.setDescription("");
                content.setName(wt.getName());
                content.setLocId(wt.getLocid());
                temp.add(content);
                System.out.println(content.getName());

            }

            return GPResult.ok(temp);

        }else{

            Example wtExample = new Example(GPWalkthrough.class);
            wtExample.createCriteria().andEqualTo("typeid",type.getId());
            PageHelper.startPage(pageNum, pageSize);
            List<GPWalkthrough> wtRes = walkthroughMapper.selectByExample(wtExample);
//            List<GPWalkthrough> wtRes = this.getPicOneWalkthrough(type);
            System.out.println(wtRes.size());



            for (GPWalkthrough wt:wtRes){
                ContentResult content = new ContentResult();
                content.setId(wt.getId());
                content.setImage(wt.getContent());
                content.setDescription("");
                content.setName(wt.getTitle());
                content.setLocId(wt.getLocid());
                temp.add(content);
                System.out.println(content.getId()+"======="+content.getLocId());

            }
            return GPResult.ok(temp);
        }

    }

//    public List<GPWalkthrough> getPicOneWalkthrough(GPType type) {
//
//        if (type.getIsitem() == 0){
//            return null;
//        }
//
//
//        Example example = new Example(GPWalkthrough.class);
//        example.createCriteria().andEqualTo("istext",1).andEqualTo("typeid",type.getId());
//        example.setDistinct(true);
//
//        List<GPWalkthrough> list = walkthroughMapper.selectByExample(example);
//
//        return GPUtil.getWalkthroughOne(list);
//
//
//    }

}
