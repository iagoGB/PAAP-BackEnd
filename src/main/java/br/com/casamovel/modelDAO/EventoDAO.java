package br.com.casamovel.modelDAO;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import br.com.casamovel.connection.ConnectionFactory;
import br.com.casamovel.model.Evento;

@RestController
@RequestMapping("/eventos")
public class EventoDAO {
	
	@GetMapping
	public ArrayList<Evento> getEvento() {
		Connection con = ConnectionFactory.getConnection();
		PreparedStatement stmt = null;
		ResultSet rs = null;
		ArrayList<Evento> listaEvento = new ArrayList<Evento>(); 
		
		try {
			stmt = con.prepareStatement("SELECT * FROM evento");
			rs= stmt.executeQuery();
			while (rs.next()) {
				Evento evento = new Evento();
				evento.setId(rs.getInt("evento_id"));
				evento.setCargaHoraria(rs.getDouble("carga_horaria"));
				evento.setDataHorario(rs.getDate("data_horario"));
				evento.setLocalizacao(rs.getString("localizacao"));
				evento.setTitulo(rs.getString("titulo"));
				evento.setCategoriaCASa(rs.getInt("categoria_casa"));
				listaEvento.add(evento);
			}
		} catch (SQLException e) {
			throw new RuntimeException("Erro ao conectar",e);
		}
		return listaEvento;	
	}
	
	@GetMapping(value ="/{id}")
	public  ArrayList<Evento> getEventoById(@PathVariable("id") Integer id) {
		
		Connection con = ConnectionFactory.getConnection();
		PreparedStatement stmt = null;
		ResultSet rs = null;
		ArrayList<Evento> listaEvento = new ArrayList<Evento>(); 
		
		try {
			stmt = con.prepareStatement("SELECT * FROM evento WHERE evento_id ="+id);
			rs= stmt.executeQuery();
			while (rs.next()) {
				Evento evento = new Evento();
				evento.setId(rs.getInt("evento_id"));
				evento.setCargaHoraria(rs.getDouble("carga_horaria"));
				evento.setDataHorario(rs.getDate("data_horario"));
				evento.setLocalizacao(rs.getString("localizacao"));
				evento.setTitulo(rs.getString("titulo"));
				evento.setCategoriaCASa(rs.getInt("categoria_casa"));
				listaEvento.add(evento);
			}
		} catch (SQLException e) {
			throw new RuntimeException("Erro ao conectar",e);
		}
		return listaEvento;
		
	}
	
	@PostMapping(value="/novo")
	public void saveEvento (@RequestBody Evento evento){
		Connection con = ConnectionFactory.getConnection();
		PreparedStatement stmt = null;
		try {
			stmt = con.prepareStatement("INSERT INTO evento (" + 
					"	 titulo, data_horario, carga_horaria, localizacao, categoria_casa, criado_em, atualizado_em)" + 
					"	VALUES (?, ?, ?, ?, ?, ?, ?);");
			stmt.setString(1, evento.getTitulo());
			stmt.setDate(2, (Date) evento.getDataHorario());
			stmt.setDouble(3, evento.getCargaHoraria());
			stmt.setString(4, evento.getLocalizacao());
			stmt.setInt(5, evento.getCategoriaCASa());
			stmt.setDate(6, (Date) evento.getCriadoEm());
			stmt.setDate(7, (Date) evento.getAtualizadoEm());
			
			stmt.executeUpdate();
			
			System.out.println("Parece que funcionou");
		} catch (SQLException e) {
			throw new RuntimeException("Foi diferente o erro", e);
		}
		
	}
}
