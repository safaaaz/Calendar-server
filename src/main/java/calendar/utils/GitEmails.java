package calendar.utils;

public class GitEmails {
    public String email;
    public boolean primary;
    public boolean verified;
    public String visibility;
    GitEmails(){}

    public GitEmails(String email, boolean primary, boolean verified, String visibility) {
        this.email = email;
        this.primary = primary;
        this.verified = verified;
        this.visibility = visibility;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setPrimary(boolean primary) {
        this.primary = primary;
    }

    public void setVerified(boolean verified) {
        this.verified = verified;
    }

    public void setVisibility(String visibility) {
        this.visibility = visibility;
    }

    public String getEmail() {
        return email;
    }

    public boolean isPrimary() {
        return primary;
    }

    public boolean isVerified() {
        return verified;
    }

    public String getVisibility() {
        return visibility;
    }
}
