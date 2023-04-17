package com.lhs.taxcpcAdmin.config;

import javax.servlet.http.HttpServletRequest;

import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.ModelAndView;

/**
* @author sakshi.bandhate
*
*/

@ControllerAdvice
public class GlobalExceptionHandler {
	 public static final String DEFAULT_ERROR_VIEW = "error";

	  @ExceptionHandler(value = { Exception.class})
	  public ModelAndView defaultErrorHandler(HttpServletRequest req, Exception e){
	    System.out.println("In GlobalExceptionHandler");
	    ModelAndView mav = new ModelAndView();
	    System.out.println("exception==="+e);
	    System.out.println("url==="+req.getRequestURL());
	    
	    mav.addObject("url", req.getRequestURL());
	    mav.setViewName(DEFAULT_ERROR_VIEW);
	    return mav;
	  }
}
