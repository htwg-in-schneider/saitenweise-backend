package de.htwg.in.schneider.saitenweise.backend.controller.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class AiSuggestionResponse {
    private String musicStyle;
    private String recommendedProducts;

    public String getMusicStyle() {
        return musicStyle;
    }

    public void setMusicStyle(String musicStyle) {
        this.musicStyle = musicStyle;
    }

    public String getRecommendedProducts() {
        return recommendedProducts;
    }

    public void setRecommendedProducts(String recommendedProducts) {
        this.recommendedProducts = recommendedProducts;
    }
}
