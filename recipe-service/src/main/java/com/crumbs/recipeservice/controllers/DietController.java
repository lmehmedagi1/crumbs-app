package com.crumbs.recipeservice.controllers;

import com.crumbs.recipeservice.models.Diet;
import com.crumbs.recipeservice.services.DietService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class DietController {

    @Autowired
    private DietService dietService;

    @GetMapping("/diets")
    public ResponseEntity<List<Diet>> getAllDiets() {
        return ResponseEntity.ok(dietService.getAllDiets());
    }
}
