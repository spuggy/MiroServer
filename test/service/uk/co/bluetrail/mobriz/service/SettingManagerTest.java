
package uk.co.bluetrail.mobriz.service;

import java.util.List;
import java.util.ArrayList;

import uk.co.bluetrail.mobriz.service.BaseManagerTestCase;
import uk.co.bluetrail.mobriz.dao.SettingDao;
import uk.co.bluetrail.mobriz.model.Setting;
import uk.co.bluetrail.mobriz.service.impl.SettingManagerImpl;

import org.jmock.Mock;
import org.springframework.orm.ObjectRetrievalFailureException;

public class SettingManagerTest extends BaseManagerTestCase {
    private final String settingId = "1";
    private SettingManagerImpl settingManager = new SettingManagerImpl();
    private Mock settingDao = null;

    protected void setUp() throws Exception {
        super.setUp();
        settingDao = new Mock(SettingDao.class);
        settingManager.setSettingDao((SettingDao) settingDao.proxy());
    }

    protected void tearDown() throws Exception {
        super.tearDown();
        settingManager = null;
    }

    public void testGetSettings() throws Exception {
        List results = new ArrayList();
        Setting setting = new Setting();
        results.add(setting);

        // set expected behavior on dao
        settingDao.expects(once()).method("getSettings")
            .will(returnValue(results));

        List settings = settingManager.getSettings(null);
        assertTrue(settings.size() == 1);
        settingDao.verify();
    }

    public void testGetSetting() throws Exception {
        // set expected behavior on dao
        settingDao.expects(once()).method("getSetting")
            .will(returnValue(new Setting()));
        Setting setting = settingManager.getSetting(settingId);
        assertTrue(setting != null);
        settingDao.verify();
    }

    public void testSaveSetting() throws Exception {
        Setting setting = new Setting();

        // set expected behavior on dao
        settingDao.expects(once()).method("saveSetting")
            .with(same(setting)).isVoid();

        settingManager.saveSetting(setting);
        settingDao.verify();
    }

    public void testAddAndRemoveSetting() throws Exception {
        Setting setting = new Setting();

        // set required fields
        setting.setSettingDescription("SwMtSzSnVzOqAcIgCgSeZvGnEeJrXmLlAgWvCsXvReGbNuEfQgUxKvGrPaQyZyVkUvEkNwVoDsVuVvZxApUnHhBbGnFwRdPsUvKsThImWsZeBaExRsXiQbVjQbTyEqOaOdYdTzTvRbOdSoPhIpZkYvKoDuShJmQyFqSqBzEzOeJsWlQtPvCkHsAwHgRfNyImWrMhQmNfPrOfXtBqDoPcTdGjSjKmRqKyJpPjVgEsQiZgElQjOyPuZpCnBlTnBtY");
        setting.setSettingName("UyWxIpGzBuOyKbGoBfQgAdDpHiUfHp");
        setting.setSettingValue("WoZgHvKaKaQhTfXjVoLoPqVwTjHkCkRzJaNzJzGzWfOtDeOtGvXmIwXwApCrQxFxWxDdDmRgHqTnUkZpOmBeDhCgHiGhDkWfFmIcChCrMnDfXhGcYfDvTbJrVtXeKzAyJiSlIyRpHfLvKcHnSeEgAwQfIyDvVwCzWlJqCiFuRqUsPbGrVzVsGdFxRyLvMcXjEfSgNdZeEpZzGbKxMiAkIaJhXsLaWqRqWkObLuWhKxPgXeXxEiLtNhYfMuTtXxB");

        // set expected behavior on dao
        settingDao.expects(once()).method("saveSetting")
            .with(same(setting)).isVoid();
        settingManager.saveSetting(setting);
        settingDao.verify();

        // reset expectations
        settingDao.reset();

        settingDao.expects(once()).method("removeSetting").with(eq(new Long(settingId)));
        settingManager.removeSetting(settingId);
        settingDao.verify();

        // reset expectations
        settingDao.reset();
        // remove
        Exception ex = new ObjectRetrievalFailureException(Setting.class, setting.getId());
        settingDao.expects(once()).method("removeSetting").isVoid();
        settingDao.expects(once()).method("getSetting").will(throwException(ex));
        settingManager.removeSetting(settingId);
        try {
            settingManager.getSetting(settingId);
            fail("Setting with identifier '" + settingId + "' found in database");
        } catch (ObjectRetrievalFailureException e) {
            assertNotNull(e.getMessage());
        }
        settingDao.verify();
    }
}
