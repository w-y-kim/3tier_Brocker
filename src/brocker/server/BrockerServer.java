package brocker.server;

import java.io.*;
import java.net.*;

public class BrockerServer {					

	
	public BrockerServer(){
		
		try {
			ServerSocket server = new ServerSocket(7777);
			System.out.println("Brocker 서버 시작");
			  
			while(true){
				Socket client = server.accept();
				System.out.println(client.getInetAddress().getHostAddress() + " 클라이언트 접속");

				ObjectInputStream ois = new ObjectInputStream(client.getInputStream());
				ObjectOutputStream oos = new ObjectOutputStream(client.getOutputStream());
				
				//읽고처리하고쓰고
				//여기서 계속 돌고 있으면 다른 클라이언트의 접속을 막기 때문에 다중접속은 스레드로 처리해주는 것이다. 
				//때문에 독립적을 실행 가능한 또 다른 메인을 만들어 주는 것 
				
				BrockerServerThread bst = new BrockerServerThread(ois,oos);
				Thread t = new Thread(bst);//독립적으로 떨어져 나가도록 
				t.start();//runnable인터페이스를 구현한 BrockerServerThread의 run()에 정의된 내용이 독립적으로 실행
			}//while
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	public static void main(String[] args) {
		new BrockerServer(); 
	}

}
