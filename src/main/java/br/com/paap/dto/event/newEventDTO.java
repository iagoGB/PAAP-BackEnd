package br.com.paap.dto.event;

import java.time.LocalDateTime;
import java.util.List;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.PastOrPresent;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@NoArgsConstructor
public class NewEventDTO {
	@NotNull @NotEmpty
	private Long category;
	@NotNull @NotEmpty
	private String title;
	@NotNull @NotEmpty @PastOrPresent
	// @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss.SSSXXX", timezone = "GMT-3")
	private LocalDateTime dateTime;
	@NotNull @NotEmpty
	private String location;
	@NotNull @NotEmpty
	private int workload;
	@NotNull @NotEmpty
	private List<String> speakers;
	
}
