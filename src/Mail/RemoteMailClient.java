package Mail;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.io.*;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

import item.Item;
import item.Item.ItemOption;

public class RemoteMailClient extends JFrame {

    private JTextField txtServerIP, txtServerPort, txtUsername, txtPassword;
    private JTextField txtRecipient, txtTitle, txtTitle2, txtContent, txtQuantity, txtParam;
    private JTextField txtItemSearch, txtOptionSearch;
    private JComboBox<String> comboItems, comboOptions;
    private JTable tableItems, tableOptions;
    private DefaultTableModel itemTableModel, optionTableModel;
    private List<Item> itemList = new ArrayList<>();
    private List<ItemOption> currentOptions = new ArrayList<>();

    // Data storage for filtering
    private List<String> allItems = new ArrayList<>();
    private List<String> allOptions = new ArrayList<>();

    // Button references for UI updates
    private JButton btnConnect, btnDisconnect, btnSend;

    private Socket serverSocket;
    private DataOutputStream out;
    private DataInputStream in;
    private boolean connected = false;

    public RemoteMailClient() {
        setTitle("Remote Mail Client - Gửi Thư Từ Xa");
        setSize(900, 700);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        // Main Panel
        JPanel mainPanel = new JPanel(new BorderLayout());
        add(mainPanel);

        // Connection Panel
        JPanel connectionPanel = createConnectionPanel();
        mainPanel.add(connectionPanel, BorderLayout.NORTH);

        // Content Panel
        JPanel contentPanel = new JPanel(new BorderLayout());

        // Input Panel
        JPanel inputPanel = createInputPanel();
        contentPanel.add(inputPanel, BorderLayout.NORTH);

        // Center Panel with Tables
        JPanel centerPanel = createCenterPanel();
        contentPanel.add(centerPanel, BorderLayout.CENTER);

        // Send Button
        JButton btnSend = new JButton("Gửi Thư");
        btnSend.setEnabled(false);

        // Store send button reference
        this.btnSend = btnSend;
        contentPanel.add(btnSend, BorderLayout.SOUTH);

        mainPanel.add(contentPanel, BorderLayout.CENTER);

        // Action Listeners
        btnSend.addActionListener(e -> sendMail());
    }

    private JPanel createConnectionPanel() {
        JPanel panel = new JPanel(new GridLayout(2, 4, 10, 10));
        panel.setBorder(BorderFactory.createTitledBorder("Kết Nối Server"));

        panel.add(new JLabel("Server IP:"));
        txtServerIP = new JTextField("localhost");
        panel.add(txtServerIP);

        panel.add(new JLabel("Port:"));
        txtServerPort = new JTextField("14445");
        panel.add(txtServerPort);

        panel.add(new JLabel("Username:"));
        txtUsername = new JTextField("admin");
        panel.add(txtUsername);

        panel.add(new JLabel("Password:"));
        txtPassword = new JPasswordField();
        panel.add(txtPassword);

        JButton btnConnect = new JButton("Kết Nối");
        JButton btnDisconnect = new JButton("Ngắt Kết Nối");
        btnDisconnect.setEnabled(false);

        // Store button references
        this.btnConnect = btnConnect;
        this.btnDisconnect = btnDisconnect;

        panel.add(btnConnect);
        panel.add(btnDisconnect);

        btnConnect.addActionListener(e -> connectToServer());
        btnDisconnect.addActionListener(e -> disconnectFromServer());

        return panel;
    }

    private JPanel createInputPanel() {
        JPanel panel = new JPanel(new GridLayout(5, 2, 10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        panel.add(new JLabel("Tên người nhận:"));
        txtRecipient = new JTextField();
        panel.add(txtRecipient);

        panel.add(new JLabel("Tên thư:"));
        txtTitle = new JTextField();
        panel.add(txtTitle);

        panel.add(new JLabel("Tiêu đề thư:"));
        txtTitle2 = new JTextField();
        panel.add(txtTitle2);

        panel.add(new JLabel("Nội dung:"));
        txtContent = new JTextField();
        panel.add(txtContent);

        return panel;
    }

    private JPanel createCenterPanel() {
        JPanel centerPanel = new JPanel(new GridLayout(2, 1, 10, 10));

        // Options Table
        optionTableModel = new DefaultTableModel(new String[] { "ID Option", "Tên Option", "Param" }, 0);
        tableOptions = new JTable(optionTableModel);
        JScrollPane optionScrollPane = new JScrollPane(tableOptions);

        JPanel optionPanel = new JPanel(new BorderLayout());
        optionPanel.add(new JLabel("Danh sách Options"), BorderLayout.NORTH);
        optionPanel.add(optionScrollPane, BorderLayout.CENTER);

        JPanel optionButtonPanel = new JPanel(new FlowLayout());

        // Option search panel
        JPanel optionSearchPanel = new JPanel(new BorderLayout());
        optionSearchPanel.add(new JLabel("Tìm Option:"), BorderLayout.WEST);
        txtOptionSearch = new JTextField(15);
        txtOptionSearch.setToolTipText("Nhập tên option để tìm kiếm...");
        optionSearchPanel.add(txtOptionSearch, BorderLayout.CENTER);

        comboOptions = new JComboBox<>(getOptionNames());
        comboOptions.setEditable(true);
        comboOptions.setToolTipText("Chọn option hoặc nhập để tìm kiếm");

        txtParam = new JTextField(5);
        JButton btnAddOption = new JButton("Thêm Option");
        JButton btnRemoveOption = new JButton("Xóa Option");

        optionButtonPanel.add(optionSearchPanel);
        optionButtonPanel.add(new JLabel("Chọn Option:"));
        optionButtonPanel.add(comboOptions);
        optionButtonPanel.add(new JLabel("Param:"));
        optionButtonPanel.add(txtParam);
        optionButtonPanel.add(btnAddOption);
        optionButtonPanel.add(btnRemoveOption);

        optionPanel.add(optionButtonPanel, BorderLayout.SOUTH);
        centerPanel.add(optionPanel);

        // Items Table
        itemTableModel = new DefaultTableModel(new String[] { "ID Vật phẩm", "Tên Vật phẩm", "Số lượng", "Options" },
                0);
        tableItems = new JTable(itemTableModel);
        JScrollPane itemScrollPane = new JScrollPane(tableItems);

        JPanel itemPanel = new JPanel(new BorderLayout());
        itemPanel.add(new JLabel("Danh sách Vật phẩm"), BorderLayout.NORTH);
        itemPanel.add(itemScrollPane, BorderLayout.CENTER);

        JPanel itemButtonPanel = new JPanel(new FlowLayout());

        // Item search panel
        JPanel itemSearchPanel = new JPanel(new BorderLayout());
        itemSearchPanel.add(new JLabel("Tìm Vật phẩm:"), BorderLayout.WEST);
        txtItemSearch = new JTextField(15);
        txtItemSearch.setToolTipText("Nhập tên vật phẩm để tìm kiếm...");
        itemSearchPanel.add(txtItemSearch, BorderLayout.CENTER);

        comboItems = new JComboBox<>(getItemNames());
        comboItems.setEditable(true);
        comboItems.setToolTipText("Chọn vật phẩm hoặc nhập để tìm kiếm");

        txtQuantity = new JTextField(5);
        JButton btnAddItem = new JButton("Thêm Vật phẩm");
        JButton btnRemoveItem = new JButton("Xóa Vật phẩm");

        itemButtonPanel.add(itemSearchPanel);
        itemButtonPanel.add(new JLabel("Chọn Vật phẩm:"));
        itemButtonPanel.add(comboItems);
        itemButtonPanel.add(new JLabel("Số lượng:"));
        itemButtonPanel.add(txtQuantity);
        itemButtonPanel.add(btnAddItem);
        itemButtonPanel.add(btnRemoveItem);

        itemPanel.add(itemButtonPanel, BorderLayout.SOUTH);
        centerPanel.add(itemPanel);

        // Action Listeners
        btnAddOption.addActionListener(e -> addOption());
        btnRemoveOption.addActionListener(e -> removeOption());
        btnAddItem.addActionListener(e -> addItem());
        btnRemoveItem.addActionListener(e -> removeItem());

        // Search listeners
        txtItemSearch.addActionListener(e -> filterItems());
        txtOptionSearch.addActionListener(e -> filterOptions());

        // Real-time search
        txtItemSearch.getDocument().addDocumentListener(new javax.swing.event.DocumentListener() {
            public void insertUpdate(javax.swing.event.DocumentEvent e) {
                filterItems();
            }

            public void removeUpdate(javax.swing.event.DocumentEvent e) {
                filterItems();
            }

            public void changedUpdate(javax.swing.event.DocumentEvent e) {
                filterItems();
            }
        });

        txtOptionSearch.getDocument().addDocumentListener(new javax.swing.event.DocumentListener() {
            public void insertUpdate(javax.swing.event.DocumentEvent e) {
                filterOptions();
            }

            public void removeUpdate(javax.swing.event.DocumentEvent e) {
                filterOptions();
            }

            public void changedUpdate(javax.swing.event.DocumentEvent e) {
                filterOptions();
            }
        });

        return centerPanel;
    }

    private void connectToServer() {
        try {
            String serverIP = txtServerIP.getText().trim();
            int serverPort = Integer.parseInt(txtServerPort.getText().trim());
            String username = txtUsername.getText().trim();
            String password = txtPassword.getText().trim();

            if (serverIP.isEmpty() || username.isEmpty() || password.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Vui lòng nhập đầy đủ thông tin kết nối!", "Lỗi",
                        JOptionPane.ERROR_MESSAGE);
                return;
            }

            // Kết nối đến server
            serverSocket = new Socket(serverIP, serverPort);
            out = new DataOutputStream(serverSocket.getOutputStream());
            in = new DataInputStream(serverSocket.getInputStream());

            // Gửi thông tin đăng nhập
            out.writeUTF("LOGIN");
            out.writeUTF(username);
            out.writeUTF(password);

            // Nhận phản hồi
            String response = in.readUTF();
            if ("LOGIN_SUCCESS".equals(response)) {
                connected = true;

                // Nhận danh sách items
                int itemCount = in.readInt();
                List<String> items = new ArrayList<>();
                for (int i = 0; i < itemCount; i++) {
                    int id = in.readInt();
                    String name = in.readUTF();
                    items.add(id + " - " + name);
                }

                // Nhận danh sách options
                int optionCount = in.readInt();
                List<String> options = new ArrayList<>();
                for (int i = 0; i < optionCount; i++) {
                    int id = in.readInt();
                    String name = in.readUTF();
                    options.add(id + " - " + name);
                }

                // Cập nhật combo boxes
                updateComboBoxes(items, options);

                JOptionPane.showMessageDialog(this,
                        "Kết nối thành công! Đã tải " + itemCount + " vật phẩm và " + optionCount + " options.",
                        "Thành công",
                        JOptionPane.INFORMATION_MESSAGE);
                updateConnectionUI(true);
            } else {
                JOptionPane.showMessageDialog(this, "Đăng nhập thất bại: " + response, "Lỗi",
                        JOptionPane.ERROR_MESSAGE);
                disconnectFromServer();
            }

        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Lỗi kết nối: " + e.getMessage(), "Lỗi", JOptionPane.ERROR_MESSAGE);
            disconnectFromServer();
        }
    }

    private void disconnectFromServer() {
        try {
            if (out != null) {
                out.writeUTF("DISCONNECT");
                out.close();
            }
            if (in != null) {
                in.close();
            }
            if (serverSocket != null) {
                serverSocket.close();
            }
        } catch (Exception e) {
            // Ignore
        } finally {
            connected = false;
            updateConnectionUI(false);
        }
    }

    private void updateConnectionUI(boolean connected) {
        txtServerIP.setEnabled(!connected);
        txtServerPort.setEnabled(!connected);
        txtUsername.setEnabled(!connected);
        txtPassword.setEnabled(!connected);

        btnConnect.setEnabled(!connected);
        btnDisconnect.setEnabled(connected);
        btnSend.setEnabled(connected);
    }

    private String[] getItemNames() {
        return new String[] { "Chưa kết nối" };
    }

    private String[] getOptionNames() {
        return new String[] { "Chưa kết nối" };
    }

    private void updateComboBoxes(List<String> items, List<String> options) {
        // Lưu dữ liệu gốc
        allItems = new ArrayList<>(items);
        allOptions = new ArrayList<>(options);

        // Cập nhật combo box items
        comboItems.removeAllItems();
        for (String item : items) {
            comboItems.addItem(item);
        }

        // Cập nhật combo box options
        comboOptions.removeAllItems();
        for (String option : options) {
            comboOptions.addItem(option);
        }
    }

    private void filterItems() {
        String searchText = txtItemSearch.getText().toLowerCase().trim();
        comboItems.removeAllItems();

        if (searchText.isEmpty()) {
            // Hiển thị tất cả
            for (String item : allItems) {
                comboItems.addItem(item);
            }
        } else {
            // Lọc theo tên
            for (String item : allItems) {
                if (item.toLowerCase().contains(searchText)) {
                    comboItems.addItem(item);
                }
            }
        }
    }

    private void filterOptions() {
        String searchText = txtOptionSearch.getText().toLowerCase().trim();
        comboOptions.removeAllItems();

        if (searchText.isEmpty()) {
            // Hiển thị tất cả
            for (String option : allOptions) {
                comboOptions.addItem(option);
            }
        } else {
            // Lọc theo tên
            for (String option : allOptions) {
                if (option.toLowerCase().contains(searchText)) {
                    comboOptions.addItem(option);
                }
            }
        }
    }

    private void addOption() {
        try {
            String selectedOption = (String) comboOptions.getSelectedItem();
            int optionId = Integer.parseInt(selectedOption.split(" - ")[0]);
            int param = Integer.parseInt(txtParam.getText().trim());

            currentOptions.add(new ItemOption(optionId, param));
            optionTableModel.addRow(new Object[] { optionId, selectedOption.split(" - ")[1], param });

            tableOptions.setPreferredScrollableViewportSize(new Dimension(
                    tableOptions.getPreferredScrollableViewportSize().width,
                    tableOptions.getRowHeight() * (optionTableModel.getRowCount() + 1)));
            tableOptions.revalidate();
            tableOptions.repaint();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Lỗi khi thêm option: " + e.getMessage(), "Lỗi",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    private void addItem() {
        try {
            String selectedItem = (String) comboItems.getSelectedItem();
            int itemId = Integer.parseInt(selectedItem.split(" - ")[0]);
            int quantity = Integer.parseInt(txtQuantity.getText().trim());

            // Tạo vật phẩm mới
            Item newItem = new Item((short) itemId);
            newItem.quantity = quantity;
            newItem.itemOptions.addAll(currentOptions);

            itemList.add(newItem);

            // Lấy thông tin các options của vật phẩm
            StringBuilder optionsInfo = new StringBuilder();
            for (ItemOption option : currentOptions) {
                optionsInfo.append("[").append(option.getOptionString()).append("] ");
            }

            // Thêm vật phẩm vào bảng
            itemTableModel.addRow(new Object[] {
                    itemId,
                    selectedItem.split(" - ")[1],
                    quantity,
                    optionsInfo.toString().trim()
            });

            // Xóa danh sách option tạm thời
            currentOptions.clear();
            optionTableModel.setRowCount(0);

            tableItems.setPreferredScrollableViewportSize(new Dimension(
                    tableItems.getPreferredScrollableViewportSize().width,
                    tableItems.getRowHeight() * (itemTableModel.getRowCount() + 1)));
            tableItems.revalidate();
            tableItems.repaint();

        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Lỗi khi thêm vật phẩm: " + e.getMessage(), "Lỗi",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    private void removeOption() {
        int selectedRow = tableOptions.getSelectedRow();
        if (selectedRow != -1) {
            currentOptions.remove(selectedRow);
            optionTableModel.removeRow(selectedRow);
        } else {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn một option để xóa!", "Thông báo",
                    JOptionPane.INFORMATION_MESSAGE);
        }
    }

    private void removeItem() {
        int selectedRow = tableItems.getSelectedRow();
        if (selectedRow != -1) {
            itemList.remove(selectedRow);
            itemTableModel.removeRow(selectedRow);
        } else {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn một vật phẩm để xóa!", "Thông báo",
                    JOptionPane.INFORMATION_MESSAGE);
        }
    }

    private void sendMail() {
        try {
            String recipient = txtRecipient.getText().trim();
            String title = txtTitle.getText().trim();
            String title2 = txtTitle2.getText().trim();
            String content = txtContent.getText().trim();

            if (recipient.isEmpty() || title.isEmpty() || content.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Vui lòng nhập đầy đủ thông tin!", "Lỗi",
                        JOptionPane.ERROR_MESSAGE);
                return;
            }

            if (!connected) {
                JOptionPane.showMessageDialog(this, "Chưa kết nối đến server!", "Lỗi", JOptionPane.ERROR_MESSAGE);
                return;
            }

            // Gửi lệnh gửi mail
            out.writeUTF("SEND_MAIL");
            out.writeUTF(recipient);
            out.writeUTF(title);
            out.writeUTF(title2);
            out.writeUTF(content);

            // Gửi danh sách vật phẩm
            out.writeInt(itemList.size());
            for (Item item : itemList) {
                out.writeShort(item.template.id);
                out.writeInt(item.quantity);
                out.writeInt(item.itemOptions.size());
                for (ItemOption option : item.itemOptions) {
                    out.writeInt(option.optionTemplate.id);
                    out.writeLong(option.param);
                }
            }

            // Nhận phản hồi
            String response = in.readUTF();
            if ("MAIL_SENT".equals(response)) {
                JOptionPane.showMessageDialog(this, "Thư đã được gửi thành công!", "Thành công",
                        JOptionPane.INFORMATION_MESSAGE);
                // Clear form
                clearForm();
            } else {
                JOptionPane.showMessageDialog(this, "Lỗi khi gửi thư: " + response, "Lỗi", JOptionPane.ERROR_MESSAGE);
            }

        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Lỗi khi gửi thư: " + e.getMessage(), "Lỗi", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void clearForm() {
        txtRecipient.setText("");
        txtTitle.setText("");
        txtTitle2.setText("");
        txtContent.setText("");
        txtQuantity.setText("");
        txtParam.setText("");
        itemList.clear();
        currentOptions.clear();
        itemTableModel.setRowCount(0);
        optionTableModel.setRowCount(0);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            RemoteMailClient client = new RemoteMailClient();
            client.setVisible(true);
        });
    }
}
