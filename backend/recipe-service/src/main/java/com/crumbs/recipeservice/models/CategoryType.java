package com.crumbs.recipeservice.models;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.util.List;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "category_type")
public class CategoryType {

    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator")
    private UUID id;

    @NotBlank
    @Size(min = 5, message = "Category Type name must be at least 5 characters long!")
    @Size(max = 30, message = "Category Type name exceeds allowed limit of 30 characters!")
    @Pattern(regexp = "^[A-Za-z\\s]+$", flags = Pattern.Flag.UNICODE_CASE, message = "Category Type name can only contain letters and whitespaces!")
    private String name;
}
