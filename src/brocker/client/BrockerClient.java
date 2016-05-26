package brocker.client;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;

import brocker.exception.DuplicateIDException;
import brocker.exception.InvalidTransactionException;
import brocker.exception.RecordNotFoundException;
import brocker.protocol.Command;
import brocker.vo.Customer;
import brocker.vo.Shares;
import brocker.vo.Stock;

public class BrockerClient {

	// private BrockerUI;
	private ObjectOutputStream oos;
	private ObjectInputStream ois;

	// 명령 및 파라미터 전달 위한 Command객체 생성 >> 서버스레드로 간다
	private Command cmd = new Command();

	public BrockerClient() {
		Socket socket;
		try {

			socket = new Socket("localhost", 7777);
			System.out.println("서버접속");
			oos = new ObjectOutputStream(socket.getOutputStream());
			ois = new ObjectInputStream(socket.getInputStream());
			System.out.println("스트림생성");
			// BrockerUI window = new BrockerUI();
			// window.frame.setVisible(true);// TODO 이 부분을 GUI에서 처리안해서 GUI
			// frame객체를
			// public

			// while (socket.isConnected()) {
			//
			// // 스트림 >> 서버로 보내준다 >> 생성자통해 스레드로 전달
			// try {
			// ois.readObject();
			// } catch (ClassNotFoundException e) {
			// // TODO Auto-generated catch block
			// e.printStackTrace();
			// }
			// oos.writeObject(cmd);
			// }

		} catch (UnknownHostException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	/*
	 * Database의 메소드 시그니처만 오버라이딩 각 메소드는 GUI에서 호출
	 * 
	 * 
	 * 
	 */



	public ArrayList<Stock> getAllStock() throws RecordNotFoundException {
		ArrayList<Stock> stockList = null;
		Command cmd = new Command(Command.GET_ALL_STOCKS);
		try {
		// 명령정보전달(파라미터없음)

		// 보냄
			oos.writeObject(cmd);
			
			// 처리결과반환in 
			cmd = (Command) ois.readObject();//여기서 못받아와서 명령도실행안됨
			stockList = (ArrayList<Stock>) cmd.getResult();
//
			// 예외처리결과(어차피 UI에서 해주지 않나?)
			int key = cmd.getStatus();
			if (key == Command.RECORD_NOTFOUND) {
				throw new RecordNotFoundException();
			}
			
		} catch (IOException | ClassNotFoundException e) {
			e.printStackTrace();
		}
		return stockList;
	}
//
	/**
	 * dlm2에 결과값인 배열을 전달
	 * 
	 * @return
	 * @throws RecordNotFoundException
	 */
	public ArrayList<Customer> getAllCustomer() throws RecordNotFoundException {
		System.out.println("클라: getAllCustomer요청실행");
		ArrayList<Customer> cusList = null;
		try {

			// 명령정보만 전달(파라미터 없음)
			// cmd.setCmdValue(Command.GET_ALL_CUSTOMER);// 리스트조회
			Command cmd = new Command(Command.GET_ALL_CUSTOMER);
			// 파라미터없음

			// 보냄
			oos.writeObject(cmd);// 객체단위로 정보 보냄
			// 처리결과반환
			cmd = (Command) ois.readObject();
			
			cusList = (ArrayList<Customer>) cmd.getResult();

			// 예외처리결과(어차피 UI에서 해주지 않나?)
			int key = cmd.getStatus();
			if (key == Command.RECORD_NOTFOUND) {
				throw new RecordNotFoundException();
			}

		} catch (ClassNotFoundException | IOException e) {
			e.printStackTrace();
		}
		return cusList;
	}

	public ArrayList<Shares> getPortfolio(String ssn) throws RecordNotFoundException {
		System.out.println("클라: getPortfolio요청실행");
		ArrayList<Shares> shareList = null;
		try {

			// 명령정보만 전달(파라미터 ssn)
			Command cmd = new Command(Command.GET_PORTFOLIO);
			Object[] array = { ssn };
			cmd.setArgs(array);
			
			// 보냄
			oos.writeObject(cmd);// 객체단위로 정보 보냄

			// 처리결과반환
			cmd = (Command) ois.readObject();
			shareList = (ArrayList<Shares>) cmd.getResult();

			// 예외처리결과(어차피 UI에서 해주지 않나?)
			int key = cmd.getStatus();
			if (key == Command.RECORD_NOTFOUND) {
				throw new RecordNotFoundException();
			}

		} catch (ClassNotFoundException | IOException e) {
			e.printStackTrace();
		}
		return shareList;
	}

	/**
	 * 신규
	 * 
	 * @param c
	 * @throws DuplicateIDException
	 */
	public void addCustomer(Customer c) throws DuplicateIDException {
		System.out.println("클라: addCustomer요청실행");
		try {
			// 명령정보만 전달(파라미터 ssn)
			Command cmd = new Command(Command.ADD_CUSTOMER);
			Object[] array = { c };
			cmd.setArgs(array);
			
			// 보냄
			oos.writeObject(cmd);// 객체단위로 정보 보냄

			// 처리결과반환없음

			// 예외처리결과(어차피 UI에서 해주지 않나?)
			int key = cmd.getStatus();
			if (key == Command.RECORD_NOTFOUND) {
				throw new DuplicateIDException();
			}

		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	public void deleteCustomer(String ssn) throws RecordNotFoundException {
		System.out.println("클라: deleteCustomer요청실행");
		try {
			// 명령정보만 전달(파라미터 ssn)
			Command cmd = new Command(Command.DELETE_CUSTOMER);
			Object[] array = { ssn };
			cmd.setArgs(array);
			
			// 보냄
			oos.writeObject(cmd);// 객체단위로 정보 보냄

			// 처리결과반환없음

			// 예외처리결과(어차피 UI에서 해주지 않나?)
			int key = cmd.getStatus();
			if (key == Command.RECORD_NOTFOUND) {
				throw new RecordNotFoundException();
			}

		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	public void updateCustomer(Customer c) throws RecordNotFoundException {
		System.out.println("클라: updateCustomer요청실행");
		try {
			// 명령정보만 전달(파라미터 ssn)
			Command cmd = new Command(Command.UPDATE_CUSTOMER);
			Object[] array = { c };
			cmd.setArgs(array);
			
			// 보냄
			oos.writeObject(cmd);// 객체단위로 정보 보냄

			// 처리결과반환없음

			// 예외처리결과(어차피 UI에서 해주지 않나?)
			int key = cmd.getStatus();
			if (key == Command.RECORD_NOTFOUND) {
				throw new RecordNotFoundException();
			}

		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void buyShares(Shares s) {
		System.out.println("클라: buyShares요청실행");
		try {
			// 명령정보만 전달(파라미터 ssn)
			Command cmd = new Command(Command.BUY_SHARES);
			Object[] array = { s };
			cmd.setArgs(array);
			
			// 보냄
			oos.writeObject(cmd);// 객체단위로 정보 보냄

			// 처리결과반환없음

			// 예외처리결과(어차피 UI에서 해주지 않나?)
			

		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	public void sellShares(Shares s) throws InvalidTransactionException, RecordNotFoundException {
		System.out.println("클라: sellShares요청실행");
		try {
			// 명령정보만 전달(파라미터 ssn)
			Command cmd = new Command(Command.SELL_SHARES);
			Object[] array = { s };
			cmd.setArgs(array);
			
			// 보냄
			oos.writeObject(cmd);// 객체단위로 정보 보냄

			// 처리결과반환없음

			// 예외처리결과(어차피 UI에서 해주지 않나?)
			

		} catch (IOException e) {
			e.printStackTrace();
		}

	}
	// 내부로직 메소드나 안쓰는 메소드는 일단 @Deprecated

	@Deprecated
	public boolean ssnExist(String ssn) {
		return false;
	}

	@Deprecated
	public boolean ssnExist2(String ssn, String symbol) {
		return false;
	}

	@Deprecated
	public Customer getCustomer(String ssn) throws RecordNotFoundException {
		return null;
	}

}
