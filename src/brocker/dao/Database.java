package brocker.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import brocker.exception.*;
import brocker.protocol.Command;
import brocker.vo.*;

public class Database {

	public int error;

	/**
	 * [find] Customer 테이블에 SSN의 존재 유무를 확인
	 * 
	 * @return 매개변수로 주어진 ssn이 존재하면 true, else false
	 * @param ssn존재유무확인하고자
	 *            하는 고객의 ssn
	 */
	public boolean ssnExist(String ssn){
		boolean result = false;

		// Statement 방식을 쓰면 setString 못쓰고, sql도 executeQuery에 넣어줘야함
		// String sql = "select * from Customer where ssn =" + "'" + ssn + "'";
		// Statement stat = con.createStatement();
		// ResultSet rs = stat.executeQuery(sql);

		Connection con = ConnectionManager.getConnection();
		String sql = "select * from Customer where ssn = ?";

		try {
			PreparedStatement pstat = con.prepareStatement(sql);
			pstat.setString(1, ssn);
			ResultSet rs = pstat.executeQuery();

			// System.out.println(rs.next());//한번 쓸때마다 줄이 바뀌기 때문에 주의할것
			if (rs.next()) {// TODO rs.next()를 쓰면 안됨, 어떻게하지?해결 일단 DB문제였음
				System.out.println("레코드가 true");
				result = true;
			} else {
				System.out.println("레코드 false");
			}

		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			ConnectionManager.close(con);
		}
		return result;
	}

	public boolean ssnExist2(String ssn, String symbol) {
		boolean result = false;

		// Statement 방식을 쓰면 setString 못쓰고, sql도 executeQuery에 넣어줘야함
		// String sql = "select * from Customer where ssn =" + "'" + ssn + "'";
		// Statement stat = con.createStatement();
		// ResultSet rs = stat.executeQuery(sql);

		Connection con = ConnectionManager.getConnection();
		String sql = "select * from Shares where ssn = ? AND symbol = ?";

		try {
			PreparedStatement pstat = con.prepareStatement(sql);
			pstat.setString(1, ssn);
			pstat.setString(2, symbol);
			ResultSet rs = pstat.executeQuery();

			// System.out.println(rs.next());//한번 쓸때마다 줄이 바뀌기 때문에 주의할것
			if (rs.next()) {// TODO rs.next()를 쓰면 안됨, 어떻게하지?해결 일단 DB문제였음
				System.out.println("레코드가 true");
				result = true;
			} else {
				System.out.println("레코드 false");
			}

		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			ConnectionManager.close(con);
		}
		return result;
	}
	public boolean ssnExist3(String ssn) throws DuplicateIDException {
		boolean result = false;

		// Statement 방식을 쓰면 setString 못쓰고, sql도 executeQuery에 넣어줘야함
		// String sql = "select * from Customer where ssn =" + "'" + ssn + "'";
		// Statement stat = con.createStatement();
		// ResultSet rs = stat.executeQuery(sql);

		Connection con = ConnectionManager.getConnection();
		String sql = "select * from Customer where ssn = ?";

		try {
			PreparedStatement pstat = con.prepareStatement(sql);
			pstat.setString(1, ssn);
			ResultSet rs = pstat.executeQuery();

			// System.out.println(rs.next());//한번 쓸때마다 줄이 바뀌기 때문에 주의할것
			if (rs.next()) {// TODO rs.next()를 쓰면 안됨, 어떻게하지?해결 일단 DB문제였음
				System.out.println("레코드가 true");
				result = true;
			} else {
				System.out.println("레코드 false");
			}

		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			ConnectionManager.close(con);
		}
		return result;
	}

	/**
	 * [show] 매개변수로 주어진 ssn에 해당하는 고객을 조회하여 반환
	 * 
	 * @param ssn조회하고자
	 *            하는 고객의 ssn
	 * @return 조회결과를 갖는 customer 객체
	 * @throws RecordNotFoundException조회하고자
	 *             하는 고객의 ssn이 존재하지 않을 경우 발생
	 */

	public Customer getCustomer(String ssn) throws RecordNotFoundException {

		if (!ssnExist(ssn))
			throw new RecordNotFoundException(); // 존재하지 않는 경우 예외처리

		Customer cus = null;
		Connection con = ConnectionManager.getConnection();
		String sql = "SELECT * FROM customer WHERE ssn = ?";

		try {
			PreparedStatement pstat = con.prepareStatement(sql);
			pstat.setString(1, ssn);
			ResultSet rs = pstat.executeQuery();
			if (rs.next()) {
				String cust_name = rs.getString("cust_name");
				String address = rs.getString("address");
				cus = new Customer(ssn, cust_name, address);

			}

		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			ConnectionManager.close(con);
		}

		return cus;
	}

	/**
	 * [showAll]Customer 테이블에 등록된 모든 고객정보를 조회한다.
	 * 
	 * @return 등록되어 있는 모든 고객정보 목록
	 * @throws RecordNotFoundException
	 */
	public ArrayList<Customer> getAllCustomer() throws RecordNotFoundException {
		ArrayList<Customer> list = new ArrayList<Customer>();

		Connection con = ConnectionManager.getConnection();
		String sql = "SELECT * FROM CUSTOMER";
		try {
			PreparedStatement pstat = con.prepareStatement(sql);
			ResultSet rs = pstat.executeQuery();
			int i = 1;
			while (rs.next()) {
				String ssn = rs.getString("ssn");
				String cust_name = rs.getString("cust_name");
				String address = rs.getString("address");
				Customer cus = new Customer(ssn, cust_name, address);
				list.add(cus);
				i++;
			}
			System.out.println(i + "명의 고객 정보가져옴");

		} catch (SQLException e) {

		} finally {
			ConnectionManager.close(con);
		}
		if (list.isEmpty()) {
			System.out.println("레코드가 비어있습니다.");
		}

		return list;
	}

	/**
	 * [insert] 매개변수로 주어진 새로운 고객정보를 등록한다.
	 * 
	 * @param c등록하고자
	 *            하는 새로운 고객정보를 가지고 있는 Customer 객체
	 * @throws DuplicateIDException등록하고자
	 *             하는 고객의 ssn이 이미 존재할 경우 발생
	 */
	public void addCustomer(Customer c) {
		Connection con = ConnectionManager.getConnection();
		String sql = "INSERT INTO CUSTOMER VALUES(?,?,?)";
		try {
			// if(ssnExist(c.getSsn()) ) {throw new DuplicateIDException();}
			ssnExist3(c.getSsn());//검사만 해 ! 그리고 예외발생하면 catch로 줘! 
			//// error = -10;//클라에도 따로 에러 전달하려면 변수가 필요
			// //예외가 발생한 순간 해당 메소드를 발생시킨 지점에서 멈추고 예외클래스로
			// //넘어가기 때문에 throws가 최종적으로 클라이언트로 가지 않는 이상 불가능한게
			// //아닌지???
			// throw new DuplicateIDException();
			// }
			// else {
			

		
				PreparedStatement pstat = con.prepareStatement(sql);
				pstat.setString(1, c.getSsn());
				pstat.setString(2, c.getCust_name());
				pstat.setString(3, c.getAddress());
				int result = pstat.executeUpdate();
				if (result == 1) {
					System.out.println("등록완료");
				} else {
					System.out.println("등록안됨");
				}

			
			// }
		} catch (DuplicateIDException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally {
			ConnectionManager.close(con);
		}
	}

	/**
	 * [delete] 매개변수로 주어진 ssn에 해당하는 고객정보 & 보유주식정보 삭제 여러개의 작업이 있으면 transcation
	 * 처리를 해주어야 함
	 * 
	 * @param ssn
	 *            삭제하고자 하는 고객의 ssn
	 * @throws RecordNotFoundException
	 *             조회하고자 하는 고객의 ssn이 존재하지 않을 경우 발생
	 */
	public void deleteCustomer(String ssn) throws RecordNotFoundException {
		if (!ssnExist(ssn))
			throw new RecordNotFoundException(); // 존재하지 않는 경우 예외처리

		Connection con = ConnectionManager.getConnection();
		String sql = "DELETE FROM CUSTOMER  WHERE SSN = ?";
		String sql2 = "DELETE FROM SHARES WHERE SSN = ?";

		try {
			con.setAutoCommit(false);

			// 외래키-참조키 관계의 테이블은 커밋 순서도 SQL에서 처럼해줘야함
			// 그것이 복잡하니까 DB생성시 외래키를 사용하는 constraint에 on delete cascade를 해줌
			// 대신 이렇게하면 int row2 = pstat2.executeUpdate();의 결과는 0이 된다.
			PreparedStatement pstat2 = con.prepareStatement(sql2);
			pstat2.setString(1, ssn);
			int row2 = pstat2.executeUpdate();
			con.commit();

			PreparedStatement pstat = con.prepareStatement(sql);
			pstat.setString(1, ssn);
			int row = pstat.executeUpdate();

			// if(true) throw new SQLException();//확인용

			if (row == 1 && row2 == 1) {
				System.out.println("삭제완료");
			} else
				System.out.println("삭제안됨");

		} catch (SQLException e) {
			e.printStackTrace();
			try {
				con.rollback();
			} catch (SQLException e1) {
				e1.printStackTrace();
			}
		} finally {
			ConnectionManager.close(con);
		}

	}

	/**
	 * [update] 매개변수로 주어진 새로운 고객정보를 갱신한다.
	 * 
	 * @param 새로운
	 *            고객정볼르 가지고 있는 Customer 객체
	 * @throws 갱신하고자
	 *             하는 고객의 ssn이 존재하지 않을 경우 발생
	 */
	public void updateCustomer(Customer c) throws RecordNotFoundException {
		if (!ssnExist(c.getSsn()))
			throw new RecordNotFoundException();
		Connection con = ConnectionManager.getConnection();
		String sql = "UPDATE CUSTOMER set cust_name=?, address=? where ssn=?";

		try {
			PreparedStatement pstat = con.prepareStatement(sql);
			pstat.setString(1, c.getCust_name());
			pstat.setString(2, c.getAddress());
			pstat.setString(3, c.getSsn());
			System.out.println(pstat);
			int result = pstat.executeUpdate();// TODO 쿼리문 틀리면 여기서 멈춰있는 문제
			if (result == 1) {
				System.out.println("업뎃완료");
			} else {
				System.out.println("업뎃안됨");// TODO 업뎃안되는걸 체크하는 방법?
			}

		} catch (SQLException e) {

		} finally {
			ConnectionManager.close(con);
		}

	}

	/**
	 * 매개변수로 주어진 ssn의 고객이 보유하고 있느 주식 목록을 조회하여 반환
	 * 
	 * @param ssn
	 *            조회하고자 하는 고객의 ssn
	 * @return 특정 고객이 보유하고 있는 주식정보 목록
	 * @throws RecordNotFoundException
	 */
	public ArrayList<Shares> getPortfolio(String ssn) throws RecordNotFoundException {
		ArrayList<Shares> list = new ArrayList<>();

		if (!ssnExist(ssn)) {
			throw new RecordNotFoundException();
		}

		Connection con = ConnectionManager.getConnection();
		// String sql = "SELECT * FROM customer c, shares s where c.? = s.?";
		String sql = "SELECT * FROM shares WHERE ssn =?";

		try {
			PreparedStatement pstat = con.prepareStatement(sql);
			pstat.setString(1, ssn);
			// pstat.setString(2, ssn);
			ResultSet rs = pstat.executeQuery();
			while (rs.next()) {

				String symbol = rs.getString("symbol");
				int quantity = rs.getInt("quantity");
				Shares s = new Shares(ssn, symbol, quantity);
				list.add(s);
			}
			// TOOD 분기처리하려면 if안의 조건에 rs.next()를 쓰게되는데 그러면 또 한줄건너뜀
			// if (result) {
			// System.out.println("고객의 주식자료 조회시작");
			//
			//
			// } else {
			// System.out.println("해당 고객은 주식자료가 없음");
			// }

		} catch (SQLException e) {
			e.printStackTrace();
		}

		return list;

	}

	public ArrayList<Stock> getAllStock() throws RecordNotFoundException {
		ArrayList<Stock> list = new ArrayList<>();

		Connection con = ConnectionManager.getConnection();
		// String sql = "SELECT * FROM stock c, shares s where c.? = s.?";
		String sql = "SELECT * FROM stock";

		try {
			PreparedStatement pstat = con.prepareStatement(sql);
			ResultSet rs = pstat.executeQuery();
			while (rs.next()) {

				String symbol = rs.getString("symbol");
				int price = rs.getInt("price");
				Stock c = new Stock(symbol, price);
				list.add(c);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}

		return list;

	}

	/**
	 * 매개변수로 전달된 주식을 매입(Shares 테이블에 레코드 입력) 매입하고자 하는 주식에 대한 보유현황이 없다면 새로운 레코드를
	 * 삽입 이미 보유하고 있다면 레코드를 업데이트
	 * 
	 * @param 매입하는
	 *            주식에 대한 정보를 담은 객체, 이것을 처리하여 DB에 SQL문 형태로 저장해줌
	 */
	// public void buyShares(Shares s) {
	// Connection con = ConnectionManager.getConnection();
	// String sql1 = "INSERT INTO SHARES VALUES(?,?,?) ";
	// String sql2 = "UPDATE SHARES SET quantity=quantity+? where ssn =? AND
	// symbol = ? ";
	// boolean search = ssnExist2(s.getSsn(), s.getSymbol());
	// PreparedStatement pstat;
	//
	// if (!search) {// 새주식이라면 , sql1
	//
	// try {
	// pstat = con.prepareStatement(sql1);
	// pstat.setString(1, s.getSsn());
	// pstat.setString(2, s.getSymbol());
	// pstat.setInt(3, s.getQuantity());
	// pstat.executeUpdate();
	// } catch (SQLException e) {
	// // TODO Auto-generated catch block
	// e.printStackTrace();
	// } finally {
	// ConnectionManager.close(con);
	// }
	//
	// } else {// 기존 보유주식이라면 , sql2
	//
	// try {
	// pstat = con.prepareStatement(sql2);
	// pstat.setInt(1, s.getQuantity());
	// pstat.setString(2, s.getSsn());
	// pstat.setString(3, s.getSymbol());
	// pstat.executeUpdate();
	// } catch (SQLException e) {
	// // TODO Auto-generated catch block
	// e.printStackTrace();
	// } finally {
	// ConnectionManager.close(con);
	// }
	//
	// }
	// }
	public void buyShares(Shares s) {
		Connection con = ConnectionManager.getConnection();
		String sql = "SELECT quantity FROM SHARES where ssn =? AND symbol = ? ";

		PreparedStatement pstat;
		try {
			pstat = con.prepareStatement(sql);
			pstat.setString(1, s.getSsn());
			pstat.setString(2, s.getSymbol());
			ResultSet rs = pstat.executeQuery();
			if (rs.next()) {// 기존 수량 존재 시
				sql = "UPDATE SHARES SET quantity = quantity+? where ssn=? AND symbol=?";
				pstat = con.prepareStatement(sql);
				pstat.setInt(1, s.getQuantity());
				pstat.setString(2, s.getSsn());
				pstat.setString(3, s.getSymbol());
				pstat.executeUpdate();
			} else {
				try {
					sql = "INSERT INTO SHARES VALUES(?,?,?)";
					pstat = con.prepareStatement(sql);
					pstat.setString(1, s.getSsn());
					pstat.setString(2, s.getSymbol());
					pstat.setInt(3, s.getQuantity());
					pstat.executeUpdate();

				} catch (Exception e) {
					// TODO: handle exception
				} finally {
					ConnectionManager.close(con);
				}
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			ConnectionManager.close(con);
		}
	}

	// public void buyShares(Shares s){
	// Connection con = ConnectionManager.getConnection();
	// String sql;
	// if(existShares(s)){// 존재 하면
	// sql ="UPDATE shares SET quantity = quantity + ? WHERE ssn = ? AND symbol
	// = ?";
	// try{
	// PreparedStatement stmt = con.prepareStatement(sql);
	// stmt.setString(1, s.getQuantity());
	// stmt.setString(2, s.getSsn());
	// stmt.setString(3, s.getSymbol());
	//
	// int row = stmt.executeUpdate();
	// System.out.println(row +"개 정보 추가");
	// }catch(SQLException e ){
	// e.printStackTrace();
	// }finally{
	// ConnectionManager.close(con);
	// }
	// }else {//존재 하지 않으면
	// sql = "INSERT into shares Values(?, ?, ? ) ";
	// try{
	// PreparedStatement stmt = con.prepareStatement(sql);
	// stmt.setString(1, s.getSsn());
	// stmt.setString(2, s.getSymbol());
	// stmt.setString(3, s.getQuantity());
	//
	// int row = stmt.executeUpdate();
	// System.out.println(row +"개 정보 삽입");
	// }catch(SQLException e ){
	// e.printStackTrace();
	// }finally{
	// ConnectionManager.close(con);
	// }
	//
	// }
	// }

	/**
	 * <pre>
	 * 매개변수로 전달된 보유주식을 매도한다. 
	 * 1- 보유하고 있는 주식보다 적은 수량을 매도하는 경우에는 보유수량을 차감 후 업데이트 
	 * 2-보유하고 있는 주식과 같은 수량(전량)을 매도하는 경우에는 포트폴리오 삭제(Shares테이블에 해당 레코드를 삭제) 
	 * 3- 보유하고 있는 주식보다 많은 수량을 매도하고자 하는 경우에는 예외를 발생
	 * 4-매도하려고 하는 주식에 대한 정보를 아예 보유정보가 없는 경우도 예외발생
	 * </pre>
	 * 
	 * @param s
	 * @throws InvalidTransactionException
	 * @throws RecordNotFoundException
	 */
	public void sellShares(Shares s) throws InvalidTransactionException, RecordNotFoundException {
		if (!ssnExist2(s.getSsn(), s.getSymbol())) {// 4.정보가 없으면
			throw new RecordNotFoundException();
		} else {// 정보 있을 때 (매도가능/전량매도(레코드삭제)/거래불가)

			Connection con = ConnectionManager.getConnection();
			String sql = "SELECT quantity FROM SHARES where ssn =? AND symbol = ? ";
			PreparedStatement pstat;

			try {// 수량가져와서 담음
				pstat = con.prepareStatement(sql);
				pstat.setString(1, s.getSsn());
				pstat.setString(2, s.getSymbol());
				ResultSet rs = pstat.executeQuery();
				if (rs.next()) {// rs.next()를 안쓰면 밑에서 rs.getString("")을 못쓴다 rs,
								// stat 은 무조건 1회성이다.

					int nowQuantity = Integer.parseInt(rs.getString("quantity"));
					if (nowQuantity > s.getQuantity()) {
						// 기존 수량 존재 시
						sql = "UPDATE SHARES SET quantity = quantity - ? where ssn = ? AND symbol = ?";
						pstat = con.prepareStatement(sql);
						pstat.setInt(1, s.getQuantity());
						pstat.setString(2, s.getSsn());
						pstat.setString(3, s.getSymbol());
						pstat.executeUpdate();
					} else if (nowQuantity == s.getQuantity()) {
						// 레코드 삭제

						try {
							sql = "DELETE FROM SHARES where quantity = ? AND symbol =? ";
							pstat = con.prepareStatement(sql);
							pstat.setInt(1, s.getQuantity());
							pstat.setString(2, s.getSymbol());
							pstat.executeUpdate();

						} catch (Exception e) {
							// TODO: handle exception
						} finally {
							ConnectionManager.close(con);
						}

					}
				} else {
					// 적으면 에러발생
					throw new InvalidTransactionException();
				}

			} catch (SQLException e) {
				e.printStackTrace();
			} finally {
				ConnectionManager.close(con);
			}
		}

	}

	// public void sellShares(Shares s)throws InvalidTransactionException{
	// Connection con = ConnectionManager.getConnection();
	// String sql ="SELECT quantity FROM shares WHERE ssn = ? AND symbol = ? ";
	// try{
	// PreparedStatement stmt = con.prepareStatement(sql);
	// stmt.setString(1, s.getSsn());
	// stmt.setString(2, s.getSymbol());
	// ResultSet rs = stmt.executeQuery();
	// if(rs.next()){
	// String quan = rs.getString("quantity");
	// int ownQuan = Integer.parseInt(quan);
	// int getQuan = Integer.parseInt(s.getQuantity());
	// if(ownQuan > getQuan){
	// sql = "UPDATE shares SET quantity = quantity - ? WHERE ssn = ? AND symbol
	// = ?";
	// stmt = con.prepareStatement(sql);
	// stmt.setString(1, s.getQuantity());
	// stmt.setString(2, s.getSsn());
	// stmt.setString(3, s.getSymbol());
	// int row = stmt.executeUpdate();
	// System.out.println(row + "개 정보가 update 되었습니다.");
	// }else if(ownQuan == getQuan){
	// sql = "DELETE FROM SHARES WHERE ssn = ? AND symbol = ?";
	// stmt = con.prepareStatement(sql);
	// stmt.setString(1, s.getSsn());
	// stmt.setString(2, s.getSymbol());
	// int row = stmt.executeUpdate();
	// System.out.println(row + "개 정보를 shares 테이블에서 삭제");
	// }else if(ownQuan < getQuan){
	// throw new InvalidTransactionException();
	// }
	// }else{
	// throw new InvalidTransactionException();
	// }
	// }catch(SQLException e){
	// e.printStackTrace();
	// }finally{
	// ConnectionManager.close(con);
	// }
	// }

	// public static void main(String[] args) {
	// Database db = new Database();
	//
	// // 찾기(중복검사)
	//
	// // boolean result = db.ssnExist("111-112");
	// // System.out.println(result);
	//
	// System.out.println("=====================================");
	//
	// // 컬럼하나출력
	//
	// // try {
	// // Customer c = db.getCustomer("111-112");
	// // String str = c.toString();
	// // System.out.println(str);
	// // // System.out.println(c);//toString생략
	// // } catch (RecordNotFoundException e) {
	// // e.printStackTrace();
	// // }
	//
	// System.out.println("=====================================");
	//
	// // 전체출력
	//
	// // try {
	// // ArrayList<Customer> result = db.getAllCustomer();
	// //
	// // for (int i = 0; i < result.size(); i++) {
	// // Customer c = result.get(i);
	// // System.out.println(c);
	// // String str = c.toString();
	// // System.out.println(str);
	// // }
	// // } catch (RecordNotFoundException e) {
	// // e.printStackTrace();
	// // }
	//
	// System.out.println("=====================================");
	//
	// // 추가
	//
	// // Customer c_insert = new Customer("111-111", "김사람", "강남");
	// // try {
	// // db.addCustomer(c_insert);
	// // } catch (DuplicateIDException e) {
	// // e.printStackTrace();
	// // }
	//
	// System.out.println("=====================================");
	//
	// // 삭제
	//
	// // try {
	// // db.deleteCustomer("111-119");
	// // } catch (RecordNotFoundException e) {
	// // e.printStackTrace();
	// // }
	//
	// System.out.println("=====================================");
	//
	// // 업데이트
	//
	// // Customer c_update = new Customer("111-111", "김사람", "강남");
	// // try {
	// // db.updateCustomer(c_update);
	// // } catch (RecordNotFoundException e) {
	// // e.printStackTrace();
	// // }
	//
	// System.out.println("=====================================");
	//
	// // get_shares테이블
	//
	// // ArrayList<Shares> result;
	// // try {
	// // result = db.getPortfolio("111-112");
	// // for (int i = 0; i < result.size(); i++) {
	// // Shares s = result.get(i);
	// // System.out.println(s);
	// // }
	// // for (Shares s : result) {
	// // System.out.println(s.toString());
	// // }
	// // } catch (RecordNotFoundException e) {
	// // // TODO Auto-generated catch block
	// // e.printStackTrace();
	// // }
	//
	// // get_stocks주식정보 테이블
	// Shares s1 = new Shares("111-111", "SUNW", 3000);// 기존주식
	// Shares s2 = new Shares("111-111", "JDK", 1000);// 신규주식
	//
	// // db.buyShares(s1); //추가매입
	// // db.buyShares(s2); //신규매입
	//
	// try {
	// db.sellShares(s1);
	// } catch (InvalidTransactionException | RecordNotFoundException e) {
	// // TODO Auto-generated catch block
	// e.printStackTrace();
	// }//
	// }
}
