package com.crumbs.userservice.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.io.Serializable;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Embeddable
public class SubscriptionId implements Serializable {
    @Column(name = "author_id")
    private UUID authorId;

    @Column(name = "subscriber_id")
    private UUID subscriberId;
}
