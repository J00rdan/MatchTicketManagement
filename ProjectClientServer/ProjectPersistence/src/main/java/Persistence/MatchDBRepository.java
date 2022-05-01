package Persistence;

import Model.Match;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

public class MatchDBRepository implements MatchRepository{
    private JdbcUtils dbUtils;

    private static final Logger logger= LogManager.getLogger();

    private TeamDBRepository teamDBRepository;

    public MatchDBRepository(Properties props, TeamDBRepository teamDBRepository) {
        logger.info("Initializing TeamDBRepository with properties: {} ",props);
        dbUtils=new JdbcUtils(props);
        this.teamDBRepository = teamDBRepository;
    }

    @Override
    public Match findOne(Integer id) {
        logger.traceEntry();
        Connection con = dbUtils.getConnection();

        String query = "select * from matches where id = (?)";
        try(PreparedStatement preStmt = con.prepareStatement(query)){
            preStmt.setInt(1, id);
            try(ResultSet result = preStmt.executeQuery()){
                while(result.next()){

                    int id1 = result.getInt("team1_id");
                    int id2 = result.getInt("team2_id");
                    int ticketPrice = result.getInt("ticket_price");
                    int seatsAvailable = result.getInt("seats_available");
                    String status = result.getString("status");
                    Match match = new Match(teamDBRepository.findOne(id1), teamDBRepository.findOne(id2), ticketPrice, seatsAvailable, status);
                    match.setId(id);

                    logger.traceExit();
                    return match;
                }
            }
        }catch (SQLException ex){
            logger.error(ex);
            System.err.println("Error DB " + ex);
        }
        logger.traceExit();
        return null;

    }

    @Override
    public Iterable<Match> findAll() {
        logger.traceEntry();
        Connection con = dbUtils.getConnection();
        List<Match> matches = new ArrayList<>();
        try(PreparedStatement preStmt=con.prepareStatement("select * from matches")){
            try(ResultSet result = preStmt.executeQuery()){
                while(result.next()){
                    int id = result.getInt("id");
                    int id1 = result.getInt("team1_id");
                    int id2 = result.getInt("team2_id");
                    int ticketPrice = result.getInt("ticket_price");
                    int seatsAvailable = result.getInt("seats_available");
                    String status = result.getString("status");
                    Match match = new Match(teamDBRepository.findOne(id1), teamDBRepository.findOne(id2), ticketPrice, seatsAvailable, status);
                    match.setId(id);
                    matches.add(match);
                }
            }
        }
        catch (SQLException e){
            logger.error(e);
            System.err.println("Error DB "+ e);
        }
        logger.traceExit();
        return matches;
    }

    @Override
    public void save(Match match) {
        logger.traceEntry("saving task {} ", match);
        Connection con = dbUtils.getConnection();

        String query = "insert into matches (team1_id, team2_id, ticket_price, seats_available, status) values (?,?,?,?,?)";
        try(PreparedStatement preStmt = con.prepareStatement(query)){

            preStmt.setInt(1, match.getTeam1().getId());
            preStmt.setInt(2, match.getTeam2().getId());
            preStmt.setInt(3, match.getTicketPrice());
            preStmt.setInt(4, match.getSeatsAvailable());
            preStmt.setString(5, match.getStatus());
            int result = preStmt.executeUpdate();
            logger.trace("Saved {} instances", result);

        }catch (SQLException ex){
            logger.error(ex);
            System.err.println("Error DB " + ex);
        }
        logger.traceExit();
    }

    @Override
    public void delete(Integer id) {
        logger.traceEntry();
        Connection con = dbUtils.getConnection();

        String query = "delete from matches where id = (?)";
        try(PreparedStatement preStmt = con.prepareStatement(query)){
            preStmt.setInt(1, id);

            int result = preStmt.executeUpdate();
            logger.trace("Deleted {} instances", result);

        }catch (SQLException ex){
            logger.error(ex);
            System.err.println("Error DB " + ex);
        }
        logger.traceExit();
    }

    @Override
    public void update(Match entity) {
        logger.traceEntry();
        Connection con = dbUtils.getConnection();

        String query = "update matches set ticket_price = (?), seats_available = (?) where id = (?)";
        try(PreparedStatement preStmt = con.prepareStatement(query)){
            preStmt.setInt(1, entity.getTicketPrice());
            preStmt.setInt(2, entity.getSeatsAvailable());
            preStmt.setInt(3, entity.getId());

            int result = preStmt.executeUpdate();
            logger.trace("Saved {} instances", result);

        }catch (SQLException ex){
            logger.error(ex);
            System.err.println("Error DB " + ex);
        }
        logger.traceExit();
    }

    @Override
    public Iterable<Match> findAllAvailable() {
        logger.traceEntry();
        Connection con = dbUtils.getConnection();
        List<Match> matches = new ArrayList<>();
        try(PreparedStatement preStmt=con.prepareStatement("select * from matches where seats_available > 0 order by seats_available desc")){
            try(ResultSet result = preStmt.executeQuery()){
                while(result.next()){
                    int id = result.getInt("id");
                    int id1 = result.getInt("team1_id");
                    int id2 = result.getInt("team2_id");
                    int ticketPrice = result.getInt("ticket_price");
                    int seatsAvailable = result.getInt("seats_available");
                    String status = result.getString("status");
                    Match match = new Match(teamDBRepository.findOne(id1), teamDBRepository.findOne(id2), ticketPrice, seatsAvailable, status);
                    match.setId(id);
                    matches.add(match);
                }
            }
        }
        catch (SQLException e){
            logger.error(e);
            System.err.println("Error DB "+ e);
        }
        logger.traceExit();
        return matches;
    }
}
