package dev.projectfitness.ipfitness.repositories;

import dev.projectfitness.ipfitness.models.entities.ActivityEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ActivityEntityRepository extends JpaRepository<ActivityEntity, Integer> {
    @Query("SELECT e FROM ActivityEntity e WHERE e.userId=:userId and MONTH(e.datePosted)=:month AND YEAR(e.datePosted)=:year ORDER BY e.datePosted DESC")
    List<ActivityEntity> getAllUserActivitiesForPeriodOrderDesc(Integer userId, Integer month, Integer year);

    @Query("SELECT e FROM ActivityEntity e WHERE e.userId=:userId and MONTH(e.datePosted)=:month AND YEAR(e.datePosted)=:year ORDER BY e.datePosted ASC")
    List<ActivityEntity> getAllUserActivitiesForPeriodOrderAsc(Integer userId, Integer month, Integer year);
}
