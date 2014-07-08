/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sam_testclient.services;

import java.util.Calendar;
import java.util.Timer;
import java.util.TimerTask;

/**
 *
 * @author Jan
 */
public class HistoricizationJob {
    
    Timer timer;

    public HistoricizationJob() {
    }

    public void d() {
        Calendar date = Calendar.getInstance();
        date.set(Calendar.HOUR_OF_DAY, 0);
        date.set(Calendar.HOUR, 0);
        date.set(Calendar.MINUTE, 0);
        date.set(Calendar.SECOND, 0);
        date.set(Calendar.MILLISECOND, 0);
        timer.schedule(
                new HistTask(),
                date.getTime(),
                1000 * 60 * 60 * 24 * 7
        );
    }
    
    class HistTask extends TimerTask{

        @Override
        public void run() {
            HistoricizationService.doHistoricizationIfItsNeeded();
        }
        
    }

}
