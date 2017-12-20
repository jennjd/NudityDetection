package com.imperva.encreept.controllers;

import com.imperva.encreept.api.PictureClassifierService;
import com.imperva.encreept.api.PictureMetadataCacheService;
import com.imperva.encreept.dtos.BlameReportDto;
import com.imperva.encreept.dtos.ClassifyResultDto;
import com.imperva.encreept.dataStructures.Tree;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.URISyntaxException;
import java.security.NoSuchAlgorithmException;
import java.util.Map;

@RestController
@RequestMapping("classify")
public class MainController {

    private static final Logger LOGGER = LoggerFactory.getLogger(MainController.class);

    @Autowired
    private PictureClassifierService pictureClassifierService;

    @Autowired
    private PictureMetadataCacheService pictureMetadataCacheService;

    @RequestMapping(value = "/test", method = RequestMethod.GET, produces = "application/json")
    public ResponseEntity<?> test() throws IOException {
        return new ResponseEntity<>("En-Creep-T", HttpStatus.OK);
    }

    @RequestMapping(value = "/", method = RequestMethod.POST, produces = "application/json")
    public ResponseEntity<?> handleFileUpload(@RequestParam("file") MultipartFile file, @RequestParam("phoneNumber") String phoneNumber) throws IOException, NoSuchAlgorithmException, URISyntaxException, InterruptedException {
        LOGGER.info("Uploading picture with name: " + file.getOriginalFilename());
        Double result = pictureClassifierService.classify(file, phoneNumber);
        return new ResponseEntity<>(new ClassifyResultDto(result), HttpStatus.OK);
    }

    @RequestMapping(value = "/report", method = RequestMethod.GET, produces = "application/json")
    public ResponseEntity<?> reportDecryption(@RequestParam("md5") String md5, @RequestParam("phoneNumbers") String phoneNumbersCommaSeparated) {
        LOGGER.info("Recieved Report for Blame path for picture {} is: {}" ,md5, phoneNumbersCommaSeparated);
        pictureMetadataCacheService.storePictureMetadata(md5, phoneNumbersCommaSeparated);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @RequestMapping(value = "/get-blame-path", method = RequestMethod.GET, produces = "application/json")
    public ResponseEntity<?> getBlamePath() {
        Map<String, Tree> blamePath = pictureMetadataCacheService.getBlamePath();
        return new ResponseEntity<>(new BlameReportDto(blamePath), HttpStatus.OK);
    }

    @RequestMapping(value = "/", method = RequestMethod.DELETE, produces = "application/json")
    public ResponseEntity<?> clearCache() {
        pictureMetadataCacheService.clearCache();
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
