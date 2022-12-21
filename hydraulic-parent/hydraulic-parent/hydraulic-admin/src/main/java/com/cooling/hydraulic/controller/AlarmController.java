package com.cooling.hydraulic.controller;

import com.cooling.hydraulic.entity.AlarmConfig;
import com.cooling.hydraulic.entity.Station;
import com.cooling.hydraulic.exception.BadRequestException;
import com.cooling.hydraulic.model.alarm.AlarmDto;
import com.cooling.hydraulic.model.alarm.AlarmQueryCriteria;
import com.cooling.hydraulic.model.station.StationQueryCriteria;
import com.cooling.hydraulic.service.AlarmService;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.Set;

import static org.hibernate.id.IdentifierGenerator.ENTITY_NAME;

@Controller
@RequestMapping("/api/alarm")
public class AlarmController {

    @Resource
    private AlarmService alarmService;

    @GetMapping
//    @PreAuthorize("@el.check('alarm:list')")
    public ResponseEntity<Object> queryAlarm(AlarmQueryCriteria criteria, Pageable pageable) throws Exception {
        return new ResponseEntity<>(alarmService.queryAll(criteria, pageable), HttpStatus.OK);
    }

    @PostMapping
//    @PreAuthorize("@el.check('alarm:add')")
    public ResponseEntity<Object> createAlarm( @RequestBody AlarmDto resources){
        if (resources.getId() != null) {
            throw new BadRequestException("A new "+ ENTITY_NAME +" cannot already have an ID");
        }
        alarmService.create(resources);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @PutMapping
//    @PreAuthorize("@el.check('alarm:edit')")
    public ResponseEntity<Object> updateAlarm(@RequestBody AlarmDto resources){
        alarmService.update(resources);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @DeleteMapping
//    @PreAuthorize("@el.check('alarm:del')")
    public ResponseEntity<Object> deleteMenu(@RequestBody Set<Integer> ids){
        alarmService.delete(ids);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @RequestMapping("/getAlarmConfig")
    @ResponseBody
    public Object getAlarmConfig(Integer stationId) {
        return alarmService.getAlarmConfig(stationId);
    }

    @RequestMapping("/alarmConfigSave")
    @ResponseBody
    public Object alarmConfigSave(AlarmDto form) {
        return alarmService.saveAlarmConfig(form);
    }

    @RequestMapping("/updateAlarmStatus")
    @ResponseBody
    public Object updateAlarmStatus(Integer id) {
        if(null==id){
            return false;
        }
        return alarmService.updateAlarmStatus(id);
    }
}
