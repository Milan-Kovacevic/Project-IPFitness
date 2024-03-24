package dev.projectfitness.ipfitness.services;

import dev.projectfitness.ipfitness.models.dtos.Category;
import dev.projectfitness.ipfitness.models.dtos.CategoryAttribute;
import dev.projectfitness.ipfitness.models.dtos.CategorySubscription;
import dev.projectfitness.ipfitness.models.requests.CategorySubscriptionChangeRequest;

import java.util.List;

public interface CategoryService {
    List<Category> getAllCategories();

    List<CategoryAttribute> getAllCategoryAttributes(Integer categoryId);

    List<CategorySubscription> getAllCategorySubscriptions(Integer userId);

    void changeUserCategorySubscription(Integer userId, CategorySubscriptionChangeRequest request);
}
