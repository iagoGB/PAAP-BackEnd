package br.com.paap.dto.event;

import java.time.LocalDateTime;
import java.util.List;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.PastOrPresent;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class UpdateEventDTO {
    Long id;
    @NotNull
	@NotEmpty
	private Long category;
	@NotNull
	@NotEmpty
	private String title;
	private String description;
	@NotNull
	@NotEmpty
	@PastOrPresent
	private LocalDateTime dateTime;
	@NotNull
	@NotEmpty
	private String location;
	@NotNull
	@NotEmpty
	private int workload;
	@NotNull
	@NotEmpty
	private List<String> speakers;
}
