package br.com.casamovel.modelDAO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.casamovel.connection.ConnectionFactory;
import br.com.casamovel.model.Evento;

@RestController
@RequestMapping("/eventos")
public class EventoDAO {
	//Liste todos os eventos
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
				evento.setEvento_id(rs.getInt("evento_id"));
				evento.setCarga_horaria(rs.getDouble("carga_horaria"));
				evento.setData_horario(rs.getDate("data_horario"));
				evento.setLocalizacao(rs.getString("localizacao"));
				evento.setTitulo(rs.getString("titulo"));
				evento.setCategoria_evento(rs.getInt("categoria_evento"));
				evento.setPalestrante(getPalestrante(con, stmt, rs, evento.getEvento_id()));
				listaEvento.add(evento);
			}
			return listaEvento;	
		} catch (SQLException e) {
			throw new RuntimeException("Erro ao conectar",e);
		} finally {
			ConnectionFactory.closeConnection(con, stmt, rs);
		}
		
	}
	//Liste um evento único
	@GetMapping(value ="/{id}")
	public  Evento getEventoById(@PathVariable("id") int id) {
		
		Connection con = ConnectionFactory.getConnection();
		PreparedStatement stmt = null;
		ResultSet rs = null;
		Evento evento = new Evento();
		
		try {
			stmt = con.prepareStatement("SELECT * FROM evento WHERE evento_id = ?");
			stmt.setInt(1, id);
			rs= stmt.executeQuery();
			if (rs.next()) {
				//Jogue os dados para o objeto Evento
				evento.setEvento_id(rs.getInt("evento_id"));
				evento.setCarga_horaria(rs.getDouble("carga_horaria"));
				evento.setData_horario(rs.getDate("data_horario"));
				evento.setLocalizacao(rs.getString("localizacao"));
				evento.setTitulo(rs.getString("titulo"));
				evento.setCategoria_evento(rs.getInt("categoria_evento"));
			}
			//Percorrer a tabela de palestrantes e insira-os em um array no objeto Evento
			evento.setPalestrante(getPalestrante(con, stmt, rs, id));
			return evento;
		} catch (SQLException e) {
			throw new RuntimeException("Erro ao conectar",e);
		} finally {
			ConnectionFactory.closeConnection(con,stmt,rs);
		}
	}
	
	//Liste o(s) palestrante(s) do evento
	public ArrayList<String> getPalestrante(Connection con, PreparedStatement stmt,ResultSet rs, Integer id){
		ArrayList<String> listaPalestrante = new ArrayList<String>();
		
		try {
			stmt = con.prepareStatement("SELECT palestrante FROM evento AS e, evento_palestrante AS ep WHERE ep.evento_id = e.evento_id AND e.evento_id = ?");
			stmt.setInt(1, id);
			rs= stmt.executeQuery();
			while (rs.next()) {
				listaPalestrante.add(rs.getString("palestrante"));
			}
			return listaPalestrante;
		} catch (SQLException e) {
			System.err.println("Erro:" + e);
			return null;
		}
	}
	//Insira um novo evento
	@PostMapping(value="/novo")
	public boolean saveEvento ( @RequestBody Evento evento	){
		
		System.out.println("Evento: "+ evento);
		
		Connection con = ConnectionFactory.getConnection();
		PreparedStatement stmt = null;
		try {
			stmt = con.prepareStatement(
				"INSERT INTO evento (" + 
				"titulo, data_horario, carga_horaria, localizacao, categoria_evento, criado_em, atualizado_em)" + 
				"VALUES (?, ?, ?, ?, ?, ?, ?);"
			);
			stmt.setString(1, evento.getTitulo());
			stmt.setDate(2, evento.getData_horario());
			stmt.setDouble(3, evento.getCarga_horaria());
			stmt.setString(4, evento.getLocalizacao());
			stmt.setInt(5, evento.getCategoria_evento());
			stmt.setDate(6, evento.getCriado_em());
			stmt.setDate(7, evento.getAtualizado_em());
			
			stmt.executeUpdate();
			
			return true;
		} catch (SQLException e) {
			System.err.println("erro:"+ e);
			return false;	
		} finally {
			ConnectionFactory.closeConnection(con,stmt);
		}
		
	}
	//Atualize os dados de um evento
	@PutMapping(value="/editar/{id}")
	public void editEvento (
		@RequestBody Evento evento,
		@PathVariable("id") Integer id
	){
		
		System.out.println("Evento: "+ evento);
		
		Connection con = ConnectionFactory.getConnection();
		PreparedStatement stmt = null;
		try {
			stmt = con.prepareStatement("UPDATE evento SET " + 
					"titulo = ?, data_horario = ?, carga_horaria = ?, localizacao = ?, categoria_evento = ?, criado_em = ?, atualizado_em = ? "
					+ "WHERE evento_id =?");
			
			stmt.setString(1, evento.getTitulo());
			stmt.setDate(2,  evento.getData_horario());
			stmt.setDouble(3, evento.getCarga_horaria());
			stmt.setString(4, evento.getLocalizacao());
			stmt.setInt(5, evento.getCategoria_evento());
			stmt.setDate(6,  evento.getCriado_em());
			stmt.setDate(7,  evento.getAtualizado_em());
			stmt.setInt(8, id);
			
			stmt.executeUpdate();
			
			System.out.println("Parece que funcionou");
		} catch (SQLException e) {
			throw new RuntimeException("Foi diferente o erro", e);
		} finally {
			ConnectionFactory.closeConnection(con, stmt);
		}
		
	}
	//Delete um evento único
	@DeleteMapping(value="/deletar/{id}")
	public boolean deleteEvento (@PathVariable("id") Integer id){
		Connection con = ConnectionFactory.getConnection();
		PreparedStatement stmt = null;
		try {
			stmt = con.prepareStatement(" DELETE FROM evento WHERE evento_id = ?");
			stmt.setInt(1, id);
			stmt.executeUpdate();
			
			return true;
		} catch (SQLException e) {
			System.err.println("Erro:"+ e);
			return false;
		} finally {
			ConnectionFactory.closeConnection(con, stmt);
		}
		
	}
	
}
