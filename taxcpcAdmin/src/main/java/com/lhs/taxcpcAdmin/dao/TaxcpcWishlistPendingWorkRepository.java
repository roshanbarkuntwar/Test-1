package com.lhs.taxcpcAdmin.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.lhs.taxcpcAdmin.dao.generic.GenericCustomRepository;
import com.lhs.taxcpcAdmin.model.entity.TaxcpcWishlistPendingWork;

@Repository
public interface TaxcpcWishlistPendingWorkRepository extends JpaRepository<TaxcpcWishlistPendingWork, Long> ,GenericCustomRepository<Long,TaxcpcWishlistPendingWork>{

	@Query("from TaxcpcWishlistPendingWork t where t.work_type = :work_type")
	List<TaxcpcWishlistPendingWork> findByWorkType(String work_type);
}