package br.com.casamovel.dto.evento;

import java.time.LocalDateTime;
import java.util.List;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.PastOrPresent;

import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@NoArgsConstructor
public class NovoEventoDTO {
	@NotNull @NotEmpty
	private Long categoria;
	@NotNull @NotEmpty
	private String titulo;
	@NotNull @NotEmpty @PastOrPresent
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss.SSSXXX", timezone = "GMT-3")
	private LocalDateTime data_horario;
	@NotNull @NotEmpty
	private String local;
	@NotNull @NotEmpty
	private int carga_horaria;
	@NotNull @NotEmpty
	private List<String> palestrantes;
	
}
