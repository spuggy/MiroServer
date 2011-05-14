package uk.co.bluetrail.mobriz.webapp.action;

import uk.co.bluetrail.mobriz.model.LookupDefItem;
import uk.co.bluetrail.mobriz.webapp.action.BaseControllerTestCase;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.validation.BindException;
import org.springframework.validation.Errors;
import org.springframework.web.servlet.ModelAndView;

public class LookupDefItemFormControllerTest extends BaseControllerTestCase {
    private LookupDefItemFormController c;
    private MockHttpServletRequest request;
    private ModelAndView mv;

    protected void setUp() throws Exception {
        // needed to initialize a user
        super.setUp();
        c = (LookupDefItemFormController) ctx.getBean("lookupDefItemFormController");
    }

    protected void tearDown() {
        c = null;
    }

    public void testEdit() throws Exception {
        log.debug("testing edit...");
        request = newGet("/editLookupDefItem.html");
        request.addParameter("id", "1");

        mv = c.handleRequest(request, new MockHttpServletResponse());

        assertEquals("lookupDefItemForm", mv.getViewName());
    }

    public void testSave() throws Exception {
        request = newGet("/editLookupDefItem.html");
        request.addParameter("id", "1");

        mv = c.handleRequest(request, new MockHttpServletResponse());

        LookupDefItem lookupDefItem = (LookupDefItem) mv.getModel().get(c.getCommandName());
        assertNotNull(lookupDefItem);
        request = newPost("/editLookupDefItem.html");
        super.objectToRequestParameters(lookupDefItem, request);

        // update the form's fields and add it back to the request
        mv = c.handleRequest(request, new MockHttpServletResponse());
        Errors errors = (Errors) mv.getModel().get(BindException.MODEL_KEY_PREFIX + "lookupDefItem");

        if (errors != null) {
            log.debug(errors);
        }
        assertNull(errors);
        assertNotNull(request.getSession().getAttribute("successMessages"));        
    }

    public void testRemove() throws Exception {
        request = newPost("/editLookupDefItem.html");
        request.addParameter("delete", "");
        request.addParameter("id", "2");
        mv = c.handleRequest(request, new MockHttpServletResponse());
        assertNotNull(request.getSession().getAttribute("successMessages"));
    }
}
