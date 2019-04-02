package ro.ulbsibiu.indinfo.ants.gui;

import com.jgoodies.forms.builder.PanelBuilder;
import com.jgoodies.forms.layout.CellConstraints;
import com.jgoodies.forms.layout.FormLayout;

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
    private Canvas canvas = new Canvas();
    private ArrayList<Point> clickedPoints = new ArrayList<>();

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
        JMenuItem viewOptions = new JCheckBoxMenuItem("Options Panel");
        viewOptions.setMnemonic(KeyEvent.VK_R);
        viewOptions.setSelected(true);
        viewOptions.addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent e) {
                if (e.getStateChange() == ItemEvent.SELECTED) {
                    controlsPanel.setVisible(true);
                } else {
                    controlsPanel.setVisible(false);
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
        viewMenu.add(viewOptions);
        JMenuBar menuBar = new JMenuBar();
        menuBar.add(fileMenu);
        menuBar.add(viewMenu);
        menuBar.add(helpMenu);
        setJMenuBar(menuBar);
        //endregion

        setLayout(new BorderLayout());
        add(canvas, BorderLayout.CENTER);
        add(controlsPanel, BorderLayout.EAST);

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

    private class Canvas extends JComponent {

        private int circleRadius = 10;

        public Canvas() {
            setBorder(BorderFactory.createBevelBorder(BevelBorder.LOWERED));
            addMouseListener(new MouseListener() {
                @Override
                public void mouseClicked(MouseEvent e) {
                }

                @Override
                public void mousePressed(MouseEvent e) {
                    clickedPoints.add(new Point(e.getX(), e.getY()));
                    repaint();
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
            graphics2D.setStroke(new BasicStroke(3));
            graphics2D.setPaint(new Color(255, 0, 0, 60));
            graphics2D.setPaintMode();
            graphics2D.drawLine(50, 50, 100, 100);

            graphics2D.setPaint(new Color(0, 255, 0, 255));
            for (Point point : clickedPoints) {
                int x = point.x - circleRadius / 2;
                int y = point.y - circleRadius / 2;
                graphics2D.fillOval(x, y, circleRadius, circleRadius);
            }
            graphics2D.drawString(String.valueOf(clickedPoints.size()), 20, 20);
        }
    }

    private class ControlsPanel extends JPanel {
        ControlsPanel() {
            setBorder(BorderFactory.createBevelBorder(BevelBorder.LOWERED));
            setLayout(new BorderLayout());
            FormLayout layout = new FormLayout(
                    "right:pref, 3dlu, left:pref, 7dlu, right:pref, 3dlu, left:pref",
                    "p, 3dlu, p, 3dlu, p, 3dlu, p, 3dlu, p, 9dlu, " +
                            "p, 3dlu, p, 3dlu, p, 3dlu, p, 9dlu, " +
                            "p, 3dlu, p, 9dlu");
            layout.setColumnGroups(new int[][]{{1, 5}, {3, 7}});
            PanelBuilder builder = new PanelBuilder(layout);
            builder.setDefaultDialogBorder();

            CellConstraints cc = new CellConstraints();
            // Add a titled separator to cell (1, 1) that spans 7 columns.
            builder.addSeparator("Options", cc.xyw(1, 1, 7));
            builder.addLabel("Num. Cities", cc.xy(1, 3));
            builder.add(new JTextField("10"), cc.xy(3, 3));
            builder.addLabel("Num. Ants", cc.xy(5, 3));
            builder.add(new JTextField("10"), cc.xy(7, 3));

            builder.addLabel("Evaporation", cc.xy(1, 5));
            builder.add(new JTextField("10"), cc.xy(3, 5));
            builder.addLabel("Pheromone increase", cc.xy(5, 5));
            builder.add(new JTextField("10"), cc.xy(7, 5));

            builder.addLabel("Pheromone exponent", cc.xy(1, 7));
            builder.add(new JTextField("10"), cc.xy(3, 7));
            builder.addLabel("Visibility exponent", cc.xy(5, 7));
            builder.add(new JTextField("10"), cc.xy(7, 7));

            builder.addLabel("Best path factor", cc.xy(1, 9));
            builder.add(new JTextField("10"), cc.xy(3, 9));
            builder.addLabel("Other option", cc.xy(5, 9));
            builder.add(new JTextField("10"), cc.xy(7, 9));

            builder.addSeparator("Controls", cc.xyw(1, 11, 7));
            builder.add(new JButton("Iterate"), cc.xy(1, 13));
            builder.add(new JButton("Randomize"), cc.xy(5, 13));
            builder.add(new JButton("Reset"), cc.xy(1, 15));
            builder.add(new JButton("Clear"), cc.xy(5, 15));
            builder.addLabel("Show diagram", cc.xy(1, 17));
            builder.add(new JCheckBox(), cc.xy(3, 17));
            builder.addLabel("Show chart", cc.xy(5, 17));
            builder.add(new JCheckBox(), cc.xy(7, 17));

            builder.addSeparator("Info", cc.xyw(1, 19, 7));
            builder.addLabel("Num. elitist ants", cc.xy(1, 21));
            builder.add(new JTextField("35"), cc.xy(3, 21));
            builder.addLabel("Best distance", cc.xy(5, 21));
            builder.add(new JTextField("135"), cc.xy(7, 21));
            add(builder.getPanel(), BorderLayout.CENTER);
        }
    }
}
