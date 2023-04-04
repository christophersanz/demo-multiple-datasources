package com.example.demomultipledatasources.dao.comercio;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.demomultipledatasources.model.comercio.Producto;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductoRepository extends JpaRepository<Producto, Integer> {

}
