package br.com.casamovel.dto.evento;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import br.com.casamovel.model.Evento;

public class DetalhesEventoDTO {
	
	private Long id;
	private String foto;
	private String titulo;
	private String localizacao;
	private LocalDateTime data_horario;
	private Integer carga_horaria;
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
		this.localizacao = local;
		this.setData_horario(dataHorario);
		this.setCarga_horaria(cargaHoraria);
		this.categoria = categoria;
		this.palestrantes = palestrantes;
		this.participantes = participantes;
	}
	
	public static DetalhesEventoDTO parse(Evento evento) {
		List<String> palestrantes = new ArrayList<>();
		List<String> participantes = new ArrayList<>();
		System.out.println("kk "+evento.getPalestrantes());
		evento.getPalestrantes().forEach( p -> 
		{
			palestrantes.add(p.getNome_palestrante_id().getNome());
		});
		evento.getUsuarios().forEach(u ->{
			participantes.add(u.getUsuario_id().getNome());
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

	public String getCategorias() {
		return categoria;
	}

	public List<String> getPalestrantes() {
		return palestrantes;
	}

	public List<String> getParticipantes() {
		return participantes;
	}

	public LocalDateTime getData_horario() {
		return data_horario;
	}

	public void setData_horario(LocalDateTime data_horario) {
		this.data_horario = data_horario;
	}

	public Integer getCarga_horaria() {
		return carga_horaria;
	}

	public void setCarga_horaria(Integer carga_horaria) {
		this.carga_horaria = carga_horaria;
	}

	public String getLocalizacao() {
		return localizacao;
	}

	public void setLocalizacao(String localizacao) {
		this.localizacao = localizacao;
	}
	
	
	
	
}
