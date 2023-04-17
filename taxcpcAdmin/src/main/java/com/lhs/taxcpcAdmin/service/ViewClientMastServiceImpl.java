package com.lhs.taxcpcAdmin.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.lhs.taxcpcAdmin.dao.ViewClientMastRepository;
import com.lhs.taxcpcAdmin.model.entity.ViewClientMast;

@Service
public class ViewClientMastServiceImpl implements ViewClientMastService {

	@Autowired
	private ViewClientMastRepository clientMastRepository;

	@Override
	public ViewClientMast getClientById(String client_code) {
		// TODO Auto-generated method stub

		return clientMastRepository.findById(client_code).get();
	}

}
