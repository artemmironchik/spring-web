package com.example.springweb.service;

import com.example.springweb.dto.UserDto;
import com.example.springweb.entity.Product;
import com.example.springweb.entity.User;
import com.example.springweb.exception.MainException;
import com.example.springweb.mapper.UserMapper;
import com.example.springweb.repository.ProductRepository;
import com.example.springweb.repository.UserRepository;
import lombok.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static com.example.springweb.mapper.UserMapper.convertEntityToDto;

@Slf4j
@AllArgsConstructor
@Service
public class UserService {

    private UserRepository userRepository;
    private ProductRepository productRepository;
    private PasswordEncoder passwordEncoder;
    private RoleService roleService;

    public UserDto login(User user) throws MainException {
        User currentUser = userRepository.findByEmail(user.getEmail());
        if (currentUser == null) {
            throw new MainException("User with this email doesn't exist!");
        }
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        if (!encoder.matches(user.getPassword(), currentUser.getPassword())) {
            throw new MainException("Password is incorrect!");
        }
        return UserMapper.convertEntityToDto(currentUser);
    }

    public UserDto saveUser(User user) {
        roleService.setUserRole(user);
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        return UserMapper.convertEntityToDto(userRepository.save(user));
    }

    public void deleteUser(Long id){
        userRepository.deleteById(id);
    }

    public User findUserById(Long id) {
        Optional<User> candidate = userRepository.findById(id);
        log.info("id", id);
        log.info("user", candidate);
        return candidate.orElse(null);
    }

    public User findByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    public List<UserDto> findAllUsers() {
        List<User> users = userRepository.findAll();
        return users.stream().map(UserMapper::convertEntityToDto)
                .collect(Collectors.toList());
    }

    public List<Product> findUserProducts(Long userId) {
        return productRepository.findProductsByUserId(userId);
    }

    public void setUser(Product product, Long userId){
        Optional<User> user = userRepository.findById(userId);
        product.setUser(user.get());
    }
}
