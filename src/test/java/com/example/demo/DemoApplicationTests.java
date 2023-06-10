package com.example.demo;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import com.example.demo.model.User;
import com.example.demo.repository.UserRepository;
import com.example.demo.service.UserService;

@SpringBootTest
class DemoApplicationTests {

    @Autowired
    private UserService userService;

    @MockBean
    private UserRepository userRepository;

    @Test
    public void testCreateUser() {
        User user = new User();
        user.setName("John Doe");
        user.setEmail("john@example.com");
        user.setPhone("1234567890");

        when(userRepository.save(user)).thenReturn(user);

        User createdUser = userService.createUser(user);

        assertEquals(user, createdUser);
        verify(userRepository, times(1)).save(user);
    }

    @Test
    public void testGetAllUsers() {
        List<User> users = new ArrayList<>();
        users.add(new User("John Doe", "john@example.com", "1234567890"));
        users.add(new User("Jane Smith", "jane@example.com", "9876543210"));

        when(userRepository.findAll()).thenReturn(users);

        List<User> retrievedUsers = userService.getAllUsers();

        assertEquals(users, retrievedUsers);
        verify(userRepository, times(1)).findAll();
    }

    @Test
    public void testGetUserById() {
        Long userId = 1L;
        User user = new User(userId, "John Doe", "john@example.com", "1234567890");

        when(userRepository.findById(userId)).thenReturn(Optional.of(user));

        Optional<User> retrievedUser = userService.getUserById(userId);

        assertEquals(Optional.of(user), retrievedUser);
        verify(userRepository, times(1)).findById(userId);
    }

    @Test
    public void testUpdateUser() {
        // Obtener un usuario existente por su ID
        Long userId = 1L;
        Optional<User> existingUserOptional = userRepository.findById(userId);

        if (existingUserOptional.isPresent()) {
            User existingUser = existingUserOptional.get();

            // Crear una copia del usuario existente con los nuevos datos
            User updatedUser = new User(existingUser.getId(), "Jane Smith", existingUser.getEmail(),
                    existingUser.getPhone());

            // Mockear el comportamiento de userRepository.save()
            when(userRepository.save(updatedUser)).thenReturn(updatedUser);

            // Llamar al método updateUser() de userService
            User result = userService.updateUser(updatedUser);

            // Verificar que el usuario actualizado tenga los nuevos datos
            assertEquals(updatedUser, result);

            // Verificar que se haya llamado userRepository.save() una vez con el usuario
            // actualizado
            verify(userRepository, times(1)).save(updatedUser);
        } else {
            fail("User not found with ID: " + userId);
        }
    }

    @Test
    public void testDeleteUser() {
        Long userId = 1L;

        userService.deleteUser(userId);

        verify(userRepository, times(1)).deleteById(userId);
    }
}
