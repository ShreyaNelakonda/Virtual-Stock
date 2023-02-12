package view;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Graphics;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.Icon;
import javax.swing.border.Border;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.border.MatteBorder;

/**
 * A class to generate Barchart for the given x and y values.
 */
public class BarPanel extends JPanel {
  private final int histogramHeight = 200;
  private final int barWidth = 20;
  private final int barGap = 1;

  private final JPanel barChartPanel;
  private final JPanel xLabelPanel;

  private final List<Bar> bars = new ArrayList<Bar>();

  /**
   * Constructor that initializes Bar Chart values.
   */
  public BarPanel() {
    setBorder(new EmptyBorder(10, 10, 10, 10));
    setLayout(new BorderLayout());
    barChartPanel = new JPanel(new GridLayout(1, 0, barGap, 0));
    Border outer = new MatteBorder(1, 1, 1, 1, Color.BLACK);
    Border inner = new EmptyBorder(10, 10, 0, 10);
    Border compound = new CompoundBorder(outer, inner);
    barChartPanel.setBorder(compound);
    xLabelPanel = new JPanel(new GridLayout(1, 0, barGap, 0));
    xLabelPanel.setBorder(new EmptyBorder(5, 10, 0, 10));

    add(barChartPanel, BorderLayout.CENTER);
    add(xLabelPanel, BorderLayout.PAGE_END);
  }

  public void addBar(String label, int value, Color color) {
    Bar bar = new Bar(label, value, color);
    bars.add(bar);
  }

  /**
   * Sets the layout of the bar chart.
   */
  public void formatBarChart() {
    barChartPanel.removeAll();
    xLabelPanel.removeAll();
    int maxValue = 0;
    for (Bar bar : bars) {
      maxValue = Math.max(maxValue, bar.getValue());
    }

    for (Bar bar : bars) {
      JLabel label = new JLabel(bar.getValue() + "");
      //label.setHorizontalAlignment(11);
      label.setHorizontalTextPosition(JLabel.CENTER);
      label.setHorizontalAlignment(JLabel.CENTER);
      label.setVerticalTextPosition(JLabel.TOP);
      label.setVerticalAlignment(JLabel.BOTTOM);
      int barHeight = (bar.getValue() * histogramHeight) / maxValue;
      Icon icon = new BarColor(bar.getColor(), barWidth, barHeight);
      label.setIcon(icon);
      barChartPanel.add(label);

      JLabel barLabel = new JLabel(bar.getLabel());
      barLabel.setHorizontalAlignment(JLabel.CENTER);
      xLabelPanel.add(barLabel);
    }
  }

  private class Bar {
    private final String label;
    private final int value;
    private final Color color;

    public Bar(String label, int value, Color color) {
      this.label = label;
      this.value = value;
      this.color = color;
    }

    public String getLabel() {
      return label;
    }

    public int getValue() {
      return value;
    }

    public Color getColor() {
      return color;
    }
  }

  private class BarColor implements Icon {
    private final int shadow = 3;

    private final Color color;
    private final int width;
    private final int height;

    public BarColor(Color color, int width, int height) {
      this.color = color;
      this.width = width;
      this.height = height;
    }

    @Override
    public void paintIcon(Component c, Graphics g, int x, int y) {
      g.setColor(color);
      g.fillRect(x, y, width - shadow, height);
      g.setColor(Color.GRAY);
      g.fillRect(x + width - shadow, y + shadow, shadow, height - shadow);
    }

    @Override
    public int getIconWidth() {
      return width;
    }

    @Override
    public int getIconHeight() {
      return height;
    }
  }
}
