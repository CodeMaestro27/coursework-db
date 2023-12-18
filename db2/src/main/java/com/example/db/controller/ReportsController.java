package com.example.db.controller;

import com.example.db.service.ReportsService;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/reports")
@RequiredArgsConstructor
public class ReportsController {

    private final ReportsService reportServices;

    @GetMapping
    @Secured("ROLE_ADMIN")
    public String reports() {
        return "reports/reports";
    }

    @GetMapping("/getBalanceOperationsCount")
    @Secured("ROLE_ADMIN")
    public ResponseEntity<ByteArrayResource> getHighestExpenseArticle() {
        byte[] data = reportServices.getBalanceOperationsCount();
        ByteArrayResource resource = new ByteArrayResource(data);

        return ResponseEntity
                .ok()
                .contentLength(data.length)
                .header("Content-type", "application/octet-stream")
                .header("Content-disposition", "attachment; filename=\"1.pdf\"")
                .body(resource);
    }

    @GetMapping("/getUncalculatedOperations")
    @Secured("ROLE_ADMIN")
    public ResponseEntity<ByteArrayResource> getLastBalanceOperations() {
        byte[] data = reportServices.getUncalculatedOperations();
        ByteArrayResource resource = new ByteArrayResource(data);

        return ResponseEntity
                .ok()
                .contentLength(data.length)
                .header("Content-type", "application/octet-stream")
                .header("Content-disposition", "attachment; filename=\"2.pdf\"")
                .body(resource);
    }
}
