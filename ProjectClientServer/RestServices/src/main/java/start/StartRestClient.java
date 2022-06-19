package start;

import Model.Match;
import org.springframework.web.client.RestClientException;
import rest.client.MatchesClient;
import services.rest.MatchController;
import services.rest.ServiceException;

public class StartRestClient {
    private final static MatchesClient matchesClient = new MatchesClient();

    public static void main(String[] args) {
        try{

            show(()->{
                for(Match m: matchesClient.getAll()){
                    System.out.println(m);
                }
            });

            Match matchT = new Match("ASD", "ASD", 10, 10, "Regular");
            show(()-> System.out.println(matchesClient.create(matchT)));

            show(()->{
                for(Match m: matchesClient.getAll()){
                    System.out.println(m);
                }
            });

            show(()->{
                System.out.println(matchesClient.getById(String.valueOf(25)));
            });

            Match newMatch = new Match("ZZZ", "WWW", 120, 120, "Regular");
            newMatch.setId(25);

            show(()->{
                matchesClient.update(newMatch);
            });

            show(()->{
                for(Match m: matchesClient.getAll()){
                    System.out.println(m);
                }
            });

        }catch(RestClientException ex){
            System.out.println("Exception ... "+ex.getMessage());
        }
    }



    private static void show(Runnable task) {
        try {
            task.run();
        } catch (ServiceException e) {
            //  LOG.error("Service exception", e);
            System.out.println("Service exception"+ e);
        }
    }
}
