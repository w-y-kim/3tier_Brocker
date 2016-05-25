package brocker.exception;

/**사용자정의예외
 * 동일한 아이디가 존재할 경우 발생시킬 예외처리클래스 
 * @author user
 *
 */
public class DuplicateIDException extends Exception {
	
	public DuplicateIDException(){
		super("동일한 ID의 레코드가 존재합니다.");//부모클래스의 생성자 호출 
	}

	public DuplicateIDException(String msg){
		super(msg); 
	}


}
