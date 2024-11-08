import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.HashMap;

public class LibraryManagementSystem extends JFrame {

    // Data structures for storing book information
    private ArrayList<HashMap<String, String>> books;
    private DefaultTableModel tableModel;

    // GUI Components
    private JTextField titleField, authorField, isbnField, yearField, searchField;
    private JComboBox<String> genreBox;
    private JCheckBox availabilityBox;
    private JTable bookTable;

    public LibraryManagementSystem() {
        books = new ArrayList<>();
        initializeUI();
    }

    private void initializeUI() {
        setTitle("Library Management System");
        setSize(800, 600);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        // MenuBar setup
        JMenuBar menuBar = new JMenuBar();
        JMenu fileMenu = new JMenu("File");
        JMenu editMenu = new JMenu("Edit");
        JMenu helpMenu = new JMenu("Help");
        menuBar.add(fileMenu);
        menuBar.add(editMenu);
        menuBar.add(helpMenu);
        setJMenuBar(menuBar);

        // Toolbar setup
        JToolBar toolBar = new JToolBar();
        JButton addButton = new JButton("Add Book");
        JButton removeButton = new JButton("Remove Book");
        JButton searchButton = new JButton("Search");
        toolBar.add(addButton);
        toolBar.add(removeButton);
        toolBar.add(searchButton);
        add(toolBar, BorderLayout.NORTH);

        // TabbedPane setup
        JTabbedPane tabbedPane = new JTabbedPane();
        tabbedPane.add("Book Details", createBookDetailsPanel());
        tabbedPane.add("Book List", createBookListPanel());
        add(tabbedPane, BorderLayout.CENTER);

        // Event Handlers
        addButton.addActionListener(e -> addBook());
        removeButton.addActionListener(e -> removeBook());
        searchButton.addActionListener(e -> searchBook());

        setVisible(true);
    }

    // Book Details Panel
    private JPanel createBookDetailsPanel() {
        JPanel bookDetailsPanel = new JPanel(new GridLayout(6, 2));

        bookDetailsPanel.add(new JLabel("Title:"));
        titleField = new JTextField();
        bookDetailsPanel.add(titleField);

        bookDetailsPanel.add(new JLabel("Author:"));
        authorField = new JTextField();
        bookDetailsPanel.add(authorField);

        bookDetailsPanel.add(new JLabel("ISBN:"));
        isbnField = new JTextField();
        bookDetailsPanel.add(isbnField);

        bookDetailsPanel.add(new JLabel("Publication Year:"));
        yearField = new JTextField();
        bookDetailsPanel.add(yearField);

        bookDetailsPanel.add(new JLabel("Genre:"));
        genreBox = new JComboBox<>(new String[]{"Fiction", "Non-Fiction", "Science", "Biography", "Fantasy"});
        bookDetailsPanel.add(genreBox);

        bookDetailsPanel.add(new JLabel("Available:"));
        availabilityBox = new JCheckBox();
        bookDetailsPanel.add(availabilityBox);

        JButton addBookButton = new JButton("Add Book");
        JButton updateBookButton = new JButton("Update Book");
        bookDetailsPanel.add(addBookButton);
        bookDetailsPanel.add(updateBookButton);

        // Event Listeners for buttons
        addBookButton.addActionListener(e -> addBook());
        updateBookButton.addActionListener(e -> updateBook());

        return bookDetailsPanel;
    }

    // Book List Panel
    private JPanel createBookListPanel() {
        JPanel bookListPanel = new JPanel(new BorderLayout());

        // Table for displaying books
        String[] columnNames = {"Title", "Author", "ISBN", "Genre", "Available"};
        tableModel = new DefaultTableModel(columnNames, 0);
        bookTable = new JTable(tableModel);
        JScrollPane scrollPane = new JScrollPane(bookTable);
        bookListPanel.add(scrollPane, BorderLayout.CENTER);

        // Search bar and button
        JPanel searchPanel = new JPanel();
        searchField = new JTextField(20);
        JButton searchButton = new JButton("Search");
        searchPanel.add(searchField);
        searchPanel.add(searchButton);
        bookListPanel.add(searchPanel, BorderLayout.NORTH);

        searchButton.addActionListener(e -> searchBook());

        return bookListPanel;
    }

    // Add Book method
    private void addBook() {
        String title = titleField.getText();
        String author = authorField.getText();
        String isbn = isbnField.getText();
        String year = yearField.getText();
        String genre = genreBox.getSelectedItem().toString();
        String available = availabilityBox.isSelected() ? "Yes" : "No";

        // Add book to data structure
        HashMap<String, String> book = new HashMap<>();
        book.put("Title", title);
        book.put("Author", author);
        book.put("ISBN", isbn);
        book.put("Year", year);
        book.put("Genre", genre);
        book.put("Available", available);
        books.add(book);

        // Add book to table
        tableModel.addRow(new Object[]{title, author, isbn, genre, available});

        // Clear fields after adding
        titleField.setText("");
        authorField.setText("");
        isbnField.setText("");
        yearField.setText("");
        availabilityBox.setSelected(false);
    }

    // Update Book method
    private void updateBook() {
        int selectedRow = bookTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Please select a book to update.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Update data structure
        String title = titleField.getText();
        String author = authorField.getText();
        String isbn = isbnField.getText();
        String year = yearField.getText();
        String genre = genreBox.getSelectedItem().toString();
        String available = availabilityBox.isSelected() ? "Yes" : "No";

        HashMap<String, String> book = books.get(selectedRow);
        book.put("Title", title);
        book.put("Author", author);
        book.put("ISBN", isbn);
        book.put("Year", year);
        book.put("Genre", genre);
        book.put("Available", available);

        // Update table
        tableModel.setValueAt(title, selectedRow, 0);
        tableModel.setValueAt(author, selectedRow, 1);
        tableModel.setValueAt(isbn, selectedRow, 2);
        tableModel.setValueAt(genre, selectedRow, 3);
        tableModel.setValueAt(available, selectedRow, 4);
    }

    // Remove Book method
    private void removeBook() {
        int selectedRow = bookTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Please select a book to remove.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        int confirm = JOptionPane.showConfirmDialog(this, "Are you sure you want to remove this book?", "Confirm", JOptionPane.YES_NO_OPTION);
        if (confirm == JOptionPane.YES_OPTION) {
            // Remove from data structure
            books.remove(selectedRow);

            // Remove from table
            tableModel.removeRow(selectedRow);
        }
    }

    // Search Book method
    private void searchBook() {
        String searchTerm = searchField.getText().toLowerCase();
        if (searchTerm.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please enter a search term.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        for (int i = 0; i < books.size(); i++) {
            HashMap<String, String> book = books.get(i);
            if (book.get("Title").toLowerCase().contains(searchTerm) || book.get("Author").toLowerCase().contains(searchTerm)) {
                bookTable.setRowSelectionInterval(i, i);
                break;
            }
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(LibraryManagementSystem::new);
    }
}
