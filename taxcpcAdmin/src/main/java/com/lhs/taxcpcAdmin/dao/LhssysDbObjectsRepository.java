package com.lhs.taxcpcAdmin.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.lhs.taxcpcAdmin.dao.generic.GenericCustomRepository;
import com.lhs.taxcpcAdmin.model.entity.LhssysDbObjects;

@Repository
public interface LhssysDbObjectsRepository extends JpaRepository<LhssysDbObjects, String>,GenericCustomRepository<Long,LhssysDbObjects> {

}
