package sample;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.TextAlignment;
import javafx.util.converter.FloatStringConverter;
import javafx.util.converter.IntegerStringConverter;

import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;


public class Controller implements Initializable {
    public ArrayList<Process> P_arr;
    @FXML private TableView<Process> MainTable;
    @FXML private TableColumn<Process,String> idCol;
    @FXML private TableColumn<Process,Float> arrivalCol;
    @FXML private TableColumn<Process,Float> burstCol;
    @FXML private TableColumn<Process,Integer> priorityCol;


    @FXML private TextField P_ID;
    @FXML private TextField P_ARRIVE;
    @FXML private TextField P_BURST;
    @FXML private TextField P_P;

    @FXML private TextField RR_Q;
    @FXML private VBox GANTT;

    public HBox rects,times,times2;

    @FXML
    public void EXIT()
    {
        Main.window.close();
    }

    public ObservableList<Process> getProcesses()
    {
        ObservableList<Process> Processes= FXCollections.observableArrayList();

//        Processes.add(new Process("p1",0,3));
//        Processes.add(new Process("p2",2,4));
//        Processes.add(new Process("p3",6,2));
//        Processes.add(new Process("p4",4,6));
        return Processes;
    }
    @Override
    public void initialize(URL location, ResourceBundle resources)
    {
        P_arr=new ArrayList<Process>();


        idCol.setMinWidth(100);
        arrivalCol.setMinWidth(100);
        burstCol.setMinWidth(100);
        priorityCol.setMinWidth(100);

        idCol.setCellValueFactory(new PropertyValueFactory<>("id"));
        arrivalCol.setCellValueFactory(new PropertyValueFactory<>("arrivalTime"));
        burstCol.setCellValueFactory(new PropertyValueFactory<>("burstTime"));
        priorityCol.setCellValueFactory(new PropertyValueFactory<> ("priority"));
        MainTable.setItems(getProcesses());

        //allow the table to be editable
        MainTable.setEditable(true);
        priorityCol.setCellFactory(TextFieldTableCell.<Process,Integer>forTableColumn(new IntegerStringConverter()));
        idCol.setCellFactory(TextFieldTableCell.forTableColumn());
        burstCol.setCellFactory(TextFieldTableCell.<Process,Float>forTableColumn(new FloatStringConverter()));
        arrivalCol.setCellFactory(TextFieldTableCell.<Process,Float>forTableColumn(new FloatStringConverter()));
    }
    @FXML
    public void ADD_PROCESS()
    {
        Process X=new Process();
        X.setId(P_ID.getText());
        X.setArrivalTime(Float.parseFloat(P_ARRIVE.getText()));
        X.setBurstTime(Float.parseFloat(P_BURST.getText()));
        if(P_P.getText().trim().equals(""))
        {
            X.setPriority(-1);
        }
        else
             X.setPriority(Integer.parseInt(P_P.getText()));
        //clearing the fields
        P_ID.clear();
        P_ARRIVE.clear();
        P_BURST.clear();
        P_P.clear();
        //add it to the processes array
        P_arr.add(X);
        //add it to the table
        MainTable.getItems().add(X);
    }

    public void DELETE_PROCESS()
    {
        ObservableList<Process> selected,all;
        all=MainTable.getItems();
        selected=MainTable.getSelectionModel().getSelectedItems();
        String selected_id=selected.get(0).getId();
        //removing it from the array
        for(int i=0;i<P_arr.size();i++)
        {
            if(P_arr.get(i).getId()==selected_id)
            {
                P_arr.remove(i);
                break;
            }
        }

        selected.forEach(all::remove);
    }
    //updating the cells values
    public void changeCell_P(TableColumn.CellEditEvent editedcell)
    {
        Process selected= MainTable.getSelectionModel().getSelectedItem();
        selected.setPriority(Integer.parseInt(editedcell.getNewValue().toString()));
    }

    public void changeCell_ID(TableColumn.CellEditEvent editedcell)
    {
        Process selected= MainTable.getSelectionModel().getSelectedItem();
        selected.setId(editedcell.getNewValue().toString());
    }

    public void changeCell_ARRIVE(TableColumn.CellEditEvent editedcell)
    {
        Process selected= MainTable.getSelectionModel().getSelectedItem();
        selected.setArrivalTime(Float.parseFloat(editedcell.getNewValue().toString()));
    }

    public void changeCell_BURST(TableColumn.CellEditEvent editedcell)
    {
        Process selected= MainTable.getSelectionModel().getSelectedItem();
        selected.setBurstTime(Float.parseFloat(editedcell.getNewValue().toString()));
    }

    public void CALC_FIFO()
    {
        GANTT.getChildren().clear();
        HBox title=new HBox();
        Label titleText=new Label("GANTT CHART");
        titleText.setPrefSize(GANTT.getWidth(),20);
        titleText.setAlignment(Pos.CENTER);
        title.getChildren().add(titleText);
        GANTT.getChildren().add(title);
        ArrayList<Process>seq=new ArrayList<Process>();
        scheduler.reset(P_arr);
        scheduler.sort(P_arr);
        seq=scheduler.calc_FCFS(P_arr);

        ganttDraw(seq);
    }
    public void CALC_FSJ_NP()
    {
        GANTT.getChildren().clear();
        HBox title=new HBox();
        Label titleText=new Label("GANTT CHART");
        titleText.setPrefSize(GANTT.getWidth(),20);
        titleText.setAlignment(Pos.CENTER);
        title.getChildren().add(titleText);
        GANTT.getChildren().add(title);
        ArrayList<Process>seq=new ArrayList<Process>();
        scheduler.reset(P_arr);
        scheduler.sort(P_arr);
        seq=scheduler.calc_FSJ_PR(P_arr,1);

        ganttDraw(seq);
    }
    public void CALC_FSJ_P()
    {
        GANTT.getChildren().clear();
        HBox title=new HBox();
        Label titleText=new Label("GANTT CHART");
        titleText.setPrefSize(GANTT.getWidth(),20);
        titleText.setAlignment(Pos.CENTER);
        title.getChildren().add(titleText);
        GANTT.getChildren().add(title);
        ArrayList<Process>seq=new ArrayList<Process>();
        scheduler.reset(P_arr);
        scheduler.sort(P_arr);
        seq=scheduler.calc_FSJ_PR_P(P_arr,1);

        ganttDraw(seq);
    }
    public void CALC_P_NP()
    {
        GANTT.getChildren().clear();
        HBox title=new HBox();
        Label titleText=new Label("GANTT CHART");
        titleText.setPrefSize(GANTT.getWidth(),20);
        titleText.setAlignment(Pos.CENTER);
        title.getChildren().add(titleText);
        GANTT.getChildren().add(title);
        ArrayList<Process>seq=new ArrayList<Process>();
        scheduler.reset(P_arr);
        scheduler.sort(P_arr);
        seq=scheduler.calc_FSJ_PR(P_arr,2);

        ganttDraw(seq);
    }
    public void CALC_P_P()
    {
        GANTT.getChildren().clear();
        HBox title=new HBox();
        Label titleText=new Label("GANTT CHART");
        titleText.setPrefSize(GANTT.getWidth(),20);
        titleText.setAlignment(Pos.CENTER);
        title.getChildren().add(titleText);
        GANTT.getChildren().add(title);
        ArrayList<Process>seq=new ArrayList<Process>();
        scheduler.reset(P_arr);
        scheduler.sort(P_arr);
        seq=scheduler.calc_FSJ_PR_P(P_arr,2);

        ganttDraw(seq);
    }
    public void CALC_RR()
    {
        GANTT.getChildren().clear();
        HBox title=new HBox();
        Label titleText=new Label("GANTT CHART");
        titleText.setPrefSize(GANTT.getWidth(),20);
        titleText.setAlignment(Pos.CENTER);
        title.getChildren().add(titleText);
        GANTT.getChildren().add(title);
        ArrayList<Process>seq=new ArrayList<Process>();
        scheduler.reset(P_arr);
        scheduler.sort(P_arr);
        seq=scheduler.calc_RR(P_arr,Float.parseFloat(RR_Q.getText()));

        ganttDraw(seq);
    }
    public void ganttDraw(ArrayList<Process>seq)
    {
        rects=new HBox();
        times=new HBox();
        times2=new HBox();
        float totalTime=0;
        for(int i=0;i<seq.size();i++)
        {
            totalTime+=(seq.get(i).getDepartureTime()-seq.get(i).getArrivalTime());
        }
        float TAT=0;
        float WT=0;
        for(int i=0;i<P_arr.size();i++)
        {
            if(P_arr.get(i).getId()=="Gap")
                continue;
            TAT+=(P_arr.get(i).getDepartureTime()-P_arr.get(i).getArrivalTime());
            WT+=(P_arr.get(i).getDepartureTime()-P_arr.get(i).getArrivalTime()-P_arr.get(i).getBurstTime());
        }
        TAT=TAT/P_arr.size();
        WT=WT/P_arr.size();
        for(int i=0;i<seq.size();i++)
        {
            float Burst_T=seq.get(i).getDepartureTime()-seq.get(i).getArrivalTime();
            Label l=new Label();
            l.setPrefSize((GANTT.getWidth()-50)*Burst_T/totalTime,50);
            l.setStyle("-fx-border-color: black");
            l.setText(seq.get(i).getId());
            l.setTextAlignment(TextAlignment.CENTER);
            rects.getChildren().add(l);
        }
        for(int i=0;i<seq.size();i++)
        {
            float Burst_T=seq.get(i).getDepartureTime()-seq.get(i).getArrivalTime();
            Label l=new Label();
            l.setPrefSize((GANTT.getWidth()-50)*Burst_T/totalTime,10);
            l.setText(Float.toString(seq.get(i).getArrivalTime()));
            times.getChildren().add(l);
        }
        if(seq.size()>0)
        {
            times.getChildren().add(new Label(Float.toString(seq.get(seq.size()-1).getDepartureTime())));
        }
        GANTT.getChildren().addAll(rects,times);
        times2.getChildren().addAll(new Label("ATAT = "+Float.toString(TAT)+"\t\t"),new Label("AWT = "+Float.toString(WT)));
        GANTT.getChildren().addAll(times2);
    }
}
