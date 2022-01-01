package br.com.casamovel.dto.usuario;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;

import br.com.casamovel.model.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Builder
@Getter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class UserDTO {
	public Long id;
	public String email;
	public Long cpf;
	public String name;
	public Integer workload;
	public String departament;
	public String telephone;
	public LocalDate entryDate;
	public String avatar;
	@Builder.Default
	public List<EventUserDTO> events = new ArrayList<>();
	
	public UserDTO(User user) {
		this.id = user.getId();
		this.name = user.getName();
		this.cpf = user.getCpf();
		this.email = user.getEmail();
		this.workload = user.getWorkload();
		this.departament = user.getDepartament();
		this.telephone = user.getPhone();
		this.setEntryDate(user.getEntryDate());
		this.setAvatar(user.getAvatar());
		var collect = user.getEvents().stream().map(eu -> new EventUserDTO(eu)).collect(Collectors.toList());
		this.events = collect;
	}
	

	public Long getId() {
		return id;
	}
	
	public void setId(Long id) {
		this.id = id;
	}
	
	public String getEmail() {
		return email;
	}
	
	public void setEmail(String email) {
		this.email = email;
	}
	
	public Long getCpf() {
		return cpf;
	}
	
	public void setCpf(Long cpf) {
		this.cpf = cpf;
	}
	
	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	

	public String getDepartament() {
		return departament;
	}

	public void setDepartament(String departament) {
		this.departament = departament;
	}

	public String getTelephone() {
		return telephone;
	}

	public void setTelephone(String telephone) {
		this.telephone = telephone;
	}
	
	public static Page<UserDTO> parse(Page<User> usuarios) {
		return usuarios.map(UserDTO::new);
	}
	
	public static UserDTO parse(User usuario) {
		return new UserDTO(usuario);
	}

	public Integer getWorkload() {
		return workload;
	}

	public void setWorkload(Integer workload) {
		this.workload = workload;
	}

	public LocalDate getEntryDate() {
		return entryDate;
	}

	public void setEntryDate(LocalDate entryDate) {
		this.entryDate = entryDate;
	}

	public String getAvatar() {
		return avatar;
	}

	public void setAvatar(String avatar) {
		this.avatar = avatar;
	}

	
	
	
	
	
}
