package uk.co.bluetrail.mobriz.webapp.action;

import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import uk.co.bluetrail.mobriz.Constants;
import uk.co.bluetrail.mobriz.webapp.action.BaseControllerTestCase;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.mock.web.MockHttpServletRequest;

public class AccountControllerTest extends BaseControllerTestCase {

    public void testHandleRequest() throws Exception {
        AccountController c = 
            (AccountController) ctx.getBean("accountController");
        ModelAndView mav = c.handleRequest(new MockHttpServletRequest(),
                                           (HttpServletResponse) null);
        Map m = mav.getModel();
        assertNotNull(m.get(Constants.ACCOUNT_LIST));
        assertEquals(mav.getViewName(), "accountList");
    }
}
