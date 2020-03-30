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
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.json.MappingJacksonValue;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import java.net.URI;
import java.util.Optional;

@Api( description="API pour gérer les utilisateurs et les droits.")

@RestController()
@Slf4j
@RequestMapping("/api/v1")
public class SecurityController {
    private static final UpdatableBCrypt bcrypt = new UpdatableBCrypt(11);
    private static String getCurrentUsername(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (!(authentication instanceof AnonymousAuthenticationToken)) {
            String currentUserName = authentication.getName();
            return currentUserName;
        }
        return null;
    }
    private static Authentication getCurrentUser(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (!(authentication instanceof AnonymousAuthenticationToken)) {
            return authentication;
        }
        return null;
    }

    @Autowired
    private UsersDAO usersDAO;
    @Autowired
    private AuthoritiesDAO authoritiesDAO;

    @Autowired
    private AuthenticationManager authManager;

	@RequestMapping("/")
	public String root() {
		return "redirect:/static/index.html";
	}

    @RequestMapping(value ="/login", method = RequestMethod.POST)
    public Users loginPage(@RequestParam(value = "error", required = false) String error,
                            @RequestParam(value = "logout", required = false) String logout,
                            @RequestParam(value = "username", required = true) String username,
                            @RequestParam(value = "password", required = true) String password,
                                    final HttpServletRequest request) {
        log.warn(username);
        log.warn(password);
        UsernamePasswordAuthenticationToken authReq = new UsernamePasswordAuthenticationToken(username, password);
        Authentication auth = authManager.authenticate(authReq);

        SecurityContext sc = SecurityContextHolder.getContext();
        sc.setAuthentication(auth);
        HttpSession session = request.getSession(true);
        session.setAttribute("SPRING_SECURITY_CONTEXT", sc);
        String errorMessge = null;
        if(error != null) {
            errorMessge = "Username or Password is incorrect !!";
        }
        if(logout != null) {
            errorMessge = "You have been successfully logged out !!";
        }
        final Users users = usersDAO.findById(username).orElse(null);
        log.warn(users.toString());
        return users;
    }

    @RequestMapping(value="/logout", method = RequestMethod.GET)
    public String logoutPage (HttpServletRequest request, HttpServletResponse response) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null){
            new SecurityContextLogoutHandler().logout(request, response, auth);
        }
        return "redirect:/login?logout=true";
    }
    /*
        Get the uers list.
     */
    @RequestMapping(value = "/Users", method = RequestMethod.GET)
    public MappingJacksonValue userList() {
        log.warn(getCurrentUsername()+" ask get /Users");
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
        log.warn(getCurrentUsername()+" ask get /Authorities");

        Iterable<Authorities> authorities = authoritiesDAO.findAll();
        return  new MappingJacksonValue(authorities);
    }


    /*
        retrieve users by username;
     */
    @ApiOperation(value = "Récupère un utilisateur grâce à son username")
    @GetMapping(value = "/Users/{username}")
    public Users getUser(@PathVariable String username) {
        log.warn(getCurrentUsername()+" ask get /Users/{username}",usersDAO.findById(username));
        return usersDAO.findById(username)
                .orElseThrow(()->new MissingUserException("L'utilisateur avec l'username " + username + " est INTROUVABLE."));
    }

    /*
        retrieve authorities by username
     */
    @ApiOperation(value = "Récupère les authorities d'un username")
    @GetMapping(value = "/Authorities/{username}")
    public Authorities getAuthorities(@PathVariable String username) {
        log.warn(getCurrentUsername()+" ask get /Authorities/{username}");
        return authoritiesDAO.findById(username)
                .orElseThrow(()->new MissingAuthoritiesException("L'authorities avec l'username " + username + " est INTROUVABLE."));
    }

    /*
        add a user.
     */
    @PostMapping(value = "/Users")
    public ResponseEntity<Void> addUser(@Valid @RequestBody Users users) {
        log.warn(getCurrentUsername()+" ask post /Users");
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
        log.warn(getCurrentUsername()+" ask post /Authorities");

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
        log.warn(getCurrentUsername()+" ask delete /Users");
        Users users = usersDAO.getOne(username);
        usersDAO.delete(users);
    }

    /*
        delete authorites  with username
     */
    @DeleteMapping (value = "/Authorities/{username}")
    public void deleteAuthorities(@PathVariable String username) {
        log.warn(getCurrentUsername()+" ask delete /Authorities");
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
