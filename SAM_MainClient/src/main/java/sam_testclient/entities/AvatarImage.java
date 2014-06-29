/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package sam_testclient.entities;

import javax.swing.ImageIcon;
import sam_testclient.validations.Avatar;


/**
 * Validateable class for the Avatar
 * @author janhorak
 */
@Avatar
public class AvatarImage extends ImageIcon{
    
    public AvatarImage(String path){
        super(path);
    }
    
}
