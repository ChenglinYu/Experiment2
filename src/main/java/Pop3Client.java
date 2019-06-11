
import java.io.*;
import java.net.Socket;
import java.util.Scanner;

/**
 * Created by chenglinyu on 2019-06-11
 * Description:
 */
public class Pop3Client {
    // 客户端套接字
    private Socket clientSocket;

    // 套接字输入流
    private Scanner socketReader;

    // 套接字输出流
    private PrintWriter socketWriter;

    public Pop3Client(Socket clientSocket) throws IOException {
        this.clientSocket = clientSocket;
        this.socketReader = new Scanner(clientSocket.getInputStream());
        this.socketWriter = new PrintWriter(clientSocket.getOutputStream(), true); // Don't leave out flush
    }


    public static void main(String... args) {
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(System.in));
        System.out.print(">");


        try {

            // Establish connection
            // e.g. Pop3Client whu.edu.cn 110
            String connectionInfo = bufferedReader.readLine();
            String[] connectionInfoTokens = connectionInfo.split("\\s+");
            String applicationName = connectionInfoTokens[0];
            if (!applicationName.equals("Pop3Client")) {
                System.out.println("command not found");
            }
            String mailServer = connectionInfoTokens[1];
            int portNum = Integer.valueOf(connectionInfoTokens[2]);

            // 建立连接
            Socket connectionSocket = new Socket(mailServer, portNum);
            Pop3Client pop3Client = new Pop3Client(connectionSocket);

            // 打印欢迎语
            System.out.println(pop3Client.socketReader.nextLine());

            // 读取命令
            String command = bufferedReader.readLine();

            // 有些命令的返回仅为一行，有些则为多行，因此需要分类讨论

            // 只要不是退出命令，就一直不退出
            while (!command.equals("quit")) {

                // 发送命令
                pop3Client.socketWriter.println(command);

                if (command.equals("list") || command.startsWith("retr")) {
                    // 读取并打印所有返回消息
                    String tempLine = pop3Client.socketReader.nextLine();
                    System.out.print(tempLine + "\n");
                    while (!tempLine.equals(".")) {
                        tempLine = pop3Client.socketReader.nextLine();
                        System.out.print(tempLine + "\n");
                    }
                } else {
                    // 读取并打印所有返回消息
                    String tempLine = pop3Client.socketReader.nextLine();
                    System.out.print(tempLine + "\n");
                }

                try {
                    // 读取新命令
                    command = bufferedReader.readLine();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        } catch (
                Exception e) {
            e.printStackTrace();
        }
    }
}