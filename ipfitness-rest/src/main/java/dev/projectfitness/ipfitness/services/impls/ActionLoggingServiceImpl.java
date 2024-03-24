package dev.projectfitness.ipfitness.services.impls;

import dev.projectfitness.ipfitness.models.entities.UserActionEntity;
import dev.projectfitness.ipfitness.models.enums.ActionSeverity;
import dev.projectfitness.ipfitness.repositories.UserActionEntityRepository;
import dev.projectfitness.ipfitness.services.ActionLoggingService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;

import java.util.Date;

import static dev.projectfitness.ipfitness.util.Constants.ENDPOINT_SERVLET_ATTRIBUTE;
import static dev.projectfitness.ipfitness.util.Constants.IPADDRESS_SERVLET_ATTRIBUTE;

@Service
@RequiredArgsConstructor
public class ActionLoggingServiceImpl implements ActionLoggingService {
    private final UserActionEntityRepository userActionEntityRepository;
    @Value("${ipfitness.logging.severity}")
    private Integer level;

    @Override
    public void logException(Throwable e) {
        StringBuilder builder = new StringBuilder();
        builder.append(e);
        builder.append(System.lineSeparator());
        for (StackTraceElement element : e.getStackTrace()) {
            builder.append(element);
            builder.append(System.lineSeparator());
        }
        logActionInternal(builder.toString(), ActionSeverity.EXCEPTION);
    }

    @Override
    public void logMessage(String message) {
        logActionInternal(message, ActionSeverity.INFO);
    }

    @Override
    public void logSensitiveAction(String message) {
        logActionInternal(message, ActionSeverity.SENSITIVE);
    }

    private void logActionInternal(String message, ActionSeverity severity) {
        if (severity.getLevel() < level)
            return;

        String endpoint = (String) RequestContextHolder.currentRequestAttributes().getAttribute(ENDPOINT_SERVLET_ATTRIBUTE, RequestAttributes.SCOPE_REQUEST);
        String ipAddress = (String) RequestContextHolder.currentRequestAttributes().getAttribute(IPADDRESS_SERVLET_ATTRIBUTE, RequestAttributes.SCOPE_REQUEST);
        Date currentTime = new Date();

        UserActionEntity action = new UserActionEntity();
        action.setActionTime(currentTime);
        action.setEndpoint(endpoint);
        action.setIpAddress(ipAddress);
        action.setMessage(message);
        action.setSeverity(severity);
        userActionEntityRepository.saveAndFlush(action);
    }
}
