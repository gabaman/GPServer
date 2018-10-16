package com.stan.service;

import com.github.abel533.entity.Example;
import com.github.pagehelper.PageHelper;
import com.stan.dao.ElasticsearchDao;
import com.stan.mapper.*;
import com.stan.model.GPResult;
import com.stan.model.pojo.*;
import com.stan.model.vo.FinderResult;
import com.stan.utils.COSClientUtil;
import com.stan.utils.GPUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class ConsoleService {

    @Autowired
    private GPItemMapper itemMapper;

    @Autowired
    private GPTypeMapper typeMapper;

    @Autowired
    private GPGameMapper gameMapper;

    @Autowired
    private GPWalkthroughMapper walkthroughMapper;

    @Autowired
    private GPItemAttributeMapper attributeMapper;

    @Autowired
    private ElasticsearchDao esDao;


    public List<FinderResult> finder(String key,Long gameId,Boolean isItem){
        if (isItem){
            return this.itemFinderModelList(key,gameId);
        }else {
            return this.walkthroughFinderModelList(key,gameId);
        }
    }

    public int replaceKey(String key,String replace,Long gameId,Boolean isItem){
        if (isItem){
            return this.itemReplace(key,replace,gameId);
        }else {
            return this.walkthroughReplace(key,replace,gameId);
        }
    }

    private List<FinderResult> itemFinderModelList(String key,Long gameId){

        List<FinderResult> finderResultList = new ArrayList<>();

        Example typeExample = new Example(GPType.class);
        typeExample.createCriteria().andEqualTo("gameid",gameId).andEqualTo("isitem",0);
        List<GPType> typeList = typeMapper.selectByExample(typeExample);

        for (GPType type:typeList){
            Example attributeExample = new Example(GPItemAttribute.class);
            attributeExample.createCriteria().andEqualTo("typeid",type.getId());
            List<GPItemAttribute> attributeList = attributeMapper.selectByExample(attributeExample);

            List<GPItemAttribute> tagList = new ArrayList<>();

            for (GPItemAttribute attribute:attributeList){
                if (attribute.getSearchable() ==  0){
                    tagList.add(attribute);
                }
            }


            Example example = new Example(GPItem.class);
            example.createCriteria().andEqualTo("typeid",type.getId());
            List<GPItem> list = itemMapper.selectByExample(example);

            for (GPItem item:list){
                if (item.getName().contains(key)){
                    FinderResult finderResult = new FinderResult(item.getLocid(),type.getTypename(),"name",item.getName());
                    finderResultList.add(finderResult);
                }

                if (item.getDescription().contains(key)){
                    FinderResult finderResult = new FinderResult(item.getLocid(),type.getTypename(),"description",item.getDescription());
                    finderResultList.add(finderResult);
                }

                for (GPItemAttribute attribute:tagList){
                    switch (attribute.getAttributeIndex().intValue()){
                        case 0:
                            if (item.getAttribute1().contains(key)){
                                FinderResult finderResult = new FinderResult(item.getLocid(),type.getTypename(),attribute.getName(),item.getAttribute1());
                                finderResultList.add(finderResult);
                            }
                            break;
                        case 1:
                            if (item.getAttribute2().contains(key)){
                                FinderResult finderResult = new FinderResult(item.getLocid(),type.getTypename(),attribute.getName(),item.getAttribute2());
                                finderResultList.add(finderResult);
                            }
                            break;
                        case 2:
                            if (item.getAttribute3().contains(key)){
                                FinderResult finderResult = new FinderResult(item.getLocid(),type.getTypename(),attribute.getName(),item.getAttribute3());
                                finderResultList.add(finderResult);
                            }
                            break;
                        case 3:
                            if (item.getAttribute4().contains(key)){
                                FinderResult finderResult = new FinderResult(item.getLocid(),type.getTypename(),attribute.getName(),item.getAttribute4());
                                finderResultList.add(finderResult);
                            }
                            break;
                        case 4:
                            if (item.getAttribute5().contains(key)){
                                FinderResult finderResult = new FinderResult(item.getLocid(),type.getTypename(),attribute.getName(),item.getAttribute5());
                                finderResultList.add(finderResult);
                            }
                            break;
                        case 5:
                            if (item.getAttribute6().contains(key)){
                                FinderResult finderResult = new FinderResult(item.getLocid(),type.getTypename(),attribute.getName(),item.getAttribute6());
                                finderResultList.add(finderResult);
                            }
                            break;
                        case 6:
                            if (item.getAttribute7().contains(key)){
                                FinderResult finderResult = new FinderResult(item.getLocid(),type.getTypename(),attribute.getName(),item.getAttribute7());
                                finderResultList.add(finderResult);
                            }
                            break;
                        case 7:
                            if (item.getAttribute8().contains(key)){
                                FinderResult finderResult = new FinderResult(item.getLocid(),type.getTypename(),attribute.getName(),item.getAttribute8());
                                finderResultList.add(finderResult);
                            }
                            break;
                            default:break;
                    }

                }


            }

        }

        return finderResultList;

    }


    private List<FinderResult> walkthroughFinderModelList(String key,Long gameId) {

        List<FinderResult> finderResultList = new ArrayList<>();

        Example typeExample = new Example(GPType.class);
        typeExample.createCriteria().andEqualTo("gameid",gameId).andEqualTo("isitem",1);
        List<GPType> typeList = typeMapper.selectByExample(typeExample);

        for (GPType type:typeList) {
            Example wtExample = new Example(GPWalkthrough.class);
            wtExample.createCriteria().andEqualTo("typeid",type.getId()).andEqualTo("istext",0);
            List<GPWalkthrough> wtList = walkthroughMapper.selectByExample(wtExample);

            for (GPWalkthrough wt:wtList){
                if (wt.getTitle().contains(key)){
                    FinderResult finderResult = new FinderResult(wt.getLocid(),type.getTypename(),"title",wt.getTitle());
                    finderResultList.add(finderResult);
                }

                if (wt.getContent().contains(key)){
                    FinderResult finderResult = new FinderResult(wt.getLocid(),type.getTypename(),"content",wt.getContent());
                    finderResultList.add(finderResult);
                }
            }
        }


        return finderResultList;
    }


    private int itemReplace(String key,String replace,Long gameId){
        List<FinderResult> list = this.itemFinderModelList(key,gameId);



        if (list.size() < 1){
            return 0;
        }
        int tag = 0 ;
        int temp = 0;

        for (FinderResult finder:list){
            Example itemExample = new Example(GPItem.class);
            itemExample.createCriteria().andEqualTo("locid",finder.getLocId());
            List<GPItem> itemList = itemMapper.selectByExample(itemExample);
            GPItem item =  itemList.get(0);

            if (temp ==item.getLocid().intValue()){
                continue;
            }


            temp = item.getLocid().intValue();


            item.setName(item.getName().replace(key,replace));
            item.setDescription(item.getDescription().replace(key,replace));
            item.setAttribute1(item.getAttribute1().replace(key,replace));
            item.setAttribute2(item.getAttribute2().replace(key,replace));
            item.setAttribute3(item.getAttribute3().replace(key,replace));
            item.setAttribute4(item.getAttribute4().replace(key,replace));
            item.setAttribute5(item.getAttribute5().replace(key,replace));
            item.setAttribute6(item.getAttribute6().replace(key,replace));
            item.setAttribute7(item.getAttribute7().replace(key,replace));
            item.setAttribute8(item.getAttribute8().replace(key,replace));

            tag ++;
        }
        return tag;

    }

    private int walkthroughReplace(String key,String replace,Long gameId){
        List<FinderResult> list = this.walkthroughFinderModelList(key,gameId);
        if (list.size() < 1){
            return 0;
        }

        int tag = 0 ;
        int temp = 0;

        for (FinderResult finder:list){
            Example wtExample = new Example(GPWalkthrough.class);
            wtExample.createCriteria().andEqualTo("locid",finder.getLocId());
            List<GPWalkthrough> wtList = walkthroughMapper.selectByExample(wtExample);
            GPWalkthrough item =  wtList.get(0);

            if (temp == item.getLocid().intValue()){
                continue;
            }
            temp = item.getLocid().intValue();


            item.setContent(item.getContent().replace(key,replace));
            item.setTitle(item.getTitle().replace(key,replace));


            tag ++;


        }

        return tag;

    }

    public GPResult retrieveData(Long typeId,int pageNum,int pageSize){

        Example typeExample = new Example(GPType.class);
        typeExample.createCriteria().andEqualTo("typeid",typeId);
        List<GPType> typeList = typeMapper.selectByExample(typeExample);

        if (typeList.size() < 1 || typeList == null){
            return GPResult.build(400,"未找到对应的typeId");
        }





        if (typeList.get(0).getIsItem() == Long.valueOf(0)){

            Example attributeExample = new Example(GPItemAttribute.class);
            attributeExample.createCriteria().andEqualTo("typeid",typeList.get(0).getId());
            List<GPItemAttribute> attributeList = attributeMapper.selectByExample(attributeExample);

            Example example = new Example(GPItem.class);
            example.createCriteria().andEqualTo("typeid",typeId);
            PageHelper.startPage(pageNum, pageSize);
            List<GPItem> list = itemMapper.selectByExample(example);


            List<Map> res = new ArrayList<>();
            for (GPItem item:list){
                Map map = GPUtil.convertItem(item,attributeList);
                res.add(map);
            }

            return GPResult.ok(res);

        }else{
            Example example = new Example(GPWalkthrough.class);
            example.createCriteria().andEqualTo("typeid",typeId);
            PageHelper.startPage(pageNum, pageSize);
            List<GPWalkthrough> list = walkthroughMapper.selectByExample(example);

            return GPResult.ok(list);

        }

    }

    public GPResult typeList(Long gameId){




        Example example = new Example(GPType.class);
        example.createCriteria().andEqualTo("gameid",gameId);
        List<GPType> list = typeMapper.selectByExample(example);
        if (list.size()<1||list == null){
            return GPResult.build(400,"找不到对应的type");
        }
        return GPResult.ok(list);
    }


    public GPResult gameList(){

        Example example = new Example(GPGame.class);
        example.createCriteria().andIsNotNull("id");
        List<GPGame> list = gameMapper.selectByExample(example);
        if (list.size()<1||list == null){
            return GPResult.build(400,"找不到game");
        }
        return GPResult.ok(list);
    }

    public GPResult itemUpdate(Map<String,String> map,Long locId){

        Example itemExample = new Example(GPItem.class);
        itemExample.createCriteria().andEqualTo("locId",locId);
        List<GPItem> itemList = itemMapper.selectByExample(itemExample);

        if (itemList.size() < 1 || itemList == null){
            return GPResult.build(400,"找不到对应的Item");

        }

        Example example = new Example(GPType.class);
        example.createCriteria().andEqualTo("typeid",itemList.get(0).getTypeid());
        List<GPType> typeList = typeMapper.selectByExample(example);



        Example attributeExample = new Example(GPItemAttribute.class);
        example.createCriteria().andEqualTo("typeid",typeList.get(0).getId());
        List<GPItemAttribute> attributeList = attributeMapper.selectByExample(example);


        GPItem thisItem = itemList.get(0);

        for (GPItemAttribute attribute:attributeList){
            for (Map.Entry<String, String> entry : map.entrySet()) {
                thisItem = this.setGPItem(thisItem,attribute,entry.getKey(),entry.getValue());
            }
        }

        int res = itemMapper.updateByPrimaryKeySelective(thisItem);

        return GPResult.ok(res);
    }


    private GPItem setGPItem(GPItem item,GPItemAttribute attribute,String key,String value){

        if (!key.equals(attribute.getName())){
            return item;
        }

        switch (attribute.getAttributeIndex().intValue()){
            case 0:
                item.setAttribute1(value);
                break;
            case 1:
                item.setAttribute2(value);
                break;
            case 2:
                item.setAttribute3(value);
                break;
            case 3:
                item.setAttribute4(value);
                break;
            case 4:
                item.setAttribute5(value);
                break;
            case 5:
                item.setAttribute6(value);
                break;
            case 6:
                item.setAttribute7(value);
                break;
            case 7:
                item.setAttribute8(value);
                break;
            default:break;
        }

        return item;
    }

    public GPResult walkthroughUpdateContent(Long id,String content){
        Example example = new Example(GPWalkthrough.class);
        example.createCriteria().andEqualTo("id",id).;
        List<GPWalkthrough> walkthroughList = walkthroughMapper.selectByExample(example);
        if (walkthroughList.size() <1 || walkthroughList == null){
            GPResult.build(400,"找不到对应id");
        }
        GPWalkthrough wt = walkthroughList.get(0);
        wt.setContent(content);
        return GPResult.ok(walkthroughMapper.updateByPrimaryKeySelective(wt));

    }

    public GPResult walkthroughUpdateTitle(Long typeId,String title){
        Example example = new Example(GPWalkthrough.class);
        example.createCriteria().andEqualTo("typeId",typeId).;
        List<GPWalkthrough> walkthroughList = walkthroughMapper.selectByExample(example);
        if (walkthroughList.size() <1 || walkthroughList == null){
            GPResult.build(400,"找不到对应id");
        }

        int tag = 0;
        for (GPWalkthrough wt:walkthroughList){
            GPWalkthrough temp = wt;
            temp.setTitle(title);
            tag = tag + walkthroughMapper.updateByPrimaryKeySelective(temp);
        }

        return GPResult.ok(tag);

    }

    public GPResult esSaveAll(Long gameId){
        Example example = new Example(GPType.class);
        example.createCriteria().andEqualTo("gameid",gameId);
        List<GPType> typeList = typeMapper.selectByExample(example);

        List<Map> temp = new ArrayList<>();

        for (GPType type:typeList) {
            if (type.getIsItem().intValue() == 0){
                temp.addAll(this.esSaveAllItem(type.getId()));
            }else {
                temp.addAll(this.esSaveAllWalkthrought(type.getId(),type.getSearchImage()));
            }
        }



        if (temp.size() <1){
            return GPResult.ok();
        }else{
            return GPResult.build(400,"同步数据错误",temp);
        }
    }

    private List<Map> esSaveAllItem(Long typeId){
        Example example = new Example(GPItem.class);
        example.createCriteria().andEqualTo("typeId",typeId);
        List<GPItem> itemList = itemMapper.selectByExample(example);
        List<Map> temp = new ArrayList<>();

        for (GPItem item:itemList){
            Boolean isSuccess = esDao.saveItem(item);
            if (!isSuccess){
                Map map = new HashMap();
                map.put("locid",item.getLocid());
                map.put("error","es保存失败");
                temp.add(map);
            }
        }
        return temp;

    }

    private List<Map> esSaveAllWalkthrought(Long typeId,String imageName){
        Example example = new Example(GPWalkthrough.class);
        example.createCriteria().andEqualTo("typeId",typeId).andEqualTo("istext",0);
        List<GPWalkthrough> wtlist = walkthroughMapper.selectByExample(example);

        List<Map> temp = new ArrayList<>();
        List<GPWalkthrough> resList =  GPUtil.getWalkthroughOne(wtlist);

        for (GPWalkthrough wt:resList){
            Boolean isSuccess = esDao.saveWalkthrough(wt,imageName);
            if (!isSuccess){
                Map map = new HashMap();
                map.put("locid",wt.getLocid());
                map.put("error","es保存失败");
                temp.add(map);
            }
        }
        return temp;

    }

    public GPResult uploadImageWithTypeId(MultipartFile file, Long typeId) throws Exception {

        COSClientUtil util = new COSClientUtil();

        if (typeId.startsWith("1")||typeId.startsWith("0")) {
            return GPResult.build(400, "不能修改temple和主线的图片");
        }
        if (!findTypeId(typeId)){
            return GPResult.build(400, "未能找到typeId");
        }

        String name = util.uploadFile2Cos(file,"content/"+typeId+".jpg");
        String imgUrl = util.getImgUrl(name);
        String[] split = imgUrl.split("\\?");

        return GPResult.ok(split[0]);

    }

}


