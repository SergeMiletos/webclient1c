package com.webclient1c.release02.pages;

import java.util.Base64;
import java.util.List;


import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import javax.persistence.metamodel.EntityType;
import javax.persistence.metamodel.Metamodel;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.tapestry5.alerts.AlertManager;
import org.apache.tapestry5.annotations.InjectComponent;
import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.annotations.SessionState;
import org.apache.tapestry5.annotations.SetupRender;
import org.apache.tapestry5.corelib.components.Form;
import org.apache.tapestry5.corelib.components.PasswordField;
import org.apache.tapestry5.corelib.components.TextField;
import org.apache.tapestry5.ioc.annotations.Inject;
import org.apache.tapestry5.ioc.annotations.InjectService;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import org.mindrot.jbcrypt.BCrypt;

import com.webclient1c.release02.entities.Users1C;
import com.webclient1c.release02.services.DataSyncService;
import com.webclient1c.release02.services.HibernateSession;
import com.webclient1c.release02.services.StockLevelsSyncService;

public class Login {

    private static final Logger logger = LogManager.getLogger(Login.class);

    @SessionState
    @Property
    private Users1C loggedUser;
    @SuppressWarnings("unused")
    @Property
	private boolean loggedUserExists;
//    @Inject
//    Session session;
    @Inject
    private AlertManager alertManager;

    @InjectComponent
    private Form loginForm;

    @InjectComponent("user1cinput")
    private TextField user1cinputField;

    @InjectComponent("password")
    private PasswordField passwordField;

    @Property
    private String user1cinput;

    @Property
    private String password;

    @InjectService(value = "HibernateSession")
    private HibernateSession hSessionImpl;

    @Inject
    StockLevelsSyncService stockLevelsSyncService;
    @SetupRender
    void setupRender() {
    	if (loggedUserExists && loggedUser != null) {
    		loggedUser = null;
    	}
    }
    
    void onValidateFromLoginForm() {

    	SessionFactory sessionFactory = hSessionImpl.createSessionFactory();
    	Session session = sessionFactory.openSession();
    	session.beginTransaction();
    	
    	CriteriaBuilder cBuilder = session.getCriteriaBuilder();
    	CriteriaQuery<Users1C> criteriaQ = cBuilder.createQuery(Users1C.class);

    	Metamodel m = session.getMetamodel();
    	EntityType<Users1C> modEntUser1C = m.entity(Users1C.class);
    	Root<Users1C> rootOfUsers1C = criteriaQ.from(modEntUser1C);
    	
    	criteriaQ.select(rootOfUsers1C).where(cBuilder.equal(rootOfUsers1C.get("userName"), user1cinput));
    	Query<Users1C> rq = session.createQuery(criteriaQ);
    	
    	List<Users1C> results = rq.getResultList();
 
    	if ( results != null && !results.isEmpty() ) {
    		
    		Users1C foundedUser = results.get(0);
    		
    		// Login credentials initialization
    		if ( foundedUser.getPassword() == null || foundedUser.getPassword().isEmpty() ) {
    			foundedUser.setPassword(BCrypt.hashpw(password, BCrypt.gensalt()));

    			String userpass = foundedUser.getUserName() + ":" + password;
    	        String auth = "Basic " + Base64.getEncoder().encodeToString(userpass.getBytes());
    			foundedUser.setBase64val(auth);

    			session.saveOrUpdate(foundedUser);
    			session.getTransaction().commit();
        		loggedUser = foundedUser;

    		} else if ( BCrypt.checkpw(password, foundedUser.getPassword()) ) {
        		loggedUser = foundedUser;
    		} else {
    			loggedUser = null;
    			loginForm.recordError(passwordField, "Incorrect login.");
    		}
    	}

    	session.close();
    }

    Object onSuccessFromLoginForm() {
        logger.info("Login successful!");
        alertManager.success("Welcome aboard, "+loggedUser.getUserName()+"!");
        return Index.class;
    }

    void onFailureFromLoginForm() {
        logger.warn("Login error!");
        alertManager.error("I'm sorry but I can't log you in!");
    }
}
