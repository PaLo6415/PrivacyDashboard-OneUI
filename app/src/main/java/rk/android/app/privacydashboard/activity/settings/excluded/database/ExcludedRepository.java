package rk.android.app.privacydashboard.activity.settings.excluded.database;

import android.app.Application;

import java.util.List;

import rk.android.app.privacydashboard.model.Apps;

public class ExcludedRepository {
    ExcludedDao excludedDao;

    public ExcludedRepository(Application application) {
        ExcludedDatabase database = ExcludedDatabase.getInstance(application);
        excludedDao = database.exceptionsDao();
    }

    public boolean isExcluded(String packageName) {
        return excludedDao.isExcluded(packageName);
    }

    public int getCount() {
        return excludedDao.getCount();
    }

    public List<String> getPackages() {
        return excludedDao.getPackages();
    }

    public void insert(Apps apps) {
        ExcludedDatabase.databaseWriteExecutor.execute(() -> excludedDao.insert(apps));
    }

    public void delete(Apps apps) {
        ExcludedDatabase.databaseWriteExecutor.execute(() -> excludedDao.delete(apps));
    }

    public void clearLogs() {
        ExcludedDatabase.databaseWriteExecutor.execute(() -> excludedDao.clearAll());
    }
}
