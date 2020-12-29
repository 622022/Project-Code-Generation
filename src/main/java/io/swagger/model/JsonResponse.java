package io.swagger.model;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonRootName;
import com.fasterxml.jackson.annotation.JsonValue;
import org.springframework.http.HttpStatus;
import org.springframework.lang.Nullable;
import org.springframework.validation.annotation.Validated;

import javax.persistence.Entity;

@JsonAutoDetect(
        fieldVisibility = JsonAutoDetect.Visibility.ANY,
        getterVisibility = JsonAutoDetect.Visibility.NONE,
        setterVisibility = JsonAutoDetect.Visibility.NONE,
        creatorVisibility = JsonAutoDetect.Visibility.NONE
)
public class JsonResponse<T> {

    @Nullable
    @JsonProperty("Data")
    private final T body;

    @Nullable
    @JsonProperty("Response")
    private final T UserMessage;

    public JsonResponse(@Nullable T body, @Nullable T userMessage) {
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
        return this.body;
    }

    @Nullable
    public T getUserMessage() {
        return this.UserMessage;
    }


    static public class UserMessage{


        public String Text;
        public HttpStatus Status;
        public Boolean IsSuccessful;

        public UserMessage() {
            Text = "Handled";
            Status = HttpStatus.OK;
            IsSuccessful = true;
        }
        public UserMessage(HttpStatus status) {
            Text = "Handled";
            Status = status;
            IsSuccessful = true;
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
