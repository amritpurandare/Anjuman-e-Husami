package anjuman.e.husami;

public class Notifications {

    public static final String _ID = "_id";
    public static final String _NOTIFICATIONTITLE = "notificationTitle";
    public static final String _NOTIFICATIONMESSAGE = "notificationmessage";
    public static final String NOTIFICATION_DATE = "notificationDate";
    public static final String _NOTIFICATIONS_TABLE_NAME = "NOTIFICATIONS";


    /* -------------- Start - Notifications  Table ----------------- */
    public static final String CREATE_NOTIFICATIONS_TABLE = "CREATE TABLE IF NOT EXISTS " + _NOTIFICATIONS_TABLE_NAME + " (" +
            _ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
            _NOTIFICATIONTITLE + " VARCHAR , " +
            _NOTIFICATIONMESSAGE + " VARCHAR , " +
            NOTIFICATION_DATE + " VARCHAR " +
            ")";

    public static final String DROP_NOTIFICATIONS_TABLE = "DROP TABLE IF EXISTS NOTIFICATIONS";
    /* -------------- End - Notifications Table ----------------- */

    public Notifications(int notificationId, String mNotificationTitle, String mNotificationMessage, String mNotificationDateTime) {
        super();

        this.mNotificationId = notificationId;
        this.mNotificationTitle = mNotificationTitle;
        this.mNotificationMessage = mNotificationMessage;
        this.mNotificationDateTime = mNotificationDateTime;
    }

    /**
     * Default Notifications Constructor
     */
    public Notifications() {
        super();
    }

    public int mNotificationId;
    public String mNotificationTitle;
    public String mNotificationMessage;
    public String mNotificationDateTime;
}
