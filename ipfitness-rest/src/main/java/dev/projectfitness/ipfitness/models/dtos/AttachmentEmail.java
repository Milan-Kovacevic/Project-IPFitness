package dev.projectfitness.ipfitness.models.dtos;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Data
public class AttachmentEmail{
    private String emailFrom;
    private String sendTo;
    private String subject;
    private String body;
    private boolean isHtml;
    private List<MultipartFile> attachmentFiles;
}
