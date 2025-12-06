package com.group17.oop_project_group17_bongo_meat.fahim.Farm_Manager;

import com.group17.oop_project_group17_bongo_meat.SceneSwitcher;

import javafx.event.ActionEvent;
import java.io.IOException;
import static com.group17.oop_project_group17_bongo_meat.SceneSwitcher.switchTo;

public class FarmManagerDashBoardController
{
    @javafx.fxml.FXML
    public void initialize() {
    }

    @javafx.fxml.FXML
    public void approveLivestockTransferButton(ActionEvent actionEvent) throws IOException{
        switchTo("/com/group17/oop_project_group17_bongo_meat/fahim/Farm_Manager/ApproveLivestockTransfertoSlaughterhouse.fxml",actionEvent);
    }

    @javafx.fxml.FXML
    public void generateFarmProductionReportButton(ActionEvent actionEvent){
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