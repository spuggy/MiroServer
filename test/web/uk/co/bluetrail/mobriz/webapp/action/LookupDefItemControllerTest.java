package uk.co.bluetrail.mobriz.webapp.action;

import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import uk.co.bluetrail.mobriz.Constants;
import uk.co.bluetrail.mobriz.webapp.action.BaseControllerTestCase;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.mock.web.MockHttpServletRequest;

public class LookupDefItemControllerTest extends BaseControllerTestCase {

    public void testHandleRequest() throws Exception {
        LookupDefItemController c = 
            (LookupDefItemController) ctx.getBean("lookupDefItemController");
        ModelAndView mav = c.handleRequest(new MockHttpServletRequest(),
                                           (HttpServletResponse) null);
        Map m = mav.getModel();
        assertNotNull(m.get(Constants.LOOKUPDEFITEM_LIST));
        assertEquals(mav.getViewName(), "lookupDefItemList");
    }
}
