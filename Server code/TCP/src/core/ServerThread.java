package core;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.text.SimpleDateFormat;

import com.utils.JdbcUtils;

/**
 * 
 * @author ������
 * @version 1.0
 * @since  9.0.4
 * 
 * 
 */
/*
* �������̴߳�����
*/
public class ServerThread extends Thread {
   // �ͱ��߳���ص�Socket
   Socket socket = null;

   public ServerThread(Socket socket) {
      this.socket = socket;
  
   }

	Map <String,Socket> map=new HashMap<>();
	
   //�߳�ִ�еĲ�������Ӧ�ͻ��˵�����
   public void run(){
      InputStream is=null;
      InputStreamReader isr=null;
      BufferedReader br=null;
      OutputStream os=null;
      PrintWriter pw=null;
      try {
         //��ȡ������������ȡ�ͻ�����Ϣ
         is = socket.getInputStream();
         isr = new InputStreamReader(is);
         br = new BufferedReader(isr);
         String info=null;

         //��ȡ���������Ӧ�ͻ��˵�����
         os = socket.getOutputStream();
         pw = new PrintWriter(os);

         while((info=br.readLine())!=null){//ѭ����ȡ�ͻ��˵���Ϣ
            System.out.println("�ͻ��ˣ�"+info);          
            String[]  strs=info.split("&");
            System.out.println(strs[0].toString());
            System.out.println(strs[1].toString());
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd  HH:mm:ss");
            Connection conn = null;
			PreparedStatement st = null;
			 System.out.println(sdf.format(new Date()));
					try {
						String sql = "insert into rpass (rpass.user_name, rpass.ID,rpass.datetime ) values (?,?,?)";
						conn = JdbcUtils.getConnection();
						conn.setAutoCommit(true);
						st = conn.prepareStatement(sql);
						st.setString(1,strs[0].toString() );
						st.setString(2,strs[1].toString() );
						st.setString(3,sdf.format(new Date()));
						st.executeUpdate();
					} catch (SQLException e) {
						try {
							if (conn != null) {
								conn.rollback();
							}
						} catch (SQLException e1) {
							// throw new RuntimeException(e);
						}

					} finally {
						JdbcUtils.close(conn, st, null);
					}
            //Scanner sc=new Scanner(System.in);
            System.out.println("�������������յ�\r");
            //String s1=sc.nextLine();
            //pw.write(s1);
            pw.flush();//����flush()�������������
         }
         socket.shutdownInput();//�ر�������
      } catch (IOException e) {
         // TODO Auto-generated catch block
         e.printStackTrace();
      }finally{
         //�ر���Դ
         try {
            if(pw!=null)
               pw.close();
            if(os!=null)
               os.close();
            if(br!=null)
               br.close();
            if(isr!=null)
               isr.close();
            if(is!=null)
               is.close();
            if(socket!=null)
               socket.close();
         } catch (IOException e) {
            e.printStackTrace();
         }
      }
   }
}