// SharedViewModel.java
// SharedViewModel.java
package com.example.tracker.ThirdFragment;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import java.util.ArrayList;

public class SharedViewModel extends ViewModel {
    private final MutableLiveData<Integer> rowCount = new MutableLiveData<>(0);
    private final MutableLiveData<Integer> columnCount = new MutableLiveData<>(0);
    private final MutableLiveData<ArrayList<ArrayList<String>>> tableData = new MutableLiveData<>(new ArrayList<>());
    private final MutableLiveData<String> databaseName = new MutableLiveData<>();

    public void setRowCount(int count) {
        rowCount.setValue(count);
    }

    public LiveData<Integer> getRowCount() {
        return rowCount;
    }

    public void setColumnCount(int count) {
        columnCount.setValue(count);
    }

    public LiveData<Integer> getColumnCount() {
        return columnCount;
    }

    public String getCellValue(int rowIndex, int columnIndex) {
        ArrayList<ArrayList<String>> data = tableData.getValue();
        if (data != null && rowIndex < data.size() && columnIndex < data.get(rowIndex).size()) {
            return data.get(rowIndex).get(columnIndex);
        }
        return "";
    }

    public void setTableData(ArrayList<ArrayList<String>> data) {
        tableData.setValue(data);
    }

    public LiveData<ArrayList<ArrayList<String>>> getTableData() {
        return tableData;
    }

    public void SetDatabaseName(String databaseName){
        this.databaseName.setValue(databaseName);
    }

    public LiveData<String> getDatabaseName(){
        return databaseName;
    }

}

