package com.example;

import com.example.http.HttpURLConnectionExample;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.WindowConstants;

/**
 * com.example.Main
 */
public class Main {

    public static void main(String[] args) {
        SwingLinster swingLinster = new SwingLinster();
    }

    static class SwingLinster{
        public SwingLinster(){
            JPanel panel = new JPanel();                // 创建面板容器，使用默认的布局管理器
            final JTextField typeName = new JTextField(8);
            typeName.setFont(new Font(null, Font.PLAIN, 20));
            JLabel jlb1 = new JLabel("搜索品名");
            panel.add(jlb1);
            panel.add(typeName);

            JPanel panel2 = new JPanel();                // 创建面板容器，使用默认的布局管理器
            JLabel locLabel = new JLabel("本地存放地址");
            panel2.add(locLabel);
            final JTextField location = new JTextField(8);
            location.setFont(new Font(null, Font.PLAIN, 20));
            panel2.add(location);

            JPanel panel4 = new JPanel();                // 创建面板容器，使用默认的布局管理器
            JLabel jlb4 = new JLabel("默认存放地址为：c:/");
            jlb4.setFont(new Font("",12,12));
            panel4.add(jlb4);

            JPanel panel3 = new JPanel();                // 创建面板容器，使用默认的布局管理器
            // 创建一个按钮，点击后获取文本框中的文本
            JButton btn = new JButton("生成");
            //btn.setFont(new Font(null, Font.PLAIN, 20));
            panel3.add(btn);

            // 5. 显示窗口，前面创建的信息都在内存中，通过 jf.setVisible(true) 把内存中的窗口显示在屏幕上。
            // 1. 创建一个顶层容器（窗口）
            JFrame jf = new JFrame("中国通关查询接口");          // 创建窗口
            jf.add(panel);
            jf.add(panel2);
            jf.add(panel4);
            jf.add(panel3);
            //jf.setContentPane(panel);// 4. 把 面板容器 作为窗口的内容面板 设置到 窗口
            jf.setSize(500, 350);                       // 设置窗口大小
            jf.setLocationRelativeTo(null);             // 把窗口位置设置到屏幕中心
            jf.setLayout(new GridLayout(10,10));
            jf.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE); // 当点击窗口的关闭按钮时退出程序（没有这一句，程序不会退出）
            jf.setVisible(true);

            //监听按钮
            btn.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    System.out.println("生成: " + typeName.getText());
                    String name = typeName.getText();
                    String path = location.getText() == null || "" == location.getText()? "C:\\": location.getText();
                    boolean flag = HttpURLConnectionExample.sendRequest(name, path);
                    if(flag){
                        jlb4.setText("品名 "+typeName.getText()+" 已成功生成，生产路径为 "+path);
                    }else{
                        jlb4.setText("品名 "+typeName.getText()+" 生成失败");
                    }
                }
            });

        }
    }
}


