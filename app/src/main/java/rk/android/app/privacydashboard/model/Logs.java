package rk.android.app.privacydashboard.model;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "logs_table")
public class Logs {
    public int logId;
    @PrimaryKey
    public long startTimestamp;
    public long endTimestamp;
    public String packageName;
    public String permission;
    public int state;
    public String date;

    public Logs(int logId, long startTimestamp, String packageName, String permission, int state, String date) {
        this.logId = logId;
        this.startTimestamp = startTimestamp;
        this.packageName = packageName;
        this.permission = permission;
        this.state = state;
        this.date = date;
    }

    public void setEndTimestamp(long endTimestamp) {
        this.endTimestamp = endTimestamp;
    }
}
