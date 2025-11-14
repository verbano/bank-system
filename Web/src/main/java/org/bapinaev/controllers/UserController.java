package org.bapinaev.controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.bapinaev.contracts.users.UserService;
import org.bapinaev.dto.users.CreateUserRequest;
import org.bapinaev.dto.users.FriendDto;
import org.bapinaev.dto.users.UserDto;
import org.bapinaev.dto.users.UserInfoResponse;
import org.bapinaev.enums.Gender;
import org.bapinaev.enums.HairColor;
import org.bapinaev.mappers.UserDtoMapper;
import org.bapinaev.models.users.User;
import org.bapinaev.models.users.UserInfo;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/users")
public class UserController {
    private final UserService userService;
    private final UserDtoMapper userDtoMapper;

    public UserController(UserService userService, UserDtoMapper userDtoMapper) {
        this.userService = userService;
        this.userDtoMapper = userDtoMapper;
    }

    @PostMapping
    @Operation(summary = "Create a new user")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "User successfully created"),
            @ApiResponse(responseCode = "409", description = "User with the given login already exists")
    })
    public ResponseEntity<UserDto> createUser(@RequestBody CreateUserRequest request) {
        User user = userDtoMapper.toDomain(request);
        boolean created = userService.create(user);
        return created
                ? new ResponseEntity<>(userDtoMapper.toDto(user), HttpStatus.CREATED)
                : new ResponseEntity<>(HttpStatus.CONFLICT);
    }

    @GetMapping("/{login}")
    @Operation(summary = "Get user information by login")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "User information retrieved successfully"),
            @ApiResponse(responseCode = "404", description = "User not found")
    })
    public ResponseEntity<UserInfoResponse> getUserInfo(@PathVariable String login) {
        UserInfo userInfo = userService.getInfo(login);
        return ResponseEntity.ok(userDtoMapper.toInfoResponse(userInfo));
    }

    @PostMapping("/{userLogin}/friends/{friendLogin}")
    @Operation(summary = "Add a friend to the user's friend list")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Friend successfully added"),
            @ApiResponse(responseCode = "404", description = "User or friend not found")
    })
    public ResponseEntity<Void> addFriend(
            @PathVariable String userLogin,
            @PathVariable String friendLogin) {

        userService.addFriend(userLogin, friendLogin);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{userLogin}/friends/{friendLogin}")
    @Operation(summary = "Remove a friend from the user's friend list")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Friend successfully removed"),
            @ApiResponse(responseCode = "404", description = "User or friend not found")
    })
    public ResponseEntity<Void> removeFriend(
            @PathVariable String userLogin,
            @PathVariable String friendLogin) {

        userService.removeFriend(userLogin, friendLogin);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/{login}/friends")
    @Operation(summary = "Get a list of the user's friends")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Friend list retrieved successfully"),
            @ApiResponse(responseCode = "404", description = "User not found")
    })
    public ResponseEntity<List<FriendDto>> getFriends(@PathVariable String login) {
        List<User> friends = userService.getFriends(login);
        List<FriendDto> friendDtos = friends.stream()
                .map(userDtoMapper::toFriend)
                .toList();
        return ResponseEntity.ok(friendDtos);
    }

    @GetMapping
    @Operation(summary = "Get all users filtered by hair color and gender")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Users retrieved successfully")
    })
    public ResponseEntity<List<UserDto>> getAllUsers(
            @RequestParam HairColor hairColor,
            @RequestParam Gender gender) {

        List<User> users = userService.getUsersByFilters(hairColor, gender);
        List<UserDto> usersDto = users.stream()
                .map(userDtoMapper::toDto)
                .toList();

        return ResponseEntity.ok(usersDto);
    }
}
