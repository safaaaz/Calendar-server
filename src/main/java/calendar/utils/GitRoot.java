package calendar.utils;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;

public class GitRoot {
    public String access_token;
    public String token_type;
    public String scope;
    public GitRoot(){}
    public GitRoot(String access_token, String token_type, String scope) {
        this.access_token = access_token;
        this.token_type = token_type;
        this.scope = scope;
    }
    public String getEmailFromToken(){
        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization","Bearer "+access_token);
        HttpEntity<String> entity = new HttpEntity<>(null,headers);
        String url="https://api.github.com/user/emails";
        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<GitEmails[]> response = null;
        String email;
        try {
            response = restTemplate.exchange(url, HttpMethod.GET,entity, GitEmails[].class);
            email = Arrays.stream(response.getBody()).filter(element -> element.primary==true).findFirst().get().getEmail();
            System.out.println(email);
        } catch (RestClientException e) {
            throw new RuntimeException(e);
        }
        return email;
    }
    public String getAccess_token() {
        return access_token;
    }

    public String getToken_type() {
        return token_type;
    }

    public String getScope() {
        return scope;
    }

    public void setAccess_token(String access_token) {
        this.access_token = access_token;
    }

    public void setToken_type(String token_type) {
        this.token_type = token_type;
    }

    public void setScope(String scope) {
        this.scope = scope;
    }
}

