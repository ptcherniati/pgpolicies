package fr.ptcherniati.pg_policies.web.controller.security;

import com.fasterxml.jackson.databind.ser.FilterProvider;
import com.fasterxml.jackson.databind.ser.impl.SimpleBeanPropertyFilter;
import com.fasterxml.jackson.databind.ser.impl.SimpleFilterProvider;
import fr.ptcherniati.pg_policies.dao.Security.AuthoritiesDAO;
import fr.ptcherniati.pg_policies.dao.Security.UsersDAO;
import fr.ptcherniati.pg_policies.model.security.Authorities;
import fr.ptcherniati.pg_policies.model.security.Users;
import fr.ptcherniati.pg_policies.utils.UpdatableBCrypt;
import fr.ptcherniati.pg_policies.web.exceptions.DuplicateUserException;
import fr.ptcherniati.pg_policies.web.exceptions.MissingAuthoritiesException;
import fr.ptcherniati.pg_policies.web.exceptions.MissingUserException;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.json.MappingJacksonValue;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.validation.Valid;
import java.net.URI;
import java.util.Optional;

@Api( description="API pour gérer les utilisateurs et les droits.")

@RestController()
public class SecurityController {
    private static final UpdatableBCrypt bcrypt = new UpdatableBCrypt(11);

    @Autowired
    private UsersDAO usersDAO;
    @Autowired
    private AuthoritiesDAO authoritiesDAO;


    /*
        Get the uers list.
     */
    @RequestMapping(value = "/Users", method = RequestMethod.GET)
    public MappingJacksonValue userList() {

        Iterable<Users> users = usersDAO.findAll();

        SimpleBeanPropertyFilter userFilter = SimpleBeanPropertyFilter.serializeAllExcept("password");

        FilterProvider usersFilterList = new SimpleFilterProvider().addFilter("userFilter", userFilter);

        MappingJacksonValue usersFilter = new MappingJacksonValue(users);

        usersFilter.setFilters(usersFilterList);

        return usersFilter;
    }
    /*
        Get the authorities list.
     */
    @RequestMapping(value = "/Authorities", method = RequestMethod.GET)
    public MappingJacksonValue authorithiesList() {

        Iterable<Authorities> users = authoritiesDAO.findAll();
        return  new MappingJacksonValue(users);
    }


    /*
        retrieve users by username;
     */
    @ApiOperation(value = "Récupère un utilisateur grâce à son username")
    @GetMapping(value = "/Users/{username}")
    public Users getUser(@PathVariable String username) {
        return usersDAO.findById(username)
                .orElseThrow(()->new MissingUserException("L'utilisateur avec l'username " + username + " est INTROUVABLE."));
    }

    /*
        retrieve authorities by username
     */
    @ApiOperation(value = "Récupère les authorities d'un username")
    @GetMapping(value = "/Authorities/{username}")
    public Authorities getAuthorities(@PathVariable String username) {
        return authoritiesDAO.findById(username)
                .orElseThrow(()->new MissingAuthoritiesException("L'authorities avec l'username " + username + " est INTROUVABLE."));
    }

    /*
        add a user.
     */
    @PostMapping(value = "/Users")
    public ResponseEntity<Void> addUser(@Valid @RequestBody Users users) {
        Optional<Users> dbUsers = usersDAO.findById(users.getUsername());
        if(dbUsers.isPresent()){
            throw new DuplicateUserException("L'utilisateur avec l'username " + users.getUsername() + " existe déjà.");
        }
        users.setPassword(bcrypt.hash(users.getPassword()));
        Users addedUser =  usersDAO.save(users);
        if (addedUser == null) {
            return ResponseEntity.noContent().build();
        }

        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{username}")
                .buildAndExpand(addedUser.getUsername())
                .toUri();

        return ResponseEntity.created(location).build();
    }

    /*
        add Authorities.
     */
    @PostMapping(value = "/Authorities")
    public ResponseEntity<Void> addAuthorities(@Valid @RequestBody Authorities authorities) {

        Authorities addedAuthorities =  authoritiesDAO.save(authorities);
        if (addedAuthorities == null) {
            return ResponseEntity.noContent().build();
        }

        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{username}")
                .buildAndExpand(addedAuthorities.getUsername())
                .toUri();

        return ResponseEntity.created(location).build();
    }
    /*
        delete user  with username
     */
    @DeleteMapping (value = "/Users/{username}")
    public void deleteUser(@PathVariable String username) {
        Users users = usersDAO.getOne(username);
        usersDAO.delete(users);
    }

    /*
        delete authorites  with username
     */
    @DeleteMapping (value = "/Authorities/{username}")
    public void deleteAuthorities(@PathVariable String username) {
        Authorities authorities = authoritiesDAO.getOne(username);
        authoritiesDAO.delete(authorities);
    }

    /*
        update user
     */
    @PutMapping (value = "/Users")
    public void updateUser(@RequestBody Users users) {
        usersDAO.save(users);
    }

    /*
        update user
     */
    @PutMapping (value = "/Authorities")
    public void updateAuthorities(@RequestBody Authorities authorities) {
        authoritiesDAO.save(authorities);
    }
}
