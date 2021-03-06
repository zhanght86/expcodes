#set( $symbol_pound = '#' )
#set( $symbol_dollar = '$' )
#set( $symbol_escape = '\' )
package ${package}._demo.services.sock.io;

import exp.libs.warp.net.sock.bean.SocketBean;
import exp.libs.warp.net.sock.io.client.SocketClient;

/**
 * <PRE>
 * IOSocket客户端 样例
 * </PRE>
 * <B>PROJECT：</B> exp-libs
 * <B>SUPPORT：</B> EXP
 * @version   1.0 2017-08-17
 * @author    EXP: 272629724@qq.com
 * @since     jdk版本：jdk1.6
 */
public class Demo_SocketClient {

	public static void main(String[] args) {
//		SocketBean sb = Config.getInstn().getSocket();
		SocketBean sb = new SocketBean();
		sb.setIp("127.0.0.1");
		sb.setPort(9998);
		
		SocketClient client = new SocketClient(sb);
		client.conn();
		System.out.println(client.read());
		client.close();
	}
	
}
