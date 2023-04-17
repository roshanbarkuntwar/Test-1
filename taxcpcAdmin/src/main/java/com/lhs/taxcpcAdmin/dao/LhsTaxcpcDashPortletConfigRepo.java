package com.lhs.taxcpcAdmin.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.lhs.taxcpcAdmin.model.entity.LhsTaxcpcDashPortletConfig;
import com.lhs.taxcpcAdmin.model.entity.LhsTaxcpcDashPortletConfigID;


@Repository
public interface LhsTaxcpcDashPortletConfigRepo  extends JpaRepository<LhsTaxcpcDashPortletConfig, Long>{
	

}
