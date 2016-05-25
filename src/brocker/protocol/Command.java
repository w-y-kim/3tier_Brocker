package brocker.protocol;

import java.io.Serializable;

/**
 * Brocker 프로그램에서 서버와 클라이언트 사이에 요청과 응답을 처리할 클래스 
 * 요청 시 필요한 요청 명령상수와 필요한 매개변수 값을 저장하여 서버에 전달되고
 * 서버에서는 요청을 처리 후 반환되는 결과 및 처리상태(예외)를 저장하여 클라이언트에게
 * 재전송 된다.
 * @author user
 *
 */
public class Command implements Serializable{
	
	//요청(전달) 및 응답(처리결과)에 필요한 변수들
	
	private int cmdValue;//요청을 구분할 명령상수값을 저장 일반 숫자는 모호해서 enum 등 쓰거나 상수
	private Object[] args = {""}; //요청을 처리할 때 필요한 매개변수값을 저장 
	
	private Object result;//서버에서 처리된 결과를 저장 
	private int status; 
	
	//사용자요청명령
	public static final int ADD_CUSTOMER = 10; //상수는 보통 public static 하게 씀, 숫자 자체는 의미 없음 
	public static final int DELETE_CUSTOMER = 20; //삭제
	public static final int UPDATE_CUSTOMER = 30;//수정
	public static final int GET_CUSTOMER = 40;//안씀
	public static final int GET_ALL_CUSTOMER = 50;//dlm2
	public static final int GET_ALL_STOCKS = 60;//dlm1
	public static final int BUY_SHARES = 70;//매수
	public static final int SELL_SHARES = 80;//매도
	public static final int GET_PORTFOLIO = 90;//dlm3
	
	//발생예외
	public static final int RECORD_NOTFOUND = -10;
	public static final int DUPLICATE_ID = -20;
	public static final int INVALID_TRANSCATION = -30;
	
	public Command(){}
	public Command(int cmdValue){//setcmdValue 안쓰고 바로 생성자 통해 변경 하기 위함 
		this.cmdValue = cmdValue;
		
	}
	public int getCmdValue() {
		return cmdValue;
	}
	public void setCmdValue(int cmdValue) {
		this.cmdValue = cmdValue;
	}
	public Object[] getArgs() {
		return args;
	}
	public void setArgs(Object[] args) {
		this.args = args;
	}
	public Object getResult() {
		return result;
	}
	public void setResult(Object result) {
		this.result = result;
	}
	public int getStatus() {
		return status;
	}
	public void setStatus(int status) {
		this.status = status;
	}
}
