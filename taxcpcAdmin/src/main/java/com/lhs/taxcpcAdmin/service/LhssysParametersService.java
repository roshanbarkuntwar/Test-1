package com.lhs.taxcpcAdmin.service;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.lhs.taxcpcAdmin.dao.LhssysParametersRepository;
import com.lhs.taxcpcAdmin.model.entity.LhssysParameters;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class LhssysParametersService {

	@Autowired
	private LhssysParametersRepository lhssysParametersRepo;

	public List<LhssysParameters> getAllParameters() {
		try {
			return lhssysParametersRepo.getAllParameters();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return new ArrayList<>();
	}// end

}
