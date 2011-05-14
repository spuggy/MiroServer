package uk.co.bluetrail.mobriz.dao.hibernate;

import java.util.Iterator;
import java.util.List;

import uk.co.bluetrail.mobriz.model.Constraint;
import uk.co.bluetrail.mobriz.model.Option;
import uk.co.bluetrail.mobriz.model.Question;
import uk.co.bluetrail.mobriz.model.User;
import uk.co.bluetrail.mobriz.dao.QuestionDAO;

import org.hibernate.Criteria;
import org.hibernate.Hibernate;
import org.hibernate.HibernateException;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.criterion.Example;
import org.hibernate.criterion.Expression;
import org.hibernate.criterion.MatchMode;
import org.springframework.orm.ObjectRetrievalFailureException;
import org.springframework.orm.hibernate3.HibernateCallback;

public class QuestionDAOHibernate extends BaseDaoHibernate implements
		QuestionDAO {

	public List getUpdated(Long ckp, boolean deleted, User newParam) {

		if (!deleted) {
			return getHibernateTemplate().find(
					"from Question q where q.deleted = false and q.checkPoint > "
							+ ckp.toString());
		} else {
			return getHibernateTemplate().find(
					"from Question q where q.deleted = true and q.checkPoint > "
							+ ckp.toString() + " and q.survey.deleted = false");
		}
	}

	/**
	 * Retrieves all of the for a sid
	 */
	public List getQuestions(Long sid) {
		return getHibernateTemplate().find(
				"from Question q where q.deleted = false and q.survey_id="
						+ sid.toString());
	}

	/**
	 * @see uk.co.bluetrail.mobriz.dao.QuestionDAO#getQuestions(uk.co.bluetrail.mobriz.model.Question)
	 */
	public List getQuestions(final Question question) {
		return getHibernateTemplate().find(
				"from Question q where q.deleted = false");

		/*
		 * Remove the line above and uncomment this code block if you want to
		 * use Hibernate's Query by Example API. if (question == null) { return
		 * getHibernateTemplate().find("from Question"); } else { // filter on
		 * properties set in the question HibernateCallback callback = new
		 * HibernateCallback() { public Object doInHibernate(Session session)
		 * throws HibernateException { Example ex =
		 * Example.create(question).ignoreCase().enableLike(MatchMode.ANYWHERE);
		 * return session.createCriteria(Question.class).add(ex).list(); } };
		 * return (List) getHibernateTemplate().execute(callback); }
		 */
	}

	/**
	 * @see uk.co.bluetrail.mobriz.dao.QuestionDAO#getQuestion(Long id)
	 */
	public Question getQuestion(final Long id) {

		Question question = (Question) getHibernateTemplate().get(
				Question.class, id);
		if (question == null) {
			log.warn("uh oh, question with id '" + id + "' not found...");
			throw new ObjectRetrievalFailureException(Question.class, id);
		}

		return question;
	}

	/**
	 * @see uk.co.bluetrail.mobriz.dao.QuestionDAO#saveQuestion(Question
	 *      question)
	 */
	public void saveQuestion(final Question question) {
		question.setCheckPoint(getNextCheckPoint());

		// if it is deleted then delete the options
		if (question.isDeleted() && question.getOptions() != null) {
			Iterator itr = question.getOptions().iterator();
			Option option = null;
			while (itr.hasNext()) {
				option = (Option) itr.next();
				option.setDeleted(true);
			}

		}

		// if it is deleted then delete the constraints
		if (question.isDeleted() && question.getConstraints() != null) {
			Iterator itr = question.getConstraints().iterator();
			Constraint constraint = null;
			while (itr.hasNext()) {
				constraint = (Constraint) itr.next();
				constraint.setDeleted(true);
			}

		}

		getHibernateTemplate().saveOrUpdate(question);
	}

	/**
	 * @see uk.co.bluetrail.mobriz.dao.QuestionDAO#removeQuestion(Long id)
	 */
	public void removeQuestion(final Long id) {
		// getHibernateTemplate().delete(getQuestion(id));
		// soft delete!
		Question q = getQuestion(id);
		q.setDeleted(true);
		saveQuestion(q);

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see uk.co.bluetrail.mobriz.dao.QuestionDAO#getPrevQuestion(java.lang.Long)
	 */
	public Question getPrevQuestion(Long id) {

		List qs = (List) getHibernateTemplate().find(
				"from Question q where q.deleted = false and q.JQuestion_id="
						+ id.toString());

		if (qs == null || qs.size() == 0) {
			return null;
		} else {
			return (Question) qs.get(0);
		}

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see uk.co.bluetrail.mobriz.dao.QuestionDAO#getNextQuestion(java.lang.Long)
	 */
	public Question getNextQuestion(Long id) {

		List qs = (List) getHibernateTemplate().find(
				"from Question q where q.deleted = false and q.id="
						+ id.toString());
		if (qs == null || qs.size() == 0) {
			return null;
		} else {
			return (Question) qs.get(0);
		}

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see uk.co.bluetrail.mobriz.dao.QuestionDAO#removeBranchesToQuestion(java.lang.Long)
	 */
	public void removeBranchesToQuestion(final Long qid) {

		final Long nextCheckPoint = getNextCheckPoint() ; 
		
		
		HibernateCallback callback = new HibernateCallback() {
			public Object doInHibernate(Session session)
					throws HibernateException {
				
				String updateSql = "update Question set branch_jquestion_id = 0 , checkPoint = " + nextCheckPoint.toString() + " where branch_jquestion_id = " + qid.toString(); 
				session.createQuery( updateSql ).executeUpdate();
				return null;
			}

		};
		
		getHibernateTemplate().execute(callback);
		return ;

	}

	/* (non-Javadoc)
	 * @see uk.co.bluetrail.mobriz.dao.QuestionDAO#getQuestions_by_date(java.lang.Long)
	 */
	public List getQuestions_by_date(Long sid) {
		return getHibernateTemplate().find(
				"from Question q where q.deleted = false and q.survey_id="
						+ sid.toString() + " order by created_on, id");
	}
}
