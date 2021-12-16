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
    // Начальные размеры окна приложения
    private static final int WIDTH = 800;
    private static final int HEIGHT = 600;
    // Объект диалогового окна для выбора файлов
    private JFileChooser fileChooser = null;
    private JCheckBoxMenuItem showAxisMenuItem;
    private JCheckBoxMenuItem showMarkersMenuItem;
    private JCheckBoxMenuItem rotatePanelMenuItem;
    private GraphicsDisplay display = new GraphicsDisplay();
    private boolean fileLoaded = false;

    public Main() {

        super("Построение графиков функций на основе заранее подготовленных файлов");
// Установка размеров окна
                setSize(WIDTH, HEIGHT);
        Toolkit kit = Toolkit.getDefaultToolkit();
// Отцентрировать окно приложения на экране
        setLocation((kit.getScreenSize().width - WIDTH)/2,
                (kit.getScreenSize().height - HEIGHT)/2);
// Развртывание окна на весь экран
        setExtendedState(MAXIMIZED_BOTH);
// Создать и установить полосу меню
        JMenuBar menuBar = new JMenuBar();
        setJMenuBar(menuBar);
// Добавить пункт меню "Файл"
        JMenu fileMenu = new JMenu("Файл");
        menuBar.add(fileMenu);
// Создать действие по открытию файла
        Action openGraphicsAction = new AbstractAction("Открыть файл с  графиком") {
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
// Добавить соответствующий элемент меню
        fileMenu.add(openGraphicsAction);
        //fileMenu.add(openGraphicsAction1);

    // Создать пункт меню "График"
    JMenu graphicsMenu = new JMenu("График");
menuBar.add(graphicsMenu);
    // Создать действие для реакции на активацию элемента "Показывать оси координат"
    Action showAxisAction = new AbstractAction("Показывать оси координат") {
    public void actionPerformed(ActionEvent event) {
        display.setShowAxis(showAxisMenuItem.isSelected());
    }
};
        showAxisMenuItem = new JCheckBoxMenuItem(showAxisAction);
// Добавить соответствующий элемент в меню
        graphicsMenu.add(showAxisMenuItem);
// Элемент по умолчанию включен (отмечен флажком)
        showAxisMenuItem.setSelected(true);
// Повторить действия для элемента "Показывать маркеры точек"

        Action showMarkersAction = new AbstractAction("Показывать маркеры точек") {
            public void actionPerformed(ActionEvent event) {
                display.setShowMarkers(showMarkersMenuItem.isSelected());
            }
        };
        Action turnGrid = new AbstractAction("Поменять оси") {
            public void actionPerformed(ActionEvent event) {
// свойство showAxis класса GraphicsDisplay истина, если элемент меню
// showAxisMenuItem отмечен флажком, и ложь - в противном случае
                display.setTurnGrid(rotatePanelMenuItem.isSelected());
            }
        };
        rotatePanelMenuItem = new JCheckBoxMenuItem(turnGrid);
        graphicsMenu.add(rotatePanelMenuItem);
        rotatePanelMenuItem.setSelected(false);
        showMarkersMenuItem = new JCheckBoxMenuItem(showMarkersAction);
        graphicsMenu.add(showMarkersMenuItem);
// Элемент по умолчанию включен (отмечен флажком)
        showMarkersMenuItem.setSelected(true);
// Зарегистрировать обработчик событий, связанных с меню "График"
        graphicsMenu.addMenuListener(new GraphicsMenuListener());
// Установить GraphicsDisplay в цент граничной компоновки
        getContentPane().add(display, BorderLayout.CENTER);
        }

// Считывание данных графика из существующего файла
protected void openGraphics(File selectedFile) {
        try {
            DataInputStream in = new DataInputStream(new FileInputStream(selectedFile));
            //Всего байт в потоке - in.available() байт;
            //Размер числа Double - Double.SIZE бит, или Double.SIZE/8 байт;
            //Так как числа записываются парами, то число пар меньше в 2 раза
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
            JOptionPane.showMessageDialog(Main.this, "Указанный файл не найден", "Ошибка загрузки данных", JOptionPane.WARNING_MESSAGE);
            return;
        } catch (IOException ex) {
            JOptionPane.showMessageDialog(Main.this, "Ошибка чтения координат точек из файла", "Ошибка загрузки данных",
                    JOptionPane.WARNING_MESSAGE);
            return;
        }
        }
public static void main(String[] args) {
// Создать и показать экземпляр главного окна приложения
        Main frame = new Main();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
        }
// Класс-слушатель событий, связанных с отображением меню
private class GraphicsMenuListener implements MenuListener {
    // Обработчик, вызываемый перед показом меню
    public void menuSelected(MenuEvent e) {
// Доступность или недоступность элементов меню "График" определяется загруженностью данных
        showAxisMenuItem.setEnabled(fileLoaded);
        showMarkersMenuItem.setEnabled(fileLoaded);
    }
    // Обработчик, вызываемый после того, как меню исчезло с экрана
    public void menuDeselected(MenuEvent e) {
    }
// Обработчик, вызываемый в случае отмены выбора пункта меню
    public void menuCanceled(MenuEvent e) {
    }
}
}