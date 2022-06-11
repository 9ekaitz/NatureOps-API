package eus.natureops.natureops.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import eus.natureops.natureops.domain.Achievement;
import eus.natureops.natureops.domain.AchievementUser;
import eus.natureops.natureops.domain.AchievementUserKey;
import eus.natureops.natureops.domain.Role;
import eus.natureops.natureops.domain.User;
import eus.natureops.natureops.dto.UserView;
import eus.natureops.natureops.exceptions.AchievementBindingException;
import eus.natureops.natureops.form.UserRegistrationForm;
import eus.natureops.natureops.repository.AchievementsUserRepository;
import eus.natureops.natureops.repository.AchivementRepository;
import eus.natureops.natureops.repository.RoleRepository;
import eus.natureops.natureops.repository.UserRepository;
import eus.natureops.natureops.service.UserService;

@Service
public class UserServiceImpl implements UserService {

  @Autowired
  private UserRepository userRepository;

  @Autowired
  private RoleRepository roleRepository;

  @Autowired
  private PasswordEncoder passwordEncoder;

  @Autowired
  private AchivementRepository achievementRepository;

  @Autowired
  private AchievementsUserRepository achievementsUserRepository;

  @Override
  public User register(UserRegistrationForm form) {
    User user = new User();
    user.setName(form.getName());
    user.setEmail(form.getEmail());
    user.setUsername(form.getUsername());
    user.setPassword(passwordEncoder.encode(form.getPassword()));
    user.setRole(roleRepository.findByName("ROLE_USER"));
    user.setEnabled(true);

    user = userRepository.save(user);
    List<Achievement> achievements = achievementRepository.findAll();
    if (achievements != null) {
      createAchivementsForUser(achievements, user);
    }

    return userRepository.save(user);
  }

  @Override
  public User save(User user) {
    return userRepository.save(user);
  }

  @Override
  public User findByUsername(String username) {
    return userRepository.findByUsername(username, User.class);
  }

  @Override
  public User disable(User user) {
    user.setEnabled(false);
    user.setUsername(user.getId() + "_" + user.getUsername());
    return userRepository.save(user);
  }

  @Override
  public User setRole(String username, String name) {
    Role role = roleRepository.findByName(name);
    User user = userRepository.findByUsername(username, User.class);

    user.setRole(role);

    return save(user);
  }

  @Override
  public UserView loadView(String username) {
    return userRepository.findByUsername(username, UserView.class);
  }

  private void createAchivementsForUser(List<Achievement> achievements, User user) {
    AchievementUser achievementUser;
    AchievementUserKey key;
    for (Achievement item : achievements) {
      key = new AchievementUserKey();
      key.setAchievementId(item.getId());
      key.setUserId(user.getId());
      achievementUser = new AchievementUser();
      achievementUser.setId(key);
      achievementUser.setAchievement(item);
      achievementUser.setUser(user);

      achievementUser.setProgress(0);
      try {
        achievementsUserRepository.save(achievementUser);
      } catch (Exception e) {
        throw new AchievementBindingException();
      }
    }
  }

}
