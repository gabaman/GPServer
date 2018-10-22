package com.stan.service;

import tk.mybatis.mapper.entity.Example;
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


    public List<FinderResult> finder(String key, Long gameId, Boolean isItem) {
        if (isItem) {
            return this.itemFinderModelList(key, gameId);
        } else {
            return this.walkthroughFinderModelList(key, gameId);
        }
    }

    public int replaceKey(String key, String replace, Long gameId, Boolean isItem) {
        if (isItem) {
            return this.itemReplace(key, replace, gameId);
        } else {
            return this.walkthroughReplace(key, replace, gameId);
        }
    }

    private List<FinderResult> itemFinderModelList(String key, Long gameId) {


        List<FinderResult> finderResultList = new ArrayList<>();

        Example typeExample = new Example(GPType.class);
        typeExample.createCriteria().andEqualTo("gameid", gameId).andEqualTo("isitem", 0);
        List<GPType> typeList = typeMapper.selectByExample(typeExample);

        for (GPType type : typeList) {
            Example attributeExample = new Example(GPItemAttribute.class);
            attributeExample.createCriteria().andEqualTo("typeid", type.getId());
            List<GPItemAttribute> attributeList = attributeMapper.selectByExample(attributeExample);

            List<GPItemAttribute> tagList = new ArrayList<>();

            for (GPItemAttribute attribute : attributeList) {
                if (attribute.getSearchable() == 0) {
                    tagList.add(attribute);
                }
            }


            Example example = new Example(GPItem.class);
            example.createCriteria().andEqualTo("typeid", type.getId());
            List<GPItem> list = itemMapper.selectByExample(example);

            for (GPItem item : list) {
                if (item.getName().contains(key)) {
                    FinderResult finderResult = new FinderResult(item.getLocid(), type.getTypename(), "name", item.getName());
                    finderResultList.add(finderResult);
                }

                if (item.getDescription().contains(key)) {
                    FinderResult finderResult = new FinderResult(item.getLocid(), type.getTypename(), "description", item.getDescription());
                    finderResultList.add(finderResult);
                }

                for (GPItemAttribute attribute : tagList) {
                    switch (attribute.getAttributeindex().intValue()) {
                        case 0:
                            if (item.getAttribute1().contains(key)) {
                                FinderResult finderResult = new FinderResult(item.getLocid(), type.getTypename(), attribute.getName(), item.getAttribute1());
                                finderResultList.add(finderResult);
                            }
                            break;
                        case 1:
                            if (item.getAttribute2().contains(key)) {
                                FinderResult finderResult = new FinderResult(item.getLocid(), type.getTypename(), attribute.getName(), item.getAttribute2());
                                finderResultList.add(finderResult);
                            }
                            break;
                        case 2:
                            if (item.getAttribute3().contains(key)) {
                                FinderResult finderResult = new FinderResult(item.getLocid(), type.getTypename(), attribute.getName(), item.getAttribute3());
                                finderResultList.add(finderResult);
                            }
                            break;
                        case 3:
                            if (item.getAttribute4().contains(key)) {
                                FinderResult finderResult = new FinderResult(item.getLocid(), type.getTypename(), attribute.getName(), item.getAttribute4());
                                finderResultList.add(finderResult);
                            }
                            break;
                        case 4:
                            if (item.getAttribute5().contains(key)) {
                                FinderResult finderResult = new FinderResult(item.getLocid(), type.getTypename(), attribute.getName(), item.getAttribute5());
                                finderResultList.add(finderResult);
                            }
                            break;
                        case 5:
                            if (item.getAttribute6().contains(key)) {
                                FinderResult finderResult = new FinderResult(item.getLocid(), type.getTypename(), attribute.getName(), item.getAttribute6());
                                finderResultList.add(finderResult);
                            }
                            break;
                        case 6:
                            if (item.getAttribute7().contains(key)) {
                                FinderResult finderResult = new FinderResult(item.getLocid(), type.getTypename(), attribute.getName(), item.getAttribute7());
                                finderResultList.add(finderResult);
                            }
                            break;
                        case 7:
                            if (item.getAttribute8().contains(key)) {
                                FinderResult finderResult = new FinderResult(item.getLocid(), type.getTypename(), attribute.getName(), item.getAttribute8());
                                finderResultList.add(finderResult);
                            }
                            break;
                        default:
                            break;
                    }

                }


            }

        }

        return finderResultList;

    }


    private List<FinderResult> walkthroughFinderModelList(String key, Long gameId) {

        List<FinderResult> finderResultList = new ArrayList<>();

        Example typeExample = new Example(GPType.class);
        typeExample.createCriteria().andEqualTo("gameid", gameId).andEqualTo("isitem", 1);
        List<GPType> typeList = typeMapper.selectByExample(typeExample);

        for (GPType type : typeList) {
            Example wtExample = new Example(GPWalkthrough.class);
            wtExample.createCriteria().andEqualTo("typeid", type.getId()).andEqualTo("istext", 0);
            List<GPWalkthrough> wtList = walkthroughMapper.selectByExample(wtExample);

            for (GPWalkthrough wt : wtList) {
                if (wt.getTitle().contains(key)) {
                    FinderResult finderResult = new FinderResult(wt.getLocid(), type.getTypename(), "title", wt.getTitle());
                    finderResultList.add(finderResult);
                }

                if (wt.getContent().contains(key)) {
                    FinderResult finderResult = new FinderResult(wt.getLocid(), type.getTypename(), "content", wt.getContent());
                    finderResultList.add(finderResult);
                }
            }
        }


        return finderResultList;
    }


    private int itemReplace(String key, String replace, Long gameId) {
        List<FinderResult> list = this.itemFinderModelList(key, gameId);


        if (list.size() < 1) {
            return 0;
        }
        int tag = 0;
        int temp = 0;

        for (FinderResult finder : list) {
            Example itemExample = new Example(GPItem.class);
            itemExample.createCriteria().andEqualTo("locid", finder.getLocId());
            GPItem item = itemMapper.selectOneByExample(itemExample);

            if (temp == item.getLocid().intValue()) {
                continue;
            }


            temp = item.getLocid().intValue();


            item.setName(item.getName().replace(key, replace));
            item.setDescription(item.getDescription().replace(key, replace));
            if (item.getAttribute1() != null) {
                item.setAttribute1(item.getAttribute1().replace(key, replace));
            }
            ;
            if (item.getAttribute2() != null) {
                item.setAttribute2(item.getAttribute2().replace(key, replace));
            }
            ;
            if (item.getAttribute3() != null) {
                item.setAttribute3(item.getAttribute3().replace(key, replace));
            }
            ;
            if (item.getAttribute4() != null) {
                item.setAttribute4(item.getAttribute4().replace(key, replace));
            }
            ;
            if (item.getAttribute5() != null) {
                item.setAttribute5(item.getAttribute5().replace(key, replace));
            }
            ;
            if (item.getAttribute6() != null) {
                item.setAttribute6(item.getAttribute6().replace(key, replace));
            }
            ;
            if (item.getAttribute7() != null) {
                item.setAttribute7(item.getAttribute7().replace(key, replace));
            }
            ;
            if (item.getAttribute8() != null) {
                item.setAttribute8(item.getAttribute8().replace(key, replace));
            }
            ;

            itemMapper.updateByPrimaryKey(item);

            tag++;
        }
        return tag;

    }

    private int walkthroughReplace(String key, String replace, Long gameId) {
        List<FinderResult> list = this.walkthroughFinderModelList(key, gameId);
        if (list.size() < 1) {
            return 0;
        }

        int tag = 0;
        int temp = 0;

        for (FinderResult finder : list) {
            Example wtExample = new Example(GPWalkthrough.class);
            wtExample.createCriteria().andEqualTo("locid", finder.getLocId());
            List<GPWalkthrough> wtList = walkthroughMapper.selectByExample(wtExample);
            GPWalkthrough item = wtList.get(0);

            if (temp == item.getLocid().intValue()) {
                continue;
            }
            temp = item.getLocid().intValue();


            item.setContent(item.getContent().replace(key, replace));
            item.setTitle(item.getTitle().replace(key, replace));


            tag++;


        }

        return tag;

    }

    public GPResult itemData(Long typeId, int pageNum, int pageSize) {

        Example typeExample = new Example(GPType.class);
        typeExample.createCriteria().andEqualTo("id", typeId).andEqualTo("isitem",0);
        GPType type = typeMapper.selectOneByExample(typeExample);

        if (type == null) {
            return GPResult.build(400, "未找到对应的typeId");
        }

        Example attributeExample = new Example(GPItemAttribute.class);
        attributeExample.createCriteria().andEqualTo("typeid",type.getId());
        List<GPItemAttribute> attributeList = attributeMapper.selectByExample(attributeExample);

        Example example = new Example(GPItem.class);
        example.createCriteria().andEqualTo("typeid", typeId);
        PageHelper.startPage(pageNum, pageSize);
        List<GPItem> list = itemMapper.selectByExample(example);


        List<Map> res = new ArrayList<>();
        for (GPItem item : list) {
            Map map = GPUtil.convertItem(item, attributeList);
            res.add(map);
        }

        return GPResult.ok(res);




    }

    public GPResult walkthroughData(Long typeId, int pageNum, int pageSize) {
        Example example = new Example(GPWalkthrough.class);
        example.createCriteria().andEqualTo("typeid", typeId).andEqualTo("isfirst", 0);
        PageHelper.startPage(pageNum, pageSize);
        List<GPWalkthrough> list = walkthroughMapper.selectByExample(example);

        return GPResult.ok(list);
    }

    public GPResult walkthroughDetail(Long locId) {
        Example example = new Example(GPWalkthrough.class);
        example.createCriteria().andEqualTo("locid",locId);
        List<GPWalkthrough> list = walkthroughMapper.selectByExample(example);

        return GPResult.ok(list);
    }


        public GPResult typeList(Long gameId, Long isItem) {


        Example example = new Example(GPType.class);
        if (isItem == 0) {
            example.createCriteria().andEqualTo("gameid", gameId).andEqualTo("isitem", 0);

        } else if (isItem == 1) {
            example.createCriteria().andEqualTo("gameid", gameId).andEqualTo("isitem", 1);

        } else {
            example.createCriteria().andEqualTo("gameid", gameId);
        }
        List<GPType> list = typeMapper.selectByExample(example);
        if (list.size() < 1 || list == null) {
            return GPResult.build(400, "找不到对应的type");
        }
        return GPResult.ok(list);
    }



    public GPResult typeAttribute(Long typeId) {


        Example example = new Example(GPItemAttribute.class);
        example.createCriteria().andEqualTo("typeid", typeId);
        List<GPItemAttribute> list = attributeMapper.selectByExample(example);
        if (list.size() < 1 || list == null) {
            return GPResult.build(400, "找不到对应的Attribute");
        }
        return GPResult.ok(list);
    }


    public GPResult gameList() {

        Example example = new Example(GPGame.class);
        example.createCriteria().andIsNotNull("id");
        List<GPGame> list = gameMapper.selectByExample(example);
        if (list.size() < 1 || list == null) {
            return GPResult.build(400, "找不到game");
        }
        return GPResult.ok(list);
    }

    public GPResult itemUpdate(Map<String, String> map, Long locId) {

        Example itemExample = new Example(GPItem.class);
        itemExample.createCriteria().andEqualTo("locid", locId);
        List<GPItem> itemList = itemMapper.selectByExample(itemExample);

        if (itemList.size() < 1 || itemList == null) {
            return GPResult.build(400, "找不到对应的Item");

        }

        Example example = new Example(GPType.class);
        example.createCriteria().andEqualTo("id", itemList.get(0).getTypeid());
        List<GPType> typeList = typeMapper.selectByExample(example);


        Example attributeExample = new Example(GPItemAttribute.class);
        attributeExample.createCriteria().andEqualTo("typeid", typeList.get(0).getId());
        List<GPItemAttribute> attributeList = attributeMapper.selectByExample(attributeExample);


        GPItem thisItem = itemList.get(0);
        if (map.containsKey("name")) {
            thisItem.setName(map.get("name"));
        }

        if (map.containsKey("description")) {
            thisItem.setDescription(map.get("description"));
        }


        for (GPItemAttribute attribute : attributeList) {
            for (Map.Entry<String, String> entry : map.entrySet()) {
                thisItem = this.setGPItem(thisItem, attribute, entry.getKey(), entry.getValue());
            }
        }

        int res = itemMapper.updateByPrimaryKeySelective(thisItem);

        return GPResult.ok(res);
    }


    private GPItem setGPItem(GPItem item, GPItemAttribute attribute, String key, String value) {

        if (!key.equals(attribute.getName())) {
            return item;
        }

        switch (attribute.getAttributeindex().intValue()) {
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
            default:
                break;
        }

        return item;
    }

    public GPResult walkthroughUpdateContent(Long id, String content) {
        Example example = new Example(GPWalkthrough.class);
        example.createCriteria().andEqualTo("id", id);
        List<GPWalkthrough> walkthroughList = walkthroughMapper.selectByExample(example);
        if (walkthroughList.size() < 1 || walkthroughList == null) {
            GPResult.build(400, "找不到对应id");
        }
        GPWalkthrough wt = walkthroughList.get(0);
        wt.setContent(content);
        return GPResult.ok(walkthroughMapper.updateByPrimaryKeySelective(wt));

    }

    public GPResult walkthroughUpdateTitle(Long locId, String title) {
        Example example = new Example(GPWalkthrough.class);
        example.createCriteria().andEqualTo("locid", locId);
        List<GPWalkthrough> walkthroughList = walkthroughMapper.selectByExample(example);
        if (walkthroughList.size() < 1 || walkthroughList == null) {
            GPResult.build(400, "找不到对应id");
        }

        int tag = 0;
        for (GPWalkthrough wt : walkthroughList) {

            wt.setTitle(title);
            tag = tag + walkthroughMapper.updateByPrimaryKeySelective(wt);
        }

        return GPResult.ok(tag);

    }

    public GPResult esSaveAll(Long gameId) {
        Example example = new Example(GPType.class);
        example.createCriteria().andEqualTo("gameid", gameId);
        List<GPType> typeList = typeMapper.selectByExample(example);

        List<Map> temp = new ArrayList<>();

        for (GPType type : typeList) {
            if (type.getIsitem().intValue() == 0) {
                temp.addAll(this.esSaveAllItem(type.getId()));
            } else {
                temp.addAll(this.esSaveAllWalkthrought(type.getId(), type.getSearchimage()));
            }
        }


        if (temp.size() < 1) {
            return GPResult.ok();
        } else {
            return GPResult.build(400, "同步数据错误", temp);
        }
    }

    private List<Map> esSaveAllItem(Long typeId) {
        Example example = new Example(GPItem.class);
        example.createCriteria().andEqualTo("typeid", typeId);
        List<GPItem> itemList = itemMapper.selectByExample(example);
        List<Map> temp = new ArrayList<>();

        for (GPItem item : itemList) {
            Boolean isSuccess = esDao.saveItem(item);
            if (!isSuccess) {
                Map map = new HashMap();
                map.put("locid", item.getLocid());
                map.put("error", "es保存失败");
                temp.add(map);
            }
        }
        return temp;

    }

    private List<Map> esSaveAllWalkthrought(Long typeId, String imageName) {
        Example example = new Example(GPWalkthrough.class);
        example.createCriteria().andEqualTo("typeid", typeId).andEqualTo("istext", 0).andEqualTo("isfirst", 0);
        List<GPWalkthrough> resList = walkthroughMapper.selectByExample(example);

        List<Map> temp = new ArrayList<>();
//        List<GPWalkthrough> resList = GPUtil.getWalkthroughOne(wtlist);

        for (GPWalkthrough wt : resList) {
            Boolean isSuccess = esDao.saveWalkthrough(wt, imageName);
            if (!isSuccess) {
                Map map = new HashMap();
                map.put("locid", wt.getLocid());
                map.put("error", "es保存失败");
                temp.add(map);
            }
        }
        return temp;

    }

    public GPResult walkthroughUploadImage(MultipartFile file, Long id) throws Exception {

        COSClientUtil util = new COSClientUtil();


        Example walkthroughExample = new Example(GPWalkthrough.class);
        walkthroughExample.createCriteria().andEqualTo("id", id);
        List<GPWalkthrough> walkthroughList = walkthroughMapper.selectByExample(walkthroughExample);

        if (walkthroughList.size() != 1 || walkthroughList == null) {
            return GPResult.build(400, "找不到对应的Walkthrough");
        }

        GPWalkthrough wt = walkthroughList.get(0);

        if (wt.getIstext() != 1) {
            return GPResult.build(400, "content不是image");

        }

        Example typeExample = new Example(GPType.class);
        typeExample.createCriteria().andEqualTo("id", wt.getTypeid());
        List<GPType> typeList = typeMapper.selectByExample(typeExample);


        Example gameExample = new Example(GPGame.class);
        gameExample.createCriteria().andEqualTo("id", typeList.get(0).getGameid());
        List<GPGame> gameList = gameMapper.selectByExample(gameExample);

        if (gameList.size() != 1 || gameList == null) {
            return GPResult.build(400, "gameId不正确");

        }


        String[] suffixList = wt.getContent().split("_");
        String suffix = suffixList[suffixList.length - 1];

        String name = util.uploadFile2Cos(file, gameList.get(0).getImagedoc() + "/" + walkthroughList.get(0).getLocid() + "_" + suffix);
        String imgUrl = util.getImgUrl(name);
        String[] split = imgUrl.split("\\?");

        util.destory();

        return GPResult.ok(split[0]);

    }

    public GPResult itemUploadImage(MultipartFile file, Long locId) throws Exception {

        COSClientUtil util = new COSClientUtil();


        Example itemExample = new Example(GPItem.class);
        itemExample.createCriteria().andEqualTo("locid", locId);
        List<GPItem> itemList = itemMapper.selectByExample(itemExample);

        if (itemList.size() != 1 || itemList == null) {
            return GPResult.build(400, "找不到对应的Item");

        }

        Example typeExample = new Example(GPType.class);
        typeExample.createCriteria().andEqualTo("id", itemList.get(0).getTypeid());
        List<GPType> typeList = typeMapper.selectByExample(typeExample);


        Example gameExample = new Example(GPGame.class);
        gameExample.createCriteria().andEqualTo("id", typeList.get(0).getGameid());
        List<GPGame> gameList = gameMapper.selectByExample(gameExample);

        if (gameList.size() != 1 || gameList == null) {
            return GPResult.build(400, "gameId不正确");

        }


        String name = util.uploadFile2Cos(file, gameList.get(0).getImagedoc() + "/" + itemList.get(0).getLocid() + ".jpg");
        String imgUrl = util.getImgUrl(name);
        String[] split = imgUrl.split("\\?");

        util.destory();

        return GPResult.ok(split[0]);

    }

    public GPResult deleteWT(Long id) {

        Example wtExample = new Example(GPWalkthrough.class);
        wtExample.createCriteria().andEqualTo("id", id);
        GPWalkthrough wt = walkthroughMapper.selectOneByExample(wtExample);
        if (wt == null) {
            return GPResult.build(400, "id不正确");
        }

        Example locExample = new Example(GPWalkthrough.class);
        locExample.createCriteria().andEqualTo("locid", wt.getLocid());
        locExample.orderBy("id").asc();
        List<GPWalkthrough> wtList = walkthroughMapper.selectByExample(locExample);

        int res = 0;

        if (wt.getIsfirst() == 0) {
            if (wtList.size() < 2) {
                res = walkthroughMapper.delete(wt);
            } else {
                GPWalkthrough upWT = wtList.get(1);
                upWT.setIsfirst(Long.valueOf(0));
                walkthroughMapper.updateByPrimaryKeySelective(upWT);

                res = walkthroughMapper.delete(wt);
            }
        } else {
            res = walkthroughMapper.delete(wt);
        }


        return GPResult.ok(res);

    }

    public GPResult deleteItem(Long locId) {
        Example itemExample = new Example(GPItem.class);
        itemExample.createCriteria().andEqualTo("locid", locId);
        List<GPItem> list = itemMapper.selectByExample(itemExample);
        if (list.size() != 1 || list == null) {
            return GPResult.build(400, "locId不正确");
        }

        int res = itemMapper.delete(list.get(0));
        return GPResult.ok(res);
    }

    public GPResult deleteWTByLocId(Long locId) {
        Example wtExample = new Example(GPWalkthrough.class);
        wtExample.createCriteria().andEqualTo("locid", locId);
        List<GPWalkthrough> list = walkthroughMapper.selectByExample(wtExample);
        if (list.size() < 1 || list == null) {
            return GPResult.build(400, "locId不正确");
        }

        int res = walkthroughMapper.deleteByExample(wtExample);

//        int res = 0;
//        for (GPWalkthrough wt:list){
//            res = res + walkthroughMapper.delete(wt);
//
//        }

        return GPResult.ok(res);

    }


    public GPResult addItem(Map<String, String> map, Long typeId, String name, String description, MultipartFile file) throws Exception {


        Example example = new Example(GPType.class);
        example.createCriteria().andEqualTo("id", typeId);
        List<GPType> typeList = typeMapper.selectByExample(example);

        if (typeList.size() < 1 || typeList == null) {
            return GPResult.build(400, "type不正确");
        }

        Example itemExample = new Example(GPItem.class);
        itemExample.createCriteria().andEqualTo("typeid", typeId);
        List<GPItem> itemList = itemMapper.selectByExample(itemExample);

        if (itemList.size() < 1 || itemList == null) {
            return GPResult.build(400, "获取不到item");
        }

        Long newLocId = GPUtil.getLocId(itemList);

        Example gameExample = new Example(GPGame.class);
        gameExample.createCriteria().andEqualTo("id", typeList.get(0).getGameid());
        List<GPGame> gameList = gameMapper.selectByExample(gameExample);

        Example attributeExample = new Example(GPItemAttribute.class);
        attributeExample.createCriteria().andEqualTo("typeid", typeList.get(0).getId());
        List<GPItemAttribute> attributeList = attributeMapper.selectByExample(attributeExample);


        GPItem newItem = new GPItem();
        newItem.setDescription(description);
        newItem.setName(name);
        newItem.setLocid(newLocId);
        newItem.setTypeid(typeId);
        String suffix = gameList.get(0).getImagedoc() + "/" + newLocId + ".jpg";

        newItem.setImage(GPUtil.imageUri + suffix);

        for (GPItemAttribute attribute : attributeList) {
            for (Map.Entry<String, String> entry : map.entrySet()) {
                newItem = this.setGPItem(newItem, attribute, entry.getKey(), entry.getValue());
            }
        }


        COSClientUtil util = new COSClientUtil();


        String imgName = util.uploadFile2Cos(file, suffix);
        String imgUrl = util.getImgUrl(name);
        String[] split = imgUrl.split("\\?");

        util.destory();

        int result = itemMapper.insertSelective(newItem);

        return GPResult.ok(result);

    }

    public GPResult addWalkthroughImage(Long locId, MultipartFile image) throws Exception {


        Example walkthroughExample = new Example(GPWalkthrough.class);
        walkthroughExample.createCriteria().andEqualTo("locid", locId);
        List<GPWalkthrough> walkthroughList = walkthroughMapper.selectByExample(walkthroughExample);


        if (walkthroughList.size() < 1) {
            return GPResult.build(400, "locId不存在");
        }

        Example typeExample = new Example(GPType.class);
        typeExample.createCriteria().andEqualTo("id", walkthroughList.get(0).getTypeid());
        List<GPType> typeList = typeMapper.selectByExample(typeExample);


        Example gameExample = new Example(GPGame.class);
        gameExample.createCriteria().andEqualTo("id", typeList.get(0).getGameid());
        List<GPGame> gameList = gameMapper.selectByExample(gameExample);

        if (gameList.size() != 1 || gameList == null) {
            return GPResult.build(400, "gameId不正确");

        }


        int tag = 1;
        for (GPWalkthrough wt : walkthroughList) {
            if (wt.getIstext() == 1) {
                tag++;
            }
        }

        String suffix = gameList.get(0).getImagedoc() + "/" + locId + "_" + tag + ".jpg";

        COSClientUtil util = new COSClientUtil();


        String imgName = util.uploadFile2Cos(image, suffix);
        String imgUrl = util.getImgUrl(imgName);
        String[] split = imgUrl.split("\\?");

        util.destory();

        GPWalkthrough wt = new GPWalkthrough();
        wt.setTitle(walkthroughList.get(0).getTitle());
        wt.setContent(GPUtil.imageUri + suffix);
        wt.setTypeid(walkthroughList.get(0).getTypeid());
        wt.setLocid(locId);
        wt.setIstext(Long.valueOf(1));
        wt.setIsfirst(Long.valueOf(1));


        int res = walkthroughMapper.insertSelective(wt);


        return GPResult.ok(res);

    }
//
//    public GPResult addWalkthroughText(Long locId,String content,String title){
//
//        Example walkthroughExample = new Example(GPWalkthrough.class);
//        walkthroughExample.createCriteria().andEqualTo("locid",locId);
//        List<GPWalkthrough> walkthroughList = walkthroughMapper.selectByExample(walkthroughExample);
//
//        if (walkthroughList.size() <1 || walkthroughList == null){
//
//
//        }else {
//            GPWalkthrough wt = new GPWalkthrough();
//            wt.setTitle(walkthroughList.get(0).getTitle());
//            wt.setContent(GPUtil.imageUri + suffix);
//            wt.setTypeid(walkthroughList.get(0).getTypeid());
//            wt.setLocid(locId);
//            wt.setIstext(Long.valueOf(0));
//        }
//    }

    public GPResult addNewWalkthroughtLoc(Long typeId, String content, String title) {
        Example typeExample = new Example(GPType.class);
        typeExample.createCriteria().andEqualTo("id", typeId);
        GPType currentType = typeMapper.selectOneByExample(typeExample);

        if (currentType == null) {
            return GPResult.build(400, "找不到对应type");
        }


        Example wtExample = new Example(GPWalkthrough.class);
        wtExample.createCriteria().andEqualTo("typeid", currentType.getId());
        List<GPWalkthrough> wtList = walkthroughMapper.selectByExample(wtExample);

        Long newLocId = GPUtil.getWTLocId(wtList);

        GPWalkthrough wt = new GPWalkthrough();
        wt.setTitle(title);
        wt.setContent(content);
        wt.setTypeid(currentType.getId());
        wt.setLocid(newLocId);
        wt.setIstext(Long.valueOf(0));
        wt.setIsfirst(Long.valueOf(0));


        return GPResult.ok(walkthroughMapper.insertSelective(wt));
    }

    public GPResult addWalkthroughText(Long locId, String content) {

        Example walkthroughExample = new Example(GPWalkthrough.class);
        walkthroughExample.createCriteria().andEqualTo("locid", locId);
        List<GPWalkthrough> walkthroughList = walkthroughMapper.selectByExample(walkthroughExample);

        if (walkthroughList.size() < 1 || walkthroughList == null) {
            return GPResult.build(400, "找不到对应locId");

        }

        GPWalkthrough wt = new GPWalkthrough();
        wt.setTitle(walkthroughList.get(0).getTitle());
        wt.setContent(content);
        wt.setTypeid(walkthroughList.get(0).getTypeid());
        wt.setLocid(locId);
        wt.setIstext(Long.valueOf(0));
        wt.setIsfirst(Long.valueOf(1));


        return GPResult.ok(walkthroughMapper.insertSelective(wt));
    }


    public GPResult updateType(Long typeId, String name, MultipartFile image) throws Exception {
        Example typeExample = new Example(GPType.class);
        typeExample.createCriteria().andEqualTo("id", typeId);
        List<GPType> typeList = typeMapper.selectByExample(typeExample);

        if (typeList.size() < 1 || typeList == null) {
            GPResult.build(400, "typeId不正确");
        }

        int res = 0;
        if (image != null) {
            String suffix = "type/" + typeId.toString() + ".jpg";

            COSClientUtil util = new COSClientUtil();


            String img = util.uploadFile2Cos(image, suffix);
            String imgUrl = util.getImgUrl(img);
            String[] split = imgUrl.split("\\?");

            util.destory();


        }

        if (name != null && !name.equals("")) {

            GPType type = typeList.get(0);
            type.setTypename(name);
            res = typeMapper.updateByPrimaryKeySelective(type);
        }


        return GPResult.ok(res);

    }

    public GPResult addType(Long gameId, String name, Long isItem, MultipartFile searchImage, MultipartFile image) throws Exception {

        Example gameExample = new Example(GPGame.class);
        gameExample.createCriteria().andEqualTo("id", gameId);
        GPGame game = gameMapper.selectOneByExample(gameExample);

        Example typeExample = new Example(GPType.class);
        typeExample.createCriteria().andEqualTo("gameid", gameId);
        List<GPType> typeList = typeMapper.selectByExample(typeExample);
        if (game == null) {
            GPResult.build(400, "typeId不正确");
        }
        for (GPType tp : typeList) {
            if (tp.getTypename().equals(name.replace(" ", ""))) {
                GPResult.build(400, "typeName重复");
            }
        }


        if (name == null || name.equals("")) {
            GPResult.build(400, "name为空");
        }
        if (isItem == null) {
            GPResult.build(400, "isItem为空");
        }
        if (image == null) {
            GPResult.build(400, "image未上传");
        }
        if (isItem != 0) {
            if (searchImage == null) {
                GPResult.build(400, "searchImage未上传");

            }
        }

        GPType type = new GPType();
        type.setGameid(gameId);
        type.setIsitem(isItem);
        type.setTypename(name);

        int v = typeMapper.insertSelective(type);
        if (v < 0) {
            GPResult.build(400, "创建失败");
        }
        type.setTypeprefix(type.getId());

        String imgSuffix = "type/" + type.getId().toString() + ".jpg";
        String searchSuffix = "";
        if (isItem != 0) {
            searchSuffix = "searchImage/" + type.getId() + ".jpg";

        }

        type.setImage(GPUtil.imageUri + imgSuffix);
        type.setSearchimage(GPUtil.imageUri + searchSuffix);

        COSClientUtil util = new COSClientUtil();

        String res = "";

        if (searchImage != null) {
            String simg = util.uploadFile2Cos(image, searchSuffix);
            String simgUrl = util.getImgUrl(simg);
            String[] ssplit = simgUrl.split("\\?");
            res = ssplit[0] + "///";

        }

        String img = util.uploadFile2Cos(image, imgSuffix);
        String imgUrl = util.getImgUrl(img);
        String[] split = imgUrl.split("\\?");
        res = res + split[0];

        util.destory();

        int result = typeMapper.updateByPrimaryKeySelective(type);


        return GPResult.ok(result);
    }

    public GPResult addTypeAttribute(Long typeId, String name, Long searchable) {
        Example example = new Example(GPItemAttribute.class);
        example.createCriteria().andEqualTo("typeid", typeId);
        List<GPItemAttribute> list = attributeMapper.selectByExample(example);

        if (list.size() > 7) {
            GPResult.build(400, "属性数量超出上限");
        }

        int temp = 0;

        for (GPItemAttribute attribute : list) {
            if (attribute.getAttributeindex() > temp) {
                temp = attribute.getAttributeindex().intValue();
            }
        }

        GPItemAttribute newAttribute = new GPItemAttribute();
        newAttribute.setAttributeindex(Long.valueOf(temp + 1));
        newAttribute.setName(name);
        newAttribute.setSearchable(searchable);

        int res = attributeMapper.insertSelective(newAttribute);
        return GPResult.ok(res);
    }


    public GPResult updateTypeAttribute(Long typeId, String name, Long attributeIndex) {

        Example example = new Example(GPItemAttribute.class);
        example.createCriteria().andEqualTo("typeid", typeId).andEqualTo("attributeindex", attributeIndex);
        GPItemAttribute attribute = attributeMapper.selectOneByExample(example);

        if (attribute == null) {
            GPResult.build(400, "未找到对应的属性");
        }
        attribute.setName(name);

        int res = attributeMapper.updateByPrimaryKeySelective(attribute);
        return GPResult.ok(res);
    }

}