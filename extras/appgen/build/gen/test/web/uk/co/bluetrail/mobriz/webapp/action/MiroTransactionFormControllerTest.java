package uk.co.bluetrail.mobriz.webapp.action;

import uk.co.bluetrail.mobriz.model.MiroTransaction;
import uk.co.bluetrail.mobriz.webapp.action.BaseControllerTestCase;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.validation.BindException;
import org.springframework.validation.Errors;
import org.springframework.web.servlet.ModelAndView;

public class MiroTransactionFormControllerTest extends BaseControllerTestCase {
    private MiroTransactionFormController c;
    private MockHttpServletRequest request;
    private ModelAndView mv;

    protected void setUp() throws Exception {
        // needed to initialize a user
        super.setUp();
        c = (MiroTransactionFormController) ctx.getBean("miroTransactionFormController");
    }

    protected void tearDown() {
        c = null;
    }

    public void testEdit() throws Exception {
        log.debug("testing edit...");
        request = newGet("/editMiroTransaction.html");
        request.addParameter("id", "1");

        mv = c.handleRequest(request, new MockHttpServletResponse());

        assertEquals("miroTransactionForm", mv.getViewName());
    }

    public void testSave() throws Exception {
        request = newGet("/editMiroTransaction.html");
        request.addParameter("id", "1");

        mv = c.handleRequest(request, new MockHttpServletResponse());

        MiroTransaction miroTransaction = (MiroTransaction) mv.getModel().get(c.getCommandName());
        assertNotNull(miroTransaction);
        request = newPost("/editMiroTransaction.html");
        super.objectToRequestParameters(miroTransaction, request);

        // update the form's fields and add it back to the request
        mv = c.handleRequest(request, new MockHttpServletResponse());
        Errors errors = (Errors) mv.getModel().get(BindException.MODEL_KEY_PREFIX + "miroTransaction");

        if (errors != null) {
            log.debug(errors);
        }
        assertNull(errors);
        assertNotNull(request.getSession().getAttribute("successMessages"));        
    }

    public void testRemove() throws Exception {
        request = newPost("/editMiroTransaction.html");
        request.addParameter("delete", "");
        request.addParameter("id", "2");
        mv = c.handleRequest(request, new MockHttpServletResponse());
        assertNotNull(request.getSession().getAttribute("successMessages"));
    }
}
