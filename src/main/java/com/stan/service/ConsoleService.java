package com.stan.service;

import com.github.abel533.entity.Example;
import com.stan.mapper.GPItemAttributeMapper;
import com.stan.mapper.GPItemMapper;
import com.stan.mapper.GPTypeMapper;
import com.stan.model.pojo.GPItem;
import com.stan.model.pojo.GPItemAttribute;
import com.stan.model.pojo.GPType;
import com.stan.model.vo.FinderResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class ConsoleService {

    @Autowired
    private GPItemMapper itemMapper;

    @Autowired
    private GPTypeMapper typeMapper;

    @Autowired
    private GPItemAttributeMapper attributeMapper;

    public List<FinderResult> finderModelList(String key,Long gameId){

        List<FinderResult> finderResultList = new ArrayList<>();

        Example typeExample = new Example(GPType.class);
        typeExample.createCriteria().andEqualTo("gameid",gameId);
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


}
