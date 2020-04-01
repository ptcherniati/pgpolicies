package fr.ptcherniati.pg_policies.web.controller.aliment;

import com.fasterxml.jackson.databind.ser.FilterProvider;
import com.fasterxml.jackson.databind.ser.impl.SimpleBeanPropertyFilter;
import com.fasterxml.jackson.databind.ser.impl.SimpleFilterProvider;
import fr.ptcherniati.pg_policies.dao.aliment.CategorieDAO;
import fr.ptcherniati.pg_policies.model.aliments.Category;
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
public class CategorieResources {

    @Autowired
    private CategorieDAO categorieDAO;


    //Récupérer la liste des produits

    @RequestMapping(value = "/Categories", method = RequestMethod.GET)
    public MappingJacksonValue listeCategories() {

        Iterable<Category> produits = categorieDAO.findAll();

        SimpleBeanPropertyFilter monFiltre = SimpleBeanPropertyFilter.serializeAllExcept("prixAchat");

        FilterProvider listDeNosFiltres = new SimpleFilterProvider().addFilter("monFiltreDynamique", monFiltre);

        MappingJacksonValue produitsFiltres = new MappingJacksonValue(produits);

        produitsFiltres.setFilters(listDeNosFiltres);

        return produitsFiltres;
    }

    /*
        add Authorities.
     */
    @PostMapping(value = "/Categories")
    public ResponseEntity<Void> addACategories(@Valid @RequestBody Category category) {
        Category addedCategories = categorieDAO.save(category);
        if (addedCategories == null) {
            return ResponseEntity.noContent().build();
        }

        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{categories}")
                .buildAndExpand(addedCategories.getId())
                .toUri();

        return ResponseEntity.created(location).build();
    }

    /*
        delete Categories with id
     */
    @DeleteMapping(value = "/Categories/{category_id}")
    public void deleteUser(@PathVariable Long category_id) {
        Category category = categorieDAO.getOne(category_id);
        categorieDAO.delete(category);
    }

    /*
        delete Aliments
     */
    @DeleteMapping(value = "/Categories")
    public void deleteUser(@RequestBody Category category) {
        categorieDAO.delete(category);
    }
}