/**
 * Created by chenglinyu on 2019-06-10
 * Description:
 */
import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

public class POPClient {
    private Socket socket;
    private Scanner socketReader;
    private PrintWriter socketWriter;

    public POPClient(String server, int port) throws IOException {
        try {
            socket = new Socket(server, port);
            socketReader = new Scanner(socket.getInputStream());
            socketWriter = new PrintWriter(socket.getOutputStream(), true);

        } catch (IOException e) {
            throw new IOException("the url or port of the server is wrong");
        }
    }

    //send  command to the server
    private void sendCommand(String command) {
        socketWriter.println(command);
    }

    //get answer from the server
    private String getAnswer() {
        String answer = socketReader.nextLine();
        return answer;
    }

    //login the mailbox
    private boolean login(String userName, String password) {
        sendCommand("USER " + userName);
        //if username is wrong,return false
        if (!getAnswer().startsWith("+OK")) {
            System.out.println("Wrong user name");
            return false;
        }
        //if the username is right then send the password
        sendCommand("PASS " + password);
        //if the password if wrong return false
        if (!getAnswer().startsWith("+OK")) {
            System.out.println("Wrong password");
            return false;
        }
        return true;
    }
    //the content of the mail finished with a "."
    private String getContent(){
        String content=socketReader.nextLine();
        String temp=socketReader.nextLine();
        while (!temp.equals(".")){
            content+=temp;
            temp=socketReader.nextLine();
        }
        return content;
    }

    public static void main(String[] args) {
        Scanner clientInput = new Scanner(System.in);
        System.out.println("welcome to the mailbox");
        System.out.println("Please input the URL of POP3 server");
        String server=clientInput.nextLine();
        //String server = "pop3.126.com";
        System.out.println("Please input the port of POP3 server");
        int port=clientInput.nextInt();
        //int port = 110;
        try {
            POPClient popClient = new POPClient(server, port);
            System.out.println(popClient.getAnswer());
            clientInput.nextLine();
            System.out.println("Please input the username");
            String userName=clientInput.nextLine();
            //String userName = "homework_1236@126.com";
            System.out.println("please input the password");
            String password=clientInput.nextLine();
            //String password = "homework123";
            if (popClient.login(userName, password)) {
                client:
                while (true) {
                    System.out.println("Choose your operation:");
                    System.out.println("1.Check the amount of mails and the size of the mailbox(STAT)");
                    System.out.println("2.Check the size of a mail(LIST [msg])");
                    System.out.println("3.Check the information of a mail(RETR msg)");
                    System.out.println("4.Delete a mail(DELE msg)");
                    System.out.println("5.Quit from the mailbox(QUIT)");
                    int command = clientInput.nextInt();
                    int num;
                    switch (command) {
                        case 1:
                            popClient.sendCommand("STAT");
                            System.out.println(popClient.getAnswer());
                            break;
                        case 2:
                            System.out.println("Please input the number of the mail you want to check:");
                            num = clientInput.nextInt();
                            popClient.sendCommand("LIST " + num);
                            System.out.println(popClient.getAnswer());
                            break;
                        case 3:
                            System.out.println("Please input the number of the mail you want to check:");
                            num = clientInput.nextInt();
                            popClient.sendCommand("RETR " + num);
                            String answer=popClient.getAnswer();
                            if(answer.startsWith("+OK")){
                                System.out.println(answer);
                                System.out.println(popClient.getContent());
                            }

                            break;
                        case 4:
                            System.out.println("Please input the number of the mail you want to delete:");
                            num = clientInput.nextInt();
                            popClient.sendCommand("DELE " + num);
                            System.out.println(popClient.getAnswer());
                            break;
                        case 5:
                            break client;

                    }

                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}