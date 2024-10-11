package in.scrapeco.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.UNAUTHORIZED)
public class UnauthorizedException extends RuntimeException {
    String newKey = "NA";

    public UnauthorizedException(String unauthorized) {
        super(unauthorized);
    }
}
