/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package java2ddrawingapplication;

import java.awt.BasicStroke;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Paint;
import java.awt.GridLayout;
import java.awt.Point;
import java.awt.Stroke;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;
import java.util.ArrayList;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JColorChooser;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.JSpinner;

/**
 *
 * @author acv
 */
public class DrawingApplicationFrame extends JFrame
{

    // Create the panels for the top of the application. One panel for each
    // line and one to contain both of those panels. 
    private final JPanel panel1;
    private final JPanel panel2;
    private final JPanel topPanel;    
    // create the widgets for the firstLine Panel.
    private final JComboBox shapeChosen;
    private final JButton color1Button; 
    private final JButton color2Button; 
     
    private final JButton undoButton; 
    private final JButton clearButton; 
    private final JLabel shapeLabel = new JLabel("Shape: ");
    
    //create the widgets for the secondLine Panel.
        //textbox for "Filled" "gradient" "dashed"
        //JSpinner for lineWidth, dashLength
    private JCheckBox filledBox; 
    private JCheckBox gradientBox; 
    private JCheckBox dashedBox; 
    private JSpinner lineWidthSpinner; 
    private JSpinner dashLengthSpinner; 
    private final JLabel optionLabel = new JLabel("Options: ");
    private final JLabel lineWidthLabel = new JLabel("Line Width: ");
    private final JLabel dashLengthLabel = new JLabel("Dash Length: ");
    
    // Variables for drawPanel.
    private final DrawPanel drawPanel; 
    
    // add status label
        private JLabel statusLabel;
    // Constructor for DrawingApplicationFrame
    public DrawingApplicationFrame()
    {
        super("Java 2D Drawings");
        setLayout(new BorderLayout());
        // firstLine widgets
        panel1 = new JPanel(); 
        panel2 = new JPanel(); 
        topPanel = new JPanel(); 
        shapeChosen = new JComboBox(new String[] {"Rectangle", "Oval", "Line"}); 
        color1Button = new JButton("1st Button..."); 
        color2Button = new JButton("2nd Button...");
        
        undoButton = new JButton("Undo");
        clearButton = new JButton("Clear");
        // secondLine widgets
        filledBox = new JCheckBox("Filled");
        gradientBox = new JCheckBox("User Gradient");
        dashedBox = new JCheckBox("Dashed"); 
        lineWidthSpinner = new JSpinner();
        dashLengthSpinner = new JSpinner();
        // add widgets to panels
            //panel1
        panel1.add(shapeLabel);
        panel1.add(shapeChosen);
        panel1.add(color1Button);
        panel1.add(color2Button);
        panel1.add(undoButton);
        panel1.add(clearButton);
        panel1.setBackground(Color.BLUE);
            //panel2
        panel2.add(optionLabel);
        panel2.add(filledBox);
        panel2.add(gradientBox);
        panel2.add(dashedBox);
        panel2.add(lineWidthLabel);
        panel2.add(lineWidthSpinner);
        panel2.add(dashLengthLabel);
        panel2.add(dashLengthSpinner);
        panel2.setBackground(Color.BLUE);
   
        // add top panel of two panels
        topPanel.setLayout(new GridLayout(2,1));
        topPanel.add(panel1);
        topPanel.add(panel2);
        
        //create drawPanel
        drawPanel = new DrawPanel(); 
        drawPanel.setBackground(Color.WHITE);
        
        statusLabel = new JLabel();
        statusLabel.setBackground(Color.LIGHT_GRAY);

        // add topPanel to North, drawPanel to Center, and statusLabel to South
        add(topPanel, BorderLayout.NORTH);
        add(drawPanel, BorderLayout.CENTER);
        add(statusLabel, BorderLayout.SOUTH);
        //add listeners and event handlers

        color1Button.addActionListener(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent event){
                drawPanel.color1 = JColorChooser.showDialog(DrawingApplicationFrame.this, "choose a color", drawPanel.color1);
            }
        }); 
        color2Button.addActionListener(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent event){
                drawPanel.color2 = JColorChooser.showDialog(DrawingApplicationFrame.this, "choose a color", drawPanel.color2);
            }
        });
        undoButton.addActionListener(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent event){
                if (drawPanel.shapes.isEmpty()){;
                }else{
                    drawPanel.shapes.remove(drawPanel.shapes.size()-1);}
                    drawPanel.repaint();
            }
        });
        clearButton.addActionListener(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent event){
                if (drawPanel.shapes.isEmpty()){;
                }else{
                    drawPanel.shapes.clear();
                    drawPanel.repaint();
                }
                
            }
        });
        
        
    }

    // Create event handlers, if needed

    // Create a private inner class for the DrawPanel.

    private class DrawPanel extends JPanel
    {
        ArrayList<MyShapes> shapes;
        private MyShapes currentShape;
        private boolean filled = false;  
        private int lineWidth; 
        private float[] dashLength; 
        private Color color1 = Color.BLACK; 
        private Color color2 = Color.BLACK; 
        private Paint paint;
        private Stroke stroke;
        private String test;
        
        public DrawPanel()
        {   
            
            shapes = new ArrayList<>();
            test = new String();
            addMouseListener(new MouseHandler());
            addMouseMotionListener(new MouseHandler());
            
        }

        public void paintComponent(Graphics g)
        {
            super.paintComponent(g);
            Graphics2D g2d = (Graphics2D) g;

            //loop through and draw each shape in the shapes arraylist
            for (MyShapes shape: shapes){
                shape.draw(g2d);
            }
        }


        private class MouseHandler extends MouseAdapter implements MouseMotionListener
        {
            @Override
            public void mousePressed(MouseEvent event)
            {
                lineWidth = (int) lineWidthSpinner.getValue();
                dashLength = new float[] {(int)dashLengthSpinner.getValue()};
                if(gradientBox.isSelected()){
                    paint = new GradientPaint(0, 0, color1, 50, 50, color2, true);
                }else{
                    paint = color1;
                }
                if (dashedBox.isSelected() && dashLength[0] != 0){
                    stroke = new BasicStroke(lineWidth, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND, 10, dashLength, 0);
                } else{
                    stroke = new BasicStroke(lineWidth, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND);
                }
                if (filledBox.isSelected()){filled = true;}
                if (shapeChosen.getSelectedItem().toString() == "Rectangle"){
                    shapes.add(new MyRectangle(new Point(event.getX(), event.getY()), new Point(event.getX(), event.getY()), paint, stroke,filled));    
                }else if (shapeChosen.getSelectedItem().toString()== "Oval"){
                    shapes.add(new MyOval(new Point(event.getX(), event.getY()), new Point(event.getX(), event.getY()), paint, stroke,filled));
                }else if (shapeChosen.getSelectedItem().toString()== "Line"){
                    shapes.add(new MyLine(new Point(event.getX(), event.getY()), new Point(event.getX(), event.getY()), paint, stroke));
                }
                filled = false;
                repaint();
            }
            @Override
            public void mouseReleased(MouseEvent event)
            {
               currentShape = shapes.get(shapes.size()-1);
               currentShape.setEndPoint(new Point(event.getX(), event.getY()));
               repaint();
            }

            @Override
            public void mouseDragged(MouseEvent event)
            {
                currentShape = shapes.get(shapes.size()-1);
                currentShape.setEndPoint(new Point(event.getX(), event.getY()));
                repaint();
            }
            
            @Override
            public void mouseMoved(MouseEvent event)
            {
                statusLabel.setText(String.format("%d,%d", event.getX(),event.getY()));
                
            }
        }

    }
}