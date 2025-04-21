package core;

import javax.swing.*;

import panel.AddBookPanel;
import panel.AddWriterPanel;
import panel.ManagerPanel;
import panel.ReaderLoginPanel;
import panel.ReaderMainPanel;
import panel.ReaderPanel;
import panel.ReaderSignupPanel;
import panel.TopRatedBooksPanel;
import panel.WriterLoginPanel;
import panel.WriterMainPanel;
import panel.WriterPanel;
import panel.ManagerReportPanel;
import java.awt.*;
import java.sql.Connection;
import java.sql.SQLException;

public class MainFrame extends JFrame {

    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public MainFrame(){
    	setTitle("Yellow Petal Publishing House");
        setSize(700, 500); // wider window to fit the title
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null); // center the window on screen
        
        // Set application icon
        ImageIcon appIcon = new ImageIcon("C:\\Users\\ENES\\Desktop\\Veritabani\\src\\core\\Yellow_Petal.jpg");
        setIconImage(appIcon.getImage());
        
        Connection conn = null;
        try {
            conn = ObjectHelper.getConnection();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        CardLayout cardLayout = new CardLayout();
        JPanel cardPanel = new JPanel(cardLayout);
        
        // Create the image for display in panels
        ImageIcon logoImage = new ImageIcon("Yellow_Petal.jpg");
        Image scaledImage = logoImage.getImage().getScaledInstance(200, 150, Image.SCALE_SMOOTH);
        logoImage = new ImageIcon(scaledImage);
        JLabel imageLabel = new JLabel(logoImage);
        
        // Create a panel for the main menu that includes the image
        JPanel mainMenuWithLogo = new JPanel(new BorderLayout());
        mainMenuWithLogo.add(imageLabel, BorderLayout.NORTH);
        mainMenuWithLogo.add(new MainMenu(cardLayout, cardPanel), BorderLayout.CENTER);
        
        ReaderLoginPanel readerLogin = new ReaderLoginPanel(cardLayout, cardPanel, new ReaderDAO(conn));
        WriterLoginPanel writerLogin = new WriterLoginPanel(cardLayout, cardPanel, new WriterDAO(conn));
        
        // Create and add panels
        cardPanel.add(mainMenuWithLogo, "mainMenu");
        cardPanel.add(new ReaderPanel(cardLayout, cardPanel), "readerPanel");
        cardPanel.add(new ManagerPanel(cardLayout, cardPanel), "managerPanel");
        cardPanel.add(new WriterPanel(cardLayout, cardPanel), "writerPanel");
        cardPanel.add(new AddWriterPanel(cardLayout, cardPanel, new WriterDAO(conn)), "addWriterPanel");
        cardPanel.add(new AddBookPanel(cardLayout, cardPanel, new WriterDAO(conn), new BookDAO(conn)), "addBookPanel");
        cardPanel.add(new ReaderSignupPanel(cardLayout, cardPanel, new ReaderDAO(conn)), "readerSignUpPanel");
        cardPanel.add(readerLogin, "readerLoginPanel");
        cardPanel.add(new ReaderMainPanel(cardLayout, cardPanel, new ReaderDAO(conn), new BookDAO(conn), new WriterDAO(conn), new FeedbackDAO(conn),
                readerLogin), "readerMainPanel");
        cardPanel.add(writerLogin, "writerLoginPanel");
        cardPanel.add(new WriterMainPanel(cardLayout, cardPanel, new WriterDAO(conn), new FeedbackDAO(conn), 0), "writerMainPanel");
        cardPanel.add(new ManagerReportPanel(cardLayout, cardPanel, new FeedbackDAO(conn), new WriterDAO(conn), new BookDAO(conn)), "managerReportPanel");
        cardPanel.add(new TopRatedBooksPanel(cardLayout, cardPanel, new FeedbackDAO(conn), new BookDAO(conn)), "topRated");
        
        
        add(cardPanel);
        setVisible(true);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(MainFrame::new);
    }
}