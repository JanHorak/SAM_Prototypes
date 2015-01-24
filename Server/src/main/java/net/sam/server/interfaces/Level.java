/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.sam.server.interfaces;

/**
 *
 * @author janhorak
 */
public interface Level {

    public int LOW = 1;

    public int MIDDLE = 2;

    public int HIGH = 3;

    public void increaseLevel();

    public void decreaseLevel();

    public int getCurrentLevel();

}
