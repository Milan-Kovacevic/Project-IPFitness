package dev.projectfitness.ipfitness.repositories;

import dev.projectfitness.ipfitness.models.entities.FitnessUserEntity;
import dev.projectfitness.ipfitness.models.entities.MessageEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface MessageEntityRepository extends JpaRepository<MessageEntity, Integer> {

    @Query("SELECT DISTINCT u FROM FitnessUserEntity u INNER JOIN MessageEntity m ON u.userId=m.userFromId OR u.userId=m.userToId" +
            " WHERE u.userId!=:userId AND (m.userFromId=:userId OR m.userToId=:userId)")
    List<FitnessUserEntity> getAllSendersForUser(Integer userId);

    // TODO: Add limit to number of queryable messages, or add pagination

    @Query("SELECT m FROM MessageEntity m WHERE (m.userFromId=:userId AND m.userToId=:otherUserId) OR (m.userFromId=:otherUserId AND m.userToId=:userId) ORDER BY m.timeSent DESC")
    List<MessageEntity> getAllUserChatMessages(Integer userId, Integer otherUserId);
}
