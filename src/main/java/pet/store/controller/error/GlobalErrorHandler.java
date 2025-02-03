package pet.store.controller.error;

import java.util.Map;
import java.util.NoSuchElementException;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import lombok.extern.slf4j.Slf4j;

@RestControllerAdvice
@Slf4j
public class GlobalErrorHandler {
	
	
	@ExceptionHandler(NoSuchElementException.class)
	@ResponseStatus(code = HttpStatus.NOT_FOUND)
	public Map<String, String> handleNoSuchElement(NoSuchElementException ex){
		log.error("Exception {}", ex.toString());
		return Map.of("message", ex.toString());
	}
	
	@ExceptionHandler(IllegalArgumentException.class)
	@ResponseStatus(code = HttpStatus.FORBIDDEN)
	public Map<String, String> handleIllegalArguement(IllegalArgumentException ex){
		log.error("Exception {}", ex.toString());
		return Map.of("message", ex.toString());
	}

}
