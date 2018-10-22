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

    @RequestMapping(value = "/search/replace", method = RequestMethod.POST)
    @ResponseBody
    public GPResult query(HttpServletRequest request, String key, String replace,Long gameId,Long isItem) {
        int tag = service.replaceKey(key,replace,gameId,(isItem == 0));
        return GPResult.ok(tag);
    }

    @RequestMapping(value = "/search/finder", method = RequestMethod.POST)
    @ResponseBody
    public GPResult save(HttpServletRequest request, String key,Long gameId,Long isItem) {

        return GPResult.ok(service.finder(key,gameId,(isItem == 0)));
    }

    @RequestMapping(value = "/type/list", method = RequestMethod.POST)
    @ResponseBody
    public GPResult typeList(HttpServletRequest request, Long gameId,Long isItem) {

        return service.typeList(gameId,isItem);
    }

    @RequestMapping(value = "/typeattribute/list", method = RequestMethod.POST)
    @ResponseBody
    public GPResult typeAttribute(HttpServletRequest request,Long typeId){

        return service.typeAttribute(typeId);
    }

    @RequestMapping(value = "/game/list", method = RequestMethod.POST)
    @ResponseBody
    public GPResult gameList(HttpServletRequest request) {

        return service.gameList();
    }

    @RequestMapping(value = "/item/list", method = RequestMethod.POST)
    @ResponseBody
    public GPResult itemList(HttpServletRequest request, Long typeId,int pageNum,int pageSize) {

        return service.itemData(typeId,pageNum,pageSize);
    }

    @RequestMapping(value = "/walkthrough/list", method = RequestMethod.POST)
    @ResponseBody
    public GPResult walkthroughList(HttpServletRequest request, Long typeId,int pageNum,int pageSize) {

        return service.walkthroughData(typeId,pageNum,pageSize);
    }

    @RequestMapping(value = "/walkthrough/detail", method = RequestMethod.POST)
    @ResponseBody
    public GPResult walkthroughDetail(HttpServletRequest request, Long locId) {

        return service.walkthroughDetail(locId);
    }


    @RequestMapping(value = "/item/update", method = RequestMethod.POST)
    @ResponseBody
    public GPResult itemUpdate(HttpServletRequest request,Long locId) {

        Map paramMap = GPUtil.getRequestParamMap(request);
        return service.itemUpdate(paramMap,locId);
    }

    /**
     * 更新攻略的标题
     * @param request
     * @param lodId
     * @param text
     * @return
     */
    @RequestMapping(value = "/walkthrough/update/title", method = RequestMethod.POST)
    @ResponseBody
    public GPResult updateWalkthroughTitle(HttpServletRequest request,Long lodId,String text) {
        return service.walkthroughUpdateTitle(lodId,text);


    }

    /**
     * 更新攻略的内容（文字）
     * @param request
     * @param id
     * @param text
     * @return
     */
    @RequestMapping(value = "/walkthrough/update/content", method = RequestMethod.POST)
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
    @RequestMapping(value = "/search/sync", method = RequestMethod.POST)
    @ResponseBody
    public GPResult synchron(HttpServletRequest request,Long gameId) {

        return service.esSaveAll(gameId);
    }

    @RequestMapping(value = "/item/imageupload", method = RequestMethod.POST)
    @ResponseBody
    public GPResult uploadItemImage(HttpServletRequest request, @RequestParam("image") MultipartFile image, Long locId) throws Exception {

        return service.itemUploadImage(image,locId);
    }

    @RequestMapping(value = "/walkthrough/imageupload", method = RequestMethod.POST)
    @ResponseBody
    public GPResult uploadWalkthrough(HttpServletRequest request, @RequestParam("image") MultipartFile image, Long id) throws Exception {

        return service.walkthroughUploadImage(image,id);
    }

    @RequestMapping(value = "/item/delete", method = RequestMethod.POST)
    @ResponseBody
    public GPResult deleteItem(HttpServletRequest request,  Long locId) throws Exception {

        return service.deleteItem(locId);
    }
    @RequestMapping(value = "/walkthrough/delete/byId", method = RequestMethod.POST)
    @ResponseBody
    public GPResult deleteWT(HttpServletRequest request,  Long id) throws Exception {

        return service.deleteWT(id);
    }
    @RequestMapping(value = "/walkthrough/delete/byLocId", method = RequestMethod.POST)
    @ResponseBody
    public GPResult deleteWTByType(HttpServletRequest request,  Long locId) throws Exception {

        return service.deleteWTByLocId(locId);
    }


    @RequestMapping(value = "/item/add", method = RequestMethod.POST)
    @ResponseBody
    public GPResult addItem(HttpServletRequest request,Long typeId,String name,String description,@RequestParam("image") MultipartFile image) throws Exception {

        Map paramMap = GPUtil.getRequestParamMap(request);
        return service.addItem(paramMap,typeId,name,description,image);
    }

    @RequestMapping(value = "/walkthrough/add/image", method = RequestMethod.POST)
    @ResponseBody
    public GPResult addWalkthroughImage(HttpServletRequest request,Long locId,@RequestParam("image") MultipartFile image) throws Exception {

        return service.addWalkthroughImage(locId,image);
    }

    @RequestMapping(value = "/walkthrough/add/newLoc", method = RequestMethod.POST)
    @ResponseBody
    public GPResult addWalkthroughContent(HttpServletRequest request,Long typeId,String content,String title) {

        return service.addNewWalkthroughtLoc(typeId,content,title);
    }

    /**
     *
     * @param request
     * @param locId
     * @param content
     * @return
     */
    @RequestMapping(value = "/walkthrough/add/content", method = RequestMethod.POST)
    @ResponseBody
    public GPResult addWalkthroughNew(HttpServletRequest request,Long locId,String content) {

        return service.addWalkthroughText(locId,content);
    }


    @RequestMapping(value = "/type/update", method = RequestMethod.POST)
    @ResponseBody
    public GPResult typeUpdate(HttpServletRequest request,String name,Long typeId,@RequestParam("image") MultipartFile image) throws Exception {

        return service.updateType(typeId,name,image);
    }


    @RequestMapping(value = "/type/add", method = RequestMethod.POST)
    @ResponseBody
    public GPResult typeUpdate(HttpServletRequest request,Long gameId,Long isItem,String name,@RequestParam(value = "searchImage",required = false) MultipartFile searchImage,@RequestParam("image") MultipartFile image) throws Exception {
        return service.addType(gameId,name,isItem,searchImage,image);

    }

    @RequestMapping(value = "/typeattribute/add", method = RequestMethod.POST)
    @ResponseBody
    public GPResult typeAttributeAdd(HttpServletRequest request,Long typeId,String name,Long searchable){

        return service.addTypeAttribute(typeId,name,searchable);
    }

    @RequestMapping(value = "/typeattribute/update", method = RequestMethod.POST)
    @ResponseBody
    public GPResult typeAttributeUpdate(HttpServletRequest request,Long typeId,String name,Long attributeIndex){

        return service.updateTypeAttribute(typeId,name,attributeIndex);
    }

}
