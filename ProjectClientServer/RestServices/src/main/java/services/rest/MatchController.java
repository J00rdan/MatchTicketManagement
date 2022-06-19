package services.rest;

import Model.Match;
import Persistence.MatchRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@CrossOrigin
@RestController
@RequestMapping("/app/match")
public class MatchController {
    private static final String template = "Hello, %s!";

    @Autowired
    private MatchRepository matchRepository;

    @RequestMapping( method= RequestMethod.GET)
    public Iterable<Match> getAll(){
        System.out.println("Get all users ...");
        return matchRepository.findAll();
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public ResponseEntity<?> getById(@PathVariable String id){
        System.out.println("Get by id "+id);
        Match match = matchRepository.findOne(Integer.valueOf(id));
        if (match == null)
            return new ResponseEntity<String>("Match not found", HttpStatus.NOT_FOUND);
        else
            return new ResponseEntity<Match>(match, HttpStatus.OK);
    }

    @RequestMapping(method = RequestMethod.POST)
    public Match create(@RequestBody Match match){
        System.out.println(match);

        return matchRepository.saveMatch(match);

    }

    @RequestMapping(value = "/{id}", method = RequestMethod.PUT)
    public Match update(@RequestBody Match match) {
        System.out.println("Updating user ...");
        matchRepository.update(match);
        return match;

    }

    @RequestMapping(value="/{id}", method= RequestMethod.DELETE)
    public ResponseEntity<?> delete(@PathVariable String id){
        System.out.println("Deleting user ... " + id);
        try {
            matchRepository.delete(Integer.valueOf(id));
            return new ResponseEntity<Match>(HttpStatus.OK);
        }catch (RuntimeException ex){
            System.out.println("Ctrl Delete user exception");
            return new ResponseEntity<String>(ex.getMessage(),HttpStatus.BAD_REQUEST);
        }
    }

    @ExceptionHandler(ServiceException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public String userError(ServiceException e) {
        return e.getMessage();
    }
}