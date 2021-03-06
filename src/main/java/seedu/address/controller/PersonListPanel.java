package seedu.address.controller;

import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.ListView;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.SplitPane;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import seedu.address.browser.BrowserManager;
import seedu.address.model.ModelManager;
import seedu.address.model.person.ReadOnlyPerson;
import seedu.address.ui.PersonListViewCell;
import seedu.address.util.AppLogger;
import seedu.address.util.LoggerManager;

import java.util.ArrayList;
import java.util.List;

/**
 * Dialog to view the list of persons and their details
 *
 * setConnections should be set before showing stage
 */
public class PersonListPanel extends BaseUiPart {
    private static AppLogger logger = LoggerManager.getLogger(PersonListPanel.class);
    public static final String FXML = "PersonListPanel.fxml";
    private VBox panel;
    private AnchorPane placeHolderPane;

    @FXML
    private ListView<ReadOnlyPerson> personListView;

    public PersonListPanel() {
        super();
    }

    public static PersonListPanel load(Stage primaryStage, AnchorPane personListPlaceholder,
                                       ModelManager modelManager, BrowserManager browserManager) {
        logger.debug("Loading person list panel.");
        PersonListPanel personListPanel =
                UiPartLoader.loadUiPart(primaryStage, personListPlaceholder, new PersonListPanel());
        personListPanel.configure(modelManager, browserManager,
                                  modelManager.getPersonsAsReadOnlyObservableList());
        return personListPanel;
    }

    private void addToPlaceholder() {
        SplitPane.setResizableWithParent(placeHolderPane, false);
        placeHolderPane.getChildren().add(panel);
    }

    private void configure(ModelManager modelManager, BrowserManager browserManager,
                           ObservableList<ReadOnlyPerson> personList){
        setConnections(browserManager, personList);
        addToPlaceholder();
    }

    @Override
    public void setNode(Node node) {
        panel = (VBox) node;
    }

    @Override
    public String getFxmlPath() {
        return FXML;
    }

    @Override
    public void setPlaceholder(AnchorPane pane) {
        this.placeHolderPane = pane;
    }

    public void setConnections(BrowserManager browserManager,
                               ObservableList<ReadOnlyPerson> personList) {
        personListView.setItems(personList);
        personListView.setCellFactory(listView -> new PersonListViewCell());
        loadGithubProfilePageWhenPersonIsSelected(browserManager);
        setupListviewSelectionModelSettings();
    }

    private void setupListviewSelectionModelSettings() {
        personListView.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
        personListView.getItems().addListener((ListChangeListener<ReadOnlyPerson>) c -> {
            while(c.next()) {
                if (c.wasRemoved()) {
                    ObservableList<Integer> currentIndices = personListView.getSelectionModel().getSelectedIndices();
                    if (currentIndices.size() > 1) {
                        personListView.getSelectionModel().clearAndSelect(currentIndices.get(0));
                    }
                }
            }
        });
    }

    private void loadGithubProfilePageWhenPersonIsSelected(BrowserManager browserManager) {
        personListView.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                logger.debug("Person in list view clicked. Loading GitHub profile page: '{}'", newValue);
                browserManager.loadPersonPage(newValue);
            }
        });
    }


    /**
     * Initializes the controller class. This method is automatically called
     * after the fxml file has been loaded.
     */
    @FXML
    private void initialize() {

    }

    public List<ReadOnlyPerson> getSelectedPersons() {
        return new ArrayList<>(personListView.getSelectionModel().getSelectedItems());
    }

    public List<ReadOnlyPerson> getDisplayedPersons() {
        return this.personListView.getItems();
    }
}
