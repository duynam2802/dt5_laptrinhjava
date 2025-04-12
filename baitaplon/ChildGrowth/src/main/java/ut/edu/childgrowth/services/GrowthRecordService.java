package ut.edu.childgrowth.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ut.edu.childgrowth.models.GrowthRecord;
import ut.edu.childgrowth.repositories.GrowthRecordRepository;

@Service
public class GrowthRecordService {

    @Autowired
    private GrowthRecordRepository growthRecordRepository;

    public GrowthRecord saveGrowthRecord(GrowthRecord growthRecord) {
        return growthRecordRepository.save(growthRecord);
    }

    public void save(GrowthRecord newRecord) {
        growthRecordRepository.save(newRecord);
    }
}
