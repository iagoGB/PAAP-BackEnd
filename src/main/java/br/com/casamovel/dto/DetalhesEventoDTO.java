package br.com.casamovel.dto;

import java.time.LocalTime;
import java.util.List;

public class DetalhesEventoDTO {
	
	private Long id;
	private String foto;
	private String titulo;
	private String local;
	private LocalTime dataHorario;
	private LocalTime cargaHoraria;
	private List<String> categorias;
	private List<String> palestrantes;
	private List<String> participantes;
	
	public DetalhesEventoDTO() {
		
	}
	
	public DetalhesEventoDTO(Long id, String foto, String titulo, String local, LocalTime dataHorario,
			LocalTime cargaHoraria, List<String> categorias, List<String> palestrantes,
			List<String> participantes) {
		super();
		this.id = id;
		this.foto = foto;
		this.titulo = titulo;
		this.local = local;
		this.dataHorario = dataHorario;
		this.cargaHoraria = cargaHoraria;
		this.categorias = categorias;
		this.palestrantes = palestrantes;
		this.participantes = participantes;
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

	public LocalTime getDataHorario() {
		return dataHorario;
	}

	public LocalTime getCargaHoraria() {
		return cargaHoraria;
	}

	public List<String> getCategorias() {
		return categorias;
	}

	public List<String> getPalestrantes() {
		return palestrantes;
	}

	public List<String> getParticipantes() {
		return participantes;
	}
	
	
}
