package com.rosamusic;

import java.util.ArrayList;
import java.util.Arrays;
import java.io.File;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.CheckMenuItem;
import javafx.scene.control.MenuItem;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.control.TableView;
import javafx.scene.control.TableColumn;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Dialog;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ButtonBar.ButtonData;
import javafx.geometry.Insets;
import javafx.stage.FileChooser;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;

public class gui extends Application
{
    private static ObservableList<String> formatOptions =
        FXCollections.observableArrayList(
            "12\" or 10\" Vinyl Record",
            "7\" Vinyl Record",
            "CD",
            "Cassette Tape",
            "Digital File",
            "Shellac Record"
        );

    private static ObservableList<String> genreOptions =
        FXCollections.observableArrayList(
            "Country", "Electronic", "Folk", "Hip-Hop", "Jazz", "Metal", "Orchestral", "OST", "Pop", "Punk", "R&B", "Rock"
        );

    private static int artistNum; // used for a label in additemgui
    private static Date tempDate;
    private static String tempTitle;
    private static String tempGenre;
    private static recordRelease.Format tempFormat;

    // widget that displays all items in the collectionContainer
    private static TableView<recordReleaseStringRep> Table;

    private static CheckMenuItem VinylLpSortMenu, VinylEpSortMenu, CDSortMenu, CassetteSortMenu, DigitalSortMenu, ShellacSortMenu;

    // array that stores which collections are currently viewable 
    /*
     * 0 - vinyl lp
     * 1 - vinyl ep
     * 2 - cd
     * 3 - cassette 
     * 4 - digital
     * 5 - shellac 
     */
    private static boolean[] viewCollection = {true,true,true,true,true,true};

    private static void AddItemGUI(Stage primaryStage)
    {
        tempTitle = "";
        ArrayList<String> tempArtists = new ArrayList<String>();
        tempGenre = "";
        tempDate = new Date();
        // don't forget about tempFormat too! 

        artistNum = 1;


        Label titleLabel = new Label("Title");
        TextField titleTF = new TextField();

        Label artistLabel = new Label("Artist " + Integer.toString(artistNum));
        TextField artistTF = new TextField();
        Button addArtistButton = new Button("Add Artist");
        addArtistButton.setOnAction(new EventHandler<ActionEvent>()
        {
            @Override
            public void handle(ActionEvent t)
            {
                String temp = artistTF.getText();
                

                Dialog<String> dialog = new Dialog<String>();
                ButtonType okButton = new ButtonType("Ok", ButtonData.OK_DONE);
                dialog.getDialogPane().getButtonTypes().add(okButton);

                if (!temp.equals(""))
                {
                    dialog.setTitle("Success!");
                    dialog.setContentText("Artist successfully added!");

                    tempArtists.add(temp);

                    dialog.showAndWait();

                    artistNum++;

                    artistLabel.setText("Artist " + Integer.toString(artistNum));
                    artistTF.clear();
                }
                else 
                {
                    dialog.setTitle("Fail!");
                    dialog.setContentText("Failed to add artist! Field is likely blank.");

                    dialog.showAndWait();
                }

            }
        });

        Label genreLabel = new Label("Genre");
        ComboBox<String> genreComboBox = new ComboBox<String>(genreOptions);

        Label releaseDateLabel = new Label("Release Date ");
        DatePicker datePicker = new DatePicker();

        Label formatLabel = new Label("Format");
        ComboBox<String> formatComboBox = new ComboBox<String>(formatOptions);

        Stage newWindow = new Stage();

        Button addItemButton = new Button("Add Item");
        addItemButton.setOnAction(new EventHandler<ActionEvent>()
        {
            @Override
            public void handle(ActionEvent t)
            {
                if (datePicker.getValue() != null && !titleTF.getText().isEmpty() && tempArtists.size() > 0 && !genreComboBox.getSelectionModel().isEmpty() && !formatComboBox.getSelectionModel().isEmpty())
                {
                    tempTitle = titleTF.getText();
                    tempGenre = genreComboBox.getValue();
                    tempDate = new Date(datePicker.getValue());

                    switch (formatComboBox.getValue())
                    {
                        case "12\" or 10\" Vinyl Record":
                            tempFormat = recordRelease.Format.VINYL_LP;
                            break;
                        case "7\" Vinyl Record":
                            tempFormat = recordRelease.Format.VINYL_EP;
                            break;
                        case "CD":
                            tempFormat = recordRelease.Format.CD;
                            break;
                        case "Cassette Tape":
                            tempFormat = recordRelease.Format.CASSETTE;
                            break;
                        case "Digital File":
                            tempFormat = recordRelease.Format.DIGITAL;
                            break;
                        case "Shellac Record":
                            tempFormat = recordRelease.Format.SHELLAC;
                            break;
                    }

                    recordRelease tempRR = new recordRelease(tempTitle, tempArtists, new ArrayList<String>(Arrays.asList(tempGenre)), tempDate, tempFormat);

                    cc.addItem(tempRR);
                    updateTable();

                    newWindow.close();
                }
            }
        });

        GridPane layout = new GridPane();
        layout.setVgap(0);
        layout.setHgap(2);
        layout.setPadding(new Insets(5,0,0,10));
        layout.addRow(0,titleLabel,titleTF);
        layout.addRow(1,artistLabel,artistTF,addArtistButton);
        layout.addRow(2,genreLabel,genreComboBox);
        layout.addRow(3,releaseDateLabel,datePicker);
        layout.addRow(4,formatLabel,formatComboBox);
        layout.addRow(5,addItemButton);

        Scene scene = new Scene(layout,350,200);
        scene.getStylesheets().add("stylesheet.css");
        
        newWindow.setTitle("Add Item");
        newWindow.setScene(scene);
        newWindow.setResizable(false);
        newWindow.setX(primaryStage.getX()+50);
        newWindow.setY(primaryStage.getY()+25);

        newWindow.show();
    }

    public static collectionContainer cc;

    // filter that makes it that only json files can be viewed by the filechooser
    private static final FileChooser.ExtensionFilter extFilter =
        new FileChooser.ExtensionFilter("JSON files (*.json)", "*.json");

    @Override
    public void start(Stage primaryStage) throws Exception
    {
        cc = new collectionContainer();

        BorderPane root = new BorderPane();
        Scene scene = new Scene(root,720,480);
        scene.getStylesheets().add("stylesheet.css");

        // menu
        MenuBar menubar = new MenuBar();

        // FILE
        Menu FileMenu = new Menu("File");
        MenuItem Save = new MenuItem("Save");
        Save.setOnAction(new EventHandler<ActionEvent>()
        {
            public void handle(ActionEvent t)
            {
                FileChooser fc = new FileChooser();
                fc.getExtensionFilters().add(extFilter);
                fc.setTitle("Save File");
                
                File tempFile = fc.showSaveDialog(primaryStage);
                if (tempFile != null)
                {
                    cc.save(tempFile.getAbsolutePath());
                }
            }
        });
        MenuItem Load = new MenuItem("Load");
        Load.setOnAction(new EventHandler<ActionEvent>()
        {
            public void handle(ActionEvent t)
            {
                FileChooser fc = new FileChooser();
                fc.getExtensionFilters().add(extFilter);
                fc.setTitle("Load File");
                File tempFile = fc.showOpenDialog(primaryStage);
                if (tempFile != null)
                {
                    cc.read(tempFile.getAbsolutePath());
                    updateTable();
                    VinylLpSortMenu.setSelected(collectionContainer.genreSortList[0]);
                    VinylEpSortMenu.setSelected(collectionContainer.genreSortList[1]);
                    CDSortMenu.setSelected(collectionContainer.genreSortList[2]);
                    CassetteSortMenu.setSelected(collectionContainer.genreSortList[3]);
                    DigitalSortMenu.setSelected(collectionContainer.genreSortList[4]);
                    ShellacSortMenu.setSelected(collectionContainer.genreSortList[5]);
                }
            }
        });
        FileMenu.getItems().addAll(Save,Load);

        //EDIT
        Menu EditMenu = new Menu("Edit");
        MenuItem AddItemMenu = new MenuItem("Add Item");
        AddItemMenu.setOnAction(new EventHandler<ActionEvent>()
        {
            @Override
            public void handle(ActionEvent t)
            {
                AddItemGUI(primaryStage);
            }
        });
        MenuItem DeleteItemMenu = new MenuItem("Delete Item");
        // code that deletes an item from the collection
        // this is such a hacky implementation LMAOOO i'm honestly surprised i got it to work
        DeleteItemMenu.setOnAction(new EventHandler<ActionEvent>()
        {
            public void handle(ActionEvent t)
            {
                recordReleaseStringRep rrsr = Table.getSelectionModel().getSelectedItem();
                if (rrsr != null)
                {
                    cc.sortFullCollection();
                    for (int i = 0; i < cc.getFullCollection().size(); ++i)
                    {
                        if (rrsr.equals(cc.getFullCollection().get(i).createStringRep()))
                        {
                            cc.removeItem(i);
                            break;
                        }
                    }
                }
                updateTable();
            }
        });
        MenuItem DuplicateItemMenu = new MenuItem("Duplicate Item");
        // code that duplicates an item from the collection
        DuplicateItemMenu.setOnAction(new EventHandler<ActionEvent>()
        {
            public void handle(ActionEvent t)
            {
                recordReleaseStringRep rrsr = Table.getSelectionModel().getSelectedItem();
                if (rrsr != null)
                {
                    for (int i = 0; i < cc.getFullCollection().size(); ++i)
                    {
                        if (rrsr.equals(cc.getFullCollection().get(i).createStringRep()))
                        {
                            cc.addItem(cc.getFullCollection().get(i));
                            break;
                        }
                    }
                    updateTable();
                }
            }
        });
        EditMenu.getItems().addAll(AddItemMenu,DeleteItemMenu,DuplicateItemMenu);

        // SELECT FORMAT 
        Menu SelectFormatMenu = new Menu("View");
        CheckMenuItem VinylLpMenu = new CheckMenuItem("Vinyl LP");
        VinylLpMenu.setOnAction(new EventHandler<ActionEvent>()
        {
            public void handle(ActionEvent t)
            {
                viewCollection[0] = VinylLpMenu.selectedProperty().get();
                updateTable();
            }
        });
        VinylLpMenu.setSelected(true);
        CheckMenuItem VinylEpMenu = new CheckMenuItem("Vinyl EP");
        VinylEpMenu.setOnAction(new EventHandler<ActionEvent>()
        {
            public void handle(ActionEvent t)
            {
                viewCollection[1] = VinylEpMenu.selectedProperty().get();
                updateTable();
            }
        });
        VinylEpMenu.setSelected(true);
        CheckMenuItem CDMenu = new CheckMenuItem("CD");
        CDMenu.setOnAction(new EventHandler<ActionEvent>()
        {
            public void handle(ActionEvent t)
            {
                viewCollection[2] = CDMenu.selectedProperty().get();
                updateTable();
            }
        });
        CDMenu.setSelected(true);
        CheckMenuItem CassetteMenu = new CheckMenuItem("Cassette Tape");
        CassetteMenu.setOnAction(new EventHandler<ActionEvent>()
        {
            public void handle(ActionEvent t)
            {
                viewCollection[3] = CassetteMenu.selectedProperty().get();
                updateTable();
            }
        });
        CassetteMenu.setSelected(true);
        CheckMenuItem DigitalMenu = new CheckMenuItem("Digital File");
        DigitalMenu.setOnAction(new EventHandler<ActionEvent>()
        {
            public void handle(ActionEvent t)
            {
                viewCollection[4] = DigitalMenu.selectedProperty().get();
                updateTable();
            }
        });
        DigitalMenu.setSelected(true);
        CheckMenuItem ShellacMenu = new CheckMenuItem("Shellac");
        ShellacMenu.setOnAction(new EventHandler<ActionEvent>()
        {
            public void handle(ActionEvent t)
            {
                viewCollection[5] = ShellacMenu.selectedProperty().get();
                updateTable();
            }
        });
        ShellacMenu.setSelected(true);
        SelectFormatMenu.getItems().addAll(VinylLpMenu,VinylEpMenu,CDMenu,CassetteMenu,DigitalMenu,ShellacMenu);

        Menu SortByGenreMenu = new Menu("Sort Settings");
        VinylLpSortMenu = new CheckMenuItem("Sort Vinyl LP by genre?");
        VinylLpSortMenu.setOnAction(new EventHandler<ActionEvent>()
        {
            public void handle(ActionEvent t)
            {
                collectionContainer.genreSortList[0] = VinylLpSortMenu.selectedProperty().get();
                cc.sort();
                updateTable();
            }
        });
        VinylLpSortMenu.setSelected(true);
        VinylEpSortMenu = new CheckMenuItem("Sort Vinyl EP by genre?");
        VinylEpSortMenu.setOnAction(new EventHandler<ActionEvent>()
        {
            public void handle(ActionEvent t)
            {
                collectionContainer.genreSortList[1] = VinylEpSortMenu.selectedProperty().get();
                cc.sort();
                updateTable();
            }
        });
        VinylEpSortMenu.setSelected(true);
        CDSortMenu = new CheckMenuItem("Sort CD by genre?");
        CDSortMenu.setOnAction(new EventHandler<ActionEvent>()
        {
            public void handle(ActionEvent t)
            {
                collectionContainer.genreSortList[2] = CDSortMenu.selectedProperty().get();
                cc.sort();
                updateTable();
            }
        });
        CDSortMenu.setSelected(true);
        CassetteSortMenu = new CheckMenuItem("Sort Cassette Tape by genre?");
        CassetteSortMenu.setOnAction(new EventHandler<ActionEvent>()
        {
            public void handle(ActionEvent t)
            {
                collectionContainer.genreSortList[3] = CassetteSortMenu.selectedProperty().get();
                cc.sort();
                updateTable();
            }
        });
        CassetteSortMenu.setSelected(true);
        DigitalSortMenu = new CheckMenuItem("Sort Digital File by genre?");
        DigitalSortMenu.setOnAction(new EventHandler<ActionEvent>()
        {
            public void handle(ActionEvent t)
            {
                collectionContainer.genreSortList[4] = DigitalSortMenu.selectedProperty().get();
                cc.sort();
                updateTable();
            }
        });
        DigitalSortMenu.setSelected(true);
        ShellacSortMenu = new CheckMenuItem("Sort Shellac by genre?");
        ShellacSortMenu.setOnAction(new EventHandler<ActionEvent>()
        {
            public void handle(ActionEvent t)
            {
                collectionContainer.genreSortList[5] = ShellacSortMenu.selectedProperty().get();
                cc.sort();
                updateTable();
            }
        });
        ShellacSortMenu.setSelected(true);
        
        SortByGenreMenu.getItems().addAll(VinylLpSortMenu,VinylEpSortMenu, CDSortMenu, CassetteSortMenu, DigitalSortMenu, ShellacSortMenu);

        root.setTop(menubar);
        menubar.getMenus().addAll(FileMenu, EditMenu, SelectFormatMenu, SortByGenreMenu);
        menubar.setPrefHeight(26);

        // table
        Table = new TableView<recordReleaseStringRep>();
        Table.setEditable(false);
        TableColumn<recordReleaseStringRep, String> TitleColumn = new TableColumn<recordReleaseStringRep, String>("Title");
            TitleColumn.setCellValueFactory(
                new PropertyValueFactory<recordReleaseStringRep, String>("title")
            );
        TableColumn<recordReleaseStringRep, String> ArtistColumn = new TableColumn<recordReleaseStringRep, String>("Artist");
            ArtistColumn.setCellValueFactory(
                new PropertyValueFactory<recordReleaseStringRep, String>("artists")
            );
        TableColumn<recordReleaseStringRep, String> GenreColumn = new TableColumn<recordReleaseStringRep, String>("Genre");
            GenreColumn.setCellValueFactory(
                new PropertyValueFactory<recordReleaseStringRep, String>("genres")
            );
        TableColumn<recordReleaseStringRep, String> DateColumn = new TableColumn<recordReleaseStringRep, String>("Release Date");
            DateColumn.setCellValueFactory(
                new PropertyValueFactory<recordReleaseStringRep, String>("releaseDate")
            );
        TableColumn<recordReleaseStringRep, String> FormatColumn = new TableColumn<recordReleaseStringRep,String>("Format");
            FormatColumn.setCellValueFactory(
                new PropertyValueFactory<recordReleaseStringRep, String>("format")
            );

        TitleColumn.setPrefWidth(.2 * 720);
        ArtistColumn.setPrefWidth(.2 * 720);
        GenreColumn.setPrefWidth(.2 * 720);
        DateColumn.setPrefWidth(.2 * 720);
        FormatColumn.setPrefWidth(.2 * 720);

        Table.getColumns().addAll(TitleColumn, ArtistColumn, GenreColumn, DateColumn, FormatColumn);

        Button addItemButton = new Button("Add Item");
        addItemButton.setPrefWidth(720);
        addItemButton.setPrefHeight(26);
        addItemButton.setOnAction(new EventHandler<ActionEvent>()
        {
            public void handle(ActionEvent t)
            {
                AddItemGUI(primaryStage);
            }
        });

        // addItemButton and menubar are both 26 pixels tall
        // 480 - 2(26) = 428 pixels tall for the table
        // difference of 52 pixels

        Table.setPrefHeight(428);

        GridPane gp = new GridPane();
        gp.addRow(0,addItemButton);
        gp.addRow(1,Table);
        gp.setPrefHeight(454);

        root.setCenter(gp);

        primaryStage.widthProperty().addListener(new ChangeListener<Number>() 
        {
            @Override
            public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) 
            {
                if (newValue.doubleValue()!= oldValue.doubleValue())
                {
                    gp.setPrefWidth(newValue.doubleValue());
                    Table.setPrefWidth(newValue.doubleValue());
                    addItemButton.setPrefWidth(newValue.doubleValue());
                    TitleColumn.setPrefWidth(newValue.doubleValue() * .2);
                    ArtistColumn.setPrefWidth(newValue.doubleValue() * .2);
                    GenreColumn.setPrefWidth(newValue.doubleValue() * .2);
                    DateColumn.setPrefWidth(newValue.doubleValue() * .2);
                    FormatColumn.setPrefWidth(newValue.doubleValue() * .2);
                }
            }
        });

        primaryStage.heightProperty().addListener(new ChangeListener<Number>()
        {
            @Override 
            public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue)
            {
                if (newValue.doubleValue() != oldValue.doubleValue())
                {
                    gp.setPrefHeight(newValue.doubleValue());
                    Table.setPrefHeight(newValue.doubleValue() - 52);
                }
            }
        });

        primaryStage.setScene(scene);
        primaryStage.setTitle("Record Collection Organizer");
        primaryStage.setResizable(true);
        primaryStage.show();
    }

    public static void main(String[] args)
    {
        launch(args);
    }

    // updates the table that shows all the items in the collection
    private static void updateTable()
    {
        // Updates the table in the main gui, will need to be migrated to its own function
        Table.getItems().clear();
        // add vinyl lp
        if (viewCollection[0])
        {
            for (int i = 0; i < cc.getVinylLP().size(); ++i)
            {
                Table.getItems().add(cc.getVinylLP().get(i).createStringRep());
            }
        }
        // add vinyl ep
        if (viewCollection[1])
        {
            for (int i = 0; i < cc.getVinylEP().size(); ++i)
            {
                Table.getItems().add(cc.getVinylEP().get(i).createStringRep());
            }
        }
        // add cd
        if (viewCollection[2])
        {
            for (int i = 0; i < cc.getCD().size(); ++i)
            {
                Table.getItems().add(cc.getCD().get(i).createStringRep());
            }
        }
        // add cassette tapes 
        if (viewCollection[3])
        {
            for (int i = 0; i < cc.getCassette().size(); ++i)
            {
                Table.getItems().add(cc.getCassette().get(i).createStringRep());
            }
        }
        // add digital files 
        if (viewCollection[4])
        {
            for (int i = 0; i < cc.getDigital().size(); ++i)
            {
                Table.getItems().add(cc.getDigital().get(i).createStringRep());
            }
        }
        // add shellac records
        if (viewCollection[5])
        {
            for (int i = 0; i < cc.getShellac().size(); ++i)
            {
                Table.getItems().add(cc.getShellac().get(i).createStringRep());
            }
        }
    }
}