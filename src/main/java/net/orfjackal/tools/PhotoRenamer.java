/*
 * Copyright (c) 2005, Esko Luontola. All Rights Reserved.
 *
 * Redistribution and use in source and binary forms, with or without modification,
 * are permitted provided that the following conditions are met:
 *
 *     * Redistributions of source code must retain the above copyright notice,
 *       this list of conditions and the following disclaimer.
 *     * Redistributions in binary form must reproduce the above copyright notice,
 *       this list of conditions and the following disclaimer in the documentation
 *       and/or other materials provided with the distribution.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND
 * ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED
 * WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 * DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE LIABLE FOR
 * ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
 * (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES;
 * LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON
 * ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
 * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */

package net.orfjackal.tools;

import com.drew.imaging.jpeg.JpegMetadataReader;
import com.drew.imaging.jpeg.JpegProcessingException;
import com.drew.metadata.Directory;
import com.drew.metadata.Metadata;
import com.drew.metadata.exif.ExifDirectory;

import java.io.File;
import java.io.FileFilter;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Comparator;
import java.util.GregorianCalendar;

/**
 * Simple rename script for renaming photos taken with a digital camera.
 *
 * @author Esko Luontola
 * @since 12.9.2005
 */
public class PhotoRenamer {

    private int number;

    private File dir;

    private SimpleDateFormat datetimeFormat = new SimpleDateFormat("yyyy:MM:dd HH:mm:ss");
    private SimpleDateFormat outputFormat = new SimpleDateFormat("yyyy-MM-dd HHmm");
    private int digits = 3;

    public PhotoRenamer(File dir) {
        this.number = 0;
        this.dir = dir;
    }

    private File[] getFiles() {
        File[] files = dir.listFiles(new FileFilter() {
            public boolean accept(File pathname) {
                String s = pathname.getName().toLowerCase();
                return (s.endsWith(".jpg") || s.endsWith(".jpeg"));
            }
        });
        digits = Math.max(digits, Integer.toString(files.length).length());

        Arrays.sort(files, new Comparator<File>() {
            public int compare(File o1, File o2) {
                int i = (int) (getPictureTakenTime(o1) - getPictureTakenTime(o2));
                if (i == 0) {
                    i = o1.compareTo(o2);
                }
                return i;
            }
        });
        return files;
    }

    private long getPictureTakenTime(File file) {
        long result = 0;
        try {
            Metadata metadata = JpegMetadataReader.readMetadata(file);
            Directory exifDirectory = metadata.getDirectory(ExifDirectory.class);
            String datetime = exifDirectory.getString(ExifDirectory.TAG_DATETIME_ORIGINAL);
            result = datetimeFormat.parse(datetime).getTime();
        } catch (JpegProcessingException e) {
            System.err.println(file);
            e.printStackTrace();
        } catch (ParseException e) {
            System.err.println(file);
            e.printStackTrace();
        }
        return result;
    }

    /**
     * Returns a new name for the given JPEG file. This method may be called only once for each file in the numbering
     * order, or the numbering sequence will get messed up.
     */
    private String getNewName(File file) {
        number++;
        Calendar mod = new GregorianCalendar();
        mod.setTimeInMillis(getPictureTakenTime(file));
        return "Strängnäs (" + outputFormat.format(mod.getTime()) + ") " + pad(number, digits) + ".jpg";
    }

    private static String pad(int n, int chars) {
        String s = Integer.toString(n);
        while (s.length() < chars) {
            s = "0" + s;
        }
        return s;
    }

    public void preview() {
        System.out.println("Preview:");
        File[] files = getFiles();
        for (File file : files) {
            System.out.println(file.getName() + " -> " + getNewName(file));
        }
    }

    public void rename() {
        System.out.println("Rename:");
        File[] files = getFiles();
        if (files.length == 0) {
            return;
        }

        // create a output directory that does not already exist to avoid overwriting files
        File outDir = new File(this.dir, "Renamed");
        int i = 1;
        while (outDir.exists()) {
            outDir = new File(this.dir, "Renamed-" + i);
            i++;
        }
        if (!outDir.mkdir()) {
            System.out.println("Unable to create directory " + outDir.getAbsolutePath());
        }

        // rename the files and move them to the output directory
        for (File file : files) {
            String newName = getNewName(file);
            System.out.print(file.getName() + " -> " + newName);
            File dest = new File(outDir, newName);
            if (!file.renameTo(dest)) {
                System.out.print("\tFAILED!");
            }
            System.out.println();
        }
    }

    public static void main(String[] args) {
        PhotoRenamer r = new PhotoRenamer(new File("E:\\Komposti\\Grafiikka\\_DIGIKUVAT_\\Strängnäs 2005"));
        r.preview();
        //r.rename();
    }
}
