package HUATUBAN;

import java.awt.AWTException;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.Rectangle;
import java.awt.Robot;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;

import javax.imageio.ImageIO;
import javax.swing.ButtonGroup;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.border.BevelBorder;
import javax.swing.filechooser.FileNameExtensionFilter;

/**
 * Draw类，用于界面的初始化和部分动作的监听和处理
 * 
 * @author Yaomz
 * @version 1.0
 */
@SuppressWarnings("serial")
public class Draw extends JFrame {
	private DrawListener dl;
	private Graphics g;
	private String str;
	private JPanel jp1 = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
	public JPanel jp2 = new JPanel();// 画布面板
	private JPanel jp3 = new JPanel();
	public void SeedFill(int x,int y,Graphics g,Color c){    
        Robot rbt = null;
        try {rbt = new Robot();} 
        catch (AWTException e) {e.printStackTrace();}
        java.awt.Point point = getLocationOnScreen();
        BufferedImage image = rbt.createScreenCapture(new Rectangle(point.x, point.y, getWidth(), getHeight()));
        int rgb = image.getRGB(x,y);
        Color bgcolor = new Color(rgb);
        
        if(bgcolor.getRGB()!=c.getRGB()){
            g.fillOval(x,y,1,1);
            SeedFill(x+1,y,g,c);
            SeedFill(x-1,y,g,c);
            SeedFill(x,y+1,g,c);
            SeedFill(x,y-1,g,c);
        }
        
    }
	// 界面初始化方法
	public void showUI() {
		setTitle("画图");// 窗体名称
		setSize(885, 700);// 窗体大小
		setDefaultCloseOperation(3);
		setLocationRelativeTo(null);// 窗体居中
		// 流式布局左对齐
		FlowLayout layout = new FlowLayout(FlowLayout.LEFT);
		setLayout(layout);// 窗体使用流式布局管理器
		this.setResizable(false);// 窗体大小不变

		// 菜单栏
		// file #1
		JMenuItem FileNew = new JMenuItem("新建(N)");
		FileNew.setActionCommand("新建");
		JMenuItem FileOpen = new JMenuItem("打开(O)");
		FileOpen.setActionCommand("打开");
		JMenuItem FileClose = new JMenuItem("关闭(C)");
		FileClose.setActionCommand("关闭");
		JMenuItem FileSave = new JMenuItem("保存(S)");
		FileSave.setActionCommand("保存");
		JMenuItem FileSaveAs = new JMenuItem("另存为(A)");
		FileSaveAs.setActionCommand("另存为");
		JMenuItem FileSaveAll = new JMenuItem("保存全部");
		FileSaveAll.setEnabled(false);
		JMenuItem FileRestart = new JMenuItem("重启(R)");
		FileRestart.setActionCommand("重启");
		JMenuItem FileExit = new JMenuItem("退出(E)");
		FileExit.setActionCommand("退出");
		// edit #2
		JMenuItem EditDelete = new JMenuItem("清空");
		EditDelete.setActionCommand("清空");
		JMenuItem EditRedo = new JMenuItem("前进");
		JMenuItem EditUndoTyping = new JMenuItem("后退");
		JMenuItem EditCut = new JMenuItem("剪切");
		JMenuItem EditPaste = new JMenuItem("粘贴");
		JMenuItem EditFind = new JMenuItem("查找/替换");
		JMenuItem EditSelectAll = new JMenuItem("全选");

		// color #3
		JMenuItem ColorWhite = new JMenuItem("白色");
		ColorWhite.setActionCommand("白色");
		JMenuItem ColorRed = new JMenuItem("红色");
		ColorRed.setActionCommand("红色");
		JMenuItem ColorPink = new JMenuItem("粉红色");
		ColorPink.setActionCommand("粉红色");
		JMenuItem ColorOrange = new JMenuItem("橘色");
		ColorOrange.setActionCommand("橘色");
		JMenuItem ColorMagenta = new JMenuItem("紫红色");
		ColorMagenta.setActionCommand("紫红色");
		JMenuItem ColorYellow = new JMenuItem("黄色");
		ColorYellow.setActionCommand("黄色");
		JMenuItem ColorGreen = new JMenuItem("绿色");
		ColorGreen.setActionCommand("绿色");
		JMenuItem ColorCyan = new JMenuItem("蓝绿色");
		ColorCyan.setActionCommand("蓝绿色");
		JMenuItem ColorBlue = new JMenuItem("蓝色");
		ColorBlue.setActionCommand("蓝色");
		JMenuItem ColorLightgray = new JMenuItem("浅灰色");
		ColorLightgray.setActionCommand("浅灰色");
		JMenuItem ColorGray = new JMenuItem("灰色");
		ColorGray.setActionCommand("灰色");
		JMenuItem ColorDarkgray = new JMenuItem("深灰色");
		ColorDarkgray.setActionCommand("深灰色");
		JMenuItem ColorBlack = new JMenuItem("黑色");
		ColorBlack.setActionCommand("黑色");
		JMenuItem ColorChooser = new JMenuItem("选择喜欢的颜色");
		ColorBlack.setActionCommand("选择喜欢的颜色");
		

		// line #4
		JMenuItem LineSmall = new JMenuItem("很细");// 很细
		LineSmall.setActionCommand("很细");
		JMenuItem LineThin = new JMenuItem("细");// 细
		LineThin.setActionCommand("细");
		JMenuItem LineGeneral = new JMenuItem("一般");// 一般
		LineGeneral.setActionCommand("一般");
		JMenuItem LineThick = new JMenuItem("粗");// 粗
		LineThick.setActionCommand("粗");
		JMenuItem LineCoarse = new JMenuItem("较粗");// 较粗
		LineCoarse.setActionCommand("较粗");
		JMenuItem LineCrude = new JMenuItem("非常粗");// 非常粗
		LineCrude.setActionCommand("非常粗");

		// format #5
		JMenuItem FormatStraightline = new JMenuItem("直线");// 直线
		FormatStraightline.setActionCommand("直线");
		JMenuItem FormatDottedline = new JMenuItem("虚线");// 虚线
		FormatDottedline.setActionCommand("虚线");
		JMenuItem FormatDashedline = new JMenuItem("点划线");// 点划线
		FormatDashedline.setActionCommand("点划线");
		JMenuItem FormatBrokenline = new JMenuItem("短划线");// 折线
		FormatBrokenline.setActionCommand("短划线");
		JMenuItem FormatCurve = new JMenuItem("曲线");// 曲线
		FormatCurve.setActionCommand("曲线");

		JMenuItem FormatRectangle1 = new JMenuItem("矩形");// 正方形/矩形
		FormatRectangle1.setActionCommand("矩形");
		JMenuItem FormatRectangle2 = new JMenuItem("矩形填充");// 正方形/矩形 有边框 填充
		FormatRectangle2.setActionCommand("矩形填充");

		JMenuItem FormatPolygon1 = new JMenuItem("多边形");// 多边形
		FormatPolygon1.setActionCommand("多边形");
		JMenuItem FormatPolygon2 = new JMenuItem("多边形填充");// 多边形 有边框 填充
		FormatPolygon2.setActionCommand("多边形填充");

		JMenuItem FormatEllipse1 = new JMenuItem("椭圆");// 椭圆/圆
		FormatEllipse1.setActionCommand("椭圆");
		JMenuItem FormatEllipse2 = new JMenuItem("椭圆填充");// 椭圆/圆 有边框 填充
		FormatEllipse2.setActionCommand("椭圆填充");

		JMenuItem FormatRoundedRec1 = new JMenuItem("圆角矩形");// 圆角矩形
		FormatRoundedRec1.setActionCommand("圆角矩形");
		JMenuItem FormatRoundedRec2 = new JMenuItem("圆角矩形填充");// 圆角矩形 有边框 填充
		FormatRoundedRec2.setActionCommand("圆角矩形填充");

		// tools #6
		JMenuItem ToolWord1 = new JMenuItem("字体");// 字体
		JMenuItem ToolWord2 = new JMenuItem("加粗");// 加粗
		JMenuItem ToolWord3 = new JMenuItem("大小");// 大小
		JMenuItem ToolPipeta = new JMenuItem("取色器");// 取色器
		ToolPipeta.setActionCommand("取色器");
		JMenuItem ToolMagnifier = new JMenuItem("放大镜");// 放大镜
		ToolMagnifier.setActionCommand("放大镜");
		JMenuItem ToolEraser = new JMenuItem("橡皮擦");// 橡皮擦
		ToolEraser.setActionCommand("橡皮擦");

		// help #7
		JMenuItem HelpWelcome = new JMenuItem("欢迎界面");
		HelpWelcome.setActionCommand("欢迎界面");
		JMenuItem HelpContents = new JMenuItem("帮助文档");
		HelpContents.setActionCommand("帮助文档");
		JMenuItem HelpContact = new JMenuItem("联系我们");
		HelpContact.setActionCommand("联系我们");
		JMenuItem HelpAbout = new JMenuItem("关于画图");
		HelpAbout.setActionCommand("关于画图");
		
		// fan #8
		JMenuItem FanLine = new JMenuItem("反混淆绘制直线");
		FanLine.setActionCommand("反混淆绘制直线");
		

		// fill #9
		JMenuItem TianCircle = new JMenuItem("种子填充圆");
		TianCircle.setActionCommand("种子填充算法填充圆");
		JMenuItem TianPolygon = new JMenuItem("扫描线填充多边形");
		TianPolygon.setActionCommand("扫描线填充多边形");
		
		// cut #10
		JMenuItem CutInterPoloygn = new JMenuItem("多边形内裁剪");
		CutInterPoloygn.setActionCommand("多边形内裁剪");
		JMenuItem CutExterPoloygn = new JMenuItem("多边形外裁剪");
		CutExterPoloygn.setActionCommand("多边形外裁剪");
		
		// transfer#11
		JMenuItem Transfer2D = new JMenuItem("2D图形变换");
		Transfer2D.setActionCommand("2D图形变换");
		JMenuItem Transfer3D = new JMenuItem("3D图形变换");
		Transfer3D.setActionCommand("3D图形变换");
		
		
		JMenu file = new JMenu("文件(F)");
		file.add(FileNew);
		file.add(FileOpen);
		file.addSeparator();
		file.add(FileClose);
		file.addSeparator();
		file.add(FileSave);
		file.add(FileSaveAs);
		file.add(FileSaveAll);
		FileSaveAll.setEnabled(false);
		file.addSeparator();
		file.add(FileRestart);
		file.add(FileExit);
		FileExit.setEnabled(true);

		JMenu edit = new JMenu("编辑(E)");
		edit.add(EditDelete);
		edit.addSeparator();
		edit.add(EditRedo);
		EditRedo.setEnabled(false);
		edit.add(EditUndoTyping);
		EditUndoTyping.setEnabled(false);
		edit.addSeparator();
		edit.add(EditCut);
		edit.add(EditPaste);
		edit.addSeparator();
		edit.add(EditSelectAll);
		edit.addSeparator();
		edit.add(EditFind);

		JMenu color = new JMenu("颜色(C)");
		color.add(ColorWhite);
		color.add(ColorRed);
		color.add(ColorPink);
		color.add(ColorOrange);
		color.add(ColorMagenta);
		color.add(ColorYellow);
		color.add(ColorGreen);
		color.add(ColorCyan);
		color.add(ColorBlue);
		color.add(ColorLightgray);
		color.add(ColorGray);
		color.add(ColorDarkgray);
		color.add(ColorBlack);
		color.addSeparator();
		color.add(ColorChooser);

		JMenu line = new JMenu("线条(L)");
		line.add(LineSmall);
		line.add(LineThin);
		line.add(LineGeneral);
		line.add(LineThick);
		line.add(LineCoarse);
		line.add(LineCrude);

		JMenu format = new JMenu("形状(S)");
		format.add(FormatStraightline);
		format.add(FormatDottedline);
		format.add(FormatDashedline);
		format.addSeparator();
		JMenu FormatRectangle = new JMenu("矩形");
		FormatRectangle.add(FormatRectangle1);
		FormatRectangle.add(FormatRectangle2);

		format.add(FormatRectangle);
		JMenu FormatPolygon = new JMenu("多边形");
		FormatPolygon.add(FormatPolygon1);
		FormatPolygon.add(FormatPolygon2);

		format.add(FormatPolygon);
		JMenu FormatEllipse = new JMenu("椭圆");
		FormatEllipse.add(FormatEllipse1);
		FormatEllipse.add(FormatEllipse2);

		format.add(FormatEllipse);
		JMenu FormatRoundedRec = new JMenu("圆角矩形");
		FormatRoundedRec.add(FormatRoundedRec1);
		FormatRoundedRec.add(FormatRoundedRec2);

		format.add(FormatRoundedRec);
		format.addSeparator();
		format.add(FormatBrokenline);
		format.add(FormatCurve);

		JMenu tool = new JMenu("工具(T)");
		JMenu word = new JMenu("文字");
		word.add(ToolWord1);
		word.add(ToolWord2);
		word.add(ToolWord3);
		tool.add(word);
		tool.add(ToolPipeta);
		tool.add(ToolMagnifier);
		ToolMagnifier.setEnabled(false);// 功能未实现
		tool.add(ToolEraser);

		JMenu help = new JMenu("帮助(H)");
		help.add(HelpWelcome);
		help.addSeparator();
		help.add(HelpContents);
		help.addSeparator();
		help.add(HelpContact);
		help.addSeparator();
		help.add(HelpAbout);
		
		JMenu fan = new JMenu("反混淆(FL)");
		fan.add(FanLine);
		
		JMenu tian = new JMenu("填充(TI)");
		tian.add(TianPolygon);
		tian.add(TianCircle);
		
		JMenu cut = new JMenu("裁剪(CU)");
		cut.add(CutInterPoloygn);
		cut.add(CutExterPoloygn);
		
		JMenu transfer = new JMenu("变换(Tr)");
		transfer.add(Transfer2D);
		transfer.add(Transfer3D);
		
		JMenuBar bar = new JMenuBar();
		bar.add(file);
		bar.add(edit);
		bar.add(color);
		bar.add(line);
		bar.add(format);
		bar.add(tool);
		bar.add(tian);
		bar.add(cut);
		bar.add(fan);
		bar.add(transfer);
		bar.add(help);
		bar.setBackground(new Color(235, 233, 238));
		setJMenuBar(bar);
		// 实例化DrawListener对象
		ButtonGroup bgroup = new ButtonGroup();
		dl = new DrawListener(g, bgroup);

		// 为菜单栏添加监听
		FileNew.addActionListener(dl);
		FileNew.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				 str = (String) JOptionPane.showInputDialog(null, "请输入文件名：\n", "新建画板", JOptionPane.PLAIN_MESSAGE,
						null, null, "未命名1");
				if (str != null)
					setTitle(str + "-画图");
				;

			}
		});
		FileOpen.addActionListener(dl);
		FileClose.addActionListener(dl);
		FileClose.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				setTitle("无标题-画图");

			}
		});
		FileSave.addActionListener(dl);
		FileSave.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				File file = null;
				String fileName = str;
				if(str==null)
				{
					str="未命名1";
				}
				file=new File(str);
				String[] ends = { ".jpeg" };
				JFileChooser fileChooser = new JFileChooser(".");
				fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
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
				fileChooser.setSelectedFile(file);
				int n = fileChooser.showSaveDialog(fileChooser);
				if (n == fileChooser.APPROVE_OPTION) {
					// 获得过滤器的扩展名
					FileNameExtensionFilter myfilter = (FileNameExtensionFilter) fileChooser.getFileFilter();
					ends = myfilter.getExtensions();
				}
				fileName =fileName + "." + ends[0];
				// 否则是个文件夹
				file = fileChooser.getCurrentDirectory();// 获得当前目录
				String path = file.getPath() + java.io.File.separator + fileName;
				file = new File(path);
				if (file.exists()) { // 若选择已有文件----询问是否要覆盖
					int i = JOptionPane.showConfirmDialog(fileChooser, "该文件已经存在，确定要覆盖吗？");
					if (i == JOptionPane.YES_OPTION)
						;
					else
						return;
				}
				BufferedImage image = null;
				try {
					image = new Robot().createScreenCapture(
							new Rectangle(jp2.getLocationOnScreen().x, jp2.getLocationOnScreen().y, jp2.getWidth(), jp2.getHeight()));
					ImageIO.write(image, ends[0], file);  
				} catch (AWTException e2) {
					// TODO Auto-generated catch block
					e2.printStackTrace();
					JOptionPane.showMessageDialog(fileChooser, "文件保存出错" + e2.getMessage());
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
					JOptionPane.showMessageDialog(fileChooser, "文件保存出错" + e1.getMessage());
				}

			}
		});
		FileSaveAs.addActionListener(dl);
		FileSaveAs.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				File file = null;
				String fileName = null;
				String[] ends = { ".jpeg" };
				JFileChooser fileChooser = new JFileChooser("/Users/galaxy/Desktop/");
				// fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
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
				int n = fileChooser.showSaveDialog(fileChooser);
				if (n == fileChooser.APPROVE_OPTION) {
					file = fileChooser.getSelectedFile(); // 如果这里并没有选取中任何的文件，下面的fileChooser.getName(file)将会返回手输入的文件名
					FileNameExtensionFilter myfilter = (FileNameExtensionFilter) fileChooser.getFileFilter();
					// 获得过滤器的扩展名
					ends = myfilter.getExtensions();
				}
				fileName = fileChooser.getName(file) + "." + ends[0];
				if (fileName == null || fileName.trim().length() == 0) {
					JOptionPane.showMessageDialog(fileChooser, "文件名为空！");
				}
				// 否则是个文件夹
				file = fileChooser.getCurrentDirectory();// 获得当前目录
				String path = file.getPath() + java.io.File.separator + fileName;
				file = new File(path);
				if (file.exists()) { // 若选择已有文件----询问是否要覆盖
					int i = JOptionPane.showConfirmDialog(fileChooser, "该文件已经存在，确定要覆盖吗？");
					if (i == JOptionPane.YES_OPTION)
						;
					else
						return;
				}
				BufferedImage image = null;
				try {
					image = new Robot().createScreenCapture(
							new Rectangle(jp2.getLocationOnScreen().x, jp2.getLocationOnScreen().y, jp2.getWidth(), jp2.getHeight()));
					ImageIO.write(image, ends[0], file);  
				} catch (AWTException e2) {
					// TODO Auto-generated catch block
					e2.printStackTrace();
					JOptionPane.showMessageDialog(fileChooser, "文件保存出错" + e2.getMessage());
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
					JOptionPane.showMessageDialog(fileChooser, "文件保存出错" + e1.getMessage());
				}

			}
		});
		FileRestart.addActionListener(dl);
		FileExit.addActionListener(dl);

		EditRedo.addActionListener(dl);
		EditUndoTyping.addActionListener(dl);
		EditCut.addActionListener(dl);
		EditPaste.addActionListener(dl);
		EditDelete.addActionListener(dl);
		EditFind.addActionListener(dl);
		EditSelectAll.addActionListener(dl);

		ColorWhite.addActionListener(dl);
		ColorRed.addActionListener(dl);
		ColorPink.addActionListener(dl);
		ColorOrange.addActionListener(dl);
		ColorMagenta.addActionListener(dl);
		ColorYellow.addActionListener(dl);
		ColorGreen.addActionListener(dl);
		ColorCyan.addActionListener(dl);
		ColorBlue.addActionListener(dl);
		ColorLightgray.addActionListener(dl);
		ColorGray.addActionListener(dl);
		ColorDarkgray.addActionListener(dl);
		ColorBlack.addActionListener(dl);
		ColorChooser.addActionListener(dl);

		LineSmall.addActionListener(dl);
		LineThin.addActionListener(dl);
		LineGeneral.addActionListener(dl);
		LineThick.addActionListener(dl);
		LineCoarse.addActionListener(dl);
		LineCrude.addActionListener(dl);

		FormatStraightline.addActionListener(dl);
		FormatDottedline.addActionListener(dl);
		FormatDashedline.addActionListener(dl);
		FormatBrokenline.addActionListener(dl);
		FormatCurve.addActionListener(dl);
		FormatRectangle1.addActionListener(dl);
		FormatRectangle2.addActionListener(dl);

		FormatPolygon1.addActionListener(dl);
		FormatPolygon2.addActionListener(dl);

		FormatEllipse1.addActionListener(dl);
		FormatEllipse2.addActionListener(dl);

		FormatRoundedRec1.addActionListener(dl);
		FormatRoundedRec2.addActionListener(dl);

		ToolWord1.addActionListener(dl);
		ToolWord2.addActionListener(dl);
		ToolWord3.addActionListener(dl);
		ToolPipeta.addActionListener(dl);
		ToolMagnifier.addActionListener(dl);
		ToolEraser.addActionListener(dl);

		HelpWelcome.addActionListener(dl);
		HelpContents.addActionListener(dl);
		HelpContact.addActionListener(dl);
		HelpAbout.addActionListener(dl);
		
		FanLine.addActionListener(dl);
		
		TianCircle.addActionListener(dl);
		TianPolygon.addActionListener(dl);
		
		CutInterPoloygn.addActionListener(dl);
		CutExterPoloygn.addActionListener(dl);
		
		Transfer2D.addActionListener(dl);
		Transfer2D.addActionListener(new ActionListener() {
			public void paint(Graphics g){
				Font ef=new Font("TimesRoman",Font.PLAIN,16); //创建字体
				// 设置字体
				g.setFont( ef );
				g.drawString("Hello, World!", 500, 400);
				Font cf = new Font("楷体", Font.PLAIN, 24); g.setFont( cf );
				g.drawString("你好，世界!", 500, 420);
				g.setColor( Color.RED ); // 设置绘图颜色
				g.drawRect(100,180,350,350); //画一个正方形
				g.setColor( Color.BLACK ); // 设置填充颜色 
				g.fillRect(21, 181, 149, 149); // 将正方形填充颜色
			}
			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				JFrame frame = new JFrame("2D图形变换");
				frame.setSize(800, 600);// 窗体大小
				frame.setLocationRelativeTo(null);// 窗体居中
				frame.setVisible(true);
				FlowLayout layout = new FlowLayout(FlowLayout.LEFT);// 流式布局左对齐
				frame.setLayout(layout);// 窗体使用流式布局管理器
//				frame.setResizable(false);// 窗体大小不变
				
			}
		});
		Transfer3D.addActionListener(dl);
		Transfer3D.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				JFrame frame = new JFrame("3D图形变换");
				frame.setSize(800, 600);// 窗体大小
				frame.setLocationRelativeTo(null);// 窗体居中
				frame.setVisible(true);
				FlowLayout layout = new FlowLayout(FlowLayout.LEFT);// 流式布局左对齐
				frame.setLayout(layout);// 窗体使用流式布局管理器
//				frame.setResizable(false);// 窗体大小不变
				
			}
		});

		// 使用数组保存按钮名
		String buttonName[] = { "裁剪", "选定", "橡皮擦", "填充", "取色器", "放大镜", "铅笔", "刷子", "喷桶", "文字", "直线", "曲线", "矩形", "多边形",
				"椭圆", "圆角矩形" };
		// 用于保存图形按钮，使用网格布局
		jp1.setPreferredSize(new Dimension(30, 600));
		jp1.setBackground(new Color(235, 233, 238));
		// 循环为按钮面板添加按钮
		for (int i = 0; i <= 15; i++) {

			JRadioButton jbutton = new JRadioButton();
			
			ImageIcon img1 = new ImageIcon(ClassLoader.getSystemResource("images/draw" + i + ".jpg"));
			ImageIcon img2 = new ImageIcon(ClassLoader.getSystemResource("images/draw" + i + "-1.jpg"));
			ImageIcon img3 = new ImageIcon(ClassLoader.getSystemResource("images/draw" + i + "-2.jpg"));
			ImageIcon img4 = new ImageIcon(ClassLoader.getSystemResource("images/draw" + i + "-3.jpg"));

			jbutton.setToolTipText(buttonName[i]);
			jbutton.setIcon(img1);
			jbutton.setRolloverIcon(img2);
			jbutton.setPressedIcon(img3);
			jbutton.setSelectedIcon(img4);
			jbutton.setBorder(null);
			jbutton.setPreferredSize(new Dimension(30, 30));
			// 设置默认选中的按钮
			if (i == 10) {
				jbutton.setSelected(true);
			}
			jbutton.setName(buttonName[i]);
			jbutton.setActionCommand(buttonName[i]);
			jbutton.addActionListener(dl);// 为按钮添加监听
			bgroup.add(jbutton);
			jp1.add(jbutton);
		}

		// JPanel jp2 = new JPanel();// 画布面板
		jp2.setPreferredSize(new Dimension(800, 600));
		jp2.setBackground(Color.WHITE);
	

		BevelBorder bb = new BevelBorder(1, Color.white, Color.white);
		BevelBorder bb1 = new BevelBorder(1, Color.yellow, Color.yellow);
		// JPanel jp3 = new JPanel();
		jp3.setOpaque(true);
		jp3.setLayout(new FlowLayout(FlowLayout.RIGHT, 0, 3));
		jp3.setPreferredSize(new Dimension(30, 600));

		// 颜色数组，用来设置按钮的背景颜色
		Color[] colorArray = { Color.BLACK, Color.DARK_GRAY, Color.GRAY, Color.LIGHT_GRAY, Color.RED, Color.ORANGE,
				Color.GREEN, Color.BLUE, Color.PINK, Color.CYAN, Color.MAGENTA, Color.YELLOW, Color.WHITE };
		// 循环添加13个颜色按钮
		for (int i = 0; i < colorArray.length; i++) {
			JButton button = new JButton();
			button.setBackground(colorArray[i]);
			button.setPreferredSize(new Dimension(30, 30));
			button.setBorder(bb);
			button.addActionListener(dl);
			button.setOpaque(true);
			jp3.add(button);
		}
		// 将面板添加到主窗体
		this.add(jp1);
		this.add(jp2);
		this.setJMenuBar(bar);
		this.add(jp3);

		// 添加按钮，作为当前颜色
		JButton nowColor = new JButton();
		nowColor.setPreferredSize(new Dimension(30, 30));
		nowColor.setBackground(Color.BLACK);// 默认黑色
		nowColor.setOpaque(true);
		nowColor.setBorder(bb1);
		// nowColor.setBorderPainted(false);

		jp3.add(nowColor);
		// 设置窗体的组件可见，如果为FALSE就看不到任何组件
		setVisible(true);
		// 获取画笔对象
		g = jp2.getGraphics();
		dl.setG(g);
		dl.setNowColor(nowColor);
		// 为面板添加鼠标监听，用于绘制图形
		jp2.addMouseListener(dl);
		jp2.addMouseMotionListener(dl);
	}

}
