/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sam_testclient.sources;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.nio.file.Files;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author janhorak
 */
public class FileManager {

    public static void serialize(Object ob, String path) {
        FileOutputStream f_out = null;
        try {
            f_out = new FileOutputStream(path);

            ObjectOutputStream obj_out = new ObjectOutputStream(f_out);

            obj_out.writeObject(ob);
        } catch (FileNotFoundException ex) {
            Logger.getLogger(FileManager.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(FileManager.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                f_out.close();
            } catch (IOException ex) {
                Logger.getLogger(FileManager.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    public static Object deserialize(String path) {
        FileInputStream f_in = null;
        Object result = null;
        try {
            FileInputStream fis = null;

            f_in = new FileInputStream(path);

            ObjectInputStream obj_in
                    = new ObjectInputStream(f_in);

            result = obj_in.readObject();
        } catch (FileNotFoundException ex) {
            Logger.getLogger(FileManager.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(FileManager.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(FileManager.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                f_in.close();
            } catch (IOException ex) {
                Logger.getLogger(FileManager.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return result;
    }
    
    public static byte[] returnBytesOfFile(File file){
        try {
            return Files.readAllBytes(file.toPath());
        } catch (IOException ex) {
            Logger.getLogger(FileManager.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }
}
