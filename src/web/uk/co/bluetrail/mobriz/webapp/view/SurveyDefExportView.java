package uk.co.bluetrail.mobriz.webapp.view;

import java.io.PrintWriter;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.servlet.view.AbstractView;

import uk.co.bluetrail.mobriz.model.Option;
import uk.co.bluetrail.mobriz.model.Question;
import uk.co.bluetrail.mobriz.model.Survey;
import uk.co.bluetrail.mobriz.model.SurveyResponse;
import uk.co.bluetrail.mobriz.model.User;
import uk.co.bluetrail.mobriz.service.QuestionManager;
import uk.co.bluetrail.mobriz.webapp.form.WebServiceForm;

public class SurveyDefExportView extends AbstractView {

	private QuestionManager questionManager = null;

	public QuestionManager getQuestionManager() {
		return questionManager;
	}

	public void setQuestionManager(QuestionManager questionManager) {
		this.questionManager = questionManager;
	}

	@Override
	protected void renderMergedOutputModel(Map args,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		Survey survey = (Survey) args.get("survey");
		response.setHeader("Content-Disposition", "attachment; filename=\""
				+ survey.getTitle() + ".txt\"");

		PrintWriter pw = response.getWriter();

		// export the title
		pw.println(Survey.TITLE_LINE);
		pw.println(survey.export());

		HashMap questionMap = survey.getQuestionMap();

		Long nextQid = survey.getFirstQuestion_id();
		Question question;
		while (nextQid.longValue() != 0) {
			question = (Question) questionMap.get(nextQid);
			pw.println(question.export());

			if (question.getQTypeStr().equals(Question.QTYPE_CHECKBOX)
					|| question.getQTypeStr().equals(Question.QTYPE_RADIO)
					|| question.getQTypeStr().equals(Question.QTYPE_SCORE)
					|| question.getQTypeStr().equals(Question.QTYPE_PERCENT)) {

				Set options = question.getOptions();
				Iterator itr = options.iterator();
				Option option = null;
				while (itr.hasNext()) {
					option = (Option) itr.next();
					pw.println(option.export());
				}

			}

			nextQid = question.getJQuestion_id();
		}

	}

}
