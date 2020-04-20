package org.itstep.msk.app.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.UNSUPPORTED_MEDIA_TYPE )
public class UnsupportedMediaTypeException extends RuntimeException {
}
