package com.crumbs.userservice.utility;

import com.crumbs.userservice.controllers.UserProfileController;
import com.crumbs.userservice.models.UserProfile;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Component
public class UserProfileModelAssembler implements RepresentationModelAssembler<UserProfile, EntityModel<UserProfile>> {
    @Override
    public EntityModel<UserProfile> toModel(UserProfile userProfile) {
        return EntityModel.of(userProfile, linkTo(methodOn(UserProfileController.class).getUserProfile(userProfile.getUserId())).withSelfRel());
    }

    @Override
    public CollectionModel<EntityModel<UserProfile>> toCollectionModel(Iterable<? extends UserProfile> entities) {
        return null;
    }
}
