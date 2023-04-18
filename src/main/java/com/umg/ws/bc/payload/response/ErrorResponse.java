package com.umg.ws.bc.payload.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatus;

import javax.xml.bind.annotation.XmlRootElement;
import java.util.Date;
import java.util.List;

@XmlRootElement(name = "error")
@Setter
@Getter
public class ErrorResponse {
    // customizing timestamp serialization format
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss",  timezone = "America/Guatemala")
    private Date timeStamp;
    private int code;
    private String status;
    //General error message about nature of error
    private String message;

    private String path;
    private List<String> details;
    public ErrorResponse(){
        timeStamp = new Date();
    }
    public ErrorResponse(HttpStatus httpStatus, String message, List<String> details) {
        this();
        this.code = httpStatus.value();
        this.status = httpStatus.name();
        this.message = message;
        this.details = details;
    }

    public ErrorResponse(HttpStatus httpStatus, String message, List<String> details, String path) {
        this(httpStatus, message, details);
        this.path = path;
    }
}
