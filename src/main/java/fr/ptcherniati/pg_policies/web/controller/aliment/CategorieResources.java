package fr.ptcherniati.pg_policies.web.controller.aliment;

import fr.ptcherniati.pg_policies.dao.aliment.CategorieDAO;
import fr.ptcherniati.pg_policies.model.aliments.Category;
import fr.ptcherniati.pg_policies.web.exceptions.MissingCategoryException;
import fr.ptcherniati.pg_policies.web.model.CategoryVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.json.MappingJacksonValue;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import org.springframework.web.util.UriComponentsBuilder;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.net.URI;
import java.util.stream.Collectors;

@RestController()
@Slf4j
@RequestMapping("/api/v1")
public class CategorieResources {

    @Autowired
    private CategorieDAO categorieDAO;


    //Récupérer la liste des produits

    @RequestMapping(value = "/Categories", method = RequestMethod.GET)
    public MappingJacksonValue listeCategories(HttpServletRequest request) {

        Iterable<CategoryVO> categories = categorieDAO.findAll()
                .stream()
                .map(a -> new CategoryVO(a))
                .peek(a -> a.setUri(UriComponentsBuilder.fromUriString(request.getRequestURL().toString()).path("/").path(Long.toString(a.getId())).build().toUri()))
                .collect(Collectors.toList());

        return new MappingJacksonValue(categories);
    }

    @RequestMapping(value = "/Categories/{id}", method = RequestMethod.GET)
    public MappingJacksonValue getCategoriesById(HttpServletRequest request, @PathVariable Long id) {

        CategoryVO categories = categorieDAO.findById(id)
                .map(a -> new CategoryVO(a))
                .map(a -> {
                    a.setUri(UriComponentsBuilder.fromUriString(request.getRequestURL().toString()).build().toUri());
                    return a;
                }).orElseThrow(() -> new MissingCategoryException(String.format("La category %s est manquente", id)));
        return new MappingJacksonValue(categories);
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
    public void deleteCategoryById(@PathVariable Long category_id) {
        Category category = categorieDAO.findById(category_id)
                .orElseThrow(() -> new MissingCategoryException(String.format("La category %s est manquente", category_id)));
        categorieDAO.delete(category);
    }

    /*
        delete Aliments
     */
    @DeleteMapping(value = "/Categories")
    public void deletecategory(@RequestBody Category category) {
        categorieDAO.delete(
                categorieDAO.findById(category.getId())
                        .orElseThrow(() -> new MissingCategoryException(String.format("La category %s est manquente", category.getId())))
        );
    }
}