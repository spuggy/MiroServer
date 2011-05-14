package uk.co.bluetrail.mobriz.webapp.action;

import java.util.Locale;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import uk.co.bluetrail.mobriz.webapp.action.BaseFormController;
import uk.co.bluetrail.mobriz.model.WebPage;
import uk.co.bluetrail.mobriz.service.WebPageManager;
import org.springframework.validation.BindException;
import org.springframework.web.servlet.ModelAndView;

public class WebPageFormController extends BaseFormController {
    private WebPageManager webPageManager = null;

    public void setWebPageManager(WebPageManager webPageManager) {
        this.webPageManager = webPageManager;
    }
    public WebPageFormController() {
        setCommandName("webPage");
        setCommandClass(WebPage.class);
    }

    protected Object formBackingObject(HttpServletRequest request)
    throws Exception {
        String id = request.getParameter("id");
        WebPage webPage = null;

        if (!StringUtils.isEmpty(id)) {
            webPage = webPageManager.getWebPage(id);
        } else {
            webPage = new WebPage();
        }

        return webPage;
    }

    public ModelAndView onSubmit(HttpServletRequest request,
                                 HttpServletResponse response, Object command,
                                 BindException errors)
    throws Exception {
        if (log.isDebugEnabled()) {
            log.debug("entering 'onSubmit' method...");
        }

        WebPage webPage = (WebPage) command;
        boolean isNew = (webPage.getId() == null);
        Locale locale = request.getLocale();

        if (request.getParameter("delete") != null) {
            webPageManager.removeWebPage(webPage.getId().toString());

            saveMessage(request, getText("webPage.deleted", locale));
        } else {
            webPageManager.saveWebPage(webPage);

            String key = (isNew) ? "webPage.added" : "webPage.updated";
            saveMessage(request, getText(key, locale));

            if (!isNew) {
                return new ModelAndView("redirect:editWebPage.html", "id", webPage.getId());
            }
        }

        return new ModelAndView(getSuccessView());
    }
}
