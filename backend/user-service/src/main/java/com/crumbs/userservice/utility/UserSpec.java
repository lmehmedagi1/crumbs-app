package com.crumbs.userservice.utility;

import com.crumbs.userservice.models.User;
import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.Predicate;

public class UserSpec {

    public static Specification<User> isSearched(String search) {
        return (root, query, cb) -> {
            Predicate firstNamePredicate = cb.like(cb.lower(root.get("userProfile").get("firstName")), "%"+ search.toLowerCase() + "%");
            Predicate lastNamePredicate = cb.like(cb.lower(root.get("userProfile").get("lastName")), "%"+ search.toLowerCase() + "%");
            return cb.or(firstNamePredicate, lastNamePredicate);
        };
    }
}
