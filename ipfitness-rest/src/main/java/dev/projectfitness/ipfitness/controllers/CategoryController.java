package dev.projectfitness.ipfitness.controllers;

import dev.projectfitness.ipfitness.models.dtos.Category;
import dev.projectfitness.ipfitness.models.dtos.CategoryAttribute;
import dev.projectfitness.ipfitness.models.dtos.CategorySubscription;
import dev.projectfitness.ipfitness.models.requests.CategorySubscriptionChangeRequest;
import dev.projectfitness.ipfitness.services.CategoryService;
import dev.projectfitness.ipfitness.util.Utility;
import jakarta.validation.Valid;
import jakarta.websocket.server.PathParam;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("${ipfitness.base-url}")
public class CategoryController {
    private final CategoryService categoryService;

    @GetMapping("/categories")
    public List<Category> getAllCategories() {
        return categoryService.getAllCategories();
    }

    @GetMapping("/categories/{categoryId}/attributes")
    public List<CategoryAttribute> getAllCategoryAttributes(@PathVariable("categoryId") Integer categoryId) {
        return categoryService.getAllCategoryAttributes(categoryId);
    }

    @GetMapping("/users/{userId}/categories")
    public List<CategorySubscription> getAllUserCategorySubscriptions(@PathVariable("userId") Integer userId, Authentication auth) {
        Utility.authorizeFitnessUser(auth, userId);
        return categoryService.getAllCategorySubscriptions(userId);
    }

    @PutMapping("/users/{userId}/categories")
    public void changeUserCategorySubscriptions(@PathVariable("userId") Integer userId,
                                                @RequestBody @Valid CategorySubscriptionChangeRequest request, Authentication auth) {
        Utility.authorizeFitnessUser(auth, userId);
        categoryService.changeUserCategorySubscription(userId, request);
    }

}
