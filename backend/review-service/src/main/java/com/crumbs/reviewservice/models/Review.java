package com.crumbs.reviewservice.models;

import com.crumbs.reviewservice.utility.annotation.NullOrNotBlank;
import com.crumbs.reviewservice.utility.converters.CustomEnumType;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Type;
import org.hibernate.annotations.TypeDef;
import org.springframework.data.annotation.CreatedDate;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.UUID;

@TypeDef(name = "entype", typeClass = CustomEnumType.class)
@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "reviews")
public class Review implements Serializable {

    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator")
    private UUID id;

    @NotNull
    @Column(name = "user_id")
    @JsonProperty("user_id")
    private UUID userId;

    @NotNull
    @Column(name = "entity_id")
    @JsonProperty("entity_id")
    private UUID entityId;

    public enum EntityType {recipe, diet}

    @NotNull
    @Column(name = "entity_type")
    @JsonProperty("entity_type")
    @Enumerated(EnumType.STRING)
    @Type(type = "entype")
    private EntityType entityType;

    @Column(name = "is_liked")
    @JsonProperty("is_liked")
    private Boolean isLiked;

    @Min(value = 1, message = "Rating must be between 1-5 inclusive!")
    @Max(value = 5, message = "Rating must be between 1-5 inclusive!")
    private Integer rating;

    @NullOrNotBlank
    @Size(min = 5, message = "Comment must contain at least 5 characters!")
    @Size(max = 2500, message = "Comment exceeds allowed limit of 2500 characters!")
    @Pattern(regexp = "^[A-Za-z0-9 .,:;\\-_?!&%/'@()\"]*$", flags = {Pattern.Flag.MULTILINE, Pattern.Flag.UNICODE_CASE},
            message = "Comment can only contain letters, numbers, spaces, and punctuation!")
    private String comment;

    @NotNull
    @CreatedDate
    @Column(name = "created_at")
    @JsonProperty("created_at")
    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    @JsonSerialize(using = LocalDateTimeSerializer.class)
    private LocalDateTime createdAt = LocalDateTime.now();

    @Column(name = "last_modify")
    @JsonProperty("last_modify")
    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    @JsonSerialize(using = LocalDateTimeSerializer.class)
    private LocalDateTime lastModify = LocalDateTime.now();
}
