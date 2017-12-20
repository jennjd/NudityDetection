package com.imperva.encreept.dtos;

import com.imperva.encreept.dataStructures.Tree;

import java.util.Map;

public class BlameReportDto {

    private Map<String, Tree> blamePath;

    public BlameReportDto(Map<String, Tree> blamePath) {
        this.blamePath = blamePath;
    }

    public Map<String, Tree> getBlamePath() {
        return blamePath;
    }

    public void setBlamePath(Map<String, Tree> blamePath) {
        this.blamePath = blamePath;
    }
}
