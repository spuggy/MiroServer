package uk.co.bluetrail.mobriz.webapp.view;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.servlet.View;

import flexjson.JSONSerializer;

public class JsonView  implements View {

    public String getContentType() {
        return "text/json";
    }

    public void render(Map model, HttpServletRequest request, HttpServletResponse response) throws Exception {
        try {
            String data = new JSONSerializer().exclude("*.class").deepSerialize(model);
            response.getWriter().write(data);
        } catch (java.io.IOException e) {
            // leave empty
        }
    }


}