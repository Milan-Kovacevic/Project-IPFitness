package dev.projectfitness.ipfitness.services.impls;

import dev.projectfitness.ipfitness.models.dtos.DailyExercises;
import dev.projectfitness.ipfitness.models.dtos.Exercise;
import dev.projectfitness.ipfitness.repositories.ExerciseRepository;
import dev.projectfitness.ipfitness.services.ExerciseService;
import dev.projectfitness.ipfitness.util.Utility;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.*;

import static org.hibernate.query.sqm.TemporalUnit.DAY;

@Service
@RequiredArgsConstructor
public class ExerciseServiceImpl implements ExerciseService {
    private final ExerciseRepository exerciseRepository;

    @Value("${ipfitness.exercises.api-url}")
    private String apiUrl;
    @Value("${ipfitness.exercises.api-key}")
    private String apiKey;
    private static final String API_KEY_HEADER = "X-Api-Key";
    private static final int REQUIRED_NUMBER_OF_EXERCISES = 10;

    @Override
    public DailyExercises getDailyExercises() {
        Date todayDate = new Date();
        Optional<DailyExercises> exercisesOpt = exerciseRepository.getAllExercises();

        if(exercisesOpt.isEmpty())
            return getNewDailyExercises();

        DailyExercises dailyExercises = exercisesOpt.get();
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(dailyExercises.getForDate());
        calendar.add(Calendar.DATE, 1);

        if (dailyExercises.getExercises().size() != REQUIRED_NUMBER_OF_EXERCISES
                || Utility.compareUtilDates(calendar.getTime(), todayDate) < 0) {
            return getNewDailyExercises();
        } else {
            return dailyExercises;
        }
    }

    private DailyExercises getNewDailyExercises(){
        List<Exercise> exercises = fetchDailyExercises();
        DailyExercises newDailyExercises = new DailyExercises(new Date(), exercises);
        exerciseRepository.saveExercises(newDailyExercises);
        return newDailyExercises;
    }

    public List<Exercise> fetchDailyExercises() {
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
        headers.add(API_KEY_HEADER, apiKey);

        HttpEntity<String> entity = new HttpEntity<>("parameters", headers);
        ResponseEntity<Exercise[]> result =
                restTemplate.exchange(apiUrl, HttpMethod.GET, entity, Exercise[].class);
        Exercise[] responseBody = result.getBody();
        if (responseBody == null)
            return Collections.emptyList();

        return Arrays.asList(responseBody);
    }
}
