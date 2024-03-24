package dev.projectfitness.ipfitness.util;

import dev.projectfitness.ipfitness.exceptions.ForbiddenException;
import dev.projectfitness.ipfitness.exceptions.UnauthorizedException;
import dev.projectfitness.ipfitness.models.enums.ProgramDifficulty;
import dev.projectfitness.ipfitness.models.enums.ProgramState;
import dev.projectfitness.ipfitness.models.enums.TrainingLocation;
import dev.projectfitness.ipfitness.models.requests.FiltersRequest;
import dev.projectfitness.ipfitness.security.models.JwtUser;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.Calendar;

public final class Utility {

    public static java.util.Date getUtilCurrentDate() {
        return new java.util.Date(System.currentTimeMillis());
    }

    public static java.util.Date addToUtilDate(java.util.Date date, long millis) {
        return new java.util.Date(date.getTime() + millis);
    }

    public static java.util.Date getOnlyDate(java.util.Date dateWithTime) {
        Calendar calendar = Calendar.getInstance();

        calendar.setTime(dateWithTime);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);

        return calendar.getTime();
    }

    public static int compareUtilDates(java.util.Date ld, java.util.Date rd) {
        return ld.compareTo(rd);
    }

    public static JwtUser getJwtUser() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null || auth.getPrincipal() == null || !(auth.getPrincipal() instanceof JwtUser))
            return null;
        return (JwtUser) auth.getPrincipal();
    }

    public static <T extends Enum<T>> T parseEnumValue(Class<T> type, String value) {
        if (value.isEmpty())
            return null;
        return Enum.valueOf(type, value);
    }

    // Throws runtime exceptions if authorization is invalid
    public static void authorizeFitnessUser(Authentication auth, Integer requestUserId) {
        if (auth == null)
            throw new UnauthorizedException();
        JwtUser jwtUser = (JwtUser) auth.getPrincipal();
        if (!jwtUser.getUserId().equals(requestUserId))
            throw new ForbiddenException();
    }

    public static FiltersRequest parseFiltersRequest(Integer categoryFilter, String locationFilter,
                                                     String difficultyFilter, String stateFilter, String searchQuery) {
        TrainingLocation location = Utility.parseEnumValue(TrainingLocation.class, locationFilter);
        ProgramDifficulty difficulty = Utility.parseEnumValue(ProgramDifficulty.class, difficultyFilter);
        ProgramState state = Utility.parseEnumValue(ProgramState.class, stateFilter);
        return new FiltersRequest(categoryFilter, location, difficulty, state, searchQuery);
    }
}
