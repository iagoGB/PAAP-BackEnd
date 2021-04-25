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
	private String foto;
	private String titulo;
	private String localizacao;
	private String qrCode;
	private LocalDateTime data_horario;
	private Integer carga_horaria;
	private String categoria;
	private List<String> palestrantes = new ArrayList<>();
	private List<String> participantes = new ArrayList<>();
	
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
		this.categoria = event.getCategory().getName();
		this.data_horario = event.getDateTime();
		this.foto = event.getPicture();
		this.id = event.getId();
		this.localizacao = event.getLocal();
		this.palestrantes = event.getSpeakers();
		this.qrCode = event.getQrCode();
		this.titulo = event.getTitle();
		this.participantes = event.getUsers().stream()
				.map(eu -> eu.getUser().getName()).collect(Collectors.toList());
		this.carga_horaria = event.getWorkload();
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
