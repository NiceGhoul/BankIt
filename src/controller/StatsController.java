package controller;

import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.PieChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.ComboBox;
import javafx.scene.layout.AnchorPane;
import model.Transaction;

import java.math.BigDecimal;
import java.net.URL;
import java.time.LocalDate;
import java.util.List;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

public class StatsController implements Initializable {

    @FXML
    private ComboBox<String> chartSelector;

    @FXML
    private ComboBox<String> dateComboBox;

    @FXML
    private ComboBox<String> monthComboBox;

    @FXML
    private ComboBox<String> yearComboBox;

    @FXML
    private AnchorPane chartContainer;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        chartSelector.getItems().addAll("Line Chart", "Pie Chart");

        setupDateComboBox();
        setupMonthComboBox();
        setupYearComboBox();

        chartSelector.setOnAction(event -> updateChart());
        dateComboBox.setOnAction(event -> updateChart());
        monthComboBox.setOnAction(event -> updateChart());
        yearComboBox.setOnAction(event -> updateChart());

        updateChart();
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

    private void updateChart() {
        String selectedDate = dateComboBox.getValue();
        String selectedMonth = monthComboBox.getValue();
        String selectedYear = yearComboBox.getValue();
        String selectedChart = chartSelector.getValue();

        if (selectedMonth == null || selectedYear == null || selectedChart == null) {
            return;
        }

        List<Transaction> transactions = TransactionController.getTransactions().stream()
                .filter(t -> t.getDate().getMonthValue() == monthComboBox.getItems().indexOf(selectedMonth) + 1
                        && t.getDate().getYear() == Integer.parseInt(selectedYear))
                .collect(Collectors.toList());

        if (!"Date".equals(selectedDate)) {
            int day = Integer.parseInt(selectedDate);
            transactions = transactions.stream()
                    .filter(t -> t.getDate().getDayOfMonth() == day)
                    .collect(Collectors.toList());
        }

        if ("Line Chart".equals(selectedChart)) {
            displayLineChart(transactions);
        } else if ("Pie Chart".equals(selectedChart)) {
            displayPieChart(transactions);
        }
    }

    private void displayLineChart(List<Transaction> filteredTransactions) {
        chartContainer.getChildren().clear();

        NumberAxis xAxis = new NumberAxis();
        xAxis.setLabel("Transaction Index");
        NumberAxis yAxis = new NumberAxis();
        yAxis.setLabel("Amount");

        LineChart<Number, Number> lineChart = new LineChart<>(xAxis, yAxis);
        lineChart.setTitle("Transactions on " + monthComboBox.getValue() + " " + yearComboBox.getValue());

        XYChart.Series<Number, Number> series = new XYChart.Series<>();
        series.setName("Transactions");

        for (int i = 0; i < filteredTransactions.size(); i++) {
            BigDecimal amount = filteredTransactions.get(i).getAmount();
            series.getData().add(new XYChart.Data<>(i + 1, amount.doubleValue()));
        }

        lineChart.getData().add(series);
        chartContainer.getChildren().add(lineChart);
        setAnchors(lineChart);
    }

    private void displayPieChart(List<Transaction> filteredTransactions) {
        chartContainer.getChildren().clear();

        PieChart pieChart = new PieChart();
        pieChart.setTitle("Transaction Categories on " + monthComboBox.getValue() + " " + yearComboBox.getValue());

        filteredTransactions.stream()
                .collect(Collectors.groupingBy(
                        t -> String.valueOf(t.getCategoryId()),
                        Collectors.reducing(BigDecimal.ZERO, Transaction::getAmount, BigDecimal::add)
                ))
                .forEach((categoryId, total) -> pieChart.getData().add(new PieChart.Data(categoryId, total.doubleValue())));

        chartContainer.getChildren().add(pieChart);
        setAnchors(pieChart);
    }

    private void setAnchors(javafx.scene.Node node) {
        AnchorPane.setTopAnchor(node, 0.0);
        AnchorPane.setBottomAnchor(node, 0.0);
        AnchorPane.setLeftAnchor(node, 0.0);
        AnchorPane.setRightAnchor(node, 0.0);
    }
}
