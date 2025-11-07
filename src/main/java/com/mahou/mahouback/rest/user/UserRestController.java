package com.mahou.mahouback.rest.user;

import com.mahou.mahouback.logic.entity.http.GlobalResponseHandler;
import com.mahou.mahouback.logic.entity.http.Meta;
import com.mahou.mahouback.logic.entity.role.Role;
import com.mahou.mahouback.logic.entity.role.RoleEnum;
import com.mahou.mahouback.logic.entity.role.RoleRepository;
import com.mahou.mahouback.logic.entity.user.User;
import com.mahou.mahouback.logic.entity.user.UserRepository;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/users")
public class UserRestController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private RoleRepository roleRepository;

    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'SUPER_ADMIN')")
    public ResponseEntity<?> getAll(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size,
            HttpServletRequest request) {

        Pageable pageable = PageRequest.of(page-1, size);
        Page<User> usersPage = userRepository.findAll(pageable);
        Meta meta = new Meta(request.getMethod(), request.getRequestURL().toString());
        meta.setTotalPages(usersPage.getTotalPages());
        meta.setTotalElements(usersPage.getTotalElements());
        meta.setPageNumber(usersPage.getNumber() + 1);
        meta.setPageSize(usersPage.getSize());

        return new GlobalResponseHandler().handleResponse("Users retrieved successfully",
                usersPage.getContent(), HttpStatus.OK, meta);
    }

    @PostMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'SUPER_ADMIN')")
    public ResponseEntity<?> addUser(@RequestBody User user, HttpServletRequest request) {
        Optional<User> foundUser = userRepository.findByEmail(user.getEmail());
        Optional<User> foundUserName = userRepository.findByUsername(user.getUsername());
        if (foundUser.isPresent()) {
            return new GlobalResponseHandler().handleResponse("El usuario " + user.getEmail() + ". ya se encuentra registrado", HttpStatus.CONFLICT, request);
        }

        if (foundUserName.isPresent()) {
            return new GlobalResponseHandler().handleResponse("El nombre de usuario " + user.getUsername() + ". ya se encuentra registrado", HttpStatus.CONFLICT, request);
        }

        Role role = new Role();
        user.setPassword(passwordEncoder.encode(user.getPassword()));

        Optional<Role> optionalRole = roleRepository.findByName(RoleEnum.USER);
        user.setRole(optionalRole.orElse(role));
        user.setStatus(true);
        userRepository.save(user);
        return new GlobalResponseHandler().handleResponse("User updated successfully",
                user, HttpStatus.OK, request);
    }

    @PutMapping("/{userId}")
    @PreAuthorize("hasAnyRole('ADMIN', 'SUPER_ADMIN')")
    public ResponseEntity<?> updateUser(@PathVariable Long userId, @RequestBody User user, HttpServletRequest request) {
        Optional<User> foundOrder = userRepository.findById(userId);
        if(foundOrder.isPresent()) {
            user.setPassword(passwordEncoder.encode(user.getPassword()));
            userRepository.save(user);
            return new GlobalResponseHandler().handleResponse("User updated successfully",
                    user, HttpStatus.OK, request);
        } else {
            return new GlobalResponseHandler().handleResponse("User id " + userId + " not found"  ,
                    HttpStatus.NOT_FOUND, request);
        }
    }

    @PutMapping("/pass/{email}")
    public ResponseEntity<?> updateUserByEmail(@RequestBody User user, HttpServletRequest request) {

        Optional<User> foundUser = userRepository.findByEmail(user.getEmail());

        if(foundUser.isPresent()) {
            foundUser.get().setPassword(passwordEncoder.encode(user.getPassword()));

            userRepository.save(foundUser.get());

            return new GlobalResponseHandler().handleResponse("Contraseña actualizada exitosamente",
                    user.getEmail(), HttpStatus.OK, request);
        } else {
            return new GlobalResponseHandler().handleResponse("Usuario no encontrado " + user.getEmail() + ", si no se ha registrado puede hacerlo!!"  ,
                    HttpStatus.NOT_FOUND, request);
        }
    }

    @PutMapping("/update")
    public ResponseEntity<?> updateOrSave(@RequestBody User user, HttpServletRequest request) {
        Optional<User> foundUser = userRepository.findByEmail(user.getEmail());

        if (foundUser.isEmpty()) {
            addUser(user, request);

            return new GlobalResponseHandler().handleResponse("Nuevo Usuario Registrado!!",
                    user, HttpStatus.OK, request);

        }

        if (foundUser.isPresent()) {
            User existingUser = foundUser.get();
            existingUser.setUsername(user.getUsername());
            existingUser.setName(user.getName());
            existingUser.setLastname(user.getLastname());
            existingUser.setEmail(user.getEmail());
            existingUser.setPhoto(user.getPhoto());
            existingUser.setPassword(passwordEncoder.encode(user.getPassword()));
            Optional<Role> optionalRole = roleRepository.findByName(user.getRole().getName());
            existingUser.setRole(optionalRole.orElse(new Role()));
            existingUser.setStatus(true);

            userRepository.save(existingUser);

            return new GlobalResponseHandler().handleResponse("Usuario Correctemente Actualizado!!",
                    existingUser, HttpStatus.OK, request);
        }

        return new GlobalResponseHandler().handleResponse("User not found", HttpStatus.NOT_FOUND, request);
    }


    @DeleteMapping("/{userId}")
    @PreAuthorize("hasAnyRole('ADMIN', 'SUPER_ADMIN')")
    public ResponseEntity<?> deleteUser(@PathVariable Long userId, HttpServletRequest request) {
        Optional<User> foundOrder = userRepository.findById(userId);
        if(foundOrder.isPresent()) {
            userRepository.deleteById(userId);
            return new GlobalResponseHandler().handleResponse("User deleted successfully",
                    foundOrder.get(), HttpStatus.OK, request);
        } else {
            return new GlobalResponseHandler().handleResponse("User id " + userId + " not found"  ,
                    HttpStatus.NOT_FOUND, request);
        }
    }

    @GetMapping("/me")
    @PreAuthorize("isAuthenticated()")
    public User authenticatedUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return (User) authentication.getPrincipal();
    }
}
