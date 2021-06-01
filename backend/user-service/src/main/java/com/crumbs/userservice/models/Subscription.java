package com.crumbs.userservice.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.MapsId;
import javax.persistence.Table;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "subscriptions")
public class Subscription {

    @EmbeddedId
    private SubscriptionId id;

    @ManyToOne
    @MapsId("authorId")
    @JoinColumn(name = "author_id")
    private User author;

    @ManyToOne
    @MapsId("subscriberId")
    @JoinColumn(name = "subscriber_id")
    private User subscriber;

    @Column(name = "created_at")
    private LocalDateTime createdAt;
}
