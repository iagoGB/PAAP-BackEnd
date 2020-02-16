package br.com.casamovel.dto.evento;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import br.com.casamovel.model.Evento;

public class DetalhesEventoDTO {
	
	private Long id;
	private String foto;
	private String titulo;
	private String local;
	private LocalDateTime dataHorario;
	private Integer cargaHoraria;
	private String categoria;
	private List<String> palestrantes = new ArrayList<>();
	private List<String> participantes = new ArrayList<>();
	
	public DetalhesEventoDTO() {
		
	}
	
	public DetalhesEventoDTO(Long id, String foto, String titulo, String local, LocalDateTime dataHorario,
			Integer cargaHoraria, String categoria, List<String> palestrantes,
			List<String> participantes) {
		super();
		this.id = id;
		this.foto = foto;
		this.titulo = titulo;
		this.local = local;
		this.dataHorario = dataHorario;
		this.cargaHoraria = cargaHoraria;
		this.categoria = categoria;
		this.palestrantes = palestrantes;
		this.participantes = participantes;
	}
	
	public static DetalhesEventoDTO parse(Evento evento) {
		List<String> palestrantes = new ArrayList<>();
		List<String> participantes = new ArrayList<>();
		evento.getPalestrantes().forEach( p -> 
		{
			palestrantes.add(p.getNome_palestrante_id().getNome());
		});
		evento.getUsuarios().forEach(u ->{
			participantes.add(u.getUsuario().getNome());
		});
		return new DetalhesEventoDTO
				(
						evento.getId(),
						evento.getFoto(),
						evento.getTitulo(),
						evento.getLocal(),
						evento.getDataHorario(),
						evento.getCargaHoraria(),
						evento.getCategoria().getNome(),
						palestrantes,
						participantes
						
				);
	}

	public Long getId() {
		return id;
	}

	public String getFoto() {
		return foto;
	}

	public String getTitulo() {
		return titulo;
	}

	public String getLocal() {
		return local;
	}

	public LocalDateTime getDataHorario() {
		return dataHorario;
	}

	public Integer getCargaHoraria() {
		return cargaHoraria;
	}

	public String getCategorias() {
		return categoria;
	}

	public List<String> getPalestrantes() {
		return palestrantes;
	}

	public List<String> getParticipantes() {
		return participantes;
	}
	
	
	
	
}
