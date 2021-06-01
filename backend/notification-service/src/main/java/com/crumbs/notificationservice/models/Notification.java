package com.crumbs.notificationservice.models;

import com.crumbs.notificationservice.utility.converters.CustomEnumType;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.GenericGenerator;
import org.springframework.data.annotation.CreatedDate;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;
import java.util.UUID;
import org.hibernate.annotations.Type;
import org.hibernate.annotations.TypeDef;

@TypeDef(name = "entype", typeClass = CustomEnumType.class)
@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "notifications")
public class Notification {

    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator")
    private UUID id;

    @NotNull
    @Column(name = "user_id")
    private UUID userId;

    @NotNull
    @Column(name = "entity_id")
    private UUID entityId;

    public enum EntityType {recipe, diet, crumbs_user}

    @NotNull
    @Column(name = "entity_type")
    @Enumerated(EnumType.STRING)
    @Type(type = "entype")
    private EntityType entityType;

    @NotBlank
    @Size(max = 100, message = "Title exceeds allowed limit of 100 characters!")
    @Pattern(regexp = "^[A-Za-z0-9 .,:;\\-_?!&%/'@()\"]*$", flags = {Pattern.Flag.MULTILINE, Pattern.Flag.UNICODE_CASE},
            message = "Title can only contain letters, numbers, spaces, and punctuation marks!")
    private String title;

    @NotBlank
    @Size(max = 150, message = "Description exceeds allowed limit of 250 characters!")
    @Pattern(regexp = "^[A-Za-z0-9 .,:;\\-_?!&%/'@()\"]*$", flags = {Pattern.Flag.MULTILINE, Pattern.Flag.UNICODE_CASE},
            message = "Description can only contain letters, numbers, spaces, and punctuation marks!")
    private String description;

    @NotNull
    @Column(name = "is_read")
    private Boolean isRead;

    @NotNull
    @CreatedDate
    @Column(name = "created_at")
    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    @JsonSerialize(using = LocalDateTimeSerializer.class)
    private LocalDateTime createdAt = LocalDateTime.now();
}
