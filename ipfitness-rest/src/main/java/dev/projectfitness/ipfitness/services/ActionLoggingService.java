package dev.projectfitness.ipfitness.services;

public interface ActionLoggingService {
    void logException(Throwable e);
    void logMessage(String message);
    void logSensitiveAction(String message);
}
