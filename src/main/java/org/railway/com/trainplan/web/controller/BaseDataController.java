package org.railway.com.trainplan.web.controller;

import org.railway.com.trainplan.service.BaseDataService;
import org.railway.com.trainplan.web.dto.BureauDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by star on 5/13/14.
 */
@RestController
@RequestMapping(value = "/base")
public class BaseDataController {

    @Autowired
    private BaseDataService baseDataService;

    @RequestMapping(value = "bureau/all", method = RequestMethod.GET)
    public List<BureauDTO> getBureauList() {
        List<Map<String, Object>> list = baseDataService.getBureauList();
        List<BureauDTO> result = new ArrayList<BureauDTO>();
        for(Map<String, Object> map: list) {
            result.add(new BureauDTO(map));
        }
        return result;
    }
}
