/*
 * MyApplication.java
 *
 * Created on September 20, 2011, 2:35 PM
 */
package netention.ui;

import com.google.common.base.Predicate;
import com.google.common.collect.Collections2;
import com.vaadin.Application;
import com.vaadin.ui.*;
import com.vaadin.ui.LoginForm.LoginEvent;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import netention.Community;
import netention.Detail;
import netention.Message;
import netention.NType;
import netention.Property;
import netention.Self;
import netention.test.ExampleCommunity;
import org.vaadin.appfoundation.authentication.SessionHandler;
import org.vaadin.appfoundation.authentication.util.PasswordUtil;
import org.vaadin.appfoundation.authorization.Permissions;
import org.vaadin.appfoundation.authorization.jpa.JPAPermissionManager;
import org.vaadin.appfoundation.i18n.Lang;
import org.vaadin.appfoundation.persistence.facade.FacadeFactory;
import org.vaadin.appfoundation.persistence.facade.IFacade;
import org.vaadin.appfoundation.view.ViewHandler;

/** 
 *
 * @author me
 * @version 
 */
public class NApplication extends Application implements Community {
    private IFacade db;
    private Community community;

    public NApplication() {
        super();


//        if (schema == null) {
//            schema = new Schema();
//            SeedSelfBuilder.build(schema);
//
//            db.store(schema);
//        }
    }

    public Community getCommunity() {
        return community;
    }

    //Currently authenticated self, or null if not
    public Self getSelf() {
        return (Self)SessionHandler.get();
    }

    @Override
    public void init() {
        SessionHandler.initialize(this);
        ViewHandler.initialize(this);
        Lang.initialize(this);
        Permissions.initialize(this, new JPAPermissionManager());

        setTheme("netention");

        db = FacadeFactory.getFacade();
        
        community = new ExampleCommunity();
        AppWindow mainWindow = new AppWindow(this, community, "id");

        setMainWindow(mainWindow);

//        getMainWindow().addURIHandler(new URIHandler() {
//
//            @Override
//            public DownloadStream handleURI(URL context, String relativeUri) {
//                System.out.println(relativeUri);
//                return null;
//            }
//            
//        });
    }
    

    //TODO extract LoginWindow class
    public void login(final AppWindow browser) {
        //show a dialog window w/ login form
          // Create the window...
        final Window loginWindow = new Window("Login");
        // ...and make it modal
        loginWindow.setModal(true);
        loginWindow.setWidth("25%");
        loginWindow.setHeight("50%");

        // Configure the windws layout; by default a VerticalLayout
        VerticalLayout content = (VerticalLayout) loginWindow.getContent();
        content.setMargin(true);
        content.setSpacing(true);
               
        LoginForm login = new LoginForm();
        login.setWidth("100%");
        login.setHeight("300px");
        login.addListener(new LoginForm.LoginListener() {
            public void onLogin(LoginEvent event) {

                String username = event.getLoginParameter("username").toLowerCase();
                String pw = PasswordUtil.generateHashedPassword(event.getLoginParameter("password"));
                
//                browser.showNotification(
//                        "New Login",
//                        "Username: " + event.getLoginParameter("username")
//                                + ", password: "
//                                + event.getLoginParameter("password"));
                
//                User u = null;
//                try {
//                    u = AuthenticationUtil.authenticate(username, pw);
//                } catch (InvalidCredentialsException ex) {
//                    Logger.getLogger(Header.class.getName()).log(Level.SEVERE, null, ex);
//                } catch (AccountLockedException ex) {
//                    Logger.getLogger(Header.class.getName()).log(Level.SEVERE, null, ex);
//                }
//                if (u == null) {
//                    UserUtil.registerUser(username, pw, pw);
//                }                
//                System.out.println("authenticated: " + u);
                                
                String query = "SELECT u FROM User AS u WHERE u.username=:username AND u.password=:password";
                Map<String, Object> parameters = new HashMap<String, Object>();
                parameters.put("username", username);
                parameters.put("password", pw);
                
                Self self = (Self)db.find(query, parameters);
                if (self==null) {
                    browser.showNotification("Invalid username or password.");
                }
                else {                
                    SessionHandler.setUser(self);                
                }
                
                
                browser.removeWindow(loginWindow);
                
                //Header.this.getWindow().open(new ExternalResource(app.getURL()));
                browser.refresh();
            }
        });
        content.addComponent(login);

        browser.addWindow(loginWindow);
    }

    public void register(final AppWindow root) {
        //show a dialog window w/ login form
          // Create the window...
        final Window regWindow = new Window("Register");
        // ...and make it modal
        regWindow.setModal(true);
        regWindow.setWidth("25%");
        regWindow.setHeight("50%");

        // Configure the windws layout; by default a VerticalLayout
        VerticalLayout content = (VerticalLayout) regWindow.getContent();
        content.setMargin(true);
        content.setSpacing(true);

        LoginForm login = new LoginForm();
        login.setLoginButtonCaption("Register");
        login.setWidth("100%");
        login.setHeight("300px");
        
        
        login.addListener(new LoginForm.LoginListener() {
            
            public void onLogin(LoginEvent event) {
                
                Self newSelf = new Self();
                newSelf.setUsername(event.getLoginParameter("username").toLowerCase());
                newSelf.setName(event.getLoginParameter("username"));
                                
                newSelf.setPassword(PasswordUtil.generateHashedPassword(event.getLoginParameter("password")));
                
                //SeedSelfBuilder.build(newSelf);
                                
                db.store(newSelf);                
                
                SessionHandler.setUser(newSelf);
        
                root.removeWindow(regWindow);
                
                root.refresh();
            }
        });
        content.addComponent(login);

        root.addWindow(regWindow);
    }
    
    public void logout(AppWindow browser) {
        SessionHandler.setUser(null);
        browser.refresh();
    }

    @Override
    public Self getSelf(String agent) {
        return getCommunity().getSelf(agent);
    }

    @Override
    public Collection<Detail> getDetails(Self who) {
        return getCommunity().getDetails(who);
    }

    @Override
    public Collection<Message> getMessages(Self who) {
        return getCommunity().getMessages(who);
    }

    @Override
    public NType newType(String id) {
        return getCommunity().newType(id);
    }

    @Override
    public NType getType(String id) {
        return getCommunity().getType(id);
    }

    @Override
    public Collection<NType> getTypes(Predicate<NType> pred) {
        return getCommunity().getTypes(pred);
    }

    @Override
    public void save(NType t) {
        getCommunity().save(t);
    }

    @Override
    public void remove(NType t) {
        getCommunity().remove(t);
    }

    @Override
    public Property getPropertyByID(String id) {
        return getCommunity().getPropertyByID(id);
    }


    
    @Override
    public Collection<Property> getProperties(Predicate<Property> pred) {
        return getCommunity().getProperties(pred);
    }

    @Override
    public void save(Property p) {
        getCommunity().save(p);
    }

    @Override
    public void remove(Property p) {
        getCommunity().remove(p);
    }

    @Override
    public Map<Property, Double> getAvailableProperties(Detail d, String... patternID) {
        return getCommunity().getAvailableProperties(d, patternID);
    }

    @Override
    public void save(Detail d) {
        getCommunity().save(d);
    }

    @Override
    public void save(Message m) {
        getCommunity().save(m);
    }

    @Override
    public Collection<Message> getMessages(Predicate<Message> pred) {
        return getCommunity().getMessages(pred);
    }

    @Override
    public Collection<Detail> getDetails(Predicate<Detail> pred) {
        return getCommunity().getDetails(pred);
    }

    @Override
    public Collection<Property> getProperties(final NType type, boolean includeInherited, boolean includeOrphaned) {
        return getCommunity().getProperties(type, includeInherited, includeOrphaned);
    }

    @Override
    public Set<String> getSuperTypes(NType type) {
        return getCommunity().getSuperTypes(type);
    }
    
    
}
