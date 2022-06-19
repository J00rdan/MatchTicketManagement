package rest.client;

import Model.Match;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestTemplate;
import services.rest.ServiceException;

import java.util.concurrent.Callable;

public class MatchesClient {
    public static final String URL = "http://localhost:8080/app/match";

    private RestTemplate restTemplate = new RestTemplate();

    private <T> T execute(Callable<T> callable) {
        try {
            return callable.call();
        } catch (ResourceAccessException | HttpClientErrorException e) { // server down, resource exception
            throw new ServiceException(e);
        } catch (Exception e) {
            throw new ServiceException(e);
        }
    }

    public Match[] getAll() {
        return execute(() -> restTemplate.getForObject(URL, Match[].class));
    }

    public Match getById(String id) {
        return execute(() -> restTemplate.getForObject(String.format("%s/%s", URL, id), Match.class));
    }

    public Match create(Match match) {
        return execute(() -> restTemplate.postForObject(URL, match, Match.class));
    }

    public void update(Match match) {
        execute(() -> {
            restTemplate.put(String.format("%s/%s", URL, match.getId()), match);
            return null;
        });
    }

    public void delete(String id) {
        execute(() -> {
            restTemplate.delete(String.format("%s/%s", URL, id));
            return null;
        });
    }
}
