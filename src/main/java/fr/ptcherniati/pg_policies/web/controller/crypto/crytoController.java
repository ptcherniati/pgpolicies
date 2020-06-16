package fr.ptcherniati.pg_policies.web.controller.crypto;

import fr.ptcherniati.pg_policies.model.crypto.Crypto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.converter.json.MappingJacksonValue;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.SessionAttributes;

import javax.servlet.http.HttpSession;
import java.io.UnsupportedEncodingException;
import java.security.NoSuchAlgorithmException;

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
            session.setAttribute("verifier", Crypto.getInstance());
        }
        return new MappingJacksonValue(session.getAttribute("verifier"));
    }
}