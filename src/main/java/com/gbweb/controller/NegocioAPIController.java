package com.gbweb.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.gbweb.service.NegocioService;

@RestController
@RequestMapping("/api")
public class NegocioAPIController {
	
	@Autowired
	NegocioService negocioService;
	
	
    @GetMapping("/negocios")
    public Object getNegocios() {
        return negocioService.findAll();
    }
    


}
