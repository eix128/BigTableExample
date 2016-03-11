import java.awt.event.InputEvent;
import java.time.LocalDate;
import java.util.*;
import java.util.Timer;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collector;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import com.sun.javafx.scene.control.skin.TableHeaderRow;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.beans.property.*;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.print.*;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.image.WritableImage;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.transform.Scale;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.stage.WindowEvent;
import javafx.util.Callback;
import javafx.util.Pair;
import jpa.CityEntity;
import main.Item;
import main.PairValueCell;
import org.controlsfx.control.NotificationPane;
import org.controlsfx.control.action.*;
import org.controlsfx.control.action.Action;
import org.controlsfx.control.textfield.AutoCompletionBinding;
import org.controlsfx.control.textfield.TextFields;
import utils.jpa.DynamicJPA2;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Path;
import javax.persistence.criteria.Root;
import javax.persistence.metamodel.EntityType;
import javax.persistence.metamodel.Metamodel;
import javax.swing.*;

public class AddMoreTableDataOnScrollToBottom extends Application {
    private Callback<AutoCompletionBinding.ISuggestionRequest, Collection<String>> collectionCallback;
    private TableView<Item> table;
    private Stage primaryStage;

    private void printIt() {
        Printer printer = Printer.getDefaultPrinter();
        PageLayout pageLayout = printer.createPageLayout(Paper.A4, PageOrientation.LANDSCAPE, Printer.MarginType.DEFAULT);
        double scaleX = pageLayout.getPrintableWidth() / table.getBoundsInParent().getWidth();
        double scaleY = pageLayout.getPrintableHeight() / table.getBoundsInParent().getHeight();
        table.getTransforms().add(new Scale(scaleX, scaleY));

        PrinterJob job = PrinterJob.createPrinterJob();
        if (job != null) {
            boolean successPrintDialog = job.showPrintDialog(primaryStage);
            if (successPrintDialog) {
                boolean success = job.printPage(pageLayout, table);
                if (success) {
                    job.endJob();
                }
            }
            table.getTransforms().add(new Scale(1 / scaleX, 1 / scaleY));
        }
    }


    public void print(WritableImage writableImage, Stage primaryStage) {
        ImageView imageView = new ImageView(writableImage);
        Printer printer = Printer.getDefaultPrinter();
        PageLayout pageLayout = printer.createPageLayout(Paper.A4, PageOrientation.LANDSCAPE, Printer.MarginType.DEFAULT);
        double scaleX = pageLayout.getPrintableWidth() / imageView.getBoundsInParent().getWidth();
        double scaleY = pageLayout.getPrintableHeight() / imageView.getBoundsInParent().getHeight();
        imageView.getTransforms().add(new Scale(scaleX, scaleY));

        PrinterJob job = PrinterJob.createPrinterJob();
        if (job != null) {
            boolean successPrintDialog = job.showPrintDialog(primaryStage.getOwner());
            if (successPrintDialog) {
                boolean success = job.printPage(pageLayout, imageView);
                if (success) {
                    job.endJob();
                }
            }
        }
    }


    public void printIt2() {
        Printer printer = Printer.getDefaultPrinter();
        Stage dialogStage = new Stage(StageStyle.DECORATED);
        PrinterJob job = PrinterJob.createPrinterJob(printer);
        if (job != null) {
            boolean showDialog = job.showPageSetupDialog(dialogStage);
            if (showDialog) {
                table.setScaleX(0.60);
                table.setScaleY(0.60);
                table.setTranslateX(-220);
                table.setTranslateY(-70);
                boolean success = job.printPage(table);
                if (success) {
                    job.endJob();
                }
                table.setTranslateX(0);
                table.setTranslateY(0);
                table.setScaleX(1.0);
                table.setScaleY(1.0);
            }
        }
    }

    // Change focus
    public void changeTableCellFocus(final TableView<?> table, final int focusIndex) {
//        table.requestFocus();
//        table.getSelectionModel().clearAndSelect(focusIndex);
//        table.getFocusModel().focus(focusIndex);

        Platform.runLater(new Runnable() {
            @Override
            public void run() {
//                table.requestFocus();
//                table.getSelectionModel().select(0);
//                table.getFocusModel().focus(0);

                // move focus & selection to TableView
                table.getSelectionModel().clearSelection(); // We don't want repeated selections
                table.requestFocus();                       // Get the focus
                table.getSelectionModel().selectFirst();    // select first item in TableView model
                table.getFocusModel().focus(0);             // set the focus on the first element
//                tableClickHandler(null);                    // render the selected item in the TableView
            }
        });

    }

    public final void click2(Node vbox) {
        Event.fireEvent(vbox, new MouseEvent(MouseEvent.MOUSE_CLICKED, 0,
                0, 0, 0, MouseButton.PRIMARY, 1, true, true, true, true,
                true, true, true, true, true, true, null));
    }

    public static void click(javafx.scene.Node control) {

        java.awt.Point originalLocation = java.awt.MouseInfo.getPointerInfo().getLocation();
        javafx.geometry.Point2D buttonLocation = control.localToScreen(control.getLayoutBounds().getMinX() + 10, control.getLayoutBounds().getMinY() + 10);
        try {
            java.awt.Robot robot = new java.awt.Robot();
            robot.mouseMove((int) buttonLocation.getX(), (int) buttonLocation.getY());
            try {
                Thread.sleep(150);
            } catch (Exception epx) {
            }
            robot.mousePress(InputEvent.BUTTON1_MASK);
            try {
                Thread.sleep(150);
            } catch (Exception epx) {
            }
            robot.mouseRelease(InputEvent.BUTTON1_MASK);
            try {
                Thread.sleep(150);
            } catch (Exception epx) {
            }
            robot.mouseMove((int) originalLocation.getX(), (int) originalLocation.getY());
            try {
                Thread.sleep(150);
            } catch (Exception epx) {
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void start(Stage primaryStage) {
        final EntityManagerFactory entityManagerFactory = Persistence.createEntityManagerFactory("MainData");
        final EntityManager entityManager = entityManagerFactory.createEntityManager();

        this.primaryStage = primaryStage;
//        TextField filterField = new TextField();
        final BorderPane borderPane = new BorderPane();

        Scene scene = new Scene(borderPane, 400, 400);
        primaryStage.setScene(scene);

        final ObservableList<Object> objects = FXCollections.observableArrayList();
        final TextField textField = new TextField() {

        };

        //        filterField.setOnAutoCompleted(eventHandler);
//            objects.addAll("Apple","Orange","Kadir");



        ObjectProperty<Callback<AutoCompletionBinding.ISuggestionRequest, Collection<String>>> suggestionProvider = new SimpleObjectProperty<>(collectionCallback);
//        AutoCompletionBinding filterField = TextFields.bindAutoCompletion( textField , objects );
//        final EventHandler<AutoCompletionBinding.AutoCompletionEvent<String>> eventHandler = new EventHandler<AutoCompletionBinding.AutoCompletionEvent<String>>() {
//            @Override
//            public void handle(AutoCompletionBinding.AutoCompletionEvent<String> event) {
//                String valueFromAutoCompletion = event.getCompletion();
//                System.out.println(valueFromAutoCompletion);
//            }
//        };


        DatePicker datePicker = new DatePicker();
        datePicker.setPromptText("Zaman");
// Add some action (in Java 8 lambda syntax style).
        datePicker.setOnAction(event -> {
            LocalDate date = datePicker.getValue();
            System.out.println("Selected date: " + date);
        });


//        filterField.setOnAutoCompleted(eventHandler);
        table = new TableView<>();
//        table.getColumns().add(column("Item", Item::nameProperty));
//        table.getColumns().add(column("Value", Item::valueProperty));

        TableColumn<Item, Object> firstNameCol = new TableColumn<Item, Object>();
        TableColumn<Item, Object> firstNameCol2 = new TableColumn<Item, Object>();
        TableColumn<Item, Object> lastNameCol = new TableColumn<Item, Object>();


        TableColumn firstEmailCol = new TableColumn("Primary");
        firstEmailCol.setPrefWidth(98);
        TableColumn secondEmailCol = new TableColumn("Secondary");
        secondEmailCol.setPrefWidth(98);
        lastNameCol.getColumns().addAll(secondEmailCol);
//        lastNameCol.setVisible(false);

        firstNameCol2.getColumns().addAll(firstEmailCol);


        firstNameCol.setPrefWidth(200);
        firstNameCol2.setGraphic(datePicker);

        firstEmailCol.setCellValueFactory(new PropertyValueFactory<Item, Object>("firstName"));
        secondEmailCol.setCellValueFactory(new PropertyValueFactory<Item, Object>("lastName"));

        TextField colHeaderTextField = new TextField();



        colHeaderTextField.setAlignment(Pos.BASELINE_CENTER);
        colHeaderTextField.setPromptText("Name");
        lastNameCol.setPrefWidth(80);

        /* bu da deneme için */
        final CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        javax.persistence.criteria.CriteriaQuery<CityEntity> cq2 = cb.createQuery(CityEntity.class);
        final javax.persistence.criteria.Root<CityEntity> from = cq2.from(CityEntity.class);
        final javax.persistence.criteria.CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        final javax.persistence.criteria.Path<String> city1 = from.get("city");

//        cq2.where("ORDER BY      STATS.last_execution_time DESC");
//        where.orderBy(  );last_execution_time

        // = new ArrayList<String>(8);
        collectionCallback = new Callback<AutoCompletionBinding.ISuggestionRequest, Collection<String>>() {
            @Override
            public Collection call(AutoCompletionBinding.ISuggestionRequest param) {
                final String userText = param.getUserText();
                ArrayList<String> liste = new ArrayList<String>();
                liste.add(userText);
                //return null;
//                System.out.println(userText);
                final javax.persistence.criteria.Predicate like = criteriaBuilder.like(city1, userText+"%");
                final CriteriaQuery<CityEntity> where = cq2.where(like);
                final TypedQuery<CityEntity> query1 = entityManager.createQuery(where);
                final List<CityEntity> resultList = query1.setMaxResults(8).getResultList();
//                liste.clear();
                List<String> obje = resultList.parallelStream().map(new Function<CityEntity, String>() {
                    @Override
                    public String apply(CityEntity cityEntity) {
                        return cityEntity.getCity();
                    }
                }).collect(Collectors.toList());
//                map(String::length)
                obje.stream().forEachOrdered(liste::add);
                return liste;
            }
        };
        AutoCompletionBinding filterField2 = TextFields.bindAutoCompletion(colHeaderTextField, collectionCallback);
        lastNameCol.setGraphic(filterField2.getCompletionTarget());

        filterField2.setOnAutoCompleted(value -> {

        });


        table.getColumns().setAll(firstNameCol2, lastNameCol);
//        borderPane.getChildren().add(textField);

//        borderPane.getChildren().remove(filterField.getCompletionTarget());

        table.onKeyPressedProperty().set(new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent event) {
                final ObservableList<TablePosition> selectedCells = table.getSelectionModel().getSelectedCells();
                if (selectedCells != null && selectedCells.size() > 0) {
                    final TablePosition tablePosition = selectedCells.get(0);
                    final int columnIndex = tablePosition.getColumn();
                    final int row = tablePosition.getRow();
                    if (columnIndex == 1 && row > 1) {
                        colHeaderTextField.setText("");
//                        click(filterField2.getCompletionTarget());
                        colHeaderTextField.requestFocus();
                    }
                };
            }
        });

        final PairValueCell pairValueCell = new PairValueCell();
        final PairValueCell pairValueCel2 = new PairValueCell();
        firstNameCol2.setCellFactory(new Callback<TableColumn<Item, Object>, TableCell<Item, Object>>() {
            @Override
            public TableCell<Item, Object> call(TableColumn<Item, Object> param) {
                return new PairValueCell();
            }
        });

        lastNameCol.setCellFactory(new Callback<TableColumn<Item, Object>, TableCell<Item, Object>>() {
            @Override
            public TableCell<Item, Object> call(TableColumn<Item, Object> param) {
                return new PairValueCell();
            }
        });
//        firstNameCol.setCellFactory(new Callback<TableColumn<Pair<String, Object>, Str>, TableCell<Pair<String, Object>, Object>>() {
//            @Override
//            public TableCell<Pair<String, Object>, Object> call(TableColumn<Pair<String, Object>, Object> column) {
//                return new PairValueCell();
//            }
//        });


        table.getSelectionModel().setCellSelectionEnabled(true);
        final DynamicJPA dynamicJPA = new DynamicJPA(entityManager);
        final DynamicJPA2 dynamicJPA2 = new DynamicJPA2();
        SortedList<Item> sortedList = new SortedList<Item>(dynamicJPA,
                (Item stock1, Item stock2) -> {
                    return stock1.getFirstName().compareTo(stock2.getFirstName());
                });

        sortedList.getSource().setAll(dynamicJPA);
        // 1. Wrap the ObservableList in a FilteredList (initially display all data).
//        FilteredList<Item> filteredData = new FilteredList<>(dynamicJPA, p -> true);

//        Arrays.stream(new int[]{1, 2, 3})
//                .map(n -> 2 * n + 1)
//                .average()
//                .ifPresent(System.out::println);  // 5.0





        // 3. Wrap the FilteredList in a SortedList.
        SortedList<Item> sortedData = new SortedList<Item>(sortedList);


        final javax.persistence.criteria.CriteriaBuilder criteriaBuilder1 = entityManager.getCriteriaBuilder();
        final javax.persistence.criteria.CriteriaQuery<CityEntity> query = criteriaBuilder1.createQuery( CityEntity.class );

        final javax.persistence.criteria.Root<CityEntity> from1 = query.from(CityEntity.class);


        Metamodel m = entityManager.getMetamodel();
        EntityType<CityEntity> CityEntity_ = m.entity(CityEntity.class);

        final Path<String> city = from1.get("city");
        EventHandler onSearchAction = event -> {
            LocalDate value;
            String text;
            try {
                text = colHeaderTextField.getText();
                value = datePicker.getValue();

                //get text && value
                final javax.persistence.criteria.Predicate equal = criteriaBuilder1.like(city, text+"%");
                final CriteriaQuery<CityEntity> likeAll = query.where(equal);
                final TypedQuery<CityEntity> query1 = entityManager.createQuery(likeAll);
                final List<CityEntity> resultList = query1.getResultList();
                if(text == null && value == null) {
                    sortedList.getSource().setAll(dynamicJPA);
                } else {
                    final List<Item> collect = resultList.parallelStream().map(new Function<CityEntity, Item>() {
                        @Override
                        public Item apply(CityEntity cityEntity) {
                            return new Item(cityEntity.getCity(), cityEntity.getCity());
                        }
                    }).collect(Collectors.toList());
                    dynamicJPA2.setItems( collect );
                    dynamicJPA2.setSize( collect.size() );
                    sortedList.getSource().setAll(dynamicJPA2);

                    Platform.runLater(new Runnable() {
                        @Override
                        public void run() {
                            table.setItems(null);
                            SortedList<Item> sortedList = new SortedList<Item>(dynamicJPA2,
                                    (Item stock1, Item stock2) -> {
                                        return stock1.getFirstName().compareTo(stock2.getFirstName());
                                    });
                            sortedList.comparatorProperty().bind(table.comparatorProperty());
                            table.setItems(sortedList);
                            // System.out.println(dynamicJPA2.getItems().size());
                            //refresh
                            final boolean visible = table.getColumns().get(0).isVisible();
                            table.getColumns().get(0).setVisible(false);
                            table.getColumns().get(0).setVisible(visible);
                            table.refresh();

                        }
                    });

                }
            } finally {

//                Timer timer = new Timer("Deneme");
//
//                timer.schedule(new TimerTask() {
//                    @Override
//                    public void run() {
//                        Platform.runLater(new Runnable() {
//                            @Override
//                            public void run() {
//                                table.setItems(sortedData);
//                            }
//                        });
//                    }
//                }, 5000);

            }
        };

        colHeaderTextField.setOnKeyPressed(onSearchAction);
        datePicker.setOnAction( onSearchAction );

        // 4. Bind the SortedList comparator to the TableView comparator.
        sortedData.comparatorProperty().bind(table.comparatorProperty());

        table.setItems(sortedData);
        //refresh
        table.getColumns().get(0).setVisible(false);
        table.getColumns().get(0).setVisible(true);
        table.refresh();
//        final Validator<Object> message = Validator.createEmptyValidator("message");
//        new ValidationSupport().registerValidator(table, false, message );


        //addMoreData(table, 20);
        borderPane.setCenter(table);
//        borderPane.setTop(completionTarget);


        NotificationPane notificationPane = new NotificationPane(borderPane);
        notificationPane.setText("Deneme 1 2 3");
//                            notificationPane.setShowFromTop(true);
        notificationPane.getStyleClass().add(NotificationPane.STYLE_CLASS_DARK);

        Timer timer = new Timer("Deneme");
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                Platform.runLater(() -> {
                            notificationPane.show();

                        }
                );

//                table.setItems();
            }
        }, 5000);


        final ObjectProperty<EventHandler<WindowEvent>> eventHandlerObjectProperty = primaryStage.onCloseRequestProperty();
        eventHandlerObjectProperty.set(new EventHandler<WindowEvent>() {
            @Override
            public void handle(WindowEvent event) {
                System.exit(0);
            }
        });


        final Callback<TableColumn<Item, String>, TableCell<Item, String>> callback = new Callback<TableColumn<Item, String>, TableCell<Item, String>>() {

            @Override
            public TableCell<Item, String> call(TableColumn<Item, String> param) {

                TableCell<Item, String> cell = new TableCell<Item, String>() {

                    ImageView image = new ImageView(new Image("grey.png"));

                    @Override
                    public void updateItem(String item, boolean empty) {

                        super.updateItem(item, empty);

                        if (empty) {

                            setGraphic(null);
                            setText(null);
                        } else {

                            setGraphic(image);
                            setText(null);
                        }
                    }
                };

                return cell;
            }

        };

        final ObservableList<Integer> highlightRows = FXCollections.observableArrayList();

        table.setRowFactory(new Callback<TableView<Item>, TableRow<Item>>() {
            @Override
            public TableRow<Item> call(TableView<Item> tableView) {
                final TableRow<Item> row = new TableRow<Item>() {
                    @Override
                    protected void updateItem(Item person, boolean empty) {
                        super.updateItem(person, empty);
                        if (highlightRows.contains(getIndex())) {
                            if (!getStyleClass().contains("highlightedRow")) {
                                getStyleClass().add("highlightedRow");
                            }
                        } else {

                            getStyleClass().removeAll(Collections.singleton("highlightedRow"));
                        }
                    }
                };


//                highlightRows.addListener(new ListChangeListener<Integer>() {
//                    @Override
//                    public void onChanged(Change<? extends Integer> change) {
//                        if (highlightRows.contains(row.getIndex())) {
//                            if (! row.getStyleClass().contains("highlightedRow")) {
//                                row.getStyleClass().add("highlightedRow");
//                            }
//                        } else {
//                            row.getStyleClass().removeAll(Collections.singleton("highlightedRow"));
//                        }
//                    }
//                });
                return row;
            }


        });


//        verticalBar.setVisible(true);
//        verticalBar.valueProperty().addListener((obs, oldValue, newValue) -> {
////            System.out.println( oldValue+" "+ newValue );
//            System.out.println(verticalBar.getOrientation() + ": range " + verticalBar.getMin() + " => " + verticalBar.getMax() + ", value " + verticalBar.getValue());
//        });


        primaryStage.show();

        final ScrollBar scrollBarH = (ScrollBar) table.lookup(".scroll-bar:hotizontal");
        scrollBarH.setVisible(false);

        ScrollBar verticalBar = (ScrollBar) table.lookup(".scroll-bar:vertical");
        verticalBar.setVisible(true);
        verticalBar.autosize();
    }

    private void addMoreData(TableView<Item> table, int numItems) {
        Task<List<Item>> dataRetrieveTask = new Task<List<Item>>() {
            @Override
            public List<Item> call() throws Exception {
                // mimic connect to db:
                Thread.sleep(500);
                List<Item> items = new ArrayList<>();
                int nextItem = table.getItems().size() + 1;
                for (int i = nextItem; i < nextItem + numItems; i++) {
                    items.add(new Item("DynamicJPA", "1 2 3"));
                }
                return items;
            }
        };
        dataRetrieveTask.setOnSucceeded(e -> table.getItems().addAll(dataRetrieveTask.getValue()));
        new Thread(dataRetrieveTask).start();
    }

    private <S, T> TableColumn<S, T> column(String title, Function<S, ObservableValue<T>> prop) {
        TableColumn<S, T> col = new TableColumn<>(title);
        col.setCellValueFactory(cellData -> prop.apply(cellData.getValue()));
        return col;
    }


    public static void main(String[] args) {
        launch(args);
    }
}