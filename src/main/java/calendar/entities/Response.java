package calendar.entities;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;

public class Response {
    private Object data;
    private String message;
    private LocalDateTime timeStamp;

    public Response(Object data, String message){
        this.data = data;
        this.message = message;
        this.timeStamp = LocalDateTime.now();
    }

    public Object getData() {
        return data;
    }

    public String getMessage() {
        return message;
    }

    public LocalDateTime getTimestamp() {
        return timeStamp;
    }

    public void setData(Object data) {
        this.data = data;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public void setTimestamp(LocalDateTime timeStamp) {
        this.timeStamp = timeStamp;
    }
}
