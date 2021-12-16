package bsu.rfe.java.gr10.lab4.Davydzenko;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Paint;
import java.awt.Stroke;
import java.awt.font.FontRenderContext;
import java.awt.geom.Ellipse2D;
import java.awt.geom.GeneralPath;
import java.awt.geom.Line2D;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.NumberFormat;
import java.util.LinkedList;
import javax.swing.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;



@SuppressWarnings("serial")

public class GraphicsDisplay extends JPanel {

    // ������ ��������� ����� ��� ���������� �������
    private Double[][] graphicsData;

    // �������� ����������, �������� ������� ����������� �������
    private boolean showAxis = true;
    private boolean showMarkers = true;

    // ������� ��������� ������������, ����������� �����������
    private double minX;
    private double maxX;
    private double minY;
    private double maxY;
    private boolean showIntegrals = false;
	private BasicStroke lineStroke;
    // ������������ ������� �����������

    private double scale;
    // ��������� ����� �������� �����

    private BasicStroke graphicsStroke;
    private BasicStroke axisStroke;
    private boolean turnGraph = false;
    private BasicStroke markerStroke;
    // ��������� ������ ����������� ��������

    private Font axisFont;
    private Font smallfont;
    public GraphicsDisplay() {
// ���� ������� ���� ������� ����������� - �����

        setBackground(Color.WHITE);
// ��������������� ����������� �������, ������������ � ���������
// ���� ��� ��������� �������

        graphicsStroke = new BasicStroke(2.0f, BasicStroke.CAP_BUTT,

                BasicStroke.JOIN_ROUND, 10.0f, new float[] {10, 10, 10, 10, 10, 10, 30 , 30, 30,30,30,30}, 0.0f);
// ���� ��� ��������� ���� ���������

        axisStroke = new BasicStroke(2.0f, BasicStroke.CAP_BUTT,

                BasicStroke.JOIN_MITER, 10.0f, null, 0.0f);
// ���� ��� ��������� �������� ��������

        markerStroke = new BasicStroke(1.0f, BasicStroke.CAP_BUTT,

                BasicStroke.JOIN_MITER, 10.0f, null, 0.0f);
// ����� ��� �������� ���� ���������


        axisFont = new Font("Serif", Font.BOLD, 36);
    }
    // ������ ����� ���������� �� ����������� �������� ���� "������� ���� � ��������"
    // �������� ���� ���������� � ������ �������� �������� ������

    public void showGraphics(Double[][] graphicsData) {
// ��������� ������ ����� �� ���������� ���� ������
        this.graphicsData = graphicsData;
// ��������� ����������� ����������, �.�. ������ ������� paintComponent()
        repaint();
    }

    // ������-������������ ��� ��������� ���������� ����������� �������
// ��������� ������ ��������� �������� � ����������� �������
    public void setShowAxis(boolean showAxis) {
        this.showAxis = showAxis;
        repaint();

    }
    public void setShowMarkers(boolean showMarkers) {
        this.showMarkers = showMarkers;
        repaint();
    }
    public void setShowIntegrals(boolean showIntegrals) {
        this.showIntegrals = showIntegrals;
        repaint();
    }
    // ����� ����������� ����� ����������, ����������� ������

    @Override
    public void paintComponent (Graphics g){
        /* ��� 1 - ������� ����� ������ ��� ������� ������� ������ ������� ����
         * ��� ���������������� - ������������, ��� �������� � ���������� ��
         * paintComponent ������ JPanel
         */
        super.paintComponent(g);
// ��� 2 - ���� ������ ������� �� ��������� (��� ������ ���������� ��� ������� ���������) - ������ �� ������
        if (graphicsData == null || graphicsData.length == 0) return;
// ��� 3 - ���������� ����������� � ������������ �������� ��� ��������� X � Y
// ��� ���������� ��� ����������� ������� ������������, ���������� �����������
// � ������� ����� ���� ��� (minX, maxY) - ������ ������ ��� (maxX, minY)
        minX = graphicsData[0][0];
        maxX = graphicsData[graphicsData.length - 1][0];
        minY = graphicsData[0][1];
        maxY = minY;
// ����� ����������� � ������������ �������� �������
        for (int i = 1; i < graphicsData.length; i++) {
            if (graphicsData[i][1] < minY) {
                minY = graphicsData[i][1];
            }
            if (graphicsData[i][1] > maxY) {
                maxY = graphicsData[i][1];
            }
        }
/* ��� 4 - ���������� (������ �� �������� ����) �������� �� ���� X
� Y - ������� ��������
* ���������� �� ������� ����� �� X � �� Y
*/
        if (!turnGraph) {
            double scaleX = getSize().getWidth() / (maxX - minX);
            double scaleY = getSize().getHeight() / (maxY - minY);
// ��� 5 - ����� ����������� ���� ����������� - ������� ������ ���� ��������
// �������� �� ������ �����������
            scale = Math.min(scaleX, scaleY);
// ��� 6 - ������������� ������ ������������ ������� �������� ���������� ��������
            if (scale == scaleX) {
/* ���� �� ������ ��� ���� ������� �� ��� X, ������ �� ��� Y
������� ������,
* �.�. ���������� ������������ �������� �� Y ����� ������
������ ����.
* ������ ���������� �������� �������, ������� ��� ���:
* 1) ��������, ������� ������� ������ �� Y ��� ���������
�������� - getSize().getHeight()/scale
* 2) ������ �� ����� ������� ������� ����������� ����������
* 3) �������� �� �������� ������������ ���������� �� maxY �
minY
*/
                double yIncrement = (getSize().getHeight() / scale - (maxY - minY)) / 2;
                maxY += yIncrement;
                minY -= yIncrement;
            }
            if (scale == scaleY) {
// ���� �� ������ ��� ���� ������� �� ��� Y, ����������� �� ��������
                double xIncrement = (getSize().getWidth() / scale - (maxX - minX)) / 4;// ��� ������� /4 � �������� if
                maxX += xIncrement;
                minX -= xIncrement;
            }
        } else {
            double scaleX = getSize().getHeight() / (maxX - minX);
            double scaleY = getSize().getWidth() / (maxY - minY);
            scale = Math.min(scaleX, scaleY);
            if (scale == scaleY) {
                double xIncrement = (getSize().getHeight() / scale - (maxX - minX)) / 2;
                maxX += xIncrement;
                minX -= xIncrement;
            }
            if (scale == scaleX) {
                double yIncrement = (getSize().getWidth() / scale - (maxY - minY)) / 2;
                maxY += yIncrement;
                minY -= yIncrement;
            }
        }
// ��� 7 - ��������� ������� ��������� ������
        Graphics2D canvas = (Graphics2D) g;
        Stroke oldStroke = canvas.getStroke();
        Color oldColor = canvas.getColor();
        Paint oldPaint = canvas.getPaint();
        Font oldFont = canvas.getFont();
// ��� 8 - � ������ ������� ������� ������ ����������� ��������� �������
// ������� ������ ������� ����� ��������, �.�. ���������� ������� ����� ���������� �����������
// ������� (���� �����) �������������� ��� ���������.
        if (turnGraph) {
            rotatePanel(canvas);
        }
        if (showAxis) paintAxis(canvas);
// ����� ������������ ��� ������
        paintGraphics(canvas);
// ����� (���� �����) ������������ ������� �����, �� ������� �������� ������.
        if (showIntegrals) paintIntegrals(canvas);
        if (showMarkers) paintMarkers(canvas);
// ��� 9 - ������������ ������ ��������� ������
        canvas.setFont(oldFont);
        canvas.setPaint(oldPaint);
        canvas.setColor(oldColor);
        canvas.setStroke(oldStroke);
    }


    // ��������� ������� �� ����������� �����������
    protected void paintGraphics(Graphics2D canvas) {
// ������� ����� ��� ��������� �������
        canvas.setStroke(graphicsStroke);
// ������� ���� �����
        canvas.setColor(Color.magenta);
/* ����� �������� ����� ������� ��� ����, ��������� �� ���������
��������� (GeneralPath)
* ������ ���� ��������������� � ������ ����� �������, ����� ����
������ ����������� ��
* ���������� �������
*/
        GeneralPath graphics = new GeneralPath();
        for (int i = 0; i < graphicsData.length; i++) {
// ������������� �������� (x,y) � ����� �� ������ point
            Point2D.Double point = xyToPoint(graphicsData[i][0], graphicsData[i][1]);
            if (i > 0) {
// �� ������ �������� ����� - ����� ����� � ����� point
                graphics.lineTo(point.getX(), point.getY());
            } else {
// ������ �������� ����� - ���������� ������ ���� � ����� point
                graphics.moveTo(point.getX(), point.getY());
            }
        }
// ���������� ������
        canvas.draw(graphics);
    }
    protected void paintIntegrals(Graphics2D canvas) {
        LinkedList<Integer> indexses = new LinkedList<>();
        Double domens = 0.0;
        GeneralPath path = new GeneralPath();
        for (int i = 0; i < graphicsData.length - 1; i++) {
            System.out.println("X: " + graphicsData[i][0] + " Y: " + graphicsData[i][1] + " i: " + i);
            if ((graphicsData[i][1] < 0 == graphicsData[i + 1][1] >= 0) || (graphicsData[i][1] == 0)) {
                if (domens != 0) {
                    domens += 1;
                    if (graphicsData[i][1] == 0)
                        indexses.add(i - 1);
                    else
                        indexses.add(i);
                    indexses.add(i);
                    System.out.println("End+Start");
                    if(graphicsData[i+1][1]==0) {
                        i++;
                    }
                    System.out.println("X: " + graphicsData[i][0] + " Y: " + graphicsData[i][1] + " i: " + i);
                    continue;
                } else {
                    indexses.add(i);
                    System.out.println("Start");
                    domens += 0.5;
                }

            }
            if (domens.intValue() == domens) {
                //  System.out.println("end");
            } else {
                //System.out.println("start");
            }
        }
        LinkedList<Double> xcoordinates = new LinkedList<>();
        for (int i = 0; i < 2 * domens.intValue(); i++) {
            //�������� ��� �������� ����� ������������ ������ � ���� ��������� �� ��������� ���� ������
            xcoordinates.add(-graphicsData[indexses.get(i)][1] / (graphicsData[indexses.get(i) + 1][1] - graphicsData[indexses.get(i)][1]) * (graphicsData[indexses.get(i) + 1][0] - graphicsData[indexses.get(i)][0]) + graphicsData[indexses.get(i)][0]);
            System.out.println("���������� x ����������� c Ox � �������� " + i + " " + xcoordinates.get(i) + " �� ��������� �� " + indexses.get(i) + " �� " + (indexses.get(i) + 1));
        }

        int k = 0;
        Double[] integral = new Double[xcoordinates.size() / 2];
        for (int i = 0; i < xcoordinates.size() / 2; i++) {
            integral[i] = 0.0;
        }
        Double maxy = 0.0;
        Double miny = 0.0;
        Double[] averagey = new Double[xcoordinates.size() / 2];
        for (int i = 0; i < graphicsData.length; i++) {
            // System.out.println("INDEX: "+ i+ " left " +xcoordinates.get(k)+"<="+graphicsData[i][0]+"<"+xcoordinates.get(k+1)+ " ? ");
            if (graphicsData[i][0] >= xcoordinates.get(k) && graphicsData[i][0] < xcoordinates.get(k + 1)) {
// ������������� �������� (x,y) � ����� �� ������ point
                if (maxy < graphicsData[i][1]) {
                    maxy = graphicsData[i][1];
                }
                if (miny > graphicsData[i][1]) {
                    miny = graphicsData[i][1];
                }
                if (graphicsData[i - 1][0] <= xcoordinates.get(k)) {
// ������ �������� ����� - ���������� ������ ���� � ����� point
                    integral[k / 2] += Math.abs((graphicsData[i][0] - xcoordinates.get(k)) * graphicsData[i][1] / 2);
                    canvas.setColor(Color.red);
                    Point2D.Double point = xyToPoint(xcoordinates.get(k), 0);
                    path.moveTo(point.getX(), point.getY());
                    System.out.println("The line moved to its initial position, x = " + point.getX() + " on the itaration i = " + i);
                    point = xyToPoint(graphicsData[i][0], graphicsData[i][1]);
                    path.lineTo(point.getX(), point.getY());
                }
                if (graphicsData[i + 1][0] >= xcoordinates.get(k + 1)) {
// �� ������ �������� ����� - ����� ����� � ����� point
                    Point2D.Double point = xyToPoint(graphicsData[i][0], graphicsData[i][1]);
                    path.lineTo(point.getX(), point.getY());
                    point = xyToPoint(xcoordinates.get(k + 1), 0);
                    path.lineTo(point.getX(), point.getY());
                    integral[k / 2] += Math.abs(graphicsData[i][1] / 2 * (xcoordinates.get(k + 1) - graphicsData[i][0]));
                    path.closePath();
                    System.out.println("The line was closed , x = " + point.getX() + " on the itaration i = " + i);
                    canvas.fill(path);
                    canvas.draw(path);
                    if (maxy == 0.0)
                        averagey[k / 2] = miny;
                    else
                        averagey[k / 2] = maxy;
                    if (k >= xcoordinates.size() - 2) break;
                    k += 2;
                    maxy = 0.0;
                    miny = 0.0;
                }
                if(!(graphicsData[i + 1][0] >= xcoordinates.get(k + 1))&&!(graphicsData[i - 1][0] <= xcoordinates.get(k))) {
                    integral[k / 2] += Math.abs((graphicsData[i][0] - graphicsData[i - 1][0]) * (graphicsData[i][1] + graphicsData[i - 1][1]) / 2);
                    Point2D.Double point = xyToPoint(graphicsData[i][0], graphicsData[i][1]);
                    path.lineTo(point.getX(), point.getY());
                }

            }
        }
        System.out.println("Integral" + (int) (k / 2) + " = " + integral[k / 2]);

        canvas.setFont(smallfont);
        FontRenderContext context = canvas.getFontRenderContext();
        for (
                int i = 0; i < xcoordinates.size() / 2; i++) {
            canvas.setColor(Color.black);
            Rectangle2D bounds = smallfont.getStringBounds(String.format("%.3f", integral[i]), context);
            System.out.println(bounds.getX());
            canvas.drawString(String.format("%.3f", integral[i]), (float) (xyToPoint(xcoordinates.get(2 * i) + (xcoordinates.get(2 * i + 1) - xcoordinates.get(2 * i)) / 2 - bounds.getX(), averagey[i] / 2).getX()), (float) xyToPoint(xcoordinates.get(2 * i) + (xcoordinates.get(2 * i + 1) - xcoordinates.get(2 * i)) / 2, averagey[i] / 2).getY());
        }
    }

    protected void rotatePanel(Graphics2D canvas){
        canvas.translate(0, getHeight());
        canvas.rotate(-Math.PI/2);
    }
    protected void paintMarkers(Graphics2D canvas) {
// ��� 1 - ���������� ����������� ���� ��� �������� �������� ��������
        canvas.setStroke(markerStroke);
// ������� ������� ����� ��� �������� ��������
        canvas.setColor(Color.BLUE);
// ������� ������� ���� ��� ������������ �������� ������
        canvas.setPaint(Color.BLUE);
// ��� 2 - ������������ ���� �� ���� ������ �������
        for (Double[] point : graphicsData) {
// ���������������� ������ ��� ������ ��� ������������� �������
            int size = 5;
            Ellipse2D.Double marker = new Ellipse2D.Double();
            Point2D.Double center = xyToPoint(point[0], point[1]);
            //line.setLine();
// ���� �������������� - ������� �� ���������� (3,3)
            Point2D.Double corner = shiftPoint(center, size, size);
// ������ ������ �� ������ � ���������
            marker.setFrameFromCenter(center, corner);


            Line2D.Double line = new Line2D.Double(shiftPoint(center, -size, 0), shiftPoint(center, size, 0));
            Boolean highervalue = true;
            DecimalFormat formatter = (DecimalFormat) NumberFormat.getInstance();
            formatter.setMaximumFractionDigits(2);
            DecimalFormatSymbols dottedDouble =
                    formatter.getDecimalFormatSymbols();
            dottedDouble.setDecimalSeparator('.');
            formatter.setDecimalFormatSymbols(dottedDouble);
            String temp = formatter.format(Math.abs(point[1]));
            temp = temp.replace(".", "");
            //System.out.println(temp);
            for (int i = 0; i < temp.length() - 1; i++) {

                if (temp.charAt(i) != 46 && (int) temp.charAt(i) > (int) temp.charAt(i + 1)) {
                    highervalue = false;
                    break;
                }
            }
            if (highervalue) {
                canvas.setColor(Color.BLACK);
            }
            canvas.draw(line);
            line.setLine(shiftPoint(center, 0, -size), shiftPoint(center, 0, size));
            canvas.draw(line);
            canvas.draw(marker); // ��������� ������ �������
            canvas.setColor(Color.BLUE);
/* ������ ����� ���������� ����������� �������� ���������
��� ������
� ���� ��������������, � ������� �� ������ */
// ����� - � ����� (x,y)

        }
    }


    // �����, �������������� ����������� ���� ���������
    protected void paintAxis(Graphics2D canvas) {
// ���������� ������ ���������� ��� ����
        canvas.setStroke(axisStroke);
// ��� �������� ����� ������
        canvas.setColor(Color.BLACK);
// ������� ���������� ����� ������
        canvas.setPaint(Color.BLACK);
// ������� � ������������ ���� �������� ����������� �������
        canvas.setFont(axisFont);
// ������� ������ ��������� ����������� ������ - ��� ��������� ������������� ���������� (������)

        FontRenderContext context = canvas.getFontRenderContext();
// ����������, ������ �� ���� ����� ��� Y �� �������
        if (minX<=0.0 && maxX>=0.0) {
// ��� ������ ���� �����, ���� ����� ������� ������������ ������� (minX) <= 0.0,

// � ������ (maxX) >= 0.0
// ���� ��� - ��� ����� ����� ������� (0, maxY) � (0, minY)
            canvas.draw(new Line2D.Double(xyToPoint(0, maxY),

                    xyToPoint(0, minY)));

// ������� ��� Y
            GeneralPath arrow = new GeneralPath();
// ���������� ��������� ����� ������� ����� �� ������� ����� ��� Y

            Point2D.Double lineEnd = xyToPoint(0, maxY);
            arrow.moveTo(lineEnd.getX(), lineEnd.getY());
// ����� ����� "����" ������� � ����� � �������������� ������������ (5,20)

            arrow.lineTo(arrow.getCurrentPoint().getX()+5,

                    arrow.getCurrentPoint().getY()+20);

// ����� ������ ����� ������� � ����� � �������������� ������������ (-10, 0)

            arrow.lineTo(arrow.getCurrentPoint().getX()-10,

                    arrow.getCurrentPoint().getY());

// �������� ����������� �������
            arrow.closePath();
            canvas.draw(arrow); // ���������� �������
            canvas.fill(arrow); // ��������� �������
// ���������� ������� � ��� Y
// ����������, ������� ����� ����������� ��� ������� "y"
            Rectangle2D bounds = axisFont.getStringBounds("y", context);
            Point2D.Double labelPos = xyToPoint(0, maxY);
// ������� ������� � ����� � ������������ ������������
            canvas.drawString("y", (float)labelPos.getX() + 10,

                    (float)(labelPos.getY() - bounds.getY()));
            

            Rectangle2D centerBounds = axisFont.getStringBounds("0", context);
            Point2D.Double centerLabelPos = xyToPoint(0, 0);
            canvas.drawString("0", (float)centerLabelPos.getX() + 10,
                    (float)(centerLabelPos.getY() - centerBounds.getY()));
            

          
        }
        
        Rectangle2D centerBounds = axisFont.getStringBounds("1", context);
        Point2D.Double centerLabelPos = xyToPoint(0, 1);
        canvas.drawString("1", (float)centerLabelPos.getX() + 10,
                (float)(centerLabelPos.getY() - centerBounds.getY()));
        canvas.draw(new Line2D.Double(xyToPoint(0.5, 1), xyToPoint(-0.5, 1)));
        
        
        
// ����������, ������ �� ���� ����� ��� X �� �������
        if (minY<=0.0 && maxY>=0.0) {
// ��� ������ ���� �����, ���� ������� ������� ������������ ������� (maxX) >= 0.0,

// � ������ (minY) <= 0.0
            canvas.draw(new Line2D.Double(xyToPoint(minX, 0),

                    xyToPoint(maxX, 0)));

// ������� ��� X
            GeneralPath arrow = new GeneralPath();
// ���������� ��������� ����� ������� ����� �� ������ ����� ��� X

            Point2D.Double lineEnd = xyToPoint(maxX, 0);
            arrow.moveTo(lineEnd.getX(), lineEnd.getY());
// ����� ������� "����" ������� � ����� � �������������� ������������ (-20,-5)

            arrow.lineTo(arrow.getCurrentPoint().getX()-20,

                    arrow.getCurrentPoint().getY()-5);

// ����� ����� ����� ������� � ����� � �������������� ������������ (0, 10)

            arrow.lineTo(arrow.getCurrentPoint().getX(),

                    arrow.getCurrentPoint().getY()+10);

// �������� ����������� �������
            arrow.closePath();
            canvas.draw(arrow); // ���������� �������
            canvas.fill(arrow); // ��������� �������
// ���������� ������� � ��� X
// ����������, ������� ����� ����������� ��� ������� "x"
            Rectangle2D bounds = axisFont.getStringBounds("x", context);
            Point2D.Double labelPos = xyToPoint(maxX, 0);
// ������� ������� � ����� � ������������ ������������
            canvas.drawString("x", (float)(labelPos.getX() -
                    bounds.getWidth() - 10), (float)(labelPos.getY() + bounds.getY()));

        }
    }
    protected Point2D.Double xyToPoint(double x, double y) {
// ��������� �������� X �� ����� ����� ����� (minX)
        double deltaX = x - minX;
// ��������� �������� Y �� ����� ������� ����� (maxY)
        double deltaY = maxY - y;
        return new Point2D.Double(deltaX*scale, deltaY*scale);
    }
    /* �����-��������, ������������ ��������� ������ Point2D.Double
     * �������� �� ��������� � ��������� �� deltaX, deltaY
     * � ���������, ������������ ������, ������������ ����� ������, ���.
     */
    protected Point2D.Double shiftPoint(Point2D.Double src, double deltaX,
                                        double deltaY) {

// ���������������� ����� ��������� �����
        Point2D.Double dest = new Point2D.Double();
// ������ � ���������� ��� ���������� ������������ ����� + �������� ��������

        dest.setLocation(src.getX() + deltaX, src.getY() + deltaY);
        return dest;
    }
    public void setTurnGrid(boolean turnGraph) {
        this.turnGraph = turnGraph;
        System.out.println(turnGraph);
        repaint();
    }
}