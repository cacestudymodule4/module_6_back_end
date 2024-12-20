package com.example.module_6_back_end.service;

import com.example.module_6_back_end.dto.ChangePasswordRequest;
import com.example.module_6_back_end.exception.UnauthorizedException;
import com.example.module_6_back_end.model.Role;
import com.example.module_6_back_end.model.Staff;
import com.example.module_6_back_end.model.User;
import com.example.module_6_back_end.repository.RoleRepository;
import com.example.module_6_back_end.repository.UserRepository;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final RoleRepository roleRepository;

    public UserServiceImpl(UserRepository userRepository, PasswordEncoder passwordEncoder, RoleRepository roleRepository) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.roleRepository = roleRepository;
    }

    @Override
    public void changePassword(ChangePasswordRequest request) throws Exception {
        User user = getCurrentUser();
        if (user == null) {
            throw new Exception("Người dùng chưa đăng nhập");
        }
        if (!passwordEncoder.matches(request.getCurrentPassword(), user.getPassword())) {
            throw new Exception("Mật khẩu hiện tại không đúng");
        }
        user.setPassword(passwordEncoder.encode(request.getNewPassword()));
        userRepository.save(user);
    }

    @Override
    public User getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()) {
            return null;
        }
        String username = authentication.getName();
        return userRepository.findByEmailOrUsernameOrPhone(username);
    }

    @Override
    public User getUserByStaff(Staff staff) {
        return userRepository.findByStaff(staff);
    }

    @Override
    public void deleteUser(User user) {
        userRepository.delete(user);
    }

    @Override
    public void isAdmin() {
        User auTh = getCurrentUser();
        List<Role> roles = roleRepository.findByUser(auTh);
        boolean isAdmin = roles.stream()
                .anyMatch(role -> role.getName().equalsIgnoreCase("ADMIN"));
        if (!isAdmin) {
            throw new UnauthorizedException("Bạn không có quyền thực hiện hành động này");
        }
    }
}
