package com.crumbs.recipeservice.models;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.util.UUID;

@Data
@NoArgsConstructor
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
    @Pattern(regexp = "^[A-Za-zščćžđŠČĆŽĐ\\s]+$", flags = Pattern.Flag.UNICODE_CASE, message = "Category Type name can only contain letters and whitespaces!")
    private String name;

    public CategoryType(UUID id, String name) {
        this.id = id;
        this.name = name;
    }
}
