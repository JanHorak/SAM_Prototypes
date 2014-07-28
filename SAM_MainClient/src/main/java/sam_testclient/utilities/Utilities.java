/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sam_testclient.utilities;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.LineNumberReader;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.logging.Level;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;
import org.apache.log4j.Logger;
import sun.audio.AudioPlayer;
import sun.audio.AudioStream;

/**
 *
 * @author janhorak
 */
public class Utilities {

    private static Logger logger = Logger.getLogger(Utilities.class);

    /**
     * Builds and returns the MD5- Hash of the incoming String.
     *
     * @param input
     * @return Hashed String
     */
    public static String getHash(String input) {
        MessageDigest md = null;
        try {
            md = MessageDigest.getInstance("MD5");
            md.update(input.getBytes());
        } catch (NoSuchAlgorithmException ex) {
            System.err.println("Error with Hash");
        }
        byte byteData[] = md.digest();

        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < byteData.length; i++) {
            sb.append(Integer.toString((byteData[i] & 0xff) + 0x100, 16).substring(1));
        }
        return sb.toString();
    }

    public static String getLogTime() {
        return "[" + new Date().toString() + "]";
    }

    public static String getTime() {
        SimpleDateFormat sm = new SimpleDateFormat("HH:mm");
        return sm.format(new Date());
    }

    public static Map<Integer, String> getOnlineMap(String input) {
        Map<Integer, String> map = new HashMap<Integer, String>();
        List<String> statusList = new ArrayList<>();
        input = input.substring(1, input.length() - 1);
        String[] cleaned = input.split(", ");
        for (int i = 0; i < cleaned.length; i++){
            statusList.add(cleaned[i]);
        }
        
        for (String status : statusList){
            String[] content = status.split(",");
            String[] availability = content[0].split("=");
            String id = availability[0];
            String date = "";
            String active = availability[1].split(" ")[0];
            StringBuilder sb = new StringBuilder();
            if (active.equals("false")){
                sb.append(availability[1].split(" ")[1]);
                sb.append(" "+availability[1].split(" ")[2]);
                date = sb.toString();
            }
            System.out.println(id);
            System.out.println(active);
            System.out.println(date);
            map.put(Integer.decode(id), active + " " + date);
        }
        System.out.println(map);
        return map;
    }

    /**
     * Generates a random UUID for the Secret
     *
     * @return UUID
     */
    public static UUID generateRandomUUID() {
        return UUID.randomUUID();
    }

    /**
     * Generates a random UUID and returns the cleaned String. Removes all
     * letters and sonder symbols from the UUID
     *
     * @return cleaned UUID
     */
    public static String generateNewCleanedUUID() {
        return generateRandomUUID().toString().replaceAll("[^\\d.]", "");
    }

    /**
     * Returns a incoming String from letters and sonder symbols.
     *
     * @param uuid (Type String)
     * @return cleaned String
     */
    public static String generateCleanedUUID(String uuid) {
        return uuid.replaceAll("[^\\d.]", "");
    }

    /**
     * Returns a random int in a passed range
     *
     * @param min
     * @param max
     * @return random int in range
     */
    public static int generateRandomNumberBetween(int min, int max) {
        return (int) (min + (Math.random() * (max - min)));
    }

    /**
     * Calculates the Secret.
     *
     * @param secret
     * @return Result of the calculation
     */
    public static String calculateSecret(String secret) {
        String cleaned[] = secret.split(" ");
        String cleanedUUID = generateCleanedUUID(cleaned[0]);
        String result = "";
        String number = cleaned[1];
        for (int i = 0; i < cleanedUUID.length(); i++) {
            int c = cleanedUUID.charAt(i);
            result += String.valueOf(c % Integer.valueOf(number));
        }
        return result;
    }

    /**
     * Compression of a List of Pathes.
     *
     * @param pathOfZip Path of the outcoming Zip- File
     * @param filePathes List of pathes of files
     */
    public static void generateZip(String pathOfZip, List<String> filePathes) {
        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(pathOfZip);
        } catch (FileNotFoundException ex) {
            logger.error("Utilities: generateZip- failed:", ex);
        }
        ZipOutputStream zos = new ZipOutputStream(fos);

        for (String filePath : filePathes) {
            File file = new File(filePath);
            ZipEntry ze = new ZipEntry(file.getName());
            try {
                zos.putNextEntry(ze);
            } catch (IOException ex) {
                logger.error("Cant create Zip- Entry", ex);
            }
            FileInputStream fis = null;
            try {
                fis = new FileInputStream(file.getAbsoluteFile());
            } catch (FileNotFoundException ex) {
                logger.error("Utilities: generateZip- File not found: " + file, ex);
            }
            byte[] buffer = new byte[2048];
            int len;
            try {
                while ((len = fis.read(buffer)) > 0) {
                    try {
                        zos.write(buffer, 0, len);
                    } catch (IOException ex) {
                        logger.error("Utilities: generateZip- ZipOutputStream cannot write", ex);
                    }
                }
            } catch (IOException ex) {
                logger.error("Utilities: generateZip- Problems with Buffer- Length", ex);
            }
            try {
                zos.closeEntry();
                fis.close();
            } catch (IOException ex) {
                logger.error("Utilities: generateZip- Cannot close Streams (SubStreams):", ex);
            }
        }
        try {
            zos.close();
            fos.close();
        } catch (IOException ex) {
            logger.error("Utilities: generateZip- Cannot close Streams (MainStreams):", ex);
        }
    }

    /**
     * Decompression of passed zipfile.
     *
     * @param zipFile
     * @param pathForUnzip
     */
    public static void decompressZip(File zipFile, String pathForUnzip) {
        byte[] buffer = new byte[1024];
        ZipInputStream zis = null;
        try {
            File folder = new File(pathForUnzip);
            if (!folder.exists()) {
                folder.mkdir();
            }
            zis = new ZipInputStream(new FileInputStream(zipFile));
            ZipEntry ze = zis.getNextEntry();

            while (ze != null) {
                String fileName = ze.getName();
                File newFile = new File(pathForUnzip + File.separator + fileName);
                new File(newFile.getParent()).mkdirs();
                FileOutputStream fos = new FileOutputStream(newFile);
                int len;
                while ((len = zis.read(buffer)) > 0) {
                    fos.write(buffer, 0, len);
                }
                fos.close();
                ze = zis.getNextEntry();
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        } finally {
            try {
                zis.closeEntry();
                zis.close();
            } catch (IOException ex) {
                logger.error("IOException: " + ex);
            }
        }
    }

    /**
     * Writes a passed content to a file at the passed path.
     *
     * @param path path of File
     * @param content
     * @param append if append is true, the will put the content and the end of
     * the file. if append is false, it will replace the whole content of the
     * aimed file.
     */
    public static void writeContentToFile(String path, String content, boolean append) {
        File file = new File(path);
        if (!file.exists()) {
            try {
                file.createNewFile();
            } catch (IOException ex) {
                java.util.logging.Logger.getLogger(Utilities.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        FileWriter fw = null;
        try {
            fw = new FileWriter(file.getAbsoluteFile(), append);
        } catch (IOException ex) {
            java.util.logging.Logger.getLogger(Utilities.class.getName()).log(Level.SEVERE, null, ex);
        }
        BufferedWriter bw = new BufferedWriter(fw);
        try {
            bw.write("\n" + content);
        } catch (IOException ex) {
            java.util.logging.Logger.getLogger(Utilities.class.getName()).log(Level.SEVERE, null, ex);
        }
        try {
            bw.close();
        } catch (IOException ex) {
            java.util.logging.Logger.getLogger(Utilities.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * Returns the logfile in a list of files
     *
     * @param folder
     * @param endsWithFilterRegEx
     * @return logfile
     */
    public static String getlogFileFromFolder(File[] folder, final String endsWithFilterRegEx) {
        File result = null;
        for (int i = 0; i < folder.length; i++) {
            File f = folder[i];
            if (f.getAbsolutePath().endsWith("log")) {
                result = f;
                return result.getAbsolutePath();
            }
        }
        return "";
    }

    public static String getContentOfLastLogFileByBuddyName(String buddy) {
        File[] folder = getFilesOfPath("resources/buddies/" + buddy + "/history");
        if (folder == null) {
            return "";
        }
        String logfile = getlogFileFromFolder(folder, "log");
        if (!new File(logfile).exists()) {
            return "";
        } else {
            String content = readSinceLineInFile(9, logfile);
            return content;
        }
    }

    /**
     * Returns the size of a file
     *
     * @param path
     * @return size (in Byte)
     */
    public static long getSizeOfFileContent(String path) {
        return new File(path).length();
    }

    /**
     * Returns all files in a passed path
     *
     * @param path
     * @return File- array
     */
    public static File[] getFilesOfPath(String path) {
        String pathToFolder = path;
        File folderFile = new File(pathToFolder);
        pathToFolder = folderFile.getAbsolutePath();
        File dir = new File(pathToFolder);
        return dir.listFiles();
    }

    /**
     * Returns a specific line in a passed path of file
     *
     * @param goalLineNumber
     * @param path
     * @return line
     */
    public static String readLineInFile(int goalLineNumber, String path) {
        File f = new File(path);
        BufferedReader br = null;
        try {
            br = new BufferedReader(new FileReader(f));
        } catch (FileNotFoundException ex) {
            java.util.logging.Logger.getLogger(Utilities.class.getName()).log(Level.SEVERE, null, ex);
        }
        try (LineNumberReader lnr = new LineNumberReader(br)) {
            String line = null;
            int linecounter = 0;
            int lnum = 0;
            while ((line = lnr.readLine()) != null) {
                if (linecounter == goalLineNumber) {
                    return line;
                }
                linecounter++;
            }
        } catch (IOException ex) {
            java.util.logging.Logger.getLogger(Utilities.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    /**
     * Returns a specific line in a passed path of file
     *
     * @param goalLineNumber
     * @param path
     * @return line
     */
    public static String readSinceLineInFile(int sinceLineNumber, String path) {
        File f = new File(path);
        String result = "";
        BufferedReader br = null;
        try {
            br = new BufferedReader(new FileReader(f));
        } catch (FileNotFoundException ex) {
            java.util.logging.Logger.getLogger(Utilities.class.getName()).log(Level.SEVERE, null, ex);
        }
        try (LineNumberReader lnr = new LineNumberReader(br)) {
            String line = null;

            int linecounter = 0;
            int lnum = 0;
            while ((line = lnr.readLine()) != null) {
                if (linecounter >= sinceLineNumber) {
                    result += "\n" + line;
                }
                linecounter++;
            }
        } catch (IOException ex) {
            java.util.logging.Logger.getLogger(Utilities.class.getName()).log(Level.SEVERE, null, ex);
        }
        return result;
    }

    public static void playSound(String path) {
        InputStream in = null;
        try {
            in = new FileInputStream(path);
        } catch (FileNotFoundException ex) {
            java.util.logging.Logger.getLogger(Utilities.class.getName()).log(Level.SEVERE, null, ex);
        }
        AudioStream audioStream = null;
        try {
            audioStream = new AudioStream(in);
        } catch (IOException ex) {
            java.util.logging.Logger.getLogger(Utilities.class.getName()).log(Level.SEVERE, null, ex);
        }
        AudioPlayer.player.start(audioStream);
    }

    public static byte[] combineBytes(List<byte[]> bytes) {
        byte[] result;
        int l = 0;
        for (byte[] b : bytes) {
            l += b.length;
        }
        result = new byte[l];
        l = 0;
        for (byte[] b : bytes) {
            for (int i = 0; i < b.length; i++) {
                result[l] = b[i];
            }
        }

        return result;
    }

}
