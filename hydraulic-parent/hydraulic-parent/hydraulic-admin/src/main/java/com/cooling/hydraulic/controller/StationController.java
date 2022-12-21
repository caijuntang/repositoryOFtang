package com.cooling.hydraulic.controller;


import com.cooling.hydraulic.entity.Menu;
import com.cooling.hydraulic.entity.Station;
import com.cooling.hydraulic.exception.BadRequestException;
import com.cooling.hydraulic.model.menu.MenuDto;
import com.cooling.hydraulic.model.menu.MenuQueryCriteria;
import com.cooling.hydraulic.model.station.StationDto;
import com.cooling.hydraulic.model.station.StationQueryCriteria;
import com.cooling.hydraulic.service.StationService;
import com.cooling.hydraulic.utils.PageUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.*;
import java.util.stream.Collectors;

import static org.hibernate.id.IdentifierGenerator.ENTITY_NAME;

@Controller
@RequestMapping("/api/station")
public class StationController {

    @Resource
    private StationService stationService;

    @GetMapping
//    @PreAuthorize("@el.check('station:list')")
    public ResponseEntity<Object> queryMenu(StationQueryCriteria criteria, Pageable pageable) throws Exception {
        return new ResponseEntity<>(stationService.queryAll(criteria, pageable), HttpStatus.OK);
    }

    @PostMapping
//    @PreAuthorize("@el.check('station:add')")
    public ResponseEntity<Object> createStation(@Validated @RequestBody Station resources){
        if (resources.getId() != null) {
            throw new BadRequestException("A new "+ ENTITY_NAME +" cannot already have an ID");
        }
        stationService.create(resources);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @PutMapping
//    @PreAuthorize("@el.check('station:edit')")
    public ResponseEntity<Object> updateStation(@RequestBody Station resources){
        stationService.update(resources);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @DeleteMapping
//    @PreAuthorize("@el.check('station:del')")
    public ResponseEntity<Object> deleteStation(@RequestBody Set<Integer> ids){
        stationService.delete(ids);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @RequestMapping("/getStationData")
    @ResponseBody
    public Object getStationData() {
        Station defaultStation = stationService.getDefaultStation();
        List<Station> allStation = stationService.findAllStation();
        Map<String, Object> result = new HashMap<>();
        result.put("default",defaultStation);
        result.put("all",allStation);
        return result;
    }
}
