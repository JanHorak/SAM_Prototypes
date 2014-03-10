/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.sam.server.manager;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.nio.file.Path;
import java.util.logging.Level;
import java.util.logging.Logger;
import net.sam.server.entities.MediaStorage;

/**
 *
 * @author janhorak
 */
public class FileManager {

    public static void serialize(Object ob) {
        FileOutputStream foutStream = null;
        try {
            foutStream = new FileOutputStream("data.bin");
        } catch (FileNotFoundException ex) {
            Logger.getLogger(FileManager.class.getName()).log(Level.SEVERE, null, ex);
        }

        ObjectOutputStream obOutStream = null;
        try {
            obOutStream = new ObjectOutputStream(foutStream);
        } catch (IOException ex) {
            Logger.getLogger(FileManager.class.getName()).log(Level.SEVERE, null, ex);
        }
        try {
            obOutStream.writeObject(ob);
        } catch (IOException ex) {
            Logger.getLogger(FileManager.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public static void deserialize(Path path) {
        FileInputStream finStream = null;
        try {
            finStream = new FileInputStream(path.toString());
        } catch (FileNotFoundException ex) {
            Logger.getLogger(FileManager.class.getName()).log(Level.SEVERE, null, ex);
        }

        ObjectInputStream obj_in = null;
        try {
            obj_in = new ObjectInputStream(finStream);
        } catch (IOException ex) {
            Logger.getLogger(FileManager.class.getName()).log(Level.SEVERE, null, ex);
        }

        Object obj = null;
        try {
            obj = obj_in.readObject();
        } catch (IOException ex) {
            Logger.getLogger(FileManager.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(FileManager.class.getName()).log(Level.SEVERE, null, ex);
        }

        if (obj instanceof MediaStorage) {
            MediaStorage media = (MediaStorage) obj;
        }
    }

}
