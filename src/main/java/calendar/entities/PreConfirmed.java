package calendar.entities;

import calendar.utils.Token;

import javax.persistence.*;

@Entity
public class PreConfirmed{
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name = "token", nullable = false)
    private String token;
    @Column(name = "email", nullable = false)
    private String email;
    @Column(name = "password", nullable = false)
    private String password;

    public PreConfirmed() {
        this.token = Token.generate();
    }

    public PreConfirmed(String email, String password) {
        this.token = Token.generate();
        this.email = email;
        this.password = password;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Override
    public String toString() {
        return "PreConfirmed{" +
                "id=" + id +
                ", token='" + token + '\'' +
                ", email='" + email + '\'' +
                ", password='" + password + '\'' +
                '}';
    }
}