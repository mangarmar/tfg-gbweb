package com.gbweb.controller;

import java.util.List;

import org.apache.http.HttpStatus;
import org.hibernate.annotations.common.util.impl.Log;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
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
