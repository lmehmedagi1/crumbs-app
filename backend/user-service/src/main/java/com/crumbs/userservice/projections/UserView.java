package com.crumbs.userservice.projections;

import org.springframework.beans.factory.annotation.Value;

import java.util.UUID;

public interface UserView {
    UUID getId();
    String getUsername();
    String getEmail();

    @Value("#{target.userProfile.firstName}")
    String getFirstName();

    @Value("#{target.userProfile.lastName}")
    String getLastName();

    @Value("#{target.userProfile.gender}")
    String getGender();

    @Value("#{target.userProfile.phoneNumber}")
    String getPhoneNumber();

    @Value("#{target.userProfile.avatar}")
    String getAvatar();
}
