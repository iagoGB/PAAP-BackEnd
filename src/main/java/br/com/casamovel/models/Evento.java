package br.com.casamovel.models;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.CollectionTable;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;


@Entity
public class Evento implements Serializable {
	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE )
	private Long evento_id;
	
	private String titulo;
	
	@ManyToOne
	@JoinColumn(name="categoria_id", nullable = false)
	private Categoria categoria;
	
	@ElementCollection
	@CollectionTable(name = "evento_palestrantes",joinColumns = @JoinColumn(name="evento_id"))
	private List<String> palestrantes;
	
	@ManyToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
	@JoinTable (
		name = "usuario_evento",
		joinColumns= @JoinColumn(name = "evento_id"),
		inverseJoinColumns = @JoinColumn(name ="usuario_id")
	)
	List<Usuario> usuarios = new ArrayList<Usuario>();
	
	public Long getEvento_id() {
		return evento_id;
	}
	public void setEvento_id(Long evento_id) {
		this.evento_id = evento_id;
	}
	public List<Usuario> getUsuarios() {
		return usuarios;
	}
	public void setUsuarios(List<Usuario> usuarios) {
		this.usuarios = usuarios;
	}
	public Evento() {
		
	}
	public void setTitulo(String titulo) {
		this.titulo = titulo;
	}
	
	public String getTitulo() {
		return titulo;
	}
	
	public Categoria getCategoria() {
		return categoria;
	}
	
	public void setCategoria(Categoria categoria) {
		this.categoria = categoria;
	}
	public List<String> getPalestrantes() {
		return palestrantes;
	}
	public void setPalestrantes(List<String> palestrantes) {
		this.palestrantes = palestrantes;
	}
}
