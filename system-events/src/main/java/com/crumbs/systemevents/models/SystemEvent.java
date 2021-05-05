package com.crumbs.systemevents.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.GenericGenerator;
import org.springframework.data.annotation.CreatedDate;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
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

    @NotBlank
    @Column(name = "service_name")
    private String serviceName;

    @NotBlank
    @Column(name = "resource_name")
    private String resourceName;

    @NotBlank
    @Column(name = "method")
    private String method;

    @NotBlank
    @Column(name = "response_status")
    private String responseStatus;
}
