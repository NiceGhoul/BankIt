package controller;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.chart.PieChart;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import model.Transaction;

import java.math.BigDecimal;
import java.net.URL;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

import factory.TransactionFactory;

public class StatsController implements Initializable {

    @FXML
    private ComboBox<String> dateComboBox;

    @FXML
    private ComboBox<String> monthComboBox;

    @FXML
    private ComboBox<String> yearComboBox;

    @FXML
    private ComboBox<String> filterComboBox;

    @FXML
    private AnchorPane chartContainer;

    @FXML
    private TableView<TransactionDetail> detailsTable;

    @FXML
    private TableColumn<TransactionDetail, String> categoryColumn;

    @FXML
    private TableColumn<TransactionDetail, String> amountColumn;

    private final List<String> incomeColors = Arrays.asList(
            "#4CAF50", // Green
            "#8BC34A", // Light Green
            "#CDDC39", // Lime
            "#FFEB3B", // Yellow
            "#FFC107", // Amber
            "#FF9800", // Orange
            "#FF5722", // Deep Orange
            "#795548", // Brown
            "#607D8B", // Blue Grey
            "#9C27B0", // Purple
            "#673AB7", // Deep Purple
            "#3F51B5"  // Indigo
        );

        private final List<String> expenseColors = Arrays.asList(
            "#F44336", // Red
            "#E91E63", // Pink
            "#9C27B0", // Purple
            "#3F51B5", // Indigo
            "#2196F3", // Blue
            "#03A9F4", // Light Blue
            "#00BCD4", // Cyan
            "#009688", // Teal
            "#4CAF50", // Green
            "#8BC34A", // Light Green
            "#CDDC39", // Lime
            "#FFEB3B"  // Yellow
        );
    private Map<String, String> categoryColorMap = new HashMap<>();

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        setupDateComboBox();
        setupMonthComboBox();
        setupYearComboBox();
        setupFilterComboBox();
        setupTableView();

        filterComboBox.setValue("Show All");
        updateChart();

        dateComboBox.setOnAction(event -> updateChart());
        monthComboBox.setOnAction(event -> updateChart());
        yearComboBox.setOnAction(event -> updateChart());
        filterComboBox.setOnAction(event -> updateChart());
    }

    private void setupDateComboBox() {
        dateComboBox.getItems().add("Date");
        for (int i = 1; i <= 31; i++) {
            dateComboBox.getItems().add(String.valueOf(i));
        }
        dateComboBox.setValue("Date");
    }

    private void setupMonthComboBox() {
        String[] months = {"January", "February", "March", "April", "May", "June",
                "July", "August", "September", "October", "November", "December"};
        monthComboBox.setItems(FXCollections.observableArrayList(months));
        monthComboBox.setValue(months[LocalDate.now().getMonthValue() - 1]);
    }

    private void setupYearComboBox() {
        int currentYear = LocalDate.now().getYear();
        for (int year = 2020; year <= currentYear; year++) {
            yearComboBox.getItems().add(String.valueOf(year));
        }
        yearComboBox.setValue(String.valueOf(currentYear));
    }

    private void setupFilterComboBox() {
        filterComboBox.setItems(FXCollections.observableArrayList("Show All", "Income", "Expense"));
    }

    private void setupTableView() {
        categoryColumn.setCellValueFactory(new PropertyValueFactory<>("category"));
        amountColumn.setCellValueFactory(new PropertyValueFactory<>("amount"));

        categoryColumn.setStyle("-fx-text-fill: black; -fx-font-size: 12px;");
        amountColumn.setStyle("-fx-text-fill: black; -fx-font-size: 12px;");
    }

    private void updateChart() {
        String selectedDate = dateComboBox.getValue();
        String selectedMonth = monthComboBox.getValue();
        String selectedYear = yearComboBox.getValue();
        String selectedFilter = filterComboBox.getValue();

        List<Transaction> transactions = TransactionFactory.getTransactionList().stream()
                .filter(t -> t.getDate().getMonthValue() == monthComboBox.getItems().indexOf(selectedMonth) + 1
                        && t.getDate().getYear() == Integer.parseInt(selectedYear))
                .collect(Collectors.toList());

        if (!"Date".equals(selectedDate)) {
            int day = Integer.parseInt(selectedDate);
            transactions = transactions.stream()
                    .filter(t -> t.getDate().getDayOfMonth() == day)
                    .collect(Collectors.toList());
        }

        if ("Income".equals(selectedFilter)) {
            transactions = transactions.stream()
                    .filter(t -> t.getTransactionType().equalsIgnoreCase("Income"))
                    .collect(Collectors.toList());
        } else if ("Expense".equals(selectedFilter)) {
            transactions = transactions.stream()
                    .filter(t -> t.getTransactionType().equalsIgnoreCase("Expense"))
                    .collect(Collectors.toList());
        }

        displayChart(transactions, selectedFilter);
        populateDetailsTable(transactions, selectedFilter);
    }

    private void displayChart(List<Transaction> transactions, String filter) {
        chartContainer.getChildren().clear();
        categoryColorMap.clear(); 

        PieChart pieChart = new PieChart();
        pieChart.setTitle("Transaction Categories");
        pieChart.setLegendVisible(false); 

        var groupedData = transactions.stream()
                .collect(Collectors.groupingBy(
                        filter.equals("Show All") ? Transaction::getTransactionType : Transaction::getCategoryName,
                        Collectors.reducing(BigDecimal.ZERO, Transaction::getAmount, BigDecimal::add)));

        int colorIndex = 0;
        for (var entry : groupedData.entrySet()) {
            String category = entry.getKey();
            BigDecimal amount = entry.getValue();

            PieChart.Data slice = new PieChart.Data(category, amount.doubleValue());
            pieChart.getData().add(slice);

            String color;

            if ("Show All".equals(filter)) {
                if ("Income".equalsIgnoreCase(category)) {
                    color = "#4CAF50"; // Green
                } else if ("Expense".equalsIgnoreCase(category)) {
                    color = "#F44336"; // Red
                } else {
                    color = "#2196F3"; //Blue
                }
            } else if ("Income".equals(filter)) {
                color = incomeColors.get(colorIndex % incomeColors.size());
                colorIndex++;
            } else { 
                color = expenseColors.get(colorIndex % expenseColors.size());
                colorIndex++;
            }
            categoryColorMap.put(category, color);

            slice.getNode().setStyle("-fx-pie-color: " + color + ";");
        }

        pieChart.lookup(".chart-title").setStyle("-fx-text-fill: white; -fx-font-size: 16px;");

        Platform.runLater(() -> {
            for (PieChart.Data data : pieChart.getData()) {
                Node node = data.getNode();
                if (node != null) {
                    Text label = (Text) node.lookup(".chart-pie-label");
                    if (label != null) {
                        label.setFill(Color.WHITE);
                        label.setStyle("-fx-font-size: 12px; -fx-font-weight: bold;");
                    }
                }
            }
        });

        VBox customLegend = createCustomLegend(groupedData.keySet(), filter);
        VBox container = new VBox(10); 
        container.getChildren().addAll(pieChart, customLegend);

        AnchorPane.setTopAnchor(container, 0.0);
        AnchorPane.setBottomAnchor(container, 0.0);
        AnchorPane.setLeftAnchor(container, 0.0);
        AnchorPane.setRightAnchor(container, 0.0);

        chartContainer.getChildren().add(container);
    }

    /**
     * Creates a custom legend based on the categories and selected filter.
     *
     * @param categories Set of category names.
     * @param filter     Current selected filter ("Show All", "Income", "Expense").
     * @return VBox containing the custom legend items.
     */
    private VBox createCustomLegend(Set<String> categories, String filter) {
        VBox legendBox = new VBox(5); // 5px spacing between legend items
        legendBox.setStyle("-fx-background-color: transparent; -fx-padding: 10;");

        for (String category : categories) {
            HBox legendItem = new HBox(5); 
            legendItem.setAlignment(javafx.geometry.Pos.CENTER_LEFT);

            // Create color box
            Rectangle colorBox = new Rectangle(15, 15);
            String color = categoryColorMap.getOrDefault(category, "#2196F3"); 
            colorBox.setFill(Color.web(color));

            // Create label
            Label label = new Label(category);
            label.setStyle("-fx-text-fill: white; -fx-font-size: 12px;"); 

            legendItem.getChildren().addAll(colorBox, label);
            legendBox.getChildren().add(legendItem);
        }

        return legendBox;
    }

    private void populateDetailsTable(List<Transaction> transactions, String filter) {
        ObservableList<TransactionDetail> details = FXCollections.observableArrayList();

        transactions.stream()
                .collect(Collectors.groupingBy(
                        filter.equals("Show All") ? Transaction::getTransactionType : Transaction::getCategoryName,
                        Collectors.reducing(BigDecimal.ZERO, Transaction::getAmount, BigDecimal::add)))
                .forEach((category, amount) -> details.add(new TransactionDetail(category, "Rp " + amount)));

        detailsTable.setItems(details);
    }

    public static class TransactionDetail {
        private final String category;
        private final String amount;

        public TransactionDetail(String category, String amount) {
            this.category = category;
            this.amount = amount;
        }

        public String getCategory() {
            return category;
        }

        public String getAmount() {
            return amount;
        }
    }
}
