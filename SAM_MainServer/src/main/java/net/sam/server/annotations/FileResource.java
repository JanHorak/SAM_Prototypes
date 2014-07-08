/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.sam.server.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 *
 * @author janhorak
 */
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface FileResource {

    String path();

    boolean writeable() default true;

    Type kindOfResource() default Type.PROPERTY;

    enum Type {
        PROPERTY, IMAGE, TEXTFILE, XMLFILE
    }
}