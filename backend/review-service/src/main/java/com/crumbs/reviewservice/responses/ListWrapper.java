package com.crumbs.reviewservice.responses;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ListWrapper {
    private List<UUID> uuids;

    public List<UUID> getUuids() {
        return uuids;
    }

}
