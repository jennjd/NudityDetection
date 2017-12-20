package com.imperva.encreept.api;

import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.URISyntaxException;
import java.security.NoSuchAlgorithmException;

public interface PictureClassifierService {

    Double classify(MultipartFile file, String phoneNumber) throws IOException, NoSuchAlgorithmException, URISyntaxException, InterruptedException;
}
