package com.crumbs.systemevents.repositories;

import com.crumbs.systemevents.models.SystemEvent;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface SystemEventsRepository extends JpaRepository<SystemEvent, UUID> {
}
