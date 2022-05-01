package utils;

import Services.Service;
import rpcprotocol.ClientRpcReflectionWorker;

import java.net.Socket;


public class ConcurrentServer extends AbsConcurrentServer {
    private Service service;
    public ConcurrentServer(int port, Service service) {
        super(port);
        this.service = service;
        System.out.println("Chat- ChatRpcConcurrentServer");
    }

    @Override
    protected Thread createWorker(Socket client) {
        // ChatClientRpcWorker worker=new ChatClientRpcWorker(chatServer, client);
        ClientRpcReflectionWorker worker=new ClientRpcReflectionWorker(service, client);

        Thread tw=new Thread(worker);
        return tw;
    }

    @Override
    public void stop(){
        System.out.println("Stopping services ...");
    }
}
