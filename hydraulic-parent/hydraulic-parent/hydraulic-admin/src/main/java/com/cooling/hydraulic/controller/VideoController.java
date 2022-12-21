package com.cooling.hydraulic.controller;

import com.cooling.hydraulic.entity.Station;
import com.cooling.hydraulic.entity.VideoChannel;
import com.cooling.hydraulic.exception.BadRequestException;
import com.cooling.hydraulic.model.alarm.AlarmQueryCriteria;
import com.cooling.hydraulic.model.video.VideoDto;
import com.cooling.hydraulic.model.video.VideoQueryCriteria;
import com.cooling.hydraulic.service.VideoChannelService;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;
import java.util.Set;

import static org.hibernate.id.IdentifierGenerator.ENTITY_NAME;

@Controller
@RequestMapping(path="/api/video")
public class VideoController {

    @Resource
    private VideoChannelService videoChannelService;

    @RequestMapping("/getVideoMap")
    @ResponseBody
    public Object getVideoMap(Integer stationId) {
        return videoChannelService.findVideoByStationId(stationId);
    }

    @GetMapping
//    @PreAuthorize("@el.check('video:list')")
    public ResponseEntity<Object> queryMenu(VideoQueryCriteria criteria, Pageable pageable) throws Exception {
        return new ResponseEntity<>(videoChannelService.queryAll(criteria, pageable), HttpStatus.OK);
    }

    @PostMapping
//    @PreAuthorize("@el.check('video:add')")
    public ResponseEntity<Object> createVideo( @RequestBody VideoDto resources){
        if (resources.getId() != null) {
            throw new BadRequestException("A new "+ ENTITY_NAME +" cannot already have an ID");
        }
        videoChannelService.create(resources);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @PutMapping
//    @PreAuthorize("@el.check('video:edit')")
    public ResponseEntity<Object> updateVideo(@RequestBody VideoDto resources){
        videoChannelService.update(resources);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @DeleteMapping
//    @PreAuthorize("@el.check('video:del')")
    public ResponseEntity<Object> deleteVideo(@RequestBody Set<Integer> ids){
        videoChannelService.delete(ids);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
