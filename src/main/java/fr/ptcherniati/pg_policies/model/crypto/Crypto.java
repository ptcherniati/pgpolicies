package fr.ptcherniati.pg_policies.model.crypto;

import org.apache.tomcat.util.codec.binary.Base64;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Random;

public class Crypto {
    String verifier;
    String challenge;
    public Crypto() {
    }

    public static final Crypto getInstance() throws NoSuchAlgorithmException, UnsupportedEncodingException {
        Crypto crypto = new Crypto();
        byte[] array = new byte[32]; // length is bounded by 7
        new Random().nextBytes(array);
        crypto.verifier = Base64.encodeBase64URLSafeString(array);
        MessageDigest md = MessageDigest.getInstance("SHA-256");
        md.update(array, 0, array.length);
        byte[] digest = md.digest();
        crypto.challenge = Base64.encodeBase64URLSafeString(digest);
        return crypto;
    }

    public String getVerifier() {
        return verifier;
    }

    public void setVerifier(String verifier) {
        this.verifier = verifier;
    }

    public String getChallenge() {
        return challenge;
    }

    public void setChallenge(String challenge) {
        this.challenge = challenge;
    }
}
