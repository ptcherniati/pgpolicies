package fr.ptcherniati.pg_policies.web.controller.crypto;

import fr.ptcherniati.pg_policies.dao.aliment.AlimentDAO;
import fr.ptcherniati.pg_policies.model.aliments.Aliment;
import fr.ptcherniati.pg_policies.model.crypto.Crypto;
import fr.ptcherniati.pg_policies.web.exceptions.MissingAlimentException;
import fr.ptcherniati.pg_policies.web.model.AlimentVO;
import lombok.extern.slf4j.Slf4j;
import org.apache.tomcat.util.codec.binary.Base64;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.json.MappingJacksonValue;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import org.springframework.web.util.UriComponentsBuilder;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.stream.Collectors;
@RestController()
@Slf4j
@RequestMapping("/api/v1")
@SessionAttributes("verifier")
public class crytoController {
    Crypto verifier;

    //Récupérer la liste des Aliments

    @RequestMapping(value = "/Verifier", method = RequestMethod.GET)
    public MappingJacksonValue verifier(HttpSession session) throws UnsupportedEncodingException, NoSuchAlgorithmException {
        Crypto crypto = (Crypto) session.getAttribute("verifier");
        if (session.getAttribute("verifier") == null) {
            session.setAttribute("verifier" ,Crypto.getInstance());
        }
	    return new MappingJacksonValue((Crypto) session.getAttribute("verifier"));
    }
}