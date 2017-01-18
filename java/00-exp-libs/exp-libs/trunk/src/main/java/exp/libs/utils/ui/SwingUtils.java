package exp.libs.utils.ui;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.FlowLayout;
import java.awt.GridLayout;

import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.border.TitledBorder;

import exp.libs.utils.pub.StrUtils;

/**
 * <PRE>
 * swing组件工具包
 * </PRE>
 * <B>PROJECT：</B> exp-libs
 * <B>SUPPORT：</B> EXP
 * @version   1.0 2015-12-27
 * @author    EXP: 272629724@qq.com
 * @since     jdk版本：jdk1.6
 */
public class SwingUtils {

	public static final int AUTO_SCROLL_MODE = 0;
	
	public static final int SHOW_SCROLL_MODE = 1;
	
	public static final int HIDE_SCROLL_MODE = -1;
	
	protected SwingUtils() {}
	
	/**
	 * 为指定组件添加自动滚动条（当文本超过宽/高边界时自动出现水平/垂直滚动条）
	 * @param component 组件
	 * @return 已添加滚动条的组件
	 */
	public static JScrollPane addScroll(Component component) {
		return addAutoScroll(component);
	}
	
	/**
	 * 为指定组件添加自动滚动条（当内容超过宽/高边界时自动出现水平/垂直滚动条）
	 * @param component 组件
	 * @return 已添加滚动条的组件
	 */
	public static JScrollPane addAutoScroll(Component component) {
		return addScroll(component, AUTO_SCROLL_MODE);
	}
	
	/**
	 * 为指定组件添加显式滚动条（总是显示水平/垂直滚动条）
	 * @param component 组件
	 * @return 已添加滚动条的组件
	 */
	public static JScrollPane addShowScroll(Component component) {
		return addScroll(component, SHOW_SCROLL_MODE);
	}
	
	/**
	 * 为指定组件添加隐式滚动条（总是隐藏水平/垂直滚动条）
	 * @param component 组件
	 * @return 已添加滚动条的组件
	 */
	public static JScrollPane addHideScroll(Component component) {
		return addScroll(component, HIDE_SCROLL_MODE);
	}
	
	/**
	 * 为指定组件添加滚动条
	 * @param component 组件
	 * @param mode 
	 * 		0:自动模式（当内容超过宽/高边界时自动出现水平/垂直滚动条）.
	 * 		1:显式模式（总是出现水平/垂直滚动条）.
	 * 		-1:隐式模式（总是隐藏水平/垂直滚动条）
	 * @return 已添加滚动条的组件
	 */
	public static JScrollPane addScroll(Component component, int mode) {
		component = (component == null ? new JTextArea() : component);
		JScrollPane scroll = new JScrollPane(component); 
		mode = (mode < AUTO_SCROLL_MODE ? HIDE_SCROLL_MODE : mode);
		mode = (mode > AUTO_SCROLL_MODE ? SHOW_SCROLL_MODE : mode);
		
		// 分别设置水平和垂直滚动条总是出现 
		if(mode == SHOW_SCROLL_MODE) {
			scroll.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS); 
			scroll.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
			
		//分别设置水平和垂直滚动条总是隐藏
		} else if(mode == HIDE_SCROLL_MODE) {
			scroll.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER); 
			scroll.setVerticalScrollBarPolicy( JScrollPane.VERTICAL_SCROLLBAR_NEVER);
			
		//分别设置水平和垂直滚动条自动出现 
		} else {
			scroll.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED); 
			scroll.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED); 
		}
		return scroll;
	}
	
	/**
	 * 获取配对组件面板（提示组件+输入组件）
	 *  布局风格为BorderLayout: 提示组件WEST, 输入组件CENTER
	 * @param label 提示组件的提示信息
	 * @return JLabel + JTextFields
	 */
	public static JPanel getPairsPanel(String label) {
		return getPairsPanel(label, "");
	}
	
	/**
	 * 获取配对组件面板（提示组件+输入组件）
	 * 	布局风格为BorderLayout: 提示组件WEST, 输入组件CENTER
	 * @param label 提示组件的提示信息
	 * @param input 输入组件的默认输入值
	 * @return JLabel + JTextFields
	 */
	public static JPanel getPairsPanel(String label, String input) {
		return getPairsPanel(label, new JTextField(input));
	}
	
	/**
	 * 获取配对组件面板（提示组件+输入组件）
	 * 	布局风格为BorderLayout: 提示组件WEST, 输入组件CENTER
	 * @param label 提示组件的提示信息
	 * @param input 输入组件
	 * @return JLabel + 自定义的输入组件
	 */
	public static JPanel getPairsPanel(String label, Component input) {
		return getPairsPanel(new JLabel(StrUtils.concat("  [", label, "]:  ")), input);
	}
	
	/**
	 * 获取配对组件面板（提示组件+输入组件）
	 * 	布局风格为BorderLayout: 提示组件WEST, 输入组件CENTER
	 * @param label 提示组件
	 * @param input 输入组件
	 * @return 自定义的提示组件 + 自定义的输入组件
	 */
	public static JPanel getPairsPanel(Component label, Component input) {
		JPanel panel = new JPanel(new BorderLayout());
		panel.add(label, BorderLayout.WEST);
		panel.add(input, BorderLayout.CENTER);
		return panel;
	}
	
	/**
	 * 获取水平流式布局面板
	 * @param components 添加到该面板的组件集合
	 * @return 水平流式布局面板
	 */
	public static JPanel getHFlowPanel(Component... components) {
		JPanel panel = new JPanel(new FlowLayout());
		if(components != null) {
			for(Component component : components) {
				panel.add(component);
			}
		}
		return panel;
	}
	
	/**
	 * 获取垂直流式布局面板
	 * @param components 添加到该面板的组件集合
	 * @return 垂直流式布局面板
	 */
	public static JPanel getVFlowPanel(Component... components) {
		JPanel panel = new JPanel(new FlowLayout());
		if(components != null) {
			for(Component component : components) {
				panel.add(component);
			}
		}
		return panel;
	}
	
	/**
	 * 获取水平表格布局面板(1行N列)
	 * @param components 添加到该面板的组件集合
	 * @return 水平表格布局面板(1行N列)
	 */
	public static JPanel getHGridPanel(Component... components) {
		int num = (components == null ? 1 : components.length);
		JPanel panel = new JPanel(new GridLayout(1, num));
		if(components != null) {
			for(int i = 0; i < num; i++) {
				panel.add(components[i], i);
			}
		}
		return panel;
	}
	
	/**
	 * 获取垂直表格布局面板(N行1列)
	 * @param components 添加到该面板的组件集合
	 * @return 垂直表格布局面板(N行1列)
	 */
	public static JPanel getVGridPanel(Component... components) {
		int num = (components == null ? 1 : components.length);
		JPanel panel = new JPanel(new GridLayout(num, 1));
		if(components != null) {
			for(int i = 0; i < num; i++) {
				panel.add(components[i], i);
			}
		}
		return panel;
	}
	
	/**
	 * 获取 [中心-北] 边框布局面板
	 * @param center 期望置放到中心的组件
	 * @param north 期望置放到北方的组件
	 * @return [中心-北] 边框布局面板
	 */
	public static JPanel getNBorderPanel(Component center, Component north) {
		JPanel panel = new JPanel(new BorderLayout());
		panel.add(center, BorderLayout.CENTER);
		panel.add(north, BorderLayout.NORTH);
		return panel;
	}
	
	/**
	 * 获取 [中心-南] 边框布局面板
	 * @param center 期望置放到中心的组件
	 * @param north 期望置放到南方的组件
	 * @return [中心-南] 边框布局面板
	 */
	public static JPanel getSBorderPanel(Component center, Component south) {
		JPanel panel = new JPanel(new BorderLayout());
		panel.add(center, BorderLayout.CENTER);
		panel.add(south, BorderLayout.SOUTH);
		return panel;
	}
	
	/**
	 * 获取 [中心-西] 边框布局面板
	 * @param center 期望置放到中心的组件
	 * @param north 期望置放到西方的组件
	 * @return [中心-西] 边框布局面板
	 */
	public static JPanel getWBorderPanel(Component center, Component west) {
		JPanel panel = new JPanel(new BorderLayout());
		panel.add(center, BorderLayout.CENTER);
		panel.add(west, BorderLayout.WEST);
		return panel;
	}
	
	/**
	 * 获取 [中心-东] 边框布局面板
	 * @param center 期望置放到中心的组件
	 * @param north 期望置放到东方的组件
	 * @return [中心-东] 边框布局面板
	 */
	public static JPanel getEBorderPanel(Component center, Component east) {
		JPanel panel = new JPanel(new BorderLayout());
		panel.add(center, BorderLayout.CENTER);
		panel.add(east, BorderLayout.EAST);
		return panel;
	}
	
	/**
	 * 为组件添加边框
	 * @param component 需要添加边框的组件
	 * @return 已添加边框的组件（与入参为同一对象）
	 */
	public static JComponent addBorder(JComponent component) {
		return addBorder(component, "");
	}
	
	/**
	 * 为组件添加边框
	 * @param component 需要添加边框的组件
	 * @param borderTitle 边框提示
	 * @return 已添加边框的组件（与入参为同一对象）
	 */
	public static JComponent addBorder(JComponent component, String borderTitle) {
		component.setBorder(new TitledBorder(borderTitle));
		return component;
	}
	
	/**
	 * 信息弹窗
	 * @param msg 普通消息
	 */
	public static void info(String msg) {
		JOptionPane.showMessageDialog(
			    null, msg, "Info", JOptionPane.INFORMATION_MESSAGE);
	}
	
	/**
	 * 警告弹窗
	 * @param msg 警告消息
	 */
	public static void warn(String msg) {
		JOptionPane.showMessageDialog(
			    null, msg, "Warn", JOptionPane.WARNING_MESSAGE);
	}
	
	/**
	 * 异常弹窗
	 * @param msg 异常消息
	 * @param e 异常
	 */
	public static void error(String msg) {
		error(msg, null);
	}
	
	/**
	 * 异常弹窗
	 * @param msg 异常消息
	 * @param e 异常
	 */
	public static void error(String msg, Throwable e) {
		JOptionPane.showMessageDialog(
			    null, msg, "Error", JOptionPane.ERROR_MESSAGE);
		if(e != null) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 确认弹窗
	 * @param msg 确认消息
	 * @param true:是; false:否
	 */
	public static boolean confirm(String msg) {
		return (0 == JOptionPane.showConfirmDialog(
			    null, msg, "Tips", JOptionPane.ERROR_MESSAGE));
	}
	
}