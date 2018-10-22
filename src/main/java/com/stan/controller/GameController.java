package com.stan.controller;

import com.stan.model.GPResult;
import com.stan.model.pojo.GPItem;
import com.stan.service.GameService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;

@RequestMapping(value = "/wx")
@Controller
public class GameController {

    @Autowired
    private GameService service;


    @RequestMapping(value = "/search", method = RequestMethod.POST)
    @ResponseBody
    public GPResult query(HttpServletRequest request, String query) {
        return GPResult.ok(service.findAll(query));
    }


    @RequestMapping(value = "/game/list", method = RequestMethod.POST)
    @ResponseBody
    public GPResult gameList(HttpServletRequest request) {

        return GPResult.ok(service.getGameList());
    }

    @RequestMapping(value = "/walkthrough/detail", method = RequestMethod.POST)
    @ResponseBody
    public GPResult walkthrough(HttpServletRequest request, Long locId) {

        return GPResult.ok(service.getWalkthroughByLocId(locId));
    }

    @RequestMapping(value = "/item/detail", method = RequestMethod.POST)
    @ResponseBody
    public GPResult item(HttpServletRequest request, Long locId) {

        return  service.getItemByTypeId(locId);

    }
    @RequestMapping(value = "/type/list", method = RequestMethod.POST)
    @ResponseBody
    public GPResult gameCategory(HttpServletRequest request, Long gameId) {



        return GPResult.ok(service.getGameCategory(gameId));
    }

    @RequestMapping(value = "/content/list", method = RequestMethod.POST)
    @ResponseBody
    public GPResult contentList(HttpServletRequest request, Long typeId) {


        return service.getContentList(typeId);
    }

}
