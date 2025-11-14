package org.bapinaev.Application;

import org.bapinaev.Controllers.dto.AccountResponse;
import org.bapinaev.Controllers.dto.AmountRequest;
import org.bapinaev.Controllers.dto.TransactionResponse;
import org.bapinaev.Controllers.dto.TransferRequest;
import org.bapinaev.enums.TransactionType;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.UUID;

@Service
public class AccountServiceClient {
    private final RestTemplate restTemplate;
    private final String baseUrl = "http://localhost:8080/api/accounts";

    public AccountServiceClient(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public List<AccountResponse> getAllAccounts() {
        return restTemplate.exchange(
                baseUrl,
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<List<AccountResponse>>() {}
        ).getBody();
    }

    public List<AccountResponse> getAccountsByUser(String userLogin) {
        return restTemplate.exchange(
                baseUrl + "/user/" + userLogin,
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<List<AccountResponse>>() {}
        ).getBody();
    }

    public List<TransactionResponse> getTransactions(UUID accountId, TransactionType type) {
        String url = String.format("%s/transactions?accountId=%s&type=%s",
                baseUrl, accountId, type.name());

        return restTemplate.exchange(
                url,
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<List<TransactionResponse>>() {}
        ).getBody();
    }

    public void transfer(TransferRequest request) {
        restTemplate.postForObject(
                baseUrl + "/transfer",
                request,
                Void.class
        );
    }

    public void withdraw(UUID accountId, AmountRequest request) {
        restTemplate.postForObject(
                baseUrl + "/" + accountId + "/withdraw",
                request,
                Void.class
        );
    }

    public void deposit(UUID accountId, AmountRequest request) {
        restTemplate.postForObject(
                baseUrl + "/" + accountId + "/deposit",
                request,
                Void.class
        );
    }
}
