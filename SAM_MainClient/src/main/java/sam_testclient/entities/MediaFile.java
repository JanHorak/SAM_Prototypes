/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package sam_testclient.entities;

import java.io.File;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.persistence.Entity;
import sam_testclient.enums.EnumMediaType;

/**
 *
 * @author janhorak
 */

public class MediaFile extends MediaStorage{
    
    public static EnumMediaType getEnumTypeOfFile(File f){
        Pattern pattern_image = Pattern.compile("([^\\s]+(\\.(?i)(jpg|png|gif|bmp))$)");
        Pattern pattern_video = Pattern.compile("([^\\s]+(\\.(?i)(avi|wmv|mov|flv))$)");
        Matcher matcher_i = pattern_image.matcher(f.getName());
        Matcher matcher_v = pattern_video.matcher(f.getName());
        if (matcher_i.matches()){
            return EnumMediaType.IMAGE;
        }
        if (matcher_v.matches()){
            return EnumMediaType.VIDEO;
        }
        return null;
    }
    
    
}
