package com.lhs.taxcpcAdmin.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.lhs.taxcpcAdmin.model.entity.ViewClientMast;

@Repository
public interface ViewClientMastRepository extends JpaRepository<ViewClientMast, String> {

}
