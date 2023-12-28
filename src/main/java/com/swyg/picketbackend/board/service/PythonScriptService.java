package com.swyg.picketbackend.board.service;

import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

@Service
public class PythonScriptService {

    public String runPythonScript() {
        // 파이썬 스크립트의 정확한 경로
        String scriptPath = "C:\\Users\\choig\\git\\PicketBackend\\src\\script\\test.py";

        // 파이썬 실행 파일의 경로 (아나콘다 가상 환경 내부)
        String pythonExecutablePath = "C:\\Users\\choig\\miniconda3\\envs\\transformers_env\\python.exe";

        // 파이썬 스크립트 실행
        ProcessBuilder processBuilder = new ProcessBuilder(
                pythonExecutablePath, scriptPath
        );
        processBuilder.redirectErrorStream(true); // 에러 출력을 표준 출력으로 리다이렉트

        try {
            Process process = processBuilder.start();
            StringBuilder output = new StringBuilder();
            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));

            String line;
            while ((line = reader.readLine()) != null) {
                output.append(line).append(System.lineSeparator());
            }

            int scriptExitCode = process.waitFor();
            if (scriptExitCode == 0) {
                return output.toString().trim(); // 출력 결과 반환
            } else {
                return "Error: Python script exited with code " + scriptExitCode;
            }
        } catch (IOException | InterruptedException e) {
            Thread.currentThread().interrupt();
            return "Error: " + e.getMessage();
        }
    }
}
