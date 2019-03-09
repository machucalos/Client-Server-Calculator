import java.net.ServerSocket;
import java.net.Socket;
import java.io.IOException;
import java.io.Serializable;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.lang.ClassNotFoundException;
class CalObject implements Serializable{
  private char operator;
  private double val_1;
  private double val_2;
  public char getOp(){
    return operator;
  }
  public double getV1(){
    return val_1;
  }
  public double getV2(){
    return val_2;
  }
  public CalObject(char op, double v1, double v2 ){
    operator = op;
    val_1 = v1;
    val_2 = v2;
  }
  public String toString(){
    return operator + " " + String.valueOf(val_1) + " " + String.valueOf(val_2);
  }
}
public class Server {
  final static int port = 9000;
  private ServerSocket serversocket = null;
  private Socket clientcbsocket = null;
  public Server() {

    try{
      serversocket = new ServerSocket(port);
      System.out.println("Listening on port " + port);
      while(true){

        clientcbsocket = serversocket.accept();
        System.out.println("Connected From " + clientcbsocket.getInetAddress().getLocalHost());
        ObjectInputStream in = new ObjectInputStream(clientcbsocket.getInputStream());

        //CalObject obj = (CalObject)in.readObject();
        char op = (char)in.readObject();
        double v1 = (double)in.readObject();
        double v2 = (double)in.readObject();
        // System.out.println(obj);


        ObjectOutputStream out = new ObjectOutputStream(clientcbsocket.getOutputStream());

        out.writeObject(processOperation(new CalObject(op, v1, v2)));
        out.flush();
        out.close();
        in.close();
      }



    }catch(IOException e){
      System.out.print(e.getMessage());
    }catch(ClassNotFoundException e){
        System.out.print(e.getMessage());
    }

  }
  private String processOperation(CalObject o)
  {
    double result = 0;
    switch (o.getOp())
        {
            case '+':
                result = o.getV1() + o.getV2();
                break;
            case '-':
                result = o.getV1() - o.getV2();
                break;
            case '*':
                result = o.getV1() * o.getV2();
                break;
            case '/':
                result = o.getV1() / o.getV2();
                break;
            default:
                break;
        }
      String rtn_val = o.getV1() + " " + o.getOp() + " " + o.getV2() + " = " + result;
      System.out.println(rtn_val);
      return rtn_val;
  }
  public static void main(String[] args) {
    Server server = new Server();

  }
}
