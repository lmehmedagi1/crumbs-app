package com.crumbs.recipeservice.controllers;

import com.crumbs.recipeservice.projections.UserDietView;
import com.crumbs.recipeservice.requests.DietRequest;
import com.crumbs.recipeservice.requests.WebClientRequest;
import com.crumbs.recipeservice.responses.DietResponse;
import com.crumbs.recipeservice.responses.DietViewResponse;
import com.crumbs.recipeservice.services.DietService;
import com.crumbs.recipeservice.utility.JwtConfigAndUtil;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping(value = "/diets")
@ApiResponses(value = {
        @ApiResponse(code = 200, message = "OK"),
        @ApiResponse(code = 400, message = "Bad Request"),
        @ApiResponse(code = 404, message = "Not Found"),
        @ApiResponse(code = 500, message = "Internal Server Error")
})
public class DietController {

    private final DietService dietService;
    private final WebClientRequest webClientRequest;

    @Autowired
    public DietController(DietService dietService, WebClientRequest webClientRequest) {
        this.dietService = dietService;
        this.webClientRequest = webClientRequest;
    }

    @GetMapping
    public ResponseEntity<DietViewResponse> getDiets(
            @RequestParam(defaultValue = "0") Integer pageNo,
            @RequestParam(defaultValue = "3") Integer pageSize,
            @RequestParam(defaultValue = "title-asc") String sort,
            @RequestParam(defaultValue = "") String search) {
        return ResponseEntity.ok(dietService.getDietViews(pageNo, pageSize, sort, search));
    }

    @GetMapping("/public")
    public ResponseEntity<DietResponse> getDiet(@RequestParam @NotNull UUID id) {
        return ResponseEntity.ok(dietService.getPublicDiet(id));
    }

    @GetMapping("/private")
    public ResponseEntity<DietResponse> getPrivateDiet(@RequestParam @NotNull UUID id, @RequestHeader("Authorization") String jwt) {
        return ResponseEntity.ok(dietService.getPrivateDiet(id, webClientRequest.getUserPreview(JwtConfigAndUtil.getUserIdFromJwt(jwt))));
    }

    @PostMapping
    public ResponseEntity<String> createDiet(@RequestBody @Valid DietRequest dietRequest, @RequestHeader("Authorization") String jwt) {
        webClientRequest.checkIfUserExists(jwt);
        final UUID userId = JwtConfigAndUtil.getUserIdFromJwt(jwt);
        dietService.saveDiet(dietRequest, userId);
        return ResponseEntity.ok("Diet successfully added");
    }

    @PatchMapping
    public ResponseEntity<String> updateDiet(@RequestParam("id") @NotNull UUID id,
                                        @RequestBody @Valid DietRequest dietRequest,
                                        @RequestHeader("Authorization") String jwt) {
        webClientRequest.checkIfUserExists(jwt);
        final UUID userId = JwtConfigAndUtil.getUserIdFromJwt(jwt);
        dietService.updateDiet(dietRequest, id, userId);
        return ResponseEntity.ok("Diet successfully updated");
    }

    @DeleteMapping
    public ResponseEntity<?> deleteDiet(@RequestParam("id") @NotNull UUID id, @RequestHeader("Authorization") String jwt) {
        UUID userId = JwtConfigAndUtil.getUserIdFromJwt(jwt);
        dietService.deleteDiet(id, userId);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/user")
    public ResponseEntity<List<UserDietView>> getUserDiets(@RequestParam UUID id, @RequestHeader(value = "Authorization", required = false) String jwt) {
        List<UserDietView> diets;
        if (jwt != null && !JwtConfigAndUtil.getUserIdFromJwt(jwt).equals(id)) diets = dietService.getPublicUserDiets(id);
        else diets = dietService.getUserDiets(id);
        for (UserDietView diet : diets) {
            diet.setImage(dietService.getDietImage(diet.getId()));
        }
        return ResponseEntity.ok(diets);
    }
}
