package com.crumbs.userservice.utility;

import com.crumbs.userservice.controllers.UserDetailsController;
import com.crumbs.userservice.models.UserDetails;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Component
public class UserDetailsModelAssembler implements RepresentationModelAssembler<UserDetails, EntityModel<UserDetails>> {
    @Override
    public EntityModel<UserDetails> toModel(UserDetails userDetails) {
        return EntityModel.of(userDetails, linkTo(methodOn(UserDetailsController.class).getUserDetails(userDetails.getId())).withSelfRel());
    }

    @Override
    public CollectionModel<EntityModel<UserDetails>> toCollectionModel(Iterable<? extends UserDetails> entities) {
        return null;
    }
}
