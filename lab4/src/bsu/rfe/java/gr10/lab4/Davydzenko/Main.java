package bsu.rfe.java.gr10.lab4.Davydzenko;

import java.awt.BorderLayout;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.JCheckBoxMenuItem;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JOptionPane;
import javax.swing.event.MenuEvent;
import javax.swing.event.MenuListener;

@SuppressWarnings("serial")
public class Main extends JFrame {
    // ��������� ������� ���� ����������
    private static final int WIDTH = 800;
    private static final int HEIGHT = 600;
    // ������ ����������� ���� ��� ������ ������
    private JFileChooser fileChooser = null;
    private JCheckBoxMenuItem showAxisMenuItem;
    private JCheckBoxMenuItem showMarkersMenuItem;
    private JCheckBoxMenuItem rotatePanelMenuItem;
    private GraphicsDisplay display = new GraphicsDisplay();
    private boolean fileLoaded = false;

    public Main() {

        super("���������� �������� ������� �� ������ ������� �������������� ������");
// ��������� �������� ����
                setSize(WIDTH, HEIGHT);
        Toolkit kit = Toolkit.getDefaultToolkit();
// �������������� ���� ���������� �� ������
        setLocation((kit.getScreenSize().width - WIDTH)/2,
                (kit.getScreenSize().height - HEIGHT)/2);
// ������������ ���� �� ���� �����
        setExtendedState(MAXIMIZED_BOTH);
// ������� � ���������� ������ ����
        JMenuBar menuBar = new JMenuBar();
        setJMenuBar(menuBar);
// �������� ����� ���� "����"
        JMenu fileMenu = new JMenu("����");
        menuBar.add(fileMenu);
// ������� �������� �� �������� �����
        Action openGraphicsAction = new AbstractAction("������� ���� �  ��������") {
        public void actionPerformed(ActionEvent event) {
            if (fileChooser==null) {
                fileChooser = new JFileChooser();
                fileChooser.setCurrentDirectory(new File("."));
            }
            if (fileChooser.showOpenDialog(Main.this) ==
                    JFileChooser.APPROVE_OPTION)
                openGraphics(fileChooser.getSelectedFile());
        }
    };
// �������� ��������������� ������� ����
        fileMenu.add(openGraphicsAction);
        //fileMenu.add(openGraphicsAction1);

    // ������� ����� ���� "������"
    JMenu graphicsMenu = new JMenu("������");
menuBar.add(graphicsMenu);
    // ������� �������� ��� ������� �� ��������� �������� "���������� ��� ���������"
    Action showAxisAction = new AbstractAction("���������� ��� ���������") {
    public void actionPerformed(ActionEvent event) {
        display.setShowAxis(showAxisMenuItem.isSelected());
    }
};
        showAxisMenuItem = new JCheckBoxMenuItem(showAxisAction);
// �������� ��������������� ������� � ����
        graphicsMenu.add(showAxisMenuItem);
// ������� �� ��������� ������� (������� �������)
        showAxisMenuItem.setSelected(true);
// ��������� �������� ��� �������� "���������� ������� �����"

        Action showMarkersAction = new AbstractAction("���������� ������� �����") {
            public void actionPerformed(ActionEvent event) {
                display.setShowMarkers(showMarkersMenuItem.isSelected());
            }
        };
        Action turnGrid = new AbstractAction("�������� ���") {
            public void actionPerformed(ActionEvent event) {
// �������� showAxis ������ GraphicsDisplay ������, ���� ������� ����
// showAxisMenuItem ������� �������, � ���� - � ��������� ������
                display.setTurnGrid(rotatePanelMenuItem.isSelected());
            }
        };
        rotatePanelMenuItem = new JCheckBoxMenuItem(turnGrid);
        graphicsMenu.add(rotatePanelMenuItem);
        rotatePanelMenuItem.setSelected(false);
        showMarkersMenuItem = new JCheckBoxMenuItem(showMarkersAction);
        graphicsMenu.add(showMarkersMenuItem);
// ������� �� ��������� ������� (������� �������)
        showMarkersMenuItem.setSelected(true);
// ���������������� ���������� �������, ��������� � ���� "������"
        graphicsMenu.addMenuListener(new GraphicsMenuListener());
// ���������� GraphicsDisplay � ���� ��������� ����������
        getContentPane().add(display, BorderLayout.CENTER);
        }

// ���������� ������ ������� �� ������������� �����
protected void openGraphics(File selectedFile) {
        try {
            DataInputStream in = new DataInputStream(new FileInputStream(selectedFile));
            //����� ���� � ������ - in.available() ����;
            //������ ����� Double - Double.SIZE ���, ��� Double.SIZE/8 ����;
            //��� ��� ����� ������������ ������, �� ����� ��� ������ � 2 ����
            Double[][] graphicsData = new Double[in.available()/(Double.SIZE/8)/2][];
            int i = 0;
            while (in.available()>0) {
                Double x = in.readDouble();
                Double y = in.readDouble();
                graphicsData[i++] = new Double[] {x, y};
            }
            if (graphicsData!=null && graphicsData.length>0) {
                fileLoaded = true;
                display.showGraphics(graphicsData);
            }
            in.close();
        } catch (FileNotFoundException ex) {
            JOptionPane.showMessageDialog(Main.this, "��������� ���� �� ������", "������ �������� ������", JOptionPane.WARNING_MESSAGE);
            return;
        } catch (IOException ex) {
            JOptionPane.showMessageDialog(Main.this, "������ ������ ��������� ����� �� �����", "������ �������� ������",
                    JOptionPane.WARNING_MESSAGE);
            return;
        }
        }
public static void main(String[] args) {
// ������� � �������� ��������� �������� ���� ����������
        Main frame = new Main();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
        }
// �����-��������� �������, ��������� � ������������ ����
private class GraphicsMenuListener implements MenuListener {
    // ����������, ���������� ����� ������� ����
    public void menuSelected(MenuEvent e) {
// ����������� ��� ������������� ��������� ���� "������" ������������ �������������� ������
        showAxisMenuItem.setEnabled(fileLoaded);
        showMarkersMenuItem.setEnabled(fileLoaded);
    }
    // ����������, ���������� ����� ����, ��� ���� ������� � ������
    public void menuDeselected(MenuEvent e) {
    }
// ����������, ���������� � ������ ������ ������ ������ ����
    public void menuCanceled(MenuEvent e) {
    }
}
}