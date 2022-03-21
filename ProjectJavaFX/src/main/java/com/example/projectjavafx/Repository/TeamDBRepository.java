package com.example.projectjavafx.Repository;

import com.example.projectjavafx.Model.Team;
import com.example.projectjavafx.Utils.JdbcUtils;
import com.example.projectjavafx.Validators.DuplicateException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

public class TeamDBRepository implements TeamRepository{
    private JdbcUtils dbUtils;

    private static final Logger logger= LogManager.getLogger();

    public TeamDBRepository(Properties props) {
        logger.info("Initializing TeamDBRepository with properties: {} ",props);
        dbUtils=new JdbcUtils(props);
    }

    @Override
    public Team findOne(Integer id) {
        logger.traceEntry();
        Connection con = dbUtils.getConnection();

        String query = "select * from teams where id = (?)";
        try(PreparedStatement preStmt = con.prepareStatement(query)){
            preStmt.setInt(1, id);
            try(ResultSet result = preStmt.executeQuery()){
                while(result.next()){

                    String teamName = result.getString("team_name");
                    Team team = new Team(teamName);
                    team.setId(id);

                    logger.traceExit();
                    return team;
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
    public Iterable<Team> findAll() {
        logger.traceEntry();
        Connection con = dbUtils.getConnection();
        List<Team> teams = new ArrayList<>();
        try(PreparedStatement preStmt=con.prepareStatement("select * from teams")){
            try(ResultSet result = preStmt.executeQuery()){
                while(result.next()){
                    int id = result.getInt("id");
                    String teamName = result.getString("team_name");
                    Team team = new Team(teamName);
                    team.setId(id);
                    teams.add(team);
                }
            }
        }
        catch (SQLException e){
            logger.error(e);
            System.err.println("Error DB "+ e);
        }
        logger.traceExit();
        return teams;
    }

    @Override
    public void save(Team team) {
        logger.traceEntry("saving task {} ", team);
        Connection con = dbUtils.getConnection();

        if(checkExistence(team.getTeamName()))
            throw new DuplicateException("Team already exists");

        String query = "insert into teams (team_name) values (?)";
        try(PreparedStatement preStmt = con.prepareStatement(query)){

            preStmt.setString(1, team.getTeamName());
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

        String query = "delete from teams where id = (?)";
        try(PreparedStatement preStmt = con.prepareStatement(query)){
            preStmt.setInt(1, id);

            int result = preStmt.executeUpdate();
            logger.trace("Saved {} instances", result);

        }catch (SQLException ex){
            logger.error(ex);
            System.err.println("Error DB " + ex);
        }
        logger.traceExit();
    }

    @Override
    public void update(Team entity) {
        logger.traceEntry();
        Connection con = dbUtils.getConnection();

        String query = "update teams set team_name = (?) where id = (?)";
        try(PreparedStatement preStmt = con.prepareStatement(query)){
            preStmt.setString(1, entity.getTeamName());
            preStmt.setInt(2, entity.getId());

            int result = preStmt.executeUpdate();
            logger.trace("Saved {} instances", result);

        }catch (SQLException ex){
            logger.error(ex);
            System.err.println("Error DB " + ex);
        }
        logger.traceExit();
    }

    @Override
    public Team findOneByTeamName(String teamName) {
        logger.traceEntry();
        Connection con = dbUtils.getConnection();

        String query = "select * from teams where team_name = (?)";
        try(PreparedStatement preStmt = con.prepareStatement(query)){
            preStmt.setString(1, teamName);
            try(ResultSet result = preStmt.executeQuery()){
                while(result.next()){

                    int id = result.getInt("id");
                    Team team = new Team(teamName);
                    team.setId(id);

                    logger.traceExit();
                    return team;
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
    public boolean checkExistence(String teamName) {
        Connection con = dbUtils.getConnection();

        String query = "select * from teams where team_name = (?)";
        try(PreparedStatement preStmt = con.prepareStatement(query)){
            preStmt.setString(1, teamName);
            try(ResultSet result = preStmt.executeQuery()){
                while(result.next()){
                    return true;
                }
            }
        }catch (SQLException ex){
            logger.error(ex);
            System.err.println("Error DB " + ex);
        }
        return false;
    }
}
