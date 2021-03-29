package com.crumbs.recipeservice.models;

import lombok.AllArgsConstructor;
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
@AllArgsConstructor
@Entity
@Table(name = "ingredients")
public class Ingredient {

    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator")
    private UUID id;

    @NotBlank
    @Size(min = 5, message = "Ingredient name must be at least 5 characters long!")
    @Size(max = 30, message = "Ingredient name exceeds allowed limit of 30 characters!")
    @Pattern(regexp = "^[A-Za-z\\s]+$", flags = Pattern.Flag.UNICODE_CASE, message = "Ingredient name can only contain letters and whitespaces!")
    private String name;
}
