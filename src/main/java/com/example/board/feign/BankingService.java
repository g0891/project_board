package com.example.board.feign;

import com.example.board.config.feign.BankingServiceConfiguration;
import com.example.board.rest.dto.feign.TransactionDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;


@FeignClient(
        name = "banking",
        url = "${bankingService.url}",
        path = "${bankingService.api.path}",
        configuration = BankingServiceConfiguration.class
)
public interface BankingService {
    @GetMapping("/bankAccount/{personId}/balance")
    ResponseEntity<Long> getPersonBalance(@PathVariable long personId);

    @PutMapping("/bankAccount/{personId}/addMoney")
    ResponseEntity addMoney(@PathVariable long personId, @RequestBody TransactionDto transactionDto);

    @PutMapping("/bankAccount/{personId}/pay")
    ResponseEntity payForProject(@PathVariable long personId, @RequestBody TransactionDto transactionDto);

    @GetMapping("/bankAccount/{personId}/history")
    ResponseEntity<List<TransactionDto>> getTransactionsForPerson(@PathVariable long personId);

    @GetMapping("/bankAccount/projectPayments/{projectId}")
    ResponseEntity<Long> getTotalPaymentsForProject(@PathVariable long projectId);
}
