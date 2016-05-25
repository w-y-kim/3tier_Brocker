package brocker.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**Connection을 관리하는 클래스 
 * 드라이버 로딩하고 닫는데 사용하는 유틸리티 클래스 
 * 빈번하게 쓰이는 Connection에 대한 소스를 모아놓고 필요한 때만 메소드를 통해 호출해 
 * 사용하도록 한다. 
 * 
 * 객체의 새로운 생성을 막는대신 특정 클래스의 객체가 필요할 때 
 * 매번 new로 쓰면 새로 객체를 만드는 거니까 여기서는 그렇게 하면 안된다. 
 * 때문에 싱글턴이라는 클래스 디자인 패턴을 사용해야한다.
 * 클래스의 객체가 오직 하나만 생성되도록 하는 것 
 * 
 * 해당 클래스의 객체 생성을 막아야 한다.
 * 생성자의 접근지정자를 private하게 만들면 외부에서 이 클래스의 인스턴스 생성을 막는다.
 * @author user
 * @since 160519 Thr
 *
 */
public class ConnectionManager {

	private static String driver = "oracle.jdbc.driver.OracleDriver";
	private static  String url = "jdbc:oracle:thin:@localhost:1521:XE";// DB이름이 XE인 경우
	private static  String user = "hr";
	private static  String password = "hr";
	
	
	/**외부에서 객체  생성 막는다..
	 * 대신 클래스가 읽어질 때 객체가 생성되도록 하여야 함 
	 * 때문에 static변수로 객체를 만들자 
	 */
	
	private static ConnectionManager cm = new ConnectionManager();  
	
	private  ConnectionManager() {//생성자 다른 곳에서 못만들도록 
		
	}
	
	/**어디서든 객체를 가져다 쓸 수 있도록 static을 붙인다.
	 * 혹시 객체를 가져다 쓸 일이 있을 수도 있을까봐 만들어 둔 메소드일 뿐 
	 * @return
	 */
	public static ConnectionManager getInstance(){
		return cm; 
	}

	
	static{
		// static 블록 : 클래스 로딩 타임 시 단 한번 생성된다 
		try {
			Class.forName(driver);
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} 
	} 

	public static Connection getConnection(){
		Connection con = null;
		try {
			con = DriverManager.getConnection(url,user,password);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return con; 
	}

	public static void close(Connection con){
		try {
			if(con != null) con.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
}
