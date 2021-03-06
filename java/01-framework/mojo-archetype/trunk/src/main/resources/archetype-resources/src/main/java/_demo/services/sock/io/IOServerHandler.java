#set( $symbol_pound = '#' )
#set( $symbol_dollar = '$' )
#set( $symbol_escape = '\' )
package ${package}._demo.services.sock.io;

import exp.libs.warp.net.sock.io.common.ISession;
import exp.libs.warp.net.sock.io.server.IHandler;

/**
 * <PRE>
 * IOSocket服务端业务处理器 样例
 * </PRE>
 * <B>PROJECT：</B> exp-libs
 * <B>SUPPORT：</B> EXP
 * @version   1.0 2017-08-17
 * @author    EXP: 272629724@qq.com
 * @since     jdk版本：jdk1.6
 */
class IOServerHandler implements IHandler {

	@Override
	public void _handle(ISession session) {
		// TODO Auto-generated method stub
		session.write("conn success");
	}

	@Override
	public IHandler _clone() {
		return new IOServerHandler();
	}

}
