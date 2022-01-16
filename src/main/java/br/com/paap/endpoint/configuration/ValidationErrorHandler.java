package br.com.paap.endpoint.configuration;

import java.util.ArrayList;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import br.com.paap.dto.ErrorDTO;
import br.com.paap.dto.ValidationErrorDTO;

@RestControllerAdvice
public class ValidationErrorHandler {
	
	// Por padrão spring retorna 200 pq supõe que estamos tratando erros.
	// Então é necessário colocar o codigo de resposta com 400
	@ResponseStatus(code = HttpStatus.BAD_REQUEST)
	@ExceptionHandler(MethodArgumentNotValidException.class)
	public List<ValidationErrorDTO> handler(MethodArgumentNotValidException exception) {
		
		List<FieldError> fieldErrors = exception.getBindingResult().getFieldErrors();
		List<ValidationErrorDTO> listaErros = new ArrayList<ValidationErrorDTO>();
		fieldErrors.forEach(e ->{
			ValidationErrorDTO erroValidacaoDTO = new ValidationErrorDTO(e.getField(),e.getDefaultMessage());
			listaErros.add(erroValidacaoDTO);
		});
		return listaErros;	
	}

	@ResponseStatus(code = HttpStatus.NOT_FOUND)
	@ExceptionHandler(RuntimeException.class)
	public ResponseEntity<?> handler(RuntimeException exception) {
		var erroDTO = new ErrorDTO(HttpStatus.NOT_FOUND.value(), exception.getClass().getName(), exception.getMessage());	
		return ResponseEntity.status(HttpStatus.NOT_FOUND).body(erroDTO);
	}

	@ResponseStatus(code = HttpStatus.BAD_REQUEST)
	@ExceptionHandler(IllegalArgumentException.class)
	public ValidationErrorDTO handler(IllegalArgumentException ex){
		return new ValidationErrorDTO("erro", ex.getMessage());
	}
}
