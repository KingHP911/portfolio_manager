package ru.kinghp.portfolio_manager.service.impl;

import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.argon2.Argon2PasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import ru.kinghp.portfolio_manager.models.DBUser;
import ru.kinghp.portfolio_manager.repository.DBUserRepository;
import ru.kinghp.portfolio_manager.service.DBUserService;

@Data
@Service
public class DBUserServiceImpl implements UserDetailsService, DBUserService {

    final private DBUserRepository DBUserRepository;
    final private PasswordEncoder encoder;

    @Override
    public UserDetails loadUserByUsername(String login) throws UsernameNotFoundException {

        DBUser dbUser = DBUserRepository.findByLogin(login);

        if (dbUser == null) {
            throw new UsernameNotFoundException("Unknown user: " + login);
        }

        //todo возможно позже прикрутить полноценные роли
        String role = dbUser.getIsAdmin() ? "ADMIN" : "USER";

        UserDetails user = User.builder()
                .username(dbUser.getLogin())
                .password(dbUser.getPassword())
                .roles(role)
                .build();

        return user;
    }

    @Override
    public DBUser add(DBUser user) {

        DBUser dbUser = DBUserRepository.findByLogin(user.getLogin());
        if (dbUser != null){
            return null;
        }

        user.setPassword(encoder.encode(user.getPassword()));
        return DBUserRepository.save(user);
    }
}
