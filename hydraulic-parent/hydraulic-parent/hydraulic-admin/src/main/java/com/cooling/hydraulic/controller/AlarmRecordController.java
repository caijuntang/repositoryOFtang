package com.cooling.hydraulic.controller;


import com.cooling.hydraulic.entity.AlarmRecord;
import com.cooling.hydraulic.model.alarm.AlarmDto;
import com.cooling.hydraulic.model.alarm.AlarmQueryCriteria;
import com.cooling.hydraulic.model.alarm.AlarmRecordDto;
import com.cooling.hydraulic.model.alarm.AlarmRecordQueryCriteria;
import com.cooling.hydraulic.service.AlarmRecordService;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

@Controller
@RequestMapping("/api/alarmRecord")
public class AlarmRecordController {
    @Resource
    private AlarmRecordService alarmRecordService;

    @GetMapping
//    @PreAuthorize("@el.check('alarmRecord:list')")
    public ResponseEntity<Object> queryAlarm(AlarmRecordQueryCriteria criteria, Pageable pageable) {
        return new ResponseEntity<>(alarmRecordService.queryAll(criteria, pageable), HttpStatus.OK);
    }


    @PutMapping
//    @PreAuthorize("@el.check('alarmRecord:edit')")
    public ResponseEntity<Object> updateAlarm(@RequestBody AlarmRecordDto resources){
        alarmRecordService.updateStatus(resources);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
