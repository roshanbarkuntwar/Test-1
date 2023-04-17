package com.lhs.taxcpcAdmin.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.lhs.taxcpcAdmin.model.entity.ClientMast;

@Repository
public interface ClientMastRepository extends JpaRepository<ClientMast, String> {

	public ClientMast findByLoginId(String loginId);
}
