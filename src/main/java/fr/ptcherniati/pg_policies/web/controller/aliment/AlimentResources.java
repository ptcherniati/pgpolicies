package fr.ptcherniati.pg_policies.web.controller.aliment;

import fr.ptcherniati.pg_policies.dao.PgPoliciesDAO;
import fr.ptcherniati.pg_policies.dao.aliment.AlimentDAO;
import fr.ptcherniati.pg_policies.model.aliments.Aliment;
import fr.ptcherniati.pg_policies.web.exceptions.MissingAlimentException;
import fr.ptcherniati.pg_policies.web.model.AlimentVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.json.MappingJacksonValue;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import org.springframework.web.util.UriComponentsBuilder;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.net.URI;
import java.util.LinkedList;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController()
@Slf4j
@RequestMapping("/api/v1")
public class AlimentResources {

    @Autowired
    private AlimentDAO alimentDAO;
    @Autowired
    private PgPoliciesDAO pgPoliciesDAO;
    @Autowired
    private AuthenticationManager authManager;


    //Récupérer la liste des Aliments

    @RequestMapping(value = "/Aliments", method = RequestMethod.GET)
    public MappingJacksonValue listeAliments(HttpServletRequest request) {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String username;
        if (principal instanceof UserDetails) {
            username = ((UserDetails) principal).getUsername();
        } else {
            username = principal.toString();
        }
        pgPoliciesDAO.use(username);

        Iterable<AlimentVO> aliments = alimentDAO.findAll()
                .stream()
                .map(a -> new AlimentVO(a))
                .peek(a -> {
                    a.getCategoryVO().setUri(UriComponentsBuilder.fromUriString(request.getRequestURL().toString().replaceAll("Aliments.*", "Categories")).path("/").path(Long.toString(a.getCategoryVO().getId())).build().toUri());
                    a.setUri(UriComponentsBuilder.fromUriString(request.getRequestURL().toString()).path("/").path(Long.toString(a.getId())).build().toUri());
                })
                .collect(Collectors.toList());
        pgPoliciesDAO.use("dbuser");

        return new MappingJacksonValue(Optional.ofNullable(aliments).orElse(new LinkedList<>()));
    }
    //Récupérer un Aliments par id

    @RequestMapping(value = "/Aliments/{id}", method = RequestMethod.GET)
    public MappingJacksonValue getAlimentsById(HttpServletRequest request, @PathVariable Long id) {
        AlimentVO aliments = alimentDAO.findById(id)
                .map(a -> new AlimentVO(a))
                .map(a -> {
                    a.getCategoryVO().setUri(UriComponentsBuilder.fromUriString(request.getRequestURL().toString().replaceAll("Aliments.*", "Categories")).path("/").path(Long.toString(a.getCategoryVO().getId())).build().toUri());
                    a.setUri(UriComponentsBuilder.fromUriString(request.getRequestURL().toString()).build().toUri());
                    return a;
                }).orElseThrow(() -> new MissingAlimentException(String.format("L'aliment' %s est manquente", id)));

        return new MappingJacksonValue(aliments);
    }

    /*
        add Aliments.
     */
    @PostMapping(value = "/Aliments")
    public ResponseEntity<Void> addAliments(@Valid @RequestBody Aliment aliment) {
        Aliment addedAliments = alimentDAO.save(aliment);
        if (addedAliments == null) {
            return ResponseEntity.noContent().build();
        }

        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{aliments}")
                .buildAndExpand(addedAliments.getId())
                .toUri();

        return ResponseEntity.created(location).build();
    }

    /*
        delete Aliments with id
     */
    @DeleteMapping(value = "/Aliments/{id}")
    public void deleteAlimentById(@PathVariable Long id) {
        Aliment aliment = alimentDAO.findById(id)
                .orElseThrow(() -> new MissingAlimentException(String.format("L'aliment' %s est manquente", id)));
        alimentDAO.delete(aliment);
    }

    /*
        delete Aliments
     */
    @DeleteMapping(value = "/Aliments")
    public void deleteAliment(@RequestBody Aliment aliment) {
        Aliment dnAliment1 = alimentDAO.findById(aliment.getId())
                .orElseThrow(() -> new MissingAlimentException(String.format("L'aliment' %s est manquente", aliment.getId())));
        alimentDAO.delete(aliment);
    }
}