package br.ti.springboot.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import br.ti.springboot.model.User;

public interface UserRepository extends JpaRepository<User, Long>{
	
	
	@Query("SELECT u FROM User u WHERE u.id > :id")
	public List<User> findAllMoreThan(@Param("id") Long id);

//	buscando usando palavras reservadas do spring
	public List<User> findByIdGreaterThan(Long id);
	
	public List<User> findByNameIgnoreCase(String name);
	

}
