package com.scoop.crawler.weibo.ui;

import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.InputStream;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTextField;

import com.scoop.crawler.weibo.util.Logger;

/**
 * 输入验证码的对话框。
 * 
 * @author taofucheng
 * 
 */
public class InputValidCodeDialog extends JFrame {
	private static final long serialVersionUID = -7064636388745934395L;
	private static final JTextField input = new JTextField("", 8);
	private static final JButton btn = new JButton();
	private static InputValidCodeDialog instance;
	/** 验证码输入框名称 */
	public static final String VALID_CODE_NAME = "door";
	protected static String VALID_CODE_URL = "http://login.sina.com.cn/cgi/pin.php?r=49541020&s=0&p=ddd4250f1f919b444afed84b62bdd7e77aed";

	public InputValidCodeDialog() {
		setTitle("输入验证码");
		setSize(400, 90);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		int windowWidth = getWidth(); // 获得窗口宽
		int windowHeight = getHeight(); // 获得窗口高
		Toolkit kit = Toolkit.getDefaultToolkit(); // 定义工具包
		Dimension screenSize = kit.getScreenSize(); // 获取屏幕的尺寸
		int screenWidth = screenSize.width; // 获取屏幕的宽
		int screenHeight = screenSize.height; // 获取屏幕的高
		setLocation(screenWidth / 2 - windowWidth / 2, screenHeight / 2 - windowHeight / 2);// 设置窗口居中显示
	}

	public static void main(String[] args) {
		input(null);
	}

	public static String input(InputStream image) {
		if (instance == null) {
			instance = new InputValidCodeDialog();
		}
		instance.showDialog();
		return "";
	}

	private void showDialog() {
		btn.addActionListener(new Click());
		btn.setText("确定");
		//
		JPanel jp = new JPanel();
		jp.add(input);
		jp.add(btn);

		add(jp);
		//
		setVisible(true);
	}

	static class Click implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			instance.dispose();
			Logger.log(input.getText());
		}
	}

	static class Content extends JPanel {
		private static final long serialVersionUID = -962912904177115831L;
	}
}
