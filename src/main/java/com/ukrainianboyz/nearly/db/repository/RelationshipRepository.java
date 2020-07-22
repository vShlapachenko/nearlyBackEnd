package com.ukrainianboyz.nearly.db.repository;

import com.ukrainianboyz.nearly.db.entity.UserRelationshipPK;
import com.ukrainianboyz.nearly.db.entity.UserRelationship;
import com.ukrainianboyz.nearly.model.enums.Status;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface RelationshipRepository extends JpaRepository<UserRelationship, UserRelationshipPK>{

    @Query(value = "SELECT entity FROM UserRelationship entity WHERE entity.status = :status "
            + "AND (entity.requesterId = :id OR entity.responderId = :id) ")
    List<UserRelationship> findByRequesterIdOrResponderIdAndStatus(@Param("id")String id,@Param("status") Status status);

    List<UserRelationship> findByRequesterIdAndStatus(String requesterId, Status status);

    List<UserRelationship> findByResponderIdAndStatus(String responderId, Status status);

    @Query(value = "SELECT entity FROM UserRelationship entity WHERE (entity.responderId = :firstId AND entity.requesterId = :secondId)" +
            " OR (entity.responderId = :secondId AND entity.requesterId = :firstId)")
    Optional<UserRelationship> findUserRelationship(@Param("firstId") String firstUserId, @Param("secondId") String secondUserId);
}
