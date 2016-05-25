package brocker.server;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;

import brocker.protocol.Command;
import brocker.vo.Customer;
import brocker.vo.Shares;
import brocker.vo.Stock;
import brocker.dao.Database;
import brocker.exception.DuplicateIDException;
import brocker.exception.RecordNotFoundException;

public class BrockerServerThread implements Runnable {

	private ObjectInputStream ois;
	private ObjectOutputStream oos;
	private boolean exit;// 무한반복해서 실행할 때 문제가 .. 사용자가 접속 끊으면 readObject에서 예외가
							// 터진다, 플래그 변수를 쓰는게 좋다
	private Database db;

	public BrockerServerThread(ObjectInputStream ois, ObjectOutputStream oos) {
		super();
		this.ois = ois;// 명령값, 파라미터 정보
		this.oos = oos;// 처리결과
		db = new Database();
	}

	@Override
	public void run() {// run 반복문 안의 플래그변수
		while (!exit) {
			try {
				System.out.println("dddddddddddddddd");

				Command cmd = (Command) ois.readObject();// 반환하는건 클라이언트가 보낸 요청인
															// 커맨드 객체 , 메신저 역할을
															// 하는 클래스를 설계한 것
				Object[] para = cmd.getArgs();
				// TODO 1.담겨있는 명령을 분기처리 하자!
				switch (cmd.getCmdValue()) {

				// 파라미터없는 명령들
				case Command.GET_ALL_STOCKS:
					ArrayList<Stock> stockList = db.getAllStock();
					cmd.setResult(stockList);
					oos.writeObject(cmd);

					break;
				case Command.GET_CUSTOMER:
					ArrayList<Customer> custList = db.getAllCustomer();
					cmd.setResult(custList);
					oos.writeObject(cmd);

					break;

				// 파라미터, 리턴 갖는 명령
				case Command.GET_PORTFOLIO:
					ArrayList<Shares> portList = db.getPortfolio((String) para[0]);
					cmd.setResult(portList);
					oos.writeObject(cmd);
					break;

				// 리턴값 없는 명령들{추가,수정,삭제,매수,매도}
				case Command.ADD_CUSTOMER:
					Customer c = (Customer) para[0];// 서버에서 VO import
					db.addCustomer(c);// 처리완료

					// cmd.setStatus(-20);// 예외발생하면 보내줌

					// Object result = new String("메소드실행");
					// cmd.setResult(result);
					// oos.writeObject(cmd);
					break;
				case Command.UPDATE_CUSTOMER:
					Customer updated_cus = (Customer) para[0];// 서버에서 VO import
					db.updateCustomer(updated_cus);// 처리완료

					// Object result = new String("메소드실행");
					// cmd.setResult(result);
					// oos.writeObject(cmd);
					break;

				case Command.DELETE_CUSTOMER:
					db.deleteCustomer((String) para[0]);
					break;
				case Command.BUY_SHARES:
					Shares boughtShare = (Shares) para[0];
					db.buyShares(boughtShare);
					break;
				case Command.SELL_SHARES:
					Shares sellingShare = (Shares) para[0];
					db.buyShares(sellingShare);
					break;
				default:
					break;
				}
			} catch (ClassNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
				exit = true; // 클라이언트가 접속 끊으면 플래그변수를 true로 바꾸면서 반복을 종료
			} catch (RecordNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (DuplicateIDException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

}
