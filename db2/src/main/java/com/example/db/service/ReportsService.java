package com.example.db.service;

import com.example.db.repos.ArticleRepository;
import com.example.db.repos.BalanceRepository;
import com.example.db.util.PdfUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ReportsService {
    private final ArticleRepository articleRepository;
    private final PdfUtil pdfUtil;

    public byte[] getBalanceOperationsCount(){
        return pdfUtil.createBalanceOperationsTablePdf(articleRepository.getBalanceOperationsCount());
    }

    public byte[] getUncalculatedOperations(){
        return pdfUtil.createTablePdf(articleRepository.getUncalculatedOperations());
    }
}
