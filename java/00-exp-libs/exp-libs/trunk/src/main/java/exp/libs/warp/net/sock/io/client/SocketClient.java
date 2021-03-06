package exp.libs.warp.net.sock.io.client;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.Socket;
import java.net.SocketTimeoutException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import exp.libs.utils.os.ThreadUtils;
import exp.libs.utils.other.StrUtils;
import exp.libs.warp.net.sock.bean.SocketBean;
import exp.libs.warp.net.sock.bean.SocketByteBuffer;
import exp.libs.warp.net.sock.io.common.ISession;

/**
 * <pre>
 * Socket客户端(阻塞模式)
 * 
 * 使用示例:
 * 	SocketBean sockConf = new SocketBean(SERVER_IP, SERVER_PORT);
 * 	SocketClient client = new SocketClient(sockConf);
 * 	if(client.conn()) {
 * 		String msg = client.read();	// IO模式下，读取数据会阻塞等待
 * 		client.write(msg);
 * 	}
 * 	client.close();
 * </pre>	
 * <B>PROJECT：</B> exp-libs
 * <B>SUPPORT：</B> EXP
 * @version   1.0 2015-12-27
 * @author    EXP: 272629724@qq.com
 * @since     jdk版本：jdk1.6
 */
public class SocketClient implements ISession {

	/** 日志器 */
	private Logger log = LoggerFactory.getLogger(SocketClient.class);
	
	/** Socket重连间隔(ms) */
	private final static long RECONN_INTERVAL = 10000;
	
	/** Socket连续重连次数上限 */
	private final static int RECONN_LIMIT = 30;
	
	/** Socket配置信息 */
	private SocketBean sockConf;
	
	/** Socket客户端 */
	private Socket socket;
	
	/** Socket本地读缓存 */
	private SocketByteBuffer localBuffer;
	
	/**
	 * 构造函数
	 * @param ip 服务IP
	 * @param port 服务端口
	 */
	public SocketClient(String ip, int port) {
		this.sockConf = new SocketBean(ip, port);
	}
	
	/**
	 * 构造函数
	 * @param sockConf socket配置信息
	 */
	public SocketClient(SocketBean sockConf) {
		this.sockConf = (sockConf == null ? new SocketBean() : sockConf);
	}
	
	/**
	 * 获取客户端标识
	 * @return 客户端标识
	 */
	@Override
	public String ID() {
		return sockConf.getId();
	}
	
	/**
	 * 获取socket配置
	 * @return socket配置
	 */
	@Override
	public SocketBean getSocketBean() {
		return sockConf;
	}
	
	/**
	 * 获取socket会话对象
	 * @return socket会话对象
	 */
	@Override
	public Socket getSocket() {
		return socket;
	}
	
	/**
	 * 连接socket服务
	 * @return true:连接成功; false:连接失败
	 */
	@Override
	public boolean conn() {
		if(isClosed() == false) {
			return true;
		}
		
		// 创建会话
		boolean isOk = true;
		try {
			socket = new Socket(sockConf.getIp(), sockConf.getPort());
			socket.setSoTimeout(sockConf.getOvertime());
			socket.setReceiveBufferSize(sockConf.getReadBufferSize());
			localBuffer = new SocketByteBuffer(	//本地缓存要比Socket缓存稍大
					sockConf.getReadBufferSize() * 2, sockConf.getReadCharset());
			log.info("客户端 [{}] 连接到Socket服务 [{}] 成功", 
					sockConf.getAlias(), sockConf.getSocket());
			
		} catch (Exception e) {
			isOk = false;
			log.error("客户端 [{}] 连接到Socket服务 [{}] 失败", 
					sockConf.getAlias(), sockConf.getSocket(), e);
		}
		return isOk;
	}
	
	/**
	 * 重连 socket服务
	 * @return true:连接成功; false:连接失败
	 */
	public boolean reconn() {
		int cnt = 0;
		do {
			if(conn() == true) {
				break;
				
			} else {
				close();
				log.warn("客户端 [{}] {}ms后重连(已重试 {}/{} 次)", 
						sockConf.getAlias(), RECONN_INTERVAL, cnt, RECONN_LIMIT);
			}
			
			cnt++;
			ThreadUtils.tSleep(RECONN_INTERVAL);
		} while(RECONN_LIMIT < 0 || cnt < RECONN_LIMIT);
		return !isClosed();
	}
	
	/**
	 * 检查socket连接是否已断开
	 * @return true:已断开; false:未断开
	 */
	@Override
	public boolean isClosed() {
		boolean isClosed = true;
		if(socket != null) {
			isClosed = socket.isClosed();
		}
		return isClosed;
	}
	
	/**
	 * 断开socket连接并释放所有资源
	 * @return true:断开成功; false:断开异常
	 */
	@Override
	public boolean close() {
		boolean isClose = true;
		if(socket != null) {
			try {
				socket.close();
			} catch (Exception e) {
				isClose = false;
				log.error("客户端 [{}] 断开Socket连接异常", sockConf.getAlias(), e);
			}
		}
		
		if(localBuffer != null) {
			localBuffer.clear();
		}
		return isClose;
	}
	
	/**
	 * Socket读操作
	 * @return 服务端返回的消息(若返回null，则出现超时等异常)
	 */
	@Override
	public String read() {
		String msg = null;
		if(isClosed()) {
			log.error("Socket [{}] 连接已断开, 无法读取返回消息.", sockConf.getId());
			return msg;
		}
		
		try {
			msg = read(socket.getInputStream(), localBuffer, 
					sockConf.getReadDelimiter(), sockConf.getOvertime());
			
		} catch (ArrayIndexOutOfBoundsException e) {
			log.error("Socket [{}] 本地缓冲区溢出(单条报文过长), 当前缓冲区大小: {}KB.", 
					sockConf.getId(), (sockConf.getReadBufferSize() * 2), e);
						
		} catch (UnsupportedEncodingException e) {
			log.error("Socket [{}] 编码非法, 当前编码: {}.", 
					sockConf.getId(), sockConf.getReadCharset(), e);
					
		} catch (SocketTimeoutException e) {
			log.error("Socket [{}] 读操作超时, 自动断开会话. 当前超时上限: {}ms.", 
					sockConf.getId(), sockConf.getOvertime(), e);
			close();
			
		} catch (Exception e) {
			log.error("Socket [{}] 读操作异常, 自动断开会话.", sockConf.getId(), e);
			close();
		}
		return msg;
	}
	
	/**
	 * <pre>
	 * Socket读操作.
	 * 
	 * 此方法会阻塞调用，直到从input读到的字节流中包含delimiter才会返回，
	 * 返回的String编码就是在sockBuff初始化时定义的编码，
	 * 返回后String后，sockBuff会保留剩下的字节。
	 * 
	 * 必须保证一次读取的消息不能大于sockBuff的size，
	 * 否则会抛出溢出异常ArrayIndexOutOfBoundsException，
	 * 并且sockBuff被重置，所有已读取的字节丢失。
	 * </pre>
	 * @param input 消息流入管道
	 * @param localBuff 本地缓冲区
	 * @param delimiter 结束符
	 * @param timeout 读操作超时(ms)
	 * @return 以delimiter结束的单条消息(绝不返回null)
	 * @throws IOException 读操作异常
	 */
	private String read(InputStream input, SocketByteBuffer localBuff, 
			final String delimiter, final long timeout) throws IOException {
		long bgnTime = System.currentTimeMillis();
		int endIndex = localBuff.indexOf(delimiter);	
		int readLen = 0;
		
		if(endIndex != -1) {
			// None: 本地缓冲区 localBuff 中仍有完整的数据未取出
			
		} else {
			while(true) {
				byte[] buffer = new byte[10240];	//每次最多取出10K的数据
				readLen = input.read(buffer);
				localBuff.append(buffer, readLen);
				endIndex = localBuff.indexOf(delimiter);
				if(endIndex != -1) {	// 当存在结束符时，退出循环
					break;
				}
				
				if(timeout > 0) {
					if(System.currentTimeMillis() - bgnTime > timeout) {
						throw new SocketTimeoutException("Socket服务端超时未返回消息终止符.");
						
					} else {
						ThreadUtils.tSleep(1);
					}
				}
			}
		}
		
		String msg = localBuff.subString(endIndex);
		if(msg != null) {
			msg = msg.trim();
			localBuff.delete(endIndex);
		} else {
			msg = "";
		}
		return msg;
	}
	
	/**
	 * Socket写操作.
	 * @param msg 需发送到服务端的的消息报文
	 */
	@Override
	public boolean write(final String msg) {
		boolean isOk = reconn();
		
		try {
			write(socket.getOutputStream(), 
					StrUtils.concat(msg, sockConf.getWriteDelimiter()), 
					sockConf.getWriteCharset());
			
		} catch (UnsupportedEncodingException e) {
			isOk = false;
			log.error("Socket [{}] 编码非法, 当前编码: {}.", 
					sockConf.getId(), sockConf.getWriteCharset(), e);
					
		} catch (Exception e) {
			isOk = false;
			log.error("Socket [{}] 写操作异常.", sockConf.getId(), e);
			close();
		}
		return isOk;
	}
	
	/**
	 * Socket写操作.
	 * @param output 消息流出管道
	 * @param msg 需要发送的消息
	 * @param charset 消息编码
	 * @throws IOException 写操作异常
	 */
	private void write(OutputStream output, final String msg, final String charset) 
			throws IOException {
		output.write(msg.getBytes(charset));
		output.flush();
	}
	
	/**
	 * 临时清理本地缓存.
	 * 建议完成一次完整的读写交互后执行.
	 */
	@Override
	public void clearIOBuffer() {
		if(localBuffer != null) {
			localBuffer.reset();
		}
	}
	
	/**
	 * 返回socket配置信息
	 * @return socket配置信息
	 */
	@Override
	public String toString() {
		return sockConf.toString();
	}
	
}
