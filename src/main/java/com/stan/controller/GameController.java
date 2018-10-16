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

@RequestMapping(value = "/game")
@Controller
public class GameController {

    @Autowired
    private GameService service;


    @RequestMapping(value = "/search", method = RequestMethod.POST)
    @ResponseBody
    public GPResult query(HttpServletRequest request, String query) {
        return GPResult.ok(service.findAll(query));
    }


    @RequestMapping(value = "/gameList", method = RequestMethod.POST)
    @ResponseBody
    public GPResult gameList(HttpServletRequest request) {

        return GPResult.ok(service.getGameList());
    }

    @RequestMapping(value = "/walkthroughDetail", method = RequestMethod.POST)
    @ResponseBody
    public GPResult walkthrough(HttpServletRequest request, String locId) {

        return GPResult.ok(service.getWalkthroughByLocId(locId));
    }

    @RequestMapping(value = "/itemDetail", method = RequestMethod.POST)
    @ResponseBody
    public GPResult item(HttpServletRequest request, String locId) {

        return  service.getItemByTypeId(locId);

    }
    @RequestMapping(value = "/gameCategory", method = RequestMethod.POST)
    @ResponseBody
    public GPResult gameCategory(HttpServletRequest request, String gameId) {



        return GPResult.ok(service.getGameCategory(Long.valueOf(gameId)));
    }

    @RequestMapping(value = "/contentList", method = RequestMethod.POST)
    @ResponseBody
    public GPResult contentList(HttpServletRequest request, String categoryId) {


        return GPResult.ok(service.getContentList(categoryId));
    }

}
