package brocker.exception;

/**사용자정의 예외 클래스
 * @author user
 *
 */
public class RecordNotFoundException extends Exception {

	//사용자정의 예외는 보통 2가지의  생성자를 정의
	public RecordNotFoundException(){
		//예외회피시 throws , 예외발생 시키는 키워드는 throw
		super("레코드를 찾을 수 없습니다.");//부모클래스의 생성자 호출 
	}

	public RecordNotFoundException(String msg){
		super(msg); 
	}


}
