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

	@Builder.Default
	private Boolean isOpen = true;

	private String title;

	@Builder.Default
	private String picture = "https://fcdocente-teste.s3.sa-east-1.amazonaws.com/eventos/paap.png";

	private String qrCode;

	private LocalDateTime dateTime;

	private String local;

	private Integer workload;

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

		setPicture("default.png");
		setTitle(eDto.getTitle());
		setCategory(category);
		setWorkload(eDto.getWorkload());
		setLocal(eDto.getLocation());
		setDateTime(eDto.getDateTime());
		setIsOpen(true);
		category.getEvents().add(this);
		setSpeakers(eDto.getSpeakers());
	}

	public static Event parseFrom(NewEventDTO eDto, Category category) {
		var event = Event.builder()
				.picture("default.png")
				.title(eDto.getTitle())
				.category(category)
				.workload(eDto.getWorkload())
				.local(eDto.getLocation())
				.dateTime(eDto.getDateTime())
				.isOpen(true)
				.build();
		category.getEvents().add(event);
		event.setSpeakers(eDto.getSpeakers());
		return event;
	}
}
