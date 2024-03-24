package dev.projectfitness.ipfitness.controllers;

import dev.projectfitness.ipfitness.models.dtos.Activity;
import dev.projectfitness.ipfitness.models.dtos.ChartData;
import dev.projectfitness.ipfitness.models.requests.ActivityAddRequest;
import dev.projectfitness.ipfitness.services.ActivityService;
import dev.projectfitness.ipfitness.util.Utility;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("${ipfitness.base-url}")
@RequiredArgsConstructor
public class UserActivityController {
    private final ActivityService activityService;

    @GetMapping("/users/{userId}/activities")
    public List<Activity> getAllUserActivities(@PathVariable("userId") Integer userId,
                                               @RequestParam(name = "month", required = true) Integer month,
                                               @RequestParam(name = "year", required = true) Integer year, Authentication auth) {
        Utility.authorizeFitnessUser(auth, userId);
        return activityService.getUserActivitiesForPeriod(userId, month, year);
    }

    @PostMapping("/activities")
    public Activity addActivity(@RequestBody @Valid ActivityAddRequest request, Authentication auth) {
        Utility.authorizeFitnessUser(auth, request.getUserId());
        return activityService.addNewActivity(request);
    }

    @GetMapping("/users/{userId}/activities/monthly-data")
    public List<ChartData> getMonthlyUserActivityData(@PathVariable("userId") Integer userId,
                                                      @RequestParam(name = "month", required = true) Integer month,
                                                      @RequestParam(name = "year", required = true) Integer year, Authentication auth) {
        Utility.authorizeFitnessUser(auth, userId);
        return activityService.getMonthlyUserActivityData(userId, month, year);
    }

    @GetMapping("/users/{userId}/activities/download")
    public ResponseEntity<Resource> downloadUserActivitiesForPeriod(@PathVariable("userId") Integer userId,
                                                                    @RequestParam(name = "month", required = true) Integer month,
                                                                    @RequestParam(name = "year", required = true) Integer year, Authentication auth){
        Utility.authorizeFitnessUser(auth, userId);
        final Resource resource = activityService.getUserActivitiesAsPdfResource(userId, month, year);
        return ResponseEntity
                .ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + resource.getFilename() + "\"")
                .header(HttpHeaders.ACCESS_CONTROL_EXPOSE_HEADERS, "Content-Disposition")
                .contentType(MediaType.APPLICATION_PDF)
                .body(resource);
    }
}
