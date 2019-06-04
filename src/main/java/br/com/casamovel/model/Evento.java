package br.com.casamovel.model;

import java.sql.Date;


public class Evento {
	
		private int eventoId;
		private int categoriaCASa;
		private String titulo;
		private Date DataHorario;
		private String localizacao;
		private double cargaHoraria;
		private Date criadoEm;
		private Date AtualizadoEm;
		
		public int getId() {
			return eventoId;
		}
		public void setId(int id) {
			this.eventoId = id;
		}
		public int getCategoriaCASa() {
			return categoriaCASa;
		}
		public void setCategoriaCASa(int categoriaCASa) {
			this.categoriaCASa = categoriaCASa;
		}
		public String getTitulo() {
			return titulo;
		}
		public void setTitulo(String titulo) {
			this.titulo = titulo;
		}
		public Date getDataHorario() {
			return DataHorario;
		}
		public void setDataHorario(Date dataHorario) {
			DataHorario = dataHorario;
		}
		public String getLocalizacao() {
			return localizacao;
		}
		public void setLocalizacao(String localizacao) {
			this.localizacao = localizacao;
		}
		public double getCargaHoraria() {
			return cargaHoraria;
		}
		public void setCargaHoraria(double cargaHoraria) {
			this.cargaHoraria = cargaHoraria;
		}
		public Date getCriadoEm() {
			return criadoEm;
		}
		public void setCriadoEm(Date criadoEm) {
			this.criadoEm = criadoEm;
		}
		public Date getAtualizadoEm() {
			return AtualizadoEm;
		}
		public void setAtualizadoEm(Date atualizadoEm) {
			AtualizadoEm = atualizadoEm;
		}
		
		
}
