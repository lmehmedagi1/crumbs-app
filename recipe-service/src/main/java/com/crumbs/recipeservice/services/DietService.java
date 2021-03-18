package com.crumbs.recipeservice.services;

import com.crumbs.recipeservice.models.Diet;
import com.crumbs.recipeservice.repositories.DietRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class DietService {

    @Autowired
    private DietRepository dietRepository;

    @Transactional(readOnly = true)
    public List<Diet> getAllDiets() {
        return dietRepository.findAll();
    }
}
