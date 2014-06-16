/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package net.sam.server.services;

import net.sam.server.utilities.Utilities;
import org.apache.deltaspike.cdise.api.CdiContainer;
import org.apache.deltaspike.cdise.api.CdiContainerLoader;
import org.apache.deltaspike.cdise.api.ContextControl;
import org.apache.deltaspike.core.api.provider.BeanProvider;
import org.apache.log4j.Logger;

/**
 *
 * @author janhorak
 */
public class ContainerService {
    
    private CdiContainer cdiContainer;
    private ContextControl contextControl;
    
    private static Logger logger;
    
    public ContainerService(){
        logger = Logger.getLogger(ContainerService.class);
    }

    /**
     * This method starts the Deltaspike- Container and the Contexts.
     */
    public void startContainer() {
        cdiContainer = CdiContainerLoader.getCdiContainer();
        cdiContainer.boot();
        contextControl = cdiContainer.getContextControl();
        logger.info(Utilities.getLogTime() + " Deltaspike- Container is started...");
        contextControl.startContexts();
    }
    
    public void shutDown(){
        contextControl.stopContexts();
        cdiContainer.shutdown();
        logger.info(Utilities.getLogTime() + " Deltaspike- Container is ended");
    }
    
    /**
     * This method will return a Bean which is in the container.
     * If the object is not in the context, it will be added and returned next 
     * time (in dependency to the annotation and the Scope)
     * @param <T>
     * @param clazz
     * @return Object of kind <T>
     */
    public static <T extends Object> T getBean(Class<T> clazz){
        Object o = BeanProvider.getContextualReference(clazz, true);
        return (T) o;
    }
    
}
