package br.com.casamovel.models;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.CollectionTable;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
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
	private Long eventoId;
	private String titulo;
	@ManyToOne
	@JoinColumn(name="categoria_id", nullable = false)
	private Categoria categoria;
	@ElementCollection
	@CollectionTable (name="evento_palestrante", joinColumns=@JoinColumn(name="evento_id"))
	List<String> palestrantes = new ArrayList<String>();
	@ManyToMany
	@JoinTable (
		name = "usuario_evento",
		joinColumns= @JoinColumn(name = "evento_id"),
		inverseJoinColumns = @JoinColumn(name ="usuario_id")
	)
	List<Usuario> eventos = new ArrayList<Usuario>();
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
}
