package brocker.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import brocker.exception.DuplicateIDException;
import brocker.exception.InvalidTransactionException;
import brocker.exception.RecordNotFoundException;
import brocker.vo.Customer;
import brocker.vo.Shares;
import brocker.vo.Stock;

public class DatabaseFM {
	
	/**
	 * customer 테이블에서 SSN의 존재 유무를 확인
	 * @return 매개변수로 주어진 ssn이 존재하면 true를 그렇지 않으면 false를 반환
	 * @param ssn 존재 유무를 확인하고자 하는 고객의 ssn 
	 * */
	public boolean ssnExists(String ssn){
		Connection con = ConnectionManager.getConnection();
		String sql = "select ssn from customer where ssn=?";
		boolean result = false;
		try {
			PreparedStatement pstmt = con.prepareStatement(sql);
			pstmt.setString(1, ssn);
			ResultSet rs = pstmt.executeQuery();
			result = rs.next();
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			ConnectionManager.close(con);
		}
		return result;
	}
	
	/**
	 * 매개변수로 주어진 ssn에 해당하는 고객을 조회하여 반환
	 * @param ssn 조회하고자 하는 고객의 ssn
	 * @return 조회 결과를 갖는 Customer 객체
	 * @throws RecordNotFoundException 조회하고자 하는 고객의 ssn이 존재하지 않을 경우 발생 
	 * */
	public Customer getCustomer(String ssn) throws RecordNotFoundException {
		if(!ssnExists(ssn)) throw new RecordNotFoundException();
		Customer c = null;
		
		Connection con = ConnectionManager.getConnection();
		String sql = "select * from customer where ssn=?";
		
		try {
			PreparedStatement pstmt = con.prepareStatement(sql);
			pstmt.setString(1, ssn);
			ResultSet rs = pstmt.executeQuery();
			if(rs.next()){
				String cust_name = rs.getString("cust_name");
				String address = rs.getString("address");
				c = new Customer(ssn, cust_name, address);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			ConnectionManager.close(con);
		}
		
		
		return c;
	}
	
	/**
	 * 매개변수로 주어진 새로운 고객 정보를 등록한다.
	 * @param c 등록하고자 하는 새로운 고객 정보를 가지고 있는 Customer 객체
	 * @throws DuplicateIDException 등록하고자 하는 고객의 ssn이 이미 존재할 경우 발생
	 * */
	public void addCustomer(Customer c) throws DuplicateIDException {
		if(ssnExists(c.getSsn())) throw new DuplicateIDException();
		
		Connection con = ConnectionManager.getConnection();
		String sql = "insert into customer values (?,?,?)";
		try {
			PreparedStatement pstmt = con.prepareStatement(sql);
			pstmt.setString(1, c.getSsn());
			pstmt.setString(2, c.getCust_name());
			pstmt.setString(3, c.getAddress());
			pstmt.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			ConnectionManager.close(con);
		}
	}
	
	/**
	 * 매개변수로 주어진 ssn에 해당하는 고객 정보 및 보유주식 정보를 삭제
	 * @param ssn 삭제하고자 한는 고객의 ssn
	 * @throws RecordNotFoundException 삭제하고자 하는 고객의 ssn이 존재하지 않을경우 발생
	 * */
	public void deleteCustomer(String ssn) throws RecordNotFoundException {
		if(!ssnExists(ssn)) throw new RecordNotFoundException();
		
		Connection con = ConnectionManager.getConnection();
		String sql1 = "delete from shares where ssn=?";
		String sql2 = "delete from customer where ssn=?";
		try {
			con.setAutoCommit(false);
			PreparedStatement pstmt = con.prepareStatement(sql1);
			pstmt.setString(1, ssn);
			pstmt.executeUpdate();
			pstmt = con.prepareStatement(sql2);
			pstmt.setString(1, ssn);
			pstmt.executeUpdate();
			con.commit();
		} catch (SQLException e) {
			e.printStackTrace();
			try {
				con.rollback();
			} catch (SQLException e1) {
				e1.printStackTrace();
			}
		} finally {
			try {
				con.setAutoCommit(true);
			} catch (SQLException e) {
				e.printStackTrace();
			}
			ConnectionManager.close(con);
		}
	}
	
	/**
	 * 매개변수로 주어진 새로운 고객정보를 갱신한다.
	 * @param c 새로운 고객정보를 가지고 있는 Customer 객체
	 * @throws RecordNotFoundException 갱신하고자 하는 고객의 ssn이 존재하지 않을경우 발생
	 * */
	public void updateCustomer(Customer c) throws RecordNotFoundException {
		if(!ssnExists(c.getSsn())) throw new RecordNotFoundException();
		
		Connection con = ConnectionManager.getConnection();
		String sql = "update customer set cust_name=?, address=? where ssn=?";
		try {
			PreparedStatement pstmt = con.prepareStatement(sql);
			pstmt.setString(1, c.getCust_name());
			pstmt.setString(2, c.getAddress());
			pstmt.setString(3, c.getSsn());
			pstmt.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			ConnectionManager.close(con);
		}
	}
	
	/**
	 * 매개변수로 주어진 ssn의 고객이 보유하고 있는 주식 목록을 조회하여 반환
	 * @param ssn 조회하고자 하는 고객의 ssn
	 * @return 특정 고객이 보유하고 있는 주식정보 목록
	 * @throws RecordNotFoundException 조회하고자 하는 ssn이 존재하지 않을경우 발생
	 * */
	public ArrayList<Shares> getPortfolio(String ssn) throws RecordNotFoundException {
		if(!ssnExists(ssn)) throw new RecordNotFoundException();
		ArrayList<Shares> list = new ArrayList<>();
		String sql = "select * from shares where ssn=?";
		Connection con = ConnectionManager.getConnection();
		
		try {
			PreparedStatement pstmt = con.prepareStatement(sql);
			pstmt.setString(1, ssn);
			ResultSet rs = pstmt.executeQuery();
			while(rs.next()){
				String symbol = rs.getString("symbol");
				int quantity = rs.getInt("quantity");
				Shares s = new Shares(ssn, symbol, quantity);
				list.add(s);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			ConnectionManager.close(con);
		}
		
		return list;
	}
	
	/**
	 * Customer 테이블에 등록된 모든 고객정보를 조회한다.
	 * @return 등록되어 있는 모든 고객정보 목록
	 * */
	public ArrayList<Customer> getAllCustomer() {
		ArrayList<Customer> list = new ArrayList<>();
		
		return list;
	}
	
	/**
	 * Stock 테이블에 등록된 모든 주식정보를 조회한다.
	 * @return 등록되어 있는 모든 주식정보 목록
	 * */
	public ArrayList<Stock> getAllStock(){
		ArrayList<Stock> list = new ArrayList<>();
		
		return list;
	}
	
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
				sql = "UPDATE SHARES SET quantitiy = quantity + ? where ssn=? AND symbol=?";
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
	
	   public void sellShares(Shares s)throws InvalidTransactionException{
    Connection con = ConnectionManager.getConnection();
    String sql ="SELECT quantity FROM shares WHERE ssn = ? AND symbol = ? ";
    try{
    PreparedStatement stmt = con.prepareStatement(sql);
    stmt.setString(1, s.getSsn());
    stmt.setString(2, s.getSymbol());
    ResultSet rs = stmt.executeQuery();
    if(rs.next()){
    String quan =   rs.getString("quantity");
    int ownQuan = Integer.parseInt(quan);
    int getQuan = s.getQuantity();
    if(ownQuan > getQuan){
    sql = "UPDATE shares SET quantity = quantity - ? WHERE ssn = ? AND symbol = ?";
    stmt = con.prepareStatement(sql);
    stmt.setString(1, Integer.toString(s.getQuantity()));
    stmt.setString(2, s.getSsn());
    stmt.setString(3, s.getSymbol());
    int row = stmt.executeUpdate();
    System.out.println(row + "개 정보가 update 되었습니다.");
    }else if(ownQuan == getQuan){
    sql = "DELETE FROM SHARES WHERE ssn = ? AND symbol = ?";   
    stmt = con.prepareStatement(sql);
    stmt.setString(1, s.getSsn());
    stmt.setString(2, s.getSymbol());
    int row  = stmt.executeUpdate();
    System.out.println(row + "개 정보를 shares 테이블에서 삭제");
    }else if(ownQuan < getQuan){
       throw new InvalidTransactionException();
    }
    }else{
       throw new InvalidTransactionException();
    }
    }catch(SQLException e){
       e.printStackTrace();
    }finally{
       ConnectionManager.close(con);
    }
 } 
	
	
	
	public static void main(String args[]){
		Database db = new Database();
		try {
			db.deleteCustomer("111-111");
		} catch (RecordNotFoundException e) {
			e.printStackTrace();
		}
	}
}









