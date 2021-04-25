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
	private Long category;
	@NotNull @NotEmpty
	private String title;
	@NotNull @NotEmpty @PastOrPresent
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss.SSSXXX", timezone = "GMT-3")
	private LocalDateTime dateTime;
	@NotNull @NotEmpty
	private String local;
	@NotNull @NotEmpty
	private int workload;
	@NotNull @NotEmpty
	private List<String> speakers;
	
}
