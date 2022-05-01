package protobuffprotocol;

import Model.Customer;
import Model.Employee;
import Model.Match;
import Services.Observer;
import Services.Service;
import Services.ServiceException;
import rpcprotocol.Request;
import rpcprotocol.Response;
import rpcprotocol.ResponseType;
import rpcprotocol.ServicesRpcProxy;

import java.io.*;
import java.net.Socket;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

public class ProtoProxy implements Service {
    private String host;
    private int port;

    private Observer client;

    private InputStream input;
    private OutputStream output;
    private Socket connection;

    private BlockingQueue<Protobufs.Response> qresponses;
    private volatile boolean finished;

    public ProtoProxy(String host, int port) {
        this.host = host;
        this.port = port;
        qresponses=new LinkedBlockingQueue<Protobufs.Response>();
    }

    private void initializeConnection() throws ServiceException {
        try {
            connection=new Socket(host,port);
            output=connection.getOutputStream();
            //output.flush();
            input=connection.getInputStream();     //new ObjectInputStream(connection.getInputStream());
            finished=false;
            startReader();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void closeConnection() {
        finished=true;
        try {
            input.close();
            output.close();
            connection.close();
            client=null;
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    private void sendRequest(Protobufs.Request request)throws ServiceException {
        try {
            System.out.println("Sending request ..."+request);
            synchronized(output){
                request.writeDelimitedTo(output);
                output.flush();
            }
            System.out.println("Request sent.");
        } catch (IOException e) {
            throw new ServiceException("Error sending object "+e);
        }

    }

    private Protobufs.Response readResponse() throws ServiceException {
        Protobufs.Response response=null;
        try{

            response=qresponses.take();

        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return response;
    }

    private void startReader(){
        Thread tw=new Thread(new ReaderThread());
        tw.start();
    }

    private void handleUpdate(Protobufs.Response updateResponse){
        switch (updateResponse.getType()){
            case TicketsSold: {
                Customer customer = ProtoUtils.getCustomer(updateResponse);
                try {
                    client.ticketsSold(customer);
                } catch (ServiceException e) {
                    e.printStackTrace();
                }
                break;
            }
        }

    }

    @Override
    public boolean loginAdmin(String pass) throws ServiceException {
        return false;
    }

    @Override
    public boolean login(Employee employee, Observer client) throws ServiceException {
        initializeConnection();
        System.out.println("Login request ...");
        sendRequest(ProtoUtils.createLoginRequest(employee));
        Protobufs.Response response=readResponse();
        if (response.getType()==Protobufs.Response.Type.Ok){
            this.client=client;
            return true;
        }
        if (response.getType()==Protobufs.Response.Type.Error){
            String errorText=ProtoUtils.getError(response);
            closeConnection();
            throw new ServiceException(errorText);
        }
        return false;
    }

    @Override
    public void addTeam(String teamName) throws ServiceException {

    }

    @Override
    public void addMatch(String teamName1, String teamName2, int ticketPrice, int numberOfSeats, String status) throws ServiceException {

    }

    @Override
    public void addEmployee(String firstName, String lastName, String username, String pass) throws ServiceException {

    }

    @Override
    public Iterable<Match> getAllMatches() throws ServiceException {
        System.out.println("GetAll request ...");
        sendRequest(ProtoUtils.createGetAllRequest());
        Protobufs.Response response=readResponse();

        if (response.getType() == Protobufs.Response.Type.Error){
            String err=ProtoUtils.getError(response);
            throw new ServiceException(err);
        }
        return ProtoUtils.getMatches(response);
    }

    @Override
    public void sellTickets(Customer customer) throws ServiceException {
        System.out.println("Sell tickets request ...");
        sendRequest(ProtoUtils.createSellTicketsRequest(customer));
        Protobufs.Response response=readResponse();

        if (response.getType() == Protobufs.Response.Type.Error){
            String err=ProtoUtils.getError(response);
            throw new ServiceException(err);
        }
    }

    @Override
    public Iterable<Match> sortMatches() throws ServiceException {
        System.out.println("GetAll Available request ...");
        sendRequest(ProtoUtils.createGetAllAvailableRequest());
        Protobufs.Response response=readResponse();

        if (response.getType() == Protobufs.Response.Type.Error){
            String err=ProtoUtils.getError(response);
            throw new ServiceException(err);
        }
        return ProtoUtils.getMatches(response);
    }

    @Override
    public void logout(Employee employee, Observer client) throws ServiceException {
        sendRequest(ProtoUtils.createLogoutRequest(employee));
        Protobufs.Response response=readResponse();

        if (response.getType()==Protobufs.Response.Type.Error){
            String errorText=ProtoUtils.getError(response);
            throw new ServiceException(errorText);
        }
        closeConnection();
    }

    private class ReaderThread implements Runnable{
        public void run() {
            while(!finished){
                try {
                    Protobufs.Response response=Protobufs.Response.parseDelimitedFrom(input);
                    System.out.println("response received "+response);

                    if (isUpdateResponse(response.getType())){
                        handleUpdate(response);
                    }else{
                        try {
                            qresponses.put(response);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                } catch (IOException e) {
                    System.out.println("Reading error "+e);
                }
            }
        }
    }
    private boolean isUpdateResponse(Protobufs.Response.Type type){
        if (type == Protobufs.Response.Type.TicketsSold) {
            return true;
        }
        return false;
    }
}
