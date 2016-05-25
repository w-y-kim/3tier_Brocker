package brocker.exception;

public class InvalidTransactionException extends Exception {
	
	public InvalidTransactionException(){
		super("유효한 거래가 이루어졌습니다.");//부모클래스의 생성자 호출 
	}

	public InvalidTransactionException(String msg){
		super(msg); 
	}


}
