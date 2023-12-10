package ch.zhaw.controller;

import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ch.zhaw.freelancer4u.model.Job;
import ch.zhaw.freelancer4u.model.JobStateChangeDTO;
import ch.zhaw.freelancer4u.model.Mail;
import ch.zhaw.freelancer4u.service.JobService;
import ch.zhaw.freelancer4u.service.MailService;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;

// Für weitere Informationen bzgl. Service siehe SW04 PDF
@RestController
@RequestMapping("/api/service")
public class ServiceController {

    @Autowired
    JobService jobService;

    @Autowired
    MailService mailService;

    // PUT Methode für Job-Zustand "ASSIGN"
    @PutMapping("/assignjob")
    @Secured("ROLE_admin")
    public ResponseEntity<Job> assignJob(@RequestBody JobStateChangeDTO changeS) {
        String freelancerEmail = changeS.getFreelancerEmail();
        String jobId = changeS.getJobId();
        Optional<Job> job = jobService.assignJob(jobId, freelancerEmail);
        if (job.isPresent()) {
            return new ResponseEntity<>(job.get(), HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }

    // PUT Methode für Job-Zustand "DONE"
    @PutMapping("/completejob")
    @Secured("ROLE_admin")
    public ResponseEntity<Job> completeJob(@RequestBody JobStateChangeDTO changeS) {
        String freelancerEmail = changeS.getFreelancerEmail();
        String jobId = changeS.getJobId();
        Optional<Job> job = jobService.completeJob(jobId, freelancerEmail);
        if (job.isPresent()) {
            return new ResponseEntity<>(job.get(), HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }

    @PutMapping("/me/assignjob")
    public ResponseEntity<Job> assignToMe(@RequestParam String jobId,
            @AuthenticationPrincipal Jwt jwt) {
        String userEmail = jwt.getClaimAsString("email");
        Optional<Job> job = jobService.assignJob(jobId, userEmail);
        if (job.isPresent()) {
            // Versende E-Mail mit aktuellem Projektstatus
            sendAssignmentEmail(job.get(), userEmail);
            return new ResponseEntity<>(job.get(), HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }

    ////// Methode zur Versendung der E-Mail beim Zuweisen eines Jobs ////////
    private void sendAssignmentEmail(Job job, String userEmail) {
        try {
            String subject = "Freelancer Job: " + job.getId();
            String message = "Der Status des Jobs mit der ID " + job.getId()
                    + " hat sich geändert. Neuer Status: "
                    + job.getJobState();

            Mail mail = new Mail();
            mail.setTo(userEmail);
            mail.setSubject(subject);
            mail.setMessage(message);

            mailService.sendMail(mail);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
