package dev.projectfitness.ipfitness.services.impls;

import dev.projectfitness.ipfitness.exceptions.BadRequestException;
import dev.projectfitness.ipfitness.exceptions.ForbiddenException;
import dev.projectfitness.ipfitness.exceptions.NotFoundException;
import dev.projectfitness.ipfitness.exceptions.UnauthorizedException;
import dev.projectfitness.ipfitness.models.dtos.*;
import dev.projectfitness.ipfitness.models.entities.*;
import dev.projectfitness.ipfitness.models.enums.ProgramDifficulty;
import dev.projectfitness.ipfitness.models.enums.ProgramState;
import dev.projectfitness.ipfitness.models.enums.TrainingLocation;
import dev.projectfitness.ipfitness.models.requests.FiltersRequest;
import dev.projectfitness.ipfitness.models.requests.FitnessProgramAddRequest;
import dev.projectfitness.ipfitness.models.requests.FitnessProgramRequest;
import dev.projectfitness.ipfitness.models.requests.ProgramInformationUpdateRequest;
import dev.projectfitness.ipfitness.repositories.*;
import dev.projectfitness.ipfitness.security.models.JwtUser;
import dev.projectfitness.ipfitness.services.ActionLoggingService;
import dev.projectfitness.ipfitness.services.FitnessProgramService;
import dev.projectfitness.ipfitness.services.StorageAccessService;
import dev.projectfitness.ipfitness.util.ActionMessageResolver;
import dev.projectfitness.ipfitness.util.Utility;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class FitnessProgramServiceImpl implements FitnessProgramService {
    private final FitnessProgramEntityRepository fitnessProgramEntityRepository;
    private final CategoryAttributeEntityRepository categoryAttributeEntityRepository;
    private final AttributeValueEntityRepository attributeValueEntityRepository;
    private final ProgramDemonstrationEntityRepository programDemonstrationEntityRepository;
    private final ProgramPictureEntityRepository programPictureEntityRepository;
    private final CategoryEntityRepository categoryEntityRepository;
    private final FitnessUserEntityRepository fitnessUserEntityRepository;
    private final StorageAccessService storageAccessService;
    private final CommentEntityRepository commentEntityRepository;
    private final ProgramPurchaseEntityRepository programPurchaseEntityRepository;
    private final ModelMapper modelMapper;
    private final ActionLoggingService actionLoggingService;

    @Override
    public Page<FitnessProgram> getAllFitnessPrograms(Pageable page, FiltersRequest request) {
        if (ProgramState.ACTIVE.equals(request.getState()))
            return fitnessProgramEntityRepository
                    .findAllProgramsFilteredAndActive(page, request.getCategoryFilter(),
                            request.getLocation(), request.getDifficulty(), request.getSearchQuery())
                    .map(this::mapEntityToDto);
        else if (ProgramState.UPCOMING.equals(request.getState())) {
            return fitnessProgramEntityRepository
                    .findAllProgramsFilteredAndUpcoming(page, request.getCategoryFilter(),
                            request.getLocation(), request.getDifficulty(), request.getSearchQuery())
                    .map(this::mapEntityToDto);
        } else if (ProgramState.FINISHED.equals(request.getState())) {
            return fitnessProgramEntityRepository
                    .findAllProgramsFilteredAndFinished(page, request.getCategoryFilter(),
                            request.getLocation(), request.getDifficulty(), request.getSearchQuery())
                    .map(this::mapEntityToDto);
        } else {
            return fitnessProgramEntityRepository
                    .findAllProgramsFiltered(page, request.getCategoryFilter(),
                            request.getLocation(), request.getDifficulty(), request.getSearchQuery())
                    .map(this::mapEntityToDto);
        }
    }

    @Override
    public SingleFitnessProgram getFitnessProgram(Integer programId) {
        return fitnessProgramEntityRepository
                .getByIdAndNotDeleted(programId).map(e -> {
                    SingleFitnessProgram program = modelMapper.map(e, SingleFitnessProgram.class);
                    program.setCategoryId(e.getCategoryId());

                    List<String> images = getProgramImages(programId);
                    program.setImages(images);

                    List<String> demonstrations = getProgramDemonstrations(programId);
                    program.setDemonstrations(demonstrations);

                    List<AttributeValue> attributeValues = attributeValueEntityRepository
                            .findAllByProgramId(programId)
                            .stream()
                            .map(a -> modelMapper.map(a, AttributeValue.class))
                            .toList();
                    program.setAttributeValues(attributeValues);

                    return program;
                })
                .orElseThrow(NotFoundException::new);
    }

    @Override
    public FitnessProgramDetails getFitnessProgramDetails(Integer programId) {
        return fitnessProgramEntityRepository
                .getByIdAndNotDeleted(programId).map(e -> {
                    FitnessProgramDetails program = modelMapper.map(e, FitnessProgramDetails.class);
                    program.setOwnerId(e.getFitnessUser().getUserId());
                    program.setOwnerFirstName(e.getFitnessUser().getFirstName());
                    program.setOwnerLastName(e.getFitnessUser().getLastName());
                    program.setOwnerAvatar(e.getFitnessUser().getAvatar());
                    program.setOwnerBiography(e.getFitnessUser().getBiography());
                    program.setOwnerContactInfo(e.getFitnessUser().getContactInfo());

                    JwtUser user = Utility.getJwtUser();
                    if (user == null)
                        program.setIsPurchased(false);
                    else
                        program.setIsPurchased(programPurchaseEntityRepository.existsByFitnessProgramProgramIdAndUserId(programId, user.getUserId()));

                    program.setNumberOfComments(commentEntityRepository.countAllByProgramId(programId));
                    program.setNumberOfParticipants(programPurchaseEntityRepository.countAllByFitnessProgramProgramId(programId));


                    if (e.getCategoryId() != null) {
                        CategoryEntity category = categoryEntityRepository.findById(e.getCategoryId()).orElseThrow(NotFoundException::new);
                        // Setting information about category and its attributes in response
                        program.setCategoryName(category.getName());
                        List<CategoryAttributeEntity> categoryAttributes = categoryAttributeEntityRepository.findAllByCategoryId(category.getCategoryId());

                        List<ProgramAttribute> programAttributes = new ArrayList<>();
                        categoryAttributes.forEach(a -> {
                            Optional<AttributeValueEntity> valueOpt = attributeValueEntityRepository.findByProgramIdAndAttributeId(programId, a.getAttributeId());
                            if (valueOpt.isPresent()) {
                                AttributeValueEntity value = valueOpt.get();
                                ProgramAttribute attribute = new ProgramAttribute();
                                attribute.setValueId(value.getValueId());
                                attribute.setName(a.getName());
                                attribute.setValue(value.getValue());
                                programAttributes.add(attribute);
                            }
                        });
                        program.setAttributes(programAttributes);
                    }


                    // Setting program pictures in response;
                    List<String> images = getProgramImages(programId);
                    program.setPictures(images);

                    return program;
                })
                .orElseThrow(NotFoundException::new);
    }

    @Override
    @Transactional
    public FitnessProgram addFitnessProgram(FitnessProgramAddRequest request) {
        JwtUser authenticatedUser = Utility.getJwtUser();
        if (authenticatedUser == null || !authenticatedUser.getUserId().equals(request.getUserId()))
            throw new UnauthorizedException();

        FitnessProgramEntity entity = modelMapper.map(request, FitnessProgramEntity.class);
        entity.setCreatedAt(new Date());
        entity.setDeleted(false);
        entity.setCategoryId(request.getCategoryId());

        FitnessUserEntity user = fitnessUserEntityRepository.findById(request.getUserId()).orElseThrow(NotFoundException::new);
        entity.setFitnessUser(user);
        FitnessProgramEntity savedEntity = fitnessProgramEntityRepository.saveAndFlush(entity);


        // Add attribute values if it belongs to the correct category...
        if (request.getAttributeValues() != null) {
            Arrays.stream(request.getAttributeValues()).forEach((attribute) -> {
                if (categoryAttributeEntityRepository.existsByAttributeIdAndCategoryId(attribute.getAttributeId(), request.getCategoryId())) {
                    AttributeValueEntity attributeValueEntity = modelMapper.map(attribute, AttributeValueEntity.class);
                    attributeValueEntity.setProgramId(savedEntity.getProgramId());
                    attributeValueEntity.setValueId(null);
                    attributeValueEntityRepository.saveAndFlush(attributeValueEntity);
                }
            });
        }

        if (request.getDemonstrations() != null) {
            // Add demonstration videos...
            for (int i = 0; i < request.getDemonstrations().size(); i++) {
                ProgramDemonstrationEntity demonstrationEntity = new ProgramDemonstrationEntity();
                demonstrationEntity.setOrderNumber(i);
                demonstrationEntity.setVideoUrl(request.getDemonstrations().get(i));
                demonstrationEntity.setProgramId(savedEntity.getProgramId());
                programDemonstrationEntityRepository.saveAndFlush(demonstrationEntity);
            }
        }

        if (request.getPictures() != null) {
            // Add program pictures...
            for (int i = 0; i < request.getPictures().length; i++) {
                Optional<String> picture = storageAccessService.saveFile(request.getPictures()[i]);
                if (picture.isPresent()) {
                    ProgramPictureEntity pictureEntity = new ProgramPictureEntity();
                    pictureEntity.setOrderNumber(i);
                    pictureEntity.setPictureLocation(picture.get());
                    pictureEntity.setProgramId(savedEntity.getProgramId());
                    programPictureEntityRepository.saveAndFlush(pictureEntity);
                }
            }
        }
        actionLoggingService.logMessage(ActionMessageResolver.resolveAddFitnessProgram(savedEntity));
        return mapEntityToDto(savedEntity);
    }

    @Override
    public void deleteFitnessProgram(Integer programId) {
        FitnessProgramEntity entity = fitnessProgramEntityRepository.findById(programId).orElseThrow(NotFoundException::new);

        JwtUser authenticatedUser = Utility.getJwtUser();
        if (authenticatedUser == null || !authenticatedUser.getUserId().equals(entity.getFitnessUser().getUserId()))
            throw new UnauthorizedException();

        // Only sets the flag that it is deleted
        entity.setDeleted(true);

        fitnessProgramEntityRepository.saveAndFlush(entity);
        actionLoggingService.logSensitiveAction(ActionMessageResolver.resolveDeleteFitnessProgram(programId, authenticatedUser));
    }

    @Override
    @Transactional
    public void updateFitnessProgramInformation(Integer programId, ProgramInformationUpdateRequest request) {
        FitnessProgramEntity entity = fitnessProgramEntityRepository.getByIdAndNotDeleted(programId).orElseThrow(NotFoundException::new);
        JwtUser authenticatedUser = Utility.getJwtUser();
        if (authenticatedUser == null || !authenticatedUser.getUserId().equals(entity.getFitnessUser().getUserId()))
            throw new UnauthorizedException();
        modelMapper.map(request, entity);

        if (!categoryEntityRepository.existsById(request.getCategoryId()))
            throw new NotFoundException();
        entity.setCategoryId(request.getCategoryId());

        fitnessProgramEntityRepository.saveAndFlush(entity);
        attributeValueEntityRepository.deleteAllByProgramId(entity.getProgramId());
        Arrays.stream(request.getValues()).forEach((attribute) -> {
            if (categoryAttributeEntityRepository.existsByAttributeIdAndCategoryId(attribute.getAttributeId(), request.getCategoryId())) {
                AttributeValueEntity attributeValueEntity = modelMapper.map(attribute, AttributeValueEntity.class);
                attributeValueEntity.setProgramId(entity.getProgramId());
                attributeValueEntity.setValueId(null);
                attributeValueEntityRepository.saveAndFlush(attributeValueEntity);
            }
        });
        actionLoggingService.logMessage(ActionMessageResolver.resolveUpdateFitnessProgram(programId, authenticatedUser));
    }

    @Override
    @Transactional
    public void updateFitnessProgramPictures(Integer programId, MultipartFile[] pictures) {
        FitnessProgramEntity entity = fitnessProgramEntityRepository.getByIdAndNotDeleted(programId).orElseThrow(NotFoundException::new);
        JwtUser authenticatedUser = Utility.getJwtUser();
        if (authenticatedUser == null || !authenticatedUser.getUserId().equals(entity.getFitnessUser().getUserId()))
            throw new UnauthorizedException();

        programPictureEntityRepository.deleteAllByProgramId(programId);
        if (pictures != null) {
            for (int i = 0; i < pictures.length; i++) {
                Optional<String> picture = storageAccessService.saveFile(pictures[i]);
                if (picture.isPresent()) {
                    ProgramPictureEntity pictureEntity = new ProgramPictureEntity();
                    pictureEntity.setOrderNumber(i);
                    pictureEntity.setPictureLocation(picture.get());
                    pictureEntity.setProgramId(programId);
                    programPictureEntityRepository.saveAndFlush(pictureEntity);
                }
            }
        }
    }

    @Override
    @Transactional
    public void updateFitnessProgramDemonstrations(Integer programId, List<String> videos) {
        FitnessProgramEntity entity = fitnessProgramEntityRepository.getByIdAndNotDeleted(programId).orElseThrow(NotFoundException::new);
        JwtUser authenticatedUser = Utility.getJwtUser();
        if (authenticatedUser == null || !authenticatedUser.getUserId().equals(entity.getFitnessUser().getUserId()))
            throw new UnauthorizedException();

        programDemonstrationEntityRepository.deleteAllByProgramId(programId);
        for (int i = 0; i < videos.size(); i++) {
            ProgramDemonstrationEntity demonstrationEntity = new ProgramDemonstrationEntity();
            demonstrationEntity.setOrderNumber(i);
            demonstrationEntity.setVideoUrl(videos.get(i));
            demonstrationEntity.setProgramId(programId);
            programDemonstrationEntityRepository.saveAndFlush(demonstrationEntity);
        }
    }

    @Override
    public List<String> getFitnessProgramDemonstrations(Integer programId) {
        FitnessProgramEntity entity = fitnessProgramEntityRepository.getByIdAndNotDeleted(programId).orElseThrow(NotFoundException::new);
        JwtUser authenticatedUser = Utility.getJwtUser();
        if (authenticatedUser == null)
            throw new UnauthorizedException();

        if (!programPurchaseEntityRepository.existsByFitnessProgramProgramIdAndUserId(programId, authenticatedUser.getUserId()))
            throw new ForbiddenException();

        return entity
                .getOnlineDemonstrations()
                .stream()
                .map(ProgramDemonstrationEntity::getVideoUrl)
                .collect(Collectors.toList());
    }

    private FitnessProgram mapEntityToDto(FitnessProgramEntity entity) {
        FitnessProgram program = modelMapper.map(entity, FitnessProgram.class);
        if (entity.getCategoryId() != null) {
            CategoryEntity category = categoryEntityRepository.findById(entity.getCategoryId()).orElseThrow(NotFoundException::new);
            program.setCategoryName(category.getName());
        }

        program.setCreatedBy(entity.getFitnessUser().getFirstName() + " " + entity.getFitnessUser().getLastName());
        program.setUserId(entity.getFitnessUser().getUserId());
        List<String> images = getProgramImages(entity.getProgramId());
        program.setImages(images);
        return program;
    }


    private List<String> getProgramImages(Integer programId) {
        return programPictureEntityRepository
                .findByProgramIdOrderByOrderNumberAsc(programId)
                .stream()
                .map(ProgramPictureEntity::getPictureLocation)
                .collect(Collectors.toList());
    }

    private List<String> getProgramDemonstrations(Integer programId) {
        return programDemonstrationEntityRepository
                .findAllByProgramIdOrderByOrderNumberAsc(programId)
                .stream()
                .map(ProgramDemonstrationEntity::getVideoUrl)
                .toList();
    }
}
