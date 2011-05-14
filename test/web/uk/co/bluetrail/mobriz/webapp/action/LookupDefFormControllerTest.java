package uk.co.bluetrail.mobriz.webapp.action;

import uk.co.bluetrail.mobriz.model.LookupDef;
import uk.co.bluetrail.mobriz.webapp.action.BaseControllerTestCase;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.validation.BindException;
import org.springframework.validation.Errors;
import org.springframework.web.servlet.ModelAndView;

public class LookupDefFormControllerTest extends BaseControllerTestCase {
    private LookupDefFormController c;
    private MockHttpServletRequest request;
    private ModelAndView mv;

    protected void setUp() throws Exception {
        // needed to initialize a user
        super.setUp();
        c = (LookupDefFormController) ctx.getBean("lookupDefFormController");
    }

    protected void tearDown() {
        c = null;
    }

    public void testEdit() throws Exception {
        log.debug("testing edit...");
        request = newGet("/editLookupDef.html");
        request.addParameter("id", "1");

        mv = c.handleRequest(request, new MockHttpServletResponse());

        assertEquals("lookupDefForm", mv.getViewName());
    }

    public void testSave() throws Exception {
        request = newGet("/editLookupDef.html");
        request.addParameter("id", "1");

        mv = c.handleRequest(request, new MockHttpServletResponse());

        LookupDef lookupDef = (LookupDef) mv.getModel().get(c.getCommandName());
        assertNotNull(lookupDef);
        request = newPost("/editLookupDef.html");
        super.objectToRequestParameters(lookupDef, request);

        // update the form's fields and add it back to the request
        lookupDef.setName("BeMjSlYeTiWmFdNxYlOpHqFkFkBlBa");
        lookupDef.setDesc("IoOySvWiDzCuWnOgSeVbIkCiGzQwNwUgJlHxLjRiFqSfAyFvVkTcXtMmIfGjYqZkHnImFeJbXpQiAoNjUhXrZoFzAvKnUzDvAmQgZyUhYiYkFdYdQvHhZrJwCbDtXvZmAeGgEnVuAeKlKsOtRpPaAeCaBwLcMuTkAlRvEuBrTvHxEjCfWkBvRtZtZiOoQoQgCdQtYsKoZnIlDbZgKaCgLoLcDeRoTkReXxAiXuMpIgVaAsVtTeSiFaNyLqUlNwU");
        lookupDef.setLookupDesc1("WbObCcMuCcDnHfVeLlDf");
        lookupDef.setLookupDesc2("ScCxOvTwSxYaCzGvKqFk");
        lookupDef.setLookupDesc3("UgKaDkAgXuXnZjXzPuQg");
        lookupDef.setLookupDesc4("CpIdFyNwNiIuLqExKoWg");
        lookupDef.setLookupDesc5("XhLjMtHoYpCeAkWjYgVf");
        mv = c.handleRequest(request, new MockHttpServletResponse());
        Errors errors = (Errors) mv.getModel().get(BindException.MODEL_KEY_PREFIX + "lookupDef");

        if (errors != null) {
            log.debug(errors);
        }
        assertNull(errors);
        assertNotNull(request.getSession().getAttribute("successMessages"));        
    }

    public void testRemove() throws Exception {
        request = newPost("/editLookupDef.html");
        request.addParameter("delete", "");
        request.addParameter("id", "2");
        mv = c.handleRequest(request, new MockHttpServletResponse());
        assertNotNull(request.getSession().getAttribute("successMessages"));
    }
}
