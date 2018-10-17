package com.stan.controller;

import com.stan.model.GPResult;
import com.stan.service.ConsoleService;
import com.stan.utils.GPUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

@RequestMapping(value = "/console")
@Controller
public class ConsoleController {

    @Autowired
    private ConsoleService service;

    @RequestMapping(value = "/replace", method = RequestMethod.POST)
    @ResponseBody
    public GPResult query(HttpServletRequest request, String key, String replace,Long gameId,Long isItem) {
        int tag = service.replaceKey(key,replace,gameId,(isItem == 0));
        return GPResult.ok(tag);
    }

    @RequestMapping(value = "/finder", method = RequestMethod.POST)
    @ResponseBody
    public GPResult save(HttpServletRequest request, String key,Long gameId,Long isItem) {

        return GPResult.ok(service.finder(key,gameId,(isItem == 0)));
    }

    @RequestMapping(value = "/typeList", method = RequestMethod.POST)
    @ResponseBody
    public GPResult typeList(HttpServletRequest request, Long gameId) {

        return GPResult.ok(service.typeList(gameId));
    }

    @RequestMapping(value = "/typeAttribute", method = RequestMethod.POST)
    @ResponseBody
    public GPResult typeAttribute(HttpServletRequest request,Long typeId){

        return GPResult.ok(service.typeAttribute(typeId));
    }

    @RequestMapping(value = "/gameList", method = RequestMethod.POST)
    @ResponseBody
    public GPResult gameList(HttpServletRequest request) {

        return GPResult.ok(service.gameList());
    }

    @RequestMapping(value = "/contentList", method = RequestMethod.POST)
    @ResponseBody
    public GPResult contentList(HttpServletRequest request, Long typeId,int pageNum,int pageSize) {

        return GPResult.ok(service.retrieveData(typeId,pageNum,pageSize));
    }


    @RequestMapping(value = "/itemUpdate", method = RequestMethod.POST)
    @ResponseBody
    public GPResult itemUpdate(HttpServletRequest request,Long locId) {

        Map paramMap = GPUtil.getRequestParamMap(request);
        return service.itemUpdate(paramMap,locId);
    }

    /**
     * 更新攻略的标题
     * @param request
     * @param typeId
     * @param text
     * @return
     */
    @RequestMapping(value = "/updateWalkthroughTitle", method = RequestMethod.POST)
    @ResponseBody
    public GPResult updateWalkthroughTitle(HttpServletRequest request,Long typeId,String text) {
        return service.walkthroughUpdateTitle(typeId,text);


    }

    /**
     * 更新攻略的内容（文字）
     * @param request
     * @param id
     * @param text
     * @return
     */
    @RequestMapping(value = "/updateWalkthroughContent", method = RequestMethod.POST)
    @ResponseBody
    public GPResult updateWalkthroughContent(HttpServletRequest request,Long id,String text) {
        return service.walkthroughUpdateContent(id,text);


    }

    /**
     * 同步到搜索服务器
     * @param request
     * @param gameId
     * @return
     */
    @RequestMapping(value = "/sync", method = RequestMethod.POST)
    @ResponseBody
    public GPResult synchron(HttpServletRequest request,Long gameId) {

        return service.esSaveAll(gameId);
    }

    @RequestMapping(value = "/upload", method = RequestMethod.POST)
    @ResponseBody
    public GPResult upload(HttpServletRequest request, @RequestParam("image") MultipartFile image, Long locId,Long id,Long gameId) throws Exception {

        return service.uploadImage(image,id,gameId);
    }

    @RequestMapping(value = "/delete", method = RequestMethod.POST)
    @ResponseBody
    public GPResult delete(HttpServletRequest request,  Long id,Long isItem) throws Exception {

        return service.delete(id,isItem);
    }


    @RequestMapping(value = "/addItem", method = RequestMethod.POST)
    @ResponseBody
    public GPResult addItem(HttpServletRequest request,Long typeId,String name,String description,@RequestParam("image") MultipartFile image) throws Exception {

        Map paramMap = GPUtil.getRequestParamMap(request);
        return service.addItem(paramMap,typeId,name,description,image);
    }

    @RequestMapping(value = "/addWalkthroughImage", method = RequestMethod.POST)
    @ResponseBody
    public GPResult addWalkthroughImage(HttpServletRequest request,Long locId,@RequestParam("image") MultipartFile image) throws Exception {

        return service.addWalkthroughImage(locId,image);
    }

    @RequestMapping(value = "/addWalkthroughText", method = RequestMethod.POST)
    @ResponseBody
    public GPResult addWalkthroughText(HttpServletRequest request,Long locId,String content) {

        return service.addWalkthroughText(locId,content);
    }

}
