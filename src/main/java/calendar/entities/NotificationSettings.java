package calendar.entities;

import org.hibernate.annotations.DynamicInsert;

import javax.persistence.*;

@Entity
@Table(name = "notifications_settings")
@DynamicInsert
public class NotificationSettings {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column
    private boolean byEmail;

    @Column
    private boolean byPopUp;

    @Column
    private boolean userStatusChanged;

    @Column
    private boolean eventDataChanged;

    @Column
    private boolean eventCanceled;

    @Column
    private boolean userWasUninvited;

    @Column
    private boolean remind1MinBefore;

    @Column
    private boolean remind5MinBefore;

    @Column
    private boolean remind10MinBefore;

    public NotificationSettings() {
    }

    public static NotificationSettings defaultSettings() {
        return new NotificationSettings(true, true, true, true, true, true, true, true, true);
    }

    public NotificationSettings(boolean byEmail, boolean byPopUp,
                                boolean userStatusChanged, boolean eventDataChanged,
                                boolean eventCanceled, boolean userWasUninvited,
                                boolean remind1MinBefore, boolean remind5MinBefore,
                                boolean remind10MinBefore) {
        this.byEmail = byEmail;
        this.byPopUp = byPopUp;
        this.userStatusChanged = userStatusChanged;
        this.eventDataChanged = eventDataChanged;
        this.eventCanceled = eventCanceled;
        this.userWasUninvited = userWasUninvited;
        this.remind1MinBefore = remind1MinBefore;
        this.remind5MinBefore = remind5MinBefore;
        this.remind10MinBefore = remind10MinBefore;
    }

    public boolean getByEmail() {
        return byEmail;
    }

    public boolean getByPopUp() {
        return byPopUp;
    }

    public boolean getUserStatusChanged() {
        return userStatusChanged;
    }

    public boolean getEventDataChanged() {
        return eventDataChanged;
    }

    public boolean getEventCanceled() {
        return eventCanceled;
    }

    public boolean getUserWasUninvited() {
        return userWasUninvited;
    }

    public boolean getRemind1MinBefore() {
        return remind1MinBefore;
    }

    public boolean getRemind5MinBefore() {
        return remind5MinBefore;
    }

    public boolean getRemind10MinBefore() {
        return remind10MinBefore;
    }

    public void setByEmail(boolean byEmail) {
        this.byEmail = byEmail;
    }

    public void setByPopUp(boolean byPopUp) {
        this.byPopUp = byPopUp;
    }

    public void setUserStatusChanged(boolean userStatusChanged) {
        this.userStatusChanged = userStatusChanged;
    }

    public void setEventDataChanged(boolean eventDataChanged) {
        this.eventDataChanged = eventDataChanged;
    }

    public void setEventCanceled(boolean eventCanceled) {
        this.eventCanceled = eventCanceled;
    }

    public void setUserWasUninvited(boolean userWasUninvited) {
        this.userWasUninvited = userWasUninvited;
    }

    public void setRemind1MinBefore(boolean remind1MinBefore) {
        this.remind1MinBefore = remind1MinBefore;
    }

    public void setRemind5MinBefore(boolean remind5MinBefore) {
        this.remind5MinBefore = remind5MinBefore;
    }

    public void setRemind10MinBefore(boolean remind10MinBefore) {
        this.remind10MinBefore = remind10MinBefore;
    }

    public void setNotificationSettings(NotificationSettings notificationSettings){
        byEmail = notificationSettings.getByEmail();
        byPopUp = notificationSettings.getByPopUp();
        userStatusChanged = notificationSettings.getUserStatusChanged();
        eventDataChanged = notificationSettings.getEventDataChanged();
        eventCanceled = notificationSettings.getEventCanceled();
        userWasUninvited = notificationSettings.getUserWasUninvited();
        remind1MinBefore = notificationSettings.getRemind1MinBefore();
        remind5MinBefore = notificationSettings.getRemind5MinBefore();
        remind10MinBefore = notificationSettings.getRemind10MinBefore();
    }

    @Override
    public String toString() {
        return "Notification{" +
                "id=" + id +
                ", byEmail=" + byEmail +
                ", byPopUp=" + byPopUp +
                ", userStatusChanged=" + userStatusChanged +
                ", eventDataChanged=" + eventDataChanged +
                ", eventCanceled=" + eventCanceled +
                ", userWasUninvited=" + userWasUninvited +
                ", remind1MinBefore=" + remind1MinBefore +
                ", remind5MinBefore=" + remind5MinBefore +
                ", remind10MinBefore=" + remind10MinBefore +
                '}';
    }
}
