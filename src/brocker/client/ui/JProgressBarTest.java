package brocker.client.ui;



import java.awt.Color;
import java.awt.GridLayout;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.text.BadLocationException;
 
class MyFrame implements KeyListener
{
    private JFrame frm = new JFrame();
    private JPanel progressPanel = new JPanel();
    /*textProgressBar는 화면의 글자수를 표시하기 위한 진행바*/
    private JProgressBar textProgressBar = new JProgressBar();
    /*ExampleProgressBar는 프로그래스바의 또 다른 기능을 보여주기 위한 진행바*/
    private JProgressBar ExampleProgressBar = new JProgressBar();
    private JTextArea textArea = new JTextArea();
    
    public MyFrame()
    {
        //프로그래스바 설정
        textProgressBar.setMinimum(0); //진행바 최소값 설정
        textProgressBar.setMaximum(100); //진행바 최대값 설정
        textProgressBar.setStringPainted(true); //진행사항 퍼센티지로 보여주기 설정
        textProgressBar.setForeground(Color.DARK_GRAY); //진행바 색설정
        
        ExampleProgressBar.setForeground(Color.GREEN);
        ExampleProgressBar.setBorderPainted(false); //경계선 표시 설정
        ExampleProgressBar.setIndeterminate(true); //진행이 안될때 모습 표시
        
        //텍스트에어리어 설정
        textArea.addKeyListener(this);
        
        textArea.setLineWrap(true);
        
        //프로그래스바 패널에 프로그래스바 장착
        progressPanel.setLayout(new GridLayout(2,0));
        progressPanel.add(textProgressBar);
        progressPanel.add(ExampleProgressBar);
        
        //프레임에 패널 및 컴포넌트 장착
        frm.add(new JScrollPane(textArea, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, 
                                                                    JScrollPane.HORIZONTAL_SCROLLBAR_NEVER));     
        frm.add(progressPanel, "South");
        
        //프래임 기본설정
        frm.setTitle("프로그래스바 예제");
        frm.setSize(300, 300);
        frm.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frm.setVisible(true);
    }
 
    public void keyPressed(KeyEvent e){}
    public void keyReleased(KeyEvent e){}
    
    @Override
    public void keyTyped(KeyEvent e) 
    {
        int strLength = textArea.getText().length();
        textProgressBar.setValue(strLength);
            
        if(textProgressBar.getValue() == 100)
        {
            JOptionPane.showMessageDialog(frm, "100글자를 초과하였습니다.");
            try
            {
                //텍스트에어리어에 100이상 문자가 추가가 되지 않도록 한다
                textArea.setText(textArea.getText(0,100));
            } 
            catch (BadLocationException e1) 
            {
                e1.printStackTrace();
            }
        }
    }
}
 
public class JProgressBarTest
{
    public static void main(String[] args) 
    {
        new MyFrame();
    }
}