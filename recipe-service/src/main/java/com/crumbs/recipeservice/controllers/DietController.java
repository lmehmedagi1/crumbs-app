package com.crumbs.recipeservice.controllers;

import com.crumbs.recipeservice.exceptions.DietNotFoundException;
import com.crumbs.recipeservice.models.Diet;
import com.crumbs.recipeservice.requests.CreateDietRequest;
import com.crumbs.recipeservice.requests.UpdateDietRequest;
import com.crumbs.recipeservice.responses.DietResponse;
import com.crumbs.recipeservice.services.DietService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import javax.validation.Valid;
import java.util.List;

@RestController
public class DietController {

    @Autowired
    private DietService dietService;

    @GetMapping("/diets")
    public ResponseEntity<List<Diet>> getAllDiets() {
        return ResponseEntity.ok(dietService.getAllDiets());
    }

    @GetMapping("/diet")
    public ResponseEntity<Diet> getDiet(@RequestParam @Valid String id) {
        try {
            return ResponseEntity.ok(dietService.getDiet(id));
        } catch (DietNotFoundException dietNotFoundException) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, dietNotFoundException.getMessage());
        }
    }

    @PostMapping("/diet/create")
    public ResponseEntity<DietResponse> createDiet(@RequestBody @Valid CreateDietRequest createDietRequest) {
        try {
            final Diet diet = dietService.saveDiet(createDietRequest);
            return ResponseEntity.ok(new DietResponse(diet));
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
        }
    }

    @PatchMapping("/diet/update")
    public ResponseEntity<DietResponse> updateDiet(@RequestBody @Valid UpdateDietRequest updateDietRequest) {
        try {
            final Diet diet = dietService.updateDiet(updateDietRequest);
            return ResponseEntity.ok(new DietResponse(diet));
        } catch (DietNotFoundException dietNotFoundException) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, dietNotFoundException.getMessage());
        }
    }

    @DeleteMapping("/diet/delete")
    public void deleteDiet(@RequestParam @Valid String id) {
        try {
            dietService.deleteDiet(id);
        } catch (DietNotFoundException dietNotFoundException) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, dietNotFoundException.getMessage());
        }
    }
}
