package dev.projectfitness.ipfitness.models.dtos;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class RssNews {
    String title;
    String link;
    String image;
    String description;
    String copyright;
    List<RssNewsItem> items = new ArrayList<>();
}
