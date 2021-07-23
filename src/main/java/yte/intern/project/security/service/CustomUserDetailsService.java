package yte.intern.project.security.service;

import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.security.core.userdetails.UserDetailsService;
import yte.intern.project.security.MyUserPrincipal;
import yte.intern.project.users.entity.Users;
import yte.intern.project.users.repository.UsersRepository;

import java.util.ArrayList;

@Service
public class CustomUserDetailsService implements UserDetailsService{

    private final UsersRepository usersRepository;

    public CustomUserDetailsService(UsersRepository usersRepository) {
        this.usersRepository = usersRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) {
        Users user = usersRepository.findByUsername(username);
        if (user == null) {
            throw new UsernameNotFoundException(username);
        }
        //return new MyUserPrincipal(user);
        return new User(user.getUsername(), user.getPassword(), new ArrayList<>());
    }
    /*public UserDetails loadUserByUsername(String s) throws UsernameNotFoundException {
        return new User("hebele", "hubele", new ArrayList<>());
    }*/
}
