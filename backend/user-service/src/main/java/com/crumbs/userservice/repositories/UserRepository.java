package com.crumbs.userservice.repositories;

import com.crumbs.userservice.models.User;
import com.crumbs.userservice.projections.UserClassView;
import com.crumbs.userservice.projections.UserView;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.QueryHints;
import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import th.co.geniustree.springdata.jpa.repository.JpaSpecificationExecutorWithProjection;

import java.util.List;
import java.util.UUID;

@Repository
public interface UserRepository extends JpaRepository<User, UUID>, JpaSpecificationExecutorWithProjection<User>,
        JpaSpecificationExecutor<User> {
    User findByEmail(String email);

    User findByUsername(String username);

    UserView findOneById(UUID id);

    @Query("SELECT new com.crumbs.userservice.projections.UserClassView(s.subscriber.id, " +
            "s.subscriber.userProfile.firstName, s.subscriber.userProfile.lastName," +
            "s.subscriber.username, s.subscriber.email) FROM User u INNER JOIN u.subscribers as s WHERE u.id=?1")
    List<UserClassView> getUserSubscribers(UUID id);

    @Query("SELECT new com.crumbs.userservice.projections.UserClassView(c.author.id, " +
            "c.author.userProfile.firstName, c.author.userProfile.lastName," +
            "c.author.username, c.author.email) FROM Subscription c WHERE c.subscriber.id = ?1")
    List<UserClassView> getUserSubscriptions(UUID id);
}
