package com.example.demomultipledatasources.dao.persona;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.demomultipledatasources.model.persona.Persona;
import org.springframework.stereotype.Repository;

@Repository
public interface PersonaRepository extends JpaRepository<Persona, Integer> {

}
