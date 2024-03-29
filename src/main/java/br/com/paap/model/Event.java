package br.com.paap.model;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.ForeignKey;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

import br.com.paap.dto.event.NewEventDTO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
public class Event implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	private String keyword;

	private String title;

	@Builder.Default
	private String picture = "https://fcdocente-teste.s3.sa-east-1.amazonaws.com/eventos/paap.png";

	private String qrCode;

	private LocalDateTime dateTime;

	private String local;

	private Integer workload;

	private String description;

	@ManyToOne
	@JoinColumn(name = "fk_categoria_id", referencedColumnName = "id", foreignKey = @ForeignKey(name = "fk_categoria_id"), nullable = false)
	private Category category;

	@OneToMany(mappedBy = "event", orphanRemoval = true) // nome da chave do outro lado do relacionamento
	@Builder.Default
	List<EventUser> users = new ArrayList<>();

	@ElementCollection
	@Builder.Default
	List<String> speakers = new ArrayList<>();

	public void parse(NewEventDTO eDto, Category category) {
		setTitle(eDto.getTitle());
		setDescription(eDto.getDescription());
		setCategory(category);
		setWorkload(eDto.getWorkload());
		setLocal(eDto.getLocation());
		setDateTime(eDto.getDateTime());
		category.getEvents().add(this);
		setSpeakers(eDto.getSpeakers());
	}

	public static Event parseFrom(NewEventDTO eDto, Category category) {
		var event = Event.builder()
				.title(eDto.getTitle())
				.description(eDto.getDescription())
				.category(category)
				.workload(eDto.getWorkload())
				.local(eDto.getLocation())
				.dateTime(eDto.getDateTime())
				.build();
		category.getEvents().add(event);
		event.setSpeakers(eDto.getSpeakers());
		return event;
	}
}
