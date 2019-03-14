package fx;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyEvent;
import javafx.stage.FileChooser;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import java.util.Scanner;


public class Controller implements Initializable {

    private static final String ABOUT_CONTEXT_MESSAGE =
            "You can find the latest source code on Github @baksha97."
                    + "\nBe sure to check out any branches for works in progress " +
                    "& please create an issue if a problem is found. " +
                    "\n-Travis";
    private static final String DEFAULT_PROGRAM_NAME = "lang-l-current-program.txt";

    @FXML
    public TextField inputField;
    public TextArea programArea;
    public Label stateLabel;
    public Label countLabel;
    public TextArea outputArea;
    public TextField stepByField;
    public Label nextInstructionLabel;
    public Label snapshotLabel;
    public TextArea variableHistoryArea;
    public Label prevInstructionLabel;


    private LanguageLEnvironment env;
    private FileChooser fileChooser;

    //Execution Buttons
    public void onSetClick() {
        outputArea.setText("");
        variableHistoryArea.setText("");
        saveEditor();
        if (!setupEnv()) return;
        updateInterface();
    }

    public void onRunClick() {
        saveEditor();
        if (env == null) {
            println("Setup first.");
            return;
        }
        while (env.hasInstructions()) {
            env.executeNext();
            updateInterface();
        }
    }

    public void onStepClick() {
        saveEditor();
        if (env == null) {
            println("Setup first.");
            return;
        }
        try {
            int steps = Integer.valueOf(stepByField.getText().trim());
            for (int i = 0; i < steps && env.hasInstructions(); i++) {
                env.executeNext();
                updateInterface();
            }
        } catch (Exception e) {
            println("Unable to step.");
            println(e.getLocalizedMessage());
            e.printStackTrace();
        }
    }


    private boolean setupEnv() {
        try {
            env = new LanguageLEnvironment(programArea.getText().trim(), inputField.getText().trim());
        } catch (Exception e) {
            println("Invalid input.");
            println(e.getLocalizedMessage());
            return false;
        }

        return true;
    }


    //Configure display
    private void updateInterface() {
        int exCount = env.getExecutionCount();
        int plen = env.getInstructionCount();
        String state = env.getCurrentState();
        String nextInst = env.hasInstructions() ? String.valueOf(env.getNextInstruction().originalLine()) : "Halted";
        String prevInst = env.getPrevInstruction() != null ? String.valueOf(env.getPrevInstruction().originalLine()) : "None";
        String varsTxt = env.variables()
                .toString()
                .replace("=", " = ");
        String snapShotText = (exCount+1 + " ,   " + varsTxt);
        String exeCountLabel = "Execution: #" + exCount+ " to " + state;
        prevInstructionLabel.setText(prevInst);
        stateLabel.setText(state);
        nextInstructionLabel.setText(nextInst);
        snapshotLabel.setText(snapShotText);
        countLabel.setText(exeCountLabel);

        println("Execution: #" + exCount);
        printlnt("State: " + state);
        printlnt("Prev Executed: " + prevInst);
        printlnt("Snapshots: " + snapShotText);
        printlnt("Next Execution: " + nextInst);
        println("");
        if(exCount == 0) variableHistoryArea.appendText(snapShotText);
        else variableHistoryArea.appendText("\n"+ snapShotText);
    }

    //Menu Buttons
    public void onOpenClick() {
        File picked = fileChooser.showOpenDialog(null);
        if (picked == null) return;
        load_file(picked.getAbsolutePath());
    }

    public void onSaveAsClick() {
        File file = fileChooser.showSaveDialog(null);
        if (file == null) return;
        save_file(file.getAbsolutePath());
    }

    public void onAboutClick() {
        Alert alert = new Alert(Alert.AlertType.INFORMATION, ABOUT_CONTEXT_MESSAGE);
        alert.setHeaderText("About Editor");
        alert.show();
    }

    //Keyboard events
    public void keyPressed(KeyEvent e) {
        switch (e.getCode()) {
            case ENTER:
                if (e.isShiftDown()) onSetClick();
                else onStepClick();
                break;
            default:
                break;
        }
    }

    //Output area printing
    private void println(Object o) {
        outputArea.appendText(o + "\n");
    }

    private void printlnt(Object o) {
        println("\t" + o);
    }

    //File editor management
    private void saveEditor() {
        save_file(DEFAULT_PROGRAM_NAME);
    }

    private void save_file(String location) {
        List<String> lines = new ArrayList<>();
        Scanner prg = new Scanner(programArea.getText());
        while (prg.hasNextLine()) {
            String line = prg.nextLine();
            lines.add(line);
        }

        Path file = Paths.get(location);
        try {
            Files.write(file, lines, Charset.forName("UTF-8"));
        } catch (IOException e) {
            println(e.getLocalizedMessage());
        }
    }

    private void load_file(String fileLocation) {
        programArea.setText("");
        try {
            List<String> lines = Files.readAllLines(Paths.get(fileLocation));
            for (String line : lines) {
                programArea.appendText(line + "\n");
            }
        } catch (IOException e) {
            println("No program found.");
            println(e.getLocalizedMessage());
        }
    }

    private void initializeFileChooser() {
        fileChooser = new FileChooser();
        fileChooser.setTitle("File Management");
        fileChooser.setInitialDirectory(Paths.get(".").toFile());
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("TXT files (*.txt)", "*.txt"));
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        initializeFileChooser();
        load_file(DEFAULT_PROGRAM_NAME);
    }
}

