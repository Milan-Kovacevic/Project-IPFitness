package dev.projectfitness.ipfitness.services;

import dev.projectfitness.ipfitness.models.dtos.Activity;
import dev.projectfitness.ipfitness.models.dtos.ChartData;
import dev.projectfitness.ipfitness.models.requests.ActivityAddRequest;
import org.springframework.core.io.Resource;

import java.util.List;

public interface ActivityService {
    List<Activity> getUserActivitiesForPeriod(Integer userId, Integer month, Integer year);

    Activity addNewActivity(ActivityAddRequest request);

    List<ChartData> getMonthlyUserActivityData(Integer userId, Integer month, Integer year);

    Resource getUserActivitiesAsPdfResource(Integer userId, Integer month, Integer year);
}
