package uk.co.bluetrail.mobriz.webapp.action;

import uk.co.bluetrail.mobriz.model.Account;
import uk.co.bluetrail.mobriz.webapp.action.BaseControllerTestCase;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.validation.BindException;
import org.springframework.validation.Errors;
import org.springframework.web.servlet.ModelAndView;

public class AccountFormControllerTest extends BaseControllerTestCase {
    private AccountFormController c;
    private MockHttpServletRequest request;
    private ModelAndView mv;

    protected void setUp() throws Exception {
        // needed to initialize a user
        super.setUp();
        c = (AccountFormController) ctx.getBean("accountFormController");
    }

    protected void tearDown() {
        c = null;
    }

    public void testEdit() throws Exception {
        log.debug("testing edit...");
        request = newGet("/editAccount.html");
        request.addParameter("id", "1");

        mv = c.handleRequest(request, new MockHttpServletResponse());

        assertEquals("accountForm", mv.getViewName());
    }

    public void testSave() throws Exception {
        request = newGet("/editAccount.html");
        request.addParameter("id", "1");

        mv = c.handleRequest(request, new MockHttpServletResponse());

        Account account = (Account) mv.getModel().get(c.getCommandName());
        assertNotNull(account);
        request = newPost("/editAccount.html");
        super.objectToRequestParameters(account, request);

        // update the form's fields and add it back to the request
        account.setCompanyName("MuPpBsIgVmKeRcNyMnNoWmYeQrElYsXjGxNfGwFmAyOaTdEqBe");
        mv = c.handleRequest(request, new MockHttpServletResponse());
        Errors errors = (Errors) mv.getModel().get(BindException.MODEL_KEY_PREFIX + "account");

        if (errors != null) {
            log.debug(errors);
        }
        assertNull(errors);
        assertNotNull(request.getSession().getAttribute("successMessages"));        
    }

    public void testRemove() throws Exception {
        request = newPost("/editAccount.html");
        request.addParameter("delete", "");
        request.addParameter("id", "2");
        mv = c.handleRequest(request, new MockHttpServletResponse());
        assertNotNull(request.getSession().getAttribute("successMessages"));
    }
}
