package fr.ptcherniati.pg_policies.web.controller.aliment;

import com.fasterxml.jackson.databind.ser.FilterProvider;
import com.fasterxml.jackson.databind.ser.impl.SimpleBeanPropertyFilter;
import com.fasterxml.jackson.databind.ser.impl.SimpleFilterProvider;
import fr.ptcherniati.pg_policies.dao.aliment.AlimentDAO;
import fr.ptcherniati.pg_policies.model.aliments.Aliment;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.json.MappingJacksonValue;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.validation.Valid;
import java.net.URI;

@RestController()
@Slf4j
@RequestMapping("/api/v1")
public class AlimentResources {

    @Autowired
    private AlimentDAO alimentDAO;


    //Récupérer la liste des Aliments

    @RequestMapping(value = "/Aliments", method = RequestMethod.GET)
    public MappingJacksonValue listeAliments() {

        Iterable<Aliment> produits = alimentDAO.findAll();

        SimpleBeanPropertyFilter monFiltre = SimpleBeanPropertyFilter.serializeAllExcept("prixAchat");

        FilterProvider listDeNosFiltres = new SimpleFilterProvider().addFilter("monFiltreDynamique", monFiltre);

        MappingJacksonValue produitsFiltres = new MappingJacksonValue(produits);

        produitsFiltres.setFilters(listDeNosFiltres);

        return produitsFiltres;
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
    public void deleteUser(@PathVariable Long id) {
        Aliment aliment = alimentDAO.getOne(id);
        alimentDAO.delete(aliment);
    }

    /*
        delete Aliments
     */
    @DeleteMapping(value = "/Aliments")
    public void deleteUser(@RequestBody Aliment aliment) {
        alimentDAO.delete(aliment);
    }
}