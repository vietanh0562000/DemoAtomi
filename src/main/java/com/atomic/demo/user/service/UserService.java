package com.atomic.demo.user.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import com.atomic.demo.user.repository.UserRepository;
import com.atomic.demo.user.model.User;
import com.atomic.demo.user.dto.response.UserDTO;
import com.atomic.demo.user.dto.request.CreateUserRequest;
import com.atomic.demo.user.dto.request.UpdateUserRequest;
import java.util.List;
import org.springframework.data.domain.PageImpl;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;

    public Page<UserDTO> getAllUsers(Pageable pageable) {
        Page<User> users = userRepository.findAll(pageable);
        return users.map(user -> new UserDTO(user.getId(), user.getName(), user.getEmail()));
    }

    public Page<UserDTO> searchUsers(String name, Pageable pageable) {
        List<User> users = userRepository.findByNameContaining(name, pageable);

        List<UserDTO> dtos = users.stream()
                .map(u -> new UserDTO(u.getId(), u.getName(), u.getEmail()))
                .toList();

        return new PageImpl<>(dtos, pageable, dtos.size());
    }

    public UserDTO getUserById(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found"));
        return new UserDTO(user.getId(), user.getName(), user.getEmail());
    }

    public UserDTO createUser(CreateUserRequest createUserRequest) {
        User user = new User();
        user.setName(createUserRequest.name());
        user.setEmail(createUserRequest.email());
        return new UserDTO(userRepository.save(user).getId(), user.getName(), user.getEmail());
    }

    public UserDTO updateUser(Long id, UpdateUserRequest updateUserRequest) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found"));
        user.setName(updateUserRequest.name());
        user.setEmail(updateUserRequest.email());
        return new UserDTO(userRepository.save(user).getId(), user.getName(), user.getEmail());
    }

    public void deleteUser(Long id) {
        userRepository.deleteById(id);
    }
}
