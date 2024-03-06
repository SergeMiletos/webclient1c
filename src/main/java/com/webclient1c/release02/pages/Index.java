package com.webclient1c.release02.pages;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.tapestry5.ComponentResources;
import org.apache.tapestry5.EventContext;
import org.apache.tapestry5.SymbolConstants;

import org.apache.tapestry5.annotations.InjectPage;
import org.apache.tapestry5.annotations.Log;
import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.annotations.SessionState;
import org.apache.tapestry5.commons.Messages;

import org.apache.tapestry5.ioc.annotations.Inject;
import org.apache.tapestry5.ioc.annotations.Symbol;

import org.apache.tapestry5.services.HttpError;
import org.apache.tapestry5.services.ajax.AjaxResponseRenderer;
import java.time.ZonedDateTime;
import com.webclient1c.release02.entities.Users1C;

/**
 * Start page of application release02.
 */

public class Index {

    private static final Logger logger = LogManager.getLogger(Index.class);

    @SessionState
    private Users1C loggedUser;
    @SuppressWarnings("unused")
	private boolean loggedUserExists;
    
    @Inject
    private AjaxResponseRenderer ajaxResponseRenderer;

    @Property
    @Inject
    @Symbol(SymbolConstants.TAPESTRY_VERSION)
    private String tapestryVersion;

    @InjectPage
    private About about;
       
   @Inject
   private ComponentResources resources;
   @Inject 
   Messages messages;
   
     
    // Handle call with an unwanted context
    Object onActivate(EventContext eventContext)
    {
    	return eventContext.getCount() > 0 ?
            new HttpError(404, "Resource not found") :
            null;
     }

    @Log
    public Object onComplete()
    {
    	return DynaBeanGridExample.class;
    }

    public String getCurrentTime()
    {
    	return ZonedDateTime.now().toString();
    }
}


