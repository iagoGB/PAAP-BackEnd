package br.com.casamovel.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import br.com.casamovel.dto.evento.NovoEventoDTO;
import br.com.casamovel.repository.PalestranteRepository;
import lombok.EqualsAndHashCode;

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
@EqualsAndHashCode
@Entity
public class Evento implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	private String keyword;

	private Boolean estaAberto = true;

	private String titulo;

	private String foto;

	// @JsonFormat(shape = JsonFormat.Shape.STRING, pattern =
	// "yyyy-MM-dd'T'HH:mm:ss.SSSXXX", timezone = "GMT-3")
	private LocalDateTime dataHorario;

	private String local;

	// @JsonFormat(pattern = "HH:mm", timezone = "GMT-3")
	private int cargaHoraria;

	@ManyToOne
	@JoinColumn(name = "fk_categoria_id", referencedColumnName = "id", foreignKey = @ForeignKey(name = "fk_categoria_id"), nullable = false)
	private Categoria categoria;

	@OneToMany(mappedBy = "evento_id") // acho que aqui é evento ID
	List<EventoUsuario> usuarios = new ArrayList<EventoUsuario>();

	@OneToMany(mappedBy = "evento_id", cascade = { CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REFRESH })
	List<EventoPalestrante> palestrantes = new ArrayList<EventoPalestrante>();

	public Evento() {

	}

	public String getEventKeyword() {
		return keyword;
	}

	public void setEventKeyword(String eventKeyword) {
		this.keyword = eventKeyword;
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

	public void parse(NovoEventoDTO eDto, Categoria categoria, PalestranteRepository pr) {
		
        
        setFoto("default.png");
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
