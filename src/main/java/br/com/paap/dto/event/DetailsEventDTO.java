package br.com.paap.dto.event;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;

import br.com.paap.dto.category.CategoryDTO;
import br.com.paap.model.Event;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
public class DetailsEventDTO {
	
	private Long id;
	private String picture;
	private String title;
	private String location;
	private String qrCode;
	private LocalDateTime dateTime;
	private Integer workload;
	private String description;
	private CategoryDTO category;
	private List<String> speakers = new ArrayList<>();
	private List<String> enrolled = new ArrayList<>();
	private List<String> attended = new ArrayList<>();
	
	public DetailsEventDTO(Event event) {
		this.category = CategoryDTO.builder()
				.id(event.getCategory().getId())
				.name(event.getCategory().getName())
				.urlImage(event.getCategory().getUrlImage())
				.build();
		this.dateTime = event.getDateTime();
		this.picture = event.getPicture();
		this.id = event.getId();
		this.location = event.getLocal();
		this.speakers = event.getSpeakers();
		this.qrCode = event.getQrCode();
		this.title= event.getTitle();
		this.enrolled = event.getUsers().stream()
				.map(eu -> eu.getUser().getName()).collect(Collectors.toList());
		this.attended = event.getUsers().stream().filter(eu -> eu.isUserPresent()).map(eu -> eu.getUser().getName()).collect(Collectors.toList());
		this.workload = event.getWorkload();
		this.description = event.getDescription();
	}
	
	public static Page<DetailsEventDTO> parse(Page<Event> pageEvent) {
		return pageEvent.map(DetailsEventDTO::new);
	}
	
	public static DetailsEventDTO parse(Event event) {
		var participants = new ArrayList<String>();
		event.getUsers().forEach(eu -> {
			participants.add(eu.getUser().getName());
		});
		return new DetailsEventDTO(event);
	}
	
}
