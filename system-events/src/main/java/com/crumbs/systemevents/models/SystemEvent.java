package com.crumbs.systemevents.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.GenericGenerator;
import org.springframework.data.annotation.CreatedDate;
import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "system_events")
public class SystemEvent implements Serializable {

    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator")
    private UUID id;

    @NotNull
    @CreatedDate
    private LocalDateTime timestamp = LocalDateTime.now();

    @Column(name = "service_name")
    private String serviceName;

    @Column(name = "action_type")
    private String actionType;

    @Column(name = "resource_name")
    private String resourceName;

    @Column(name = "response_type")
    private String responseType;

    @Column(name = "message")
    private String message;
}
