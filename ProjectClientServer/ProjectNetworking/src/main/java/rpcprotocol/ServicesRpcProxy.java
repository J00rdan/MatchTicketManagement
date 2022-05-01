package rpcprotocol;

import Model.Customer;
import Model.Employee;
import Model.Match;
import Services.Observer;
import Services.Service;
import Services.ServiceException;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

public class ServicesRpcProxy implements Service {
    private String host;
    private int port;

    private Observer client;

    private ObjectInputStream input;
    private ObjectOutputStream output;
    private Socket connection;

    private BlockingQueue<Response> qresponses;
    private volatile boolean finished;

    public ServicesRpcProxy(String host, int port) {
        this.host = host;
        this.port = port;
        qresponses=new LinkedBlockingQueue<Response>();
    }

    private void initializeConnection() throws ServiceException {
        try {
            connection=new Socket(host,port);
            output=new ObjectOutputStream(connection.getOutputStream());
            output.flush();
            input=new ObjectInputStream(connection.getInputStream());
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

    private void sendRequest(Request request)throws ServiceException {
        try {
            synchronized(output){
                output.writeObject(request);
                output.flush();
            }
        } catch (IOException e) {
            throw new ServiceException("Error sending object "+e);
        }

    }

    private Response readResponse() throws ServiceException {
        Response response=null;
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

    @Override
    public boolean loginAdmin(String pass) throws ServiceException {
        return false;
    }

    @Override
    public boolean login(Employee employee, Observer client) throws ServiceException {
        initializeConnection();
        Request req=new Request.Builder().type(RequestType.LOGIN).data(employee).build();
        sendRequest(req);
        Response response=readResponse();
        if (response.type()== ResponseType.OK){
            this.client=client;
            return true;
        }
        if (response.type()== ResponseType.ERROR){
            String err=response.data().toString();
            closeConnection();
            throw new ServiceException(err);
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
//        initializeConnection();
        Request req=new Request.Builder().type(RequestType.GET_ALL).build();
        sendRequest(req);
        Response response=readResponse();
        if (response.type()== ResponseType.ERROR){
            String err=response.data().toString();
            throw new ServiceException(err);
        }
        return (Iterable<Match>) response.data();
    }

    @Override
    public void sellTickets(Customer customer) throws ServiceException {
        Request req=new Request.Builder().type(RequestType.SELL_TICKETS).data(customer).build();
        sendRequest(req);
        Response response=readResponse();
        if (response.type()== ResponseType.ERROR){
            String err=response.data().toString();
            throw new ServiceException(err);
        }
    }

    @Override
    public Iterable<Match> sortMatches() throws ServiceException {
//        initializeConnection();
        Request req=new Request.Builder().type(RequestType.GET_ALL_AVAILABLE).build();
        sendRequest(req);
        Response response=readResponse();
        if (response.type()== ResponseType.ERROR){
            String err=response.data().toString();
            throw new ServiceException(err);
        }
        return (Iterable<Match>) response.data();
    }

    public void logout(Employee employee, Observer client) throws ServiceException {

//        Request req=new Request.Builder().type(RequestType.LOGOUT).data(employee).build();
//        sendRequest(req);
//        Response response=readResponse();
//        closeConnection();
//        if (response.type()== ResponseType.ERROR){
//            String err=response.data().toString();
//            throw new ServiceException(err);
//        }
    }

    private void handleUpdate(Response response){

        if (response.type()== ResponseType.TICKETS_SOLD){
            Customer customer= (Customer) response.data();
            try {
                client.ticketsSold(customer);
            } catch (ServiceException e) {
                e.printStackTrace();
            }
        }
    }

    private boolean isUpdate(Response response){
        return response.type()== ResponseType.TICKETS_SOLD;
    }

    private class ReaderThread implements Runnable{
        public void run() {
            while(!finished){
                try {
                    Object response=input.readObject();
                    System.out.println("response received "+response);
                    if (isUpdate((Response)response)){
                        handleUpdate((Response)response);
                    }else{

                        try {
                            qresponses.put((Response)response);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                } catch (IOException e) {
                    System.out.println("Reading error "+e);
                } catch (ClassNotFoundException e) {
                    System.out.println("Reading error "+e);
                }
            }
        }
    }
}
