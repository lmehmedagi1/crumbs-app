package com.crumbs.recipeservice.models;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.util.UUID;

@Data
@NoArgsConstructor
@Entity
@Table(name = "categories")
public class Category implements Serializable {

    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator")
    private UUID id;

    @NotBlank
    @Size(min = 5, message = "Category name must be at least 5 characters long!")
    @Size(max = 30, message = "Category name exceeds allowed limit of 30 characters!")
    @Pattern(regexp = "^[A-Za-zščćžđŠČĆŽĐ\\s]+$", flags = Pattern.Flag.UNICODE_CASE, message = "Category name can only contain letters and whitespaces!")
    private String name;

    @NotNull
    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "type_id", referencedColumnName = "id")
    private CategoryType categoryType;

    public Category(UUID id, String name, CategoryType categoryType) {
        this.id = id;
        this.name = name;
        this.categoryType = new CategoryType(categoryType.getId(), categoryType.getName());
    }

}
