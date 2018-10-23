package imageToPrime;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;

import javax.imageio.ImageIO;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JRadioButton;
import javax.swing.JTextField;
import javax.swing.JTextPane;
import javax.swing.SwingUtilities;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.text.MutableAttributeSet;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyledDocument;

public class imageToPrimeRunner {
	private static BufferedImage image;
	private static JTextField areaInput;
	private static int area;
	private static double scale;
	
	public static void main(String[] args) {
		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				JFileChooser fileChooser = new JFileChooser();
				
				JFrame frame = new JFrame("Image to Prime");
				frame.setSize(500,500);
				frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
				frame.setVisible(true);
				
				JButton btnNewButton = new JButton("Choose File");
				btnNewButton.setBounds(101, 16, 288, 29);
				btnNewButton.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent arg0) {
						try {
							fileChooser.setFileFilter(new FileNameExtensionFilter("Images", "jpg", "gif", "png"));
							fileChooser.showOpenDialog(null);
							File in = fileChooser.getSelectedFile();
							image = ImageIO.read(in);
							if (image == null) throw new IllegalArgumentException(in + " is not a JPG, GIF, or PNG");
						}
						catch(Exception e) {
							JOptionPane.showMessageDialog(null, e.toString(), "Error", JOptionPane.ERROR_MESSAGE);
						}
					}
				});
				
				String [] choosePrime = {"Integrate Prime Into Image", "Append Prime at Bottom"};
				frame.getContentPane().setLayout(null);
				frame.getContentPane().add(btnNewButton);
				
				JRadioButton integratePrime = new JRadioButton("Integrate Prime Into Image");
				integratePrime.setSelected(true);
				integratePrime.setActionCommand("0");
				integratePrime.setBounds(127, 57, 246, 29);
				frame.getContentPane().add(integratePrime);
				
				JRadioButton appendPrime = new JRadioButton("Append Prime Under Image");
				appendPrime.setActionCommand("1");
				appendPrime.setBounds(127, 94, 241, 29);
				frame.getContentPane().add(appendPrime);
				
				JRadioButton exactArea = new JRadioButton("Exact Area");
				exactArea.setActionCommand("2");
				exactArea.setBounds(127, 130, 241, 29);
				frame.getContentPane().add(exactArea);
				
				ButtonGroup primeType = new ButtonGroup();
				primeType.add(integratePrime);
				primeType.add(appendPrime);
				primeType.add(exactArea);
				SimpleAttributeSet center = new SimpleAttributeSet();
				
				JTextPane areaInfo = new JTextPane();
				areaInfo.setBackground(new Color(240, 240, 240));
				areaInfo.setFont(new Font("Monospace", Font.PLAIN, 14));
				areaInfo.setText("Enter an area value and the program will get the closest area to keep the picture's aspect ratio. If you want the prime number to have the exact number of digits you specify, choose Exact Area.");
				areaInfo.setEditable(false);
				areaInfo.setBounds(122, 188, 251, 139);
				StyledDocument doc = areaInfo.getStyledDocument();
				StyleConstants.setAlignment(center, StyleConstants.ALIGN_CENTER);
				doc.setParagraphAttributes(0, doc.getLength(), center, false);
				frame.getContentPane().add(areaInfo);
				
				areaInput = new JTextField();
				areaInput.setText("1100");
				areaInput.setBounds(177, 343, 146, 26);
				areaInput.setColumns(10);
				frame.getContentPane().add(areaInput);
				
				
				JButton btnBuild = new JButton("Build");
				btnBuild.setBounds(101, 399, 288, 29);
				btnBuild.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent arg0) {
						
						area = Integer.parseInt(areaInput.getText());
						
						BufferedImage scaleImage = resizeImage(image, area);
						String ascii = new ASCII().convert(scaleImage);
						String primeAscii = nextPrime.integrateNextPrime(ascii, scaleImage, area, primeType.getSelection().getActionCommand());
						
						JTextPane textArea = new JTextPane();
						textArea.setText(primeAscii);
						textArea.setFont(new Font("Monospaced", Font.BOLD, 20));
						textArea.setPreferredSize(new Dimension(textArea.getPreferredSize().width + 15, textArea.getPreferredSize().height + 10));
						textArea.setEditable(false);
						
						JDialog dialog = new JOptionPane(textArea, JOptionPane.PLAIN_MESSAGE).createDialog("Prime to ASCII Result");
						dialog.getContentPane().setLayout(new GridBagLayout());
						dialog.setResizable(true);
						dialog.setVisible(true);
					}
				});
				frame.getContentPane().add(btnBuild);
				
			}
		});
	}
	
	private static BufferedImage resizeImage(BufferedImage originalImage, int area) {
		scale = calculateDefaultSize(originalImage.getWidth(), originalImage.getHeight(), area);
		
		int width = (int) Math.floor(originalImage.getWidth() * scale);
		int height = (int) Math.floor(originalImage.getHeight() * scale);
		
		int fileType = originalImage.getType() == 0? BufferedImage.TYPE_INT_ARGB : originalImage.getType();

		BufferedImage resizedImage = new BufferedImage(width, height, fileType);
		Graphics2D graphics = resizedImage.createGraphics();
		graphics.drawImage(originalImage, 0, 0, width, height, null);
		graphics.dispose();

		return resizedImage;
	}
	
	public static double calculateDefaultSize(int width, int height, int area) {
		double scale = 1;
		
		while(((width * scale) * (height * scale)) > area) {
				scale = scale - 0.0001;
		}
			
		return scale;
		
	}
}