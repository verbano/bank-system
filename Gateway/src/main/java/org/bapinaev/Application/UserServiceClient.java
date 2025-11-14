package org.bapinaev.Application;

import org.bapinaev.Controllers.dto.CreateUserRequest;
import org.bapinaev.Controllers.dto.UserDto;
import org.bapinaev.Controllers.dto.UserInfoResponse;
import org.bapinaev.enums.Gender;
import org.bapinaev.enums.HairColor;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@Service
public class UserServiceClient {
    private final RestTemplate restTemplate;
    private final String baseUrl = "http://localhost:8080/api/users";

    public UserServiceClient(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public void createUser(CreateUserRequest request) {
        restTemplate.postForObject(baseUrl, request, Void.class);
    }

    public List<UserDto> getFilteredUsers(HairColor hairColor, Gender gender) {
        String url = String.format("%s?hairColor=%s&gender=%s",
                baseUrl, hairColor.name(), gender.name());

        return restTemplate.exchange(
                url,
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<List<UserDto>>() {}
        ).getBody();
    }

    public UserInfoResponse getUserInfo(String login) {
        return restTemplate.getForObject(baseUrl + "/" + login, UserInfoResponse.class);
    }

    public void addFriend(String userLogin, String friendLogin) {
        String url = String.format("%s/%s/friends/%s",
                baseUrl, userLogin, friendLogin);
        restTemplate.postForObject(url, null, Void.class);
    }

    public void removeFriend(String userLogin, String friendLogin) {
        String url = String.format("%s/%s/friends/%s",
                baseUrl, userLogin, friendLogin);
        restTemplate.delete(url);
    }
}
