package br.com.casamovel.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import br.com.casamovel.dto.evento.NovoEventoDTO;
import br.com.casamovel.repository.PalestranteRepository;

import java.io.Serializable;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.ForeignKey;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;


@JsonIgnoreProperties({ "hibernateLazyInitializer", "handler" })

@Entity
public class Evento implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE)
	private Long id;
	
	private Boolean estaAberto = true;
	
	private String titulo;

	private String foto;

	//@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss.SSSXXX", timezone = "GMT-3")
	private LocalDateTime dataHorario;

	private String local;

	//@JsonFormat(pattern = "HH:mm", timezone = "GMT-3")
	private int cargaHoraria;

	@ManyToOne
	@JoinColumn(
		name = "fk_categoria_id", 
		referencedColumnName = "id", 
		foreignKey = @ForeignKey(name = "fk_categoria_id"), 
		nullable = false
	)
	private Categoria categoria;

	@OneToMany(mappedBy = "usuario_id")
	List<EventoUsuario> usuarios = new ArrayList<EventoUsuario>();

	@OneToMany(mappedBy = "evento_id", cascade = {CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REFRESH })
	List<EventoPalestrante> palestrantes = new ArrayList<EventoPalestrante>();

	public Evento() {

	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getTitulo() {
		return titulo;
	}

	public void setTitulo(String titulo) {
		this.titulo = titulo;
	}

	public String getFoto() {
		return foto;
	}

	public void setFoto(String foto) {
		this.foto = foto;
	}

	public LocalDateTime getDataHorario() {
		return dataHorario;
	}

	public void setDataHorario(LocalDateTime dataHorario) {
		this.dataHorario = dataHorario;
	}

	public String getLocal() {
		return local;
	}

	public void setLocal(String local) {
		this.local = local;
	}

	public Integer getCargaHoraria() {
		return cargaHoraria;
	}

	public void setCargaHoraria(int cargaHoraria) {
		this.cargaHoraria = cargaHoraria;
	}

	public Categoria getCategoria() {
		return categoria;
	}

	public void setCategoria(Categoria categoria) {
		this.categoria = categoria;
	}

	public List<EventoUsuario> getUsuarios() {
		return usuarios;
	}

	public void setUsuarios(List<EventoUsuario> usuarios) {
		this.usuarios = usuarios;
	}

	public List<EventoPalestrante> getPalestrantes() {
		return palestrantes;
	}

	public void setPalestrantes(List<EventoPalestrante> palestrantes) {
		this.palestrantes = palestrantes;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + cargaHoraria;
		result = prime * result + ((categoria == null) ? 0 : categoria.hashCode());
		result = prime * result + ((dataHorario == null) ? 0 : dataHorario.hashCode());
		result = prime * result + ((foto == null) ? 0 : foto.hashCode());
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		result = prime * result + ((local == null) ? 0 : local.hashCode());
		result = prime * result + ((palestrantes == null) ? 0 : palestrantes.hashCode());
		result = prime * result + ((titulo == null) ? 0 : titulo.hashCode());
		result = prime * result + ((usuarios == null) ? 0 : usuarios.hashCode());
		return result;
	}

	
	
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Evento other = (Evento) obj;
		if (cargaHoraria != other.cargaHoraria)
			return false;
		if (categoria == null) {
			if (other.categoria != null)
				return false;
		} else if (!categoria.equals(other.categoria))
			return false;
		if (dataHorario == null) {
			if (other.dataHorario != null)
				return false;
		} else if (!dataHorario.equals(other.dataHorario))
			return false;
		if (foto == null) {
			if (other.foto != null)
				return false;
		} else if (!foto.equals(other.foto))
			return false;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		if (local == null) {
			if (other.local != null)
				return false;
		} else if (!local.equals(other.local))
			return false;
		if (palestrantes == null) {
			if (other.palestrantes != null)
				return false;
		} else if (!palestrantes.equals(other.palestrantes))
			return false;
		if (titulo == null) {
			if (other.titulo != null)
				return false;
		} else if (!titulo.equals(other.titulo))
			return false;
		if (usuarios == null) {
			if (other.usuarios != null)
				return false;
		} else if (!usuarios.equals(other.usuarios))
			return false;
		return true;
	}

	public void parse(NovoEventoDTO eDto, Categoria categoria, PalestranteRepository pr) {
		
        
        setFoto("Caminho da foto aqui");
        setTitulo(eDto.getTitulo());
        setCategoria(categoria);
        setCargaHoraria(eDto.getCarga_horaria());
        setLocal(eDto.getLocal());
        setDataHorario(eDto.getData_horario());
        setEstaAberto(true);
        categoria.getEventos().add(this);
        //Relação palestrante e evento
        eDto.getPalestrantes().forEach( nome -> 
        {
        	Palestrante palestrante = pr.findByNome(nome);
        	EventoPalestrante ep = new EventoPalestrante();
        	ep.setEvento_id(this);
        	ep.setNome_palestrante_id(palestrante);
        	this.palestrantes.add(ep);
        	palestrante.getEventos().add(ep);
        	System.out.println("Terminou de executar  kkj");
        	
        });
	}

	public Boolean getEstaAberto() {
		return estaAberto;
	}

	public void setEstaAberto(Boolean estaAberto) {
		this.estaAberto = estaAberto;
	}
	
}
