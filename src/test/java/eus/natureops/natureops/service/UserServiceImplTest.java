package eus.natureops.natureops.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import eus.natureops.natureops.domain.Role;
import eus.natureops.natureops.domain.User;
import eus.natureops.natureops.dto.UserView;
import eus.natureops.natureops.form.UserRegistrationForm;
import eus.natureops.natureops.repository.RoleRepository;
import eus.natureops.natureops.repository.UserRepository;
import eus.natureops.natureops.service.impl.UserServiceImpl;

@ExtendWith(MockitoExtension.class)
class UserServiceImplTest {
  @InjectMocks
  private UserServiceImpl userServiceImpl;

  @Mock
  private UserRepository userRepository;

  @Mock
  private RoleRepository roleRepository;

  @Mock
  private PasswordEncoder passwordEncoder;

  @Test
  void testRegister() {
    UserRegistrationForm form = new UserRegistrationForm("dummy", "password", "Dummy", "dummy@email.com");

    Role userRole = new Role(1L, "ROLE_USER", true, 1);
    User parsedUser = new User();
    parsedUser.setUsername("dummy");
    parsedUser.setName("Dummy");
    parsedUser.setPassword("encoded");
    parsedUser.setEmail("dummy@email.com");
    parsedUser.setRole(userRole);
    parsedUser.setEnabled(true);

    when(passwordEncoder.encode("password")).thenReturn("encoded");
    when(roleRepository.findByName("ROLE_USER")).thenReturn(userRole);
    when(userRepository.save(parsedUser)).thenReturn(parsedUser);

    assertEquals(parsedUser, userServiceImpl.register(form));
  }

  @Test
  void testSetRole() {
    Role userRole = new Role(1L, "ROLE_USER", true, 1);
    User user = new User(1L, "dummy", "password", "name", "email", true, null, 1);
    User user2 = new User(1L, "dummy", "password", "name", "email", true, userRole, 1);
    
    when(userRepository.findByUsername("dummy", User.class)).thenReturn(user);
    when(roleRepository.findByName("ROLE_USER")).thenReturn(userRole);
    when(userRepository.save(user2)).thenReturn(user2);

    assertEquals(user2, userServiceImpl.setRole("dummy", "ROLE_USER"));
  }

  @Test
  void testloadView() {
    UserView userView = new UserView(1L, "dummy", "name", "email");
    
    when(userRepository.findByUsername("dummy", UserView.class)).thenReturn(userView);

    assertEquals(userView, userServiceImpl.loadView("dummy"));
  }
}
