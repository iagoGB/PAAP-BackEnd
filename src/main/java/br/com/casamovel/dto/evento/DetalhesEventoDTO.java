package br.com.casamovel.dto.evento;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;

import br.com.casamovel.model.Event;
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
public class DetalhesEventoDTO {
	
	private Long id;
	private String picture;
	private String title;
	private String location;
	private String qrCode;
	private LocalDateTime dateTime;
	private Integer workload;
	private String category;
	private List<String> speakers = new ArrayList<>();
	private List<String> enrolled = new ArrayList<>();
	
//	public DetalhesEventoDTO(Long id, String foto, String titulo, String local, String qrCode, LocalDateTime dataHorario,
//			Integer cargaHoraria, String categoria, List<String> palestrantes,
//			List<String> participantes) {
//		super();
//		this.id = id;
//		this.foto = foto;
//		this.titulo = titulo;
//		this.localizacao = local;
//		this.qrCode = qrCode;
//		this.setData_horario(dataHorario);
//		this.setCarga_horaria(cargaHoraria);
//		this.categoria = categoria;
//		this.palestrantes = palestrantes;
//		this.participantes = participantes;
//	}
	
	public DetalhesEventoDTO(Event event) {
		this.category = event.getCategory().getName();
		this.dateTime = event.getDateTime();
		this.picture = event.getPicture();
		this.id = event.getId();
		this.location = event.getLocal();
		this.speakers = event.getSpeakers();
		this.qrCode = event.getQrCode();
		this.title= event.getTitle();
		this.enrolled = event.getUsers().stream()
				.map(eu -> eu.getUser().getName()).collect(Collectors.toList());
		this.workload = event.getWorkload();
	}
	
	public static Page<DetalhesEventoDTO> parse(Page<Event> pageEvent) {
		return pageEvent.map(DetalhesEventoDTO::new);
	}
	
	public static DetalhesEventoDTO parse(Event event) {
		// TODO - Refatorar for each para map. Tratar momentos em que lista de usuarios vem nulo.
		var participants = new ArrayList<String>();
		event.getUsers().forEach(eu -> {
			participants.add(eu.getUser().getName());
		});
		return new DetalhesEventoDTO(
					event.getId(),
					event.getPicture(),
					event.getTitle(),
					event.getLocal(),
					event.getQrCode(),
					event.getDateTime(),
					event.getWorkload(),
					event.getCategory().getName(),
					event.getSpeakers(),
					participants		
				);
	}
	
}
