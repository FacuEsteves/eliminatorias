package eliminatorias.eliminatorias.service;

import eliminatorias.eliminatorias.domain.User;
import eliminatorias.eliminatorias.model.UserDto;

public interface UserService {
    void saveUser(UserDto userDto);

    User findUserByEmail(String email);
}