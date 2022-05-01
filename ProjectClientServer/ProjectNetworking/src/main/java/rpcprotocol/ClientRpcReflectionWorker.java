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
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.Socket;

public class ClientRpcReflectionWorker implements Runnable, Observer {
    private Service service;
    private Socket connection;

    private ObjectInputStream input;
    private ObjectOutputStream output;
    private volatile boolean connected;

    public ClientRpcReflectionWorker(Service service, Socket connection) {
        this.service = service;
        this.connection = connection;
        try{
            output=new ObjectOutputStream(connection.getOutputStream());
            output.flush();
            input=new ObjectInputStream(connection.getInputStream());
            connected=true;
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void ticketsSold(Customer customer) throws ServiceException {
        Response resp=new Response.Builder().type(ResponseType.TICKETS_SOLD).data(customer).build();
        System.out.println("Sold tickets");
        try {
            sendResponse(resp);
        } catch (IOException e) {
            throw new ServiceException("Sending error: "+e);
        }
    }

    @Override
    public void run() {
        while(connected){
            try {
                Object request=input.readObject();
                Response response=handleRequest((Request)request);
                if (response!=null){
                    sendResponse(response);
                }
            } catch (IOException e) {
                e.printStackTrace();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        try {
            input.close();
            output.close();
            connection.close();
        } catch (IOException e) {
            System.out.println("Error "+e);
        }
    }

    private static Response okResponse=new Response.Builder().type(ResponseType.OK).build();
    //  private static Response errorResponse=new Response.Builder().type(ResponseType.ERROR).build();
    private Response handleRequest(Request request){
        Response response=null;
        String handlerName="handle"+(request).type();
        System.out.println("HandlerName "+handlerName);
        try {
            Method method=this.getClass().getDeclaredMethod(handlerName, Request.class);
            response=(Response)method.invoke(this,request);
            System.out.println("Method "+handlerName+ " invoked");
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }

        return response;
    }

    private void sendResponse(Response response) throws IOException{
        System.out.println("sending response "+response);
        synchronized(output){
            output.writeObject(response);
            output.flush();
        }
    }
    private Response handleLOGIN(Request request){
        System.out.println("Login request ..."+request.type());
        Employee employee=(Employee)request.data();

        try {
            if(service.login(employee, this))
                return okResponse;
            return new Response.Builder().type(ResponseType.ERROR).data("Wrong Credentials").build();
        } catch (ServiceException e) {
            connected=false;
            return new Response.Builder().type(ResponseType.ERROR).data(e.getMessage()).build();
        }
    }

    private Response handleGET_ALL(Request request){
        System.out.println("Get All request ..."+request.type());

        try {
            return new Response.Builder().type(ResponseType.GET_LOGGED_FRIENDS).data(service.getAllMatches()).build();
        } catch (ServiceException e) {
            return new Response.Builder().type(ResponseType.ERROR).data(e.getMessage()).build();
        }
    }

    private Response handleGET_ALL_AVAILABLE(Request request){
        System.out.println("Get All request ..."+request.type());

        try {
            return new Response.Builder().type(ResponseType.GET_LOGGED_FRIENDS).data(service.sortMatches()).build();
        } catch (ServiceException e) {
            return new Response.Builder().type(ResponseType.ERROR).data(e.getMessage()).build();
        }
    }

    private Response handleSELL_TICKETS(Request request){
        System.out.println("Sell Tickets request ..."+request.type());
        Customer customer=(Customer) request.data();

        try {
            service.sellTickets(customer);
            return okResponse;
        } catch (ServiceException e) {
            return new Response.Builder().type(ResponseType.ERROR).data(e.getMessage()).build();
        }
    }
//    private Response handleLOGOUT(Request request){
////        System.out.println("Logout request...");
////        Employee employee=(Employee)request.data();
////        try {
////            service.logout(employee, this);
////            connected=false;
////            return okResponse;
////
////        } catch (ServiceException e) {
////            return new Response.Builder().type(ResponseType.ERROR).data(e.getMessage()).build();
////        }
//    }

}
