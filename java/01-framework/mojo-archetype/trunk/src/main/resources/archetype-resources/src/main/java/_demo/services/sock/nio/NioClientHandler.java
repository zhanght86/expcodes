#set( $symbol_pound = '#' )
#set( $symbol_dollar = '$' )
#set( $symbol_escape = '\' )
package ${package}._demo.services.sock.nio;

import exp.libs.warp.net.sock.nio.common.interfaze.IHandler;
import exp.libs.warp.net.sock.nio.common.interfaze.ISession;

/**
 * <PRE>
 * NioSocket客户端端业务处理器 样例
 * </PRE>
 * <B>PROJECT：</B> exp-libs
 * <B>SUPPORT：</B> EXP
 * @version   1.0 2017-08-17
 * @author    EXP: 272629724@qq.com
 * @since     jdk版本：jdk1.6
 */
class NioClientHandler implements IHandler {

	@Override
	public void onSessionCreated(ISession session) throws Exception {
		// Undo
		System.out.println(111);
	}

	@Override
	public void onMessageReceived(ISession session, Object msg)
			throws Exception {
		System.out.println("C-GET:" + msg);
	}

	@Override
	public void onExceptionCaught(ISession session, Throwable exception) {
		exception.printStackTrace();
	}

}
