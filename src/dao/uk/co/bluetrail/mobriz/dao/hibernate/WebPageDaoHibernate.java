
package uk.co.bluetrail.mobriz.dao.hibernate;

import java.sql.SQLException;
import java.util.List;

import uk.co.bluetrail.mobriz.dao.hibernate.BaseDaoHibernate;
import uk.co.bluetrail.mobriz.model.SurveyResponse;
import uk.co.bluetrail.mobriz.model.WebPage;
import uk.co.bluetrail.mobriz.dao.WebPageDao;

import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.criterion.Expression;
import org.hibernate.criterion.Order;
import org.springframework.orm.ObjectRetrievalFailureException;
import org.springframework.orm.hibernate3.HibernateCallback;

public class WebPageDaoHibernate extends BaseDaoHibernate implements WebPageDao {

    /**
     * @see uk.co.bluetrail.mobriz.dao.WebPageDao#getWebPages(uk.co.bluetrail.mobriz.model.WebPage)
     */
    public List getWebPages(final WebPage webPage) {
        return getHibernateTemplate().find("from WebPage");

        /* Remove the line above and uncomment this code block if you want 
           to use Hibernate's Query by Example API.
        if (webPage == null) {
            return getHibernateTemplate().find("from WebPage");
        } else {
            // filter on properties set in the webPage
            HibernateCallback callback = new HibernateCallback() {
                public Object doInHibernate(Session session) throws HibernateException {
                    Example ex = Example.create(webPage).ignoreCase().enableLike(MatchMode.ANYWHERE);
                    return session.createCriteria(WebPage.class).add(ex).list();
                }
            };
            return (List) getHibernateTemplate().execute(callback);
        }*/
    }

    /**
     * @see uk.co.bluetrail.mobriz.dao.WebPageDao#getWebPage(Long id)
     */
    public WebPage getWebPage(final Long id) {
        WebPage webPage = (WebPage) getHibernateTemplate().get(WebPage.class, id);
        if (webPage == null) {
            log.warn("uh oh, webPage with id '" + id + "' not found...");
            throw new ObjectRetrievalFailureException(WebPage.class, id);
        }

        return webPage;
    }

    /**
     * @see uk.co.bluetrail.mobriz.dao.WebPageDao#saveWebPage(WebPage webPage)
     */    
    public void saveWebPage(final WebPage webPage) {
        getHibernateTemplate().saveOrUpdate(webPage);
    }

    /**
     * @see uk.co.bluetrail.mobriz.dao.WebPageDao#removeWebPage(Long id)
     */
    public void removeWebPage(final Long id) {
        getHibernateTemplate().delete(getWebPage(id));
    }

	public WebPage getWebPageByName(final String name) {

      HibernateCallback callback = new HibernateCallback() {
            public Object doInHibernate(Session session) throws HibernateException, SQLException {

         Criteria crit = session.createCriteria(WebPage.class);
             crit.setMaxResults(1);
             crit.add(Expression.eq("pageName",name));
             crit.addOrder(Order.desc("id"));
          return  crit.list();

             }
         };

         List webPages=  (List) getHibernateTemplate().execute(callback);

         if(webPages != null && webPages.size() > 0 ) {
                return (WebPage) webPages.get(0) ;
        } else {
             return null;
         }

	}

    public List  getWebPagesTypeByDate(final int pageType, final int limit) {
        HibernateCallback callback = new HibernateCallback() {
            public Object doInHibernate(Session session) throws HibernateException, SQLException {
         Criteria crit = session.createCriteria(WebPage.class);
            crit.setMaxResults(limit);
            crit.add(Expression.eq("pageType",pageType));
            crit.add(Expression.eq("pageStatus",WebPage.PUBLISHED));
            crit.addOrder(Order.desc("publishedDate"));
            return  crit.list();

            }
        };

        List webPages=  (List) getHibernateTemplate().execute(callback);

        return webPages;

    }
}
