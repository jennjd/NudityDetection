package com.imperva.encreept.api;

import com.imperva.encreept.dataStructures.Tree;

import java.util.Map;

public interface PictureMetadataCacheService {

    void createPictureMetadata(String md5, String phoneNumber);

    void storePictureMetadata(String md5, String phoneNumbersCommaSeparated);

    Map<String, Tree> getBlamePath();

    void clearCache();
}
