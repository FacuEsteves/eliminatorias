package eliminatorias.eliminatorias.service;

import eliminatorias.eliminatorias.domain.User;
import eliminatorias.eliminatorias.model.UserDto;

import java.util.List;

public interface UserService {
    void saveUser(UserDto userDto);
    List<User> findAll();
    User findUserByEmail(String email);
}