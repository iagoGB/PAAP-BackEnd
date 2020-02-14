package br.com.casamovel.dto;

import br.com.casamovel.model.Palestrante;

public class PalestranteDTO {
	Long id;
	String nome;
	String desc;
	String foto;
	
	public PalestranteDTO(Long id, String nome, String desc, String foto) {
		super();
		this.id = id;
		this.nome = nome;
		this.desc = desc;
		this.foto = foto;
	}
	
	public PalestranteDTO(Palestrante palestrante) {
		this.id = palestrante.getId();
		this.foto = palestrante.getFoto();
		this.nome = palestrante.getNome();
		this.desc = palestrante.getDescricao();
	}
	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getNome() {
		return nome;
	}
	public void setNome(String nome) {
		this.nome = nome;
	}
	public String getDesc() {
		return desc;
	}
	public void setDesc(String desc) {
		this.desc = desc;
	}
	public String getFoto() {
		return foto;
	}
	public void setFoto(String foto) {
		this.foto = foto;
	}
	
	public static PalestranteDTO parse(Palestrante palestrante) {
		return new PalestranteDTO(palestrante);
	}
	
	
	
}
