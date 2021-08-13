package com.example.board.rest.controller;

import com.example.board.config.security.SecurityUser;
import com.example.board.feign.BankingService;
import com.example.board.rest.dto.feign.TransactionDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;


@RestController
@RequestMapping("${api.path}/banking")
@Tag(name = "Контроллер работы с банком", description = "Позволяет выполнять транзакции, просматривать баланс и т.п.")
public class BankingController {

    private final Logger logger = LoggerFactory.getLogger(BankingController.class);
    private final BankingService bankingService;

    public BankingController(BankingService bankingService) {
        this.bankingService = bankingService;
    }

    @GetMapping("/balance")
    @PreAuthorize("hasAuthority('banking:management')")
    @Operation(summary = "Баланс", description = "Позволяет узнать баланс пользователя")
    public ResponseEntity<Long> getPersonBalance(Authentication authentication) {
        SecurityUser user = (SecurityUser) authentication.getPrincipal();
        logger.info("Requested balance for user = {}", user.getUsername());
        ResponseEntity<Long> balanceResponse = bankingService.getPersonBalance(user.getId());
        logger.info("Requested balance for user = {} provided", user.getUsername());
        return balanceResponse;
    }

    @GetMapping("/history")
    @PreAuthorize("hasAuthority('banking:management')")
    @Operation(summary = "История транзакций", description = "Позволяет получить историю транзакций пользователя")
    public ResponseEntity<List<TransactionDto>> getHistory(Authentication authentication) {
        SecurityUser user = (SecurityUser) authentication.getPrincipal();
        logger.info("Requested transactions history for user = {}", user.getUsername());
        ResponseEntity<List<TransactionDto>> transactionsListResponse = bankingService.getTransactionsForPerson(user.getId());
        logger.info("Transactions history for user = {} provided", user.getUsername());
        return transactionsListResponse;
    }

    @PutMapping("/addMoney")
    @PreAuthorize("hasAuthority('banking:management')")
    @Operation(summary = "Внести на счет", description = "Позволяет внести деньги на счет пользователя")
    public ResponseEntity addMoney(@RequestBody TransactionDto transactionDto, Authentication authentication) {
        SecurityUser user = (SecurityUser) authentication.getPrincipal();
        logger.info("Adding money for user = {}", user.getUsername());
        bankingService.addMoney(user.getId(), transactionDto);
        logger.info("Money added for user = {}", user.getUsername());
        return ResponseEntity.ok().build();
    }

    @PutMapping("/pay")
    @PreAuthorize("hasAuthority('banking:management')")
    @Operation(summary = "Оплатить проект", description = "Позволяет произвести оплату проекта")
    public ResponseEntity payForProject(@RequestBody TransactionDto transactionDto, Authentication authentication) {
        SecurityUser user = (SecurityUser) authentication.getPrincipal();
        logger.info("User = {} pay for project", user.getUsername());
        bankingService.payForProject(user.getId(), transactionDto);
        logger.info("User = {} payed for project", user.getUsername());
        return ResponseEntity.ok().build();
    }


    @GetMapping("projectPayments/{projectId}")
    @PreAuthorize("hasAuthority('banking:verifyPayment')")
    @Operation(summary = "Поступившие оплаты по проекту", description = "Позволяет получить общую сумму платежей по указанному проекту")
    public ResponseEntity<Long> getProjectsPaymentsAmount(@PathVariable @Parameter(description = "Идентификатор проекта") long projectId) {
        logger.info("Verifying payments for project id = {}", projectId);
        ResponseEntity<Long> amount = bankingService.getTotalPaymentsForProject(projectId);
        logger.info("Current amount payed for project id = {} provided", projectId);
        return amount;
    }




}
