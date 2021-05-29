package com.crumbs.userservice.responses;

import com.crumbs.userservice.projections.UserView;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class UserListResponse {
    private List<UserView> users;
    private boolean hasNext;
}
