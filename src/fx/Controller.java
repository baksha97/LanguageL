package fx;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.input.KeyEvent;
import javafx.stage.FileChooser;
import language.LanguageLEnvironment;

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
                    "\n\t\t-Travis";
    private static final String DEFAULT_PROGRAM_NAME = "lang-l-current-program.txt";

    @FXML
    public TextField inputField;
    public TextArea programArea;
    public Label currentLabelLabel;
    public Label countLabel;
    public TextArea outputArea;
    public TextField stepByField;
    public Label nextInstructionLabel;
    public Label snapshotLabel;
    public TextArea variableHistoryArea;
    public Label prevInstructionLabel;
    public CheckBox noHistoryCheck;


    private LanguageLEnvironment env;
    private FileChooser fileChooser;

    private boolean needsReset;

    //Execution Buttons
    public void onSetClick() {

        outputArea.setText("");
        variableHistoryArea.setText("");
        saveEditor();
        if (!setupEnv()) return;
        onHistoryOffTick();
        updateInterface();
    }

    public void onRunClick() {
        saveEditor();

        if (env == null || needsReset) {
            println("Setup first.");
            return;
        }
        try {
            while (env.hasInstructions()) {
                env.executeNext();
            }
            updateInterface();
        } catch (Exception e) {
            updateInterface();
            e.printStackTrace();
            println(e.getLocalizedMessage());
            scrollDown();
            needsReset = true;
        }
    }

    public void onStepClick() {
        saveEditor();
        if (env == null || needsReset) {
            println("Setup first.");
            return;
        }

        try {
            int steps = Integer.valueOf(stepByField.getText().trim());
            for (int i = 0; i < steps && env.hasInstructions(); i++) {
                env.executeNext();
            }
            updateInterface();
        } catch (Exception e) {
            updateInterface();
            println("Unable to step.");
            println(e.getLocalizedMessage());
            e.printStackTrace();
            scrollDown();
            needsReset = true;
        }
    }

    public void onHistoryOffTick() {
        if (env == null) return;
        if (noHistoryCheck.isSelected()) {
            env.stopKeepingHistory();
        } else {
            env.startKeepingHistory();
        }
    }


    private boolean setupEnv() {
        try {
            env = new LanguageLEnvironment(programArea.getText().trim(), inputField.getText().trim());
            needsReset = false;
        } catch (Exception e) {
            updateInterface();
            println("Invalid input.");
            e.printStackTrace();
            println(e.getLocalizedMessage());
            needsReset = true;
            return false;
        }

        return true;
    }


    //Configure display
    private void updateInterface() {
        prevInstructionLabel.setText(env.vm.getPreviousInstruction());
        currentLabelLabel.setText(env.vm.getCurrentLabel());
        nextInstructionLabel.setText(env.vm.getNextInstruction());
        snapshotLabel.setText(env.vm.getSnapshot());
        countLabel.setText(env.vm.getExecutionCount());
        if (!noHistoryCheck.isSelected()) {
            variableHistoryArea.setText(env.getVariableHistory());
            outputArea.setText(env.getExeHistory());
        }
        scrollDown();
    }

    private void scrollDown() {
        variableHistoryArea.positionCaret(variableHistoryArea.getLength());
        outputArea.positionCaret(outputArea.getLength());
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
        needsReset = false;
    }
}

