package com.educandoweb.course.services;

import java.util.List;
import java.util.Optional;

import javax.persistence.EntityNotFoundException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;

import com.educandoweb.course.entities.User;
import com.educandoweb.course.repositories.UserRepository;
import com.educandoweb.course.services.exceptions.DatabaseException;
import com.educandoweb.course.services.exceptions.ResourceNotFoundException;

@Service
public class UserService {

	@Autowired
	private UserRepository repository;
	
	
	public List<User> findAll(){
		return repository.findAll();
	}
	
	//Buscar usuário por ID
	public User findById(Long id) {
		
		Optional<User> obj = repository.findById(id);
		
		//Tratamento de Exceção para erro no FindById
		return obj.orElseThrow(()-> new ResourceNotFoundException(id));
	}

	//Inserir usuário
	public User insert(User obj) {
		return repository.save(obj);
	}
	
	
	//Deletar Usuário
	public void delete(Long id) {
		try {
		repository.deleteById(id);
		} 
		//Tratamento 404 para busca por id
		catch (EmptyResultDataAccessException e) {
			throw new ResourceNotFoundException(id);
		} 
		//Tratamento 404 bad gateway para delete
		catch (DataIntegrityViolationException e) {
			throw new DatabaseException(e.getMessage());
		}
	}
	
	
	//Atualizar usuário
	public User update(Long id, User obj) {
		try {
		User entity = repository.getOne(id);
		
		updateData(entity,obj);
		
		return repository.save(entity);
		} catch (EntityNotFoundException e) {
			throw new ResourceNotFoundException(id);
		}
	}


	private void updateData(User entity, User obj) {
		entity.setName(obj.getName());
		entity.setEmail(obj.getEmail());
		entity.setPhone(obj.getPhone());
	}
	
}
