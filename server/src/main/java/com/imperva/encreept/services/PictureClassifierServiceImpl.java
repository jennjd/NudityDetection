package com.imperva.encreept.services;

import com.imperva.encreept.api.PictureClassifierService;
import com.imperva.encreept.api.PictureMetadataCacheService;
import org.apache.commons.codec.binary.Hex;
import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URISyntaxException;
import java.nio.file.*;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Date;

@Service
public class PictureClassifierServiceImpl implements PictureClassifierService {

    private static final Logger LOGGER = LoggerFactory.getLogger(PictureClassifierServiceImpl.class);

//    public static final String[] COMMAND = {"cmd", "/c", "docker run --volume=C:\\Hacathon_2017\\open_nsfw:/workspace caffe:cpu python ./classify_nsfw.py --model_def nsfw_model/deploy.prototxt --pretrained_model nsfw_model/resnet_50_1by2_nsfw.caffemodel"};
    public static final String picturePath = "./";
    public static final String pictureFileName = "test_image.jpg";
    public static final String pictureFullPath = picturePath + pictureFileName;
    private String resultFilePath = "./NSFW_result.txt";

    @Autowired
    private PictureMetadataCacheService pictureMetadataCacheService;

    @Override

    public synchronized Double classify(MultipartFile file, String phoneNumber) throws IOException, NoSuchAlgorithmException, URISyntaxException, InterruptedException {
        storeFile(file);
        cachePictureMetadata(phoneNumber);
        return classifyPicture();
    }

    private void cachePictureMetadata(String phoneNumber) throws IOException, NoSuchAlgorithmException {
        String pictureMd5 = calcFileMd5(pictureFullPath);
        pictureMetadataCacheService.createPictureMetadata(pictureMd5, phoneNumber);
    }

    private Double classifyPicture() throws IOException, URISyntaxException, InterruptedException {
        LOGGER.info("##############################################################################################");
        Date startDate = new Date();
//        Process dockerCommand;
        Double result = null;

        File resultFile = new File(resultFilePath);
        File testPictureFile = new File(pictureFullPath);
        if(resultFile.exists()) {
            Files.deleteIfExists(resultFile.toPath());
        }

//        ProcessBuilder builder = new ProcessBuilder(COMMAND);
//        builder.redirectErrorStream(true);
//        builder.redirectOutput(ProcessBuilder.Redirect.PIPE);
//        builder.redirectError(ProcessBuilder.Redirect.INHERIT);
//        dockerCommand = builder.start();

//        BufferedReader reader = new BufferedReader(new InputStreamReader(dockerCommand.getInputStream()));

//        String line = reader.readLine();
//        while (line != null) {
//            line = reader.readLine();
//            if(line!=null) {
////                LOGGER.info(line);
//                if(line.contains("NSFW score")) {
//                    String tempParseLine = line.replaceAll("NSFW score:","").replaceAll("\\s","");
//                    result = Double.valueOf(tempParseLine);
//                    LOGGER.info("Result: " + result);
//                }
//            }
//        }
        while(!resultFile.exists() || testPictureFile.exists()) {
            Thread.sleep(100);
        }

        String resultStr = FileUtils.readFileToString(resultFile);
        result = Double.valueOf(resultStr);
        LOGGER.info("Result: " + result);

        if(resultFile.exists()) {
            Files.deleteIfExists(resultFile.toPath());
        }

        Date endDate = new Date();
        LOGGER.info("Classification took {} seconds ", (endDate.getTime()-startDate.getTime())/1000);
        LOGGER.info("##############################################################################################");
        return result;
    }

    private void storeFile(MultipartFile file) throws IOException {
        String originalFilename = file.getOriginalFilename();
        if (file.isEmpty()) {
            LOGGER.error("Failed to storeFile empty file " + originalFilename);
            throw new RuntimeException("File is empty");
        }
        Path pictureFileFullPath = Paths.get(picturePath).resolve(pictureFileName);
        copyFile(file, pictureFileFullPath);
        LOGGER.info("Uploaded File: {}, stored successfully", originalFilename);
    }

    private void copyFile(MultipartFile file, Path pictureFilePath) throws IOException {
        try {
            Files.copy(file.getInputStream(), pictureFilePath, StandardCopyOption.REPLACE_EXISTING);
        } catch (FileAlreadyExistsException e) {
            LOGGER.info("Uploaded file already exists");
        }
    }

    private String calcFileMd5(String filePath) throws IOException, NoSuchAlgorithmException {
        MessageDigest md = MessageDigest.getInstance("MD5");
        FileInputStream is = new FileInputStream(filePath);
        String digest = getDigest(is, md, 2048);
        is.close();
        return digest;
    }

    public static String getDigest(InputStream is, MessageDigest md, int byteArraySize)
            throws NoSuchAlgorithmException, IOException {

        md.reset();
        byte[] bytes = new byte[byteArraySize];
        int numBytes;
        while ((numBytes = is.read(bytes)) != -1) {
            md.update(bytes, 0, numBytes);
        }
        byte[] digest = md.digest();
        String result = new String(Hex.encodeHex(digest));
        return result;
    }

}
