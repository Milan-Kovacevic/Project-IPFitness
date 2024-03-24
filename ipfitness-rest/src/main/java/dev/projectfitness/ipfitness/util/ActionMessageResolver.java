package dev.projectfitness.ipfitness.util;

import dev.projectfitness.ipfitness.models.dtos.AttachmentEmail;
import dev.projectfitness.ipfitness.models.entities.*;
import dev.projectfitness.ipfitness.security.models.JwtUser;

import java.util.List;

public final class ActionMessageResolver {

    public static String resolveSuccessfulLogin(FitnessUserEntity fitnessUser) {
        return "User " + fitnessUser.getFirstName() + " " + fitnessUser.getLastName()
                + ", with username " + fitnessUser.getUsername() + " and id=" + fitnessUser.getUserId() + " logged in successfully. "
                + "Account status: " + (fitnessUser.getActive() ? "active" : "inactive");
    }

    public static String resolveRegister(FitnessUserEntity fitnessUser) {
        return "Registered new fitness user with username " + fitnessUser.getUsername() + ". "
                + "Account status: " + (fitnessUser.getActive() ? "active" : "inactive");
    }

    public static String resolveAccountVerification(FitnessUserEntity fitnessUser) {
        return "Account activated successfully for fitness user " + fitnessUser.getFirstName() + " " + fitnessUser.getLastName()
                + " with username " + fitnessUser.getUsername() + ". Account status: " + (fitnessUser.getActive() ? "active" : "inactive");
    }

    public static String resolveAttachmentMailSend(AttachmentEmail email) {
        return "Attachment email sent to " + email.getSendTo() + ", with subject: "
                + email.getSubject() + ". " + email.getAttachmentFiles().size() + " files total attached.";
    }

    public static String resolveTemplatedMailSend(List<String> sendTo, String subject) {
        return "Templated email send to " + sendTo.stream().reduce((x, acc) -> acc += x + ";") + " with subject: " + subject;
    }

    public static String resolveCategorySubscription(FitnessUserEntity user, CategoryEntity category, boolean isSubscribed) {
        return "User " + user.getFirstName() + " " + user.getLastName() + ", id=" + user.getUserId() + " " + (isSubscribed ? "subscribed to" : "unsubscribed from")
                + " category " + category.getName() + ", with id=" + category.getCategoryId();
    }

    public static String resolveProgramCommentPost(FitnessUserEntity user, CommentEntity newEntity) {
        return "User " + user.getFirstName() + " " + user.getLastName() + ", id=" + user.getUserId() + ", posted comment on fitness program with id=" + newEntity.getProgramId();
    }

    public static String resolveAddFitnessProgram(FitnessProgramEntity fitnessProgram) {
        return "User " + fitnessProgram.getFitnessUser().getFirstName() + " " + fitnessProgram.getFitnessUser().getLastName()
                + ", id=" + fitnessProgram.getFitnessUser().getUserId() + " created new fitness program with id=" + fitnessProgram.getProgramId();
    }

    public static String resolveDeleteFitnessProgram(Integer programId, JwtUser user) {
        return "User with id=" + user.getUserId() + " successfully delete fitness program with id=" + programId;
    }

    public static String resolveUpdateFitnessProgram(Integer programId, JwtUser user) {
        return "User " + user.getUsername() + ", with id=" + user.getUserId() + " updated fitness program with id=" + programId;
    }

    public static String resolveDirectMessageSend(MessageEntity message, boolean isInitial) {
        return "User with id=" + message.getUserFromId() + " sent "
                + (isInitial ? "initial(discovery)" : "direct") + " message to user with id=" + message.getUserToId()
                + ". Message id=" + message.getMessageId();
    }

    public static String resolveFitnessProgramPurchase(ProgramPurchaseEntity purchase) {
        return "User with id=" + purchase.getUserId() + " purchased fitness program with id=" + purchase.getFitnessProgram().getProgramId()
                + " for " + purchase.getPrice() + "$. Purchase item id=" + purchase.getPurchaseId();
    }

    public static String resolveQuestionSend(QuestionEntity question) {
        return "User with id=" + question.getUserId() + " sent question to advisor. Question id=" + question.getQuestionId() + ", status: "
                + (question.getIsRead() ? "Read" : "Unread");
    }

    public static String resolveAddActivity(ActivityEntity activity) {
        return "User with id=" + activity.getUserId() + " added new activity with id=" + activity.getActivityId();
    }

    public static String resolveDownloadActivityAsPdf(Integer userId, Integer month, Integer year, int size) {
        return "User with id=" + userId + " requested activity download for period " + month + "." + year
                + ". Generated pdf file with size=" + size;
    }

    public static String resolveUserInfoUpdate(FitnessUserEntity user) {
        return "Fitness user " + user.getUsername() + " with id=" + user.getUserId()
                + " updated his profile information. Account status: " + (user.getActive() ? "active" : "inactive");
    }

    public static String resolveUserAvatarUpdate(FitnessUserEntity user, String picture) {
        return "Fitness user with id=" + user.getUserId() + " updated his profile picture. Picture saved on server with new name: " + picture;
    }

    public static String resolveUserPasswordChange(FitnessUserEntity user) {
        return "User with id=" + user.getUserId() + " successfully changed his/her password.";
    }
}
