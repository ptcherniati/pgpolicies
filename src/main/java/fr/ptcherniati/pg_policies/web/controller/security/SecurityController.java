package fr.ptcherniati.pg_policies.web.controller.security;

import com.fasterxml.jackson.databind.ser.FilterProvider;
import com.fasterxml.jackson.databind.ser.impl.SimpleBeanPropertyFilter;
import com.fasterxml.jackson.databind.ser.impl.SimpleFilterProvider;
import fr.ptcherniati.pg_policies.dao.PgPoliciesDAO;
import fr.ptcherniati.pg_policies.dao.Security.AuthoritiesDAO;
import fr.ptcherniati.pg_policies.dao.Security.PoliciesDAO;
import fr.ptcherniati.pg_policies.dao.Security.RolesDAO;
import fr.ptcherniati.pg_policies.dao.Security.UsersDAO;
import fr.ptcherniati.pg_policies.model.security.*;
import fr.ptcherniati.pg_policies.utils.UpdatableBCrypt;
import fr.ptcherniati.pg_policies.web.exceptions.DuplicateUserException;
import fr.ptcherniati.pg_policies.web.exceptions.MissingAuthoritiesException;
import fr.ptcherniati.pg_policies.web.exceptions.MissingRolesException;
import fr.ptcherniati.pg_policies.web.exceptions.MissingUserException;
import fr.ptcherniati.pg_policies.web.model.AuthoritiesVO;
import fr.ptcherniati.pg_policies.web.model.PoliciesVO;
import fr.ptcherniati.pg_policies.web.model.RolesVO;
import fr.ptcherniati.pg_policies.web.model.UsersVO;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.Info;
import io.swagger.annotations.SwaggerDefinition;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.json.MappingJacksonValue;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import org.springframework.web.util.UriComponentsBuilder;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import java.net.URI;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@SwaggerDefinition(
        info = @Info(
                title = "Security",
                version = "1.0",
                description = "API pour gérer les utilisateurs et les droits."
        )
)
@RestController()
@Slf4j
@RequestMapping("/api/v1")
public class SecurityController {
    private static final UpdatableBCrypt bcrypt = new UpdatableBCrypt(11);
    @Autowired
    private UsersDAO usersDAO;
    @Autowired
    private AuthoritiesDAO authoritiesDAO;
    @Autowired
    private RolesDAO rolesDAO;
    @Autowired
    private PoliciesDAO policiesDAO;
    @Autowired
    private AuthenticationManager authManager;
    @Autowired
    private PgPoliciesDAO pgPoliciesDAO;

    private static String getCurrentUsername() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (!(authentication instanceof AnonymousAuthenticationToken)) {
            return authentication.getName();
        }
        return null;
    }

    private static Authentication getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (!(authentication instanceof AnonymousAuthenticationToken)) {
            return authentication;
        }
        return null;
    }

    @RequestMapping("/")
    public String root() {
        return "redirect:/index.html";
    }

    @RequestMapping("/static/static")
    public String static2() {
        return "redirect:/static/index.html";
    }


    @RequestMapping(value = "/Login", method = RequestMethod.POST)
    public UsersVO loginPage(@RequestParam(value = "error", required = false) String error,
                             @RequestParam(value = "logout", required = false) String logout,
                             @RequestParam(value = "username") String username,
                             @RequestParam(value = "password") String password,
                             final HttpServletRequest request) {
        UsernamePasswordAuthenticationToken authReq = new UsernamePasswordAuthenticationToken(username, password);
        Authentication auth = authManager.authenticate(authReq);
        String user = pgPoliciesDAO.setUser("");
        log.warn("user in base = " + user);
        SecurityContext sc = SecurityContextHolder.getContext();
        sc.setAuthentication(auth);
        HttpSession session = request.getSession(true);
        session.setAttribute("SPRING_SECURITY_CONTEXT", sc);
        String errorMessge = null;
        if (error != null) {
            errorMessge = "Username or Password is incorrect !!";
        }
        if (logout != null) {
            errorMessge = "You have been successfully logged out !!";
        }
        final Users users = usersDAO.findById(username).orElse(null);
        final List<Authorities> authorities = authoritiesDAO.findByUsername(username);
        UsersVO usersVO = new UsersVO(users, authorities);
        return usersVO;
    }

    @RequestMapping(value = "/Logout", method = RequestMethod.GET)
    public String logoutPage(HttpServletRequest request, HttpServletResponse response) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null) {
            new SecurityContextLogoutHandler().logout(request, response, auth);
        }
        return "redirect:/login?logout=true";
    }

    /*
        Get the uers list.
     */
    @RequestMapping(value = "/Users", method = RequestMethod.GET)
    public MappingJacksonValue userList(HttpServletRequest request) {
        Iterable<UsersVO> users = usersDAO.findAll()
                .stream()
                .map(u -> new UsersVO(u, authoritiesDAO.findByUsername(u.getUsername())))
                .peek(u -> {
                    u.setUri(UriComponentsBuilder.fromUriString(request.getRequestURL().toString()).path("/").path(u.getUsername()).build().toUri());
                    u.getAuthorities().stream()
                            .map(au -> {
                                au.setUri(UriComponentsBuilder.fromUriString(request.getRequestURL().toString().replaceAll("Users.*", "Authorities")).path("/").path(au.getUsername()).build().toUri());
                                return au;
                            })
                            .collect(Collectors.toList());
                })
                .collect(Collectors.toList());

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
    public MappingJacksonValue authorithiesList(HttpServletRequest request) {

        List<AuthoritiesVO> authorities = authoritiesDAO.findAll()
                .stream()
                .map(u -> new AuthoritiesVO(u))
                .peek(u -> u.setUri(UriComponentsBuilder.fromUriString(request.getRequestURL().toString()).path("/").path(u.getUsername()).build().toUri()))
                .collect(Collectors.toList());
        return new MappingJacksonValue(authorities);
    }

    /*
        Get the roles list.
     */
    @RequestMapping(value = "/Roles", method = RequestMethod.GET)
    public MappingJacksonValue rolesList(HttpServletRequest request) {

        List<RolesVO> roles = rolesDAO.findAll()
                .stream()
                .map(u -> new RolesVO(u))
                .peek(u -> u.setUri(UriComponentsBuilder.fromUriString(request.getRequestURL().toString()).path("/").path(u.getNom()).build().toUri()))
                .collect(Collectors.toList());
        return new MappingJacksonValue(roles);
    }

    /*
        Get the policies list.
     */
    @RequestMapping(value = "/Policies", method = RequestMethod.GET)
    public MappingJacksonValue policiesList(HttpServletRequest request) {

        List<PoliciesVO> policies = policiesDAO.findAll()
                .stream()
                .map(u -> new PoliciesVO(u))
                .peek(u -> u.setUri(UriComponentsBuilder.fromUriString(request.getRequestURL().toString()).path("/").path(Long.toString(u.getId())).build().toUri()))
                .collect(Collectors.toList());
        return new MappingJacksonValue(policies);
    }


    /*
        retrieve users by username;
     */
    @ApiOperation(value = "Récupère un utilisateur grâce à son username")
    @GetMapping(value = "/Users/{username}")
    public UsersVO getUser(HttpServletRequest request, @PathVariable String username) {
        return usersDAO.findById(username)
                .map(u -> new UsersVO(u, authoritiesDAO.findByUsername(u.getUsername())))
                .map(u -> {
                    u.setUri(UriComponentsBuilder.fromUriString(request.getRequestURL().toString()).build().toUri());
                    u.getAuthorities().stream()
                            .map(au -> {
                                au.setUri(UriComponentsBuilder.fromUriString(request.getRequestURL().toString().replaceAll("Users.*", "Authorities")).path("/").path(au.getUsername()).build().toUri());
                                return au;
                            })
                            .collect(Collectors.toList());
                    return u;
                })
                .orElseThrow(() -> new MissingUserException("L'utilisateur avec l'username " + username + " est INTROUVABLE."));
    }

    /*
        retrieve authorities by username
     */
    @ApiOperation(value = "Récupère les authorities d'un username")
    @GetMapping(value = "/Authorities/{username}")
    public List<AuthoritiesVO> getAuthorities(HttpServletRequest request, @PathVariable String username) {
        return authoritiesDAO.findByUsername(username).stream()
                .map(a -> new AuthoritiesVO(a))
                .peek(a -> a.setUri(UriComponentsBuilder.fromUriString(request.getRequestURL().toString()).build().toUri()))
                .collect(Collectors.toList());
    }

    /*
        retrieve role by nom
     */
    @ApiOperation(value = "Récupère le role d'un nom")
    @GetMapping(value = "/Roles/{nom}")
    public RolesVO getRole(HttpServletRequest request, @PathVariable String nom) {
        return rolesDAO.findById(nom)
                .map(a -> new RolesVO(a))
                .map(a -> {
                    a.setUri(UriComponentsBuilder.fromUriString(request.getRequestURL().toString()).build().toUri());
                    return a;
                })
                .orElseThrow(() -> new MissingRolesException("Le role avec le nom  " + nom + " est INTROUVABLE."));
    }

    /*
        retrieve policy by id
     */
    @ApiOperation(value = "Récupère la policy par id")
    @GetMapping(value = "/Policies/{id}")
    public PoliciesVO getPolicy(HttpServletRequest request, @PathVariable Long id) {
        return policiesDAO.findById(id)
                .map(a -> new PoliciesVO(a))
                .map(a -> {
                    a.setUri(UriComponentsBuilder.fromUriString(request.getRequestURL().toString()).build().toUri());
                    return a;
                })
                .orElseThrow(() -> new MissingRolesException("La policy avec l'id'  " + id + " est INTROUVABLE."));
    }

    /*
        add a user.
     */
    @PostMapping(value = "/Users")
    public ResponseEntity<Void> addUser(@Valid @RequestBody Users users) {
        Optional<Users> dbUsers = usersDAO.findById(users.getUsername());
        if (dbUsers.isPresent()) {
            throw new DuplicateUserException("L'utilisateur avec l'username " + users.getUsername() + " existe déjà.");
        }
        users.setPassword(bcrypt.hash(users.getPassword()));
        Users addedUser = usersDAO.save(users);
        if (addedUser == null) {
            return ResponseEntity.noContent().build();
        }
        pgPoliciesDAO.initUser(users);
        Roles dbRole = rolesDAO.findById("ROLE_USER")
                .orElseThrow(() -> new MissingRolesException("Le role avec le nom  ROLE_USER est INTROUVABLE."));
        authoritiesDAO.save(new Authorities(addedUser.getUsername(), dbRole));
        addedUser = usersDAO.getOne(addedUser.getUsername());
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
        Authorities addedAuthorities = authoritiesDAO.save(authorities);
        if (addedAuthorities == null) {
            return ResponseEntity.noContent().build();
        }
        pgPoliciesDAO.addToGroup(authorities);
        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{username}")
                .buildAndExpand(addedAuthorities.getUsername())
                .toUri();

        return ResponseEntity.created(location).build();
    }

    /*
        add Roles.
     */
    @PostMapping(value = "/Roles")
    public ResponseEntity<Void> addRole(@Valid @RequestBody Roles role) {
        Roles addedRoles = rolesDAO.save(role);
        if (addedRoles == null) {
            return ResponseEntity.noContent().build();
        }
        pgPoliciesDAO.createRole(addedRoles.getNom());
        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{username}")
                .buildAndExpand(addedRoles.getNom())
                .toUri();

        return ResponseEntity.created(location).build();
    }

    /*
        add Policies.
     */
    @PostMapping(value = "/Policies")
    public ResponseEntity<Void> addPolicy(@Valid @RequestBody Policies policies) {
        if (policies == null) {
            return ResponseEntity.noContent().build();
        }
        String roleName = Optional.ofNullable(policies)
                .map(p -> String.format("POLICY_%s", p.getNom().getNom()).toLowerCase())
                .orElse("");
        if (pgPoliciesDAO.getRole(roleName).isPresent()) {
            return ResponseEntity.status(HttpStatus.CONFLICT).build();
        } else {
            policies.getNom().setNom(roleName);
            addRole(policies.getNom());
            pgPoliciesDAO.createPolicy(policies);
        }

        Policies addPolicy = policiesDAO.save(policies);
        if (addPolicy == null) {
            return ResponseEntity.noContent().build();
        }
        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(addPolicy.getId())
                .toUri();

        return ResponseEntity.created(location).build();
    }

    /*
        delete user  with username
     */
    @DeleteMapping(value = "/Users/{username}")
    public void deleteUserByUsername(@PathVariable String username) {
        Users users = usersDAO.findById(username)
                .orElseThrow(() -> new MissingUserException(String.format("L'utilisateur %s n'existe pas", username)));
        usersDAO.delete(users);
    }

    /*
        delete authority  with username
     */
    @DeleteMapping(value = "/Authorities/{username}/{authorities}")
    public void deleteAuthoritiesByUsername(@PathVariable String username, @PathVariable String authorities) {
        Roles dbRole = rolesDAO.findById(authorities)
                .orElseThrow(() -> new MissingRolesException("Le role avec le nom  " + authorities + " est INTROUVABLE."));
        Authorities dbAuthorities = authoritiesDAO.findById(new AuthoritiesID(username, dbRole))
                .orElseThrow(() -> new MissingAuthoritiesException(String.format("L'authorithies %s n'existe pas pour l'utilisateur %s", authorities, username)));
        authoritiesDAO.delete(dbAuthorities);
    }

    /*
        delete user
     */
    @DeleteMapping(value = "/Users")
    public void deleteUser(@RequestBody Users users) {
        Users dbUsers = usersDAO.findById(users.getUsername())
                .orElseThrow(() -> new MissingUserException(String.format("L'utilisateur %s n'existe pas", users.getUsername())));
        usersDAO.delete(dbUsers);
    }

    /*
        delete authority
     */
    @DeleteMapping(value = "/Roles")
    public void deleteRoles(@RequestBody Roles roles) {
        Roles dbRole = rolesDAO.findById(roles.getNom())
                .orElseThrow(() -> new MissingRolesException("Le role avec le nom  " + roles.getNom() + " est INTROUVABLE."));
        rolesDAO.delete(dbRole);
        pgPoliciesDAO.dropRole(dbRole.getNom());
    }

    /*
        delete policy
     */
    @DeleteMapping(value = "/Policies")
    public void deletePolicies(@RequestBody Policies policies) {
        Policies dbPolicy = policiesDAO.findById(policies.getId())
                .orElseThrow(() -> new MissingRolesException("Le role avec le nom  " + policies.getId() + " est INTROUVABLE."));
        authoritiesDAO.findAll().stream()
                .filter(a -> a.getAuthorities().equals(policies.getNom().getNom()))
                .peek(a -> System.out.println(a.getAuthorities() + " " + policies.getNom().getNom()))
                .forEach(this::deleteAuthorities);
        pgPoliciesDAO.dropPolicy(policies);
        String roleName = Optional.ofNullable(policies)
                .map(p -> p.getNom().getNom().toLowerCase())
                .orElse("");
        policiesDAO.delete(dbPolicy);
        deleteRoles(dbPolicy.getNom());
    }

    /*
        delete authorites  with username
     */
    @DeleteMapping(value = "/Authorities")
    public void deleteAuthorities(@RequestBody Authorities authorities) {
        Roles dbRole = rolesDAO.findById(authorities.getRoles().getNom())
                .orElseThrow(() -> new MissingRolesException("Le role avec le nom  " + authorities.getUsername() + " est INTROUVABLE."));
        Authorities dbAuthorities = authoritiesDAO.findById(new AuthoritiesID(authorities.getUsername(), dbRole))
                .orElseThrow(() -> new MissingAuthoritiesException(String.format("L'authorithies %s n'existe pas pour l'utilisateur %s", authorities.getAuthorities(), authorities.getUsername())));
        authoritiesDAO.delete(authorities);
        pgPoliciesDAO.removeToGroup(authorities);
    }

    /*
        update user
     */
    @PutMapping(value = "/Users")
    public void updateUser(@RequestBody Users users) {
        Users dbUsers = usersDAO.findById(users.getUsername())
                .orElseThrow(() -> new MissingUserException(String.format("L'utilisateur %s n'existe pas", users.getUsername())));
        usersDAO.save(users);
    }

    /*
        update user
     */
    @PutMapping(value = "/Authorities")
    public void updateAuthorities(@RequestBody Authorities authorities) {
        Roles dbRole = rolesDAO.findById(authorities.getUsername())
                .orElseThrow(() -> new MissingRolesException("Le role avec le nom  " + authorities.getUsername() + " est INTROUVABLE."));
        Authorities dbAuthorities = authoritiesDAO.findById(new AuthoritiesID(authorities.getUsername(), dbRole))
                .orElseThrow(() -> new MissingAuthoritiesException(String.format("L'authorithies %s n'existe pas pour l'utilisateur %s", authorities.getAuthorities(), authorities.getUsername())));
        authoritiesDAO.save(authorities);
    }

    /*
        update user
     */
    @PutMapping(value = "/Policies")
    public void updatePolicy(@RequestBody Policies policies) {
        Policies dbPolicy = policiesDAO.findById(policies.getId())
                .orElseThrow(() -> new MissingUserException(String.format("L'utilisateur %s n'existe pas", policies.getId())));
        policiesDAO.save(policies);
    }
}
