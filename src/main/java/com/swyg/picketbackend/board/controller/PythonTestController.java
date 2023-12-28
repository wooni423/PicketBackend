package com.swyg.picketbackend.board.controller;


import com.swyg.picketbackend.board.service.PythonScriptService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/python")
public class PythonTestController {
    private final PythonScriptService pythonScriptService;

    public PythonTestController(PythonScriptService pythonScriptService) {
        this.pythonScriptService = pythonScriptService;
    }

    @GetMapping("/run")
    public ResponseEntity<String> runScript() {
        String result = pythonScriptService.runPythonScript();
        return ResponseEntity.ok(result);
    }
}
