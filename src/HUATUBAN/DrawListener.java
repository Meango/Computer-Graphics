package HUATUBAN;

/**
 * 画图版监听类，用于监听画图版的界面
 * 
 * 
 * @author Yaomz
 * @version 1.0
 */

import java.awt.AWTException;
import java.awt.BasicStroke;
import java.awt.Canvas;
import java.awt.Color;
import java.awt.Desktop;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.HeadlessException;
import java.awt.Image;
import java.awt.Rectangle;
import java.awt.Robot;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.geom.RoundRectangle2D;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.ObjectInputStream.GetField;
import java.io.OutputStreamWriter;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;

import javax.swing.AbstractButton;
import javax.swing.ButtonGroup;
import javax.swing.ButtonModel;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JColorChooser;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.JTextPane;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.text.Document;
import javax.swing.text.JTextComponent;



import java.awt.*;
import java.awt.event.*;
import java.util.*;
import javax.swing.*;
import java.awt.geom.*;
import java.awt.image.BufferedImage;
import java.io.*;
import HUATUBAN.Draw;
import HUATUBAN.Point;

class Edge {
	int ymax;
	double x, deltax;
	Edge nextEdge = null;

	public int getYmax() {
		return ymax;
	}

	public void setYmax(int ymax) {
		this.ymax = ymax;
	}

	public double getX() {
		return x;
	}

	public void setX(double x) {
		this.x = x;
	}

	public double getDeltax() {
		return deltax;
	}

	public void setDeltax(double deltax) {
		this.deltax = deltax;
	}

	public Edge getNextEdge() {
		return nextEdge;
	}

	public void setNextEdge(Edge nextEdge) {
		this.nextEdge = nextEdge;
	}
}

public class DrawListener implements ActionListener, MouseListener, MouseMotionListener {
	public ButtonGroup bg;
	private Color color;// 颜色属性
	private Graphics g;// 画笔属性
	private String str;// 保存按钮上的字符串，区分不同的按钮
	private int x1, y1, x2, y2;// (x1,y1),(x2,y2)分别为鼠标的按下和释放时的坐标
	private JButton nowColor;// 当前颜色按钮
	private int point[][] = new int[100][2];
	private Point NodePointA[];
	private Point NodePointB[];
	private int num; // 在绘制多边形时采用，记录多边形的点，并绘制
	private int flag = 1;// 在裁剪多边形采用
	public Random r = new Random();
	// 构造函数1

	public DrawListener(Graphics g1) {
		g = g1;
	}

	// 构造函数2
	public DrawListener(Graphics g2, ButtonGroup bg2) {
		g = g2;
		bg = bg2;
	}

	/**
	 * 绘制虚线边框的矩形函数
	 */
	public void drawDash() {
		// 取得g原本的颜色
		Color c = g.getColor();
		Color color = new Color(51, 153, 255);
		// 绘制两条水平线
		for (int i = x1; i <= x2; i += 4) {
			// 设置虚线可见部分的颜色
			g.setColor(color);
			g.drawLine(i, y1, i + 2, y1);
			g.drawLine(i, y2, i + 2, y2);
			// 设置虚线不可见部分为白色
			g.setColor(Color.WHITE);
			g.drawLine(i + 2, y1, i + 4, y1);
			g.drawLine(i + 2, y2, i + 4, y2);
		}
		for (int i = y1; i <= y2; i += 4) {
			// 设置虚线可见部分的颜色
			g.setColor(color);
			g.drawLine(x1, i, x1, i + 2);
			g.drawLine(x2, i, x2, i + 2);
			// 设置虚线不可见部分为白色
			g.setColor(Color.WHITE);
			g.drawLine(x1, i + 2, x1, i + 4);
			g.drawLine(x2, i + 2, x2, i + 4);
		}
		// 绘制虚线框的四个定点以及四条边的中点使其变得更明显
		g.setColor(color);
		// 设置画笔的粗细
		((Graphics2D) g).setStroke(new BasicStroke(3));
		g.drawLine(x1, y1, x1, y1);
		g.drawLine(x1, y2, x1, y2);
		g.drawLine(x2, y1, x2, y1);
		g.drawLine(x2, y2, x2, y2);
		g.drawLine((x1 + x2) >> 1, y1, (x1 + x2) >> 1, y1);
		g.drawLine(x1, (y1 + y2) >> 1, x1, (y1 + y2) >> 1);
		g.drawLine(x2, (y1 + y2) >> 1, x2, (y1 + y2) >> 1);
		g.drawLine((x1 + x2) >> 1, y2, (x1 + x2) >> 1, y2);
		// 恢复g原本的颜色
		g.setColor(c);
		((Graphics2D) g).setStroke(new BasicStroke(1));
	}

	/**
	 * 绘制虚线函数
	 * @param x0,y0,x1,y1
	 */
	public void drawDotLine(int x0, int y0, int x1, int y1) {
		int Model[] = { 1, 0, 1, 0, 1, 0, 1, 0 };
		int x = x0;
		int y = y0;

		int w = x1 - x0;
		int h = y1 - y0;

		int dx1 = w < 0 ? -1 : (w > 0 ? 1 : 0);
		int dy1 = h < 0 ? -1 : (h > 0 ? 1 : 0);

		int dx2 = w < 0 ? -1 : (w > 0 ? 1 : 0);
		int dy2 = 0;

		int fastStep = Math.abs(w);
		int slowStep = Math.abs(h);
		if (fastStep <= slowStep) {
			fastStep = Math.abs(h);
			slowStep = Math.abs(w);

			dx2 = 0;
			dy2 = h < 0 ? -1 : (h > 0 ? 1 : 0);
		}
		int numerator = fastStep >> 1;

		for (int i = 0; i <= fastStep; i++) {
			if (Model[i % 8] == 1) {
				g.drawLine(x, y, x, y);
			}
			numerator += slowStep;
			if (numerator >= fastStep) {
				numerator -= fastStep;
				x += dx1;
				y += dy1;
			} else {
				x += dx2;
				y += dy2;
			}
		}
	}

	/**
	 * 绘制点划线函数
	 * @param x0,y0,x1,y1
	 */
	public void drawDashLine(int x0, int y0, int x1, int y1) {
		int Model[] = { 1, 1, 1, 0, 0, 1, 0, 0 };
		int x = x0;
		int y = y0;

		int w = x1 - x0;
		int h = y1 - y0;

		int dx1 = w < 0 ? -1 : (w > 0 ? 1 : 0);
		int dy1 = h < 0 ? -1 : (h > 0 ? 1 : 0);

		int dx2 = w < 0 ? -1 : (w > 0 ? 1 : 0);
		int dy2 = 0;

		int fastStep = Math.abs(w);
		int slowStep = Math.abs(h);
		if (fastStep <= slowStep) {
			fastStep = Math.abs(h);
			slowStep = Math.abs(w);

			dx2 = 0;
			dy2 = h < 0 ? -1 : (h > 0 ? 1 : 0);
		}
		int numerator = fastStep >> 1;

		for (int i = 0; i <= fastStep; i++) {
			if (Model[i % 8] == 1) {
				g.drawLine(x, y, x, y);
			}
			numerator += slowStep;
			if (numerator >= fastStep) {
				numerator -= fastStep;
				x += dx1;
				y += dy1;
			} else {
				x += dx2;
				y += dy2;
			}
		}
	}

	/**
	 * 绘制短划线函数
	 * @param x0,y0,x1,y1
	 */
	public void drawBrokenLine(int x0, int y0, int x1, int y1) {
		int Model[] = { 1, 1, 1, 1, 0, 0, 0, 0 };
		int x = x0;
		int y = y0;

		int w = x1 - x0;
		int h = y1 - y0;

		int dx1 = w < 0 ? -1 : (w > 0 ? 1 : 0);
		int dy1 = h < 0 ? -1 : (h > 0 ? 1 : 0);

		int dx2 = w < 0 ? -1 : (w > 0 ? 1 : 0);
		int dy2 = 0;

		int fastStep = Math.abs(w);
		int slowStep = Math.abs(h);
		if (fastStep <= slowStep) {
			fastStep = Math.abs(h);
			slowStep = Math.abs(w);

			dx2 = 0;
			dy2 = h < 0 ? -1 : (h > 0 ? 1 : 0);
		}
		int numerator = fastStep >> 1;

		for (int i = 0; i <= fastStep; i++) {
			if (Model[i % 8] == 1) {
				g.drawLine(x, y, x, y);
			}
			numerator += slowStep;
			if (numerator >= fastStep) {
				numerator -= fastStep;
				x += dx1;
				y += dy1;
			} else {
				x += dx2;
				y += dy2;
			}
		}
	}

	/**
	 * 根据4对称原理绘制其他象限的椭圆
	 * 
	 */
	void EllipsePoints(int CenterX, int CenterY, int x, int y) {
		drawBLine(x + CenterX, y + CenterY, x + CenterX, y + CenterY);
		drawBLine(-x + CenterX, y + CenterY, -x + CenterX, y + CenterY);
		drawBLine(x + CenterX, -y + CenterY, x + CenterX, -y + CenterY);
		drawBLine(-x + CenterX, -y + CenterY, -x + CenterX, -y + CenterY);

	}

	/**
	 * 中点算法实现绘制椭圆
	 * @param x0,y0,x1,y1
	 */
	public void drawOval(int x0, int y0, int x1, int y1) // 圆心x，圆心y，横长，纵长
	{
		int CenterX = (x0 + x1) / 2;
		int CenterY = (y0 + y1) / 2;
		int a = Math.abs(x1 - x0);
		int b = Math.abs(y1 - y0);
		double d2, d1 = Math.pow(b, 2) - (Math.pow(a, 2) * b) + 0.25 * Math.pow(a, 2);
		int x = 0, y = b;
		EllipsePoints(CenterX, CenterY, x, y);

		// 梯度检测
		while (Math.pow(a, 2) * (y - 0.5) > Math.pow(b, 2) * (x + 1)) {
			// 在区域一
			if (d1 < 0) // 选择E点
			{
				d1 += Math.pow(b, 2) * (2 * x + 3);
			} else // 选择SE点
			{
				d1 += Math.pow(b, 2) * (2 * x + 3) + Math.pow(a, 2) * (-2 * y + 2);
				y--;
			}
			x++;
			EllipsePoints(CenterX, CenterY, x, y);

		}
		d2 = Math.pow(b, 2) * Math.pow(x + 0.5, 2) + Math.pow(a, 2) * Math.pow(y - 1, 2) - Math.pow(a * b, 2);
		while (y > 0) // 区域二
		{
			if (d2 < 0) // 选择SE点
			{
				d2 += Math.pow(b, 2) * (2 * x + 2) + Math.pow(a, 2) * (-2 * y + 3);
				x++;
			} else {
				d2 += Math.pow(a, 2) * (-2 * y + 3);
			}
			y--;
			EllipsePoints(CenterX, CenterY, x, y);
		}
	}

	/**
	 * 中点算法实现画圆
	 * @param x0,y0,x1,y1
	 */
	public void drawCicle(int x0, int y0, int x1, int y1) // 输入圆心和终点
	{
		int x, y, d;
		int r = (int) Math.sqrt(Math.pow((x1 - x0), 2) + Math.pow((y1 - y0), 2));// 圆心x，圆心y，半径
		x = 0;
		y = r;
		d = 5 - 4 * r;
		g.drawLine(x + x0, y + y0, x + x0, y + y0);
		g.drawLine(y + x0, x + y0, y + x0, x + y0);
		g.drawLine(-y + x0, x + y0, -y + x0, x + y0);
		g.drawLine(-x + x0, y + y0, -x + x0, y + y0);
		g.drawLine(y + x0, -x + y0, y + x0, -x + y0);
		g.drawLine(x + x0, -y + y0, x + x0, -y + y0);
		g.drawLine(-x + x0, -y + y0, -x + x0, -y + y0);
		g.drawLine(-y + x0, -x + y0, -y + x0, -x + y0);
		while (y > x) {
			if (d <= 0) {
				d += 8 * x + 12;
			} else {
				d += 8 * (x - y) + 20;
				y--;
			}
			x++;
			g.drawLine(x + x0, y + y0, x + x0, y + y0);
			g.drawLine(y + x0, x + y0, y + x0, x + y0);
			g.drawLine(-y + x0, x + y0, -y + x0, x + y0);
			g.drawLine(-x + x0, y + y0, -x + x0, y + y0);
			g.drawLine(y + x0, -x + y0, y + x0, -x + y0);
			g.drawLine(x + x0, -y + y0, x + x0, -y + y0);
			g.drawLine(-x + x0, -y + y0, -x + x0, -y + y0);
			g.drawLine(-y + x0, -x + y0, -y + x0, -x + y0);
		}
	}

	/**
	 * 中点算法实现画线
	 * @param x0,y0,x1,y1
	 */
	public void drawMLine(int x0, int y0, int x1, int y1) // 输入起点和终点
	{
		int dx, dy, incrE, incrNE, d, x, y;
		dx = x1 - x0;
		dy = y1 - y0;
		d = dx - 2 * dy;
		incrE = -2 * dy;
		incrNE = 2 * (dx - dy);
		x = x0;
		y = y0;
		g.drawLine(x, y, x, y);
		while (x < x1) {
			if (d > 0) {
				d += incrE;
			} else {
				d += incrNE;
				y++;
			}
			x++;
			g.drawLine(x, y, x, y);
		}
	}

	/**
	 * 反混淆算法实现画线
	 * @param x0,y0,x1,y1
	 */
	public void drawFanLine(int x0, int y0, int x1, int y1) // 输入起点和终点
	{
		double temp, w1, w2, w3, temp2;
		int weight[][] = { { 1, 2, 4, 2, 1 }, { 2, 5,6,5, 2 }, { 4, 6, 8,6,4 },{2,5,6,5,2 },{1,2,4,2,1}};
		int myRed;
		int myGreen;
		int myBlue;
		int x = x0;
		int y = y0;

		int w = x1 - x0;
		int h = y1 - y0;

		int dx1 = w < 0 ? -1 : (w > 0 ? 1 : 0);
		int dy1 = h < 0 ? -1 : (h > 0 ? 1 : 0);

		int dx2 = w < 0 ? -1 : (w > 0 ? 1 : 0);
		int dy2 = 0;

		int fastStep = Math.abs(w);
		int slowStep = Math.abs(h);
		if (fastStep <= slowStep) {
			fastStep = Math.abs(h);
			slowStep = Math.abs(w);

			dx2 = 0;
			dy2 = h < 0 ? -1 : (h > 0 ? 1 : 0);
		}
		int numerator = fastStep >> 1;

		if (x0 == x1) {
			for (int i = 0; i <= fastStep; i++) {
				g.drawLine(x, y, x, y);
				numerator += slowStep;
				if (numerator >= fastStep) {
					numerator -= fastStep;
					x += dx1;
					y += dy1;
				} else {
					x += dx2;
					y += dy2;
				}
			}
		} else {
			for (int i = 0; i <= fastStep; i++) {

				// #1
				w1 = 0;
				for (int ii = 0; ii < 5; ii++) {
					for (int jj = 0; jj < 5; jj++) {
						temp = (y + 1 + jj / 5.0 - 1 / 5.0 - 1.0 * h / w * (x + ii / 5.0 - 1 / 5.0 - x0)
								- 0.5 * Math.sqrt(1 + h / w * h / w) - y0);
						if (temp < 0) {
							w1 += weight[ii][jj];
						}
					}
				}
				myRed = color.getRed();
				myGreen = color.getGreen();
				myBlue = color.getBlue();
				myRed = (int) ((255 - myRed) * (1 - w1 / 88.0)) + myRed;
				myGreen = (int) ((255 - myGreen) * (1 - w1 / 88.0)) + myGreen;
				myBlue = (int) ((255 - myBlue) * (1 - w1 / 88.0)) + myBlue;
				g.setColor(new Color(myRed, myGreen, myBlue));
				g.drawLine(x, y + 1, x, y + 1);

				// #2
				w2 = 0;
				for (int ii = 0; ii < 5; ii++) {
					for (int jj = 0; jj < 5; jj++) {
						temp = (y + jj / 5.0 - 1 / 5.0 - 1.0 * h / w * (x + ii / 5.0 - 1 / 5.0 - x0)
								- 0.5 * Math.sqrt(1 + h / w * h / w) - y0);
						temp2 = (y + jj / 5.0 - 1 / 5.0 - 1.0 * h / w * (x + ii / 5.0 - 1 / 5.0 - x0)
								+ 0.5 * Math.sqrt(1 + h / w * h / w) - y0);
						if (temp < 0 && temp2 > 0) {
							w2 += weight[ii][jj];
						}
					}
				}
				myRed = color.getRed();
				myGreen = color.getGreen();
				myBlue = color.getBlue();
				myRed = (int) ((255 - myRed) * (1 - w2 / 88.0)) + myRed;
				myGreen = (int) ((255 - myGreen) * (1 - w2 / 88.0)) + myGreen;
				myBlue = (int) ((255 - myBlue) * (1 - w2 / 88.0)) + myBlue;
				g.setColor(new Color(myRed, myGreen, myBlue));
				g.drawLine(x, y, x, y);
				// #3
				w3 = 0;
				for (int ii = 0; ii < 5; ii++) {
					for (int jj = 0; jj < 5; jj++) {
						temp = (y - 1 + jj / 5.0 - 1 / 5.0 - 1.0 * h / w * (x + ii / 5.0 - 1 / 5.0 - x0)
								+ 0.5 * Math.sqrt(1 + h / w * h / w) - y0);
						if (temp > 0) {
							w3 += weight[ii][jj];
						}
					}
				}
				myRed = color.getRed();
				myGreen = color.getGreen();
				myBlue = color.getBlue();
				myRed = (int) ((255 - myRed) * (1 - w3 / 88.0)) + myRed;
				myGreen = (int) ((255 - myGreen) * (1 - w3 / 88.0)) + myGreen;
				myBlue = (int) ((255 - myBlue) * (1 - w3 / 88.0)) + myBlue;
				g.setColor(new Color(myRed, myGreen, myBlue));
				g.drawLine(x, y - 1, x, y - 1);
				// 测试权重是否正确
				// System.out.println("权重" + w1 / 16.0 + " " + w2 / 16.0 + " " + w3 / 16.0 + "
				// ");
				numerator += slowStep;
				if (numerator >= fastStep) {
					numerator -= fastStep;
					x += dx1;
					y += dy1;
				} else {
					x += dx2;
					y += dy2;
				}
			}
		}
	}

	/**
	 * Bresenham算法实现画线
	 */
	public void drawBLine(int x0, int y0, int x1, int y1) // 输入起点和终点
	{
		int x = x0;
		int y = y0;

		int w = x1 - x0;
		int h = y1 - y0;

		int dx1 = w < 0 ? -1 : (w > 0 ? 1 : 0);
		int dy1 = h < 0 ? -1 : (h > 0 ? 1 : 0);

		int dx2 = w < 0 ? -1 : (w > 0 ? 1 : 0);
		int dy2 = 0;

		int fastStep = Math.abs(w);
		int slowStep = Math.abs(h);
		if (fastStep <= slowStep) {
			fastStep = Math.abs(h);
			slowStep = Math.abs(w);

			dx2 = 0;
			dy2 = h < 0 ? -1 : (h > 0 ? 1 : 0);
		}
		int numerator = fastStep >> 1;

		for (int i = 0; i <= fastStep; i++) {
			g.drawLine(x, y, x, y);
			numerator += slowStep;
			if (numerator >= fastStep) {
				numerator -= fastStep;
				x += dx1;
				y += dy1;
			} else {
				x += dx2;
				y += dy2;
			}
		}
	}

	/**
	 * 实现绘制矩形的功能
	 */
	public void drawRect(int x0, int y0, int x1, int y1) {
		int width, height;
		width = x1 - x0;
		height = y1 - y0;
		drawBLine(x0, y0, x0 + width, y0);
		drawBLine(x0 + width, y0, x1, y1);
		drawBLine(x0, y0, x0, y0 + height);
		drawBLine(x0, y0 + height, x1, y1);
	}

	/**
	 * 实现扫描线转换填充多边形的功能
	 */
	public void FillPolygon(int a[][]) {
		int p[][] = a;
		int min = a[0][1];
		int max = a[0][1];
		for (int i = 0; i < num - 1; i++) {
			if (a[i][1] < min) {
				min = a[i][1];
			}
			if (a[i][1] > max) {
				max = a[i][1];
			}
		}
		// System.out.println("将在屏幕上填充这些点围成的多边形:");
		// for (int i = 0; i < num - 1; i++) {
		// System.out.println(p[i][0] + "," + p[i][1]);
		// }
		// 排序
		int b[] = new int[num - 1];
		for (int i = 0; i < num - 1; i++) {
			b[i] = p[i][1];

		}
		for (int i = 0; i < num - 1; i++) {
			for (int j = i; j < num - 1; j++) {
				if (b[i] > b[j]) {
					int temp = b[i];
					b[i] = b[j];
					b[j] = temp;
				}
			}
		}
		Edge ET[] = new Edge[max];
		for (int i = min; i < max; i++) {
			ET[i] = null;
			for (int j = 0; j < num - 1; j++) {
				if (a[j][1] == i) {
					if (j < num - 2 && j > 0) {
						if (a[j + 1][1] > i) {
							if (ET[i] == null) {

								ET[i] = new Edge();
								ET[i].ymax = Math.max(a[j + 1][1], a[j][1]);
								ET[i].setX(a[j][0]);
								ET[i].deltax = (double) (a[j + 1][0] - a[j][0]) / (a[j + 1][1] - a[j][1]);
								ET[i].nextEdge = null;
							} else {
								Edge sptr = ET[i];
								Edge newEdge = new Edge();
								while (sptr.nextEdge != null) {
									sptr = sptr.nextEdge;
								}
								newEdge.ymax = Math.max(a[j + 1][1], a[j][1]);
								newEdge.setX(a[j][0]);
								newEdge.deltax = (double) (a[j + 1][0] - a[j][0]) / (a[j + 1][1] - a[j][1]);
								newEdge.nextEdge = null;
								sptr.nextEdge = newEdge;
							}

						}
						if (a[j - 1][1] > i) {
							if (ET[i] == null) {
								ET[i] = new Edge();
								ET[i].ymax = Math.max(a[j - 1][1], a[j][1]);
								ET[i].setX(a[j][0]);
								ET[i].deltax = (double) (a[j - 1][0] - a[j][0]) / (a[j - 1][1] - a[j][1]);
								ET[i].nextEdge = null;
							} else {
								Edge sptr = ET[i];
								Edge newEdge = new Edge();
								while (sptr.nextEdge != null) {
									sptr = sptr.nextEdge;
								}
								newEdge.ymax = Math.max(a[j - 1][1], a[j][1]);
								newEdge.setX(a[j][0]);
								newEdge.deltax = (double) (a[j - 1][0] - a[j][0]) / (a[j - 1][1] - a[j][1]);
								newEdge.nextEdge = null;
								sptr.nextEdge = newEdge;
							}

						}
					} else if (j == num - 2) {
						if (a[0][1] > i) {
							if (ET[i] == null) {
								ET[i] = new Edge();
								ET[i].ymax = Math.max(a[0][1], a[j][1]);
								ET[i].setX(a[j][0]);
								ET[i].deltax = (double) (a[0][0] - a[j][0]) / (a[0][1] - a[j][1]);
								ET[i].nextEdge = null;
							} else {
								Edge sptr = ET[i];
								Edge newEdge = new Edge();
								while (sptr.nextEdge != null) {
									sptr = sptr.nextEdge;
								}
								newEdge.ymax = Math.max(a[j + 1][1], a[j][1]);
								newEdge.setX(a[j][0]);
								newEdge.deltax = (double) (a[0][0] - a[j][0]) / (a[0][1] - a[j][1]);
								newEdge.nextEdge = null;
								sptr.nextEdge = newEdge;
							}
						}
						if (a[j - 1][1] > i) {
							if (ET[i] == null) {
								ET[i] = new Edge();
								ET[i].ymax = Math.max(a[j - 1][1], a[j][1]);
								ET[i].setX(a[j][0]);
								ET[i].deltax = (double) (a[j - 1][0] - a[j][0]) / (a[j - 1][1] - a[j][1]);
								ET[i].nextEdge = null;
							} else {
								Edge sptr = ET[i];
								Edge newEdge = new Edge();
								while (sptr.nextEdge != null) {
									sptr = sptr.nextEdge;
								}
								newEdge.ymax = Math.max(a[j - 1][1], a[j][1]);
								newEdge.setX(a[j][0]);
								newEdge.deltax = (double) (a[j - 1][0] - a[j][0]) / (a[j - 1][1] - a[j][1]);
								newEdge.nextEdge = null;
								sptr.nextEdge = newEdge;
							}
						}

					} else if (j == 0) {
						if (a[j + 1][1] > i) {
							if (ET[i] == null) {
								ET[i] = new Edge();
								ET[i].ymax = Math.max(a[j + 1][1], a[j][1]);
								ET[i].setX(a[j][0]);
								ET[i].deltax = (double) (a[j + 1][0] - a[j][0]) / (a[j + 1][1] - a[j][1]);
								ET[i].nextEdge = null;
							} else {
								Edge sptr = ET[i];
								Edge newEdge = new Edge();
								while (sptr.nextEdge != null) {
									sptr = sptr.nextEdge;
								}
								newEdge.ymax = Math.max(a[j + 1][1], a[j][1]);
								newEdge.setX(a[j][0]);
								newEdge.deltax = (double) (a[j + 1][0] - a[j][0]) / (a[j + 1][1] - a[j][1]);
								newEdge.nextEdge = null;
								sptr.nextEdge = newEdge;

							}

						}
						if (a[num - 2][1] > i) {
							if (ET[i] == null) {
								ET[i] = new Edge();
								ET[i].ymax = Math.max(a[num - 2][1], a[j][1]);
								ET[i].setX(a[j][0]);
								ET[i].deltax = (double) (a[num - 2][0] - a[j][0]) / (a[num - 2][1] - a[j][1]);
								ET[i].nextEdge = null;
							} else {
								Edge sptr = ET[i];
								Edge newEdge = new Edge();
								while (sptr.nextEdge != null) {
									sptr = sptr.nextEdge;
								}
								newEdge.ymax = Math.max(a[num - 2][1], a[j][1]);
								newEdge.setX(a[j][0]);
								newEdge.deltax = (double) (a[num - 2][0] - a[j][0]) / (a[num - 2][1] - a[j][1]);
								newEdge.nextEdge = null;
								sptr.nextEdge = newEdge;
							}
						}

					}
				}
			}
		}
		// 排序
		for (int i = min; i < max; i++) {
			if (ET[i] != null) {
				Edge j;
				Edge k;
				int t1;
				double t2, t3;
				for (j = ET[i]; j.nextEdge != null; j = j.nextEdge) {
					for (k = j.nextEdge; k != null; k = k.nextEdge) {
						if (j.x > k.x) {
							t1 = j.getYmax();
							t2 = j.getX();
							t3 = j.getDeltax();
							j.ymax = k.ymax;
							j.deltax = k.deltax;
							j.x = k.x;
							k.ymax = t1;
							k.x = t2;
							k.deltax = t3;
						}
					}
				}
			}
		}
		//// 测试边的分类表的正确性
		// for (int i = min; i < max; i++) {
		// if (ET[i] != null) {
		// Edge sptr;
		// sptr = ET[i];
		// System.out.print("第" + i + "行：");
		// while (sptr != null) {
		// System.out.println(sptr.getYmax() + "," + sptr.getX() + "," +
		//// sptr.getDeltax());
		// sptr = sptr.nextEdge;
		// }
		//
		// }
		// }
		Edge AEL;
		Edge sptr = null;
		AEL = null;
		int c[] = new int[30];
		int i = min;
		while (i < max) {
			num = 0;
			if (ET[i] != null) {
				sptr = ET[i];
				if (AEL != null) {
					sptr = AEL;
					while (sptr.nextEdge != null) {
						sptr = sptr.nextEdge;
					}
					sptr.nextEdge = ET[i];
				} else {
					AEL = ET[i];
				}
			}
			if (AEL != null) {
				Edge j;
				Edge k;
				int t1;
				double t2, t3;
				for (j = AEL; j.nextEdge != null; j = j.nextEdge) {
					num++;
					for (k = j.nextEdge; k != null; k = k.nextEdge) {
						if (j.x > k.x) {
							t1 = j.ymax;
							t2 = j.x;
							t3 = j.deltax;
							j.ymax = k.ymax;
							j.x = k.x;
							j.deltax = k.deltax;
							k.ymax = t1;
							k.x = t2;
							k.deltax = t3;
						}
					}
				}
			}
			sptr = AEL;
			while (sptr != null) {
				sptr = sptr.nextEdge;

			}
			sptr = AEL;
			int t = 0;
			while (sptr != null) {
				if (t % 2 == 0)
					c[t] = (int) (sptr.x + 0.5);
				else
					c[t] = (int) (sptr.x - 0.5);
				t++;
				sptr = sptr.nextEdge;
			}
			for (int m = 0; m < num; m += 2) {
				drawBLine(c[m], i, c[m + 1], i);
			}
			i++;

			if (AEL.ymax != i) {
				Edge sp = AEL;
				sptr = AEL.nextEdge;
				while (sptr != null) {
					if (sptr.ymax == i) {
						sp.nextEdge = sptr.nextEdge;
						sptr = sptr.nextEdge;
					} else {
						sptr = sptr.nextEdge;
						sp = sp.nextEdge;
					}
				}
			} else {
				AEL = AEL.nextEdge;
				Edge sp = AEL;
				sptr = AEL.nextEdge;
				while (sptr != null) {
					if (sptr.ymax == i) {
						sp.nextEdge = sptr.nextEdge;
						sptr = sptr.nextEdge;
					} else {
						sptr = sptr.nextEdge;
						sp = sp.nextEdge;
					}
				}
			}

			for (sptr = AEL; sptr != null; sptr = sptr.nextEdge) {
				sptr.x = sptr.x + sptr.deltax;
			}

		}

	}

	/**
	 * 种子扫描算法实现填充圆
	 */

	void FloodFill(int x, int y)
	// (x,y) 种子像素的坐标；
	{
		try {
			Color bgcolor;
			Robot robot = new Robot();// Robot类的使用
			// 拿到坐标点的那个矩形
			Rectangle rect = new Rectangle(x, y, 1, 1);
			// 生成该矩形的缓冲图片
			BufferedImage bi = robot.createScreenCapture(rect);
			// 得到图片的背景颜色
			int c = bi.getRGB(0, 0);
			bgcolor = new Color(c);
			if (bgcolor.getRGB() == Color.black.getRGB()) {
				g.drawLine(x, y, x, y);
				FloodFill(x, y - 1);
				FloodFill(x, y + 1);
				FloodFill(x + 1, y);
				FloodFill(x - 1, y);
			}
		} catch (AWTException e1) {
			e1.printStackTrace();
		}

	}

	/**
	 * 实现扫描线转换填充矩形的功能
	 */
	public void FillRect(int x0, int y0, int x1, int y1) {
		int ymin, ymax, xmin, xmax;
		xmin = Math.min(x0, x1);
		xmax = Math.max(x0, x1);
		ymin = Math.min(y0, y1);
		ymax = Math.max(y0, y1);
		for (int y = ymin; y <= ymax; y++) {
			drawBLine(xmin, y, xmax, y);
		}
	}

	/**
	 * 橡皮擦
	 */
	public void Clear(int x0, int y0, int x1, int y1) {
		g.setColor(Color.white);
		FillRect(x0, y0, x1, y1);
		g.setColor(color);
	}

	/**
	 * 把线擦除
	 */
	public void ClearLine(int x0, int y0, int x1, int y1) {
		g.setColor(Color.white);
		drawBLine(x0, y0, x1, y1);
		g.setColor(color);
	}

	/**
	 * 实现绘制圆角矩形的功能
	 */
	public void drawRoundRect(int x0, int y0, int x1, int y1) {
		int x, y, w, h, a;
		w = Math.abs(x0 - x1);
		h = Math.abs(y0 - y1);
		a = 15;
		if (x0 < x1 && y0 > y1) {
			x = x0;
			y = y0;
		} else if (x0 > x1 && y0 < y1) {
			x = x1;
			y = y1;
		} else if (x0 < x1 && y0 < y1) {
			x = x0;
			y = y1;
		} else {
			x = x1;
			y = y0;
		}
		Graphics2D graphics2 = (Graphics2D) g;
		RoundRectangle2D roundedRectangle = new RoundRectangle2D.Float(x, y, w, h, a, a);
		graphics2.draw(roundedRectangle);
	}

	public void setG(Graphics g) {
		this.g = g;
	}

	// 获取当前颜色按钮
	public void setNowColor(JButton nowColor) {
		this.nowColor = nowColor;
		color = nowColor.getBackground();
	}

	@Override
	// 鼠标拖动的方法
	public void mouseDragged(MouseEvent e) {
		// 画曲线的方法
		if ("曲线".equals(str) || "铅笔".equals(str)) {
			int x, y;
			x = e.getX();
			y = e.getY();
			drawBLine(x, y, x1, y1);
			x1 = x;
			y1 = y;
		}
		if ("刷子".equals(str)) {
			((Graphics2D) g).setStroke(new BasicStroke(10));// 设置画笔 粗细
			int x, y;
			x = e.getX();
			y = e.getY();
			drawBLine(x, y, x1, y1);
			x1 = x;
			y1 = y;
			((Graphics2D) g).setStroke(new BasicStroke(1));// 设置画笔 粗细
		}
	}

	@Override
	// 鼠标移动方法
	public void mouseMoved(MouseEvent e) {

	}

	@Override
	// 鼠标单击方法
	public void mouseClicked(MouseEvent e) {
		// 多边形图形双击封闭
		int count = e.getClickCount();
		if (count == 2 && "多边形".equals(str)) {
			drawBLine(point[num - 1][0], point[num - 1][1], point[0][0], point[0][1]);
			num = 0;
			System.out.println("多边形封闭");
		}
		if (count == 2 && "多边形填充".equals(str)) {
			drawBLine(point[num - 1][0], point[num - 1][1], point[0][0], point[0][1]);
			FillPolygon(point);
			System.out.println("填充多边形结束");
			num = 0;
		}
		if (count == 2 && "扫描线填充多边形".equals(str)) {
			drawBLine(point[num - 1][0], point[num - 1][1], point[0][0], point[0][1]);
			FillPolygon(point);
			System.out.println("扫描线填充多边形结束");
			num = 0;
		}
		if (count == 2 && "多边形内裁剪".equals(str)) {
			if (flag == 1) {
				drawDotLine(point[num - 1][0], point[num - 1][1], point[0][0], point[0][1]);
				NodePointA = new Point[num - 1];
				for (int i = 0; i < num - 1; i++) {
					NodePointA[i] = new Point(point[i][0], point[i][1]);
				}
				System.out.println("绘制主多边形结束");
				num = 0;
				flag++;
			} else if (flag == 2) {
				drawDotLine(point[num - 1][0], point[num - 1][1], point[0][0], point[0][1]);
				NodePointB = new Point[num - 1];
				for (int i = 0; i < num - 1; i++) {
					NodePointB[i] = new Point(point[i][0], point[i][1]);
				}
				System.out.println("绘制裁剪多边形结束");
//				g.setColor(Color.WHITE);
//				for (int ii = 0; ii < NodePointA.length - 1; ii++) {
//					drawBLine(NodePointA[ii].x, NodePointA[ii].y, NodePointA[ii + 1].x, NodePointA[ii + 1].y);
//				}
//				drawBLine(NodePointA[NodePointA.length - 1].x, NodePointA[NodePointA.length - 1].y, NodePointA[0].x,
//						NodePointA[0].y);
//				for (int ii = 0; ii < NodePointB.length - 1; ii++) {
//					drawBLine(NodePointB[ii].x, NodePointB[ii].y, NodePointB[ii + 1].x, NodePointB[ii + 1].y);
//				}
//				drawBLine(NodePointB[NodePointB.length - 1].x, NodePointB[NodePointB.length - 1].y, NodePointB[0].x,
//						NodePointB[0].y);
//				g.setColor(color);
				g.setColor(Color.RED);
				PolygonCut cut=new PolygonCut();
				DoubleLinkedList res[]=cut.getDList(NodePointA,NodePointB);
				DoubleLinkedList res1[]=cut.getInternalPaint(res[0], res[1], NodePointB);	
				for(int i=0;i<cut.res_length;i++){
					
					Node pp=res1[i].beginMarker.next;
					while(pp.next.data!=null)
					{
						g.drawLine(pp.data.x, pp.data.y,pp.next.data.x, pp.next.data.y);
						pp=pp.next;
					}
			
				}
				g.setColor(color);
				num = 0;
				flag = 1;
			}

		}
		if (count == 2 && "多边形外裁剪".equals(str)) {
			if (flag == 1) {
				drawDotLine(point[num - 1][0], point[num - 1][1], point[0][0], point[0][1]);
				NodePointA = new Point[num - 1];
				for (int i = 0; i < num - 1; i++) {
					NodePointA[i] = new Point(point[i][0], point[i][1]);
				}
				System.out.println("绘制主多边形结束");
				num = 0;
				flag++;
			} else if (flag == 2) {
				drawDotLine(point[num - 1][0], point[num - 1][1], point[0][0], point[0][1]);
				NodePointB = new Point[num - 1];
				for (int i = 0; i < num - 1; i++) {
					NodePointB[i] = new Point(point[i][0], point[i][1]);
				}
				System.out.println("绘制裁剪多边形结束");
//				g.setColor(Color.WHITE);
//				for (int ii = 0; ii < NodePointA.length - 1; ii++) {
//					drawBLine(NodePointA[ii].x, NodePointA[ii].y, NodePointA[ii + 1].x, NodePointA[ii + 1].y);
//				}
//				drawBLine(NodePointA[NodePointA.length - 1].x, NodePointA[NodePointA.length - 1].y, NodePointA[0].x,
//						NodePointA[0].y);
//				for (int ii = 0; ii < NodePointB.length - 1; ii++) {
//					drawBLine(NodePointB[ii].x, NodePointB[ii].y, NodePointB[ii + 1].x, NodePointB[ii + 1].y);
//				}
//				drawBLine(NodePointB[NodePointB.length - 1].x, NodePointB[NodePointB.length - 1].y, NodePointB[0].x,
//						NodePointB[0].y);
//				g.setColor(color);
				g.setColor(Color.RED);
				PolygonCut cut=new PolygonCut();
				DoubleLinkedList res[]=cut.getDList(NodePointA,NodePointB);
				DoubleLinkedList res1[]=cut.getExternalPaint(res[0], res[1], NodePointB);	
				for(int i=0;i<cut.res_length;i++){
					Node pp=res1[i].beginMarker.next;
					while(pp.next.data!=null)
					{
						g.drawLine(pp.data.x, pp.data.y,pp.next.data.x, pp.next.data.y);
						pp=pp.next;
					}
				}
				g.setColor(color);
				num = 0;
				flag = 1;
			}

		}
	}

	@Override
	// 鼠标按下方法
	public void mousePressed(MouseEvent e) {

		g.setColor(color);// 改变画笔的颜色

		x1 = e.getX();// 获取按下时鼠标的x坐标
		y1 = e.getY();// 获取按下时鼠标的y坐标

		// 判断选择的是左面板中的那个按钮被选中（前面已经设置每个按钮的名称了）
		ButtonModel bm = bg.getSelection();// 拿到按钮组中被选中的按钮
		if (str == null) {
			str = bm.getActionCommand();// 拿到选中按钮的名字
		}
		// 实现菜单栏和pane1之间的同步变化
		if (str != bm.getActionCommand()) {
			Enumeration<AbstractButton> enu = bg.getElements();
			while (enu.hasMoreElements()) {
				AbstractButton Button = enu.nextElement();
				if (Button.getName() == str) {
					Button.setSelected(true);
					break;
				}
			}
		}
		if ("多边形".equals(str) && num == 0) {
			point[num][0] = x1;
			point[num][1] = y1;
			num++;
		}
		if ("多边形填充".equals(str) && num == 0) {
			point[num][0] = x1;
			point[num][1] = y1;
			num++;
		}
		if ("扫描线填充多边形".equals(str) && num == 0) {
			point[num][0] = x1;
			point[num][1] = y1;
			num++;
		}
		if ("多边形外裁剪".equals(str) && num == 0) {
			point[num][0] = x1;
			point[num][1] = y1;
			num++;
		}
		if ("多边形内裁剪".equals(str) && num == 0) {
			point[num][0] = x1;
			point[num][1] = y1;
			num++;
		}
		if ("清空".equals(str)) {
			// 判断选择的是左面板中的那个按钮被选中（前面已经设置每个按钮的名称了）
			str = bm.getActionCommand();// 拿到选中按钮的名字
		}
	}

	@Override
	// 鼠标释放方法
	public void mouseReleased(MouseEvent e) {
		x2 = e.getX();// 获取释放时鼠标的x坐标
		y2 = e.getY();// 获取释放时鼠标的y坐标
		// 画直线的方法
		if ("直线".equals(str)) {
			drawBLine(x1, y1, x2, y2);
		}
		if ("反混淆绘制直线".equals(str)) {
			drawFanLine(x1, y1, x2, y2);
		}
		if ("虚线".equals(str)) {
			drawDotLine(x1, y1, x2, y2);

		}
		if ("点划线".equals(str)) {
			drawDashLine(x1, y1, x2, y2);
		}
		if ("短划线".equals(str)) {
			drawBrokenLine(x1, y1, x2, y2);
		}
		if ("椭圆".equals(str)) {

			// drawCicle(x1, y1, x2, y2);
			drawOval(x1, y1, x2, y2);
			// g.drawOval(x1, y1, x2, y2);
		}
		if ("矩形".equals(str)) {

			drawRect(x1, y1, x2, y2);
		}
		if ("矩形填充".equals(str)) {

			FillRect(x1, y1, x2, y2);
		}
		if ("圆角矩形".equals(str)) {
			drawRoundRect(x1, y1, x2, y2);
		}
		if ("多边形".equals(str)) {
			point[num][0] = x2;
			point[num][1] = y2;
			drawBLine(point[num - 1][0], point[num - 1][1], point[num][0], point[num][1]);
			num++;
		}
		if ("多边形内裁剪".equals(str)) {
			point[num][0] = x2;
			point[num][1] = y2;
			drawDotLine(point[num - 1][0], point[num - 1][1], point[num][0], point[num][1]);
			num++;
		}
		if ("多边形外裁剪".equals(str)) {
			point[num][0] = x2;
			point[num][1] = y2;
			drawDotLine(point[num - 1][0], point[num - 1][1], point[num][0], point[num][1]);
			num++;
		}
		if ("多边形填充".equals(str)) {
			point[num][0] = x2;
			point[num][1] = y2;
			drawBLine(point[num - 1][0], point[num - 1][1], point[num][0], point[num][1]);
			num++;
		}
		if ("扫描线填充多边形".equals(str)) {
			point[num][0] = x2;
			point[num][1] = y2;
			drawBLine(point[num - 1][0], point[num - 1][1], point[num][0], point[num][1]);
			num++;
		}
		if ("种子填充算法填充圆".equals(str)) {
			int r = (int) Math.sqrt(Math.pow((x2 - x1), 2) + Math.pow((y2 - y1), 2));
			Ellipse2D.Double circle = new Ellipse2D.Double((x1 - r), (y1 - r), 2 * r, 2 * r);
			Graphics2D g2d = (Graphics2D) g;
			Paint p = g2d.getPaint();
			g2d.setPaint(color);
			g2d.fill(circle);
			g2d.setPaint(p);
			System.out.println("种子填充填充圆完成");
		}
		if ("椭圆填充".equals(str)) {
			int a = Math.abs(x2 - x1);
			int b = Math.abs(y2 - y1);
			Ellipse2D.Double circle = new Ellipse2D.Double(x1, y1, a, b);
			Graphics2D g2d = (Graphics2D) g;
			Paint p = g2d.getPaint();
			g2d.setPaint(color);
			g2d.fill(circle);
			g2d.setPaint(p);
		}
		if ("取色器".equals(str)) {
			// 拿到相对面板的那个坐标
			int x = e.getXOnScreen();
			int y = e.getYOnScreen();

			try {

				Robot robot = new Robot();// Robot类的使用

				// 拿到坐标点的那个矩形
				Rectangle rect = new Rectangle(x, y, 1, 1);
				// 生成该矩形的缓冲图片
				BufferedImage bi = robot.createScreenCapture(rect);
				// 得到图片的背景颜色
				int c = bi.getRGB(0, 0);
				// 将该颜色进行封装
				color = new Color(c);
				// 将取色笔取来的图片设置成画笔的颜色
				nowColor.setBackground(color);
			} catch (AWTException e1) {
				e1.printStackTrace();
			}
		}
		if ("喷桶".equals(str)) {
			int x, y;
			for (int i = 0; i < 200; i++) {
				x = r.nextInt(25) - 12;
				y = r.nextInt(25) - 12;
				if (x * x + y * y > 121)
					continue;// 如果生成的点不在以点击的位置为圆心以11为半径的圆内那么直接跳过这个点
				g.drawLine(x2 + x, y2 + y, x2 + x, y2 + y);
			}
		}
		if ("选定".equals(str)) {
			drawDash();
		}
		if ("文字".equals(str)) {
			g.drawString("请输入文字", x1, y1);
		}
		if ("橡皮擦".equals(str)) {
			Clear(x1, y1, x2, y2);
		}

	}

	@Override
	// 鼠标进入方法
	public void mouseEntered(MouseEvent e) {

	}

	@Override
	// 鼠标退出方法
	public void mouseExited(MouseEvent e) {

	}

	@Override
	// 处理按钮上的鼠标点击动作
	public void actionPerformed(ActionEvent e) {
		System.out.println(e.getActionCommand() + "按钮被选中");
		// file
		if ("重启".equals(e.getActionCommand())) {

			Runtime.getRuntime().addShutdownHook(new Thread() {
				public void run() {
					try {
						Runtime.getRuntime().exec("java -classpath bin HUATUBAN.Start");
						System.out.println("重启结束");

					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}

				}
			});
			System.out.println("重启中...");
			System.exit(0);
		}
		if ("退出".equals(e.getActionCommand())) {

			System.exit(0);
		}
		// edit
		if ("清空".equals(e.getActionCommand()) || "关闭".equals(e.getActionCommand())) {
			Clear(0, 0, 800, 600);
			flag=1;
			num=0;
		}

		// 判断是颜色按钮还是图形按钮
		if ("".equals(e.getActionCommand())) {
			JButton jb = (JButton) e.getSource();
			color = jb.getBackground();
			nowColor.setBackground(color);// 处理当前颜色
		}
		// file
		else if ("新建".equals(e.getActionCommand())) {

		} else if ("关闭".equals(e.getActionCommand())) {

		} else if ("打开".equals(e.getActionCommand())) {
			JFileChooser fileChooser = new JFileChooser(".");
			FileNameExtensionFilter filter = new FileNameExtensionFilter("JPEG(*.jpg;*.jpeg;*.jpe;*.jfif;)", "jpg",
					"jpeg", "jpe", "jfif");
			FileNameExtensionFilter filter1 = new FileNameExtensionFilter("GIF(*.gif)", "gif");
			FileNameExtensionFilter filter2 = new FileNameExtensionFilter("PNG(*.png)", "png");
			FileNameExtensionFilter filter3 = new FileNameExtensionFilter("ICO(*.ico)", "ico");
			FileNameExtensionFilter filter4 = new FileNameExtensionFilter("TIFF(*.tiff)", "tiff");
			FileNameExtensionFilter filter5 = new FileNameExtensionFilter("位图文件(*.bmp;*.dib)", "bmp", "dib");
			fileChooser.addChoosableFileFilter(filter);
			fileChooser.addChoosableFileFilter(filter1);
			fileChooser.addChoosableFileFilter(filter2);
			fileChooser.addChoosableFileFilter(filter3);
			fileChooser.addChoosableFileFilter(filter4);
			fileChooser.addChoosableFileFilter(filter5);
			int n = fileChooser.showOpenDialog(fileChooser);
			if (n == fileChooser.APPROVE_OPTION) {
				ImageIcon icon = new ImageIcon(fileChooser.getSelectedFile().getPath());
				Image backgroundImage = icon.getImage();
				g.drawImage(backgroundImage, 0, 0, null);
			}

		} else if ("保存".equals(e.getActionCommand())) {

		} else if ("另存为".equals(e.getActionCommand())) {

		}
		// color
		else if ("选择喜欢的颜色".equals(e.getActionCommand())) {
			JColorChooser colorChooser = new JColorChooser();
			Color ctemp = colorChooser.showDialog(null, "请选择你喜欢的颜色", color);
			if (ctemp != null) {
				color = ctemp;
				nowColor.setBackground(color);

			}

		} else if ("欢迎界面".equals(e.getActionCommand())) {
			JDialog dlg = new JDialog();
			dlg.setSize(new Dimension(600, 460));

			dlg.setTitle("欢迎界面");
			dlg.setLocationRelativeTo(null);
			dlg.setVisible(true);
			String str = "";
			try {

				File file = new File(ClassLoader.getSystemResource("welcome.txt").getPath());// 定义一个file对象，用来初始化FileReader
				FileReader reader = new FileReader(file);// 定义一个fileReader对象，用来初始化BufferedReader
				BufferedReader bReader = new BufferedReader(reader);// new一个BufferedReader对象，将文件内容读取到缓存
				StringBuilder sb = new StringBuilder();// 定义一个字符串缓存，将字符串存放缓存中
				String s = "";
				while ((s = bReader.readLine()) != null) {// 逐行读取文件内容，不读取换行符和末尾的空格
					sb.append(s + "\n");// 将读取的字符串添加换行符后累加存放在缓存中
				}
				bReader.close();
				str = sb.toString();
			} catch (Exception e1) {
				e1.printStackTrace();
			}
			JTextArea textarea = new JTextArea(str);
			textarea.setLineWrap(true);
			dlg.add(textarea);
		} else if ("帮助文档".equals(e.getActionCommand())) {
			File file = new File(ClassLoader.getSystemResource("doc/index.html").getPath());
			String webSite = System.getProperty("user.dir") + "/doc/index.html";
			webSite = "https://www.baidu.com/";
			try {
				Desktop desktop = Desktop.getDesktop();
				if (desktop.isDesktopSupported() && desktop.isSupported(Desktop.Action.BROWSE)) {
					URI uri = new URI(webSite);
					uri = file.toURI();
					desktop.browse(uri);
				}
			} catch (IOException ex) {
				ex.printStackTrace();
			} catch (URISyntaxException ex) {
				ex.printStackTrace();
			}
		} else if ("联系我们".equals(e.getActionCommand())) {
			File file = new File(ClassLoader.getSystemResource("contact.html").getPath());
			String webSite = "https://www.baidu.com/";
			try {
				Desktop desktop = Desktop.getDesktop();
				if (desktop.isDesktopSupported() && desktop.isSupported(Desktop.Action.BROWSE)) {
					URI uri = new URI(webSite);
					uri = file.toURI();
					desktop.browse(uri);
				}
			} catch (IOException ex) {
				ex.printStackTrace();
			} catch (URISyntaxException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		} else if ("关于画图".equals(e.getActionCommand())) {
			JDialog dlg = new JDialog();
			dlg.setSize(new Dimension(400, 400));

			dlg.setTitle("关于画图");
			dlg.setLocationRelativeTo(null);
			dlg.setVisible(true);
			String str = "";
			try {
				File file = new File(ClassLoader.getSystemResource("aboutpainting.txt").getPath());// 定义一个file对象，用来初始化FileReader
				FileReader reader = new FileReader(file);// 定义一个fileReader对象，用来初始化BufferedReader
				BufferedReader bReader = new BufferedReader(reader);// new一个BufferedReader对象，将文件内容读取到缓存
				StringBuilder sb = new StringBuilder();// 定义一个字符串缓存，将字符串存放缓存中
				String s = "";
				while ((s = bReader.readLine()) != null) {// 逐行读取文件内容，不读取换行符和末尾的空格
					sb.append(s + "\n");// 将读取的字符串添加换行符后累加存放在缓存中
				}
				bReader.close();
				str = sb.toString();
			} catch (Exception e1) {
				e1.printStackTrace();
			}
			JTextArea textarea = new JTextArea(str);
			textarea.setLineWrap(true);
			dlg.add(textarea);
		} else if ("白色".equals(e.getActionCommand())) {
			color = Color.white;
			nowColor.setBackground(color);
		} else if ("红色".equals(e.getActionCommand())) {
			color = Color.red;
			nowColor.setBackground(color);
		} else if ("粉红色".equals(e.getActionCommand())) {
			color = Color.pink;
			nowColor.setBackground(color);
		} else if ("橘色".equals(e.getActionCommand())) {
			color = Color.orange;
			nowColor.setBackground(color);
		} else if ("紫红色".equals(e.getActionCommand())) {
			color = Color.magenta;
			nowColor.setBackground(color);
		} else if ("黄色".equals(e.getActionCommand())) {
			color = Color.yellow;
			nowColor.setBackground(color);
		} else if ("绿色".equals(e.getActionCommand())) {
			color = Color.green;
			nowColor.setBackground(color);
		} else if ("蓝绿色".equals(e.getActionCommand())) {
			color = Color.cyan;
			nowColor.setBackground(color);
		} else if ("蓝色".equals(e.getActionCommand())) {
			color = Color.blue;
			nowColor.setBackground(color);
		} else if ("浅灰色".equals(e.getActionCommand())) {
			color = Color.lightGray;
			nowColor.setBackground(color);
		} else if ("灰色".equals(e.getActionCommand())) {
			color = Color.gray;
			nowColor.setBackground(color);
		} else if ("深灰色".equals(e.getActionCommand())) {
			color = Color.darkGray;
			nowColor.setBackground(color);
		} else if ("黑色".equals(e.getActionCommand())) {
			color = Color.black;
			nowColor.setBackground(color);
		} else if ("很细".equals(e.getActionCommand())) {
			((Graphics2D) g).setStroke(new BasicStroke(1));// 设置画笔 粗细
		} else if ("细".equals(e.getActionCommand())) {
			((Graphics2D) g).setStroke(new BasicStroke(5));// 设置画笔 粗细
		} else if ("一般".equals(e.getActionCommand())) {
			((Graphics2D) g).setStroke(new BasicStroke(8));// 设置画笔 粗细
		} else if ("粗".equals(e.getActionCommand())) {
			((Graphics2D) g).setStroke(new BasicStroke(10));// 设置画笔 粗细
		} else if ("较粗".equals(e.getActionCommand())) {
			((Graphics2D) g).setStroke(new BasicStroke(13));// 设置画笔 粗细
		} else if ("非常粗".equals(e.getActionCommand())) {
			((Graphics2D) g).setStroke(new BasicStroke(15));// 设置画笔 粗细
		} else {
			str = e.getActionCommand();
			if (str == "多边形") {
				num = 0;
			}
			if (str == "多边形填充") {
				num = 0;
			}
			if (str == "多边形裁剪") {
				num = 0;
			}
		}

	}
}
