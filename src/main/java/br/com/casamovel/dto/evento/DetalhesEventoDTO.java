package br.com.casamovel.dto.evento;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import br.com.casamovel.model.Evento;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
@Setter
@Getter
@NoArgsConstructor
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
	
	public DetalhesEventoDTO(Long id, String foto, String titulo, String local, String qrCode, LocalDateTime dataHorario,
			Integer cargaHoraria, String categoria, List<String> palestrantes,
			List<String> participantes) {
		super();
		this.id = id;
		this.foto = foto;
		this.titulo = titulo;
		this.localizacao = local;
		this.qrCode = qrCode;
		this.setData_horario(dataHorario);
		this.setCarga_horaria(cargaHoraria);
		this.categoria = categoria;
		this.palestrantes = palestrantes;
		this.participantes = participantes;
	}
	
	public static DetalhesEventoDTO parse(Evento evento) {
		var palestrantes = new ArrayList<String>();
		var participantes = new ArrayList<String>();
		evento.getPalestrantes().forEach( p -> {
			palestrantes.add(p.getNome_palestrante_id().getNome());
		});
		evento.getUsuarios().forEach(u -> {
			participantes.add(u.getUsuarioID().getNome());
		});
		return new DetalhesEventoDTO(
					evento.getId(),
					evento.getFoto(),
					evento.getTitulo(),
					evento.getLocal(),
					evento.getQrCode(),
					evento.getDataHorario(),
					evento.getCargaHoraria(),
					evento.getCategoria().getNome(),
					palestrantes,
					participantes		
				);
	}
	
}
