package uk.co.bluetrail.mobriz.service;

import uk.co.bluetrail.mobriz.Constants;
import uk.co.bluetrail.mobriz.model.User;
import org.springframework.beans.BeanUtils;
import org.springframework.test.AbstractTransactionalDataSourceSpringContextTests;
import org.springframework.util.ClassUtils;

public class UserExistsExceptionTest extends AbstractTransactionalDataSourceSpringContextTests {
    private UserManager manager = null;

    public void setUserManager(UserManager userManager) {
        this.manager = userManager;
        User u = new User() ;
        u.setAccount_id(new Long(1)) ;
        this.manager.setUser(u);
        
    }
    
    protected String[] getConfigLocations() {
        String pkg = ClassUtils.classPackageAsResourcePath(Constants.class);
        return new String[] {"classpath*:/" + pkg + "/dao/applicationContext-*.xml",
                             "classpath*:/" + pkg + "/service/applicationContext-service.xml",
                             "classpath*:META-INF/applicationContext-*.xml"};
    }

    public void testAddExistingUser() throws Exception {
        logger.debug("entered 'testAddExistingUser' method");
        
      
        
        
        assertNotNull(manager);

        
        
        
        User user = manager.getUser("1");
        
        assertNotNull(user);
        
              
        // create new object with null id - Hibernate doesn't like setId(null)
        User user2 = new User();
        BeanUtils.copyProperties(user, user2);
        
        
        user2.setId(null);
        user2.setVersion(null);
        user2.setRoles(null);
        
        
        
        // try saving as new user, this should fail b/c of unique keys
        try {
            manager.saveUser(user2);
       
            
            fail("Duplicate user didn't throw UserExistsException");
        } catch (UserExistsException uee) {
            assertNotNull(uee);
        }
    }    
}
