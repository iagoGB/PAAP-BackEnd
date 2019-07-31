package br.com.casamovel.models;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

@Entity
@Table(name="categoria")
public class Categoria implements Serializable {
	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE )
	private long categoria_id;
	private String nome;
	@OneToMany(mappedBy="categoria")
	List<Evento> eventos = new ArrayList<Evento>();
	
	public void setCategoria_id(long categoria_id) {
		this.categoria_id = categoria_id;
	}

	public Categoria() {
	}
	
	public Categoria (String nome) {
		this.nome = nome;
	}
	
	public long getCategoria_id() {
		return categoria_id;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}
	
	public List<Evento> getEventos() {
		return eventos;
	}
	
	public void setEventos(List<Evento> eventos) {
		this.eventos = eventos;
	}

	
	
}
