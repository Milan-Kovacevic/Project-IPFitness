package dev.projectfitness.ipfitness.models.dtos;

import lombok.Data;

@Data
public class RssNewsItem {
    private String category;
    private String title;
    private String description;
    private String link;
}
