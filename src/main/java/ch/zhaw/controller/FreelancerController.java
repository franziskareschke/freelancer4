package ch.zhaw.controller;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured; // zusätzlicher Import für Sicherheit SW08
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;
import ch.zhaw.freelancer4u.model.Freelancer;
import ch.zhaw.freelancer4u.model.FreelancerCreateDTO;
import ch.zhaw.freelancer4u.model.MailInformation;
import ch.zhaw.freelancer4u.repository.FreelancerRepository;
import ch.zhaw.freelancer4u.service.MailValidatorService;

import java.util.Optional;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;

@RestController
@RequestMapping("/api")
public class FreelancerController {

    @Autowired
    FreelancerRepository freelancerRepository;

    @Autowired
    MailValidatorService mailValidatorService;

    // POST FREELANCER
    // Berechtigung Erklärung SW08: Nun muss auch noch das Backend geschützt werden,
    // so dass User ohne Admin-Rolle nicht auf die
    // Admin-Endpoints zugreifen können. Die Annotation @Secured erlaubt es bei
    // jedem Endpoint diejenigen Rollen zu definieren, welche diesen Aufrufen
    // können.
    @PostMapping("/freelancer")
    @Secured("ROLE_admin") // Nur die Rolle Admin darf einen POST-Request auf job ausführen
    public ResponseEntity<Freelancer> createFreelancer(@RequestBody FreelancerCreateDTO fDTO) {

        // Aufruf der validateEmail-Funktion des MailValidatorService
        MailInformation mailInfo = mailValidatorService.validateEmail(fDTO.getEmail());

        // Überprüfung, ob die E-Mail-Adresse gültig ist
        if (mailInfo != null && mailInfo.isFormat() && !mailInfo.isDisposable() && mailInfo.isDns()) { // true Werte für
            // format,
            // disposable,
            // dns
            // Fortfahren, wenn die E-Mail-Adresse gültig ist
            Freelancer fDAO = new Freelancer(fDTO.getEmail(), fDTO.getName());
            Freelancer f = freelancerRepository.save(fDAO);
            return new ResponseEntity<>(f, HttpStatus.CREATED);
        } else {
            // Wenn die E-Mail-Adresse ungültig ist, HttpStatus.BAD_REQUEST zurückgeben
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/freelancer")
    public ResponseEntity<List<Freelancer>> getAllFreelancer() {
        List<Freelancer> allFree = freelancerRepository.findAll();
        return new ResponseEntity<>(allFree, HttpStatus.OK);
    }

    // bestimmte Daten von Freelancer abfragen
    @GetMapping("/freelancer/{id}")
    public ResponseEntity<Freelancer> getFreelancerById(@PathVariable String id) {
        Optional<Freelancer> optFreelancer = freelancerRepository.findById(id);

        if (optFreelancer.isPresent()) { // isPresent --> wenn es den Freelancer/Wert gibt
            return new ResponseEntity<>(optFreelancer.get(), HttpStatus.OK); // wenn isPresent OK, dann hol Wert +
                                                                             // Freelancer zurückgeben
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND); // wenn ID nicht gefunden --> NOT_FOUND zurückgeben
        }

    }

    @GetMapping("/me/freelancer")
    public ResponseEntity<Freelancer> assignToMe(@AuthenticationPrincipal Jwt jwt) {
        String userEmail = jwt.getClaimAsString("email");

        Freelancer foundedFreelancer = freelancerRepository.findFirstByEmail(userEmail);
        if (foundedFreelancer != null) {
            return new ResponseEntity<>(foundedFreelancer, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }
}
