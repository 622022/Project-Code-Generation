package io.swagger.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonValue;
import org.springframework.http.HttpStatus;
import org.springframework.lang.Nullable;
import org.springframework.validation.annotation.Validated;

import javax.persistence.Entity;


public class JSONResponse<T> {


    @Nullable
    @JsonProperty("body")
    private final T body;
    @Nullable
    @JsonProperty("UserMessage")
    private final T UserMessage;

    public JSONResponse(@Nullable T body, @Nullable T userMessage) {
        this.body = body;
        UserMessage = userMessage;
    }
    @Override
    public String toString() {
        return  "{" +
                "body: " + body +
                ", UserMessage:" + UserMessage +
                '}';
    }

    @Nullable
    public T getBody() {
        return body;
    }

    @Nullable
    public T getUserMessage() {
        return UserMessage;
    }

    static public class UserMessage{
       @Override
       public String toString() {
           return "UserMessage{" +
                   "Text:'" + Text + '\'' +
                   ", Status:" + Status +
                   ", IsSuccessful:" + IsSuccessful +
                   '}';
       }

        public String Text;
        public HttpStatus Status;
        public Boolean IsSuccessful;

        public UserMessage() {
        }

        public UserMessage(String text, HttpStatus status, Boolean isSuccessful) {
            Text = text;
            Status = status;
            IsSuccessful = isSuccessful;
        }
        @JsonProperty("Text")
        public String getText() {
            return Text;
        }

        public void setText(String text) {
            Text = text;
        }
        @JsonProperty("Status")
        public HttpStatus getStatus() {
            return Status;
        }

        public void setStatus(HttpStatus status) {
            Status = status;
        }
        @JsonProperty("IsSuccessful")
        public Boolean getSuccessful() {
            return IsSuccessful;
        }

        public void setSuccessful(Boolean successful) {
            IsSuccessful = successful;
        }
    }
}
