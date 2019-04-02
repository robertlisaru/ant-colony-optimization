package ro.ulbsibiu.indinfo.ants.gui;

import com.jgoodies.forms.builder.PanelBuilder;
import com.jgoodies.forms.layout.CellConstraints;
import com.jgoodies.forms.layout.FormLayout;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.XYLineAndShapeRenderer;
import org.jfree.data.xy.XYDataset;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;
import ro.ulbsibiu.indinfo.ants.Util;
import ro.ulbsibiu.indinfo.ants.World;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.border.BevelBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.IOException;
import java.util.ArrayList;

public class MainFrame extends JFrame {
    private ControlsPanel controlsPanel = new ControlsPanel();
    private SouthPanel southPanel = new SouthPanel();
    private Canvas canvas = new Canvas();
    private ArrayList<Point> clickedPoints = new ArrayList<>();
    private World world;
    private int[][] distances;
    private int[] minPath;
    private int numIterations = 0;
    private double[][] pheromoneMap;

    private MainFrame() {
        setTitle("Ant Colony Optimization");
        try {
            setIconImage(ImageIO.read(getClass().getResourceAsStream("/ant.png")));
        } catch (IOException e) {
            e.printStackTrace();
        }
        setMinimumSize(new Dimension(800, 500));
        setLocationRelativeTo(null); //screen center
        setExtendedState(getExtendedState() | JFrame.MAXIMIZED_BOTH);

        //region menu bar
        JMenu fileMenu = new JMenu("File");
        fileMenu.setMnemonic(KeyEvent.VK_F);
        JMenu viewMenu = new JMenu("View");
        viewMenu.setMnemonic(KeyEvent.VK_V);
        JMenuItem exit = new JMenuItem("Exit");
        exit.setMnemonic(KeyEvent.VK_X);
        exit.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.exit(0);
            }
        });
        JMenuItem viewChart = new JCheckBoxMenuItem("Chart");
        viewChart.setMnemonic(KeyEvent.VK_R);
        viewChart.setSelected(true);
        viewChart.addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent e) {
                if (e.getStateChange() == ItemEvent.SELECTED) {
                    southPanel.setVisible(true);
                } else {
                    southPanel.setVisible(false);
                }
            }
        });
        JMenuItem about = new JMenuItem("About");
        about.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JOptionPane.showMessageDialog(MainFrame.this,
                        "Student: Lisaru Robert-Adrian \n" +
                                "Grupa: 241/2\n" +
                                "Anul universtar: 4\n" +
                                "Anul calendaristic: 2019\n");
            }
        });

        JMenu helpMenu = new JMenu("Help");
        helpMenu.add(about);
        fileMenu.add(exit);
        viewMenu.add(viewChart);
        JMenuBar menuBar = new JMenuBar();
        menuBar.add(fileMenu);
        menuBar.add(viewMenu);
        menuBar.add(helpMenu);
        setJMenuBar(menuBar);
        //endregion

        setLayout(new BorderLayout());
        add(canvas, BorderLayout.CENTER);
        add(controlsPanel, BorderLayout.EAST);
        add(southPanel, BorderLayout.SOUTH);

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        pack();
        setVisible(true);
    }

    public static void main(String args[]) {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (ClassNotFoundException | InstantiationException
                | UnsupportedLookAndFeelException | IllegalAccessException e) {
            e.printStackTrace();
        }
        MainFrame mainFrame = new MainFrame();
    }

    private void reset() {
        canvas.clickingEnabled = true;
        controlsPanel.numCities.setEnabled(true);
        controlsPanel.numAnts.setEnabled(true);
        controlsPanel.evaporation.setEnabled(true);
        controlsPanel.pheromoneIncrease.setEnabled(true);
        controlsPanel.pheromoneExponent.setEnabled(true);
        controlsPanel.visibilityExponent.setEnabled(true);
        controlsPanel.minPathMultiplier.setEnabled(true);

        clickedPoints = new ArrayList<>();
        world = null;
        pheromoneMap = null;
        minPath = null;
        distances = null;
        controlsPanel.minDistance.setText("0");
        numIterations = 0;
        controlsPanel.iterations.setText("0");
        controlsPanel.numCities.setText("0");

        southPanel.series.clear();
        canvas.repaint();
    }

    private void startAgain() {
        world = null;
        pheromoneMap = null;
        minPath = null;
        controlsPanel.minDistance.setText("0");
        numIterations = 0;
        controlsPanel.iterations.setText("0");

        controlsPanel.numAnts.setEnabled(true);
        controlsPanel.evaporation.setEnabled(true);
        controlsPanel.pheromoneIncrease.setEnabled(true);
        controlsPanel.pheromoneExponent.setEnabled(true);
        controlsPanel.visibilityExponent.setEnabled(true);
        controlsPanel.minPathMultiplier.setEnabled(true);

        southPanel.series.clear();
        canvas.repaint();
    }

    private class Canvas extends JComponent {
        private boolean clickingEnabled = true;
        private int circleRadius = 10;
        private int lineStroke = 3;

        Canvas() {
            setBorder(BorderFactory.createBevelBorder(BevelBorder.LOWERED));
            addMouseListener(new MouseListener() {
                @Override
                public void mouseClicked(MouseEvent e) {
                }

                @Override
                public void mousePressed(MouseEvent e) {
                    if (clickingEnabled) {
                        clickedPoints.add(new Point(e.getX(), e.getY()));
                        controlsPanel.numCities.setText(String.valueOf(clickedPoints.size()));
                        repaint();
                    }
                }

                @Override
                public void mouseReleased(MouseEvent e) {

                }

                @Override
                public void mouseEntered(MouseEvent e) {

                }

                @Override
                public void mouseExited(MouseEvent e) {

                }
            });
        }

        @Override
        public void paintComponent(Graphics g) {
            super.paintComponent(g);
            Graphics2D graphics2D = (Graphics2D) g;
            graphics2D.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                    RenderingHints.VALUE_ANTIALIAS_ON);
            graphics2D.setPaintMode();
            graphics2D.setPaint(new Color(255, 255, 255, 255));
            graphics2D.fillRect(0, 0, getWidth(), getHeight());
            graphics2D.setPaint(new Color(0, 128, 255, 255));
            for (Point point : clickedPoints) {
                int x = point.x - circleRadius / 2;
                int y = point.y - circleRadius / 2;
                graphics2D.fillOval(x, y, circleRadius, circleRadius);
            }
            graphics2D.setStroke(new BasicStroke(lineStroke));
            if (pheromoneMap != null) {
                for (int i = 1; i < clickedPoints.size(); i++) {
                    for (int j = 0; j < i; j++) {
                        Point a = clickedPoints.get(i);
                        Point b = clickedPoints.get(j);
                        double maxPheromone = Util.maxPheromone(pheromoneMap, clickedPoints.size());
                        double ratio = pheromoneMap[i][j] / maxPheromone;
                        int alpha = Long.valueOf(Math.round(ratio * 255)).intValue();
                        graphics2D.setPaint(new Color(255, 128, 0, alpha));
                        graphics2D.drawLine(a.x, a.y, b.x, b.y);
                    }
                }
            }
            if (minPath != null && controlsPanel.showMinPath.isSelected()) {
                graphics2D.setPaint(new Color(0, 255, 0, 255));
                for (int i = 0; i < clickedPoints.size() - 1; i++) {
                    Point a = clickedPoints.get(minPath[i]);
                    Point b = clickedPoints.get(minPath[i + 1]);
                    graphics2D.drawLine(a.x, a.y, b.x, b.y);
                }

                graphics2D.setPaint(new Color(0, 0, 0, 255));
                int x = clickedPoints.get(minPath[0]).x - circleRadius / 2;
                int y = clickedPoints.get(minPath[0]).y - circleRadius / 2;
                graphics2D.fillOval(x, y, circleRadius, circleRadius);

                x = clickedPoints.get(minPath[clickedPoints.size() - 1]).x - circleRadius / 2;
                y = clickedPoints.get(minPath[clickedPoints.size() - 1]).y - circleRadius / 2;
                graphics2D.fillOval(x, y, circleRadius, circleRadius);


            }
            graphics2D.setPaint(new Color(255, 128, 0, 255));
            graphics2D.drawString("Pheromone map (click to draw cities)", 15, 15);
        }
    }

    private class ControlsPanel extends JPanel {
        private JButton iterate = new JButton("Iterate");
        private JButton reset = new JButton("Reset");
        private JButton startAgain = new JButton("Start again");
        private JButton randomize = new JButton("Randomize");
        private JTextField numCities = new JTextField("50", 5);
        private JTextField numAnts = new JTextField("50", 5);
        private JTextField evaporation = new JTextField("0.5", 5);
        private JTextField pheromoneIncrease = new JTextField("100", 5);
        private JTextField pheromoneExponent = new JTextField("1.0", 5);
        private JTextField visibilityExponent = new JTextField("1.0", 5);
        private JTextField minPathMultiplier = new JTextField("5", 5);
        private JTextField minDistance = new JTextField("0", 5);
        private JTextField iterations = new JTextField("0", 5);
        private JCheckBox showMinPath = new JCheckBox("Show min path", true);
        private JCheckBox showChart = new JCheckBox("Show chart", true);
        private JTextField iterationsAtOnce = new JTextField("50", 5);

        ControlsPanel() {
            setBorder(BorderFactory.createBevelBorder(BevelBorder.LOWERED));
            setLayout(new BorderLayout());

            minDistance.setEnabled(false);
            iterations.setEnabled(false);

            showMinPath.addItemListener(new ItemListener() {
                @Override
                public void itemStateChanged(ItemEvent e) {
                    canvas.repaint();
                }
            });
            showChart.addItemListener(new ItemListener() {
                @Override
                public void itemStateChanged(ItemEvent e) {
                    if (e.getStateChange() == ItemEvent.SELECTED) {
                        southPanel.setVisible(true);
                    } else {
                        southPanel.setVisible(false);
                    }
                }
            });

            startAgain.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    MainFrame.this.startAgain();
                }
            });
            iterate.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    if (clickedPoints.size() > 0) {
                        if (world == null) {
                            canvas.clickingEnabled = false;
                            numCities.setEnabled(false);
                            numAnts.setEnabled(false);
                            evaporation.setEnabled(false);
                            pheromoneIncrease.setEnabled(false);
                            pheromoneExponent.setEnabled(false);
                            visibilityExponent.setEnabled(false);
                            minPathMultiplier.setEnabled(false);
                            distances = new int[clickedPoints.size()][clickedPoints.size()];
                            for (int i = 1; i < clickedPoints.size(); i++) {
                                for (int j = 0; j < i; j++) {
                                    int distance = Util.distanceInt(clickedPoints.get(i), clickedPoints.get(j));
                                    distances[i][j] = distances[j][i] = distance;
                                }
                            }
                            world = new World(
                                    clickedPoints.size(),
                                    Integer.parseInt(numAnts.getText()),
                                    distances,
                                    Double.parseDouble(evaporation.getText()),
                                    Double.parseDouble(pheromoneIncrease.getText()),
                                    Double.parseDouble(pheromoneExponent.getText()),
                                    Double.parseDouble(visibilityExponent.getText()),
                                    Double.parseDouble(minPathMultiplier.getText()));
                            pheromoneMap = world.getPheromoneMap();
                        }
                        for (int i = 0; i < Integer.parseInt(iterationsAtOnce.getText()) - 1; i++) {
                            int minDistance = world.iterate();
                            southPanel.series.add(numIterations, minDistance);
                            world.resetAnts();
                            ++numIterations;
                        }
                        int minDistance = world.iterate();
                        southPanel.series.add(numIterations, minDistance);
                        minPath = world.getCopyOfMinPath();
                        canvas.repaint();
                        ControlsPanel.this.minDistance.setText(String.valueOf(minDistance));
                        ControlsPanel.this.iterations.setText(String.valueOf(++numIterations));
                        world.resetAnts();
                    }
                }
            });
            reset.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    MainFrame.this.reset();
                }
            });
            randomize.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    int numCitiesInt = Integer.parseInt(numCities.getText());
                    MainFrame.this.reset();
                    for (int i = 0; i < numCitiesInt; i++) {
                        clickedPoints.add(new Point(
                                Util.getRandomInt(
                                        2 * canvas.circleRadius,
                                        canvas.getWidth() - 2 * canvas.circleRadius),
                                Util.getRandomInt(
                                        2 * canvas.circleRadius,
                                        canvas.getHeight() - 2 * canvas.circleRadius)
                        ));
                    }
                    numCities.setText(String.valueOf(numCitiesInt));
                }
            });
            //region layout
            FormLayout layout = new FormLayout(
                    "fill:d, 3dlu, left:pref, 7dlu, fill:d, 3dlu, left:pref",
                    "p, 3dlu, p, 3dlu, p, 3dlu, p, 3dlu, p, 9dlu, " + //options
                            "p, 3dlu, p, 3dlu, p, 3dlu, p, 9dlu, " + //controls
                            "p, 3dlu, p, 9dlu"); //info
            layout.setColumnGroups(new int[][]{{1, 5}, {3, 7}});
            PanelBuilder builder = new PanelBuilder(layout);
            builder.setDefaultDialogBorder();

            CellConstraints cc = new CellConstraints();
            // Add a titled separator to cell (1, 1) that spans 7 columns.
            builder.addSeparator("Options", cc.xyw(1, 1, 7));
            builder.addLabel("Num. Cities", cc.xy(1, 3));
            builder.add(numCities, cc.xy(3, 3));
            builder.addLabel("Num. Ants", cc.xy(5, 3));
            builder.add(numAnts, cc.xy(7, 3));

            builder.addLabel("Evaporation", cc.xy(1, 5));
            builder.add(evaporation, cc.xy(3, 5));
            builder.addLabel("Pheromone increase", cc.xy(5, 5));
            builder.add(pheromoneIncrease, cc.xy(7, 5));

            builder.addLabel("Pheromone exponent", cc.xy(1, 7));
            builder.add(pheromoneExponent, cc.xy(3, 7));
            builder.addLabel("Visibility exponent", cc.xy(5, 7));
            builder.add(visibilityExponent, cc.xy(7, 7));

            builder.addLabel("Min path multiplier", cc.xy(1, 9));
            builder.add(minPathMultiplier, cc.xy(3, 9));
            builder.addLabel("Iterations at once", cc.xy(5, 9));
            builder.add(iterationsAtOnce, cc.xy(7, 9));

            builder.addSeparator("Controls", cc.xyw(1, 11, 7));
            builder.add(iterate, cc.xy(1, 13));
            builder.add(randomize, cc.xy(5, 13));
            builder.add(reset, cc.xy(1, 15));
            builder.add(startAgain, cc.xy(5, 15));
            builder.add(showMinPath, cc.xy(1, 17));
            builder.add(showChart, cc.xy(5, 17));

            builder.addSeparator("Info", cc.xyw(1, 19, 7));
            builder.addLabel("Iterations", cc.xy(1, 21));
            builder.add(iterations, cc.xy(3, 21));
            builder.addLabel("Min distance", cc.xy(5, 21));
            builder.add(minDistance, cc.xy(7, 21));
            add(builder.getPanel(), BorderLayout.CENTER);
            //endregion
        }
    }

    private class SouthPanel extends JPanel {
        private XYSeries series;
        private XYDataset dataset;
        private JFreeChart chart;

        SouthPanel() {
            setLayout(new BorderLayout());
            setBorder(BorderFactory.createBevelBorder(BevelBorder.LOWERED));
            setBackground(Color.white);
            setPreferredSize(new Dimension(MainFrame.this.getWidth(), 200));
            dataset = createDataset();
            chart = createChart(dataset);
            add(new ChartPanel(chart), BorderLayout.CENTER);
        }

        private XYDataset createDataset() {
            series = new XYSeries("series");

            XYSeriesCollection dataset = new XYSeriesCollection();
            dataset.addSeries(series);

            return dataset;
        }

        private JFreeChart createChart(XYDataset dataset) {
            JFreeChart chart = ChartFactory.createXYLineChart(
                    "Minimum distance at each iteration",
                    "Iteration",
                    "Min distance",
                    dataset,
                    PlotOrientation.VERTICAL,
                    false,
                    false,
                    false
            );

            XYPlot plot = chart.getXYPlot();

            XYLineAndShapeRenderer renderer = new XYLineAndShapeRenderer();
            renderer.setSeriesPaint(0, Color.GREEN);
            renderer.setSeriesStroke(0, new BasicStroke(2.0f));

            plot.setRenderer(renderer);
            plot.setBackgroundPaint(Color.white);

            plot.setRangeGridlinesVisible(true);
            plot.setRangeGridlinePaint(Color.BLACK);

            plot.setDomainGridlinesVisible(true);
            plot.setDomainGridlinePaint(Color.BLACK);

            return chart;
        }
    }
}
