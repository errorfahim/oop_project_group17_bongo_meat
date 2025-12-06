package com.group17.oop_project_group17_bongo_meat.fahim.Farm_Manager;

import com.group17.oop_project_group17_bongo_meat.SceneSwitcher;

import com.group17.oop_project_group17_bongo_meat.fahim.Admin.SystemAlert;
import javafx.event.ActionEvent;
import javafx.scene.control.TextArea;

import java.io.File;
import java.io.FileInputStream;
import java.io.ObjectInputStream;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.io.IOException;
import static com.group17.oop_project_group17_bongo_meat.SceneSwitcher.switchTo;

public class FarmManagerDashBoardController
{
    @javafx.fxml.FXML
    private TextArea alertMessage;

    private final String FILE_PATH = "alerts.bin";

    @javafx.fxml.FXML
    public void initialize() {
        loadAlertsToTextArea();
    }


    private void loadAlertsToTextArea() {

        File file = new File(FILE_PATH);

        if (!file.exists()) {
            alertMessage.setText("No system alerts available.");
            return;
        }

        List<SystemAlert> alertList = new ArrayList<>();

        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(file))) {

            alertList = (List<SystemAlert>) ois.readObject();

        } catch (Exception e) {
            e.printStackTrace();
            alertMessage.setText("Error loading system alerts.");
            return;
        }


        StringBuilder builder = new StringBuilder();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");

        for (SystemAlert alert : alertList) {
            builder.append("Date: ")
                    .append(alert.getDate().format(formatter))
                    .append("\nMessage: ")
                    .append(alert.getMessage())
                    .append("\n\n");
        }

        alertMessage.setText(builder.toString());

    }

    @javafx.fxml.FXML
    public void approveLivestockTransferButton(ActionEvent actionEvent) throws IOException{
        switchTo("/com/group17/oop_project_group17_bongo_meat/fahim/Farm_Manager/ApproveLivestockTransfertoSlaughterhouse.fxml",actionEvent);
    }

    @javafx.fxml.FXML
    public void generateFarmProductionReportButton(ActionEvent actionEvent)throws IOException{
        switchTo("/com/group17/oop_project_group17_bongo_meat/fahim/Farm_Manager/GenerateFarmProductionReport.fxml",actionEvent);
    }

    @javafx.fxml.FXML
    public void requestVeterinaryInspectionButton(ActionEvent actionEvent) throws IOException{
        switchTo("/com/group17/oop_project_group17_bongo_meat/fahim/Farm_Manager/RequestVeterinaryInspection.fxml",actionEvent);
    }

    @javafx.fxml.FXML
    public void registerNewLivestockButton(ActionEvent actionEvent) throws IOException{
        switchTo("/com/group17/oop_project_group17_bongo_meat/fahim/Farm_Manager/RegisterNewLivestock.fxml", actionEvent);
    }

    @javafx.fxml.FXML
    public void trackLivestockInventoryButton(ActionEvent actionEvent) throws  IOException{
        switchTo("/com/group17/oop_project_group17_bongo_meat/fahim/Farm_Manager/TrackLivestockInventory.fxml", actionEvent);
    }

    @javafx.fxml.FXML
    public void updateLivestockHealthStatusButton(ActionEvent actionEvent)throws IOException {
        switchTo("/com/group17/oop_project_group17_bongo_meat/fahim/Farm_Manager/UpdateLivestockHealthStatus.fxml", actionEvent);
    }

    @Deprecated
    public void ScheduleAnimalFeddingButton(ActionEvent actionEvent) throws IOException{
        switchTo("/com/group17/oop_project_group17_bongo_meat/fahim/Farm_Manager/ScheduleAnimalFeeding.fxml", actionEvent);
    }


    @javafx.fxml.FXML
    public void scheduleAnimalFeddingButton(ActionEvent actionEvent) throws  IOException{
        switchTo("/com/group17/oop_project_group17_bongo_meat/fahim/Farm_Manager/ScheduleAnimalFeeding.fxml", actionEvent);
    }

    @javafx.fxml.FXML
    public void farmInstrumentInventoryButton (ActionEvent actionEvent) throws IOException{
        switchTo("/com/group17/oop_project_group17_bongo_meat/fahim/Farm_Manager/FarmInstrumentInventory.fxml", actionEvent);
    }
}