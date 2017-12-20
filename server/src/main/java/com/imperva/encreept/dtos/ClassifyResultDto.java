package com.imperva.encreept.dtos;

public class ClassifyResultDto {
    private Double score;

    public ClassifyResultDto(Double score) {
        this.score = score;
    }

    public Double getScore() {
        return score;
    }

    public void setScore(Double score) {
        this.score = score;
    }
}
