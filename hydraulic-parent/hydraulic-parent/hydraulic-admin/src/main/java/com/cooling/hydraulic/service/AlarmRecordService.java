package com.cooling.hydraulic.service;

import com.cooling.hydraulic.dao.AlarmRecordRepository;
import com.cooling.hydraulic.entity.AlarmRecord;
import com.cooling.hydraulic.exception.BadRequestException;
import com.cooling.hydraulic.exception.EntityNotFoundException;
import com.cooling.hydraulic.model.alarm.AlarmDto;
import com.cooling.hydraulic.model.alarm.AlarmRecordDto;
import com.cooling.hydraulic.model.alarm.AlarmRecordQueryCriteria;
import com.cooling.hydraulic.utils.DateTimeUtil;
import com.cooling.hydraulic.utils.DateUtil;
import com.cooling.hydraulic.utils.PageUtil;
import com.cooling.hydraulic.utils.QueryHelp;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class AlarmRecordService {

    @Resource
    private AlarmRecordRepository alarmRecordRepository;

    public Object queryAll(AlarmRecordQueryCriteria criteria, Pageable pageable) {
        Page<AlarmRecord> page = alarmRecordRepository.findAll((root, criteriaQuery, criteriaBuilder) -> QueryHelp.getPredicate(root, criteria, criteriaBuilder), pageable);
        return PageUtil.toPage(page);
    }

    @Transactional
    public void updateStatus(AlarmRecordDto resources) {
        Integer id = resources.getId();
        if(null==id){
            throw new BadRequestException("id缺失");
        }
        AlarmRecord record = alarmRecordRepository.getOne(id);
        if(null==record){
            throw new EntityNotFoundException(AlarmRecord.class,"id",id.toString());
        }
        record.setFixTime(DateTimeUtil.getNowDateTime());
        record.setStatus(0);
        record.setRemark(resources.getRemark());
        alarmRecordRepository.save(record);
    }

    @Transactional
    public void create(AlarmRecord resources) {
        resources.setCreateTime(DateTimeUtil.getNowDateTime());
        resources.setStatus(1);
        resources.setRemark(resources.getRemark());
        alarmRecordRepository.save(resources);
    }


    public List<AlarmRecord> getRecordList(Integer stationId) {
        LocalDateTime nowDateTime = DateTimeUtil.getNowDateTime();
        LocalDateTime recentDay = nowDateTime.minusDays(2);
        String recently = DateUtil.localDateTimeFormatyMdHms(recentDay);
        return alarmRecordRepository.findRecentByAndStation(stationId,recentDay);
    }
}
