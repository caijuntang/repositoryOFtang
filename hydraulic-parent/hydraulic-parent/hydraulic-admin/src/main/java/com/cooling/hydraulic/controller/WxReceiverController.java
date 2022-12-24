package com.cooling.hydraulic.controller;

import com.cooling.hydraulic.entity.WXUser;
import com.cooling.hydraulic.exception.BadRequestException;
import com.cooling.hydraulic.model.video.VideoDto;
import com.cooling.hydraulic.model.video.VideoQueryCriteria;
import com.cooling.hydraulic.model.wx.WxUserQueryCriteria;
import com.cooling.hydraulic.service.WXService;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.Set;

import static org.hibernate.id.IdentifierGenerator.ENTITY_NAME;

@Controller
@RequestMapping("/api/wxReceiver")
public class WxReceiverController {

    @Resource
    private WXService wxService;

    @GetMapping
//    @PreAuthorize("@el.check('wxUser:list')")
    public ResponseEntity<Object> addWxuser(WxUserQueryCriteria criteria, Pageable pageable) throws Exception {
        return new ResponseEntity<>(wxService.queryAll(criteria, pageable), HttpStatus.OK);
    }

    @GetMapping("syncWxuser")
//    @PreAuthorize("@el.check('wxUser:add')")
    public ResponseEntity<Object> syncWxuser(){
        wxService.syncWxuser();
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @PutMapping
//    @PreAuthorize("@el.check('wxUser:edit')")
    public ResponseEntity<Object> updateWxuser(@RequestBody WXUser resources){
        wxService.update(resources);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @DeleteMapping
//    @PreAuthorize("@el.check('wxUser:del')")
    public ResponseEntity<Object> deleteVideo(@RequestBody Set<Integer> ids){
        wxService.delete(ids);
        return new ResponseEntity<>(HttpStatus.OK);
    }

}
