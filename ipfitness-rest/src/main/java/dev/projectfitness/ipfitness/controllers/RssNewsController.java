package dev.projectfitness.ipfitness.controllers;

import dev.projectfitness.ipfitness.models.dtos.RssNews;
import dev.projectfitness.ipfitness.services.RssNewsService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("${ipfitness.base-url}/news")
public class RssNewsController {
    private final RssNewsService rssNewsService;

    @GetMapping
    public RssNews getRssFitnessNews(){
        return rssNewsService.getAllFitnessNews();
    }
}
