package dev.projectfitness.ipfitness.services.impls;

import dev.projectfitness.ipfitness.exceptions.NotFoundException;
import dev.projectfitness.ipfitness.models.dtos.Category;
import dev.projectfitness.ipfitness.models.dtos.CategoryAttribute;
import dev.projectfitness.ipfitness.models.dtos.CategorySubscription;
import dev.projectfitness.ipfitness.models.entities.CategoryEntity;
import dev.projectfitness.ipfitness.models.entities.FitnessUserEntity;
import dev.projectfitness.ipfitness.models.requests.CategorySubscriptionChangeRequest;
import dev.projectfitness.ipfitness.repositories.CategoryAttributeEntityRepository;
import dev.projectfitness.ipfitness.repositories.CategoryEntityRepository;
import dev.projectfitness.ipfitness.repositories.FitnessUserEntityRepository;
import dev.projectfitness.ipfitness.services.ActionLoggingService;
import dev.projectfitness.ipfitness.services.CategoryService;
import dev.projectfitness.ipfitness.util.ActionMessageResolver;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CategoryServiceImpl implements CategoryService {
    private final CategoryEntityRepository categoryEntityRepository;
    private final FitnessUserEntityRepository fitnessUserEntityRepository;
    private final CategoryAttributeEntityRepository categoryAttributeEntityRepository;
    private final ModelMapper modelMapper;
    private final ActionLoggingService actionLoggingService;

    @Override
    public List<Category> getAllCategories() {
        return categoryEntityRepository
                .findAll()
                .stream()
                .map(c -> modelMapper.map(c, Category.class))
                .collect(Collectors.toList());
    }

    @Override
    public List<CategoryAttribute> getAllCategoryAttributes(Integer categoryId) {
        return categoryAttributeEntityRepository
                .findAllByCategoryId(categoryId)
                .stream()
                .map(c -> modelMapper.map(c, CategoryAttribute.class))
                .collect(Collectors.toList());
    }

    @Override
    public List<CategorySubscription> getAllCategorySubscriptions(Integer userId) {
        FitnessUserEntity user = fitnessUserEntityRepository.findById(userId).orElseThrow(NotFoundException::new);
        Set<Integer> subscribedCategories = user
                .getSubscribedCategories()
                .stream()
                .map(CategoryEntity::getCategoryId)
                .collect(Collectors.toSet());

        return categoryEntityRepository
                .findAll()
                .stream()
                .map(c -> {
                    CategorySubscription subscription = modelMapper.map(c, CategorySubscription.class);
                    boolean isSubscribed = subscribedCategories.contains(c.getCategoryId());
                    subscription.setIsSubscribed(isSubscribed);
                    return subscription;
                })
                .collect(Collectors.toList());
    }

    @Override
    public void changeUserCategorySubscription(Integer userId, CategorySubscriptionChangeRequest request) {
        FitnessUserEntity user = fitnessUserEntityRepository.findById(userId).orElseThrow(NotFoundException::new);
        Set<Integer> subscribedCategories = user
                .getSubscribedCategories()
                .stream()
                .map(CategoryEntity::getCategoryId)
                .collect(Collectors.toSet());

        if (request.getSubscribe().equals(true) && !subscribedCategories.contains(request.getCategoryId())) {
            CategoryEntity entity = categoryEntityRepository.findById(request.getCategoryId()).orElseThrow(NotFoundException::new);
            entity.getFitnessUsers().add(user);
            user.getSubscribedCategories().add(entity);
            fitnessUserEntityRepository.saveAndFlush(user);
            actionLoggingService.logMessage(ActionMessageResolver.resolveCategorySubscription(user, entity, true));
        } else if (request.getSubscribe().equals(false) && subscribedCategories.contains(request.getCategoryId())) {
            CategoryEntity entity = categoryEntityRepository.findById(request.getCategoryId()).orElseThrow(NotFoundException::new);
            entity.getFitnessUsers().remove(user);
            user.getSubscribedCategories().remove(entity);
            fitnessUserEntityRepository.saveAndFlush(user);
            actionLoggingService.logMessage(ActionMessageResolver.resolveCategorySubscription(user, entity, false));
        }
    }
}
