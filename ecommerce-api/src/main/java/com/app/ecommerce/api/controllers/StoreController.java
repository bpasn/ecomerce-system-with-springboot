package com.app.ecommerce.api.controllers;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.app.application.ApiResponse;
import com.app.application.dto.StoreDTO;
import com.app.application.interfaces.StoreService;

@RestController
@RequestMapping(value = "${api.prefix.route}/store")
public class StoreController {
    private final StoreService storeService;

    public StoreController(StoreService storeService){
        this.storeService = storeService;
    }
    @GetMapping
    public ResponseEntity<ApiResponse<List<StoreDTO>>> getAll(){
        return ResponseEntity.ok(storeService.getAll());
    }
    @GetMapping("find-one")
    public ResponseEntity<ApiResponse<StoreDTO>> findOne(){
        return ResponseEntity.ok(storeService.findFirstByOrderByIdDesc());
    }

    @PostMapping
    public ResponseEntity<ApiResponse<StoreDTO>> create(@RequestBody StoreDTO body){
        return ResponseEntity.ok(storeService.create(body));
    }
}
