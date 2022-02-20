package rk.android.app.privacydashboard.activity.permission.log;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import java.util.Calendar;
import java.util.List;

import rk.android.app.privacydashboard.activity.permission.log.database.LogsRepository;
import rk.android.app.privacydashboard.model.Logs;
import rk.android.app.privacydashboard.util.Utils;

public class LogsViewModel extends AndroidViewModel {
    LogsRepository repository;
    LiveData<List<Logs>> liveLogs;
    private String date = "01-Jan-2021";

    public LogsViewModel(Application application) {
        super(application);
        repository = new LogsRepository(application);
        date = Utils.getDateFromTimestamp(Calendar.getInstance().getTimeInMillis());
    }

    public LiveData<List<Logs>> getLogs(String permission) {
        return liveLogs = repository.getAllLogsForPermission(permission);
    }

    public int getLogsCount(String permission) {
        return repository.getLogsCount(permission, date);
    }

    public void clearLogs() {
        repository.clearLogs();
    }
}
