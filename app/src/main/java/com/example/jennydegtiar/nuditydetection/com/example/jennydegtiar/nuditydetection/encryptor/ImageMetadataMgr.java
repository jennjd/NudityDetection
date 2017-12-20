package com.example.jennydegtiar.nuditydetection.com.example.jennydegtiar.nuditydetection.encryptor;

import org.apache.commons.imaging.ImageReadException;
import org.apache.commons.imaging.ImageWriteException;
import org.apache.commons.imaging.Imaging;
import org.apache.commons.imaging.common.IImageMetadata;

import org.apache.commons.imaging.formats.jpeg.JpegImageMetadata;
import org.apache.commons.imaging.formats.jpeg.exif.ExifRewriter;
import org.apache.commons.imaging.formats.tiff.TiffField;
import org.apache.commons.imaging.formats.tiff.TiffImageMetadata;
import org.apache.commons.imaging.formats.tiff.constants.ExifTagConstants;
import org.apache.commons.imaging.formats.tiff.taginfos.TagInfo;
import org.apache.commons.imaging.formats.tiff.write.TiffOutputDirectory;
import org.apache.commons.imaging.formats.tiff.write.TiffOutputSet;
import org.apache.commons.io.FileUtils;

import java.io.*;

/**
 * Created by yonatan.katz on 12/20/2017.
 */
public class ImageMetadataMgr {
    public static void main(String[] args) throws IOException, ImageReadException, ImageWriteException {
        String path = "C:/Users/yonatan.katz/Pictures/1enc1.jpg";
        String outputPath = "C:/Users/yonatan.katz/Pictures/out.jpg";
        File jpegImageFile = new File(path);
        File jpegOutputImageFile = new File(outputPath);
        changeExifMetadata(jpegImageFile, jpegOutputImageFile, "haaaa123aa");
        System.out.println(getUserComment(jpegOutputImageFile));
    }
    public static String getUserComment(final File file) throws ImageReadException,
            IOException {
        // get all metadata stored in EXIF format (ie. from JPEG or TIFF).
        final IImageMetadata metadata = Imaging.getMetadata(file);

        // System.out.println(metadata);

        if (metadata instanceof JpegImageMetadata) {
            final JpegImageMetadata jpegMetadata = (JpegImageMetadata) metadata;

            for (TiffField field : jpegMetadata.getExif().getAllFields())  {
                if (field.getTagName().equalsIgnoreCase("UserComment")) {
                    return field.getStringValue();
                }
            }
        }
        return "";
    }

    private static void printTagValue(final JpegImageMetadata jpegMetadata,
                                      final TagInfo tagInfo) {
        final TiffField field = jpegMetadata.findEXIFValueWithExactMatch(tagInfo);
        if (field == null) {
            System.out.println(tagInfo.name + ": " + "Not Found.");
        } else {
            System.out.println(tagInfo.name + ": "
                    + field.getValueDescription());
        }
    }

    public void removeExifMetadata(final File jpegImageFile, final File dst)
            throws IOException, ImageReadException, ImageWriteException {
        try (FileOutputStream fos = new FileOutputStream(dst);
             OutputStream os = new BufferedOutputStream(fos)) {
            new ExifRewriter().removeExifMetadata(jpegImageFile, os);
        }
    }

    /**
     * This example illustrates how to add/update EXIF metadata in a JPEG file.
     *
     * @param jpegImageFile
     *            A source image file.
     * @param dst
     *            The output file.
     * @throws IOException
     * @throws ImageReadException
     * @throws ImageWriteException
     */
    public static void changeExifMetadata(final File jpegImageFile, final File dst, String data)
            throws IOException, ImageReadException, ImageWriteException {

        try (FileOutputStream fos = new FileOutputStream(dst);
             OutputStream os = new BufferedOutputStream(fos);) {

            TiffOutputSet outputSet = null;

            // note that metadata might be null if no metadata is found.
            final IImageMetadata metadata = Imaging.getMetadata(jpegImageFile);
            final JpegImageMetadata jpegMetadata = (JpegImageMetadata) metadata;
            if (null != jpegMetadata) {
                // note that exif might be null if no Exif metadata is found.
                final TiffImageMetadata exif = jpegMetadata.getExif();

                if (null != exif) {
                    // TiffImageMetadata class is immutable (read-only).
                    // TiffOutputSet class represents the Exif data to write.
                    //
                    // Usually, we want to update existing Exif metadata by
                    // changing
                    // the values of a few fields, or adding a field.
                    // In these cases, it is easiest to use getOutputSet() to
                    // start with a "copy" of the fields read from the image.
                    outputSet = exif.getOutputSet();
                }
            }

            // if file does not contain any exif metadata, we create an empty
            // set of exif metadata. Otherwise, we keep all of the other
            // existing tags.
            if (null == outputSet) {
                outputSet = new TiffOutputSet();
            }

            {
                // Example of how to add a field/tag to the output set.
                //
                // Note that you should first remove the field/tag if it already
                // exists in this directory, or you may end up with duplicate
                // tags. See above.
                //
                // Certain fields/tags are expected in certain Exif directories;
                // Others can occur in more than one directory (and often have a
                // different meaning in different directories).
                //
                // TagInfo constants often contain a description of what
                // directories are associated with a given tag.
                //
                final TiffOutputDirectory exifDirectory = outputSet.getOrCreateExifDirectory();
                // make sure to remove old value if present (this method will
                // not fail if the tag does not exist).
                exifDirectory.removeField(ExifTagConstants.EXIF_TAG_USER_COMMENT);
                exifDirectory.add(ExifTagConstants.EXIF_TAG_USER_COMMENT, data);
            }

            // printTagValue(jpegMetadata, TiffConstants.TIFF_TAG_DATE_TIME);

            new ExifRewriter().updateExifMetadataLossless(jpegImageFile, os,
                    outputSet);
        }
    }

    /**
     * This example illustrates how to remove a tag (if present) from EXIF
     * metadata in a JPEG file.
     *
     * In this case, we remove the "aperture" tag from the EXIF metadata if
     * present.
     *
     * @param jpegImageFile
     *            A source image file.
     * @param dst
     *            The output file.
     * @throws IOException
     * @throws ImageReadException
     * @throws ImageWriteException
     */
    public void removeExifTag(final File jpegImageFile, final File dst) throws IOException,
            ImageReadException, ImageWriteException {
        try (FileOutputStream fos = new FileOutputStream(dst);
             OutputStream os = new BufferedOutputStream(fos)) {
            TiffOutputSet outputSet = null;

            // note that metadata might be null if no metadata is found.
            final IImageMetadata metadata = Imaging.getMetadata(jpegImageFile);
            final JpegImageMetadata jpegMetadata = (JpegImageMetadata) metadata;
            if (null != jpegMetadata) {
                // note that exif might be null if no Exif metadata is found.
                final TiffImageMetadata exif = jpegMetadata.getExif();

                if (null != exif) {
                    // TiffImageMetadata class is immutable (read-only).
                    // TiffOutputSet class represents the Exif data to write.
                    //
                    // Usually, we want to update existing Exif metadata by
                    // changing
                    // the values of a few fields, or adding a field.
                    // In these cases, it is easiest to use getOutputSet() to
                    // start with a "copy" of the fields read from the image.
                    outputSet = exif.getOutputSet();
                }
            }

            if (null == outputSet) {
                // file does not contain any exif metadata. We don't need to
                // update the file; just copy it.
                FileUtils.copyFile(jpegImageFile, dst);
                return;
            }

            {
                // Example of how to remove a single tag/field.
                // There are two ways to do this.

                // Option 1: brute force
                // Note that this approach is crude: Exif data is organized in
                // directories. The same tag/field may appear in more than one
                // directory, and have different meanings in each.
                outputSet.removeField(ExifTagConstants.EXIF_TAG_APERTURE_VALUE);

                // Option 2: precision
                // We know the exact directory the tag should appear in, in this
                // case the "exif" directory.
                // One complicating factor is that in some cases, manufacturers
                // will place the same tag in different directories.
                // To learn which directory a tag appears in, either refer to
                // the constants in ExifTagConstants.java or go to Phil Harvey's
                // EXIF website.
                final TiffOutputDirectory exifDirectory = outputSet.getExifDirectory();
                if (null != exifDirectory) {
                    exifDirectory.removeField(ExifTagConstants.EXIF_TAG_APERTURE_VALUE);
                }
            }

            new ExifRewriter().updateExifMetadataLossless(jpegImageFile, os,
                    outputSet);
        }
    }

}
