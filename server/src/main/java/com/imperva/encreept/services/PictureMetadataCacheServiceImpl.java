package com.imperva.encreept.services;

import com.imperva.encreept.api.PictureMetadataCacheService;
import com.imperva.encreept.dataStructures.Tree;
import com.imperva.encreept.dataStructures.TreeNode;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

@Service
public class PictureMetadataCacheServiceImpl implements PictureMetadataCacheService {

    Map<String, Tree> cacheMap = new HashMap<>();

    @Override
    public void createPictureMetadata(String md5, String phoneNumber){
        if(cacheMap.containsKey(md5)) {
            cacheMap.remove(md5);
        }
        cacheMap.put(md5, new Tree(new TreeNode(phoneNumber, null, new ArrayList<>())));
    }

    @Override
    public void storePictureMetadata(String md5, String phoneNumbersCommaSeparated) {
        if(!cacheMap.containsKey(md5)) {
            throw new RuntimeException("picture with md5 " + md5 + " NOT exist");
        }
        cacheMap.get(md5).addNode(phoneNumbersCommaSeparated);
    }

    @Override
    public Map<String, Tree> getBlamePath() {
        return cacheMap;
    }

    @Override
    public void clearCache() {
        cacheMap = new HashMap<>();
    }
}
