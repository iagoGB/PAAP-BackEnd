package br.com.casamovel.dto;

import java.time.LocalDateTime;
import java.time.LocalTime;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.PastOrPresent;

import com.fasterxml.jackson.annotation.JsonFormat;


public class NovoEventoDTO {
	@NotNull @NotEmpty
	private Long categoria;
	@NotNull @NotEmpty
	private String titulo;
	@NotNull @NotEmpty @PastOrPresent
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss.SSSXXX", timezone = "GMT-3")
	private LocalDateTime dataHorario;
	@NotNull @NotEmpty
	private String local;
	@NotNull @NotEmpty
	@JsonFormat(pattern = "HH:mm", timezone = "GMT-3")
	private LocalTime cargaHoraria;
	@NotNull @NotEmpty
	private String[] palestrantes;

	public Long getCategoria() {
		return categoria;
	}

	public void setCategoria(Long categoria) {
		this.categoria = categoria;
	}

	public String getTitulo() {
		return titulo;
	}

	public void setTitulo(String titulo) {
		this.titulo = titulo;
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

	public LocalTime getCargaHoraria() {
		return cargaHoraria;
	}

	public void setCargaHoraria(LocalTime cargaHoraria) {
		this.cargaHoraria = cargaHoraria;
	}

	public String[] getPalestrantes() {
		return palestrantes;
	}

	public void setPalestrantes(String[] palestrantes) {
		this.palestrantes = palestrantes;
	}
	
	
}
