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
     *
     * @param request
     * @param isTitle 如果是title id传typeId 如果是content id传id
     * @param id
     * @param text
     * @return
     */
    @RequestMapping(value = "/walkthroughUpdate", method = RequestMethod.POST)
    @ResponseBody
    public GPResult walkthroughUpdate(HttpServletRequest request,Long isTitle,Long id,String text) {

        if (isTitle == Long.valueOf(0)){
            return service.walkthroughUpdateTitle(id,text);
        }else {
            return service.walkthroughUpdateContent(id,text);
        }

    }

    @RequestMapping(value = "/sync", method = RequestMethod.POST)
    @ResponseBody
    public GPResult synchron(HttpServletRequest request,Long gameId) {

        return service.esSaveAll(gameId);
    }

    @RequestMapping(value = "/upload", method = RequestMethod.POST)
    @ResponseBody
    public GPResult upload(HttpServletRequest request, @RequestParam("file") MultipartFile file, String typeId) throws Exception {

        return service.uploadImageWithTypeId(file,typeId);
    }

    @RequestMapping(value = "/delete", method = RequestMethod.POST)
    @ResponseBody
    public GPResult delete(HttpServletRequest request,  String typeId) throws Exception {

        return service.delete(typeId);
    }

    @RequestMapping(value = "/addItem", method = RequestMethod.POST)
    @ResponseBody
    public GPResult addItem(HttpServletRequest request,  ConsoleUpdater updater, String categoryId) throws Exception {

        return service.addItem(updater,categoryId);
    }


}
