package uk.co.bluetrail.mobriz.webapp.action;

import uk.co.bluetrail.mobriz.model.Setting;
import uk.co.bluetrail.mobriz.webapp.action.BaseControllerTestCase;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.validation.BindException;
import org.springframework.validation.Errors;
import org.springframework.web.servlet.ModelAndView;

public class SettingFormControllerTest extends BaseControllerTestCase {
    private SettingFormController c;
    private MockHttpServletRequest request;
    private ModelAndView mv;

    protected void setUp() throws Exception {
        // needed to initialize a user
        super.setUp();
        c = (SettingFormController) ctx.getBean("settingFormController");
    }

    protected void tearDown() {
        c = null;
    }

    public void testEdit() throws Exception {
        log.debug("testing edit...");
        request = newGet("/editSetting.html");
        request.addParameter("id", "1");

        mv = c.handleRequest(request, new MockHttpServletResponse());

        assertEquals("settingForm", mv.getViewName());
    }

    public void testSave() throws Exception {
        request = newGet("/editSetting.html");
        request.addParameter("id", "1");

        mv = c.handleRequest(request, new MockHttpServletResponse());

        Setting setting = (Setting) mv.getModel().get(c.getCommandName());
        assertNotNull(setting);
        request = newPost("/editSetting.html");
        super.objectToRequestParameters(setting, request);

        // update the form's fields and add it back to the request
        setting.setSettingDescription("OdJfAwRtSbFvMiQiHwRmZaVqCuUeJaEoDsOePeNjSyKbClGrWiOjKjUcFyPgYnMaOwHtIzBzJiGdJdRrSvEnJpZoDoGxLgVqVtUdSdWtSrCnFrEfXhIgTtEqAiRkWpKaLvJrItRzHlZbViUeEcEpVaAjFhDnTmDlDyZkZyRzWfApCxFfFvQkAoFlCqJaBxUoGdYsOgEvIaUgVxOdPxNyFwLwHsRjDpTfBbYmPwPnDaJoLxAjRkGgEnGpUgIqZsP");
        setting.setSettingName("PmMlGoRmPoLgFvWrSdGuGkWsUzWkTr");
        setting.setSettingValue("GrNyGwScVcOsPuSoDzFhVpSwFkXaWpZkJuDsCrRoHzAeEoRiHaLcMhThCsIjFkMeZmQcEeUaBvPpCjXjMiCbMzNrYmFwQsJhPiOjNwZcFrRxGhDrBvOoHkXaVqKpKiYvTlYmXxAnJlSfOyUvFeZlZwVePjWkTbBiCmAjTxCpLjNtMaCqAzLvVsPbGhDlGrXzGqEiRvYcQcBcOdJpWuOdGyMgNbEcQtTrQzWrWjDgRuGtHaOzRzBkFaQpRdHcQxO");
        setting.setAccount_id(new Long(1));
        mv = c.handleRequest(request, new MockHttpServletResponse());
        Errors errors = (Errors) mv.getModel().get(BindException.MODEL_KEY_PREFIX + "setting");

        if (errors != null) {
            log.debug(errors);
        }
        assertNull(errors);
        assertNotNull(request.getSession().getAttribute("successMessages"));        
    }

    public void testRemove() throws Exception {
        request = newPost("/editSetting.html");
        request.addParameter("delete", "");
        request.addParameter("id", "2");
        mv = c.handleRequest(request, new MockHttpServletResponse());
        assertNotNull(request.getSession().getAttribute("successMessages"));
    }
}
